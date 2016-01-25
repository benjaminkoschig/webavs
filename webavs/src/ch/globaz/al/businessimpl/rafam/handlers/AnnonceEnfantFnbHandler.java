package ch.globaz.al.businessimpl.rafam.handlers;

import globaz.jade.exception.JadeApplicationException;
import ch.globaz.al.business.constantes.ALCSDroit;
import ch.globaz.al.business.constantes.enumerations.RafamFamilyAllowanceType;
import ch.globaz.al.businessimpl.rafam.ContextAnnonceRafam;

/**
 * Gère les annonces de type Enfant avec supplément pour famille nombreuse (code 11). Le comportement est identique à
 * celui de {@link AnnonceEnfantHandler}, seule le code de l'annonce change
 * 
 * @author jts
 * 
 */
public class AnnonceEnfantFnbHandler extends AnnonceEnfantHandler {

    /**
     * Constructeur
     * 
     * @param context
     *            Context contenant toutes les données nécessaires à la gestion des annonces RAFam
     */
    public AnnonceEnfantFnbHandler(ContextAnnonceRafam context) {
        super(context);
    }

    @Override
    protected RafamFamilyAllowanceType getType() throws JadeApplicationException {
        return RafamFamilyAllowanceType.ENFANT_AVEC_SUPPLEMENT;
    }

    @Override
    protected boolean isCurrentAllowanceTypeActive() {
        return (ALCSDroit.TYPE_ENF.equals(context.getDroit().getDroitModel().getTypeDroit()) && context.getDroit()
                .getDroitModel().getSupplementFnb());
    }
}