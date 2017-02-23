/**
 *
 */
package globaz.osiris.helpers.yellowreportfile;

import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BIPersistentObjectList;
import globaz.globall.util.JAVector;
import globaz.osiris.db.ordres.sepa.CACamt054Processor;
import globaz.osiris.db.yellowreportfile.CAYellowReportFileListViewBean;
import globaz.osiris.db.yellowreportfile.CAYellowReportFileType;
import globaz.osiris.db.yellowreportfile.CAYellowReportFileViewBean;
import java.util.List;

public class CAYellowReportFileHelper extends FWHelper {

    @Override
    protected void _find(BIPersistentObjectList persistentList, FWAction action, BISession session) throws Exception {
        CAYellowReportFileType type = ((CAYellowReportFileListViewBean) persistentList).getForTypeOfFile();

        // Si le type dans le filtre est ISO20022, alors nous nous connectons au FTP afin de lister tous les fichiers
        if (CAYellowReportFileType.ISO20022.equals(type)) {
            ((CAYellowReportFileListViewBean) persistentList).setModeFtp(true);
            ((CAYellowReportFileListViewBean) persistentList).setContainer(new JAVector());

            final List<String> files = new CACamt054Processor().getListFiles();
            for (final String file : files) {
                final CAYellowReportFileViewBean reportFile = new CAYellowReportFileViewBean();
                reportFile.setFileName(file);
                ((CAYellowReportFileListViewBean) persistentList).getContainer().add(reportFile);
            }
        } else {
            ((CAYellowReportFileListViewBean) persistentList).setModeFtp(false);
            super._find(persistentList, action, session);
        }
    }
}
