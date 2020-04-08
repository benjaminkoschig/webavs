package globaz.apg.util;

import ch.admin.cdc.rapg.core.dto.generated._1.RapgAnnoncesRequestType;
import ch.admin.cdc.seodor.core.dto.generated._1.GetServicePeriodsRequestType;
import ch.admin.cdc.seodor.core.dto.generated._1.ServicePeriodsRequestType;
import ch.eahv.rapg.common._4.DeliveryOfficeType;
import org.xml.sax.Locator;

import java.math.BigInteger;

public class APGSeodorServiceMappingUtil {

    public static final String SOURCE_LOCATION = "";

    public static final GetServicePeriodsRequestType convertSeodorDataBeanToRequestDelivery(APGSeodorDataBean seodorDataBean) throws Exception {
        GetServicePeriodsRequestType getServicePeriodsRequestType = new GetServicePeriodsRequestType();
        Locator locator = null;

        getServicePeriodsRequestType.setMessage(seodorDataBean.getMessage());
        getServicePeriodsRequestType.setSourceLocation(locator);
//        getServicePeriodsRequestType.setStartDate(seodorDataBean.getStartDate());
//        getServicePeriodsRequestType.setVn(Long.parseLong(seodorDataBean.getNss()));
        getServicePeriodsRequestType.setSourceLocation(locator);


        return getServicePeriodsRequestType;
    }
}
