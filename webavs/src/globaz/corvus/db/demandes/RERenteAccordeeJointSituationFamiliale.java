package globaz.corvus.db.demandes;

import globaz.corvus.db.basescalcul.REBasesCalcul;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.db.rentesaccordees.RERenteCalculee;
import globaz.externe.IPRConstantesExternes;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.prestation.db.demandes.PRDemande;

public class RERenteAccordeeJointSituationFamiliale extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @param schema
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static final String createFromClause(String schema) {
        StringBuffer fromClauseBuffer = new StringBuffer();
        String innerJoin = " INNER JOIN ";
        String leftJoin = " LEFT JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REDemandeRente.TABLE_NAME_DEMANDE_RENTE);

        // jointure entre table des demandes de rentes et table des rentes
        // calculees
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RERenteCalculee.TABLE_NAME_RENTE_CALCULEE);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REDemandeRente.TABLE_NAME_DEMANDE_RENTE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REDemandeRente.FIELDNAME_ID_RENTE_CALCULEE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RERenteCalculee.TABLE_NAME_RENTE_CALCULEE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RERenteCalculee.FIELDNAME_ID_RENTE_CALCULEE);

        // jointure entre table des rentes calculees et table des bases de
        // calculs
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REBasesCalcul.TABLE_NAME_BASES_CALCUL);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RERenteCalculee.TABLE_NAME_RENTE_CALCULEE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RERenteCalculee.FIELDNAME_ID_RENTE_CALCULEE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REBasesCalcul.TABLE_NAME_BASES_CALCUL);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REBasesCalcul.FIELDNAME_ID_RENTE_CALCULEE);

        // jointure entre table des bases de calculs et table des prestations
        // accordées
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REBasesCalcul.TABLE_NAME_BASES_CALCUL);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REBasesCalcul.FIELDNAME_ID_BASES_DE_CALCUL);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE);

        // jointure entre tables des prestations accordées et les rentes
        // accordées
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RERenteAccordee.FIELDNAME_ID_RENTE_ACCORDEE);

        // jointure entre table des demandes et table des demandes de rentes
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
        fromClauseBuffer.append(REDemandeRente.TABLE_NAME_DEMANDE_RENTE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REDemandeRente.FIELDNAME_ID_DEMANDE_PRESTATION);

        // jointure entre table des demandes et table des tiers
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IPRConstantesExternes.TABLE_TIERS);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(PRDemande.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(PRDemande.FIELDNAME_IDTIERS);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IPRConstantesExternes.TABLE_TIERS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IPRConstantesExternes.FIELDNAME_TABLE_TIERS_ID_TIERS_TI);

        // jointure entre table des tiers et table des numeros AVS
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IPRConstantesExternes.TABLE_AVS);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(PRDemande.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(PRDemande.FIELDNAME_IDTIERS);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IPRConstantesExternes.TABLE_AVS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IPRConstantesExternes.FIELDNAME_TABLE_TIERS_ID_TIERS_TI);

        // jointure entre table des tiers et table des personnes
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REDemandeRenteJointDemande.TABLE_PERSONNE);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(PRDemande.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(PRDemande.FIELDNAME_IDTIERS);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IPRConstantesExternes.TABLE_PERSONNE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IPRConstantesExternes.FIELDNAME_TABLE_TIERS_ID_TIERS_TI);

        // jointure entre tables des tiers et la situation familiale
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RESituationFamiliale.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RESituationFamiliale.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RESituationFamiliale.FIELD_IDTIERS);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RESituationFamiliale.TABLE_TIERS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RESituationFamiliale.FIELD_TI_IDTIERS);

        // jointure entre la situation familiale et la table des périodes
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IPRConstantesExternes.TABLE_PERIOD);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IPRConstantesExternes.TABLE_PERIOD);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IPRConstantesExternes.TABLE_PERIOD_FIELD_IDMEMBREFAMILLE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RESituationFamiliale.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RESituationFamiliale.FIELD_IDMEMBREFAMILLE);

        // LEFT JOIN WEBAVS.SFPERIOD ON WEBAVS.SFPERIOD.WHIDMF =
        // WEBAVS.SFMBRFAM.WGIMEF

        /*
         * fromClauseBuffer.append(PRDemande.TABLE_NAME);
         * 
         * // jointure entre table des demandes et table des tiers fromClauseBuffer.append(leftJoin);
         * fromClauseBuffer.append(schema); fromClauseBuffer.append(IPRConstantesExternes.TABLE_TIERS);
         * fromClauseBuffer.append(on); fromClauseBuffer.append(schema); fromClauseBuffer.append(PRDemande.TABLE_NAME);
         * fromClauseBuffer.append(point); fromClauseBuffer.append(PRDemande.FIELDNAME_IDTIERS);
         * fromClauseBuffer.append(egal); fromClauseBuffer.append(schema);
         * fromClauseBuffer.append(IPRConstantesExternes.TABLE_TIERS); fromClauseBuffer.append(point);
         * fromClauseBuffer.append(IPRConstantesExternes .FIELDNAME_TABLE_TIERS_ID_TIERS_TI);
         * 
         * // jointure entre table des tiers et table des numeros AVS fromClauseBuffer.append(leftJoin);
         * fromClauseBuffer.append(schema); fromClauseBuffer.append(IPRConstantesExternes.TABLE_AVS);
         * fromClauseBuffer.append(on); fromClauseBuffer.append(schema); fromClauseBuffer.append(PRDemande.TABLE_NAME);
         * fromClauseBuffer.append(point); fromClauseBuffer.append(PRDemande.FIELDNAME_IDTIERS);
         * fromClauseBuffer.append(egal); fromClauseBuffer.append(schema);
         * fromClauseBuffer.append(IPRConstantesExternes.TABLE_AVS); fromClauseBuffer.append(point);
         * fromClauseBuffer.append(IPRConstantesExternes .FIELDNAME_TABLE_TIERS_ID_TIERS_TI);
         * 
         * // jointure entre table des tiers et table des personnes fromClauseBuffer.append(leftJoin);
         * fromClauseBuffer.append(schema); fromClauseBuffer.append(REDemandeRenteJointDemande.TABLE_PERSONNE);
         * fromClauseBuffer.append(on); fromClauseBuffer.append(schema); fromClauseBuffer.append(PRDemande.TABLE_NAME);
         * fromClauseBuffer.append(point); fromClauseBuffer.append(PRDemande.FIELDNAME_IDTIERS);
         * fromClauseBuffer.append(egal); fromClauseBuffer.append(schema);
         * fromClauseBuffer.append(IPRConstantesExternes.TABLE_PERSONNE); fromClauseBuffer.append(point);
         * fromClauseBuffer.append(IPRConstantesExternes .FIELDNAME_TABLE_TIERS_ID_TIERS_TI);
         * 
         * // jointure entre table des bases de calculs et table des prestations accordées
         * fromClauseBuffer.append(innerJoin); fromClauseBuffer.append(schema);
         * fromClauseBuffer.append(REPrestationsAccordees .TABLE_NAME_PRESTATIONS_ACCORDEES);
         * fromClauseBuffer.append(on); fromClauseBuffer.append(schema);
         * fromClauseBuffer.append(REBasesCalcul.TABLE_NAME_BASES_CALCUL); fromClauseBuffer.append(point);
         * fromClauseBuffer.append(REBasesCalcul.FIELDNAME_ID_BASES_DE_CALCUL); fromClauseBuffer.append(egal);
         * fromClauseBuffer.append(schema); fromClauseBuffer
         * .append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES); fromClauseBuffer.append(point);
         * fromClauseBuffer.append(REPrestationsAccordees .FIELDNAME_ID_BASES_CALCUL);
         * 
         * // jointure entre tables des prestations accordées et les rentes accordées
         * fromClauseBuffer.append(innerJoin); fromClauseBuffer.append(schema);
         * fromClauseBuffer.append(RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE); fromClauseBuffer.append(on);
         * fromClauseBuffer.append(schema); fromClauseBuffer
         * .append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES); fromClauseBuffer.append(point);
         * fromClauseBuffer.append(REPrestationsAccordees .FIELDNAME_ID_PRESTATION_ACCORDEE);
         * fromClauseBuffer.append(egal); fromClauseBuffer.append(schema);
         * fromClauseBuffer.append(RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE); fromClauseBuffer.append(point);
         * fromClauseBuffer.append(RERenteAccordee.FIELDNAME_ID_RENTE_ACCORDEE);
         * 
         * // jointure entre tables des tiers et la situation familiale fromClauseBuffer.append(leftJoin);
         * fromClauseBuffer.append(schema); fromClauseBuffer.append(RESituationFamiliale.TABLE_TIERS);
         * fromClauseBuffer.append(on); fromClauseBuffer.append(schema);
         * fromClauseBuffer.append(RESituationFamiliale.TABLE_NAME); fromClauseBuffer.append(point);
         * fromClauseBuffer.append(RESituationFamiliale.FIELD_IDTIERS); fromClauseBuffer.append(egal);
         * fromClauseBuffer.append(schema); fromClauseBuffer.append(RESituationFamiliale.TABLE_TIERS);
         * fromClauseBuffer.append(point); fromClauseBuffer.append(RESituationFamiliale.FIELD_TI_IDTIERS);
         * 
         * // jointure entre la situation familiale et la table des périodes fromClauseBuffer.append(leftJoin);
         * fromClauseBuffer.append(schema); fromClauseBuffer.append(IPRConstantesExternes.TABLE_PERIOD);
         * fromClauseBuffer.append(on); fromClauseBuffer.append(schema);
         * fromClauseBuffer.append(IPRConstantesExternes.TABLE_PERIOD); fromClauseBuffer.append(point);
         * fromClauseBuffer.append(IPRConstantesExternes .TABLE_PERIOD_FIELD_IDMEMBREFAMILLE);
         * fromClauseBuffer.append(egal); fromClauseBuffer.append(schema);
         * fromClauseBuffer.append(RESituationFamiliale.FIELD_IDTIERS); fromClauseBuffer.append(point);
         * fromClauseBuffer.append(RESituationFamiliale.FIELD_IDMEMBREFAMILLE);
         */

        return fromClauseBuffer.toString();
    }

    private String dateDebutEtude = "";
    private String dateDeces = "";
    private String dateFinEtude = "";
    private String dateNaissance = "";
    private String genreDePrestation = "";
    private String motif = "";
    private String nom = "";
    private String nss = "";
    private String prenom = "";

    private String sexe = "";

    @Override
    protected String _getTableName() {
        // Auto-generated method stub
        return null;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {

        nss = statement.dbReadString(IPRConstantesExternes.FIELDNAME_TABLE_AVS_NUM_AVS);
        nom = statement.dbReadString(IPRConstantesExternes.FIELDNAME_TABLE_TIERS_NOM);
        prenom = statement.dbReadString(IPRConstantesExternes.FIELDNAME_TABLE_TIERS_PRENOM);
        dateNaissance = statement.dbReadDateAMJ(IPRConstantesExternes.FIELDNAME_TABLE_PERSONNE_DATENAISSANCE);
        sexe = statement.dbReadString(IPRConstantesExternes.FIELDNAME_TABLE_PERSONNE_SEXE);
        genreDePrestation = statement.dbReadString(REPrestationsAccordees.FIELDNAME_GENRE_PRESTATION_ACCORDEE);
        // motif = statement.dbReadString(RERenteAccordee.FIELDNAME_);
        dateDebutEtude = statement.dbReadDateAMJ(IPRConstantesExternes.FIELDNAME_TABLE_PERIODE_DATEDEBUT);
        dateFinEtude = statement.dbReadDateAMJ(IPRConstantesExternes.FIELDNAME_TABLE_PERIODE_DATEFIN);
        dateDeces = statement.dbReadDateAMJ(RESituationFamiliale.FIELD_DATEDECES);

    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // Auto-generated method stub

    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        // Auto-generated method stub
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // Auto-generated method stub

    }

    public String getDateDebutEtude() {
        return dateDebutEtude;
    }

    public String getDateDeces() {
        return dateDeces;
    }

    public String getDateFinEtude() {
        return dateFinEtude;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public String getGenreDePrestation() {
        return genreDePrestation;
    }

    public String getMotif() {
        return motif;
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

    public String getSexe() {
        return sexe;
    }

    public void setDateDebutEtude(String dateDebutEtude) {
        this.dateDebutEtude = dateDebutEtude;
    }

    public void setDateDeces(String dateDeces) {
        this.dateDeces = dateDeces;
    }

    public void setDateFinEtude(String dateFinEtude) {
        this.dateFinEtude = dateFinEtude;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setGenreDePrestation(String genreDePrestation) {
        this.genreDePrestation = genreDePrestation;
    }

    public void setMotif(String motif) {
        this.motif = motif;
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

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

}