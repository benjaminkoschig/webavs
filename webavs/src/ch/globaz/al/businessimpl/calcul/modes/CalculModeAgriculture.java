package ch.globaz.al.businessimpl.calcul.modes;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.exception.JadeApplicationException;
import java.util.HashSet;
import ch.globaz.al.business.constantes.ALCSTarif;
import ch.globaz.al.business.exceptions.calcul.ALCalculException;
import ch.globaz.al.business.models.dossier.DossierAgricoleComplexModel;
import ch.globaz.al.business.models.dossier.DossierComplexModelRoot;
import ch.globaz.al.business.models.droit.DroitModel;

/**
 * Mode de calcul pour les agriculteurs. La différence par rapport à un calcul standard est que ce n'est pas le tarif
 * cantonal qui est utilisé mais le tarif fédéral pour l'agriculture.
 * 
 * @author jts
 */
public class CalculModeAgriculture extends CalculModeAbstract {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.businessimpl.calcul.modes.CalculModeAbstract#getCategoriesList
     * (ch.globaz.al.business.models.dossier.DossierComplexModelAbstract, ch.globaz.al.business.models.droit.DroitModel,
     * java.lang.String)
     */
    @Override
    protected HashSet<String> getCategoriesList(DossierComplexModelRoot dossier, DroitModel droitModel,
            String dateCalcul) throws JadeApplicationException {

        if (dossier == null) {
            throw new ALCalculException("CalculModeAgriculture#getCategoriesList : dossier is null");
        }

        if (!(dossier instanceof DossierAgricoleComplexModel)) {
            throw new ALCalculException(
                    "CalculModeAgriculture#getCategoriesList : dossier is not an instance of DossierAgricoleComplexModel");
        }

        if (droitModel == null) {
            throw new ALCalculException("CalculModeAgriculture#getCategoriesList : droitModel is null");
        }

        if (!JadeDateUtil.isGlobazDate(dateCalcul)) {
            throw new ALCalculException("CalculModeAgriculture#getCategoriesList : " + dateCalcul
                    + " is not a valid date");
        }

        HashSet<String> set = new HashSet<String>();

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
