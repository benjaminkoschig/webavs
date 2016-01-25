package globaz.vulpecula.vb.syndicat;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.common.vb.JadeAbstractAjaxFindForDomain;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.business.services.decompte.DecompteSalaireService;
import ch.globaz.vulpecula.domain.models.syndicat.AffiliationSyndicat;
import ch.globaz.vulpecula.domain.repositories.Repository;

public class PTSyndicatAjaxViewBean extends JadeAbstractAjaxFindForDomain<AffiliationSyndicat> {
    private static final long serialVersionUID = -7781649323860594378L;

    private String idTravailleur;

    private DecompteSalaireService decompteSalaireService = VulpeculaServiceLocator.getDecompteSalaireService();

    public List<AffiliationSyndicat> getAffiliationsSyndicats() {
        List<AffiliationSyndicat> liste = new ArrayList<AffiliationSyndicat>();
        for (AffiliationSyndicat affiliationSyndicat : getList()) {
            affiliationSyndicat.setCumulSalaires(decompteSalaireService.cumulSalaire(idTravailleur, affiliationSyndicat
                    .getPeriode().getDateDebut(), affiliationSyndicat.getPeriode().getDateFin()));
            liste.add(affiliationSyndicat);
        }
        return liste;
    }

    @Override
    public AffiliationSyndicat getEntity() {
        return new AffiliationSyndicat();
    }

    @Override
    public Repository<AffiliationSyndicat> getRepository() {
        return VulpeculaRepositoryLocator.getAffiliationSyndicatRepository();
    }

    @Override
    public List<AffiliationSyndicat> findByRepository() {
        return VulpeculaRepositoryLocator.getAffiliationSyndicatRepository().findByIdTravailleur(idTravailleur);
    }

    public void setIdTravailleur(String idTravailleur) {
        this.idTravailleur = idTravailleur;
    }
}
