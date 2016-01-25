package globaz.pegasus.vb.transfertdossier;

import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.pegasus.utils.PCUserHelper;
import globaz.prestation.tools.nnss.PRNSSUtil;
import ch.globaz.pegasus.business.models.demande.Demande;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.model.TiersSimpleModel;

public class PCDemandeTransfertRenteViewBean extends BJadePersistentObjectViewBean {

    private String dateAnnonce = null;
    private String dateTransfert = null;
    private String idCaisseAgence = null;
    private String idDemandePc = null;
    private String idDossier = null;
    private String idGestionnaire = null;
    private String idRequerant = null;
    private String mailAddress = null;
    private String noCaisseAgence = null;
    private PersonneEtendueComplexModel requerant;

    @Override
    public void add() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void delete() throws Exception {
        // TODO Auto-generated method stub

    }

    public String getDateAnnonce() {
        return dateAnnonce;
    }

    public String getDateTransfert() {
        return dateTransfert;
    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getIdCaisseAgence() {
        return idCaisseAgence;
    }

    public String getIdDemandePc() {
        return idDemandePc;
    }

    public String getIdDossier() {
        return idDossier;
    }

    public String getIdGestionnaire() {
        return idGestionnaire;
    }

    public String getIdRequerant() {
        return idRequerant;
    }

    public String getMailAddress() {
        return mailAddress;
    }

    public String getNoCaisseAgence() {
        return noCaisseAgence;
    }

    /**
     * Formatte une chaine de caratere pour afficher les infos du requérant
     * 
     * @return
     */
    public String getRequerantInfos() {

        TiersSimpleModel tiers = requerant.getTiers();

        String NSS = requerant.getPersonneEtendue().getNumAvsActuel();
        String NomPrenom = tiers.getDesignation1() + " " + tiers.getDesignation2();
        String dateNaissance = requerant.getPersonne().getDateNaissance();
        String sexe = getSession().getCodeLibelle(requerant.getPersonne().getSexe());
        String nationalite = PCUserHelper.getLibellePays(requerant);// this.getSession().getCodeLibelle(

        String reqInfos = PRNSSUtil.formatDetailRequerantDetail(NSS, NomPrenom, dateNaissance, sexe, nationalite);

        return reqInfos;
    }

    /**
     * Retourne l'objet session
     * 
     * @return objet BSession
     */
    public BSession getSession() {
        return (BSession) getISession();
    }

    @Override
    public BSpy getSpy() {
        // return new BSpy(this.getSession());
        return null;
    }

    @Override
    public void retrieve() throws Exception {

        Demande demande = PegasusServiceLocator.getDemandeService().read(idDemandePc);
        requerant = demande.getDossier().getDemandePrestation().getPersonneEtendue();

    }

    public void setDateAnnonce(String dateAnnonce) {
        this.dateAnnonce = dateAnnonce;
    }

    public void setDateTransfert(String dateTransfert) {
        this.dateTransfert = dateTransfert;
    }

    @Override
    public void setId(String newId) {
        // TODO Auto-generated method stub

    }

    public void setIdCaisseAgence(String idCaisseAgence) {
        this.idCaisseAgence = idCaisseAgence;
    }

    public void setIdDemandePc(String idDemandePc) {
        this.idDemandePc = idDemandePc;
    }

    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    public void setIdGestionnaire(String idGestionnaire) {
        this.idGestionnaire = idGestionnaire;
    }

    public void setIdRequerant(String idRequerant) {
        this.idRequerant = idRequerant;
    }

    public void setMailAddress(String eMailAddress) {
        mailAddress = eMailAddress;
    }

    public void setNoCaisseAgence(String noCaisseAgence) {
        this.noCaisseAgence = noCaisseAgence;
    }

    @Override
    public void update() throws Exception {
        // TODO Auto-generated method stub

    }

}
