package ch.globaz.al.businessimpl.rafam.handlers;

import globaz.jade.exception.JadeApplicationException;
import ch.globaz.al.business.constantes.ALCSDroit;
import ch.globaz.al.business.constantes.enumerations.RafamFamilyAllowanceType;
import ch.globaz.al.businessimpl.rafam.ContextAnnonceRafam;

/**
 * G�re les annonces de type Naissance diff�rentielle (code 13). Le comportement est identique � celui de
 * {@link AnnonceNaissanceHandler}, seule le code de l'annonce et le type d'allocataion de NAIS/ACCE changent
 * 
 * @author jts
 * 
 */
public class AnnonceNaissanceDifferentielleHandler extends AnnonceNaissanceHandler {

    /**
     * Constructeur
     * 
     * @param context
     *            Context contenant toutes les donn�es n�cessaires � la gestion des annonces RAFam
     */
    public AnnonceNaissanceDifferentielleHandler(ContextAnnonceRafam context) {
        super(context);
    }

    @Override
    protected RafamFamilyAllowanceType getType() throws JadeApplicationException {
        return RafamFamilyAllowanceType.DIFFERENCE_NAISSANCE;
    }

    @Override
    protected String getTypeAllocationNaissance() {
        return ALCSDroit.NAISSANCE_TYPE_NAIS;
    }
}
