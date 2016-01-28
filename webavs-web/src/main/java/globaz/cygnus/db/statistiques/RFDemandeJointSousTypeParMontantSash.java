/*
 * Créer le 16 janvier 2012
 */
package globaz.cygnus.db.statistiques;

import globaz.cygnus.db.decisions.RFDecision;
import globaz.cygnus.db.demandes.RFDemande;
import globaz.cygnus.db.typeDeSoins.RFSousTypeDeSoin;
import globaz.cygnus.db.typeDeSoins.RFTypeDeSoin;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * @author mbo
 */
public class RFDemandeJointSousTypeParMontantSash extends BEntity {

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

        // jointure entre la table des demandes RFM et la table des sous-type de
        // soin
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFSousTypeDeSoin.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDemande.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDemande.FIELDNAME_ID_SOUS_TYPE_DE_SOIN);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFSousTypeDeSoin.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFSousTypeDeSoin.FIELDNAME_ID_SOUS_TYPE_SOIN);

        // jointure entre la table des demandes RFM et la table des décisions
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDecision.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDemande.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDemande.FIELDNAME_ID_DECISION);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDecision.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDecision.FIELDNAME_ID_DECISION);

        // jointure entre la table des sous-types de soin et la table des types
        // de soin
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFTypeDeSoin.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFSousTypeDeSoin.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFSousTypeDeSoin.FIELDNAME_ID_TYPE_SOIN);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFTypeDeSoin.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFTypeDeSoin.FIELDNAME_ID_TYPE_SOIN);

        return fromClauseBuffer.toString();

    }

    private String idDemande = "";
    private Boolean isForcerPaiement = Boolean.FALSE;
    private String montantAPayer = "";

    @Override
    protected String _getTableName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        montantAPayer = statement.dbReadNumeric(RFDemande.FIELDNAME_MONTANT_A_PAYER, 2);
        isForcerPaiement = statement.dbReadBoolean(RFDemande.FIELDNAME_IS_FORCER_PAIEMENT);
        idDemande = statement.dbReadNumeric(RFDemande.FIELDNAME_ID_DEMANDE);
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

    public String getIdDemande() {
        return idDemande;
    }

    public Boolean getIsForcerPaiement() {
        return isForcerPaiement;
    }

    public String getMontantAPayer() {
        return montantAPayer;
    }

    public void setIdDemande(String idDemande) {
        this.idDemande = idDemande;
    }

    public void setIsForcerPaiement(Boolean isForcerPaiement) {
        this.isForcerPaiement = isForcerPaiement;
    }

    public void setMontantAPayer(String montantAPayer) {
        this.montantAPayer = montantAPayer;
    }

}
