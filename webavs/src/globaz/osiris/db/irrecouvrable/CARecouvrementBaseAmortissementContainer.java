package globaz.osiris.db.irrecouvrable;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class CARecouvrementBaseAmortissementContainer {
    private Map<Integer, CARecouvrementBaseAmortissement> recouvrementBaseAmortissementMap;

    public CARecouvrementBaseAmortissementContainer() {
        recouvrementBaseAmortissementMap = new HashMap<Integer, CARecouvrementBaseAmortissement>();
    }

    /**
     * Ajoute un recouvrementBaseAmortissement à la structure de données
     * 
     * @param montantAmortissement
     * @param montantEtatCi
     * @param annee
     * @param genreDecision
     */
    public void addRecouvrementBaseAmortissement(Integer anneeAmortissement, BigDecimal cumulCotisationForAnnee) {
        CARecouvrementBaseAmortissement recouvrementBaseAmortissement = new CARecouvrementBaseAmortissement(
                anneeAmortissement, cumulCotisationForAnnee.negate());
        recouvrementBaseAmortissementMap.put(anneeAmortissement, recouvrementBaseAmortissement);
    }

    /**
     * Retourne la map CARecouvrementBaseAmortissementMap
     * 
     * @return
     */
    public Map<Integer, CARecouvrementBaseAmortissement> getRecouvrementAmortissementMap() {
        return recouvrementBaseAmortissementMap;
    }

    @Override
    public String toString() {
        String result = "";
        for (Map.Entry<Integer, CARecouvrementBaseAmortissement> recouvrementBaseAmortissementEntry : recouvrementBaseAmortissementMap
                .entrySet()) {
            Integer key = recouvrementBaseAmortissementEntry.getKey();
            CARecouvrementBaseAmortissement recouvrementBaseAmortissement = recouvrementBaseAmortissementEntry
                    .getValue();
            result += "\nkey(annee) : " + key.intValue();
            result += "\n" + recouvrementBaseAmortissement.toString();
            result += "###########################################################################";
        }
        return result;
    }
}
