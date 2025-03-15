package ru.skypro.homework.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.exception.ErrorImageProcessingException;
import ru.skypro.homework.exception.WrongFileFormatException;
import ru.skypro.homework.repository.ImageRepository;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImageServiceImplTest {

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private MultipartFile file;

    @InjectMocks
    private ImageServiceImpl imageService;

    private Image testImage;
    private String originalFileName;

    @BeforeEach
    void setUp() {
        originalFileName = "original.jpg";
        byte[] imageData = "image data".getBytes();
        testImage = new Image()
                .setPath("/images/original.jpg")
                .setSize(imageData.length)
                .setMediaType("image/jpg")
                .setData(imageData);
    }

    @Test
    void testSaveImage_ShouldReturnSavedImage() throws Exception {
        when(file.getOriginalFilename()).thenReturn(originalFileName);
        when(file.getBytes()).thenReturn(testImage.getData());
        when(file.getSize()).thenReturn((long) testImage.getData().length);
        when(file.getContentType()).thenReturn(testImage.getMediaType());
        when(imageRepository.save(any(Image.class))).thenReturn(testImage);

        Image actual = imageService.saveImage(file);

        assertThat(actual).isNotNull();
        assertThat(actual.getPath()).isEqualTo(testImage.getPath());
        assertThat(actual.getSize()).isEqualTo(testImage.getData().length);
        assertThat(actual.getMediaType()).isEqualTo(testImage.getMediaType());
    }

    @Test
    void testSaveImage_whenErrorImageProcessing_shouldThrowException() throws Exception {
        when(file.getOriginalFilename()).thenReturn(originalFileName);
        when(file.getBytes()).thenThrow(new IOException("File corrupted"));

        assertThatThrownBy(() -> imageService.saveImage(file))
                .isInstanceOf(ErrorImageProcessingException.class);
    }

    @Test
    void testSaveImage_whenWrongFileFormat_shouldThrowException() {
        assertThatThrownBy(() -> imageService.saveImage(file))
                .isInstanceOf(WrongFileFormatException.class);
    }

    @Test
    void testUpdateImage_ShouldReturnUpdatedImage() throws Exception {
        int imageId = testImage.getId();
        Image expected = new Image()
                .setId(imageId)
                .setPath(testImage.getPath())
                .setSize(testImage.getSize())
                .setMediaType(testImage.getMediaType())
                .setData(testImage.getData());
        byte[] updateImageData = "updated image data".getBytes();
        when(file.getBytes()).thenReturn(updateImageData);
        when(file.getSize()).thenReturn((long) updateImageData.length);
        when(file.getContentType()).thenReturn("image/png");
        when(imageRepository.findById(imageId)).thenReturn(Optional.of(testImage));
        when(imageRepository.save(any(Image.class))).thenReturn(testImage);

        Image actual = imageService.updateImage(file, imageId);

        assertThat(actual).isNotNull();
        assertThat(actual.getPath()).isEqualTo(testImage.getPath());
        assertThat(actual.getPath()).isEqualTo(expected.getPath());
        assertThat(actual.getSize()).isEqualTo(testImage.getSize());
        assertThat(actual.getSize()).isNotEqualTo(expected.getSize());
        assertThat(actual.getMediaType()).isEqualTo(testImage.getMediaType());
        assertThat(actual.getMediaType()).isNotEqualTo(expected.getMediaType());
        assertThat(actual.getData()).isEqualTo(updateImageData);
        assertThat(actual.getData()).isNotEqualTo(expected.getData());
    }

    @Test
    void testUpdateImage_whenErrorImageProcessing_shouldThrowException() throws Exception {
        int imageId = 1;
        Image existingImage = new Image()
                .setId(imageId)
                .setPath("/images/old.png");
        when(imageRepository.findById(imageId)).thenReturn(Optional.of(existingImage));
        when(file.getBytes()).thenThrow(new IOException("File corrupted"));

        assertThatThrownBy(() -> imageService.updateImage(file, imageId))
                .isInstanceOf(ErrorImageProcessingException.class);
    }

    @Test
    void testRemoveImage_whenImageExist_shouldRemoveThisImage() {
        int imageId = 1;
        Image image =
                new Image()
                        .setId(imageId)
                        .setPath("/images/old.png");
        when(imageRepository.findById(imageId)).thenReturn(Optional.of(image));

        imageService.removeImage(imageId);

        verify(imageRepository).delete(image);
        verify(imageRepository).findById(imageId);
    }

    @Test
    void testRemoveImage_whenImageNotFound_shouldThrowException() {
        int imageId = 1;
        when(imageRepository.findById(imageId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> imageService.removeImage(imageId))
                .isInstanceOf(ErrorImageProcessingException.class);

        verify(imageRepository).findById(imageId);
    }
}
