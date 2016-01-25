package globaz.vulpecula.vb.postetravail;

import java.util.List;
import ch.globaz.common.vb.JadeAbstractAjaxFindRawSQLForDomain;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.external.models.musca.Passage;

public class PTPrestationsAjaxViewBean extends JadeAbstractAjaxFindRawSQLForDomain<Passage> {
    private static final long serialVersionUID = 4088770521970071176L;

    private String idEmployeur;

    public String getIdEmployeur() {
        return idEmployeur;
    }

    public void setIdEmployeur(String idEmployeur) {
        this.idEmployeur = idEmployeur;
    }

    public List<Passage> getPassages() {
        return getList();
    }

    @Override
    public Passage getEntity() {
        return new Passage();
    }

    @Override
    public List<Passage> findBySQL() {
        return VulpeculaServiceLocator.getPassageService().findByIdEmployeur(idEmployeur, getOffset(), getSize());
    }

    @Override
    public int nbOfResultMathingQuery() {
        return VulpeculaServiceLocator.getPassageService().countByIdEmployeur(idEmployeur);
    }
}
