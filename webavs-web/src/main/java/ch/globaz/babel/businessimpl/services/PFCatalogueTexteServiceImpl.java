package ch.globaz.babel.businessimpl.services;

import globaz.babel.api.ICTDocument;
import globaz.babel.api.helper.ICTDocumentHelper;
import globaz.globall.db.BSessionUtil;
import globaz.prestation.interfaces.babel.PRBabelHelper;
import ch.globaz.babel.business.exception.CatalogueTexteException;
import ch.globaz.babel.business.services.PFCatalogueTexteService;
import ch.globaz.perseus.business.constantes.IPFCatalogueTextes;

public class PFCatalogueTexteServiceImpl implements PFCatalogueTexteService {

    private static String CODE_ISO_LANGUE = null;
    private final ICTDocumentHelper babelDoc = null;
    private final String DOC_TYPE_DECISION = IPFCatalogueTextes.CS_DECISION_OCTROI_PARTIEL;
    private final String NOM_OPEN_OFFICE = IPFCatalogueTextes.NOM_OPEN_OFFICE;

    /* Spécifique au domaine PC */
    public final String PF_CS_DOMAINE = IPFCatalogueTextes.CS_PF;

    /**
     * (non-Javadoc)
     * 
     * @see ch.globaz.babel.business.services.PCCatalogueTexteService#searchForTypeDecision(java.lang.String)
     */
    @Override
    public ICTDocumentHelper searchForTypeDecision(String csTypeCatalgoue, String CodeIsoLangue)
            throws CatalogueTexteException, Exception {
        if ((csTypeCatalgoue == null) || (CodeIsoLangue == null)) {
            throw new CatalogueTexteException(
                    "unable to search ITCDocumentHelper for babel - the name of the document passed is null");
        }

        PFCatalogueTexteServiceImpl.CODE_ISO_LANGUE = CodeIsoLangue;
        // Chargement octroi partiel
        ICTDocument iTCDocOctroiPartiel = null;
        iTCDocOctroiPartiel = PRBabelHelper.getDocumentHelper(BSessionUtil.getSessionFromThreadContext());
        iTCDocOctroiPartiel.setCsDomaine(PF_CS_DOMAINE);
        iTCDocOctroiPartiel.setCsTypeDocument(csTypeCatalgoue);
        iTCDocOctroiPartiel.setNom(NOM_OPEN_OFFICE);
        iTCDocOctroiPartiel.setDefault(Boolean.FALSE);
        iTCDocOctroiPartiel.setActif(Boolean.TRUE);
        // TODO attention au format du CodeIsoLangue, doit être : "fr" "de" "it";
        iTCDocOctroiPartiel.setCodeIsoLangue(PFCatalogueTexteServiceImpl.CODE_ISO_LANGUE);

        ICTDocument[] documents = iTCDocOctroiPartiel.load();

        return (ICTDocumentHelper) documents[0];
    }

    private ICTDocumentHelper searchHeaderDecision() throws CatalogueTexteException, Exception {
        // Chargement octroi partiel
        ICTDocument iTCDocCommune = null;
        iTCDocCommune = PRBabelHelper.getDocumentHelper(BSessionUtil.getSessionFromThreadContext());
        iTCDocCommune.setCsDomaine(PF_CS_DOMAINE);
        iTCDocCommune.setCsTypeDocument(IPFCatalogueTextes.CS_DECISION_COMMUNE);
        iTCDocCommune.setNom(NOM_OPEN_OFFICE);
        iTCDocCommune.setDefault(Boolean.FALSE);
        iTCDocCommune.setActif(Boolean.TRUE);
        // TODO attention au format du CodeIsoLangue, doit être : "fr" "de" "it";
        iTCDocCommune.setCodeIsoLangue(PFCatalogueTexteServiceImpl.CODE_ISO_LANGUE);

        ICTDocument[] documents = iTCDocCommune.load();

        return (ICTDocumentHelper) documents[0];
    }
}
