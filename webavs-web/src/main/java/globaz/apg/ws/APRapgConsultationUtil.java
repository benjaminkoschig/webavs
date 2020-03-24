package globaz.apg.ws;

import ch.admin.cdc.rapg.core.dto.generated._1.RapgAnnoncesRequestType;
import ch.admin.cdc.rapg.core.dto.generated._1.RapgAnnoncesResponseType;
import ch.admin.cdc.rapg.core.service.ws._1.RapgConsultation1;
import ch.admin.cdc.rapg.core.service.ws._1.RapgConsultationService1;
import ch.admin.ofit.commun.ws._2.ErreurMessageType;
import ch.eahv.rapg.common._4.DeliveryOfficeType;
import ch.eahv.rapg.eahv000601._4.RegisterStatusRecordType;
import ch.globaz.common.properties.CommonProperties;
import ch.globaz.common.properties.CommonPropertiesUtils;
import ch.globaz.common.properties.PropertiesException;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.crypto.JadeDefaultEncrypters;
import globaz.phenix.util.WIRRDataBean;
import globaz.phenix.util.WIRRServiceCallUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import globaz.globall.db.BSession;
import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wirrch.admin.ws.zas.regcent.nrr._0.NRRQueryServicePortType;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class APRapgConsultationUtil {
    private static final String SSL_SOCKET_FACTORY_ORACLE_JDK = "com.sun.xml.ws.transport.https.client.SSLSocketFactory";
    private static final String SSL_SOCKET_FACTORY_JAX_WS_RI = "com.sun.xml.internal.ws.transport.https.client.SSLSocketFactory";
    private static final Logger logger = LoggerFactory.getLogger(APRapgConsultationUtil.class);

    public static void findAnnonces(BSession session, String nss, String numCaisse, String numBranche) throws PropertiesException {
        String urlRAPGWS = CommonPropertiesUtils.getValue(CommonProperties.RAPG_ENDPOINT_ADDRESS);
        String certFileName = CommonPropertiesUtils.getValue(CommonProperties.RAPG_KEYSTORE_PATH);
        String certPassword = CommonPropertiesUtils.getValue(CommonProperties.RAPG_KEYSTORE_PASSWORD);
        String certType = CommonPropertiesUtils.getValue(CommonProperties.RAPG_KEYSTORE_TYPE);


        boolean secure = false;


        //1) Create the service
        RapgConsultation1 rapgConsultation;
        try {
            rapgConsultation = getRapgConsultation(session,urlRAPGWS, certFileName, certPassword,certType);
        } catch (final Exception e) {
            e.printStackTrace();
            return;
        }
        System.out.println("Create request... "); //$NON-NLS-1$
        // 2) Create the request StandardConsultationInputType
        final RapgAnnoncesRequestType request = createRequest(Long.parseLong(nss), Integer.parseInt(numCaisse), Integer.parseInt(numBranche));

        System.out.println("Calling RapgConsultationSSLSession.findAnnonces ... "); //$NON-NLS-1$
        //3) Call webservice
        final RapgAnnoncesResponseType response = rapgConsultation.findAnnonces(request);

        //4)Check the return ack
        System.out.println("Call successful, result : " + response.getAck().getValue()); //$NON-NLS-1$
        switch (response.getAck().getValue()) {
            //All those statuses have error message joined (normaly)
            case FAILURE: //Something goes wrong and the webservice can not execute corectly. Check the error to know in witch side is the problem.
            case PARTIALFAILURE: //In case of Patialfailure you maybe have a correct business response, it's depend on the error. Check it.
            case WARNING:
                //5) Use the Errors
                if (response.getErrors() != null) {
                    for (final ErreurMessageType error : response.getErrors()) {
                        // Make what you need

                        // It's an exemple to get error
                        System.out.println("Error message : " + error.getMessage()); //$NON-NLS-1$
                    }
                }
                break;
            //Everything goes well.
            case SUCCESS:
                //6)  Use the RegisterStatus answer
                System.out.println("Number of Records : " + response.getMessage().getContent().getRegisterStatusRecords().size()); //$NON-NLS-1$
                for (final RegisterStatusRecordType registerStatus : response.getMessage().getContent().getRegisterStatusRecords()) {
                    //TODO: Vérifier les doublons

                }
        }
        System.out.println("End of test.");

    }

    public static boolean checkDuplicate(String nss, String deliveryOffice,String officeIdentifier, String branch,BSession session){
        boolean isCheck = false;



        return isCheck;
    }
    /**
     * Create the request
     * @param nas AHV-Versichertennummer/Numero AVS (13 position)
     * @param cafCode office identifier max 3 positions
     * @param agencyCode branch office branch max 3 positions
     * @return the request
     */
    private static RapgAnnoncesRequestType createRequest(final Long nas, final int cafCode, final int agencyCode) {
        final RapgAnnoncesRequestType request = new RapgAnnoncesRequestType();
        request.setVn(nas.longValue());
        final DeliveryOfficeType caisse = new DeliveryOfficeType();
        caisse.setOfficeIdentifier(cafCode);
        caisse.setBranch(agencyCode);
        request.setDeliveryOffice(caisse);
        return request;
    }
    private static RapgConsultation1 getRapgConsultation(BSession session,final String urlRAPGWS, final String certFileName, final String certPassword, String certType)
            throws IOException, PropertiesException {
        // Instantiate the service object generated from wsdl.
        String pathWsdl = CommonPropertiesUtils.getValue(CommonProperties.RAPG_SEODOR_WSDL_PATH);
        String nameSpace = CommonPropertiesUtils.getValue(CommonProperties.RAPG_WEBSERVICE_NAMESPACE);
        String name = CommonPropertiesUtils.getValue(CommonProperties.RAPG_WEBSERVICE_NAME);
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        URL wsdlLocation = classloader.getResource(pathWsdl);
        QName qName = new QName(nameSpace,name);
        final RapgConsultationService1 rapgConsultationService = new RapgConsultationService1(wsdlLocation,qName);
        //Getting the port. Entry point for the Webservice.

        final RapgConsultation1 port = rapgConsultationService.getRapgConsultationPort1();

        // Set endpoint address (URL) of the webservice.
        final Map<String, Object> ctxt = ((BindingProvider) port).getRequestContext();
        ctxt.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, urlRAPGWS);

        configureSSLOnTheClient(port, certFileName, certPassword,certType);
        return port;
    }


    /**
     * Configure SSL On the client
     * @param proxy webservice proxy
     * @param certFileName certFileName certificat filename with full path
     * @param certPassword certificat password
     */
    private static void configureSSLOnTheClient(final RapgConsultation1 proxy, final String certFileName, final String certPassword, String certType) {
        // Get httpConduit
        final Client client = ClientProxy.getClient(proxy);
        final HTTPConduit httpConduit = (HTTPConduit) client.getConduit();
        KeyStore ks = null;

        try {
            final TLSClientParameters tlsParams = new TLSClientParameters();
            tlsParams.setDisableCNCheck(true);

            // Read the certificate
            FileInputStream filePkcs12;
            ks = KeyStore.getInstance(certType);
            filePkcs12 = new FileInputStream(certFileName);

            // For a better security you can encode your password and decode it here
            ks.load(filePkcs12, certPassword.toCharArray());

            try {
                filePkcs12.close();
            } catch (final IOException e) {
                System.err.println("Error on close " + certFileName + " file");
            }

            // Add certificate to the conduit
            final KeyManagerFactory keyFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyFactory.init(ks, certPassword.toCharArray());
            final KeyManager[] km = keyFactory.getKeyManagers();
            tlsParams.setKeyManagers(km);

            httpConduit.setTlsClientParameters(tlsParams);

        } catch (final FileNotFoundException e) {
            System.err.println("File " + certFileName + " doesn't exist");
        } catch (final IOException ioe) {
            System.err.println("File " + certFileName + " doesn't exist. Cause by " + ioe.getCause());
        } catch (final KeyStoreException kse) {
            System.out.println("Security configuration failed with the following: " + kse.getCause());
        } catch (final NoSuchAlgorithmException nsa) {
            System.out.println("Security configuration failed with the following: " + nsa.getCause());
        } catch (final GeneralSecurityException gse) {
            System.out.println("Security configuration failed with the following: " + gse.getCause());
        }
    }

}
