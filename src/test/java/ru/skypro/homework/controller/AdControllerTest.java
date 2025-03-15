package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.skypro.homework.dto.ad.Ad;
import ru.skypro.homework.dto.ad.Ads;
import ru.skypro.homework.dto.ad.CreateOrUpdateAd;
import ru.skypro.homework.dto.ad.ExtendedAd;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.service.impl.AdServiceImpl;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class AdControllerTest {
    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private AdServiceImpl adService;

    @Mock
    private AdRepository adRepository;

    @Autowired
    private AdController adController;

    private Ads testAds;
    private Ad testdAd;
    private ExtendedAd testExtendedAd;
    private AdEntity adEntity;
    private CreateOrUpdateAd createdAd;
    private MockMultipartFile testImageFile;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(adController, "adService", adService);
        ReflectionTestUtils.setField(adService, "adRepository", adRepository);
        mockMvc =
                MockMvcBuilders
                        .webAppContextSetup(context)
                        .apply(springSecurity())
                        .build();

        objectMapper = new ObjectMapper();

        UserEntity testUser = new UserEntity()
                .setId(1)
                .setFirstName("Ivan")
                .setLastName("Ivanov")
                .setPhone("+7(999)999-99-99")
                .setEmail("user@gmail.com");

        Image testImagePath = new Image()
                .setId(1)
                .setPath("test-image.jpg");

        testImageFile =
                new MockMultipartFile(
                        "image",
                        "test.jpg",
                        "image/jpeg",
                        "Test image content".getBytes()
                );

        adEntity = new AdEntity()
                .setPk(1)
                .setPrice(150)
                .setTitle("Test Ad")
                .setDescription("Test description")
                .setAuthor(testUser)
                .setImage(testImagePath);

        List<AdEntity> adEntities = List.of(adEntity);

        testdAd = new Ad()
                .setPk(adEntity.getPk())
                .setTitle(adEntity.getTitle())
                .setPrice(adEntity.getPrice())
                .setAuthor(adEntity.getAuthor().getId())
                .setImage(adEntity.getImage().getPath());

        testAds = new Ads()
                .setCount(adEntities.size())
                .setResults(List.of(testdAd));

        testExtendedAd = new ExtendedAd()
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

        createdAd = new CreateOrUpdateAd()
                .setTitle("title")
                .setDescription("description")
                .setPrice(100);
    }

    @Test
    @WithMockUser(username = "testUser")
    void getAllAds() throws Exception {
        when(adService.getAllAds()).thenReturn(testAds);

        mockMvc.perform(get("/ads"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(testAds.getCount()))
                .andExpect(jsonPath("$.results[0].pk").value(testAds.getResults().get(0).getPk()))
                .andExpect(jsonPath("$.results[0].title").value(testAds.getResults().get(0).getTitle()))
                .andExpect(jsonPath("$.results[0].price").value(testAds.getResults().get(0).getPrice()))
                .andExpect(jsonPath("$.results[0].author").value(testAds.getResults().get(0).getAuthor()))
                .andExpect(jsonPath("$.results[0].image").value(testAds.getResults().get(0).getImage()));

        verify(adService).getAllAds();
    }

    @Test
    @WithMockUser(username = "testUser")
    void addAd() throws Exception {
        String adJson = objectMapper.writeValueAsString(createdAd);
        when(adService.addAd(any(), any(MockMultipartFile.class))).thenReturn(testdAd);

        mockMvc.perform(multipart("/ads")
                        .file(new MockMultipartFile("properties", "", "application/json", adJson.getBytes()))
                        .file(testImageFile))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.pk").value(testdAd.getPk()))
                .andExpect(jsonPath("$.title").value(testdAd.getTitle()))
                .andExpect(jsonPath("$.price").value(testdAd.getPrice()))
                .andExpect(jsonPath("$.author").value(testdAd.getAuthor()))
                .andExpect(jsonPath("$.image").value(testdAd.getImage()));

        verify(adService).addAd(eq(createdAd), eq(testImageFile));
    }

    @Test
    @WithMockUser(username = "testUser")
    void getAds() throws Exception {
        when(adService.getAdById(anyInt())).thenReturn(testExtendedAd);

        mockMvc.perform(get("/ads/{id}", testExtendedAd.getPk()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pk").value(testExtendedAd.getPk()))
                .andExpect(jsonPath("$.image").value(testExtendedAd.getImage()))
                .andExpect(jsonPath("$.title").value(testExtendedAd.getTitle()))
                .andExpect(jsonPath("$.price").value(testExtendedAd.getPrice()))
                .andExpect(jsonPath("$.author").value(testExtendedAd.getAuthor()))
                .andExpect(jsonPath("$.description").value(testExtendedAd.getDescription()))
                .andExpect(jsonPath("$.authorFirstName").value(testExtendedAd.getAuthorFirstName()))
                .andExpect(jsonPath("$.authorLastName").value(testExtendedAd.getAuthorLastName()))
                .andExpect(jsonPath("$.email").value(testExtendedAd.getEmail()))
                .andExpect(jsonPath("$.phone").value(testExtendedAd.getPhone()));

        verify(adService).getAdById(eq(testExtendedAd.getPk()));
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void removeAd_asAdmin() throws Exception {
        mockMvc.perform(delete("/ads/{id}", adEntity.getPk()))
                .andExpect(status().isNoContent());

        verify(adService).removeAdById(eq(adEntity.getPk()));
    }

    @Test
    @WithMockUser(username = "user")
    void removeAd_asAuthor() throws Exception {
        mockMvc.perform(delete("/ads/{id}", this.adEntity.getPk()))
                .andExpect(status().isNoContent());

        verify(adService).removeAdById(adEntity.getPk());
    }

    @Test
    @WithAnonymousUser
    public void removeAd_unauthorized() throws Exception {

        mockMvc.perform(delete("/ads/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "testUser")
    void updateAds() throws Exception {
        when(adService.updateAdById(testdAd.getPk(), createdAd)).thenReturn(testdAd);

        mockMvc.perform(patch("/ads/{id}", testdAd.getPk())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createdAd)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pk").value(testdAd.getPk()))
                .andExpect(jsonPath("$.title").value(testdAd.getTitle()))
                .andExpect(jsonPath("$.price").value(testdAd.getPrice()));

        verify(adService).updateAdById(eq(testdAd.getPk()), eq(createdAd));
    }

    @Test
    @WithMockUser(username = "user")
    void getAdsMe() throws Exception {
        when(adService.getAds()).thenReturn(testAds);

        mockMvc.perform(get("/ads/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(testAds.getCount()))
                .andExpect(jsonPath("$.results").isArray())
                .andExpect(jsonPath("$.results[0].pk").value(testAds.getResults().get(0).getPk()));

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

        verify(adService).updateImage(eq(id), eq(testImageFile));
    }
}