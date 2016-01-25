package globaz.cygnus.db.paiement;

import globaz.cygnus.db.decisions.RFDecision;
import globaz.cygnus.db.ordresversements.RFOrdresVersements;
import globaz.globall.db.BStatement;

/**
 * @author mbo
 */
public class RFDecisionJointPrestationJointOrdreVersement extends RFDecision {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Génération de la clause from pour la requête > Jointure depuis les décisions jusqu'au prestations
     * 
     * @param schema
     * 
     * @return la clause from
     */
    public static final String createFromClause(String schema) {
        StringBuffer fromClauseBuffer = new StringBuffer();
        String innerJoin = " INNER JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDecision.TABLE_NAME);

        // jointure entre la table des prestations et la table des decisions
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFPrestation.TABLE_NAME_PREST);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDecision.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDecision.FIELDNAME_ID_DECISION);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFPrestation.TABLE_NAME_PREST);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFPrestation.FIELDNAME_ID_DECISION);

        // jointure entre la table des prestations et la table des ordres de versements
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFOrdresVersements.TABLE_NAME_ORVER);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFPrestation.TABLE_NAME_PREST);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFPrestation.FIELDNAME_ID_PRESTATION);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFOrdresVersements.TABLE_NAME_ORVER);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFOrdresVersements.FIELDNAME_ID_PRESTATION);

        return fromClauseBuffer.toString();
    }

    private transient String fromClause = null;

    String idDecision = "";

    String montantDette = "";

    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            StringBuffer from = new StringBuffer(
                    RFDecisionJointPrestationJointOrdreVersement.createFromClause(_getCollection()));
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

        idDecision = statement.dbReadNumeric(RFDecision.FIELDNAME_ID_DECISION);
        montantDette = statement.dbReadNumeric(RFOrdresVersements.FIELDNAME_MONTANT);

    }

    public String getFromClause() {
        return fromClause;
    }

    @Override
    public String getIdDecision() {
        return idDecision;
    }

    public String getMontantDette() {
        return montantDette;
    }

    public void setFromClause(String fromClause) {
        this.fromClause = fromClause;
    }

    @Override
    public void setIdDecision(String idDecision) {
        this.idDecision = idDecision;
    }

    public void setMontantDette(String montantDette) {
        this.montantDette = montantDette;
    }

}
