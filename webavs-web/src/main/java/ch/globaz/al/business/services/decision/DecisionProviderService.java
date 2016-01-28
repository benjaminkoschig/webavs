package ch.globaz.al.business.services.decision;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.dossier.DossierComplexModel;

/**
 * @author VYJ
 * 
 *         Interface d�finissant les m�thodes utilis�es pour rechercher le type de d�cision � cr�er
 */
public interface DecisionProviderService extends JadeApplicationService {
    /**
     * M�thode qui retourne la classe ad�quate selon le type de d�cision � imprimer
     * 
     * @param dossierComplex
     *            Un mod�le complexe de dossier AF
     * @return Le type de service a utiliser pour fusionner la d�cision
     * @throws JadeApplicationException
     *             Erreur applicative retourn�e lors de la recherche du service
     */
    public Class getDecisionService(DossierComplexModel dossierComplex) throws JadeApplicationException;
}
