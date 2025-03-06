package ru.skypro.homework.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.exception.UnsuccessfulImageSavingException;
import ru.skypro.homework.repository.ImageRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class ImageServiceTest {

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private ImageService imageService;

    @TempDir
    Path tempDir;

    @Test
    void saveImage_ShouldSaveNewImage() throws Exception {

        ReflectionTestUtils.setField(imageService, "imageDir", tempDir.toString() + "/");

        when(multipartFile.getOriginalFilename()).thenReturn("test.jpg");
        when(multipartFile.getContentType()).thenReturn("image/jpeg");
        when(multipartFile.getBytes()).thenReturn(new byte[100]);
        when(multipartFile.getSize()).thenReturn(100L);
        when(imageRepository.findById(anyInt())).thenReturn(Optional.empty());
        when(imageRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Image result = imageService.saveImage(multipartFile, 1);

        assertNotNull(result);
        assertTrue(result.getPath().matches("^\\d+\\.jpg$"));
        assertEquals(100L, result.getSize());
        assertEquals("image/jpeg", result.getMediaType());
        assertTrue(Files.exists(tempDir.resolve(result.getPath())));
    }

    @Test
    void saveImage_ShouldUpdateExistingImage() throws Exception {

        ReflectionTestUtils.setField(imageService, "imageDir", tempDir.toString() + "/");

        Image existingImage = new Image()
                .setId(1)
                .setPath("existing.jpg")
                .setData(new byte[50]);
        Files.write(tempDir.resolve("existing.jpg"), new byte[50]);

        when(imageRepository.findById(1)).thenReturn(Optional.of(existingImage));
        when(multipartFile.getBytes()).thenReturn(new byte[200]);
        when(multipartFile.getSize()).thenReturn(200L);
        when(multipartFile.getContentType()).thenReturn("image/png");
        when(imageRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Image result = imageService.saveImage(multipartFile, 1);

        assertEquals("existing.jpg", result.getPath());
        assertEquals(200L, result.getSize());
        assertEquals("image/png", result.getMediaType());
        assertEquals(200, Files.size(tempDir.resolve("existing.jpg")));
    }

    @Test
    void saveImage_ShouldThrowExceptionWhenIOError() throws Exception {

        when(multipartFile.getOriginalFilename()).thenReturn("test.jpg");
        when(multipartFile.getContentType()).thenReturn("image/jpeg");

        when(multipartFile.getBytes()).thenThrow(new IOException());

        assertThrows(UnsuccessfulImageSavingException.class,
                () -> imageService.saveImage(multipartFile, 1));
    }

    @Test
    void removeImage_ShouldDeleteImageAndFile() throws Exception {

        ReflectionTestUtils.setField(imageService, "imageDir", tempDir.toString() + "/");

        Image image = new Image()
                .setId(1)
                .setPath("image.jpg")
                .setData(new byte[100]);
        Files.write(tempDir.resolve("image.jpg"), new byte[100]);

        when(imageRepository.findById(1)).thenReturn(Optional.of(image));
        doNothing().when(imageRepository).delete(image);

        imageService.removeImage(1);

        assertFalse(Files.exists(tempDir.resolve("image.jpg")));
        verify(imageRepository).delete(image);
    }

    @Test
    void removeImage_ShouldHandleMissingImage() {

        when(imageRepository.findById(1)).thenReturn(Optional.empty());
        assertDoesNotThrow(() -> imageService.removeImage(1));
        verify(imageRepository, never()).delete(any());
    }

    @Test
    void buildFileName_ShouldGenerateCorrectFilename() {

        when(multipartFile.getOriginalFilename()).thenReturn("photo.png");
        String fileName = imageService.buildFileName(multipartFile);
        assertTrue(fileName.matches("^\\d+\\.png$"));
    }

    @Test
    void saveToDir_ShouldHandleIOError() throws Exception {

        ReflectionTestUtils.setField(imageService, "imageDir", tempDir.toString() + "/");

        Image image = new Image()
                .setPath("test.jpg")
                .setData(new byte[100]);

        // Создаем файл с атрибутом "только для чтения"
        Path filePath = tempDir.resolve("test.jpg");
        Files.createFile(filePath);
        filePath.toFile().setReadOnly();

        assertThrows(UnsuccessfulImageSavingException.class,
                () -> imageService.saveToDir(image));

        filePath.toFile().setWritable(true);
    }
}