package ch.globaz.al.businessimpl.services.models.attribut;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.ArrayList;
import ch.globaz.al.business.constantes.ALCSAffilie;
import ch.globaz.al.business.exceptions.model.attribut.ALAttributEntiteModelException;
import ch.globaz.al.business.models.attribut.AttributEntiteModel;
import ch.globaz.al.business.models.attribut.AttributEntiteSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.models.attribut.AttributEntiteBusinessService;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;
import ch.globaz.naos.business.model.AffiliationSimpleModel;
import ch.globaz.param.business.models.ParameterModel;
import ch.globaz.param.business.models.ParameterSearchModel;
import ch.globaz.param.business.service.ParamServiceLocator;

/**
 * 
 * Impl�mentation des services m�tier des attributs entit�
 * 
 * @author GMO
 * 
 */
public class AttributEntiteBusinessServiceImpl extends ALAbstractBusinessServiceImpl implements
        AttributEntiteBusinessService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.attribut.AttributEntiteBusinessService
     * #changeAttributValueForEntite(java.lang.String, java.lang.String,
     * ch.globaz.al.business.models.attribut.AttributEntiteModel)
     */
    @Override
    public void changeAttributValueForEntite(String idEntite, String typeEntite, AttributEntiteModel updatedAttribut)
            throws JadeApplicationException, JadePersistenceException {

        // Configuration la recherche sur les param�tres g�n�raux de
        // l'application pour v�rifier si attribut � ins�rer est une valeur par
        // d�faut ou une particularit� de l'entit�
        ParameterSearchModel searchParameters = new ParameterSearchModel();
        searchParameters.setForIdCleDiffere(updatedAttribut.getNomAttribut());
        searchParameters.setForValeurAlphaParametre(updatedAttribut.getValeurAlpha());
        searchParameters.setForValeurNumParametre(updatedAttribut.getValeurNum());
        if (AffiliationSimpleModel.class.getName().toString().equals(typeEntite)) {
            searchParameters.setForIdTypeCode(ALCSAffilie.GROUP_ATTRIBUT_AFFILIE);
        } else {
            searchParameters.setForIdTypeCode("0");
        }

        searchParameters = ParamServiceLocator.getParameterModelService().search(searchParameters);

        // configuration la recherche sur les attributs afin de contr�ler si il
        // faut updater ou cr�er selon la valeur de l'attribut
        AttributEntiteSearchModel searchAttributs = new AttributEntiteSearchModel();
        searchAttributs.setForTypeEntite(updatedAttribut.getTypeEntite());
        searchAttributs.setForNomAttribut(updatedAttribut.getNomAttribut());
        searchAttributs.setForCleEntite(updatedAttribut.getCleEntite());
        searchAttributs = ALServiceLocator.getAttributEntiteModelService().search(searchAttributs);

        // si le param�tre g�n�ral (par d�faut) existe avec la m�me valeur,
        // alors on efface l'attribut entit�
        // pour autant qu'il existe
        if (searchParameters.getSize() > 0) {

            if (searchAttributs.getSize() > 1) {
                throw new ALAttributEntiteModelException(
                        "AttributEntiteBusinessServiceImpl#changeAttributValueForEntite :Unable to change attribut-too many similar attributs found");
            }
            if (searchAttributs.getSize() == 1) {
                ALServiceLocator.getAttributEntiteModelService().delete(
                        (AttributEntiteModel) (searchAttributs.getSearchResults()[0]));
            }

        }
        // sinon on peut mettre � jour la valeur de l'attribut de l'entit� ou le
        // cr�e si il n'existe pas
        else {
            if (searchAttributs.getSize() > 1) {
                throw new ALAttributEntiteModelException(
                        "AttributEntiteBusinessServiceImpl#changeAttributValueForEntite :Unable to change attribut-too many similar attributs found");
            }
            // si il existe on l'update avec sa new valeur
            if (searchAttributs.getSize() == 1) {
                String newValAlpha = updatedAttribut.getValeurAlpha();
                String newValNum = updatedAttribut.getValeurNum();
                updatedAttribut = (AttributEntiteModel) searchAttributs.getSearchResults()[0];
                updatedAttribut.setValeurAlpha(newValAlpha);
                updatedAttribut.setValeurNum(newValNum);
                updatedAttribut = ALServiceLocator.getAttributEntiteModelService().update(updatedAttribut);
            } else {
                // sinon on le cr�er dans la table attribut entit�
                updatedAttribut = ALServiceLocator.getAttributEntiteModelService().create(updatedAttribut);
            }
        }

        // 1. recherche dans les param�tres g�n�raux la valeur par d�faut
        // si la valeur de l'attribut = valeur par d�faut => alors on vire de la
        // table attribut entit� si il y est bien s�r
        // sinon on cr�e / update dans la table attribut entit�

    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.attribut.AttributEntiteBusinessService
     * #getAttributListForEntite(java.lang.String, java.lang.String)
     */
    @Override
    public ArrayList getAttributListForEntite(String idEntite, String typeEntite) throws JadeApplicationException,
            JadePersistenceException {

        AttributEntiteSearchModel searchAttributAffilie = new AttributEntiteSearchModel();
        searchAttributAffilie.setForTypeEntite(typeEntite);
        searchAttributAffilie.setForCleEntite(idEntite);
        searchAttributAffilie = ALServiceLocator.getAttributEntiteModelService().search(searchAttributAffilie);

        ParameterSearchModel searchParameters = new ParameterSearchModel();

        if (AffiliationSimpleModel.class.getName().equals(typeEntite)) {
            searchParameters.setForIdTypeCode(ALCSAffilie.GROUP_ATTRIBUT_AFFILIE);
        }

        searchParameters = ParamServiceLocator.getParameterModelService().search(searchParameters);

        ArrayList attributList = new ArrayList();

        for (int i = 0; i < searchParameters.getSearchResults().length; i++) {
            boolean uselessParameter = false;
            ParameterModel currentParameter = ((ParameterModel) searchParameters.getSearchResults()[i]);

            // Pour le param�tre r�cup�r�, on regarde si il utile ou pas (si
            // red�fini pour l'entit�, param�tre g�n�ral inutile)
            for (int j = 0; j < searchAttributAffilie.getSearchResults().length; j++) {
                AttributEntiteModel currentAttributEntite = (AttributEntiteModel) searchAttributAffilie
                        .getSearchResults()[j];

                if (currentParameter.getIdCleDiffere().equals(currentAttributEntite.getNomAttribut())) {
                    uselessParameter = true;
                    break;
                }

            }
            if (!uselessParameter) {
                AttributEntiteModel attributEntiteModel = new AttributEntiteModel();
                attributEntiteModel.setCleEntite(idEntite);
                attributEntiteModel.setNomAttribut(currentParameter.getIdCleDiffere());
                attributEntiteModel.setTypeEntite(typeEntite);
                attributEntiteModel.setValeurAlpha(currentParameter.getValeurAlphaParametre());
                attributEntiteModel.setValeurNum(currentParameter.getValeurNumParametre());

                attributList.add(attributEntiteModel);
            }

        }

        for (int i = 0; i < searchAttributAffilie.getSearchResults().length; i++) {

            AttributEntiteModel currentAttributEntite = (AttributEntiteModel) searchAttributAffilie.getSearchResults()[i];

            attributList.add(currentAttributEntite);

        }

        return attributList;
    }
}
