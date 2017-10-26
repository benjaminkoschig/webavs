package ch.globaz.pegasus.rpc.businessImpl.sedex;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnmarshallerHelper<T> {
    private static final Logger LOG = LoggerFactory.getLogger(UnmarshallerHelper.class);

    private final JAXBContext context;
    private final File path;
    private final Collection<File> files;
    private boolean parallel = false;
    private Converter<T, ?> converter;

    private UnmarshallerHelper(Class<T> classToUnmarshall, String pathWhereXmlAre) {
        context = createContext(classToUnmarshall);
        path = new File(pathWhereXmlAre);
        files = FileUtils.listFiles(path, null, false);
    }

    public UnmarshallerHelper(Class<T> classToUnmarshall, List<String> pathFiles) {
        path = null;
        context = createContext(classToUnmarshall);
        files = new ArrayList<File>();
        for (String pathFile : pathFiles) {
            files.add(new File(pathFile));
        }
    }

    public static <T> UnmarshallerHelper<T> newInstance(Class<T> classToUnmarshall, String pathWhereXmlAre) {
        return new UnmarshallerHelper<T>(classToUnmarshall, pathWhereXmlAre);
    }

    public static <T> UnmarshallerHelper<T> newInstance(Class<T> classToUnmarshall, List<String> pathFiles) {
        return new UnmarshallerHelper<T>(classToUnmarshall, pathFiles);
    }

    public UnmarshallerHelper<T> parallel() {
        parallel = true;
        return this;
    }

    public UnmarshallerHelper<T> converter(Converter<T, ?> converter) {
        this.converter = converter;
        return this;
    }

    public List<Retour<T>> unmarshall() {
        return unmarshallFiles(files);
    }

    public <D> List<D> unmarshallAndConvert() {

        List<Retour<T>> retours = null;
        if (parallel) {
            retours = unmarshallFilesWithThread(files);
        } else {
            retours = unmarshallFiles(files);
        }
        List<D> list = new ArrayList<D>();
        for (Retour<T> retour : retours) {
            if (!retour.hasError()) {
                list.add((D) retour.getDomaine());
            } else {
                LOG.error("Error with this file {}", retour.getFile().getAbsolutePath(), retour.getException());
            }
        }
        return list;
    }

    public static <T> Retour<T> unmarshall(File file, JAXBContext jaxbContext, Converter<T, ?> converter) {
        Unmarshaller unmarshaller = createUnmarshaller(jaxbContext);
        try {
            T xml = (T) unmarshaller.unmarshal(file);
            if (converter != null) {
                return new Retour<T>(file, xml, converter.convert(xml, file));
            } else {
                return new Retour<T>(file, xml);
            }
        } catch (JAXBException e) {
            return new Retour<T>(file, e);
        }
    }

    private List<Retour<T>> unmarshallFiles(Collection<File> files) {
        if (parallel) {
            return unmarshallFilesWithThread(files);
        } else {
            List<Retour<T>> messages = new ArrayList<Retour<T>>();
            for (File file : files) {
                messages.add(unmarshall(file));
            }
            return messages;
        }
    }

    private Retour<T> unmarshall(File file) {
        return unmarshall(file, this.context, this.converter);
    }

    private static Unmarshaller createUnmarshaller(JAXBContext context) {
        try {
            return context.createUnmarshaller();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> JAXBContext createContext(Class<T> classToUnmarshall) {
        try {
            return JAXBContext.newInstance(classToUnmarshall);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Retour<T>> unmarshallFilesWithThread(Collection<File> files) {

        int availableProcessors = Runtime.getRuntime().availableProcessors();
        ExecutorService execServ = Executors.newFixedThreadPool(availableProcessors + 1);
        LOG.info("Nb availableProcessors {}, {} thread will be use", availableProcessors, availableProcessors + 1);

        List<UnmarshallThread<T>> threads = new ArrayList<UnmarshallThread<T>>();
        for (final File file : files) {
            threads.add(new UnmarshallThread<T>(file, context, this.converter));
        }

        for (UnmarshallThread<T> thread : threads) {
            execServ.execute(thread);
        }

        execServ.shutdown();

        while (!execServ.isTerminated()) {

        }
        List<Retour<T>> messages = new ArrayList<Retour<T>>();
        for (UnmarshallThread<T> thread : threads) {
            messages.add(thread.getRetour());
        }
        return messages;
    }
}
