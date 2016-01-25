package globaz.hermes.db.parametrage;

import globaz.framework.bean.FWListViewBeanInterface;

/**
 * Insérez la description du type ici. Date de création : (04.11.2002 13:05:53)
 * 
 * @author: Administrator
 */
public class HECriteremotifListViewBean extends HECriteremotifManager implements FWListViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Commentaire relatif au constructeur HECriteremotifListViewBean.
     */
    public HECriteremotifListViewBean() {
        super();
    }

    public String getCsIdcritere(int pos) {
        /*
         * // On triche, on va chercher le resultat dans la liste du manager HECriteremotif entity = (HECriteremotif)
         * liste.elementAt(pos); // On ajoute une jolie tabulation pour distinguer les niveaux StringBuffer indent = new
         * StringBuffer(entity.getLevel()); indent.setCharAt(0, '-'); return indent +
         * entity.getCsIdcritere(getSession()).getCurrentCodeUtilisateur ().getLibelle();
         */
        return null;
    }

    public String getFatherId(int pos) {
        // on triche, on va chercher les résultats dans le vecteur liste
        HECriteremotif entity = (HECriteremotif) liste.elementAt(pos);
        return entity.getFatherId();
    }

    public String getIdCode(int pos) {
        // on triche, on va chercher les résultats dans le veteur liste
        HECriteremotif entity = (HECriteremotif) liste.elementAt(pos);
        return entity.getId();
    }

    public String getNb(int pos) {
        HECriteremotif entity = (HECriteremotif) liste.elementAt(pos);
        return entity.getLevel();
    }
}
