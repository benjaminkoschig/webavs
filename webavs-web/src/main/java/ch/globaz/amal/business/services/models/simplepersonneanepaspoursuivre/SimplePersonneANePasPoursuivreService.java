package ch.globaz.amal.business.services.models.simplepersonneanepaspoursuivre;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import FamilleException.SimplePersonneANePasPoursuivreException;
import ch.globaz.amal.business.models.simplepersonneanepaspoursuivre.SimplePersonneANePasPoursuivre;
import ch.globaz.amal.business.models.simplepersonneanepaspoursuivre.SimplePersonneANePasPoursuivreSearch;

public interface SimplePersonneANePasPoursuivreService extends JadeApplicationService {
    /**
     * Permet la cr�ation d'une entit�
     * 
     * @param simplePersonneANePasPoursuivreService
     *            l'entit� a cr�er
     * @return l'entit� cr�e
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws SimplePersonneANePasPoursuivreServiceException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
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
