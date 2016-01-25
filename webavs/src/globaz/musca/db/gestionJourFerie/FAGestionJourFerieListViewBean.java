package globaz.musca.db.gestionJourFerie;

import globaz.framework.bean.FWListViewBeanInterface;
import java.util.ArrayList;

/**
 * Représente le model de la vue "_rcListe"
 * 
 * @author Pascal Lovy, 03-dec-2004
 */
public class FAGestionJourFerieListViewBean extends FAGestionJourFerieManager implements FWListViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Attributs
     */
    private ArrayList<String> domaineFerie = new ArrayList<String>();

    /**
     * GETTER
     */
    public ArrayList<String> getDomaineFerie() {
        return domaineFerie;
    }

    /**
     * SETTER
     */
    public void setDomaineFerie(ArrayList<String> newDomaineFerie) {
        domaineFerie = newDomaineFerie;
    }

}
