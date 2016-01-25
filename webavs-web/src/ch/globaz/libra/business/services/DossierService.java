package ch.globaz.libra.business.services;

import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.libra.business.exceptions.LibraException;

public interface DossierService extends JadeApplicationService {

    /**
     * [2] Clôture d'un dossier
     * 
     * -> Changement de l'état en "Clôturé"
     * 
     * @param transaction
     * @param idExterne
     * 
     * @throws LibraException
     *             Levée en cas de problème lors de la création de...
     */
    public void clotureDossier(String idExterne) throws LibraException;

    /**
     * [1] Création de dossier simple
     * 
     * -> Dossier de base -> Groupe et Utilisateur par défaut selon domaine fourni -> Si dossier déjà existant,
     * réactivation si clos, si déjà ouvert, msg d'erreur
     * 
     * @param transaction
     * @param idTiers
     * @param csDomaine
     * @param idExterne
     * 
     * @throws LibraException
     *             Levée en cas de problème lors de la création de...
     */
    public void createDossier(String idTiers, String csDomaine, String idExterne) throws LibraException;

    /**
     * [3] Réactivation d'un dossier
     * 
     * -> Changement de l'état "Clôturé" en "Ouvert"
     * 
     * @param transaction
     * @param idExterne
     * 
     * @throws LibraException
     *             Levée en cas de problème lors de la création de...
     */
    public void reactivationDossier(String idExterne) throws LibraException;

}
