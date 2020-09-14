package ch.globaz.al.utils;

import ch.globaz.al.business.constantes.ALConstParametres;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.param.business.exceptions.models.ParameterModelException;
import ch.globaz.param.business.models.ParameterModel;
import ch.globaz.param.business.models.ParameterSearchModel;
import ch.globaz.param.business.service.ParamServiceLocator;
import ch.globaz.param.business.vo.KeyNameParameter;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;

public class ALFomationUtils {

    private ALFomationUtils(){
    }

    public static Integer getAgeFormation(String dateNaissance) throws JadeApplicationException, JadePersistenceException {
        ParameterSearchModel searchModel = new ParameterSearchModel();
        searchModel.setForIdApplParametre(ALConstParametres.APPNAME);
        searchModel.setForIdCleDiffere(ALConstParametres.DEBUT_DROIT_FORMATION);
        searchModel = ParamServiceLocator
                .getParameterModelService().search(searchModel);
        if (searchModel.getSize() == 0) {
            throw new ParameterModelException(new KeyNameParameter(ALConstParametres.DEBUT_DROIT_FORMATION),
                    "Unable to get the parameter - no parameter found (" + ALConstParametres.DEBUT_DROIT_FORMATION);
        }

        Integer currentAge = null;
        for (int i = 0; i < searchModel.getSize(); i++) {
            ParameterModel currentResult = (ParameterModel) searchModel.getSearchResults()[i];
            Integer age = Integer.valueOf(currentResult.getValeurAlphaParametre());
            String dateDebut = currentResult.getDateDebutValidite();
            if(currentAge != null){
                if(JadeDateUtil.isDateBefore(dateNaissance, dateDebut)
                        && JadeDateUtil.getNbYearsBetween(dateNaissance, dateDebut) >= currentAge) {
                    return currentAge;
                } else if(JadeDateUtil.isDateBefore(dateNaissance, dateDebut)
                        && JadeDateUtil.getNbYearsBetween(dateNaissance, dateDebut) >= age) {
                    return age;
                } else {
                    return age;
                }
            }
            currentAge = age;

        }
        return currentAge;
    }

    public static String calculDateDebutFormation(String dateNaissance) throws JadeApplicationException, JadePersistenceException {
        return ALImplServiceLocator.getDatesEcheancePrivateService()
                .getDateDebutValiditeDroit(calculEcheanceFormation(dateNaissance));
    }
    public static String calculEcheanceFormation(String dateNaissance) throws JadeApplicationException, JadePersistenceException {
        ParameterSearchModel searchModel = new ParameterSearchModel();
        searchModel.setForIdApplParametre(ALConstParametres.APPNAME);
        searchModel.setForIdCleDiffere(ALConstParametres.DEBUT_DROIT_FORMATION);
        searchModel.setOrderKey("dateDebutValiditeAsc");
        searchModel = ParamServiceLocator
                .getParameterModelService().search(searchModel);
        if (searchModel.getSize() == 0) {
            throw new ParameterModelException(new KeyNameParameter(ALConstParametres.DEBUT_DROIT_FORMATION),
                    "Unable to get the parameter - no parameter found (" + ALConstParametres.DEBUT_DROIT_FORMATION);
        }

        Integer currentAge = null;
        for (int i = 0; i < searchModel.getSize(); i++) {
            ParameterModel currentResult = (ParameterModel) searchModel.getSearchResults()[i];
            Integer age = Integer.valueOf(currentResult.getValeurAlphaParametre());
            String dateDebut = currentResult.getDateDebutValidite();
            if(currentAge != null){
                if(JadeDateUtil.isDateBefore(dateNaissance, dateDebut)
                        && JadeDateUtil.getNbYearsBetween(dateNaissance, dateDebut) >= currentAge) {
                    // si 16 ans avant le 01.08.2020
                    return getDateDebutFormatte(dateNaissance, currentAge);
                } else if(JadeDateUtil.isDateBefore(dateNaissance, dateDebut)
                        && JadeDateUtil.getNbYearsBetween(dateNaissance, dateDebut) >= age) {
                    // si 15 ans avant le 01.08.2020
                    return getDateDebutFormatte(JadeDateUtil.addMonths(dateDebut,-1),0);
                } else {
                    // si 15 ans après le 31.07.2020
                    return getDateDebutFormatte(JadeDateUtil.addMonths(dateNaissance, -1), age);
                }
            }
            currentAge = age;

        }
        return getDateDebutFormatte(dateNaissance, currentAge);
    }

    private static String getDateDebutFormatte(String datenaissance, int age) throws JadeApplicationException {
        return ALDateUtils.getDateAjoutAnneesFinMois(datenaissance, age);
    }

}
