package ch.globaz.babel.business.services;

import globaz.babel.api.helper.ICTDocumentHelper;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.babel.business.exception.CatalogueTexteException;

public interface PFCatalogueTexteService extends JadeApplicationService {

    /**
     * Recherche les catalogues de texte pour le type decision
     * 
     * @param type
     * @param dec
     * @return
     * @throws CatalogueTexteException
     * @throws Exception
     */
    public ICTDocumentHelper searchForTypeDecision(String csTypeCatalgoue, String CodeIsoLangue)
            throws CatalogueTexteException, Exception;
}
