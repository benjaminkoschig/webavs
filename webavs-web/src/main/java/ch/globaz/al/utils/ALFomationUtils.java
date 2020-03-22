package ch.globaz.al.utils;

import ch.globaz.al.business.constantes.ALConstParametres;
import ch.globaz.param.business.service.ParamServiceLocator;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;

import java.util.Date;

public class ALFomationUtils {

    private ALFomationUtils(){
    }

    public static Integer getAgeFormation(String date) throws JadeApplicationException, JadePersistenceException {
        String age = ParamServiceLocator
                .getParameterModelService()
                .getParameterByName(ALConstParametres.APPNAME, ALConstParametres.DEBUT_DROIT_FORMATION,
                        date).getValeurAlphaParametre();
        return Integer.valueOf(age);
    }

}
