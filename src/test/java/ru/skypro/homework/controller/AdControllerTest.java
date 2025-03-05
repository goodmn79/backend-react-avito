package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.skypro.homework.component.mapper.AdMapper;
import ru.skypro.homework.config.WebSecurityConfig;
import ru.skypro.homework.dto.ad.Ad;
import ru.skypro.homework.dto.ad.Ads;
import ru.skypro.homework.dto.ad.CreateOrUpdateAd;
import ru.skypro.homework.dto.ad.ExtendedAd;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.service.AdService;


import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


//@ActiveProfiles("test")
@WebMvcTest(AdController.class)
@Import(WebSecurityConfig.class)
class AdControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AdService adService;


    private Ads expectedAds;
    private Ad expectedAd;
    private ExtendedAd expectedExtendedAd;
    private AdEntity adEntity;
    private CreateOrUpdateAd createOrUpdateAd;
    private List<AdEntity> adEntities;
    private MockMultipartFile testImageFile;




    @BeforeEach
    void setUp() {

        UserEntity testUser = new UserEntity()
                .setId(1)
                .setFirstName("Ivan")
                .setLastName("Ivanov")
                .setPhone("+7(999)999-99-99")
                .setEmail("user@gmail.com");

        Image testImagePath = new Image()
                .setId(1)
                .setPath("test-image.jpg");

        testImageFile = new MockMultipartFile(
                "image", "test.jpg", "image/jpeg", "Test image content".getBytes());

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

        expectedAd = new Ad ()
                .setPk(adEntity.getPk())
                .setTitle(adEntity.getTitle())
                .setPrice(adEntity.getPrice())
                .setAuthor(adEntity.getAuthor().getId())
                .setImage(adEntity.getImage().getPath());

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
    @WithMockUser(username = "testUser")
    void getAllAds() throws Exception {

        when(adService.getAllAds()).thenReturn(expectedAds);

        mockMvc.perform(get("/ads"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(expectedAds.getCount()))
                .andExpect(jsonPath("$.result[0].pk").value(expectedAds.getResult().get(0).getPk()))
                .andExpect(jsonPath("$.result[0].title").value(expectedAds.getResult().get(0).getTitle()))
                .andExpect(jsonPath("$.result[0].price").value(expectedAds.getResult().get(0).getPrice()))
                .andExpect(jsonPath("$.result[0].author").value(expectedAds.getResult().get(0).getAuthor()))
                .andExpect(jsonPath("$.result[0].image").value(expectedAds.getResult().get(0).getImage()));

        verify(adService).getAllAds();
    }

    @Test
    @WithMockUser(username = "testUser")
    void addAd() throws Exception {

        String jsonString = "{\"price\": 123,\"title\": \"Test Ad\", \"description\": \"Test description\"}";

        when(adService.addAd(anyString(), any(MockMultipartFile.class))).thenReturn(expectedAd);

        mockMvc.perform(multipart("/ads")
                        .file(testImageFile)
                        .param("properties", jsonString))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.pk").value(expectedAd.getPk()))
                .andExpect(jsonPath("$.title").value(expectedAd.getTitle()))
                .andExpect(jsonPath("$.price").value(expectedAd.getPrice()))
                .andExpect(jsonPath("$.author").value(expectedAd.getAuthor()))
                .andExpect(jsonPath("$.image").value(expectedAd.getImage()));

        verify(adService).addAd(anyString(), any(MockMultipartFile.class));
    }

    @Test
    @WithMockUser(username = "testUser")
    void getAds() throws Exception {
        when(adService.getAdById(any(Integer.class))).thenReturn(expectedExtendedAd);

        mockMvc.perform(get("/ads/{id}", expectedExtendedAd.getPk()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pk").value(expectedExtendedAd.getPk()))
                .andExpect(jsonPath("$.image").value(expectedExtendedAd.getImage()))
                .andExpect(jsonPath("$.title").value(expectedExtendedAd.getTitle()))
                .andExpect(jsonPath("$.price").value(expectedExtendedAd.getPrice()))
                .andExpect(jsonPath("$.author").value(expectedExtendedAd.getAuthor()))
                .andExpect(jsonPath("$.description").value(expectedExtendedAd.getDescription()))
                .andExpect(jsonPath("$.authorFirstName").value(expectedExtendedAd.getAuthorFirstName()))
                .andExpect(jsonPath("$.authorLastName").value(expectedExtendedAd.getAuthorLastName()))
                .andExpect(jsonPath("$.email").value(expectedExtendedAd.getEmail()))
                .andExpect(jsonPath("$.phone").value(expectedExtendedAd.getPhone()));

        verify(adService).getAdById(expectedExtendedAd.getPk());
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void removeAdAsAdmin() throws Exception {
        // метод isAdAuthor не вызывается
       when(adService.isAdAuthor(anyInt())).thenReturn(false);

        mockMvc.perform(delete("/ads/{id}", this.adEntity.getPk()))
                .andExpect(status().isNoContent());

        verify(adService).removeAdById(adEntity.getPk());
    }

    @Test
    @WithMockUser(username = "testUser")
    void removeAdAsAuthor() throws Exception {

        mockMvc.perform(delete("/ads/{id}", this.adEntity.getPk()))
                .andExpect(status().isNoContent());

        verify(adService).removeAdById(adEntity.getPk());
    }

    /*
    @Test
    @WithMockUser(username = "otherUser")
    void removeAdAsNonAuthor() throws Exception {
        // метод не вызывается
        when(adService.isAdAuthor(anyInt())).thenReturn(false);

        doNothing().when(adService).removeAdById(anyInt());

        mockMvc.perform(delete("/ads/1"))
                .andExpect(status().isForbidden());

        verify(adService, never()).removeAdById(this.adEntity.getPk());
    }

     */

    @Test
    @WithMockUser(username = "testUser")
    void updateAds() throws Exception{

    when(adService.updateAdById(expectedAd.getPk(), createOrUpdateAd)).thenReturn(expectedAd);

    mockMvc.perform(patch("/ads/{id}", expectedAd.getPk())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createOrUpdateAd)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.pk").value(expectedAd.getPk()))
            .andExpect(jsonPath("$.title").value(expectedAd.getTitle()))
            .andExpect(jsonPath("$.price").value(expectedAd.getPrice()));

        verify(adService).updateAdById(expectedAd.getPk(), createOrUpdateAd);
    }

    @Test
    @WithMockUser(username = "testUser")
    void updateAdsAsAdmin() throws Exception{
    }

    @Test
    @WithMockUser(username = "testUser")
    void updateAdsAsNonAuthor() throws Exception{
    }

    @Test
    @WithMockUser(username = "testUser")
    void getAdsMe() throws Exception {

    when(adService.getAds()).thenReturn(expectedAds);

    mockMvc.perform(get("/ads/me"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.count").value(expectedAds.getCount()))
            .andExpect(jsonPath("$.result").isArray())
            .andExpect(jsonPath("$.result[0].pk").value(expectedAds.getResult().get(0).getPk()));

        verify(adService).getAds();
    }


    @Test
    @WithMockUser(username = "testUser")
    void updateImage() throws Exception {
        int id = 1;

        when(adService.updateImage(id, testImageFile)).thenReturn("Updated image content".getBytes());

        mockMvc.perform(multipart("/ads/{id}/image", id)
                        .file(testImageFile)
                        .with(request -> {
                            request.setMethod("PATCH");
                            return request;
                        })
                )
                .andExpect(status().isOk())
                .andExpect(content().bytes("Updated image content".getBytes()));

        verify(adService).updateImage(id, testImageFile);
    }


    @Test
    @WithMockUser(username = "testUser")
    void updateImageAsAdmin() throws Exception {

    }
    @Test
    @WithMockUser(username = "testUser")
    void updateImageAsNonAuthor() throws Exception {

    }

}