package globaz.amal.vb.sedexpt;

import ch.globaz.amal.business.models.annoncesedex.ComplexAnnonceSedex;
import ch.globaz.amal.business.services.AmalServiceLocator;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;

public class AMSedexptViewBean extends BJadePersistentObjectViewBean {

    private ComplexAnnonceSedex complexAnnonceSedex = null;
    private String email = null;
    public String getEmail() {
        if (JadeStringUtil.isBlank(email)) {
            return JadeThread.currentUserEmail();
        }
        return email;
    }

    public AMSedexptViewBean() {
        super();
        setComplexAnnonceSedex(new ComplexAnnonceSedex());
    }

    /**
     * Constructor called from rcListe
     *
     * @param complexAnnonceSedex
     */
    public AMSedexptViewBean(ComplexAnnonceSedex complexAnnonceSedex) {
        this();
        this.complexAnnonceSedex = complexAnnonceSedex;
    }

    @Override
    public void add() throws Exception {
    }

    @Override
    public void delete() throws Exception {
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
    }

}
