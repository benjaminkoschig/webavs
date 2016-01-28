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
 * Service d'importation des donn�es allocataire
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
     *            mod�le agricole
     * @param idAllocataire
     *            id de l'allocataire auquel l'information d'agriculteur doit �tre li�e
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public void importAllocataireAgricole(AgricoleModel agricole, String idAllocataire)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * importation des revenus li�s � un allocataire
     * 
     * @param idAllocataire
     *            id de l'allocataire auquel sont li�s les revenus � importer
     * @param revenus
     *            ArrayList
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public void importRevenus(String idAllocataire, ArrayList<RevenuModel> revenus) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Recherche si un allocataire correspondant au mod�le pass� en param�tre existe d�j� en persistance
     * 
     * @param allocataire
     *            mod�le de l'allocataire � rechercher
     * @return R�sultat de la recherche. Si aucun allocataire n'existe, le mod�le pass� en param�tre est retourn�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws Exception
     */
    public AllocataireComplexModel searchAllocataire(AllocataireComplexModel allocataire, String idDossier,
            String idPersonneAlfa) throws JadeApplicationException, JadePersistenceException, Exception;

}
