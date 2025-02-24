package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.ad.Ad;
import ru.skypro.homework.dto.ad.Ads;
import ru.skypro.homework.dto.ad.CreateOrUpdateAd;
import ru.skypro.homework.dto.ad.ExtendedAd;

import java.io.IOException;

@RestController
@RequestMapping("/ads")
@Tag(name = "Объявления")
public class AdController {

    @Operation(summary = "Получение всех объявлений")
    @GetMapping
    public Ads getAllAds() {
        return new Ads();
    }

    @Operation(summary = "Добавление объявления")
    @PostMapping
    public Ad addAd(@RequestPart("image") MultipartFile image,
                    @RequestPart("properties") CreateOrUpdateAd createOrUpdateAd) {
        return new Ad();
    }

    @Operation(summary = "Получение информации об объявлении")
    @GetMapping("/{id}")
    public ExtendedAd getAds(@PathVariable("id") long id) {
        return new ExtendedAd();
    }

    @Operation(summary = "Удаление объявления")
    @DeleteMapping("/{id}")
    public void removeAd(@PathVariable("id") long id) {

    }

    @Operation(summary = "Обновление информации об объявлении")
    @PatchMapping("/{id}")
    public Ad updateAds(@PathVariable("id") long id,
                        @RequestBody CreateOrUpdateAd createOrUpdateAd) {
        return new Ad();
    }

    @Operation(summary = "Получение объявлений авторизованного пользователя")
    @GetMapping("/me")
    public Ads getAdsMe() {
        return new Ads();
    }

    @Operation(summary = "Обновление картинки объявления",
            responses = {@ApiResponse(description = "OK",
                    content = @Content(mediaType = "application/octet-stream"))})
    @PatchMapping("/{id}/image")
    public byte[] updateImage(@PathVariable("id") long id,
                              @RequestParam("image") MultipartFile image) throws IOException {
        return image.getBytes();
    }
}
