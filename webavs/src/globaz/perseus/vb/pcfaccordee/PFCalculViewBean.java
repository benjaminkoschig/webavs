/**
 * 
 */
package globaz.perseus.vb.pcfaccordee;

import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import ch.globaz.perseus.business.models.demande.Demande;
import ch.globaz.perseus.business.models.pcfaccordee.PCFAccordee;
import ch.globaz.perseus.business.models.pcfaccordee.PCFAccordeeSearchModel;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

/**
 * @author DDE
 * 
 */
public class PFCalculViewBean extends BJadePersistentObjectViewBean {

    private String checkCalcul = null;
    private Demande demande = null;
    private String idDemande = null;
    private PCFAccordee pcfAccordee = null;

    /**
	 * 
	 */
    public PFCalculViewBean() {
        super();
        demande = new Demande();
        pcfAccordee = new PCFAccordee();
    }

    @Override
    public void add() throws Exception {
        // TODO Auto-generated method stub

    }

    public void calculer() throws Exception {
        pcfAccordee = PerseusServiceLocator.getPCFAccordeeService().calculer(idDemande);
    }

    @Override
    public void delete() throws Exception {
        // TODO Auto-generated method stub

    }

    /**
     * @return the checkCalcul
     */
    public String getCheckCalcul() {
        return checkCalcul;
    }

    /**
     * @return the demande
     */
    public Demande getDemande() {
        return demande;
    }

    @Override
    public String getId() {
        return pcfAccordee.getId();
    }

    /**
     * @return the idDemande
     */
    public String getIdDemande() {
        return idDemande;
    }

    /**
     * @return the pcfAccordee
     */
    public PCFAccordee getPcfAccordee() {
        return pcfAccordee;
    }

    @Override
    public BSpy getSpy() {
        return new BSpy(pcfAccordee.getSpy());
    }

    @Override
    public void retrieve() throws Exception {
        demande = PerseusServiceLocator.getDemandeService().read(idDemande);
        PCFAccordeeSearchModel pcfAccordeeSearchModel = new PCFAccordeeSearchModel();
        pcfAccordeeSearchModel.setForIdDemande(idDemande);
        pcfAccordeeSearchModel = PerseusServiceLocator.getPCFAccordeeService().search(pcfAccordeeSearchModel);
        if (pcfAccordeeSearchModel.getSize() == 1) {
            pcfAccordee = (PCFAccordee) pcfAccordeeSearchModel.getSearchResults()[0];
        }
        checkCalcul = PerseusServiceLocator.getCalculPCFService().checkCalcul(demande);
    }

    /**
     * @param checkCalcul
     *            the checkCalcul to set
     */
    public void setCheckCalcul(String checkCalcul) {
        this.checkCalcul = checkCalcul;
    }

    /**
     * @param demande
     *            the demande to set
     */
    public void setDemande(Demande demande) {
        this.demande = demande;
    }

    @Override
    public void setId(String newId) {
        // TODO Auto-generated method stub

    }

    /**
     * @param idDemande
     *            the idDemande to set
     */
    public void setIdDemande(String idDemande) {
        this.idDemande = idDemande;
    }

    /**
     * @param pcfAccordee
     *            the pcfAccordee to set
     */
    public void setPcfAccordee(PCFAccordee pcfAccordee) {
        this.pcfAccordee = pcfAccordee;
    }

    @Override
    public void update() throws Exception {
        // TODO Auto-generated method stub

    }

}
