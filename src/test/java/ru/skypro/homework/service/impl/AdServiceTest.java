package ru.skypro.homework.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.component.mapper.AdMapper;
import ru.skypro.homework.dto.ad.Ad;
import ru.skypro.homework.dto.ad.Ads;
import ru.skypro.homework.dto.ad.CreateOrUpdateAd;
import ru.skypro.homework.dto.ad.ExtendedAd;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.exception.AdNotFoundException;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.service.UserService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
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

    @InjectMocks
    private AdServiceImpl adService;

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
        UserEntity testUser = new UserEntity()
                .setId(1)
                .setFirstName("Ivan")
                .setLastName("Ivanov")
                .setPhone("+7(999)999-99-99")
                .setEmail("user@gmail.com")
                .setUsername("testUser")
                .setPassword("password");

        Image testImagePath = new Image()
                .setId(1)
                .setPath("test-image.jpg");

        image = mock(MultipartFile.class);

        currentUser = new UserEntity()
                .setId(1)
                .setUsername("testUser");

        adEntities = List.of(
                new AdEntity().setPk(1).setTitle("Test Ad 1").setPrice(100).setAuthor(testUser).setImage(testImagePath),
                new AdEntity().setPk(2).setTitle("Test Ad 2").setPrice(200).setAuthor(testUser).setImage(testImagePath));

        expectedAds = new Ads()
                .setCount(adEntities.size())
                .setResults(adEntities.stream()
                        .map(a -> new Ad()
                                .setPk(a.getPk())
                                .setTitle(a.getTitle())
                                .setPrice(a.getPrice())
                                .setAuthor(a.getAuthor().getId())
                                .setImage(a.getImage().getPath()))
                        .toList());

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
    void getAllAdsTest() {
        when(adMapper.map(List.of(adEntity))).thenReturn(expectedAds);
        when(adRepository.findAll()).thenReturn(List.of(adEntity));

        Ads actualAds = adService.getAllAds();

        assertThat(actualAds).isEqualTo(expectedAds);
        verify(adRepository).findAll();
        verify(adMapper).map(List.of(adEntity));
    }

    @Test
    void addAdTest() {
        when(adMapper.map(createOrUpdateAd)).thenReturn(adEntity);
        when(imageService.saveImage(any())).thenReturn(adEntity.getImage());
        when(adRepository.save(any())).thenReturn(adEntity);
        when(adMapper.map(adEntity)).thenReturn(expectedExtendedAd);

        Ad actual = adService.addAd(createOrUpdateAd, image);

        assertThat(actual).isEqualTo(expectedExtendedAd);
        verify(adRepository).save(any());
        verify(adMapper).map(createOrUpdateAd);
        verify(adMapper).map(adEntity);
    }

    @Test
    void getAdByIdTest() {
        when(adRepository.findById(adId)).thenReturn(Optional.of(adEntity));
        when(adMapper.map(adEntity)).thenReturn(expectedExtendedAd);

        ExtendedAd actualExtendedAd = adService.getAdById(adId);

        assertThat(actualExtendedAd).isEqualTo(expectedExtendedAd);
        verify(adRepository).findById(adId);
        verify(adMapper).map(adEntity);
    }

    @Test
    void removeAdByIdTest() {
        adEntity.setAuthor(currentUser);
        when(adRepository.findById(adId)).thenReturn(Optional.of(adEntity));
        lenient().when(userService.getCurrentUser()).thenReturn(currentUser);

        adService.removeAdById(adId);

        verify(adRepository, times(1)).deleteById(adId);
    }

    @Test
    void updateAdByIdTest() {
        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(adRepository.findById(adId)).thenReturn(Optional.of(adEntity));
        when(adRepository.save(any())).thenReturn(adEntity);
        when(adMapper.map(adEntity)).thenReturn(expectedExtendedAd);

        Ad actualAd = adService.updateAdById(adId, createOrUpdateAd);

        assertThat(actualAd).isEqualTo(expectedExtendedAd);
        verify(adRepository, times(2)).findById(adId);
        verify(adRepository).save(any());
        verify(adMapper).map(adEntity);
    }

    @Test
    void updateImageTest() {
        byte[] updatedImageData = "Updated image content".getBytes();
        Image updatedImage =
                new Image()
                        .setData(updatedImageData);
        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(adRepository.findById(adId)).thenReturn(Optional.of(adEntity));
        when(imageService.updateImage(image, adId)).thenReturn(updatedImage);
        when(adRepository.save(any())).thenReturn(adEntity);

        byte[] actualImage = adService.updateImage(adId, image);

        assertThat(actualImage).isEqualTo(updatedImageData);
        verify(adRepository, times(3)).findById(eq(adId));
        verify(adRepository).save(any());
        verify(imageService).updateImage(eq(image), eq(adId));
    }

    @Test
    void getAdsTest() {
        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(adRepository.findByAuthor(currentUser)).thenReturn(adEntities);
        when(adMapper.map(adEntities)).thenReturn(expectedAds);

        Ads actualAds = adService.getAds();

        assertThat(actualAds).isEqualTo(expectedAds);
        verify(adRepository).findByAuthor(eq(currentUser));
        verify(adMapper).map(eq(adEntities));
    }

    @Test
    void getAdEntityTest() {
        when(adRepository.findById(adId)).thenReturn(Optional.of(adEntity));

        AdEntity actualEntity = adService.getAdEntity(adId);

        assertThat(actualEntity).isEqualTo(adEntity);
        verify(adRepository).findById(adId);
    }

    @Test
    void isAdAuthorTest_positive() {
        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(adRepository.findById(adId)).thenReturn(Optional.of(adEntity));

        boolean isAuthor = adService.isAdAuthor(adId);

        assertThat(isAuthor).isTrue();
    }

    @Test
    void testGetAdEntityThrowsException() {
        doThrow(new AdNotFoundException()).when(adRepository).findById(adId);

        assertThatThrownBy(() -> adService.getAdEntity(adId))
                .isInstanceOf(AdNotFoundException.class);

        verify(adRepository).findById(adId);
    }
}
