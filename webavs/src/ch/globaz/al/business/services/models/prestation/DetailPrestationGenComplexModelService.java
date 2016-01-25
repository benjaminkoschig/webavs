package ch.globaz.al.business.services.models.prestation;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.prestation.DetailPrestationGenComplexSearchModel;

/**
 * Service de gestion de persistance des données du modèle <code>DetailPrestationComlexModel</code>
 * 
 * @author jts
 * 
 * @see ch.globaz.al.business.models.prestation.DetailPrestationGenComplexModel
 */
public interface DetailPrestationGenComplexModelService extends JadeApplicationService {

    /**
     * 
     * @param searchModel
     *            Le modèle de recherche
     * @return Le nombre de projets selon le searchModel spécifié
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    public int count(DetailPrestationGenComplexSearchModel searchModel) throws JadeApplicationException,
            JadePersistenceException;

    /***
     * Recherche si il existe déjà des prestations pour le couple idDossier et idDroit durant une période spécifiée
     * 
     * @param idDossier
     *            ID de la table Dossier
     * @param idDroit
     *            ID de la table Droits
     * @param periodeDe
     *            Période à partir de laquelle les prestations doivent être recherchées au format (MMAAA)
     * @param periodeA
     *            Période jusqu'à laquelle les prestations doivent être recherchées au format (MMAAA)
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @return <code>true</code> si on retrouve au moins une prestation pour la période spéficiée
     */
    public boolean hasExistingPrestations(String idDossier, String idDroit, String periodeDe, String periodeA)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Recherche les détail de prestation correspondant aux critères définis dans le modèle de recherche
     * 
     * @param searchModel
     *            Le modèle de recherche
     * @return Le modèle de recherche contenant les résultats
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DetailPrestationGenComplexSearchModel search(DetailPrestationGenComplexSearchModel searchModel)
            throws JadeApplicationException, JadePersistenceException;
}
