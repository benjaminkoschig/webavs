package globaz.corvus.db.demandes;

import globaz.corvus.db.basescalcul.REBasesCalcul;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.db.rentesaccordees.RERenteCalculee;
import globaz.corvus.vb.demandes.REDemandeRenteJointDemandeListViewBean;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.tools.PRDateFormater;

/**
 * Entité encapsulant les données des demandes de rentes, et des prestation accordés liées à cette demande <br/>
 * Il y aura un objet de ce type par prestation accordée
 * 
 * @author PBA
 * 
 */
public class REDemandeRenteJointPrestationAccordee extends REDemandeRenteJointDemande {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Génère la partie "FROM" de la requête SQL pour récupérer cette entité de la base de données
     * 
     * @param schema
     *            le schéma de base de données à utiliser
     * @return le bout de requête
     */
    public static String createFromClause(String schema) {
        StringBuffer sql = new StringBuffer();

        sql.append(REDemandeRenteJointDemande.createFromClause(schema));

        // Jointure pour récupérer les codes prestations des rentes accordées
        // on part depuis l'id d'une demande de rente, et il faut arriver sur les prestations accordées

        // jointure sur les rentes calculées
        sql.append(" LEFT JOIN ");
        sql.append(schema).append(RERenteCalculee.TABLE_NAME_RENTE_CALCULEE);
        sql.append(" ON ");
        sql.append(schema).append(REDemandeRente.TABLE_NAME_DEMANDE_RENTE).append(".")
                .append(REDemandeRente.FIELDNAME_ID_RENTE_CALCULEE);
        sql.append(" = ");
        sql.append(schema).append(RERenteCalculee.TABLE_NAME_RENTE_CALCULEE).append(".")
                .append(RERenteCalculee.FIELDNAME_ID_RENTE_CALCULEE);

        // jointure sur les bases de calculs
        sql.append(" LEFT JOIN ");
        sql.append(schema).append(REBasesCalcul.TABLE_NAME_BASES_CALCUL);
        sql.append(" ON ");
        sql.append(schema).append(RERenteCalculee.TABLE_NAME_RENTE_CALCULEE).append(".")
                .append(RERenteCalculee.FIELDNAME_ID_RENTE_CALCULEE);
        sql.append(" = ");
        sql.append(schema).append(REBasesCalcul.TABLE_NAME_BASES_CALCUL).append(".")
                .append(REBasesCalcul.FIELDNAME_ID_RENTE_CALCULEE);

        // jointure sur les rentes accordées
        sql.append(" LEFT JOIN ");
        sql.append(schema).append(RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE);
        sql.append(" ON ");
        sql.append(schema).append(REBasesCalcul.TABLE_NAME_BASES_CALCUL).append(".")
                .append(REBasesCalcul.FIELDNAME_ID_BASES_DE_CALCUL);
        sql.append(" = ");
        sql.append(schema).append(RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE).append(".")
                .append(RERenteAccordee.FIELDNAME_ID_BASE_CALCUL);

        // arrivé sur les prestations accordées
        sql.append(" LEFT JOIN ");
        sql.append(schema).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES);
        sql.append(" ON ");
        sql.append(schema).append(RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE).append(".")
                .append(RERenteAccordee.FIELDNAME_ID_RENTE_ACCORDEE);
        sql.append(" = ");
        sql.append(schema).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(".")
                .append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE);

        return sql.toString();
    }

    private String codeCasSpecial1 = "";
    private String codeCasSpecial2 = "";
    private String codeCasSpecial3 = "";
    private String codeCasSpecial4 = "";
    private String codeCasSpecial5 = "";
    private String codePrestation = "";
    private String csEtatPrestationAccordee = "";
    private String csTypeDemandeRente = "";
    private String dateDebutDroit = "";
    private String dateEcheanceRenteAccordee = "";
    private String dateFinDroit = "";
    private transient String fromClause = null;
    private String genrePrestation;
    private String idRenteAccordee = "";
    private String idTiersBeneficiairePrestationAccordee;
    private String montantPrestationAccordee;
    private String nbPostit = "";

    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            fromClause = REDemandeRenteJointPrestationAccordee.createFromClause(_getCollection());
        }
        return fromClause;
    }

    @Override
    protected String _getTableName() {
        return _getCollection() + PRDemande.TABLE_NAME;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);
        csTypeDemandeRente = statement.dbReadNumeric(REDemandeRente.FIELDNAME_CS_TYPE_DEMANDE_RENTE);
        codePrestation = statement.dbReadNumeric(REPrestationsAccordees.FIELDNAME_CODE_PRESTATION);
        csEtatPrestationAccordee = statement.dbReadNumeric(REPrestationsAccordees.FIELDNAME_CS_ETAT);
        dateDebutDroit = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                .dbReadNumeric(REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT));
        dateEcheanceRenteAccordee = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                .dbReadNumeric(REPrestationsAccordees.FIELDNAME_DATE_ECHEANCE));
        dateFinDroit = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                .dbReadNumeric(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT));
        genrePrestation = statement.dbReadNumeric(REPrestationsAccordees.FIELDNAME_GENRE_PRESTATION_ACCORDEE);
        idRenteAccordee = statement.dbReadNumeric(RERenteAccordee.FIELDNAME_ID_RENTE_ACCORDEE);
        idTiersBeneficiairePrestationAccordee = statement
                .dbReadNumeric(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE);
        montantPrestationAccordee = statement.dbReadNumeric(REPrestationsAccordees.FIELDNAME_MONTANT_PRESTATION);
        nbPostit = statement.dbReadNumeric(REDemandeRenteJointDemandeListViewBean.FIELDNAME_COUNT_POSTIT);

        codeCasSpecial1 = statement.dbReadString(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_1);
        codeCasSpecial2 = statement.dbReadString(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_2);
        codeCasSpecial3 = statement.dbReadString(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_3);
        codeCasSpecial4 = statement.dbReadString(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_4);
        codeCasSpecial5 = statement.dbReadString(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_5);
    }

    public String getCodeCasSpecial1() {
        return codeCasSpecial1;
    }

    public String getCodeCasSpecial2() {
        return codeCasSpecial2;
    }

    public String getCodeCasSpecial3() {
        return codeCasSpecial3;
    }

    public String getCodeCasSpecial4() {
        return codeCasSpecial4;
    }

    public String getCodeCasSpecial5() {
        return codeCasSpecial5;
    }

    public String getCodePrestation() {
        return codePrestation;
    }

    public String getCsEtatPrestationAccordee() {
        return csEtatPrestationAccordee;
    }

    public String getCsTypeDemandeRente() {
        return csTypeDemandeRente;
    }

    public String getDateDebutDroit() {
        return dateDebutDroit;
    }

    public String getDateEcheanceRenteAccordee() {
        return dateEcheanceRenteAccordee;
    }

    public String getDateFinDroit() {
        return dateFinDroit;
    }

    public String getGenrePrestation() {
        return genrePrestation;
    }

    public String getIdRenteAccordee() {
        return idRenteAccordee;
    }

    public String getIdTiersBeneficiairePrestationAccordee() {
        return idTiersBeneficiairePrestationAccordee;
    }

    public String getMontantPrestationAccordee() {
        return montantPrestationAccordee;
    }

    public boolean hasPostIt() {
        return JadeStringUtil.isBlank(nbPostit) ? false : Integer.parseInt(nbPostit) > 0;
    }

    public void setPostIt(String nbPostit) {
        this.nbPostit = nbPostit;
    }

    public void setCodePrestation(String codePrestation) {
        this.codePrestation = codePrestation;
    }

    public void setCsTypeDemandeRente(String csTypeDemandeRente) {
        this.csTypeDemandeRente = csTypeDemandeRente;
    }

    public void setDateDebutDroit(String dateDebutDroit) {
        this.dateDebutDroit = dateDebutDroit;
    }

    public void setDateEcheanceRenteAccordee(String dateEcheanceRenteAccordee) {
        this.dateEcheanceRenteAccordee = dateEcheanceRenteAccordee;
    }

    public void setDateFinDroit(String dateFinDroit) {
        this.dateFinDroit = dateFinDroit;
    }

    public void setGenrePrestation(String genrePrestation) {
        this.genrePrestation = genrePrestation;
    }

    public void setIdRenteAccordee(String idRenteAccordee) {
        this.idRenteAccordee = idRenteAccordee;
    }

    public void setIdTiersBeneficiairePrestationAccordee(String idTiersBeneficiairePrestationAccordee) {
        this.idTiersBeneficiairePrestationAccordee = idTiersBeneficiairePrestationAccordee;
    }

    public void setMontantPrestationAccordee(String montantPrestationAccordee) {
        this.montantPrestationAccordee = montantPrestationAccordee;
    }
}
