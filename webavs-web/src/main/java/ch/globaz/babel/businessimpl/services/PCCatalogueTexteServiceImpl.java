package ch.globaz.babel.businessimpl.services;

import java.util.HashMap;
import java.util.Map;
import ch.globaz.babel.business.exception.CatalogueTexteException;
import ch.globaz.babel.business.services.PCCatalogueTexteService;
import ch.globaz.common.business.models.CTDocumentImpl;
import ch.globaz.common.document.babel.CatalogueTexteLoader;
import ch.globaz.jade.business.models.Langues;
import ch.globaz.pegasus.business.constantes.IPCCatalogueTextes;
import ch.globaz.pegasus.business.constantes.demanderenseignement.IPCDemandeRenseignementBuilderType;
import ch.globaz.pegasus.business.constantes.transfertdossier.TransfertDossierBuilderType;

public class PCCatalogueTexteServiceImpl implements PCCatalogueTexteService {

    private static final String DOC_TYPE_PAGE_GARDE_COPIE = IPCCatalogueTextes.CS_TYPE_PAGE_GARDE_COPIE;
    private final String DOC_TYPE_ADAPTATION_ANNUELLE = IPCCatalogueTextes.CS_TYPE_ADAPTATION_ANNUELLE;
    private final String DOC_TYPE_COMMUNICATION_OCC = IPCCatalogueTextes.CS_TYPE_COMMUNICATION_OCC;
    private final String DOC_TYPE_DECISION = IPCCatalogueTextes.CS_TYPE_DECISION;
    private final String DOC_TYPE_DEMANDE_RENSEIGNEMENT = IPCCatalogueTextes.CS_TYPE_DEMANDE_RENSEIGNEMENT;
    private final String DOC_TYPE_TRANSFERT_DOSSIER = IPCCatalogueTextes.CS_TYPE_TRANSFERT_DOSSIER;
    /* Spécifique au domaine PC */
    private final String PC_CS_DOMAINE = IPCCatalogueTextes.CS_PC;

    /**
     * Retourne la chaine de caractère du texte d'introduction de la DAC a setter dans l'objet lors de la préparation
     * 
     * @return
     * @throws Exception
     */
    @Override
    public String getTextIntroForDAC(Langues langueTiers) throws Exception {

        Map<Langues, CTDocumentImpl> documentsBabel = searchForTypeDecision(IPCCatalogueTextes.BABEL_DOC_NAME_APRES_CALCUL);
        CTDocumentImpl document = documentsBabel.get(langueTiers);

        return document.getTextes(2).getTexte(10).getDescription();
    }

    @Override
    public Map<Langues, CTDocumentImpl> searchForAdaptationImpression() throws Exception {
        Map<Langues, CTDocumentImpl> map;
        CatalogueTexteLoader loader = new CatalogueTexteLoader();

        map = loader.loadICTDocumentImpByDomaineTypeDocAndName(PC_CS_DOMAINE, DOC_TYPE_ADAPTATION_ANNUELLE,
                IPCCatalogueTextes.BABEL_DOC_NAME_ADAPTATION_ANNUELLE);

        return map;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.babel.business.services.PCCatalogueTexteService#searchForCommunicationOCC()
     */
    @Override
    public Map<Langues, CTDocumentImpl> searchForCommunicationOCC() throws CatalogueTexteException, Exception {
        Map<Langues, CTDocumentImpl> map;
        CatalogueTexteLoader loader = new CatalogueTexteLoader();

        map = loader.loadICTDocumentImpByDomaineTypeDocAndName(PC_CS_DOMAINE, DOC_TYPE_COMMUNICATION_OCC,
                IPCCatalogueTextes.BABEL_DOC_NAME_COMMUNICATION_OCC);

        return map;
    }

    @Override
    public Map<Langues, CTDocumentImpl> searchForDemandeRenseignement(String typeCatalogue) throws Exception {
        Map<Langues, CTDocumentImpl> map;
        CatalogueTexteLoader loader = new CatalogueTexteLoader();

        if (IPCDemandeRenseignementBuilderType.BABEL_AGENCE_COMMUNALE_AVS.equals(typeCatalogue)) {
            map = loader.loadICTDocumentImpByDomaineTypeDocAndName(PC_CS_DOMAINE, DOC_TYPE_DEMANDE_RENSEIGNEMENT,
                    IPCCatalogueTextes.BABEL_DOC_NAME_DEMANDE_AGENCE_COMMUNALE_AVS);
        } else if (IPCDemandeRenseignementBuilderType.BABEL_COMMUN.equals(typeCatalogue)) {
            map = loader.loadICTDocumentImpByDomaineTypeDocAndName(PC_CS_DOMAINE, DOC_TYPE_DEMANDE_RENSEIGNEMENT,
                    IPCCatalogueTextes.BABEL_DOC_NAME_DEMANDE_RENSEIGNEMENT);
        } else {
            throw new CatalogueTexteException("Unknown catalogue type! Type: " + typeCatalogue);
        }
        return map;
    }

    @Override
    public Map<Langues, CTDocumentImpl> searchForPageGardeCopiePC() throws CatalogueTexteException, Exception {

        Map<Langues, CTDocumentImpl> map = new HashMap<Langues, CTDocumentImpl>();
        CatalogueTexteLoader loader = new CatalogueTexteLoader();

        map = loader.loadICTDocumentImpByDomaineTypeDocAndName(PC_CS_DOMAINE,
                PCCatalogueTexteServiceImpl.DOC_TYPE_PAGE_GARDE_COPIE,
                IPCCatalogueTextes.BABEL_DOC_NAME_PAGE_GARDE_COPIE);

        return map;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.babel.business.services.PCCatalogueTexteService#searchForCommunicationOCC()
     */
    @Override
    public Map<Langues, CTDocumentImpl> searchForTransfertDossierPC(String typeBuilder) throws CatalogueTexteException,
            Exception {
        Map<Langues, CTDocumentImpl> map;
        CatalogueTexteLoader loader = new CatalogueTexteLoader();

        if (TransfertDossierBuilderType.DEMANDE_INITIALE_SANS_PC.equals(typeBuilder)) {

            map = loader.loadICTDocumentImpByDomaineTypeDocAndName(PC_CS_DOMAINE, DOC_TYPE_TRANSFERT_DOSSIER,
                    IPCCatalogueTextes.BABEL_DOC_NAME_TRANSFERT_DOSSIER);
        } else if (TransfertDossierBuilderType.DEMANDE_EN_COURS.equals(typeBuilder)) {
            map = loader.loadICTDocumentImpByDomaineTypeDocAndName(PC_CS_DOMAINE, DOC_TYPE_DECISION,
                    IPCCatalogueTextes.BABEL_DOC_NAME_TRANSFERT_DOSSIER_SUPPRESSION);
        } else {
            throw new CatalogueTexteException("Unknown builder type! Type: " + typeBuilder.toString());
        }

        return map;
    }

    @Override
    public Map<Langues, CTDocumentImpl> searchForTransfertRente() throws CatalogueTexteException, Exception {
        Map<Langues, CTDocumentImpl> map;
        CatalogueTexteLoader loader = new CatalogueTexteLoader();

        map = loader.loadICTDocumentImpByDomaineTypeDocAndName(PC_CS_DOMAINE, DOC_TYPE_TRANSFERT_DOSSIER,
                IPCCatalogueTextes.BABEL_DOC_NAME_TRANSFERT_RENTE);
        return map;
    }

    /**
     * (non-Javadoc)
     * 
     * @see ch.globaz.babel.business.services.PCCatalogueTexteService#searchForTypeDecision(java.lang.String)
     */
    @Override
    public Map<Langues, CTDocumentImpl> searchForTypeDecision(String docName) throws CatalogueTexteException, Exception {
        Map<Langues, CTDocumentImpl> map;
        CatalogueTexteLoader loader = new CatalogueTexteLoader();

        if ((docName == null)) {
            throw new CatalogueTexteException(
                    "unable to search ITCDocumentHelper for babel - the name of the document passed is null");
        }

        map = loader.loadICTDocumentImpByDomaineTypeDocAndName(PC_CS_DOMAINE, DOC_TYPE_DECISION, docName);

        return map;
    }

}
