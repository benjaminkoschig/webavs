package ch.globaz.al.business.services.models.prestation;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.prestation.RecapitulatifEntrepriseModel;
import ch.globaz.al.business.models.prestation.RecapitulatifEntrepriseSearchModel;

/**
 * service de gestion des donn�es inh�rentes � RecapitulatifEntreprise dans Prestation
 * 
 * @author PTA
 */
public interface RecapitulatifEntrepriseModelService extends JadeApplicationService {

    /**
     * Retourne le nombre de r�cap trouv�es par le mod�le de recherche
     * 
     * @param recapSearch
     *            mod�le contenant les crit�res de recherche
     * @return nombre d'enregistrement correspondant � la recherche
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public int count(RecapitulatifEntrepriseSearchModel recapSearch) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Cr�ation d'un r�capitulatif entreprise selon le mod�le pass� en param�tre
     * 
     * @param recapEntrepriseModel
     *            mod�le � enregistrer
     * @return le mod�le enregistr�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public RecapitulatifEntrepriseModel create(RecapitulatifEntrepriseModel recapEntrepriseModel)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Suppression d'un r�capitulatif entreprise selon le mod�le pass� en param�tre
     * 
     * @param recapEntrepriseModel
     *            le mod�le � supprimer
     * @return le mod�le supprim�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public RecapitulatifEntrepriseModel delete(RecapitulatifEntrepriseModel recapEntrepriseModel)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Efface un r�capitulatif entreprise selon le mod�le pass� en param�tre si il ne contient n prestations o� n=
     * <code>size</code>
     * 
     * @param idRecap
     *            le mod�le � (�ventuellement) effacer
     * @param size
     *            le nombre de prestations contenues dans la r�cap
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public void deleteIfSizeEquals(String idRecap, int size) throws JadeApplicationException, JadePersistenceException;

    /**
     * Lecture d'un r�capitulatif entreprise selon son identifiant pass� en param�tre
     * 
     * @param idRecapEntrepriseModel
     *            id du mod�le � charger
     * @return le mod�le charg�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public RecapitulatifEntrepriseModel read(String idRecapEntrepriseModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Effectue une recherche sur <code>recapSearch</code>
     * 
     * @param recapSearch
     *            mod�le contenant les crit�res de recherche
     * @return mod�le contenant le r�sultat de la recherche
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public RecapitulatifEntrepriseSearchModel search(RecapitulatifEntrepriseSearchModel recapSearch)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * 
     * Mise � jour d'un r�capitulatif entreprise selon le mod�le pass� en param�tre
     * 
     * @param recapEntrepriseModel
     *            le mod�le � mettre � jour
     * @return le mod�le mis � jour
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public RecapitulatifEntrepriseModel update(RecapitulatifEntrepriseModel recapEntrepriseModel)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Effectue une recherche sur <code>recapSearch</code>, � appeler uniquement depuis les �crans dans tablib
     * <ct:widget
     * 
     * @param recapSearch
     *            mod�le contenant les crit�res de recherche
     * @return mod�le contenant le r�sultat de la recherche
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public RecapitulatifEntrepriseSearchModel widgetFind(RecapitulatifEntrepriseSearchModel recapSearch)
            throws JadeApplicationException, JadePersistenceException;
}
