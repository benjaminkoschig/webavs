package ch.globaz.al.business.services.models.rafam;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.rafam.AnnonceRafamDelegueComplexModel;
import ch.globaz.al.business.models.rafam.AnnonceRafamDelegueComplexSearchModel;

public interface AnnonceRafamDelegueComplexModelService extends JadeApplicationService {
    /**
     * R�cup�re les donn�es de l'annonce correspondant � <code>idAnnonce</code>
     * 
     * @param idAnnonce
     *            Id de l'annonce � charger
     * @return Annonce charg�e
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AnnonceRafamDelegueComplexModel read(String idAnnonce) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Recherche les annonces correspondant aux crit�re contenus dans le mod�le de recherche pass� en param�tre
     * 
     * @param search
     *            mod�le contenant les crit�res de recherche
     * @return r�sultat de la recherche
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AnnonceRafamDelegueComplexSearchModel search(AnnonceRafamDelegueComplexSearchModel search)
            throws JadeApplicationException, JadePersistenceException;

}
