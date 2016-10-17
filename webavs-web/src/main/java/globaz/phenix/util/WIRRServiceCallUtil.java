package globaz.phenix.util;

import globaz.globall.db.BSession;
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
import ch.admin.bsv.xmlns.ebsv_2028_000101._1.Delivery;
import ch.admin.ws.zas.regcent.nrr._0.NRRQueryServicePortType;
import ch.globaz.common.properties.CommonProperties;

public class WIRRServiceCallUtil {

    private static final String SSL_SOCKET_FACTORY_ORACLE_JDK = "com.sun.xml.ws.transport.https.client.SSLSocketFactory";
    private static final String SSL_SOCKET_FACTORY_JAX_WS_RI = "com.sun.xml.internal.ws.transport.https.client.SSLSocketFactory";
    private static final String WIRR_WSDL_RESOURCE_PATH = "wsdl/wirr_wsdl.xml";

    public static final WIRRDataBean searchRenteWIRR(BSession session, WIRRDataBean wirrDataBean) {

        try {
            // init service
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            URL wsdlLocation = classloader.getResource(WIRR_WSDL_RESOURCE_PATH);
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
                bindingProvider.getRequestContext().put(SSL_SOCKET_FACTORY_JAX_WS_RI, sc.getSocketFactory());
                bindingProvider.getRequestContext().put(SSL_SOCKET_FACTORY_ORACLE_JDK, sc.getSocketFactory());

            }

            Delivery requestDelivery = WIRRServiceMappingUtil.convertWirrDataBeanToRequestDelivery(wirrDataBean);
            ch.admin.bsv.xmlns.ebsv_2028_000102._1.Delivery responseDelivery = port.searchData(requestDelivery);

            wirrDataBean = WIRRServiceMappingUtil.putResponseDeliveryResultInWirrDataBean(session, responseDelivery,
                    wirrDataBean);

        } catch (Exception e) {
            if (wirrDataBean == null) {
                wirrDataBean = new WIRRDataBean();
            }

            wirrDataBean.setHasTechnicalError(true);
            wirrDataBean.setMessageForUser(session.getLabel("ERROR_REASON_DETAILED_UNEXCEPTED_ERROR"));
            e.printStackTrace();
        } finally {

            if (wirrDataBean == null) {
                wirrDataBean = new WIRRDataBean();
            }

            if (wirrDataBean.hasTechnicalError()) {
                System.out.println(wirrDataBean.getErrorReason());
                System.out.println(wirrDataBean.getErrorDetailedReason());
                System.out.println(wirrDataBean.getErrorComment());
            }

        }

        return wirrDataBean;

    }

}