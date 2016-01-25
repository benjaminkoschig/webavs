package ch.globaz.al.businessimpl.rafam.handlers;

import globaz.jade.exception.JadeApplicationException;
import ch.globaz.al.business.constantes.ALCSDroit;
import ch.globaz.al.business.constantes.enumerations.RafamFamilyAllowanceType;
import ch.globaz.al.businessimpl.rafam.ContextAnnonceRafam;

/**
 * Gère les annonces de type Formation anticipée avec formation pour famille nombreuse (code 23). Le comportement est
 * identique à celui de {@link AnnonceFormationAnticipeeHandler}, seule le code de l'annonce change
 * 
 * @author jts
 * 
 */
public class AnnonceFormationAnticipeeFnbHandler extends AnnonceFormationAnticipeeHandler {

    /**
     * Constructeur
     * 
     * @param context
     *            Context contenant toutes les données nécessaires à la gestion des annonces RAFam
     */
    public AnnonceFormationAnticipeeFnbHandler(ContextAnnonceRafam context) {
        super(context);
    }

    @Override
    protected RafamFamilyAllowanceType getType() throws JadeApplicationException {
        return RafamFamilyAllowanceType.FORMATION_ANTICIPEE_AVEC_SUPPLEMENT;
    }

    @Override
    protected boolean isCurrentAllowanceTypeActive() {
        return (ALCSDroit.TYPE_FORM.equals(context.getDroit().getDroitModel().getTypeDroit()) && context.getDroit()
                .getDroitModel().getSupplementFnb());
    }
}
