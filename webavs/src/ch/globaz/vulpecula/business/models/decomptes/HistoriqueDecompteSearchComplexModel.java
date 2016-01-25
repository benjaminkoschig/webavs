package ch.globaz.vulpecula.business.models.decomptes;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import ch.globaz.vulpecula.domain.models.decompte.EtatDecompte;

public class HistoriqueDecompteSearchComplexModel extends JadeSearchComplexModel {
    private static final long serialVersionUID = -7800501330241004532L;

    private String forId;
    private String forIdDecompte;
    private Collection<String> forIdDecompteIn;
    private Collection<String> forEtatsIn;
    private String forEtat;

    public String getForId() {
        return forId;
    }

    public void setForId(final String forId) {
        this.forId = forId;
    }

    public String getForIdDecompte() {
        return forIdDecompte;
    }

    public void setForIdDecompte(final String forIdDecompte) {
        this.forIdDecompte = forIdDecompte;
    }

    public void setForIdDecompteIn(final List<String> ids) {
        forIdDecompteIn = ids;
    }

    public Collection<String> getForIdDecompteIn() {
        return forIdDecompteIn;
    }

    public void setForIdDecompteIn(final Collection<String> forIdDecompteIn) {
        this.forIdDecompteIn = forIdDecompteIn;
    }

    public String getForEtat() {
        return forEtat;
    }

    public void setForEtat(String forEtat) {
        this.forEtat = forEtat;
    }

    public Collection<String> getForEtatsIn() {
        return forEtatsIn;
    }

    public void setForEtats(Collection<EtatDecompte> forEtatsIn) {
        List<String> etats = new ArrayList<String>();
        for (EtatDecompte etat : forEtatsIn) {
            etats.add(etat.getValue());
        }
        setForEtatsIn(etats);
    }

    public void setForEtatsIn(Collection<String> forEtatsIn) {
        this.forEtatsIn = forEtatsIn;
    }

    @Override
    public Class<HistoriqueDecompteComplexModel> whichModelClass() {
        return HistoriqueDecompteComplexModel.class;
    }
}
