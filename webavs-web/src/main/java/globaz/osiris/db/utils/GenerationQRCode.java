package globaz.osiris.db.utils;

import com.google.gson.JsonObject;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class GenerationQRCode {

    private static final String QR_CODE_LOCATION = "C://Users/EBSC/Documents/QR_CODE/qr_code";

    private GenerationQRCode() {
    }

    public static final String generateQRCodeImage(JsonObject object, int width, int height)
            throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(object.toString(), BarcodeFormat.QR_CODE, width, height);

        Path path = FileSystems.getDefault().getPath(QR_CODE_LOCATION);
        MatrixToImageWriter.writeToPath(bitMatrix, "JPG", path);

        return path.toString();
    }
}
