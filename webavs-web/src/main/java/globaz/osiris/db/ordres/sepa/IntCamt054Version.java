package globaz.osiris.db.ordres.sepa;

import java.util.List;
import org.w3c.dom.Document;

public interface IntCamt054Version {
    List<CACamt054Notification> extractPojos(final Document doc, final String fileName);
}
