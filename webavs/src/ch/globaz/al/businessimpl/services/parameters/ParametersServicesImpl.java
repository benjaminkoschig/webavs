package ch.globaz.al.businessimpl.services.parameters;

import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.Date;
import ch.globaz.al.business.constantes.ALConstParametres;
import ch.globaz.al.business.exceptions.generation.prestations.ALGenerationException;
import ch.globaz.al.business.services.parameters.ParametersServices;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;
import ch.globaz.param.business.models.ParameterModel;
import ch.globaz.param.business.service.ParamServiceLocator;

/**
 * Implémentation du service de gestion de paramètres AF
 * 
 * @author jts
 * 
 */
public class ParametersServicesImpl extends ALAbstractBusinessServiceImpl implements ParametersServices {

    @Override
    public String getNomCaisse() throws JadeApplicationException, JadePersistenceException {
        return this.getNomCaisse(JadeDateUtil.getGlobazFormattedDate(new Date()));
    }

    @Override
    public String getNomCaisse(String date) throws JadeApplicationException, JadePersistenceException {

        ParameterModel param = ParamServiceLocator.getParameterModelService().getParameterByName(
                ALConstParametres.APPNAME, ALConstParametres.NOM_CAISSE, date);

        return param.getValeurAlphaParametre();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.parameters.ParametersServices#isCheckPCFamille()
     */
    @Override
    public Boolean isCheckPCFamille() throws JadeApplicationException, JadePersistenceException {
        try {
            ParameterModel param = ParamServiceLocator.getParameterModelService().getParameterByName(
                    ALConstParametres.APPNAME, ALConstParametres.CHECK_PC_FAMILLE, JACalendar.todayJJsMMsAAAA());
            if ("0".equals(param.getValeurAlphaParametre())) {
                return false;
            } else if ("1".equals(param.getValeurAlphaParametre())) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean isFnbEnabled(String date) throws JadeApplicationException, JadePersistenceException {
        ParameterModel param = ParamServiceLocator.getParameterModelService().getParameterByName(
                ALConstParametres.APPNAME, ALConstParametres.FNB_IS_ENABLED, date);

        if ("0".equals(param.getValeurAlphaParametre())) {
            return false;
        } else if ("1".equals(param.getValeurAlphaParametre())) {
            return true;
        } else {
            throw new ALGenerationException("ParametersServicesImpl#isFnbEnabled : Parameter has not a valid value");
        }
    }
}
