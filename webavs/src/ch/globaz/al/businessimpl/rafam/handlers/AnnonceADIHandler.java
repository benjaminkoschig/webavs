package ch.globaz.al.businessimpl.rafam.handlers;

import globaz.jade.exception.JadeApplicationException;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.constantes.enumerations.RafamFamilyAllowanceType;
import ch.globaz.al.businessimpl.rafam.ContextAnnonceRafam;

/**
 * Gère les annonces de type ADI (code 31)
 * 
 * @author jts
 * 
 */
public class AnnonceADIHandler extends AnnonceHandlerAbstract {

    /**
     * Constructeur
     * 
     * @param context
     *            Context contenant toutes les données nécessaire à la gestion des annonces RAFam
     */
    public AnnonceADIHandler(ContextAnnonceRafam context) {
        this.context = context;
    }

    @Override
    protected RafamFamilyAllowanceType getType() throws JadeApplicationException {
        return RafamFamilyAllowanceType.ADI;
    }

    @Override
    protected boolean isCurrentAllowanceTypeActive() {
        return ALCSDossier.STATUT_IS.equals(context.getDossier().getDossierModel().getStatut());
    }
}
