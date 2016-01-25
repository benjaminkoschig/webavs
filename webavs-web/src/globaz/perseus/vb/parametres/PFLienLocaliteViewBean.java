package globaz.perseus.vb.parametres;

import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import ch.globaz.perseus.business.models.parametres.LienLocalite;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

/**
 * 
 * @author MBO
 * 
 */
public class PFLienLocaliteViewBean extends BJadePersistentObjectViewBean {

    private LienLocalite lienLocalite = null;

    public PFLienLocaliteViewBean() {
        super();
        lienLocalite = new LienLocalite();
    }

    public PFLienLocaliteViewBean(LienLocalite lienLocalite) {
        super();
        this.lienLocalite = lienLocalite;
    }

    @Override
    public void add() throws Exception {
        PerseusServiceLocator.getLienLocaliteService().create(lienLocalite);
    }

    @Override
    public void delete() throws Exception {
        PerseusServiceLocator.getLienLocaliteService().delete(lienLocalite);
    }

    /**
     * @return the dateDebut
     */
    public String getDateDebutConverter() {
        return (!JadeStringUtil.isEmpty(lienLocalite.getSimpleLienLocalite().getDateDebut())) ? lienLocalite
                .getSimpleLienLocalite().getDateDebut().substring(3) : "";
    }

    /**
     * @return the dateFin
     */
    public String getDateFinConverter() {
        return (!JadeStringUtil.isEmpty(lienLocalite.getSimpleLienLocalite().getDateFin())) ? lienLocalite
                .getSimpleLienLocalite().getDateFin().substring(3) : "";
    }

    @Override
    public String getId() {
        return lienLocalite.getId();
    }

    public LienLocalite getLienLocalite() {
        return lienLocalite;
    }

    @Override
    public BSpy getSpy() {
        return new BSpy(lienLocalite.getSpy());
    }

    @Override
    public void retrieve() throws Exception {
        lienLocalite = PerseusServiceLocator.getLienLocaliteService().read(lienLocalite.getId());
    }

    /**
     * @param dateDebut
     *            the dateDebut to set
     */
    public void setDateDebutConverter(String dateDebut) {
        if (!JadeStringUtil.isEmpty(dateDebut)) {
            dateDebut = "01." + dateDebut;
            lienLocalite.getSimpleLienLocalite().setDateDebut(dateDebut);
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
            lienLocalite.getSimpleLienLocalite().setDateFin(dateFin);
        } else {
            dateFin = null;
            lienLocalite.getSimpleLienLocalite().setDateFin(dateFin);
        }
    }

    @Override
    public void setId(String newId) {
        lienLocalite.setId(newId);
    }

    public void setLienLocalite(LienLocalite lienLocalite) {
        this.lienLocalite = lienLocalite;
    }

    @Override
    public void update() throws Exception {
        PerseusServiceLocator.getLienLocaliteService().update(lienLocalite);
    }
}
