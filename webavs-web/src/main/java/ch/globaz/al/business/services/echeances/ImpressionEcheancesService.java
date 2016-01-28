package ch.globaz.al.business.services.echeances;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.ArrayList;
import ch.globaz.al.business.loggers.ProtocoleLogger;
import ch.globaz.al.business.models.droit.DroitEcheanceComplexModel;

/**
 * Service de gestion des échéances à imprimer
 * 
 * @author PTA
 * 
 */
public interface ImpressionEcheancesService extends JadeApplicationService {

    /**
     * 
     * Méthode qui charge les documents à imprimer
     * 
     * @param droitsResult
     *            résultat de la recherche
     * @param dateEcheance
     * 
     * @param printCopieAllocPourDossierBeneficiaire
     * 
     * @return tableau contenant des droitEcheanceComplexModel
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public JadePrintDocumentContainer loadDocuments(ArrayList<DroitEcheanceComplexModel> droitsResult,
            String dateEcheance, boolean printCopieAllocPourDossierBeneficiaire, ProtocoleLogger logger)
            throws JadePersistenceException, JadeApplicationException;

}
