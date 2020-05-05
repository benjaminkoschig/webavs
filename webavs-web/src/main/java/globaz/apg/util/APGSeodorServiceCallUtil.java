package globaz.apg.util;

import ch.admin.cdc.seodor.core.dto.generated._1.GetServicePeriodsRequestType;
import ch.admin.cdc.seodor.core.dto.generated._1.GetServicePeriodsResponseType;
import ch.admin.zas.seodor.ws.service_periods._1.ServicePeriodsPort10;
import ch.admin.zas.seodor.ws.service_periods._1.ServicePeriodsService10;
import ch.globaz.common.properties.CommonProperties;
import ch.globaz.common.properties.PropertiesException;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.crypto.JadeDefaultEncrypters;
import globaz.phenix.util.WIRRDataBean;
import globaz.phenix.util.WIRRServiceMappingUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wirrch.admin.bsv.xmlns.ebsv_2028_000101._1.Delivery;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.security.*;
import java.security.cert.CertificateException;
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
                    // TODO Ligne à supprimer. Pour test
                    keyStorePath = "C:\\Users\\eniv\\Downloads\\certificat\\T6-600024-1.p12";

                    SSLContext sc = SSLContext.getInstance(contextType);
                    KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                    KeyStore ks = KeyStore.getInstance(keyStoreType);
                    FileInputStream fis = new FileInputStream(keyStorePath);
                    String certPasswd = JadeDefaultEncrypters.getJadeDefaultEncrypter().decrypt(keyStorePass);

                    ks.load(fis, certPasswd.toCharArray());
                    IOUtils.closeQuietly(fis);
                    kmf.init(ks, certPasswd.toCharArray());
                    sc.init(kmf.getKeyManagers(), null, null);

                    final Map<String, Object> ctxt = ((BindingProvider) port).getRequestContext();
                    ctxt.put(SSL_SOCKET_FACTORY_JAX_WS_RI, sc.getSocketFactory());
                    ctxt.put(SSL_SOCKET_FACTORY_ORACLE_JDK, sc.getSocketFactory());
                    BindingProvider bindingProvider = (BindingProvider) port;

                // Si la propriété ide.webservice.url.endpoint existe on surcharge l'adresse du endpoint
                String endpoint = CommonProperties.SEODOR_ENDPOINT_ADDRESS.getValue();
//                if (StringUtils.isNotEmpty(endpoint)) {
//                    bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,  endpoint);
//                }
//                bindingProvider.getRequestContext().put(SSL_SOCKET_FACTORY_JAX_WS_RI, sc.getSocketFactory());
//                bindingProvider.getRequestContext().put(SSL_SOCKET_FACTORY_ORACLE_JDK, sc.getSocketFactory());
            }

            GetServicePeriodsRequestType requestDelivery = APGSeodorServiceMappingUtil.convertSeodorDataBeanToRequestDelivery(apgSeodorDataBean);

            GetServicePeriodsResponseType responseDelivery = port.getServicePeriods(requestDelivery);

                // TODO Mapper la réponse

                apgSeodorDataBeans = APGSeodorServiceMappingUtil.putResponseDeliveryResultInApgSeodorDataBean(session, responseDelivery, apgSeodorDataBean);

            // TODO Changer exception
            } catch (Exception e) {

                // TODO Logger les erreurs
                if (apgSeodorDataBean == null) {
                    apgSeodorDataBean = new APGSeodorDataBean();
                }

                apgSeodorDataBean.setHasTechnicalError(true);
                String message = session.getLabel("ERROR_REASON_DETAILED_UNEXCEPTED_ERROR");
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

//    public static final APGSeodorDataBean getPeriode(BSession session, APGSeodorDataBean apgSeodorDataBean) {
//        try {
//            // init service
//            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
//            URL wsdlLocation = classloader.getResource(CommonProperties.SEODOR_SEODOR_WSDL_PATH.getValue());
//            Service service = Service.create(wsdlLocation,
//                    new QName(CommonProperties.SEODOR_WEBSERVICE_NAMESPACE.getValue(),
//                            CommonProperties.SEODOR_WEBSERVICE_NAME.getValue()));
//
//            ServicePeriodsPort10 port = service.getPort(ServicePeriodsPort10.class);
//
//            if (!JadeStringUtil.isBlankOrZero(CommonProperties.SEODOR_KEYSTORE_PATH.getValue())
//                    && !JadeStringUtil.isBlankOrZero(CommonProperties.SEODOR_KEYSTORE_TYPE.getValue())
//                    && !JadeStringUtil.isBlankOrZero(CommonProperties.SEODOR_KEYSTORE_PASSWORD.getValue())
//                    && !JadeStringUtil.isBlankOrZero(CommonProperties.SEODOR_SSL_CONTEXT_TYPE.getValue())) {
//
//                SSLContext sc = SSLContext.getInstance(CommonProperties.SEODOR_SSL_CONTEXT_TYPE.getValue());
//
//                KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
//
//                KeyStore ks = KeyStore.getInstance(CommonProperties.SEODOR_KEYSTORE_TYPE.getValue());
//                FileInputStream fis = new FileInputStream(CommonProperties.SEODOR_KEYSTORE_PATH.getValue());
//
//                String certPasswd = JadeDefaultEncrypters.getJadeDefaultEncrypter().decrypt(
//                        CommonProperties.SEODOR_KEYSTORE_PASSWORD.getValue());
//
//                ks.load(fis, certPasswd.toCharArray());
//                IOUtils.closeQuietly(fis);
//                kmf.init(ks, certPasswd.toCharArray());
//
//                sc.init(kmf.getKeyManagers(), null, null);
//                final Map<String, Object> ctxt = ((BindingProvider) port).getRequestContext();
//                ctxt.put(SSL_SOCKET_FACTORY_JAX_WS_RI, sc.getSocketFactory());
//                ctxt.put(SSL_SOCKET_FACTORY_ORACLE_JDK, sc.getSocketFactory());
//                BindingProvider bindingProvider = (BindingProvider) port;
//
//                // Si la propriété ide.webservice.url.endpoint existe on surcharge l'adresse du endpoint
//                String endpoint = CommonProperties.SEODOR_ENDPOINT_ADDRESS.getValue();
//                if (StringUtils.isNotEmpty(endpoint)) {
//                    bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,  endpoint);
//                }
////                bindingProvider.getRequestContext().put(SSL_SOCKET_FACTORY_JAX_WS_RI, sc.getSocketFactory());
////                bindingProvider.getRequestContext().put(SSL_SOCKET_FACTORY_ORACLE_JDK, sc.getSocketFactory());
//            }
//
//            GetServicePeriodsRequestType requestDelivery = APGSeodorServiceMappingUtil.convertSeodorDataBeanToRequestDelivery(apgSeodorDataBean);
//
//            GetServicePeriodsResponseType responseDelivery = port.getServicePeriods(requestDelivery);
//
//
//        } catch (Exception e) {
//            LOG.error("test");
//        }
//        return apgSeodorDataBean;
//    }

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
    private static void configureSSLOnTheClient(final ServicePeriodsPort10 proxy, final String keyStorePath,
                                                final String keyStorePass, String keyStoreType)
            throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException {

        // Get httpConduit
        final Client client = ClientProxy.getClient(proxy);
        final HTTPConduit httpConduit = (HTTPConduit) client.getConduit();
        KeyStore ks = null;

        final TLSClientParameters tlsParams = new TLSClientParameters();
        tlsParams.setDisableCNCheck(true);

        // Read the certificate
        FileInputStream filePkcs12;
        ks = KeyStore.getInstance(keyStoreType);
        filePkcs12 = new FileInputStream(keyStorePath);

        // For a better security you can encode your password and decode it here
        ks.load(filePkcs12, keyStorePass.toCharArray());
        filePkcs12.close();

        // Add certificate to the conduit
        final KeyManagerFactory keyFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyFactory.init(ks, keyStorePass.toCharArray());
        final KeyManager[] km = keyFactory.getKeyManagers();
        tlsParams.setKeyManagers(km);

        httpConduit.setTlsClientParameters(tlsParams);

    }
}
