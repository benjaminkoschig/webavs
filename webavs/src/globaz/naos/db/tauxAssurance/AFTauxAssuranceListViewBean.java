package globaz.naos.db.tauxAssurance;

import globaz.framework.bean.FWViewBeanInterface;

/**
 * Le listViewBean de l'entité TauxAssurance.
 * 
 * @author administrator
 */
public class AFTauxAssuranceListViewBean extends AFTauxAssuranceManager implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public String getCategorieId(int index) {
        return ((AFTauxAssurance) getEntity(index)).getCategorieId();
    }

    public String getDateDebut(int index) {
        return ((AFTauxAssurance) getEntity(index)).getDateDebut();
    }

    public String getFraction(int index) {
        return ((AFTauxAssurance) getEntity(index)).getFraction();
    }

    public String getGenreValeur(int index) {
        return ((AFTauxAssurance) getEntity(index)).getGenreValeur();
    }

    public String getPeriodiciteMontant(int index) {
        return ((AFTauxAssurance) getEntity(index)).getPeriodiciteMontant();
    }

    public String getRang(int index) {
        return ((AFTauxAssurance) getEntity(index)).getRang();
    }

    public String getSexe(int index) {
        return ((AFTauxAssurance) getEntity(index)).getSexe();
    }

    public String getTauxAssuranceId(int index) {
        return ((AFTauxAssurance) getEntity(index)).getTauxAssuranceId();
    }

    public String getTranche(int index) {
        return ((AFTauxAssurance) getEntity(index)).getTranche();
    }

    public String getTypeId(int index) {
        return ((AFTauxAssurance) getEntity(index)).getTypeId();
    }

    public String getValeurTotal(int index) {
        return ((AFTauxAssurance) getEntity(index)).getValeurTotal();
    }

}
