package ch.globaz.amal.process.repriseDecisionsTaxations;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import ch.globaz.amal.business.constantes.IAMProcess;
import ch.globaz.amal.business.exceptions.models.parametreapplication.ParametreApplicationException;
import ch.globaz.amal.business.models.parametreapplication.SimpleParametreApplication;
import ch.globaz.amal.business.models.parametreapplication.SimpleParametreApplicationSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;

public class AMAbstractProcessJAXBReprise {
    protected static JAXBContext context = null;
    private Map<String, String> mapGroup = null;
    protected Map<String, Map<String, String>> mapProperties = new HashMap<String, Map<String, String>>();
    protected Unmarshaller unmarshaller = null;

    protected void clearProps() {
        mapGroup.clear();
        mapProperties.clear();
        mapGroup = null;
        mapProperties = null;
    }

    public Map<String, Map<String, String>> getMapProperties() {
        return mapProperties;
    }

    protected void initJaxb() {
        try {
            initParameters(IAMProcess.XML_GROUPE_CONTRIBUABLES);
            initParameters(IAMProcess.XML_GROUPE_PERSONNES_CHARGE);

            String class1 = IAMProcess.CLASS_CONTRIBUABLES;
            String class2 = IAMProcess.CLASS_PERSONNES_CHARGE;

            AMAbstractProcessJAXBReprise.context = JAXBContext
                    .newInstance(Class.forName(class1), Class.forName(class2));
            unmarshaller = AMAbstractProcessJAXBReprise.context.createUnmarshaller();
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (Exception e) {
            JadeThread.logError("AMProcessServletAction.actionExporter()",
                    "amal.process.uploadFichierReprise.propertyMissing");
            JadeLogger.error(this, e.getMessage());
        }
    }

    /**
     * Va chercher les propriétés pour la reprise dans la table Parametres applicatifs (MAPARAPP) <br>
     * <ul>
     * <li>namespaceXsd --> Namespace du fichier xsd</li>
     * <li>namespaceXsdUrl --> Url du namespace</li>
     * <li>xsdFile --> Nom du fichier xsd</li> <br>
     * <br>
     * 
     * @param params
     * @throws JadePersistenceException
     * @throws ParametreApplicationException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws Exception
     */
    protected void initParameters(String params) throws JadePersistenceException, ParametreApplicationException,
            JadeApplicationServiceNotAvailableException, Exception {
        SimpleParametreApplicationSearch simpleParametreApplicationSearch = new SimpleParametreApplicationSearch();
        simpleParametreApplicationSearch.setForCsGroupeParametre(params);
        simpleParametreApplicationSearch = AmalServiceLocator.getSimpleParametreApplicationService().search(
                simpleParametreApplicationSearch);

        if (simpleParametreApplicationSearch.getSize() <= 0) {
            throw new Exception("Parameters '" + params + "' not found in application parameters !");
        } else {
            mapGroup = new HashMap<String, String>();
            // this.mapProperties = new HashMap<String, Map<String, String>>();
            for (JadeAbstractModel param : simpleParametreApplicationSearch.getSearchResults()) {
                SimpleParametreApplication parametreApplication = (SimpleParametreApplication) param;
                if (JadeStringUtil.isBlankOrZero(parametreApplication.getValeurParametre())) {
                    throw new Exception("Error ! Param nameSpaceXsd ('" + parametreApplication.getCsTypeParametre()
                            + " can't be null !')");
                }
                if (IAMProcess.XML_NAMESPACE_XSD.equals(parametreApplication.getCsTypeParametre())) {
                    mapGroup.put(IAMProcess.XML_NAMESPACE_XSD, parametreApplication.getValeurParametre());
                } else if (IAMProcess.XML_NAMESPACE_URL.equals(parametreApplication.getCsTypeParametre())) {
                    mapGroup.put(IAMProcess.XML_NAMESPACE_URL, parametreApplication.getValeurParametre());
                } else if (IAMProcess.XML_XSD_FILENAME.equals(parametreApplication.getCsTypeParametre())) {
                    mapGroup.put(IAMProcess.XML_XSD_FILENAME, parametreApplication.getValeurParametre());
                } else {
                    // Erreur
                    throw new Exception("Error ! Parameter type is incorrect ('"
                            + parametreApplication.getCsTypeParametre() + "')");
                }
            }
            mapProperties.put(params, mapGroup);
        }
    }

}
