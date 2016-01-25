package globaz.apg.db.prestation;

import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.db.lots.APLot;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.demandes.PRDemande;
import globaz.pyxis.db.tiers.ITIPersonneAvsDefTable;
import globaz.pyxis.db.tiers.ITIPersonneDefTable;
import globaz.pyxis.db.tiers.ITITiersDefTable;

/**
 * @author DVH
 */
public class APPrestationJointLotTiersDroit extends APPrestation {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_DATE_NAISSANCE = "HPDNAI";
    public static final String FIELDNAME_ID_TIERS_TI = "HTITIE";
    public static final String FIELDNAME_NATIONALITE = "HNIPAY";
    public static final String FIELDNAME_NOM = "HTLDE1";
    public static final String FIELDNAME_NOM_FOR_SEARCH = "HTLDU1";
    public static final String FIELDNAME_NUM_AVS = "HXNAVS";
    public static final String FIELDNAME_PRENOM = "HTLDE2";
    public static final String FIELDNAME_PRENOM_FOR_SEARCH = "HTLDU2";
    public static final String FIELDNAME_SEXE = "HPTSEX";
    public static final String TABLE_AVS = "TIPAVSP";
    public static final String TABLE_TIERS = "TITIERP";
    public static final String TABLE_TIERS_DETAIL = "TIPERSP";

    private String csNationalite;
    private String csSexe;
    private String dateDebutDroit;
    private String dateNaissance;
    private String descriptionLot;
    private String genreService;
    private String idTiers;
    private String noAVS;
    private String noLot;
    private String nom;
    private String prenom;

    public APPrestationJointLotTiersDroit() {
        super();

        csNationalite = "";
        csSexe = "";
        dateDebutDroit = "";
        dateNaissance = "";
        descriptionLot = "";
        genreService = "";
        idTiers = "";
        noAVS = "";
        noLot = "";
        nom = "";
        prenom = "";
    }

    /**
     * redefinie a false car les updates et suppressions se feront sur la table de son parent
     */
    @Override
    protected boolean _autoInherits() {
        return false;
    }

    @Override
    protected void _beforeUpdate(BTransaction transaction) throws Exception {
        // recalculer le montant journalier
        double brut = JadeStringUtil.toDouble(getMontantBrut());
        double nbJours = JadeStringUtil.toDouble(getNombreJoursSoldes());

        setMontantJournalier(JANumberFormatter.formatNoQuote(brut / nbJours));

        super._beforeUpdate(transaction);
    }

    @Override
    protected String _getFrom(BStatement statement) {

        String tablePrestation = _getCollection() + APPrestation.TABLE_NAME;
        String tableLot = _getCollection() + APLot.TABLE_NAME;
        String tableDroitLAPG = _getCollection() + APDroitLAPG.TABLE_NAME_LAPG;
        String tableDemandePrestation = _getCollection() + PRDemande.TABLE_NAME;
        String tableTiers = _getCollection() + ITITiersDefTable.TABLE_NAME;
        String tablePersonne = _getCollection() + ITIPersonneDefTable.TABLE_NAME;
        String tablePersonneAvs = _getCollection() + ITIPersonneAvsDefTable.TABLE_NAME;

        StringBuilder sql = new StringBuilder();

        sql.append(tablePrestation);

        // jointure entre tables prestation et lot
        sql.append(" LEFT OUTER JOIN ");
        sql.append(tableLot);
        sql.append(" ON ");
        sql.append(tablePrestation).append(".").append(APPrestation.FIELDNAME_IDLOT);
        sql.append("=");
        sql.append(tableLot).append(".").append(APLot.FIELDNAME_IDLOT);

        // jointure entre tables prestation et droits
        sql.append(" INNER JOIN ");
        sql.append(tableDroitLAPG);
        sql.append(" ON ");
        sql.append(tablePrestation).append(".").append(APPrestation.FIELDNAME_IDDROIT);
        sql.append("=");
        sql.append(tableDroitLAPG).append(".").append(APDroitLAPG.FIELDNAME_IDDROIT_LAPG);

        // jointure entre tables droits et demande
        sql.append(" INNER JOIN ");
        sql.append(tableDemandePrestation);
        sql.append(" ON ");
        sql.append(tableDroitLAPG).append(".").append(APDroitLAPG.FIELDNAME_IDDEMANDE);
        sql.append("=");
        sql.append(tableDemandePrestation).append(".").append(PRDemande.FIELDNAME_IDDEMANDE);

        // jointure entre tables demandes et tiers
        sql.append(" INNER JOIN ");
        sql.append(tableTiers);
        sql.append(" ON ");
        sql.append(tableDemandePrestation).append(".").append(PRDemande.FIELDNAME_IDTIERS);
        sql.append("=");
        sql.append(tableTiers).append(".").append(ITITiersDefTable.ID_TIERS);

        // jointure entre table des tiers et table détail des tiers
        sql.append(" LEFT OUTER JOIN ");
        sql.append(tablePersonne);
        sql.append(" ON ");
        sql.append(tableTiers).append(".").append(ITITiersDefTable.ID_TIERS);
        sql.append("=");
        sql.append(tablePersonne).append(".").append(ITIPersonneDefTable.ID_TIERS);

        // jointure entre tables tiers et avs
        sql.append(" INNER JOIN ");
        sql.append(tablePersonneAvs);
        sql.append(" ON ");
        sql.append(tablePersonne).append(".").append(ITIPersonneDefTable.ID_TIERS);
        sql.append("=");
        sql.append(tablePersonneAvs).append(".").append(ITIPersonneAvsDefTable.ID_TIERS);

        return sql.toString();
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);

        csNationalite = statement.dbReadNumeric(ITITiersDefTable.ID_PAYS);
        csSexe = statement.dbReadNumeric(ITIPersonneDefTable.CS_SEXE);
        dateDebutDroit = statement.dbReadDateAMJ(APDroitLAPG.FIELDNAME_DATEDEBUTDROIT);
        dateNaissance = statement.dbReadDateAMJ(ITIPersonneDefTable.DATE_NAISSANCE);
        descriptionLot = statement.dbReadString(APLot.FIELDNAME_DESCRIPTION);
        genreService = statement.dbReadNumeric(APDroitLAPG.FIELDNAME_GENRESERVICE);
        idTiers = statement.dbReadNumeric(ITITiersDefTable.ID_TIERS);
        noAVS = statement.dbReadString(ITIPersonneAvsDefTable.NUMERO_AVS_ACTUEL);
        noLot = statement.dbReadNumeric(APLot.FIELDNAME_NOLOT);
        nom = statement.dbReadString(ITITiersDefTable.DESIGNATION_1);
        prenom = statement.dbReadString(ITITiersDefTable.DESIGNATION_2);
    }

    public String getCsNationalite() {
        return csNationalite;
    }

    public String getCsSexe() {
        return csSexe;
    }

    public String getDateDebutDroit() {
        return dateDebutDroit;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public String getDescriptionLot() {
        return descriptionLot;
    }

    public String getGenreService() {
        return genreService;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getNoAVS() {
        return noAVS;
    }

    public String getNoLot() {
        return noLot;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setCsNationalite(String csNationalite) {
        this.csNationalite = csNationalite;
    }

    public void setCsSexe(String csSexe) {
        this.csSexe = csSexe;
    }

    public void setDateDebutDroit(String dateDebutDroit) {
        this.dateDebutDroit = dateDebutDroit;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setDescriptionLot(String descriptionLot) {
        this.descriptionLot = descriptionLot;
    }

    public void setGenreService(String genreService) {
        this.genreService = genreService;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setNoAVS(String noAVS) {
        this.noAVS = noAVS;
    }

    public void setNoLot(String noLot) {
        this.noLot = noLot;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
}
