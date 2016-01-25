package globaz.perseus.vb.parametres;

import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import ch.globaz.perseus.business.models.parametres.Loyer;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

public class PFLoyerViewBean extends BJadePersistentObjectViewBean {

    private Loyer loyer = null;

    public PFLoyerViewBean() {
        super();
        loyer = new Loyer();
    }

    public PFLoyerViewBean(Loyer loyer) {
        super();
        this.loyer = loyer;
    }

    @Override
    public void add() throws Exception {
        PerseusServiceLocator.getLoyerService().create(loyer);
    }

    @Override
    public void delete() throws Exception {
        PerseusServiceLocator.getLoyerService().delete(loyer);
    }

    /**
     * @return the dateDebut
     */
    public String getDateDebutConverter() {
        return (!JadeStringUtil.isEmpty(loyer.getSimpleLoyer().getDateDebut())) ? loyer.getSimpleLoyer().getDateDebut()
                .substring(3) : "";
    }

    /**
     * @return the dateFin
     */
    public String getDateFinConverter() {
        return (!JadeStringUtil.isEmpty(loyer.getSimpleLoyer().getDateFin())) ? loyer.getSimpleLoyer().getDateFin()
                .substring(3) : "";
    }

    @Override
    public String getId() {
        return loyer.getId();
    }

    public Loyer getLoyer() {
        return loyer;
    }

    @Override
    public BSpy getSpy() {
        return new BSpy(loyer.getSpy());
    }

    @Override
    public void retrieve() throws Exception {
        loyer = PerseusServiceLocator.getLoyerService().read(loyer.getId());
    }

    /**
     * @param dateDebut
     *            the dateDebut to set
     */
    public void setDateDebutConverter(String dateDebut) {
        if (!JadeStringUtil.isEmpty(dateDebut)) {
            dateDebut = "01." + dateDebut;
            loyer.getSimpleLoyer().setDateDebut(dateDebut);
        }
    }

    /**
     * @param dateFin
     *            the dateFin to set
     */
    public void setDateFinConverter(String dateFin) {
        if (!JadeStringUtil.isEmpty(dateFin)) {
            dateFin = "01." + dateFin;
            dateFin = JadeDateUtil.addMonths(dateFin, 1);
            dateFin = JadeDateUtil.addDays(dateFin, -1);
            loyer.getSimpleLoyer().setDateFin(dateFin);
        } else {
            dateFin = null;
            loyer.getSimpleLoyer().setDateFin(dateFin);
        }
    }

    @Override
    public void setId(String newId) {
        loyer.setId(newId);
    }

    public void setLoyer(Loyer loyer) {
        this.loyer = loyer;
    }

    @Override
    public void update() throws Exception {
        PerseusServiceLocator.getLoyerService().update(loyer);
    }
}
