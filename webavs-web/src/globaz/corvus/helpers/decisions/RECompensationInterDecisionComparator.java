package globaz.corvus.helpers.decisions;

import java.util.Comparator;

public class RECompensationInterDecisionComparator implements Comparator<REOVInterDecisionVO> {

    @Override
    public int compare(REOVInterDecisionVO c1, REOVInterDecisionVO c2) {
        // On ajoute l'id de la prestation pour assuer que les clés sont uniques.
        // car les elem. avec la meme clé ne sont pas ajouté
        // String k1 = c1.getPriority() + "-" + c1.getIdPrestation();
        // String k2 = c2.getPriority() + "-" + c2.getIdPrestation();
        String k1 = c1.getPriority() + "-" + c1.getIdOV();
        String k2 = c2.getPriority() + "-" + c2.getIdOV();

        return k1.compareTo(k2);
    }
}
