package ch.globaz.pegasus.rpc.businessImpl.repositoriesjade.loader;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.common.collect.Lists;

public class ThreadLoaderRunner<I, O> {
    private static final Logger LOG = LoggerFactory.getLogger(ThreadLoaderRunner.class);

    private final List<ThreadLoader<I, O>> threads = new ArrayList<ThreadLoader<I, O>>();
    private List<O> ouptus = Collections.synchronizedList(new ArrayList<O>());
    private Transformer<I, O> transformer;
    private Collection<I> inputs;
    private I input;
    private BSession session;
    private int partitionSize = 0;
    private boolean partitionForMap = false;
    private boolean partitionForList = false;
    private boolean isParallel = false;
    private Map<String, Throwable> threadErrors = new ConcurrentHashMap<String, Throwable>();

    public ThreadLoaderRunner() {
        this.session = BSessionUtil.getSessionFromThreadContext();
    }

    public ThreadLoaderRunner<I, O> inputs(Collection<I> inputs) {
        this.inputs = inputs;
        return this;
    }

    public ThreadLoaderRunner<I, O> input(I input) {
        this.input = input;
        return this;
    }

    public ThreadLoaderRunner<I, O> transformer(Transformer<I, O> transformer) {
        this.transformer = transformer;
        return this;
    }

    public ThreadLoaderRunner<I, O> partitionMap(int size) {
        partitionSize = size;
        partitionForMap = true;
        return this;
    }

    public ThreadLoaderRunner<I, O> partitionList(int size) {
        partitionSize = size;
        partitionForList = true;
        return this;
    }

    public ThreadLoaderRunner<I, O> parallel() {
        this.isParallel = true;
        return this;
    }

    public ThreadLoaderRunner<I, O> parallel(Boolean parallel) {
        this.isParallel = parallel;
        return this;
    }

    // private void partitionMap(){
    // List<List<List<IdContainer>>> partition = Lists.partition(
    // new ArrayList<List<IdContainer>>(idContainers.values()), size);
    // List<IdsContainer> containers = new ArrayList<IdsContainer>();
    // for (List<List<IdContainer>> listSplited : partition) {
    // Map<String, List<IdContainer>> map = new HashMap<String, List<IdContainer>>();
    // for (List<IdContainer> list : listSplited) {
    // map.put(list.get(0).getIdVersionDroit(), list);
    // }
    // containers.add(new IdsContainer(map));
    // }
    // return containers;
    //
    // }

    private void executeWithThread() {

        int availableProcessors = Runtime.getRuntime().availableProcessors();
        ExecutorService execServ = Executors.newFixedThreadPool(availableProcessors + 1);
        LOG.info("Nb availableProcessors {}, {} thread will be use", availableProcessors, availableProcessors + 1);
        List<String> rolesList = loadRoles();

        for (final I dataInput : inputs) {
            createThread(dataInput, rolesList);
        }
        for (ThreadLoader<I, O> thread : threads) {
            execServ.execute(thread);
        }

        execServ.shutdown();

        while (!execServ.isTerminated()) {
            if (!this.threadErrors.isEmpty()) {
                LOG.error("Thread in error thread item !", this.threadErrors.values().iterator().next());
                execServ.shutdownNow();
                throw new RuntimeException("Error in thread", this.threadErrors.values().iterator().next());
            }
        }
    }

    public List<O> load() {
        doPatition();
        if (this.isParallel) {
            executeWithThread();
            List<O> list = new ArrayList<O>();
            for (ThreadLoader<I, O> thread : threads) {
                list.add(thread.getDatas());
            }
            return list;
        } else {
            List<O> outputs = new ArrayList<O>();
            for (final I in : inputs) {
                outputs.add(transformer.transform(in));
            }
            return outputs;
        }
    }

    public O loadAndJoinMap() {
        doPatition();
        // Future
        if (this.isParallel) {
            executeWithThread();
            Map map = new HashMap();
            for (ThreadLoader<I, O> thread : threads) {
                map.putAll((Map) thread.getDatas());
            }
            return (O) map;
        } else {
            Map map = new HashMap();
            for (final I in : inputs) {
                map.putAll((Map) transformer.transform(in));
            }
            return (O) map;
        }
    }

    private List<String> loadRoles() {
        List<String> rolesList = new ArrayList<String>();
        try {
            String[] roles = JadeAdminServiceLocatorProvider.getInstance().getServiceLocator().getRoleUserService()
                    .findAllIdRoleForIdUser(session.getUserId());
            if ((roles != null) && (roles.length > 0)) {
                rolesList = Arrays.asList(roles);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return rolesList;
    }

    private void createThread(final I input, List<String> rolesList) {
        threads.add(new ThreadLoader<I, O>(session, rolesList) {
            private O output;

            @Override
            public O getDatas() {
                return output;
            }

            @Override
            public void load() {
                synchronized (this) {
                    try {
                        output = transformer.transform(input);
                    } catch (Throwable e) {
                        ThreadLoaderRunner.this.threadErrors.put(toString(), e);
                    }
                }
            }
        });
    }

    private void doPatition() {
        if (this.partitionForList) {
            this.inputs = (Collection<I>) Lists.partition(new ArrayList<String>((Collection) input), partitionSize);
        }
    }

}
