package ch.globaz.al.business.services.models.prestation;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.prestation.DetailPrestationGenComplexSearchModel;

/**
 * Service de gestion de persistance des donn�es du mod�le <code>DetailPrestationComlexModel</code>
 * 
 * @author jts
 * 
 * @see ch.globaz.al.business.models.prestation.DetailPrestationGenComplexModel
 */
public interface DetailPrestationGenComplexModelService extends JadeApplicationService {

    /**
     * 
     * @param searchModel
     *            Le mod�le de recherche
     * @return Le nombre de projets selon le searchModel sp�cifi�
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    public int count(DetailPrestationGenComplexSearchModel searchModel) throws JadeApplicationException,
            JadePersistenceException;

    /***
     * Recherche si il existe d�j� des prestations pour le couple idDossier et idDroit durant une p�riode sp�cifi�e
     * 
     * @param idDossier
     *            ID de la table Dossier
     * @param idDroit
     *            ID de la table Droits
     * @param periodeDe
     *            P�riode � partir de laquelle les prestations doivent �tre recherch�es au format (MMAAA)
     * @param periodeA
     *            P�riode jusqu'� laquelle les prestations doivent �tre recherch�es au format (MMAAA)
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @return <code>true</code> si on retrouve au moins une prestation pour la p�riode sp�fici�e
     */
    public boolean hasExistingPrestations(String idDossier, String idDroit, String periodeDe, String periodeA)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Recherche les d�tail de prestation correspondant aux crit�res d�finis dans le mod�le de recherche
     * 
     * @param searchModel
     *            Le mod�le de recherche
     * @return Le mod�le de recherche contenant les r�sultats
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DetailPrestationGenComplexSearchModel search(DetailPrestationGenComplexSearchModel searchModel)
            throws JadeApplicationException, JadePersistenceException;
}
