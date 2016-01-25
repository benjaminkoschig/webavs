package ch.globaz.perseus.businessimpl.checkers.situationfamille;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import ch.globaz.perseus.business.models.demande.DemandeSearchModel;
import ch.globaz.perseus.business.models.situationfamille.SimpleMembreFamille;
import ch.globaz.perseus.business.models.situationfamille.SimpleSituationFamiliale;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.businessimpl.checkers.PerseusAbstractChecker;

public class SimpleSituationFamilialeChecker extends PerseusAbstractChecker {
    /**
     * @param situationFamiliale
     */
    public static void checkForCreate(SimpleSituationFamiliale situationFamiliale) {
        SimpleSituationFamilialeChecker.checkMandatory(situationFamiliale);
    }

    /**
     * @param situationFamiliale
     */
    public static void checkForDelete(SimpleSituationFamiliale situationFamiliale) {

    }

    /**
     * @param situationFamiliale
     */
    public static void checkForUpdate(SimpleSituationFamiliale situationFamiliale) {
        SimpleSituationFamilialeChecker.checkMandatory(situationFamiliale);
    }

    /**
     * @param situationFamiliale
     *            va checker les champs obligatoires dans la situation familiale PC Familles
     */
    private static void checkMandatory(SimpleSituationFamiliale situationFamiliale) {
        if (JadeStringUtil.isEmpty(situationFamiliale.getIdRequerant())) {
            JadeThread.logError(SimpleMembreFamille.class.getName(),
                    "perseus.simplesituationfamiliale.idrequerant.mandatory");
        }
        if (JadeStringUtil.isBlankOrZero(situationFamiliale.getIdConjoint())
                && !JadeStringUtil.isBlankOrZero(situationFamiliale.getCsTypeConjoint())) {
            JadeThread.logError(SimpleMembreFamille.class.getName(),
                    "perseus.simplesituationfamiliale.idconjoint.mandatory");
        }
        if (!JadeStringUtil.isBlankOrZero(situationFamiliale.getIdConjoint())
                && JadeStringUtil.isBlankOrZero(situationFamiliale.getCsTypeConjoint())) {

            DemandeSearchModel demandeSearch = new DemandeSearchModel();
            demandeSearch.setForIdSituationFamiliale(situationFamiliale.getIdSituationFamilliale());

            try {
                if (PerseusServiceLocator.getDemandeService().count(demandeSearch) >= 1) {
                    JadeThread.logError(SimpleMembreFamille.class.getName(),
                            "perseus.simplesituationfamiliale.cstypeconjoint.mandatory");
                }
            } catch (Exception e) {
                new Exception("Exception in SimpleSituationFamilialeChecker#checkMandatory : " + e.toString(), e);
            }

        }
    }

}
