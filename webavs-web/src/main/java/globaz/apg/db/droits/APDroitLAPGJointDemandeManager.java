/*
 * Créé le 9 mai 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.apg.db.droits;

import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.format.IFormatData;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.application.PRAbstractApplication;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.interfaces.tiers.IPRTiers;
import globaz.prestation.tools.PRAbstractManagerHierarchique;
import globaz.prestation.tools.PRStringUtils;
import globaz.prestation.tools.nnss.PRNSSUtil;

/**
 * DOCUMENT ME!
 * 
 * @author vre
 */
public class APDroitLAPGJointDemandeManager extends PRAbstractManagerHierarchique {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** DOCUMENT ME! */
    public static final String CLE_DROITS_TOUS = "DROITS_TOUS";
    public static final String CLE_GENRE_TOUS = "GENRE_TOUS";

    /** DOCUMENT ME! */
    public static final String CLE_NON_DEFINITIFS = "DROITS_NON_DEFINITIFS";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String forCsSexe = "";

    private String forDateNaissance = "";
    private String forDroitContenuDansDateDebut = "";
    private String forDroitContenuDansDateFin = "";
    private String forEtatDemande = "";
    private String forEtatDroit = "";
    private String forGenreService = "";
    private String forIdDroit = "";
    private String forIdGestionnaire = "";
    private String forIdTiers = "";
    private String forTypeDemande = "";
    private transient String fromClause = null;
    private String fromDateDebutDroit = "";
    private String fromNom = "";
    private String fromNumeroAVS = "";
    private String fromPrenom = "";
    private String likeNom = "";
    private String likeNumeroAVS = "";

    private String likeNumeroAVSNNSS = "";
    private String likePrenom = "";
    private String notForGenreService = "";
    private String forGenreServiceListDroit = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            StringBuffer from = new StringBuffer(APDroitLAPGJointDemande.createFromClause(_getCollection()));

            if ((!JadeStringUtil.isBlankOrZero(likeNumeroAVSNNSS) && !JadeStringUtil.isEmpty(getLikeNumeroAVS()))) {
                from.append(" LEFT JOIN " + _getCollection() + IPRTiers.TABLE_AVS_HIST + " AS "
                        + IPRTiers.TABLE_AVS_HIST + " ON (" + _getCollection() + IPRTiers.TABLE_AVS + "."
                        + IPRTiers.FIELD_TI_IDTIERS + " = " + IPRTiers.TABLE_AVS_HIST + "." + IPRTiers.FIELD_TI_IDTIERS
                        + ")");
            }

            fromClause = from.toString();
        }

        return fromClause;
    }

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";
        String schema = _getCollection();

        if ((!JadeStringUtil.isBlankOrZero(likeNumeroAVSNNSS) && !JadeStringUtil.isEmpty(getLikeNumeroAVS()))) {

            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += PRNSSUtil.getWhereNSS(_getCollection(), getLikeNumeroAVS(), getLikeNumeroAVSNNSS());
        }

        if (!JAUtil.isDateEmpty(forDateNaissance)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema + APDroitLAPGJointDemande.TABLE_TIERS_DETAIL + "."
                    + APDroitLAPGJointDemande.FIELDNAME_DATE_NAISSANCE + "="
                    + _dbWriteDateAMJ(statement.getTransaction(), forDateNaissance);
        }

        if (!JAUtil.isDateEmpty(forCsSexe)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema + APDroitLAPGJointDemande.TABLE_TIERS_DETAIL + "."
                    + APDroitLAPGJointDemande.FIELDNAME_SEXE + "="
                    + _dbWriteNumeric(statement.getTransaction(), forCsSexe);
        }

        if (!JadeStringUtil.isEmpty(forEtatDemande)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema + PRDemande.TABLE_NAME + "." + PRDemande.FIELDNAME_ETAT + "="
                    + _dbWriteNumeric(statement.getTransaction(), forEtatDemande);
        }

        if (!JadeStringUtil.isEmpty(forEtatDroit) && !CLE_DROITS_TOUS.equals(forEtatDroit)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            if (CLE_NON_DEFINITIFS.equals(forEtatDroit)) {
                // Les droits non définitifs sont les droits dont l'état n'est pas égale à : DEFINITIF, REFUSER et TRANFERE.
                sqlWhere += schema + APDroitLAPG.TABLE_NAME_LAPG + "." + APDroitLAPG.FIELDNAME_ETAT + "<>"
                        + _dbWriteNumeric(statement.getTransaction(), IAPDroitLAPG.CS_ETAT_DROIT_DEFINITIF);
                sqlWhere += " AND ";
                sqlWhere += schema + APDroitLAPG.TABLE_NAME_LAPG + "." + APDroitLAPG.FIELDNAME_ETAT + "<>"
                        + _dbWriteNumeric(statement.getTransaction(), IAPDroitLAPG.CS_ETAT_DROIT_REFUSE);
                sqlWhere += " AND ";
                sqlWhere += schema + APDroitLAPG.TABLE_NAME_LAPG + "." + APDroitLAPG.FIELDNAME_ETAT + "<>"
                        + _dbWriteNumeric(statement.getTransaction(), IAPDroitLAPG.CS_ETAT_DROIT_TRANSFERE);
            } else {
                sqlWhere += schema + APDroitLAPG.TABLE_NAME_LAPG + "." + APDroitLAPG.FIELDNAME_ETAT + "="
                        + _dbWriteNumeric(statement.getTransaction(), forEtatDroit);
            }
        }

        if (!JadeStringUtil.isEmpty(forIdGestionnaire)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema + APDroitLAPG.TABLE_NAME_LAPG + "." + APDroitLAPG.FIELDNAME_IDGESTIONNAIRE + "="
                    + _dbWriteString(statement.getTransaction(), forIdGestionnaire);
        }

        if (!JadeStringUtil.isEmpty(forTypeDemande)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema + PRDemande.TABLE_NAME + "." + PRDemande.FIELDNAME_TYPE_DEMANDE + "="
                    + _dbWriteNumeric(statement.getTransaction(), forTypeDemande);
        }

        if (!JAUtil.isDateEmpty(fromDateDebutDroit)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema + APDroitLAPG.TABLE_NAME_LAPG + "." + APDroitLAPG.FIELDNAME_DATEDEBUTDROIT + ">="
                    + _dbWriteDateAMJ(statement.getTransaction(), fromDateDebutDroit);
        }

        if (!JadeStringUtil.isEmpty(fromNumeroAVS)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            // formatage de l'AVS

            String avs = null;

            try {
                IFormatData avsFormater = PRAbstractApplication.getAvsFormater();

                if (avsFormater != null) {
                    avs = avsFormater.format(fromNumeroAVS);
                }
            } catch (Exception e1) {
                avs = fromNumeroAVS;
            }

            sqlWhere += schema + APDroitLAPGJointDemande.TABLE_AVS + "." + APDroitLAPGJointDemande.FIELDNAME_NUM_AVS
                    + ">=" + _dbWriteString(statement.getTransaction(), avs);
        }

        if (!JadeStringUtil.isEmpty(fromNom)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema + APDroitLAPGJointDemande.TABLE_TIERS + "."
                    + APDroitLAPGJointDemande.FIELDNAME_NOM_FOR_SEARCH + ">="
                    + _dbWriteString(statement.getTransaction(), PRStringUtils.upperCaseWithoutSpecialChars(fromNom));
        }

        if (!JadeStringUtil.isEmpty(fromPrenom)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema
                    + APDroitLAPGJointDemande.TABLE_TIERS
                    + "."
                    + APDroitLAPGJointDemande.FIELDNAME_PRENOM_FOR_SEARCH
                    + ">="
                    + _dbWriteString(statement.getTransaction(), PRStringUtils.upperCaseWithoutSpecialChars(fromPrenom));
        }

        if (!JadeStringUtil.isEmpty(likeNom)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema
                    + APDroitLAPGJointDemande.TABLE_TIERS
                    + "."
                    + APDroitLAPGJointDemande.FIELDNAME_NOM_FOR_SEARCH
                    + " LIKE "
                    + _dbWriteString(statement.getTransaction(), PRStringUtils.upperCaseWithoutSpecialChars(likeNom)
                            + "%");
        }

        if (!JadeStringUtil.isEmpty(likePrenom)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema
                    + APDroitLAPGJointDemande.TABLE_TIERS
                    + "."
                    + APDroitLAPGJointDemande.FIELDNAME_PRENOM_FOR_SEARCH
                    + " LIKE "
                    + _dbWriteString(statement.getTransaction(), PRStringUtils.upperCaseWithoutSpecialChars(likePrenom)
                            + "%");
        }

        if (!JadeStringUtil.isEmpty(forIdTiers)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema + PRDemande.TABLE_NAME + "." + PRDemande.FIELDNAME_IDTIERS + "="
                    + _dbWriteNumeric(statement.getTransaction(), forIdTiers);
        }

        if (!JAUtil.isDateEmpty(forDroitContenuDansDateDebut) && !JAUtil.isDateEmpty(forDroitContenuDansDateFin)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += " (( " + schema + APDroitLAPG.TABLE_NAME_LAPG + "." + APDroitLAPG.FIELDNAME_DATEDEBUTDROIT
                    + ">=" + _dbWriteDateAMJ(statement.getTransaction(), forDroitContenuDansDateDebut) +

                    " AND " +

                    schema + APDroitLAPG.TABLE_NAME_LAPG + "." + APDroitLAPG.FIELDNAME_DATEDEBUTDROIT + "<="
                    + _dbWriteDateAMJ(statement.getTransaction(), forDroitContenuDansDateFin) + " ) " +

                    " OR " +

                    " ( " + schema + APDroitLAPG.TABLE_NAME_LAPG + "." + APDroitLAPG.FIELDNAME_DATEFINDROIT + ">="
                    + _dbWriteDateAMJ(statement.getTransaction(), forDroitContenuDansDateDebut) +

                    " AND " +

                    schema + APDroitLAPG.TABLE_NAME_LAPG + "." + APDroitLAPG.FIELDNAME_DATEFINDROIT + "<="
                    + _dbWriteDateAMJ(statement.getTransaction(), forDroitContenuDansDateFin) + " ) " +

                    " OR " +

                    " ( " + schema + APDroitLAPG.TABLE_NAME_LAPG + "." + APDroitLAPG.FIELDNAME_DATEDEBUTDROIT + "<="
                    + _dbWriteDateAMJ(statement.getTransaction(), forDroitContenuDansDateDebut) +

                    " AND " +

                    schema + APDroitLAPG.TABLE_NAME_LAPG + "." + APDroitLAPG.FIELDNAME_DATEFINDROIT + ">="
                    + _dbWriteDateAMJ(statement.getTransaction(), forDroitContenuDansDateFin) + " )) ";
        }

        if (!JadeStringUtil.isEmpty(forGenreServiceListDroit) && !CLE_GENRE_TOUS.equals(forGenreServiceListDroit)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema + APDroitLAPG.TABLE_NAME_LAPG + "." + APDroitLAPG.FIELDNAME_GENRESERVICE + "="
                    + _dbWriteNumeric(statement.getTransaction(), forGenreServiceListDroit);
        }

        if (!JadeStringUtil.isEmpty(forGenreService)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema + APDroitLAPG.TABLE_NAME_LAPG + "." + APDroitLAPG.FIELDNAME_GENRESERVICE + "="
                    + _dbWriteNumeric(statement.getTransaction(), forGenreService);
        }

        if (!JadeStringUtil.isEmpty(notForGenreService)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema + APDroitLAPG.TABLE_NAME_LAPG + "." + APDroitLAPG.FIELDNAME_GENRESERVICE + "<>"
                    + _dbWriteNumeric(statement.getTransaction(), notForGenreService);
        }

        if (!JadeStringUtil.isEmpty(forIdDroit)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema + APDroitLAPG.TABLE_NAME_LAPG + "." + APDroitLAPG.FIELDNAME_IDDROIT_LAPG + "="
                    + _dbWriteNumeric(statement.getTransaction(), forIdDroit);
        }

        return sqlWhere;
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new APDroitLAPGJointDemande();
    }

    /**
     * @return
     */
    public String getForCsSexe() {
        return forCsSexe;
    }

    /**
     * @return
     */
    public String getForDateNaissance() {
        return forDateNaissance;
    }

    /**
     * @return
     */
    public String getForDroitContenuDansDateDebut() {
        return forDroitContenuDansDateDebut;
    }

    /**
     * @return
     */
    public String getForDroitContenuDansDateFin() {
        return forDroitContenuDansDateFin;
    }

    /**
     * getter pour l'attribut for etat demande
     * 
     * @return la valeur courante de l'attribut for etat demande
     */
    public String getForEtatDemande() {
        return forEtatDemande;
    }

    /**
     * getter pour l'attribut for etat droit
     * 
     * @return la valeur courante de l'attribut for etat droit
     */
    public String getForEtatDroit() {
        return forEtatDroit;
    }

    /**
     * @return
     */
    public String getForGenreService() {
        return forGenreService;
    }

    public String getForIdDroit() {
        return forIdDroit;
    }

    /**
     * getter pour l'attribut for id gestionnaire
     * 
     * @return la valeur courante de l'attribut for id gestionnaire
     */
    public String getForIdGestionnaire() {
        return forIdGestionnaire;
    }

    /**
     * @return
     */
    public String getForIdTiers() {
        return forIdTiers;
    }

    /**
     * getter pour l'attribut for type demande
     * 
     * @return la valeur courante de l'attribut for type demande
     */
    public String getForTypeDemande() {
        return forTypeDemande;
    }

    /**
     * getter pour l'attribut from date debut droit
     * 
     * @return la valeur courante de l'attribut from date debut droit
     */
    public String getFromDateDebutDroit() {
        return fromDateDebutDroit;
    }

    /**
     * getter pour l'attribut from nom
     * 
     * @return la valeur courante de l'attribut from nom
     */
    public String getFromNom() {
        return fromNom;
    }

    /**
     * getter pour l'attribut fromr numero AVS
     * 
     * @return la valeur courante de l'attribut fromr numero AVS
     */
    public String getFromNumeroAVS() {
        return fromNumeroAVS;
    }

    /**
     * getter pour l'attribut from prenom
     * 
     * @return la valeur courante de l'attribut from prenom
     */
    public String getFromPrenom() {
        return fromPrenom;
    }

    @Override
    public String getHierarchicalOrderBy() {
        return getOrderByDefaut();
    }

    /**
     * @return
     */
    public String getLikeNom() {
        return likeNom;
    }

    /**
     * @return
     */
    public String getLikeNumeroAVS() {
        return likeNumeroAVS;
    }

    /**
     * @return
     */
    public String getLikeNumeroAVSNNSS() {
        return likeNumeroAVSNNSS;
    }

    /**
     * @return
     */
    public String getLikePrenom() {
        return likePrenom;
    }

    public String getNotForGenreService() {
        return notForGenreService;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.prestation.db.PRAbstractManager#getOrderByDefaut()
     * 
     * @return la valeur courante de l'attribut order by defaut
     */
    @Override
    public String getOrderByDefaut() {
        if ((!JadeStringUtil.isBlankOrZero(getLikeNumeroAVSNNSS()) && !JadeStringUtil.isEmpty(getLikeNumeroAVS()))) {
            return APDroitLAPG.FIELDNAME_IDDROIT_LAPG + PRNSSUtil.SECONDARY_ORDER_BY;
        } else {
            return APDroitLAPG.FIELDNAME_IDDROIT_LAPG;
        }
    }

    /**
     * @param string
     */
    public void setForCsSexe(String string) {
        forCsSexe = string;
    }

    /**
     * @param string
     */
    public void setForDateNaissance(String string) {
        forDateNaissance = string;
    }

    /**
     * @param string
     */
    public void setForDroitContenuDansDateDebut(String string) {
        forDroitContenuDansDateDebut = string;
    }

    /**
     * @param string
     */
    public void setForDroitContenuDansDateFin(String string) {
        forDroitContenuDansDateFin = string;
    }

    /**
     * setter pour l'attribut for etat demande
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForEtatDemande(String string) {
        forEtatDemande = string;
    }

    /**
     * setter pour l'attribut for etat droit
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForEtatDroit(String string) {
        forEtatDroit = string;
    }

    /**
     * @param string
     */
    public void setForGenreService(String string) {
        forGenreService = string;
    }

    public void setForIdDroit(String forIdDroit) {
        this.forIdDroit = forIdDroit;
    }

    /**
     * setter pour l'attribut for id gestionnaire
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdGestionnaire(String string) {
        forIdGestionnaire = string;
    }

    /**
     * @param string
     */
    public void setForIdTiers(String string) {
        forIdTiers = string;
    }

    /**
     * setter pour l'attribut for type demande
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForTypeDemande(String string) {
        forTypeDemande = string;
    }

    /**
     * setter pour l'attribut from date debut droit
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setFromDateDebutDroit(String string) {
        fromDateDebutDroit = string;
    }

    /**
     * setter pour l'attribut from nom
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setFromNom(String string) {
        fromNom = string;
    }

    /**
     * setter pour l'attribut fromr numero AVS
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setFromNumeroAVS(String string) {
        fromNumeroAVS = string;
    }

    /**
     * setter pour l'attribut from prenom
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setFromPrenom(String string) {
        fromPrenom = string;
    }

    /**
     * @param string
     */
    public void setLikeNom(String string) {
        likeNom = string;
    }

    /**
     * @param string
     */
    public void setLikeNumeroAVS(String string) {
        likeNumeroAVS = string;
    }

    /**
     * @param string
     */
    public void setLikeNumeroAVSNNSS(String string) {
        likeNumeroAVSNNSS = string;
    }

    /**
     * @param string
     */
    public void setLikePrenom(String string) {
        likePrenom = string;
    }

    public void setNotForGenreService(String notForGenreService) {
        this.notForGenreService = notForGenreService;
    }

    public String getForGenreServiceListDroit() {
        return forGenreServiceListDroit;
    }

    public void setForGenreServiceListDroit(String forGenreServiceListDroit) {
        this.forGenreServiceListDroit = forGenreServiceListDroit;
    }
}
