package globaz.cygnus.db.demandes;

/*
 * Créé le 17 juin 2013
 */
import globaz.commons.nss.NSUtil;
import globaz.cygnus.db.decisions.RFDecision;
import globaz.cygnus.db.dossiers.RFDossier;
import globaz.globall.db.BStatement;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.tools.PRHierarchique;

/**
 * @author mbo
 */

public class RFDemandeJointDecisionJointDossierJointTiers extends RFDemande implements PRHierarchique {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_DECISION_ANNEE_QD = "EBDAQD";
    public static final String FIELDNAME_DEMANDE_ID_DEMANDE = "EDIDEM";
    public static final String FIELDNAME_DEMANDE_NUMERO_DECISION = "EBNNUM";
    public static final String FIELDNAME_TIERS_NOM = "HTLDE1";
    public static final String FIELDNAME_TIERS_NUM_AVS = "HXNAVS";
    public static final String FIELDNAME_TIERS_PRENOM = "HTLDE2";

    public static final String createFromClause(String schema) {
        StringBuffer fromClauseBuffer = new StringBuffer();
        String innerJoin = " INNER JOIN ";
        String leftJoin = " LEFT JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDemande.TABLE_NAME);

        // jointure entre la table des demandes RFM et la table des dossiers
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDossier.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDemande.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDemande.FIELDNAME_ID_DOSSIER);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDossier.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDossier.FIELDNAME_ID_DOSSIER);

        // jointure entre la table des dossiers et la table des demandes PR
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(PRDemande.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(PRDemande.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(PRDemande.FIELDNAME_IDDEMANDE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDossier.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDossier.FIELDNAME_ID_PRDEM);

        // jointure entre la table des demandes PR et la table des numeros AVS
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDemandeJointDossierJointTiers.TABLE_AVS);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(PRDemande.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(PRDemande.FIELDNAME_IDTIERS);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDemandeJointDossierJointTiers.TABLE_AVS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDemandeJointDossierJointTiers.FIELDNAME_ID_TIERS_TI);

        // jointure entre la table des numeros AVS et la table des personnes
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDemandeJointDossierJointTiers.TABLE_PERSONNE);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDemandeJointDossierJointTiers.TABLE_AVS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDemandeJointDossierJointTiers.FIELDNAME_ID_TIERS_TI);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDemandeJointDossierJointTiers.TABLE_PERSONNE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDemandeJointDossierJointTiers.FIELDNAME_ID_TIERS_TI);

        // jointure entre la table des personnes et la table des tiers
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDemandeJointDossierJointTiers.TABLE_TIERS);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDemandeJointDossierJointTiers.TABLE_PERSONNE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDemandeJointDossierJointTiers.FIELDNAME_ID_TIERS_TI);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDemandeJointDossierJointTiers.TABLE_TIERS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDemandeJointDossierJointTiers.FIELDNAME_ID_TIERS_TI);

        // jointure entre la table des demandes et la table des décisions
        fromClauseBuffer.append(leftJoin);
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

        return fromClauseBuffer.toString();
    }

    private String anneeQd = "";
    private transient String fromClause = null;
    private String nom = "";
    private String nss = "";

    private String numeroDecision = "";

    private String numeroDemande = "";

    private String prenom = "";

    /**
     * Il est interdit d'ajouter un objet de ce type.
     * 
     * @return false
     * 
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
     * 
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
     * 
     * @see globaz.globall.db.BEntity#_allowUpdate()
     */
    @Override
    protected boolean _allowUpdate() {
        return false;
    }

    /**
     * @see globaz.globall.db.BEntity#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            fromClause = RFDemandeJointDecisionJointDossierJointTiers.createFromClause(_getCollection());
        }

        return fromClause;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);

        nss = NSUtil.formatAVSUnknown(statement
                .dbReadString(RFDemandeJointDecisionJointDossierJointTiers.FIELDNAME_TIERS_NUM_AVS));
        nom = statement.dbReadString(RFDemandeJointDecisionJointDossierJointTiers.FIELDNAME_TIERS_NOM);
        prenom = statement.dbReadString(RFDemandeJointDecisionJointDossierJointTiers.FIELDNAME_TIERS_PRENOM);
        anneeQd = statement.dbReadString(RFDemandeJointDecisionJointDossierJointTiers.FIELDNAME_DECISION_ANNEE_QD);
        numeroDemande = statement
                .dbReadString(RFDemandeJointDecisionJointDossierJointTiers.FIELDNAME_DEMANDE_ID_DEMANDE);
        numeroDecision = statement
                .dbReadString(RFDemandeJointDecisionJointDossierJointTiers.FIELDNAME_DEMANDE_NUMERO_DECISION);
    }

    public String getAnneeQd() {
        return anneeQd;
    }

    @Override
    public String getIdMajeur() {
        return getIdDemande();
    }

    @Override
    public String getIdParent() {
        return getIdDemandeParent();
    }

    public String getNom() {
        return nom;
    }

    public String getNss() {
        return nss;
    }

    public String getNumeroDecision() {
        return numeroDecision;
    }

    public String getNumeroDemande() {
        return numeroDemande;
    }

    public String getPrenom() {
        return prenom;
    }

    @Override
    public boolean hasSpy() {
        return false;
    }

    public void setAnneeQd(String anneeQd) {
        this.anneeQd = anneeQd;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public void setNumeroDecision(String numeroDecision) {
        this.numeroDecision = numeroDecision;
    }

    public void setNumeroDemande(String numeroDemande) {
        this.numeroDemande = numeroDemande;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

}
