package globaz.phenix.db.communications;

import globaz.globall.api.BISession;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.phenix.translation.CodeSystem;
import globaz.pyxis.constantes.IConstantes;
import java.util.Vector;
import javax.servlet.http.HttpSession;

public class CPRejets extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public final static String CELIBATAIRE = "1";

    public final static String CS_ETAT_ABANDONNE = "622004";
    public final static String CS_ETAT_ENVOYE = "622003";
    /**
     * Fichier CPSEREJ
     */
    public final static String CS_ETAT_NON_TRAITE = "622001";
    public final static String CS_ETAT_TRAITE = "622002";
    public final static String FEMME = "2";
    public final static String HOMME = "1";
    public final static String MARIE = "2";
    public final static int REJET_AUTRE = 90;
    public final static int REJET_LIQUIDATION = 2;
    public final static int REJET_PARTI = 1;

    public final static int REJET_PAS_CONCERNE = 3;

    public static Vector<String[]> getListRejets(HttpSession httpSession) {
        Vector<String[]> vList = new Vector<String[]>();
        String[] element = null;
        try {
            BSession _session = CodeSystem.getSession(httpSession);
            vList = new Vector<String[]>(5);
            element = new String[2];
            element[0] = "";
            element[1] = "";
            vList.add(element);
            element = new String[2];
            element[0] = String.valueOf(CPRejets.REJET_PARTI);
            element[1] = CPRejets.getRejetLibelle(_session, CPRejets.REJET_PARTI);
            vList.add(element);
            element = new String[2];
            element[0] = String.valueOf(CPRejets.REJET_LIQUIDATION);
            element[1] = CPRejets.getRejetLibelle(_session, CPRejets.REJET_LIQUIDATION);
            vList.add(element);
            element = new String[2];
            element[0] = String.valueOf(CPRejets.REJET_PAS_CONCERNE);
            element[1] = CPRejets.getRejetLibelle(_session, CPRejets.REJET_PAS_CONCERNE);
            vList.add(element);
            element = new String[2];
            element[0] = String.valueOf(CPRejets.REJET_AUTRE);
            element[1] = CPRejets.getRejetLibelle(_session, CPRejets.REJET_AUTRE);
            vList.add(element);
        } catch (Exception e) {
            JadeLogger.error(null, e);
        }
        return vList;
    }

    public static String getRejetLibelle(BSession _session, int codeRejet) {
        return _session.getLabel("REJET_LIBELLE_" + codeRejet);
    }

    private String action = "";

    private String adresse1 = "";

    private String adresse2 = "";

    private String annee = "";

    private String dateInitialeMessage = "";

    private String dateMessage = "";

    private String dateNaissance = "";

    private String dateRenvoi = "";

    private String departementReference = "";

    private String emailReference = "";

    private String etat = "";

    private String etatCivil = "";

    private String idDemande = "";

    private String idRejets = "";

    private String localite = "";

    private String messageId = "";

    private String messagePriorite = "";

    private String nom = "";

    private String nomReference = "";

    private String numContribuable = "";

    private String ourBusinessReferenceId = "";

    private String personId = "";

    private String personIdCategory = "";

    private String prenom = "";

    private String recipientId = "";

    private String referenceMessageId = "";

    private String rejet = "";

    private String remark = "";

    private String rue = "";

    private String senderId = "";

    private String sex = "";

    private String subject = "";

    private String telephoneReference = "";

    private String testFlag = "";

    private String ville = "";

    private String yourBusinessReferenceId = "";

    public CPRejets() {
        super();
    }

    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdRejets(this._incCounter(transaction, "0"));
    }

    /**
     * Renvoie le nom de la table
     */

    @Override
    protected String _getTableName() {
        return "CPSEREJ";
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idRejets = statement.dbReadNumeric("CPSEID");
        senderId = statement.dbReadString("SENDID");
        nomReference = statement.dbReadString("SENNOM");
        departementReference = statement.dbReadString("SENDEP");
        telephoneReference = statement.dbReadString("SENPHO");
        emailReference = statement.dbReadString("SENEMA");
        recipientId = statement.dbReadString("RECIID");
        messageId = statement.dbReadString("MESSID");
        referenceMessageId = statement.dbReadString("REMEID");
        ourBusinessReferenceId = statement.dbReadString("OBREID");
        yourBusinessReferenceId = statement.dbReadString("YBREID");
        subject = statement.dbReadString("SUBJEC");
        personIdCategory = statement.dbReadString("PERIDC");
        personId = statement.dbReadString("PERSID");
        nom = statement.dbReadString("OFNAME");
        prenom = statement.dbReadString("FINAME");
        sex = statement.dbReadNumeric("PERSEX");
        dateNaissance = statement.dbReadString("DATNAI");
        adresse1 = statement.dbReadString("ADRLI1");
        adresse2 = statement.dbReadString("ADRLI2");
        rue = statement.dbReadString("STREET");
        localite = statement.dbReadString("LOCALI");
        ville = statement.dbReadString("TOWNA");
        etatCivil = statement.dbReadNumeric("MARITA");
        dateMessage = statement.dbReadString("MESDAT");
        dateInitialeMessage = statement.dbReadString("IMESDA");
        action = statement.dbReadNumeric("ACTINU");
        testFlag = statement.dbReadString("TESTFL");
        messagePriorite = statement.dbReadNumeric("MESPRI");
        rejet = statement.dbReadNumeric("REJECT");
        remark = statement.dbReadString("REMARK");
        etat = statement.dbReadNumeric("STATUS");
        annee = statement.dbReadNumeric("ICANDD");
        if (JadeStringUtil.isBlankOrZero(annee)) {
            annee = statement.dbReadNumeric("ANDD");
        }
        idDemande = statement.dbReadNumeric("IDCOMF");
        if (JadeStringUtil.isEmpty(idDemande)) {
            idDemande = statement.dbReadNumeric("IBIDCF");
        }
        numContribuable = statement.dbReadString("HXNCON");
        if (JadeStringUtil.isBlankOrZero(numContribuable)) {
            numContribuable = statement.dbReadString("NCON");
        }
        dateRenvoi = statement.dbReadDateAMJ("DATENV");
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("CPSEID", this._dbWriteNumeric(statement.getTransaction(), getIdRejets(), ""));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("CPSEID", this._dbWriteNumeric(statement.getTransaction(), getIdRejets()));
        statement.writeField("SENDID", this._dbWriteString(statement.getTransaction(), getSenderId()));
        statement.writeField("SENNOM", this._dbWriteString(statement.getTransaction(), getNomReference()));
        statement.writeField("SENDEP", this._dbWriteString(statement.getTransaction(), getDepartementReference()));
        statement.writeField("SENPHO", this._dbWriteString(statement.getTransaction(), getTelephoneReference()));
        statement.writeField("SENEMA", this._dbWriteString(statement.getTransaction(), getEmailReference()));
        statement.writeField("RECIID", this._dbWriteString(statement.getTransaction(), getRecipientId()));
        statement.writeField("MESSID", this._dbWriteString(statement.getTransaction(), getMessageId()));
        statement.writeField("REMEID", this._dbWriteString(statement.getTransaction(), getReferenceMessageId()));
        statement.writeField("OBREID", this._dbWriteString(statement.getTransaction(), getOurBusinessReferenceId()));
        statement.writeField("YBREID", this._dbWriteString(statement.getTransaction(), getYourBusinessReferenceId()));
        statement.writeField("SUBJEC", this._dbWriteString(statement.getTransaction(), getSubject()));
        statement.writeField("PERIDC", this._dbWriteString(statement.getTransaction(), getPersonIdCategory()));
        statement.writeField("PERSID", this._dbWriteString(statement.getTransaction(), getPersonId()));
        statement.writeField("OFNAME", this._dbWriteString(statement.getTransaction(), getNom()));
        statement.writeField("FINAME", this._dbWriteString(statement.getTransaction(), getPrenom()));
        statement.writeField("DATNAI", this._dbWriteString(statement.getTransaction(), getDateNaissance()));
        statement.writeField("ADRLI1", this._dbWriteString(statement.getTransaction(), getAdresse1()));
        statement.writeField("ADRLI2", this._dbWriteString(statement.getTransaction(), getAdresse2()));
        statement.writeField("STREET", this._dbWriteString(statement.getTransaction(), getRue()));
        statement.writeField("LOCALI", this._dbWriteString(statement.getTransaction(), getLocalite()));
        statement.writeField("TOWNA", this._dbWriteString(statement.getTransaction(), getVille()));
        statement.writeField("MESDAT", this._dbWriteString(statement.getTransaction(), getDateMessage()));
        statement.writeField("IMESDA", this._dbWriteString(statement.getTransaction(), getDateInitialeMessage()));
        statement.writeField("TESTFL", this._dbWriteString(statement.getTransaction(), getTestFlag()));
        statement.writeField("REMARK", this._dbWriteString(statement.getTransaction(), getRemark()));
        statement.writeField("REJECT", this._dbWriteNumeric(statement.getTransaction(), getRejet()));
        statement.writeField("MESPRI", this._dbWriteNumeric(statement.getTransaction(), getMessagePriorite()));
        statement.writeField("ACTINU", this._dbWriteNumeric(statement.getTransaction(), getAction()));
        statement.writeField("PERSEX", this._dbWriteNumeric(statement.getTransaction(), getSex()));
        statement.writeField("MARITA", this._dbWriteNumeric(statement.getTransaction(), getEtatCivil()));
        statement.writeField("STATUS", this._dbWriteNumeric(statement.getTransaction(), getEtat()));
        statement.writeField("DATENV", this._dbWriteDateAMJ(statement.getTransaction(), getDateRenvoi()));
    }

    public void changerStatus(BISession bSession, String selectedId) {
        setSession((BSession) bSession);
        setIdRejets(selectedId);
        try {
            this.retrieve();
            if (getEtat().equals(CPRejets.CS_ETAT_TRAITE)) {
                setEtat(CPRejets.CS_ETAT_NON_TRAITE);
            } else if (getEtat().equals(CPRejets.CS_ETAT_NON_TRAITE)) {
                setEtat(CPRejets.CS_ETAT_TRAITE);
            }
            this.update();
        } catch (Exception e) {
            // On ne fait rien
        }
    }

    public String getAction() {
        return action;
    }

    public String getAdresse1() {
        return adresse1;
    }

    public String getAdresse2() {
        return adresse2;
    }

    public String getAnnee() {
        return annee;
    }

    /**
     * Le canton de trouve dans le senderId qui est renseigné par le FISc Ex: 2-VD-5
     * 
     * @return String
     */
    public String getCodeCantonFisc() {
        if (getSenderId().length() >= 5) {
            return getSenderId().substring(2, 4);
        }
        return "";
    }

    public String getDateInitialeMessage() {
        return dateInitialeMessage;
    }

    public String getDateMessage() {
        return dateMessage;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public String getDateRenvoi() {
        return dateRenvoi;
    }

    public String getDepartementReference() {
        return departementReference;
    }

    public String getEmailReference() {
        return emailReference;
    }

    public String getEtat() {
        return etat;
    }

    public String getEtatCivil() {
        return etatCivil;
    }

    public String getEtatCivilLibelle() {
        if (getEtatCivil().equals(CPRejets.MARIE)) {
            return getSession().getLabel("REJETS_MARIE");
        } else if (getEtatCivil().equals(CPRejets.CELIBATAIRE)) {
            return getSession().getLabel("REJETS_CELIBATAIRE");
        } else {
            return "";
        }

    }

    public String getIdDemande() {
        return idDemande;
    }

    public String getIdRejets() {
        return idRejets;
    }

    public String getLocalite() {
        return localite;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getMessagePriorite() {
        return messagePriorite;
    }

    public String getNom() {
        return nom;
    }

    public String getNomReference() {
        return nomReference;
    }

    public String getNumContribuable() {
        return numContribuable;
    }

    public String getOurBusinessReferenceId() {
        return ourBusinessReferenceId;
    }

    public String getPersonId() {
        return personId;
    }

    public String getPersonIdCategory() {
        return personIdCategory;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public String getReferenceMessageId() {
        return referenceMessageId;
    }

    public String getRejet() {
        return rejet;
    }

    public String getRejetLibelle(int codeRejet) {
        return getSession().getLabel("REJET_LIBELLE_" + codeRejet);
    }

    public String getRejetVisible() {
        return getSession().getLabel("REJET_LIBELLE_" + getRejet());
    }

    public String getRemark() {
        return remark;
    }

    public String getRue() {
        return rue;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getSex() {
        return sex;
    }

    public String getSexLibelle() {
        if (getSex().equals(CPRejets.HOMME)) {
            return getSession().getCodeLibelle(IConstantes.CS_PERSONNE_SEXE_HOMME);
        } else if (getSex().equals(CPRejets.FEMME)) {
            return getSession().getCodeLibelle(IConstantes.CS_PERSONNE_SEXE_FEMME);
        } else {
            return "";
        }

    }

    public String getSubject() {
        return subject;
    }

    public String getTelephoneReference() {
        return telephoneReference;
    }

    public String getTestFlag() {
        return testFlag;
    }

    public String getVille() {
        return ville;
    }

    public String getVisibleStatus() {
        try {
            return globaz.phenix.translation.CodeSystem.getLibelle(getSession(), getEtat());
        } catch (Exception e) {
            getSession().addError(e.getMessage());
            return "";
        }
    }

    public String getYourBusinessReferenceId() {
        return yourBusinessReferenceId;
    }

    public boolean isReenvoyable() throws Exception {
        if (getEtat().equals(CPRejets.CS_ETAT_ENVOYE) || getEtat().equals(CPRejets.CS_ETAT_ABANDONNE)) {
            return false;
        }
        // Initialement il n'y avait pas de table historique...
        // Du coup dans le cahmp message id il y a toujours le dernier
        // message id envoyé...
        if (!JadeStringUtil.isEmpty(getReferenceMessageId())) {
            CPCommunicationFiscale comm = new CPCommunicationFiscale();
            comm.setSession(getSession());
            comm.setAlternateKey(CPCommunicationFiscale.AK_ID_MESSAGE_SEDEX);
            comm.setIdMessageSedex(getReferenceMessageId());
            comm.retrieve();
            if (comm.isNew()) {
                // regarder dans la table historique
                CPLienSedexCommunicationFiscaleManager mng = new CPLienSedexCommunicationFiscaleManager();
                mng.setSession(getSession());
                mng.setForIdMessageSedex(getReferenceMessageId());
                mng.find();
                if (mng.size() > 0) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return true;
            }
        }
        return false;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setAdresse1(String adresse1) {
        this.adresse1 = adresse1;
    }

    public void setAdresse2(String adresse2) {
        this.adresse2 = adresse2;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void setDateInitialeMessage(String dateInitialeMessage) {
        this.dateInitialeMessage = dateInitialeMessage;
    }

    public void setDateMessage(String dateMessage) {
        this.dateMessage = dateMessage;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setDateRenvoi(String dateRenvoi) {
        this.dateRenvoi = dateRenvoi;
    }

    public void setDepartementReference(String departementReference) {
        this.departementReference = departementReference;
    }

    public void setEmailReference(String emailReference) {
        this.emailReference = emailReference;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public void setEtatCivil(String etatCivil) {
        this.etatCivil = etatCivil;
    }

    public void setIdDemande(String idDemande) {
        this.idDemande = idDemande;
    }

    public void setIdRejets(String idRejets) {
        this.idRejets = idRejets;
    }

    public void setLocalite(String localite) {
        this.localite = localite;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public void setMessagePriorite(String messagePriorite) {
        this.messagePriorite = messagePriorite;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setNomReference(String nomReference) {
        this.nomReference = nomReference;
    }

    public void setNumContribuable(String numContribuable) {
        this.numContribuable = numContribuable;
    }

    public void setOurBusinessReferenceId(String ourBusinessReferenceId) {
        this.ourBusinessReferenceId = ourBusinessReferenceId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public void setPersonIdCategory(String personIdCategory) {
        this.personIdCategory = personIdCategory;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public void setReferenceMessageId(String referenceMessageId) {
        this.referenceMessageId = referenceMessageId;
    }

    public void setRejet(String rejet) {
        this.rejet = rejet;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setRue(String rue) {
        this.rue = rue;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setTelephoneReference(String telephoneReference) {
        this.telephoneReference = telephoneReference;
    }

    public void setTestFlag(String testFlag) {
        this.testFlag = testFlag;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public void setYourBusinessReferenceId(String yourBusinessReferenceId) {
        this.yourBusinessReferenceId = yourBusinessReferenceId;
    }
}