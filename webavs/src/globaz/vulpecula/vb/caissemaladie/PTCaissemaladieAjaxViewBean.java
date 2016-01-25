package globaz.vulpecula.vb.caissemaladie;

import java.util.Iterator;
import java.util.List;
import ch.globaz.common.vb.JadeAbstractAjaxFindForDomain;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.domain.models.caissemaladie.AffiliationCaisseMaladie;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.domain.repositories.Repository;

public class PTCaissemaladieAjaxViewBean extends JadeAbstractAjaxFindForDomain<AffiliationCaisseMaladie> {
    private static final long serialVersionUID = 241464031891926649L;

    private String idTravailleur;

    public String getIdTravailleur() {
        return idTravailleur;
    }

    public void setIdTravailleur(String idTravailleur) {
        this.idTravailleur = idTravailleur;
    }

    public List<AffiliationCaisseMaladie> getAffiliationsCaissesMaladies() {
        List<AffiliationCaisseMaladie> liste = getList();
        for (Iterator iterator = liste.iterator(); iterator.hasNext();) {
            AffiliationCaisseMaladie affiliationCaisseMaladie = (AffiliationCaisseMaladie) iterator.next();
            PosteTravail poste = VulpeculaRepositoryLocator.getPosteTravailRepository()
                    .findByIdPosteTravailWithDependencies(affiliationCaisseMaladie.getIdPosteTravail());
            affiliationCaisseMaladie.setDescriptionEmployeur(poste.getEmployeur().getRaisonSociale());
        }
        return getList();
    }

    @Override
    public AffiliationCaisseMaladie getEntity() {
        return new AffiliationCaisseMaladie();
    }

    @Override
    public Repository<AffiliationCaisseMaladie> getRepository() {
        return VulpeculaRepositoryLocator.getAffiliationCaisseMaladieRepository();
    }

    @Override
    public List<AffiliationCaisseMaladie> findByRepository() {
        return VulpeculaRepositoryLocator.getAffiliationCaisseMaladieRepository().findByIdTravailleur(idTravailleur);
    }
}
