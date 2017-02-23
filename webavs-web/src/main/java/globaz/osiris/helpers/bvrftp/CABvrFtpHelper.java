/**
 *
 */
package globaz.osiris.helpers.bvrftp;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BIPersistentObjectList;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.osiris.db.bvrftp.CABvrFtpListViewBean;
import globaz.osiris.db.process.CABvrViewBean;
import globaz.osiris.db.yellowreportfile.CAYellowReportFile;
import globaz.osiris.db.yellowreportfile.CAYellowReportFileService;

/**
 * @author SCO
 * 
 */
public class CABvrFtpHelper extends FWHelper {

    @Override
    protected void _find(BIPersistentObjectList persistentList, FWAction action, BISession session) throws Exception {
        ((CABvrFtpListViewBean) persistentList).listDirectoryFiles();
    }

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        CABvrViewBean vb = (CABvrViewBean) viewBean;
        String fileAbsPath = null;

        try {
            if (!JadeStringUtil.isEmpty(vb.getIdYellowReportFile())) {
                CAYellowReportFile reportFile = new CAYellowReportFileService((BSession) session).read(vb
                        .getIdYellowReportFile());

                fileAbsPath = reportFile.getFileName();

            } else {
                CABvrFtpListViewBean listViewBean = new CABvrFtpListViewBean();
                listViewBean.setDistantDirectoryName(vb.getDistantDirectoryName());
                listViewBean.setISession(session);

                fileAbsPath = listViewBean.getFileFromFtp(vb.getUserSelectedFileName());

            }
        } catch (Exception e) {
            JadeLogger.error(e, e.getMessage());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.toString());
        }

        vb.setFileName(fileAbsPath);
    }
}
