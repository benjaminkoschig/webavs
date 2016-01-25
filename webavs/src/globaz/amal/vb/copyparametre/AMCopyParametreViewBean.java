package globaz.amal.vb.copyparametre;

import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.amal.business.exceptions.models.deductionsFiscalesEnfants.DeductionsFiscalesEnfantsException;
import ch.globaz.amal.business.exceptions.models.parametreannuel.ParametreAnnuelException;
import ch.globaz.amal.business.exceptions.models.subsideannee.SubsideAnneeException;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.amal.businessimpl.utils.parametres.ParametresAnnuelsProvider;

public class AMCopyParametreViewBean extends BJadePersistentObjectViewBean {

    public AMCopyParametreViewBean() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void add() throws Exception {
        // TODO Auto-generated method stub

    }

    public void copyParams(String paramType, String yearToCopy, String newYear)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, SubsideAnneeException,
            ParametreAnnuelException, DeductionsFiscalesEnfantsException {
        switch (Integer.valueOf(paramType)) {
            case 1: // Subsides années
                AmalServiceLocator.getSimpleSubsideAnneeService().copyParams(yearToCopy, newYear);
                break;
            case 2:
                AmalServiceLocator.getParametreAnnuelService().copyParams(yearToCopy, newYear);
                break;
            case 3:
                AmalServiceLocator.getDeductionsFiscalesEnfantsService().copyParams(yearToCopy, newYear);
                break;
            default:
                break;
        }

        // On efface le "cache" des paramètres annuels
        ParametresAnnuelsProvider.resetParametersCache();
        // Puis on reconstruit
        ParametresAnnuelsProvider.initParamAnnuels();
    }

    @Override
    public void delete() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BSpy getSpy() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void retrieve() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void setId(String newId) {
        // TODO Auto-generated method stub

    }

    @Override
    public void update() throws Exception {
        // TODO Auto-generated method stub

    }

}
