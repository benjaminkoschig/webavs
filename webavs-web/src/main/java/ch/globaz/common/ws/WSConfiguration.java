package ch.globaz.common.ws;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@ApplicationPath("/rest")
public class WSConfiguration extends Application {

    private Set<Class<?>> classes;
    public WSConfiguration() {
        LOG.info("new instance of WSConfiguration");
        this.classes = this.loadClasses();
    }

    /**
     * On récupère toutes les classes "WebService" en s'appuyant sur l'annotation "Path" et tout les provider.
     * On parcours uniquement les sous-packages de "globaz" et on filtre sur les packages ".ws".
     *
     * @return le set de classes WS
     */
    @Override
    public Set<Class<?>> getClasses() {
        return this.classes;
    }

    /**
     * On récupère toutes les classes "WebService" en s'appuyant sur l'annotation "Path" et tout les provider.
     * On parcours uniquement les sous-packages de "globaz" et on filtre sur les packages ".ws".
     *
     * @return le set de classes WS
     */
    public Set<Class<?>> loadClasses() {
        try {
            ImmutableSet<ClassPath.ClassInfo> allClasses =
                    ImmutableSet.<ClassPath.ClassInfo>builder()
                                .addAll(ClassPath.from(WSConfiguration.class.getClassLoader()).getTopLevelClassesRecursive("globaz"))
                                .addAll(ClassPath.from(WSConfiguration.class.getClassLoader()).getTopLevelClassesRecursive("ch.globaz"))
                            .build();

            Set<Class<?>> classes = allClasses.stream().filter(each -> each.getPackageName().contains(".ws"))
                                              .map(ClassPath.ClassInfo::load)
                                              .filter(clazz -> clazz.getAnnotation(Path.class) != null || clazz.getAnnotation(Provider.class) != null)
                                              .collect(Collectors.toSet());

            LOG.info("Nb classes used for jax-rs is {} :",classes.size());
            classes.stream().sorted(Comparator.comparing(aClass -> aClass.getCanonicalName()))
                   .forEach(aClass -> LOG.info("  {}",aClass.getCanonicalName()));
            return classes;
        } catch (IOException e) {
            LOG.error("Impossible de récupérer les classes des API Rest. ", e);
        }
        return Collections.EMPTY_SET;
    }
}
