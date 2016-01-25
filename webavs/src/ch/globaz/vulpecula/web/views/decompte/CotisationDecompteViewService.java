package ch.globaz.vulpecula.web.views.decompte;

import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.business.services.decompte.DecompteSalaireService;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.decompte.CotisationDecompte;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;
import ch.globaz.vulpecula.util.I18NUtil;

public class CotisationDecompteViewService {
    private final DecompteSalaireService decompteSalaireService = VulpeculaServiceLocator.getDecompteSalaireService();

    public CotisationDecompteContainer getCotisationDecompteViewForReaffichage(String idDecompteSalaire)
            throws Exception {
        DecompteSalaire decompteSalaire = decompteSalaireService.findById(idDecompteSalaire);
        return createContainer(decompteSalaire);
    }

    public CotisationDecompteContainer getCotisationDecompteView(final String idDecompteSalaire,
            final Montant masseSalariale, final Montant masseAC2, final Montant masseFranchise) throws Exception {
        DecompteSalaire decompteSalaire = decompteSalaireService.findByIdForSimulation(idDecompteSalaire, true,
                masseSalariale, masseAC2, masseFranchise);
        decompteSalaire.setSalaireTotal(masseSalariale);
        return createContainer(decompteSalaire);
    }

    private CotisationDecompteContainer createContainer(DecompteSalaire decompteSalaire) throws Exception {
        Montant masseSalariale = decompteSalaire.getSalaireTotal();
        CotisationDecompteContainer cotisationDecompteContainer = new CotisationDecompteContainer();

        for (CotisationDecompte cotisation : decompteSalaire.getCotisationsDecompte()) {
            Taux taux = cotisation.getTaux();

            String planCaisseLibelle = cotisation.getPlanCaisseLibelle();
            String assuranceLibelle = cotisation.getAssuranceLibelle(I18NUtil.getUserLocale());
            String tauxValue = taux.getValue();
            Montant franchise = Montant.ZERO;

            Montant masseSalarialeEffective = cotisation.getMasse(masseSalariale);
            if (!cotisation.isAssuranceAC2() && !cotisation.isAssuranceAC()) {
                franchise = masseSalariale.substract(masseSalarialeEffective);
            }

            CotisationDecompteView cotisationDecompteView = new CotisationDecompteView(cotisation.getId(),
                    planCaisseLibelle, assuranceLibelle, masseSalarialeEffective.getValue(), tauxValue,
                    masseSalarialeEffective.multiply(taux).getValue(), franchise.getValue());
            cotisationDecompteContainer.addCotisationDecompteView(cotisationDecompteView);
        }
        return cotisationDecompteContainer;
    }
}
