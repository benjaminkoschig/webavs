package globaz.vulpecula.helpers.listes;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcess;
import globaz.globall.db.BProcessLauncher;
import globaz.musca.db.facturation.FAModuleFacturation;
import globaz.vulpecula.vb.listes.PTPrestationsViewBean;
import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;
import ch.globaz.vulpecula.documents.prestations.DocumentAJPrinter;
import ch.globaz.vulpecula.documents.prestations.DocumentCPPrinter;
import ch.globaz.vulpecula.documents.prestations.DocumentPrestationsPrinter;
import ch.globaz.vulpecula.documents.prestations.DocumentSMPrinter;
import ch.globaz.vulpecula.domain.models.common.Destinataire;
import ch.globaz.vulpecula.domain.models.prestations.PrestationParEmployeur;
import ch.globaz.vulpecula.process.prestations.AbstractPrestationExcelProcess;
import ch.globaz.vulpecula.process.prestations.ListAJExcelProcess;
import ch.globaz.vulpecula.process.prestations.ListCPExcelProcess;
import ch.globaz.vulpecula.process.prestations.ListSMExcelProcess;

public class PTPrestationsHelper extends FWHelper {
    public enum Prestations {
        ABSENCES_JUSTIFIEES(FAModuleFacturation.CS_MODULE_ABSENCES_JUSTIFIEES),
        CONGES_PAYES(FAModuleFacturation.CS_MODULE_CONGE_PAYE),
        SERVICES_MILITAIRE(FAModuleFacturation.CS_MODULE_SERVICE_MILITAIRE);

        private String codeSysteme;

        private Prestations(String codeSysteme) {
            this.codeSysteme = codeSysteme;
        }

        /**
         * Construit une instance valide de <code>Prestations</code> associée au code système passé en paramètre.
         * 
         * @param value un code système valide représentant un état de prestation (absence justifiée, congé payé, ...)
         * @return une instance valide de <code>Prestations</code>
         * @throws GlobazTechnicalException si le code système passé en paramètre n'est pas valide
         */
        public static Prestations fromValue(String value) {
            for (Prestations prestation : Prestations.values()) {
                if (prestation.codeSysteme.equals(value)) {
                    return prestation;
                }
            }
            throw new GlobazTechnicalException(ExceptionMessage.UNKNOWN_VALUE_CODE_SYSTEME);
        }
    }

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        BProcess process = null;
        try {
            PTPrestationsViewBean vb = (PTPrestationsViewBean) viewBean;
            String typePrestation = vb.getTypePrestation();
            String destinataire = vb.getDestinataire();
            if (Destinataire.INTERNE.equals(Destinataire.fromValue(destinataire))) {
                AbstractPrestationExcelProcess excelProcess = getExcelProcess(typePrestation);
                configureExcelProcess(excelProcess, vb);
                process = excelProcess;
            } else {
                DocumentPrestationsPrinter<?> documentPrinter = getDocumentProcess(typePrestation);
                configureDocumentProcess(documentPrinter, vb);
                process = documentPrinter;
            }

            process.setSendCompletionMail(true);
            process.setSendMailOnError(true);
            BProcessLauncher.start(process);
        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.toString());
        }
    }

    private <T extends PrestationParEmployeur> DocumentPrestationsPrinter<? extends PrestationParEmployeur> getDocumentProcess(
            String typePrestation) {
        switch (Prestations.fromValue(typePrestation)) {
            case ABSENCES_JUSTIFIEES:
                return new DocumentAJPrinter();
            case CONGES_PAYES:
                return new DocumentCPPrinter();
            case SERVICES_MILITAIRE:
                return new DocumentSMPrinter();
        }
        throw new AssertionError();
    }

    private AbstractPrestationExcelProcess getExcelProcess(String typePrestation) {
        switch (Prestations.fromValue(typePrestation)) {
            case ABSENCES_JUSTIFIEES:
                return new ListAJExcelProcess();
            case CONGES_PAYES:
                return new ListCPExcelProcess();
            case SERVICES_MILITAIRE:
                return new ListSMExcelProcess();
        }
        throw new AssertionError();
    }

    private void configureExcelProcess(AbstractPrestationExcelProcess process, PTPrestationsViewBean vb) {
        process.setEMailAddress(vb.getEmail());
        process.setIdTravailleur(vb.getIdTravailleur());
        process.setIdEmployeur(vb.getIdEmployeur());
        process.setIdConvention(vb.getIdConvention());
        process.setIdPassageFacturation(vb.getIdPassageFacturation());
        process.setPeriodeDebut(vb.getPeriodeDebut());
        process.setPeriodeFin(vb.getPeriodeFin());
    }

    private void configureDocumentProcess(DocumentPrestationsPrinter<?> process, PTPrestationsViewBean vb) {
        process.setEMailAddress(vb.getEmail());
        process.setIdTravailleur(vb.getIdTravailleur());
        process.setIdEmployeur(vb.getIdEmployeur());
        process.setIdConvention(vb.getIdConvention());
        process.setIdPassageFacturation(vb.getIdPassageFacturation());
        process.setPeriodeDebut(vb.getPeriodeDebut());
        process.setPeriodeFin(vb.getPeriodeFin());
    }
}
