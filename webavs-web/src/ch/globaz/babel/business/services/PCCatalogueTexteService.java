package ch.globaz.babel.business.services;

import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.Map;
import ch.globaz.babel.business.exception.CatalogueTexteException;
import ch.globaz.common.business.models.CTDocumentImpl;
import ch.globaz.jade.business.models.Langues;

public interface PCCatalogueTexteService extends JadeApplicationService {

    public String getTextIntroForDAC(Langues langueTiers) throws CatalogueTexteException, Exception;

    public Map<Langues, CTDocumentImpl> searchForAdaptationImpression() throws Exception;

    /**
     * Recherche les catalogues de texte pour la communication OCC
     * 
     * @return le documentHelper
     * @throws CatalogueTexteException
     *             en cas d'erreur de catalogue de texte
     * @throws Exception
     *             en cas d'autres erreurs
     */

    public Map<Langues, CTDocumentImpl> searchForCommunicationOCC() throws CatalogueTexteException, Exception;

    public Map<Langues, CTDocumentImpl> searchForDemandeRenseignement(String typeCatalogue)
            throws CatalogueTexteException, Exception;

    public abstract Map<Langues, CTDocumentImpl> searchForPageGardeCopiePC() throws CatalogueTexteException, Exception;

    public abstract Map<Langues, CTDocumentImpl> searchForTransfertDossierPC(String typeBuilder)
            throws CatalogueTexteException, Exception;

    public Map<Langues, CTDocumentImpl> searchForTransfertRente() throws CatalogueTexteException, Exception;

    /**
     * Recherche les catalogues de texte pour le type decision
     * 
     * @param type
     * @param dec
     * @return
     * @throws CatalogueTexteException
     * @throws Exception
     */
    public Map<Langues, CTDocumentImpl> searchForTypeDecision(String docName) throws CatalogueTexteException, Exception;
}
