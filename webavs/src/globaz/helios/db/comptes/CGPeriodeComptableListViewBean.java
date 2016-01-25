package globaz.helios.db.comptes;

/**
 * Insérez la description du type ici. Date de création : (17.09.2002 16:45:19)
 * 
 * @author: Administrator
 */
public class CGPeriodeComptableListViewBean extends CGPeriodeComptableManager implements
        globaz.framework.bean.FWListViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Commentaire relatif au constructeur CGPeriodeComptableListViewBean.
     */
    public CGPeriodeComptableListViewBean() {
        super();
    }

    public String getCode(int pos) {
        CGPeriodeComptable entity = (CGPeriodeComptable) getEntity(pos);
        return entity.getCode();

    }

    public String getDateDebut(int pos) {
        CGPeriodeComptable entity = (CGPeriodeComptable) getEntity(pos);
        return entity.getDateDebut();
    }

    public String getDateFin(int pos) {
        CGPeriodeComptable entity = (CGPeriodeComptable) getEntity(pos);
        return entity.getDateFin();
    }

    public String getIdPeriodeComptable(int pos) {
        CGPeriodeComptable entity = (CGPeriodeComptable) getEntity(pos);
        return entity.getIdPeriodeComptable();

    }

    public String getPeriodeComptable(int pos) {
        CGPeriodeComptable entity = (CGPeriodeComptable) getEntity(pos);

        return entity.getFullDescription();

    }

    public boolean isCloture(int pos) {
        CGPeriodeComptable entity = (CGPeriodeComptable) getEntity(pos);
        return entity.isCloture();
    }

    public Boolean isEstCloture(int pos) {
        CGPeriodeComptable entity = (CGPeriodeComptable) getEntity(pos);
        return entity.isEstCloture();
    }
}
