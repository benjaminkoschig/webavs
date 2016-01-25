package ch.globaz.al.business.services.adiDecomptes;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.adi.AdiDecompteComplexModel;

/**
 * Service liées au chargement des données pour la création des décomptes adi versement
 * 
 * @author PTA
 * 
 */
public interface AdiDecomptesService extends JadeApplicationService {

    /**
     * 
     * @param idDecompteAdi
     *            identifiant du décompte ADi
     * @param typeDecompte
     *            type de décompte (global, détaillé
     * @return JadePrintDocumentContainer
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public JadePrintDocumentContainer getAdiDecompteDossier(String idDecompteAdi, String typeDecompte, boolean isGed)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Méthode générant des documents de décomptes ADI (détaillé et global) pour tous les dossier ADI
     * 
     * @param periode
     *            période à traiter (année)
     * @param typeDecompte
     *            type de décompte à traiter (détaillé, global ou les deux
     * @return JadePrintDocumentContainer
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public JadePrintDocumentContainer getAdiDecomptesAll(String periode, String typeDecompte)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Méthode générant des documents de décomptes adi (détaillée et global) pour un dossier adi
     * 
     * @param adiDecompte
     *            identifiant du décompte adi
     * @param typeDecompte
     *            type de décompte (détaillé, global ou les deux)
     * @return JadePrintDocumentContainer
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public JadePrintDocumentContainer getDocuments(AdiDecompteComplexModel adiDecompte, String typeDecompte,
            boolean isGed) throws JadeApplicationException, JadePersistenceException;

}
