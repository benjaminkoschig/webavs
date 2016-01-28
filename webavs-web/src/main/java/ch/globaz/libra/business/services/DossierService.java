package ch.globaz.libra.business.services;

import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.libra.business.exceptions.LibraException;

public interface DossierService extends JadeApplicationService {

    /**
     * [2] Cl�ture d'un dossier
     * 
     * -> Changement de l'�tat en "Cl�tur�"
     * 
     * @param transaction
     * @param idExterne
     * 
     * @throws LibraException
     *             Lev�e en cas de probl�me lors de la cr�ation de...
     */
    public void clotureDossier(String idExterne) throws LibraException;

    /**
     * [1] Cr�ation de dossier simple
     * 
     * -> Dossier de base -> Groupe et Utilisateur par d�faut selon domaine fourni -> Si dossier d�j� existant,
     * r�activation si clos, si d�j� ouvert, msg d'erreur
     * 
     * @param transaction
     * @param idTiers
     * @param csDomaine
     * @param idExterne
     * 
     * @throws LibraException
     *             Lev�e en cas de probl�me lors de la cr�ation de...
     */
    public void createDossier(String idTiers, String csDomaine, String idExterne) throws LibraException;

    /**
     * [3] R�activation d'un dossier
     * 
     * -> Changement de l'�tat "Cl�tur�" en "Ouvert"
     * 
     * @param transaction
     * @param idExterne
     * 
     * @throws LibraException
     *             Lev�e en cas de probl�me lors de la cr�ation de...
     */
    public void reactivationDossier(String idExterne) throws LibraException;

}
