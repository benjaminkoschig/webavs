package ch.globaz.pegasus.business.domaine.parametre.home;

import ch.globaz.pegasus.business.domaine.parametre.MapWithListSortedByDate;

public class TypesChambrePrix extends MapWithListSortedByDate<String, TypeChambrePrix, TypesChambrePrix> {

    @Override
    public Class<TypesChambrePrix> getTypeClass() {
        return TypesChambrePrix.class;
    }

}
