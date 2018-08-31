package ch.globaz.vulpecula.web.views.postetravail;

import globaz.jade.client.util.JadeStringUtil;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;
import ch.globaz.common.domaine.Date;

public class AdhesionCotisationViewContainer extends TreeSet<AdhesionCotisationView> {
    private static final long serialVersionUID = -6566017589607549449L;

    public AdhesionCotisationViewContainer() {
        super();
    }

    public AdhesionCotisationViewContainer(Collection<AdhesionCotisationView> adhesionsCotisationsPossibles) {
        super(adhesionsCotisationsPossibles);
    }

    public boolean isAuMoins1CotiActive() {
        Iterator<AdhesionCotisationView> it = iterator();
        while (it.hasNext()) {
            AdhesionCotisationView adhesion = it.next();
            if (adhesion.isChecked()) {
                if (JadeStringUtil.isBlank(adhesion.dateFin)) {
                    return true;
                } else {
                    Date dateFin = new Date(adhesion.dateFin);
                    if (Date.now().beforeOrEquals(dateFin)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isCotiDesactive() {
        boolean isCotiDesactive = true;
        Iterator<AdhesionCotisationView> it = iterator();
        while (it.hasNext()) {
            AdhesionCotisationView adhesion = it.next();
            if (adhesion.isChecked()) {
                isCotiDesactive = false;
            }
        }
        return isCotiDesactive;
    }

}
