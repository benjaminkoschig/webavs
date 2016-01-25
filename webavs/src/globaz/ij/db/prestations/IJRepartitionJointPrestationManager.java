/*
 * Créé le 11 juil. 05
 */
package globaz.ij.db.prestations;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class IJRepartitionJointPrestationManager extends IJRepartitionPaiementsManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forDateDebut = "";
    private String forDateFin = "";
    private String forEtatPrestation = "";
    private String forIdAffilie = "";
    private String forIdLot = "";
    private String forIdTiers = "";

    // private String forDateDebutComptabilise = "";
    // private String forDateFinComptabilise = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return IJRepartitionJointPrestation.createFromClause(_getCollection());
    }

    /**
     * (non-Javadoc)
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = super._getWhere(statement);
        String schema = _getCollection();

        if (!JadeStringUtil.isIntegerEmpty(forIdLot)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema + IJPrestation.TABLE_NAME + "." + IJPrestation.FIELDNAME_IDLOT + "="
                    + _dbWriteNumeric(statement.getTransaction(), forIdLot);
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdTiers)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema + IJRepartitionPaiements.TABLE_NAME + "." + IJRepartitionPaiements.FIELDNAME_IDTIERS
                    + "=" + _dbWriteNumeric(statement.getTransaction(), forIdTiers);
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdAffilie)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema + IJRepartitionPaiements.TABLE_NAME + "." + IJRepartitionPaiements.FIELDNAME_IDAFFILIE
                    + "=" + _dbWriteNumeric(statement.getTransaction(), forIdAffilie);
        }

        if (!JadeStringUtil.isIntegerEmpty(forDateDebut)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema + IJPrestation.TABLE_NAME + "." + IJPrestation.FIELDNAME_DATEDEBUT + ">="
                    + _dbWriteNumeric(statement.getTransaction(), forDateDebut);
        }

        if (!JadeStringUtil.isIntegerEmpty(forDateFin)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema + IJPrestation.TABLE_NAME + "." + IJPrestation.FIELDNAME_DATEFIN + "<="
                    + _dbWriteNumeric(statement.getTransaction(), forDateFin);
        }

        if (!JadeStringUtil.isIntegerEmpty(forEtatPrestation)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema + IJPrestation.TABLE_NAME + "." + IJPrestation.FIELDNAME_CS_ETAT + "<="
                    + _dbWriteNumeric(statement.getTransaction(), forEtatPrestation);
        }

        // if (!JadeStringUtil.isIntegerEmpty(forDateDebutComptabilise)) {
        // if (sqlWhere.length() != 0) {
        // sqlWhere += " AND ";
        // }
        //
        // sqlWhere += schema + IJPrestation.TABLE_NAME + "." +
        // IJPrestation.FIELDNAME_DATEDECOMPTE +
        // ">=" +
        // _dbWriteNumeric(statement.getTransaction(),
        // forDateDebutComptabilise);
        // }
        //
        // if (!JadeStringUtil.isIntegerEmpty(forDateFinComptabilise)) {
        // if (sqlWhere.length() != 0) {
        // sqlWhere += " AND ";
        // }
        //
        // sqlWhere += schema + IJPrestation.TABLE_NAME + "." +
        // IJPrestation.FIELDNAME_DATEDECOMPTE +
        // "<=" +
        // _dbWriteNumeric(statement.getTransaction(), forDateFinComptabilise);
        // }

        return sqlWhere;
    }

    /**
     * (non-Javadoc)
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new IJRepartitionJointPrestation();
    }

    public String getForDateDebut() {
        return forDateDebut;
    }

    public String getForDateFin() {
        return forDateFin;
    }

    /**
     * @return
     */
    public String getForEtatPrestation() {
        return forEtatPrestation;
    }

    /**
     * getter pour l'attribut for id affilie
     * 
     * @return la valeur courante de l'attribut for id affilie
     */
    public String getForIdAffilie() {
        return forIdAffilie;
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
     * getter pour l'attribut for id tiers
     * 
     * @return la valeur courante de l'attribut for id tiers
     */
    public String getForIdTiers() {
        return forIdTiers;
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
        return IJRepartitionPaiements.FIELDNAME_IDREPARTITION_PAIEMENT;
    }

    public void setForDateDebut(String forDateDebut) {
        this.forDateDebut = forDateDebut;
    }

    public void setForDateFin(String forDateFin) {
        this.forDateFin = forDateFin;
    }

    /**
     * @param string
     */
    public void setForEtatPrestation(String string) {
        forEtatPrestation = string;
    }

    /**
     * setter pour l'attribut for id affilie
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdAffilie(String string) {
        forIdAffilie = string;
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
     * setter pour l'attribut for id tiers
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdTiers(String string) {
        forIdTiers = string;
    }

    // public String getForDateDebutComptabilise() {
    // return forDateDebutComptabilise;
    // }
    //
    // public void setForDateDebutComptabilise(String forDateDebutComptabilise)
    // {
    // this.forDateDebutComptabilise = forDateDebutComptabilise;
    // }
    //
    // public String getForDateFinComptabilise() {
    // return forDateFinComptabilise;
    // }
    //
    // public void setForDateFinComptabilise(String forDateFinComptabilise) {
    // this.forDateFinComptabilise = forDateFinComptabilise;
    // }

}
