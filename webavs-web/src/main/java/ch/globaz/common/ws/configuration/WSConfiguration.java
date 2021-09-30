package ch.globaz.common.ws.configuration;

import ch.globaz.common.exceptions.CommonTechnicalException;
import ch.globaz.common.exceptions.Exceptions;
import ch.globaz.common.ws.ApiHealthChecker;
import ch.globaz.common.ws.ExceptionHandler;
import ch.globaz.common.ws.ExceptionMapper;
import ch.globaz.common.ws.FilterMapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Priority;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@ApplicationPath("/api")
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
        List<Class<ApiHealthChecker>> apiHealthCheckers = new ArrayList<>();
        try (ScanResult result = new ClassGraph().enableAnnotationInfo()
                                                 .acceptPackages("globaz", "ch.globaz")
                                                 .scan()) {
            classesTemp = loadClasses(result);
            exceptionMapperClassesTemp = loadExceptionHandler(result);
            filterMappersTemp = resolveFilter(result);
            apiHealthCheckers = loadApiChecker(result);

        } catch (Exception e) {
            LOG.error("Impossible de récupérer les classes des API Rest. ", e);
        }

        this.classes = classesTemp;
        this.exceptionMapperClasses = ImmutableMap.copyOf(exceptionMapperClassesTemp);
        this.filterMappers = ImmutableSet.copyOf(filterMappersTemp);
        this.INSTANCE = this;
        ApiHealthCheckerService.check(apiHealthCheckers);
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
    private static Map<String /*path*/, ExceptionHandler> loadExceptionHandler(final ScanResult result) {
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
     * Malheureusement on est obligé de faire cette recherche pour trouver les classes dédiées à jax-rs car la détection de ces class ne fonctionne
     * pas avec webSphere :(. Avec Tomcat clea fonctionne parfaitement et toutes les fonctions de cette class pourraient être supprimé.
     *
     * @return le set de classes WS.
     */
    private static Set<Class<?>> loadClasses(ScanResult result) {

        Set<Class<?>> allClasses = new TreeSet<>(WSConfiguration::orderByPriorityAnnotation);
        allClasses.addAll(result.getClassesWithAnnotation(Path.class).loadClasses());
        allClasses.addAll(result.getClassesWithAnnotation(Provider.class).loadClasses());

        allClasses = allClasses.stream()
                               .filter(aClass -> Arrays.stream(aClass.getInterfaces()).noneMatch(o -> o.equals(FilterMapper.class)))
                               .collect(Collectors.toCollection(() -> new TreeSet<>(WSConfiguration::orderByPriorityAnnotation)));

        LOG.info("° Nb classes used for jax-rs Path and Provider is {} :", allClasses.size());

        allClasses.forEach(aClass -> LOG.info("   {}", aClass.getCanonicalName()));

        return allClasses;
    }

    private static List<Class<ApiHealthChecker>> loadApiChecker(final ScanResult result) {
        List<Class<?>> classList = result.getClassesImplementing(ApiHealthChecker.class).loadClasses();

        LOG.info("° Nb classes used to check the API is {} :", classList.size());

        return classList.stream().map(it->(Class<ApiHealthChecker>)it).collect(Collectors.toList());
    }

    private static int orderByPriorityAnnotation(final FilterMapper filterMapper1, final FilterMapper filterMapper2) {
        return orderByPriorityAnnotation(filterMapper1.getClass(), filterMapper2.getClass());
    }

    private static int orderByPriorityAnnotation(final Class<?> o1, final Class<?> o2) {
        Priority annotation1 = o1.getAnnotation(Priority.class);
        Priority annotation2 = o2.getAnnotation(Priority.class);

        if (annotation1 != null && annotation2 != null) {
            return Integer.compare(annotation1.value(), annotation2.value());
        }
        if (annotation1 != null) {
            return -1;
        }
        return o1.getCanonicalName().compareTo(o2.getCanonicalName());
    }

    private static Set<FilterMapper> resolveFilter(final ScanResult result) {
        Set<FilterMapper> filterMappers = result.getClassesImplementing(FilterMapper.class).stream()
                                                .filter(classInfo -> !classInfo.isAbstract())
                                                .map(ClassInfo::loadClass)
                                                .map(aClass -> (FilterMapper) Exceptions.checkedToUnChecked(aClass::newInstance, "error with this classe :" + aClass))
                                                .sorted(WSConfiguration::orderByPriorityAnnotation)
                                                .collect(Collectors.toCollection(() -> new TreeSet<>(WSConfiguration::orderByPriorityAnnotation)));

        LOG.info("° Nb filter used for jax-rs is {} :", filterMappers.size());
        filterMappers.stream()
                     .map(FilterMapper::getClass)
                     .forEach(aClass -> LOG.info("   {}", aClass.getCanonicalName()));

        return filterMappers;
    }

    static void executeOthersFilters(final ServletRequest request, final ServletResponse servletResponse, final FilterChain chain) {
        INSTANCE.getFilterMappers()
                .forEach(filter -> {
                    HttpServletRequest httpServletRequest = (HttpServletRequest) request;
                    if (!servletResponse.isCommitted() && httpServletRequest.getPathInfo()!=null && filter.isFilterable(httpServletRequest)) {
                        try {
                            filter.doFilter(request, servletResponse, chain);
                        } catch (Exception e) {
                            gererException((HttpServletRequest) request, (HttpServletResponse) servletResponse, e);
                        }
                    }
                });
    }

    private static void gererException(final HttpServletRequest request, final HttpServletResponse response, final Exception e) {
        BSession session = BSessionUtil.getSessionFromThreadContext();
        Response exceptionResponse;

        try {
            WSExceptionMapper wsExceptionMapper = new WSExceptionMapper();
            wsExceptionMapper.setRequest(request);
            wsExceptionMapper.setResponse(response);

            exceptionResponse = wsExceptionMapper.toResponse(e);
            response.setStatus(exceptionResponse.getStatus());
            exceptionResponse.getMetadata()
                             .forEach((key, value) ->
                                              response.addHeader(key, value.stream().map(String::valueOf).collect(Collectors.joining(" ; ")))
                             );
        } finally {
            if (session != null) {
                try {
                    session.getCurrentThreadTransaction().rollback();
                } catch (Exception ex) {
                    throw new CommonTechnicalException(ex);
                }
            }
        }

        Exceptions.checkedToUnChecked(() -> {
            PrintWriter out = response.getWriter();
            out.print(JacksonJsonProvider.getInstance().writeValueAsString(exceptionResponse.getEntity()));
            out.flush();
        });
    }
}
