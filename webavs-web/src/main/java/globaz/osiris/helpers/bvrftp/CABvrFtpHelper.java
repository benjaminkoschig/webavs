/**
 *
 */
package globaz.osiris.helpers.bvrftp;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BIPersistentObjectList;
import globaz.osiris.db.bvrftp.CABvrFtpListViewBean;
import globaz.osiris.db.process.CABvrViewBean;

/**
 * @author SCO
 * 
 */
public class CABvrFtpHelper extends FWHelper {
    /*
     * (non-Javadoc)
     * 
     * @seeglobaz.framework.controller.FWHelper#_find(globaz.globall.db. BIPersistentObjectList,
     * globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _find(BIPersistentObjectList persistentList, FWAction action, BISession session) throws Exception {
        ((CABvrFtpListViewBean) persistentList).listDirectoryFiles();
    }

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        CABvrFtpListViewBean listViewBean = new CABvrFtpListViewBean();
        listViewBean.setDistantDirectoryName(((CABvrViewBean) viewBean).getDistantDirectoryName());
        listViewBean.setISession(session);

        try {
            // TODO sch 25 févr. 2010 Selon les best practice de R&T la méthode
            // getFileFromFtp devrait être dans le Helper
            String fileAbsPath = listViewBean.getFileFromFtp(((CABvrViewBean) viewBean).getUserSelectedFileName());

            ((CABvrViewBean) viewBean).setFileName(fileAbsPath);
        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.toString());
        }

        // return viewBean;
    }
}
