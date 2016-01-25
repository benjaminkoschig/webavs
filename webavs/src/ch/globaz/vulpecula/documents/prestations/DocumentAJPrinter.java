package ch.globaz.vulpecula.documents.prestations;

import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.jade.client.util.JadeStringUtil;
import java.util.Collections;
import java.util.List;
import ch.globaz.vulpecula.business.models.absencejustifiee.AbsenceJustifieeSearchComplexModel;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.absencejustifiee.AJParEmployeur;
import ch.globaz.vulpecula.domain.models.absencejustifiee.AbsenceJustifiee;
import ch.globaz.vulpecula.domain.models.absencejustifiee.AbsencesJustifiees;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.domain.models.prestations.PrestationByNameComparator;

public class DocumentAJPrinter extends DocumentPrestationsPrinter<AJParEmployeur> {
    private static final long serialVersionUID = 3721912632239314835L;

    public DocumentAJPrinter() {
        super();
    }

    @Override
    public FWIDocumentManager createDocument() throws Exception {
        return new DocumentAJ(getCurrentElement());
    }

    @Override
    protected String getEMailObject() {
        return DocumentConstants.ABSENCES_JUSTIFIEES_DOC_NAME;
    }

    @Override
    public String getNumeroInforom() {
        return DocumentConstants.ABSENCES_JUSTIFIEES_TYPE_NUMBER;
    }

    @Override
    public void retrieve() {
        List<AbsenceJustifiee> absences;
        if (!JadeStringUtil.isEmpty(periodeDebut)) {
            absences = VulpeculaRepositoryLocator.getAbsenceJustifieeRepository().findBy(idPassageFacturation,
                    idEmployeur, idTravailleur, idConvention, new Periode(periodeDebut, periodeFin),
                    AbsenceJustifieeSearchComplexModel.ORDER_BY_CONVENTION_ASC);
        } else {
            absences = VulpeculaRepositoryLocator.getAbsenceJustifieeRepository().findBy(idPassageFacturation,
                    idEmployeur, idTravailleur, idConvention,
                    AbsenceJustifieeSearchComplexModel.ORDER_BY_CONVENTION_ASC);
        }
        Collections.sort(absences, new PrestationByNameComparator());
        List<AJParEmployeur> ajs = AbsencesJustifiees.groupByEmployeur(absences);
        for (AJParEmployeur aj : ajs) {
            Employeur employeur = aj.getEmployeur();
            employeur.setAdressePrincipale(VulpeculaRepositoryLocator.getAdresseRepository()
                    .findAdresseDomicileByIdTiers(employeur.getIdTiers()));
        }
        setElements(ajs);
    }
}
