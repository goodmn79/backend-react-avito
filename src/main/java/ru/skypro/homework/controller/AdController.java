package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.ad.Ad;
import ru.skypro.homework.dto.ad.Ads;
import ru.skypro.homework.dto.ad.CreateOrUpdateAd;
import ru.skypro.homework.dto.ad.ExtendedAd;
import ru.skypro.homework.service.AdService;

import java.io.IOException;

@RestController
@RequestMapping("/ads")
@RequiredArgsConstructor
@Tag(name = "Объявления")
public class AdController {
    private final AdService adService;

    @Operation(summary = "Получение всех объявлений")
    @GetMapping
    public Ads getAllAds() {
        return adService.getAllAds();
    }

    @Operation(summary = "Добавление объявления")
    @PostMapping(consumes = "multipart/form-data", produces = "application/json")
    @PreAuthorize("isAuthenticated()")
    public Ad addAd(@RequestParam("properties") String jsonString,
                    @RequestPart("image") MultipartFile image) throws IOException {
        return adService.addAd(jsonString, image);
    }

    @Operation(summary = "Получение информации об объявлении")
    @GetMapping("/{id}")
    public ExtendedAd getAds(@PathVariable("id") int id) {
        return adService.getAdById(id);
    }

    @Operation(summary = "Удаление объявления")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or @adService.isAdAuthor(#id, authentication.principal.username)")
    public void removeAd(@PathVariable("id") int id) {
        adService.removeAdById(id);
    }

    @Operation(summary = "Обновление информации об объявлении")
    @PatchMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public Ad updateAds(@PathVariable("id") int id,
                        @RequestBody CreateOrUpdateAd createOrUpdateAd) {
        return adService.updateAdById(id, createOrUpdateAd);
    }

    @Operation(summary = "Получение объявлений авторизованного пользователя")
    @GetMapping("/me")
    public Ads getAdsMe() {
        return adService.getAds();
    }

    @Operation(summary = "Обновление картинки объявления",
            responses = {@ApiResponse(description = "OK",
                    content = @Content(mediaType = "application/octet-stream"))})
    @PatchMapping("/{id}/image")
    @PreAuthorize("isAuthenticated()")
    public byte[] updateImage(@PathVariable("id") int id,
                              @RequestParam("image") MultipartFile image) throws IOException {
        return adService.updateImage(id, image);
    }
}
