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
 * Classe abstraite contenant les m�thodes communes aux diff�rents services de calcul
 * 
 * @author jts
 * 
 */
public abstract class CalculAbstractService extends ALAbstractBusinessServiceImpl {
    /**
     * V�rifie que le mod�le complexe contienne toutes les informations n�cessaire � l'ex�cution du calcul.
     * 
     * Si <code>dossier</code> n'est pas de type <code>DossierComplexModel</code> ou
     * <code>DossierAgricoleComplexModel</code> une exception est lev�e.
     * 
     * Si <code>dossier</code> est de type <code>DossierComplexModel</code> et que l'allocataire est un agriculteur, le
     * mod�le est recharg� en utilisant un mod�le de type <code>DossierAgricoleComplexModel</code>
     * 
     * 
     * @param dossier
     *            le dossier � contr�ler
     * @return le dossier contr�l�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    protected DossierComplexModelRoot checkModelComplex(DossierComplexModelRoot dossier)
            throws JadeApplicationException, JadePersistenceException {

        // dossier "standard"

        if (DossierComplexModel.class.equals(dossier.getClass())
                || DossierAgricoleComplexModel.class.equals(dossier.getClass())
                || DossierDecisionComplexModel.class.equals(dossier.getClass())) {

            // si c'est un dossier agricole qui a �t� charg� en utilisant le
            // mod�le standard on recharge le dossier complet
            if (ALServiceLocator.getDossierBusinessService().isAgricole(
                    dossier.getDossierModel().getActiviteAllocataire())) {

                dossier = ALServiceLocator.getDossierAgricoleComplexModelService().read(
                        dossier.getDossierModel().getIdDossier());
            }
        } else {
            throw new ALCalculException(
                    "CalculServiceImpl#getCalcul : Impossible d'ex�cuter le calcul, la classe dossier '"
                            + dossier.getClass() + "' n'est pas valide");
        }
        return dossier;
    }

    /**
     * Execute le calcul pour le <code>dossier</code> � la date <code>dateCalcul</code>
     * 
     * @param contextCalcul
     *            Contexte contenant les informations n�cessaires � l'ex�cution du calcul
     * @return Une <code>ArrayList</code> contenant des objets
     *         {@link ch.globaz.al.business.models.droit.CalculBusinessModel} correspondant chacun au r�sultat du calcul
     *         pour un droit du <code>dossier</code>
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    protected List<CalculBusinessModel> execute(ContextCalcul contextCalcul) throws JadeApplicationException,
            JadePersistenceException {

        // r�cup�ration du type de calcul
        CalculType calcType = CalculTypeFactory.getCalculType(contextCalcul);

        // r�cup�ration du mode de calcul
        CalculMode calcMode = CalculModeFactory.getCalculMode(contextCalcul);

        // ex�cution du calcul et retour du r�sultat
        return calcType.compute(contextCalcul, calcMode);
    }

}
