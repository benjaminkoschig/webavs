/*
 * Créé le 1 février 2010
 */
package globaz.cygnus.db.pegasus;

import globaz.cygnus.db.decisions.RFDecision;
import globaz.cygnus.db.demandes.RFDemande;
import globaz.cygnus.db.dossiers.RFDossier;
import globaz.cygnus.db.motifsDeRefus.RFAssMotifsRefusDemande;
import globaz.cygnus.db.motifsDeRefus.RFMotifsDeRefus;
import globaz.cygnus.db.qds.RFAssQdDossier;
import globaz.cygnus.db.qds.RFQd;
import globaz.cygnus.db.qds.RFQdAssure;
import globaz.cygnus.db.qds.RFQdPrincipale;
import globaz.cygnus.db.typeDeSoins.RFSousTypeDeSoin;
import globaz.cygnus.db.typeDeSoins.RFTypeDeSoin;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.prestation.db.demandes.PRDemande;

/**
 * Modèle servant à la recherche des frais médicaux pour le service PC des transferts de dossier en cas de décision de
 * suppression.
 * 
 * @author eco
 */
public class PCDemandeJointQdJointDossierJointTiers extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String egal = "=";
    public static final String FIELDNAME_ID_TIERS_TI = "HTITIE";
    static final String innerJoin = " INNER JOIN ";
    static final String leftJoin = " LEFT JOIN ";
    private static final String on = " ON ";
    private static final String point = ".";
    public static final String TABLE_PERSONNE = "TIPERSP";
    public static final String TABLE_TIERS = "TITIERP";

    /**
     * Génération de la clause from pour la requête > des Qds jusqu'au tiers
     * 
     * @param schema
     * @return la clause from
     */
    public static final String createFromClause(String schema) {
        StringBuffer fromClauseBuffer = new StringBuffer();

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDemande.TABLE_NAME);

        // jointure entre la table des QdPrincipales et des demandes
        PCDemandeJointQdJointDossierJointTiers.leftJoinTable(schema, fromClauseBuffer, RFDemande.TABLE_NAME,
                RFDemande.FIELDNAME_ID_QD_PRINCIPALE, RFQdPrincipale.TABLE_NAME,
                RFQdPrincipale.FIELDNAME_ID_QD_PRINCIPALE);

        // jointure entre la table des Qds et la table des QdPrinicpales
        PCDemandeJointQdJointDossierJointTiers.leftJoinTable(schema, fromClauseBuffer, RFQdPrincipale.TABLE_NAME,
                RFQdPrincipale.FIELDNAME_ID_QD_PRINCIPALE, RFQd.TABLE_NAME, RFQd.FIELDNAME_ID_QD);

        // TODO voir si la jointure sur les QD assurés est inutile
        // jointure entre la table des Qds et la table des QdAssures
        PCDemandeJointQdJointDossierJointTiers.leftJoinTable(schema, fromClauseBuffer, RFQd.TABLE_NAME,
                RFQd.FIELDNAME_ID_QD, RFQdAssure.TABLE_NAME, RFQdAssure.FIELDNAME_ID_QD_ASSURE);

        // jointure entre la table des QdAssures et la table des sousTypes
        PCDemandeJointQdJointDossierJointTiers.leftJoinTable(schema, fromClauseBuffer, RFQdAssure.TABLE_NAME,
                RFQdAssure.FIELDNAME_ID_SOUS_TYPE_DE_SOIN, RFSousTypeDeSoin.TABLE_NAME,
                RFSousTypeDeSoin.FIELDNAME_ID_SOUS_TYPE_SOIN);

        // jointure entre la table des types de soin et la table des sous type
        // de soin
        PCDemandeJointQdJointDossierJointTiers.leftJoinTable(schema, fromClauseBuffer, RFSousTypeDeSoin.TABLE_NAME,
                RFSousTypeDeSoin.FIELDNAME_ID_TYPE_SOIN, RFTypeDeSoin.TABLE_NAME, RFTypeDeSoin.FIELDNAME_ID_TYPE_SOIN);

        // jointure entre la table des QdBase et la table Association Qd,dossier
        PCDemandeJointQdJointDossierJointTiers.innerJoinTable(schema, fromClauseBuffer, RFQd.TABLE_NAME,
                RFQd.FIELDNAME_ID_QD, RFAssQdDossier.TABLE_NAME, RFAssQdDossier.FIELDNAME_ID_QD);

        // jointure entre la table Association Qd, dossier et la table des
        // dossier
        PCDemandeJointQdJointDossierJointTiers.innerJoinTable(schema, fromClauseBuffer, RFAssQdDossier.TABLE_NAME,
                RFAssQdDossier.FIELDNAME_ID_DOSSIER, RFDossier.TABLE_NAME, RFDossier.FIELDNAME_ID_DOSSIER);

        // jointure entre la table des dossiers et la table des prDemande
        PCDemandeJointQdJointDossierJointTiers.innerJoinTable(schema, fromClauseBuffer, RFDossier.TABLE_NAME,
                RFDossier.FIELDNAME_ID_PRDEM, PRDemande.TABLE_NAME, PRDemande.FIELDNAME_IDDEMANDE);

        // jointure entre la table association des motifs de refus et des demandes
        PCDemandeJointQdJointDossierJointTiers.leftJoinTable(schema, fromClauseBuffer, RFDemande.TABLE_NAME,
                RFDemande.FIELDNAME_ID_DEMANDE, RFAssMotifsRefusDemande.TABLE_NAME,
                RFAssMotifsRefusDemande.FIELDNAME_ID_DEMANDE);

        PCDemandeJointQdJointDossierJointTiers.leftJoinTable(schema, fromClauseBuffer,
                RFAssMotifsRefusDemande.TABLE_NAME, RFAssMotifsRefusDemande.FIELDNAME_ID_MOTIF_REFUS,
                RFMotifsDeRefus.TABLE_NAME, RFMotifsDeRefus.FIELDNAME_ID_MOTIF_REFUS);

        PCDemandeJointQdJointDossierJointTiers.leftJoinTable(schema, fromClauseBuffer, RFDemande.TABLE_NAME,
                RFDemande.FIELDNAME_ID_DECISION, RFDecision.TABLE_NAME, RFDecision.FIELDNAME_ID_DECISION);

        return fromClauseBuffer.toString();
    }

    /**
     * ajoute une jointure INNER sur une nouvelle table. <br>
     * Il se construit sur le modèle "INNER JOIN tableDest ON tableSrc.field = tableDest.field"
     * 
     * @param schema
     *            schema
     * @param fromClauseBuffer
     *            tampon
     * @param tableJoinSrc
     *            table de reference de la jointure
     * @param fieldJoinSrc
     *            champ de liaison pour la table de reference de la jointure
     * @param tableJoinDest
     *            table à ajouter
     * @param fieldJoinDest
     *            champ de liaison pour la table à ajouter
     */
    private static void innerJoinTable(String schema, StringBuffer fromClauseBuffer, String tableJoinSrc,
            String fieldJoinSrc, String tableJoinDest, String fieldJoinDest) {
        PCDemandeJointQdJointDossierJointTiers.joinTable(PCDemandeJointQdJointDossierJointTiers.innerJoin, schema,
                fromClauseBuffer, tableJoinSrc, fieldJoinSrc, PCDemandeJointQdJointDossierJointTiers.egal,
                tableJoinDest, fieldJoinDest);
    }

    private static void joinTable(String typeJoin, String schema, StringBuffer fromClauseBuffer, String tableJoinSrc,
            String fieldJoinSrc, final String operator, String tableJoinDest, String fieldJoinDest) {
        fromClauseBuffer.append(typeJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(tableJoinDest);
        fromClauseBuffer.append(PCDemandeJointQdJointDossierJointTiers.on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(tableJoinSrc);
        fromClauseBuffer.append(PCDemandeJointQdJointDossierJointTiers.point);
        fromClauseBuffer.append(fieldJoinSrc);
        fromClauseBuffer.append(operator);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(tableJoinDest);
        fromClauseBuffer.append(PCDemandeJointQdJointDossierJointTiers.point);
        fromClauseBuffer.append(fieldJoinDest);
    }

    /**
     * ajoute une jointure LEFT sur une nouvelle table.<br>
     * Il se construit sur le modèle "LEFT JOIN tableDest ON tableSrc.field = tableDest.field"
     * 
     * @param schema
     *            schema
     * @param fromClauseBuffer
     *            tampon
     * @param tableJoinSrc
     *            table de reference de la jointure
     * @param fieldJoinSrc
     *            champ de liaison pour la table de reference de la jointure
     * @param tableJoinDest
     *            table à ajouter
     * @param fieldJoinDest
     *            champ de liaison pour la table à ajouter
     */
    private static void leftJoinTable(String schema, StringBuffer fromClauseBuffer, String tableJoinSrc,
            String fieldJoinSrc, String tableJoinDest, String fieldJoinDest) {
        PCDemandeJointQdJointDossierJointTiers.joinTable(PCDemandeJointQdJointDossierJointTiers.leftJoin, schema,
                fromClauseBuffer, tableJoinSrc, fieldJoinSrc, PCDemandeJointQdJointDossierJointTiers.egal,
                tableJoinDest, fieldJoinDest);
    }

    private String csTypeMotifRefus = "";
    private String csTypePaiement = "";
    private String csTypeQdPrincipal = "";
    private String idDemande = "";
    private String idTiers = "";
    private String idTypeSoin = "";
    private String montantFactureAPayer;
    private String montantRefuse = "";
    private String montantRetroactifPaiementCourant;
    private String qdAssureCodeSousTypeDeSoin = "";
    private String qdAssureCodeTypeDeSoin = "";

    /**
     * Il est interdit d'ajouter un objet de ce type.
     * 
     * @return false
     * @see globaz.globall.db.BEntity#_allowAdd()
     */
    @Override
    protected boolean _allowAdd() {
        return false;
    }

    /**
     * Il est interdit d'effacer un objet de ce type.
     * 
     * @return false
     * @see globaz.globall.db.BEntity#_allowDelete()
     */
    @Override
    protected boolean _allowDelete() {
        return false;
    }

    /**
     * Il est interdit de mettre un objet de ce type à jour.
     * 
     * @return false
     * @see globaz.globall.db.BEntity#_allowUpdate()
     */
    @Override
    protected boolean _allowUpdate() {
        return false;
    }

    @Override
    protected String _getTableName() {
        return RFDemande.TABLE_NAME;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        // super._readProperties(statement);

        idDemande = statement.dbReadString(RFDemande.FIELDNAME_ID_DEMANDE);

        qdAssureCodeSousTypeDeSoin = statement.dbReadString(RFSousTypeDeSoin.FIELDNAME_CODE);
        qdAssureCodeTypeDeSoin = statement.dbReadString(RFTypeDeSoin.FIELDNAME_CODE);

        idTiers = statement.dbReadNumeric(PRDemande.FIELDNAME_IDTIERS);
        montantFactureAPayer = statement.dbReadNumeric(RFDemande.FIELDNAME_MONTANT_A_PAYER, 2);
        csTypeQdPrincipal = statement.dbReadNumeric(RFQdPrincipale.FIELDNAME_CS_TYPE_PC_ACCORDEE);
        montantRefuse = statement.dbReadNumeric(RFAssMotifsRefusDemande.FIELDNAME_MNT_MOTIF_REFUS);

        csTypeMotifRefus = statement.dbReadString(RFMotifsDeRefus.FIELDNAME_ID_MOTIF_REFUS_SYSTEME);

        csTypePaiement = statement.dbReadString(RFDecision.FIELDNAME_TYPE_PAIEMENT);

        montantRetroactifPaiementCourant = statement
                .dbReadNumeric(RFDecision.FIELDNAME_MONTANT_COURANT_PARTIE_RETROACTIVE);

        idTypeSoin = statement.dbReadNumeric(RFSousTypeDeSoin.FIELDNAME_ID_TYPE_SOIN);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        // TODO Auto-generated method stub

    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // TODO Auto-generated method stub

    }

    public String getCsTypeMotifRefus() {
        return csTypeMotifRefus;
    }

    public String getCsTypePaiement() {
        return csTypePaiement;
    }

    public String getCsTypeQdPrincipal() {
        return csTypeQdPrincipal;
    }

    public String getIdDemande() {
        return idDemande;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getIdTypeSoin() {
        return idTypeSoin;
    }

    public String getMontantFactureAPayer() {
        return montantFactureAPayer;
    }

    public String getMontantRefuse() {
        return montantRefuse;
    }

    public String getMontantRetroactifPaiementCourant() {
        return montantRetroactifPaiementCourant;
    }

    public String getQdAssureCodeSousTypeDeSoin() {
        return qdAssureCodeSousTypeDeSoin;
    }

    public String getQdAssureCodeTypeDeSoin() {
        return qdAssureCodeTypeDeSoin;
    }

    @Override
    public boolean hasSpy() {
        return false;
    }

    public void setCsTypeMotifRefus(String csTypeMotifRefus) {
        this.csTypeMotifRefus = csTypeMotifRefus;
    }

    public void setCsTypePaiement(String csTypePaiement) {
        this.csTypePaiement = csTypePaiement;
    }

    public void setCsTypeQdPrincipal(String csTypeQdPrincipal) {
        this.csTypeQdPrincipal = csTypeQdPrincipal;
    }

    public void setIdDemande(String idDemande) {
        this.idDemande = idDemande;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setIdTypeSoin(String idTypeSoin) {
        this.idTypeSoin = idTypeSoin;
    }

    public void setMontantFactureAPayer(String montantFactureAPayer) {
        this.montantFactureAPayer = montantFactureAPayer;
    }

    public void setMontantRefuse(String montantRefuse) {
        this.montantRefuse = montantRefuse;
    }

    public void setMontantRetroactifPaiementCourant(String montantRetroactifPaiementCourant) {
        this.montantRetroactifPaiementCourant = montantRetroactifPaiementCourant;
    }

    public void setQdAssureCodeSousTypeDeSoin(String qdAssureCodeSousTypeDeSoin) {
        this.qdAssureCodeSousTypeDeSoin = qdAssureCodeSousTypeDeSoin;
    }

    public void setQdAssureCodeTypeDeSoin(String qdAssureCodeTypeDeSoin) {
        this.qdAssureCodeTypeDeSoin = qdAssureCodeTypeDeSoin;
    }

}