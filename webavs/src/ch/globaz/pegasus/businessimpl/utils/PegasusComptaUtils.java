package ch.globaz.pegasus.businessimpl.utils;

import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIReferenceRubrique;
import globaz.osiris.api.APIRubrique;
import globaz.prestation.tools.PRSession;
import ch.globaz.pegasus.business.exceptions.models.lot.ComptabiliserLotException;

public class PegasusComptaUtils {
    public static APIRubrique findRubrique(String idCodeReferenceRubrique) throws ComptabiliserLotException {
        APIRubrique rubrique = null;

        if (JadeStringUtil.isIntegerEmpty(idCodeReferenceRubrique)) {
            throw new ComptabiliserLotException("Unable to findRubrique the  idCodeReferenceRubrique is empty!");
        }

        APIReferenceRubrique referenceRubrique;
        try {
            referenceRubrique = (APIReferenceRubrique) PRSession.connectSession(
                    BSessionUtil.getSessionFromThreadContext(), "OSIRIS").getAPIFor(APIReferenceRubrique.class);
        } catch (Exception e) {
            throw new ComptabiliserLotException("Technical exception, error to retrieve the reference rubrique", e);
        }

        rubrique = referenceRubrique.getRubriqueByCodeReference(idCodeReferenceRubrique);
        if (rubrique == null) {
            throw new ComptabiliserLotException("No rubrique was found with this reférenceRubrique: [label: "
                    + BSessionUtil.getSessionFromThreadContext().getCodeLibelle(idCodeReferenceRubrique) + "], [id: "
                    + idCodeReferenceRubrique
                    + "]! You need to paramatrize the reference rubrique in the module osiris");
        }
        return rubrique;

    }
}
