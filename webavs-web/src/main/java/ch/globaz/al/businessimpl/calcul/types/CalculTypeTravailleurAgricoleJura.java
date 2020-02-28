package ch.globaz.al.businessimpl.calcul.types;

import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.ArrayList;
import java.util.List;

import ch.globaz.al.business.constantes.ALCSDroit;
import ch.globaz.al.business.constantes.ALCSTarif;
import ch.globaz.al.business.exceptions.calcul.ALCalculException;
import ch.globaz.al.business.exceptions.calcul.ALCalculModeException;
import ch.globaz.al.business.models.dossier.DossierAgricoleComplexModel;
import ch.globaz.al.business.models.dossier.DossierModel;
import ch.globaz.al.business.models.droit.CalculBusinessModel;
import ch.globaz.al.businessimpl.calcul.context.ContextCalcul;
import ch.globaz.al.businessimpl.calcul.modes.CalculMode;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * Type de calcul spécifique aux travailleurs agricoles du canton du Jura avant la LAFam (01.01.2009)
 * 
 * Il fonctionne de la même manière que le type <code>CalculTypeTravailleurAgricole</code> mais traite, en plus,
 * l'allocation de ménage LJA accordé aux travailleurs agricoles jurassiens (montant complémentaire de CHF 15).
 * 
 * Ce type est valide pour les calculs répondant aux conditions suivantes :
 * <ul>
 * <li>date de calcul antérieur au 01.01.2009 (non LAFam)</li>
 * <li>travailleur agricole ayant sont exploitation dans le canton duJura</li>
 * </ul>
 * 
 * @author jts
 */
public class CalculTypeTravailleurAgricoleJura extends CalculTypeStandard {

    /**
     * Montant du ménage jurassien
     */
    protected static final String MONTANT_MENAGE_JURA = "15.0";

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.businessimpl.calcul.types.CalculTypeStandard#compute(ch.
     * globaz.al.business.models.dossier.DossierComplexModel, ch.globaz.al.businessimpl.calcul.modes.CalculMode,
     * java.lang.String)
     */
    @Override
    public List<CalculBusinessModel> compute(ContextCalcul context, CalculMode calcMode)
            throws JadeApplicationException, JadePersistenceException {

        if (context == null) {
            throw new ALCalculModeException("CalculTypeTravailleurAgricoleJura#compute : context is null");
        }

        if (!(context.getDossier() instanceof DossierAgricoleComplexModel)) {
            throw new ALCalculException(
                    "CalculTypeTravailleurAgricoleJura#compute : dossier is not an instance of DossierAgricoleComplexModel");
        }

        if (calcMode == null) {
            throw new ALCalculModeException("CalculTypeTravailleurAgricoleJura#compute : calcMode is null");
        }

        // calcul des droits selon le type travailleur agricole (y compris
        // ménage fédéral agricole)
        List<CalculBusinessModel> droitsCalcules = super.compute(context, calcMode);

        // ... + ménage Jurassien pour l'agriculture
        computeMenageJura(context.getDossier().getDossierModel(), droitsCalcules);

        return droitsCalcules;
    }

    /**
     * Attribue le montant du ménage agricole jurassien accordé aux travailleurs agricoles
     * 
     * @param dossier
     *            Dossier pour lequel les droits sont calculés
     * @param droitsCalcules
     *            Liste des droits calculés
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    private void computeMenageJura(DossierModel dossier, List<CalculBusinessModel> droitsCalcules)
            throws JadeApplicationException {

        // le calcul des prestations "spéciales" ne doit pas se faire si on est
        // en train de calculer le montant des droits de l'autre conjoint dans
        // le cas d'une intercantonale
        if (JadeThread.currentContext().getTemporaryAttribute("TARIF_AUTRE_PARENT") == null) {

            if (dossier == null) {
                throw new ALCalculModeException("CalculTypeTravailleurAgricoleJura#computeMenageJura : dossier is null");
            }

            if (droitsCalcules == null) {
                throw new ALCalculModeException(
                        "CalculTypeTravailleurAgricoleJura#computeMenageJura : droitsCalcules is null");
            }
            // TODO : à vérifier
            ALImplServiceLocator.getCalculMontantsService().addDroitCalculeSpecial(MONTANT_MENAGE_JURA, droitsCalcules,
                    null, ALCSDroit.TYPE_MEN_LJA, ALCSTarif.CATEGORIE_LJU, ALCSTarif.CATEGORIE_LJU,
                    ALCSTarif.CATEGORIE_LJU);
        }
    }
}
