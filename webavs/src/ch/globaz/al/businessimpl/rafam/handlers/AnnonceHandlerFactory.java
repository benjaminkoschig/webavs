package ch.globaz.al.businessimpl.rafam.handlers;

import globaz.jade.exception.JadeApplicationException;
import ch.globaz.al.business.exceptions.model.annonces.rafam.ALAnnonceRafamException;
import ch.globaz.al.businessimpl.rafam.ContextAnnonceRafam;
import ch.globaz.al.businessimpl.rafam.ContextAnnonceRafamDelegue;
import ch.globaz.al.businessimpl.rafam.handlers.afdelegue.AnnonceDelegueHandler;

/**
 * Factory permettant de récupérer une instance de la classe qui va gérer le traitement d'une annonce RAFam
 * 
 * @author jts
 * 
 */
public final class AnnonceHandlerFactory {

    /**
     * Retourne une instance de la classe qui va gérer le traitement d'une annonce RAFAam. Pour cela, le type contenu
     * dans le contexte passé en paramètre est utilisé.
     * 
     * @param context
     *            Contexte contenant les informations nécessaire au traitement des annocnes
     * @return Instance de la classe de traitement
     * 
     * @throws JadeApplicationException
     *             Exception levée si aucun type connu n'a pu être identifié
     */
    public static AnnonceHandlerAbstract getHandler(ContextAnnonceRafam context) throws JadeApplicationException {
        // gestion des annonces en provenance de l'employeur délégué
        if (context instanceof ContextAnnonceRafamDelegue) {
            return new AnnonceDelegueHandler((ContextAnnonceRafamDelegue) context);
        } else {
            switch (context.getType()) {
                case ADC:
                    return new AnnonceADCHandler(context);

                case ADI:
                    return new AnnonceADIHandler(context);

                case ADOPTION:
                    return new AnnonceAdoptionHandler(context);

                case NAISSANCE:
                    return new AnnonceNaissanceHandler(context);

                case DIFFERENCE_NAISSANCE:
                    return new AnnonceNaissanceDifferentielleHandler(context);

                case DIFFERENCE_ADOPTION:
                    return new AnnonceAdoptionDifferentielleHandler(context);

                case ENFANT:
                    return new AnnonceEnfantHandler(context);

                case ENFANT_AVEC_SUPPLEMENT:
                    return new AnnonceEnfantFnbHandler(context);

                case ENFANT_EN_INCAPACITE:
                    return new AnnonceEnfantIncapableExercerHandler(context);

                case ENFANT_EN_INCAPACITE_AVEC_SUPPLEMENT:
                    return new AnnonceEnfantIncapableExercerFnbHandler(context);

                case FORMATION:
                    return new AnnonceFormationHandler(context);

                case FORMATION_AVEC_SUPPLEMENT:
                    return new AnnonceFormationFnbHandler(context);

                case FORMATION_ANTICIPEE:
                    return new AnnonceFormationAnticipeeHandler(context);

                case FORMATION_ANTICIPEE_AVEC_SUPPLEMENT:
                    return new AnnonceFormationAnticipeeFnbHandler(context);

                default:
                    throw new ALAnnonceRafamException("AnnonceHandlerFactory#getHandler : this type is not supported");
            }
        }
    }
}
