package globaz.pavo.db.bta;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;

public class CIRequerantBta extends BEntity implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // Lien de parenté
    public final static String CS_LIEN_ARRIEREGRANDPARENT = "333000";
    public final static String CS_LIEN_AUTRE = "333009";
    public final static String CS_LIEN_BEAUPARENT = "333003";
    public final static String CS_LIEN_CONJOINT = "333004";
    public final static String CS_LIEN_ENFANT = "333006";
    public final static String CS_LIEN_ENFANTAUTRELIT = "333007";
    public final static String CS_LIEN_FRERESOEUR = "333005";
    public final static String CS_LIEN_GRANDPARENT = "333001";
    public final static String CS_LIEN_PARENT = "333002";
    public final static String CS_LIEN_PETITENFANT = "333008";
    public final static String CS_TYPE_AUTRE = "334002";
    public final static String CS_TYPE_CONJOINT = "334001";

    // CODE SYSTEME
    // Type de requérant
    public final static String CS_TYPE_PRINCIPAL = "334000";
    private String dateDebut = new String();
    private String dateFin = new String();
    // conjoint,indique l'id
    // du conjoint
    private String dateInscriptionRetroFlag = new String();
    private String dateNaissanceCadet = new String();

    private String dateNaissanceRequerant = new String();

    // pour le viewBean et le rc_list
    String demandeRequerant = "";
    private Boolean hasEnfant = new Boolean(false);
    private String idConjointRequerant = new String();// si requerant de type

    private String idDossierBta = new String();// clé étrangère
    private String idRequerant = new String();// clé primaire
    private String idTiersRequerant = new String();// clé étrangère
    private Boolean isMenageCommun = new Boolean(false);
    private String lienParente = new String();
    private String nomRequerant = new String();
    // pour le résultat de la jointure (pour le _rc)
    private String numeroNnssRequerant = new String();
    private String prenomRequerant = new String();
    private String sexeRequerant = new String();

    private String typeRequerant = new String();

    @Override
    protected void _beforeAdd(BTransaction transaction) throws java.lang.Exception {
        // incrémente de +1 l'id du dossier
        if (JadeStringUtil.isBlank(getIdRequerant())) {
            setIdRequerant(this._incCounter(transaction, "0"));
        }
    }

    @Override
    protected String _getTableName() {
        return "CIBTARP";
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idRequerant = statement.dbReadNumeric("KDIDRE");
        idTiersRequerant = statement.dbReadNumeric("HTITIE");
        idDossierBta = statement.dbReadNumeric("KDIDD");
        dateDebut = statement.dbReadDateAMJ("KDDATD");
        dateFin = statement.dbReadDateAMJ("KDDATF");
        hasEnfant = statement.dbReadBoolean("KDENFA");
        dateNaissanceCadet = statement.dbReadDateAMJ("KDDATC");
        lienParente = statement.dbReadNumeric("KDPARE");
        isMenageCommun = statement.dbReadBoolean("KDMENA");
        typeRequerant = statement.dbReadNumeric("KDTYPR");
        idConjointRequerant = statement.dbReadNumeric("KDIDCO");
        dateInscriptionRetroFlag = statement.dbReadDateAMJ("KDREFL");

        // Pour la lecture des autres colonnes de la table résultant de la
        // jointure
        numeroNnssRequerant = statement.dbReadString("HXNAVS");
        nomRequerant = statement.dbReadString("HTLDE1");
        prenomRequerant = statement.dbReadString("HTLDE2");
        sexeRequerant = statement.dbReadNumeric("HPTSEX");
        dateNaissanceRequerant = statement.dbReadDateAMJ("HPDNAI");
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        if (JadeStringUtil.isBlank(getIdTiersRequerant())) {
            _addError(statement.getTransaction(), getSession().getLabel("MSG_AUCUN_TIERS"));
        }
        if (JadeStringUtil.isBlank(getIdRequerant())) {
            _addError(statement.getTransaction(), getSession().getLabel("MSG_ERREUR_ID_DOSSIER_BTA"));
        }
        if (JadeStringUtil.isBlank(getIdDossierBta())) {
            _addError(statement.getTransaction(), getSession().getLabel("MSG_ERREUR_ID_REQUERANT_BTA"));
        }
        if (JadeStringUtil.isBlank((getTypeRequerant()))) {
            _addError(statement.getTransaction(), getSession().getLabel("MSG_AUCUN_TYPE_REQUERANT"));
        }
        if (JadeStringUtil.isBlank(getLienParente()) && !getTypeRequerant().equals(CIRequerantBta.CS_TYPE_CONJOINT)) {
            _addError(statement.getTransaction(), getSession().getLabel("MSG_AUCUN_LIEN_PARENTE"));
        }
        /*
         * if(getHasEnfant().booleanValue() && JadeStringUtil.isBlank((getDateNaissanceCadet()))){
         * _addError(statement.getTransaction(), getSession().getLabel("MSG_AUCUNE_DATE_NAI_CADET")); }
         */
        if (getTypeRequerant().equals(CIRequerantBta.CS_TYPE_CONJOINT)
                && JadeStringUtil.isBlank(getIdConjointRequerant())) {
            _addError(statement.getTransaction(), getSession().getLabel("MSG_AUCUN_CONJOINT"));
        }
        if (JadeStringUtil.isBlank(getDemandeRequerant())) {
            _addError(statement.getTransaction(), getSession().getLabel("MSG_ACCEPTER_REFUSER_DOSSIER"));
        }
        if (JadeStringUtil.isBlank(getDateDebut())) {
            _addError(statement.getTransaction(), getSession().getLabel("MSG_AUCUN_DATE_DEBUT"));
        }

        // si un requerant du dossier est déjà flagé (c-a-d : inscription CI
        // déjà passées) alors
        // dateDebut > dateFlag
        // Récupération des requérants du dossier
        CIRequerantBtaManager requerantManager = new CIRequerantBtaManager();
        requerantManager.setSession(getSession());
        requerantManager.setForIdDossierBta(idDossierBta);
        requerantManager.find();
        int anneeFlagMax = 0;
        for (int i = 0; i < requerantManager.size(); i++) {
            CIRequerantBta requerant = (CIRequerantBta) requerantManager.getEntity(i);
            if (!JadeStringUtil.isBlank(requerant.getDateInscriptionRetroFlag())) {
                int anneeFlag = JADate.getYear(requerant.getDateInscriptionRetroFlag()).intValue();
                if (anneeFlag > anneeFlagMax) {
                    anneeFlagMax = anneeFlag;
                }
            }
        }
        if (JadeStringUtil.isBlank(getDateInscriptionRetroFlag()) && (anneeFlagMax != 0)
                && (JADate.getYear(getDateDebut()).intValue() != 1111)
                && (JADate.getYear(getDateDebut()).intValue() <= anneeFlagMax)) {
            _addError(statement.getTransaction(), getSession().getLabel("MSG_BTA_INSCRIPTION_CI_EXISTE") + " "
                    + anneeFlagMax + getSession().getLabel("MSG_BTA_INSCRIPTION_CI_EXISTE_SUITE"));
        }

    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(_getCollection() + _getTableName() + ".KDIDRE",
                this._dbWriteNumeric(statement.getTransaction(), getIdRequerant(), ""));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("KDIDRE",
                this._dbWriteNumeric(statement.getTransaction(), getIdRequerant(), "idRequerant"));
        statement.writeField("HTITIE",
                this._dbWriteNumeric(statement.getTransaction(), getIdTiersRequerant(), "idTiersRequerant"));
        statement.writeField("KDIDD",
                this._dbWriteNumeric(statement.getTransaction(), getIdDossierBta(), "idDossierBta"));
        statement.writeField("KDDATD", this._dbWriteDateAMJ(statement.getTransaction(), getDateDebut(), "dateDebut"));
        statement.writeField("KDDATF", this._dbWriteDateAMJ(statement.getTransaction(), getDateFin(), "dateFin"));
        statement.writeField("KDENFA", this._dbWriteBoolean(statement.getTransaction(), getHasEnfant(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "hasEnfant"));
        statement.writeField("KDDATC",
                this._dbWriteDateAMJ(statement.getTransaction(), getDateNaissanceCadet(), "dateNaissanceCadet"));
        statement.writeField("KDPARE",
                this._dbWriteNumeric(statement.getTransaction(), getLienParente(), "lienParente"));
        statement.writeField("KDMENA", this._dbWriteBoolean(statement.getTransaction(), getIsMenageCommun(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "isMenageCommun"));
        statement.writeField("KDTYPR",
                this._dbWriteNumeric(statement.getTransaction(), getTypeRequerant(), "typeRequerant"));
        statement.writeField("KDIDCO",
                this._dbWriteNumeric(statement.getTransaction(), getIdConjointRequerant(), "idConjointRequerant"));
        statement.writeField("KDREFL", this._dbWriteDateAMJ(statement.getTransaction(), getDateInscriptionRetroFlag(),
                "dateInscriptionRetroFlag"));
    }

    public String getAnneeDebut() {
        String dateDebut = getDateDebut();
        String annee = "";
        if (!JadeStringUtil.isEmpty(dateDebut) && (dateDebut.length() == 10) && !dateDebut.substring(6).equals("1111")) {
            annee = dateDebut.substring(6);
        }

        return annee;
    }

    public String getAnneeFin() {
        String dateFin = getDateFin();
        String annee = "";
        if (!JadeStringUtil.isEmpty(dateFin) && (dateFin.length() == 10) && !dateFin.substring(6).equals("1111")) {
            annee = dateFin.substring(6);
        }

        return annee;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public String getDateInscriptionRetroFlag() {
        return dateInscriptionRetroFlag;
    }

    public String getDateNaissanceCadet() {
        return dateNaissanceCadet;
    }

    public String getDateNaissanceRequerant() {
        return dateNaissanceRequerant;
    }

    public String getDemandeRequerant() {
        return demandeRequerant;
    }

    public Boolean getHasEnfant() {
        return hasEnfant;
    }

    public String getIdConjointRequerant() {
        return idConjointRequerant;
    }

    public String getIdDossierBta() {
        return idDossierBta;
    }

    public String getIdRequerant() {
        return idRequerant;
    }

    public String getIdTiersRequerant() {
        return idTiersRequerant;
    }

    public Boolean getIsMenageCommun() {
        return isMenageCommun;
    }

    public String getLienParente() {
        return lienParente;
    }

    public String getNomRequerant() {
        return nomRequerant;
    }

    public String getNumeroNnssRequerant() {
        return numeroNnssRequerant;
    }

    public String getPrenomRequerant() {
        return prenomRequerant;
    }

    public String getSexeRequerant() {
        return sexeRequerant;
    }

    public String getTypeRequerant() {
        return typeRequerant;
    }

    public void setAnneeDebut(String annee) {
        if (demandeRequerant.equals("refused")) {
            annee = "1111";
            setDateDebut("01.01." + annee);
        } else {
            if (!JadeStringUtil.isEmpty(annee)) {
                setDateDebut("01.01." + annee);
            } else {
                setDateDebut("");
            }
        }
    }

    public void setAnneeFin(String annee) {
        if (demandeRequerant.equals("refused")) {
            annee = "1111";
            setDateFin("01.01." + annee);
        } else {
            if (!JadeStringUtil.isEmpty(annee)) {
                setDateFin("31.12." + annee);
            } else {
                setDateFin("");
            }
        }
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setDateInscriptionRetroFlag(String dateInscriptionRetroFlag) {
        this.dateInscriptionRetroFlag = dateInscriptionRetroFlag;
    }

    public void setDateNaissanceCadet(String dateNaissanceCadet) {
        this.dateNaissanceCadet = dateNaissanceCadet;
    }

    public void setDateNaissanceRequerant(String dateNaissanceRequerant) {
        this.dateNaissanceRequerant = dateNaissanceRequerant;
    }

    public void setDemandeRequerant(String demandeRequerant) {
        this.demandeRequerant = demandeRequerant;
    }

    public void setHasEnfant(Boolean hasEnfant) {
        this.hasEnfant = hasEnfant;
    }

    public void setIdConjointRequerant(String idConjointRequerant) {
        this.idConjointRequerant = idConjointRequerant;
    }

    public void setIdDossierBta(String idDossierBta) {
        this.idDossierBta = idDossierBta;
    }

    public void setIdRequerant(String idRequerant) {
        this.idRequerant = idRequerant;
    }

    public void setIdTiersRequerant(String idTiersRequerant) {
        this.idTiersRequerant = idTiersRequerant;
    }

    public void setIsMenageCommun(Boolean isMenageCommun) {
        this.isMenageCommun = isMenageCommun;
    }

    public void setLienParente(String lienParente) {
        this.lienParente = lienParente;
    }

    public void setNomRequerant(String nomRequerant) {
        this.nomRequerant = nomRequerant;
    }

    public void setNumeroNnssRequerant(String numeroNnssRequerant) {
        this.numeroNnssRequerant = numeroNnssRequerant;
    }

    public void setPrenomRequerant(String prenomRequerant) {
        this.prenomRequerant = prenomRequerant;
    }

    public void setSexeRequerant(String sexeRequerant) {
        this.sexeRequerant = sexeRequerant;
    }

    public void setTypeRequerant(String typeRequerant) {
        this.typeRequerant = typeRequerant;
    }

}
