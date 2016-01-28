package ch.globaz.al.business.services.declarationVersement;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.ArrayList;
import ch.globaz.al.business.loggers.ProtocoleLogger;
import ch.globaz.al.business.services.documents.DocumentDataContainer;

/**
 * Interface d�finissant les m�thodes communes � l'�tablissement de document d'attestation de d�claration de versement
 * 
 * @author PTA
 * 
 */
public interface DeclarationVersementService extends JadeApplicationService {

    /**
     * M�thode qui ajoute les donn�es n�cessaire aux documents li�s aux d�clarations de versement
     * 
     * @param idDossier
     *            identifiant du dossier
     * @param dateDebut
     *            date du d�but
     * @param dateFin
     *            date de fin
     * @param dateImpression
     *            date d'impression
     * @param logger
     *            Logger permettant de stocker les erreurs pour l'impression du protocole
     * @return DeclarationVersementContainer
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    ArrayList<DocumentDataContainer> getDeclarationVersement(String idDossier, String dateDebut, String dateFin,
            String dateImpression, ProtocoleLogger logger, String langueDocument, Boolean textImpot)
            throws JadePersistenceException, JadeApplicationException;

}
