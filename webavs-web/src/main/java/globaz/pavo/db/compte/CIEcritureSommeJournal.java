/*
 * Créé le 5 juil. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.pavo.db.compte;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazServer;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pavo.application.CIApplication;
import globaz.pavo.util.CIUtil;

/**
 * @author jmc
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CIEcritureSommeJournal extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static String padAfterString(String stringToPad, String padString, int length) {
        while (stringToPad.length() < length) {
            stringToPad = stringToPad + padString;
        }
        return stringToPad;
    }

    public static String padBeforeString(String stringToPad, String padString, int length) {
        while (stringToPad.length() < length) {
            stringToPad = padString + stringToPad;
        }
        return stringToPad;
    }

    private Boolean avecEcrituresNegatives = new Boolean(false);
    private Boolean ecrituresSalariees = new Boolean(false);
    private String forAnneeCotisation;
    private String forCode;
    private String forCodeSpecial;
    private String forExtourne;
    private String forGenre;
    private String forGenreEcrituresAParser;
    private String forGenreParse = new String();
    private java.lang.String forIdTypeCompte;
    private java.lang.String forIdTypeJournal;
    private String forItTypeJournalMultiple;
    private java.lang.String forNomEspion;
    private String forNotExtourne;
    private java.lang.String forNumeroAffilie;
    private String forParticulier;
    private java.lang.String fromAnneeCotisation;
    private java.lang.String fromDateInscription;
    private java.lang.String fromNumeroAffilie;
    private java.lang.String fromNumeroAvs;
    private java.lang.String fromNumeroJournal;
    private String isBta = new String();
    private String Secure = new String();
    private java.lang.String toAnneeCotisation;
    private java.lang.String toDateInscription;
    private java.lang.String toNumeroAvs;

    private java.lang.String toNumeroJournal;

    private java.lang.String tri;

    /**
	 * 
	 */
    public CIEcritureSommeJournal() {
        super();
        // TODO Raccord de constructeur auto-généré
    }

    public String getForCodeSpecial() {
        return forCodeSpecial;
    }

    public void setForCodeSpecial(String forCodeSpecial) {
        this.forCodeSpecial = forCodeSpecial;
    }

    @Override
    protected String _getFrom(BStatement statement) {
        String joinStr = "";
        joinStr += " LEFT OUTER JOIN " + _getCollection() + "CIJOURP ON " + _getCollection() + "CIECRIP.KCID="
                + _getCollection() + "CIJOURP.KCID ";
        // CI
        joinStr += "LEFT OUTER JOIN " + _getCollection() + "CIINDIP ON " + _getCollection() + "CIECRIP.KAIIND="
                + _getCollection() + "CIINDIP.KAIIND ";
        // Sur les affiliés
        joinStr += "LEFT OUTER JOIN " + _getCollection() + "AFAFFIP ON " + _getCollection() + "CIECRIP.KBITIE="
                + _getCollection() + "AFAFFIP.MAIAFF ";
        // Sur les tiers
        joinStr += "LEFT OUTER JOIN " + _getCollection() + "TITIERP ON " + _getCollection() + "AFAFFIP.HTITIE="
                + _getCollection() + "TITIERP.HTITIE ";
        String firstUnion = " (select KBIECR,KBMMON from " + _getCollection() + _getTableName();
        String secondUnion = " AND KBTEXT IN (0,311006,311002,311008) union select KBIECR,-KBMMON AS KBMMON from "
                + _getCollection() + _getTableName();
        String endUnion = " AND KBTEXT IN (311001,311003,311004,311005,311007,311009)) as RESULT";
        // Récupération du CI, du tiers/partenaire
        // Obligation de renommée la table CIINDIP pour requête sur le
        // partenaire
        String sqlWhere = "";
        // Les jointures
        // Avec les journeaux
        // sqlWhere = _getCollection() + "CIJOURP.KCID=" + _getCollection() +
        // "CIECRIP.KCID";
        // Avec les comptes individuels
        // sqlWhere += " AND " + _getCollection() + "CIINDIP.KAIIND=" +
        // _getCollection() + "CIECRIP.KAIIND";
        // Test sur le numéro d'affilié

        if (!JadeStringUtil.isBlankOrZero(getForCodeSpecial())) {

            sqlWhere += " AND " + _getCollection() + "CIECRIP.KBTSPE = "
                    + this._dbWriteNumeric(statement.getTransaction(), getForCodeSpecial());
        }

        if (!globaz.globall.util.JAUtil.isStringEmpty(forNumeroAffilie)) {
            // recherche de l'id
            try {
                CIApplication app = (CIApplication) GlobazServer.getCurrentSystem().getApplication(
                        CIApplication.DEFAULT_APPLICATION_PAVO);
                String in = app.getAffiliesByNo(getSession(), forNumeroAffilie);
                if (in.length() > 0) {
                    sqlWhere += " AND " + _getCollection() + "CIECRIP.KBITIE IN (" + in + ") ";
                } else {
                    // recherche vide
                    sqlWhere += " AND " + _getCollection() + "CIECRIP.KBITIE=0 ";
                }
            } catch (Exception e) {
                // recherche vide
                sqlWhere += " AND " + _getCollection() + "CIECRIP.KBITIE=0 "; // laisser
                // tel
                // quel
            }
        }
        // Test sur le numero AVS
        if ((!globaz.globall.util.JAUtil.isStringEmpty(fromNumeroAvs))
                && (!globaz.globall.util.JAUtil.isStringEmpty(toNumeroAvs))) {
            // Padding
            String fromNumeroAvsPadded = padAfterString(fromNumeroAvs, "0", 11);
            String toNumeroAvsPadded = padAfterString(toNumeroAvs, "9", 11);
            // Si from et to sont égaux
            if (fromNumeroAvs.equals(toNumeroAvs)) {
                sqlWhere += " AND " + _getCollection() + "CIINDIP.KANAVS='" + fromNumeroAvsPadded + "'";
            } else {
                sqlWhere += " AND " + _getCollection() + "CIINDIP.KANAVS>='" + fromNumeroAvsPadded + "' AND "
                        + _getCollection() + "CIINDIP.KANAVS<='" + toNumeroAvsPadded + "'";
            }
        }
        // Seul from est non vide
        else if (!globaz.globall.util.JAUtil.isStringEmpty(fromNumeroAvs)) {
            // Padding
            String fromNumeroAvsPadded = padAfterString(fromNumeroAvs, "0", 11);
            sqlWhere += " AND " + _getCollection() + "CIINDIP.KANAVS>='" + fromNumeroAvsPadded + "'";
        }
        // Seul to est non vide
        else if (!globaz.globall.util.JAUtil.isStringEmpty(toNumeroAvs)) {
            // Padding
            String toNumeroAvsPadded = padAfterString(toNumeroAvs, "9", 11);
            sqlWhere += " AND " + _getCollection() + "CIINDIP.KANAVS<='" + toNumeroAvsPadded + "'";
        }
        // Test sur le numéro du journal
        if ((!globaz.globall.util.JAUtil.isStringEmpty(fromNumeroJournal))
                && (!globaz.globall.util.JAUtil.isStringEmpty(toNumeroJournal))) {
            // Si from et to sont égaux
            if (fromNumeroJournal.equals(toNumeroJournal)) {
                sqlWhere += " AND " + _getCollection() + "CIECRIP.KCID=" + fromNumeroJournal;
            } else {
                sqlWhere += " AND " + _getCollection() + "CIECRIP.KCID>=" + fromNumeroJournal + " AND "
                        + _getCollection() + "CIECRIP.KCID<=" + toNumeroJournal;
            }
        }
        // Seul from est non vide
        else if (!globaz.globall.util.JAUtil.isStringEmpty(fromNumeroJournal)) {
            sqlWhere += " AND " + _getCollection() + "CIECRIP.KCID>=" + fromNumeroJournal;
        } else if (!globaz.globall.util.JAUtil.isStringEmpty(toNumeroJournal)) {
            sqlWhere += " AND " + _getCollection() + "CIECRIP.KCID<=" + toNumeroJournal;
        }
        // Test sur l'année de cotisation
        if ((!globaz.globall.util.JAUtil.isStringEmpty(fromAnneeCotisation))
                && (!globaz.globall.util.JAUtil.isStringEmpty(toAnneeCotisation))) {
            // Si from et to sont égaux
            if (fromAnneeCotisation.equals(toAnneeCotisation)) {
                sqlWhere += " AND " + _getCollection() + "CIECRIP.KBNANN=" + fromAnneeCotisation;
            } else {
                sqlWhere += " AND " + _getCollection() + "CIECRIP.KBNANN>=" + fromAnneeCotisation + " AND "
                        + _getCollection() + "CIECRIP.KBNANN<=" + toAnneeCotisation;
            }
        }
        // Seul from est non vide
        else if (!globaz.globall.util.JAUtil.isStringEmpty(fromAnneeCotisation)) {
            sqlWhere += " AND " + _getCollection() + "CIECRIP.KBNANN>=" + fromAnneeCotisation;
        } else if (!globaz.globall.util.JAUtil.isStringEmpty(toAnneeCotisation)) {
            sqlWhere += " AND " + _getCollection() + "CIECRIP.KBNANN<=" + toAnneeCotisation;
        }
        // Test sur la date d'inscription
        if ((!globaz.globall.util.JAUtil.isStringEmpty(fromDateInscription))
                && (!globaz.globall.util.JAUtil.isStringEmpty(toDateInscription))) {
            // Si from et to sont égaux
            if (fromDateInscription.equals(toDateInscription)) {
                sqlWhere += " AND " + _getCollection() + "CIECRIP.KBLESP like '"
                        + _dbWriteDateAMJ(statement.getTransaction(), fromDateInscription) + "%'";
            } else {
                sqlWhere += " AND " + _getCollection() + "CIECRIP.KBLESP>='"
                        + _dbWriteDateAMJ(statement.getTransaction(), fromDateInscription) + "' AND " + "SUBSTR("
                        + _getCollection() + "CIECRIP.KBLESP,1,8)<='"
                        + _dbWriteDateAMJ(statement.getTransaction(), toDateInscription) + "'";
            }
        }
        // Seul from est non vide
        else if (!globaz.globall.util.JAUtil.isStringEmpty(fromDateInscription)) {
            sqlWhere += " AND " + _getCollection() + "CIECRIP.KBLESP>='"
                    + _dbWriteDateAMJ(statement.getTransaction(), fromDateInscription) + "'";
        } else if (!globaz.globall.util.JAUtil.isStringEmpty(toDateInscription)) {
            sqlWhere += " AND " + _getCollection() + "CIECRIP.KBLESP<='"
                    + _dbWriteDateAMJ(statement.getTransaction(), toDateInscription) + "'";
        }
        // Le type de compte
        if (!globaz.globall.util.JAUtil.isStringEmpty(forIdTypeCompte)) {
            if (CIEcriture.CS_TEMPORAIRE.equals(forIdTypeCompte)) {
                // ajouter temporaire suspens
                sqlWhere += " AND " + _getCollection() + "CIECRIP.KBTCPT in (" + CIEcriture.CS_TEMPORAIRE_SUSPENS
                        + ", " + CIEcriture.CS_TEMPORAIRE + ") ";
            } else {
                sqlWhere += " AND " + _getCollection() + "CIECRIP.KBTCPT=" + forIdTypeCompte;
            }

        } else {
            // On exclut les types correction
            sqlWhere += " AND " + _getCollection() + "CIECRIP.KBTCPT<>"
                    + globaz.pavo.db.compte.CIEcriture.CS_CORRECTION;
        }
        // Le type de journal
        if (!JAUtil.isStringEmpty(getForGenreEcrituresAParser())) {
            parseGenre(getForGenreEcrituresAParser(), statement.getTransaction());
        }
        if (!globaz.globall.util.JAUtil.isStringEmpty(forIdTypeJournal)) {
            sqlWhere += " AND " + _getCollection() + "CIJOURP.KCITIN=" + forIdTypeJournal;
        }
        // Test sur le nom d'utilisateur
        if (!globaz.globall.util.JAUtil.isStringEmpty(forNomEspion)) {
            sqlWhere += " AND " + _getCollection() + "CIECRIP.KBLESP LIKE '%" + forNomEspion + "%' ";
        }
        if (!JAUtil.isStringEmpty(getIsBta())) {
            if ("True".equals(getIsBta())) {
                sqlWhere += " AND " + _getCollection() + "CIECRIP.KBNBTA <> 0 ";

            }
        }
        if (!JAUtil.isStringEmpty(getForCode())) {
            sqlWhere += " AND " + _getCollection() + "CIECRIP.KBTCOD=  " + getForCode();
        }

        if (!JAUtil.isStringEmpty(forItTypeJournalMultiple)) {
            sqlWhere += " AND " + _getCollection() + "CIJOURP.KCITIN IN( " + forItTypeJournalMultiple + " ) ";
        }
        // évite l'écrasement du genre par dans la boucle des totaux
        if (!JAUtil.isStringEmpty(getForGenreParse())) {
            sqlWhere += " AND " + _getCollection() + "CIECRIP.KBTGEN= " + getForGenreParse();
        }
        if (!JAUtil.isStringEmpty(getForGenre())) {
            sqlWhere += " AND " + _getCollection() + "CIECRIP.KBTGEN= " + getForGenre();
        }
        if (!JAUtil.isStringEmpty(getForAnneeCotisation())) {
            sqlWhere += " AND " + _getCollection() + "CIECRIP.KBNANN = "
                    + _dbWriteNumeric(statement.getTransaction(), getForAnneeCotisation());
        }
        if (!JAUtil.isStringEmpty(getForExtourne())) {
            sqlWhere += " AND " + _getCollection() + "CIECRIP.KBTEXT = "
                    + _dbWriteNumeric(statement.getTransaction(), getForExtourne());
        }
        // /fonctionne seulement pour les extourne de genre 1
        if (!JAUtil.isStringEmpty(getForNotExtourne())) {
            sqlWhere += " AND " + _getCollection() + "CIECRIP.KBTEXT <> "
                    + _dbWriteNumeric(statement.getTransaction(), CIEcriture.CS_EXTOURNE_1);
        }
        if (!JAUtil.isStringEmpty(getForParticulier())) {
            sqlWhere += " AND " + _getCollection() + "CIECRIP.KBTPAR = "
                    + _dbWriteNumeric(statement.getTransaction(), getForParticulier());
        }
        if (!avecEcrituresNegatives.booleanValue()) {
            sqlWhere += " AND " + _getCollection() + "CIECRIP.KBTEXT = 0 ";
        }
        if (getSecure().length() != 0) {
            int acces = 317000 + CIUtil.getUserAccessLevel(getSession());

            sqlWhere += " AND " + _getCollection() + "CIINDIP.KATSEC <= "
                    + _dbWriteNumeric(statement.getTransaction(), String.valueOf(acces));
        }
        if (ecrituresSalariees.booleanValue()) {
            sqlWhere += " AND " + _getCollection()
                    + "CIECRIP.KBTGEN <> 310003 AND KBTGEN <> 310009 AND (KBTGEN <> 310007 OR KBTSPE <> 312002) ";
        }

        if (sqlWhere.startsWith(" AND")) {
            sqlWhere = sqlWhere.substring(5);
            sqlWhere = " where " + sqlWhere;
        }

        return firstUnion + joinStr + sqlWhere + secondUnion + joinStr + sqlWhere + endUnion;
    }

    /**
     * Renvoie le nom de la table
     */
    protected String _getTableName() {
        return "CIECRIP";
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        // TODO Raccord de méthode auto-généré
        return null;
    }

    /**
     * Renvoie le code système asssocié à un code utilisateur et un groupe.
     * 
     * @param Un
     *            object BTransaction.
     * @param Le
     *            code utilisateur.
     * @param Le
     *            groupe.
     * @return Le code système asssocié à un code utilisateur et un groupe
     */
    public String codeUtilisateurToCodeSysteme(BTransaction transaction, String code, String groupe) throws Exception {
        if (!JAUtil.isIntegerEmpty(code.trim())) {
            try {
                int valeurCode = new Integer(code).intValue();
                // En dessous de 300000, c'est une code utilisateur (pour PAVO
                // en tout cas)
                if (valeurCode < 300000) {
                    throw new Exception();
                }
                // code est déjà un code système
                return code;
            } catch (Exception e) {
                // C'est un code utilisateur. Il faut obtenir le code système
                FWParametersSystemCodeManager systemCodeMng = new FWParametersSystemCodeManager();
                systemCodeMng.setSession(getSession());
                systemCodeMng.setForIdGroupe(groupe);
                systemCodeMng.setForCodeUtilisateur(code);
                systemCodeMng.find(transaction);
                if (!systemCodeMng.hasErrors()) {
                    if (systemCodeMng.getSize() > 0) {
                        return ((FWParametersSystemCode) systemCodeMng.getEntity(0)).getIdCode();
                    } else {
                        // Pas de code système pour le code utilisateur
                        return "";
                    }
                } else {
                    _addError(transaction, getSession().getLabel("MSG_ECRITURE_USER_CODE"));
                    return "";
                }
            }
        } else {
            return "";
        }
    }

    /**
     * Renvoie le code système asssocié à un code utilisateur et un groupe. Pour l'ecriture
     * 
     * @param Un
     *            object BTransaction.
     * @param Le
     *            code utilisateur.
     * @param Le
     *            groupe.
     * @return Le code système asssocié à un code utilisateur et un groupe
     */
    public String codeUtilisateurToCodeSystemeGre(BTransaction transaction, String code, String groupe)
            throws Exception {
        if (!JAUtil.isStringEmpty(code.trim())) {
            try {
                int valeurCode = new Integer(code).intValue();
                // En dessous de 300000, c'est une code utilisateur (pour PAVO
                // en tout cas)
                if (valeurCode < 300000) {
                    throw new Exception();
                }
                // code est déjà un code système
                return code;
            } catch (Exception e) {
                // C'est un code utilisateur. Il faut obtenir le code système
                FWParametersSystemCodeManager systemCodeMng = new FWParametersSystemCodeManager();
                systemCodeMng.setSession(getSession());
                systemCodeMng.setForIdGroupe(groupe);
                systemCodeMng.setForCodeUtilisateur(code);
                systemCodeMng.find(transaction);
                if (!systemCodeMng.hasErrors()) {
                    if (systemCodeMng.getSize() > 0) {
                        return ((FWParametersSystemCode) systemCodeMng.getEntity(0)).getIdCode();
                    } else {
                        // Pas de code système pour le code utilisateur
                        return "";
                    }
                } else {
                    _addError(transaction, getSession().getLabel("MSG_ECRITURE_USER_CODE"));
                    return "";
                }
            }
        } else {
            return "";
        }
    }

    /**
     * @return
     */
    public Boolean getAvecEcrituresNegatives() {
        return avecEcrituresNegatives;
    }

    public Boolean getEcrituresSalariees() {
        return ecrituresSalariees;
    }

    /**
     * @return
     */
    public String getForAnneeCotisation() {
        return forAnneeCotisation;
    }

    /**
     * @return
     */
    public String getForCode() {
        return forCode;
    }

    /**
     * @return
     */
    public String getForExtourne() {
        return forExtourne;
    }

    /**
     * @return
     */
    public String getForGenre() {
        return forGenre;
    }

    /**
     * @return
     */
    public String getForGenreEcrituresAParser() {
        return forGenreEcrituresAParser;
    }

    /**
     * @return
     */
    public String getForGenreParse() {
        return forGenreParse;
    }

    /**
     * @return
     */
    public java.lang.String getForIdTypeCompte() {
        return forIdTypeCompte;
    }

    /**
     * @return
     */
    public java.lang.String getForIdTypeJournal() {
        return forIdTypeJournal;
    }

    /**
     * @return
     */
    public String getForItTypeJournalMultiple() {
        return forItTypeJournalMultiple;
    }

    /**
     * @return
     */
    public java.lang.String getForNomEspion() {
        return forNomEspion;
    }

    /**
     * @return
     */
    public String getForNotExtourne() {
        return forNotExtourne;
    }

    /**
     * @return
     */
    public java.lang.String getForNumeroAffilie() {
        return forNumeroAffilie;
    }

    /**
     * @return
     */
    public String getForParticulier() {
        return forParticulier;
    }

    /**
     * @return
     */
    public java.lang.String getFromAnneeCotisation() {
        return fromAnneeCotisation;
    }

    /**
     * @return
     */
    public java.lang.String getFromDateInscription() {
        return fromDateInscription;
    }

    /**
     * @return
     */
    public java.lang.String getFromNumeroAffilie() {
        return fromNumeroAffilie;
    }

    /**
     * @return
     */
    public java.lang.String getFromNumeroAvs() {
        return fromNumeroAvs;
    }

    /**
     * @return
     */
    public java.lang.String getFromNumeroJournal() {
        return fromNumeroJournal;
    }

    /**
     * @return
     */
    public String getIsBta() {
        return isBta;
    }

    /**
     * @return
     */
    public String getSecure() {
        return Secure;
    }

    /**
     * @return
     */
    public java.lang.String getToAnneeCotisation() {
        return toAnneeCotisation;
    }

    /**
     * @return
     */
    public java.lang.String getToDateInscription() {
        return toDateInscription;
    }

    /**
     * @return
     */
    public java.lang.String getToNumeroAvs() {
        return toNumeroAvs;
    }

    /**
     * @return
     */
    public java.lang.String getToNumeroJournal() {
        return toNumeroJournal;
    }

    /**
     * @return
     */
    public java.lang.String getTri() {
        return tri;
    }

    protected void parseGenre(String gre, BTransaction transaction) {
        if (!JAUtil.isStringEmpty(gre.trim())) {
            String greCourant = gre;
            if (greCourant.length() == 2) {
                if ("8".equals(greCourant.substring(0, 1))) {
                    greCourant = "0" + greCourant;
                } else {
                    greCourant = greCourant + "0";
                }
            }
            // Si GRE de bonne taille...
            if (greCourant.length() == 3) {
                try {
                    setForGenreParse(codeUtilisateurToCodeSystemeGre(transaction, greCourant.substring(1, 2),
                            "CICODGEN"));
                    setForExtourne(codeUtilisateurToCodeSysteme(transaction, greCourant.substring(0, 1), "CICODEXT"));
                    setForParticulier(codeUtilisateurToCodeSysteme(transaction, greCourant.substring(2, 3), "CIGENSPL"));
                } catch (Exception e) {
                }
                // pour le code particulier:
                // setParticulier(greCourant.substring(2, 3));
            }
        }
    }

    /**
     * @param boolean1
     */
    public void setAvecEcrituresNegatives(Boolean boolean1) {
        avecEcrituresNegatives = boolean1;
    }

    public void setEcrituresSalariees(Boolean ecrituresSalariees) {
        this.ecrituresSalariees = ecrituresSalariees;
    }

    /**
     * @param string
     */
    public void setForAnneeCotisation(String string) {
        forAnneeCotisation = string;
    }

    /**
     * @param string
     */
    public void setForCode(String string) {
        forCode = string;
    }

    /**
     * @param string
     */
    public void setForExtourne(String string) {
        forExtourne = string;
    }

    /**
     * @param string
     */
    public void setForGenre(String string) {
        forGenre = string;
    }

    /**
     * @param string
     */
    public void setForGenreEcrituresAParser(String string) {
        forGenreEcrituresAParser = string;
    }

    /**
     * @param string
     */
    public void setForGenreParse(String string) {
        forGenreParse = string;
    }

    /**
     * @param string
     */
    public void setForIdTypeCompte(java.lang.String string) {
        forIdTypeCompte = string;
    }

    /**
     * @param string
     */
    public void setForIdTypeJournal(java.lang.String string) {
        forIdTypeJournal = string;
    }

    /**
     * @param string
     */
    public void setForItTypeJournalMultiple(String string) {
        forItTypeJournalMultiple = string;
    }

    /**
     * @param string
     */
    public void setForNomEspion(java.lang.String string) {
        forNomEspion = string;
    }

    /**
     * @param string
     */
    public void setForNotExtourne(String string) {
        forNotExtourne = string;
    }

    /**
     * @param string
     */
    public void setForNumeroAffilie(java.lang.String string) {
        forNumeroAffilie = string;
    }

    /**
     * @param string
     */
    public void setForParticulier(String string) {
        forParticulier = string;
    }

    /**
     * @param string
     */
    public void setFromAnneeCotisation(java.lang.String string) {
        fromAnneeCotisation = string;
    }

    /**
     * @param string
     */
    public void setFromDateInscription(java.lang.String string) {
        fromDateInscription = string;
    }

    /**
     * @param string
     */
    public void setFromNumeroAffilie(java.lang.String string) {
        fromNumeroAffilie = string;
    }

    /**
     * @param string
     */
    public void setFromNumeroAvs(java.lang.String string) {
        fromNumeroAvs = string;
    }

    /**
     * @param string
     */
    public void setFromNumeroJournal(java.lang.String string) {
        fromNumeroJournal = string;
    }

    /**
     * @param string
     */
    public void setIsBta(String string) {
        isBta = string;
    }

    /**
     * @param string
     */
    public void setSecure(String string) {
        Secure = string;
    }

    /**
     * @param string
     */
    public void setToAnneeCotisation(java.lang.String string) {
        toAnneeCotisation = string;
    }

    /**
     * @param string
     */
    public void setToDateInscription(java.lang.String string) {
        toDateInscription = string;
    }

    /**
     * @param string
     */
    public void setToNumeroAvs(java.lang.String string) {
        toNumeroAvs = string;
    }

    /**
     * @param string
     */
    public void setToNumeroJournal(java.lang.String string) {
        toNumeroJournal = string;
    }

    /**
     * @param string
     */
    public void setTri(java.lang.String string) {
        tri = string;
    }

}