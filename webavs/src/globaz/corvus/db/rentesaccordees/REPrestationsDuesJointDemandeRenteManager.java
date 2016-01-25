/*
 * Créé le 16 fevr. 07
 */
package globaz.corvus.db.rentesaccordees;

import globaz.corvus.db.basescalcul.REBasesCalcul;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;
import globaz.prestation.tools.PRDateFormater;

/**
 * @author bsc
 * 
 */

public class REPrestationsDuesJointDemandeRenteManager extends PRAbstractManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCleIdTiersAdrPmtReferencePmt = "";
    private String forCsEtatPrestationDue = "";
    private String forCsEtatPrestationDueNotEqual = "";
    private String forCsTypePrestationDue = "";
    private String forDate = "";
    private String forIdBaseCalcul = "";
    private String forIdPrestationDue = "";
    private String forIdTiersAdrPmt = "";
    private Boolean forIncudeRAWithInteretMoratoireOnly = null;
    private String forNoDemandeRente = "";
    private String forNoRenteAccordee = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

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

        // Ne ressort que les entity qui ont la même clé
        // idTiersAdressePmt_domaine_referencePmt passée en paramètre
        if (!JadeStringUtil.isIntegerEmpty(forCleIdTiersAdrPmtReferencePmt)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            String idTiersAdrPmt = "";
            String referencePmt = "";
            String stringProvisoire = "";

            int position = forCleIdTiersAdrPmtReferencePmt.lastIndexOf("_");

            referencePmt = forCleIdTiersAdrPmtReferencePmt.substring(position + 1,
                    forCleIdTiersAdrPmtReferencePmt.length());
            stringProvisoire = forCleIdTiersAdrPmtReferencePmt.substring(0, position);

            idTiersAdrPmt = stringProvisoire;

            sqlWhere += "(" + REInformationsComptabilite.FIELDNAME_ID_TIERS_ADRESSE_PMT + "="
                    + _dbWriteNumeric(statement.getTransaction(), idTiersAdrPmt)

                    + " AND "

                    + REPrestationsAccordees.FIELDNAME_REFERENCE_PMT + "="
                    + _dbWriteString(statement.getTransaction(), referencePmt)

                    + ") ";

        }

        if (!JadeStringUtil.isIntegerEmpty(forNoDemandeRente)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += REDemandeRente.FIELDNAME_ID_DEMANDE_RENTE + "="
                    + _dbWriteNumeric(statement.getTransaction(), forNoDemandeRente);
        }

        if (!JadeStringUtil.isIntegerEmpty(forNoRenteAccordee)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE + "="
                    + _dbWriteNumeric(statement.getTransaction(), forNoRenteAccordee);
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdPrestationDue)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += REPrestationDue.FIELDNAME_ID_PRESTATION_DUE + "="
                    + _dbWriteNumeric(statement.getTransaction(), forIdPrestationDue);
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdBaseCalcul)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += REBasesCalcul.FIELDNAME_ID_BASES_DE_CALCUL + "="
                    + _dbWriteNumeric(statement.getTransaction(), forIdBaseCalcul);
        }

        if (!JadeStringUtil.isIntegerEmpty(forCsTypePrestationDue)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += REPrestationDue.FIELDNAME_CS_TYPE + "="
                    + _dbWriteNumeric(statement.getTransaction(), forCsTypePrestationDue);
        }

        if (!JadeStringUtil.isIntegerEmpty(forCsEtatPrestationDue)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += REPrestationDue.FIELDNAME_CS_ETAT + "="
                    + _dbWriteNumeric(statement.getTransaction(), forCsEtatPrestationDue);
        }

        if (!JadeStringUtil.isIntegerEmpty(forCsEtatPrestationDueNotEqual)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += REPrestationDue.FIELDNAME_CS_ETAT + "<>"
                    + _dbWriteNumeric(statement.getTransaction(), forCsEtatPrestationDueNotEqual);
        }

        if (!JadeStringUtil.isIntegerEmpty(getForDate())) {
            if (sqlWhere.length() > 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += REPrestationDue.FIELDNAME_DATE_DEBUT_PAIEMENT;
            sqlWhere += "<=";
            sqlWhere += _dbWriteNumeric(statement.getTransaction(),
                    PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(getForDate()));
            sqlWhere += " AND ";

            sqlWhere += " ( ";
            sqlWhere += REPrestationDue.FIELDNAME_DATE_FIN_PAIEMENT;
            sqlWhere += ">=";
            sqlWhere += _dbWriteNumeric(statement.getTransaction(),
                    PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(getForDate()));
            sqlWhere += " OR ";
            sqlWhere += REPrestationDue.FIELDNAME_DATE_FIN_PAIEMENT;
            sqlWhere += "=";
            sqlWhere += _dbWriteNumeric(statement.getTransaction(), "0");
            sqlWhere += " )";
        }

        if (forIncudeRAWithInteretMoratoireOnly != null && forIncudeRAWithInteretMoratoireOnly.booleanValue()) {

            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += REPrestationsAccordees.FIELDNAME_ID_CALCUL_INTERET_MORATOIRE;
            sqlWhere += " > ";
            sqlWhere += _dbWriteNumeric(statement.getTransaction(), "0");

        }

        if (!JadeStringUtil.isIntegerEmpty(forIdTiersAdrPmt)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += REInformationsComptabilite.FIELDNAME_ID_TIERS_ADRESSE_PMT + "="
                    + _dbWriteNumeric(statement.getTransaction(), forIdTiersAdrPmt);
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
        return new REPrestationsDuesJointDemandeRente();
    }

    public String getForCleIdTiersAdrPmtReferencePmt() {
        return forCleIdTiersAdrPmtReferencePmt;
    }

    /**
     * @return
     */
    public String getForCsEtatPrestationDue() {
        return forCsEtatPrestationDue;
    }

    /**
     * @return
     */
    public String getForCsEtatPrestationDueNotEqual() {
        return forCsEtatPrestationDueNotEqual;
    }

    /**
     * @return
     */
    public String getForCsTypePrestationDue() {
        return forCsTypePrestationDue;
    }

    /**
     * @return
     */
    public String getForDate() {
        return forDate;
    }

    public String getForIdBaseCalcul() {
        return forIdBaseCalcul;
    }

    public String getForIdPrestationDue() {
        return forIdPrestationDue;
    }

    public String getForIdTiersAdrPmt() {
        return forIdTiersAdrPmt;
    }

    public Boolean getForIncudeRAWithInteretMoratoireOnly() {
        return forIncudeRAWithInteretMoratoireOnly;
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
    public String getForNoRenteAccordee() {
        return forNoRenteAccordee;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.db.PRAbstractManager#getOrderByDefaut()
     */
    @Override
    public String getOrderByDefaut() {
        return REPrestationDue.FIELDNAME_ID_PRESTATION_DUE;
    }

    public void setForCleIdTiersAdrPmtReferencePmt(String forCleIdTiersAdrPmtReferencePmt) {
        this.forCleIdTiersAdrPmtReferencePmt = forCleIdTiersAdrPmtReferencePmt;
    }

    /**
     * @param string
     */
    public void setForCsEtatPrestationDue(String string) {
        forCsEtatPrestationDue = string;
    }

    /**
     * @param string
     */
    public void setForCsEtatPrestationDueNotEqual(String string) {
        forCsEtatPrestationDueNotEqual = string;
    }

    /**
     * @param string
     */
    public void setForCsTypePrestationDue(String string) {
        forCsTypePrestationDue = string;
    }

    /**
     * @param string
     */
    public void setForDate(String string) {
        forDate = string;
    }

    public void setForIdBaseCalcul(String forIdBaseCalcul) {
        this.forIdBaseCalcul = forIdBaseCalcul;
    }

    public void setForIdPrestationDue(String forIdPrestationDue) {
        this.forIdPrestationDue = forIdPrestationDue;
    }

    public void setForIdTiersAdrPmt(String forIdTiersAdrPmt) {
        this.forIdTiersAdrPmt = forIdTiersAdrPmt;
    }

    public void setForIncudeRAWithInteretMoratoireOnly(Boolean forIncudeRAWithInteretMoratoireOnly) {
        this.forIncudeRAWithInteretMoratoireOnly = forIncudeRAWithInteretMoratoireOnly;
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
    public void setForNoRenteAccordee(String string) {
        forNoRenteAccordee = string;
    }

}
