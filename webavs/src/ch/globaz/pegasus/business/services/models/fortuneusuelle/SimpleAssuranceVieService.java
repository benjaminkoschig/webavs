package ch.globaz.pegasus.business.services.models.fortuneusuelle;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.AssuranceVieException;
import ch.globaz.pegasus.business.models.fortuneusuelle.SimpleAssuranceVie;

public interface SimpleAssuranceVieService extends JadeApplicationService {

    /**
     * Permet la création d'une entité SimpleAssuranceVie
     * 
     * @param simpleAssuranceVie
     *            L'entité simpleAssuranceVie à créer
     * @return L'entité simpleAssuranceVie créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AssuranceVieException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleAssuranceVie create(SimpleAssuranceVie simpleAssuranceVie) throws JadePersistenceException,
            AssuranceVieException;

    /**
     * Permet la suppression d'une entité SimpleAssuranceVie
     * 
     * @param SimpleAssuranceVie
     *            L'entité SimpleAssuranceVie à supprimer
     * @return L'entité SimpleAssuranceVie supprimé
     * @throws AssuranceVieException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleAssuranceVie delete(SimpleAssuranceVie simpleAssuranceVie) throws AssuranceVieException,
            JadePersistenceException;

    public void deleteParListeIdDoFinH(List<String> idsDonneFinanciere) throws JadePersistenceException;

    /**
     * Permet de charger en mémoire d'une entité SimpleAssuranceVie
     * 
     * @param idAssuranceVie
     *            L'identifiant de l'entité SimpleAssuranceVie à charger en mémoire
     * @return L'entité SimpleAssuranceVie chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AssuranceVieException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleAssuranceVie read(String idAssuranceVie) throws JadePersistenceException, AssuranceVieException;

    /**
     * 
     * Permet la mise à jour d'une entité SimpleAssuranceVie
     * 
     * @param SimpleAssuranceVie
     *            L'entité SimpleAssuranceVie à mettre à jour
     * @return L'entité SimpleAssuranceVie mise à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AssuranceVieException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleAssuranceVie update(SimpleAssuranceVie simpleAssuranceVie) throws JadePersistenceException,
            AssuranceVieException;

}
