package com.QRCodeService.Project.resource;

import com.QRCodeService.Project.domain.QRCodeGenerator;
import com.QRCodeService.Project.model.ErrorCorrection;
import com.QRCodeService.Project.utils.ResponseErrorHandler;
import com.google.zxing.WriterException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Configuration
@RestController
public class QRCodeResource {

    @GetMapping("/api/health")
    public ResponseEntity<Void> getHealth() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/qrcode")
    public ResponseEntity<?> getQRCode(@RequestParam String contents,
                                       @RequestParam(defaultValue = "250") int size,
                                       @RequestParam(defaultValue = "png") String type,
                                       @RequestParam(defaultValue = "L") String correction) throws IOException, WriterException {

        if (contents.isBlank() || contents.isEmpty()) {
            return new ResponseErrorHandler("Contents cannot be null or blank").getResponseEntity();
        }

        if (size < 150 || size > 350) {
            return new ResponseErrorHandler("Image size must be between 150 and 350 pixels").getResponseEntity();
        }

        if (!ObjectUtils.containsConstant(ErrorCorrection.values(), correction, true)) {
            return new ResponseErrorHandler("Permitted error correction levels are L, M, Q, H").getResponseEntity();
        }

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            BufferedImage bufferedImage = new QRCodeGenerator(size, contents, correction).getBufferedImage();
            MediaType mediaType = MediaType.IMAGE_JPEG;
            switch (type) {
                case "jpeg" -> {
                }
                case "png" -> mediaType = MediaType.IMAGE_PNG;
                case "gif" -> mediaType = MediaType.IMAGE_GIF;
                default -> {
                    return new ResponseErrorHandler("Only png, jpeg and gif image types are supported").getResponseEntity();
                }
            }

            ImageIO.write(bufferedImage, type, byteArrayOutputStream);
            byte[] bytes = byteArrayOutputStream.toByteArray();
            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .body(bytes);

        } catch (WriterException e) {
            throw new WriterException(e.getMessage());
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }


    }

    @Bean
    public HttpMessageConverter<BufferedImage> bufferedImageHttpMessageConverter() {
        return new BufferedImageHttpMessageConverter();
    }
}