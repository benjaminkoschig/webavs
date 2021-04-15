package globaz.corvus.ws;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

public class WebAvsAcor2020Application extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(WebAvsAcor2020Service.class);
        classes.add(CORSFilter.class);
        return classes;
    }
}
