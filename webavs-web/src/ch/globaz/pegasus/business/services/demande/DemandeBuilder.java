package ch.globaz.pegasus.business.services.demande;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;

public interface DemandeBuilder {

    /**
     * Point d'entrée de la construction du document Billag
     * 
     * @param idDemande
     *            l'identifiant de la demande générant l'attestation
     * @param dateDoc
     *            date sur le document
     * @param dateDebut
     *            date de début
     * @param mailGest
     *            mail du gestionnaire (ou recevoir le fichier)
     * @param nss
     *            le nss de la personne
     * @param idTiers
     *            l'idTiers de l apersonne
     * @return une instance de JadePrintDocumentContainer
     * @throws DecisionException
     *             exception succeptible d'être levée
     * @throws JadeApplicationServiceNotAvailableException
     *             exception succeptible d'être levée
     * @throws JadePersistenceException
     *             exception succeptible d'être levée
     * @throws Exception
     *             exception succeptible d'être levée
     */
    public JadePrintDocumentContainer buildBillag(String idDemande, String dateDoc, String dateDebut, String mailGest,
            String nss, String idTiers, String gestionnaire) throws DecisionException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, Exception;

}
