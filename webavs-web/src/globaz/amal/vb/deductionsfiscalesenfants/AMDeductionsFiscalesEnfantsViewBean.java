/**
 * 
 */
package globaz.amal.vb.deductionsfiscalesenfants;

import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import ch.globaz.amal.business.models.deductionsfiscalesenfants.SimpleDeductionsFiscalesEnfants;
import ch.globaz.amal.business.services.AmalServiceLocator;

/**
 * @author CBU
 * 
 */
public class AMDeductionsFiscalesEnfantsViewBean extends BJadePersistentObjectViewBean {
    private SimpleDeductionsFiscalesEnfants simpleDeductionsFiscalesEnfants = null;

    public AMDeductionsFiscalesEnfantsViewBean() {
        super();
        simpleDeductionsFiscalesEnfants = new SimpleDeductionsFiscalesEnfants();
    }

    public AMDeductionsFiscalesEnfantsViewBean(SimpleDeductionsFiscalesEnfants simpleDeductionsFiscalesEnfants) {
        this();
        this.simpleDeductionsFiscalesEnfants = simpleDeductionsFiscalesEnfants;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        simpleDeductionsFiscalesEnfants = AmalServiceLocator.getDeductionsFiscalesEnfantsService().create(
                simpleDeductionsFiscalesEnfants);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        simpleDeductionsFiscalesEnfants = AmalServiceLocator.getDeductionsFiscalesEnfantsService().delete(
                simpleDeductionsFiscalesEnfants);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {
        return simpleDeductionsFiscalesEnfants.getId();
    }

    public SimpleDeductionsFiscalesEnfants getSimpleDeductionsFiscalesEnfants() {
        return simpleDeductionsFiscalesEnfants;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        if (simpleDeductionsFiscalesEnfants != null) {
            return new BSpy(simpleDeductionsFiscalesEnfants.getSpy());
        } else {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
        simpleDeductionsFiscalesEnfants.setId(newId);
    }

    public void setSimpleDeductionsFiscalesEnfants(SimpleDeductionsFiscalesEnfants simpleDeductionsFiscalesEnfants) {
        this.simpleDeductionsFiscalesEnfants = simpleDeductionsFiscalesEnfants;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        simpleDeductionsFiscalesEnfants = AmalServiceLocator.getDeductionsFiscalesEnfantsService().update(
                simpleDeductionsFiscalesEnfants);
    }

}
