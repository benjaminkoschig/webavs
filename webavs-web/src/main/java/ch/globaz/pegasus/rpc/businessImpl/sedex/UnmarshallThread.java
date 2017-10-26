package ch.globaz.pegasus.rpc.businessImpl.sedex;

import java.io.File;
import javax.xml.bind.JAXBContext;

public class UnmarshallThread<T> implements Runnable {

    private T xml;
    private final File file;
    private final JAXBContext context;
    private final Converter<T, ?> converter;
    private Retour<T> retour;

    public UnmarshallThread(File file, JAXBContext context, Converter<T, ?> converter) {
        this.file = file;
        this.context = context;
        this.converter = converter;
    }

    @Override
    public void run() {
        retour = UnmarshallerHelper.unmarshall(file, context, converter);
    }

    public T getXml() {
        return xml;
    }

    public File getFile() {
        return file;
    }

    public Retour<T> getRetour() {
        return retour;
    }

}
