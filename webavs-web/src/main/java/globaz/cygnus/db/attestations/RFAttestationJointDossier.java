/*
 * Créé le 31 août 2011
 */
package globaz.cygnus.db.attestations;

import globaz.cygnus.api.attestations.IRFAttestations;
import globaz.cygnus.api.demandes.IRFDemande;
import globaz.cygnus.db.dossiers.RFDossier;
import globaz.cygnus.db.typeDeSoins.RFSousTypeDeSoin;
import globaz.cygnus.db.typeDeSoins.RFTypeDeSoin;
import globaz.globall.db.BStatement;
import globaz.prestation.tools.PRCodeSystem;
import java.util.Vector;

/**
 * author fha
 */
public class RFAttestationJointDossier extends RFAssAttestationDossier {

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

        return fromClauseBuffer.toString();
    }

    private transient Vector<String[]> csNiveauAvertissement = null;
    private transient Vector<String[]> csTypeAttestation = null;

    private String dateCreation = "";
    private String forCsNiveauAvertissement = "";
    private String forCsTypeAttestation = "";
    private String forOrderBy = "";
    private transient String fromClause = null;
    // champs nécessaires table association
    private String idDossier = "";

    private String idTypeSoin = "";

    private transient Vector<String[]> orderBy = null;

    private String sousTypeSoin = "";

    private String typeSoin = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    // Constructor
    /**
     * Crée une nouvelle instance de la classe RFSousTypeSoin
     */
    public RFAttestationJointDossier() {
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

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            StringBuffer from = new StringBuffer(RFAttestationJointDossier.createFromClause(_getCollection()));
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

        idDossier = statement.dbReadNumeric(RFAssAttestationDossier.FIELDNAME_ID_DOSSIER);
        dateCreation = statement.dbReadDateAMJ(RFAttestation.FIELDNAME_DATE_CREATION);
        sousTypeSoin = statement.dbReadString(RFSousTypeDeSoin.FIELDNAME_CODE);
        typeSoin = statement.dbReadString(RFTypeDeSoin.FIELDNAME_CODE);
        forCsTypeAttestation = statement.dbReadNumeric(RFAttestation.FIELDNAME_TYPE_DOCUMENT);
        idTypeSoin = statement.dbReadNumeric(RFTypeDeSoin.FIELDNAME_ID_TYPE_SOIN);

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

    public String getIdTypeSoin() {
        return idTypeSoin;
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
            orderBy.add(new String[] { RFAttestation.FIELDNAME_TYPE_DOCUMENT,
                    getSession().getLabel("JSP_RF_DECISION_TRIER_PAR_TYPE_DOCUMENT") });
        }
        return orderBy;
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

    public void setCsNiveauAvertissement(Vector<String[]> csNiveauAvertissement) {
        this.csNiveauAvertissement = csNiveauAvertissement;
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

    public void setIdTypeSoin(String idTypeSoin) {
        this.idTypeSoin = idTypeSoin;
    }

    public void setOrderBy(Vector<String[]> orderBy) {
        this.orderBy = orderBy;
    }

    public void setSousTypeSoin(String sousTypeSoin) {
        this.sousTypeSoin = sousTypeSoin;
    }

    public void setTypeSoin(String typeSoin) {
        this.typeSoin = typeSoin;
    }

}
