package globaz.corvus.db.rentesaccordees;

import globaz.corvus.db.basescalcul.REBasesCalcul;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.hera.db.famille.SFConjoint;
import globaz.hera.db.famille.SFMembreFamille;
import globaz.hera.db.famille.SFRelationConjoint;
import globaz.pyxis.db.tiers.ITIPersonneAvsDefTable;
import globaz.pyxis.db.tiers.ITIPersonneDefTable;
import globaz.pyxis.db.tiers.ITITiersDefTable;

/**
 *
 */
public class RERenteActiveJoinMembresFamille extends BEntity {

    public String getIdInformationComptable() {
        return idInformationComptable;
    }

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String codeNationalite;
    private String codePrestation;
    private String cs1;
    private String cs2;
    private String cs3;
    private String cs4;
    private String cs5;
    private String csDomaine;
    private String csEtatPrestationAccordee;
    private String csSexe;
    private String csTypeRelation;
    private String dateDebutRelation;
    private String dateDeces;
    private String dateFinRelation;
    private String dateNaissance;
    private String degreInvalidite;
    private String fractionRente;
    private String idConjoint;
    private String idMFBeneficiaire;
    private String idMFConjoint1;
    private String idMFConjoint2;
    private String idPrestationAccordee;
    private String idTiers;
    private String idTiersBaseCalcul;
    private boolean isTiersInactif;
    private String nom;
    private String nss;
    private String prenom;
    private String idInformationComptable;

    public RERenteActiveJoinMembresFamille() {
        super();

        codeNationalite = "";
        codePrestation = "";
        cs1 = "";
        cs2 = "";
        cs3 = "";
        cs4 = "";
        cs5 = "";
        csDomaine = "";
        csSexe = "";
        csTypeRelation = "";
        dateDebutRelation = "";
        dateDeces = "";
        dateFinRelation = "";
        dateNaissance = "";
        degreInvalidite = "";
        fractionRente = "";
        idConjoint = "";
        idMFBeneficiaire = "";
        idMFConjoint1 = "";
        idMFConjoint2 = "";
        idPrestationAccordee = "";
        idTiers = "";
        idTiersBaseCalcul = "";
        isTiersInactif = false;
        nom = "";
        nss = "";
        prenom = "";
        idInformationComptable = "";
    }

    @Override
    protected boolean _allowAdd() {
        return false;
    }

    @Override
    protected boolean _allowDelete() {
        return false;
    }

    @Override
    protected boolean _allowUpdate() {
        return false;
    }

    @Override
    protected String _getFields(BStatement statement) {

        StringBuilder sql = new StringBuilder();

        String tablePrestationAccordee = _getCollection() + REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES;
        String tableRenteAccordee = _getCollection() + RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE;
        String tableBaseCalcul = _getCollection() + REBasesCalcul.TABLE_NAME_BASES_CALCUL;
        String tableTiers = _getCollection() + ITITiersDefTable.TABLE_NAME;
        String tablePersonne = _getCollection() + ITIPersonneDefTable.TABLE_NAME;
        String tablePersonneAvs = _getCollection() + ITIPersonneAvsDefTable.TABLE_NAME;
        String tableMembreFamille = _getCollection() + SFMembreFamille.TABLE_NAME;
        String tableConjoint = _getCollection() + SFConjoint.TABLE_NAME;
        String tableRelationConjugale = _getCollection() + SFRelationConjoint.TABLE_NAME;
        String tableInformationComptabilite = _getCollection() + REInformationsComptabilite.TABLE_NAME_INFO_COMPTA;

        sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE)
                .append(",");
        sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_CODE_PRESTATION)
                .append(",");
        sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_CS_ETAT).append(",");
        sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_FRACTION_RENTE)
                .append(",");
        sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE)
                .append(",");
        sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_ID_INFO_COMPTA)
                .append(",");

        sql.append(tableRenteAccordee).append(".").append(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_1).append(",");
        sql.append(tableRenteAccordee).append(".").append(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_2).append(",");
        sql.append(tableRenteAccordee).append(".").append(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_3).append(",");
        sql.append(tableRenteAccordee).append(".").append(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_4).append(",");
        sql.append(tableRenteAccordee).append(".").append(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_5).append(",");

        sql.append(tableBaseCalcul).append(".").append(REBasesCalcul.FIELDNAME_DEGRE_INVALIDITE).append(",");
        sql.append(tableBaseCalcul).append(".").append(REBasesCalcul.FIELDNAME_ID_TIERS_BASE_CALCUL).append(",");

        sql.append(tableTiers).append(".").append(ITITiersDefTable.DESIGNATION_1).append(",");
        sql.append(tableTiers).append(".").append(ITITiersDefTable.DESIGNATION_2).append(",");
        sql.append(tableTiers).append(".").append(ITITiersDefTable.ID_PAYS).append(",");
        sql.append(tableTiers).append(".").append(ITITiersDefTable.IS_INACTIF).append(",");

        sql.append(tablePersonne).append(".").append(ITIPersonneDefTable.CS_SEXE).append(",");
        sql.append(tablePersonne).append(".").append(ITIPersonneDefTable.DATE_DECES).append(",");
        sql.append(tablePersonne).append(".").append(ITIPersonneDefTable.DATE_NAISSANCE).append(",");

        sql.append(tablePersonneAvs).append(".").append(ITIPersonneAvsDefTable.NUMERO_AVS_ACTUEL).append(",");

        sql.append(tableMembreFamille).append(".").append(SFMembreFamille.FIELD_IDTIERS).append(",");
        sql.append(tableMembreFamille).append(".").append(SFMembreFamille.FIELD_IDMEMBREFAMILLE).append(",");
        sql.append(tableMembreFamille).append(".").append(SFMembreFamille.FIELD_DOMAINE_APPLICATION).append(",");

        sql.append(tableConjoint).append(".").append(SFConjoint.FIELD_IDCONJOINTS).append(",");
        sql.append(tableConjoint).append(".").append(SFConjoint.FIELD_IDCONJOINT1).append(",");
        sql.append(tableConjoint).append(".").append(SFConjoint.FIELD_IDCONJOINT2).append(",");

        sql.append(tableRelationConjugale).append(".").append(SFRelationConjoint.FIELD_IDRELATIONCONJOINT).append(",");
        sql.append(tableRelationConjugale).append(".").append(SFRelationConjoint.FIELD_DATEDEBUT).append(",");
        sql.append(tableRelationConjugale).append(".").append(SFRelationConjoint.FIELD_DATEFIN).append(",");
        sql.append(tableRelationConjugale).append(".").append(SFRelationConjoint.FIELD_TYPERELATION).append(",");

        sql.append(tableInformationComptabilite).append(".")
                .append(REInformationsComptabilite.FIELDNAME_ID_INFO_COMPTA);
        return sql.toString();
    }

    @Override
    protected String _getFrom(BStatement statement) {

        StringBuilder sql = new StringBuilder();

        String tablePrestationAccordee = _getCollection() + REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES;
        String tableRenteAccordee = _getCollection() + RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE;
        String tableBaseCalcul = _getCollection() + REBasesCalcul.TABLE_NAME_BASES_CALCUL;
        String tableTiers = _getCollection() + ITITiersDefTable.TABLE_NAME;
        String tablePersonne = _getCollection() + ITIPersonneDefTable.TABLE_NAME;
        String tablePersonneAvs = _getCollection() + ITIPersonneAvsDefTable.TABLE_NAME;
        String tableMembreFamille = _getCollection() + SFMembreFamille.TABLE_NAME;
        String tableConjoint = _getCollection() + SFConjoint.TABLE_NAME;
        String tableRelationConjugale = _getCollection() + SFRelationConjoint.TABLE_NAME;
        String tableInformationComptabilite = _getCollection() + REInformationsComptabilite.TABLE_NAME_INFO_COMPTA;

        sql.append(tablePrestationAccordee);
        sql.append(" LEFT OUTER JOIN ").append(tableInformationComptabilite);
        sql.append(" ON ").append(tablePrestationAccordee).append(".")
                .append(REPrestationsAccordees.FIELDNAME_ID_INFO_COMPTA).append("=")
                .append(tableInformationComptabilite).append(".")
                .append(REInformationsComptabilite.FIELDNAME_ID_INFO_COMPTA);

        sql.append(" LEFT OUTER JOIN ").append(tableRenteAccordee);
        sql.append(" ON ").append(tableRenteAccordee).append(".").append(RERenteAccordee.FIELDNAME_ID_RENTE_ACCORDEE)
                .append("=").append(tablePrestationAccordee).append(".")
                .append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE);

        sql.append(" INNER JOIN ").append(tableTiers);
        sql.append(" ON ").append(tablePrestationAccordee).append(".")
                .append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE).append("=").append(tableTiers)
                .append(".").append(ITITiersDefTable.ID_TIERS);

        sql.append(" INNER JOIN ").append(tablePersonne);
        sql.append(" ON ").append(tableTiers).append(".").append(ITITiersDefTable.ID_TIERS).append("=")
                .append(tablePersonne).append(".").append(ITIPersonneDefTable.ID_TIERS);

        sql.append(" INNER JOIN ").append(tablePersonneAvs);
        sql.append(" ON ").append(tablePersonne).append(".").append(ITIPersonneDefTable.ID_TIERS).append("=")
                .append(tablePersonneAvs).append(".").append(ITIPersonneAvsDefTable.ID_TIERS);

        sql.append(" LEFT OUTER JOIN ").append(tableBaseCalcul);
        sql.append(" ON ").append(tableRenteAccordee).append(".").append(RERenteAccordee.FIELDNAME_ID_BASE_CALCUL)
                .append("=").append(tableBaseCalcul).append(".").append(REBasesCalcul.FIELDNAME_ID_BASES_DE_CALCUL);

        sql.append(" INNER JOIN ").append(tableMembreFamille);
        sql.append(" ON ").append(tablePrestationAccordee).append(".")
                .append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE).append("=").append(tableMembreFamille)
                .append(".").append(SFMembreFamille.FIELD_IDTIERS);

        sql.append(" LEFT OUTER JOIN ").append(tableConjoint);
        sql.append(" ON (");
        sql.append(tableMembreFamille).append(".").append(SFMembreFamille.FIELD_IDMEMBREFAMILLE).append("=")
                .append(tableConjoint).append(".").append(SFConjoint.FIELD_IDCONJOINT1);
        sql.append(" OR ");
        sql.append(tableMembreFamille).append(".").append(SFMembreFamille.FIELD_IDMEMBREFAMILLE).append("=")
                .append(tableConjoint).append(".").append(SFConjoint.FIELD_IDCONJOINT2);
        sql.append(")");

        sql.append(" LEFT OUTER JOIN ").append(tableRelationConjugale);
        sql.append(" ON ").append(tableConjoint).append(".").append(SFConjoint.FIELD_IDCONJOINTS).append("=")
                .append(tableRelationConjugale).append(".").append(SFRelationConjoint.FIELD_IDCONJOINTS);

        return sql.toString();
    }

    @Override
    protected String _getTableName() {
        return null; // PAS DE TABLES !!!
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {

        codeNationalite = statement.dbReadNumeric(ITITiersDefTable.ID_PAYS);
        codePrestation = statement.dbReadString(REPrestationsAccordees.FIELDNAME_CODE_PRESTATION);
        cs1 = statement.dbReadString(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_1);
        cs2 = statement.dbReadString(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_2);
        cs3 = statement.dbReadString(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_3);
        cs4 = statement.dbReadString(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_4);
        cs5 = statement.dbReadString(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_5);
        csEtatPrestationAccordee = statement.dbReadNumeric(REPrestationsAccordees.FIELDNAME_CS_ETAT);
        csDomaine = statement.dbReadNumeric(SFMembreFamille.FIELD_DOMAINE_APPLICATION);
        csSexe = statement.dbReadNumeric(ITIPersonneDefTable.CS_SEXE);
        csTypeRelation = statement.dbReadNumeric(SFRelationConjoint.FIELD_TYPERELATION);
        dateDebutRelation = statement.dbReadNumeric(SFRelationConjoint.FIELD_DATEDEBUT);
        dateDeces = statement.dbReadNumeric(ITIPersonneDefTable.DATE_DECES);
        dateFinRelation = statement.dbReadNumeric(SFRelationConjoint.FIELD_DATEFIN);
        dateNaissance = statement.dbReadNumeric(ITIPersonneDefTable.DATE_NAISSANCE);
        degreInvalidite = statement.dbReadNumeric(REBasesCalcul.FIELDNAME_DEGRE_INVALIDITE);
        fractionRente = statement.dbReadString(REPrestationsAccordees.FIELDNAME_FRACTION_RENTE);
        idConjoint = statement.dbReadNumeric(SFConjoint.FIELD_IDCONJOINTS);
        idMFBeneficiaire = statement.dbReadNumeric(SFMembreFamille.FIELD_IDMEMBREFAMILLE);
        idMFConjoint1 = statement.dbReadNumeric(SFConjoint.FIELD_IDCONJOINT1);
        idMFConjoint2 = statement.dbReadNumeric(SFConjoint.FIELD_IDCONJOINT2);
        idPrestationAccordee = statement.dbReadNumeric(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE);
        idTiers = statement.dbReadNumeric(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE);
        idTiersBaseCalcul = statement.dbReadNumeric(REBasesCalcul.FIELDNAME_ID_TIERS_BASE_CALCUL);
        isTiersInactif = statement.dbReadBoolean(ITITiersDefTable.IS_INACTIF);
        nom = statement.dbReadString(ITITiersDefTable.DESIGNATION_1);
        nss = statement.dbReadString(ITIPersonneAvsDefTable.NUMERO_AVS_ACTUEL);
        prenom = statement.dbReadString(ITITiersDefTable.DESIGNATION_2);
        idInformationComptable = statement.dbReadNumeric(REInformationsComptabilite.FIELDNAME_ID_INFO_COMPTA);

    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        _addError(statement.getTransaction(), "interdit d'ajouter");
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        // ON NE FAIT RIEN
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // ON NE FAIT RIEN
    }

    public String getCodeNationalite() {
        return codeNationalite;
    }

    public String getCodePrestation() {
        return codePrestation;
    }

    public String getCs1() {
        return cs1;
    }

    public String getCs2() {
        return cs2;
    }

    public String getCs3() {
        return cs3;
    }

    public String getCs4() {
        return cs4;
    }

    public String getCs5() {
        return cs5;
    }

    public String getCsDomaine() {
        return csDomaine;
    }

    public String getCsEtatPrestationAccordee() {
        return csEtatPrestationAccordee;
    }

    public String getCsSexe() {
        return csSexe;
    }

    public String getCsTypeRelation() {
        return csTypeRelation;
    }

    public String getDateDebutRelation() {
        return dateDebutRelation;
    }

    public String getDateDeces() {
        return dateDeces;
    }

    public String getDateFinRelation() {
        return dateFinRelation;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public String getDegreInvalidite() {
        return degreInvalidite;
    }

    public String getFractionRente() {
        return fractionRente;
    }

    public String getIdConjoint() {
        return idConjoint;
    }

    public String getIdMFBeneficiaire() {
        return idMFBeneficiaire;
    }

    public String getIdMFConjoint1() {
        return idMFConjoint1;
    }

    public String getIdMFConjoint2() {
        return idMFConjoint2;
    }

    public String getIdPrestationAccordee() {
        return idPrestationAccordee;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getIdTiersBaseCalcul() {
        return idTiersBaseCalcul;
    }

    public Boolean getIsTiersInactif() {
        return isTiersInactif;
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
}
