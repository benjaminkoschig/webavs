package ch.globaz.vulpecula.process.entreprises;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.publish.document.JadePublishDocumentInfoProvider;
import java.util.List;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.process.prestations.AbstractPrestationExcelProcess;

public class ListEntreprisesExcelProcess extends AbstractPrestationExcelProcess {
    private static final long serialVersionUID = -1292989805297661709L;

    @Override
    protected boolean _executeProcess() throws Exception {
        super._executeProcess();
        ListEntreprisesExcel listEntreprisesExcel = new ListEntreprisesExcel(getSession(),
                DocumentConstants.LISTES_ENTREPRISES_NAME, DocumentConstants.LISTES_ENTREPRISES_DOC_NAME);

        listEntreprisesExcel.setEntreprises(retrieve());

        if (getConvention() != null) {
            listEntreprisesExcel.setConvention(getConvention());
        }
        listEntreprisesExcel.create();
        registerAttachedDocument(JadePublishDocumentInfoProvider.newInstance(this),
                listEntreprisesExcel.getOutputFile());
        return false;
    }

    @Override
    protected String getEMailObject() {
        return getSession().getLabel("LISTE_ENTREPRISES");
    }

    public List<Employeur> retrieve() throws JadePersistenceException {
        List<Employeur> listEmployeurs = VulpeculaServiceLocator.getEmployeurService()
                .findByIdConventionNonRadieWithParticulariteSansPersonnel(idConvention);
        return listEmployeurs;
    }
}
