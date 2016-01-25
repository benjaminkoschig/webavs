package ch.globaz.al.businessimpl.rafam.handlers;

import globaz.jade.exception.JadeApplicationException;
import ch.globaz.al.business.constantes.ALCSDroit;
import ch.globaz.al.business.constantes.enumerations.RafamFamilyAllowanceType;
import ch.globaz.al.businessimpl.rafam.ContextAnnonceRafam;

/**
 * Gère les annonces de type Adoption (code 02). Le comportement est identique à celui de
 * {@link AnnonceNaissanceHandler}, seule le code de l'annonce et le type d'allocataion de NAIS/ACCE changent
 * 
 * @author jts
 * 
 */
public class AnnonceAdoptionHandler extends AnnonceNaissanceHandler {

    /**
     * Constructeur
     * 
     * @param context
     *            Context contenant toutes les données nécessaires à la gestion des annonces RAFam
     */
    public AnnonceAdoptionHandler(ContextAnnonceRafam context) {
        super(context);
    }

    @Override
    protected RafamFamilyAllowanceType getType() throws JadeApplicationException {
        return RafamFamilyAllowanceType.ADOPTION;
    }

    @Override
    protected String getTypeAllocationNaissance() {
        return ALCSDroit.NAISSANCE_TYPE_ACCE;
    }
}
