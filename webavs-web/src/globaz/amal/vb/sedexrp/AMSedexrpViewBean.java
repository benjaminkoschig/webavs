package globaz.amal.vb.sedexrp;

import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import ch.globaz.amal.business.models.annoncesedex.ComplexAnnonceSedex;
import ch.globaz.amal.business.services.AmalServiceLocator;

public class AMSedexrpViewBean extends BJadePersistentObjectViewBean {
    // private AdministrationAdresseComplexModel adresseComplexModel = null;
    private ComplexAnnonceSedex complexAnnonceSedex = null;

    public AMSedexrpViewBean() {
        super();
        setComplexAnnonceSedex(new ComplexAnnonceSedex());
    }

    /**
     * Constructor called from rcListe
     * 
     * @param job
     */
    public AMSedexrpViewBean(ComplexAnnonceSedex complexAnnonceSedex) {
        this();
        this.complexAnnonceSedex = complexAnnonceSedex;
    }

    @Override
    public void add() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void delete() throws Exception {
        // TODO Auto-generated method stub

    }

    public ComplexAnnonceSedex getComplexAnnonceSedex() {
        return complexAnnonceSedex;
    }

    @Override
    public String getId() {
        return complexAnnonceSedex.getId();
    }

    @Override
    public BSpy getSpy() {
        return new BSpy(complexAnnonceSedex.getSpy());
    }

    @Override
    public void retrieve() throws Exception {
        complexAnnonceSedex = AmalServiceLocator.getComplexAnnonceSedexService().read(getId());
    }

    public void setComplexAnnonceSedex(ComplexAnnonceSedex complexAnnonceSedex) {
        this.complexAnnonceSedex = complexAnnonceSedex;
    }

    @Override
    public void setId(String newId) {
        complexAnnonceSedex.setId(newId);
    }

    @Override
    public void update() throws Exception {
        // TODO Auto-generated method stub
    }

}
