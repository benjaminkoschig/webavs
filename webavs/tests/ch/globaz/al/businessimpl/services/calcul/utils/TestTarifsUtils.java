package ch.globaz.al.businessimpl.services.calcul.utils;

import java.util.HashMap;
import ch.globaz.al.business.constantes.ALCSDroit;
import ch.globaz.al.business.constantes.ALCSTarif;

/**
 * Classe fournissant des méthodes utiles pour le test des tarifs
 * 
 * @author jts
 * 
 */
public class TestTarifsUtils {

    /**
     * Liste des catégories de tarif
     */
    private static HashMap<String, String> categories = null;
    /**
     * Liste des législations
     */
    private static HashMap<String, String> legislations = null;

    /**
     * Liste des type de prestations
     */
    private static HashMap<String, String> prestations = null;
    /**
     * Liste des type de résidents
     */
    private static HashMap<String, String> residents = null;

    /**
     * Récupère le code système de type de catégorie correspondant à <code>code</code>
     * 
     * @param code
     *            code à récupérer
     * @return le code système correspondant
     * 
     * @throws Exception
     *             Exception levée si <code>code</code> n'est pas valide
     */
    public static String getCSCategorie(String code) throws Exception {
        if (TestTarifsUtils.categories == null) {
            TestTarifsUtils.categories = new HashMap<String, String>();
            TestTarifsUtils.categories.put("AG", ALCSTarif.CATEGORIE_AG);
            TestTarifsUtils.categories.put("AGLS", ALCSTarif.CATEGORIE_AGLS);
            TestTarifsUtils.categories.put("AI", ALCSTarif.CATEGORIE_AI);
            TestTarifsUtils.categories.put("ALKO", ALCSTarif.CATEGORIE_ALKO);
            TestTarifsUtils.categories.put("AR", ALCSTarif.CATEGORIE_AR);
            TestTarifsUtils.categories.put("BE", ALCSTarif.CATEGORIE_BE);
            TestTarifsUtils.categories.put("BL", ALCSTarif.CATEGORIE_BL);
            TestTarifsUtils.categories.put("BS", ALCSTarif.CATEGORIE_BS);
            TestTarifsUtils.categories.put("CCJU", ALCSTarif.CATEGORIE_CCJU);
            TestTarifsUtils.categories.put("CCVD", ALCSTarif.CATEGORIE_CCVD);
            TestTarifsUtils.categories.put("CICI", ALCSTarif.CATEGORIE_CICI);
            TestTarifsUtils.categories.put("CVCI", ALCSTarif.CATEGORIE_CVCI);
            TestTarifsUtils.categories.put("FACO", ALCSTarif.CATEGORIE_FACO);
            TestTarifsUtils.categories.put("FED", ALCSTarif.CATEGORIE_FED);
            TestTarifsUtils.categories.put("FR", ALCSTarif.CATEGORIE_FR);
            TestTarifsUtils.categories.put("GE", ALCSTarif.CATEGORIE_GE);
            TestTarifsUtils.categories.put("GL", ALCSTarif.CATEGORIE_GL);
            TestTarifsUtils.categories.put("GR", ALCSTarif.CATEGORIE_GR);
            TestTarifsUtils.categories.put("H510", ALCSTarif.CATEGORIE_H510);
            TestTarifsUtils.categories.put("H513", ALCSTarif.CATEGORIE_H513);
            TestTarifsUtils.categories.put("H514", ALCSTarif.CATEGORIE_H514);
            TestTarifsUtils.categories.put("H515", ALCSTarif.CATEGORIE_H515);
            TestTarifsUtils.categories.put("H517", ALCSTarif.CATEGORIE_H517);
            TestTarifsUtils.categories.put("H51X", ALCSTarif.CATEGORIE_H51X);
            TestTarifsUtils.categories.put("JU", ALCSTarif.CATEGORIE_JU);
            TestTarifsUtils.categories.put("LFM", ALCSTarif.CATEGORIE_LFM);
            TestTarifsUtils.categories.put("LFP", ALCSTarif.CATEGORIE_LFP);
            TestTarifsUtils.categories.put("LJU", ALCSTarif.CATEGORIE_LJU);
            TestTarifsUtils.categories.put("LU", ALCSTarif.CATEGORIE_LU);
            TestTarifsUtils.categories.put("NE", ALCSTarif.CATEGORIE_NE);
            TestTarifsUtils.categories.put("NW", ALCSTarif.CATEGORIE_NW);
            TestTarifsUtils.categories.put("OW", ALCSTarif.CATEGORIE_OW);
            TestTarifsUtils.categories.put("SG", ALCSTarif.CATEGORIE_SG);
            TestTarifsUtils.categories.put("SH", ALCSTarif.CATEGORIE_SH);
            TestTarifsUtils.categories.put("SO", ALCSTarif.CATEGORIE_SO);
            TestTarifsUtils.categories.put("SZ", ALCSTarif.CATEGORIE_SZ);
            TestTarifsUtils.categories.put("TG", ALCSTarif.CATEGORIE_TG);
            TestTarifsUtils.categories.put("TI", ALCSTarif.CATEGORIE_TI);
            TestTarifsUtils.categories.put("UR", ALCSTarif.CATEGORIE_UR);
            TestTarifsUtils.categories.put("VD", ALCSTarif.CATEGORIE_VD);
            TestTarifsUtils.categories.put("VD_DROIT_ACQUIS", ALCSTarif.CATEGORIE_VD_DROIT_ACQUIS);
            TestTarifsUtils.categories.put("VS", ALCSTarif.CATEGORIE_VS);
            TestTarifsUtils.categories.put("ZG", ALCSTarif.CATEGORIE_ZG);
            TestTarifsUtils.categories.put("ZH", ALCSTarif.CATEGORIE_ZH);
            TestTarifsUtils.categories.put("SUP_HORLO", ALCSTarif.CATEGORIE_SUP_HORLO);

            TestTarifsUtils.categories.put("FPV_IAV", ALCSTarif.CATEGORIE_FPV_IAV);
            TestTarifsUtils.categories.put("FPV_GAR", ALCSTarif.CATEGORIE_FPV_GAR);
            TestTarifsUtils.categories.put("FPV_GEOM", ALCSTarif.CATEGORIE_FPV_GEOM);
            TestTarifsUtils.categories.put("FPV_LIBR", ALCSTarif.CATEGORIE_FPV_LIBR);
            TestTarifsUtils.categories.put("FPV_AT", ALCSTarif.CATEGORIE_FPV_AT);
            TestTarifsUtils.categories.put("FPV_NOT", ALCSTarif.CATEGORIE_FPV_NOT);
            TestTarifsUtils.categories.put("FPV_AGA", ALCSTarif.CATEGORIE_FPV_AGA);
            TestTarifsUtils.categories.put("FPV_AFIT", ALCSTarif.CATEGORIE_FPV_AFIT);

            TestTarifsUtils.categories.put("FPV_VISANA_AG", ALCSTarif.CATEGORIE_FPV_VISANA_AG);
            TestTarifsUtils.categories.put("FPV_VISANA_AI", ALCSTarif.CATEGORIE_FPV_VISANA_AI);
            TestTarifsUtils.categories.put("FPV_VISANA_AR", ALCSTarif.CATEGORIE_FPV_VISANA_AR);
            TestTarifsUtils.categories.put("FPV_VISANA_BE", ALCSTarif.CATEGORIE_FPV_VISANA_BE);
            TestTarifsUtils.categories.put("FPV_VISANA_BL", ALCSTarif.CATEGORIE_FPV_VISANA_BL);
            TestTarifsUtils.categories.put("FPV_VISANA_BS", ALCSTarif.CATEGORIE_FPV_VISANA_BS);
            TestTarifsUtils.categories.put("FPV_VISANA_FR", ALCSTarif.CATEGORIE_FPV_VISANA_FR);
            TestTarifsUtils.categories.put("FPV_VISANA_GE", ALCSTarif.CATEGORIE_FPV_VISANA_GE);
            TestTarifsUtils.categories.put("FPV_VISANA_GL", ALCSTarif.CATEGORIE_FPV_VISANA_GL);
            TestTarifsUtils.categories.put("FPV_VISANA_GR", ALCSTarif.CATEGORIE_FPV_VISANA_GR);
            TestTarifsUtils.categories.put("FPV_VISANA_JU", ALCSTarif.CATEGORIE_FPV_VISANA_JU);
            TestTarifsUtils.categories.put("FPV_VISANA_LU", ALCSTarif.CATEGORIE_FPV_VISANA_LU);
            TestTarifsUtils.categories.put("FPV_VISANA_NE", ALCSTarif.CATEGORIE_FPV_VISANA_NE);
            TestTarifsUtils.categories.put("FPV_VISANA_NW", ALCSTarif.CATEGORIE_FPV_VISANA_NW);
            TestTarifsUtils.categories.put("FPV_VISANA_OW", ALCSTarif.CATEGORIE_FPV_VISANA_OW);
            TestTarifsUtils.categories.put("FPV_VISANA_SG", ALCSTarif.CATEGORIE_FPV_VISANA_SG);
            TestTarifsUtils.categories.put("FPV_VISANA_SH", ALCSTarif.CATEGORIE_FPV_VISANA_SH);
            TestTarifsUtils.categories.put("FPV_VISANA_SO", ALCSTarif.CATEGORIE_FPV_VISANA_SO);
            TestTarifsUtils.categories.put("FPV_VISANA_SZ", ALCSTarif.CATEGORIE_FPV_VISANA_SZ);
            TestTarifsUtils.categories.put("FPV_VISANA_TG", ALCSTarif.CATEGORIE_FPV_VISANA_TG);
            TestTarifsUtils.categories.put("FPV_VISANA_TI", ALCSTarif.CATEGORIE_FPV_VISANA_TI);
            TestTarifsUtils.categories.put("FPV_VISANA_UR", ALCSTarif.CATEGORIE_FPV_VISANA_UR);
            TestTarifsUtils.categories.put("FPV_VISANA_VD", ALCSTarif.CATEGORIE_FPV_VISANA_VD);
            TestTarifsUtils.categories.put("FPV_VISANA_VS", ALCSTarif.CATEGORIE_FPV_VISANA_VS);
            TestTarifsUtils.categories.put("FPV_VISANA_ZG", ALCSTarif.CATEGORIE_FPV_VISANA_ZG);
            TestTarifsUtils.categories.put("FPV_VISANA_ZH", ALCSTarif.CATEGORIE_FPV_VISANA_ZH);
        }

        String res = TestTarifsUtils.categories.get(code);

        if (res == null) {
            throw new Exception("TestCalculUtils#getCSLegislation : " + code + " is not valid");
        }

        return res;
    }

    /**
     * Récupère le code système de type de législation correspondant à <code>code</code>
     * 
     * @param code
     *            code à récupérer
     * @return le code système correspondant
     * 
     * @throws Exception
     *             Exception levée si <code>code</code> n'est pas valide
     */
    public static String getCSLegislation(String code) throws Exception {
        if (TestTarifsUtils.legislations == null) {
            TestTarifsUtils.legislations = new HashMap<String, String>();

            TestTarifsUtils.legislations.put("AGRICOLE", ALCSTarif.LEGISLATION_AGRICOLE);
            TestTarifsUtils.legislations.put("CAISSE", ALCSTarif.LEGISLATION_CAISSE);
            TestTarifsUtils.legislations.put("CANTONAL", ALCSTarif.LEGISLATION_CANTONAL);
            TestTarifsUtils.legislations.put("FEDERAL", ALCSTarif.LEGISLATION_FEDERAL);
        }

        String res = TestTarifsUtils.legislations.get(code);

        if (res == null) {
            throw new Exception("TestCalculUtils#getCSLegislation : " + code + " is not valid");
        }

        return res;
    }

    /**
     * Récupère le code système de type de prestation correspondant à <code>code</code>
     * 
     * @param code
     *            code à récupérer
     * @return le code système correspondant
     * 
     * @throws Exception
     *             Exception levée si <code>code</code> n'est pas valide
     */
    public static String getCSPrestations(String code) throws Exception {
        if (TestTarifsUtils.prestations == null) {
            TestTarifsUtils.prestations = new HashMap<String, String>();

            TestTarifsUtils.prestations.put("ACCE", ALCSDroit.TYPE_ACCE);
            TestTarifsUtils.prestations.put("ENF", ALCSDroit.TYPE_ENF);
            TestTarifsUtils.prestations.put("FORM", ALCSDroit.TYPE_FORM);
            TestTarifsUtils.prestations.put("MEN", ALCSDroit.TYPE_MEN);
            TestTarifsUtils.prestations.put("NAIS", ALCSDroit.TYPE_NAIS);
        }

        String res = TestTarifsUtils.prestations.get(code);

        if (res == null) {
            throw new Exception("TestCalculUtils#getCSLegislation : " + code + " is not valid");
        }

        return res;
    }

    /**
     * Récupère le code système de type de résident correspondant à <code>code</code>
     * 
     * @param code
     *            code à récupérer
     * @return le code système correspondant
     * 
     * @throws Exception
     *             Exception levée si <code>code</code> n'est pas valide
     */
    public static String getCSResident(String code) throws Exception {
        if (TestTarifsUtils.residents == null) {
            TestTarifsUtils.residents = new HashMap<String, String>();

            TestTarifsUtils.residents.put("CH", ALCSTarif.RESIDENT_CH);
            TestTarifsUtils.residents.put("ETR", ALCSTarif.RESIDENT_ETR);
            TestTarifsUtils.residents.put("F", ALCSTarif.RESIDENT_F);
        }

        String res = TestTarifsUtils.residents.get(code);

        if (res == null) {
            throw new Exception("TestCalculUtils#getCSResident : " + code + " is not valid");
        }

        return res;
    }
}