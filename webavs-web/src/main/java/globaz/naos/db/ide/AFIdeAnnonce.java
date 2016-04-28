package globaz.naos.db.ide;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.translation.CodeSystem;
import java.io.Serializable;

public class AFIdeAnnonce extends BEntity implements Serializable {

    private static final long serialVersionUID = 8755637531995554164L;

    private static final String ALIAS_TABLE_AFFILIATION_WITHOUT_POINT = "AFF";
    private static final String ALIAS_TABLE_ANNONCE_WITHOUT_POINT = "ANN";
    private static final String ALIAS_TABLE_COTISATION_WITHOUT_POINT = "COT";

    private static final String ALIAS_TABLE_AFFILIATION = "AFF.";
    private static final String ALIAS_TABLE_ANNONCE = "ANN.";
    private static final String ALIAS_TABLE_COTISATION = "COT.";

    public static final String IDE_ANNONCE_TABLE_NAME = "AFANOIDE";
    public static final String IDE_ANNONCE_FIELD_ID_ANNONCE = "AIDEID";
    public static final String IDE_ANNONCE_FIELD_ID_AFFILIATION = "AIDEAF";
    public static final String IDE_ANNONCE_FIELD_ID_COTISATION = "AIDECO";
    public static final String IDE_ANNONCE_FIELD_CATEGORIE = "AIDECA";
    public static final String IDE_ANNONCE_FIELD_TYPE = "AIDETY";
    public static final String IDE_ANNONCE_FIELD_ETAT = "AIDEET";
    public static final String IDE_ANNONCE_FIELD_DATE_CREATION = "AIDEDC";
    public static final String IDE_ANNONCE_FIELD_DATE_TRAITEMENT = "AIDEDT";
    public static final String IDE_ANNONCE_FIELD_NUMERO_IDE_REMPLACEMENT = "AIDENR";

    public static final String IDE_ANNONCE_FIELD_HISTORIQUE_ADRESSE = "AIDEHA";
    public static final String IDE_ANNONCE_FIELD_HISTORIQUE_RUE = "AIDHAR";
    public static final String IDE_ANNONCE_FIELD_HISTORIQUE_NPA = "AIDHAN";
    public static final String IDE_ANNONCE_FIELD_HISTORIQUE_LOCALITE = "AIDHAL";
    public static final String IDE_ANNONCE_FIELD_HISTORIQUE_CANTON = "AIDEHC";
    public static final String IDE_ANNONCE_FIELD_HISTORIQUE_RAISON_SOCIALE = "AIDEHR";
    public static final String IDE_ANNONCE_FIELD_HISTORIQUE_FORME_JURIDIQUE = "AIDEHF";
    public static final String IDE_ANNONCE_FIELD_HISTORIQUE_LANGUE_TIERS = "AIDEHL";
    public static final String IDE_ANNONCE_FIELD_HISTORIQUE_BRANCHE_ECONOMIQUE = "AIDEHB";
    public static final String IDE_ANNONCE_FIELD_HISTORIQUE_NUMERO_IDE = "AIDEHN";
    public static final String IDE_ANNONCE_FIELD_HISTORIQUE_STATUT_IDE = "AIDEHS";
    public static final String IDE_ANNONCE_FIELD_HISTORIQUE_TYPE_ANNONCE_DATE_IDE = "AIDEHD";
    // D0181
    public static final String IDE_ANNONCE_FIELD_HISTORIQUE_NAISSANCE = "AIDHDN";
    public static final String IDE_ANNONCE_FIELD_HISTORIQUE_ACTIVITE = "AIDHAC";
    public static final String IDE_ANNONCE_FIELD_HISTORIQUE_NOGA = "AIDHNO";
    public static final String IDE_ANNONCE_FIELD_HISTORIQUE_NUMERO_AFFILIE = "AIDHNA";

    public static final String IDE_ANNONCE_FIELD_MESSAGE_ERREUR_BUSINESS = "AIDEMB";
    public static final String IDE_ANNONCE_FIELD_MESSAGE_ERREUR_TECHNICAL = "AIDEMT";

    public static final String IDE_ANNONCE_FIELD_LIST_ID_AFFILIATION_LIEE = "AIDELA";
    public static final String IDE_ANNONCE_FIELD_LIST_NUMERO_AFFILIE_LIEE = "AIDENA";

    private String ideAnnonceIdAnnonce = "";
    private String ideAnnonceIdAffiliation = "";
    private String ideAnnonceIdCotisation = "";
    private String ideAnnonceCategorie = "";
    private String ideAnnonceType = "";
    private String ideAnnonceEtat = "";
    private String ideAnnonceDateCreation = "";
    private String ideAnnonceDateTraitement = "";
    private String histAdresse = "";
    private String histRue = "";
    private String histNPA = "";
    private String histLocalite = "";
    private String histCanton = "";
    private String histRaisonSociale = "";
    private String histFormeJuridique = "";
    private String histLangueTiers = "";
    private String histBrancheEconomique = "";
    private String histNumeroIde = "";
    private String histStatutIde = "";
    private String histTypeAnnonceDate = "";
    // D0181
    private String histNaissance = "";
    private String histActivite = "";
    /**
     * code noga selon le registre != code noga dans l'affiliation
     */
    private String histNoga = "";
    private String histNumeroAffilie = "";

    private String numeroIdeRemplacement = "";
    private String ideAnnonceListIdAffiliationLiee = "";
    private String ideAnnonceListNumeroAffilieLiee = "";

    // Ces champs n'appartiennent pas à cette table mais à la table AFAFFIP
    // Ils sont présents ici afin de pouvoir être renseignés lors de la lecture cette entité
    private String numeroIde = "";
    private String numeroAffilie = "";
    private String raisonSociale = "";
    private String statutIde = "";
    private String idTiers = "";

    private String typeAnnonceDate = "";

    private String messageErreurForBusinessUser = "";
    private String messageErreurForTechnicalUser = "";

    public String getNumeroIdeRemplacement() {
        return numeroIdeRemplacement;
    }

    public void setNumeroIdeRemplacement(String numeroIdeRemplacement) {
        this.numeroIdeRemplacement = numeroIdeRemplacement;
    }

    public String getIdeAnnonceListNumeroAffilieLiee() {
        return ideAnnonceListNumeroAffilieLiee;
    }

    public void setIdeAnnonceListNumeroAffilieLiee(String ideAnnonceListNumeroAffilieLiee) {
        this.ideAnnonceListNumeroAffilieLiee = ideAnnonceListNumeroAffilieLiee;
    }

    public String getHistNumeroIde() {
        if (JadeStringUtil.isEmpty(histNumeroIde)) {
            return getNumeroIde();
        }
        return histNumeroIde;
    }

    public void setHistNumeroIde(String histNumeroIde) {
        this.histNumeroIde = histNumeroIde;
    }

    public String getIdeAnnonceListIdAffiliationLiee() {
        return ideAnnonceListIdAffiliationLiee;
    }

    public void setIdeAnnonceListIdAffiliationLiee(String ideAnnonceListIdAffiliationLiee) {
        this.ideAnnonceListIdAffiliationLiee = ideAnnonceListIdAffiliationLiee;
    }

    public String getHistStatutIde() {
        if (JadeStringUtil.isEmpty(histStatutIde)) {
            return getStatutIde();
        }
        return histStatutIde;
    }

    public void setHistStatutIde(String histStatutIde) {
        this.histStatutIde = histStatutIde;
    }

    public String getHistAdresse() {
        return histAdresse;
    }

    public void setHistAdresse(String histAdresse) {
        this.histAdresse = histAdresse;
    }

    public String getHistRue() {
        return histRue;
    }

    public void setHistRue(String histRue) {
        this.histRue = histRue;
    }

    public String getHistNPA() {
        return histNPA;
    }

    public void setHistNPA(String histNPA) {
        this.histNPA = histNPA;
    }

    public String getHistLocalite() {
        return histLocalite;
    }

    public void setHistLocalite(String histLocalite) {
        this.histLocalite = histLocalite;
    }

    public String getHistCanton() {
        return histCanton;
    }

    public void setHistCanton(String histCanton) {
        this.histCanton = histCanton;
    }

    /**
     * CAUTION with fallBack to getRaisonSociale() if hist is empty
     * 
     * @return historique ou raison sociale de l'affilié si historique vide
     */
    public String getHistRaisonSociale() {
        if (JadeStringUtil.isEmpty(histRaisonSociale)) {
            return getRaisonSociale();
        }
        return histRaisonSociale;
    }

    /**
     * hist only, empty if DB column is empty<br>
     * CAUTION this one BECAUSE getHistRaisonSociale() auto fallBack to getRaisonSociale() if hist is empty
     * 
     * @return only the hist, without fallback
     */
    public String getHistRaisonSocialeONLY() {
        return histRaisonSociale;
    }

    public void setHistRaisonSociale(String histRaisonSociale) {
        this.histRaisonSociale = histRaisonSociale;
    }

    public String getHistFormeJuridique() {
        return histFormeJuridique;
    }

    public void setHistFormeJuridique(String histFormeJuridique) {
        this.histFormeJuridique = histFormeJuridique;
    }

    public String getHistLangueTiers() {
        return histLangueTiers;
    }

    public void setHistLangueTiers(String histLangueTiers) {
        this.histLangueTiers = histLangueTiers;
    }

    public String getHistBrancheEconomique() {
        return histBrancheEconomique;
    }

    public void setHistBrancheEconomique(String histBrancheEconomique) {
        this.histBrancheEconomique = histBrancheEconomique;
    }

    public String getMessageErreurForBusinessUser() {
        return messageErreurForBusinessUser;
    }

    public void setMessageErreurForBusinessUser(String messageErreurForBusinessUser) {
        this.messageErreurForBusinessUser = messageErreurForBusinessUser;
    }

    public String getMessageErreurForTechnicalUser() {
        return messageErreurForTechnicalUser;
    }

    public void setMessageErreurForTechnicalUser(String messageErreurForTechnicalUser) {
        this.messageErreurForTechnicalUser = messageErreurForTechnicalUser;
    }

    public boolean hasAnnonceErreur() {
        return !JadeStringUtil.isBlankOrZero(messageErreurForBusinessUser)
                || !JadeStringUtil.isBlankOrZero(messageErreurForTechnicalUser);
    }

    public boolean isAnnonceEnAttente() {
        return JadeDateUtil.isDateAfter(getTypeAnnonceDate(), JACalendar.todayJJsMMsAAAA());
    }

    @Override
    protected String _getFrom(BStatement statement) {

        StringBuffer sqlFrom = new StringBuffer();

        sqlFrom.append(_getCollection() + AFIdeAnnonce.IDE_ANNONCE_TABLE_NAME + " AS "
                + ALIAS_TABLE_ANNONCE_WITHOUT_POINT);
        sqlFrom.append(" LEFT OUTER JOIN ");
        sqlFrom.append(_getCollection() + AFAffiliation.TABLE_NAME + " AS " + ALIAS_TABLE_AFFILIATION_WITHOUT_POINT);
        sqlFrom.append(" ON(" + ALIAS_TABLE_AFFILIATION + AFAffiliation.FIELDNAME_AFFILIATION_ID + " = "
                + ALIAS_TABLE_ANNONCE + AFIdeAnnonce.IDE_ANNONCE_FIELD_ID_AFFILIATION + ")");
        sqlFrom.append(" LEFT OUTER JOIN ");
        sqlFrom.append(_getCollection() + AFCotisation.TABLE_NAME + " AS " + ALIAS_TABLE_COTISATION_WITHOUT_POINT);
        sqlFrom.append(" ON(" + ALIAS_TABLE_COTISATION + AFCotisation.FIELDNAME_COTISATION_ID + " = "
                + ALIAS_TABLE_ANNONCE + AFIdeAnnonce.IDE_ANNONCE_FIELD_ID_COTISATION + ")");
        return sqlFrom.toString();

    }

    public String getIdTiers() {
        return idTiers;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public String getNumeroIde() {
        return numeroIde;
    }

    public void setNumeroIde(String numeroIde) {
        this.numeroIde = numeroIde;
    }

    public String getNumeroAffilie() {
        return numeroAffilie;
    }

    public void setNumeroAffilie(String numeroAffilie) {
        this.numeroAffilie = numeroAffilie;
    }

    public String getRaisonSociale() {
        return raisonSociale;
    }

    public void setRaisonSociale(String raisonSociale) {
        this.raisonSociale = raisonSociale;
    }

    public String getStatutIde() {
        return statutIde;
    }

    public void setStatutIde(String statutIde) {
        this.statutIde = statutIde;
    }

    public AFIdeAnnonce() {
        super();
    }

    @Override
    protected void _beforeAdd(final BTransaction transaction) throws Exception {

        setIdeAnnonceIdAnnonce(this._incCounter(transaction, "0"));
        setIdeAnnonceDateCreation(JACalendar.todayJJsMMsAAAA());

    }

    @Override
    protected String _getTableName() {
        return AFIdeAnnonce.IDE_ANNONCE_TABLE_NAME;
    }

    @Override
    protected void _readProperties(final BStatement statement) throws Exception {
        ideAnnonceIdAnnonce = statement.dbReadNumeric(AFIdeAnnonce.IDE_ANNONCE_FIELD_ID_ANNONCE);
        ideAnnonceIdAffiliation = statement.dbReadNumeric(AFIdeAnnonce.IDE_ANNONCE_FIELD_ID_AFFILIATION);
        ideAnnonceListIdAffiliationLiee = statement
                .dbReadString(AFIdeAnnonce.IDE_ANNONCE_FIELD_LIST_ID_AFFILIATION_LIEE);
        ideAnnonceListNumeroAffilieLiee = statement
                .dbReadString(AFIdeAnnonce.IDE_ANNONCE_FIELD_LIST_NUMERO_AFFILIE_LIEE);
        ideAnnonceIdCotisation = statement.dbReadNumeric(AFIdeAnnonce.IDE_ANNONCE_FIELD_ID_COTISATION);
        ideAnnonceCategorie = statement.dbReadNumeric(AFIdeAnnonce.IDE_ANNONCE_FIELD_CATEGORIE);
        ideAnnonceType = statement.dbReadNumeric(AFIdeAnnonce.IDE_ANNONCE_FIELD_TYPE);
        ideAnnonceEtat = statement.dbReadNumeric(AFIdeAnnonce.IDE_ANNONCE_FIELD_ETAT);
        ideAnnonceDateCreation = statement.dbReadDateAMJ(AFIdeAnnonce.IDE_ANNONCE_FIELD_DATE_CREATION);
        ideAnnonceDateTraitement = statement.dbReadDateAMJ(AFIdeAnnonce.IDE_ANNONCE_FIELD_DATE_TRAITEMENT);
        histAdresse = statement.dbReadString(AFIdeAnnonce.IDE_ANNONCE_FIELD_HISTORIQUE_ADRESSE);
        histRue = statement.dbReadString(AFIdeAnnonce.IDE_ANNONCE_FIELD_HISTORIQUE_RUE);
        histNPA = statement.dbReadString(AFIdeAnnonce.IDE_ANNONCE_FIELD_HISTORIQUE_NPA);
        histLocalite = statement.dbReadString(AFIdeAnnonce.IDE_ANNONCE_FIELD_HISTORIQUE_LOCALITE);
        histCanton = statement.dbReadString(AFIdeAnnonce.IDE_ANNONCE_FIELD_HISTORIQUE_CANTON);
        histRaisonSociale = statement.dbReadString(AFIdeAnnonce.IDE_ANNONCE_FIELD_HISTORIQUE_RAISON_SOCIALE);
        histLangueTiers = statement.dbReadString(AFIdeAnnonce.IDE_ANNONCE_FIELD_HISTORIQUE_LANGUE_TIERS);
        histFormeJuridique = statement.dbReadString(AFIdeAnnonce.IDE_ANNONCE_FIELD_HISTORIQUE_FORME_JURIDIQUE);
        histBrancheEconomique = statement.dbReadString(AFIdeAnnonce.IDE_ANNONCE_FIELD_HISTORIQUE_BRANCHE_ECONOMIQUE);
        histNumeroIde = statement.dbReadString(AFIdeAnnonce.IDE_ANNONCE_FIELD_HISTORIQUE_NUMERO_IDE);
        histStatutIde = statement.dbReadString(AFIdeAnnonce.IDE_ANNONCE_FIELD_HISTORIQUE_STATUT_IDE);
        histTypeAnnonceDate = statement.dbReadDateAMJ(AFIdeAnnonce.IDE_ANNONCE_FIELD_HISTORIQUE_TYPE_ANNONCE_DATE_IDE);

        messageErreurForBusinessUser = statement.dbReadString(AFIdeAnnonce.IDE_ANNONCE_FIELD_MESSAGE_ERREUR_BUSINESS);
        messageErreurForTechnicalUser = statement.dbReadString(AFIdeAnnonce.IDE_ANNONCE_FIELD_MESSAGE_ERREUR_TECHNICAL);
        numeroIdeRemplacement = statement.dbReadString(AFIdeAnnonce.IDE_ANNONCE_FIELD_NUMERO_IDE_REMPLACEMENT);

        // D0181
        histNumeroAffilie = statement.dbReadString(AFIdeAnnonce.IDE_ANNONCE_FIELD_HISTORIQUE_NUMERO_AFFILIE);
        histNoga = statement.dbReadString(AFIdeAnnonce.IDE_ANNONCE_FIELD_HISTORIQUE_NOGA);
        histNaissance = statement.dbReadDateAMJ(AFIdeAnnonce.IDE_ANNONCE_FIELD_HISTORIQUE_NAISSANCE);
        histActivite = statement.dbReadString(AFIdeAnnonce.IDE_ANNONCE_FIELD_HISTORIQUE_ACTIVITE);

        numeroIde = statement.dbReadString(AFAffiliation.FIELDNAME_NUMERO_IDE);
        numeroAffilie = statement.dbReadString(AFAffiliation.FIELDNAME_NUMERO_AFFILIE);
        raisonSociale = statement.dbReadString(AFAffiliation.FIELDNAME_RAISON_SOCIALE);
        statutIde = statement.dbReadNumeric(AFAffiliation.FIELDNAME_STATUT_IDE);
        idTiers = statement.dbReadNumeric(AFAffiliation.FIELDNAME_TIER_ID);

        if (!CodeSystem.ETAT_ANNONCE_IDE_TRAITE.equalsIgnoreCase(ideAnnonceEtat)) {
            if (JadeStringUtil.isBlankOrZero(ideAnnonceIdCotisation)) {
                if (CodeSystem.TYPE_ANNONCE_IDE_DESENREGISTREMENT_ACTIF.equalsIgnoreCase(getIdeAnnonceType())
                        || CodeSystem.TYPE_ANNONCE_IDE_RADIATION.equalsIgnoreCase(getIdeAnnonceType())) {
                    typeAnnonceDate = statement.dbReadDateAMJ(AFAffiliation.FIELDNAME_AFF_DFIN);
                } else if (CodeSystem.TYPE_ANNONCE_IDE_ENREGISTREMENT_ACTIF.equalsIgnoreCase(getIdeAnnonceType())) {
                    typeAnnonceDate = statement.dbReadDateAMJ(AFAffiliation.FIELDNAME_AFF_DDEBUT);
                }
            } else {
                if (CodeSystem.TYPE_ANNONCE_IDE_DESENREGISTREMENT_ACTIF.equalsIgnoreCase(getIdeAnnonceType())) {
                    typeAnnonceDate = statement.dbReadDateAMJ(AFCotisation.FIELDNAME_DATE_FIN);
                } else if (CodeSystem.TYPE_ANNONCE_IDE_ENREGISTREMENT_ACTIF.equalsIgnoreCase(getIdeAnnonceType())) {
                    typeAnnonceDate = statement.dbReadDateAMJ(AFCotisation.FIELDNAME_DATE_DEB);
                }
            }
        }
    }

    @Override
    protected void _validate(final BStatement statement) throws Exception {

        // todo
    }

    @Override
    protected void _writePrimaryKey(final BStatement statement) throws Exception {
        statement.writeKey(AFIdeAnnonce.IDE_ANNONCE_FIELD_ID_ANNONCE,
                this._dbWriteNumeric(statement.getTransaction(), getIdeAnnonceIdAnnonce(), ""));
    }

    @Override
    protected void _writeProperties(final BStatement statement) throws Exception {
        statement.writeField(AFIdeAnnonce.IDE_ANNONCE_FIELD_ID_ANNONCE,
                this._dbWriteNumeric(statement.getTransaction(), ideAnnonceIdAnnonce, "ideAnnonceIdAnnonce"));
        statement.writeField(AFIdeAnnonce.IDE_ANNONCE_FIELD_ID_AFFILIATION,
                this._dbWriteNumeric(statement.getTransaction(), ideAnnonceIdAffiliation, "ideAnnonceIdAffiliation"));
        statement.writeField(AFIdeAnnonce.IDE_ANNONCE_FIELD_LIST_ID_AFFILIATION_LIEE, this._dbWriteString(
                statement.getTransaction(), ideAnnonceListIdAffiliationLiee, "ideAnnonceListIdAffiliationLiee"));
        statement.writeField(AFIdeAnnonce.IDE_ANNONCE_FIELD_LIST_NUMERO_AFFILIE_LIEE, this._dbWriteString(
                statement.getTransaction(), ideAnnonceListNumeroAffilieLiee, "ideAnnonceListNumeroAffilieLiee"));
        statement.writeField(AFIdeAnnonce.IDE_ANNONCE_FIELD_ID_COTISATION,
                this._dbWriteNumeric(statement.getTransaction(), ideAnnonceIdCotisation, "ideAnnonceIdCotisation"));
        statement.writeField(AFIdeAnnonce.IDE_ANNONCE_FIELD_CATEGORIE,
                this._dbWriteNumeric(statement.getTransaction(), ideAnnonceCategorie, "ideAnnonceCategorie"));
        statement.writeField(AFIdeAnnonce.IDE_ANNONCE_FIELD_TYPE,
                this._dbWriteNumeric(statement.getTransaction(), ideAnnonceType, "ideAnnonceType"));
        statement.writeField(AFIdeAnnonce.IDE_ANNONCE_FIELD_ETAT,
                this._dbWriteNumeric(statement.getTransaction(), ideAnnonceEtat, "ideAnnonceEtat"));
        statement.writeField(AFIdeAnnonce.IDE_ANNONCE_FIELD_DATE_CREATION,
                this._dbWriteDateAMJ(statement.getTransaction(), ideAnnonceDateCreation, "ideAnnonceDateCreation"));
        statement.writeField(AFIdeAnnonce.IDE_ANNONCE_FIELD_DATE_TRAITEMENT,
                this._dbWriteDateAMJ(statement.getTransaction(), ideAnnonceDateTraitement, "ideAnnonceDateTraitement"));

        statement.writeField(AFIdeAnnonce.IDE_ANNONCE_FIELD_HISTORIQUE_ADRESSE,
                this._dbWriteString(statement.getTransaction(), histAdresse, "histAdresse"));
        statement.writeField(AFIdeAnnonce.IDE_ANNONCE_FIELD_HISTORIQUE_RUE,
                this._dbWriteString(statement.getTransaction(), histRue, "histRue"));
        statement.writeField(AFIdeAnnonce.IDE_ANNONCE_FIELD_HISTORIQUE_NPA,
                this._dbWriteString(statement.getTransaction(), histNPA, "histNPA"));
        statement.writeField(AFIdeAnnonce.IDE_ANNONCE_FIELD_HISTORIQUE_LOCALITE,
                this._dbWriteString(statement.getTransaction(), histLocalite, "histLocalite"));
        statement.writeField(AFIdeAnnonce.IDE_ANNONCE_FIELD_HISTORIQUE_CANTON,
                this._dbWriteString(statement.getTransaction(), histCanton, "histCanton"));
        statement.writeField(AFIdeAnnonce.IDE_ANNONCE_FIELD_HISTORIQUE_RAISON_SOCIALE,
                this._dbWriteString(statement.getTransaction(), histRaisonSociale, "histRaisonSociale"));
        statement.writeField(AFIdeAnnonce.IDE_ANNONCE_FIELD_HISTORIQUE_LANGUE_TIERS,
                this._dbWriteString(statement.getTransaction(), histLangueTiers, "histLangueTiers"));
        statement.writeField(AFIdeAnnonce.IDE_ANNONCE_FIELD_HISTORIQUE_FORME_JURIDIQUE,
                this._dbWriteString(statement.getTransaction(), histFormeJuridique, "histFormeJuridique"));
        statement.writeField(AFIdeAnnonce.IDE_ANNONCE_FIELD_HISTORIQUE_BRANCHE_ECONOMIQUE,
                this._dbWriteString(statement.getTransaction(), histBrancheEconomique, "histBrancheEconomique"));
        statement.writeField(AFIdeAnnonce.IDE_ANNONCE_FIELD_HISTORIQUE_NUMERO_IDE,
                this._dbWriteString(statement.getTransaction(), histNumeroIde, "histNumeroIde"));
        statement.writeField(AFIdeAnnonce.IDE_ANNONCE_FIELD_HISTORIQUE_STATUT_IDE,
                this._dbWriteString(statement.getTransaction(), histStatutIde, "histStatutIde"));
        statement.writeField(AFIdeAnnonce.IDE_ANNONCE_FIELD_HISTORIQUE_TYPE_ANNONCE_DATE_IDE,
                this._dbWriteDateAMJ(statement.getTransaction(), histTypeAnnonceDate, "histTypeAnnonceDate"));
        statement.writeField(AFIdeAnnonce.IDE_ANNONCE_FIELD_NUMERO_IDE_REMPLACEMENT,
                this._dbWriteString(statement.getTransaction(), numeroIdeRemplacement, "numeroIdeRemplacement"));

        statement.writeField(AFIdeAnnonce.IDE_ANNONCE_FIELD_MESSAGE_ERREUR_BUSINESS, this._dbWriteString(
                statement.getTransaction(), messageErreurForBusinessUser, "messageErreurForBusinessUser"));
        statement.writeField(AFIdeAnnonce.IDE_ANNONCE_FIELD_MESSAGE_ERREUR_TECHNICAL, this._dbWriteString(
                statement.getTransaction(), messageErreurForTechnicalUser, "messageErreurForTechnicalUser"));

        // D0181
        statement.writeField(AFIdeAnnonce.IDE_ANNONCE_FIELD_HISTORIQUE_NAISSANCE,
                this._dbWriteDateAMJ(statement.getTransaction(), histNaissance, "histNaissance"));
        statement.writeField(AFIdeAnnonce.IDE_ANNONCE_FIELD_HISTORIQUE_NUMERO_AFFILIE,
                this._dbWriteString(statement.getTransaction(), histNumeroAffilie, "histNumeroAffilie"));
        statement.writeField(AFIdeAnnonce.IDE_ANNONCE_FIELD_HISTORIQUE_NOGA,
                this._dbWriteString(statement.getTransaction(), histNoga, "histNoga"));
        statement.writeField(AFIdeAnnonce.IDE_ANNONCE_FIELD_HISTORIQUE_ACTIVITE,
                this._dbWriteString(statement.getTransaction(), histActivite, "histActivite"));

    }

    public String getIdeAnnonceIdAnnonce() {
        return ideAnnonceIdAnnonce;
    }

    public void setIdeAnnonceIdAnnonce(String ideAnnonceIdAnnonce) {
        this.ideAnnonceIdAnnonce = ideAnnonceIdAnnonce;
    }

    public String getIdeAnnonceIdAffiliation() {
        return ideAnnonceIdAffiliation;
    }

    public void setIdeAnnonceIdAffiliation(String ideAnnonceIdAffiliation) {
        this.ideAnnonceIdAffiliation = ideAnnonceIdAffiliation;
    }

    public String getIdeAnnonceIdCotisation() {
        return ideAnnonceIdCotisation;
    }

    public void setIdeAnnonceIdCotisation(String ideAnnonceIdCotisation) {
        this.ideAnnonceIdCotisation = ideAnnonceIdCotisation;
    }

    public String getIdeAnnonceDateCreation() {
        return ideAnnonceDateCreation;
    }

    public void setIdeAnnonceDateCreation(String ideAnnonceDateCreation) {
        this.ideAnnonceDateCreation = ideAnnonceDateCreation;
    }

    public String getIdeAnnonceDateTraitement() {
        return ideAnnonceDateTraitement;
    }

    public void setIdeAnnonceDateTraitement(String ideAnnonceDateTraitement) {
        this.ideAnnonceDateTraitement = ideAnnonceDateTraitement;
    }

    public String getIdeAnnonceCategorie() {
        return ideAnnonceCategorie;
    }

    public void setIdeAnnonceCategorie(String ideAnnonceCategorie) {
        this.ideAnnonceCategorie = ideAnnonceCategorie;
    }

    public String getIdeAnnonceType() {
        return ideAnnonceType;
    }

    public void setIdeAnnonceType(String ideAnnonceType) {
        this.ideAnnonceType = ideAnnonceType;
    }

    public String getIdeAnnonceEtat() {
        return ideAnnonceEtat;
    }

    public void setIdeAnnonceEtat(String ideAnnonceEtat) {
        this.ideAnnonceEtat = ideAnnonceEtat;
    }

    public String getTypeAnnonceDate() {
        if (JadeStringUtil.isBlankOrZero(typeAnnonceDate)) {
            return JACalendar.todayJJsMMsAAAA();
        }
        return typeAnnonceDate;
    }

    public void setTypeAnnonceDate(String typeAnnonceDate) {
        this.typeAnnonceDate = typeAnnonceDate;
    }

    public String getHistTypeAnnonceDate() {
        return histTypeAnnonceDate;
    }

    public void setHistTypeAnnonceDate(String histTypeAnnonceDate) {
        this.histTypeAnnonceDate = histTypeAnnonceDate;
    }

    public String getHistNaissance() {
        return histNaissance;
    }

    public void setHistNaissance(String histNaissance) {
        this.histNaissance = histNaissance;
    }

    public String getHistActivite() {
        return histActivite;
    }

    public void setHistActivite(String histActivite) {
        this.histActivite = histActivite;
    }

    public String getHistNoga() {
        return histNoga;
    }

    public void setHistNoga(String histNoga) {
        this.histNoga = histNoga;
    }

    public String getHistNumeroAffilie() {
        return histNumeroAffilie;
    }

    public void setHistNumeroAffilie(String histNumeroAffilie) {
        this.histNumeroAffilie = histNumeroAffilie;
    }

}
