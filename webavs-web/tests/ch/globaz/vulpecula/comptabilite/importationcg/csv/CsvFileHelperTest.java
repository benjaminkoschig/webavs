package ch.globaz.vulpecula.comptabilite.importationcg.csv;

import static org.junit.Assert.*;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import ch.globaz.vulpecula.comptabilite.importationcg.csv.CsvFileHelper;

/**
 * Description de la classe
 * 
 * @since WebBMS 0.5
 */
public class CsvFileHelperTest {
    private final static String FILE_NAME = "BMS_COMPTA_20150107170619000.CSV";

    @Test
    public void testGetResource() throws URISyntaxException {
        // Param
        final String fileName = FILE_NAME;

        // Result
        // ...

        // Appel
        URL url = this.getClass().getResource(fileName);
        File file = new File(url.toURI());

        // Test
        // On sait que le fichier existe bien puisque c'est avec lui qu'on travaille depuis le début.
        assertTrue(file.exists());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetResourceNullFileName() {
        // Param
        final String fileName = null;

        // Result
        // ...

        // Appel qui plante
        CsvFileHelper.getResource(fileName);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetResourceEmptyFileName() {
        // Param
        final String fileName = "";

        // Result
        // ...

        // Appel qui plante
        CsvFileHelper.getResource(fileName);

    }

    @Test
    public void testReadFile() throws URISyntaxException {
        // Param
        final String fileName = FILE_NAME;

        // Result
        final int nombreLigne = 2008;

        // Appel
        URL url = this.getClass().getResource(fileName);
        File file = new File(url.toURI());
        List<String> lines = CsvFileHelper.readFile(file);

        // Test
        Assert.assertEquals(nombreLigne, lines.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadFileNullFile() {
        // Param
        final File file = null;

        // Result
        // ...

        // Appel qui plante
        List<String> lines = CsvFileHelper.readFile(file);
    }

    @Test
    public void testReadFullFile() throws URISyntaxException {
        // Param
        final String fileName = FILE_NAME;

        // Result
        final String start = "00;1;IT03;20150107;MYPRODIS;#null#;5;;";
        final String end = "99;2008;2006;7816862";

        // Appel
        URL url = this.getClass().getResource(fileName);
        File file = new File(url.toURI());
        String content = CsvFileHelper.readFileAsString(file);
        System.out.println(content);

        // Test
        assertTrue(content.startsWith(start));
        assertTrue(content.endsWith(end));
    }
}
