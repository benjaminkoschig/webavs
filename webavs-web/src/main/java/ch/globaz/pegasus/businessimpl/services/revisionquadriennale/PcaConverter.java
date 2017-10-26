package ch.globaz.pegasus.businessimpl.services.revisionquadriennale;

import ch.globaz.common.domaine.Date;
import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.membreFamille.RoleMembreFamille;
import ch.globaz.pegasus.business.domaine.pca.Pca;
import ch.globaz.pegasus.business.domaine.pca.PcaEtat;
import ch.globaz.pegasus.business.domaine.pca.PcaEtatCalcul;
import ch.globaz.pegasus.business.domaine.pca.PcaGenre;
import ch.globaz.pegasus.business.domaine.pca.PcaType;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePCAccordee;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePlanDeCalcul;

public class PcaConverter {
    public Pca convert(SimplePCAccordee simplePCAccordee) {
        return this.convert(simplePCAccordee, null, null);
    }

    public Pca convert(SimplePCAccordee simplePCAccordee, SimplePlanDeCalcul simplePlanDeCalcul, String sousCode) {
        try {
            Pca pca = new Pca();
            pca.setDateDebut(new Date(simplePCAccordee.getDateDebut()));
            if (simplePCAccordee.getDateFin() != null && !simplePCAccordee.getDateFin().trim().isEmpty()) {
                pca.setDateFin(new Date(simplePCAccordee.getDateFin()));
            }
            pca.setEtat(PcaEtat.fromValue(simplePCAccordee.getCsEtatPC()));
            pca.setGenre(PcaGenre.fromValue(simplePCAccordee.getCsGenrePC()));
            pca.setHasCalculComparatif(simplePCAccordee.getHasCalculComparatif());
            pca.setHasJoursAppoint(simplePCAccordee.getHasJoursAppoint());
            pca.setId(simplePCAccordee.getId());
            pca.setIsCalculManuel(simplePCAccordee.getIsCalculManuel());
            pca.setIsCalculRetro(simplePCAccordee.getIsCalculRetro());
            pca.setIsSupprime(simplePCAccordee.getIsSupprime());
            pca.setSousCode(sousCode);
            pca.setIdVersionDroit(simplePCAccordee.getIdVersionDroit());

            if (simplePlanDeCalcul != null) {
                if (!simplePlanDeCalcul.getMontantPCMensuelle().trim().isEmpty()) {
                    pca.setMontant(Montant.newMensuel(simplePlanDeCalcul.getMontantPCMensuelle()));
                }
                pca.setEtatCalcul(PcaEtatCalcul.fromValue(simplePlanDeCalcul.getEtatPC()));
            }

            pca.setRoleBeneficiaire(RoleMembreFamille.fromValue(simplePCAccordee.getCsRoleBeneficiaire()));
            pca.setType(PcaType.fromValue(simplePCAccordee.getCsTypePC()));
            // TODO PRestation
            if (simplePCAccordee.getIdPrestationAccordeeConjoint() != null
                    && !simplePCAccordee.getIdPrestationAccordeeConjoint().trim().isEmpty()
                    && !simplePCAccordee.getIdPrestationAccordeeConjoint().trim().equals("0")) {
                pca.getBeneficiaireConjointDom2R().setId(
                        Long.valueOf(simplePCAccordee.getIdPrestationAccordeeConjoint()));
            }
            return pca;
        } catch (Exception e) {
            throw new RuntimeException("Imposilbe de convertire la pca suivante: id = " + simplePCAccordee.getId(), e);
        }

    }
}
