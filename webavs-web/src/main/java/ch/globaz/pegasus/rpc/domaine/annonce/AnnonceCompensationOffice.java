package ch.globaz.pegasus.rpc.domaine.annonce;

import ch.globaz.pegasus.business.domaine.membreFamille.RoleMembreFamille;
import ch.globaz.pegasus.rpc.domaine.PersonElementsCalcul;

public class AnnonceCompensationOffice {

    public static final int DEFAULT_COMPENSATION_OFFICE = 999;

    protected int compensationOffice;
    protected Integer compensationAgency;

    public AnnonceCompensationOffice(PersonElementsCalcul personData, PersonElementsCalcul requerantData) {
        if (personData.getNumeroOffice() == null) {
            if (personData.getMembreFamille().getRoleMembreFamille() == RoleMembreFamille.REQUERANT) {
                compensationOffice = DEFAULT_COMPENSATION_OFFICE;
            } else if (personData.getMembreFamille().getRoleMembreFamille() == RoleMembreFamille.ENFANT) {
                setOfficeAndAgencyFromRequerant(this, requerantData);
            } else {
                // TODO: to confirm if any other role can heritate from requerant too (i.e. conjoint)
                // Plausi PR-029 and PR-030 affected
                compensationOffice = DEFAULT_COMPENSATION_OFFICE;
            }
        } else {
            compensationOffice = personData.getNumeroOffice();
            if (personData.hasCompensationAgency()) {
                if (personData.getNumeroAgence() == null) {
                    compensationAgency = 0;
                } else {
                    compensationAgency = personData.getNumeroAgence();
                }
            }
        }
    }

    private AnnonceCompensationOffice setOfficeAndAgencyFromRequerant(AnnonceCompensationOffice co,
            PersonElementsCalcul requerantData) {
        if (requerantData != null) {
            if (requerantData.getNumeroOffice() == null) {
                co.setCompensationOffice(DEFAULT_COMPENSATION_OFFICE);
            } else {
                co.setCompensationOffice(requerantData.getNumeroOffice());
                if (requerantData.getNumeroAgence() == null) {
                    co.setCompensationAgency(0);
                } else {
                    co.setCompensationAgency(requerantData.getNumeroAgence());
                }
            }
        } else {
            co.setCompensationOffice(DEFAULT_COMPENSATION_OFFICE);
        }
        return co;
    }

    public int getCompensationOffice() {
        return compensationOffice;
    }

    public Integer getCompensationAgency() {
        return compensationAgency;
    }

    public void setCompensationOffice(int compensationOffice) {
        this.compensationOffice = compensationOffice;
    }

    public void setCompensationAgency(Integer compensationAgency) {
        this.compensationAgency = compensationAgency;
    }

}
