/*
 * Créé le 11 juil. 05
 */
package globaz.apg.db.prestation;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class APRepartitionJointPrestationManager extends APRepartitionPaiementsManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forDateDebut = "";
    private String forDateFin = "";
    private String forEtatPrestation = "";
    private String forGenrePrestation = "";
    private String forIdDroit = "";
    private String forIdLot = "";

    // private String forDateDebutComptabilise = "";
    // private String forDateFinComptabilise = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return APRepartitionJointPrestation.createFromClause(_getCollection());
    }

    /**
     * (non-Javadoc).
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

            sqlWhere += schema + APPrestation.TABLE_NAME + "." + APPrestation.FIELDNAME_IDLOT + "="
                    + _dbWriteNumeric(statement.getTransaction(), forIdLot);
        }

        if (!JadeStringUtil.isIntegerEmpty(forGenrePrestation)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema + APPrestation.TABLE_NAME + "." + APPrestation.FIELDNAME_GENRE_PRESTATION + "="
                    + _dbWriteNumeric(statement.getTransaction(), forGenrePrestation);
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdDroit)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema + APPrestation.TABLE_NAME + "." + APPrestation.FIELDNAME_IDDROIT + "="
                    + _dbWriteNumeric(statement.getTransaction(), forIdDroit);
        }

        if (!JadeStringUtil.isIntegerEmpty(forDateDebut)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema + APPrestation.TABLE_NAME + "." + APPrestation.FIELDNAME_DATEDEBUT + ">="
                    + _dbWriteNumeric(statement.getTransaction(), forDateDebut);
        }

        if (!JadeStringUtil.isIntegerEmpty(forDateFin)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema + APPrestation.TABLE_NAME + "." + APPrestation.FIELDNAME_DATEFIN + "<="
                    + _dbWriteNumeric(statement.getTransaction(), forDateFin);
        }

        if (!JadeStringUtil.isIntegerEmpty(forEtatPrestation)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema + APPrestation.TABLE_NAME + "." + APPrestation.FIELDNAME_ETAT + "="
                    + _dbWriteNumeric(statement.getTransaction(), forEtatPrestation);
        }

        // if (!JadeStringUtil.isIntegerEmpty(forDateDebutComptabilise)) {
        // if (sqlWhere.length() != 0) {
        // sqlWhere += " AND ";
        // }
        //
        // sqlWhere += schema + APPrestation.TABLE_NAME + "." +
        // APPrestation.FIELDNAME_DATEPAIEMENT +
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
        // sqlWhere += schema + APPrestation.TABLE_NAME + "." +
        // APPrestation.FIELDNAME_DATEPAIEMENT +
        // "<=" +
        // _dbWriteNumeric(statement.getTransaction(), forDateFinComptabilise);
        // }

        return sqlWhere;
    }

    /**
     * (non-Javadoc).
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
        return new APRepartitionJointPrestation();
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
     * getter pour l'attribut for genre prestation.
     * 
     * @return la valeur courante de l'attribut for genre prestation
     */
    public String getForGenrePrestation() {
        return forGenrePrestation;
    }

    /**
     * getter pour l'attribut for id droit.
     * 
     * @return la valeur courante de l'attribut for id droit
     */
    public String getForIdDroit() {
        return forIdDroit;
    }

    /**
     * getter pour l'attribut for id lot.
     * 
     * @return la valeur courante de l'attribut for id lot
     */
    public String getForIdLot() {
        return forIdLot;
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
     * setter pour l'attribut for genre prestation.
     * 
     * @param forGenrePrestation
     *            une nouvelle valeur pour cet attribut
     */
    public void setForGenrePrestation(String forGenrePrestation) {
        this.forGenrePrestation = forGenrePrestation;
    }

    /**
     * setter pour l'attribut for id droit.
     * 
     * @param forIdDroit
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdDroit(String forIdDroit) {
        this.forIdDroit = forIdDroit;
    }

    /**
     * setter pour l'attribut for id lot.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdLot(String string) {
        forIdLot = string;
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
