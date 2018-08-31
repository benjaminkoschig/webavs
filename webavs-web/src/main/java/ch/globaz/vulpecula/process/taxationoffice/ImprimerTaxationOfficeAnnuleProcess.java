package ch.globaz.vulpecula.process.taxationoffice;

import globaz.globall.db.GlobazJobQueue;
import globaz.jade.smtp.JadeSmtpClient;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.taxationoffice.TaxationOffice;
import ch.globaz.vulpecula.external.BProcessWithContext;

public class ImprimerTaxationOfficeAnnuleProcess extends BProcessWithContext {
    private static final long serialVersionUID = 1688602577944749918L;

    private Date date;

    public ImprimerTaxationOfficeAnnuleProcess() {
        date = Date.now().addDays(-1);
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        super._executeProcess();
        setSendCompletionMail(false);

        List<TaxationOffice> tos = getTaxationOffices();
        sortByNumeroAffilie(tos);
        if (!tos.isEmpty()) {
            ImprimerTaxationOfficeAnnuleExcel excelDoc = new ImprimerTaxationOfficeAnnuleExcel(getSession(),
                    DocumentConstants.LISTES_TO_ANNULE_DOC_NAME + "_" + date.getSwissValue(),
                    DocumentConstants.LISTES_TO_ANNULE, tos);
            excelDoc.create();
            List<String> emails = VulpeculaServiceLocator.getPropertiesService().getTosAnnuleesEmails();
            if (!emails.isEmpty()) {
                String[] to = emails.toArray(new String[0]);
                JadeSmtpClient.getInstance().sendMail(to,
                        DocumentConstants.LISTES_TO_ANNULE + " le " + date.getSwissValue(), "",
                        new String[] { excelDoc.getOutputFile() });
            }
        }
        return true;
    }

    private List<TaxationOffice> getTaxationOffices() {
        return VulpeculaRepositoryLocator.getTaxationOfficeRepository().findAnnuleForDate(date);
    }

    private void sortByNumeroAffilie(List<TaxationOffice> tos) {
        Collections.sort(tos, new Comparator<TaxationOffice>() {
            @Override
            public int compare(TaxationOffice to1, TaxationOffice to2) {
                return to1.getEmployeurAffilieNumero().compareTo(to2.getEmployeurAffilieNumero());
            }
        });
    }

    @Override
    protected String getEMailObject() {
        return DocumentConstants.LISTES_TO_ANNULE + " " + date.getSwissValue();
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

}
