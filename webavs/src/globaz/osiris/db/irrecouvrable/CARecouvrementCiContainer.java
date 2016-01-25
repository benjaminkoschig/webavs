package globaz.osiris.db.irrecouvrable;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Contients l'ensemble des recouvrementCi pour la mise à charges des irrécouvrables. Cette classe sert de facade à la
 * liste des recouvrements. Elle offre un ensemble de méthodes permettant la manipulation des données
 * 
 * @author sch
 * 
 */
public class CARecouvrementCiContainer {
    private Map<Integer, CARecouvrementCi> recouvrementCiMap;

    public CARecouvrementCiContainer() {
        recouvrementCiMap = new HashMap<Integer, CARecouvrementCi>();
    }

    /**
     * Ajoute un recouvrementCi à la structure de données
     * 
     * @param montantRecouvrement
     * @param montantEtatCi
     * @param annee
     * @param genreDecision
     */
    public void addRecouvrementCi(BigDecimal montantRecouvrement, BigDecimal montantEtatCi, Integer annee,
            String genreDecision) {
        CARecouvrementCi recouvrementCi = new CARecouvrementCi(montantRecouvrement, montantEtatCi, annee, genreDecision);
        recouvrementCiMap.put(annee, recouvrementCi);
    }

    /**
     * Retourne la map recouvrementCiMap
     * 
     * @return
     */
    public Map<Integer, CARecouvrementCi> getRecouvrementCiMap() {
        return recouvrementCiMap;
    }

    /**
     * Retourne true si la structure de donnée contient déjà un recouvrement pour l'année fournie
     * 
     * @param annee
     * @return
     */
    public boolean hasRecouvrementCiForAnnee(Integer annee) {
        if (recouvrementCiMap.containsKey(annee)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        String result = "";
        for (Map.Entry<Integer, CARecouvrementCi> recouvrementCiEntry : recouvrementCiMap.entrySet()) {
            Integer key = recouvrementCiEntry.getKey();
            CARecouvrementCi recouvrementCi = recouvrementCiEntry.getValue();
            result += "\nkey(annee) : " + key.intValue();
            result += "\n" + recouvrementCi.toString();
            result += "###########################################################################";
        }
        return result;
    }
}
