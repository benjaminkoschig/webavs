package ch.globaz.al.business.services.models.rafam;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.rafam.ComplementDelegueModel;
import ch.globaz.al.business.models.rafam.ComplementDelegueSearchModel;

public interface ComplementDelegueModelService extends JadeApplicationService {
    /**
     * Compte le nombre d'enregistrement retourn�s par le mod�le de recherche pass� en param�tre
     * 
     * @param search
     *            Mod�le de recherche contenant les crit�res
     * @return nombre de copies correspondant aux crit�res de recherche
     * 
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * 
     * @see ch.globaz.al.business.models.dossier.CopieModel
     */
    public int count(ComplementDelegueSearchModel search) throws JadeApplicationException, JadePersistenceException;

    /**
     * Enregistre <code>model</code> en persistance
     * 
     * @param model
     *            Annonce � enregistrer
     * @return Annonce enregistr�e
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     * @see ch.globaz.al.business.models.rafam.OverlapInformationModel
     */
    public ComplementDelegueModel create(ComplementDelegueModel model) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Supprime <code>model</code> de la persistance
     * 
     * @param model
     *            Annonce � supprimer
     * @return l'erreur d'annonce supprim�e
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     * @see ch.globaz.al.business.models.rafam.OverlapInformationModel
     */
    public ComplementDelegueModel delete(ComplementDelegueModel model) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * R�cup�re les donn�es de l'erreur d'annonce correspondant � <code>idErreurAnnonce</code>
     * 
     * @param idAnnonce
     *            Id de l'erreur d'annonce � charger
     * @return Annonce charg�e
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     * @see ch.globaz.al.business.models.rafam.OverlapInformationModel
     */
    public ComplementDelegueModel read(String idErreurAnnonce) throws JadeApplicationException,
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
    public ComplementDelegueSearchModel search(ComplementDelegueSearchModel search) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Met � jour les donn�es de <code>OverlapInformationModel</code> en persistance
     * 
     * @param model
     *            Annonce � mettre � jour
     * @return Annonce mise � jour
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     * @see ch.globaz.al.business.models.rafam.OverlapInformationModel
     */
    public ComplementDelegueModel update(ComplementDelegueModel model) throws JadeApplicationException,
            JadePersistenceException;

}
