package ru.skypro.homework.controller;

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

    @GetMapping
    public Ads getAllAds() {
        return new Ads();
    }

    @PostMapping
    public Ad addAd(@RequestPart("image") MultipartFile image,
                    @RequestPart("properties") CreateOrUpdateAd createOrUpdateAd) {
        return new Ad();
    }

    @GetMapping("/{id}")
    public ExtendedAd getAds(@PathVariable("id") long id) {
        return new ExtendedAd();
    }

    @DeleteMapping("/{id}")
    public void removeAd(@PathVariable("id") long id) {

    }

    @PatchMapping("/{id}")
    public Ad updateAds(@PathVariable("id") long id,
                        @RequestBody CreateOrUpdateAd createOrUpdateAd) {
        return new Ad();
    }

    @GetMapping("/me")
    public Ads getAdsMe() {
        return new Ads();
    }

    @PatchMapping("/{id}/image")
    public byte[] updateImage(@PathVariable("id") long id,
                              @RequestParam("image") MultipartFile image) throws IOException {
        return image.getBytes();
    }


}
