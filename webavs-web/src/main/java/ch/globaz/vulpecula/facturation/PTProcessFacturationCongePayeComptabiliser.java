package ch.globaz.vulpecula.facturation;

import java.util.List;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.business.services.compteur.CompteurService;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.congepaye.Compteurs;
import ch.globaz.vulpecula.domain.models.congepaye.CongePaye;
import ch.globaz.vulpecula.domain.models.prestations.Beneficiaire;
import ch.globaz.vulpecula.domain.models.prestations.Etat;

public class PTProcessFacturationCongePayeComptabiliser extends PTProcessFacturation {
    private static final long serialVersionUID = 1L;

    @Override
    protected boolean launch() {
        List<CongePaye> congePayes = VulpeculaRepositoryLocator.getCongePayeRepository().findForFacturation(
                getPassage().getIdPassage());
        for (CongePaye congePaye : congePayes) {
            CompteurService compteurService = VulpeculaServiceLocator.getCompteurService();
            Compteurs compteurs = compteurService.findCompteursByIdAndCreateIfNecessary(congePaye.getIdPosteTravail(),
                    congePaye.getAnneeDebut(), congePaye.getAnneeFin());
            compteurs.add(congePaye);
            majEtatCongePaye(congePaye);
            updateCompteurs(compteurs);
        }
        return true;
    }

    private void majEtatCongePaye(CongePaye congePaye) {
        congePaye.setEtat(Etat.COMPTABILISEE);

        if (Beneficiaire.NOTE_CREDIT.equals(congePaye.getBeneficiaire()) || congePaye.getMontant().isNegative()) {
            congePaye.setDateVersement(new Date(getPassage().getDateFacturation()));
        }

        VulpeculaRepositoryLocator.getCongePayeRepository().update(congePaye);
    }

    private void updateCompteurs(Compteurs compteurs) {
        VulpeculaServiceLocator.getCompteurService().update(compteurs);
    }

    @Override
    protected void clean() {
    }
}
