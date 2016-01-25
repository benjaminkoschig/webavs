package ch.globaz.vulpecula.process.prestations;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfoProvider;
import java.util.Collection;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.servicemilitaire.ServiceMilitaire;
import ch.globaz.vulpecula.domain.models.servicemilitaire.ServicesMilitaires;

public class ListSMExcelProcess extends AbstractPrestationExcelProcess {
    private static final long serialVersionUID = -1292989805297661709L;

    @Override
    protected boolean _executeProcess() throws Exception {
        super._executeProcess();
        ListSMExcel listSMExcel = new ListSMExcel(getSession(), DocumentConstants.LISTES_SM_NAME,
                DocumentConstants.LISTES_SM_DOC_NAME);
        listSMExcel.setServicesMilitairesParConvention(ServicesMilitaires.groupByGenreSMByConvention(retrieve()));
        if (!JadeStringUtil.isBlankOrZero(idPassageFacturation)) {
            listSMExcel.setIdPassageFacturation(idPassageFacturation);
        }
        if (getEmployeur() != null) {
            listSMExcel.setEmployeur(getEmployeur());
        }
        if (getTravailleur() != null) {
            listSMExcel.setTravailleur(getTravailleur());
        }
        if (getConvention() != null) {
            listSMExcel.setConvention(getConvention());
        }
        listSMExcel.setPeriodeDebut(periodeDebut);
        listSMExcel.setPeriodeFin(periodeFin);
        listSMExcel.create();
        registerAttachedDocument(JadePublishDocumentInfoProvider.newInstance(this), listSMExcel.getOutputFile());
        return false;
    }

    @Override
    protected String getEMailObject() {
        return getSession().getLabel("LISTE_SM");
    }

    public Collection<ServiceMilitaire> retrieve() {
        if (!JadeStringUtil.isEmpty(periodeDebut)) {
            return VulpeculaRepositoryLocator.getServiceMilitaireRepository().findBy(idPassageFacturation, idEmployeur,
                    idTravailleur, idConvention, new Periode(periodeDebut, periodeFin));
        } else {
            return VulpeculaRepositoryLocator.getServiceMilitaireRepository().findBy(idPassageFacturation, idEmployeur,
                    idTravailleur, idConvention);
        }
    }
}
