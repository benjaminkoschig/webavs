package ch.globaz.vulpecula.documents.prestations;

import java.util.List;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.domain.models.servicemilitaire.SMParEmployeur;
import ch.globaz.vulpecula.domain.models.servicemilitaire.ServiceMilitaire;
import ch.globaz.vulpecula.domain.models.servicemilitaire.ServicesMilitaires;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.jade.client.util.JadeStringUtil;

public class DocumentSMPrinter extends DocumentPrestationsPrinter<SMParEmployeur> {
    private static final long serialVersionUID = 3721912632239314835L;

    public DocumentSMPrinter() {
        super();
    }

    @Override
    public FWIDocumentManager createDocument() throws Exception {
        return new DocumentSM(getCurrentElement());
    }

    @Override
    protected String getEMailObject() {
        return DocumentConstants.SERVICE_MILITAIRE_NAME;
    }

    @Override
    public String getNumeroInforom() {
        return DocumentConstants.SERVICE_MILITAIRE_TYPE_NUMBER;
    }

    @Override
    public void retrieve() {
        List<ServiceMilitaire> serviceMilitaires;
        if (!JadeStringUtil.isEmpty(periodeDebut)) {
            serviceMilitaires = VulpeculaRepositoryLocator.getServiceMilitaireRepository().findBy(idPassageFacturation,
                    idEmployeur, idTravailleur, idConvention, new Periode(periodeDebut, periodeFin));
        } else {
            serviceMilitaires = VulpeculaRepositoryLocator.getServiceMilitaireRepository().findBy(idPassageFacturation,
                    idEmployeur, idTravailleur, idConvention);
        }
        List<SMParEmployeur> sms = ServicesMilitaires.groupByEmployeur(serviceMilitaires);
        for (SMParEmployeur sm : sms) {
            Employeur employeur = sm.getEmployeur();
            employeur.setAdressePrincipale(VulpeculaRepositoryLocator.getAdresseRepository()
                    .findAdressePrioriteCourrierByIdTiers(employeur.getIdTiers()));
        }
        setElements(sms);
    }
}
