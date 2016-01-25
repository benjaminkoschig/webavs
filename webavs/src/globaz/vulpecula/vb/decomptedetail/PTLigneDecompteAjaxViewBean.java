package globaz.vulpecula.vb.decomptedetail;

import java.util.List;
import ch.globaz.common.vb.JadeAbstractAjaxFindForDomain;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;
import ch.globaz.vulpecula.domain.repositories.Repository;

/**
 * @author Arnaud Geiser (AGE) | Créé le 28 mars 2014
 * 
 */
public class PTLigneDecompteAjaxViewBean extends JadeAbstractAjaxFindForDomain<DecompteSalaire> {
    private static final long serialVersionUID = 6075910866261392089L;
    
    private String idDecompte;
    private String idPosteTravail;
    private String nomTravailleur;

    private DecompteSalaire entity = new DecompteSalaire();

    public PTLigneDecompteAjaxViewBean() {
        entity = new DecompteSalaire();
    }

    public void setIdLigneDecompte(final String idLigneDecompte) {
        entity.setId(idLigneDecompte);
    }

    public List<DecompteSalaire> getLignesDecompte() {
        return getList();
    }

    public void setIdDecompte(final String idDecompte) {
        this.idDecompte = idDecompte;
    }

    @Override
    public List<DecompteSalaire> findByRepository() {
        List<DecompteSalaire> lignesDecompte = VulpeculaRepositoryLocator.getDecompteSalaireRepository()
                .findWithDependencies(idDecompte, idPosteTravail, nomTravailleur);
        return lignesDecompte;
    }

    @Override
    public DecompteSalaire getEntity() {
        if (entity == null) {
            entity = new DecompteSalaire();
        }
        return entity;
    }

    @Override
    public Repository<DecompteSalaire> getRepository() {
        return VulpeculaRepositoryLocator.getDecompteSalaireRepository();
    }

    /**
     * @return the idDecompte
     */
    public String getIdDecompte() {
        return idDecompte;
    }

    public final void setIdPosteTravail(String idPosteTravail) {
        this.idPosteTravail = idPosteTravail;
    }

    public void setNomTravailleur(String nomTravailleur) {
        this.nomTravailleur = nomTravailleur;
    }
}
