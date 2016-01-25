package globaz.naos.db.tauxAssurance;

import globaz.framework.bean.FWViewBeanInterface;

/**
 * Le listViewBean de l'entité TauxAssurance.
 * 
 * @author administrator
 */
public class AFTauxMoyenListViewBean extends AFTauxMoyenManager implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public String getAnnee(int index) {
        return ((AFTauxMoyen) getEntity(index)).getAnnee();
    }

    public Boolean getBlocage(int index) {
        return ((AFTauxMoyen) getEntity(index)).getBlocage();
    }

    public String getMasseAnnuelle(int index) {
        return ((AFTauxMoyen) getEntity(index)).getMasseAnnuelle();
    }

    public String getNbrMois(int index) {
        return ((AFTauxMoyen) getEntity(index)).getNbrMois();
    }

    public String getTauxMoyenId(int index) {
        return ((AFTauxMoyen) getEntity(index)).getTauxMoyenId();
    }

    public String getTauxTotal(int index) {
        return String.valueOf(((AFTauxMoyen) getEntity(index)).getTauxTotal());
    }

}
