package globaz.corvus.db.ci;

import globaz.corvus.db.annonces.REAnnonceHeader;
import globaz.corvus.db.annonces.REAnnonceInscriptionCI;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.pyxis.db.tiers.ITIPersonneAvsDefTable;
import globaz.pyxis.db.tiers.ITIPersonneDefTable;
import globaz.pyxis.db.tiers.ITITiersDefTable;

/**
 * N'est utilisé que pour la génération de la liste des CI additionnels
 * 
 * @author BSC
 */
public class REListeCiAdditionnels extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    static String generateFields(String schema, boolean withCaseThen) {
        String tableTiersAvs = schema + ITIPersonneAvsDefTable.TABLE_NAME;
        String tableTiers = schema + ITITiersDefTable.TABLE_NAME;
        String tableRassemblementCI = schema + RERassemblementCI.TABLE_NAME_RCI;
        String tableAnnonceInscriptionCI = schema + REAnnonceInscriptionCI.TABLE_NAME_ANNONCE_INSCRIPTION_CI;

        StringBuilder sql = new StringBuilder();

        sql.append(tableTiersAvs).append(".").append(ITIPersonneAvsDefTable.NUMERO_AVS_ACTUEL).append(",");
        sql.append(tableTiers).append(".").append(ITITiersDefTable.DESIGNATION_1).append(",");
        sql.append(tableTiers).append(".").append(ITITiersDefTable.DESIGNATION_2).append(",");
        sql.append(tableTiers).append(".").append(ITITiersDefTable.ID_TIERS).append(",");

        sql.append(tableRassemblementCI).append(".").append(RERassemblementCI.FIELDNAME_DATE_TRAITEMENT).append(",");
        sql.append(tableRassemblementCI).append(".").append(RERassemblementCI.FIELDNAME_DATE_RASSEMBLEMENT).append(",");
        sql.append(tableAnnonceInscriptionCI).append(".")
                .append(REAnnonceInscriptionCI.FIELDNAME_ATTENTE_CI_ADDITIONNEL).append(",");
        sql.append(tableAnnonceInscriptionCI).append(".")
                .append(REAnnonceInscriptionCI.FIELDNAME_MOIS_DEBUT_COTISATION).append(",");
        sql.append(tableAnnonceInscriptionCI).append(".").append(REAnnonceInscriptionCI.FIELDNAME_MOIS_FIN_COTISATION)
                .append(",");
        sql.append(tableAnnonceInscriptionCI).append(".").append(REAnnonceInscriptionCI.FIELDNAME_ANNEE_COTISATION)
                .append(",");
        sql.append(tableAnnonceInscriptionCI).append(".").append("CSPY").append(",");
        sql.append(tableAnnonceInscriptionCI).append(".").append("PSPY");

        if (withCaseThen) {
            sql.append(",");

            sql.append("SUM(");
            sql.append("CASE ").append(tableAnnonceInscriptionCI).append(".")
                    .append(REAnnonceInscriptionCI.FIELDNAME_CODE_EXTOURNE);
            sql.append(" WHEN '' THEN ").append(tableAnnonceInscriptionCI).append(".")
                    .append(REAnnonceInscriptionCI.FIELDNAME_REVENU);
            sql.append(" WHEN '0' THEN ").append(tableAnnonceInscriptionCI).append(".")
                    .append(REAnnonceInscriptionCI.FIELDNAME_REVENU);
            sql.append(" ELSE ").append(tableAnnonceInscriptionCI).append(".")
                    .append(REAnnonceInscriptionCI.FIELDNAME_REVENU).append(" * -1 ");
            sql.append(" END ");
            sql.append(") AS ").append(REListeCiAdditionnelsManager.ALIAS_MONTANT_TOTAL);
        }

        return sql.toString();
    }

    private String anneeDeCotisation;
    private String attenteCiAdditionnel;
    private String cSpy;
    private String dateReception = "";
    private String dateTraitement = "";
    private String idTiers;
    private String moisDebutCotisation;
    private String moisFinCotisation;
    private String montantTotal = "";
    private String nom = "";
    private String nss = "";
    private String prenom = "";
    private String pSpy;

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
     * @return faux
     */
    @Override
    protected boolean _autoInherits() {
        return false;
    }

    @Override
    protected String _getFields(BStatement statement) {
        return REListeCiAdditionnels.generateFields(_getCollection(), true);
    }

    @Override
    protected String _getFrom(BStatement statement) {
        StringBuilder sql = new StringBuilder();
        String innerJoin = " INNER JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";
        String schema = _getCollection();

        sql.append(schema).append(RECompteIndividuel.TABLE_NAME_CI);

        // jointure entre table des comptes individuel et table des numeros AVS
        sql.append(innerJoin);
        sql.append(schema).append(ITIPersonneAvsDefTable.TABLE_NAME);
        sql.append(on);
        sql.append(schema).append(RECompteIndividuel.TABLE_NAME_CI).append(point)
                .append(RECompteIndividuel.FIELDNAME_ID_TIERS);
        sql.append(egal);
        sql.append(schema).append(ITIPersonneAvsDefTable.TABLE_NAME).append(point)
                .append(ITIPersonneAvsDefTable.ID_TIERS);

        // jointure entre table des numeros AVS et table des personne
        sql.append(innerJoin);
        sql.append(schema).append(ITIPersonneDefTable.TABLE_NAME);
        sql.append(on);
        sql.append(schema).append(ITIPersonneAvsDefTable.TABLE_NAME).append(point)
                .append(ITIPersonneAvsDefTable.ID_TIERS);
        sql.append(egal);
        sql.append(schema).append(ITIPersonneDefTable.TABLE_NAME).append(point).append(ITIPersonneDefTable.ID_TIERS);

        // jointure entre table des personne et table des tiers
        sql.append(innerJoin);
        sql.append(schema).append(ITITiersDefTable.TABLE_NAME);
        sql.append(on);
        sql.append(schema).append(ITIPersonneDefTable.TABLE_NAME).append(point).append(ITIPersonneDefTable.ID_TIERS);
        sql.append(egal);
        sql.append(schema).append(ITITiersDefTable.TABLE_NAME).append(point).append(ITITiersDefTable.ID_TIERS);

        // jointure entre table des ci et table des rassemblements de ci
        sql.append(innerJoin);
        sql.append(schema).append(RERassemblementCI.TABLE_NAME_RCI);
        sql.append(on);
        sql.append(schema).append(RECompteIndividuel.TABLE_NAME_CI).append(point)
                .append(RECompteIndividuel.FIELDNAME_ID_CI);
        sql.append(egal);
        sql.append(schema).append(RERassemblementCI.TABLE_NAME_RCI).append(point)
                .append(RERassemblementCI.FIELDNAME_ID_CI);

        // jointure entre table des rassemblements de ci et des inscriptions ci
        sql.append(innerJoin);
        sql.append(schema).append(REInscriptionCI.TABLE_NAME_INS_CI);
        sql.append(on);
        sql.append(schema).append(RERassemblementCI.TABLE_NAME_RCI).append(point)
                .append(RERassemblementCI.FIELDNAME_ID_RCI);
        sql.append(egal);
        sql.append(schema).append(REInscriptionCI.TABLE_NAME_INS_CI).append(point)
                .append(REInscriptionCI.FIELDNAME_ID_RCI);

        // jointure entre table des inscriptions ci et des headers d'annonce
        sql.append(innerJoin);
        sql.append(schema).append(REAnnonceHeader.TABLE_NAME_ANNONCE_HEADER);
        sql.append(on);
        sql.append(schema).append(REInscriptionCI.TABLE_NAME_INS_CI).append(point)
                .append(REInscriptionCI.FIELDNAME_ID_ARC);
        sql.append(egal);
        sql.append(schema).append(REAnnonceHeader.TABLE_NAME_ANNONCE_HEADER).append(point)
                .append(REAnnonceHeader.FIELDNAME_ID_ANNONCE);

        // jointure entre table des headers d'annonce et des annonces CI
        sql.append(innerJoin);
        sql.append(schema).append(REAnnonceInscriptionCI.TABLE_NAME_ANNONCE_INSCRIPTION_CI);
        sql.append(on);
        sql.append(schema).append(REAnnonceHeader.TABLE_NAME_ANNONCE_HEADER).append(point)
                .append(REAnnonceHeader.FIELDNAME_ID_ANNONCE);
        sql.append(egal);
        sql.append(schema).append(REAnnonceInscriptionCI.TABLE_NAME_ANNONCE_INSCRIPTION_CI).append(point)
                .append(REAnnonceInscriptionCI.FIELDNAME_ID_ANNONCE_INSCRIPTION_CI);

        return sql.toString();
    }

    @Override
    protected String _getTableName() {
        return RECompteIndividuel.TABLE_NAME_CI;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idTiers = statement.dbReadNumeric(ITITiersDefTable.ID_TIERS);
        nom = statement.dbReadString(ITITiersDefTable.DESIGNATION_1);
        prenom = statement.dbReadString(ITITiersDefTable.DESIGNATION_2);
        nss = statement.dbReadString(ITIPersonneAvsDefTable.NUMERO_AVS_ACTUEL);
        dateTraitement = statement.dbReadDateAMJ(RERassemblementCI.FIELDNAME_DATE_TRAITEMENT);
        dateReception = statement.dbReadDateAMJ(RERassemblementCI.FIELDNAME_DATE_RASSEMBLEMENT);
        montantTotal = statement.dbReadNumeric(REListeCiAdditionnelsManager.ALIAS_MONTANT_TOTAL);
        attenteCiAdditionnel = statement.dbReadString(REAnnonceInscriptionCI.FIELDNAME_ATTENTE_CI_ADDITIONNEL);
        moisDebutCotisation = statement.dbReadString(REAnnonceInscriptionCI.FIELDNAME_MOIS_DEBUT_COTISATION);
        moisFinCotisation = statement.dbReadString(REAnnonceInscriptionCI.FIELDNAME_MOIS_FIN_COTISATION);
        anneeDeCotisation = statement.dbReadString(REAnnonceInscriptionCI.FIELDNAME_ANNEE_COTISATION);
        cSpy = statement.dbReadString("CSPY");
        pSpy = statement.dbReadString("PSPY");
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // rien du tout... c'est en lecture seule.
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        // rien du tout... c'est en lecture seule.
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
    }

    public final String getAnneeDeCotisation() {
        return anneeDeCotisation;
    }

    public final String getAttenteCiAdditionnel() {
        return attenteCiAdditionnel;
    }

    public final String getcSpy() {
        return cSpy;
    }

    public String getDateReception() {
        return dateReception;
    }

    public String getDateTraitement() {
        return dateTraitement;
    }

    public final String getIdTiers() {
        return idTiers;
    }

    public final String getMoisDebutCotisation() {
        return moisDebutCotisation;
    }

    public final String getMoisFinCotisation() {
        return moisFinCotisation;
    }

    public String getMontantTotal() {
        return montantTotal;
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

    public final String getpSpy() {
        return pSpy;
    }

    public final void setAnneeDeCotisation(String anneeDeCotisation) {
        this.anneeDeCotisation = anneeDeCotisation;
    }

    public final void setAttenteCiAdditionnel(String attenteCiAdditionnel) {
        this.attenteCiAdditionnel = attenteCiAdditionnel;
    }

    public final void setcSpy(String cSpy) {
        this.cSpy = cSpy;
    }

    public void setDateReception(String dateReception) {
        this.dateReception = dateReception;
    }

    public void setDateTraitement(String dateTraitement) {
        this.dateTraitement = dateTraitement;
    }

    public final void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public final void setMoisDebutCotisation(String moisDebutCotisation) {
        this.moisDebutCotisation = moisDebutCotisation;
    }

    public final void setMoisFinCotisation(String moisFinCotisation) {
        this.moisFinCotisation = moisFinCotisation;
    }

    public void setMontantTotal(String montantTotal) {
        this.montantTotal = montantTotal;
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

    public final void setpSpy(String pSpy) {
        this.pSpy = pSpy;
    }

}
