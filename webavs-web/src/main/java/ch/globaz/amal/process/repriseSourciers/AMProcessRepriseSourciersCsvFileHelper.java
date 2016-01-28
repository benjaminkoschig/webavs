/**
 * 
 */
package ch.globaz.amal.process.repriseSourciers;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import ch.globaz.amal.business.exceptions.models.uploadfichierreprise.AMUploadFichierRepriseException;

/**
 * @author dhi
 * 
 */
public class AMProcessRepriseSourciersCsvFileHelper {

    /**
     * Création des entités en fonctions des lignes objets
     * 
     * @param allLines
     * @return
     */
    public static List<AMProcessRepriseSourciersCsvEntity> createEntity(List<AMProcessRepriseSourciersCsvLine> allLines) {

        // ----------------------------------------------------------------------------------------------------
        // Variables utiles
        // ----------------------------------------------------------------------------------------------------
        List<AMProcessRepriseSourciersCsvEntity> result = new ArrayList<AMProcessRepriseSourciersCsvEntity>();
        HashMap<String, List<AMProcessRepriseSourciersCsvLine>> sourciersMap = new HashMap<String, List<AMProcessRepriseSourciersCsvLine>>();
        HashMap<String, List<AMProcessRepriseSourciersCsvLine>> conjointsMap = new HashMap<String, List<AMProcessRepriseSourciersCsvLine>>();
        // ----------------------------------------------------------------------------------------------------
        // Création des hashmaps sourciers, clé = noSourcier
        // ----------------------------------------------------------------------------------------------------
        for (int iLine = 0; iLine < allLines.size(); iLine++) {

            AMProcessRepriseSourciersCsvLine currentLine = allLines.get(iLine);
            String currentNoSourcier = currentLine.getNoSourcier();
            String currentNoConjoint = currentLine.getNoConjoint();
            // 1 - Récupération des lignes par noSourcier
            // MAP <noSourcier, ligne>
            if (!JadeStringUtil.isEmpty(currentNoSourcier)) {
                List<AMProcessRepriseSourciersCsvLine> linesForSourcier = sourciersMap.get(currentNoSourcier);
                if (linesForSourcier == null) {
                    linesForSourcier = new ArrayList<AMProcessRepriseSourciersCsvLine>();
                }
                linesForSourcier.add(currentLine);
                sourciersMap.put(currentNoSourcier, linesForSourcier);
            }
            // 2 - Renseignement également des lignes qui ont une information conjoint
            if (!JadeStringUtil.isEmpty(currentNoConjoint)) {
                List<AMProcessRepriseSourciersCsvLine> linesForConjoint = conjointsMap.get(currentNoConjoint);
                if (linesForConjoint == null) {
                    linesForConjoint = new ArrayList<AMProcessRepriseSourciersCsvLine>();
                }
                linesForConjoint.add(currentLine);
                conjointsMap.put(currentNoConjoint, linesForConjoint);
            }
        }
        // ----------------------------------------------------------------------------------------------------
        // Création des entités CSV, qui seront enregistrées en base pour une future création de population
        // ----------------------------------------------------------------------------------------------------
        List<String> noSourcierTraite = new ArrayList<String>();

        Iterator<String> iteratorSourciers = sourciersMap.keySet().iterator();
        while (iteratorSourciers.hasNext()) {
            // 1 - Récupération de la clé du sourcier et traitement si pas déjà eu lieu
            String keySourcier = iteratorSourciers.next();
            String keyConjoint = null;
            if (keySourcier.equals("100533") || keySourcier.equals("100529")) {
                String hello = "hello";
            }

            if (noSourcierTraite.contains(keySourcier)) {
                continue;
            }
            List<AMProcessRepriseSourciersCsvLine> linesForSourcier = sourciersMap.get(keySourcier);
            // 2 - Recherche du conjoint si renseigné
            if ((linesForSourcier != null) && (linesForSourcier.size() > 0)) {
                keyConjoint = linesForSourcier.get(0).getNoConjoint();
                if (JadeStringUtil.isBlankOrZero(keyConjoint)) {
                    keyConjoint = null;
                } else {
                    List<AMProcessRepriseSourciersCsvLine> linesForConjoint = sourciersMap.get(keyConjoint);
                    if ((linesForConjoint != null) && (linesForConjoint.size() > 0)) {
                        linesForSourcier.addAll(linesForConjoint);
                    }
                }
            }
            // 3 - Recherche également si pas connu comme conjoint (possible, fichier étape 2 pas toujours fiable)
            if (keyConjoint == null) {
                List<AMProcessRepriseSourciersCsvLine> linesAsConjoint = conjointsMap.get(keySourcier);
                if ((linesAsConjoint != null) && (linesAsConjoint.size() > 0)) {
                    linesForSourcier.addAll(linesAsConjoint);
                    // enregistrement de la clé conjoint (qui est le sourcier dans ce cas là)
                    keyConjoint = linesAsConjoint.get(0).getNoSourcier();
                }
            }

            // 4 - indication des noSourciers traités
            noSourcierTraite.add(keySourcier);
            if (keyConjoint != null) {
                noSourcierTraite.add(keyConjoint);
            }

            // 5 - Tri IMPORTANT (M en premier, F en second, autrement tri sur no contribuable)
            Collections.sort(linesForSourcier, Collections.reverseOrder());

            // 6 - Création de l'entité csv
            if ((linesForSourcier != null) && (linesForSourcier.size() > 0)) {
                AMProcessRepriseSourciersCsvEntity csvEntity = new AMProcessRepriseSourciersCsvEntity(linesForSourcier);
                result.add(csvEntity);
            }
        }

        return result;
    }

    /**
     * Création des entités en fonctions des lignes objets
     * 
     * @param allLines
     * @return
     */
    public static List<AMProcessRepriseSourciersCsvEntity> createEntityOld(
            List<AMProcessRepriseSourciersCsvLine> allLines) {

        // ----------------------------------------------------------------------------------------------------
        // Variables utiles
        // ----------------------------------------------------------------------------------------------------
        List<AMProcessRepriseSourciersCsvEntity> result = new ArrayList<AMProcessRepriseSourciersCsvEntity>();
        HashMap<String, List<AMProcessRepriseSourciersCsvLine>> sourciersMap = new HashMap<String, List<AMProcessRepriseSourciersCsvLine>>();
        HashMap<String, List<AMProcessRepriseSourciersCsvLine>> conjointsMap = new HashMap<String, List<AMProcessRepriseSourciersCsvLine>>();
        HashMap<String, List<AMProcessRepriseSourciersCsvLine>> conjointIsSourcierMMap = new HashMap<String, List<AMProcessRepriseSourciersCsvLine>>();
        HashMap<String, List<AMProcessRepriseSourciersCsvLine>> conjointIsSourcierFMap = new HashMap<String, List<AMProcessRepriseSourciersCsvLine>>();

        // ----------------------------------------------------------------------------------------------------
        // Création des hashmaps sourciers et conjoints
        // ----------------------------------------------------------------------------------------------------
        for (int iLine = 0; iLine < allLines.size(); iLine++) {

            AMProcessRepriseSourciersCsvLine currentLine = allLines.get(iLine);
            String currentNoSourcier = currentLine.getNoSourcier();
            String currentNoConjoint = currentLine.getNoConjoint();

            // 1 - Récupération des lignes par noSourcier
            // MAP <noSourcier, ligne>
            if (!JadeStringUtil.isEmpty(currentNoSourcier)) {
                List<AMProcessRepriseSourciersCsvLine> linesForSourcier = sourciersMap.get(currentNoSourcier);
                if (linesForSourcier == null) {
                    linesForSourcier = new ArrayList<AMProcessRepriseSourciersCsvLine>();
                }
                linesForSourcier.add(currentLine);
                sourciersMap.put(currentNoSourcier, linesForSourcier);
            }

            // 2 - Récupération des lignes par noConjoint
            // MAP <noConjoint, ligne>
            if (!JadeStringUtil.isEmpty(currentNoConjoint)) {
                List<AMProcessRepriseSourciersCsvLine> linesForConjoint = conjointsMap.get(currentNoConjoint);
                if (linesForConjoint == null) {
                    linesForConjoint = new ArrayList<AMProcessRepriseSourciersCsvLine>();
                }
                linesForConjoint.add(currentLine);
                conjointsMap.put(currentNoConjoint, linesForConjoint);
            }
        }

        // ----------------------------------------------------------------------------------------------------
        // Définition des conjoints féminins et masculins
        // ----------------------------------------------------------------------------------------------------
        Iterator<String> conjointIterator = conjointsMap.keySet().iterator();
        while (conjointIterator.hasNext()) {
            // 3 - Enregistrement des conjoints qui sont également sourcier
            String currentConjointKey = conjointIterator.next();
            List<AMProcessRepriseSourciersCsvLine> sourciersLines = sourciersMap.get(currentConjointKey);

            if ((sourciersLines != null) && (sourciersLines.size() > 0)) {
                // copy pour aucun effet de bord (référence sur les arraylist, attention)
                List<AMProcessRepriseSourciersCsvLine> copyLines = new ArrayList<AMProcessRepriseSourciersCsvLine>();
                for (int iLine = 0; iLine < sourciersLines.size(); iLine++) {
                    copyLines.add(sourciersLines.get(iLine));
                }
                String sexeSourcier = sourciersLines.get(0).getSexeSourcier();
                if (sexeSourcier.equals("M")) {
                    conjointIsSourcierMMap.put(currentConjointKey, copyLines);
                } else if (sexeSourcier.equals("F")) {
                    conjointIsSourcierFMap.put(currentConjointKey, copyLines);
                } else {
                    JadeLogger.error(null, "SEXE INCONNU affectation à F : " + sourciersLines.get(0).getLineContent());
                    conjointIsSourcierFMap.put(currentConjointKey, copyLines);
                }
            }
        }

        JadeLogger.info(null, "Nombre de sourciers : " + sourciersMap.size());
        JadeLogger.info(null, "Nombre de conjoints : " + conjointsMap.size());
        JadeLogger.info(null, "Nombre de conjoints masculin étant sourcier : " + conjointIsSourcierMMap.size());
        JadeLogger.info(null, "Nombre de conjoints féminin étant sourcier : " + conjointIsSourcierFMap.size());

        // ----------------------------------------------------------------------------------------------------
        // les lignes des conjoints féminins sont affectées au sourcier masculin
        // ----------------------------------------------------------------------------------------------------
        Iterator<String> conjointIsSourcierFIterator = conjointIsSourcierFMap.keySet().iterator();
        while (conjointIsSourcierFIterator.hasNext()) {
            // 4 - On attache les conjoints féminins aux sourciers masculin en relation
            // On supprime les conjoints féminins du rôle de sourcier principal
            String currentNoSourcierConjointF = conjointIsSourcierFIterator.next();
            List<AMProcessRepriseSourciersCsvLine> sourciersFLines = conjointIsSourcierFMap
                    .get(currentNoSourcierConjointF);
            // Affectation des lignes au conjoint principal, normalement de sexe M
            if ((sourciersFLines != null) && (sourciersFLines.size() > 0)) {
                String noSourcierM = sourciersFLines.get(0).getNoConjoint();
                for (int iLine = 0; iLine < sourciersFLines.size(); iLine++) {
                    AMProcessRepriseSourciersCsvLine currentLine = sourciersFLines.get(iLine);
                    List<AMProcessRepriseSourciersCsvLine> linesForSourcier = sourciersMap.get(currentLine
                            .getNoConjoint());
                    if (linesForSourcier != null) {
                        if (linesForSourcier.get(0).getSexeSourcier().equals(currentLine.getSexeSourcier())) {
                            JadeLogger.warn(null, "Warning, sourcier and conjoint has the same sexe");
                            JadeLogger.warn(null, currentLine.getLineContent());
                            JadeLogger.warn(null, linesForSourcier.get(0).getLineContent());
                        }
                        linesForSourcier.add(currentLine);
                        sourciersMap.put(currentLine.getNoConjoint(), linesForSourcier);
                    }
                }
                // On enlève le conjoint F des sourciers
                sourciersMap.remove(currentNoSourcierConjointF);
            }
        }
        JadeLogger.info(null, "Nombre de sourciers : " + sourciersMap.size());
        JadeLogger.info(null, "Nombre de conjoints : " + conjointsMap.size());
        JadeLogger.info(null, "Nombre de conjoints masculin étant sourcier : " + conjointIsSourcierMMap.size());
        JadeLogger.info(null, "Nombre de conjoints féminin étant sourcier : " + conjointIsSourcierFMap.size());

        // ----------------------------------------------------------------------------------------------------
        // Création des entités CSV, qui seront enregistrées en base pour une future création de population
        // ----------------------------------------------------------------------------------------------------
        Iterator<String> iteratorSourciers = sourciersMap.keySet().iterator();
        while (iteratorSourciers.hasNext()) {
            // 5 - Création des entités contribuable-sourcier, source csv
            String keySourcier = iteratorSourciers.next();
            List<AMProcessRepriseSourciersCsvLine> linesForSourcier = sourciersMap.get(keySourcier);
            if ((linesForSourcier != null) && (linesForSourcier.size() > 0)) {
                AMProcessRepriseSourciersCsvEntity csvEntity = new AMProcessRepriseSourciersCsvEntity(linesForSourcier);
                result.add(csvEntity);
            }
        }

        return result;
    }

    /**
     * Parse all lines of the file and put it in an object
     * 
     * @param allLines
     * @return
     */
    public static List<AMProcessRepriseSourciersCsvLine> parseLines(List<String> allLines) {
        List<AMProcessRepriseSourciersCsvLine> result = new ArrayList<AMProcessRepriseSourciersCsvLine>();
        // Départ à 1, ligne de titre
        for (int iLine = 1; iLine < allLines.size(); iLine++) {
            String currentLineContent = allLines.get(iLine);
            AMProcessRepriseSourciersCsvLine currentCsvLine = new AMProcessRepriseSourciersCsvLine(currentLineContent);
            result.add(currentCsvLine);
        }
        return result;
    }

    /**
     * Read a file and return a list of string
     * 
     * @param file
     * @return
     * @throws IOException
     */
    public static List<String> readFile(File file) throws IOException {

        List<String> result = new ArrayList<String>();

        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);

        for (String line = br.readLine(); line != null; line = br.readLine()) {
            result.add(line);
        }
        br.close();
        fr.close();

        return result;
    }

    /**
     * Validation des lignes
     * 
     * @param allLines
     * 
     * @return
     *         Messages d'erreur si validation a échoué
     */
    public static String validateLines(List<AMProcessRepriseSourciersCsvLine> allLines) {
        String errorMessage = "";
        int iErrorNumbers = 0;
        for (int iLine = 0; iLine < allLines.size(); iLine++) {
            AMProcessRepriseSourciersCsvLine currentLine = allLines.get(iLine);
            try {
                currentLine.validateFields();
            } catch (AMUploadFichierRepriseException ex) {
                errorMessage += ex.getMessage();
                iErrorNumbers++;
            }
        }
        return errorMessage + "\nLignes en erreur : " + iErrorNumbers;
    }

}
