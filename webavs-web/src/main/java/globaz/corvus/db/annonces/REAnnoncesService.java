package globaz.corvus.db.annonces;

import ch.globaz.queryexec.bridge.jade.SCM;
import globaz.corvus.api.arc.downloader.REAnnoncesDateAugmentation5153;
import globaz.globall.db.BSession;

import java.util.List;

public class REAnnoncesService {

    public static List<REAnnoncesDateAugmentation5153> loadDateAugmentationRente5153(BSession session) {
        List<REAnnoncesDateAugmentation5153> list = executeRequeteDateAugmentationRente(session);
        list.stream().forEach(REAnnoncesDateAugmentation5153::convertAnnonce5153);
        return list;
    }

    private static List<REAnnoncesDateAugmentation5153> executeRequeteDateAugmentationRente(BSession session) {
        return SCM.newInstance(REAnnoncesDateAugmentation5153.class).session(session)
                .query("SELECT WJDAUG as value, DATE_TRAITEMENT as datetraitement, count(*) as label from schema.REFICHA GROUP BY WJDAUG, DATE_TRAITEMENT ORDER BY WJDAUG DESC").execute();
    }
}
