package globaz.phenix.util;

import ch.admin.bsv.xmlns.ebsv_2028_000101._1.Delivery;
import ch.admin.ws.zas.regcent.nrr._0.NRRQueryServicePortType;

public class WIRRServiceCallUtil {

    public static final WIRRDataBean searchRenteWIRR(WIRRDataBean wirrDataBean, NRRQueryServicePortType port) {

        Delivery requestDelivery = WIRRServiceMappingUtil.convertWirrDataBeanToRequestDelivery(wirrDataBean);

        ch.admin.bsv.xmlns.ebsv_2028_000102._1.Delivery responseDelivery = port.searchData(requestDelivery);

        wirrDataBean = WIRRServiceMappingUtil.convertResponseDeliveryToWirrDataBean(responseDelivery);

        return wirrDataBean;
    }

}