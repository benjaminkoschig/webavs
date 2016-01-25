package ch.globaz.vulpecula.process.prestations;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfoProvider;
import java.util.List;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.congepaye.CongePaye;
import ch.globaz.vulpecula.domain.models.congepaye.CongesPayes;

public class ListCPExcelProcess extends AbstractPrestationExcelProcess {
    private static final long serialVersionUID = -1292989805297661709L;

    @Override
    protected boolean _executeProcess() throws Exception {
        super._executeProcess();
        ListCPExcel listCPExcel = new ListCPExcel(getSession(), DocumentConstants.LISTES_CP_NAME,
                DocumentConstants.LISTES_CP_DOC_NAME);
        listCPExcel.setCongesPayesParConvention(CongesPayes.groupByConvention(retrieve()));
        if (!JadeStringUtil.isBlankOrZero(idPassageFacturation)) {
            listCPExcel.setIdPassageFacturation(idPassageFacturation);
        }
        if (getEmployeur() != null) {
            listCPExcel.setEmployeur(getEmployeur());
        }
        if (getTravailleur() != null) {
            listCPExcel.setTravailleur(getTravailleur());
        }
        if (getConvention() != null) {
            listCPExcel.setConvention(getConvention());
        }
        listCPExcel.setPeriodeDebut(periodeDebut);
        listCPExcel.setPeriodeFin(periodeFin);
        listCPExcel.create();
        registerAttachedDocument(JadePublishDocumentInfoProvider.newInstance(this), listCPExcel.getOutputFile());
        return false;
    }

    @Override
    protected String getEMailObject() {
        return getSession().getLabel("LISTE_CP");
    }

    public List<CongePaye> retrieve() {
        if (!JadeStringUtil.isEmpty(periodeDebut)) {
            return VulpeculaRepositoryLocator.getCongePayeRepository().findBy(idPassageFacturation, idEmployeur,
                    idTravailleur, idConvention, new Periode(periodeDebut, periodeFin));
        } else {
            return VulpeculaRepositoryLocator.getCongePayeRepository().findBy(idPassageFacturation, idEmployeur,
                    idTravailleur, idConvention);
        }
    }
}
