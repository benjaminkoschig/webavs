package ch.globaz.vulpecula.documents.prestations;

import globaz.caisse.report.helper.CaisseHeaderReportBean;
import ch.globaz.vulpecula.documents.catalog.VulpeculaDocumentManager;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.prestations.PrestationParEmployeur;
import ch.globaz.vulpecula.external.models.pyxis.CodeLangue;

public abstract class DocumentPrestations<T extends PrestationParEmployeur> extends VulpeculaDocumentManager<T> {

    public DocumentPrestations() throws Exception {
    }

    public DocumentPrestations(T element, String documentName, String numeroInforom) throws Exception {
        super(element, documentName, numeroInforom);
    }

    @Override
    public CaisseHeaderReportBean giveBeanHeader() throws Exception {
        CaisseHeaderReportBean beanReport = new CaisseHeaderReportBean();
        beanReport.setAdresse(getCurrentElement().getEmployeur().getAdressePrincipaleFormatee());
        beanReport.setNomCollaborateur(getSession().getUserFullName());
        beanReport.setTelCollaborateur(getSession().getUserInfo().getPhone());
        beanReport.setUser(getSession().getUserInfo());
        beanReport.setDate(Date.now().getSwissValue());
        beanReport.setNoAffilie(getCurrentElement().getEmployeur().getAffilieNumero());
        beanReport.setConfidentiel(true);
        return beanReport;
    }

    @Override
    public CodeLangue getCodeLangue() {
        CodeLangue langue = CodeLangue.fromValue(getCurrentElement().getEmployeur().getLangue());
        if (langue != null) {
            return langue;
        } else {
            return CodeLangue.FR;
        }
    }
}
