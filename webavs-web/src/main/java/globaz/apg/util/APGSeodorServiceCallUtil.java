package globaz.apg.util;

import ch.admin.cdc.seodor.core.dto.generated._1.GetServicePeriodsRequestType;
import ch.admin.cdc.seodor.core.dto.generated._1.GetServicePeriodsResponseType;

import ch.admin.zas.seodor.ws.service_periods._1.ServicePeriodsPort10;
import ch.admin.zas.seodor.ws.service_periods._1.ServicePeriodsService10;
import ch.globaz.common.properties.CommonProperties;
import ch.globaz.common.properties.PropertiesException;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.security.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class APGSeodorServiceCallUtil {
    private static final String SSL_SOCKET_FACTORY_ORACLE_JDK = "com.sun.xml.ws.transport.https.client.SSLSocketFactory";
    private static final String SSL_SOCKET_FACTORY_JAX_WS_RI = "com.sun.xml.internal.ws.transport.https.client.SSLSocketFactory";
    private static final Logger LOG = LoggerFactory.getLogger(APGSeodorServiceCallUtil.class);

        public static final List<APGSeodorDataBean> getPeriode(BSession session, APGSeodorDataBean apgSeodorDataBean) {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            List<APGSeodorDataBean> apgSeodorDataBeans = new ArrayList<>();

            try {
                String webServiceNameSpace = CommonProperties.SEODOR_WEBSERVICE_NAMESPACE.getValue();
                String webServiceName = CommonProperties.SEODOR_WEBSERVICE_NAME.getValue();

                String keyStorePath = CommonProperties.SEODOR_KEYSTORE_PATH.getValue();
                String keyStoreType = CommonProperties.SEODOR_KEYSTORE_TYPE.getValue();
                String keyStorePass = CommonProperties.SEODOR_KEYSTORE_PASSWORD.getValue();
                String contextType = CommonProperties.SEODOR_SSL_CONTEXT_TYPE.getValue();

                URL wsdlLocation = classloader.getResource(CommonProperties.SEODOR_SEODOR_WSDL_PATH.getValue());

                // Création du ServicePort
                ServicePeriodsPort10 port = createServicePeriodsPort(wsdlLocation, webServiceNameSpace, webServiceName);

                // Condition pour générer la config SSL et le Binding
                if (!JadeStringUtil.isBlankOrZero(CommonProperties.SEODOR_KEYSTORE_PATH.getValue())
                        && !JadeStringUtil.isBlankOrZero(CommonProperties.SEODOR_KEYSTORE_TYPE.getValue())
                        && !JadeStringUtil.isBlankOrZero(CommonProperties.SEODOR_KEYSTORE_PASSWORD.getValue())) {

                    // Config SSL
                    configureSSLOnTheClient(port, keyStorePath, keyStorePass, keyStoreType);
            }

            GetServicePeriodsRequestType requestDelivery = APGSeodorServiceMappingUtil.convertSeodorDataBeanToRequestDelivery(apgSeodorDataBean);

            GetServicePeriodsResponseType responseDelivery = port.getServicePeriods(requestDelivery);

            apgSeodorDataBeans = APGSeodorServiceMappingUtil.putResponseDeliveryResultInApgSeodorDataBean(session, responseDelivery, apgSeodorDataBean);

            // TODO Changer exception
            } catch (Exception e) {
                if (apgSeodorDataBean == null) {
                    apgSeodorDataBean = new APGSeodorDataBean();
                }
                apgSeodorDataBean.setHasTechnicalError(true);
                String message = session.getLabel("ERROR_REASON_DETAILED_UNEXCEPTED_ERROR");
                // TODO Label a créer
                apgSeodorDataBean.setMessageTechnicalError("Erreur de connexion avec la centrale");
                //apgSeodorDataBean.setMessageForUser(message);
                LOG.error(message, e);
            } finally {

                if (Objects.isNull(apgSeodorDataBeans)) {
                    apgSeodorDataBean = new APGSeodorDataBean();
                }

                if (apgSeodorDataBean.isHasTechnicalError()) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("TechnicalError as occurr : errorReason = [");
                    //sb.append(apgSeodorDataBean.getErrorReason());
                    sb.append("] ");
                    sb.append("errorDetailedReason = [");
                    //sb.append(apgSeodorDataBean.getErrorDetailedReason());
                    sb.append("] ");
                    sb.append("errorComment = [");
                    //sb.append(apgSeodorDataBean.getErrorComment());
                    sb.append("]");
                    LOG.error(sb.toString());
                    apgSeodorDataBeans.add(apgSeodorDataBean);
                }

                return apgSeodorDataBeans;
            }
        }

    /**
     * Méthode qui créé le SericePeriodsPort
     *
     * @param wsdlLocation
     * @param webServiceNameSpace
     * @param webServiceName
     * @return
     * @throws PropertiesException
     */
        private static ServicePeriodsPort10 createServicePeriodsPort(URL wsdlLocation, String webServiceNameSpace, String webServiceName) throws PropertiesException {
            ServicePeriodsService10 servicePeriodsService10 = new ServicePeriodsService10(wsdlLocation,
                    new QName(webServiceNameSpace, webServiceName));
            return servicePeriodsService10.getPort(ServicePeriodsPort10.class);
        }

    /**
     * Configure SSL On the client
     * @param proxy webservice proxy
     * @param keyStorePath keyStorePath certificat filename with full path
     * @param keyStorePass certificat password
     * @param keyStoreType keyStoreType key types
     */
    private static void configureSSLOnTheClient(final ServicePeriodsPort10 proxy, final String keyStorePath, final String keyStorePass, String keyStoreType) throws Exception {

        SSLContext sc = null;
        KeyStore ks = null;

        try {
            sc = SSLContext.getInstance(CommonProperties.SEODOR_SSL_CONTEXT_TYPE.getValue());

            // Read the certificate
            FileInputStream filePkcs12;
            ks = KeyStore.getInstance(keyStoreType);
            filePkcs12 = new FileInputStream(keyStorePath);

            // For a better security you can encode your password and decode it here
            ks.load(filePkcs12, keyStorePass.toCharArray());

            try {
                filePkcs12.close();
            } catch (final IOException e) {
                System.err.println("Error on close " + keyStorePath + " file");
                throw e;
            }

            // Add certificate to the conduit
            final KeyManagerFactory keyFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyFactory.init(ks, keyStorePass.toCharArray());
            final KeyManager[] km = keyFactory.getKeyManagers();
            sc.init(keyFactory.getKeyManagers(), null, null);

            final Map<String, Object> ctxt = ((BindingProvider) proxy).getRequestContext();
            ctxt.put(SSL_SOCKET_FACTORY_JAX_WS_RI, sc.getSocketFactory());
            ctxt.put(SSL_SOCKET_FACTORY_ORACLE_JDK, sc.getSocketFactory());

        } catch (final FileNotFoundException e) {
            System.err.println("File " + keyStorePath + " doesn't exist");
            throw e;
        } catch (final IOException ioe) {
            System.err.println("File " + keyStorePath + " doesn't exist. Cause by " + ioe.getCause());
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
