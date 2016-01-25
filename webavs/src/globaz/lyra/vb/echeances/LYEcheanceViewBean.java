package globaz.lyra.vb.echeances;

import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import ch.globaz.lyra.business.exceptions.LYTechnicalException;

public class LYEcheanceViewBean extends BJadePersistentObjectViewBean {

    private String csDomaineApplicatifParDefaut;

    public LYEcheanceViewBean() {
        super();

        csDomaineApplicatifParDefaut = "";
    }

    @Override
    public void add() throws Exception {
        throw new LYTechnicalException("Not implemented");
    }

    @Override
    public void delete() throws Exception {
        throw new LYTechnicalException("Not implemented");
    }

    public String getCsDomaineApplicatifParDefaut() {
        return csDomaineApplicatifParDefaut;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public BSpy getSpy() {
        return null;
    }

    @Override
    public void retrieve() throws Exception {

    }

    public void setCsDomaineApplicatifParDefaut(String csDomaineApplicatifParDefaut) {
        this.csDomaineApplicatifParDefaut = csDomaineApplicatifParDefaut;
    }

    @Override
    public void setId(String newId) {
    }

    @Override
    public void update() throws Exception {
        throw new LYTechnicalException("Not implemented");
    }
}
