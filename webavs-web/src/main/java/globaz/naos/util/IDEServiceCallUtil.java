package globaz.naos.util;

import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.crypto.JadeDecryptionNotSupportedException;
import globaz.jade.crypto.JadeDefaultEncrypters;
import globaz.jade.crypto.JadeEncrypterNotFoundException;
import globaz.naos.properties.AFProperties;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.rmi.RemoteException;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import org.apache.axis.types.NonNegativeInteger;
import ch.admin.bit.xmlns.uid_wse_f._3.RegisterDeregisterItem;
import ch.admin.bit.xmlns.uid_wse_f._3.RegisterDeregisterResult;
import ch.admin.bit.xmlns.uid_wse_shared._1.RegisterDeregisterStatus;
import ch.admin.bit.xmlns.uid_wse_shared._1.SearchMode;
import ch.admin.uid.xmlns.uid_wse.ArrayOfRatedOrganisation;
import ch.admin.uid.xmlns.uid_wse.ArrayOfUidStructureType;
import ch.admin.uid.xmlns.uid_wse.IPartnerServices;
import ch.admin.uid.xmlns.uid_wse.IPartnerServicesCreateBusinessFaultFaultFaultMessage;
import ch.admin.uid.xmlns.uid_wse.IPartnerServicesCreateInfrastructureFaultFaultFaultMessage;
import ch.admin.uid.xmlns.uid_wse.IPartnerServicesCreateSecurityFaultFaultFaultMessage;
import ch.admin.uid.xmlns.uid_wse.IPartnerServicesDeleteBusinessFaultFaultFaultMessage;
import ch.admin.uid.xmlns.uid_wse.IPartnerServicesDeleteInfrastructureFaultFaultFaultMessage;
import ch.admin.uid.xmlns.uid_wse.IPartnerServicesDeleteScheduledBusinessFaultFaultFaultMessage;
import ch.admin.uid.xmlns.uid_wse.IPartnerServicesDeleteScheduledInfrastructureFaultFaultFaultMessage;
import ch.admin.uid.xmlns.uid_wse.IPartnerServicesDeleteScheduledSecurityFaultFaultFaultMessage;
import ch.admin.uid.xmlns.uid_wse.IPartnerServicesDeleteSecurityFaultFaultFaultMessage;
import ch.admin.uid.xmlns.uid_wse.IPartnerServicesDeregisterBusinessFaultFaultFaultMessage;
import ch.admin.uid.xmlns.uid_wse.IPartnerServicesDeregisterInfrastructureFaultFaultFaultMessage;
import ch.admin.uid.xmlns.uid_wse.IPartnerServicesDeregisterScheduledBusinessFaultFaultFaultMessage;
import ch.admin.uid.xmlns.uid_wse.IPartnerServicesDeregisterScheduledInfrastructureFaultFaultFaultMessage;
import ch.admin.uid.xmlns.uid_wse.IPartnerServicesDeregisterScheduledSecurityFaultFaultFaultMessage;
import ch.admin.uid.xmlns.uid_wse.IPartnerServicesDeregisterSecurityFaultFaultFaultMessage;
import ch.admin.uid.xmlns.uid_wse.IPartnerServicesReactivateBusinessFaultFaultFaultMessage;
import ch.admin.uid.xmlns.uid_wse.IPartnerServicesReactivateInfrastructureFaultFaultFaultMessage;
import ch.admin.uid.xmlns.uid_wse.IPartnerServicesReactivateSecurityFaultFaultFaultMessage;
import ch.admin.uid.xmlns.uid_wse.IPartnerServicesRegisterBusinessFaultFaultFaultMessage;
import ch.admin.uid.xmlns.uid_wse.IPartnerServicesRegisterInfrastructureFaultFaultFaultMessage;
import ch.admin.uid.xmlns.uid_wse.IPartnerServicesRegisterScheduledBusinessFaultFaultFaultMessage;
import ch.admin.uid.xmlns.uid_wse.IPartnerServicesRegisterScheduledInfrastructureFaultFaultFaultMessage;
import ch.admin.uid.xmlns.uid_wse.IPartnerServicesRegisterScheduledSecurityFaultFaultFaultMessage;
import ch.admin.uid.xmlns.uid_wse.IPartnerServicesRegisterSecurityFaultFaultFaultMessage;
import ch.admin.uid.xmlns.uid_wse.IPartnerServicesSubscribeBusinessFaultFaultFaultMessage;
import ch.admin.uid.xmlns.uid_wse.IPartnerServicesSubscribeInfrastructureFaultFaultFaultMessage;
import ch.admin.uid.xmlns.uid_wse.IPartnerServicesSubscribeSecurityFaultFaultFaultMessage;
import ch.admin.uid.xmlns.uid_wse.IPartnerServicesUnsubscribeBusinessFaultFaultFaultMessage;
import ch.admin.uid.xmlns.uid_wse.IPartnerServicesUnsubscribeInfrastructureFaultFaultFaultMessage;
import ch.admin.uid.xmlns.uid_wse.IPartnerServicesUnsubscribeSecurityFaultFaultFaultMessage;
import ch.admin.uid.xmlns.uid_wse.IPartnerServicesUpdateBusinessFaultFaultFaultMessage;
import ch.admin.uid.xmlns.uid_wse.IPartnerServicesUpdateInfrastructureFaultFaultFaultMessage;
import ch.admin.uid.xmlns.uid_wse.IPartnerServicesUpdateSecurityFaultFaultFaultMessage;
import ch.ech.xmlns.ech_0097_f._2.UidOrganisationIdCategorieType;
import ch.ech.xmlns.ech_0097_f._2.UidStructureType;
import ch.ech.xmlns.ech_0108_f._3.OrganisationType;

public class IDEServiceCallUtil {

    private final static int SERVICE_MAX_RECORD_RETURN = 30;
    private static final int MAX_LOT_ANNONCE_SORTANTE_MASSE_SIZE = 99;

    private static String logCallWebService(BSession session, String methodIDEServiceCallUtilCalled,
            String methodWebServiceCalled, String dataSended) {

        SimpleDateFormat dateHeureFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        String dateHeureCourante = dateHeureFormat.format(new Date());

        StringBuffer logMessage = new StringBuffer();
        logMessage.append(IDEServiceCallUtil.class.getName() + " Method : " + methodIDEServiceCallUtilCalled + "\n");
        logMessage.append("User : " + session.getUserId() + " \n");
        logMessage.append("Time : " + dateHeureCourante + " \n");
        logMessage.append("WebService Method called : " + methodWebServiceCalled + " \n");
        logMessage.append("Data sended : " + dataSended + " \n");

        System.out.println(logMessage.toString());

        return logMessage.toString();

    }

    public static final IPartnerServices initService() throws JadeDecryptionNotSupportedException,
            JadeEncrypterNotFoundException, Exception {
        final String userWebservice;
        final String passwordWebservice;
        final String wsdlWebservice;
        final String namespaceURIWebservice;
        final String localPartWebservice;

        try {
            userWebservice = JadeDefaultEncrypters.getJadeDefaultEncrypter().decrypt(
                    AFProperties.SERVICE_USER_PROPERTY.getValue());
            passwordWebservice = JadeDefaultEncrypters.getJadeDefaultEncrypter().decrypt(
                    AFProperties.SERVICE_PASSWORD_PROPERTY.getValue());
            wsdlWebservice = AFProperties.SERVICE_URI_WSDL.getValue();
            namespaceURIWebservice = AFProperties.SERVICE_URI_NAMESPACE.getValue();
            localPartWebservice = AFProperties.SERVICE_LOCALPART.getValue();
        } catch (GeneralSecurityException gse) {
            throw new Exception("Illegal properties decryption  : please check your properties", gse);
        }
        Authenticator myauAuthenticator = new Authenticator() {

            @Override
            protected PasswordAuthentication getPasswordAuthentication() {

                if (wsdlWebservice.contains(getRequestingHost())) {
                    return new PasswordAuthentication(userWebservice, passwordWebservice.toCharArray());
                }
                return null;
            }
        };

        Authenticator.setDefault(myauAuthenticator);

        Service service = Service.create(new URL(wsdlWebservice),
                new QName(namespaceURIWebservice, localPartWebservice));

        IPartnerServices port = service.getPort(IPartnerServices.class);

        Map<String, Object> req_ctx = ((BindingProvider) port).getRequestContext();
        req_ctx.put(BindingProvider.USERNAME_PROPERTY, userWebservice);
        req_ctx.put(BindingProvider.PASSWORD_PROPERTY, passwordWebservice);

        return port;
    }

    public static final IDEDataBean createEntiteIde(BSession session, IDEDataBean ideDataBean, IPartnerServices port)
            throws IPartnerServicesCreateBusinessFaultFaultFaultMessage,
            IPartnerServicesCreateInfrastructureFaultFaultFaultMessage,
            IPartnerServicesCreateSecurityFaultFaultFaultMessage {

        OrganisationType organisation = null;
        organisation = IDEServiceMappingUtil.getStructureForCreateEntiteIde(ideDataBean);

        try {
            logCallWebService(session, "createEntiteIde", "create", giveMeSendedDataForLog(organisation));
        } finally {
            organisation = port.create(organisation);
        }

        ideDataBean = formatdata(organisation);

        return ideDataBean;

    }

    public static final IDEDataBean reactivateEntiteIde(BSession session, IDEDataBean ideDataBean, IPartnerServices port)
            throws IPartnerServicesReactivateBusinessFaultFaultFaultMessage,
            IPartnerServicesReactivateInfrastructureFaultFaultFaultMessage,
            IPartnerServicesReactivateSecurityFaultFaultFaultMessage {

        OrganisationType organisation = null;
        UidStructureType uidStruct = null;
        uidStruct = IDEServiceMappingUtil.getStructureForReactivateEntiteIde(ideDataBean);

        try {
            logCallWebService(session, "reactivateEntiteIde", "reactivate", giveMeSendedDataForLog(uidStruct));
        } finally {
            organisation = port.reactivate(uidStruct);
        }

        ideDataBean = formatdata(organisation);

        return ideDataBean;

    }

    public static final IDEDataBean updateEntiteIde(BSession session, IDEDataBean ideDataBean, IPartnerServices port)
            throws IPartnerServicesUpdateBusinessFaultFaultFaultMessage,
            IPartnerServicesUpdateInfrastructureFaultFaultFaultMessage,
            IPartnerServicesUpdateSecurityFaultFaultFaultMessage {

        OrganisationType organisation = null;
        organisation = IDEServiceMappingUtil.getStructureForUpdateEntiteIde(ideDataBean);

        try {
            logCallWebService(session, "updateEntiteIde", "update", giveMeSendedDataForLog(organisation));
        } finally {
            organisation = port.update(organisation);
        }

        ideDataBean = formatdata(organisation);

        return ideDataBean;

    }

    public static final IDEDataBean radiationAnticipeEntiteIde(BSession session, XMLGregorianCalendar scheduledDate,
            IDEDataBean ideDataBean, IPartnerServices port)
            throws IPartnerServicesDeleteBusinessFaultFaultFaultMessage,
            IPartnerServicesDeleteInfrastructureFaultFaultFaultMessage,
            IPartnerServicesDeleteSecurityFaultFaultFaultMessage,
            IPartnerServicesDeleteScheduledBusinessFaultFaultFaultMessage,
            IPartnerServicesDeleteScheduledInfrastructureFaultFaultFaultMessage,
            IPartnerServicesDeleteScheduledSecurityFaultFaultFaultMessage {

        OrganisationType organisation = null;
        organisation = IDEServiceMappingUtil.getStructureForRadiationEntiteIde(ideDataBean);

        try {

            logCallWebService(session, "radiationAnticipeEntiteIde", "deleteScheduled",
                    giveMeSendedDataForLog(organisation, scheduledDate));
        } finally {
            port.deleteScheduled(organisation, scheduledDate);
        }

        // ideDataBean = formatdata(organisation);

        return ideDataBean;

    }

    public static final IDEDataBean radiationEntiteIde(BSession session, IDEDataBean ideDataBean, IPartnerServices port)
            throws IPartnerServicesDeleteBusinessFaultFaultFaultMessage,
            IPartnerServicesDeleteInfrastructureFaultFaultFaultMessage,
            IPartnerServicesDeleteSecurityFaultFaultFaultMessage {

        OrganisationType organisation = null;
        organisation = IDEServiceMappingUtil.getStructureForRadiationEntiteIde(ideDataBean);

        try {
            logCallWebService(session, "radiationEntiteIde", "delete", giveMeSendedDataForLog(organisation));
        } finally {
            organisation = port.delete(organisation);
        }
        ideDataBean = formatdata(organisation);

        return ideDataBean;

    }

    public static final Map<String, RegisterDeregisterStatus> desenregistrementActif(BSession session,
            ArrayOfUidStructureType listIDE, IPartnerServices port)
            throws IPartnerServicesDeregisterBusinessFaultFaultFaultMessage,
            IPartnerServicesDeregisterInfrastructureFaultFaultFaultMessage,
            IPartnerServicesDeregisterSecurityFaultFaultFaultMessage {

        Map<String, RegisterDeregisterStatus> mapRetourWebService = new HashMap<String, RegisterDeregisterStatus>();

        int startIndex = 0;
        int stopIndex = listIDE.getUidStructureType().size();
        int lastIndex = listIDE.getUidStructureType().size();

        if (stopIndex > MAX_LOT_ANNONCE_SORTANTE_MASSE_SIZE) {
            stopIndex = MAX_LOT_ANNONCE_SORTANTE_MASSE_SIZE;
        }

        while (startIndex < stopIndex) {

            ArrayOfUidStructureType subListIDE = new ArrayOfUidStructureType();
            subListIDE.getUidStructureType().addAll(listIDE.getUidStructureType().subList(startIndex, stopIndex));

            RegisterDeregisterResult registerDeregisterResult;
            try {
                logCallWebService(session, "desenregistrementActif", "deregister", giveMeSendedDataForLog(subListIDE));
            } finally {
                registerDeregisterResult = port.deregister(subListIDE);
            }

            mapRetourWebService.putAll(formatdata(registerDeregisterResult));

            startIndex = stopIndex;
            stopIndex = stopIndex + MAX_LOT_ANNONCE_SORTANTE_MASSE_SIZE;

            if (stopIndex > lastIndex) {
                stopIndex = lastIndex;
            }

        }

        return mapRetourWebService;

    }

    public static final Map<String, RegisterDeregisterStatus> enregistrementActif(BSession session,
            ArrayOfUidStructureType listIDE, IPartnerServices port)
            throws IPartnerServicesRegisterBusinessFaultFaultFaultMessage,
            IPartnerServicesRegisterInfrastructureFaultFaultFaultMessage,
            IPartnerServicesRegisterSecurityFaultFaultFaultMessage {

        Map<String, RegisterDeregisterStatus> mapRetourWebService = new HashMap<String, RegisterDeregisterStatus>();

        int startIndex = 0;
        int stopIndex = listIDE.getUidStructureType().size();
        int lastIndex = listIDE.getUidStructureType().size();

        if (stopIndex > MAX_LOT_ANNONCE_SORTANTE_MASSE_SIZE) {
            stopIndex = MAX_LOT_ANNONCE_SORTANTE_MASSE_SIZE;
        }

        while (startIndex < stopIndex) {

            ArrayOfUidStructureType subListIDE = new ArrayOfUidStructureType();
            subListIDE.getUidStructureType().addAll(listIDE.getUidStructureType().subList(startIndex, stopIndex));

            RegisterDeregisterResult registerDeregisterResult;
            try {
                logCallWebService(session, "enregistrementActif", "register", giveMeSendedDataForLog(subListIDE));
            } finally {
                registerDeregisterResult = port.register(subListIDE);
            }

            mapRetourWebService.putAll(formatdata(registerDeregisterResult));

            startIndex = stopIndex;
            stopIndex = stopIndex + MAX_LOT_ANNONCE_SORTANTE_MASSE_SIZE;

            if (stopIndex > lastIndex) {
                stopIndex = lastIndex;
            }

        }

        return mapRetourWebService;

    }

    public static final void desenregistrementActifAnticipe(BSession session,
            HashMap<XMLGregorianCalendar, ArrayOfUidStructureType> mapDesenregistrementActifAnticipe,
            IPartnerServices port) throws IPartnerServicesDeregisterScheduledBusinessFaultFaultFaultMessage,
            IPartnerServicesDeregisterScheduledInfrastructureFaultFaultFaultMessage,
            IPartnerServicesDeregisterScheduledSecurityFaultFaultFaultMessage {
        // Envoyer une liste annonce IDE pour chaque changement de date
        for (XMLGregorianCalendar keyAnnonce : mapDesenregistrementActifAnticipe.keySet()) {
            ArrayOfUidStructureType listAnnonce = mapDesenregistrementActifAnticipe.get(keyAnnonce);

            try {
                logCallWebService(session, "desenregistrementActifAnticipe", "deregisterScheduled",
                        giveMeSendedDataForLog(listAnnonce, keyAnnonce));
            } finally {
                port.deregisterScheduled(listAnnonce, keyAnnonce);
            }
        }
    }

    public static final void enregistrementActifAnticipe(BSession session,
            HashMap<XMLGregorianCalendar, ArrayOfUidStructureType> mapEnregistrementActifAnticipe, IPartnerServices port)
            throws IPartnerServicesRegisterScheduledBusinessFaultFaultFaultMessage,
            IPartnerServicesRegisterScheduledInfrastructureFaultFaultFaultMessage,
            IPartnerServicesRegisterScheduledSecurityFaultFaultFaultMessage {
        // Envoyer une liste annonce IDE pour chaque changement de date
        for (XMLGregorianCalendar keyAnnonce : mapEnregistrementActifAnticipe.keySet()) {
            ArrayOfUidStructureType listAnnonce = mapEnregistrementActifAnticipe.get(keyAnnonce);

            try {
                logCallWebService(session, "enregistrementActifAnticipe", "registerScheduled",
                        giveMeSendedDataForLog(listAnnonce, keyAnnonce));
            } finally {
                port.registerScheduled(listAnnonce, keyAnnonce);
            }
        }
    }

    public static final Map<String, RegisterDeregisterStatus> desenregistrementPassif(BSession session,
            ArrayOfUidStructureType listIDE, IPartnerServices port)
            throws IPartnerServicesUnsubscribeBusinessFaultFaultFaultMessage,
            IPartnerServicesUnsubscribeInfrastructureFaultFaultFaultMessage,
            IPartnerServicesUnsubscribeSecurityFaultFaultFaultMessage {

        Map<String, RegisterDeregisterStatus> mapRetourWebService = new HashMap<String, RegisterDeregisterStatus>();

        int startIndex = 0;
        int stopIndex = listIDE.getUidStructureType().size();
        int lastIndex = listIDE.getUidStructureType().size();

        if (stopIndex > MAX_LOT_ANNONCE_SORTANTE_MASSE_SIZE) {
            stopIndex = MAX_LOT_ANNONCE_SORTANTE_MASSE_SIZE;
        }

        while (startIndex < stopIndex) {

            ArrayOfUidStructureType subListIDE = new ArrayOfUidStructureType();
            subListIDE.getUidStructureType().addAll(listIDE.getUidStructureType().subList(startIndex, stopIndex));

            RegisterDeregisterResult registerDeregisterResult;
            try {
                logCallWebService(session, "desenregistrementPassif", "unsubscribe", giveMeSendedDataForLog(subListIDE));
            } finally {
                registerDeregisterResult = port.unsubscribe(subListIDE);
            }

            mapRetourWebService.putAll(formatdata(registerDeregisterResult));

            startIndex = stopIndex;
            stopIndex = stopIndex + MAX_LOT_ANNONCE_SORTANTE_MASSE_SIZE;

            if (stopIndex > lastIndex) {
                stopIndex = lastIndex;
            }

        }

        return mapRetourWebService;

    }

    public static final Map<String, RegisterDeregisterStatus> enregistrementPassif(BSession session,
            ArrayOfUidStructureType listIDE, IPartnerServices port)
            throws IPartnerServicesSubscribeBusinessFaultFaultFaultMessage,
            IPartnerServicesSubscribeInfrastructureFaultFaultFaultMessage,
            IPartnerServicesSubscribeSecurityFaultFaultFaultMessage {

        Map<String, RegisterDeregisterStatus> mapRetourWebService = new HashMap<String, RegisterDeregisterStatus>();

        int startIndex = 0;
        int stopIndex = listIDE.getUidStructureType().size();
        int lastIndex = listIDE.getUidStructureType().size();

        if (stopIndex > MAX_LOT_ANNONCE_SORTANTE_MASSE_SIZE) {
            stopIndex = MAX_LOT_ANNONCE_SORTANTE_MASSE_SIZE;
        }

        while (startIndex < stopIndex) {

            ArrayOfUidStructureType subListIDE = new ArrayOfUidStructureType();
            subListIDE.getUidStructureType().addAll(listIDE.getUidStructureType().subList(startIndex, stopIndex));

            RegisterDeregisterResult registerDeregisterResult;
            try {
                logCallWebService(session, "enregistrementPassif", "subscribe", giveMeSendedDataForLog(subListIDE));
            } finally {
                registerDeregisterResult = port.subscribe(subListIDE);
            }

            mapRetourWebService.putAll(formatdata(registerDeregisterResult));

            startIndex = stopIndex;
            stopIndex = stopIndex + MAX_LOT_ANNONCE_SORTANTE_MASSE_SIZE;

            if (stopIndex > lastIndex) {
                stopIndex = lastIndex;
            }

        }

        return mapRetourWebService;
    }

    public static final UidStructureType getStructureForEnregistrement(IDEDataBean ideDataBean) {

        UidStructureType uidStruct = new UidStructureType();
        NonNegativeInteger uid = new NonNegativeInteger(AFIDEUtil.giveMeNumIdeUnformatedWithoutPrefix(ideDataBean
                .getNumeroIDE()));

        uidStruct.setUidOrganisationIdCategorie(UidOrganisationIdCategorieType.CHE);
        uidStruct.setUidOrganisationId(uid);
        return uidStruct;

    }

    public static final UidStructureType getStructureForDesenregistrement(IDEDataBean ideDataBean) {
        UidStructureType uidStruct;
        if (!JadeStringUtil.isBlankOrZero(ideDataBean.getNumeroIDE())) {
            uidStruct = getStructureForEnregistrement(ideDataBean);
        } else {
            uidStruct = new UidStructureType();
            uidStruct.setUidOrganisationIdCategorie(UidOrganisationIdCategorieType.CHE);
            NonNegativeInteger uid = new NonNegativeInteger(AFIDEUtil.giveMeNumIdeUnformatedWithoutPrefix(ideDataBean
                    .getNumeroIDERemplacement()));
            uidStruct.setUidOrganisationId(uid);
        }
        return uidStruct;
    }

    private static final String giveMeSendedDataForLog(ArrayOfUidStructureType listIDE) {

        StringBuffer sendedData = new StringBuffer("");
        sendedData.append("List IDE Number : ");

        try {
            for (UidStructureType uidStructureType : listIDE.getUidStructureType()) {
                sendedData.append(IDEServiceMappingUtil.getNumeroIDE(uidStructureType) + " ");

            }
        } catch (Exception e) {
            // Nothing todo
        }

        return sendedData.toString();

    }

    private static final String giveMeSendedDataForLog(UidStructureType uidStruct) {
        String numeroIDE = "";
        try {
            numeroIDE = IDEServiceMappingUtil.getNumeroIDE(uidStruct);

        } catch (Exception e) {
            numeroIDE = "";
        }

        return "IDE Number : " + numeroIDE;

    }

    private static final String giveMeSendedDataForLog(ArrayOfUidStructureType listIDE,
            XMLGregorianCalendar scheduledDate) {

        StringBuffer sendedData = new StringBuffer("");
        sendedData.append("List IDE Number : ");

        try {
            for (UidStructureType uidStructureType : listIDE.getUidStructureType()) {
                sendedData.append(IDEServiceMappingUtil.getNumeroIDE(uidStructureType) + " ");

            }
        } catch (Exception e) {
            // Nothing todo
        }

        String scheduledDateString = "";
        try {
            scheduledDateString = IDEServiceMappingUtil.getDateJJMMYYYY(scheduledDate);
        } catch (Exception e) {
            scheduledDateString = "";
        }

        sendedData.append(" / Scheduled Date : " + scheduledDateString);

        return sendedData.toString();

    }

    private static final String giveMeSendedDataForLog(OrganisationType organisation, XMLGregorianCalendar scheduledDate) {

        String scheduledDateString = "";
        try {
            scheduledDateString = IDEServiceMappingUtil.getDateJJMMYYYY(scheduledDate);
        } catch (Exception e) {
            scheduledDateString = "";
        }

        StringBuffer sendedData = new StringBuffer();

        sendedData.append(giveMeSendedDataForLog(organisation) + " / ");
        sendedData.append("Scheduled Date : " + scheduledDateString);

        return sendedData.toString();

    }

    private static final String giveMeSendedDataForLog(OrganisationType organisation) {

        String numeroIDE = "";
        try {
            numeroIDE = IDEServiceMappingUtil.getNumeroIDE(organisation);

        } catch (Exception e) {
            numeroIDE = "";
        }

        String statut = "";
        try {
            statut = IDEServiceMappingUtil.getStatut(organisation);

        } catch (Exception e) {
            statut = "";
        }

        String raisonSociale = "";
        try {
            raisonSociale = IDEServiceMappingUtil.getRaisonSociale(organisation);

        } catch (Exception e) {
            raisonSociale = "";
        }

        String adresse = "";
        try {
            adresse = IDEServiceMappingUtil.getAdresse(organisation);

        } catch (Exception e) {
            adresse = "";
        }

        String canton = "";
        try {
            canton = IDEServiceMappingUtil.getCanton(organisation);

        } catch (Exception e) {
            canton = "";
        }

        String npa = "";
        try {
            npa = IDEServiceMappingUtil.getNPA(organisation);

        } catch (Exception e) {
            npa = "";
        }

        String localite = "";
        try {
            localite = IDEServiceMappingUtil.getLocalite(organisation);

        } catch (Exception e) {
            localite = "";
        }

        String rue = "";
        try {
            rue = IDEServiceMappingUtil.getRue(organisation);

        } catch (Exception e) {
            rue = "";
        }

        String aLAttention = "";
        try {
            aLAttention = IDEServiceMappingUtil.getCareOf(organisation);

        } catch (Exception e) {
            aLAttention = "";
        }
        String legalForm = "";
        try {
            legalForm = IDEServiceMappingUtil.getLegalForm(organisation);

        } catch (Exception e) {
            legalForm = "";
        }

        StringBuffer sendedData = new StringBuffer();

        sendedData.append("IDE Number : " + numeroIDE + " / ");
        sendedData.append("IDE Status : " + statut + " / ");
        sendedData.append("Company name : " + raisonSociale + " / ");
        sendedData.append("Address: " + adresse + " / ");
        sendedData.append("Canton : " + canton + " / ");
        sendedData.append("NPA : " + npa + " / ");
        sendedData.append("Town : " + localite + " / ");
        sendedData.append("Street : " + rue + " / ");
        sendedData.append("Care Of : " + aLAttention + " / ");
        sendedData.append("LegalForm : " + legalForm);
        return sendedData.toString();

    }

    public static final Map<String, RegisterDeregisterStatus> formatdata(
            RegisterDeregisterResult registerDeregisterResult) {

        Map<String, RegisterDeregisterStatus> mapResult = new HashMap<String, RegisterDeregisterStatus>();

        for (RegisterDeregisterItem registerDeregisterItem : registerDeregisterResult.getItems()) {
            String numIde = IDEServiceMappingUtil.getNumeroIDE(registerDeregisterItem);
            RegisterDeregisterStatus status = IDEServiceMappingUtil.getStatut(registerDeregisterItem);
            mapResult.put(numIde, status);
        }

        return mapResult;

    }

    public static final IDEDataBean formatdata(OrganisationType organisation) {
        IDEDataBean ideDataBean = new IDEDataBean();

        ideDataBean.setNumeroIDE(IDEServiceMappingUtil.getNumeroIDE(organisation));
        ideDataBean.setNumeroIDERemplacement(IDEServiceMappingUtil.getNumeroIDERemplacement(organisation));
        ideDataBean.setStatut(IDEServiceMappingUtil.getStatut(organisation));
        ideDataBean.setRaisonSociale(IDEServiceMappingUtil.getRaisonSociale(organisation));
        ideDataBean.setAdresse(IDEServiceMappingUtil.getAdresse(organisation));
        ideDataBean.setCanton(IDEServiceMappingUtil.getCanton(organisation));
        ideDataBean.setNpa(IDEServiceMappingUtil.getNPA(organisation));
        ideDataBean.setLocalite(IDEServiceMappingUtil.getLocalite(organisation));
        ideDataBean.setRue(IDEServiceMappingUtil.getRue(organisation));
        ideDataBean.setNumeroRue(IDEServiceMappingUtil.getNumeroRue(organisation));
        ideDataBean.setCareOf(IDEServiceMappingUtil.getCareOf(organisation));
        ideDataBean.setPersonnaliteJuridique(IDEServiceMappingUtil.getLegalForm(organisation));
        ideDataBean.setBrancheEconomique(IDEServiceMappingUtil.getOrganisationType(organisation));
        ideDataBean.setLangue(IDEServiceMappingUtil.getLangue(organisation));

        return ideDataBean;
    }

    public static final List<IDEDataBean> searchForNumeroIDE(String numeroIDE, BSession session)
            throws MalformedURLException, Exception, RemoteException {

        String numIdeUnformatedWithoutPrefix = AFIDEUtil.giveMeNumIdeUnformatedWithoutPrefix(numeroIDE);

        if (!AFIDEUtil.isUnformatedNumIdeWithoutPrefixValid(numIdeUnformatedWithoutPrefix)) {
            throw new Exception(session.getLabel("NAOS_RECHERCHE_IDE_WS_ERREUR_NUMERO_IDE_INVALIDE"));
        }

        List<IDEDataBean> listEntiteIde = new ArrayList<IDEDataBean>();

        OrganisationType organisationType = IDEServiceMappingUtil
                .getStructureForSearchByNumeroIDE(numIdeUnformatedWithoutPrefix);

        ArrayOfRatedOrganisation arrayOrganisation;
        try {
            IPartnerServices port = initService();
            arrayOrganisation = port.search(organisationType, SERVICE_MAX_RECORD_RETURN, SearchMode.AUTO);
        } catch (Exception e) {

            String messageForUser = AFIDEUtil.logExceptionAndCreateMessageForUser(session, e);
            throw new Exception(messageForUser);

        } finally {
            logCallWebService(session, "searchForNumeroIDE", "search", giveMeSendedDataForLog(organisationType));
        }

        listEntiteIde = formatdata(arrayOrganisation);

        return listEntiteIde;
    }

    private static List<IDEDataBean> formatdata(ArrayOfRatedOrganisation tabOrganisation) {
        List<IDEDataBean> listIdeData = new ArrayList<IDEDataBean>();

        for (int i = 0; i < tabOrganisation.getRatedOrganisation().size(); i++) {
            OrganisationType aOrganisation = tabOrganisation.getRatedOrganisation().get(i).getOrganisation();
            listIdeData.add(formatdata(aOrganisation));
        }

        return listIdeData;
    }

    public static final List<IDEDataBean> search(String forRaisonSociale, String forNpa, String forLocalite,
            String forRue, String forNumeroRue, BSession session) throws Exception, RemoteException {

        boolean isInputValid = !JadeStringUtil.isBlankOrZero(forRaisonSociale);
        isInputValid = isInputValid || !JadeStringUtil.isBlankOrZero(forNpa);
        isInputValid = isInputValid || !JadeStringUtil.isBlankOrZero(forLocalite);
        isInputValid = isInputValid || !JadeStringUtil.isBlankOrZero(forRue);
        isInputValid = isInputValid || !JadeStringUtil.isBlankOrZero(forNumeroRue);

        if (!isInputValid) {
            throw new Exception(session.getLabel("NAOS_RECHERCHE_IDE_WS_ERREUR_RECHERCHE_SANS_NUM_IDE_MANDATORY"));
        }

        if (!JadeStringUtil.isBlankOrZero(forRaisonSociale) && forRaisonSociale.length() < 4
                && forRaisonSociale.length() > 1 && (forRaisonSociale.endsWith("%") || forRaisonSociale.endsWith("*"))) {
            throw new Exception(session.getLabel("NAOS_RECHERCHE_IDE_WS_ERREUR_RECHERCHE_GENERIQUE_MOINS_3_CARACTERE"));
        }

        ArrayOfRatedOrganisation arrayOrganisation = null;
        OrganisationType organisationType = IDEServiceMappingUtil.getStructureForSearch(forRaisonSociale, forNpa,
                forLocalite, forRue, forNumeroRue);
        try {
            IPartnerServices port = initService();

            arrayOrganisation = port.search(organisationType, SERVICE_MAX_RECORD_RETURN, SearchMode.AUTO);

        } catch (Exception e) {

            String messageForUser = AFIDEUtil.logExceptionAndCreateMessageForUser(session, e);
            throw new Exception(messageForUser);
        } finally {
            logCallWebService(session, "search", "search", giveMeSendedDataForLog(organisationType));
        }

        return formatdata(arrayOrganisation);
    }

    public static XMLGregorianCalendar convertDateAMJtoXMLDateGregorian(BSession session, BTransaction transaction,
            String date, String numAffilie) {
        XMLGregorianCalendar xmlCalendar = null;
        GregorianCalendar gCalendar = new GregorianCalendar();
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        try {
            gCalendar.setTime(format.parse(date));
            DatatypeFactory dataTypeFac = DatatypeFactory.newInstance();
            xmlCalendar = dataTypeFac.newXMLGregorianCalendar(gCalendar);
        } catch (Exception e) {
            if (!JadeStringUtil.isEmpty(numAffilie)) {
                // Erreur lors du traitement de l'ann�e
                transaction
                        .addErrors(session.getLabel("NAOS_PROCESS_IDE_TRAITEMENT_ANNONCE_CONVERT_DATE_FINAFFILIATION")
                                + " " + numAffilie);
            } else {
                // Erreur lors du traitement de l'ann�e
                transaction.addErrors(session
                        .getLabel("NAOS_PROCESS_IDE_TRAITEMENT_ANNONCE_CONVERT_DATE_FINAFFILIATION"));
            }
            return null;
        }
        return xmlCalendar;
    }

}