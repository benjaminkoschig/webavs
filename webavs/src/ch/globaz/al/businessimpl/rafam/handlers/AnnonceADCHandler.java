package ch.globaz.al.businessimpl.rafam.handlers;

import globaz.jade.exception.JadeApplicationException;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.constantes.enumerations.RafamFamilyAllowanceType;
import ch.globaz.al.businessimpl.rafam.ContextAnnonceRafam;

/**
 * Gère les annonces de type ADC (code 30)
 * 
 * @author jts
 * 
 */
public class AnnonceADCHandler extends AnnonceHandlerAbstract {

    /**
     * Constructeur
     * 
     * @param context
     *            Context contenant toutes les données nécessaire à la gestion des annonces RAFam
     */
    public AnnonceADCHandler(ContextAnnonceRafam context) {
        this.context = context;
    }

    @Override
    protected RafamFamilyAllowanceType getType() throws JadeApplicationException {
        return RafamFamilyAllowanceType.ADC;
    }

    @Override
    protected boolean isCurrentAllowanceTypeActive() {
        return ALCSDossier.STATUT_CS.equals(context.getDossier().getDossierModel().getStatut());
    }
}
