package globaz.hermes.test;

import globaz.hermes.utils.EBCDICFileReader;
import globaz.hermes.utils.EBCDICFileWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * @author ado 12 nov. 03
 */
public class AsciiTest {
    public static void main(String[] args) {
        try {
            new AsciiTest().go();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Constructor for AsciiTest.
     */
    public AsciiTest() {
        super();
    }

    public void go() throws Exception {
        FileInputStream fis = new FileInputStream("Z:/hermesReprise/files/asciiTest");
        EBCDICFileWriter efw = new EBCDICFileWriter("Z:/hermesReprise/files/asciiTest_ebcdic");
        int c = 0;
        while ((c = fis.read()) != -1) {
            efw.write("" + (char) c);
        }
        fis.close();
        efw.close();
        EBCDICFileReader efr = new EBCDICFileReader("Z:/hermesReprise/files/asciiTest_ebcdic", true);
        FileOutputStream fos = new FileOutputStream("Z:/hermesReprise/files/asciiTest_ebcdic_ascii");
        while ((c = efr.read()) != -1) {
            fos.write(c);
        }
    }
}
