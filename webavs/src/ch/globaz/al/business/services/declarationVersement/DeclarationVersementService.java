package ch.globaz.al.business.services.declarationVersement;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.ArrayList;
import ch.globaz.al.business.loggers.ProtocoleLogger;
import ch.globaz.al.business.services.documents.DocumentDataContainer;

/**
 * Interface définissant les méthodes communes à l'établissement de document d'attestation de déclaration de versement
 * 
 * @author PTA
 * 
 */
public interface DeclarationVersementService extends JadeApplicationService {

    /**
     * Méthode qui ajoute les données nécessaire aux documents liés aux déclarations de versement
     * 
     * @param idDossier
     *            identifiant du dossier
     * @param dateDebut
     *            date du début
     * @param dateFin
     *            date de fin
     * @param dateImpression
     *            date d'impression
     * @param logger
     *            Logger permettant de stocker les erreurs pour l'impression du protocole
     * @return DeclarationVersementContainer
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    ArrayList<DocumentDataContainer> getDeclarationVersement(String idDossier, String dateDebut, String dateFin,
            String dateImpression, ProtocoleLogger logger, String langueDocument, Boolean textImpot)
            throws JadePersistenceException, JadeApplicationException;

}
