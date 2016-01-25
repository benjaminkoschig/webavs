package ch.globaz.vulpecula.domain.specifications.caissemaladie;

import java.util.List;
import ch.globaz.specifications.AbstractSpecification;
import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.vulpecula.domain.models.caissemaladie.AffiliationCaisseMaladie;
import ch.globaz.vulpecula.domain.models.common.Periode;

public class CMPeriodeCaisseMaladieValideSpecification extends AbstractSpecification<AffiliationCaisseMaladie> {
    List<AffiliationCaisseMaladie> listCaissesExistantes;

    public CMPeriodeCaisseMaladieValideSpecification(List<AffiliationCaisseMaladie> caissesExistantes) {
        listCaissesExistantes = caissesExistantes;
    }

    @Override
    public boolean isValid(AffiliationCaisseMaladie affiliationCaisseMaladie) {
        for (AffiliationCaisseMaladie caisseExistante : listCaissesExistantes) {
            if (!caisseExistante.getId().equals(affiliationCaisseMaladie.getId())) {
                Periode periodeNouvelle = new Periode(affiliationCaisseMaladie.getMoisAnneeDebut(),
                        affiliationCaisseMaladie.getMoisAnneeFin());
                Periode periodeAncienne = new Periode(caisseExistante.getMoisAnneeDebut(),
                        caisseExistante.getMoisAnneeFin());
                if (periodeNouvelle.chevauche(periodeAncienne)) {
                    if (caisseExistante.getIdPosteTravail().equals(affiliationCaisseMaladie.getIdPosteTravail())) {
                        addMessage(SpecificationMessage.CAISSE_MALADIE_PERIODE_CHEVAUCHANTE);
                        break;
                    }
                }
            }
        }
        return true;
    }
}
