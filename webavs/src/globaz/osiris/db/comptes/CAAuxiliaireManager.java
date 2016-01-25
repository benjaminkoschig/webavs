/*
 * Créé le Apr 6, 2005
 */
package globaz.osiris.db.comptes;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * @author dda
 */
public class CAAuxiliaireManager extends CAOperationManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forMontantABS = new String();

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.db.comptes.CAOperationManager#_getWhere(globaz.globall. db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        // Récupérer depuis la superclasse
        String sqlWhere = super._getWhere(statement);

        // traitement du positionnement selon le montant absolu
        if (getForMontantABS().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "ABS(MONTANT)=" + "ABS(" + this._dbWriteNumeric(statement.getTransaction(), getForMontantABS())
                    + ")";
        }

        return sqlWhere;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.db.comptes.CAOperationManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CAAuxiliaire();
    }

    /**
     * @return
     */
    @Override
    public String getForMontantABS() {
        return forMontantABS;
    }

    /**
     * @param string
     */
    @Override
    public void setForMontantABS(String s) {
        forMontantABS = s;
    }

}
