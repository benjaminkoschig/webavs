package globaz.cygnus.db.attestations;

import globaz.commons.nss.NSUtil;
import globaz.cygnus.api.attestations.IRFAttestations;
import globaz.cygnus.api.demandes.IRFDemande;
import globaz.cygnus.db.dossiers.RFDossier;
import globaz.cygnus.db.typeDeSoins.RFSousTypeDeSoin;
import globaz.cygnus.db.typeDeSoins.RFTypeDeSoin;
import globaz.globall.db.BStatement;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.tools.PRCodeSystem;
import java.util.Vector;

/**
 * author fha
 */
public class RFAttestationJointDossierJointTiers extends RFAssAttestationDossier {

    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_DATEDECES = "HPDDEC";
    public static final String FIELDNAME_DATENAISSANCE = "HPDNAI";

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
    public static final String TABLE_AVS = "TIPAVSP";
    public static final String TABLE_AVS_HISTO = "TIHAVSP";
    public static final String TABLE_GESTIONNAIRES = "FWSUSRP";

    public static final String TABLE_PERSONNE = "TIPERSP";
    public static final String TABLE_PRDEMAP = "PRDEMAP";
    public static final String TABLE_TIERS = "TITIERP";

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
        String on = " ON ";
        String point = ".";
        String egal = "=";

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFAttestation.TABLE_NAME);

        // jointure entre la table des attestations et la table association
        // dossiers-attestation
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFAssAttestationDossier.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFAssAttestationDossier.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFAssAttestationDossier.FIELDNAME_ID_ATTESTATION);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFAttestation.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFAttestation.FIELDNAME_ID_ATTESTATION);

        // jointure entre la table association dossiers-attestation et la table
        // dossier
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDossier.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDossier.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDossier.FIELDNAME_ID_DOSSIER);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFAssAttestationDossier.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFAssAttestationDossier.FIELDNAME_ID_DOSSIER);

        // jointure entre la table association dossiers-attestation et la table
        // sous type de soin
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFSousTypeDeSoin.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFSousTypeDeSoin.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFSousTypeDeSoin.FIELDNAME_ID_SOUS_TYPE_SOIN);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFAssAttestationDossier.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFAssAttestationDossier.FIELDNAME_ID_SOUS_TYPE_SOIN);

        // jointure entre la table association sous type de soin et la table
        // type de soin
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFTypeDeSoin.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFTypeDeSoin.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFTypeDeSoin.FIELDNAME_ID_TYPE_SOIN);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFSousTypeDeSoin.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFSousTypeDeSoin.FIELDNAME_ID_TYPE_SOIN);

        // jointure entre la table dossier et la table PRDEMAP
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

        // jointure entre la table des demandes et la table des numeros AVS
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFAttestationJointDossierJointTiers.TABLE_AVS);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(PRDemande.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(PRDemande.FIELDNAME_IDTIERS);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFAttestationJointDossierJointTiers.TABLE_AVS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFAttestationJointDossierJointTiers.FIELDNAME_ID_TIERS_TI);

        // jointure entre la table des numeros AVS et la table des personnes
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFAttestationJointDossierJointTiers.TABLE_PERSONNE);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFAttestationJointDossierJointTiers.TABLE_AVS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFAttestationJointDossierJointTiers.FIELDNAME_ID_TIERS_TI);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFAttestationJointDossierJointTiers.TABLE_PERSONNE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFAttestationJointDossierJointTiers.FIELDNAME_ID_TIERS_TI);

        // jointure entre la table des personnes et la table des tiers
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFAttestationJointDossierJointTiers.TABLE_TIERS);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFAttestationJointDossierJointTiers.TABLE_PERSONNE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFAttestationJointDossierJointTiers.FIELDNAME_ID_TIERS_TI);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFAttestationJointDossierJointTiers.TABLE_TIERS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFAttestationJointDossierJointTiers.FIELDNAME_ID_TIERS_TI);

        return fromClauseBuffer.toString();
    }

    private String csCanton = "";
    private String csNationalite = "";

    private transient Vector<String[]> csNiveauAvertissement = null;
    private String csSexe = "";

    private transient Vector<String[]> csTypeAttestation = null;

    private String dateCreation = "";
    private String dateDeces = "";
    private String dateNaissance = "";
    private String forCsNiveauAvertissement = "";
    private String forCsTypeAttestation = "";
    private String forOrderBy = "";
    private transient String fromClause = null;
    // champs nécessaires table association
    private String idDossier = "";

    // champs nécessaires description gestionnaire : est ce nécessaire?
    private String idGestionnaire = "";

    private String idTypeSoin = "";

    private String nom = "";

    // champs nécessaires description tiers
    private String nss = "";

    private transient Vector<String[]> orderBy = null;

    private String prenom = "";

    private String sousTypeSoin = "";

    private String typeSoin = "";

    /**
     * Crée une nouvelle instance de la classe RFSousTypeSoin
     */
    public RFAttestationJointDossierJointTiers() {
        super();
    }

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

    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            StringBuffer from = new StringBuffer(RFAttestationJointDossierJointTiers.createFromClause(_getCollection()));
            fromClause = from.toString();
        }

        return fromClause;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);

        nss = NSUtil.formatAVSUnknown(statement.dbReadString(RFAttestationJointDossierJointTiers.FIELDNAME_NUM_AVS));
        dateNaissance = statement.dbReadDateAMJ(RFAttestationJointDossierJointTiers.FIELDNAME_DATENAISSANCE);
        dateDeces = statement.dbReadDateAMJ(RFAttestationJointDossierJointTiers.FIELDNAME_DATEDECES);
        csSexe = statement.dbReadNumeric(RFAttestationJointDossierJointTiers.FIELDNAME_SEXE);
        nom = statement.dbReadString(RFAttestationJointDossierJointTiers.FIELDNAME_NOM);
        prenom = statement.dbReadString(RFAttestationJointDossierJointTiers.FIELDNAME_PRENOM);
        idGestionnaire = statement.dbReadString(RFAttestation.FIELDNAME_ID_GESTIONNAIRE);
        idDossier = statement.dbReadNumeric(RFAssAttestationDossier.FIELDNAME_ID_DOSSIER);
        dateCreation = statement.dbReadDateAMJ(RFAttestation.FIELDNAME_DATE_CREATION);
        sousTypeSoin = statement.dbReadString(RFSousTypeDeSoin.FIELDNAME_CODE);
        typeSoin = statement.dbReadString(RFTypeDeSoin.FIELDNAME_CODE);
        forCsTypeAttestation = statement.dbReadNumeric(RFAttestation.FIELDNAME_TYPE_DOCUMENT);
        idTypeSoin = statement.dbReadNumeric(RFTypeDeSoin.FIELDNAME_ID_TYPE_SOIN);
        csNationalite = statement.dbReadString(RFAttestationJointDossierJointTiers.FIELDNAME_NATIONALITE);
    }

    public String getCsCanton() {
        return csCanton;
    }

    public String getCsNationalite() {
        return csNationalite;
    }

    public Vector<String[]> getCsNiveauAvertissement() {
        return csNiveauAvertissement;
    }

    public Vector<String[]> getCsNiveauAvertissementData() {
        if (csNiveauAvertissement == null) {
            csNiveauAvertissement = PRCodeSystem.getLibellesPourGroupe(IRFDemande.CS_GROUPE_STATUT_DEMANDE,
                    getSession());
        }

        return csNiveauAvertissement;
    }

    public String getCsSexe() {
        return csSexe;
    }

    public Vector<String[]> getCsTypeAttestation() {
        return csTypeAttestation;
    }

    public Vector<String[]> getCsTypeAttestationData() {
        if (csTypeAttestation == null) {
            csTypeAttestation = PRCodeSystem.getLibellesPourGroupe(IRFAttestations.CS_TYPE_ATTESTATION, getSession());

            csTypeAttestation.add(0, new String[] { "", "" });
        }

        return csTypeAttestation;
    }

    public String getDateCreation() {
        return dateCreation;
    }

    public String getDateDeces() {
        return dateDeces;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public String getForCsNiveauAvertissement() {
        return forCsNiveauAvertissement;
    }

    public String getForCsTypeAttestation() {
        return forCsTypeAttestation;
    }

    public String getForOrderBy() {
        return forOrderBy;
    }

    public String getFromClause() {
        return fromClause;
    }

    @Override
    public String getIdDossier() {
        return idDossier;
    }

    public String getIdGestionnaire() {
        return idGestionnaire;
    }

    public String getIdTypeSoin() {
        return idTypeSoin;
    }

    public String getNom() {
        return nom;
    }

    public String getNss() {
        return nss;
    }

    public Vector<String[]> getOrderBy() {
        return orderBy;
    }

    /**
     * getter pour l'attribut order by data
     * 
     * @return la valeur courante de l'attribut order by data
     */
    public Vector<String[]> getOrderByData() {
        if (orderBy == null) {
            orderBy = new Vector<String[]>(2);
            orderBy.add(new String[] { "EXDFIN asc, EXDDEB desc",
                    getSession().getLabel("JSP_RF_DECISION_TRIER_PAR_DATE") });
            orderBy.add(new String[] { RFAttestation.FIELDNAME_TYPE_DOCUMENT,
                    getSession().getLabel("JSP_RF_DECISION_TRIER_PAR_TYPE_DOCUMENT") });

        }
        return orderBy;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getSousTypeSoin() {
        return sousTypeSoin;
    }

    public String getTypeSoin() {
        return typeSoin;
    }

    @Override
    public boolean hasSpy() {
        return false;
    }

    public void setCsCanton(String csCanton) {
        this.csCanton = csCanton;
    }

    public void setCsNationalite(String csNationalite) {
        this.csNationalite = csNationalite;
    }

    public void setCsNiveauAvertissement(Vector<String[]> csNiveauAvertissement) {
        this.csNiveauAvertissement = csNiveauAvertissement;
    }

    public void setCsSexe(String csSexe) {
        this.csSexe = csSexe;
    }

    public void setCsTypeAttestation(Vector<String[]> csTypeAttestation) {
        this.csTypeAttestation = csTypeAttestation;
    }

    public void setCsTypeAttestationData(Vector<String[]> csTypeAttestationData) {
        csTypeAttestation = csTypeAttestationData;
    }

    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }

    public void setDateDeces(String dateDeces) {
        this.dateDeces = dateDeces;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setForCsNiveauAvertissement(String forCsNiveauAvertissement) {
        this.forCsNiveauAvertissement = forCsNiveauAvertissement;
    }

    public void setForCsTypeAttestation(String forCsTypeAttestation) {
        this.forCsTypeAttestation = forCsTypeAttestation;
    }

    public void setForOrderBy(String forOrderBy) {
        this.forOrderBy = forOrderBy;
    }

    public void setFromClause(String fromClause) {
        this.fromClause = fromClause;
    }

    @Override
    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    public void setIdGestionnaire(String idGestionnaire) {
        this.idGestionnaire = idGestionnaire;
    }

    public void setIdTypeSoin(String idTypeSoin) {
        this.idTypeSoin = idTypeSoin;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public void setOrderBy(Vector<String[]> orderBy) {
        this.orderBy = orderBy;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setSousTypeSoin(String sousTypeSoin) {
        this.sousTypeSoin = sousTypeSoin;
    }

    public void setTypeSoin(String typeSoin) {
        this.typeSoin = typeSoin;
    }

}
