package com.qrgenerator.service;



import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.client.j2se.MatrixToImageConfig;

import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class QrService {

	public static void writeQrCode(String text, String imagePath, String outPath, int width) throws Exception {

      

        // QR codes are square, so height = width
        int height = width;
     

        // Generate QR code
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
        
        // Convert to a BufferedImage with specified colors
        BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix, new MatrixToImageConfig(0xFF000000, 0xFFFFFFFF));

        // Create a combined image for the QR and overlay
        BufferedImage combined = new BufferedImage(qrImage.getHeight(), qrImage.getWidth(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = combined.createGraphics();

        // Draw QR code on the combined image
        g.drawImage(qrImage, 0, 0, null);

        // Overlay the logo on the QR code
        addOverlayImage(g, qrImage, imagePath);

        // Write the final image to a file
        ImageIO.write(combined, "png", new File(outPath));
        System.out.println("Created QR code with overlay at " + outPath);
    }

    private static BufferedImage addOverlayImage(Graphics2D g, BufferedImage qrImage, String imagePath) throws IOException {
        // Load the logo image
        BufferedImage overlay = ImageIO.read(new File(imagePath));

        // Set maximum logo size as a fraction of QR code size (e.g., 20%)
        int maxOverlayWidth = qrImage.getWidth() / 5;
        int maxOverlayHeight = qrImage.getHeight() / 5;

        // Scale the overlay if it's too large
        if (overlay.getWidth() > maxOverlayWidth || overlay.getHeight() > maxOverlayHeight) {
            overlay = resizeImage(overlay, maxOverlayWidth, maxOverlayHeight);
        }

        // Center the overlay on the QR code
        int wOffset = (qrImage.getWidth() - overlay.getWidth()) / 2;
        int hOffset = (qrImage.getHeight() - overlay.getHeight()) / 2;

        // Draw the overlay on the QR code
        g.drawImage(overlay, wOffset, hOffset, null);
        
        return overlay;
    }

    // Helper method to resize an image to target dimensions
    private static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        Image scaledImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(scaledImage, 0, 0, null);
        g2d.dispose();

        return resizedImage;
    }

//    public static void main(String[] args) {
//        try {
//            // QR code text content
//            String text = "https://example.com";
//            // Path to the logo image
//            String imagePath = "src/main/resources/logo.png";
//            // Path to save the QR code image with overlay
//            String outPath = "src/main/resources/qr_with_logo.png";
//            // QR code size
//            int width = 300;
//
//            // Generate QR code with overlay
//            writeQrCode(text, imagePath, outPath, width);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}