/**
 * 
 */
package globaz.perseus.vb.demande;

import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import ch.globaz.perseus.business.models.demande.Demande;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

/**
 * @author DDE
 * 
 */
public class PFAnnulerDemandeViewBean extends BJadePersistentObjectViewBean {

    private Demande demande;

    public PFAnnulerDemandeViewBean() {
        super();
        demande = new Demande();
    }

    public PFAnnulerDemandeViewBean(Demande demande) {
        super();
        this.demande = demande;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
    }

    /**
     * @return the demande
     */
    public Demande getDemande() {
        return demande;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {
        return demande.getId();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        return new BSpy(demande.getSpy());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        demande = PerseusServiceLocator.getDemandeService().read(getId());
    }

    /**
     * @param demande
     *            the demande to set
     */
    public void setDemande(Demande demande) {
        this.demande = demande;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
        demande.setId(newId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        demande = PerseusServiceLocator.getDemandeService().annuler(demande);
    }

}
