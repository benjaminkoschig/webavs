package globaz.hermes.utils;

import java.io.FileOutputStream;
import java.io.RandomAccessFile;

/**
 * @author ado 7 mai 04
 */
public class SplitFile {

    public static void main(String[] args) {
        try {
            RandomAccessFile raf = new RandomAccessFile("c:/jeudessai.txt", "r");
            FileOutputStream fos11 = new FileOutputStream("c:/jeudessaiOUT.txt");
            FileOutputStream fos20 = new FileOutputStream("c:/jeudessaiIN.txt");
            String line = "";
            while ((line = raf.readLine()) != null) {
                try {
                    line = line.substring(0, 0 + 120);

                    if (line.startsWith("11")) {
                        fos11.write((line + "\n").getBytes());
                    } else {
                        fos20.write((line + "\n").getBytes());
                    }
                } catch (Exception e) {
                    // e.printStackTrace();
                }
            }
            raf.close();
            fos11.close();
            fos20.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Constructor for SplitFile.
     */
    public SplitFile() {
        super();
    }
}
