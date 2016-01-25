package ch.globaz.al.businessimpl.rafam.handlers;

import globaz.jade.exception.JadeApplicationException;
import ch.globaz.al.business.constantes.ALCSDroit;
import ch.globaz.al.business.constantes.enumerations.RafamFamilyAllowanceType;
import ch.globaz.al.businessimpl.rafam.ContextAnnonceRafam;

/**
 * G�re les annonces de type Formation anticip�e avec formation pour famille nombreuse (code 23). Le comportement est
 * identique � celui de {@link AnnonceFormationAnticipeeHandler}, seule le code de l'annonce change
 * 
 * @author jts
 * 
 */
public class AnnonceFormationAnticipeeFnbHandler extends AnnonceFormationAnticipeeHandler {

    /**
     * Constructeur
     * 
     * @param context
     *            Context contenant toutes les donn�es n�cessaires � la gestion des annonces RAFam
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
