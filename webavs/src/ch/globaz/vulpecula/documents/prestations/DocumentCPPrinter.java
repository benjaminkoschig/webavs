package ch.globaz.vulpecula.documents.prestations;

import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.jade.client.util.JadeStringUtil;
import java.util.Collections;
import java.util.List;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.congepaye.CPParEmployeur;
import ch.globaz.vulpecula.domain.models.congepaye.CongePaye;
import ch.globaz.vulpecula.domain.models.congepaye.CongesPayes;
import ch.globaz.vulpecula.domain.models.parametrage.TableParametrage;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.domain.models.prestations.PrestationByNameComparator;

public class DocumentCPPrinter extends DocumentPrestationsPrinter<CPParEmployeur> {
    private static final long serialVersionUID = 3721912632239314835L;

    public DocumentCPPrinter() {
        super();
    }

    @Override
    public FWIDocumentManager createDocument() throws Exception {
        CPParEmployeur cpParEmployeur = getCurrentElement();
        boolean hasCotisationsCongesPays = TableParametrage.getInstance().hasCotisationsCongesPays(
                cpParEmployeur.getEmployeur().getCaisseMetier().getCodeAdministrationPlanCaisse());
        if (hasCotisationsCongesPays) {
            return new DocumentCPElec(getCurrentElement());
        } else {
            return new DocumentCP(getCurrentElement());
        }
    }

    @Override
    protected String getEMailObject() {
        return DocumentConstants.CONGES_PAYES_NAME;
    }

    @Override
    public String getNumeroInforom() {
        return DocumentConstants.CONGES_PAYES_TYPE_NUMBER;
    }

    @Override
    public void retrieve() {
        List<CongePaye> congesPayes;
        if (!JadeStringUtil.isEmpty(periodeDebut)) {
            congesPayes = VulpeculaRepositoryLocator.getCongePayeRepository().findBy(idPassageFacturation, idEmployeur,
                    idTravailleur, idConvention, new Periode(periodeDebut, periodeFin));
        } else {
            congesPayes = VulpeculaRepositoryLocator.getCongePayeRepository().findBy(idPassageFacturation, idEmployeur,
                    idTravailleur, idConvention);
        }
        Collections.sort(congesPayes, new PrestationByNameComparator());
        List<CPParEmployeur> cps = CongesPayes.groupByEmployeur(congesPayes);
        for (CPParEmployeur cp : cps) {
            Employeur employeur = cp.getEmployeur();
            employeur.setAdressePrincipale(VulpeculaRepositoryLocator.getAdresseRepository()
                    .findAdresseDomicileByIdTiers(employeur.getIdTiers()));
            employeur.setCaisseMetier(VulpeculaRepositoryLocator.getAdhesionRepository().findCaisseMetier(
                    employeur.getId()));
        }
        setElements(cps);
    }
}
