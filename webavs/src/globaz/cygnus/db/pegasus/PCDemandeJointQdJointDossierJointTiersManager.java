/*
 * Créé le 06 janvier 2010
 */
package globaz.cygnus.db.pegasus;

import globaz.cygnus.api.TypesDeSoins.IRFTypesDeSoins;
import globaz.cygnus.api.demandes.IRFDemande;
import globaz.cygnus.db.decisions.RFDecision;
import globaz.cygnus.db.demandes.RFDemande;
import globaz.cygnus.db.motifsDeRefus.RFAssMotifsRefusDemande;
import globaz.cygnus.db.motifsDeRefus.RFMotifsDeRefus;
import globaz.cygnus.db.qds.RFAssQdDossier;
import globaz.cygnus.db.qds.RFQdPrincipale;
import globaz.cygnus.db.typeDeSoins.RFSousTypeDeSoin;
import globaz.cygnus.db.typeDeSoins.RFTypeDeSoin;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.demandes.PRDemande;

/**
 * @author eco
 */
public class PCDemandeJointQdJointDossierJointTiersManager extends BManager {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forAnneeDemande = "";
    private String forIdTypeSoin = "";
    private String forInIdTiers = "";

    private String forOrderBy = "";

    /**
     * Crée une nouvelle instance de la classe RFQdJointDossierJointTiersJointDemandeManager.
     */
    public PCDemandeJointQdJointDossierJointTiersManager() {
        super();
        wantCallMethodBeforeFind(true);
    }

    @Override
    protected String _getFields(BStatement statement) {

        StringBuffer fields = new StringBuffer();

        fields.append(RFDemande.FIELDNAME_ID_DEMANDE).append(",");
        fields.append(RFDemande.FIELDNAME_MONTANT_A_PAYER).append(",");
        fields.append(PRDemande.FIELDNAME_IDTIERS).append(",");
        fields.append(RFSousTypeDeSoin.FIELDNAME_CODE).append(",");
        fields.append(RFTypeDeSoin.FIELDNAME_CODE).append(",");
        fields.append(RFAssMotifsRefusDemande.FIELDNAME_MNT_MOTIF_REFUS).append(",");
        fields.append(RFQdPrincipale.FIELDNAME_CS_TYPE_PC_ACCORDEE).append(",");
        fields.append(RFMotifsDeRefus.FIELDNAME_ID_MOTIF_REFUS_SYSTEME).append(",");
        fields.append(RFDecision.FIELDNAME_TYPE_PAIEMENT).append(",");
        fields.append(RFSousTypeDeSoin.FIELDNAME_ID_TYPE_SOIN);

        // à verifier si utile

        // fields.append(RFDecision.FIELDNAME_ID_DECISION);
        // fields.append(RFQd.FIELDNAME_CS_GENRE_QD).append(",");
        // fields.append(RFQd.FIELDNAME_CS_ETAT_QD).append(",");
        // fields.append(RFQd.FIELDNAME_ID_QD).append(",");
        // fields.append(RFQd.FIELDNAME_LIMITE_ANNUELLE).append(",");
        // fields.append(RFQd.FIELDNAME_ANNEE_QD).append(",");
        // fields.append(RFQd.FIELDNAME_ID_GESTIONNAIRE).append(",");
        // fields.append(RFQd.FIELDNAME_MONTANT_CHARGE_RFM).append(",");
        // fields.append(RFQd.FIELDNAME_CS_SOURCE).append(",");
        // fields.append(RFQd.FIELDNAME_DATE_CREATION).append(",");
        // fields.append(RFQd.FIELDNAME_IS_PLAFONNEE).append(",");
        // fields.append(RFQd.FIELDNAME_MONTANT_INITIAL_CHARGE_RFM).append(",");
        // fields.append(RFQd.FIELDNAME_CS_TYPE_QD).append(",");
        //
        // fields.append(RFSousTypeDeSoin.FIELDNAME_ID_SOUS_TYPE_SOIN).append(",");
        //
        // fields.append(RFTypeDeSoin.FIELDNAME_ID_TYPE_SOIN).append(",");
        //
        // fields.append(RFQdPrincipale.FIELDNAME_CS_GENRE_PC_ACCORDEE).append(",");
        // fields.append(RFQdPrincipale.FIELDNAME_CS_TYPE_BENEFICIAIRE).append(",");
        // fields.append(RFQdPrincipale.FIELDNAME_DATE_DEBUT_PC_ACCORDEE).append(",");
        // fields.append(RFQdPrincipale.FIELDNAME_DATE_FIN_PC_ACCORDEE).append(",");
        // fields.append(RFQdPrincipale.FIELDNAME_EXCEDENT_RECETTE_PC_ACCORDEE).append(",");
        // fields.append(RFQdPrincipale.FIELDNAME_ID_FAM_MOD_SOLDE_EXCEDENT_PRE_DEC).append(",");
        // fields.append(RFQdPrincipale.FIELDNAME_ID_PC_ACCORDEE).append(",");
        // fields.append(RFQdPrincipale.FIELDNAME_ID_POT_DROIT_PC).append(",");
        // fields.append(RFQdPrincipale.FIELDNAME_ID_QD_PRINCIPALE).append(",");
        // fields.append(RFQdPrincipale.FIELDNAME_IS_LAPRAMS).append(",");
        // fields.append(RFQdPrincipale.FIELDNAME_IS_RI).append(",");
        // fields.append(RFQdPrincipale.FIELDNAME_REMBOURSEMENT_CONJOINT).append(",");
        // fields.append(RFQdPrincipale.FIELDNAME_REMBOURSEMENT_REQUERANT).append(",");
        //
        // fields.append(RFQdAssure.FIELDNAME_DATE_DEBUT).append(",");
        // fields.append(RFQdAssure.FIELDNAME_DATE_FIN).append(",");
        // fields.append(RFQdAssure.FIELDNAME_ID_POT_SOUS_TYPE_DE_SOIN).append(",");
        // fields.append(RFQdAssure.FIELDNAME_ID_QD_ASSURE).append(",");
        //
        // fields.append(RFAssQdDossier.FIELDNAME_ID_ASS_QD_DOSSIER).append(",");
        // fields.append(RFAssQdDossier.FIELDNAME_ID_DOSSIER).append(",");
        // fields.append(RFAssQdDossier.FIELDNAME_ID_QD).append(",");
        // fields.append(RFAssQdDossier.FIELDNAME_IS_COMPRIS_DANS_CALCUL).append(",");
        // fields.append(RFAssQdDossier.FIELDNAME_TYPE_RELATION).append(",");
        //
        // fields.append(RFDossier.FIELDNAME_CS_ETAT_DOSSIER).append(",");
        // fields.append(RFDossier.FIELDNAME_CS_SOURCE).append(",");
        // fields.append(RFDossier.FIELDNAME_DATE_DEBUT).append(",");
        // fields.append(RFDossier.FIELDNAME_DATE_FIN).append(",");
        // fields.append(RFDossier.FIELDNAME_ID_DOSSIER).append(",");
        // fields.append(RFDossier.FIELDNAME_ID_GESTIONNAIRE).append(",");
        // fields.append(RFDossier.FIELDNAME_ID_PRDEM);

        return fields.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return PCDemandeJointQdJointDossierJointTiers.createFromClause(_getCollection());
    }

    @Override
    protected String _getOrder(BStatement statement) {
        if (!JadeStringUtil.isEmpty(forOrderBy)) {
            return forOrderBy;
        }
        return "";
    }

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Redéfinition de la méthode _getWhere du parent afin de générer le WHERE de la requête en fonction des besoins
     * 
     * @param statement
     */
    @Override
    protected String _getWhere(BStatement statement) {

        StringBuffer sqlWhere = new StringBuffer();

        sqlWhere.append(RFAssQdDossier.FIELDNAME_IS_COMPRIS_DANS_CALCUL);
        sqlWhere.append(" = ");
        sqlWhere.append(this._dbWriteBoolean(statement.getTransaction(), true, BConstants.DB_TYPE_BOOLEAN_CHAR));

        // ignore les devis systématiquement
        sqlWhere.append(String.format("AND (%s NOT IN (%s))", RFDemande.FIELDNAME_ID_SOUS_TYPE_DE_SOIN,
                IRFTypesDeSoins.CS_DEVIS_DENTAIRE_19));

        // ignore les demandes annulées
        sqlWhere.append(String.format("AND (%s IN (%s,%s))", RFDemande.FIELDNAME_CS_ETAT, IRFDemande.VALIDE,
                IRFDemande.PAYE));
        sqlWhere.append(String.format("AND (%s != %s)", RFDemande.FIELDNAME_CS_STATUT, IRFDemande.REFUSE));

        if (!JadeStringUtil.isBlankOrZero(forInIdTiers)) {
            sqlWhere.append(String.format("AND (%s IN (%s))", PRDemande.FIELDNAME_IDTIERS, forInIdTiers));
        }

        if (!JadeStringUtil.isBlankOrZero(forAnneeDemande)) {

            final String dateDebutAnnee = "01.01." + forAnneeDemande;
            final String dateFinAnnee = "31.12." + forAnneeDemande;

            // request SQL à composer :
            // AND ((RFDemande.dateDebut >= 01.01.anneeDemande AND RFDemande.dateDebut<=31.12.anneeDemande) OR
            // (RFDemande.dateFacture >= 01.01.anneeDemande AND RFDemande.dateFacture<=31.12.anneeDemande))
            sqlWhere.append(String.format(
                    " AND ((%3$s >= %1$s AND %3$s <= %2$s) OR (%3$s IS NULL AND %4$s >= %1$s AND %4$s <= %2$s))",
                    this._dbWriteDateAMJ(statement.getTransaction(), dateDebutAnnee),
                    this._dbWriteDateAMJ(statement.getTransaction(), dateFinAnnee),
                    RFDemande.FIELDNAME_DATE_DEBUT_TRAITEMENT, RFDemande.FIELDNAME_DATE_FACTURE));
        }

        if (!JadeStringUtil.isBlank(forIdTypeSoin)) {
            sqlWhere.append(String.format("AND (%s = %s)", RFSousTypeDeSoin.FIELDNAME_ID_TYPE_SOIN, forIdTypeSoin));
        }

        return sqlWhere.toString();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Définition de l'entité (RFQdJointDossierJointTiersJointDemande)
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new PCDemandeJointQdJointDossierJointTiers();
    }

    public String getForAnneeDemande() {
        return forAnneeDemande;
    }

    public String getForInIdTiers() {
        return forInIdTiers;
    }

    public String getForOrderBy() {
        return forOrderBy;
    }

    public String getForTypeSoin() {
        return forIdTypeSoin;
    }

    public void setForAnneeDemande(String forAnneeDemande) {
        this.forAnneeDemande = forAnneeDemande;
    }

    public void setForInIdTiers(String forInIdTiers) {
        this.forInIdTiers = forInIdTiers;
    }

    public void setForOrderBy(String forOrderBy) {
        this.forOrderBy = forOrderBy;
    }

    public void setForTypeSoin(String forTypeSoin) {
        forIdTypeSoin = forTypeSoin;
    }

}
