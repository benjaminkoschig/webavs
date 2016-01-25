package ch.globaz.al.business.services.adiDecomptes;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.adi.AdiDecompteComplexModel;

/**
 * Service li�es au chargement des donn�es pour la cr�ation des d�comptes adi versement
 * 
 * @author PTA
 * 
 */
public interface AdiDecomptesService extends JadeApplicationService {

    /**
     * 
     * @param idDecompteAdi
     *            identifiant du d�compte ADi
     * @param typeDecompte
     *            type de d�compte (global, d�taill�
     * @return JadePrintDocumentContainer
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public JadePrintDocumentContainer getAdiDecompteDossier(String idDecompteAdi, String typeDecompte, boolean isGed)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * M�thode g�n�rant des documents de d�comptes ADI (d�taill� et global) pour tous les dossier ADI
     * 
     * @param periode
     *            p�riode � traiter (ann�e)
     * @param typeDecompte
     *            type de d�compte � traiter (d�taill�, global ou les deux
     * @return JadePrintDocumentContainer
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public JadePrintDocumentContainer getAdiDecomptesAll(String periode, String typeDecompte)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * M�thode g�n�rant des documents de d�comptes adi (d�taill�e et global) pour un dossier adi
     * 
     * @param adiDecompte
     *            identifiant du d�compte adi
     * @param typeDecompte
     *            type de d�compte (d�taill�, global ou les deux)
     * @return JadePrintDocumentContainer
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public JadePrintDocumentContainer getDocuments(AdiDecompteComplexModel adiDecompte, String typeDecompte,
            boolean isGed) throws JadeApplicationException, JadePersistenceException;

}
