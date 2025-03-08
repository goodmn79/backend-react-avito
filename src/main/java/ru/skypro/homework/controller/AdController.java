package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.ad.Ad;
import ru.skypro.homework.dto.ad.Ads;
import ru.skypro.homework.dto.ad.CreateOrUpdateAd;
import ru.skypro.homework.dto.ad.ExtendedAd;
import ru.skypro.homework.service.impl.AdServiceImpl;

@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/ads")
@RequiredArgsConstructor
@Tag(name = "Объявления")
public class AdController {
    private final AdServiceImpl adServiceImpl;

    @Operation(summary = "Получение всех объявлений")
    @GetMapping
    public Ads getAllAds() {
        log.info("Invoke method 'getAllAds'");
        return adServiceImpl.getAllAds();
    }

    @Operation(summary = "Добавление объявления")
    @PostMapping(consumes = "multipart/form-data", produces = "application/json")
    @PreAuthorize("isAuthenticated()")
    public Ad addAd(@RequestPart("properties") CreateOrUpdateAd createOrUpdateAd,
                    @RequestPart("image") MultipartFile image) {
        log.info("Invoke method 'addAd'");
        return adServiceImpl.addAd(createOrUpdateAd, image);
    }

    @Operation(summary = "Получение информации об объявлении")
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ExtendedAd getExtendedAd(@PathVariable("id") int id) {
        log.info("Invoke method 'getExtendedAd'");
        return adServiceImpl.getAdById(id);
    }

    @Operation(summary = "Удаление объявления")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or @adServiceImpl.isAdAuthor(#id, authentication.principal.username)")
    public void removeAd(@PathVariable("id") int id) {
        log.info("Invoke method 'removeAd'");
        adServiceImpl.removeAdById(id);
    }

    @Operation(summary = "Обновление информации об объявлении")
    @PatchMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public Ad updateAds(@PathVariable("id") int id,
                        @RequestBody CreateOrUpdateAd createOrUpdateAd) {
        log.info("Invoke method 'updateAds'");
        return adServiceImpl.updateAdById(id, createOrUpdateAd);
    }

    @Operation(summary = "Получение объявлений авторизованного пользователя")
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public Ads getAdsMe() {
        log.info("Invoke method 'getAdsMe'");
        return adServiceImpl.getAds();
    }

    @Operation(summary = "Обновление картинки объявления",
            responses = {@ApiResponse(description = "OK",
                    content = @Content(mediaType = "application/octet-stream"))})
    @PatchMapping("/{id}/image")
    @PreAuthorize("isAuthenticated()")
    public byte[] updateImage(@PathVariable("id") int id,
                              @RequestParam("image") MultipartFile image) {
        log.info("Invoke method 'updateImage'");
        return adServiceImpl.updateImage(id, image);
    }
}
