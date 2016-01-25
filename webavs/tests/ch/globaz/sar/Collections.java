package ch.globaz.sar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;

public class Collections {

    @Test
    public void testArrayList() {
        List<String> liste = new ArrayList<String>();
        liste.add("Seb");
        liste.add("Lulu");
        liste.add("toto");

        for (String t : liste) {
            System.out.println(t);
        }
    }

    @Test
    public void testHashMap() {
        Map<String, String> liste = new HashMap<String, String>();
        liste.put("Seb", "SEB");
        liste.put("Lulu", "LULU");
        liste.put("toto", "TOTO");

        for (String t : liste.values()) {
            System.out.println(t);
        }

        liste.put("dernier", "DERNIER");/*-?|Optimisation RFM|sce|c0|?*/
        liste.put("Seb", "SEB2");
        for (String t : liste.values()) {
            System.out.println(t);
        }

    }

}
