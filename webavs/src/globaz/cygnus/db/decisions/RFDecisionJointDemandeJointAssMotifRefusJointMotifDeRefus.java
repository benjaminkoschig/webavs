/*
 * Créé le 5 janvier 2011
 */
package globaz.cygnus.db.decisions;

import globaz.cygnus.db.demandes.RFDemande;
import globaz.cygnus.db.demandes.RFDemandeMoy5_6_7;
import globaz.cygnus.db.motifsDeRefus.RFAssMotifsRefusDemande;
import globaz.cygnus.db.motifsDeRefus.RFMotifsDeRefus;
import globaz.cygnus.db.typeDeSoins.RFSousTypeDeSoin;
import globaz.cygnus.db.typeDeSoins.RFTypeDeSoin;
import globaz.globall.db.BStatement;

/**
 * 
 * @author jje
 */
public class RFDecisionJointDemandeJointAssMotifRefusJointMotifDeRefus extends RFDemande {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

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
        String innerJoin = " INNER JOIN ";
        String leftJoin = " LEFT JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDecision.TABLE_NAME);

        // jointure entre la table des décisions et la table des demandes
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDemande.TABLE_NAME);
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

        // jointure entre la table des demandes moyen auxiliaire 5 et la table
        // des demandes RFM
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDemandeMoy5_6_7.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDemande.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDemande.FIELDNAME_ID_DEMANDE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDemandeMoy5_6_7.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDemandeMoy5_6_7.FIELDNAME_ID_DEMANDE_MOYENS_AUX);

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

        // jointure entre la table des demandes et la table association motif de refus demande
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFAssMotifsRefusDemande.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFAssMotifsRefusDemande.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFAssMotifsRefusDemande.FIELDNAME_ID_DEMANDE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDemande.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDemande.FIELDNAME_ID_DEMANDE);

        // Jointure entre la table des motifs de refus et la table association motif de refus demande
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFMotifsDeRefus.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFMotifsDeRefus.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFMotifsDeRefus.FIELDNAME_ID_MOTIF_REFUS);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFAssMotifsRefusDemande.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFAssMotifsRefusDemande.FIELDNAME_ID_MOTIF_REFUS);

        return fromClauseBuffer.toString();
    }

    private String codeSousTypeDeSoin = "";
    private String codeTypeDeSoin = "";
    private transient String fromClause = null;
    private Boolean hasMontant = Boolean.FALSE;
    private String idAssMotifDeRefus = "";
    private String idDecision = "";
    private String idDemande = "";
    private Boolean isMotifDeRefusSysteme = Boolean.FALSE;
    private String mntMotifsDeRefus = "";
    private String montantFacture = "";
    private String montantFacture44 = "";
    private String montantVerseOAI = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private String prenom = "";

    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            StringBuffer from = new StringBuffer(
                    RFDecisionJointDemandeJointAssMotifRefusJointMotifDeRefus.createFromClause(_getCollection()));
            fromClause = from.toString();
        }

        return fromClause;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idAssMotifDeRefus = statement.dbReadString(RFAssMotifsRefusDemande.FIELDNAME_ID_ASS);
        idDecision = statement.dbReadString(RFDecision.FIELDNAME_ID_DECISION);
        idDemande = statement.dbReadString(RFDemande.FIELDNAME_ID_DEMANDE);
        isMotifDeRefusSysteme = statement.dbReadBoolean(RFMotifsDeRefus.FIELDNAME_IS_MOTIF_SYSTEME);
        montantFacture = statement.dbReadNumeric(RFDemande.FIELDNAME_MONTANT_FACTURE, 2);
        mntMotifsDeRefus = statement.dbReadNumeric(RFAssMotifsRefusDemande.FIELDNAME_MNT_MOTIF_REFUS);
        codeSousTypeDeSoin = statement.dbReadString(RFSousTypeDeSoin.FIELDNAME_CODE);
        codeTypeDeSoin = statement.dbReadString(RFTypeDeSoin.FIELDNAME_CODE);
        montantVerseOAI = statement.dbReadNumeric(RFDemandeMoy5_6_7.FIELDNAME_MONTANT_VERSE_OAI);
        montantFacture44 = statement.dbReadNumeric(RFDemandeMoy5_6_7.FIELDNAME_MONTANT_FACTURE_44);
        hasMontant = statement.dbReadBoolean(RFMotifsDeRefus.FIELDNAME_HAS_MONTANT);

    }

    public String getCodeSousTypeDeSoin() {
        return codeSousTypeDeSoin;
    }

    public String getCodeTypeDeSoin() {
        return codeTypeDeSoin;
    }

    public String getFromClause() {
        return fromClause;
    }

    public Boolean getHasMontant() {
        return hasMontant;
    }

    public String getIdAssMotifDeRefus() {
        return idAssMotifDeRefus;
    }

    @Override
    public String getIdDecision() {
        return idDecision;
    }

    @Override
    public String getIdDemande() {
        return idDemande;
    }

    public Boolean getIsMotifDeRefusSysteme() {
        return isMotifDeRefusSysteme;
    }

    public String getMntMotifsDeRefus() {
        return mntMotifsDeRefus;
    }

    @Override
    public String getMontantFacture() {
        return montantFacture;
    }

    public String getMontantFacture44() {
        return montantFacture44;
    }

    public String getMontantVerseOAI() {
        return montantVerseOAI;
    }

    public void setCodeSousTypeDeSoin(String codeSousTypeDeSoin) {
        this.codeSousTypeDeSoin = codeSousTypeDeSoin;
    }

    public void setCodeTypeDeSoin(String codeTypeDeSoin) {
        this.codeTypeDeSoin = codeTypeDeSoin;
    }

    public void setIdAssMotifDeRefus(String idAssMotifDeRefus) {
        this.idAssMotifDeRefus = idAssMotifDeRefus;
    }

    @Override
    public void setIdDecision(String idDecision) {
        this.idDecision = idDecision;
    }

    @Override
    public void setIdDemande(String idDemande) {
        this.idDemande = idDemande;
    }

    public void setIsMotifDeRefusSysteme(Boolean isMotifDeRefusSysteme) {
        this.isMotifDeRefusSysteme = isMotifDeRefusSysteme;
    }

    public void setMntMotifsDeRefus(String mntMotifsDeRefus) {
        this.mntMotifsDeRefus = mntMotifsDeRefus;
    }

    @Override
    public void setMontantFacture(String montantFacture) {
        this.montantFacture = montantFacture;
    }

    public void setMontantFacture44(String montantFacture44) {
        this.montantFacture44 = montantFacture44;
    }

    public void setMontantVerseOAI(String montantVerseOAI) {
        this.montantVerseOAI = montantVerseOAI;
    }

}
