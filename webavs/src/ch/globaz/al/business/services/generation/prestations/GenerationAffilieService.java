package ch.globaz.al.business.services.generation.prestations;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.service.provider.application.JadeApplicationService;

/**
 * Service de génération de prestations permettant de générer les prestations des dossiers d'un affilié
 * 
 * @author jts
 * 
 */
public interface GenerationAffilieService extends JadeApplicationService {

    /**
     * Génère les prestations pour le dossier <code>dossier</code> pour la période <code>debutPeriode</code> -
     * <code>finPeriode</code>
     * 
     * @param numAffilie
     *            Affilié pour lequel les prestations doivent être générée
     * @param periode
     *            Période à générer sous la forme MM.AAAA
     * @return Protocole de génération
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public JadePrintDocumentContainer generationAffilie(String numAffilie, String periode)
            throws JadeApplicationException, JadePersistenceException;
}
