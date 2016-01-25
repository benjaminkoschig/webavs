package ch.globaz.vulpecula.web.views.postetravail;

import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;

public class AdhesionCotisationViewContainer extends TreeSet<AdhesionCotisationView> {
    private static final long serialVersionUID = -6566017589607549449L;

    public AdhesionCotisationViewContainer() {
        super();
    }

    public AdhesionCotisationViewContainer(Collection<AdhesionCotisationView> adhesionsCotisationsPossibles) {
        super(adhesionsCotisationsPossibles);
    }

    public boolean isAuMoins1Coti() {
        Iterator<AdhesionCotisationView> it = iterator();
        while (it.hasNext()) {
            AdhesionCotisationView adhesion = it.next();
            if (adhesion.isChecked()) {
                return true;
            }
        }
        return false;
    }
}
