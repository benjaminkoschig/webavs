package globaz.pavo.db.bta;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pyxis.db.tiers.TIPersonneAvsManager;
import globaz.pyxis.db.tiers.TITiersViewBean;

public class CIDossierBta extends BEntity implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String CS_ETAT_ATRAITER = "332004";
    public final static String CS_ETAT_CLOTURE = "332003";
    public final static String CS_ETAT_OUVERT = "332001";
    public final static String CS_ETAT_REFUSE = "332002";
    // CODE SYSTEME
    public final static String CS_ETAT_SAISIE = "332000";
    String dateFinDossier = new String();
    String dateNaissanceImpotent = "";

    String dateReceptionDossier = new String();
    String etatDossier = new String();
    // attributs de la table CIBTADP (dossier BTA)
    String idDossierBta = new String();
    String idTiersImpotent = new String();
    String motifFin = new String();

    String nomImpotent = "";
    String nomRequerant = "";

    String numeroInterneDossier = new String();

    // pour le résultat de la jointure (pour le _rcListe)
    String numeroNnssImpotent = "";
    String prenomImpotent = "";
    String prenomRequerant = "";
    String sexeImpotent = "";
    // informations sur le tiers
    private TITiersViewBean tiersViewBean = null;

    @Override
    protected void _beforeAdd(BTransaction transaction) throws java.lang.Exception {
        // incrémente de +1 l'id du dossier
        if (JadeStringUtil.isBlank(getIdDossierBta())) {
            setIdDossierBta(_incCounter(transaction, "0"));
        }
    }

    @Override
    protected String _getTableName() {
        return "CIBTADP";
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idDossierBta = statement.dbReadNumeric("KDIDD");
        idTiersImpotent = statement.dbReadNumeric("HTITIE");
        numeroInterneDossier = statement.dbReadString("KDIDIN");
        dateReceptionDossier = statement.dbReadDateAMJ("KDDATR");
        dateFinDossier = statement.dbReadDateAMJ("KDDATF");
        motifFin = statement.dbReadNumeric("KDMOTF");
        etatDossier = statement.dbReadNumeric("KDETAP");

        // Pour la lecture des autres colonnes de la table résultant de la
        // jointure
        numeroNnssImpotent = statement.dbReadString("HXNAVS");
        nomImpotent = statement.dbReadString("HTLDE1");
        prenomImpotent = statement.dbReadString("HTLDE2");
        sexeImpotent = statement.dbReadNumeric("HPTSEX");
        dateNaissanceImpotent = statement.dbReadDateAMJ("HPDNAI");
        nomRequerant = statement.dbReadString("HTLDE11");// renommé ainsi avec
        // "as" dans la
        // requete
        prenomRequerant = statement.dbReadString("HTLDE21");// renommé ainsi
        // avec "as" dans la
        // requete
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        if (JadeStringUtil.isBlank((getIdDossierBta()))) {
            _addError(statement.getTransaction(), getSession().getLabel("MSG_ERREUR_ID_DOSSIER_BTA"));
        }
        if (JadeStringUtil.isBlank(getIdTiersImpotent())) {
            _addError(statement.getTransaction(), getSession().getLabel("MSG_AUCUN_TIERS"));
        }
        if (JadeStringUtil.isBlank(getDateReceptionDossier())) {
            _addError(statement.getTransaction(), getSession().getLabel("MSG_AUCUNE_DATE_REC"));
        }
        if (!JadeStringUtil.isBlank(getDateFinDossier())
                && (!getEtatDossier().equals(CS_ETAT_CLOTURE) && !getEtatDossier().equals(CS_ETAT_REFUSE))) {
            _addError(statement.getTransaction(), getSession().getLabel("MSG_ERREUR_FIN_CLOTURE"));
        }
        if ((getEtatDossier().equals(CS_ETAT_CLOTURE) || getEtatDossier().equals(CS_ETAT_REFUSE))
                && JadeStringUtil.isBlank(getDateFinDossier())) {
            _addError(statement.getTransaction(), getSession().getLabel("MSG_ERREUR_CLOTURE_FIN"));
        }
        if ((getEtatDossier().equals(CS_ETAT_CLOTURE) || getEtatDossier().equals(CS_ETAT_REFUSE))
                && !JadeStringUtil.isBlank(getDateFinDossier())) {
            CIRequerantBtaManager requerants = new CIRequerantBtaManager();
            requerants.setSession(getSession());
            requerants.setForIdDossierBta(idDossierBta);
            boolean erreur = false;
            try {
                requerants.find();
                for (int i = 0; i < requerants.size(); i++) {
                    CIRequerantBta requerant = (CIRequerantBta) requerants.getEntity(i);
                    if (JadeStringUtil.isBlank(requerant.getDateFin())) {
                        erreur = true;
                    }
                }
                if (erreur) {
                    _addError(statement.getTransaction(), getSession().getLabel("MSG_ERREUR_REQUERANT_FIN"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(_getCollection() + _getTableName() + ".KDIDD",
                _dbWriteNumeric(statement.getTransaction(), getIdDossierBta(), ""));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("KDIDD", _dbWriteNumeric(statement.getTransaction(), getIdDossierBta(), "idDossierBta"));
        statement.writeField("HTITIE",
                _dbWriteNumeric(statement.getTransaction(), getIdTiersImpotent(), "idTiersImpotent"));
        statement.writeField("KDIDIN",
                _dbWriteString(statement.getTransaction(), getNumeroInterneDossier(), "numeroInterneDossier"));
        statement.writeField("KDDATR",
                _dbWriteDateAMJ(statement.getTransaction(), getDateReceptionDossier(), "dateReceptionDossier"));
        statement.writeField("KDDATF",
                _dbWriteDateAMJ(statement.getTransaction(), getDateFinDossier(), "dateFinDossier"));
        statement.writeField("KDMOTF", _dbWriteNumeric(statement.getTransaction(), getMotifFin(), "motifFin"));
        statement.writeField("KDETAP", _dbWriteNumeric(statement.getTransaction(), getEtatDossier(), "etatDossier"));
    }

    public String getDateFinDossier() {
        return dateFinDossier;
    }

    public String getDateNaissanceImpotent() {
        return dateNaissanceImpotent;
    }

    public String getDateReceptionDossier() {
        return dateReceptionDossier;
    }

    public String getEtatDossier() {
        return etatDossier;
    }

    public String getIdDossierBta() {
        return idDossierBta;
    }

    public String getIdTiersImpotent() {
        return idTiersImpotent;
    }

    public String getMotifFin() {
        return motifFin;
    }

    public String getNomImpotent() {
        return nomImpotent;
    }

    public String getNomRequerant() {
        return nomRequerant;
    }

    public String getNumeroInterneDossier() {
        return numeroInterneDossier;
    }

    public String getNumeroNnssImpotent() {
        return numeroNnssImpotent;
    }

    public String getPrenomImpotent() {
        return prenomImpotent;
    }

    public String getPrenomRequerant() {
        return prenomRequerant;
    }

    public String getSexeImpotent() {
        return sexeImpotent;
    }

    /**
     * Retourne le tiers correspondant à l'id de l'impotent
     * 
     * @return
     */
    public TITiersViewBean getTiersViewBean() {
        if (tiersViewBean == null) {
            TIPersonneAvsManager personneAvsManager = new TIPersonneAvsManager();
            personneAvsManager.setSession(getSession());
            personneAvsManager.setForIdTiers(getIdTiersImpotent());
            try {
                personneAvsManager.find();
                if (!personneAvsManager.isEmpty()) {
                    tiersViewBean = (TITiersViewBean) personneAvsManager.getFirstEntity();
                }
            } catch (Exception e) {
                _addError(null, e.getMessage());
            }
        }
        return tiersViewBean;
    }

    public void setDateFinDossier(String dateFinDossier) {
        this.dateFinDossier = dateFinDossier;
    }

    public void setDateNaissanceImpotent(String dateNaissanceImpotent) {
        this.dateNaissanceImpotent = dateNaissanceImpotent;
    }

    public void setDateReceptionDossier(String dateReception) {
        dateReceptionDossier = dateReception;
    }

    public void setEtatDossier(String etatDossier) {
        this.etatDossier = etatDossier;
    }

    public void setIdDossierBta(String idDossierBta) {
        this.idDossierBta = idDossierBta;
    }

    public void setIdTiersImpotent(String idTiersImpotent) {
        this.idTiersImpotent = idTiersImpotent;
    }

    public void setMotifFin(String motifFin) {
        this.motifFin = motifFin;
    }

    public void setNomImpotent(String nomImpotent) {
        this.nomImpotent = nomImpotent;
    }

    public void setNomRequerant(String nomRequerant) {
        this.nomRequerant = nomRequerant;
    }

    public void setNumeroInterneDossier(String numeroInterneDossier) {
        this.numeroInterneDossier = numeroInterneDossier;
    }

    public void setNumeroNnssImpotent(String numeroNnssImpotent) {
        this.numeroNnssImpotent = numeroNnssImpotent;
    }

    public void setPrenomImpotent(String prenomImpotent) {
        this.prenomImpotent = prenomImpotent;
    }

    public void setPrenomRequerant(String prenomRequerant) {
        this.prenomRequerant = prenomRequerant;
    }

    public void setSexeImpotent(String sexeImpotent) {
        this.sexeImpotent = sexeImpotent;
    }

}
