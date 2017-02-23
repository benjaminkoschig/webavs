package globaz.osiris.db.yellowreportfile;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;

public class CAYellowReportFileListViewBean extends CAYellowReportFileManager implements FWListViewBeanInterface {

    private static final long serialVersionUID = 2310988296841005441L;
    private boolean isModeFtp = false;

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CAYellowReportFileViewBean();
    }

    public void setModeFtp(boolean isModeFtp) {
        this.isModeFtp = isModeFtp;
    }

    public boolean isModeFtp() {
        return isModeFtp;
    }
}
