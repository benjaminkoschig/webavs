package ch.globaz.amal.business.services.models.simplepersonneanepaspoursuivre;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import FamilleException.SimplePersonneANePasPoursuivreException;
import ch.globaz.amal.business.models.simplepersonneanepaspoursuivre.SimplePersonneANePasPoursuivre;
import ch.globaz.amal.business.models.simplepersonneanepaspoursuivre.SimplePersonneANePasPoursuivreSearch;

public interface SimplePersonneANePasPoursuivreService extends JadeApplicationService {
    /**
     * Permet la création d'une entité
     * 
     * @param simplePersonneANePasPoursuivreService
     *            l'entité a créer
     * @return l'entité crée
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws SimplePersonneANePasPoursuivreServiceException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimplePersonneANePasPoursuivre create(SimplePersonneANePasPoursuivre simplePersonneANePasPoursuivre)
            throws JadePersistenceException, SimplePersonneANePasPoursuivreException;

    public int count(SimplePersonneANePasPoursuivreSearch simplePersonneANePasPoursuivreSearch)
            throws JadePersistenceException, SimplePersonneANePasPoursuivreException;

    public SimplePersonneANePasPoursuivre delete(SimplePersonneANePasPoursuivre simplePersonneANePasPoursuivre)
            throws JadePersistenceException, SimplePersonneANePasPoursuivreException;

    public SimplePersonneANePasPoursuivre update(SimplePersonneANePasPoursuivre simplePersonneANePasPoursuivre)
            throws JadePersistenceException, SimplePersonneANePasPoursuivreException;

    public SimplePersonneANePasPoursuivreSearch search(
            SimplePersonneANePasPoursuivreSearch simplePersonneANePasPoursuivreSearch) throws JadePersistenceException,
            SimplePersonneANePasPoursuivreException;
}
