package ch.globaz.amal.businessimpl.services.sedexRP.builder;

import ch.globaz.amal.business.constantes.AMMessagesSubTypesAnnonceSedex;
import ch.globaz.amal.business.exceptions.models.annoncesedex.AnnonceSedexException;
import ch.globaz.amal.business.models.annoncesedex.SimpleAnnonceSedex;

/**
 * Fabrique pour les annonces de type EMISSION
 * 
 * @author cbu
 * 
 */
public final class AnnonceBuilderFactory {

    public static AnnonceBuilderAbstract getAnnonceBuilder(SimpleAnnonceSedex annonceSedex)
            throws AnnonceSedexException {
        return AnnonceBuilderFactory.getAnnonceBuilder(annonceSedex, null);
    }

    public static AnnonceBuilderAbstract getAnnonceBuilder(SimpleAnnonceSedex annonceSedex,
            AnnonceInfosContainer annonceInfos) throws AnnonceSedexException {

        AMMessagesSubTypesAnnonceSedex subType = AMMessagesSubTypesAnnonceSedex.getSubType(annonceSedex
                .getMessageSubType());

        switch (subType) {
            case NOUVELLE_DECISION:
                return new AnnonceDecreeBuilder(annonceSedex);
            case INTERRUPTION:
                return new AnnonceStopBuilder(annonceSedex);
            case DEMANDE_RAPPORT_ASSURANCE:
                return new AnnonceInsuranceQueryBuilder(annonceSedex, annonceInfos);
            case ETAT_DECISIONS:
                return new AnnonceDecreeInventoryBuilder(annonceSedex, annonceInfos);
            default:
                throw new AnnonceSedexException("Unknown subtype in AnnonceBuilderFactory.getAnnonceBuilder : "
                        + subType);
        }
    }
}
