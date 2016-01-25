package globaz.amal.vb.process;

import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.jade.smtp.JadeSmtpClient;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import org.xml.sax.SAXParseException;
import ch.globaz.amal.business.constantes.IAMProcess;
import ch.globaz.amal.business.exceptions.models.parametreapplication.ParametreApplicationException;
import ch.globaz.amal.business.exceptions.models.uploadfichierreprise.AMUploadFichierRepriseException;
import ch.globaz.amal.business.models.parametreapplication.SimpleParametreApplication;
import ch.globaz.amal.business.models.parametreapplication.SimpleParametreApplicationSearch;
import ch.globaz.amal.business.models.uploadfichierreprise.SimpleUploadFichierReprise;
import ch.globaz.amal.business.models.uploadfichierreprise.SimpleUploadFichierRepriseSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.amal.businessimpl.services.models.uploadfichierreprise.SimpleUploadValidation;

public class AMUploadFichierRepriseViewBean extends BJadePersistentObjectViewBean implements IAMProcess {
    protected JAXBContext context;
    private String currentTypeReprise = null;
    private String eMailAdress = new String();
    private String fileName = new String();
    private String namespaceXsd = "";
    private String namespaceXsdUrl = "";
    private Unmarshaller unmarshaller = null;
    private String xsdFile = "";

    @Override
    public void add() throws Exception {
    }

    private void checkIfContribuablesExists() throws AMUploadFichierRepriseException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        SimpleUploadFichierRepriseSearch repriseSearch = new SimpleUploadFichierRepriseSearch();
        repriseSearch.setLikeTypeReprise("CONTRI");

        if (AmalServiceLocator.getSimpleUploadFichierService().count(repriseSearch) == 0) {
            throw new AMUploadFichierRepriseException("FICHIER CONTRIBUABLES NON IMPORTE !");
        }

    }

    @Override
    public void delete() throws Exception {
    }

    /**
     * DELETE * FROM MAUPLOFR WHERE TYPREP LIKE 'CONTRI%' OR TYPREP LIKE 'PCHARG%' AND IDJOB=0
     * 
     * @throws AMUploadFichierRepriseException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    private void deleteUploadFichierRepriseTable() throws AMUploadFichierRepriseException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        SimpleUploadFichierRepriseSearch repriseSearch = new SimpleUploadFichierRepriseSearch();
        JadeLogger.info("INFO", "---------- DELETE UPLOAD TABLE ----------");
        repriseSearch.setWhereKey("delete");
        repriseSearch.setLikeTypeRepriseContribuable("CONTRI");
        repriseSearch.setLikeTypeReprisePersonneCharge("PCHARG");
        repriseSearch.setForIdJob("0");
        repriseSearch.setDefinedSearchSize(0);
        int nbDeleted = AmalServiceLocator.getSimpleUploadFichierService().delete(repriseSearch);
        JadeLogger.info("INFO", "---------- DELETE UPLOAD TABLE DONE - " + nbDeleted + " ROWS DELETED ----------");
    }

    public String geteMailAdress() {
        return eMailAdress;
    }

    public String getFileName() {
        return fileName;
    }

    @Override
    public String getId() {
        return "";
    }

    public String getNamespaceXsd() {
        return namespaceXsd;
    }

    public String getNamespaceXsdUrl() {
        return namespaceXsdUrl;
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
    private void getRepriseParameters(String params) throws JadePersistenceException, ParametreApplicationException,
            JadeApplicationServiceNotAvailableException, Exception {
        SimpleParametreApplicationSearch simpleParametreApplicationSearch = new SimpleParametreApplicationSearch();
        simpleParametreApplicationSearch.setForCsGroupeParametre(params);
        simpleParametreApplicationSearch = AmalServiceLocator.getSimpleParametreApplicationService().search(
                simpleParametreApplicationSearch);

        if (simpleParametreApplicationSearch.getSize() <= 0) {
            throw new Exception("Parameters '" + params + "' not found in application parameters !");
        } else {
            for (JadeAbstractModel param : simpleParametreApplicationSearch.getSearchResults()) {
                SimpleParametreApplication parametreApplication = (SimpleParametreApplication) param;
                if (IAMProcess.XML_NAMESPACE_XSD.equals(parametreApplication.getCsTypeParametre())) {
                    namespaceXsd = parametreApplication.getValeurParametre();
                    if (JadeStringUtil.isBlankOrZero(namespaceXsd)) {
                        throw new Exception("Error ! Param nameSpaceXsd ('" + parametreApplication.getCsTypeParametre()
                                + " can't be null !')");
                    }
                } else if (IAMProcess.XML_NAMESPACE_URL.equals(parametreApplication.getCsTypeParametre())) {
                    namespaceXsdUrl = parametreApplication.getValeurParametre();
                    if (JadeStringUtil.isBlankOrZero(namespaceXsdUrl)) {
                        throw new Exception("Error ! Param namespaceXsdUrl ('"
                                + parametreApplication.getCsTypeParametre() + " can't be null !')");
                    }
                } else if (IAMProcess.XML_XSD_FILENAME.equals(parametreApplication.getCsTypeParametre())) {
                    xsdFile = parametreApplication.getValeurParametre();
                    if (JadeStringUtil.isBlankOrZero(xsdFile)) {
                        throw new Exception("Error ! Param xsdFile ('" + parametreApplication.getCsTypeParametre()
                                + " can't be null !')");
                    }
                } else {
                    // Erreur
                    throw new Exception("Error ! Parameter type is incorrect ('"
                            + parametreApplication.getCsTypeParametre() + "')");
                }
            }
        }
    }

    @Override
    public BSpy getSpy() {
        return new BSpy((BSession) getISession());
    }

    public String getXsdFile() {
        return xsdFile;
    }

    /**
     * Process pour les contribuables
     * 
     * @param dateDecision
     * @param jobDate
     * 
     * @param contribuables
     * @throws Exception
     */
    private void processContribuable(ch.globaz.amal.xmlns.am_0001._1.Contribuable contribuable, String jobDate,
            String dateDecision) throws Exception {
        SimpleUploadFichierReprise simpleUploadFichierReprise = new SimpleUploadFichierReprise();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Marshaller m = context.createMarshaller();

        ch.globaz.amal.xmlns.am_0001._1.ObjectFactory factory = new ch.globaz.amal.xmlns.am_0001._1.ObjectFactory();
        ch.globaz.amal.xmlns.am_0001._1.Contribuables cf = factory.createContribuables();
        cf.setParamDateDecision(dateDecision);
        cf.setParamJobDate(jobDate);
        cf.getContribuable().add(contribuable);
        m.marshal(cf, out);

        simpleUploadFichierReprise.setNoContribuable(contribuable.getNdc());
        simpleUploadFichierReprise.setXmlLignes(JadeStringUtil.decodeUTF8(out.toString()));

        simpleUploadFichierReprise.setTypeReprise(IAMProcess.TYPE_REPRISE_CONTRIBUABLE);
        for (ch.globaz.amal.xmlns.am_0001._1.Personne personne : contribuable.getPersonne()) {
            if (ch.globaz.amal.xmlns.am_0001._1.TypePersonne.PRINCIPAL.equals(personne.getType())) {
                String nomPrenom = contribuable.getPersonne().get(0).getNom() + " "
                        + contribuable.getPersonne().get(0).getPrenom();
                simpleUploadFichierReprise.setNomPrenom(nomPrenom.toUpperCase());
                break;
            }
        }
        simpleUploadFichierReprise = AmalServiceLocator.getSimpleUploadFichierService().create(
                simpleUploadFichierReprise);
    }

    private void processPersonnesCharge(ch.globaz.amal.xmlns.am_0002._1.Contribuable personneCharge) throws Exception {
        SimpleUploadFichierReprise simpleUploadFichierReprise = new SimpleUploadFichierReprise();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Marshaller m = context.createMarshaller();

        ch.globaz.amal.xmlns.am_0002._1.ObjectFactory factory = new ch.globaz.amal.xmlns.am_0002._1.ObjectFactory();
        ch.globaz.amal.xmlns.am_0002._1.Contribuables cf = factory.createContribuables();
        cf.getContribuable().add(personneCharge);
        m.marshal(cf, out);

        simpleUploadFichierReprise.setNoContribuable(personneCharge.getNdc());
        simpleUploadFichierReprise.setXmlLignes(JadeStringUtil.decodeUTF8(out.toString()));

        simpleUploadFichierReprise.setTypeReprise(IAMProcess.TYPE_REPRISE_PERSONNE_CHARGE);

        simpleUploadFichierReprise = AmalServiceLocator.getSimpleUploadFichierService().create(
                simpleUploadFichierReprise);

    }

    @Override
    public void retrieve() throws Exception {
        // TODO Auto-generated method stub
    }

    /**
     * Méthode qui va lancer le process de serialization et d'insertion en DB
     * 
     * @param path
     * @param fileNameUploaded
     * @param params
     * @return
     */
    /**
     * @param path
     * @param fileNameUploaded
     * @param params
     * @return
     */
    public SimpleUploadValidation serializeXmlFile(String path, String fileNameUploaded, String params) {
        SimpleUploadValidation validation = new SimpleUploadValidation();
        try {
            getRepriseParameters(params);
            fileName = path + fileNameUploaded;

            try {
                context = JAXBContext.newInstance(namespaceXsd);
                unmarshaller = context.createUnmarshaller();

                javax.xml.stream.XMLInputFactory xif = javax.xml.stream.XMLInputFactory.newInstance();
                XMLStreamReader xsr = xif.createXMLStreamReader(new FileReader(fileName));

                String jobDate = "";
                String dateDecision = "";
                long cpt = 0;
                if (IAMProcess.PACKAGE_CONTRIBUABLES.indexOf(namespaceXsd) != -1) {
                    deleteUploadFichierRepriseTable();
                } else if (IAMProcess.PACKAGE_PERSONNES_CHARGE.indexOf(namespaceXsd) != -1) {
                    checkIfContribuablesExists();
                }
                while (xsr.hasNext()) {
                    int type = xsr.next();
                    if ((type == XMLStreamConstants.START_ELEMENT) && ("Contribuables".equals(xsr.getLocalName()))) {
                        jobDate = xsr.getAttributeValue(null, "paramJobDate");
                        dateDecision = xsr.getAttributeValue(null, "paramDateDecision");
                    } else if ((type == XMLStreamConstants.START_ELEMENT)
                            && ("Contribuable".equals(xsr.getLocalName()))) {
                        Object o = unmarshaller.unmarshal(xsr);
                        if (IAMProcess.PACKAGE_CONTRIBUABLES.indexOf(namespaceXsd) != -1) {
                            currentTypeReprise = IAMProcess.TYPE_REPRISE_CONTRIBUABLE;
                            ch.globaz.amal.xmlns.am_0001._1.Contribuable contribuable = ((JAXBElement<ch.globaz.amal.xmlns.am_0001._1.Contribuable>) o)
                                    .getValue();
                            processContribuable(contribuable, jobDate, dateDecision);
                        } else if (IAMProcess.PACKAGE_PERSONNES_CHARGE.indexOf(namespaceXsd) != -1) {
                            currentTypeReprise = IAMProcess.TYPE_REPRISE_PERSONNE_CHARGE;
                            ch.globaz.amal.xmlns.am_0002._1.Contribuable personneCharge = ((JAXBElement<ch.globaz.amal.xmlns.am_0002._1.Contribuable>) o)
                                    .getValue();
                            processPersonnesCharge(personneCharge);
                        }
                        cpt++;

                        if (cpt >= 100) {
                            JadeThread.commitSession();
                            cpt = 0;
                        }
                    }
                }

            } catch (JAXBException jbe) {
                JadeThread.logError(this.getClass().getName(), jbe.getMessage());
                String msgError = "";
                if (!(jbe.getLinkedException() == null)) {
                    SAXParseException se = (SAXParseException) jbe.getLinkedException();
                    msgError = "Erreur fichier non valide ! ==>" + se.getMessage() + ", Ligne : " + se.getLineNumber()
                            + ", Colonne : " + se.getColumnNumber();
                    JadeThread.logError(this.getClass().getName(), msgError);
                }

                validation.setReturnCode(1);
                if (!JadeStringUtil.isBlankOrZero(msgError)) {
                    validation.setErrorMsg(msgError);
                } else {
                    validation.setErrorMsg(jbe.getMessage());
                }
                return validation;
            }
            String to = JadeThread.currentUserEmail();
            String subject = "";
            String body = "";
            if (IAMProcess.TYPE_REPRISE_CONTRIBUABLE.equals(currentTypeReprise)) {
                subject = "Web@Amal : Importation fichier 'Contribuables' effectuée avec succès !";
                body = "Le fichier '" + fileNameUploaded + "' a correctement été importé";
            } else if (IAMProcess.TYPE_REPRISE_PERSONNE_CHARGE.equals(currentTypeReprise)) {
                subject = "Web@Amal : Importation fichier 'Personnes à charges' effectuée avec succès !";
                body = "Le fichier '" + fileNameUploaded + "' a correctement été importé";
            } else {
                subject = "Web@Amal : Importation fichier effectuée avec succès !";
                body = "Le fichier " + fileNameUploaded + " a correctement été importé";
            }

            JadeSmtpClient.getInstance().sendMail(to, subject, body, null);

            return validation;
        } catch (Exception e) {
            e.printStackTrace();
            JadeThread.logError(this.getClass().getName(), e.getMessage());
            validation.setReturnCode(1);
            validation.setErrorMsg(e.toString());
            return validation;
        }
    }

    public void seteMailAdress(String eMailAdress) {
        this.eMailAdress = eMailAdress;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void setId(String newId) {
        // TODO Auto-generated method stub
    }

    public void setNamespaceXsd(String namespaceXsd) {
        this.namespaceXsd = namespaceXsd;
    }

    public void setNamespaceXsdUrl(String namespaceXsdUrl) {
        this.namespaceXsdUrl = namespaceXsdUrl;
    }

    public void setXsdFile(String xsdFile) {
        this.xsdFile = xsdFile;
    }

    @Override
    public void update() throws Exception {
        // TODO Auto-generated method stub
    }

}
