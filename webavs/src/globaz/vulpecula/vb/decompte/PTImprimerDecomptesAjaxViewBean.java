package globaz.vulpecula.vb.decompte;

import java.util.List;
import ch.globaz.common.vb.JadeAbstractAjaxFindForDomain;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.domain.repositories.Repository;

public class PTImprimerDecomptesAjaxViewBean extends JadeAbstractAjaxFindForDomain<Decompte> {
    private static final long serialVersionUID = -2745463043855968910L;

    private String idDecompte;
    private String numeroDecompte;
    private String idPassage;
    private String etatsDecomptes;
    private String noAffilie;

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
        return VulpeculaRepositoryLocator.getDecompteRepository().findBy(idDecompte, numeroDecompte, noAffilie,
                idPassage, etatsDecomptes);

    }

    public void setIdPassage(String idPassage) {
        this.idPassage = idPassage;
    }

    public void setIdDecompte(String idDecompte) {
        this.idDecompte = idDecompte;
    }

    public void setNumeroDecompte(String numeroDecompte) {
        this.numeroDecompte = numeroDecompte;
    }

    public String getNoAffilie() {
        return noAffilie;
    }

    public void setNoAffilie(String noAffilie) {
        this.noAffilie = noAffilie;
    }

    /**
     * @param etatDecompte the etatDecompte to set
     */
    public void setEtatDecompte(String etatDecompte) {
        etatsDecomptes = etatDecompte;
    }
}
