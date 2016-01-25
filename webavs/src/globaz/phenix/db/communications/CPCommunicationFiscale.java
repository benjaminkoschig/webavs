package globaz.phenix.db.communications;

import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.application.AFApplication;
import globaz.naos.db.affiliation.AFAffiliation;

public class CPCommunicationFiscale extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static int AK_ID_MESSAGE_SEDEX = 2;
    /** Clé alternée sur l'ifd et l'idTiers */
    public final static int AK_IFD_IDTIERS = 1;
    public final static String CS_ANNEEDECISION = "608003";
    public final static String CS_NOM = "608002";
    // Constantes
    public final static String CS_NUMAFFILIE = "608001";
    public final static String CS_NUMIFD = "608004";
    private java.lang.String anneePrise = "";
    private java.lang.String canton = "";
    private java.lang.String dateComptabilisation = "";
    private java.lang.String dateEnvoi = "";
    private java.lang.String dateEnvoiAnnulation = "";
    private java.lang.String dateRetour = "";
    private Boolean demandeAnnulee = new Boolean(false);
    private java.lang.String genreAffilie = "";
    private java.lang.String idAffiliation = "";
    private java.lang.String idCaisse = "";
    /** Fichier CPCOCFP */
    private java.lang.String idCommunication = "";
    private java.lang.String idIfd = "";
    private String idMessageSedex = "";
    private java.lang.String idTiers = "";
    private String numAffilie = "";
    private java.lang.String orderscope = "";

    @Override
    protected void _afterDelete(BTransaction transaction) {
        try {
            // Données du calcul
            CPLienSedexCommunicationFiscaleManager mng = new CPLienSedexCommunicationFiscaleManager();
            mng.setSession(getSession());
            mng.setForIdCommunication(getIdCommunication());
            mng.find();
            for (int i = 0; i < mng.size(); i++) {
                CPLienSedexCommunicationFiscale entity = ((CPLienSedexCommunicationFiscale) mng.getEntity(i));
                // Test si il y a un rejet pour cette communication
                // si oui suppression impossible => supprimer d'abord le rejet
                CPRejetsManager rejet = new CPRejetsManager();
                rejet.setSession(getSession());
                rejet.setForReferenceMessageId(entity.getIdMessageSedex());
                rejet.find();
                if (rejet.getSize() == 0) {
                    entity.delete(transaction);
                }
            }
        } catch (Exception e) {
            _addError(transaction, getSession().getLabel("CP_MSG_0026") + " " + e.getMessage());
        }

    }

    /*
     * Traitement avant ajout
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // incrémente de +1 le numéro
        setIdCommunication(this._incCounter(transaction, idCommunication));
        // Il ne peut y avoir qu'une communication active pour le même tiers, la
        // même année
        if (Boolean.FALSE.equals(getDemandeAnnulee())) {
            // chargement affiliation
            AFAffiliation aff = new AFAffiliation();
            aff.setSession(getSession());
            aff.setAffiliationId(getIdAffiliation());
            aff.retrieve(transaction);
            if (!aff.isNew()) {
                CPCommunicationFiscaleAffichageManager comFisMan = new CPCommunicationFiscaleAffichageManager();
                comFisMan.setSession(getSession());
                comFisMan.setForIdTiers(getIdTiers());
                comFisMan.setForIdIfd(getIdIfd());
                comFisMan.setForNumAffilie(aff.getAffilieNumero());
                comFisMan.setWithAnneeEnCours(Boolean.TRUE);
                comFisMan.find(transaction);
                boolean recherche = true;
                for (int i = 0; (i < comFisMan.getSize()) && recherche; i++) {
                    CPCommunicationFiscaleAffichage cf = (CPCommunicationFiscaleAffichage) comFisMan.getEntity(i);
                    if (JadeStringUtil.isBlankOrZero(cf.getDateRetour())
                            && !cf.getIdCommunication().equalsIgnoreCase(getIdCommunication())
                            && JadeStringUtil.isBlankOrZero(cf.getDateComptabilisation())
                            && cf.getDemandeAnnulee().equals(Boolean.FALSE)) {
                        recherche = false;
                        _addError(transaction, getSession().getLabel("COMMFISCAL_ERROR_PLUSIEURS_ACTIVES"));
                    }
                }
            } else {
                // Problème de chargement de l'affiliation
                _addError(transaction, getSession().getLabel("CP_MSG_0050"));
            }
        }
    }

    @Override
    protected void _beforeUpdate(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // incrémente de +1 le numéro
        setIdCommunication(this._incCounter(transaction, idCommunication));
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CPCOFIP";
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        anneePrise = statement.dbReadString("IBAPRI");
        orderscope = statement.dbReadString("IBCANO");
        dateEnvoi = statement.dbReadDateAMJ("IBDENV");
        dateRetour = statement.dbReadDateAMJ("IBDRET");
        dateComptabilisation = statement.dbReadDateAMJ("IBDCPT");
        genreAffilie = statement.dbReadNumeric("IBTGAF");
        idCaisse = statement.dbReadNumeric("IBICAI");
        idCommunication = statement.dbReadNumeric("IBIDCF");
        idIfd = statement.dbReadNumeric("ICIIFD");
        idTiers = statement.dbReadNumeric("HTITIE");
        idAffiliation = statement.dbReadNumeric("MAIAFF");
        demandeAnnulee = statement.dbReadBoolean("IBCSUS");
        dateEnvoiAnnulation = statement.dbReadDateAMJ("IBDANN");
        canton = statement.dbReadNumeric("IBTCAN");
        idMessageSedex = statement.dbReadString("IBMEID");
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {
        // Test si idTiers
        if (JadeStringUtil.isEmpty(idTiers)) {
            _addError(statement.getTransaction(), getSession().getLabel("COMMFISCAL_ERROR_TIERS_NOT_FOUND"));
        }
        // Tets suivant enlevé pour les cas qui reçoivent pour le même cas un
        // correctif... HNA le 21.05.08
        // La date de retour ne doit être infèrieure à celle d'envoi si elle est
        // renseignée.
        // if (!JAUtil.isDateEmpty(getDateComptabilisation())) {
        // try {
        // if (!BSessionUtil.compareDateFirstLowerOrEqual(getSession(),
        // getDateEnvoi(), getDateComptabilisation())) {
        // _addError(statement.getTransaction(),
        // getSession().getLabel("COMMFISCAL_ERROR_DATE_RETOUR"));
        // }
        // } catch (Exception e) {
        // _addError(statement.getTransaction(), e.getMessage());
        // }
        // }
        // Le numero IFD ne doit pas être vide
        if (JadeStringUtil.isBlank(idIfd) || JadeStringUtil.isEmpty(idIfd)) {
            _addError(statement.getTransaction(), getSession().getLabel("COMMFISCAL_ERROR_IFD"));
        }

    }

    /**
     * Sauvegarde les valeurs des propriétés propres de l'entité composant une clé alternée
     * 
     * @exception java.lang.Exception
     *                si la sauvegarde des propriétés a échouée
     * @param alternateKey
     *            int le numéro de la clé alternée à utiliser
     */
    @Override
    protected void _writeAlternateKey(globaz.globall.db.BStatement statement, int alternateKey) throws Exception {
        if (alternateKey == 1) {
            statement.writeKey("HTITIE", this._dbWriteNumeric(statement.getTransaction(), getIdTiers(), ""));
            statement.writeKey("ICIIFD", this._dbWriteNumeric(statement.getTransaction(), getIdIfd(), ""));
            statement.writeKey("MAIAFF", this._dbWriteNumeric(statement.getTransaction(), getIdAffiliation(), ""));
            statement.writeKey("IBDRET", this._dbWriteDateAMJ(statement.getTransaction(), getDateRetour(), ""));
            statement.writeKey("IBCSUS", this._dbWriteBoolean(statement.getTransaction(), getDemandeAnnulee(),
                    BConstants.DB_TYPE_BOOLEAN_CHAR, "demandeAnnulee"));
        } else if (alternateKey == 2) {
            statement.writeKey("IBMEID", this._dbWriteString(statement.getTransaction(), getIdMessageSedex(), ""));
        }
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey("IBIDCF", this._dbWriteNumeric(statement.getTransaction(), getIdCommunication(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField("IBAPRI", this._dbWriteString(statement.getTransaction(), getAnneePrise(), "anneePrise"));
        statement.writeField("IBCANO", this._dbWriteString(statement.getTransaction(), getOrderscope(), "orderscope"));
        statement.writeField("IBDENV", this._dbWriteDateAMJ(statement.getTransaction(), getDateEnvoi(), "dateEnvoi"));
        statement.writeField("IBDRET", this._dbWriteDateAMJ(statement.getTransaction(), getDateRetour(), "dateRetour"));
        statement.writeField("IBDCPT",
                this._dbWriteDateAMJ(statement.getTransaction(), getDateComptabilisation(), "dateComptabilisation"));
        statement.writeField("IBTGAF",
                this._dbWriteNumeric(statement.getTransaction(), getGenreAffilie(), "genreAffilie"));
        statement.writeField("IBICAI", this._dbWriteNumeric(statement.getTransaction(), getIdCaisse(), "idCaisse"));
        statement.writeField("IBIDCF",
                this._dbWriteNumeric(statement.getTransaction(), getIdCommunication(), "idCommunication"));
        statement.writeField("ICIIFD", this._dbWriteNumeric(statement.getTransaction(), getIdIfd(), "idIfd"));
        statement.writeField("HTITIE", this._dbWriteNumeric(statement.getTransaction(), getIdTiers(), "idTiers"));
        statement.writeField("MAIAFF",
                this._dbWriteNumeric(statement.getTransaction(), getIdAffiliation(), "idAffiliation"));
        statement.writeField("IBCSUS", this._dbWriteBoolean(statement.getTransaction(), getDemandeAnnulee(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "demandeAnnulee"));
        statement.writeField("IBTCAN", this._dbWriteNumeric(statement.getTransaction(), getCanton(), "canton"));
        statement.writeField("IBDANN",
                this._dbWriteDateAMJ(statement.getTransaction(), getDateEnvoiAnnulation(), "dateEnvoiAnnulation"));
        statement.writeField("IBMEID",
                this._dbWriteString(statement.getTransaction(), getIdMessageSedex(), "idMessageSedex"));
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.03.2003 14:25:49)
     * 
     * @return java.lang.String
     */
    public java.lang.String getAnneePrise() {
        return anneePrise;
    }

    /**
     * @return
     */
    public java.lang.String getCanton() {
        return canton;
    }

    public java.lang.String getCodeAnomalie() {
        return orderscope;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.03.2003 14:23:34)
     * 
     * @return java.lang.String
     */
    public CPCommunicationFiscale getCommunicationFiscaleEnvoyee(int modeRecherche) {
        try {
            CPCommunicationFiscaleAffichage cf = null;
            CPCommunicationFiscaleAffichageManager comMng = new CPCommunicationFiscaleAffichageManager();
            comMng.setSession(getSession());
            if (modeRecherche != 1) {
                comMng.setDateEnvoiNonVide(Boolean.TRUE);
            }
            comMng.setDemandeAnnuleeOuPas(Boolean.TRUE); // PO 2976
            comMng.setForIdTiers(getIdTiers());
            comMng.setForIdIfd(getIdIfd());
            comMng.setWithAnneeEnCours(Boolean.TRUE);
            comMng.setForNumAffilie(getNumAffilie());
            comMng.orderByIdCommunicationDesc();
            comMng.changeManagerSize(1);
            comMng.find();
            if (comMng.size() > 0) {
                cf = (CPCommunicationFiscaleAffichage) comMng.getFirstEntity();
            }
            if (modeRecherche == 1) {
                boolean recherche = true;
                for (int i = 0; (i < comMng.getSize()) && recherche; i++) {
                    CPCommunicationFiscaleAffichage com = (CPCommunicationFiscaleAffichage) comMng.getEntity(i);
                    if (JadeStringUtil.isBlankOrZero(com.getDateComptabilisation())
                            && com.getDemandeAnnulee().equals(Boolean.FALSE)) {
                        recherche = false;
                        cf = com;
                    }
                }
            }
            if (cf != null) {
                CPCommunicationFiscale communicationFiscale = new CPCommunicationFiscale();
                communicationFiscale.setSession(getSession());
                communicationFiscale.setIdCommunication(cf.getIdCommunication());
                communicationFiscale.retrieve();
                return communicationFiscale;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.03.2003 14:24:31)
     * 
     * @return java.lang.String
     */
    public java.lang.String getDateComptabilisation() {
        return dateComptabilisation;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.03.2003 14:24:11)
     * 
     * @return java.lang.String
     */
    public java.lang.String getDateEnvoi() {
        return dateEnvoi;
    }

    public java.lang.String getDateEnvoiAnnulation() {
        return dateEnvoiAnnulation;
    }

    public java.lang.String getDateRetour() {
        return dateRetour;
    }

    /**
     * @return
     */
    public Boolean getDemandeAnnulee() {
        return demandeAnnulee;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.03.2003 14:25:35)
     * 
     * @return java.lang.String
     */
    public java.lang.String getGenreAffilie() {
        return genreAffilie;
    }

    /**
     * @return
     */
    public java.lang.String getIdAffiliation() {
        return idAffiliation;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.03.2003 14:23:14)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdCaisse() {
        return idCaisse;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.03.2003 14:21:08)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdCommunication() {
        return idCommunication;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.03.2003 14:22:04)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdIfd() {
        return idIfd;
    }

    public String getIdMessageSedex() {
        return idMessageSedex;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.03.2003 14:22:41)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdTiers() {
        return idTiers;
    }

    public String getNumAffilie() {
        if (JadeStringUtil.isEmpty(numAffilie)) {
            try {
                BSession sessionNaos = ((BSession) GlobazSystem.getApplication(AFApplication.DEFAULT_APPLICATION_NAOS)
                        .newSession(getSession()));
                AFAffiliation aff = new AFAffiliation();
                aff.setSession(sessionNaos);
                aff.setAffiliationId(getIdAffiliation());

                aff.retrieve();
                if (!aff.isNew()) {
                    numAffilie = aff.getAffilieNumero();
                }
            } catch (Exception e) {
                return "";
            }
        }
        return numAffilie;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.03.2003 14:24:55)
     * 
     * @return java.lang.String
     */
    public java.lang.String getOrderscope() {
        return orderscope;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.03.2003 14:25:49)
     * 
     * @param newAnneePrise
     *            java.lang.String
     */
    public void setAnneePrise(java.lang.String newAnneePrise) {
        anneePrise = newAnneePrise;
    }

    /**
     * @param string
     */
    public void setCanton(java.lang.String string) {
        canton = string;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.03.2003 14:24:31)
     * 
     * @param newDateRetour
     *            java.lang.String
     */
    public void setDateComptabilisation(java.lang.String newDateRetour) {
        dateComptabilisation = newDateRetour;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.03.2003 14:24:11)
     * 
     * @param newDateEnvoi
     *            java.lang.String
     */
    public void setDateEnvoi(java.lang.String newDateEnvoi) {
        dateEnvoi = newDateEnvoi;
    }

    public void setDateEnvoiAnnulation(java.lang.String dateEnvoiAnnulation) {
        this.dateEnvoiAnnulation = dateEnvoiAnnulation;
    }

    public void setDateRetour(java.lang.String dateRetour) {
        this.dateRetour = dateRetour;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.03.2003 14:26:33)
     * 
     * @param newSuspendEnvoi
     *            boolean
     */
    public void setDemandeAnnulee(Boolean newSuspendEnvoi) {
        demandeAnnulee = newSuspendEnvoi;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.03.2003 14:25:35)
     * 
     * @param newGenreAffilie
     *            java.lang.String
     */
    public void setGenreAffilie(java.lang.String newGenreAffilie) {
        genreAffilie = newGenreAffilie;
    }

    /**
     * @param string
     */
    public void setIdAffiliation(java.lang.String string) {
        idAffiliation = string;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.03.2003 14:23:14)
     * 
     * @param newIdCaisse
     *            java.lang.String
     */
    public void setIdCaisse(java.lang.String newIdCaisse) {
        idCaisse = newIdCaisse;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.03.2003 14:21:08)
     * 
     * @param newIdCommunication
     *            java.lang.String
     */
    public void setIdCommunication(java.lang.String newIdCommunication) {
        idCommunication = newIdCommunication;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.03.2003 14:22:04)
     * 
     * @param newIdIfd
     *            java.lang.String
     */
    public void setIdIfd(java.lang.String newIdIfd) {
        idIfd = newIdIfd;
    }

    public void setIdMessageSedex(String idMessageSedex) {
        this.idMessageSedex = idMessageSedex;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.03.2003 14:22:41)
     * 
     * @param newIdTiers
     *            java.lang.String
     */
    public void setIdTiers(java.lang.String newIdTiers) {
        idTiers = newIdTiers;
    }

    public void setNumAffilie(String numAffilie) {
        this.numAffilie = numAffilie;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.03.2003 14:24:55)
     * 
     * @param neworderscope
     *            java.lang.String
     */
    public void setOrderscope(java.lang.String neworderscope) {
        orderscope = neworderscope;
    }

}
