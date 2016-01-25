package ch.globaz.vulpecula.web.util;

import globaz.globall.db.BSessionUtil;
import java.util.List;
import java.util.Vector;

/**
 * @author Arnaud Geiser (AGE) | Créé le 27 janv. 2014
 * 
 */
public class FormUtil {
    public static Vector<String[]> getEmptyList() {
        Vector<String[]> emptyList = new Vector<String[]>();
        String[] element = new String[2];
        element[0] = "";
        element[1] = "";
        emptyList.add(element);
        return emptyList;
    }

    public static Vector<String[]> getList(List<String> liste) {
        Vector<String[]> vector = new Vector<String[]>();
        generateEntries(vector, liste);
        return vector;
    }

    public static Vector<String[]> getListWithBlank(List<String> liste) {
        Vector<String[]> vector = getEmptyList();
        generateEntries(vector, liste);
        return vector;
    }

    private static void generateEntries(Vector<String[]> vector, List<String> liste) {
        for (String element : liste) {
            String[] entry = new String[2];
            entry[0] = element;
            entry[1] = BSessionUtil.getSessionFromThreadContext().getCodeLibelle(element);
            vector.add(entry);
        }
    }

    public static Vector<String[]> getListOf(List<String[]> liste) {
        Vector<String[]> vector = getEmptyList();
        for (String[] element : liste) {
            vector.add(element);
        }
        return vector;
    }
}
