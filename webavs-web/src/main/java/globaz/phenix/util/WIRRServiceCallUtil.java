package globaz.phenix.util;

import globaz.common.db.CommonLogWIRR;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.crypto.JadeDefaultEncrypters;
import java.io.FileInputStream;
import java.net.URL;
import java.security.KeyStore;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wirrch.admin.bsv.xmlns.ebsv_2028_000101._1.Delivery;
import wirrch.admin.ws.zas.regcent.nrr._0.NRRQueryServicePortType;
import ch.globaz.common.properties.CommonProperties;

public class WIRRServiceCallUtil {

    private static final String SSL_SOCKET_FACTORY_ORACLE_JDK = "com.sun.xml.ws.transport.https.client.SSLSocketFactory";
    private static final String SSL_SOCKET_FACTORY_JAX_WS_RI = "com.sun.xml.internal.ws.transport.https.client.SSLSocketFactory";
    private static final Logger logger = LoggerFactory.getLogger(WIRRServiceCallUtil.class);

    private static final void logCallWIRR(BSession session, Delivery requestDelivery) {

        BTransaction wirrTransaction = null;
        try {

            wirrTransaction = new BTransaction(session);
            wirrTransaction.openTransaction();

            CommonLogWIRR logWIRREntity = new CommonLogWIRR();
            logWIRREntity.setSession(session);
            logWIRREntity.setVisa(session.getUserName());
            logWIRREntity.setPrenomNom(session.getUserFullName());
            logWIRREntity.setSedexId(CommonProperties.WIRRWEBSERVICE_SEDEX_SENDER_ID.getValue());
            logWIRREntity.setMessageId(requestDelivery.getHeader().getMessageId());
            logWIRREntity.add(wirrTransaction);

        } catch (Exception e) {
            logger.error("Error in login call to webservice WIRR : " + e.toString(), e);
        } finally {

            try {

                if (wirrTransaction.isRollbackOnly() || wirrTransaction.hasErrors()) {
                    logger.error("WIRRTransaction has errors : " + wirrTransaction.getErrors().toString());
                    wirrTransaction.rollback();
                } else {
                    wirrTransaction.commit();
                }

                wirrTransaction.closeTransaction();

            } catch (Exception e2) {
                logger.error("Error with WIRRTransaction : " + e2.toString(), e2);
            }

        }

    }

    public static final WIRRDataBean searchRenteWIRR(BSession session, WIRRDataBean wirrDataBean) {

        try {
            // init service
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            URL wsdlLocation = classloader.getResource(CommonProperties.WIRR_WSDL_RESOURCE_PATH.getValue());
            Service service = Service.create(wsdlLocation,
                    new QName(CommonProperties.WIRRWEBSERVICE_NAMESPACE.getValue(),
                            CommonProperties.WIRRWEBSERVICE_NAME.getValue()));

            NRRQueryServicePortType port = service.getPort(NRRQueryServicePortType.class);

            if (!JadeStringUtil.isBlankOrZero(CommonProperties.WIRR_KEYSTORE_PATH.getValue())
                    && !JadeStringUtil.isBlankOrZero(CommonProperties.WIRR_KEYSTORE_TYPE.getValue())
                    && !JadeStringUtil.isBlankOrZero(CommonProperties.WIRR_KEYSTORE_PASSWORD.getValue())
                    && !JadeStringUtil.isBlankOrZero(CommonProperties.WIRR_SSL_CONTEXT_TYPE.getValue())) {

                SSLContext sc = SSLContext.getInstance(CommonProperties.WIRR_SSL_CONTEXT_TYPE.getValue());

                KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());

                KeyStore ks = KeyStore.getInstance(CommonProperties.WIRR_KEYSTORE_TYPE.getValue());
                FileInputStream fis = new FileInputStream(CommonProperties.WIRR_KEYSTORE_PATH.getValue());

                String certPasswd = JadeDefaultEncrypters.getJadeDefaultEncrypter().decrypt(
                        CommonProperties.WIRR_KEYSTORE_PASSWORD.getValue());

                ks.load(fis, certPasswd.toCharArray());
                IOUtils.closeQuietly(fis);
                kmf.init(ks, certPasswd.toCharArray());

                sc.init(kmf.getKeyManagers(), null, null);

                BindingProvider bindingProvider = (BindingProvider) port;

                // Si la propri?t? ide.webservice.url.endpoint existe on surcharge l'adresse du endpoint
                String endpoint = CommonProperties.WIRR_ENDPOINT_ADDRESS.getValue();
                if (StringUtils.isNotEmpty(endpoint)) {
                    bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,  endpoint);
                }
                bindingProvider.getRequestContext().put(SSL_SOCKET_FACTORY_JAX_WS_RI, sc.getSocketFactory());
                bindingProvider.getRequestContext().put(SSL_SOCKET_FACTORY_ORACLE_JDK, sc.getSocketFactory());
            }

            Delivery requestDelivery = WIRRServiceMappingUtil.convertWirrDataBeanToRequestDelivery(wirrDataBean);
            logCallWIRR(session, requestDelivery);
            wirrch.admin.bsv.xmlns.ebsv_2028_000102._1.Delivery responseDelivery = port.searchData(requestDelivery);

            wirrDataBean = WIRRServiceMappingUtil.putResponseDeliveryResultInWirrDataBean(session, responseDelivery,
                    wirrDataBean);

        } catch (Exception e) {
            if (wirrDataBean == null) {
                wirrDataBean = new WIRRDataBean();
            }

            wirrDataBean.setHasTechnicalError(true);
            String message = session.getLabel("ERROR_REASON_DETAILED_UNEXCEPTED_ERROR");
            wirrDataBean.setMessageForUser(message);
            logger.error(message, e);
        } finally {

            if (wirrDataBean == null) {
                wirrDataBean = new WIRRDataBean();
            }

            if (wirrDataBean.hasTechnicalError()) {
                StringBuilder sb = new StringBuilder();
                sb.append("TechnicalError as occurr : errorReason = [");
                sb.append(wirrDataBean.getErrorReason());
                sb.append("] ");
                sb.append("errorDetailedReason = [");
                sb.append(wirrDataBean.getErrorDetailedReason());
                sb.append("] ");
                sb.append("errorComment = [");
                sb.append(wirrDataBean.getErrorComment());
                sb.append("]");
                logger.error(sb.toString());
            }
        }
        return wirrDataBean;
    }

}