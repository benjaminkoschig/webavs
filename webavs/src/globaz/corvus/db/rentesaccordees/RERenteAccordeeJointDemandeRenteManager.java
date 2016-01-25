/*
 * Créé le 16 fevr. 07
 */
package globaz.corvus.db.rentesaccordees;

import globaz.corvus.db.basescalcul.REBasesCalcul;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.demandes.REDemandeRenteJointDemande;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;
import globaz.prestation.interfaces.tiers.IPRTiers;
import globaz.prestation.tools.PRDateFormater;
import globaz.prestation.tools.PRStringUtils;
import globaz.prestation.tools.nnss.PRNSSUtil;

/**
 * @author bsc
 * 
 * 
 *         scr notes :
 * @deprecated Ne devrait plus être utilisé, car incohérence dans la recherche suivant les paramètre fourni. La
 *             recherche par nss ses fait pour le tiers ayant ouvert la demande au lieu du tiers bénéficaire de la rente
 *             accordée. En conséquent, si la recherche se fait par nss, il est impératif d'avoir également une
 *             recherche par no de demande, sans quoi le résultat retourné ne sera pas correct. A remplacé par
 *             {@link RERenteAccJoinTblTiersJoinDemRenteManager}.
 */

@Deprecated
public class RERenteAccordeeJointDemandeRenteManager extends PRAbstractManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String forCsEtat = "";

    private String forCsEtatDifferentDe = "";
    private String forCsEtatIn = "";
    private String forCsSexe = "";
    private String forCsType = "";
    private String forDateNaissance = "";
    private String forGenrePrestation = "";
    private String forIdTiersBeneficiaire = "";
    private String forMoisFinRANotEmptyAndHigherOrEgal = "";
    private String forNoBaseCalcul = "";
    private String forNoDemandeRente = "";
    private transient String fromClause = null;
    private String likeNom = "";
    private String likeNumeroAVS = "";

    private String likeNumeroAVSNNSS = "";
    private String likePrenom = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {

        if (fromClause == null) {
            StringBuffer from = new StringBuffer(RERenteAccordeeJointDemandeRente.createFromClause(_getCollection()));

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
     * Renvoie la clause WHERE de la requête SQL
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */

    @Override
    protected String _getWhere(BStatement statement) {

        String sqlWhere = "";
        String schema = _getCollection();

        // Ce manager est fait ainsi... si recherche par nss, il s'agit du nss
        // de l'assuré, celui ayant ouvert
        // la demande de rente -> dans ce cas, le no de demande de rente doit
        // obligatoirement être renseigné
        // faute de quoi, le manager ne retournera pas tous les résultat.

        if ((!JadeStringUtil.isBlankOrZero(likeNumeroAVSNNSS) && !JadeStringUtil.isEmpty(getLikeNumeroAVS()))) {

            if (JadeStringUtil.isBlankOrZero(getForNoDemandeRente())) {
                statement.getTransaction().addErrors(getSession().getLabel("ERREUR_NO_DEMANDE_PARTIE_RECHERCHE"));
            }
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += PRNSSUtil.getWhereNSS(_getCollection(), getLikeNumeroAVS(), getLikeNumeroAVSNNSS());
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdTiersBeneficiaire)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema + REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES + "."
                    + REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE + " = "
                    + this._dbWriteNumeric(statement.getTransaction(), forIdTiersBeneficiaire);

        }

        if (!JadeStringUtil.isEmpty(forDateNaissance)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema + REDemandeRenteJointDemande.TABLE_PERSONNE + "."
                    + REDemandeRenteJointDemande.FIELDNAME_DATENAISSANCE + "="
                    + this._dbWriteDateAMJ(statement.getTransaction(), forDateNaissance);

        }

        if (!JadeStringUtil.isEmpty(forGenrePrestation)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema + REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES + "."
                    + REPrestationsAccordees.FIELDNAME_CODE_PRESTATION + "="
                    + this._dbWriteString(statement.getTransaction(), forGenrePrestation);
        }

        if (!JadeStringUtil.isEmpty(forCsSexe)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema + REDemandeRenteJointDemande.TABLE_PERSONNE + "."
                    + REDemandeRenteJointDemande.FIELDNAME_SEXE + "="
                    + this._dbWriteNumeric(statement.getTransaction(), forCsSexe);
        }

        if (!JadeStringUtil.isEmpty(forCsType)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema + REDemandeRente.TABLE_NAME_DEMANDE_RENTE + "."
                    + REDemandeRente.FIELDNAME_CS_TYPE_DEMANDE_RENTE + "="
                    + this._dbWriteNumeric(statement.getTransaction(), forCsType);
        }

        if (!JadeStringUtil.isEmpty(likeNom)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema
                    + REDemandeRenteJointDemande.TABLE_TIERS
                    + "."
                    + REDemandeRenteJointDemande.FIELDNAME_NOM_FOR_SEARCH
                    + " like "
                    + this._dbWriteString(statement.getTransaction(),
                            PRStringUtils.upperCaseWithoutSpecialChars(likeNom) + "%");
        }

        if (!JadeStringUtil.isEmpty(likePrenom)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema
                    + REDemandeRenteJointDemande.TABLE_TIERS
                    + "."
                    + REDemandeRenteJointDemande.FIELDNAME_PRENOM_FOR_SEARCH
                    + " like "
                    + this._dbWriteString(statement.getTransaction(),
                            PRStringUtils.upperCaseWithoutSpecialChars(likePrenom) + "%");
        }

        if (!JadeStringUtil.isEmpty(forCsEtat)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema + REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES + "."
                    + REPrestationsAccordees.FIELDNAME_CS_ETAT + "="
                    + this._dbWriteNumeric(statement.getTransaction(), forCsEtat);

        }

        if (!JadeStringUtil.isEmpty(forCsEtatDifferentDe)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema + REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES + "."
                    + REPrestationsAccordees.FIELDNAME_CS_ETAT + "<>"
                    + this._dbWriteNumeric(statement.getTransaction(), forCsEtatDifferentDe);

        }

        if (!JadeStringUtil.isIntegerEmpty(forNoDemandeRente)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema + REDemandeRente.TABLE_NAME_DEMANDE_RENTE + "."
                    + REDemandeRente.FIELDNAME_ID_DEMANDE_RENTE + "="
                    + this._dbWriteNumeric(statement.getTransaction(), forNoDemandeRente);
        }

        if (!JadeStringUtil.isBlank(forNoBaseCalcul)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema + REBasesCalcul.TABLE_NAME_BASES_CALCUL + "."
                    + REBasesCalcul.FIELDNAME_ID_BASES_DE_CALCUL + "="
                    + this._dbWriteNumeric(statement.getTransaction(), forNoBaseCalcul);
        }

        if (!JadeStringUtil.isEmpty(forMoisFinRANotEmptyAndHigherOrEgal)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "(("
                    + _getCollection()
                    + REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES
                    + "."
                    + REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT
                    + " >= "
                    + this._dbWriteNumeric(statement.getTransaction(),
                            PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(forMoisFinRANotEmptyAndHigherOrEgal)) + ")";

            sqlWhere += " OR ";

            sqlWhere += "(" + _getCollection() + REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES + "."
                    + REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT + " = 0 ))";
        }

        if (!JadeStringUtil.isEmpty(forCsEtatIn)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES + "."
                    + REPrestationsAccordees.FIELDNAME_CS_ETAT + " IN (" + forCsEtatIn + " )";
        }

        return sqlWhere;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new RERenteAccordeeJointDemandeRente();
    }

    /**
     * @return
     */
    public String getForCsEtat() {
        return forCsEtat;
    }

    /**
     * @return the forCsEtatDifferentDe
     */
    public String getForCsEtatDifferentDe() {
        return forCsEtatDifferentDe;
    }

    public String getForCsEtatIn() {
        return forCsEtatIn;
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
    public String getForCsType() {
        return forCsType;
    }

    /**
     * @return
     */
    public String getForDateNaissance() {
        return forDateNaissance;
    }

    public String getForGenrePrestation() {
        return forGenrePrestation;
    }

    public String getForIdTiersBeneficiaire() {
        return forIdTiersBeneficiaire;
    }

    public String getForMoisFinRANotEmptyAndHigherOrEgal() {
        return forMoisFinRANotEmptyAndHigherOrEgal;
    }

    public String getForNoBaseCalcul() {
        return forNoBaseCalcul;
    }

    /**
     * @return
     */
    public String getForNoDemandeRente() {
        return forNoDemandeRente;
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

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.db.PRAbstractManager#getOrderByDefaut()
     */
    @Override
    public String getOrderByDefaut() {
        if (JadeStringUtil.isEmpty(forNoDemandeRente)) {
            return REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE;
        } else {
            return REInformationsComptabilite.FIELDNAME_ID_TIERS_ADRESSE_PMT + ","
                    + REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE;
        }

    }

    /**
     * @param string
     */
    public void setForCsEtat(String string) {
        forCsEtat = string;
    }

    /**
     * @param forCsEtatDifferentDe
     *            the forCsEtatDifferentDe to set
     */
    public void setForCsEtatDifferentDe(String forCsEtatDifferentDe) {
        this.forCsEtatDifferentDe = forCsEtatDifferentDe;
    }

    public void setForCsEtatIn(String forCsEtatIn) {
        this.forCsEtatIn = forCsEtatIn;
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
    public void setForCsType(String string) {
        forCsType = string;
    }

    /**
     * @param string
     */
    public void setForDateNaissance(String string) {
        forDateNaissance = string;
    }

    public void setForGenrePrestation(String forGenrePrestation) {
        this.forGenrePrestation = forGenrePrestation;
    }

    public void setForIdTiersBeneficiaire(String forIdTiersBeneficiaire) {
        this.forIdTiersBeneficiaire = forIdTiersBeneficiaire;
    }

    public void setForMoisFinRANotEmptyAndHigherOrEgal(String forMoisFinRANotEmptyAndHigherOrEgal) {
        this.forMoisFinRANotEmptyAndHigherOrEgal = forMoisFinRANotEmptyAndHigherOrEgal;
    }

    public void setForNoBaseCalcul(String forNoBaseCalcul) {
        this.forNoBaseCalcul = forNoBaseCalcul;
    }

    /**
     * @param string
     */
    public void setForNoDemandeRente(String string) {
        forNoDemandeRente = string;
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

}
