package ch.globaz.vulpecula.comptabilite.importationcg.csv;

import static org.junit.Assert.*;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import ch.globaz.vulpecula.comptabilite.importationcg.csv.CsvFile;

/**
 * Description de la classe
 * 
 * @since WebBMS 0.5
 */
public class CsvFileTest {
    private static final int NOMBRE_COLONNE_FOOTER = 4;
    private static final int NOMBRE_LIGNE_SANS_HEADER = 2006;
    private static final Object NOMBRE_LIGNE = 2008;
    private static final int NOMBRE_COLONNE_HEADER = 7;
    private static final int NOMBRE_COLONNE = 15;
    private static final String FILE_NAME = "BMS_COMPTA_20150107170619000.CSV";
    private File file;

    @Before
    public void beforeClass() throws URISyntaxException {
        URL url = this.getClass().getResource(FILE_NAME);
        file = new File(url.toURI());
    }

    @Test
    public void testFile() {

        // Appel
        final CsvFile csvFile = new CsvFile(file);
        final File f = csvFile.getFile();

        // Test
        assertEquals(file, f);
    }

    @Test
    public void testCsvFile() {
        // Appel
        final CsvFile csvFile01 = new CsvFile(file);
        final List<String> lines = csvFile01.getLines();

        // Test
        assertEquals(NOMBRE_LIGNE, lines.size());
    }

    @Test
    public void testData() {
        // Appel
        final CsvFile csvFile = new CsvFile(file);
        final List<String[]> data = csvFile.getData();

        // Test
        assertEquals(NOMBRE_LIGNE_SANS_HEADER, data.size());

        for (String[] oneData : data) {
            assertEquals(NOMBRE_COLONNE, oneData.length);
        }
    }

    @Test
    public void testTitle() {
        // Appel
        final CsvFile csvFile = new CsvFile(file);
        final String[] title = csvFile.getHeader();

        // Test
        assertEquals(NOMBRE_COLONNE_HEADER, title.length);
    }

    @Test
    public void testFooter() {
        // Appel
        final CsvFile csvFile = new CsvFile(file);
        final String[] total = csvFile.getFooter();

        // Test
        assertEquals(NOMBRE_COLONNE_FOOTER, total.length);
    }

    @Test
    public void testMappedData() {
        // Appel
        final CsvFile csvFile = new CsvFile(file);

        String[] titles = { "typeEnregistrement", "noEnregistrement", "emetteur", "idSociete", "idEcriture",
                "idPartenerUnique", "pieceComptable", "dateValeur", "dateComptabilisation", "libelle", "montantDevise",
                "devise", "montantCHF", "noCompteDebit", "noCompteCredit" };
        List<Map<String, String>> mappedData = csvFile.getMappedData(titles);

        // Test
        assertEquals(NOMBRE_LIGNE_SANS_HEADER, mappedData.size());

        for (Map<String, String> oneMappedData : mappedData) {
            assertEquals(NOMBRE_COLONNE, oneMappedData.size());
        }
    }
}
