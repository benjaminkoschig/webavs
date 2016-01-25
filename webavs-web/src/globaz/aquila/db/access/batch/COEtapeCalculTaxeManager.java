package globaz.aquila.db.access.batch;

import globaz.aquila.common.COBManager;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * Représente un container de type Etape
 * 
 * @author Arnaud Dostes, 11-oct-2004
 */
public class COEtapeCalculTaxeManager extends COBManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdCalculTaxe = "";
    /** (idEtape) */
    private String forIdEtape = "";

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + COEtapeCalculTaxe.TABLE_NAME;
    }

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        // traitement du positionnement
        if (getForIdEtape().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += COEtapeCalculTaxe.FNAME_ID_ETAPE + "="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdEtape());
        }

        // traitement du positionnement
        if (getForIdCalculTaxe().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += COEtapeCalculTaxe.FNAME_ID_CALCUL_TAXE + "="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdCalculTaxe());
        }

        // traitement du positionnement
        if (getForIdCalculTaxe().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += COEtapeCalculTaxe.FNAME_ID_CALCUL_TAXE + "="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdCalculTaxe());
        }

        return sqlWhere;
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new COEtapeCalculTaxe();
    }

    public String getForIdCalculTaxe() {
        return forIdCalculTaxe;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getForIdEtape() {
        return forIdEtape;
    }

    public void setForIdCalculTaxe(String string) {
        forIdCalculTaxe = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setForIdEtape(String string) {
        forIdEtape = string;
    }

}
