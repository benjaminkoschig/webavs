/**
 * 
 */
package ch.globaz.amal.business.services.models.parametreapplication;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.amal.business.exceptions.models.parametreapplication.ParametreApplicationException;
import ch.globaz.amal.business.models.parametreapplication.SimpleParametreApplication;
import ch.globaz.amal.business.models.parametreapplication.SimpleParametreApplicationSearch;

/**
 * @author dhi
 * 
 */
public interface SimpleParametreApplicationService extends JadeApplicationService {

    /**
     * Renseignement du nombre d'enregistrement pr�sent selon les param�tres de recherches
     * 
     * @param search
     *            param�tres de recherche de SimpleParametreApplicationSearch
     * @return Le nombre d'enregistrement trouv�
     * @throws ParametreApplicationException
     * @throws JadePersistenceException
     */
    public int count(SimpleParametreApplicationSearch search) throws ParametreApplicationException,
            JadePersistenceException;

    /**
     * Cr�ation d'un enregistrement de type SimpleParametreApplication
     * 
     * @param simpleParametreApplication
     *            Le mod�le de donn�es renseign�
     * @return Le mod�le de donn�es renseign�, apr�s cr�ation en DB (isNew = false et PSPY, CSPY cr��s)
     * @throws ParametreApplicationException
     * @throws JadePersistenceException
     */
    public SimpleParametreApplication create(SimpleParametreApplication simpleParametreApplication)
            throws ParametreApplicationException, JadePersistenceException;

    /**
     * Effacement d'un enregistrement de type SimpleParametreApplication
     * 
     * @param simpleParametreApplication
     *            Le mod�le de donn�es renseign�, � effacer
     * @return
     * @throws JadePersistenceException
     * @throws ParametreApplicationException
     */
    public SimpleParametreApplication delete(SimpleParametreApplication simpleParametreApplication)
            throws JadePersistenceException, ParametreApplicationException;

    /**
     * Lecture d'un enregistrement de type SimpleParametreApplication
     * 
     * @param idParametreApplication
     *            L'id de l'enregistrement � lire
     * @return Le mod�le de donn�es renseign�
     * @throws JadePersistenceException
     * @throws ParametreApplicationException
     */
    public SimpleParametreApplication read(String idParametreApplication) throws JadePersistenceException,
            ParametreApplicationException;

    /**
     * Recherche d'un enregistrement de type SimpleParametreApplication, selon mod�le de recherche
     * 
     * @param parametreApplicationSearch
     *            Le mod�le de recherche renseign� avec les param�tres de recherche
     * @return Le mod�le de recherche renseign� avec les r�sultats obtenu
     * @throws JadePersistenceException
     * @throws ParametreApplicationException
     */
    public SimpleParametreApplicationSearch search(SimpleParametreApplicationSearch parametreApplicationSearch)
            throws JadePersistenceException, ParametreApplicationException;

    /**
     * Mise � jour d'un enregistrement selon mod�le de donn�es SimpleParametreApplication
     * 
     * @param simpleParametreApplication
     *            mod�le de donn�es renseign�
     * @return le m�me mod�le de donn�es renseign�
     * @throws ParametreApplicationException
     * @throws JadePersistenceException
     */
    public SimpleParametreApplication update(SimpleParametreApplication simpleParametreApplication)
            throws ParametreApplicationException, JadePersistenceException;

}
