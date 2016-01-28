package ch.globaz.vulpecula.web.views.decompte;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Taux;

/**
 * @author Arnaud Geiser (AGE) | Créé le 6 mai 2014
 * 
 */
public class CotisationDecompteContainer {
    private List<CotisationDecompteGroupView> groupsViews;

    public CotisationDecompteContainer() {
        groupsViews = new ArrayList<CotisationDecompteGroupView>();
    }

    public void addCotisationDecompteView(final CotisationDecompteView cotisationDecompteView) {
        CotisationDecompteGroupView cotisationDecompteGroupView = searchGroupView(cotisationDecompteView);
        if (cotisationDecompteGroupView == null) {
            cotisationDecompteGroupView = new CotisationDecompteGroupView(cotisationDecompteView.getCaisseSociale());
            groupsViews.add(cotisationDecompteGroupView);
        }
        cotisationDecompteGroupView.add(cotisationDecompteView);
    }

    public CotisationDecompteGroupView searchGroupView(final CotisationDecompteView cotisationDecompteView) {
        for (CotisationDecompteGroupView groupView : groupsViews) {
            if (cotisationDecompteView.getCaisseSociale().equals(groupView.getCaisseSociale())) {
                return groupView;
            }
        }
        return null;
    }

    public String getTaux() {
        Taux value = Taux.ZERO();
        for (CotisationDecompteGroupView groupView : groupsViews) {
            value = value.addTaux(new Taux(groupView.getTaux()));
        }
        return value.getValue();
    }

    public String getCotisations() {
        Montant sum = Montant.ZERO;
        for (CotisationDecompteGroupView groupView : groupsViews) {
            sum = sum.add(Montant.valueOf(groupView.getCotisations()));
        }
        return sum.getValue();
    }

    public List<CotisationDecompteGroupView> getGroupsViews() {
        return groupsViews;
    }
}
