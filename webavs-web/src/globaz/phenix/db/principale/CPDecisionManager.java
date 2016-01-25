package globaz.phenix.db.principale;

import globaz.globall.db.BConstants;
import globaz.jade.client.util.JadeStringUtil;
import globaz.phenix.process.acompte.CPAcompteCreationAnnuelle;
import globaz.pyxis.db.tiers.TITiersViewBean;

public class CPDecisionManager extends globaz.globall.db.BManager implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    private java.lang.String forAnneeDecision = "";
    private java.lang.String forDateInformation = "";
    private java.lang.String forDebutDecision = "";
    private String forEtat = "";
    private String forExceptEtatDecision = "";
    private java.lang.String forExceptGenreAffilie = "";
    private java.lang.String forExceptIdCommunication = "";
    private java.lang.String forExceptIdDecision = "";
    private String forExceptIdIfdDefinitif = "";
    private java.lang.String forExceptSpecification = "";
    private java.lang.String forExceptTypeDecision = "";
    private java.lang.String forFinAffiliation = "";
    private java.lang.String forFinDecision = "";
    private java.lang.String forGenreAffilie = "";
    private java.lang.String forIdAffiliation = "";
    private java.lang.String forIdCommunication = "";
    private java.lang.String forIdConjoint = "";
    private java.lang.String forIdDecision = "";
    private java.lang.String forIdIfdDefintif = "";
    private java.lang.String forIdIfdProvisoire = "";
    private java.lang.String forIdPassage = "";
    private java.lang.String forIdTiers = "";
    private Boolean forIsActive = Boolean.FALSE;
    private Boolean forIsComplementaire = Boolean.FALSE;
    private Boolean forIsConjointNonVide = Boolean.FALSE;
    // Facturation
    private Boolean forIsFacturation = Boolean.FALSE;
    private Boolean forIsNotActive = Boolean.FALSE;
    private Boolean forIsNotFacturation = Boolean.FALSE;
    private java.lang.String forLtIdIfdDefintif = "";
    // Selection sur le no d'affilié
    private java.lang.String forNoAffilie = "";
    private Boolean forNotComplementaire = Boolean.FALSE;
    private java.lang.String forResponsable = "";
    private java.lang.String forSpecification = "";
    private java.lang.String forTypeDecision = "";
    private java.lang.String fromAnneeDecision = "";
    private java.lang.String fromDateDebutDecision = "";
    private java.lang.String fromDateFinDecision = "";
    private String fromEtat = "";
    private java.lang.String fromFinAffiliation = "";
    private java.lang.String fromGtIdDecision = "";
    private java.lang.String fromIdDecision = "";
    private java.lang.String fromLtIdDecision = "";
    private java.lang.String fromNoAffilie = "";

    private String inEtat = "";
    private java.lang.String inGenreAffilie = "";
    private java.lang.String inTypeDecision = "";
    private java.lang.String notInEtat = "";
    private java.lang.String notInGenreAffilie = "";

    private java.lang.String notInTypeDecision = "";
    private java.lang.String order = "";
    private java.lang.String tillNoAffilie = "";
    private java.lang.String toAnneeDecision = "";
    private java.lang.String toDateDebutDecision = "";
    private java.lang.String toDateFinDecision = "";
    private java.lang.String toIdDecision = "";
    private Boolean useManagerForRepriseCotPersIndependant = Boolean.FALSE;
    private Boolean useManagerForRepriseCotPersIndependantHorsAcompte = Boolean.FALSE;
    private Boolean useManagerForRepriseCotPersNonActif = Boolean.FALSE;
    private Boolean useManagerForRepriseCotPersNonActifHorsAcompte = Boolean.FALSE;

    /**
     * Renvoie la liste des champs
     * 
     * @return la liste des champs
     */
    @Override
    protected String _getFields(globaz.globall.db.BStatement statement) {
        if (getUseManagerForRepriseCotPersNonActif().equals(Boolean.TRUE)
                || getUseManagerForRepriseCotPersNonActifHorsAcompte().equals(Boolean.TRUE)) {
            return _getFieldsForRepriseCotPersNonActif();
        } else if (getUseManagerForRepriseCotPersIndependant().equals(Boolean.TRUE)
                || getUseManagerForRepriseCotPersIndependantHorsAcompte().equals(Boolean.TRUE)) {
            return _getFieldsForRepriseCotPersIndependant();
        } else {
            return super._getFields(statement);
        }
    }

    protected String _getFieldsForRepriseCotPersIndependant() {
        return "temp.HTITIE, temp.MALNAF, " + _getCollection() + "CPDECIP.IAIDEC, " + _getCollection()
                + "CPDECIP.MAIAFF";
    }

    protected String _getFieldsForRepriseCotPersNonActif() {
        return "temp.HTITIE, temp.MALNAF, temp.HPDNAI, "
        // + _getCollection()
        // + "CPDOCAP.IHMDCA, "
                + _getCollection() + "CPDECIP.IAIDEC, " + _getCollection() + "CPDECIP.MAIAFF";
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        if (getUseManagerForRepriseCotPersNonActif().equals(Boolean.TRUE)) {
            return _getFromForRepriseCotPersNonActif(statement);
        } else if (getUseManagerForRepriseCotPersIndependant().equals(Boolean.TRUE)) {
            return _getFromForRepriseCotPersIndependant(statement);
        } else if (getUseManagerForRepriseCotPersNonActifHorsAcompte().equals(Boolean.TRUE)) {
            return _getFromForRepriseCotPersNonActifHorsAcompte(statement);
        } else if (getUseManagerForRepriseCotPersIndependantHorsAcompte().equals(Boolean.TRUE)) {
            return _getFromForRepriseCotPersIndependantHorsAcompte(statement);
        } else {
            return _getCollection() + "CPDECIP";
        }
    }

    /*
     * Requête pour sélectionner la dernière décision active de type indépendant
     */
    protected String _getFromForRepriseCotPersIndependant(globaz.globall.db.BStatement statement) {

        String tableAFAFFIP = _getCollection() + "AFAFFIP";
        String tableCPDECIP = _getCollection() + "CPDECIP";

        StringBuffer sb = new StringBuffer(" (SELECT " + tableAFAFFIP + ".HTITIE, " + tableAFAFFIP + ".MALNAF, "
                + " MAX(" + tableCPDECIP + ".IAANNE) AS IAANNE");
        sb.append(" FROM " + tableAFAFFIP);
        sb.append(" LEFT OUTER JOIN " + tableCPDECIP + " ON " + tableAFAFFIP + ".MAIAFF = " + tableCPDECIP + ".MAIAFF");
        // Type d'affiliation:indépendant et NE
        sb.append(" WHERE " + tableAFAFFIP + ".MATTAF IN (804001,804005,804008,804011) ");
        // année 2003 et 2004 dont la fin d'affiliation est vide et début
        // d'affiliation < 31.12.2004
        // Etat de la décision != CS_SORTIE
        sb.append(" AND ((" + tableCPDECIP + ".IAANNE IN (" + (Integer.parseInt(getForAnneeDecision()) - 1) + ","
                + getForAnneeDecision() + ")) AND (" + tableAFAFFIP + ".MADFIN = 0 OR " + tableAFAFFIP + ".MADFIN > "
                + getForAnneeDecision() + "0101) AND " + tableAFAFFIP + ".MADDEB < " + getForAnneeDecision() + "1231)");
        if (!JadeStringUtil.isEmpty(getForNoAffilie())) {
            sb.append(" AND " + tableAFAFFIP + ".MALNAF = "
                    + this._dbWriteString(statement.getTransaction(), getForNoAffilie()));
        }
        if (!JadeStringUtil.isEmpty(getFromNoAffilie())) {
            sb.append(" AND " + tableAFAFFIP + ".MALNAF >= "
                    + this._dbWriteString(statement.getTransaction(), getFromNoAffilie()));

        }
        // PO 8330
        sb.append(" AND " + tableCPDECIP + ".IAANNE <=" + getForAnneeDecision());

        if (!JadeStringUtil.isEmpty(getTillNoAffilie())) {
            sb.append(" AND " + tableAFAFFIP + ".MALNAF <= "
                    + this._dbWriteString(statement.getTransaction(), getTillNoAffilie()));
        }
        // Ne prendre que les validées et facturées
        sb.append(" AND ((" + tableCPDECIP + ".IATETA in (" + CPDecision.CS_PB_COMPTABILISATION + ","
                + CPDecision.CS_FACTURATION + "," + CPDecision.CS_REPRISE + ") AND " + tableCPDECIP
                + ".IAACTI ='1') OR " + tableCPDECIP + ".IATETA=" + CPDecision.CS_VALIDATION + ")");

        // AND ((facoweb.cpdecip.iateta IN ( 604009, 604004, 604005 ) and facoweb.cpdecip.iaacti='1')or
        // (facoweb.cpdecip.iateta=604003))

        // Ne prendre que les décisions actives
        // sb.append(" AND " + tableCPDECIP + ".IAACTI = '1'");
        sb.append(" GROUP BY " + tableAFAFFIP + ".HTITIE, " + tableAFAFFIP + ".MALNAF");

        sb.append(" HAVING (MAX(" + tableCPDECIP + ".IAANNE)= " + (Integer.parseInt(getForAnneeDecision()) - 1)
                + ")) AS temp(HTITIE, MALNAF, IAANNE)");

        sb.append(" INNER JOIN " + tableAFAFFIP + " ON (temp.MALNAF = " + tableAFAFFIP + ".MALNAF)");
        sb.append(" LEFT OUTER JOIN " + tableCPDECIP + " ON " + tableAFAFFIP + ".MAIAFF = " + tableCPDECIP + ".MAIAFF");

        return sb.toString();
    }

    protected String _getFromForRepriseCotPersIndependantHorsAcompte(globaz.globall.db.BStatement statement) {

        String tableAFAFFIP = _getCollection() + "AFAFFIP";
        String tableCPDECIP = _getCollection() + "CPDECIP";

        StringBuffer sb = new StringBuffer(" (SELECT " + tableAFAFFIP + ".HTITIE, " + tableAFAFFIP + ".MALNAF, "
                + " MAX(" + tableCPDECIP + ".IAANNE) AS IAANNE");
        sb.append(" FROM " + tableAFAFFIP);
        sb.append(" LEFT OUTER JOIN " + tableCPDECIP + " ON " + tableAFAFFIP + ".MAIAFF = " + tableCPDECIP + ".MAIAFF");
        // Type d'affiliation:indépendant et NE
        sb.append(" WHERE " + tableAFAFFIP + ".MATTAF IN (804001,804005,804008,804011) ");
        // année 2003 et 2004 dont la fin d'affiliation est vide et début
        // d'affiliation < 31.12.2004
        // Etat de la décision != CS_SORTIE
        sb.append(" AND ((" + tableAFAFFIP + ".MADFIN = 0 OR " + tableAFAFFIP + ".MADFIN > " + getForAnneeDecision()
                + "0101) AND " + tableAFAFFIP + ".MADDEB < " + getForAnneeDecision() + "1231)");
        if (!JadeStringUtil.isEmpty(getForNoAffilie())) {
            sb.append(" AND " + tableAFAFFIP + ".MALNAF = "
                    + this._dbWriteString(statement.getTransaction(), getForNoAffilie()));
        }
        if (!JadeStringUtil.isEmpty(getFromNoAffilie())) {
            sb.append(" AND " + tableAFAFFIP + ".MALNAF >= "
                    + this._dbWriteString(statement.getTransaction(), getFromNoAffilie()));

        }
        if (!JadeStringUtil.isEmpty(getTillNoAffilie())) {
            sb.append(" AND " + tableAFAFFIP + ".MALNAF <= "
                    + this._dbWriteString(statement.getTransaction(), getTillNoAffilie()));
        }
        sb.append(" AND " + tableCPDECIP + ".IAANNE <=" + getForAnneeDecision());
        // Ne prendre que les validées et facturées
        sb.append(" AND ((" + tableCPDECIP + ".IATETA in (" + CPDecision.CS_PB_COMPTABILISATION + ","
                + CPDecision.CS_FACTURATION + "," + CPDecision.CS_REPRISE + ") AND " + tableCPDECIP
                + ".IAACTI ='1') OR " + tableCPDECIP + ".IATETA=" + CPDecision.CS_VALIDATION + ")");
        // Ne prendre que les décisions actives en excluant les acomptes
        // sb.append(" AND " + tableCPDECIP + ".IAACTI='1' AND " + tableCPDECIP + ".IATTDE<>" + CPDecision.CS_ACOMPTE);
        sb.append(" AND " + tableCPDECIP + ".IATTDE<>" + CPDecision.CS_ACOMPTE);
        sb.append(" GROUP BY " + tableAFAFFIP + ".HTITIE, " + tableAFAFFIP + ".MALNAF");

        sb.append(" ) AS temp(HTITIE, MALNAF, IAANNE)");

        sb.append(" INNER JOIN " + tableAFAFFIP + " ON (temp.MALNAF = " + tableAFAFFIP + ".MALNAF)");
        sb.append(" LEFT OUTER JOIN " + tableCPDECIP + " ON " + tableAFAFFIP + ".MAIAFF = " + tableCPDECIP + ".MAIAFF");

        return sb.toString();
    }

    /*
     * Requête pour sélectionner la dernière décision active de type indépendant qui n'est pas un acompte
     */

    /*
     * Requête pour sélectionner la dernière décision active de type non actif
     */
    protected String _getFromForRepriseCotPersNonActif(globaz.globall.db.BStatement statement) {

        // variable age retraite homme
        int ageRetraiteHomme = 65;
        int ageRetraiteFemme = 64;

        String dateRentierHomme = Integer.toString(Integer.parseInt(getForAnneeDecision()) - ageRetraiteHomme) + "0101";
        String dateRentierFemme = Integer.toString(Integer.parseInt(getForAnneeDecision()) - ageRetraiteFemme) + "0101";

        String tableAFAFFIP = _getCollection() + "AFAFFIP";
        String tableCPDECIP = _getCollection() + "CPDECIP";
        String tableTIPERSP = _getCollection() + "TIPERSP";
        // String tableCPDOCAP = _getCollection() + "CPDOCAP";

        StringBuffer sb = new StringBuffer(" (SELECT " + tableAFAFFIP + ".HTITIE, " + tableAFAFFIP + ".MALNAF, "
                + tableTIPERSP + ".HPDNAI, MAX(" + tableCPDECIP + ".IAANNE) AS IAANNE");
        sb.append(" FROM " + tableAFAFFIP);
        sb.append(" LEFT OUTER JOIN " + tableCPDECIP + " ON " + tableAFAFFIP + ".MAIAFF = " + tableCPDECIP + ".MAIAFF");
        sb.append(" INNER JOIN " + tableTIPERSP + " ON " + tableTIPERSP + ".HTITIE = " + tableAFAFFIP + ".HTITIE");
        // Ne prendre que les rentiers en cours d'année de la reprise
        sb.append(" WHERE ((" + tableTIPERSP + ".HPDNAI>=" + dateRentierHomme + " AND " + tableTIPERSP + ".HPTSEX="
                + TITiersViewBean.CS_HOMME + ") OR (" + tableTIPERSP + ".HPDNAI>=" + dateRentierFemme + " AND "
                + tableTIPERSP + ".HPTSEX=" + TITiersViewBean.CS_FEMME + ")) AND ");
        // Type d'affiliation:indépendant et NE
        sb.append(tableAFAFFIP + ".MATTAF IN (804004,804006) ");
        // année 2003 et 2004 dont la fin d'affiliation est vide et début
        // d'affiliation < 31.12.2004
        // Etat de la décision != CS_SORTIE
        sb.append(" AND ((" + tableCPDECIP + ".IAANNE IN (" + (Integer.parseInt(getForAnneeDecision()) - 1) + ","
                + getForAnneeDecision() + ")) AND (" + tableAFAFFIP + ".MADFIN = 0 OR " + tableAFAFFIP + ".MADFIN > "
                + getForAnneeDecision() + "0101) AND " + tableAFAFFIP + ".MADDEB < " + getForAnneeDecision() + "1231)");
        if (!JadeStringUtil.isEmpty(getForNoAffilie())) {
            sb.append(" AND " + tableAFAFFIP + ".MALNAF = "
                    + this._dbWriteString(statement.getTransaction(), getForNoAffilie()));
        }
        if (!JadeStringUtil.isEmpty(getFromNoAffilie())) {
            sb.append(" AND " + tableAFAFFIP + ".MALNAF >= "
                    + this._dbWriteString(statement.getTransaction(), getFromNoAffilie()));

        }
        if (!JadeStringUtil.isEmpty(getTillNoAffilie())) {
            sb.append(" AND " + tableAFAFFIP + ".MALNAF <= "
                    + this._dbWriteString(statement.getTransaction(), getTillNoAffilie()));
        }
        sb.append(" AND " + tableCPDECIP + ".IAANNE <=" + getForAnneeDecision());
        // Ne prendre que les validées et facturées
        sb.append(" AND ((" + tableCPDECIP + ".IATETA in (" + CPDecision.CS_PB_COMPTABILISATION + ","
                + CPDecision.CS_FACTURATION + "," + CPDecision.CS_REPRISE + ") AND " + tableCPDECIP
                + ".IAACTI ='1') OR " + tableCPDECIP + ".IATETA=" + CPDecision.CS_VALIDATION + ")");
        // Ne prendre que les décisions actives
        // sb.append(" AND " + tableCPDECIP + ".IAACTI='1'");
        sb.append(" GROUP BY " + tableAFAFFIP + ".HTITIE, " + tableAFAFFIP + ".MALNAF, " + tableTIPERSP + ".HPDNAI");

        sb.append(" HAVING (MAX(" + tableCPDECIP + ".IAANNE)= " + (Integer.parseInt(getForAnneeDecision()) - 1)
                + ")) AS temp(HTITIE, MALNAF, HPDNAI, IAANNE)");

        sb.append(" INNER JOIN " + tableAFAFFIP + " ON (temp.MALNAF = " + tableAFAFFIP + ".MALNAF)");
        sb.append(" LEFT OUTER JOIN " + tableCPDECIP + " ON " + tableAFAFFIP + ".MAIAFF = " + tableCPDECIP + ".MAIAFF");

        return sb.toString();
    }

    /*
     * Requête pour sélectionner la dernière décision active de type non actif hormis les acomptes
     */
    protected String _getFromForRepriseCotPersNonActifHorsAcompte(globaz.globall.db.BStatement statement) {

        // variable age retraite homme
        int ageRetraiteHomme = 65;
        int ageRetraiteFemme = 64;

        String dateRentierHomme = Integer.toString(Integer.parseInt(getForAnneeDecision()) - ageRetraiteHomme) + "0101";
        String dateRentierFemme = Integer.toString(Integer.parseInt(getForAnneeDecision()) - ageRetraiteFemme) + "0101";

        String tableAFAFFIP = _getCollection() + "AFAFFIP";
        String tableCPDECIP = _getCollection() + "CPDECIP";
        String tableTIPERSP = _getCollection() + "TIPERSP";
        // String tableCPDOCAP = _getCollection() + "CPDOCAP";

        StringBuffer sb = new StringBuffer(" (SELECT " + tableAFAFFIP + ".HTITIE, " + tableAFAFFIP + ".MALNAF, "
                + tableTIPERSP + ".HPDNAI, MAX(" + tableCPDECIP + ".IAANNE) AS IAANNE");
        sb.append(" FROM " + tableAFAFFIP);
        sb.append(" LEFT OUTER JOIN " + tableCPDECIP + " ON " + tableAFAFFIP + ".MAIAFF = " + tableCPDECIP + ".MAIAFF");
        sb.append(" INNER JOIN " + tableTIPERSP + " ON " + tableTIPERSP + ".HTITIE = " + tableAFAFFIP + ".HTITIE");
        // Ne prendre que les rentiers en cours d'année de la reprise
        sb.append(" WHERE ((" + tableTIPERSP + ".HPDNAI>=" + dateRentierHomme + " AND " + tableTIPERSP + ".HPTSEX="
                + TITiersViewBean.CS_HOMME + ") OR (" + tableTIPERSP + ".HPDNAI>=" + dateRentierFemme + " AND "
                + tableTIPERSP + ".HPTSEX=" + TITiersViewBean.CS_FEMME + ")) AND ");
        // Type d'affiliation:indépendant et NE
        sb.append(tableAFAFFIP + ".MATTAF IN (804004,804006) ");
        // année 2003 et 2004 dont la fin d'affiliation est vide et début
        // d'affiliation < 31.12.2004
        // Etat de la décision != CS_SORTIE
        sb.append(" AND ((" + tableAFAFFIP + ".MADFIN = 0 OR " + tableAFAFFIP + ".MADFIN > " + getForAnneeDecision()
                + "0101) AND " + tableAFAFFIP + ".MADDEB < " + getForAnneeDecision() + "1231)");
        if (!JadeStringUtil.isEmpty(getForNoAffilie())) {
            sb.append(" AND " + tableAFAFFIP + ".MALNAF = "
                    + this._dbWriteString(statement.getTransaction(), getForNoAffilie()));
        }
        if (!JadeStringUtil.isEmpty(getFromNoAffilie())) {
            sb.append(" AND " + tableAFAFFIP + ".MALNAF >= "
                    + this._dbWriteString(statement.getTransaction(), getFromNoAffilie()));

        }
        if (!JadeStringUtil.isEmpty(getTillNoAffilie())) {
            sb.append(" AND " + tableAFAFFIP + ".MALNAF <= "
                    + this._dbWriteString(statement.getTransaction(), getTillNoAffilie()));
        }
        sb.append(" AND " + tableCPDECIP + ".IAANNE <=" + getForAnneeDecision());
        // Ne prendre que les validées et facturées
        sb.append(" AND ((" + tableCPDECIP + ".IATETA in (" + CPDecision.CS_PB_COMPTABILISATION + ","
                + CPDecision.CS_FACTURATION + "," + CPDecision.CS_REPRISE + ") AND " + tableCPDECIP
                + ".IAACTI ='1') OR " + tableCPDECIP + ".IATETA=" + CPDecision.CS_VALIDATION + ")");
        // Ne prendre que les décisions actives en excluant les acomptes
        // sb.append(" AND " + tableCPDECIP + ".IAACTI='1' AND " + tableCPDECIP + ".IATTDE<>" + CPDecision.CS_ACOMPTE);
        sb.append(" AND " + tableCPDECIP + ".IATTDE<>" + CPDecision.CS_ACOMPTE);
        sb.append(" GROUP BY " + tableAFAFFIP + ".HTITIE, " + tableAFAFFIP + ".MALNAF, " + tableTIPERSP + ".HPDNAI");

        sb.append(" ) AS temp(HTITIE, MALNAF, HPDNAI, IAANNE)");

        sb.append(" INNER JOIN " + tableAFAFFIP + " ON (temp.MALNAF = " + tableAFAFFIP + ".MALNAF)");
        sb.append(" LEFT OUTER JOIN " + tableCPDECIP + " ON " + tableAFAFFIP + ".MAIAFF = " + tableCPDECIP + ".MAIAFF");

        return sb.toString();
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        return getOrder();
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        // Depuis une année de décision
        if (getFromAnneeDecision().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IAANNE>=" + this._dbWriteNumeric(statement.getTransaction(), getFromAnneeDecision());
        }
        // Pour une année de décision
        if (getForAnneeDecision().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            if (getUseManagerForRepriseCotPersIndependant().equals(Boolean.TRUE)
                    || getUseManagerForRepriseCotPersIndependantHorsAcompte().equals(Boolean.TRUE)
                    || getUseManagerForRepriseCotPersNonActif().equals(Boolean.TRUE)
                    || getUseManagerForRepriseCotPersNonActifHorsAcompte().equals(Boolean.TRUE)) {
                sqlWhere += _getCollection() + "CPDECIP.IAANNE=temp.IAANNE AND " + _getCollection()
                        + "CPDECIP.IATTDE <> 605005 AND " + _getCollection() + "CPDECIP.IATETA in ("
                        + CPDecision.CS_VALIDATION + "," + CPDecision.CS_PB_COMPTABILISATION + ","
                        + CPDecision.CS_FACTURATION + "," + CPDecision.CS_REPRISE + ")";

                sqlWhere += " AND (" + _getCollection() + "AFAFFIP.MADFIN = 0 OR " + _getCollection()
                        + "AFAFFIP.MADFIN > " + getForAnneeDecision() + "0101) AND " + _getCollection()
                        + "AFAFFIP.MADDEB < " + getForAnneeDecision() + "1231";

                return sqlWhere;
            } else {
                sqlWhere += "IAANNE=" + this._dbWriteNumeric(statement.getTransaction(), getForAnneeDecision());
            }
        }

        // Pour une décision
        if (getForIdDecision().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IAIDEC=" + this._dbWriteNumeric(statement.getTransaction(), getForIdDecision());
        }
        // Pour une décision
        if (getFromLtIdDecision().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IAIDEC<" + this._dbWriteNumeric(statement.getTransaction(), getFromLtIdDecision());
        }

        // Pour une décision
        if (getFromGtIdDecision().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IAIDEC>" + this._dbWriteNumeric(statement.getTransaction(), getFromGtIdDecision());
        }
        // Pour un tiers
        if (getForIdTiers().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "HTITIE=" + this._dbWriteNumeric(statement.getTransaction(), getForIdTiers());
        }
        // Pour un etat
        if (getForEtat().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IATETA=" + this._dbWriteNumeric(statement.getTransaction(), getForEtat());
        }

        // Avec les decisons complémentaire
        if (Boolean.TRUE.equals(getForIsComplementaire())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IABCOM = "
                    + this._dbWriteBoolean(statement.getTransaction(), new Boolean(true),
                            BConstants.DB_TYPE_BOOLEAN_CHAR);
        }
        // Avec les decisons complémentaire
        if (Boolean.TRUE.equals(getForNotComplementaire())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IABCOM = "
                    + this._dbWriteBoolean(statement.getTransaction(), new Boolean(false),
                            BConstants.DB_TYPE_BOOLEAN_CHAR);
        }
        // Pour un etat
        if (getInEtat().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IATETA IN (" + getInEtat() + ")";
        }
        // Pour un id ifd définitive
        if (getForIdIfdDefintif().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "ICIIFD=" + this._dbWriteNumeric(statement.getTransaction(), getForIdIfdDefintif());
        }
        // Avant un id ifd définitive
        if (getForLtIdIfdDefintif().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "ICIIFD<" + this._dbWriteNumeric(statement.getTransaction(), getForLtIdIfdDefintif());
        }
        // Excepter un id ifd définitive
        if (getForExceptIdIfdDefinitif().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CPDecision.FIELD_ICIIFD + "<>"
                    + this._dbWriteNumeric(statement.getTransaction(), getForExceptIdIfdDefinitif());
        }
        // Pour une période ifd provisoire (base de calcul)
        if (getForIdIfdProvisoire().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "ICIIFP=" + this._dbWriteNumeric(statement.getTransaction(), getForIdIfdProvisoire());
        }

        // Pour un id. communication
        if (getForIdCommunication().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IBIDCF=" + this._dbWriteNumeric(statement.getTransaction(), getForIdCommunication());
        }
        // Except un id. communication
        if (getForExceptIdCommunication().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IBIDCF<>" + this._dbWriteNumeric(statement.getTransaction(), getForExceptIdCommunication());
        }
        // Pour un passage de facturation
        if (getForIdPassage().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "EBIPAS=" + this._dbWriteNumeric(statement.getTransaction(), getForIdPassage());
        }

        // Pour un type de décision (provisoire, définitive...)
        if (getForTypeDecision().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IATTDE=" + this._dbWriteNumeric(statement.getTransaction(), getForTypeDecision());
        }
        // Pas égal à un type de décision (provisoire, définitive...)
        if (getForExceptTypeDecision().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IATTDE <>" + this._dbWriteNumeric(statement.getTransaction(), getForExceptTypeDecision());
        }
        // Pas égal à un id décision
        if (getForExceptIdDecision().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IAIDEC <>" + this._dbWriteNumeric(statement.getTransaction(), getForExceptIdDecision());
        }
        // Egal à un type de décision (provisoire, définitive...)
        if (getInTypeDecision().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IATTDE in (" + getInTypeDecision() + ")";
        }
        // Pas égal à un type de décision (provisoire, définitive...)
        if (getNotInTypeDecision().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IATTDE not in (" + getNotInTypeDecision() + ")";
        }
        // Pas égal à un type de décision (provisoire, définitive...)
        if (getNotInEtat().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IATATA not in (" + getNotInEtat() + ")";
        }
        // Pour un conjoint
        if (getForIdConjoint().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "HTICJT=" + this._dbWriteNumeric(statement.getTransaction(), getForIdConjoint());
        }
        // A partir d'un etat
        if (getFromEtat().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CPDecision.FIELD_IATETA + ">="
                    + this._dbWriteNumeric(statement.getTransaction(), getFromEtat());
        }
        // Pour un genre d'affilié (non actif, indépendant...)
        if (getForGenreAffilie().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IATGAF=" + this._dbWriteNumeric(statement.getTransaction(), getForGenreAffilie());
        }
        // Différent d'un genre d'affilié (non actif, indépendant...)
        if (getForExceptGenreAffilie().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IATGAF<>" + this._dbWriteNumeric(statement.getTransaction(), getForExceptGenreAffilie());
        }

        // Pour une affiliation
        if (getForIdAffiliation().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MAIAFF=" + this._dbWriteNumeric(statement.getTransaction(), getForIdAffiliation());
        }

        // Pour un responsable
        if (getForResponsable().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IARESP=" + this._dbWriteNumeric(statement.getTransaction(), getForResponsable());
        }

        // Depuis un id. décision
        if (getFromIdDecision().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IAIDEC>=" + this._dbWriteNumeric(statement.getTransaction(), getFromIdDecision());
        }

        // Jusqu'à un id. décision
        if (getToIdDecision().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IAIDEC<=" + this._dbWriteNumeric(statement.getTransaction(), getToIdDecision());
        }

        // Avec date de début de la décision
        if (getForDebutDecision().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            if (getUseManagerForRepriseCotPersNonActif().equals(Boolean.TRUE)
                    || getUseManagerForRepriseCotPersNonActifHorsAcompte().equals(Boolean.TRUE)
                    || getUseManagerForRepriseCotPersIndependant().equals(Boolean.TRUE)
                    || getUseManagerForRepriseCotPersIndependantHorsAcompte().equals(Boolean.TRUE)) {
                sqlWhere += "temp.IADDEB = CPDECIP.IADDEB";
            } else {
                sqlWhere += "IADDEB=" + this._dbWriteDateAMJ(statement.getTransaction(), getForDebutDecision());
            }
        }

        // Depuis date de début de la décision
        if (getFromDateDebutDecision().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IADDEB>=" + this._dbWriteDateAMJ(statement.getTransaction(), getFromDateDebutDecision());
        }

        // Avec date de fin de la décision
        if (getForFinDecision().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            if (getUseManagerForRepriseCotPersIndependant().equals(Boolean.TRUE)
                    || getUseManagerForRepriseCotPersIndependantHorsAcompte().equals(Boolean.TRUE)
                    || getUseManagerForRepriseCotPersNonActif().equals(Boolean.TRUE)
                    || getUseManagerForRepriseCotPersNonActifHorsAcompte().equals(Boolean.TRUE)) {
                sqlWhere += "temp.IADFIN = CPDECIP.IADFIN";
            } else {
                sqlWhere += "IADFIN=" + this._dbWriteDateAMJ(statement.getTransaction(), getForFinDecision());
            }
        }

        // Depuis date de fin de la décision
        if (getFromDateFinDecision().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IADFIN>=" + this._dbWriteDateAMJ(statement.getTransaction(), getFromDateFinDecision());
        }
        // Jusqu'à une année de décision (comprise)
        if (getToAnneeDecision().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CPDecision.FIELD_IAANNE + "<="
                    + this._dbWriteNumeric(statement.getTransaction(), getToAnneeDecision());
        }
        // Jusqu'à la date de fin de la décision
        if (getToDateFinDecision().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IADFIN <=" + this._dbWriteDateAMJ(statement.getTransaction(), getToDateFinDecision());
        }
        // Jusqu'à la date de début de la décision
        if (getToDateDebutDecision().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IADDEB <=" + this._dbWriteDateAMJ(statement.getTransaction(), getToDateDebutDecision());
        }
        // Avec la date de décision
        if (getForDateInformation().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IADINF=" + this._dbWriteDateAMJ(statement.getTransaction(), getForDateInformation());
        }
        // Différent de la spécification
        if (getForExceptSpecification().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IATSPE <> " + this._dbWriteNumeric(statement.getTransaction(), getForExceptSpecification());
        }
        // Avec spécification
        if (getForSpecification().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IATSPE = " + this._dbWriteNumeric(statement.getTransaction(), getForSpecification());
        }
        // Avec les decisons qui doivent être facturées
        if (Boolean.TRUE.equals(getForIsFacturation())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IAFACT = "
                    + this._dbWriteBoolean(statement.getTransaction(), new Boolean(true),
                            BConstants.DB_TYPE_BOOLEAN_CHAR);
        }
        // Sans les décisions qui doivent pas passer en facturation
        if (Boolean.TRUE.equals(getForIsNotFacturation())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IAFACT = "
                    + this._dbWriteBoolean(statement.getTransaction(), new Boolean(false),
                            BConstants.DB_TYPE_BOOLEAN_CHAR);
        }
        // Avec les décisions actives
        if (Boolean.TRUE.equals(getForIsActive())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " IAACTI = "
                    + this._dbWriteBoolean(statement.getTransaction(), new Boolean(true),
                            BConstants.DB_TYPE_BOOLEAN_CHAR);
        }
        // Avec les id conjoints non vide
        if (Boolean.TRUE.equals(getForIsConjointNonVide())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " HTICJT <> 0 ";
        }
        // Sans les décisions actives
        if (Boolean.TRUE.equals(getForIsNotActive())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " IAACTI = "
                    + this._dbWriteBoolean(statement.getTransaction(), new Boolean(false),
                            BConstants.DB_TYPE_BOOLEAN_CHAR);
        }
        // Compris dans une sélection
        if (getInGenreAffilie().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IATGAF in (" + getInGenreAffilie() + ")";
        }
        // Non inclus dans une sélection
        if (getNotInGenreAffilie().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IATGAF not in (" + getNotInGenreAffilie() + ")";
        }

        // Pour exclure un etat
        if (getForExceptEtatDecision().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IATETA <>" + this._dbWriteNumeric(statement.getTransaction(), getForExceptEtatDecision());
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        if (getUseManagerForRepriseCotPersIndependant().equals(Boolean.TRUE)
                || getUseManagerForRepriseCotPersIndependantHorsAcompte().equals(Boolean.TRUE)
                || getUseManagerForRepriseCotPersNonActif().equals(Boolean.TRUE)
                || getUseManagerForRepriseCotPersNonActifHorsAcompte().equals(Boolean.TRUE)) {
            return new CPAcompteCreationAnnuelle();
        } else {
            return new CPDecision();
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.04.2003 13:17:03)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForAnneeDecision() {
        return forAnneeDecision;
    }

    /**
     * Returns the forDateInformation.
     * 
     * @return java.lang.String
     */
    public java.lang.String getForDateInformation() {
        return forDateInformation;
    }

    /**
     * Returns the forDebutDecision.
     * 
     * @return java.lang.String
     */
    public java.lang.String getForDebutDecision() {
        return forDebutDecision;
    }

    public String getForEtat() {
        return forEtat;
    }

    /**
     * Returns the forExceptGenreAffilie.
     * 
     * @return java.lang.String
     */
    public java.lang.String getForExceptGenreAffilie() {
        return forExceptGenreAffilie;
    }

    public java.lang.String getForExceptIdCommunication() {
        return forExceptIdCommunication;
    }

    /**
     * @return
     */
    public String getForExceptIdDecision() {
        return forExceptIdDecision;
    }

    public String getForExceptIdIfdDefinitif() {
        return forExceptIdIfdDefinitif;
    }

    /**
     * Returns the forExceptSpecification.
     * 
     * @return java.lang.String
     */
    public java.lang.String getForExceptSpecification() {
        return forExceptSpecification;
    }

    /**
     * Returns the forExceptTypeDecision.
     * 
     * @return java.lang.String
     */
    public java.lang.String getForExceptTypeDecision() {
        return forExceptTypeDecision;
    }

    /**
     * Returns the forFinAffiliation.
     * 
     * @return java.lang.String
     */
    public java.lang.String getForFinAffiliation() {
        return forFinAffiliation;
    }

    /**
     * Returns the forFinDecision.
     * 
     * @return java.lang.String
     */
    public java.lang.String getForFinDecision() {
        return forFinDecision;
    }

    public java.lang.String getForGenreAffilie() {
        return forGenreAffilie;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.02.2003 16:21:11)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdAffiliation() {
        return forIdAffiliation;
    }

    public java.lang.String getForIdCommunication() {
        return forIdCommunication;
    }

    public java.lang.String getForIdConjoint() {
        return forIdConjoint;
    }

    /**
     * Getter
     */
    public java.lang.String getForIdDecision() {
        return forIdDecision;
    }

    public java.lang.String getForIdIfdDefintif() {
        return forIdIfdDefintif;
    }

    public java.lang.String getForIdIfdProvisoire() {
        return forIdIfdProvisoire;
    }

    public java.lang.String getForIdPassage() {
        return forIdPassage;
    }

    public java.lang.String getForIdTiers() {
        return forIdTiers;
    }

    /**
     * @return
     */
    public Boolean getForIsActive() {
        return forIsActive;
    }

    public Boolean getForIsComplementaire() {
        return forIsComplementaire;
    }

    public Boolean getForIsConjointNonVide() {
        return forIsConjointNonVide;
    }

    /**
     * @return
     */
    public Boolean getForIsFacturation() {
        return forIsFacturation;
    }

    /**
     * @return
     */
    public Boolean getForIsNotActive() {
        return forIsNotActive;
    }

    /**
     * @return
     */
    public Boolean getForIsNotFacturation() {
        return forIsNotFacturation;
    }

    /**
     * @return
     */
    public String getForLtIdIfdDefintif() {
        return forLtIdIfdDefintif;
    }

    /**
     * Returns the forNoAffilie.
     * 
     * @return java.lang.String
     */
    public java.lang.String getForNoAffilie() {
        return forNoAffilie;
    }

    public Boolean getForNotComplementaire() {
        return forNotComplementaire;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.03.2003 12:03:26)
     * 
     * @return string
     */
    public java.lang.String getForResponsable() {
        return forResponsable;
    }

    /**
     * Returns the forSpecification.
     * 
     * @return java.lang.String
     */
    public java.lang.String getForSpecification() {
        return forSpecification;
    }

    public java.lang.String getForTypeDecision() {
        return forTypeDecision;
    }

    /**
     * Slect depuis l'année de décision Date de création : (22.07.2003 07:58:10)
     * 
     * @return java.lang.String
     */
    public java.lang.String getFromAnneeDecision() {
        return fromAnneeDecision;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.07.2003 07:58:10)
     * 
     * @return java.lang.String
     */
    public java.lang.String getFromDateDebutDecision() {
        return fromDateDebutDecision;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.07.2003 07:57:45)
     * 
     * @return java.lang.String
     */
    public java.lang.String getFromDateFinDecision() {
        return fromDateFinDecision;
    }

    public String getFromEtat() {
        return fromEtat;
    }

    /**
     * Returns the fromFinAffiliation.
     * 
     * @return java.lang.String
     */
    public java.lang.String getFromFinAffiliation() {
        return fromFinAffiliation;
    }

    public java.lang.String getFromGtIdDecision() {
        return fromGtIdDecision;
    }

    public java.lang.String getFromIdDecision() {
        return fromIdDecision;
    }

    /**
     * @return
     */
    public java.lang.String getFromLtIdDecision() {
        return fromLtIdDecision;
    }

    /**
     * Returns the fromNoAffilie.
     * 
     * @return java.lang.String
     */
    public java.lang.String getFromNoAffilie() {
        return fromNoAffilie;
    }

    public String getInEtat() {
        return inEtat;
    }

    public java.lang.String getInGenreAffilie() {
        return inGenreAffilie;
    }

    /**
     * @return
     */
    public String getInTypeDecision() {
        return inTypeDecision;
    }

    public java.lang.String getNotInEtat() {
        return notInEtat;
    }

    public java.lang.String getNotInGenreAffilie() {
        return notInGenreAffilie;
    }

    public java.lang.String getNotInTypeDecision() {
        return notInTypeDecision;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.08.2003 09:54:35)
     * 
     * @return java.lang.String
     */
    public java.lang.String getOrder() {
        return order;
    }

    /**
     * Returns the tillNoAffilie.
     * 
     * @return java.lang.String
     */
    public java.lang.String getTillNoAffilie() {
        return tillNoAffilie;
    }

    /**
     * Returns the toAnneeDecision.
     * 
     * @return java.lang.String
     */
    public java.lang.String getToAnneeDecision() {
        return toAnneeDecision;
    }

    public java.lang.String getToDateDebutDecision() {
        return toDateDebutDecision;
    }

    /**
     * Returns the toDateFinDecision.
     * 
     * @return java.lang.String
     */
    public java.lang.String getToDateFinDecision() {
        return toDateFinDecision;
    }

    /**
     * Returns the toIdDecision.
     * 
     * @return java.lang.String
     */
    public java.lang.String getToIdDecision() {
        return toIdDecision;
    }

    public Boolean getUseManagerForRepriseCotPersIndependant() {
        return useManagerForRepriseCotPersIndependant;
    }

    public Boolean getUseManagerForRepriseCotPersIndependantHorsAcompte() {
        return useManagerForRepriseCotPersIndependantHorsAcompte;
    }

    public Boolean getUseManagerForRepriseCotPersNonActif() {
        return useManagerForRepriseCotPersNonActif;
    }

    public Boolean getUseManagerForRepriseCotPersNonActifHorsAcompte() {
        return useManagerForRepriseCotPersNonActifHorsAcompte;
    }

    /**
     * Tri par année de décision descendant. Date de création : (21.05.2002 13:28:35)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void orderByAnneeDecision() {
        if (JadeStringUtil.isEmpty(getOrder())) {
            setOrder("IAANNE DESC");
        } else {
            setOrder(getOrder() + ", IAANNE DESC");
        }
    }

    /**
     * Tri par année de décision ascendant Date de création : (21.05.2002 13:28:35)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void orderByAnneeDecisionAsc() {
        if (JadeStringUtil.isEmpty(getOrder())) {
            setOrder("IAANNE");
        } else {
            setOrder(getOrder() + ", IAANNE");
        }
    }

    /**
     * Tri par date de comptabilisation
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void orderByDateComptabilisation() {
        if (JadeStringUtil.isEmpty(getOrder())) {
            setOrder("IADFAC DESC");
        } else {
            setOrder(getOrder() + ", IADFAC DESC");
        }
    }

    /**
     * Tri par date de décision descendante. Date de création : (21.05.2002 13:28:35)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void orderByDateDecision() {
        if (JadeStringUtil.isEmpty(getOrder())) {
            setOrder("IADINF DESC");
        } else {
            setOrder(getOrder() + ", IADINF DESC");
        }
    }

    /**
     * Tri par date de décision ascendante. Date de création : (21.05.2002 13:28:35)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void orderByDateDecisionAsc() {
        if (JadeStringUtil.isEmpty(getOrder())) {
            setOrder("IADINF");
        } else {
            setOrder(getOrder() + ", IADINF");
        }
    }

    /**
     * Tri par id décision descendant. Date de création : (21.05.2002 13:28:35)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void orderByIdDecision() {
        if (JadeStringUtil.isEmpty(getOrder())) {
            setOrder("IAIDEC DESC");
        } else {
            setOrder(getOrder() + ", IAIDEC DESC");
        }
    }

    /**
     * Tri par id décision ascendant Date de création : (21.05.2002 13:28:35)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void orderByIdDecisionAsc() {
        if (JadeStringUtil.isEmpty(getOrder())) {
            setOrder("IAIDEC");
        } else {
            setOrder(getOrder() + ", IAIDEC");
        }
    }

    /**
     * Tri par idTiers. Date de création : (21.05.2002 13:28:35)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void orderByIdTiers() {
        if (JadeStringUtil.isEmpty(getOrder())) {
            setOrder("HTITIE");
        } else {
            setOrder(getOrder() + ", HTITIE");
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.05.2002 13:28:35)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void orderByNoAffilie() {
        if (JadeStringUtil.isEmpty(getOrder())) {
            setOrder("MALNAF ASC");
        } else {
            setOrder(getOrder() + ", MALNAF ASC");
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.04.2003 13:17:03)
     * 
     * @param newForAnneeDecision
     *            java.lang.String
     */
    public void setForAnneeDecision(java.lang.String newForAnneeDecision) {
        forAnneeDecision = newForAnneeDecision;
    }

    /**
     * Sets the forDateInformation.
     * 
     * @param forDateInformation
     *            The forDateInformation to set
     */
    public void setForDateInformation(java.lang.String forDateInformation) {
        this.forDateInformation = forDateInformation;
    }

    /**
     * Sets the forDebutDecision.
     * 
     * @param forDebutDecision
     *            The forDebutDecision to set
     */
    public void setForDebutDecision(java.lang.String forDebutDecision) {
        this.forDebutDecision = forDebutDecision;
    }

    public void setForEtat(String forEtat) {
        this.forEtat = forEtat;
    }

    /**
     * Sets the forExceptGenreAffilie.
     * 
     * @param forExceptGenreAffilie
     *            The forExceptGenreAffilie to set
     */
    public void setForExceptGenreAffilie(java.lang.String forExceptGenreAffilie) {
        this.forExceptGenreAffilie = forExceptGenreAffilie;
    }

    public void setForExceptIdCommunication(java.lang.String forExceptIdCommunication) {
        this.forExceptIdCommunication = forExceptIdCommunication;
    }

    /**
     * @param string
     */
    public void setForExceptIdDecision(String string) {
        forExceptIdDecision = string;
    }

    public void setForExceptIdIfdDefinitif(String forExceptIdIfdDefinitif) {
        this.forExceptIdIfdDefinitif = forExceptIdIfdDefinitif;
    }

    /**
     * Sets the forExceptSpecification.
     * 
     * @param forExceptSpecification
     *            The forExceptSpecification to set
     */
    public void setForExceptSpecification(java.lang.String forExceptSpecification) {
        this.forExceptSpecification = forExceptSpecification;
    }

    /**
     * Sets the forExceptTypeDecision.
     * 
     * @param forExceptTypeDecision
     *            The forExceptTypeDecision to set
     */
    public void setForExceptTypeDecision(java.lang.String forExceptTypeDecision) {
        this.forExceptTypeDecision = forExceptTypeDecision;
    }

    /**
     * Sets the forFinAffiliation.
     * 
     * @param forFinAffiliation
     *            The forFinAffiliation to set
     */
    public void setForFinAffiliation(java.lang.String forFinAffiliation) {
        this.forFinAffiliation = forFinAffiliation;
    }

    /**
     * Sets the forFinDecision.
     * 
     * @param forFinDecision
     *            The forFinDecision to set
     */
    public void setForFinDecision(java.lang.String forFinDecision) {
        this.forFinDecision = forFinDecision;
    }

    public void setForGenreAffilie(java.lang.String newForGenreAffilie) {
        forGenreAffilie = newForGenreAffilie;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.02.2003 16:21:11)
     * 
     * @param newForIdAffiliation
     *            java.lang.String
     */
    public void setForIdAffiliation(java.lang.String newForIdAffiliation) {
        forIdAffiliation = newForIdAffiliation;
    }

    public void setForIdCommunication(java.lang.String newForIdCommunication) {
        forIdCommunication = newForIdCommunication;
    }

    public void setForIdConjoint(java.lang.String newForIdConjoint) {
        forIdConjoint = newForIdConjoint;
    }

    /**
     * Setter
     */
    public void setForIdDecision(java.lang.String newForIdDecision) {
        forIdDecision = newForIdDecision;
    }

    public void setForIdIfdDefintif(java.lang.String newForIdIfdDefintif) {
        forIdIfdDefintif = newForIdIfdDefintif;
    }

    public void setForIdIfdProvisoire(java.lang.String newForIdIfdProvisoire) {
        forIdIfdProvisoire = newForIdIfdProvisoire;
    }

    public void setForIdPassage(java.lang.String newForIdPassage) {
        forIdPassage = newForIdPassage;
    }

    public void setForIdTiers(java.lang.String newForIdTiers) {
        forIdTiers = newForIdTiers;
    }

    /**
     * @param boolean1
     */
    public void setForIsActive(Boolean boolean1) {
        forIsActive = boolean1;
    }

    public void setForIsComplementaire(Boolean forIsComplementaire) {
        this.forIsComplementaire = forIsComplementaire;
    }

    public void setForIsConjointNonVide(Boolean forIsConjointNonVide) {
        this.forIsConjointNonVide = forIsConjointNonVide;
    }

    /**
     * @param boolean1
     */
    public void setForIsFacturation(Boolean boolean1) {
        forIsFacturation = boolean1;
    }

    /**
     * @param boolean1
     */
    public void setForIsNotActive(Boolean boolean1) {
        forIsNotActive = boolean1;
    }

    /**
     * @param boolean1
     */
    public void setForIsNotFacturation(Boolean boolean1) {
        forIsNotFacturation = boolean1;
    }

    /**
     * @param string
     */
    public void setForLtIdIfdDefintif(String string) {
        forLtIdIfdDefintif = string;
    }

    /**
     * Sets the forNoAffilie.
     * 
     * @param forNoAffilie
     *            The forNoAffilie to set
     */
    public void setForNoAffilie(java.lang.String forNoAffilie) {
        this.forNoAffilie = forNoAffilie;
    }

    public void setForNotComplementaire(Boolean forNotComplementaire) {
        this.forNotComplementaire = forNotComplementaire;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.03.2003 12:03:26)
     * 
     * @param newForResponsable
     *            string
     */
    public void setForResponsable(java.lang.String newForResponsable) {
        forResponsable = newForResponsable;
    }

    /**
     * Sets the forSpecification.
     * 
     * @param forSpecification
     *            The forSpecification to set
     */
    public void setForSpecification(java.lang.String forSpecification) {
        this.forSpecification = forSpecification;
    }

    public void setForTypeDecision(java.lang.String newForTypeDecision) {
        forTypeDecision = newForTypeDecision;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.07.2003 07:58:10)
     * 
     * @param newFromAnneeDecision
     *            java.lang.String
     */
    public void setFromAnneeDecision(java.lang.String newFromAnneeDecision) {
        fromAnneeDecision = newFromAnneeDecision;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.07.2003 07:58:10)
     * 
     * @param newFromDateDebutDecision
     *            java.lang.String
     */
    public void setFromDateDebutDecision(java.lang.String newFromDateDebutDecision) {
        fromDateDebutDecision = newFromDateDebutDecision;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.07.2003 07:57:45)
     * 
     * @param newFromDateFinDecision
     *            java.lang.String
     */
    public void setFromDateFinDecision(java.lang.String newFromDateFinDecision) {
        fromDateFinDecision = newFromDateFinDecision;
    }

    public void setFromEtat(String fromEtat) {
        this.fromEtat = fromEtat;
    }

    /**
     * Sets the fromFinAffiliation.
     * 
     * @param fromFinAffiliation
     *            The fromFinAffiliation to set
     */
    public void setFromFinAffiliation(java.lang.String fromFinAffiliation) {
        this.fromFinAffiliation = fromFinAffiliation;
    }

    public void setFromGtIdDecision(java.lang.String fromGtIdDecision) {
        this.fromGtIdDecision = fromGtIdDecision;
    }

    public void setFromIdDecision(java.lang.String newFromIdDecision) {
        fromIdDecision = newFromIdDecision;
    }

    /**
     * @param string
     */
    public void setFromLtIdDecision(java.lang.String string) {
        fromLtIdDecision = string;
    }

    /**
     * Sets the fromNoAffilie.
     * 
     * @param fromNoAffilie
     *            The fromNoAffilie to set
     */
    public void setFromNoAffilie(java.lang.String fromNoAffilie) {
        this.fromNoAffilie = fromNoAffilie;
    }

    public void setInEtat(String inEtat) {
        this.inEtat = inEtat;
    }

    public void setInGenreAffilie(java.lang.String inGenreAffilie) {
        this.inGenreAffilie = inGenreAffilie;
    }

    /**
     * @param string
     */
    public void setInTypeDecision(String string) {
        inTypeDecision = string;
    }

    public void setNotInEtat(java.lang.String notInEtat) {
        this.notInEtat = notInEtat;
    }

    public void setNotInGenreAffilie(java.lang.String notInGenreAffilie) {
        this.notInGenreAffilie = notInGenreAffilie;
    }

    public void setNotInTypeDecision(java.lang.String notInTypeDecision) {
        this.notInTypeDecision = notInTypeDecision;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.08.2003 09:54:35)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void setOrder(java.lang.String newOrder) {
        order = newOrder;
    }

    /**
     * Sets the tillNoAffilie.
     * 
     * @param tillNoAffilie
     *            The tillNoAffilie to set
     */
    public void setTillNoAffilie(java.lang.String tillNoAffilie) {
        this.tillNoAffilie = tillNoAffilie;
    }

    /**
     * Sets the toAnneeDecision.
     * 
     * @param toAnneeDecision
     *            The toAnneeDecision to set
     */
    public void setToAnneeDecision(String toAnneeDecision) {
        this.toAnneeDecision = toAnneeDecision;
    }

    public void setToDateDebutDecision(java.lang.String toDateDebutDecision) {
        this.toDateDebutDecision = toDateDebutDecision;
    }

    /**
     * Sets the toDateFinDecision.
     * 
     * @param toDateFinDecision
     *            The toDateFinDecision to set
     */
    public void setToDateFinDecision(java.lang.String toDateFinDecision) {
        this.toDateFinDecision = toDateFinDecision;
    }

    /**
     * Sets the toIdDecision.
     * 
     * @param toIdDecision
     *            The toIdDecision to set
     */
    public void setToIdDecision(java.lang.String toIdDecision) {
        this.toIdDecision = toIdDecision;
    }

    public void setUseManagerForRepriseCotPersIndependant(Boolean useManagerForRepriseCotPersIndependant) {
        this.useManagerForRepriseCotPersIndependant = useManagerForRepriseCotPersIndependant;
    }

    public void setUseManagerForRepriseCotPersIndependantHorsAcompte(
            Boolean useManagerForRepriseCotPersIndependantHorsAcompte) {
        this.useManagerForRepriseCotPersIndependantHorsAcompte = useManagerForRepriseCotPersIndependantHorsAcompte;
    }

    public void setUseManagerForRepriseCotPersNonActif(Boolean useManagerForRepriseCotPersNonActif) {
        this.useManagerForRepriseCotPersNonActif = useManagerForRepriseCotPersNonActif;
    }

    public void setUseManagerForRepriseCotPersNonActifHorsAcompte(Boolean useManagerForRepriseCotPersNonActifHorsAcompte) {
        this.useManagerForRepriseCotPersNonActifHorsAcompte = useManagerForRepriseCotPersNonActifHorsAcompte;
    }

    public String getForExceptEtatDecision() {
        return forExceptEtatDecision;
    }

    public void setForExceptEtatDecision(String forExceptEtatDecision) {
        this.forExceptEtatDecision = forExceptEtatDecision;
    }
}
