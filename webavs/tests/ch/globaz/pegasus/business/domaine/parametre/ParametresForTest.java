package ch.globaz.pegasus.business.domaine.parametre;

class ParametresForTest extends MapWithListSortedByDate<ParametreType, ParametreForTest, ParametresForTest> {

    @Override
    public Class<ParametresForTest> getTypeClass() {
        return ParametresForTest.class;
    }

}
