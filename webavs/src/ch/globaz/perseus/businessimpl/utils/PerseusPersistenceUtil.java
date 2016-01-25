package ch.globaz.perseus.businessimpl.utils;

import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe utilitaire pour la partie PCF et RP.
 * 
 * @author dcl, rco
 * @version 2.0
 */
public class PerseusPersistenceUtil {

    /**
     * Instanciation vide et impossible.
     */
    private PerseusPersistenceUtil() {
        throw new UnsupportedOperationException();
    }

    /**
     * M�thode retournant une liste typ�e d'apr�s un module de recherche
     * <b>--> duplication du code pour remonter dans FW</b>
     * 
     * @see ch.globaz.pegasus.businessimpl.utils.PersistenceUtil
     * @param search le mod�le de recherche que l'on veut trait�
     * @param t le typde du mod�le
     * @return une liste bas�e sur le mod�le de recherche
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> typeSearch(JadeAbstractSearchModel search, Class<T> t) {
        if (search == null) {
            return null;
        }

        List<T> list = new ArrayList<T>(search.getSize());
        for (JadeAbstractModel modelAbstractModel : search.getSearchResults()) {
            list.add((T) modelAbstractModel);
        }

        return list;
    }
}
