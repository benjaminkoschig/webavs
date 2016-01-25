/**
 * 
 */
package globaz.perseus.vb.pcfaccordee;

import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.perseus.business.calcul.OutputData;
import ch.globaz.perseus.business.models.decision.DecisionSearchModel;
import ch.globaz.perseus.business.models.pcfaccordee.PCFAccordee;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

/**
 * @author DDE
 * 
 */
public class PFPcfAccordeeViewBean extends BJadePersistentObjectViewBean {

    private String mesureChargesLoyer = null;
    private String mesureCoaching = null;
    private String mesureEncouragement = null;
    private String moisCourant = null;
    private PCFAccordee pcfAccordee = null;
    private Boolean willBePaid = false;

    /**
	 * 
	 */
    public PFPcfAccordeeViewBean() {
        super();
        pcfAccordee = new PCFAccordee();
    }

    /**
     * @throws Exception
     * 
     */
    public PFPcfAccordeeViewBean(PCFAccordee pcfAccordee) throws Exception {
        this(pcfAccordee, PerseusServiceLocator.getPmtMensuelService().getDateProchainPmt());
    }

    /**
     * @throws Exception
     * 
     */
    public PFPcfAccordeeViewBean(PCFAccordee pcfAccordee, String moisCourant) throws Exception {
        this.pcfAccordee = pcfAccordee;
        this.moisCourant = moisCourant;
        willBePaid = PerseusServiceLocator.getPmtMensuelService().willBePaid(this.pcfAccordee, this.moisCourant);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        // TODO Auto-generated method stub
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        PerseusServiceLocator.getPCFAccordeeService().delete(pcfAccordee);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {
        return getPcfAccordee().getId();
    }

    /**
     * @return the mesureChargesLoyer
     */
    public String getMesureChargesLoyer() {
        return mesureChargesLoyer;
    }

    public String getMesureCoaching() {
        return mesureCoaching;
    }

    /**
     * @return the mesureEncouragement
     */
    public String getMesureEncouragement() {
        return mesureEncouragement;
    }

    /**
     * @return the pcfAccordee
     */
    public PCFAccordee getPcfAccordee() {
        return pcfAccordee;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        return new BSpy(pcfAccordee.getSpy());
    }

    /**
     * Controle si une PCF accordée a déjà une decision de créée.
     * 
     * @return true si une decision existe déjà pour la PCF accordée, false sinon
     */
    public boolean hasDecisionCreee() throws Exception {
        if (JadeStringUtil.isBlank(pcfAccordee.getId())) {
            return false;
        }
        DecisionSearchModel searchModel = new DecisionSearchModel();
        searchModel.setForIdDemande(pcfAccordee.getDemande().getId());
        searchModel = PerseusServiceLocator.getDecisionService().search(searchModel);
        if (searchModel.getSize() > 0) {
            return true;
        } else {
            return false;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        pcfAccordee = PerseusServiceLocator.getPCFAccordeeService().read(getId());
        mesureEncouragement = pcfAccordee.getCalcul().getDonneeString(OutputData.MESURE_ENCOURAGEMENT);
        mesureChargesLoyer = pcfAccordee.getCalcul().getDonneeString(OutputData.MESURE_CHARGES_LOYER);
        mesureCoaching = pcfAccordee.getCalcul().getDonneeString(OutputData.MESURE_COACHING);

        moisCourant = PerseusServiceLocator.getPmtMensuelService().getDateProchainPmt();
        willBePaid = PerseusServiceLocator.getPmtMensuelService().willBePaid(pcfAccordee, moisCourant);

        getISession().setAttribute(
                "likeNss",
                pcfAccordee.getDemande().getDossier().getDemandePrestation().getPersonneEtendue().getPersonneEtendue()
                        .getNumAvsActuel());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
        pcfAccordee.setId(newId);
    }

    /**
     * @param mesureChargesLoyer
     *            the mesureChargesLoyer to set
     */
    public void setMesureChargesLoyer(String mesureChargesLoyer) {
        this.mesureChargesLoyer = mesureChargesLoyer;
    }

    public void setMesureCoaching(String mesureCoaching) {
        this.mesureCoaching = mesureCoaching;
    }

    /**
     * @param mesureEncouragement
     *            the mesureEncouragement to set
     */
    public void setMesureEncouragement(String mesureEncouragement) {
        this.mesureEncouragement = mesureEncouragement;
    }

    /**
     * @param pcfAccordee
     *            the pcfAccordee to set
     */
    public void setPcfAccordee(PCFAccordee pcfAccordee) {
        this.pcfAccordee = pcfAccordee;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        PerseusServiceLocator.getPCFAccordeeService().update(pcfAccordee, mesureEncouragement, mesureChargesLoyer,
                mesureCoaching);
    }

    /**
     * Permet de voir si une pcfAccordée sera payée
     * 
     * @param pcfa
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     */
    public boolean willBePaid() throws JadeApplicationServiceNotAvailableException {
        return willBePaid;
    }
}
