package globaz.pegasus.vb.home;

import globaz.framework.bean.JadeAbstractAjaxFindViewBean;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.pegasus.business.models.home.PeriodeServiceEtat;
import ch.globaz.pegasus.business.models.home.SimpleHome;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class PCPeriodeAjaxViewBean extends JadeAbstractAjaxFindViewBean {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private boolean getListe = true;

    private transient PCPeriodeAjaxListViewBean listePeriodes;

    private PeriodeServiceEtat periodeServiceEtat = null;

    public PCPeriodeAjaxViewBean() {
        super();
        periodeServiceEtat = new PeriodeServiceEtat();
        initList();
    }

    public PCPeriodeAjaxViewBean(PeriodeServiceEtat periodeServiceEtat) {
        super();
        initList();
        this.periodeServiceEtat = periodeServiceEtat;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        periodeServiceEtat = PegasusServiceLocator.getHomeService().createPeriode(periodeServiceEtat.getSimpleHome(),
                periodeServiceEtat);
        this.updateListe();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        String idHome = periodeServiceEtat.getSimplePeriodeServiceEtat().getIdHome();
        periodeServiceEtat = PegasusServiceLocator.getHomeService().deletePeriode(periodeServiceEtat.getSimpleHome(),
                periodeServiceEtat);
        this.updateListe(idHome);
    }

    @Override
    public void find() throws Exception {
        listePeriodes.find();
    }

    @Override
    public JadeAbstractModel getCurrentEntity() {
        return periodeServiceEtat;
    }

    public SimpleHome getHome() {
        return periodeServiceEtat.getSimpleHome();
    }

    /**
     * Donne la période d'un service de l'etat
     * 
     * @return
     */
    public String getPeriode() {
        return periodeServiceEtat.getSimplePeriodeServiceEtat().getDateDebut().trim() + " - "
                + periodeServiceEtat.getSimplePeriodeServiceEtat().getDateFin().trim();
    }

    /**
     * @return the periodeServiceEtat
     */
    public PeriodeServiceEtat getPeriodeServiceEtat() {
        return periodeServiceEtat;
    }

    @Override
    public JadeAbstractSearchModel getSearchModel() {
        return listePeriodes.getManagerModel();
    }

    @Override
    public void initList() {
        listePeriodes = new PCPeriodeAjaxListViewBean();
    }

    /**
     * @return the getListe
     */
    public boolean isGetListe() {
        return getListe;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        periodeServiceEtat = PegasusServiceLocator.getHomeService().readPeriode(periodeServiceEtat.getId());
    }

    /**
     * @param periodeServiceEtat
     *            the periodeServiceEtat to set
     */
    public void setPeriodeServiceEtat(PeriodeServiceEtat periodeServiceEtat) {
        this.periodeServiceEtat = periodeServiceEtat;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        periodeServiceEtat = (PegasusServiceLocator.getHomeService().updatePeriode(periodeServiceEtat.getSimpleHome(),
                periodeServiceEtat));
        this.updateListe();
    }

    private void updateListe() throws Exception {
        this.updateListe(periodeServiceEtat.getSimplePeriodeServiceEtat().getIdHome());
    }

    private void updateListe(String idHome) throws Exception {
        if (getListe) {
            listePeriodes = new PCPeriodeAjaxListViewBean();
            listePeriodes.getPeriodeSearch().setForIdHome(idHome);
            listePeriodes.find();
        }
    }
}
