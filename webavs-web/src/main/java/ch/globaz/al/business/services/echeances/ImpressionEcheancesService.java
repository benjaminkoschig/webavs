package ch.globaz.al.business.services.echeances;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.ArrayList;
import ch.globaz.al.business.loggers.ProtocoleLogger;
import ch.globaz.al.business.models.droit.DroitEcheanceComplexModel;

/**
 * Service de gestion des �ch�ances � imprimer
 * 
 * @author PTA
 * 
 */
public interface ImpressionEcheancesService extends JadeApplicationService {

    /**
     * 
     * M�thode qui charge les documents � imprimer
     * 
     * @param droitsResult
     *            r�sultat de la recherche
     * @param dateEcheance
     * 
     * @param printCopieAllocPourDossierBeneficiaire
     * 
     * @return tableau contenant des droitEcheanceComplexModel
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public JadePrintDocumentContainer loadDocuments(ArrayList<DroitEcheanceComplexModel> droitsResult,
            String dateEcheance, boolean printCopieAllocPourDossierBeneficiaire, ProtocoleLogger logger)
            throws JadePersistenceException, JadeApplicationException;

}
