package ch.globaz.vulpecula.comptabilite.importationcg;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.vulpecula.comptabilite.importationcg.xmlobject.ImportEcritures;

/**
 * Classe principale à lancer pour importer les écritures
 * 
 * @since WebBMS 0.5
 */
public class EcritureHelios {

    /**
     * @param args csvFilePath destinationFilePath journalLibelle journalDate
     */
    public static void main(String... args) {
        if (args == null || args.length < 2) {
            System.out.println("Erreur, les arguments nécessaire sont : csvFilePath destinationPath");
            System.out.println("Arguments facultatifs : journalLibelle journalDate (date au format JJ.MM.AAAA)");
            System.out.println("Exemple :");
            System.out.println("EcritureHelios C:\\Temp\\myprodis.csv C:\\temp");
            System.out.println("EcritureHelios C:\\Temp\\myprodis.csv C:\\temp \"journal test\" 23.08.2015");
            return;
        }

        String csvFilepath = args[0];
        String dstFilepath = args[1];
        String journalLibelle = "";
        String journalDate = "";

        if (args.length > 2) {
            journalLibelle = args[2];
        }
        if (args.length > 3) {
            journalDate = args[3];
        }

        // *** Read CSV ***
        // ************
        List<MyProdisMyAccCsv> liste = Csv2JaxbBuilder.readCsv(csvFilepath);

        // On trie par mandat
        Map<String, List<MyProdisMyAccCsv>> ecritureParMandat = new HashMap<String, List<MyProdisMyAccCsv>>();
        for (MyProdisMyAccCsv ecriture : liste) {
            if (ecritureParMandat.containsKey(ecriture.getIdSociete())) {
                List<MyProdisMyAccCsv> listMandat = ecritureParMandat.get(ecriture.getIdSociete());
                listMandat.add(ecriture);
            } else {
                List<MyProdisMyAccCsv> listMandat = new ArrayList<MyProdisMyAccCsv>();
                listMandat.add(ecriture);
                ecritureParMandat.put(ecriture.getIdSociete(), listMandat);
            }
        }

        // On crée les fichiers xml par mandat
        for (String key : ecritureParMandat.keySet()) {
            List<MyProdisMyAccCsv> listMandat = ecritureParMandat.get(key);

            // *** Treat data CSV ***
            // ************
            ImportEcritures importEcriture = Csv2JaxbBuilder.buildXml(listMandat, journalLibelle, journalDate);

            // *** Create file ***
            // ************
            // String dstFilepathFinal = dstFilepath.replace(".xml", key + ".xml");
            String dstFilepathFinal = dstFilepath + File.separator + "compta_" + key + ".xml";
            Csv2JaxbBuilder.createXmlFile(importEcriture, dstFilepathFinal);
        }

    }
}