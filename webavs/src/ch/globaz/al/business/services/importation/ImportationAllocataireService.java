package ch.globaz.al.business.services.importation;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.ArrayList;
import ch.globaz.al.business.models.allocataire.AgricoleModel;
import ch.globaz.al.business.models.allocataire.AllocataireComplexModel;
import ch.globaz.al.business.models.allocataire.RevenuModel;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;

/**
 * Service d'importation des données allocataire
 * 
 * @author jts
 * 
 */
public interface ImportationAllocataireService extends JadeApplicationService {

    public boolean checkTiersData(PersonneEtendueComplexModel persDB, PersonneEtendueComplexModel persXML,
            String idDossier, String type, String idPersonneAlfa) throws Exception;

    /**
     * Importe les informations d'agriculteur de l'allocataire
     * 
     * @param agricole
     *            modèle agricole
     * @param idAllocataire
     *            id de l'allocataire auquel l'information d'agriculteur doit être liée
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public void importAllocataireAgricole(AgricoleModel agricole, String idAllocataire)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * importation des revenus liés à un allocataire
     * 
     * @param idAllocataire
     *            id de l'allocataire auquel sont liés les revenus à importer
     * @param revenus
     *            ArrayList
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public void importRevenus(String idAllocataire, ArrayList<RevenuModel> revenus) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Recherche si un allocataire correspondant au modèle passé en paramètre existe déjà en persistance
     * 
     * @param allocataire
     *            modèle de l'allocataire à rechercher
     * @return Résultat de la recherche. Si aucun allocataire n'existe, le modèle passé en paramètre est retourné
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws Exception
     */
    public AllocataireComplexModel searchAllocataire(AllocataireComplexModel allocataire, String idDossier,
            String idPersonneAlfa) throws JadeApplicationException, JadePersistenceException, Exception;

}
