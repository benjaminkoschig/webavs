package ch.globaz.pegasus.business.domaine.parametre.monnaieEtrangere;

import java.util.Collection;
import ch.globaz.pegasus.business.domaine.parametre.MapWithListSortedByDate;

public class MonnaiesEtrangere extends
        MapWithListSortedByDate<MonnaieEtrangereType, MonnaieEtrangere, MonnaiesEtrangere> {

    public MonnaiesEtrangere(Collection<MonnaieEtrangere> list) {
        super(list);
    }

    public MonnaiesEtrangere() {
    }

    @Override
    public Class<MonnaiesEtrangere> getTypeClass() {
        return MonnaiesEtrangere.class;
    }

}
