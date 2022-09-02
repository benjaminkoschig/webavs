package globaz.corvus.db.annonces;

import ch.globaz.common.util.Dates;
import ch.globaz.queryexec.bridge.jade.SCM;
import globaz.globall.db.BSession;

import java.util.List;
import java.util.stream.Collectors;

public class REAnnoncesService {
    public static List<String> loadDateAugmentationRente(BSession session) {
    return SCM.newInstance(String.class).session(session)
            .query("SELECT DISTINCT WJDAUG from schema.REFICHA ORDER BY WJDAUG DESC").execute()
                .stream().map(date -> Dates.formatSwiss(Dates.toDateFromDb(date))).collect(Collectors.toList());
    }
}
