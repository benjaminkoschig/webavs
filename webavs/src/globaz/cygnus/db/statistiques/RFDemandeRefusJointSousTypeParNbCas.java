/*
 * Créer le 23 janvier 2012
 */
package globaz.cygnus.db.statistiques;

import globaz.cygnus.db.demandes.RFDemande;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * @author mbo
 */

public class RFDemandeRefusJointSousTypeParNbCas extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String createFromClause(String schema) {
        StringBuffer fromClauseBuffer = new StringBuffer();
        String innerJoin = " INNER JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDemande.TABLE_NAME);

        return fromClauseBuffer.toString();

    }

    private String nombreCas = "";

    @Override
    protected String _getTableName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        nombreCas = statement.dbReadNumeric(RFDemandesJointSousTypeParNbCasManager.ALIAS_NB_CAS);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // TODO Auto-generated method stub

    }

    public String getNombreCas() {
        return nombreCas;
    }

    public void setNombreCas(String nombreCas) {
        this.nombreCas = nombreCas;
    }

}
