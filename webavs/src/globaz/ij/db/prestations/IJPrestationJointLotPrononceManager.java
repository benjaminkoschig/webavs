/*
 * Créé le 20 sept. 05
 */
package globaz.ij.db.prestations;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.util.JAUtil;
import globaz.ij.api.prestations.IIJPrestation;
import globaz.ij.db.basesindemnisation.IJBaseIndemnisation;
import globaz.ij.db.lots.IJLot;
import globaz.ij.db.prononces.IJPrononce;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;
import globaz.prestation.interfaces.tiers.IPRTiers;
import globaz.prestation.tools.PRStringUtils;
import globaz.prestation.tools.nnss.PRNSSUtil;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class IJPrestationJointLotPrononceManager extends PRAbstractManager {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** Critere de recherche pour les prestations non définitives */
    public static final String ETAT_NON_DEFINITIF = "Non définitif";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String forCsEtat = "";

    private String forCsSexe = "";
    private String forDateNaissance = "";
    private String forIdLot = "";
    private String forIdPrononce = "";
    private String forNoBaseIndemnisation = "";
    private String forNoLot = "";
    private transient String fromClause = null;
    private String fromDateDebutPrononce = "";
    private String fromDatePaiement = "";
    private String likeNom = "";
    private String likeNumeroAVS = "";
    private String likeNumeroAVSNNSS = "";
    private String likePrenom = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getFrom(BStatement statement) {

        StringBuffer from = new StringBuffer(IJPrestationJointLotPrononce.createFromClause(_getCollection()));

        if ((!JadeStringUtil.isBlankOrZero(likeNumeroAVSNNSS) && !JadeStringUtil.isEmpty(getLikeNumeroAVS()))) {
            from.append(" LEFT JOIN " + _getCollection() + IPRTiers.TABLE_AVS_HIST + " AS " + IPRTiers.TABLE_AVS_HIST
                    + " ON (" + _getCollection() + IPRTiers.TABLE_AVS + "." + IPRTiers.FIELD_TI_IDTIERS + " = "
                    + IPRTiers.TABLE_AVS_HIST + "." + IPRTiers.FIELD_TI_IDTIERS + ")");
        }

        return from.toString();

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getOrder(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return super._getOrder(statement) + ", " + IJPrestation.FIELDNAME_IDPRESTATION;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";

        if ((!JadeStringUtil.isBlankOrZero(likeNumeroAVSNNSS) && !JadeStringUtil.isEmpty(getLikeNumeroAVS()))) {

            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += PRNSSUtil.getWhereNSS(_getCollection(), getLikeNumeroAVS(), getLikeNumeroAVSNNSS());
        }

        if (!JadeStringUtil.isEmpty(likeNom)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection()
                    + IJPrestationJointLotPrononce.TABLE_TIERS
                    + "."
                    + IJPrestationJointLotPrononce.FIELDNAME_NOM_FOR_SEARCH
                    + " LIKE "
                    + this._dbWriteString(statement.getTransaction(),
                            PRStringUtils.upperCaseWithoutSpecialChars(likeNom) + "%");
        }

        if (!JadeStringUtil.isEmpty(likePrenom)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection()
                    + IJPrestationJointLotPrononce.TABLE_TIERS
                    + "."
                    + IJPrestationJointLotPrononce.FIELDNAME_PRENOM_FOR_SEARCH
                    + " LIKE "
                    + this._dbWriteString(statement.getTransaction(),
                            PRStringUtils.upperCaseWithoutSpecialChars(likePrenom) + "%");
        }

        if (!JAUtil.isDateEmpty(fromDateDebutPrononce)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + IJPrononce.TABLE_NAME_PRONONCE + "."
                    + IJPrononce.FIELDNAME_DATE_DEBUT_PRONONCE + ">="
                    + this._dbWriteDateAMJ(statement.getTransaction(), fromDateDebutPrononce);
        }

        if (!JAUtil.isDateEmpty(fromDatePaiement)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "(" + _getCollection() + IJLot.TABLE_NAME + "." + IJLot.FIELDNAME_DATECOMPTABLE + ">="
                    + this._dbWriteDateAMJ(statement.getTransaction(), fromDatePaiement) + " AND " + _getCollection()
                    + IJLot.TABLE_NAME + "." + IJLot.FIELDNAME_DATECOMPTABLE + " IS NOT NULL)";

        }

        if (!JadeStringUtil.isIntegerEmpty(forIdPrononce)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + IJPrononce.TABLE_NAME_PRONONCE + "." + IJPrononce.FIELDNAME_ID_PRONONCE
                    + "=" + this._dbWriteNumeric(statement.getTransaction(), forIdPrononce);
        }

        if (!JadeStringUtil.isIntegerEmpty(forNoLot)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + IJLot.TABLE_NAME + "." + IJLot.FIELDNAME_NOLOT + "="
                    + this._dbWriteNumeric(statement.getTransaction(), forNoLot);
        }

        if (!JAUtil.isDateEmpty(forDateNaissance)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + IJPrestationJointLotPrononce.TABLE_TIERS_DETAIL + "."
                    + IJPrestationJointLotPrononce.FIELDNAME_DATE_NAISSANCE + "="
                    + this._dbWriteDateAMJ(statement.getTransaction(), forDateNaissance);
        }

        if (!JAUtil.isDateEmpty(forCsSexe)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + IJPrestationJointLotPrononce.TABLE_TIERS_DETAIL + "."
                    + IJPrestationJointLotPrononce.FIELDNAME_SEXE + "="
                    + this._dbWriteNumeric(statement.getTransaction(), forCsSexe);
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdLot)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + IJLot.TABLE_NAME + "." + IJLot.FIELDNAME_IDLOT + "="
                    + this._dbWriteNumeric(statement.getTransaction(), forIdLot);
        }

        if (!JadeStringUtil.isIntegerEmpty(forCsEtat)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            if (forCsEtat.equals(IJPrestationJointLotPrononceManager.ETAT_NON_DEFINITIF)) {
                sqlWhere += _getCollection() + IJPrestation.TABLE_NAME + "." + IJPrestation.FIELDNAME_CS_ETAT + "<>"
                        + this._dbWriteNumeric(statement.getTransaction(), IIJPrestation.CS_DEFINITIF);
            } else {
                sqlWhere += _getCollection() + IJPrestation.TABLE_NAME + "." + IJPrestation.FIELDNAME_CS_ETAT + "="
                        + this._dbWriteNumeric(statement.getTransaction(), forCsEtat);
            }
        }

        if (!JadeStringUtil.isIntegerEmpty(forNoBaseIndemnisation)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + IJBaseIndemnisation.TABLE_NAME + "."
                    + IJBaseIndemnisation.FIELDNAME_IDBASEINDEMNISATION + "="
                    + this._dbWriteNumeric(statement.getTransaction(), forNoBaseIndemnisation);
        }

        // if (!JAUtil.isStringEmpty(likeNumeroAVS)) {
        // // formatage du numéro AVS
        // String avs = null;
        //
        // try {
        // IFormatData avsFormater = IJApplication.getAvsFormater();
        //
        // if (avsFormater != null) {
        // avs = avsFormater.format(likeNumeroAVS);
        // }
        // } catch (Exception e1) {
        // avs = likeNumeroAVS;
        // }
        //
        // if (sqlWhere.length() != 0) {
        // sqlWhere += " AND ";
        // }
        //
        // // dans les tiers le no AVS est stocké en varchar
        // sqlWhere += _getCollection() + IJPrestationJointLotPrononce.TABLE_AVS
        // + "." +
        // IJPrestationJointLotPrononce.FIELDNAME_NUM_AVS +
        // "=" +
        // _dbWriteString(statement.getTransaction(), avs);
        // }

        return sqlWhere;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new IJPrestationJointLotPrononce();
    }

    /**
     * getter pour l'attribut for cs etat
     * 
     * @return la valeur courante de l'attribut for cs etat
     */
    public String getForCsEtat() {
        return forCsEtat;
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
     * getter pour l'attribut for id lot
     * 
     * @return la valeur courante de l'attribut for id lot
     */
    public String getForIdLot() {
        return forIdLot;
    }

    /**
     * getter pour l'attribut for id prononce
     * 
     * @return la valeur courante de l'attribut for id prononce
     */
    public String getForIdPrononce() {
        return forIdPrononce;
    }

    /**
     * getter pour l'attribut for no base indemnisation
     * 
     * @return la valeur courante de l'attribut for no base indemnisation
     */
    public String getForNoBaseIndemnisation() {
        return forNoBaseIndemnisation;
    }

    /**
     * getter pour l'attribut for no lot
     * 
     * @return la valeur courante de l'attribut for no lot
     */
    public String getForNoLot() {
        return forNoLot;
    }

    /**
     * getter pour l'attribut from clause
     * 
     * @return la valeur courante de l'attribut from clause
     */
    public String getFromClause() {
        return fromClause;
    }

    /**
     * getter pour l'attribut from date debut prononce
     * 
     * @return la valeur courante de l'attribut from date debut prononce
     */
    public String getFromDateDebutPrononce() {
        return fromDateDebutPrononce;
    }

    /**
     * getter pour l'attribut from date paiement
     * 
     * @return la valeur courante de l'attribut from date paiement
     */
    public String getFromDatePaiement() {
        return fromDatePaiement;
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

    /**
     * (non-Javadoc)
     * 
     * @see globaz.prestation.db.PRAbstractManager#getOrderByDefaut()
     * 
     * @return la valeur courante de l'attribut order by defaut
     */
    @Override
    public String getOrderByDefaut() {
        if (!JadeStringUtil.isEmpty(fromDatePaiement)) {
            return IJPrestationJointLotPrononce.FIELDNAME_NUM_AVS + ", " + IJLot.FIELDNAME_DATECOMPTABLE + ", "
                    + IJPrestation.FIELDNAME_IDPRESTATION;
        } else {
            return IJPrestationJointLotPrononce.FIELDNAME_NUM_AVS + ", " + IJPrononce.FIELDNAME_ID_PRONONCE + ", "
                    + IJPrestation.FIELDNAME_IDPRESTATION;
        }

    }

    /**
     * setter pour l'attribut for cs etat
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForCsEtat(String string) {
        forCsEtat = string;
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
     * setter pour l'attribut for id lot
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdLot(String string) {
        forIdLot = string;
    }

    /**
     * setter pour l'attribut for id prononce
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdPrononce(String string) {
        forIdPrononce = string;
    }

    /**
     * setter pour l'attribut for no base indemnisation
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForNoBaseIndemnisation(String string) {
        forNoBaseIndemnisation = string;
    }

    /**
     * setter pour l'attribut for no lot
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForNoLot(String string) {
        forNoLot = string;
    }

    /**
     * setter pour l'attribut from clause
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setFromClause(String string) {
        fromClause = string;
    }

    /**
     * setter pour l'attribut from date debut prononce
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setFromDateDebutPrononce(String string) {
        fromDateDebutPrononce = string;
    }

    /**
     * setter pour l'attribut from date paiement
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setFromDatePaiement(String string) {
        fromDatePaiement = string;
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
