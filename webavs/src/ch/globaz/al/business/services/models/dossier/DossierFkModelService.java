package ch.globaz.al.business.services.models.dossier;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.dossier.DossierFkSearchModel;

/**
 * Services li�s � la persistance des dossier
 * 
 * @author PTA
 */
public interface DossierFkModelService extends JadeApplicationService {

    /**
     * Retourne le nombre de dossier trouv� par le mod�le de recherche
     * 
     * @param dossierFkSearch
     *            mod�le de recherche de dossier
     * @return nombre de dossiers correspondant aux crit�res de recherche
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public int count(DossierFkSearchModel dossierFkSearch) throws JadePersistenceException, JadeApplicationException;

    /**
     * Effectue la recherche selon le mod�le de recherche pass� en param�tre
     * 
     * @param dossierFkSearch
     *            mod�le de recherche de dossier
     * @return R�sultat de la recherche
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DossierFkSearchModel search(DossierFkSearchModel dossierFkSearch) throws JadeApplicationException,
            JadePersistenceException;

}
