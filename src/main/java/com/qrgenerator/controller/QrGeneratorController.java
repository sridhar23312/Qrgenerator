package com.qrgenerator.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.qrgenerator.service.QrService;

@RestController
@RequestMapping("/api/qrcode")
public class QrGeneratorController {

    @Autowired
    private QrService qrCodeService;

    @GetMapping(value = "/generate", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<String> generateQrCode(
            ) {

        try {
           QrService.writeQrCode("helloworld 1 demo", "src/main/resources/logo.png", "src/main/resources/qr_with_logo.png", 300);
           String qrCodeImage ="afcfycfc";

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"qrcode.png\"")
                    .body(qrCodeImage);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
