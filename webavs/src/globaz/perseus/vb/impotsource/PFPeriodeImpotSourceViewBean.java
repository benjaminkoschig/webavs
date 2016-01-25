package globaz.perseus.vb.impotsource;

import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import ch.globaz.perseus.business.models.impotsource.PeriodeImpotSource;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

public class PFPeriodeImpotSourceViewBean extends BJadePersistentObjectViewBean {

    private String isGeneree;
    private PeriodeImpotSource periode;

    public PFPeriodeImpotSourceViewBean() {
        super();
        setPeriode(new PeriodeImpotSource());

    }

    public PFPeriodeImpotSourceViewBean(PeriodeImpotSource periode) {
        super();
        setPeriode(periode);

    }

    @Override
    public void add() throws Exception {
        if ("on".equals(getIsGeneree())) {
            periode.getSimplePeriodeImpotSource().setPeriodeGeneree(Boolean.TRUE);
        } else {
            periode.getSimplePeriodeImpotSource().setPeriodeGeneree(Boolean.FALSE);
        }
        periode = PerseusServiceLocator.getPeriodeImpotSourceService().create(periode);

    }

    @Override
    public void delete() throws Exception {
        periode = PerseusServiceLocator.getPeriodeImpotSourceService().delete(periode);

    }

    @Override
    public String getId() {
        return periode.getId();
    }

    public String getIsGeneree() {
        return isGeneree;
    }

    public PeriodeImpotSource getPeriode() {
        return periode;
    }

    /**
     * Retourne l'objet session
     * 
     * @return objet BSession
     */
    public BSession getSession() {
        return (BSession) getISession();
    }

    @Override
    public BSpy getSpy() {
        return (periode != null) && !periode.isNew() ? new BSpy(periode.getSpy()) : new BSpy(getSession());
    }

    @Override
    public void retrieve() throws Exception {
        periode = PerseusServiceLocator.getPeriodeImpotSourceService().read(
                periode.getSimplePeriodeImpotSource().getIdPeriode());
    }

    @Override
    public void setId(String newId) {
        periode.setId(newId);

    }

    public void setIsGeneree(String isGeneree) {
        this.isGeneree = isGeneree;
    }

    public void setPeriode(PeriodeImpotSource periode) {
        this.periode = periode;
    }

    @Override
    public void update() throws Exception {
        if ("on".equals(getIsGeneree())) {
            periode.getSimplePeriodeImpotSource().setPeriodeGeneree(Boolean.TRUE);
        } else {
            periode.getSimplePeriodeImpotSource().setPeriodeGeneree(Boolean.FALSE);
        }
        periode = PerseusServiceLocator.getPeriodeImpotSourceService().update(periode);

    }

}
