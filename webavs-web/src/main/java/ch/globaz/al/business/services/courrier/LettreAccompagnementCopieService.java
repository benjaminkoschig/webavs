package ch.globaz.al.business.services.courrier;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.ArrayList;
import ch.globaz.al.business.loggers.ProtocoleLogger;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.services.documents.DocumentDataContainer;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * Interface pour utilis�es pour cr�er les lettres d'accompagnment � des document
 * 
 * @author PTA
 * 
 * 
 */
public interface LettreAccompagnementCopieService extends JadeApplicationService {
    /**
     * M�thode qui cr�e une lettre d'accompagnement et ajoute les messages d'erreurs li�s � la cr�ation de la lettre
     * d'acconmpagnement
     * 
     * @param logger
     *            <ProtocoleLogger> protocole d'information li� � la cr�ation de document
     * @param IdTiersDestinataireCopie
     *            identifiant du tiers destinataire de la copie
     * @param typeCopie
     *            type de copie (pour �ch�ance, pour d�cision etc....)
     * @param idDossier
     *            <String> identifiant du dossier
     * @param dateImpression
     *            <String> date d'impression
     * @return DocumentDataContainer
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public DocumentDataContainer loadData(ProtocoleLogger logger, String IdTiersDestinataireCopie, String typeCopie,
            String idDossier, String langueDocument, String dateImpression) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * M�thode qui cr�e une lettre d'accompagnement et ajoute les messages d'erreurs li�s � la cr�ation de la lettre
     * d'acconmpagnement
     * 
     * @param logger
     *            <ProtocoleLogger> protocole d'information li� � la cr�ation de document
     * @param IdTiersDestinataireCopie
     *            identifiant du tiers destinataire de la copie
     * @param typeCopie
     *            type de copie (pour �ch�ance, pour d�cision etc....)
     * @param idDossier
     *            <String> identifiant du dossier
     * @return DocumentDataContainer
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public DocumentDataContainer loadData(ProtocoleLogger logger, String IdTiersDestinataireCopie, String typeCopie,
            String idDossier, String langueDocument) throws JadePersistenceException, JadeApplicationException;

    /**
     * M�thode qui cr�e une liste de lettre d'accompagnement
     * 
     * @param logger
     *            <ProtocoleLogger> protocole d'information li� � la cr�ation de document
     * @param dossier
     *            dossierComplexModel
     * @param listIdTiers
     *            list des tiers b�n�ficiaire de lettres d'accompagnemnnet
     * @return ArrayList
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public ArrayList<DocumentData> loadDataLettresAccompagnement(ProtocoleLogger logger, DossierComplexModel dossier,
            ArrayList<String> listIdTiers, String langueDocument) throws JadePersistenceException,
            JadeApplicationException;

}
