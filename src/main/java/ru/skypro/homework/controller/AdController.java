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

/**
 * Контроллер для управления объявлениями.
 * <p>
 * Обрабатывает HTTP-запросы для создания, получения, обновления и удаления объявлений.
 * </p>
 *
 * @author Powered by ©AYE.team
 * @version 0.0.1-SNAPSHOT
 */
@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/ads")
@RequiredArgsConstructor
@Tag(name = "Объявления")
public class AdController {
    private final AdServiceImpl adServiceImpl;

    /**
     * Получение всех объявлений.
     * <br> Endpoint: GET /ads
     *
     * @return объект {@link Ads} с информацией о количестве объявлений и их списком
     */
    @Operation(summary = "Получение всех объявлений")
    @GetMapping
    public Ads getAllAds() {
        log.info("Invoke method 'getAllAds'");
        return adServiceImpl.getAllAds();
    }

    /**
     * Добавление объявления.
     * <br> Доступ к методу имеет только аутентифицированный пользователь.
     * <br> Endpoint: POST /ads
     *
     * @param jsonString строка с информацией объявления
     * @param image файл с изображением объявления
     * @return объект {@link Ad} созданное объявление
     * @throws IOException Если произошла ошибка при обработке изображения
     */
    @Operation(summary = "Добавление объявления")
    @PostMapping(consumes = "multipart/form-data", produces = "application/json")
    @PreAuthorize("isAuthenticated()")
    public Ad addAd(@RequestPart("properties") CreateOrUpdateAd createOrUpdateAd,
                    @RequestPart("image") MultipartFile image) {
        log.info("Invoke method 'addAd'");
        return adServiceImpl.addAd(createOrUpdateAd, image);
    }

    /**
     * Получение информации об объявлении.
     * <br> Доступ к методу имеет только аутентифицированный пользователь.
     * <br> Endpoint: GET /ads/{id}
     *
     * @param id идентификатор объявления
     * @return объект {@link ExtendedAd} полная информация об объявлении
     */
    @Operation(summary = "Получение информации об объявлении")
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ExtendedAd getExtendedAd(@PathVariable("id") int id) {
        log.info("Invoke method 'getExtendedAd'");
        return adServiceImpl.getAdById(id);
    }

    /**
     * Удаление объявления.
     * <br> Доступ к методу имеет пользователь с ролью ADMIN, либо автор объявления.
     * <br> Endpoint: DELETE /ads/{id}
     *
     * @param id идентификатор объявления
     */
    @Operation(summary = "Удаление объявления")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or @adServiceImpl.isAdAuthor(#id, authentication.principal.username)")
    public void removeAd(@PathVariable("id") int id) {
        log.info("Invoke method 'removeAd'");
        adServiceImpl.removeAdById(id);
    }

    /**
     * Обновление информации об объявлении.
     * <br> Доступ к методу имеет только аутентифицированный пользователь.
     * <br> Endpoint: PATCH /ads/{id}
     *
     * @param id идентификатор объявления
     * @param createOrUpdateAd информация для обновления объявления
     * @return объект {@link Ad} обновленное объявление
     */
    @Operation(summary = "Обновление информации об объявлении")
    @PatchMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public Ad updateAds(@PathVariable("id") int id,
                        @RequestBody CreateOrUpdateAd createOrUpdateAd) {
        log.info("Invoke method 'updateAds'");
        return adServiceImpl.updateAdById(id, createOrUpdateAd);
    }

    /**
     * Получение объявлений авторизованного пользователя.
     * <br> Доступ к методу имеет только аутентифицированный пользователь.
     * <br> Endpoint: GET /ads/me
     *
     * @return объект {@link Ads} с информацией о количестве объявлений и их списком
     */
    @Operation(summary = "Получение объявлений авторизованного пользователя")
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public Ads getAdsMe() {
        log.info("Invoke method 'getAdsMe'");
        return adServiceImpl.getAds();
    }

    /**
     * Обновление картинки объявления.
     * <br> Доступ к методу имеет только аутентифицированный пользователь.
     * <br> Endpoint: PATCH /ads/{id}/image
     *
     * @param id идентификатор объявления
     * @param image файл с новой картинкой объявления
     * @return объект {@link byte[]} массив байтов, содержащий данные обновлённого изображения
     * @throws IOException Если произошла ошибка при обработке изображения
     */
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
