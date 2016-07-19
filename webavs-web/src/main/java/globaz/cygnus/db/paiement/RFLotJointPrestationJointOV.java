package globaz.cygnus.db.paiement;

import globaz.commons.nss.NSUtil;
import globaz.corvus.db.lots.RELot;
import globaz.cygnus.db.decisions.RFDecisionJointDemandeJointMotifRefusJointTiersValidation;
import globaz.cygnus.db.ordresversements.RFOrdresVersements;
import globaz.globall.db.BStatement;

/**
 * @author fha
 */
public class RFLotJointPrestationJointOV extends RFPrestation {

    private static final long serialVersionUID = 1L;

    public static final String FIELDNAME_NOM = "HTLDE1";
    public static final String FIELDNAME_NUM_AVS = "HXNAVS";
    public static final String FIELDNAME_PRENOM = "HTLDE2";

    private String csRole = "";
    private transient String fromClause = null;
    private String idDomaineApplication = "";
    private String idExterne = "";
    private String idLot = "";
    private String idOrdreVersement = "";
    private String idPrestation = "";
    private String idRole = "";
    private String idSectionOrdreVersement = "";
    private String idSousTypeDeSoin = "";
    private String idTiers = "";
    private String idTiersAdressePaiement = "";
    private String idTypeDeSoin = "";
    private Boolean isCompense = Boolean.FALSE;
    private Boolean isForcerPayement = Boolean.FALSE;
    private Boolean isImportation = Boolean.FALSE;
    private String montantDepassementQD = "";
    private String montantOrdreVersement = "";
    private String nom = "";
    private String nss = "";
    private String numeroFacture = "";
    private String prenom = "";
    private String typeVersement = "";
    private String refPaiement = "";

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
        fromClauseBuffer.append(RFLot.TABLE_NAME);

        /************* Faire aussi la jointure sur le gestionnaire *******/
        // jointure entre la table des RFLots et la table des RELots
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RELot.TABLE_NAME_LOT);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFLot.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFLot.FIELDNAME_ID_LOT_RFM);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RELot.TABLE_NAME_LOT);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RELot.FIELDNAME_ID_LOT);

        // jointure entre la table des prestations et la table des lots
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFPrestation.TABLE_NAME_PREST);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFPrestation.TABLE_NAME_PREST);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFPrestation.FIELDNAME_ID_LOT);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFLot.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFLot.FIELDNAME_ID_LOT_RFM);

        // jointure entre la table des OVs et la table des prestations
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFOrdresVersements.TABLE_NAME_ORVER);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFOrdresVersements.TABLE_NAME_ORVER);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFOrdresVersements.FIELDNAME_ID_PRESTATION);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFPrestation.TABLE_NAME_PREST);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFPrestation.FIELDNAME_ID_PRESTATION);

        // jointure entre la table OV (prestation) et la table des personnes
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFSimulerValidationDecision.TABLE_PERSONNE);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFSimulerValidationDecision.TABLE_PERSONNE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFSimulerValidationDecision.FIELDNAME_ID_TIERS_TI);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFOrdresVersements.TABLE_NAME_ORVER);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFOrdresVersements.FIELDNAME_ID_TIERS);

        // jointure entre la table des numeros AVS et la table des personnes
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDecisionJointDemandeJointMotifRefusJointTiersValidation.TABLE_AVS);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDecisionJointDemandeJointMotifRefusJointTiersValidation.TABLE_AVS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDecisionJointDemandeJointMotifRefusJointTiersValidation.FIELDNAME_ID_TIERS_TI);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDecisionJointDemandeJointMotifRefusJointTiersValidation.TABLE_PERSONNE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDecisionJointDemandeJointMotifRefusJointTiersValidation.FIELDNAME_ID_TIERS_TI);

        // jointure entre la table des personnes et la table des tiers
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDecisionJointDemandeJointMotifRefusJointTiersValidation.TABLE_TIERS);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDecisionJointDemandeJointMotifRefusJointTiersValidation.TABLE_PERSONNE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDecisionJointDemandeJointMotifRefusJointTiersValidation.FIELDNAME_ID_TIERS_TI);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDecisionJointDemandeJointMotifRefusJointTiersValidation.TABLE_TIERS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDecisionJointDemandeJointMotifRefusJointTiersValidation.FIELDNAME_ID_TIERS_TI);

        return fromClauseBuffer.toString();
    }

    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            StringBuffer from = new StringBuffer(RFLotJointPrestationJointOV.createFromClause(_getCollection()));
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

        idLot = statement.dbReadNumeric(RELot.FIELDNAME_ID_LOT);
        idPrestation = statement.dbReadNumeric(RFPrestation.FIELDNAME_ID_PRESTATION);
        refPaiement = statement.dbReadString(RFPrestation.FIELDNAME_REFERENCE_PAIEMENT);
        idOrdreVersement = statement.dbReadNumeric(RFOrdresVersements.FIELDNAME_ID_ORDRE_VERSEMENT);
        montantOrdreVersement = statement.dbReadNumeric(RFOrdresVersements.FIELDNAME_MONTANT);
        montantDepassementQD = statement.dbReadNumeric(RFOrdresVersements.FIELDNAME_MONTANT_DEPASSEMENT_QD);
        csRole = statement.dbReadNumeric(RFOrdresVersements.FIELDNAME_CS_ROLE);
        idDomaineApplication = statement.dbReadNumeric(RFOrdresVersements.FIELDNAME_ID_DOMAINE_APPLICATION);
        idExterne = statement.dbReadNumeric(RFOrdresVersements.FIELDNAME_ID_EXTERNE);
        idRole = statement.dbReadNumeric(RFOrdresVersements.FIELDNAME_ID_ROLE);
        idTiers = statement.dbReadNumeric(RFOrdresVersements.FIELDNAME_ID_TIERS);
        idTiersAdressePaiement = statement.dbReadNumeric(RFOrdresVersements.FIELDNAME_ID_TIERS_ADRESSE_PAIEMENT);
        numeroFacture = statement.dbReadNumeric(RFOrdresVersements.FIELDNAME_NUMERO_FACTURE);
        typeVersement = statement.dbReadNumeric(RFOrdresVersements.FIELDNAME_TYPE_VERSEMENT);
        idTypeDeSoin = statement.dbReadNumeric(RFOrdresVersements.FIELDNAME_ID_TYPE_DE_SOIN);
        idSousTypeDeSoin = statement.dbReadNumeric(RFOrdresVersements.FIELDNAME_ID_SOUS_TYPE_DE_SOIN);
        isForcerPayement = statement.dbReadBoolean(RFOrdresVersements.FIELDNAME_IS_FORCER_PAYEMENT);
        nss = NSUtil.formatAVSUnknown(statement.dbReadString(RFLotJointPrestationJointOV.FIELDNAME_NUM_AVS));
        nom = statement.dbReadString(RFLotJointPrestationJointOV.FIELDNAME_NOM);
        prenom = statement.dbReadString(RFLotJointPrestationJointOV.FIELDNAME_PRENOM);
        isImportation = statement.dbReadBoolean(RFOrdresVersements.FIELDNAME_IS_IMPORTATION);
        isCompense = statement.dbReadBoolean(RFOrdresVersements.FIELDNAME_IS_COMPENSE);
        idSectionOrdreVersement = statement.dbReadNumeric(RFOrdresVersements.FIELDNAME_ID_SECTION_DETTE);
    }

    public String getCsRole() {
        return csRole;
    }

    public String getFromClause() {
        return fromClause;
    }

    public String getIdDomaineApplication() {
        return idDomaineApplication;
    }

    public String getIdExterne() {
        return idExterne;
    }

    @Override
    public String getIdLot() {
        return idLot;
    }

    public String getIdOrdreVersement() {
        return idOrdreVersement;
    }

    @Override
    public String getIdPrestation() {
        return idPrestation;
    }

    public String getIdRole() {
        return idRole;
    }

    public String getIdSectionOrdreVersement() {
        return idSectionOrdreVersement;
    }

    public String getIdSousTypeDeSoin() {
        return idSousTypeDeSoin;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getIdTiersAdressePaiement() {
        return idTiersAdressePaiement;
    }

    public String getIdTypeDeSoin() {
        return idTypeDeSoin;
    }

    public Boolean getIsCompense() {
        return isCompense;
    }

    public Boolean getIsForcerPayement() {
        return isForcerPayement;
    }

    public Boolean getIsImportation() {
        return isImportation;
    }

    public String getMontantDepassementQD() {
        return montantDepassementQD;
    }

    public String getMontantOrdreVersement() {
        return montantOrdreVersement;
    }

    public String getNom() {
        return nom;
    }

    public String getNss() {
        return nss;
    }

    public String getNumeroFacture() {
        return numeroFacture;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getTypeVersement() {
        return typeVersement;
    }

    public void setCsRole(String csRole) {
        this.csRole = csRole;
    }

    public void setFromClause(String fromClause) {
        this.fromClause = fromClause;
    }

    public void setIdDomaineApplication(String idDomaineApplication) {
        this.idDomaineApplication = idDomaineApplication;
    }

    public void setIdExterne(String idExterne) {
        this.idExterne = idExterne;
    }

    @Override
    public void setIdLot(String idLot) {
        this.idLot = idLot;
    }

    public void setIdOrdreVersement(String idOrdreVersement) {
        this.idOrdreVersement = idOrdreVersement;
    }

    @Override
    public void setIdPrestation(String idPrestation) {
        this.idPrestation = idPrestation;
    }

    public void setIdRole(String idRole) {
        this.idRole = idRole;
    }

    public void setIdSectionOrdreVersement(String idSectionOrdreVersement) {
        this.idSectionOrdreVersement = idSectionOrdreVersement;
    }

    public void setIdSousTypeDeSoin(String idSousTypeDeSoin) {
        this.idSousTypeDeSoin = idSousTypeDeSoin;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setIdTiersAdressePaiement(String idTiersAdressePaiement) {
        this.idTiersAdressePaiement = idTiersAdressePaiement;
    }

    public void setIdTypeDeSoin(String idTypeDeSoin) {
        this.idTypeDeSoin = idTypeDeSoin;
    }

    public void setIsCompense(Boolean isCompense) {
        this.isCompense = isCompense;
    }

    public void setIsForcerPayement(Boolean isForcerPayement) {
        this.isForcerPayement = isForcerPayement;
    }

    public void setIsImportation(Boolean isImportation) {
        this.isImportation = isImportation;
    }

    public void setMontantDepassementQD(String montantDepassementQD) {
        this.montantDepassementQD = montantDepassementQD;
    }

    public void setMontantOrdreVersement(String montantOrdreVersement) {
        this.montantOrdreVersement = montantOrdreVersement;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public void setNumeroFacture(String numeroFacture) {
        this.numeroFacture = numeroFacture;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setTypeVersement(String typeVersement) {
        this.typeVersement = typeVersement;
    }

    public String getRefPaiement() {
        return refPaiement;
    }

    public void setRefPaiement(String refPaiement) {
        this.refPaiement = refPaiement;
    }
}
