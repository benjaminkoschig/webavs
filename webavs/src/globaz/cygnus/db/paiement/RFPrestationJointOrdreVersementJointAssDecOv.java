package globaz.cygnus.db.paiement;

import globaz.cygnus.db.ordresversements.RFOrdresVersements;
import globaz.globall.db.BStatement;

/**
 * @author mbo
 */
public class RFPrestationJointOrdreVersementJointAssDecOv extends RFOrdresVersements {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Génération de la clause from pour la jointure entre les tables RFPREST - RFORVER - RFDECOV
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
        fromClauseBuffer.append(RFOrdresVersements.TABLE_NAME_ORVER);

        // jointure entre la table des prestations et la table des ordres de versement
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFPrestation.TABLE_NAME_PREST);
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

        // jointure entre la table des ordres de versement et la table associative RFDecOv
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFAssDecOv.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFAssDecOv.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFAssDecOv.FIELDNAME_ID_OV);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFOrdresVersements.TABLE_NAME_ORVER);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFOrdresVersements.FIELDNAME_ID_ORDRE_VERSEMENT);

        return fromClauseBuffer.toString();
    }

    private transient String fromClause = null;

    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            StringBuffer from = new StringBuffer(
                    RFPrestationJointOrdreVersementJointAssDecOv.createFromClause(_getCollection()));
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
