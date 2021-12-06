package ch.globaz.common.util;

import ch.globaz.common.document.reference.ReferenceQR;
import ch.globaz.common.exceptions.CommonTechnicalException;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.EnumMap;
import java.util.List;

public class GenerationQRCode {

    private static final String OVERLAY_IMAGE = "images/CH-Kreuz_7mm.png";

    private static final String TARGET_FINAL_NAME = "qrCode";
    private static final String TARGET_FINAL_EXTENSION = ".png";

    private static final int SWISS_CROSS_EDGE_SIDE_PX = 166;

    private static final int SWISS_CROSS_EDGE_SIDE_MM = 7;

    /**
     * The edge length of the qrcode inclusive its white border.
     */
    private static final int QR_CODE_EDGE_SIDE_MM = 46;

    private static final int QR_CODE_EDGE_SIDE_PX = SWISS_CROSS_EDGE_SIDE_PX * QR_CODE_EDGE_SIDE_MM  / SWISS_CROSS_EDGE_SIDE_MM;

    /**
     * M�thode permettant de g�n�rer un QR Code avec la croix swiss int�gr�e en son centre.
     *
     * @param payload : le contenu du QR Code.
     * @return le chemin vers l'image du QR code cr��.
     */
    public static String generateSwissQrCode(ReferenceQR referenceQR, String payload) {

        // generate the qr code from the payload.
        BufferedImage qrCodeImage = generateQrCodeImage(payload);
        String qrCodePath;
        try {
            // overlay the qr code with a Swiss Cross
            BufferedImage combinedQrCodeImage = overlayWithSwissCross(qrCodeImage);
            String filePath = referenceQR.getWorkApplicationPath() + TARGET_FINAL_NAME + "_" + referenceQR.getUID() + TARGET_FINAL_EXTENSION;
            qrCodePath = JadeStringUtil.change(filePath, '\\', '/');

            // Save as new file to the target location
            ImageIO.write(combinedQrCodeImage, "PNG", new File(qrCodePath));
        } catch (IOException e) {
            throw new CommonTechnicalException("Erreur lors de l'ajout de la croix suisse au QR Code", e);
        }
        return qrCodePath;
    }

    /**
     * G�n�ration du QR Code sous forme d'image.
     *
     * @param payload : le contenu du QR Code.
     * @return l'image du QR Code.
     */
    private static BufferedImage generateQrCodeImage(String payload) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        EnumMap<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        hints.put(EncodeHintType.CHARACTER_SET, StandardCharsets.UTF_8.name());
        hints.put(EncodeHintType.MARGIN, 0);

        BitMatrix bitMatrix;
        try {
            bitMatrix = qrCodeWriter.encode(payload, BarcodeFormat.QR_CODE, QR_CODE_EDGE_SIDE_PX, QR_CODE_EDGE_SIDE_PX, hints);
        } catch (WriterException e) {
            throw new CommonTechnicalException("Erreur lors de la cr�ation du QR Code", e);
        }
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

    /**
     * Ajout de la croix suisse sur l'image du QR code cr��.
     *
     * @param qrCodeImage : l'image du QR Code.
     * @return l'image du QR Code avec la croix suisse int�gr�e.
     * @throws IOException
     */
    private static BufferedImage overlayWithSwissCross(BufferedImage qrCodeImage) throws IOException {
        Path swissCrossPath;
        String pathString = JadeStringUtil.change(Jade.getInstance().getExternalModelDir() + OVERLAY_IMAGE, '\\', '/');
        swissCrossPath = FileSystems.getDefault().getPath(pathString);

        BufferedImage swissCrossImage = ImageIO.read(swissCrossPath.toFile());

        BufferedImage combindedQrCodeImage = new BufferedImage(qrCodeImage.getWidth(), qrCodeImage.getHeight(), BufferedImage.TYPE_INT_ARGB);

        // paint both images, preserving the alpha channels
        Graphics g = combindedQrCodeImage.getGraphics();
        g.drawImage(qrCodeImage, 0, 0, null);
        int swissCrossPosition = (QR_CODE_EDGE_SIDE_PX / 2) - (SWISS_CROSS_EDGE_SIDE_PX / 2);
        g.drawImage(swissCrossImage, swissCrossPosition, swissCrossPosition, null);

        return combindedQrCodeImage;
    }

    /**
     * M�thode permettant de supprimer les images temporaires de QR Code cr��es.
     */
    public static void deleteQRCodeImages(List<ReferenceQR> qrFactures) throws IOException {
        for(ReferenceQR referenceQR : qrFactures) {
            String filePath = referenceQR.getWorkApplicationPath() + TARGET_FINAL_NAME + "_" + referenceQR.getUID() + TARGET_FINAL_EXTENSION;
            String pathString = JadeStringUtil.change(filePath, '\\', '/');
            Path qrCodePath = FileSystems.getDefault().getPath(pathString);
            Files.deleteIfExists(qrCodePath);
        }
        qrFactures.clear();
    }
}
