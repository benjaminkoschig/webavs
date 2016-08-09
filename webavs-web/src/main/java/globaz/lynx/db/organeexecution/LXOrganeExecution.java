package globaz.lynx.db.organeexecution;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazServer;
import globaz.jade.client.util.JadeStringUtil;
import globaz.lynx.db.ordregroupe.LXOrdreGroupeManager;
import globaz.lynx.db.societesdebitrice.LXSocieteDebitrice;
import globaz.lynx.service.tiers.LXTiersService;
import globaz.osiris.api.ordre.APIOrganeExecution;
import globaz.osiris.application.CAApplication;
import globaz.osiris.external.IntAdressePaiement;

public class LXOrganeExecution extends BEntity implements APIOrganeExecution {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String CS_GENRE_BANQUE = "7300001";
    public static final String CS_GENRE_CAISSE = "7300003";
    public static final String CS_GENRE_LSV = "7300004";
    // Code systeme GENRE (LXGENRE)
    public static final String CS_GENRE_POSTE = "7300002";
    public static final String CS_MODE_TRANSFERT_GENRE_FTP = "246002";
    // Code systeme Mode de transfert(OSIMODTRA)
    public static final String CS_MODE_TRANSFERT_MAIL = "246001";
    public static final String FIELD_CSDOMAINE = "CSDOMAINE";
    public static final String FIELD_CSGENRE = "CSGENRE";
    public static final String FIELD_CSMODETRANSFERT = "CSMODETRANSFERT";
    public static final String FIELD_IDCOMPTECREDIT = "IDCOMPTECREDIT";

    public static final String FIELD_IDENTIFIANTDTA = "IDENTIFIANTDTA";

    // Colonnes de la table
    public static final String FIELD_IDORGANEEXECUTION = "IDORGANEEXECUTION";
    public static final String FIELD_IDSOCIETE = "IDSOCIETE";
    public static final String FIELD_NOADHERENT = "NOADHERENT";
    public static final String FIELD_NOADHERENTBVR = "NOADHERENTBVR";

    public static final String FIELD_NOM = "NOM";
    // Nom de la table
    public static final String TABLE_LXOREXP = "LXOREXP";

    private IntAdressePaiement adressePaiement = null;
    private String csDomaine = "";
    private String csGenre = "";
    private String csModeTransfert = "";
    private String idCompteCredit = "";
    private String identifiantDta = "";
    private String idOrganeExecution = "";
    private String idSociete = "";
    private String nom = "";
    private String numeroAdherent = "";

    private String numeroAdherentBVR = "";
    protected LXSocieteDebitrice societe;

    /**
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdOrganeExecution(this._incCounter(transaction, getIdOrganeExecution()));
    }

    /**
     * @see globaz.globall.db.BEntity#_beforeDelete(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeDelete(BTransaction transaction) throws Exception {
        LXOrdreGroupeManager managerOrGr = new LXOrdreGroupeManager();
        managerOrGr.setSession(getSession());
        managerOrGr.setForIdOrganeExecution(getIdOrganeExecution());
        managerOrGr.find(transaction);
        if (!managerOrGr.isEmpty()) {
            _addError(transaction, getSession().getLabel("ORGANE_EXECUTION_DEPENDANCE_ORDREGROUPE"));
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return LXOrganeExecution.TABLE_LXOREXP;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        setIdOrganeExecution(statement.dbReadNumeric(LXOrganeExecution.FIELD_IDORGANEEXECUTION));
        setIdSociete(statement.dbReadNumeric(LXOrganeExecution.FIELD_IDSOCIETE));
        setNom(statement.dbReadString(LXOrganeExecution.FIELD_NOM));
        setIdCompteCredit(statement.dbReadNumeric(LXOrganeExecution.FIELD_IDCOMPTECREDIT));
        setCsGenre(statement.dbReadNumeric(LXOrganeExecution.FIELD_CSGENRE));
        setIdentifiantDta(statement.dbReadString(LXOrganeExecution.FIELD_IDENTIFIANTDTA));
        setCsModeTransfert(statement.dbReadNumeric(LXOrganeExecution.FIELD_CSMODETRANSFERT));
        setCsDomaine(statement.dbReadNumeric(LXOrganeExecution.FIELD_CSDOMAINE));
        setNumeroAdherent(statement.dbReadString(LXOrganeExecution.FIELD_NOADHERENT));
        setNumeroAdherentBVR(statement.dbReadString(LXOrganeExecution.FIELD_NOADHERENTBVR));
    }

    /**
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        // Controle de l'id societe
        if (JadeStringUtil.isIntegerEmpty(getIdSociete())) {
            _addError(statement.getTransaction(), getSession().getLabel("VAL_IDENTIFIANT_SOCIETE"));
        }
        // Controle de l'id d'un compte de créance
        if (JadeStringUtil.isIntegerEmpty(getIdCompteCredit())) {
            _addError(statement.getTransaction(), getSession().getLabel("VAL_IDENTIFIANT_COMPTE_CREDIT"));
        }
        // Controle présence du nom
        if (JadeStringUtil.isBlank(getNom())) {
            _addError(statement.getTransaction(), getSession().getLabel("VAL_NOM_ORGANE"));
        }

        if (JadeStringUtil.isBlank(getIdentifiantDta())
                && (LXOrganeExecution.CS_GENRE_POSTE.equals(getCsGenre()) || LXOrganeExecution.CS_GENRE_BANQUE
                        .equals(getCsGenre()))) {
            _addError(statement.getTransaction(), getSession().getLabel("VAL_IDENTIFIANT_DTA"));
        }
        // Controle de la présence dans tiers d'une adresse de paiement pour le
        // domaine choisi par l'utilisateur
        if (JadeStringUtil.isBlank(LXTiersService.getAdresseOrganeExecutionPaiementAsString(getSession(), null,
                getCsDomaine(), societe.getIdTiers()))) {
            _addError(statement.getTransaction(), getSession().getLabel("VAL_ORGANE_EXECUTION_ADRESSE_PAIEMENT"));
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(LXOrganeExecution.FIELD_IDORGANEEXECUTION,
                this._dbWriteNumeric(statement.getTransaction(), getIdOrganeExecution(), ""));
    }

    /**
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(LXOrganeExecution.FIELD_IDORGANEEXECUTION,
                this._dbWriteNumeric(statement.getTransaction(), getIdOrganeExecution(), "idOrganeExecution"));
        statement.writeField(LXOrganeExecution.FIELD_IDSOCIETE,
                this._dbWriteNumeric(statement.getTransaction(), getIdSociete(), "idSociete"));
        statement.writeField(LXOrganeExecution.FIELD_NOM,
                this._dbWriteString(statement.getTransaction(), getNom(), "idNom"));
        statement.writeField(LXOrganeExecution.FIELD_IDCOMPTECREDIT,
                this._dbWriteNumeric(statement.getTransaction(), getIdCompteCredit(), "idCompteCredit"));
        statement.writeField(LXOrganeExecution.FIELD_CSGENRE,
                this._dbWriteNumeric(statement.getTransaction(), getCsGenre(), "csGenre"));
        statement.writeField(LXOrganeExecution.FIELD_IDENTIFIANTDTA,
                this._dbWriteString(statement.getTransaction(), getIdentifiantDta(), "identificationDta"));
        statement.writeField(LXOrganeExecution.FIELD_CSMODETRANSFERT,
                this._dbWriteNumeric(statement.getTransaction(), getCsModeTransfert(), "csModeTransfert"));
        statement.writeField(LXOrganeExecution.FIELD_CSDOMAINE,
                this._dbWriteNumeric(statement.getTransaction(), getCsDomaine(), "csDomaine"));
        statement.writeField(LXOrganeExecution.FIELD_NOADHERENT,
                this._dbWriteString(statement.getTransaction(), getNumeroAdherent(), "numeroAdherent"));
        statement.writeField(LXOrganeExecution.FIELD_NOADHERENTBVR,
                this._dbWriteString(statement.getTransaction(), getNumeroAdherentBVR(), "numeroAdherentBVR"));
    }

    // *******************************************************
    // Getter
    // *******************************************************

    @Override
    public IntAdressePaiement getAdresseDebitTaxes() throws Exception {
        // Not used in cas comptabilité fournisseur
        return null;
    }

    @Override
    public IntAdressePaiement getAdressePaiement() throws Exception {
        String idAdressePaiement = "";

        retrieveSociete();

        try {
            idAdressePaiement = LXTiersService.getIdAdressePaiementOrganeExecution(getSession(), null, getCsDomaine(),
                    societe.getIdTiers());
        } catch (Exception e) {
            throw new Exception(getSession().getLabel("ADRESSE_PAIEMENT_ORGANE_EXECUTION"));
        }

        if (JadeStringUtil.isIntegerEmpty(idAdressePaiement)) {
            throw new Exception(getSession().getLabel("ADRESSE_PAIEMENT_ORGANE_EXECUTION"));
        }

        if (adressePaiement == null) {
            try {
                CAApplication currentApplication = CAApplication.getApplicationOsiris();
                adressePaiement = (IntAdressePaiement) GlobazServer.getCurrentSystem()
                        .getApplication(currentApplication.getCAParametres().getApplicationExterne())
                        .getImplementationFor(getSession(), IntAdressePaiement.class);
                adressePaiement.setISession(getSession());

                adressePaiement.retrieve(idAdressePaiement);

                if (adressePaiement.isNew()) {
                    throw new Exception(getSession().getLabel("ADRESSE_PAIEMENT_ORGANE_EXECUTION"));
                }
            } catch (Exception e) {
                adressePaiement = null;
                throw e;
            }
        }

        if (adressePaiement != null) {
            adressePaiement.setISession(getSession());
        }

        return adressePaiement;
    }

    public String getCsDomaine() {
        return csDomaine;
    }

    public String getCsGenre() {
        return csGenre;
    }

    public String getCsModeTransfert() {
        return csModeTransfert;
    }

    @Override
    public String getGenre() {
        if (LXOrganeExecution.CS_GENRE_POSTE.equals(getCsGenre())) {
            return APIOrganeExecution.POSTE;
        } else if (LXOrganeExecution.CS_GENRE_BANQUE.equals(getCsGenre())) {
            return APIOrganeExecution.BANQUE;
        } else {
            // Seul la poste et la banque génère un fichier de versements.
            return null;
        }
    }

    public String getIdCompteCredit() {
        return idCompteCredit;
    }

    public String getIdentifiantDta() {
        return identifiantDta;
    }

    @Override
    public String getIdentifiantDTA() {
        return getIdentifiantDta();
    }

    public String getIdOrganeExecution() {
        return idOrganeExecution;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    @Override
    public String getIdRubrique() {
        // Not used dans le cas de la comptabilité fournisseur
        return getIdCompteCredit();
    }

    public String getIdSociete() {
        return idSociete;
    }

    @Override
    public String getModeTransfert() {
        if (LXOrganeExecution.CS_MODE_TRANSFERT_GENRE_FTP.equals(getCsModeTransfert())) {
            return APIOrganeExecution.CS_BY_FTPPOST;
        } else {
            return APIOrganeExecution.CS_BY_MAIL;
        }
    }

    @Override
    public String getNoAdherent() {
        return getNumeroAdherent();
    }

    @Override
    public String getNoAdherentBVR() {
        return getNumeroAdherentBVR();
    }

    @Override
    public String getNom() {
        return nom;
    }

    public String getNumeroAdherent() {
        return numeroAdherent;
    }

    public String getNumeroAdherentBVR() {
        return numeroAdherentBVR;
    }

    @Override
    public String getNumInterneLsv() {
        // Not used dans le cas de la comptabilité fournisseur
        return null;
    }

    /**
     * Retrouve la societe si pas encore chargée.
     */
    protected void retrieveSociete() {
        if (!JadeStringUtil.isIntegerEmpty(getIdSociete()) && (societe == null)) {
            try {
                societe = new LXSocieteDebitrice();
                societe.setSession(getSession());
                societe.setIdSociete(getIdSociete());
                societe.retrieve();

                if (societe.hasErrors() || societe.isNew()) {
                    societe = null;
                    return;
                }
            } catch (Exception e) {
                // nothing
            }
        }
    }

    // *******************************************************
    // Getter pour exécution de l'ordre groupé
    // *******************************************************

    public void setCsDomaine(String csDomaine) {
        this.csDomaine = csDomaine;
    }

    public void setCsGenre(String csGenre) {
        this.csGenre = csGenre;
    }

    public void setCsModeTransfert(String csModeTransfert) {
        this.csModeTransfert = csModeTransfert;
    }

    public void setIdCompteCredit(String idCompteCredit) {
        this.idCompteCredit = idCompteCredit;
    }

    public void setIdentifiantDta(String identifiantDta) {
        this.identifiantDta = identifiantDta;
    }

    public void setIdOrganeExecution(String idOrganeExecution) {
        this.idOrganeExecution = idOrganeExecution;
    }

    public void setIdSociete(String idSociete) {
        this.idSociete = idSociete;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setNumeroAdherent(String numeroAdherent) {
        this.numeroAdherent = numeroAdherent;
    }

    public void setNumeroAdherentBVR(String numeroAdherentBVR) {
        this.numeroAdherentBVR = numeroAdherentBVR;
    }

    @Override
    public String getCSTypeTraitementOG() {
        // TODO Auto-generated method stub
        return null;
    }

}
