package globaz.corvus.db.annonces;

import ch.globaz.common.util.Dates;
import ch.globaz.queryexec.bridge.jade.SCM;
import globaz.corvus.api.arc.downloader.REAnnoncesDateAugmentation5153;
import globaz.globall.db.BSession;

import java.util.List;
import java.util.stream.Collectors;

public class REAnnoncesService {

    public static List<REAnnoncesDateAugmentation5153> loadDateAugmentationRente5153(BSession session) {
        return executeRequeteDateAugmentationRente(session).stream().map(REAnnoncesService::convertAnnonce5153).collect(Collectors.toList());
    }

    public static REAnnoncesDateAugmentation5153 convertAnnonce5153(REAnnoncesDateAugmentation5153 annonce) {
        String date = Dates.formatSwiss(Dates.toDateFromDb(annonce.getValue()));
        annonce.setLabel(date+" - ("+annonce.getLabel()+")");
        return annonce;
    }

    private static List<REAnnoncesDateAugmentation5153> executeRequeteDateAugmentationRente(BSession session) {
        return SCM.newInstance(REAnnoncesDateAugmentation5153.class).session(session)
                .query("SELECT WJDAUG as value, count(*) as label from schema.REFICHA GROUP BY WJDAUG ORDER BY WJDAUG DESC").execute();
    }
}
