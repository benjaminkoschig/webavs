package ch.globaz.pegasus.business.domaine.parametre;

import static org.junit.Assert.*;
import org.junit.Test;
import ch.globaz.common.domaine.Date;

public class MapWithListSortedByDateTest {

    @Test
    public void testAdd() throws Exception {
        ParametresForTest parametres = build();
        String s = "";
        parametres = parametres.getParameters(ParametreType.UN);
        for (ParametreForTest p : parametres.getValues()) {
            s = s + p.getId();
        }
        assertEquals("12345", s);
    }

    @Test
    public void testResolveCourantBorne() throws Exception {
        ParametresForTest parametres = build();
        ParametreForTest parameter = parametres.getParameters(ParametreType.UN).resolveCourant(new Date("08.2014"));
        assertEquals("3", parameter.getId());
    }

    @Test
    public void testResolveCourantAvant() throws Exception {
        ParametresForTest parametres = build();
        ParametreForTest parameter = parametres.getParameters(ParametreType.UN).resolveCourant(new Date("07.2014"));
        assertEquals("4", parameter.getId());
    }

    @Test
    public void testResolveCourantApres() throws Exception {
        ParametresForTest parametres = build();
        ParametreForTest parameter = parametres.getParameters(ParametreType.UN).resolveCourant(new Date("09.2014"));
        assertEquals("3", parameter.getId());
    }

    @Test(expected = RuntimeException.class)
    public void testResolveCourantNotExist() throws Exception {
        ParametresForTest parametres = build();
        parametres.getParameters(ParametreType.UN).resolveCourant(new Date("01.2013"));
    }

    @Test(expected = RuntimeException.class)
    public void testResolveCourantNotFil() throws Exception {
        ParametresForTest parameters = new ParametresForTest();
        parameters.getParameters(ParametreType.UN).resolveCourant(new Date("01.2013"));
    }

    private ParametresForTest build() {
        ParametresForTest parameters = new ParametresForTest();
        parameters.add(buildUn("01.2014", "5"));
        parameters.add(buildUn("01.2015", "2"));
        parameters.add(buildUn("08.2014", "3"));
        parameters.add(buildUn("03.2015", "1"));
        parameters.add(buildUn("06.2014", "4"));

        parameters.add(buildDeux("01.2014", "15"));
        parameters.add(buildDeux("01.2015", "12"));
        parameters.add(buildDeux("04.2014", "13"));
        parameters.add(buildDeux("03.2015", "11"));
        parameters.add(buildDeux("03.2014", "14"));
        return parameters;
    }

    private ParametreForTest buildUn(String date, String id) {
        ParametreForTest parameter = new ParametreForTest();
        parameter.setType(ParametreType.UN);
        parameter.setDebut(new Date(date));
        parameter.setId(id);
        return parameter;
    }

    private ParametreForTest buildDeux(String date, String id) {
        ParametreForTest parameter = new ParametreForTest();
        parameter.setType(ParametreType.DEUX);
        parameter.setDebut(new Date(date));
        parameter.setId(id);
        return parameter;
    }

    // @Test
    // public void testGetMostRecent() throws Exception {
    // ParametresForTest parameters = build();
    // assertEquals("1", parameters.getParameters(ParametreType.UN).getMostRecent().getId());
    // }
}
