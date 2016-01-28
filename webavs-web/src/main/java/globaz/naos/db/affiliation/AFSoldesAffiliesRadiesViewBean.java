package globaz.naos.db.affiliation;

import globaz.globall.util.JACalendar;
import globaz.naos.db.AFAbstractViewBean;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class AFSoldesAffiliesRadiesViewBean extends AFAbstractViewBean {

    private String eMailAddress = "";
    private String fromDate = JACalendar.todayJJsMMsAAAA();
    private String typeImpression = "pdf";

    public String getEMailAddress() {
        return eMailAddress;
    }

    public String getFromDate() {
        return fromDate;
    }

    public String getTypeImpression() {
        return typeImpression;
    }

    public void setEMailAddress(String mailAddress) {
        eMailAddress = mailAddress;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public void setTypeImpression(String typeImpression) {
        this.typeImpression = typeImpression;
    }
}
