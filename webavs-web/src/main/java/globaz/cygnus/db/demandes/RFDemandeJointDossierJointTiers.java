/*
 * Créé le 07 janvier 2010
 */
package globaz.cygnus.db.demandes;

import globaz.commons.nss.NSUtil;
import globaz.cygnus.db.decisions.RFDecision;
import globaz.cygnus.db.dossiers.RFDossier;
import globaz.cygnus.db.dossiers.RFDossierJointDecisionJointTiers;
import globaz.cygnus.db.motifsDeRefus.RFAssMotifsRefusDemande;
import globaz.cygnus.db.typeDeSoins.RFSousTypeDeSoin;
import globaz.cygnus.db.typeDeSoins.RFTypeDeSoin;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.tools.PRHierarchique;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * @author jje
 */
public class RFDemandeJointDossierJointTiers extends RFDemande implements PRHierarchique {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_DATEDECES = "HPDDEC";
    public static final String FIELDNAME_DATENAISSANCE = "HPDNAI";

    public static final String FIELDNAME_ID_DECISION_ASS_DOS_DEC = "ECIDOS";

    public static final String FIELDNAME_ID_DOSSIER_ASS_DOS_DEC = "ECIDEC";
    public static final String FIELDNAME_ID_GESTIONNAIRE = "KUSER";
    public static final String FIELDNAME_ID_TIERS_TI = "HTITIE";
    public static final String FIELDNAME_NATIONALITE = "HNIPAY";
    public static final String FIELDNAME_NOM = "HTLDE1";
    public static final String FIELDNAME_NOM_FOR_SEARCH = "HTLDU1";

    public static final String FIELDNAME_NUM_AVS = "HXNAVS";
    public static final String FIELDNAME_PRENOM = "HTLDE2";
    public static final String FIELDNAME_PRENOM_FOR_SEARCH = "HTLDU2";
    public static final String FIELDNAME_SEXE = "HPTSEX";
    public static final String FIELDNAME_VISA_GESTIONNAIRE = "FVISA";

    public static final String TABLE_ASS_DOSSIER_DECISION = "RFADODE";
    public static final String TABLE_AVS = "TIPAVSP";
    public static final String TABLE_AVS_HISTO = "TIHAVSP";

    public static final String TABLE_GESTIONNAIRES = "FWSUSRP";
    public static final String TABLE_PERSONNE = "TIPERSP";
    public static final String TABLE_TIERS = "TITIERP";

    /**
     * Génération de la clause from pour la requête > Jointure depuis les dossiers jusqu'au décisions RFM
     * 
     * @param schema
     * 
     * @return la clause from
     */
    public static final String createFromClause(String schema, String idMotifRefusGrandeQd) {

        StringBuffer fromClauseBuffer = new StringBuffer();
        String innerJoin = " INNER JOIN ";
        String leftJoin = " LEFT JOIN ";
        String on = " ON ";
        String and = " AND ";
        String point = ".";
        String egal = "=";
        String openParenthese = " (";
        String closeParenthese = ") ";

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

        // jointure entre la table des demandes et la table des gestionnaires
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDemandeJointDossierJointTiers.TABLE_GESTIONNAIRES);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDemande.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDemande.FIELDNAME_ID_GESTIONNAIRE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDemandeJointDossierJointTiers.TABLE_GESTIONNAIRES);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDemandeJointDossierJointTiers.FIELDNAME_ID_GESTIONNAIRE);

        // jointure entre la table des dossiers et la table ass. des décisions
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDemandeJointDossierJointTiers.TABLE_ASS_DOSSIER_DECISION);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDossier.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDossier.FIELDNAME_ID_DOSSIER);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDemandeJointDossierJointTiers.TABLE_ASS_DOSSIER_DECISION);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDemandeJointDossierJointTiers.FIELDNAME_ID_DOSSIER_ASS_DOS_DEC);

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

        if (!JadeStringUtil.isBlankOrZero(idMotifRefusGrandeQd)) {
            // jointure association motif de refus dem jointure entre la table des demandes et la table des motifs de
            // refus
            fromClauseBuffer.append(leftJoin);
            fromClauseBuffer.append(schema);
            fromClauseBuffer.append(RFAssMotifsRefusDemande.TABLE_NAME);
            fromClauseBuffer.append(on);

            fromClauseBuffer.append(openParenthese);
            fromClauseBuffer.append(schema);
            fromClauseBuffer.append(RFDemande.TABLE_NAME);
            fromClauseBuffer.append(point);
            fromClauseBuffer.append(RFDemande.FIELDNAME_ID_DEMANDE);
            fromClauseBuffer.append(egal);
            fromClauseBuffer.append(schema);
            fromClauseBuffer.append(RFAssMotifsRefusDemande.TABLE_NAME);
            fromClauseBuffer.append(point);
            fromClauseBuffer.append(RFAssMotifsRefusDemande.FIELDNAME_ID_DEMANDE);
            fromClauseBuffer.append(and);
            fromClauseBuffer.append(RFAssMotifsRefusDemande.FIELDNAME_ID_MOTIF_REFUS);
            fromClauseBuffer.append(egal);
            fromClauseBuffer.append(idMotifRefusGrandeQd);
            fromClauseBuffer.append(closeParenthese);
        }

        return fromClauseBuffer.toString();
    }

    private String codeSousTypeDeSoin = "";

    private String codeTypeDeSoin = "";

    private String csCanton = "";

    private String csNationalite = "";

    private String csSexe = "";
    private String dateDeces = "";
    private String dateNaissance = "";
    private transient String fromClause = null;
    private String idMajeur = "";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------
    private String idParent = "";

    private String idSousTypeDeSoin = "";
    private String idTiers = "";
    private String idTypeDeSoin = "";
    private boolean isMembreFamilleSet = false;
    private Vector<String[]> listDevisData = null;
    private String listIdDevis = "";

    private String montantDepassementQd = "";
    private Map<String, String> montantsMotifsRefus = null;

    private String nom = "";
    // champs nécessaires description tiers
    private String nss = "";

    private String prenom = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    // champs nécessaires description gestionnaire
    private String visaGestionnaire = "";

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
            fromClause = RFDemandeJointDossierJointTiers.createFromClause(_getCollection(), "");
        }
        return fromClause;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);

        nss = NSUtil.formatAVSUnknown(statement.dbReadString(RFDemandeJointDossierJointTiers.FIELDNAME_NUM_AVS));
        dateNaissance = statement.dbReadDateAMJ(RFDemandeJointDossierJointTiers.FIELDNAME_DATENAISSANCE);
        dateDeces = statement.dbReadDateAMJ(RFDemandeJointDossierJointTiers.FIELDNAME_DATEDECES);
        csSexe = statement.dbReadNumeric(RFDemandeJointDossierJointTiers.FIELDNAME_SEXE);
        nom = statement.dbReadString(RFDemandeJointDossierJointTiers.FIELDNAME_NOM);
        prenom = statement.dbReadString(RFDemandeJointDossierJointTiers.FIELDNAME_PRENOM);
        visaGestionnaire = statement.dbReadString(RFDemandeJointDossierJointTiers.FIELDNAME_VISA_GESTIONNAIRE);
        codeSousTypeDeSoin = statement.dbReadString(RFSousTypeDeSoin.FIELDNAME_CODE);
        codeTypeDeSoin = statement.dbReadString(RFTypeDeSoin.FIELDNAME_CODE);
        idSousTypeDeSoin = statement.dbReadString(RFSousTypeDeSoin.FIELDNAME_ID_SOUS_TYPE_SOIN);
        idTypeDeSoin = statement.dbReadString(RFTypeDeSoin.FIELDNAME_ID_TYPE_SOIN);
        csNationalite = statement.dbReadString(RFDossierJointDecisionJointTiers.FIELDNAME_NATIONALITE);
        montantDepassementQd = statement.dbReadNumeric(RFAssMotifsRefusDemande.FIELDNAME_MNT_MOTIF_REFUS, 2);

        idTiers = statement.dbReadString(RFDemandeJointDossierJointTiers.FIELDNAME_ID_TIERS_TI);

    }

    public String getCodeSousTypeDeSoin() {
        return codeSousTypeDeSoin;
    }

    public String getCodeTypeDeSoin() {
        return codeTypeDeSoin;
    }

    public String getCsCanton() {
        return csCanton;
    }

    public String getCsNationalite() {
        return csNationalite;
    }

    public String getCsSexe() {
        return csSexe;
    }

    public String getDateDeces() {
        return dateDeces;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    @Override
    public String getIdMajeur() {
        return getIdDemande();
    }

    @Override
    public String getIdParent() {
        return getIdDemandeParent();
    }

    @Override
    public String getIdSousTypeDeSoin() {
        return idSousTypeDeSoin;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getIdTypeDeSoin() {
        return idTypeDeSoin;
    }

    public Vector<String[]> getListDevisData() {
        return listDevisData;
    }

    public String getListIdDevis() {
        return listIdDevis;
    }

    public String getMontantDepassementQd() {
        return montantDepassementQd;
    }

    public Map<String, String> getMontantsMotifsRefus() {
        if (null == montantsMotifsRefus) {
            montantsMotifsRefus = new HashMap<String, String>();
        }
        return montantsMotifsRefus;
    }

    public String getNom() {
        return nom;
    }

    public String getNss() {
        return nss;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getVisaGestionnaire() {
        return visaGestionnaire;
    }

    @Override
    public boolean hasSpy() {
        return false;
    }

    public boolean isMembreFamilleSet() {
        return isMembreFamilleSet;
    }

    public void setCodeSousTypeDeSoin(String codeSousTypeDeSoin) {
        this.codeSousTypeDeSoin = codeSousTypeDeSoin;
    }

    public void setCodeTypeDeSoin(String codeTypeDeSoin) {
        this.codeTypeDeSoin = codeTypeDeSoin;
    }

    public void setCsCanton(String csCanton) {
        this.csCanton = csCanton;
    }

    public void setCsNationalite(String csNationalite) {
        this.csNationalite = csNationalite;
    }

    public void setCsSexe(String csSexe) {
        this.csSexe = csSexe;
    }

    public void setDateDeces(String dateDeces) {
        this.dateDeces = dateDeces;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    @Override
    public void setIdSousTypeDeSoin(String idSousTypeDeSoin) {
        this.idSousTypeDeSoin = idSousTypeDeSoin;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setIdTypeDeSoin(String idTypeDeSoin) {
        this.idTypeDeSoin = idTypeDeSoin;
    }

    /*
     * public void setIdMajeur(String idMajeur) { this.idMajeur = idMajeur; }
     * 
     * public void setIdParent(String idParent) { this.idParent = idParent; }
     */

    public void setListDevisData(Vector<String[]> listDevisData) {
        this.listDevisData = listDevisData;
    }

    public void setListIdDevis(String listIdDevis) {
        this.listIdDevis = listIdDevis;
    }

    public void setMembreFamilleSet(boolean isMembreFamilleSet) {
        this.isMembreFamilleSet = isMembreFamilleSet;
    }

    public void setMontantDepassementQd(String montantDepassementQd) {
        this.montantDepassementQd = montantDepassementQd;
    }

    public void setMontantsMotifsRefus(Map<String, String> montantsMotifsRefus) {
        if (!isMembreFamilleSet()) {
            this.montantsMotifsRefus = montantsMotifsRefus;
        }
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setVisaGestionnaire(String visaGestionnaire) {
        this.visaGestionnaire = visaGestionnaire;
    }

}
