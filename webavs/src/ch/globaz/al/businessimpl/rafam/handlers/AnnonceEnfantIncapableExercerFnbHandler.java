package ch.globaz.al.businessimpl.rafam.handlers;

import globaz.jade.exception.JadeApplicationException;
import ch.globaz.al.business.constantes.ALCSDroit;
import ch.globaz.al.business.constantes.enumerations.RafamFamilyAllowanceType;
import ch.globaz.al.businessimpl.rafam.ContextAnnonceRafam;

/**
 * G�re les annonces de type Enfant incapable d'exercer avec suppl�ment pour famille nombreuse (code 13). Le
 * comportement est identique � celui de {@link AnnonceEnfantIncapableExercerHandler}, seule le code de l'annonce change
 * 
 * @author jts
 * 
 */
public class AnnonceEnfantIncapableExercerFnbHandler extends AnnonceEnfantIncapableExercerHandler {

    /**
     * Constructeur
     * 
     * @param context
     *            Context contenant toutes les donn�es n�cessaires � la gestion des annonces RAFam
     */
    public AnnonceEnfantIncapableExercerFnbHandler(ContextAnnonceRafam context) {
        super(context);
    }

    @Override
    protected RafamFamilyAllowanceType getType() throws JadeApplicationException {
        return RafamFamilyAllowanceType.ENFANT_EN_INCAPACITE_AVEC_SUPPLEMENT;
    }

    @Override
    protected boolean isCurrentAllowanceTypeActive() {
        return (ALCSDroit.TYPE_ENF.equals(context.getDroit().getDroitModel().getTypeDroit())
                && !context.getDroit().getEnfantComplexModel().getEnfantModel().getCapableExercer() && context
                .getDroit().getDroitModel().getSupplementFnb());
    }
}
