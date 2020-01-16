package globaz.osiris.db.utils;

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
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class GenerationQRCode {

    public GenerationQRCode() {
    }

    private static final String OVERLAY_IMAGE = "images/CH-Kreuz_7mm.png";

    private static final String TARGET_FINAL_NAME = "work/qrCode.png";

    private static final int SWISS_CROSS_EDGE_SIDE_PX = 166;

    private static final int SWISS_CROSS_EDGE_SIDE_MM = 7;

    /**
     * The edge length of the qrcode inclusive its white border.
     */
    private static final int QR_CODE_EDGE_SIDE_MM = 45;

    private static final int QR_CODE_EDGE_SIDE_PX = SWISS_CROSS_EDGE_SIDE_PX / SWISS_CROSS_EDGE_SIDE_MM * QR_CODE_EDGE_SIDE_MM;

    private static final String PAYLOAD_1 = "SPC\r\n" +
            "0200\r\n" +
            "1\r\n" +
            "CH4431999123000889012\r\n" +
            "S\r\n" +
            "Robert Schneider AG\r\n" +
            "Via Casa Postale\r\n" +
            "1268/2/22\r\n" +
            "2501\r\n" +
            "Biel\r\n" +
            "CH\r\n" +
            "\r\n" +
            "\r\n" +
            "\r\n" +
            "\r\n" +
            "\r\n" +
            "\r\n" +
            "\r\n" +
            "123949.75\r\n" +
            "CHF\r\n" +
            "S\r\n"+
            "Pia-Maria Rutschmann-Schnyder\r\n" +
            "Grosse Marktgasse\r\n" +
            "28/5\r\n" +
            "9400\r\n" +
            "Rorschach\r\n" +
            "CH\r\n" +
            "QRR\r\n" +
            "210000000003139471430009017\r\n" +
            "Beachten sie unsere Sonderangebotswoche bis 23.02.2017!\r\n" +
            "EPD\r\n" +
            "//S1/10/10201409/11/181105/40/0:30\r\n" +
            "eBill/B/41010560425610173";

    public String generateSwissQrCode(String payload) {

        // generate the qr code from the payload.
        BufferedImage qrCodeImage = generateQrCodeImage(PAYLOAD_1);
        String qrCodePath;
        try {
            // overlay the qr code with a Swiss Cross
            BufferedImage combinedQrCodeImage = overlayWithSwissCross(qrCodeImage);

            qrCodePath = JadeStringUtil.change(Jade.getInstance().getExternalModelDir() + TARGET_FINAL_NAME, '\\', '/');

            // Save as new file to the target location
            ImageIO.write(combinedQrCodeImage, "PNG", new File(qrCodePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return qrCodePath;
    }

    private BufferedImage generateQrCodeImage(String payload) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        hints.put(EncodeHintType.CHARACTER_SET, StandardCharsets.UTF_8.name());
        hints.put(EncodeHintType.MARGIN, 0);

        BitMatrix bitMatrix;
        try {
            bitMatrix = qrCodeWriter.encode(payload, BarcodeFormat.QR_CODE, QR_CODE_EDGE_SIDE_PX, QR_CODE_EDGE_SIDE_PX, hints);
        } catch (WriterException e) {
            throw new RuntimeException(e);
        }
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

    private BufferedImage overlayWithSwissCross(BufferedImage qrCodeImage) throws IOException {

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
}
