package ch.globaz.al.business.services.generation.prestations;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.service.provider.application.JadeApplicationService;

/**
 * Service de g�n�ration de prestations permettant de g�n�rer les prestations des dossiers d'un affili�
 * 
 * @author jts
 * 
 */
public interface GenerationAffilieService extends JadeApplicationService {

    /**
     * G�n�re les prestations pour le dossier <code>dossier</code> pour la p�riode <code>debutPeriode</code> -
     * <code>finPeriode</code>
     * 
     * @param numAffilie
     *            Affili� pour lequel les prestations doivent �tre g�n�r�e
     * @param periode
     *            P�riode � g�n�rer sous la forme MM.AAAA
     * @return Protocole de g�n�ration
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public JadePrintDocumentContainer generationAffilie(String numAffilie, String periode)
            throws JadeApplicationException, JadePersistenceException;
}
