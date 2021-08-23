package ch.globaz.common.ws.configuration;

import ch.globaz.common.exceptions.Exceptions;
import ch.globaz.common.ws.ExceptionHandler;
import ch.globaz.common.ws.ExceptionMapper;
import ch.globaz.common.ws.FilterMapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@ApplicationPath("/rest")
public class WSConfiguration extends Application {
    public static WSConfiguration INSTANCE;

    private final Set<Class<?>> classes;
    @Getter
    private final Map<String  /*path*/, ExceptionHandler> exceptionMapperClasses;
    @Getter
    private final Set<FilterMapper> filterMappers;

    public WSConfiguration() {
        LOG.info("new instance of Rest API application");
        Set<Class<?>> classesTemp = Collections.emptySet();
        Map<String  /*path*/, ExceptionHandler> exceptionMapperClassesTemp = Collections.emptyMap();
        Set<FilterMapper> filterMappersTemp = Collections.emptySet();
        try (ScanResult result = new ClassGraph().enableAnnotationInfo()
                                                 .acceptPackages("globaz", "ch.globaz")
                                                 .scan()) {
            classesTemp = loadClasses(result);
            exceptionMapperClassesTemp = loadExceptionHanlder(result);
            filterMappersTemp = resolveFilter(result);

        } catch (Exception e) {
            LOG.error("Impossible de récupérer les classes des API Rest. ", e);
        }

        this.classes = classesTemp;
        this.exceptionMapperClasses = ImmutableMap.copyOf(exceptionMapperClassesTemp);
        this.filterMappers = ImmutableSet.copyOf(filterMappersTemp);
        this.INSTANCE = this;
    }

    @Override
    public Set<Class<?>> getClasses() {
        return this.classes;
    }

    /**
     * Permet de charger les gestionnaires d'exception pour les api rest.
     *
     * @param result Permet de faire le scan.
     *
     * @return une map qui à comme clé le path ou l'on applique exception.
     */
    static Map<String /*path*/, ExceptionHandler> loadExceptionHanlder(final ScanResult result) {
        List<Class<?>> classList = result.getClassesWithAnnotation(ExceptionMapper.class).loadClasses();
        Map<String, ExceptionHandler> map = classList.stream()
                                                     .collect(Collectors.toMap(
                                                             aClass -> aClass.getAnnotation(ExceptionMapper.class).value(),
                                                             aClass -> Exceptions.checkedToUnChecked(() -> {
                                                                 Object exceptionHandler = aClass.newInstance();
                                                                 return (ExceptionHandler) exceptionHandler;
                                                             }, "new instance is impossible with this class: " + aClass)));

        LOG.info("° Nb classes used for jax-rs ExceptionMapper is {} :", map.size());
        map.entrySet().stream()
           .sorted(Comparator.comparing(entry -> entry.getValue().getClass().getCanonicalName()))
           .forEach(entry -> LOG.info("   {}", entry.getValue().getClass().getCanonicalName()));

        return new ConcurrentHashMap<>(map);
    }

    /**
     * On récupère toutes les classes "WebService" en s'appuyant sur l'annotation "Path" et tout les provider.
     * On parcours uniquement les sous-packages de "globaz" et on filtre sur les packages ".ws".
     * <p>
     * Malheureusement on est obligé de faire cette recherche pour trouver les classes dédiées à jax-rs car la dédéction de ces class ne fonctionne
     * pas avec webSphere :(.  Avec tomact clea fonctionne parfaitement et toutes les fonctions de cette class pour être supprimé.
     *
     * @return le set de classes WS
     */
    static Set<Class<?>> loadClasses(ScanResult result) {
        Set<Class<?>> allClasses = new HashSet<>();

        ClassInfoList classInfos = result.getClassesWithAnnotation(Path.class);
        allClasses.addAll(classInfos.loadClasses());
        classInfos = result.getClassesWithAnnotation(Provider.class);
        allClasses.addAll(classInfos.loadClasses());

        LOG.info("° Nb classes used for jax-rs Path and Provider is {} :", allClasses.size());
        allClasses.stream()
                  .filter(aClass -> Arrays.stream(aClass.getInterfaces()).noneMatch(o -> o.equals(FilterMapper.class)))
                  .filter(aClass -> Arrays.stream(aClass.getInterfaces()).noneMatch(o -> o.equals(Filter.class)))
                  .sorted(Comparator.comparing(Class::getCanonicalName))
                  .forEach(aClass -> LOG.info("   {}", aClass.getCanonicalName()));

        return allClasses;
    }

    static Set<FilterMapper> resolveFilter(final ScanResult result) {
        Set<FilterMapper> filterMappers = result.getClassesImplementing(FilterMapper.class).stream()
                                                .map(ClassInfo::loadClass)
                                                .map(aClass -> (FilterMapper) Exceptions.checkedToUnChecked(aClass::newInstance, "error with this classe :" + aClass))
                                                .collect(Collectors.toSet());
        LOG.info("° Nb filter used for jax-rs is {} :", filterMappers.size());
        filterMappers.stream()
                     .map(FilterMapper::getClass)
                     .sorted(Comparator.comparing(Class::getCanonicalName))
                     .forEach(aClass -> LOG.info("   {}", aClass.getCanonicalName()));

        return filterMappers;
    }

    static void executeOthersFilters(final ServletRequest request, final ServletResponse servletResponse, final FilterChain chain) {
        INSTANCE.getFilterMappers()
                .forEach(filter -> {
                    if (!servletResponse.isCommitted() && filter.isFilterable((HttpServletRequest) request)) {
                        try {
                            filter.doFilter(request, servletResponse, chain);
                        } catch (Exception e) {
                            gererException((HttpServletRequest) request, (HttpServletResponse) servletResponse, e);
                        }
                    }
                });
    }

    private static void gererException(final HttpServletRequest request, final HttpServletResponse response, final Exception e) {

        WSExceptionMapper wsExceptionMapper = new WSExceptionMapper();
        wsExceptionMapper.setRequest(request);
        wsExceptionMapper.setResponse(response);

        Response exceptionResponse = wsExceptionMapper.toResponse(e);
        response.setStatus(exceptionResponse.getStatus());
        exceptionResponse.getMetadata()
                         .forEach((key, value) ->
                             response.addHeader(key, value.stream().map(String::valueOf).collect(Collectors.joining(" ; ")))
                         );

        Exceptions.checkedToUnChecked(() -> {
            PrintWriter out = response.getWriter();
            out.print(JacksonJsonProvider.getInstance().writeValueAsString(exceptionResponse.getEntity()));
            out.flush();
        });
    }
}
