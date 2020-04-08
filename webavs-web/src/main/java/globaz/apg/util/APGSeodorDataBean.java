package globaz.apg.util;

import ch.admin.cdc.seodor.core.dto.generated._1.GetServicePeriodsRequestType;

import javax.xml.datatype.XMLGregorianCalendar;

public class APGSeodorDataBean {

    private String nss;
    private XMLGregorianCalendar startDate;
    private GetServicePeriodsRequestType.Message message;

    private boolean hasTechnicalError = false;

    public XMLGregorianCalendar getStartDate() {
        return startDate;
    }

    public String getNss() {
        return nss;
    }

    public GetServicePeriodsRequestType.Message getMessage() {
        return message;
    }

    public boolean isHasTechnicalError(){
        return hasTechnicalError;
    }

    public void setHasTechnicalError(boolean hasTechnicalError) {
        this.hasTechnicalError = hasTechnicalError;
    }
}
