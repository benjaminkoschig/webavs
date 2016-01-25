/*
 * créé le 01 octobre 2010
 */
package globaz.cygnus.vb.typeDeSoins;

import globaz.cygnus.api.IRFTypesBeneficiairePc;
import globaz.cygnus.db.typeDeSoins.RFSousTypeDeSoin;
import globaz.cygnus.utils.RFSoinsListsBuilder;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.security.FWSecurityLoginException;
import globaz.globall.api.BISession;
import globaz.globall.db.BIPersistentObject;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.tools.PRCodeSystem;
import java.util.Vector;

/**
 * author fha
 */
public class RFTypeDeSoinViewBean implements FWViewBeanInterface, BIPersistentObject {

    private transient String _methode = "";

    private String codeSousTypeDeSoin = "";
    private String codeSousTypeDeSoinList = "";
    private String codeTypeDeSoin = "";
    private String codeTypeDeSoinList = "";
    private String CsTypeBeneficiaire = "";
    private Vector<String[]> csTypeBeneficiairePcData = null;
    private transient String dateDebut = "";
    private transient String dateFin = "";
    private transient Boolean forcerPayement = Boolean.FALSE;
    private transient String idGestionnaire = "";

    private transient String idPotAssure = "";

    private transient String idSoinPot = "";
    private String idSousTypeDeSoin = "";
    private String message = "";
    private transient String montantPlafondAutresEnfants = "";
    private transient String montantPlafondCoupleDomicile = "";

    private transient String montantPlafondCouplesEnfants = "";

    private transient String montantPlafondEnfants2023 = "";

    private transient String montantPlafondEnfantsSepares = "";

    private transient String montantPlafondOrphelins = "";
    private transient String montantPlafondPersonneSeule = "";

    private transient String montantPlafondSepareDomicile = "";
    private transient String montantPlafondSepareHome = "";

    private transient String montantPourTous = "";

    private String msgType = "";
    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------
    private BISession session = null;

    private String sousTypeDeSoinParTypeInnerJavascript = "";
    private transient Vector<String[]> typeDeSoinsDemande = null;

    // ~ Constructor
    // ------------------------------------------------------------------------------------------------
    public RFTypeDeSoinViewBean() {
        super();
    }

    // ~ Accessors and mutators
    // ------------------------------------------------------------------------------------------------

    @Override
    public void add() throws Exception {
        // TODO Auto-generated method stub
    }

    @Override
    public void delete() throws Exception {
        // TODO Auto-generated method stub
    }

    public String get_methode() {
        return _methode;
    }

    public String getCodeSousTypeDeSoin() {
        return codeSousTypeDeSoin;
    }

    public String getCodeSousTypeDeSoinList() {
        return codeSousTypeDeSoinList;
    }

    public String getCodeTypeDeSoin() {
        return codeTypeDeSoin;
    }

    public String getCodeTypeDeSoinList() {
        return codeTypeDeSoinList;
    }

    public BSpy getCreationSpy() {

        RFSousTypeDeSoin sts = new RFSousTypeDeSoin();

        try {
            sts = RFSousTypeDeSoin.loadSousTypeSoin(getSession(), getSession().getCurrentThreadTransaction(),
                    getIdSousTypeDeSoin());
        } catch (Exception e) {
        }
        return sts.getCreationSpy();
    }

    public String getCsTypeBeneficiaire() {
        return CsTypeBeneficiaire;
    }

    public Vector<String[]> getCsTypeBeneficiairePCData() {
        if (csTypeBeneficiairePcData == null) {
            csTypeBeneficiairePcData = PRCodeSystem.getLibellesPourGroupe(
                    IRFTypesBeneficiairePc.CS_GROUPE_TYPE_BENEFICIAIRE_PC, getSession());

            // ajout des options custom
            csTypeBeneficiairePcData.add(0, new String[] { "", "" });
        }

        return csTypeBeneficiairePcData;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public String getDetailGestionnaire() throws FWSecurityLoginException, Exception {

        if (JadeStringUtil.isEmpty(getIdGestionnaire())) {// getVisaGestionnaire
            return "";
        } else {
            JadeUser userName = getSession().getApplication()._getSecurityManager()
                    .getUserForVisa(getSession(), getIdGestionnaire());
            return userName.getIdUser() + " - " + userName.getFirstname() + " " + userName.getLastname();
        }
    }

    public Boolean getForcerPayement() {
        return forcerPayement;
    }

    public String getGestionnaire() throws FWSecurityLoginException, Exception {

        if (JadeStringUtil.isEmpty(getIdGestionnaire())) {
            return "";
        } else {
            JadeUser userName = getSession().getApplication()._getSecurityManager()
                    .getUserForVisa(getSession(), getIdGestionnaire());
            return userName.getIdUser();
        }
    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getIdGestionnaire() {
        return idGestionnaire;
    }

    public String getIdPotAssure() {
        return idPotAssure;
    }

    public String getIdSoinPot() {
        return idSoinPot;
    }

    public String getIdSousTypeDeSoin() {
        return idSousTypeDeSoin;
    }

    public String getImageError() {
        return "/images/erreur.gif";
    }

    public String getImageSuccess() {
        return "/images/ok.gif";
    }

    @Override
    public BISession getISession() {
        return session;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getMontantPlafondAutresEnfants() {
        return montantPlafondAutresEnfants;
    }

    public String getMontantPlafondCoupleDomicile() {
        return montantPlafondCoupleDomicile;
    }

    public String getMontantPlafondCouplesEnfants() {
        return montantPlafondCouplesEnfants;
    }

    public String getMontantPlafondEnfants2023() {
        return montantPlafondEnfants2023;
    }

    public String getMontantPlafondEnfantsSepares() {
        return montantPlafondEnfantsSepares;
    }

    public String getMontantPlafondOrphelins() {
        return montantPlafondOrphelins;
    }

    public String getMontantPlafondPersonneSeule() {
        return montantPlafondPersonneSeule;
    }

    public String getMontantPlafondSepareDomicile() {
        return montantPlafondSepareDomicile;
    }

    public String getMontantPlafondSepareHome() {
        return montantPlafondSepareHome;
    }

    public String getMontantPourTous() {
        return montantPourTous;
    }

    @Override
    public String getMsgType() {
        return msgType;
    }

    public BSession getSession() {
        return (BSession) session;
    }

    /**
     * Méthode qui retourne un tableau javascript de sous type de soins à 2 dimension (code,CSlibelle)
     * 
     * @return String
     */
    public String getSousTypeDeSoinParTypeInnerJavascript() {

        if (JadeStringUtil.isBlank(sousTypeDeSoinParTypeInnerJavascript)) {
            try {
                sousTypeDeSoinParTypeInnerJavascript = RFSoinsListsBuilder.getInstance(getSession())
                        .getSousTypeDeSoinParTypeInnerJavascript();
            } catch (Exception e) {
                setMessage(e.getMessage());
                setMsgType(ERROR);
            }
        }

        return sousTypeDeSoinParTypeInnerJavascript;

    }

    public BSpy getSpy() {

        RFSousTypeDeSoin sts = new RFSousTypeDeSoin();

        try {
            sts = RFSousTypeDeSoin.loadSousTypeSoin(getSession(), getSession().getCurrentThreadTransaction(),
                    getIdSousTypeDeSoin());
        } catch (Exception e) {
        }
        return sts.getSpy();
    }

    public Vector<String[]> getTypeDeSoinData() {
        try {
            if (typeDeSoinsDemande == null) {
                typeDeSoinsDemande = RFSoinsListsBuilder.getInstance(getSession()).getTypeDeSoinsDemande();
            }
        } catch (Exception e) {
            setMessage(e.getMessage());
            setMsgType(ERROR);
        }

        return typeDeSoinsDemande;
    }

    public Vector<String[]> getTypeDeSoinsDemande() {
        return typeDeSoinsDemande;
    }

    @Override
    public void retrieve() throws Exception {
        // TODO Auto-generated method stub
    }

    public void set_methode(String methode) {
        _methode = methode;
    }

    public void setCodeSousTypeDeSoin(String codeSousTypeDeSoin) {
        this.codeSousTypeDeSoin = codeSousTypeDeSoin;
    }

    public void setCodeSousTypeDeSoinList(String codeSousTypeDeSoinList) {
        this.codeSousTypeDeSoinList = codeSousTypeDeSoinList;
    }

    public void setCodeTypeDeSoin(String codeTypeDeSoin) {
        this.codeTypeDeSoin = codeTypeDeSoin;
    }

    public void setCodeTypeDeSoinList(String codeTypeDeSoinList) {
        this.codeTypeDeSoinList = codeTypeDeSoinList;
    }

    public void setCsTypeBeneficiaire(String csTypeBeneficiaire) {
        CsTypeBeneficiaire = csTypeBeneficiaire;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setForcerPayement(Boolean forcerPayement) {
        this.forcerPayement = forcerPayement;
    }

    @Override
    public void setId(String newId) {
        idSousTypeDeSoin = newId;
    }

    public void setIdGestionnaire(String idGestionnaire) {
        this.idGestionnaire = idGestionnaire;
    }

    public void setIdPotAssure(String idPotAssure) {
        this.idPotAssure = idPotAssure;
    }

    public void setIdSoinPot(String idSoinPot) {
        this.idSoinPot = idSoinPot;
    }

    public void setIdSousTypeDeSoin(String idSousTypeDeSoin) {
        this.idSousTypeDeSoin = idSousTypeDeSoin;
    }

    @Override
    public void setISession(BISession session) {
        this.session = session;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    public void setMontantPlafondAutresEnfants(String montantPlafondAutresEnfants) {
        this.montantPlafondAutresEnfants = montantPlafondAutresEnfants;
    }

    public void setMontantPlafondCoupleDomicile(String montantPlafondCoupleDomicile) {
        this.montantPlafondCoupleDomicile = montantPlafondCoupleDomicile;
    }

    public void setMontantPlafondCouplesEnfants(String montantPlafondCouplesEnfants) {
        this.montantPlafondCouplesEnfants = montantPlafondCouplesEnfants;
    }

    public void setMontantPlafondEnfants2023(String montantPlafondEnfants2023) {
        this.montantPlafondEnfants2023 = montantPlafondEnfants2023;
    }

    public void setMontantPlafondEnfantsSepares(String montantPlafondEnfantsSepares) {
        this.montantPlafondEnfantsSepares = montantPlafondEnfantsSepares;
    }

    public void setMontantPlafondOrphelins(String montantPlafondOrphelins) {
        this.montantPlafondOrphelins = montantPlafondOrphelins;
    }

    public void setMontantPlafondPersonneSeule(String montantPlafondPersonneSeule) {
        this.montantPlafondPersonneSeule = montantPlafondPersonneSeule;
    }

    public void setMontantPlafondSepareDomicile(String montantPlafondSepareDomicile) {
        this.montantPlafondSepareDomicile = montantPlafondSepareDomicile;
    }

    public void setMontantPlafondSepareHome(String montantPlafondSepareHome) {
        this.montantPlafondSepareHome = montantPlafondSepareHome;
    }

    public void setMontantPourTous(String montantPourTous) {
        this.montantPourTous = montantPourTous;
    }

    @Override
    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public void setSession(BSession session) {
        this.session = session;
    }

    public void setTypeDeSoinsDemande(Vector<String[]> typeDeSoinsDemande) {
        this.typeDeSoinsDemande = typeDeSoinsDemande;
    }

    @Override
    public void update() throws Exception {
        // TODO Auto-generated method stub
    }

}
