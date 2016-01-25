/*
 * Créé le 10 décembre 2010
 */
package globaz.cygnus.db.paiement;

import globaz.cygnus.db.decisions.RFDecision;
import globaz.cygnus.db.demandes.RFDemande;
import globaz.globall.db.BStatement;

/**
 * @author FHA
 * 
 */
public class RFDemandeJointPrestation extends RFDemande {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Génération de la clause from pour la requête > Jointure depuis les dossiers jusque dans les tiers (Nom et AVS)
     * 
     * @param schema
     * 
     * @return la clause from
     */
    public static final String createFromClause(String schema) {
        StringBuffer fromClauseBuffer = new StringBuffer();
        String leftJoin = " LEFT JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDemande.TABLE_NAME);

        /************* Faire aussi la jointure sur le gestionnaire? *******/
        // jointure entre la table des demandes et la table des décision
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDecision.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDecision.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDecision.FIELDNAME_ID_DECISION);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDemande.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDemande.FIELDNAME_ID_DECISION);

        // jointure entre la table des décision et des prestations
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFPrestation.TABLE_NAME_PREST);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFPrestation.TABLE_NAME_PREST);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFPrestation.FIELDNAME_ID_DECISION);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDecision.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDecision.FIELDNAME_ID_DECISION);

        return fromClauseBuffer.toString();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private transient String fromClause = null;

    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            StringBuffer from = new StringBuffer(RFDemandeJointPrestation.createFromClause(_getCollection()));
            fromClause = from.toString();
        }

        return fromClause;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);

    }

    public String getFromClause() {
        return fromClause;
    }

    public void setFromClause(String fromClause) {
        this.fromClause = fromClause;
    }

}
