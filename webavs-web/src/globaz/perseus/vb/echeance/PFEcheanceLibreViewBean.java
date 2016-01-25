package globaz.perseus.vb.echeance;

import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import ch.globaz.perseus.business.models.echeance.EcheanceLibre;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

public class PFEcheanceLibreViewBean extends BJadePersistentObjectViewBean {

    private EcheanceLibre echeanceLibre;

    public PFEcheanceLibreViewBean() {
        echeanceLibre = new EcheanceLibre();
    }

    public PFEcheanceLibreViewBean(EcheanceLibre el) {
        echeanceLibre = el;
    }

    @Override
    public void add() throws Exception {
        PerseusServiceLocator.getEcheanceLibreService().create(echeanceLibre);
    }

    @Override
    public void delete() throws Exception {
        PerseusServiceLocator.getEcheanceLibreService().delete(echeanceLibre);
    }

    public EcheanceLibre getEcheanceLibre() {
        return echeanceLibre;
    }

    @Override
    public String getId() {
        return echeanceLibre.getId();
    }

    @Override
    public BSpy getSpy() {
        return new BSpy(echeanceLibre.getSpy());
    }

    @Override
    public void retrieve() throws Exception {
        echeanceLibre = PerseusServiceLocator.getEcheanceLibreService().read(getId());
    }

    public void setEcheanceLibre(EcheanceLibre echeanceLibre) {
        this.echeanceLibre = echeanceLibre;
    }

    @Override
    public void setId(String arg0) {
        echeanceLibre.setId(arg0);
    }

    @Override
    public void update() throws Exception {
        PerseusServiceLocator.getEcheanceLibreService().update(echeanceLibre);
    }

}
