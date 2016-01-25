package globaz.helios.parser;

import globaz.framework.util.FWCurrency;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.xml.JadeXmlUtil;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class CGImportEcritureByMandat {

    private final static String FILE_PREFFIX = "mandat_";

    private final static String[] tagsToDump = new String[] { "mandat", "date", "libelle", "idexternecomptedebite",
            "idexternecomptecredite", "montant" };

    /**
     * Pour debug Dump dans la console
     */
    public static void dump(Map<String, List<Element>> m) {
        for (Entry<String, List<Element>> entry : m.entrySet()) {
            String mandat = entry.getKey();
            List<Element> doubleTagList = entry.getValue();

            System.out.println("-------------------------------------");
            System.out.println("Mandat : " + mandat);
            System.out.println("-------------------------------------");

            System.out.println("<importEcritures>");
            for (Element doubleTag : doubleTagList) {
                String id = JadeXmlUtil.getAttribute(doubleTag, "id");
                System.out.println("\t<double id=\"" + id + "\">");
                for (String tag : CGImportEcritureByMandat.tagsToDump) {
                    Element e = JadeXmlUtil.getFirstNamedTagFrom(doubleTag, tag);
                    String v = JadeXmlUtil.getAttribute(e, "value");
                    System.out.println("\t\t<" + tag + " value=\"" + v + "\" />");
                }
                System.out.println("\t</double>");
            }
        }
        System.out.println("</importEcritures>");
    }

    /**
     * regroupe le contenu du fichier source par mandat dans une Map
     */
    public static Map<String, List<Element>> groupByMandat(Document source, Map<String, List<Element>> m) {
        NodeList list = source.getElementsByTagName("double");
        // construit un map ayant pour clé la valeur du tag mandat et comme
        // valeur
        // une liste des node "double" de ce mandat
        // map[mandat:[double]]

        for (int i = 0; i < list.getLength(); i++) {
            Element e = (Element) list.item(i);
            String mandat = ((Element) e.getElementsByTagName("mandat").item(0)).getAttribute("value");
            List<Element> l = (m.get(mandat) == null) ? new ArrayList<Element>() : m.get(mandat);

            l.add(e);
            m.put(mandat, l);
        }
        return m;

    }

    /*
     * Produit des fichiers par mandat
     */
    public static Map<String, Integer> toFiles(Map<String, List<Element>> m, String path) throws Exception {
        FWCurrency FMontantCumule = new FWCurrency();
        Map<String, Integer> filenamesAndCount = new HashMap<String, Integer>();
        for (Entry<String, List<Element>> entry : m.entrySet()) {
            FWCurrency FMontant = new FWCurrency();
            String mandat = entry.getKey();
            List<Element> doubleTagList = entry.getValue();
            String filename = path + File.separator + CGImportEcritureByMandat.FILE_PREFFIX + mandat + ".xml";
            File f = new File(filename);
            BufferedWriter w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f), "ISO-8859-1"));
            try {
                w.write("<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>\n");
                w.write("<importEcritures>\n");
                for (Element doubleTag : doubleTagList) {
                    String id = JadeXmlUtil.getAttribute(doubleTag, "id");
                    w.write("\t<double id=\"" + id + "\">\n");
                    for (String tag : CGImportEcritureByMandat.tagsToDump) {
                        Element e = JadeXmlUtil.getFirstNamedTagFrom(doubleTag, tag);
                        String v = JadeXmlUtil.getAttribute(e, "value");
                        w.write("\t\t<" + tag + " value=\"" + v + "\" />\n");
                        if (e.getTagName().equalsIgnoreCase("montant")) {
                            FMontant.add(v);
                            FMontantCumule.add(v);
                        }
                    }
                    w.write("\t</double>\n");
                }
                w.write("</importEcritures>");
            } finally {
                try {
                    w.close();
                    filenamesAndCount.put(filename, doubleTagList.size());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Montant total du mandat " + mandat + " : "
                    + JANumberFormatter.format(FMontant.doubleValue()));
        }
        System.out.println("Montants des mandats cumulés : " + JANumberFormatter.format(FMontantCumule.doubleValue()));
        return filenamesAndCount;
    }
}
