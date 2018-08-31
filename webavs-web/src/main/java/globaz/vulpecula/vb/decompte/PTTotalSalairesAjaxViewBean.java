package globaz.vulpecula.vb.decompte;

import globaz.vulpecula.vb.PTAjaxDisplayViewBean;
import java.util.Collection;
import java.util.Map.Entry;
import org.apache.commons.lang.Validate;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.domain.repositories.decompte.DecompteRepository;
import ch.globaz.vulpecula.models.decompte.TableauContributions;
import ch.globaz.vulpecula.models.decompte.TableauContributions.EntreeContribution;
import ch.globaz.vulpecula.models.decompte.TableauContributions.TypeContribution;

public class PTTotalSalairesAjaxViewBean extends PTAjaxDisplayViewBean {
    private static final long serialVersionUID = 860802898439198369L;

    private String idDecompte;

    private TableauContributions tableauContributions;
    private transient Decompte decompte;

    private DecompteRepository decompteRepository = VulpeculaRepositoryLocator.getDecompteRepository();

    @Override
    public void retrieve() throws Exception {
        Validate.notEmpty(idDecompte);

        decompte = decompteRepository.findByIdWithDependencies(idDecompte);
        tableauContributions = new TableauContributions(decompte);
    }

    public String getTotalContribution() {
        return decompte.getMontantContributionTotal().getValue();
    }

    public Iterable<Entry<TypeContribution, Collection<EntreeContribution>>> getTableauContributions() {
        return tableauContributions.entrySet();
    }

    public void setIdDecompte(String idDecompte) {
        this.idDecompte = idDecompte;
    }
}
