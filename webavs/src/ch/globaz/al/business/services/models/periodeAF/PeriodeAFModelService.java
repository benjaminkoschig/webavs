package ch.globaz.al.business.services.models.periodeAF;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.periodeAF.PeriodeAFModel;
import ch.globaz.al.business.models.periodeAF.PeriodeAFSearchModel;

/**
 * Service de gestion de persistance des données du modèle <code>PeriodeAFModel</code>
 * 
 * @author gmo
 * 
 * @see ch.globaz.al.business.models.periodeAF.PeriodeAFModel
 */
public interface PeriodeAFModelService extends JadeApplicationService {

    /**
     * 
     * @param periodeAFModel
     *            Modèle de la période AF
     * @return le modèle créé
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public PeriodeAFModel create(PeriodeAFModel periodeAFModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Lecture d'une période selon son id passé en paramètre
     * 
     * @param idPeriodeAF
     *            id du modèle à charger
     * @return le modèle chargé
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public PeriodeAFModel read(String idPeriodeAF) throws JadeApplicationException, JadePersistenceException;

    /**
     * Recherche les périodes correspondant aux critères définis dans le modèle de recherche
     * 
     * @param periodeSearchModel
     *            Le modèle de recherche
     * @return Le modèle de recherche contenant les résultats
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public PeriodeAFSearchModel search(PeriodeAFSearchModel periodeSearchModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Mise à jour du modèle
     * 
     * @param periodeAFModel
     *            Modèle de la période AF
     * @return le modèle mis à jour
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public PeriodeAFModel upate(PeriodeAFModel periodeAFModel) throws JadeApplicationException,
            JadePersistenceException;
}
