package globaz.osiris.db.yellowreportfile;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.jade.client.util.JadeDateUtil;

public class CAYellowReportFileViewBean extends CAYellowReportFile implements FWViewBeanInterface {

    private static final long serialVersionUID = -8970916470401969136L;

    public String getDateFormated() {
        return JadeDateUtil.getGlobazFormattedDateTime(getDateCreated());
    }

}
