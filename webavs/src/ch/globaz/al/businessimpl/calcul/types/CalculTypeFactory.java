package ch.globaz.al.businessimpl.calcul.types;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.constantes.ALCSTarif;
import ch.globaz.al.business.exceptions.calcul.ALCalculModeException;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.businessimpl.calcul.context.ContextCalcul;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * Classe fournissant les m�thodes n�cessaires � l'identification du type de calcul � appliquer en fonction du contexte
 * courant
 * 
 * @author jts
 * 
 */
public abstract class CalculTypeFactory {

    /**
     * Retourne le type de calcul pour un calcul ayant lieu sous le r�gime "avant LAFam" pour un allocataire agricole
     * 
     * @param context
     *            contexte contenant les donn�es n�cessaire � l'ex�cution du calcul
     * @return une instance de type <code>CalculType</code> � utiliser pour l'ex�cution du calcul d'un dossier agricole
     *         avant la LAFam
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.al.businessimpl.calcul.types.CalculType
     */
    private static CalculType getCalculAgricoleBeforeLAFam(ContextCalcul context) throws JadePersistenceException,
            JadeApplicationException {

        if (context == null) {
            throw new ALCalculModeException("CalculTypeFactory#getCalculAgricoleBeforeLAFam : context is null");
        }

        String tarif = context.getTarif();

        // travailleur agricole Jura
        if (ALCSDossier.ACTIVITE_TRAVAILLEUR_AGRICOLE.equals(context.getDossier().getDossierModel()
                .getActiviteAllocataire())
                && ALCSTarif.CATEGORIE_JU.equals(tarif)) {
            return new CalculTypeTravailleurAgricoleJura();
            // dans tous les autres cas, le type standard s'applique
        } else {
            return new CalculTypeStandard();
        }
    }

    /**
     * Retourne le type de calcul pour un calcul ayant lieu sous le r�gime "avant LAFam"
     * 
     * @param context
     *            contexte contenant les donn�es n�cessaire � l'ex�cution du calcul
     * @return une instance de type <code>CalculType</code> � utiliser pour l'ex�cution du calcul pour une date avant la
     *         LAFam
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.al.businessimpl.calcul.types.CalculType
     */
    private static CalculType getCalculBeforeLAFam(ContextCalcul context) throws JadePersistenceException,
            JadeApplicationException {

        if (context == null) {
            throw new ALCalculModeException("CalculTypeFactory#getCalculBeforeLAFam : context is null");
        }

        // allocataire agricole
        if (ALServiceLocator.getDossierBusinessService().isAgricole(
                context.getDossier().getDossierModel().getActiviteAllocataire())) {
            return getCalculAgricoleBeforeLAFam(context);

            // canton de Vaud (traitement famille nombreuse)
        } else if (ALCSTarif.CATEGORIE_VD.equals(context.getTarif())) {
            return new CalculTypeVaud();
            // dans tous les autres cas, le type standard s'applique
        } else {
            return new CalculTypeStandard();
        }
    }

    /**
     * Retourne le type de calcul correspondant au contexte courant
     * 
     * @param context
     *            contexte contenant les donn�es n�cessaire � l'ex�cution du calcul
     * @return une instance de type <code>CalculType</code> � utiliser pour l'ex�cution du calcul
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.al.businessimpl.calcul.types.CalculType
     */
    public static CalculType getCalculType(ContextCalcul context) throws JadePersistenceException,
            JadeApplicationException {

        if (context == null) {
            throw new ALCalculModeException("CalculTypeFactory#getCalculType : context is null");
        }

        if (ALImplServiceLocator.getCalculService().isDateLAFam(context.getDateCalcul())) {
            return new CalculTypeStandard();
        } else {
            return getCalculBeforeLAFam(context);
        }
    }
}
