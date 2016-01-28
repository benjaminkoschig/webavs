package ch.globaz.vulpecula.comptabilite.importationcg.csv;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utilitaire pour gérer un fichier CSV
 * 
 * @since WebBMS 0.5
 */
public class CsvFile {
    public static final char SEPARATOR = ';';
    public static final boolean HAS_HEADER = true;

    private File file;
    private List<String> lines;
    private String[] header;
    private String[] footer;

    /**
     * renvoie une liste de tableaux où chaque élément (i.e. chaque tableau) de la liste représente une ligne du fichier
     * CSV, et où chaque cellule du tableau correspond à une donnée (i.e. colonne) de la ligne.
     */
    private List<String[]> data;
    private List<Map<String, String>> mappedData;

    public CsvFile(String filepath) {
        this(new File(filepath));
    }

    public CsvFile(File file) {
        if (file == null) {
            throw new IllegalArgumentException("Le fichier ne peut pas être null");
        }

        if (!file.exists()) {
            throw new IllegalArgumentException("Le fichier " + file.getName() + " n'existe pas.");
        }

        this.file = file;

        // Init
        init();
    }

    private void init() {
        lines = CsvFileHelper.readFile(file);

        data = new ArrayList<String[]>(lines.size());
        String sep = new Character(SEPARATOR).toString();
        boolean first = HAS_HEADER;
        int i = 0;
        for (String line : lines) {
            i++;
            // Suppression des espaces de fin de ligne
            line = line.trim();

            // On saute les lignes vides
            if (line.length() == 0) {
                continue;
            }

            // On saute les lignes de commentaire
            if (line.startsWith("#")) {
                continue;
            }

            String[] oneData = line.split(sep);

            if (first) {
                header = oneData;
                first = false;
            } else if (lines.size() == i) {
                footer = oneData;
            } else {
                data.add(oneData);
            }
        }
    }

    private void mapData(String[] titles) {
        mappedData = new ArrayList<Map<String, String>>(data.size());

        final int titlesLength = titles.length;

        for (String[] oneData : data) {
            final Map<String, String> map = new HashMap<String, String>();
            for (int i = 0; i < titlesLength; i++) {
                final String key = titles[i];
                final String value = oneData[i];
                map.put(key, value);
            }

            mappedData.add(map);
        }
    }

    public List<Map<String, String>> getMappedData(String[] titles) {
        // On mappe les lignes trouvées
        mapData(titles);

        return mappedData;
    }

    /**
     * @return the file
     */
    public File getFile() {
        return file;
    }

    /**
     * Retourne uniquement les data sans le header et sans le footer
     * 
     * @return the data
     */
    public List<String[]> getData() {
        return data;
    }

    /**
     * Retourne l'ensemble des lignes comprenant le header, le footer et les data
     * 
     * @return the lines
     */
    public List<String> getLines() {
        return lines;
    }

    /**
     * @return the titles
     */
    public String[] getHeader() {
        return header;
    }

    /**
     * @return the total
     */
    public String[] getFooter() {
        return footer;
    }
}
