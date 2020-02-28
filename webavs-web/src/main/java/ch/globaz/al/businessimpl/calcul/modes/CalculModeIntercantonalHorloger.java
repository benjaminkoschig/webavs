package ch.globaz.al.businessimpl.calcul.modes;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.ArrayList;
import java.util.List;

import ch.globaz.al.business.exceptions.calcul.ALCalculException;
import ch.globaz.al.business.models.dossier.DossierComplexModelRoot;
import ch.globaz.al.business.models.droit.CalculBusinessModel;
import ch.globaz.al.business.models.droit.DroitComplexModel;
import ch.globaz.al.businessimpl.calcul.context.ContextCalcul;

public class CalculModeIntercantonalHorloger extends CalculModeIntercantonal {

    private CalculModeHorloger modeHorloger = new CalculModeHorloger();

    @Override
    public List<CalculBusinessModel> compute(ContextCalcul context) throws JadeApplicationException,
            JadePersistenceException {

        if (context == null) {
            throw new ALCalculException("CalculModeJura#compute : context is null");
        }

        modeHorloger.initCalculMode(context);
        return super.compute(context);
    }

    @Override
    protected boolean computeDroit(DossierComplexModelRoot dossier, DroitComplexModel droit, String dateCalcul,
            String typeResident) throws JadeApplicationException, JadePersistenceException {

        if (dossier == null) {
            throw new ALCalculException("CalculModeHorloger#computeDroit : dossier is null");
        }

        if (droit == null) {
            throw new ALCalculException("CalculModeHorloger#computeDroit : droit is null");
        }

        if (!JadeDateUtil.isGlobazDate(dateCalcul)) {
            throw new ALCalculException("CalculModeHorloger#computeDroit : " + dateCalcul + " is not a valid date");
        }

        if (JadeStringUtil.isEmpty(typeResident)) {
            throw new ALCalculException("CalculModeHorloger#computeDroit : typeResident is null or empty");
        }

        boolean res = super.computeDroit(dossier, droit, dateCalcul, typeResident);
        // si on est pas déjà en train de traiter les droits de l'autre parent
        if (JadeStringUtil.isNull(JadeThread.currentContext().getTemporaryAttribute("TARIF_AUTRE_PARENT"))) {
            modeHorloger.computeComplementHorloger(dossier, droit, dateCalcul, typeResident, droitsCalcules);
        }
        return res;
    }
}
