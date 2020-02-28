package ch.globaz.al.businessimpl.services.calcul;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.ArrayList;
import java.util.List;

import ch.globaz.al.business.exceptions.calcul.ALCalculException;
import ch.globaz.al.business.models.dossier.DossierAgricoleComplexModel;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.dossier.DossierComplexModelRoot;
import ch.globaz.al.business.models.dossier.DossierDecisionComplexModel;
import ch.globaz.al.business.models.droit.CalculBusinessModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.businessimpl.calcul.context.ContextCalcul;
import ch.globaz.al.businessimpl.calcul.modes.CalculMode;
import ch.globaz.al.businessimpl.calcul.modes.CalculModeFactory;
import ch.globaz.al.businessimpl.calcul.types.CalculType;
import ch.globaz.al.businessimpl.calcul.types.CalculTypeFactory;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;

/**
 * Classe abstraite contenant les méthodes communes aux différents services de calcul
 * 
 * @author jts
 * 
 */
public abstract class CalculAbstractService extends ALAbstractBusinessServiceImpl {
    /**
     * Vérifie que le modèle complexe contienne toutes les informations nécessaire à l'exécution du calcul.
     * 
     * Si <code>dossier</code> n'est pas de type <code>DossierComplexModel</code> ou
     * <code>DossierAgricoleComplexModel</code> une exception est levée.
     * 
     * Si <code>dossier</code> est de type <code>DossierComplexModel</code> et que l'allocataire est un agriculteur, le
     * modèle est rechargé en utilisant un modèle de type <code>DossierAgricoleComplexModel</code>
     * 
     * 
     * @param dossier
     *            le dossier à contrôler
     * @return le dossier contrôlé
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    protected DossierComplexModelRoot checkModelComplex(DossierComplexModelRoot dossier)
            throws JadeApplicationException, JadePersistenceException {

        // dossier "standard"

        if (DossierComplexModel.class.equals(dossier.getClass())
                || DossierAgricoleComplexModel.class.equals(dossier.getClass())
                || DossierDecisionComplexModel.class.equals(dossier.getClass())) {

            // si c'est un dossier agricole qui a été chargé en utilisant le
            // modèle standard on recharge le dossier complet
            if (ALServiceLocator.getDossierBusinessService().isAgricole(
                    dossier.getDossierModel().getActiviteAllocataire())) {

                dossier = ALServiceLocator.getDossierAgricoleComplexModelService().read(
                        dossier.getDossierModel().getIdDossier());
            }
        } else {
            throw new ALCalculException(
                    "CalculServiceImpl#getCalcul : Impossible d'exécuter le calcul, la classe dossier '"
                            + dossier.getClass() + "' n'est pas valide");
        }
        return dossier;
    }

    /**
     * Execute le calcul pour le <code>dossier</code> à la date <code>dateCalcul</code>
     * 
     * @param contextCalcul
     *            Contexte contenant les informations nécessaires à l'exécution du calcul
     * @return Une <code>ArrayList</code> contenant des objets
     *         {@link ch.globaz.al.business.models.droit.CalculBusinessModel} correspondant chacun au résultat du calcul
     *         pour un droit du <code>dossier</code>
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    protected List<CalculBusinessModel> execute(ContextCalcul contextCalcul) throws JadeApplicationException,
            JadePersistenceException {

        // récupération du type de calcul
        CalculType calcType = CalculTypeFactory.getCalculType(contextCalcul);

        // récupération du mode de calcul
        CalculMode calcMode = CalculModeFactory.getCalculMode(contextCalcul);

        // exécution du calcul et retour du résultat
        return calcType.compute(contextCalcul, calcMode);
    }

}
