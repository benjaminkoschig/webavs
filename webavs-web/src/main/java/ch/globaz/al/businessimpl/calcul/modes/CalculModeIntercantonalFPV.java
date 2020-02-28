package ch.globaz.al.businessimpl.calcul.modes;

import ch.globaz.al.business.exceptions.calcul.ALCalculException;
import ch.globaz.al.business.models.dossier.DossierComplexModelRoot;
import ch.globaz.al.business.models.droit.CalculBusinessModel;
import ch.globaz.al.business.models.droit.DroitModel;
import ch.globaz.al.businessimpl.calcul.context.ContextCalcul;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;

import java.util.List;
import java.util.Set;

public class CalculModeIntercantonalFPV extends CalculModeIntercantonal {

    private CalculModeFPV modeFPV = new CalculModeFPV();

    @Override
    public List<CalculBusinessModel> compute(ContextCalcul context) throws JadeApplicationException,
            JadePersistenceException {

        if (context == null) {
            throw new ALCalculException("CalculModeJura#compute : context is null");
        }

        modeFPV.initCalculMode(context);
        return super.compute(context);
    }

    @Override
    protected Set<String> getCategoriesList(DossierComplexModelRoot dossier, DroitModel droitModel,
                                            String dateCalcul) throws JadeApplicationException, JadePersistenceException {

        if (JadeStringUtil.isNull(JadeThread.currentContext().getTemporaryAttribute("TARIF_AUTRE_PARENT"))) {
            return modeFPV.getCategoriesList(dossier, droitModel, dateCalcul);
        } else {
            return super.getCategoriesList(dossier, droitModel, dateCalcul);
        }
    }
}
