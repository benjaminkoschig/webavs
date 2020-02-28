package ch.globaz.al.businessimpl.calcul.modes;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.exception.JadeApplicationException;
import java.util.HashSet;
import java.util.Set;

import ch.globaz.al.business.constantes.ALCSTarif;
import ch.globaz.al.business.exceptions.calcul.ALCalculException;
import ch.globaz.al.business.models.dossier.DossierAgricoleComplexModel;
import ch.globaz.al.business.models.dossier.DossierComplexModelRoot;
import ch.globaz.al.business.models.droit.DroitModel;

/**
 * Mode de calcul intercantonal pour les agriculteurs. Il fonctionne de la même manière que le mode
 * {@link CalculModeIntercantonal} mais utilise le tarif fédéral pour l'agriculture.
 * 
 * @author jts
 */
public class CalculModeIntercantonalAgricole extends CalculModeIntercantonal {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.businessimpl.calcul.modes.CalculModeAbstract#getCategoriesList
     * (ch.globaz.al.business.models.dossier.DossierComplexModelAbstract, ch.globaz.al.business.models.droit.DroitModel,
     * java.lang.String)
     */
    @Override
    protected Set<String> getCategoriesList(DossierComplexModelRoot dossier, DroitModel droitModel,
                                            String dateCalcul) throws JadeApplicationException {

        if (dossier == null) {
            throw new ALCalculException("CalculModeIntercantonalAgricole#getCategoriesList : dossier is null");
        }

        if (!(dossier instanceof DossierAgricoleComplexModel)) {
            throw new ALCalculException(
                    "CalculModeIntercantonalAgricole#getCategoriesList : dossier is not an instance of DossierAgricoleComplexModel");
        }

        if (droitModel == null) {
            throw new ALCalculException("CalculModeIntercantonalAgricole#getCategoriesList : droitModel is null");
        }

        if (!JadeDateUtil.isGlobazDate(dateCalcul)) {
            throw new ALCalculException("CalculModeIntercantonalAgricole#getCategoriesList : " + dateCalcul
                    + " is not a valid date");
        }

        Set<String> set = new HashSet<>();

        if (setCategoriesForceesList(dossier, droitModel, set)) {
            return set;
            // agriculteur de montagne
        } else if (((DossierAgricoleComplexModel) dossier).getAllocataireAgricoleComplexModel().getAgricoleModel()
                .getDomaineMontagne().booleanValue()) {

            set.add(ALCSTarif.CATEGORIE_LFM);
            return set;
            // agriculteur de plaine
        } else {
            set.add(ALCSTarif.CATEGORIE_LFP);
            return set;
        }
    }
}
