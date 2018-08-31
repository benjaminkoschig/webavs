package globaz.vulpecula.helpers.listes;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.process.listeQuorums.ListeQuorumsProcess;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.jade.log.JadeLogger;
import globaz.prestation.utils.DateException;
import globaz.vulpecula.vb.listes.PTListeQuorumsViewBean;

public class PTListeQuorumsHelper extends FWHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        try {
            PTListeQuorumsViewBean vb = (PTListeQuorumsViewBean) viewBean;
            ListeQuorumsProcess process = new ListeQuorumsProcess();
            process.setPeriode(datesToPeriod(vb));
            process.setEMailAddress(vb.getEmail());
            process.setCodeConvention(vb.getCodeConvention());
            process.setDetail(vb.getDetail());

            BProcessLauncher.start(process);
        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.getMessage());
            JadeLogger.error(e, e.getMessage());
        }
    }

    private Periode datesToPeriod(PTListeQuorumsViewBean viewBean) throws DateException {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd.MM.yyyy");
        DateTime dateDebut = formatter.parseDateTime(viewBean.getDateFrom());
        DateTime dateFin = formatter.parseDateTime(viewBean.getDateTo());

        if (dateDebut.isAfter(dateFin)) {
            StringBuilder sb = new StringBuilder();
            sb.append("La date de fin ");
            sb.append(formatter.print(dateFin));
            sb.append(" ne peut pas être antérieur à la date de début ");
            sb.append(formatter.print(dateDebut));
            throw new DateException(sb.toString());
        }
        return new Periode(viewBean.getDateFrom(), viewBean.getDateTo());
    }

}
