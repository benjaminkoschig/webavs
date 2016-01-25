package globaz.hermes.db.parametrage;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.hermes.application.HEApplication;
import globaz.hermes.db.gestion.HEAnnoncesViewBean;
import globaz.hermes.utils.DateUtils;
import globaz.hermes.utils.StringUtils;
import java.util.Arrays;

/**
 * Insérez la description du type ici. Date de création : (21.01.2003 15:01:09)
 * 
 * @author: Administrator
 */
public class HEAttenteRetourChampsOptimizedViewBean extends BEntity implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Lance l'application.
     * 
     * @param args
     *            un tableau d'arguments de ligne de commande
     */
    public static void main(java.lang.String[] args) {
        // Insérez ici le code de démarrage de l'application
    }

    // //////
    protected String debut = "";
    protected String enregistrement = "";
    protected String idAnnonce = ""; // IDANNONCE
    protected String idChamp = ""; // RDTCHA
    protected String libelleAnnonce = "";
    protected String libelleChamp = ""; // PCOLUT
    protected String longueur = "";
    protected String message = "";
    protected String messageFormatted = "";
    protected String paramAnnonce = ""; // REIPAE
    protected String statut = ""; // RNTSTA

    protected String valeur = ""; // VALEUR

    /**
     * Commentaire relatif au constructeur HEAttenteRetourChampsOptimizedViewBean.
     */
    public HEAttenteRetourChampsOptimizedViewBean() {
        super();
    }

    /**
     * Renvoie le nom de la table
     * 
     * @return le nom de la table
     */
    @Override
    protected String _getTableName() {
        return null;
    }

    /**
     * Lit les valeurs des propriétés propres de l'entité à partir de la base de données
     * 
     * @exception java.lang.Exception
     *                si la lecture des propriétés a échouée
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idAnnonce = statement.dbReadNumeric("IDANNONCE"); // IDANNONCE
        valeur = statement.dbReadString("VALEUR"); // VALEUR
        paramAnnonce = statement.dbReadNumeric("REIPAE"); // REIPAE
        idChamp = statement.dbReadNumeric("RDTCHA"); // RDTCHA
        libelleChamp = statement.dbReadString("PCOLUT"); // PCOLUT
        statut = statement.dbReadNumeric("RNTSTA"); // RNTSTA
        //
        debut = "" + (Integer.parseInt(statement.dbReadNumeric("RDNDEB")) - 1);
        longueur = statement.dbReadNumeric("RDNLON");
        message = statement.dbReadNumeric("MESSAGE");
        // enregistrement = st;
    }

    /**
     * Valide le contenu de l'entité (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    /**
     * Sauvegarde les valeurs des propriétés propres de l'entité composant la clé primaire
     * 
     * @exception java.lang.Exception
     *                si la sauvegarde des propriétés a échouée
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
    }

    /**
     * Sauvegarde les valeurs des propriétés propres de l'entité dans la base de données
     * 
     * @param statement
     *            l'instruction à utiliser
     * @exception java.lang.Exception
     *                si la sauvegarde des propriétés a échouée
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.01.2003 16:10:56)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdAnnonce() {
        return idAnnonce;
    }

    /**
     * Renvoit le code système du champ 118xxx
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdChamp() {
        return idChamp;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (25.03.2003 14:12:50)
     * 
     * @return java.lang.String
     */
    public java.lang.String getLibelleAnnonce() {
        return libelleAnnonce;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.01.2003 16:10:56)
     * 
     * @return java.lang.String
     */
    public java.lang.String getLibelleChamp() {
        return libelleChamp;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.01.2003 08:43:33)
     * 
     * @return java.lang.String
     */
    public java.lang.String getLongueur() {
        return longueur;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.01.2003 08:43:33)
     * 
     * @return java.lang.String
     */
    public java.lang.String getLongueur(int addedWidth) {
        return "" + (Integer.parseInt(longueur) + addedWidth);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.01.2003 08:43:33)
     * 
     * @return java.lang.String
     */
    public java.lang.String getLongueur(String key) {
        if (HEAnnoncesViewBean.isDate_MMAA(key)) { // date sur 4, on rajoute le
            // .
            return "5";
        }
        return longueur;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.01.2003 10:00:02)
     * 
     * @return java.lang.String
     */
    public java.lang.String getMessageRetour() {
        if (message.length() == 1) {
            return "";
        } else {
            try {
                FWParametersSystemCodeManager csMessage = ((HEApplication) getSession().getApplication())
                        .getCsMessageListe(getSession());
            } catch (Exception e) {
                e.printStackTrace();
                return "error reading message " + e.toString();
            }
        }
        return message;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.01.2003 16:10:56)
     * 
     * @return java.lang.String
     */
    public java.lang.String getParamAnnonce() {
        return paramAnnonce;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.01.2003 16:10:56)
     * 
     * @return java.lang.String
     */
    public java.lang.String getStatut() {
        return statut;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.01.2003 16:10:56)
     * 
     * @return java.lang.String
     */
    public java.lang.String getValeur() {
        return valeur;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.01.2003 16:10:56)
     * 
     * @return java.lang.String
     */
    public java.lang.String getValeur(String key) {
        try {
            if (HEAnnoncesViewBean.isDate_MMAA(key)) { // date sur 4, on rajoute
                // le .
                return DateUtils.convertDate(valeur, DateUtils.MMAA, DateUtils.MMAA_DOTS);
            } else if (HEAnnoncesViewBean.isCurrencyField(key)) {
                return globaz.globall.util.JANumberFormatter.fmt(valeur, true, true, true, 2);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return valeur;
        }
        return valeur;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.01.2003 16:10:56)
     * 
     * @return java.lang.String
     */
    public java.lang.String getValeur(String key, int index) {
        if (HEAnnoncesViewBean.isCustomField(key)) {
            if (valeur.trim().length() == 0) {
                return "";
            }
            valeur = StringUtils.padAfterString(valeur, " ", 9);
            switch (index) {
                case 0: {
                    return valeur.substring(0, 4);
                }
                case 1: {
                    return valeur.substring(4, 8);
                }
                default: {
                    return "";
                }
            }
        } else {
            return valeur;
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.01.2003 08:43:33)
     * 
     * @return java.lang.String
     */
    public boolean isHidden() {
        return (Arrays.asList(HEAnnoncesViewBean.hiddenFields).contains(idChamp));
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.01.2003 08:43:33)
     * 
     * @return java.lang.String
     */
    public boolean isReadOnly() {
        return (Arrays.asList(HEAnnoncesViewBean.forbiddenFields).contains(idChamp));
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.01.2003 16:10:56)
     * 
     * @return java.lang.String
     */
    public String isSelected(String key, String value) {
        // valeur et pas de chiffre clef
        // valeur et chiffre clef
        // pas de valeur
        if (HEAnnoncesViewBean.isCustomField(key)) {
            if (valeur.trim().length() == 8) { // la valeur existe et n'a pas de
                // chiffre clef
                // c'est un blanc
                if (value.equals("")) {
                    return "selected";
                }
            } else if (valeur.trim().length() == 9) { // la valeur existe et y'a
                // un chiffre clef
                // y'a un chiffre clef
                String chiffreClef = valeur.substring(8, 9);
                if (chiffreClef.equals(value)) {
                    return "selected";
                } else {
                    return "";
                }
            } else if (valeur.trim().length() == 0) { // pas de valeur
                if (value.equals("")) {
                    return "selected";
                }
            }
        }
        return "";
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (25.03.2003 14:12:50)
     * 
     * @param newLibelleAnnonce
     *            java.lang.String
     */
    public void setLibelleAnnonce(java.lang.String newLibelleAnnonce) {
        libelleAnnonce = newLibelleAnnonce;
    }

    /**
     * Renvoie une chaîne correspondant à la valeur de cet objet.
     * 
     * @return une représentation sous forme de chaîne du destinataire
     */
    public String toMyString() {
        // Insérez ici le code pour finaliser le destinataire
        // Cette implémentation transmet le message au super. Vous pouvez
        // remplacer ou compléter le message.
        return idAnnonce + "-" + valeur + "-" + paramAnnonce + "-" + idChamp + "-" + libelleChamp + "-" + statut;
    }
}
