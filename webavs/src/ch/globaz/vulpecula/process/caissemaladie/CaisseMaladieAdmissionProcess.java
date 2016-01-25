package ch.globaz.vulpecula.process.caissemaladie;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfoProvider;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.caissemaladie.AffiliationCaisseMaladie;

public class CaisseMaladieAdmissionProcess extends CaisseMaladieProcess {
    private static final long serialVersionUID = 1938382574142921185L;

    @Override
    protected void retrieve() {
        if (!JadeStringUtil.isEmpty(idCaisseMaladie)) {
            caisseMaladie = VulpeculaRepositoryLocator.getAdministrationRepository().findById(idCaisseMaladie);
        }
        affiliationsCaissesMaladies = affiliationCaisseMaladieRepository
                .findByMoisDebutBeforeDateForCaisseMaladieWhenDateDebutAnnonceIsZero(caisseMaladie, dateAnnonce);
        affiliationsGroupByCaisseMaladie = AffiliationCaisseMaladie.groupByCaisseMaladie(affiliationsCaissesMaladies);
    }

    @Override
    protected void print() throws Exception {
        CaisseMaladieAdmissionExcel caisseMaladieExcel = new CaisseMaladieAdmissionExcel(getSession(),
                DocumentConstants.LISTES_CAISSES_MALADIES_ADMISSION_DOC_NAME,
                DocumentConstants.LISTES_CAISSES_MALADIES_ADMISSION_NAME);
        caisseMaladieExcel.setAffiliationsCaisseMaladie(affiliationsGroupByCaisseMaladie);
        caisseMaladieExcel.create();
        registerAttachedDocument(JadePublishDocumentInfoProvider.newInstance(this), caisseMaladieExcel.getOutputFile());
    }

    @Override
    protected String getEMailObject() {
        return DocumentConstants.LISTES_CAISSES_MALADIES_ADMISSION_NAME;
    }
}
