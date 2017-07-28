package globaz.osiris.db.ordres.sepa;

import java.util.List;
import org.w3c.dom.Document;

public abstract class CACamt054VersionAbstract<T> implements CACamt054VersionInterface {
    public static final String DATE_FORMAT = "dd.MM.yyyy";

    public Class<T> typedClass;

    public CACamt054VersionAbstract(Class<T> typedClass) {
        this.typedClass = typedClass;
    }

    @Override
    public List<CACamt054Notification> extractPojos(final Document doc, final String fileName) {

        T documentCamt054 = CAJaxbUtil.unmarshall(doc, typedClass);

        return constructPojos(documentCamt054, fileName);
    }

    protected abstract List<CACamt054Notification> constructPojos(final T document, final String fileName);
}
