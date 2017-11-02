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
 * Interface pour utilisées pour créer les lettres d'accompagnment à des document
 * 
 * @author PTA
 * 
 * 
 */
public interface LettreAccompagnementCopieService extends JadeApplicationService {
    /**
     * Méthode qui crée une lettre d'accompagnement et ajoute les messages d'erreurs liés à la création de la lettre
     * d'acconmpagnement
     * 
     * @param logger
     *            <ProtocoleLogger> protocole d'information lié à la création de document
     * @param IdTiersDestinataireCopie
     *            identifiant du tiers destinataire de la copie
     * @param typeCopie
     *            type de copie (pour échéance, pour décision etc....)
     * @param idDossier
     *            <String> identifiant du dossier
     * @param dateImpression
     *            <String> date d'impression
     * @return DocumentDataContainer
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public DocumentDataContainer loadData(ProtocoleLogger logger, String IdTiersDestinataireCopie, String typeCopie,
            String idDossier, String langueDocument, String dateImpression) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Méthode qui crée une lettre d'accompagnement et ajoute les messages d'erreurs liés à la création de la lettre
     * d'acconmpagnement
     * 
     * @param logger
     *            <ProtocoleLogger> protocole d'information lié à la création de document
     * @param IdTiersDestinataireCopie
     *            identifiant du tiers destinataire de la copie
     * @param typeCopie
     *            type de copie (pour échéance, pour décision etc....)
     * @param idDossier
     *            <String> identifiant du dossier
     * @return DocumentDataContainer
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public DocumentDataContainer loadData(ProtocoleLogger logger, String IdTiersDestinataireCopie, String typeCopie,
            String idDossier, String langueDocument) throws JadePersistenceException, JadeApplicationException;

    /**
     * Méthode qui crée une liste de lettre d'accompagnement
     * 
     * @param logger
     *            <ProtocoleLogger> protocole d'information lié à la création de document
     * @param dossier
     *            dossierComplexModel
     * @param listIdTiers
     *            list des tiers bénéficiaire de lettres d'accompagnemnnet
     * @return ArrayList
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public ArrayList<DocumentData> loadDataLettresAccompagnement(ProtocoleLogger logger, DossierComplexModel dossier,
            ArrayList<String> listIdTiers, String langueDocument) throws JadePersistenceException,
            JadeApplicationException;

}
