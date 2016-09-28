package globaz.phenix.util;

import globaz.globall.db.BSession;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import ch.admin.bsv.xmlns.ebsv_2028_000101._1.Delivery;
import ch.admin.ws.zas.regcent.nrr._0.NRRQueryServicePortType;
import ch.globaz.common.properties.PropertiesException;

public class WIRRServiceCallUtil {

    public static final NRRQueryServicePortType initService() throws MalformedURLException, PropertiesException {

        Service service = Service.create(new URL(CPProperties.WIRRWEBSERVICE_URI_WSDL.getValue()), new QName(
                CPProperties.WIRRWEBSERVICE_NAMESPACE.getValue(), CPProperties.WIRRWEBSERVICE_NAME.getValue()));

        NRRQueryServicePortType port = service.getPort(NRRQueryServicePortType.class);

        return port;

    }

    public static final WIRRDataBean searchRenteWIRR(BSession session, WIRRDataBean wirrDataBean,
            NRRQueryServicePortType port) {

        try {

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

    // public static void main(String[] args) throws MalformedURLException, PropertiesException {
    //
    // WIRRDataBean dataBean = new WIRRDataBean();
    // dataBean.setNss("7561207361910");
    //
    // dataBean = WIRRServiceCallUtil.searchRenteWIRR(dataBean, WIRRServiceCallUtil.initService());
    //
    // System.out.println(dataBean.hasRenteWIRRFounded());
    //
    // }

}