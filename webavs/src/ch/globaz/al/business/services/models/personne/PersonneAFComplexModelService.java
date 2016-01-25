package ch.globaz.al.business.services.models.personne;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.personne.PersonneAFComplexSearchModel;

/**
 * Fournit les services sur des personnes AF (Allocataire et enfant)
 * 
 * @author GMO
 * @see ch.globaz.al.business.models.personne.PersonneAFComplexSearchModel
 */
public interface PersonneAFComplexModelService extends JadeApplicationService {

    /**
     * Recherche les personnes AF (enfant ou allocataire) correspondant aux critères des recherches contenu dans
     * <code>personneAFComplexModel</code>
     * 
     * @param personneAFComplexModel
     *            modèle de recherche
     * @return Résultat de la recherche
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public PersonneAFComplexSearchModel search(PersonneAFComplexSearchModel personneAFComplexModel)
            throws JadeApplicationException, JadePersistenceException;

}
