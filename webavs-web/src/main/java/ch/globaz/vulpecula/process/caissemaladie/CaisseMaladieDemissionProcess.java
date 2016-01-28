package ch.globaz.vulpecula.process.caissemaladie;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfoProvider;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.caissemaladie.AffiliationCaisseMaladie;

public class CaisseMaladieDemissionProcess extends CaisseMaladieProcess {
    private static final long serialVersionUID = -909824728058647401L;

    @Override
    protected String getEMailObject() {
        return DocumentConstants.LISTES_CAISSES_MALADIES_DEMISSION_NAME;
    }

    @Override
    protected void retrieve() {
        if (!JadeStringUtil.isEmpty(idCaisseMaladie)) {
            caisseMaladie = VulpeculaRepositoryLocator.getAdministrationRepository().findById(idCaisseMaladie);
        }
        affiliationsCaissesMaladies = VulpeculaRepositoryLocator.getAffiliationCaisseMaladieRepository()
                .findByMoisFinBeforeDateForCaisseMaladieWhenDateFinAnnonceIsZero(caisseMaladie, dateAnnonce);
        affiliationsGroupByCaisseMaladie = AffiliationCaisseMaladie.groupByCaisseMaladie(affiliationsCaissesMaladies);
    }

    @Override
    protected void print() throws Exception {
        CaisseMaladieAdmissionExcel caisseMaladieExcel = new CaisseMaladieAdmissionExcel(getSession(),
                DocumentConstants.LISTES_CAISSES_MALADIES_DEMISSION_DOC_NAME,
                DocumentConstants.LISTES_CAISSES_MALADIES_DEMISSION_NAME);
        caisseMaladieExcel.setAffiliationsCaisseMaladie(affiliationsGroupByCaisseMaladie);
        caisseMaladieExcel.create();
        registerAttachedDocument(JadePublishDocumentInfoProvider.newInstance(this), caisseMaladieExcel.getOutputFile());
    }

}
