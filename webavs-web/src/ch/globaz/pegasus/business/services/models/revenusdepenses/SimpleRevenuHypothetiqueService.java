package ch.globaz.pegasus.business.services.models.revenusdepenses;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.CotisationsPsalException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.RevenuHypothetiqueException;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleRevenuHypothetique;

public interface SimpleRevenuHypothetiqueService extends JadeApplicationService {

    /**
     * Permet la création d'une entité SimpleRevenuHypothetique
     * 
     * @param SimpleRevenuHypothetique
     *            L'entité SimpleRevenuHypothetique à créer
     * @return L'entité SimpleRevenuHypothetique créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws RevenuHypothetiqueException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleRevenuHypothetique create(SimpleRevenuHypothetique simpleRevenuHypothetique)
            throws JadePersistenceException, RevenuHypothetiqueException;

    /**
     * Permet la suppression d'une entité SimpleRevenuHypothetique
     * 
     * @param SimpleRevenuHypothetique
     *            L'entité SimpleRevenuHypothetique à supprimer
     * @return L'entité SimpleRevenuHypothetique supprimé
     * @throws CotisationsPsalException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws RevenuHypothetiqueException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleRevenuHypothetique delete(SimpleRevenuHypothetique simpleRevenuHypothetique)
            throws RevenuHypothetiqueException, JadePersistenceException;

    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException;

    /**
     * Permet de charger en mémoire d'une entité SimpleRevenuHypothetique
     * 
     * @param idRevenuHypothetique
     *            L'identifiant de l'entité SimpleRevenuHypothetique à charger en mémoire
     * @return L'entité SimpleRevenuHypothetique chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws RevenuHypothetiqueException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleRevenuHypothetique read(String idRevenuHypothetique) throws JadePersistenceException,
            RevenuHypothetiqueException;

    /**
     * 
     * Permet la mise à jour d'une entité SimpleRevenuHypothetique
     * 
     * @param SimpleRevenuHypothetique
     *            L'entité SimpleRevenuHypothetique à mettre à jour
     * @return L'entité SimpleRevenuHypothetique mise à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws RevenuHypothetiqueException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleRevenuHypothetique update(SimpleRevenuHypothetique simpleRevenuHypothetique)
            throws JadePersistenceException, RevenuHypothetiqueException;
}
