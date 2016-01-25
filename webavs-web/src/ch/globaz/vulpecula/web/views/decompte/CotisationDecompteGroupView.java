package ch.globaz.vulpecula.web.views.decompte;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Taux;

/**
 * @author Arnaud Geiser (AGE) | Créé le 6 mai 2014
 * 
 */
public class CotisationDecompteGroupView {
    private final String caisseSociale;
    private final List<CotisationDecompteView> cotisationDecompteViews;

    public CotisationDecompteGroupView(final String caisseSociale) {
        cotisationDecompteViews = new ArrayList<CotisationDecompteView>();
        this.caisseSociale = caisseSociale;
    }

    public List<CotisationDecompteView> getCotisationDecompteViews() {
        return cotisationDecompteViews;
    }

    public void add(final CotisationDecompteView cotisationDecompteView) {
        cotisationDecompteViews.add(cotisationDecompteView);
    }

    public String getCaisseSociale() {
        return caisseSociale;
    }

    public String getTaux() {
        Taux taux = Taux.ZERO();
        for (CotisationDecompteView cotisation : cotisationDecompteViews) {
            taux = taux.addTaux(new Taux(cotisation.getTaux()));
        }
        return taux.getValue();
    }

    public String getCotisations() {
        Montant sum = Montant.ZERO;
        for (CotisationDecompteView cotisationDecompteView : cotisationDecompteViews) {
            sum = sum.add(Montant.valueOf(cotisationDecompteView.getCotisation()));
        }
        return sum.getValue();
    }
}
