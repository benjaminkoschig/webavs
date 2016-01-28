package globaz.vulpecula.vb.decompte;

import java.util.List;
import ch.globaz.common.vb.JadeAbstractAjaxFindForDomain;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.domain.repositories.Repository;

public class PTDecompteForEmployeurAjaxViewBean extends JadeAbstractAjaxFindForDomain<Decompte> {
    private static final long serialVersionUID = 6945360872073577082L;

    private String idEmployeur;
    private String idDecompte;
    private String numeroDecompte;
    private String type;

    public List<Decompte> getDecomptes() {
        return getList();
    }

    public void setIdEmployeur(final String idEmployeur) {
        this.idEmployeur = idEmployeur;
    }

    public void setIdDecompte(String idDecompte) {
        this.idDecompte = idDecompte;
    }

    public void setNumeroDecompte(String numeroDecompte) {
        this.numeroDecompte = numeroDecompte;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public Decompte getEntity() {
        return new Decompte();
    }

    @Override
    public Repository<Decompte> getRepository() {
        return VulpeculaRepositoryLocator.getDecompteRepository();
    }

    @Override
    public List<Decompte> findByRepository() {
        return VulpeculaRepositoryLocator.getDecompteRepository().findWithDependencies(idEmployeur, idDecompte,
                numeroDecompte, type);
    }

}
