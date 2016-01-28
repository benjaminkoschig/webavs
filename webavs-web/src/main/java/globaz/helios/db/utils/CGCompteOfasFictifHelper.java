package globaz.helios.db.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Insérez la description du type ici. Date de création : (16.09.2002 09:51:19)
 * 
 * @author: Administrator
 */

public class CGCompteOfasFictifHelper {

    private static List<CGCompteOfasFictif> comptesFictif = null;
    // private static List exceptions = null;
    private static CGCompteOfasFictifHelper instance = null;

    public static CGCompteOfasFictifHelper getInstance() {
        if (CGCompteOfasFictifHelper.instance == null) {
            CGCompteOfasFictifHelper.instance = new CGCompteOfasFictifHelper();
        }

        return CGCompteOfasFictifHelper.instance;
    }

    /**
     * Constructor for CGCompteOfasFictifHelper.
     */
    protected CGCompteOfasFictifHelper() {
        init();
    }

    /**
     * Method belongsToCompteFictif.
     * 
     * @param compte
     *            : format sss.cccc
     * @return true si compte passé en paramètre appartient à un compte fictif, false sinon. Si le compte est un compte
     *         fictif, retourne false.
     */
    public boolean belongsToCompteFictif(String compteOfas) {
        try {
            String C_COMPTE_FICTIF = "999";

            String secteur3 = compteOfas.substring(0, 3);
            String numeroComtpte = compteOfas.substring(4, 8);
            String classeCompte = compteOfas.substring(4, 5);

            CGCompteOfasFictif compteFictif = getCompteFictif(secteur3);
            if (compteFictif == null) {
                return false;
            }

            // est un compte fictif
            if (C_COMPTE_FICTIF.equals(numeroComtpte.substring(1, 4))) {
                return false;
            }

            List<String> listClasseCompte = compteFictif.getClassesCompte();
            List<String> listExceptions = compteFictif.getExceptionsComptes();

            if (listClasseCompte.contains(classeCompte) && !listExceptions.contains(numeroComtpte)) {
                return true;
            }

            return false;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean belongsToCompteFictif(String idSecteurAVS, String numeroCompteAVS) {
        try {
            String C_COMPTE_FICTIF = "999";

            String secteur3 = idSecteurAVS.substring(0, 3);
            String numeroComtpte = numeroCompteAVS;
            String classeCompte = numeroCompteAVS.substring(0, 1);

            CGCompteOfasFictif compteFictif = getCompteFictif(secteur3);
            if (compteFictif == null) {
                return false;
            }

            // est un compte fictif
            if (C_COMPTE_FICTIF.equals(numeroComtpte.substring(1, 4))) {
                return false;
            }

            List<String> listClasseCompte = compteFictif.getClassesCompte();
            List<String> listExceptions = compteFictif.getExceptionsComptes();

            if (listClasseCompte.contains(classeCompte) && !listExceptions.contains(numeroComtpte)) {
                return true;
            }

            return false;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    protected CGCompteOfasFictif getCompteFictif(String secteur) {
        for (Iterator<CGCompteOfasFictif> iterator = CGCompteOfasFictifHelper.comptesFictif.iterator(); iterator
                .hasNext();) {
            CGCompteOfasFictif element = iterator.next();
            if (element.getSecteur().equals(secteur)) {
                return element;
            }
        }
        return null;
    }

    protected Set<String> getSecteursFictif() {
        Set<String> result = new HashSet<String>();
        for (Iterator<CGCompteOfasFictif> iterator = CGCompteOfasFictifHelper.comptesFictif.iterator(); iterator
                .hasNext();) {
            CGCompteOfasFictif element = iterator.next();
            result.add(element.getSecteur());
        }
        return result;
    }

    private void init() {

        CGCompteOfasFictifHelper.comptesFictif = new ArrayList<CGCompteOfasFictif>();
        // CGCompteOfasFictifHelper.exceptions = new ArrayList();
        CGCompteOfasFictif compteFictif = new CGCompteOfasFictif();
        compteFictif.setSecteur("380");
        compteFictif.addClasseCompte("5");
        compteFictif.addClasseCompte("6");
        CGCompteOfasFictifHelper.comptesFictif.add(compteFictif);

        compteFictif = new CGCompteOfasFictif();
        compteFictif.setSecteur("381");
        compteFictif.addClasseCompte("5");
        compteFictif.addClasseCompte("6");
        CGCompteOfasFictifHelper.comptesFictif.add(compteFictif);

        compteFictif = new CGCompteOfasFictif();
        compteFictif.setSecteur("382");
        compteFictif.addClasseCompte("5");
        compteFictif.addClasseCompte("6");
        CGCompteOfasFictifHelper.comptesFictif.add(compteFictif);

        compteFictif = new CGCompteOfasFictif();
        compteFictif.setSecteur("383");
        compteFictif.addClasseCompte("5");
        compteFictif.addClasseCompte("6");
        CGCompteOfasFictifHelper.comptesFictif.add(compteFictif);

        compteFictif = new CGCompteOfasFictif();
        compteFictif.setSecteur("910");
        compteFictif.addClasseCompte("5");
        // TODO Modification valable que pour l'annonce OFAS annuelle
        // compteFictif.addExceptionsComptes("5172");
        // compteFictif.addExceptionsComptes("5510");
        // compteFictif.addExceptionsComptes("5511");
        // compteFictif.addExceptionsComptes("5720");
        // compteFictif.addExceptionsComptes("5830");
        // compteFictif.addExceptionsComptes("5990");
        CGCompteOfasFictifHelper.comptesFictif.add(compteFictif);

        compteFictif = new CGCompteOfasFictif();
        compteFictif.setSecteur("920");
        compteFictif.addClasseCompte("5");
        compteFictif.addClasseCompte("6");
        CGCompteOfasFictifHelper.comptesFictif.add(compteFictif);

        compteFictif = new CGCompteOfasFictif();
        compteFictif.setSecteur("930");
        compteFictif.addClasseCompte("5");
        compteFictif.addClasseCompte("6");
        CGCompteOfasFictifHelper.comptesFictif.add(compteFictif);

        compteFictif = new CGCompteOfasFictif();
        compteFictif.setSecteur("940");
        compteFictif.addClasseCompte("5");
        compteFictif.addClasseCompte("6");
        CGCompteOfasFictifHelper.comptesFictif.add(compteFictif);

        compteFictif = new CGCompteOfasFictif();
        compteFictif.setSecteur("950");
        compteFictif.addClasseCompte("7");
        compteFictif.addClasseCompte("8");
        CGCompteOfasFictifHelper.comptesFictif.add(compteFictif);
    }

}
