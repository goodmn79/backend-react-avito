package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.ad.Ad;
import ru.skypro.homework.dto.ad.Ads;
import ru.skypro.homework.dto.ad.CreateOrUpdateAd;
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
    @PostMapping
    public Ad addAd(@RequestPart("image") MultipartFile image,
                    @RequestPart("properties") CreateOrUpdateAd createOrUpdateAd) throws IOException {
        return adService.addAd(image, createOrUpdateAd);
    }

    @Operation(summary = "Получение информации об объявлении")
    @GetMapping("/{id}")
    public Ad getAds(@PathVariable("id") int id) {
        return adService.getAdById(id);
    }

    @Operation(summary = "Удаление объявления")
    @DeleteMapping("/{id}")
    public void removeAd(@PathVariable("id") int id) {
        adService.removeAdById(id);
    }

    @Operation(summary = "Обновление информации об объявлении")
    @PatchMapping("/{id}")
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
    public byte[] updateImage(@PathVariable("id") int id,
                              @RequestParam("image") MultipartFile image) throws IOException {
        return adService.updateImage(id, image);
    }
}
