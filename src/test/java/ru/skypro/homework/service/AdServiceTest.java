package ru.skypro.homework.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.component.mapper.AdMapper;

import ru.skypro.homework.component.validation.Validatable;
import ru.skypro.homework.dto.ad.Ad;
import ru.skypro.homework.dto.ad.Ads;
import ru.skypro.homework.dto.ad.CreateOrUpdateAd;
import ru.skypro.homework.dto.ad.ExtendedAd;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.exception.AdNotFoundException;
import ru.skypro.homework.repository.AdRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdServiceTest {
    @Mock
    private AdRepository adRepository;
    @Mock
    private AdMapper adMapper;

    @Mock
    private ImageService imageService;
    @Mock
    private UserService userService;
    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private Validatable validator;

    @InjectMocks
    private AdService adService;


    private AdEntity adEntity;
    private Ads expectedAds;
    private ExtendedAd expectedExtendedAd;
    private CreateOrUpdateAd createOrUpdateAd;
    private MultipartFile image;
    private UserEntity currentUser;
    private List<AdEntity> adEntities;
    private final int adId = 1;

    @BeforeEach
    void setUp() {
        UserEntity testUser = new UserEntity();
        testUser.setId(1);
        testUser.setFirstName("Ivan");
        testUser.setLastName("Ivanov");
        testUser.setPhone("+7(999)999-99-99");
        testUser.setEmail("user@gmail.com");
        testUser.setUsername("testUser");
        testUser.setPassword("password");

        Image testImagePath = new Image()
                .setId(1)
                .setPath("test-image.jpg");

        currentUser = new UserEntity()
                .setId(1)
                .setUsername("testUser");

        adEntities = List.of(
                new AdEntity().setPk(1).setTitle("Test Ad 1").setPrice(100).setAuthor(testUser).setImage(testImagePath),
                new AdEntity().setPk(2).setTitle("Test Ad 2").setPrice(200).setAuthor(testUser).setImage(testImagePath));

        expectedAds = new Ads()
                .setCount(adEntities.size())
                .setResult(adEntities.stream()
                        .map(a -> new Ad()
                                .setPk(a.getPk())
                                .setTitle(a.getTitle())
                                .setPrice(a.getPrice())
                                .setAuthor(a.getAuthor().getId())
                                .setImage(a.getImage().getPath()))
                        .collect(Collectors.toUnmodifiableList()));

        adEntity = new AdEntity()
                .setPk(1)
                .setPrice(150)
                .setTitle("Test Ad")
                .setDescription("Test description")
                .setAuthor(testUser)
                .setImage(testImagePath);

        expectedExtendedAd = new ExtendedAd()
                .setPk(adEntity.getPk())
                .setImage(adEntity.getImage().getPath())
                .setPrice(adEntity.getPrice())
                .setTitle(adEntity.getTitle())
                .setAuthor(adEntity.getAuthor().getId())
                .setDescription(adEntity.getDescription())
                .setAuthorFirstName(adEntity.getAuthor().getFirstName())
                .setAuthorLastName(adEntity.getAuthor().getLastName())
                .setEmail(adEntity.getAuthor().getEmail())
                .setPhone(adEntity.getAuthor().getPhone());

        createOrUpdateAd = new CreateOrUpdateAd()
                .setTitle("Updated Title")
                .setDescription("Updated Description")
                .setPrice(150);
    }

    @Test
    void getAllAds() {
        when(adMapper.map(List.of(adEntity))).thenReturn(expectedAds);
        when(adRepository.findAll()).thenReturn(List.of(adEntity));

        Ads actualAds = adService.getAllAds();

        assertThat(actualAds).isEqualTo(expectedAds);

        verify(adRepository).findAll();
        verify(adMapper).map(List.of(adEntity));
    }

    @Test
    void addAd() throws IOException {
        when(objectMapper.readValue(anyString(), eq(CreateOrUpdateAd.class))).thenReturn(createOrUpdateAd);
        when(adMapper.map(createOrUpdateAd)).thenReturn(adEntity);
        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(imageService.saveImage(any(), anyInt())).thenReturn(adEntity.getImage());
        when(adRepository.save(any())).thenReturn(adEntity);
        when(adMapper.map(adEntity)).thenReturn(expectedExtendedAd);

        Ad actualAd = adService.addAd("jsonString", image);

        assertThat(actualAd).isEqualTo(expectedExtendedAd);

        verify(adRepository).save(any());
        verify(adMapper).map(createOrUpdateAd);
        verify(adMapper).map(adEntity);
    }

    @Test
    void getAdById() {
        when(adRepository.findById(adId)).thenReturn(Optional.of(adEntity));
        when(adMapper.map(adEntity)).thenReturn(expectedExtendedAd);

        ExtendedAd actualExtendedAd = adService.getAdById(adId);

        assertThat(actualExtendedAd).isEqualTo(expectedExtendedAd);

        verify(adRepository).findById(adId);
        verify(adMapper).map(adEntity);
    }

    @Test
    void removeAdById() {
        adEntity.setAuthor(currentUser);

        when(adRepository.findById(adId)).thenReturn(Optional.of(adEntity));
        lenient().when(userService.getCurrentUser()).thenReturn(currentUser);

        adService.removeAdById(adId);

        verify(adRepository, times(1)).deleteById(adId);
    }

    @Test
    void updateAdById() {
        when(adRepository.findById(adId)).thenReturn(Optional.of(adEntity));
        when(validator.validatedData(anyString(), anyInt(), anyInt())).thenAnswer(invocation -> invocation.getArgument(0));
        when(validator.validatedData(anyInt())).thenAnswer(invocation -> invocation.getArgument(0));
        when(adRepository.save(any())).thenReturn(adEntity);
        when(adMapper.map(adEntity)).thenReturn(expectedExtendedAd);

        Ad actualAd = adService.updateAdById(adId, createOrUpdateAd);

        assertThat(actualAd).isEqualTo(expectedExtendedAd);

        verify(adRepository).findById(adId);
        verify(adRepository).save(any());
        verify(adMapper).map(adEntity);
        verify(validator).validatedData(anyInt());
        verify(validator, times(2)).validatedData(anyString(), anyInt(), anyInt());
    }

    @Test
    void updateImage() throws IOException {
        byte[] updatedImageData = "Updated image content".getBytes();
        Image updatedImage = new Image();
        updatedImage.setData(updatedImageData);

        when(adRepository.findById(adId)).thenReturn(Optional.of(adEntity));
        when(imageService.saveImage(image, adEntity.getPk())).thenReturn(updatedImage);
        when(adRepository.save(any())).thenReturn(adEntity);

        byte[] actualImage = adService.updateImage(adId, image);

        assertThat(actualImage).isEqualTo(updatedImageData);

        verify(adRepository).findById(adId);
        verify(adRepository).save(any());
        verify(imageService).saveImage(image, adEntity.getPk());
    }

    @Test
    void getAds() {
        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(adRepository.findByAuthor(currentUser)).thenReturn(adEntities);
        when(adMapper.map(adEntities)).thenReturn(expectedAds);

        Ads actualAds = adService.getAds();

        assertThat(actualAds).isEqualTo(expectedAds);

        verify(adRepository).findByAuthor(currentUser);
        verify(adMapper).map(adEntities);
    }

    @Test
    void getAdEntity() {
        when(adRepository.findById(adId)).thenReturn(Optional.of(adEntity));

        AdEntity actualEntity = adService.getAdEntity(adId);

        assertThat(actualEntity).isEqualTo(adEntity);

        verify(adRepository).findById(adId);
    }

    @Test
    void isAdAuthor() {
        when(adRepository.findById(adId)).thenReturn(Optional.of(adEntity));
        when(userService.getCurrentUser()).thenReturn(currentUser);

        boolean isAuthor = adService.isAdAuthor(adId);

        assertTrue(isAuthor);
    }

    @Test
    void testGetAdEntityThrowsException() {
        doThrow(new AdNotFoundException()).when(adRepository).findById(adId);

        assertThatThrownBy(() -> adService.getAdEntity(adId))
                .isInstanceOf(AdNotFoundException.class);

        verify(adRepository).findById(adId);
    }

    //негативные сценарии, когда "не автор"
/*
    @Test
    void testRemoveAdByIdThrowsExceptionWhenNotAuthor() {
        when(userService.getCurrentUser()).thenReturn(currentUser);

        when(adRepository.findById(adId)).thenReturn(Optional.of(adEntity));
        when(adService.isAdAuthor(adId)).thenReturn(false);

        assertThrows(AccessDeniedException.class, () -> adService.removeAdById(adId));

        verify(adRepository).findById(adId);
    }

    @Test
    void testUpdateAdByIdThrowsExceptionWhenNotAuthor() {
        when(adRepository.findById(adId)).thenReturn(Optional.of(adEntity));
        when(adService.isAdAuthor(adId)).thenReturn(false);

        assertThrows(AccessDeniedException.class, () -> adService.updateAdById(adId, createOrUpdateAd));

        verify(adRepository).findById(adId);

    }

    @Test
    void testUpdateImageThrowsExceptionWhenNotAuthor() {
        when(adRepository.findById(adId)).thenReturn(Optional.of(adEntity));
        when(adService.isAdAuthor(adId)).thenReturn(false);

        assertThrows(AccessDeniedException.class, () -> adService.updateImage(adId, image));

        verify(adRepository).findById(adId);
    }

 */

}
