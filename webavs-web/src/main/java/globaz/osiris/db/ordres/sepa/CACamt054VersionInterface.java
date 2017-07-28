package globaz.osiris.db.ordres.sepa;

import java.util.List;
import org.w3c.dom.Document;

public interface CACamt054VersionInterface {
    List<CACamt054Notification> extractPojos(final Document doc, final String fileName);

    abstract CACamt054DefinitionType getType();
}
