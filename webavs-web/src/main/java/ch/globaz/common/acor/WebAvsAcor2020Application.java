package ch.globaz.common.acor;

import globaz.corvus.acor2020.ws.WebAvsAcor2020ServiceRente;
import globaz.ij.acor2020.ws.WebAvsAcor2020ServiceIJ;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

public class WebAvsAcor2020Application extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(WebAvsAcor2020ServiceRente.class);
        classes.add(WebAvsAcor2020ServiceIJ.class);
        classes.add(CORSFilter.class);
        return classes;
    }
}
