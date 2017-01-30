package globaz.amal.vb.sedexco;

import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import ch.globaz.amal.business.models.annoncesedexco.ComplexAnnonceSedexCO;
import ch.globaz.amal.business.services.AmalServiceLocator;

public class AMSedexcoViewBean extends BJadePersistentObjectViewBean {
    private ComplexAnnonceSedexCO complexAnnonceSedexCO = null;

    public AMSedexcoViewBean() {
        super();
        complexAnnonceSedexCO = new ComplexAnnonceSedexCO();
    }

    /**
     * Constructor called from rcListe
     * 
     * @param job
     */
    public AMSedexcoViewBean(ComplexAnnonceSedexCO complexAnnonceSedexCO) {
        this();
        this.complexAnnonceSedexCO = complexAnnonceSedexCO;
    }

    @Override
    public void add() throws Exception {

    }

    @Override
    public void delete() throws Exception {

    }

    public ComplexAnnonceSedexCO getComplexAnnonceSedexCO() {
        return complexAnnonceSedexCO;
    }

    public void setComplexAnnonceSedexCO(ComplexAnnonceSedexCO complexAnnonceSedexCO) {
        this.complexAnnonceSedexCO = complexAnnonceSedexCO;
    }

    @Override
    public String getId() {
        return complexAnnonceSedexCO.getId();
    }

    @Override
    public BSpy getSpy() {
        return new BSpy(complexAnnonceSedexCO.getSpy());
    }

    @Override
    public void retrieve() throws Exception {
        complexAnnonceSedexCO = AmalServiceLocator.getComplexAnnonceSedexCOService().read(getId());
    }

    @Override
    public void setId(String newId) {
        complexAnnonceSedexCO.setId(newId);
    }

    @Override
    public void update() throws Exception {
    }

}
