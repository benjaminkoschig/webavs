package ch.globaz.al.businessimpl.rafam.sedex.builder;

import globaz.jade.exception.JadeApplicationException;
import ch.globaz.al.business.constantes.enumerations.RafamTypeAnnonce;
import ch.globaz.al.business.exceptions.rafam.ALRafamSedexException;
import ch.globaz.al.business.models.rafam.AnnonceRafamComplexModel;

/**
 * Classe permettant de r�cup�rer le builder appropri� pour chaque type d'annonce g�r� par l'application
 * 
 * @author jts
 * 
 */
public final class MessageBuilderFactory {

    /**
     * Retourne une instance du builder qui devra g�rer la pr�paration de l'annonce
     * 
     * @param annonce
     *            L'annonce devant �tre envoy�e � la centrale
     * 
     * @return instance du builder
     * 
     * @throws JadeApplicationException
     *             Exception lev�e si aucun builder ne correspond au type de l'annonce pass�e en param�tre
     */
    public static MessageBuilderAbstract getMessageBuilder(AnnonceRafamComplexModel annonce)
            throws JadeApplicationException {

        RafamTypeAnnonce type = RafamTypeAnnonce.getRafamTypeAnnonce(annonce.getAnnonceRafamModel().getTypeAnnonce());

        switch (type) {
            case _68A_CREATION:
                return new NewBenefitBuilder(annonce);
            case _68B_MUTATION:
                return new BenefitMutationBuilder(annonce);
            case _68C_ANNULATION:
                return new BenefitCancelationMessageBuilder(annonce);

            default:
                throw new ALRafamSedexException("MessageBuilderFactory#getMessageBuilder : the type" + type.toString()
                        + " is not supported");
        }
    }
}
