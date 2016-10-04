package globaz.phenix.util;

import globaz.globall.db.BSession;
import globaz.jade.common.Jade;
import globaz.jade.crypto.JadeDefaultEncrypters;
import globaz.jade.crypto.JadeEncrypterNotFoundException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import ch.admin.bsv.xmlns.ebsv_2028_000101._1.Delivery;
import ch.admin.ws.zas.regcent.nrr._0.NRRQueryServicePortType;
import ch.globaz.common.properties.CommonProperties;

public class WIRRServiceCallUtil {

    public static final String SYSTEM_PROPERTY_KEYSTORE_PATH = "javax.net.ssl.keyStore";
    public static final String SYSTEM_PROPERTY_KEYSTORE_TYPE = "javax.net.ssl.keyStoreType";
    public static final String SYSTEM_PROPERTY_KEYSTORE_PASSWORD = "javax.net.ssl.keyStorePassword";

    public static final WIRRDataBean searchRenteWIRR(BSession session, WIRRDataBean wirrDataBean) {

        try {

            try {

                System.setProperty(SYSTEM_PROPERTY_KEYSTORE_PATH, CommonProperties.KEYSTORE_PATH.getValue());
                System.setProperty(SYSTEM_PROPERTY_KEYSTORE_TYPE, CommonProperties.KEYSTORE_TYPE.getValue());
                System.setProperty(SYSTEM_PROPERTY_KEYSTORE_PASSWORD, JadeDefaultEncrypters.getJadeDefaultEncrypter()
                        .decrypt(CommonProperties.KEYSTORE_PASSWORD.getValue()));

            } catch (Exception e) {
                // Nothing to do
                // Just continue without indicate keystore configuration
                // Maybe a proxy is used and no keystore is needed
            }

            Service service = Service.create(new URL(CPProperties.WIRRWEBSERVICE_URI_WSDL.getValue()), new QName(
                    CPProperties.WIRRWEBSERVICE_NAMESPACE.getValue(), CPProperties.WIRRWEBSERVICE_NAME.getValue()));

            NRRQueryServicePortType port = service.getPort(NRRQueryServicePortType.class);

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

    public static void main(String[] args) throws JadeEncrypterNotFoundException, Exception {

        Jade.getInstance();
        System.out.println(JadeDefaultEncrypters.getJadeDefaultEncrypter().encrypt("ZZIHMLVFSQ"));

    }

}