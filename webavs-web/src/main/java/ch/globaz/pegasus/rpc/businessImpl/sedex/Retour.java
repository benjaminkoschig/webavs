package ch.globaz.pegasus.rpc.businessImpl.sedex;

import java.io.File;
import javax.xml.bind.JAXBException;

public class Retour<T> {
    private final File file;
    private final T unmarshalledObject;
    private final Object domaine;
    private final Exception exception;

    public Retour(File file, T unmarshalledObject, Object domaine) {
        this.file = file;
        this.unmarshalledObject = unmarshalledObject;
        this.domaine = domaine;
        this.exception = null;
    }

    public Retour(File file, T unmarshalledObject) {
        this.file = file;
        this.unmarshalledObject = unmarshalledObject;
        this.domaine = null;
        this.exception = null;

    }

    public Retour(File file, JAXBException e) {
        this.file = file;
        this.unmarshalledObject = null;
        this.domaine = null;
        this.exception = e;
    }

    public File getFile() {
        return file;
    }

    public T getUnmarshalledObject() {
        return unmarshalledObject;
    }

    public <D> D getDomaine() {
        return (D) domaine;
    }

    public boolean hasError() {
        return exception != null;
    }

    public Exception getException() {
        return exception;
    }

}
