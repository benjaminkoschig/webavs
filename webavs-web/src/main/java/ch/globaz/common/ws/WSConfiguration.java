package ch.globaz.common.ws;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;
import javax.ws.rs.ext.Provider;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@ApplicationPath("/rest")
public class WSConfiguration extends Application {

    private Set<Class<?>> classes;

    public WSConfiguration() {
        LOG.info("new instance of WSConfiguration");
        this.classes = this.loadClasses();
    }

    @Override
    public Set<Class<?>> getClasses() {
        return this.classes;
    }

    /**
     * On récupère toutes les classes "WebService" en s'appuyant sur l'annotation "Path" et tout les provider.
     * On parcours uniquement les sous-packages de "globaz" et on filtre sur les packages ".ws".
     *
     * Malheureusement on est obligé de faire cette recherche pour trouver les classes dédiées à jax-rs car la dédéction de ces class ne fonctionne
     * pas avec webSphere :(.  Avec tomact clea fonctionne parfaitement et toutes les fonctions de cette class pour être supprimé.
     *
     * @return le set de classes WS
     */
    Set<Class<?>> loadClasses() {
        Set<Class<?>> allClasses = new HashSet<>();

        try (ScanResult result = new ClassGraph().enableAnnotationInfo()
                                                 .acceptPackages("globaz", "ch.globaz")
                                                 .scan()) {

            ClassInfoList classInfos = result.getClassesWithAnnotation(Path.class.getName());
            allClasses.addAll(classInfos.loadClasses());
            classInfos = result.getClassesWithAnnotation(Provider.class.getName());
            allClasses.addAll(classInfos.loadClasses());

            LOG.info("Nb classes used for jax-rs is {} :", allClasses.size());
            allClasses.stream()
                      .sorted(Comparator.comparing(Class::getCanonicalName))
                      .forEach(aClass -> LOG.info("  {}", aClass.getCanonicalName()));

            return allClasses;
        } catch (Exception e) {
            LOG.error("Impossible de récupérer les classes des API Rest. ", e);
        }

        return allClasses;
    }
}
