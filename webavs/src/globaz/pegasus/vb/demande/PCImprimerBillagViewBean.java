package globaz.pegasus.vb.demande;

import globaz.framework.security.FWSecurityLoginException;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeDateUtil;
import globaz.pegasus.utils.PCGestionnaireHelper;
import globaz.perseus.utils.PFUserHelper;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import ch.globaz.pegasus.business.models.demande.Demande;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class PCImprimerBillagViewBean extends BJadePersistentObjectViewBean {

    private String dateDebut = null;
    private String dateDoc = null;
    private Demande demande = null;
    private String gestionnaire = null;
    private String idDemandePc = null;

    private String mailGest = null;

    private String nss = null;

    public PCImprimerBillagViewBean() {
        super();

    }

    public List<String[]> getGestionnaires() {

        Iterator it = PCGestionnaireHelper.getResponsableData(getSession()).iterator();
        List<String[]> users = new ArrayList<String[]>();

        while (it.hasNext()) {
            String userAndName[] = (String[]) it.next();
            users.add(userAndName);
        }

        return users;

    }

    @Override
    public void add() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void delete() throws Exception {
        // TODO Auto-generated method stub

    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateDoc() {
        return dateDoc;
    }

    /**
     * Retourne la date courant au format Globaz
     * 
     * @return
     */
    public String getDateNow() {
        Date dateNow = new Date();
        return JadeDateUtil.getGlobazFormattedDate(dateNow);
    }

    public Demande getDemande() {
        return demande;
    }

    public String getGestionnaire() {
        return gestionnaire;
    }

    @Override
    public String getId() {
        return null;
    }

    public String getIdDemandePc() {
        return idDemandePc;
    }

    public String getIdDossier() {
        return demande.getDossier().getDossier().getIdDossier();
    }

    public String getIdTiers() {
        return demande.getDossier().getDemandePrestation().getPersonneEtendue().getTiers().getIdTiers();
    }

    public String getMailGest() {
        return mailGest;
    }

    public String getMailGestionnaire(BSession session) {

        return session.getUserEMail();
    }

    public String getNss() {
        return nss;
    }

    public String getNSS() {
        return demande.getDossier().getDemandePrestation().getPersonneEtendue().getPersonneEtendue().getNumAvsActuel();
    }

    public String getRequerant() throws FWSecurityLoginException, Exception {

        return PFUserHelper.getDetailAssure(getSession(), demande.getDossier().getDemandePrestation()
                .getPersonneEtendue());
    }

    private BSession getSession() {
        return (BSession) getISession();
    }

    @Override
    public BSpy getSpy() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void retrieve() throws Exception {

        demande = PegasusServiceLocator.getDemandeService().read(idDemandePc);
    }

    public void setD01emande(Demande demande) {
        this.demande = demande;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = "01." + dateDebut;
    }

    public void setDateDoc(String dateDoc) {
        this.dateDoc = dateDoc;
    }

    public void setGestionnaire(String gestionnaire) {

        this.gestionnaire = gestionnaire;

    }

    @Override
    public void setId(String newId) {
        // TODO Auto-generated method stub

    }

    public void setIdDemandePc(String idDemandePc) {
        this.idDemandePc = idDemandePc;
    }

    public void setMailGest(String mailGest) {
        this.mailGest = mailGest;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    @Override
    public void update() throws Exception {
        // TODO Auto-generated method stub

    }

}
