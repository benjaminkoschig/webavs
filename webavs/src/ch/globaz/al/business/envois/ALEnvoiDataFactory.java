/**
 * 
 */
package ch.globaz.al.business.envois;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import ch.globaz.envoi.business.models.parametrageEnvoi.FormuleList;
import ch.globaz.envoi.business.models.parametrageEnvoi.FormuleListSearch;
import ch.globaz.envoi.business.services.ENServiceLocator;

/**
 * @author dhi
 * 
 */
public class ALEnvoiDataFactory {

    /**
     * Gets le correct ALEnvoiData en fonction de la formule. Par défaut, renvoi le ALEnvoiData mère
     * 
     * @param idFormule
     * @return
     */
    public static ALEnvoiData getALEnvoiData(HashMap<Object, Object> inputMap, String idFormule) {
        // retrieve la formule et get le nom (ACREPC2 par exemple)
        try {
            FormuleListSearch formuleListSearch = new FormuleListSearch();
            formuleListSearch.setForIdFormule(idFormule);
            formuleListSearch.setDefinedSearchSize(0);
            formuleListSearch = ENServiceLocator.getFormuleListService().search(formuleListSearch);

            if (formuleListSearch.getSize() > 0) {
                FormuleList formule = (FormuleList) formuleListSearch.getSearchResults()[0];
                String formuleName = formule.getDefinitionformule().getCsDocument();
                // Essaie d'invocation, si echec, dans le catch.
                Class envoiDataSpecific = Class.forName(ALEnvoiData.class.getName() + formuleName);
                Class[] argsClasses = new Class[] { HashMap.class, String.class };
                Object[] argsValues = new Object[] { inputMap, idFormule };
                Constructor envoiDataSpecificConstructor = envoiDataSpecific.getConstructor(argsClasses);
                return (ALEnvoiData) envoiDataSpecificConstructor.newInstance(argsValues);
            } else {
                return new ALEnvoiData(inputMap, idFormule);
            }
        } catch (Exception ex) {
            return new ALEnvoiData(inputMap, idFormule);
        }
    }

}
