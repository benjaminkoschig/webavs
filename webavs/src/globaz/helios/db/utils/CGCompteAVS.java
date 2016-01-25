package globaz.helios.db.utils;

/**
 * Insérez la description du type ici. Date de création : (16.09.2002 09:51:19)
 * 
 * @author: Administrator
 */

import globaz.jade.client.util.JadeStringUtil;

public class CGCompteAVS {
    public final static String COMPTE_CLOTURE = "9210";
    public final static String COMPTE_OUVERTURE = "9200";

    /**
     * verifie le format d'un compte le format doit être du style : nnnn.nnnn.nnnn ou n est un caractère numeric de 0 à
     * 9.
     * 
     * Date de création : (16.09.2002 09:56:48)
     * 
     * @return boolean
     * @param compte
     *            java.lang.String
     */
    public static boolean hasValidFormat(String value) {
        CGCompteAVS c = new CGCompteAVS(value);

        return c.isValid;
    }

    private String compte = "";
    private boolean isValid = false;

    private String part1 = "";

    private String part2 = "";
    private String part3 = "";

    private String result = new String(); // ne doit pas être null

    /**
     * Commentaire relatif au constructeur CGCompteAVS.
     */
    public CGCompteAVS(String compte) {
        super();
        this.compte = compte;
        isValid = split();
    }

    public String getClasse() {

        if (hasValidFormat()) {
            result = part2.substring(0, 2);
        }
        return result;
    }

    public String getClasse3() {

        if (hasValidFormat()) {
            result = part2.substring(0, 3);
        }
        return result;
    }

    public String getClasseSecteur() {

        if (hasValidFormat()) {
            result = part1 + "." + part2;
        }
        return result;
    }

    public String getCompte() {

        if (hasValidFormat()) {
            result = part2;
        }
        return result;
    }

    public String getCompteOFAS() {

        if (hasValidFormat()) {
            result = part1.substring(0, 3) + part2;

        }
        return result;
    }

    public String getCompteOFASFormat() {

        if (hasValidFormat()) {
            result = part1.substring(0, 3) + "." + part2;

        }
        return result;
    }

    public String getFullCompteFormat() {

        if (hasValidFormat()) {
            result = compte;

        }
        return result;
    }

    public String getFullCompteNoFormat() {

        if (hasValidFormat()) {
            result = part1 + part2 + part3;

        }
        return result;
    }

    public String getGenre() {

        if (hasValidFormat()) {
            result = part2.substring(0, 1);
        }
        return result;
    }

    public String getSecteur1() {

        if (hasValidFormat()) {
            result = part1.substring(0, 1);
        }
        return result;
    }

    public String getSecteur3() {

        if (hasValidFormat()) {
            result = part1.substring(0, 3);
        }
        return result;
    }

    public String getSecteur4() {

        if (hasValidFormat()) {
            result = part1.substring(0, 4);
        }
        return result;
    }

    public String getSousCompte() {

        if (hasValidFormat()) {
            result = part3;
        }
        return result;
    }

    /**
     * verifie le format d'un compte le format doit être du style : nnnn.nnnn.nnnn ou n est un caractère numeric de 0 à
     * 9.
     * 
     * Date de création : (16.09.2002 09:56:48)
     * 
     * @return boolean
     * @param compte
     *            java.lang.String
     */
    public boolean hasValidFormat() {
        return isValid;
    }

    public boolean isCompteActif() {

        return (getGenre().equals("1"));
    }

    public boolean isCompteAdministration() {

        return (isCompteChargeAdministration() || isCompteProduitAdministration() || isCompteResultatAdministration());
    }

    public boolean isCompteAffilies() {

        return (getClasse3().equals("110"));
    }

    public boolean isCompteAvoir() {

        return (getClasse().equals("12"));
    }

    public boolean isCompteBilan() {

        return (isComptePassif() || isCompteCloture() || isCompteActif() || isCompteOuverture());
    }

    public boolean isCompteChargeAdministration() {

        return (getGenre().equals("5"));
    }

    public boolean isCompteCloture() {

        return (getCompte().equals(COMPTE_CLOTURE));
    }

    public boolean isCompteDepenseExploitation() {

        return (getGenre().equals("3"));
    }

    public boolean isCompteDepenseInvestissement() {

        return (getGenre().equals("7"));
    }

    public boolean isCompteDette() {

        return (getClasse().equals("21"));
    }

    public boolean isCompteExploitation() {

        return (isCompteRecetteExploitation() || isCompteDepenseExploitation() || isCompteResultatExploitation());
    }

    public boolean isCompteInvestissement() {

        return (isCompteRecetteInvestissement() || isCompteDepenseInvestissement());
    }

    public boolean isCompteOuverture() {

        return (getCompte().equals(COMPTE_OUVERTURE));
    }

    public boolean isComptePassif() {

        return (getGenre().equals("2"));
    }

    public boolean isCompteProduitAdministration() {

        return (getGenre().equals("6"));
    }

    public boolean isCompteRecetteExploitation() {

        return (getGenre().equals("4"));
    }

    public boolean isCompteRecetteInvestissement() {

        return (getGenre().equals("8"));
    }

    public boolean isCompteResultat() {

        return (isCompteResultatAdministration() || isCompteResultatExploitation());
    }

    public boolean isCompteResultatAdministration() {

        return (getClasse().equals("91"));
    }

    public boolean isCompteResultatExploitation() {

        return (getClasse().equals("90"));
    }

    public boolean isSecteurAutreTache() {

        char c;
        c = getSecteur1().charAt(0);

        switch (c) {

            case '5':
            case '6':
            case '7':
            case '8':

                return true;

            default:
                return false;
        }

    }

    public boolean isSecteurCAF() {

        return (getSecteur1().equals("5"));

    }

    public boolean isSecteurCAFAgence() {

        try {
            int secteur = Integer.parseInt(getSecteur3());
            if ((secteur >= 550) && (secteur <= 589)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public boolean isSecteurCAFGestionPropre() {

        try {
            int secteur = Integer.parseInt(getSecteur3());
            if ((secteur >= 500) && (secteur <= 549)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public boolean isSecteurTacheFederale() {

        char c;
        c = getSecteur1().charAt(0);

        switch (c) {

            case '1':
            case '2':
            case '3':
            case '4':
            case '9':

                return true;

            default:
                return false;
        }

    }

    /**
     * verifie le format d'un compte le format doit être du style : nnnn.nnnn.nnnn ou n est un caractère numeric de 0 à
     * 9.
     * 
     * Date de création : (16.09.2002 09:56:48)
     * 
     * @return boolean
     * @param compte
     *            java.lang.String
     */
    public boolean split() {

        if (JadeStringUtil.isBlank(compte)) {
            return false;
        }

        if (compte.length() != 14) {
            return false;
        }
        // ####.####.####
        if ((compte.charAt(4) != '.') || (compte.charAt(9) != '.')) {
            return false;
        }
        part1 = compte.substring(0, 4);
        part2 = compte.substring(5, 9);
        part3 = compte.substring(10);

        try {
            Integer.parseInt(part1);
            Integer.parseInt(part2);
            Integer.parseInt(part3);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        if (part1.charAt(0) == '0') {
            return false;
        }

        // #nnnn.nnnn.nnnn
        return true;
    }
}
