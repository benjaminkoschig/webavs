package ch.globaz.vulpecula.process.prestations;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfoProvider;
import java.util.List;
import ch.globaz.vulpecula.business.models.absencejustifiee.AbsenceJustifieeSearchComplexModel;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.absencejustifiee.AbsenceJustifiee;
import ch.globaz.vulpecula.domain.models.absencejustifiee.AbsencesJustifiees;
import ch.globaz.vulpecula.domain.models.common.Periode;

public class ListAJExcelProcess extends AbstractPrestationExcelProcess {
    private static final long serialVersionUID = 4673022589446026422L;

    @Override
    protected boolean _executeProcess() throws Exception {
        super._executeProcess();
        ListAJExcel list = new ListAJExcel(getSession(), DocumentConstants.LISTES_AJ_NAME,
                DocumentConstants.LISTES_AJ_DOC_NAME);
        List<AbsenceJustifiee> absences = retrieve();
        list.setAbsencesJustifiees(AbsencesJustifiees.groupByConventionAndByType(absences));
        if (!JadeStringUtil.isBlankOrZero(idPassageFacturation)) {
            list.setIdPassageFacturation(idPassageFacturation);
        }
        if (getEmployeur() != null) {
            list.setEmployeur(getEmployeur());
        }
        if (getTravailleur() != null) {
            list.setTravailleur(getTravailleur());
        }
        if (getConvention() != null) {
            list.setConvention(getConvention());
        }
        list.setPeriodeDebut(periodeDebut);
        list.setPeriodeFin(periodeFin);
        list.create();
        registerAttachedDocument(JadePublishDocumentInfoProvider.newInstance(this), list.getOutputFile());
        return false;
    }

    @Override
    protected String getEMailObject() {
        return getSession().getLabel("LISTE_AJ");
    }

    public List<AbsenceJustifiee> retrieve() {
        if (!JadeStringUtil.isEmpty(periodeDebut)) {
            return VulpeculaRepositoryLocator.getAbsenceJustifieeRepository().findBy(idPassageFacturation, idEmployeur,
                    idTravailleur, idConvention, new Periode(periodeDebut, periodeFin),
                    AbsenceJustifieeSearchComplexModel.ORDER_BY_CONVENTION_ASC);
        } else {
            return VulpeculaRepositoryLocator.getAbsenceJustifieeRepository().findBy(idPassageFacturation, idEmployeur,
                    idTravailleur, idConvention, AbsenceJustifieeSearchComplexModel.ORDER_BY_CONVENTION_ASC);
        }
    }
}
