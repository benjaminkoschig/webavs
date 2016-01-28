package globaz.helios.db.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Insérez la description du type ici. Date de création : (16.09.2002 09:51:19)
 * 
 * @author: Administrator
 */

public class CGCompteOfasFictifAdminInvestmentHelper {

    private static List comptesFictif = null;
    private static CGCompteOfasFictifAdminInvestmentHelper instance = null;

    public static CGCompteOfasFictifAdminInvestmentHelper getInstance() {
        if (instance == null) {
            instance = new CGCompteOfasFictifAdminInvestmentHelper();
        }

        return instance;
    }

    /**
     * Constructor for CGCompteOfasFictifHelper.
     */
    private CGCompteOfasFictifAdminInvestmentHelper() {
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

            String secteur1 = compteOfas.substring(0, 1);
            String numeroComtpte = compteOfas.substring(4, 8);
            String classeCompte = compteOfas.substring(4, 5);

            CGCompteOfasFictif compteFictif = getCompteFictif(secteur1);
            if (compteFictif == null) {
                return false;
            }

            // est un compte fictif

            if (C_COMPTE_FICTIF.equals(numeroComtpte.substring(1, 4))) {
                return false;
            }

            List listClasseCompte = compteFictif.getClassesCompte();
            if (listClasseCompte.contains(classeCompte)) {
                return true;
            }

            return false;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    protected CGCompteOfasFictif getCompteFictif(String secteur) {
        for (Iterator iterator = comptesFictif.iterator(); iterator.hasNext();) {
            CGCompteOfasFictif element = (CGCompteOfasFictif) iterator.next();
            if (element.getSecteur().equals(secteur)) {
                return element;
            }
        }
        return null;
    }

    private void init() {

        comptesFictif = new ArrayList();
        CGCompteOfasFictif compteFictif = new CGCompteOfasFictif();
        compteFictif.setSecteur("4");
        compteFictif.addClasseCompte("5");
        compteFictif.addClasseCompte("6");
        compteFictif.addClasseCompte("7");
        compteFictif.addClasseCompte("8");
        comptesFictif.add(compteFictif);

        compteFictif = new CGCompteOfasFictif();
        compteFictif.setSecteur("5");
        compteFictif.addClasseCompte("5");
        compteFictif.addClasseCompte("6");
        compteFictif.addClasseCompte("7");
        compteFictif.addClasseCompte("8");
        comptesFictif.add(compteFictif);

        compteFictif = new CGCompteOfasFictif();
        compteFictif.setSecteur("6");
        compteFictif.addClasseCompte("5");
        compteFictif.addClasseCompte("6");
        compteFictif.addClasseCompte("7");
        compteFictif.addClasseCompte("8");
        comptesFictif.add(compteFictif);

        compteFictif = new CGCompteOfasFictif();
        compteFictif.setSecteur("7");
        compteFictif.addClasseCompte("5");
        compteFictif.addClasseCompte("6");
        compteFictif.addClasseCompte("7");
        compteFictif.addClasseCompte("8");
        comptesFictif.add(compteFictif);

        compteFictif = new CGCompteOfasFictif();
        compteFictif.setSecteur("8");
        compteFictif.addClasseCompte("5");
        compteFictif.addClasseCompte("6");
        compteFictif.addClasseCompte("7");
        compteFictif.addClasseCompte("8");
        comptesFictif.add(compteFictif);
    }

}
