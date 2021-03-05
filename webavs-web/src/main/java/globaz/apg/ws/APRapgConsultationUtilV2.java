package globaz.apg.ws;

import ch.admin.zas.rapg.ws.consultation._2.RapgConsultation20;
import ch.admin.zas.rapg.ws.consultation._2.RapgConsultationService20;
import ch.globaz.common.properties.CommonProperties;
import ch.globaz.common.properties.CommonPropertiesUtils;
import ch.globaz.common.properties.PropertiesException;
import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.exceptions.APWebserviceException;
import globaz.globall.db.BSession;
import globaz.jade.crypto.JadeDefaultEncrypters;
import globaz.jade.log.JadeLogger;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rapg.v2.ErreurMessageType;
import rapg.v2.ch.eahv_iv.xmlns.eahv_iv_2015_000601._5.RegisterStatusRecordType;
import rapg.v2.ch.eahv_iv.xmlns.eahv_iv_2015_common._5.DeliveryOfficeType;
import rapg.v2.ws.RapgAnnoncesRequestType;
import rapg.v2.ws.RapgAnnoncesResponseType;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class APRapgConsultationUtilV2 {

    private static final String SSL_SOCKET_FACTORY_ORACLE_JDK = "com.sun.xml.ws.transport.https.client.SSLSocketFactory";
    private static final String SSL_SOCKET_FACTORY_JAX_WS_RI = "com.sun.xml.internal.ws.transport.https.client.SSLSocketFactory";
    private static final String SSL_SOCKET_FACTORY_OPEN_JDK = "javax.net.ssl.SSLSocketFactory";
    private static final String SSL_SOCKET_FACTORY_IBM_JDK = "com.ibm.websphere.ssl.protocol.SSLSocketFactory";
    private static final Logger logger = LoggerFactory.getLogger(APRapgConsultationUtil.class);

    public static List<RegisterStatusRecordType> findAnnonces(BSession session, String nss, String numCaisse, String numBranche) throws PropertiesException, APWebserviceException {
        String urlRAPGWS = CommonPropertiesUtils.getValue(CommonProperties.RAPG_ENDPOINT_ADDRESS);
        String certFileName = CommonPropertiesUtils.getValue(CommonProperties.RAPG_KEYSTORE_PATH);
        String certPassword = CommonPropertiesUtils.getValue(CommonProperties.RAPG_KEYSTORE_PASSWORD);
        String certType = CommonPropertiesUtils.getValue(CommonProperties.RAPG_KEYSTORE_TYPE);

        //1) Create the service
        RapgConsultation20 rapgConsultation;
        try {
            rapgConsultation = getRapgConsultation(session, urlRAPGWS, certFileName, certPassword, certType);
            JadeLogger.info(APRapgConsultationUtil.class, "Create request... ");
            // 2) Create the request StandardConsultationInputType
            final RapgAnnoncesRequestType request = createRequest(Long.parseLong(nss), Integer.parseInt(numCaisse), Integer.parseInt(numBranche));

            JadeLogger.info(APRapgConsultationUtil.class, "Calling RapgConsultationSSLSession.findAnnonces ... ");
            //3) Call webservice
            final RapgAnnoncesResponseType response = rapgConsultation.findAnnonces(request);

            //4)Check the return ack
            JadeLogger.info(APRapgConsultationUtil.class, "Call successful, result : " + response.getAck().getValue());
            switch (response.getAck().getValue()) {
                //All those statuses have error message joined (normaly)
                case FAILURE: //Something goes wrong and the webservice can not execute corectly. Check the error to know in witch side is the problem.
                case PARTIALFAILURE: //In case of Patialfailure you maybe have a correct business response, it's depend on the error. Check it.
                    StringBuilder str = new StringBuilder();
                    if (response.getErrors() != null) {
                        for (final ErreurMessageType error : response.getErrors()) {
                            str.append(error.getMessage());
                            str.append(" ");
                        }
                        throw new APRuleExecutionException(str.toString());
                    }
                    break;
                case WARNING:
                    StringBuilder builder = new StringBuilder();
                    if (Objects.nonNull(response.getErrors())) {
                        for (final ErreurMessageType error : response.getErrors()) {
                            builder.append(error.getMessage());
                            builder.append(" ");

                        }
                        logger.warn(builder.toString());
                    }
                    if (response.getMessage() != null) {
                        return response.getMessage().getContent().getRegisterStatusRecords();
                    } else {
                        return null;
                    }
                case SUCCESS:
                    //6)  Use the RegisterStatus answer
                    JadeLogger.info(APRapgConsultationUtil.class, "Number of Records : " + response.getMessage().getContent().getRegisterStatusRecords().size()); //$NON-NLS-1$
                    return response.getMessage().getContent().getRegisterStatusRecords();
                default:
                    break;
            }
        } catch (final Exception e) {
            throw new APWebserviceException(e);
        }
        return null;
    }


    /**
     * Create the request
     *
     * @param nas        AHV-Versichertennummer/Numero AVS (13 position)
     * @param cafCode    office identifier max 3 positions
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

    private static RapgConsultation20 getRapgConsultation(BSession session, final String urlRAPGWS, final String certFileName, final String certPassword, String certType)
            throws Exception {
        // Instantiate the service object generated from wsdl.
        String pathWsdl = CommonPropertiesUtils.getValue(CommonProperties.RAPG_WEBSERVICE_WSDL_PATH);
        String nameSpace = CommonPropertiesUtils.getValue(CommonProperties.RAPG_WEBSERVICE_NAMESPACE);
        String name = CommonPropertiesUtils.getValue(CommonProperties.RAPG_WEBSERVICE_NAME);
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        URL wsdlLocation = classloader.getResource(pathWsdl);
        QName qName = new QName(nameSpace, name);
        final RapgConsultationService20 rapgConsultationService = new RapgConsultationService20(wsdlLocation, qName);
        //Getting the port. Entry point for the Webservice.

        final RapgConsultation20 port = rapgConsultationService.getRapgConsultationPort20();

        // Set endpoint address (URL) of the webservice.
        final Map<String, Object> ctxt = ((BindingProvider) port).getRequestContext();
        if (StringUtils.isNotEmpty(urlRAPGWS)) {
            ctxt.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, urlRAPGWS);
        }

        String certPasswordDecrypt = JadeDefaultEncrypters.getJadeDefaultEncrypter().decrypt(
                certPassword);

        configureSSLOnTheClient(port, certFileName, certPasswordDecrypt, certType);
        return port;
    }


    /**
     * Configure SSL On the client
     *
     * @param proxy        webservice proxy
     * @param certFileName certFileName certificat filename with full path
     * @param certPassword certificat password
     */
    private static void configureSSLOnTheClient(final RapgConsultation20 proxy, final String certFileName, final String certPassword, String certType) throws Exception {

        SSLContext sc = null;
        KeyStore ks = null;

        try {
            sc = SSLContext.getInstance(CommonProperties.RAPG_SSL_CONTEXT_TYPE.getValue());

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
                throw e;
            }

            // Add certificate to the conduit
            final KeyManagerFactory keyFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyFactory.init(ks, certPassword.toCharArray());
            final KeyManager[] km = keyFactory.getKeyManagers();
            sc.init(keyFactory.getKeyManagers(), null, null);

            final Map<String, Object> ctxt = ((BindingProvider) proxy).getRequestContext();
            ctxt.put(SSL_SOCKET_FACTORY_JAX_WS_RI, sc.getSocketFactory());
            ctxt.put(SSL_SOCKET_FACTORY_ORACLE_JDK, sc.getSocketFactory());
            ctxt.put(SSL_SOCKET_FACTORY_IBM_JDK, sc.getSocketFactory());
            ctxt.put(SSL_SOCKET_FACTORY_OPEN_JDK, sc.getSocketFactory());

        } catch (final FileNotFoundException e) {
            System.err.println("File " + certFileName + " doesn't exist");
            throw e;
        } catch (final IOException ioe) {
            System.err.println("File " + certFileName + " doesn't exist. Cause by " + ioe.getCause());
            throw ioe;
        } catch (final KeyStoreException kse) {
            System.out.println("Security configuration failed with the following: " + kse.getCause());
            throw kse;
        } catch (final NoSuchAlgorithmException nsa) {
            System.out.println("Security configuration failed with the following: " + nsa.getCause());
            throw nsa;
        } catch (final GeneralSecurityException gse) {
            System.out.println("Security configuration failed with the following: " + gse.getCause());
            throw gse;
        } catch (Exception e) {
            System.out.println("Security configuration failed with the following: " + e.getCause());
            throw e;
        }

    }

}
