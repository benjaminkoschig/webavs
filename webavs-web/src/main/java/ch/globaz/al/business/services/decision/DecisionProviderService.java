package ch.globaz.al.business.services.decision;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.dossier.DossierComplexModel;

/**
 * @author VYJ
 * 
 *         Interface définissant les méthodes utilisées pour rechercher le type de décision à créer
 */
public interface DecisionProviderService extends JadeApplicationService {
    /**
     * Méthode qui retourne la classe adéquate selon le type de décision à imprimer
     * 
     * @param dossierComplex
     *            Un modèle complexe de dossier AF
     * @return Le type de service a utiliser pour fusionner la décision
     * @throws JadeApplicationException
     *             Erreur applicative retournée lors de la recherche du service
     */
    public Class getDecisionService(DossierComplexModel dossierComplex) throws JadeApplicationException;
}
