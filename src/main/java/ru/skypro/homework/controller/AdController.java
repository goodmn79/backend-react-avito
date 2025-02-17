package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.ad.Ad;
import ru.skypro.homework.dto.ad.Ads;
import ru.skypro.homework.dto.ad.CreateOrUpdateAd;
import ru.skypro.homework.dto.ad.ExtendedAd;

import java.io.IOException;

@RestController
@RequestMapping("/ads")
public class AdController {

    @Operation(
            tags = {"Объявления"},
            operationId = "getAllAds",
            summary = "Получение всех объявлений",
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(ref = "#/components/schemas/Ads")
                            )
                    )
            }
    )
    @GetMapping
    public Ads getAllAds() {
        return new Ads();
    }

    @Operation(
            tags = {"Объявления"},
            summary = "Добавление объявления",
            operationId = "addAd",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "multipart/form-data"
                    )
            ),
            responses = {
                    @ApiResponse(description = "Created", responseCode = "201",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(ref = "#/components/schemas/Ad")
                            )
                    ),
                    @ApiResponse(description = "Unauthorized", responseCode = "401")
            }
    )
    @PostMapping
    public Ad addAd(@RequestPart("image") MultipartFile image,
                    @RequestPart("properties") CreateOrUpdateAd createOrUpdateAd) {
        return new Ad();
    }

    @Operation(
            tags = {"Объявления"},
            summary = "Получение информации об объявлении",
            operationId = "getAds",
            parameters = {
                    @Parameter(
                            name = "id",
                            in = ParameterIn.PATH,
                            required = true,
                            schema = @Schema(type = "integer", format = "int32")
                    )
            },
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(ref = "#/components/schemas/ExtendedAd")
                            )
                    ),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Not found", responseCode = "404")
            }
    )
    @GetMapping("/{id}")
    public ExtendedAd getAds(@PathVariable("id") long id) {
        return new ExtendedAd();
    }

    @Operation(
            tags = {"Объявления"},
            summary = "Удаление объявления",
            operationId = "removeAd",
            parameters = {
                    @Parameter(
                            name = "id",
                            in = ParameterIn.PATH,
                            required = true,
                            schema = @Schema(type = "integer", format = "int32")
                    )
            },
            responses = {
                    @ApiResponse(description = "No Content", responseCode = "204"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Forbidden", responseCode = "403"),
                    @ApiResponse(description = "Not found", responseCode = "404")
            }
    )
    @DeleteMapping("/{id}")
    public void removeAd(@PathVariable("id") long id) {

    }

    @Operation(
            tags = {"Объявления"},
            summary = "Обновление информации об объявлении",
            operationId = "updateAds",
            parameters = {
                    @Parameter(
                            name = "id",
                            in = ParameterIn.PATH,
                            required = true,
                            schema = @Schema(type = "integer", format = "int32")
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(ref = "#/components/schemas/CreateOrUpdateAd")
                    )
            ),
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(ref = "#/components/schemas/Ad")
                            )
                    ),
                    @ApiResponse(description = "Forbidden", responseCode = "403"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Not found", responseCode = "404")
            }
    )
    @PatchMapping("/{id}")
    public Ad updateAds(@PathVariable("id") long id,
                        @RequestBody CreateOrUpdateAd createOrUpdateAd) {
        return new Ad();
    }

    @Operation(
            tags = {"Объявления"},
            summary = "Получение объявлений авторизованного пользователя",
            operationId = "getAdsMe",
            responses = {
                    @ApiResponse(
                            description = "OK",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(ref = "#/components/schemas/Ads")
                            )
                    ),
                    @ApiResponse(description = "Unauthorized", responseCode = "401")
            }
    )
    @GetMapping("/me")
    public Ads getAdsMe() {
        return new Ads();
    }

    @Operation(
            tags = {"Объявления"},
            summary = "Обновление картинки объявления",
            operationId = "updateImage",
            parameters = {
                    @Parameter(
                            name = "id",
                            in = ParameterIn.PATH,
                            required = true,
                            schema = @Schema(type = "integer", format = "int32")
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "multipart/form-data"
                    )
            ),
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200",
                            content = @Content(
                                    mediaType = "application/octet-stream"
                            )
                    ),
                    @ApiResponse(description = "Forbidden", responseCode = "403"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Not found", responseCode = "404")
            }
    )
    @PatchMapping("/{id}/image")
    public byte[] updateImage(@PathVariable("id") long id,
                              @RequestParam("image") MultipartFile image) throws IOException {
        return image.getBytes();
    }


}
