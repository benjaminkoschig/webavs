package globaz.corvus.db.avances;

import globaz.corvus.api.avances.IREAvances;
import globaz.corvus.api.lots.IRELot;
import globaz.corvus.db.lots.RELot;
import globaz.corvus.db.lots.RELotManager;
import globaz.corvus.utils.REPmtMensuel;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.tools.PRDateFormater;
import globaz.prestation.tools.nnss.PRNSSUtil;
import globaz.pyxis.db.tiers.ITIPersonneAvsDefTable;
import globaz.pyxis.db.tiers.ITIPersonneDefTable;
import globaz.pyxis.db.tiers.ITITiersDefTable;

/**
 * @author SCR
 */
public class REAvance extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_DATE_DEB_ACOMPTE = "WIDDAC";
    public static final String FIELDNAME_DATE_DEB_PMT_1ER_ACOMPTE = "WIDD1A";
    public static final String FIELDNAME_DATE_FIN_ACOMPTE = "WIDFAC";
    public static final String FIELDNAME_DATE_PMT_1ER_ACOMPTE = "WIDP1A";
    public static final String FIELDNAME_DOMAINE = "WITDAP";
    public static final String FIELDNAME_DOMAINE_AVANCE = "WITAVC";
    public static final String FIELDNAME_ETAT_1ER_ACOMPTE = "WITET1";
    public static final String FIELDNAME_ETAT_ACOMPTES = "WITETA";
    public static final String FIELDNAME_ID_AFFILIE = "WIIAFF";
    public static final String FIELDNAME_ID_AVANCE = "WIIAVA";
    public static final String FIELDNAME_ID_TIERS_ADR_PMT = "WIITAP";
    public static final String FIELDNAME_ID_TIERS_BENEFICIAIRE = "WIITBE";
    public static final String FIELDNAME_LIBELLE = "WILLIB";
    public static final String FIELDNAME_MONTANT_1ER_ACOMPTE = "WIM1AC";
    public static final String FIELDNAME_MONTANT_MENSUEL = "WIMMEN";

    public static final String TABLE_NAME_AVANCES = "REAVANCE";

    private String csDomaine;
    private String csDomaineAvance;
    private String csEtat1erAcompte;
    private String csEtatAcomptes;
    private String dateDebutAcompte;
    private String dateDebutPmt1erAcompte;
    private String dateFinAcompte;
    private String dateNaissance;
    private String datePmt1erAcompte;
    private String idAffilie;
    private String idAvance;
    private String idTiersAdrPmt;
    private String idTiersBeneficiaire;
    private String libelle;
    private String montant1erAcompte;
    private String montantMensuel;
    private String nationalite;
    private String nom;
    private String nss;
    private String prenom;
    private String sexe;

    public REAvance() {
        super();

        csDomaine = "";
        csDomaineAvance = "";
        csEtat1erAcompte = "";
        csEtatAcomptes = "";
        dateDebutAcompte = "";
        dateDebutPmt1erAcompte = "";
        dateFinAcompte = "";
        dateNaissance = "";
        datePmt1erAcompte = "";
        idAffilie = "";
        idAvance = "";
        idTiersAdrPmt = "";
        idTiersBeneficiaire = "";
        libelle = "";
        montant1erAcompte = "";
        montantMensuel = "";
        nationalite = "";
        nom = "";
        nss = "";
        prenom = "";
        sexe = "";
    }

    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdAvance(this._incCounter(transaction, "0"));
        setCsEtat1erAcompte(IREAvances.CS_ETAT_1ER_ACOMPTE_ATTENTE);
        setCsEtatAcomptes(IREAvances.CS_ETAT_ACOMPTE_ATTENTE);
    }

    @Override
    protected String _getFrom(BStatement statement) {

        StringBuilder sql = new StringBuilder();

        String tableAvances = _getCollection() + REAvance.TABLE_NAME_AVANCES;
        String tableTiers = _getCollection() + ITITiersDefTable.TABLE_NAME;
        String tablePersonne = _getCollection() + ITIPersonneDefTable.TABLE_NAME;
        String tablePersonneAvs = _getCollection() + ITIPersonneAvsDefTable.TABLE_NAME;

        sql.append(tableAvances);

        sql.append(" INNER JOIN ").append(tableTiers);
        sql.append(" ON ").append(tableAvances).append(".").append(REAvance.FIELDNAME_ID_TIERS_BENEFICIAIRE)
                .append("=").append(tableTiers).append(".").append(ITITiersDefTable.ID_TIERS);

        sql.append(" INNER JOIN ").append(tablePersonne);
        sql.append(" ON ").append(tableTiers).append(".").append(ITITiersDefTable.ID_TIERS).append("=")
                .append(tablePersonne).append(".").append(ITIPersonneDefTable.ID_TIERS);

        sql.append(" INNER JOIN ").append(tablePersonneAvs);
        sql.append(" ON ").append(tablePersonne).append(".").append(ITIPersonneDefTable.ID_TIERS).append("=")
                .append(tablePersonneAvs).append(".").append(ITIPersonneAvsDefTable.ID_TIERS);

        return sql.toString();
    }

    @Override
    protected String _getTableName() {
        return REAvance.TABLE_NAME_AVANCES;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {

        csDomaine = statement.dbReadNumeric(REAvance.FIELDNAME_DOMAINE);
        csDomaineAvance = statement.dbReadNumeric(REAvance.FIELDNAME_DOMAINE_AVANCE);
        csEtatAcomptes = statement.dbReadNumeric(REAvance.FIELDNAME_ETAT_ACOMPTES);
        csEtat1erAcompte = statement.dbReadNumeric(REAvance.FIELDNAME_ETAT_1ER_ACOMPTE);
        dateDebutPmt1erAcompte = PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(statement
                .dbReadNumeric(REAvance.FIELDNAME_DATE_DEB_PMT_1ER_ACOMPTE));
        dateDebutAcompte = PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(statement
                .dbReadNumeric(REAvance.FIELDNAME_DATE_DEB_ACOMPTE));
        dateFinAcompte = PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(statement
                .dbReadNumeric(REAvance.FIELDNAME_DATE_FIN_ACOMPTE));
        dateNaissance = statement.dbReadString(ITIPersonneDefTable.DATE_NAISSANCE);
        datePmt1erAcompte = PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(statement
                .dbReadNumeric(REAvance.FIELDNAME_DATE_PMT_1ER_ACOMPTE));
        idAffilie = statement.dbReadString(REAvance.FIELDNAME_ID_AFFILIE);
        idAvance = statement.dbReadNumeric(REAvance.FIELDNAME_ID_AVANCE);
        idTiersAdrPmt = statement.dbReadNumeric(REAvance.FIELDNAME_ID_TIERS_ADR_PMT);
        idTiersBeneficiaire = statement.dbReadNumeric(REAvance.FIELDNAME_ID_TIERS_BENEFICIAIRE);
        libelle = statement.dbReadString(REAvance.FIELDNAME_LIBELLE);
        montantMensuel = statement.dbReadNumeric(REAvance.FIELDNAME_MONTANT_MENSUEL);
        montant1erAcompte = statement.dbReadNumeric(REAvance.FIELDNAME_MONTANT_1ER_ACOMPTE);
        nom = statement.dbReadString(ITITiersDefTable.DESIGNATION_1);
        nss = statement.dbReadString(ITIPersonneAvsDefTable.NUMERO_AVS_ACTUEL);
        prenom = statement.dbReadString(ITITiersDefTable.DESIGNATION_2);
        nationalite = statement.dbReadString(ITITiersDefTable.ID_PAYS);

    }

    @Override
    protected void _validate(BStatement statement) throws Exception {

        JACalendar cal = new JACalendarGregorian();

        // Saisie/Maj d'un premier acompte.
        // La date de début du 1er acompte doit être supérieur à la date du jour.
        if (!JadeStringUtil.isBlankOrZero(getMontant1erAcompte())
                && IREAvances.CS_ETAT_1ER_ACOMPTE_ATTENTE.equals(getCsEtat1erAcompte())) {
            JADate today = JACalendar.today();
            JADate ddPmt1erAcompte = new JADate(getDateDebutPmt1erAcompte());
            if (cal.compare(today, ddPmt1erAcompte) != JACalendar.COMPARE_SECONDUPPER) {
                if (cal.compare(today, ddPmt1erAcompte) != JACalendar.COMPARE_EQUALS) {
                    _addError(statement.getTransaction(), getSession().getLabel("PMT_AVANCES_ERREUR_DATE_1ER_ACPT"));
                    return;
                }
            }
        }

        // si montant mensuel différent de 0, et en attente
        if (!JadeStringUtil.isBlankOrZero(getMontantMensuel())
                && IREAvances.CS_ETAT_ACOMPTE_ATTENTE.equals(getCsEtatAcomptes())) {

            // recherche des lots
            RELotManager lotMgr = new RELotManager();
            lotMgr.setSession(getSession());
            lotMgr.setForCsType(IRELot.CS_TYP_LOT_PMT_AVANCES);
            lotMgr.setForCsLotOwner(IRELot.CS_LOT_OWNER_RENTES);
            lotMgr.find(statement.getTransaction());

            // date critique pour laquelle une avance ne pourra pas être avant
            JADate dateCritique = null;
            if (lotMgr.size() > 0) {
                RELot lot = (RELot) lotMgr.getFirstEntity();
                // Date du jour
                JADate today = JACalendar.today();
                JADate creationLot = new JADate(lot.getDateCreationLot());

                // Si aoujourd'hui plus petit que date creation du lot
                if (cal.compare(today, creationLot) == JACalendar.COMPARE_FIRSTLOWER) {
                    _addError(statement.getTransaction(), "La date de début des acomptes doit être supérieur à : "
                            + creationLot.toStr("."));
                    return;
                }
            }

            dateCritique = new JADate(REPmtMensuel.getDateDernierPmt(getSession()));

            if ((cal.compare(dateCritique, new JADate(getDateDebutAcompte())) == JACalendar.COMPARE_FIRSTUPPER)) {
                _addError(statement.getTransaction(), "La date de début des acomptes doit être supérieur à : "
                        + dateCritique.toStr("."));
                return;
            }
        }

        // Saisie mise a jour idAdressePaiement,et domaine adresse si egal 0, pas d'adresse, erreur
        if (JadeStringUtil.isBlankOrZero(getIdTiersAdrPmt()) || JadeStringUtil.isBlankOrZero(getCsDomaine())) {
            _addError(statement.getTransaction(), getSession().getLabel("PMT_AVANCES_ERREUR_DATE_ADRESSE_PAIEMENT"));
        }
    }

    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey(REAvance.FIELDNAME_ID_AVANCE,
                this._dbWriteNumeric(statement.getTransaction(), idAvance, "idAvance"));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(REAvance.FIELDNAME_ID_AVANCE,
                this._dbWriteNumeric(statement.getTransaction(), idAvance, "idAvance"));
        statement.writeField(
                REAvance.FIELDNAME_DATE_PMT_1ER_ACOMPTE,
                this._dbWriteNumeric(statement.getTransaction(),
                        PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(datePmt1erAcompte), "datePmt1erAcompte"));
        statement.writeField(REAvance.FIELDNAME_DATE_DEB_PMT_1ER_ACOMPTE, this._dbWriteNumeric(
                statement.getTransaction(), PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(dateDebutPmt1erAcompte),
                "dateDebutPmt1erAcompte"));
        statement.writeField(REAvance.FIELDNAME_ID_TIERS_BENEFICIAIRE,
                this._dbWriteNumeric(statement.getTransaction(), idTiersBeneficiaire, "idTiersBeneficiaire"));
        statement.writeField(REAvance.FIELDNAME_ID_TIERS_ADR_PMT,
                this._dbWriteNumeric(statement.getTransaction(), idTiersAdrPmt, "idTiersAdrPmt"));
        statement.writeField(REAvance.FIELDNAME_DOMAINE,
                this._dbWriteNumeric(statement.getTransaction(), csDomaine, "csDomaine"));
        statement.writeField(
                REAvance.FIELDNAME_DATE_DEB_ACOMPTE,
                this._dbWriteNumeric(statement.getTransaction(),
                        PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(dateDebutAcompte), "datedebutAcompte"));
        statement.writeField(
                REAvance.FIELDNAME_DATE_FIN_ACOMPTE,
                this._dbWriteNumeric(statement.getTransaction(),
                        PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(dateFinAcompte), "dateFinAcompte"));
        statement.writeField(REAvance.FIELDNAME_MONTANT_MENSUEL,
                this._dbWriteNumeric(statement.getTransaction(), montantMensuel, "montantMensuel"));
        statement.writeField(REAvance.FIELDNAME_MONTANT_1ER_ACOMPTE,
                this._dbWriteNumeric(statement.getTransaction(), montant1erAcompte, "montant1erAcompte"));
        statement.writeField(REAvance.FIELDNAME_ETAT_ACOMPTES,
                this._dbWriteNumeric(statement.getTransaction(), csEtatAcomptes, "csEtatAcomptes"));
        statement.writeField(REAvance.FIELDNAME_ETAT_1ER_ACOMPTE,
                this._dbWriteNumeric(statement.getTransaction(), csEtat1erAcompte, "csEtat1erAcompte"));
        statement.writeField(REAvance.FIELDNAME_ID_AFFILIE,
                this._dbWriteNumeric(statement.getTransaction(), idAffilie, "idAffilie"));
        statement.writeField(REAvance.FIELDNAME_LIBELLE,
                this._dbWriteString(statement.getTransaction(), libelle, "libelle"));
        statement.writeField(REAvance.FIELDNAME_DOMAINE_AVANCE,
                this._dbWriteNumeric(statement.getTransaction(), csDomaineAvance, "csDomaineAvance"));
    }

    public String getCsDomaine() {
        return csDomaine;
    }

    public String getCsDomaineAvance() {
        return csDomaineAvance;
    }

    public String getCsEtat1erAcompte() {
        return csEtat1erAcompte;
    }

    public String getCsEtatAcomptes() {
        return csEtatAcomptes;
    }

    public String getDateDebutAcompte() {
        return dateDebutAcompte;
    }

    public String getDateDebutPmt1erAcompte() {
        return dateDebutPmt1erAcompte;
    }

    public String getDateFinAcompte() {
        return dateFinAcompte;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public String getDatePmt1erAcompte() {
        return datePmt1erAcompte;
    }

    public String getDetailRequerant() {
        return PRNSSUtil.formatDetailRequerantListe(getNss(), getNom() + " " + getPrenom(), getDateNaissance(),
                getSexe(), getNationalite());
    }

    public String getIdAffilie() {
        return idAffilie;
    }

    public String getIdAvance() {
        return idAvance;
    }

    public String getIdTiersAdrPmt() {
        return idTiersAdrPmt;
    }

    public String getIdTiersBeneficiaire() {
        return idTiersBeneficiaire;
    }

    public String getLibelle() {
        return libelle;
    }

    public String getMontant1erAcompte() {
        return montant1erAcompte;
    }

    public String getMontantMensuel() {
        return montantMensuel;
    }

    public String getNationalite() {
        return nationalite;
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

    @Override
    public boolean hasCreationSpy() {
        return true;
    }

    public void setCsDomaine(String csDomaine) {
        this.csDomaine = csDomaine;
    }

    public void setCsDomaineAvance(String csDomaineAvance) {
        this.csDomaineAvance = csDomaineAvance;
    }

    public void setCsEtat1erAcompte(String csEtat1erAcompte) {
        this.csEtat1erAcompte = csEtat1erAcompte;
    }

    public void setCsEtatAcomptes(String csEtatAcomptes) {
        this.csEtatAcomptes = csEtatAcomptes;
    }

    public void setDateDebutAcompte(String dateDebutAcompte) {
        this.dateDebutAcompte = dateDebutAcompte;
    }

    public void setDateDebutPmt1erAcompte(String dateDebutPmt1erAcompte) {
        this.dateDebutPmt1erAcompte = dateDebutPmt1erAcompte;
    }

    public void setDateFinAcompte(String dateFinAcompte) {
        this.dateFinAcompte = dateFinAcompte;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setDatePmt1erAcompte(String datePmt1erAcompte) {
        this.datePmt1erAcompte = datePmt1erAcompte;
    }

    public void setIdAffilie(String idAffilie) {
        this.idAffilie = idAffilie;
    }

    public void setIdAvance(String idAvance) {
        this.idAvance = idAvance;
    }

    public void setIdTiersAdrPmt(String idTiersAdrPmt) {
        this.idTiersAdrPmt = idTiersAdrPmt;
    }

    public void setIdTiersBeneficiaire(String idTiersBeneficiaire) {
        this.idTiersBeneficiaire = idTiersBeneficiaire;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public void setMontant1erAcompte(String montant1erAcompte) {
        this.montant1erAcompte = montant1erAcompte;
    }

    public void setMontantMensuel(String montantMensuel) {
        this.montantMensuel = montantMensuel;
    }

    public void setNationalite(String nationalite) {
        this.nationalite = nationalite;
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
