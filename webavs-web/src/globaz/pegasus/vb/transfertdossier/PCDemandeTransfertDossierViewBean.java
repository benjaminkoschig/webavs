package globaz.pegasus.vb.transfertdossier;

import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.models.demande.Demande;
import ch.globaz.pegasus.business.models.droit.DonneesPersonnelles;
import ch.globaz.pegasus.business.models.droit.DonneesPersonnellesSearch;
import ch.globaz.pegasus.business.models.droit.Droit;
import ch.globaz.pegasus.business.models.droit.DroitSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.utils.PegasusUtil;
import ch.globaz.pyxis.business.model.TiersSimpleModel;

public class PCDemandeTransfertDossierViewBean extends BJadePersistentObjectViewBean {

    private String[] annexes = null;
    private String captionDernierDomicileLegal = "";
    private String[] copies = null;
    private String dateSurDocument = null;
    private String dateTransfert = null;
    private String idDemandePc = null;
    private String idDernierDomicileLegal = null;
    private String idGestionnaire = null;

    private String idNouvelleCaisse = null;

    private String idRequerant = null;
    private String mailAddress = null;
    private String nomPrenomRequerant = null;

    public PCDemandeTransfertDossierViewBean() {
        super();
    }

    @Override
    public void add() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void delete() throws Exception {
        // TODO Auto-generated method stub

    }

    public String[] getAnnexes() {
        return annexes;
    }

    public String getCaptionDernierDomicileLegal() {
        return captionDernierDomicileLegal;
    }

    public String[] getCopies() {
        return copies;
    }

    public String getDateSurDocument() {
        return dateSurDocument;
    }

    public String getDateTransfert() {
        return dateTransfert;
    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getIdDemandePc() {
        return idDemandePc;
    }

    public String getIdDernierDomicileLegal() {
        return idDernierDomicileLegal;
    }

    public String getIdGestionnaire() {
        return idGestionnaire;
    }

    public String getIdNouvelleCaisse() {
        return idNouvelleCaisse;
    }

    public String getIdRequerant() {
        return idRequerant;
    }

    public String getMailAddress() {
        return mailAddress;
    }

    public String getNomPrenomRequerant() {
        return nomPrenomRequerant;
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

        // recherche la dernière localité si elle existe
        DroitSearch droitSearch = new DroitSearch();
        droitSearch.setForIdDemandePc(idDemandePc);
        droitSearch.setDefinedSearchSize(1);
        droitSearch = PegasusServiceLocator.getDroitService().searchDroit(droitSearch);
        if (droitSearch.getSize() > 0) {
            Droit droit = (Droit) droitSearch.getSearchResults()[0];
            String idDroit = droit.getSimpleDroit().getIdDroit();

            DonneesPersonnellesSearch dpSearch = new DonneesPersonnellesSearch();
            dpSearch.setForCsRoleFamillePC(IPCDroits.CS_ROLE_FAMILLE_REQUERANT);
            dpSearch.setForIdDroit(idDroit);
            dpSearch.setWhereKey(DonneesPersonnellesSearch.FOR_DROIT);
            droitSearch.setDefinedSearchSize(1);
            dpSearch = PegasusServiceLocator.getDroitService().searchDonneesPersonnelles(dpSearch);
            if (dpSearch.getSize() > 0) {
                DonneesPersonnelles donPer = (DonneesPersonnelles) dpSearch.getSearchResults()[0];
                idDernierDomicileLegal = donPer.getSimpleDonneesPersonnelles().getIdDernierDomicileLegale();
                captionDernierDomicileLegal = donPer.getLocalite().getNumPostal() + ", "
                        + donPer.getLocalite().getLocalite();
            }
        }

        // recherche info requerant
        Demande demande = PegasusServiceLocator.getDemandeService().read(idDemandePc);
        TiersSimpleModel requerant = demande.getDossier().getDemandePrestation().getPersonneEtendue().getTiers();
        idRequerant = requerant.getIdTiers();
        nomPrenomRequerant = PegasusUtil.formatNomPrenom(requerant);

    }

    public void setAnnexes(String annexes) {
        this.annexes = annexes.split("¦");
    }

    public void setCaptionDernierDomicileLegal(String captionDernierDomicileLegal) {
        this.captionDernierDomicileLegal = captionDernierDomicileLegal;
    }

    public void setCopies(String copies) {
        this.copies = copies.split("¦");
    }

    public void setDateSurDocument(String dateSurDocument) {
        this.dateSurDocument = dateSurDocument;
    }

    public void setDateTransfert(String dateTransfert) {
        this.dateTransfert = dateTransfert;
    }

    @Override
    public void setId(String newId) {
        // TODO Auto-generated method stub

    }

    public void setIdDemandePc(String idDemandePc) {
        this.idDemandePc = idDemandePc;
    }

    public void setIdDernierDomicileLegal(String idDernierDomicileLegal) {
        this.idDernierDomicileLegal = idDernierDomicileLegal;
    }

    public void setIdGestionnaire(String idGestionnaire) {
        this.idGestionnaire = idGestionnaire;
    }

    public void setIdNouvelleCaisse(String idNouvelleCaisse) {
        this.idNouvelleCaisse = idNouvelleCaisse;
    }

    public void setIdRequerant(String idRequerant) {
        this.idRequerant = idRequerant;
    }

    public void setMailAddress(String eMailAddress) {
        mailAddress = eMailAddress;
    }

    public void setNomPrenomRequerant(String nomPrenomRequerant) {
        this.nomPrenomRequerant = nomPrenomRequerant;
    }

    @Override
    public void update() throws Exception {
        // TODO Auto-generated method stub

    }

}
