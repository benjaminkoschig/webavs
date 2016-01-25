/*
 * Créé le 1 février 2010
 */
package globaz.cygnus.db.qds;

import globaz.cygnus.db.demandes.RFDemande;
import globaz.cygnus.db.dossiers.RFDossier;
import globaz.globall.db.BStatement;
import globaz.prestation.db.demandes.PRDemande;

/**
 * @author jje
 */
public class RFQdJointPeriodeValiditeJointDossierJointTiersJointDemande extends RFQdPrincipale {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    // public static final String FIELDNAME_DATEDECES = "HPDDEC";
    // public static final String FIELDNAME_DATENAISSANCE = "HPDNAI";

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String FIELDNAME_ID_TIERS_TI = "HTITIE";
    // public static final String FIELDNAME_NATIONALITE = "HNIPAY";

    // public static final String FIELDNAME_NOM = "HTLDE1";
    // public static final String FIELDNAME_NOM_FOR_SEARCH = "HTLDU1";
    // public static final String FIELDNAME_NUM_AVS = "HXNAVS";
    // public static final String FIELDNAME_PRENOM = "HTLDE2";
    // public static final String FIELDNAME_PRENOM_FOR_SEARCH = "HTLDU2";
    // public static final String FIELDNAME_SEXE = "HPTSEX";
    public static final String TABLE_AVS = "TIPAVSP";
    // public static final String TABLE_AVS_HISTO = "WAVDEV";
    public static final String TABLE_PERSONNE = "TIPERSP";

    // public static final String TABLE_TIERS = "TITIERP";

    /**
     * Génération de la clause from pour la requête > des Qds jusqu'au tiers
     * 
     * @param schema
     * @return la clause from
     */
    public static final String createFromClause(String schema) {
        StringBuffer fromClauseBuffer = new StringBuffer();
        String innerJoin = " INNER JOIN ";
        // String leftJoin = " LEFT JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFQd.TABLE_NAME);

        // jointure entre la table des Qds et la table des QdPrinicpales

        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFQdPrincipale.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFQd.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFQd.FIELDNAME_ID_QD);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFQdPrincipale.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFQdPrincipale.FIELDNAME_ID_QD_PRINCIPALE);

        // jointure entre la table des QdPrincipales et la table des périodes de
        // validités

        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFPeriodeValiditeQdPrincipale.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFQdPrincipale.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFQdPrincipale.FIELDNAME_ID_QD_PRINCIPALE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFPeriodeValiditeQdPrincipale.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFPeriodeValiditeQdPrincipale.FIELDNAME_ID_QD_PRINCIPALE);

        // jointure entre la table des QdBase et la table Association Qd,dossier

        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFAssQdDossier.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFQd.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFQd.FIELDNAME_ID_QD);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFAssQdDossier.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFAssQdDossier.FIELDNAME_ID_QD);

        // jointure entre la table Association Qd, dossier et la table des
        // dossier

        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDossier.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFAssQdDossier.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFAssQdDossier.FIELDNAME_ID_DOSSIER);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDossier.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDossier.FIELDNAME_ID_DOSSIER);

        // jointure entre la table des dossiers et la table des prDemande

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
        fromClauseBuffer.append(RFQdJointPeriodeValiditeJointDossierJointTiersJointDemande.TABLE_AVS);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(PRDemande.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(PRDemande.FIELDNAME_IDTIERS);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFQdJointPeriodeValiditeJointDossierJointTiersJointDemande.TABLE_AVS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFQdJointPeriodeValiditeJointDossierJointTiersJointDemande.FIELDNAME_ID_TIERS_TI);

        // jointure entre la table des numeros AVS et la table des personnes
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFQdJointPeriodeValiditeJointDossierJointTiersJointDemande.TABLE_PERSONNE);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFQdJointPeriodeValiditeJointDossierJointTiersJointDemande.TABLE_AVS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFQdJointPeriodeValiditeJointDossierJointTiersJointDemande.FIELDNAME_ID_TIERS_TI);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFQdJointPeriodeValiditeJointDossierJointTiersJointDemande.TABLE_PERSONNE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFQdJointPeriodeValiditeJointDossierJointTiersJointDemande.FIELDNAME_ID_TIERS_TI);

        // jointure entre la table des personnes et la table des tiers
        /*
         * fromClauseBuffer.append(innerJoin); fromClauseBuffer.append(schema); fromClauseBuffer.append(TABLE_TIERS);
         * fromClauseBuffer.append(on); fromClauseBuffer.append(schema); fromClauseBuffer.append(TABLE_PERSONNE);
         * fromClauseBuffer.append(point); fromClauseBuffer.append(FIELDNAME_ID_TIERS_TI);
         * fromClauseBuffer.append(egal); fromClauseBuffer.append(schema); fromClauseBuffer.append(TABLE_TIERS);
         * fromClauseBuffer.append(point); fromClauseBuffer.append(FIELDNAME_ID_TIERS_TI);
         */

        return fromClauseBuffer.toString();
    }

    private String dateDebutPeriode = "";
    private String dateFinPeriode = "";
    private String idDemande = "";
    private String idDossier = "";

    private String idPeriode = "";

    private String idTiers = "";
    // private String montantResiduel = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private Boolean isComprisDansCalcul = Boolean.FALSE;

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

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);

        idDossier = statement.dbReadNumeric(RFDossier.FIELDNAME_ID_DOSSIER);
        idDemande = statement.dbReadNumeric(RFDemande.FIELDNAME_ID_DEMANDE);
        idTiers = statement.dbReadNumeric(PRDemande.FIELDNAME_IDTIERS);

        dateDebutPeriode = statement.dbReadDateAMJ(RFPeriodeValiditeQdPrincipale.FIELDNAME_DATE_DEBUT);
        dateFinPeriode = statement.dbReadDateAMJ(RFPeriodeValiditeQdPrincipale.FIELDNAME_DATE_FIN);

        isComprisDansCalcul = statement.dbReadBoolean(RFAssQdDossier.FIELDNAME_IS_COMPRIS_DANS_CALCUL);

        idPeriode = statement.dbReadNumeric(RFPeriodeValiditeQdPrincipale.FIELDNAME_ID_PERIODE_VALIDITE);

        // montantResiduel =
        // statement.dbReadNumeric(RFQd.FIELDNAME_MONTANT_RESIDUEL);

    }

    public String getDateDebutPeriode() {
        return dateDebutPeriode;
    }

    public String getDateFinPeriode() {
        return dateFinPeriode;
    }

    public String getIdDemande() {
        return idDemande;
    }

    public String getIdDossier() {
        return idDossier;
    }

    public String getIdPeriode() {
        return idPeriode;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public Boolean getIsComprisDansCalcul() {
        return isComprisDansCalcul;
    }

    @Override
    public boolean hasSpy() {
        return false;
    }

    public void setDateDebutPeriode(String dateDebutPeriode) {
        this.dateDebutPeriode = dateDebutPeriode;
    }

    public void setDateFinPeriode(String dateFinPeriode) {
        this.dateFinPeriode = dateFinPeriode;
    }

    public void setIdDemande(String idDemande) {
        this.idDemande = idDemande;
    }

    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    public void setIdPeriode(String idPeriode) {
        this.idPeriode = idPeriode;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setIsComprisDansCalcul(Boolean isComprisDansCalcul) {
        this.isComprisDansCalcul = isComprisDansCalcul;
    }

    /*
     * public String getMontantResiduel() { return montantResiduel; }
     * 
     * public void setMontantResiduel(String montantResiduel) { this.montantResiduel = montantResiduel; }
     */

}