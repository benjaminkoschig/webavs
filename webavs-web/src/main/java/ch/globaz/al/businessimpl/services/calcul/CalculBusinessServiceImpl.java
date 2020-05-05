package ch.globaz.al.businessimpl.services.calcul;

import ch.globaz.al.business.exceptions.calcul.ALCalculException;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.dossier.DossierComplexModelRoot;
import ch.globaz.al.business.models.droit.CalculBusinessModel;
import ch.globaz.al.business.services.calcul.CalculBusinessService;
import ch.globaz.al.businessimpl.calcul.context.ContextCalcul;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;

import java.util.List;
import java.util.Map;

/**
 * Implémentation du service de calcul
 * 
 * @author jts
 * 
 */
public class CalculBusinessServiceImpl extends CalculAbstractService implements CalculBusinessService {

    @Override
    public List<CalculBusinessModel> getCalcul(DossierComplexModelRoot dossier, String dateCalcul)
            throws JadeApplicationException, JadePersistenceException {

        if (dossier == null) {
            throw new ALCalculException("CalculServiceImpl#getCalcul : Unable to compute, dossier is null");
        }

        if (!JadeDateUtil.isGlobazDate(dateCalcul)) {
            throw new ALCalculException(
                    "CalculServiceImpl#getCalcul : Unable to compute, dateCalcul is not a valid date");
        }

        dossier = checkModelComplex(dossier);

        ContextCalcul context = ContextCalcul.getContextCalcul(dossier, dateCalcul);

        return execute(context);
    }

    @Override
    public Map getTotal(DossierComplexModel dossier, List<CalculBusinessModel> droitsCalcules, String unite,
                        String nbUnite, boolean avecNAIS, String date) throws JadeApplicationException, JadePersistenceException {

        if (dossier == null) {
            throw new ALCalculException("CalculServiceImpl#getTotal : Unable to get the total, dossier is null");
        }

        if (droitsCalcules == null) {
            throw new ALCalculException("CalculServiceImpl#getTotal : Unable to get the total, droitsCalcules is null");
        }

        if (!JadeDateUtil.isGlobazDate(date)) {
            throw new ALCalculException("CalculServiceImpl#getTotal : " + date + " is not a valid date");
        }

        return ALImplServiceLocator.getCalculMontantsService().calculerTotalMontant(dossier, droitsCalcules, unite,
                nbUnite, avecNAIS, date);
    }
}