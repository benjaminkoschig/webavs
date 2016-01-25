package globaz.helios.process.consolidation.utils;

import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.helios.db.comptes.CGExerciceComptableViewBean;
import java.util.ArrayList;

public class CGProcessConsolidationUtil {

    public static final char SECTEUR_BEGIN_1 = '1';
    public static final char SECTEUR_BEGIN_2 = '2';
    public static final char SECTEUR_BEGIN_3 = '3';
    public static final char SECTEUR_BEGIN_9 = '9';

    public static CGExerciceComptableViewBean getExerciceComptable(BSession session, BTransaction transaction,
            String idExerciceComptable) throws Exception {
        CGExerciceComptableViewBean exerciceComptable = new CGExerciceComptableViewBean();
        exerciceComptable.setSession(session);

        exerciceComptable.setIdExerciceComptable(idExerciceComptable);

        exerciceComptable.retrieve(transaction);

        if (exerciceComptable.hasErrors() || exerciceComptable.isNew()) {
            throw new Exception(session.getLabel("NEED_EXERCICE_COMPTABLE_ERREUR"));
        }

        return exerciceComptable;
    }

    public static ArrayList getSecteurExportable() {
        ArrayList secteurExportable = new ArrayList();
        secteurExportable.add(String.valueOf(SECTEUR_BEGIN_1));
        secteurExportable.add(String.valueOf(SECTEUR_BEGIN_2));
        secteurExportable.add(String.valueOf(SECTEUR_BEGIN_3));
        secteurExportable.add(String.valueOf(SECTEUR_BEGIN_9));

        return secteurExportable;
    }

    public static boolean isIdExterneExportable(String idExterne) {
        return (idExterne.charAt(0) == SECTEUR_BEGIN_1 || idExterne.charAt(0) == SECTEUR_BEGIN_2
                || idExterne.charAt(0) == SECTEUR_BEGIN_3 || idExterne.charAt(0) == SECTEUR_BEGIN_9);
    }

}
