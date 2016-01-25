package globaz.osiris.db.irrecouvrable;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Contients l'ensemble des amortissementCi pour la mise � charges des irr�couvrables. Cette classe sert de facade � la
 * liste des amortissements. Elle offre un ensemble de m�thodes permettant la manipulation des donn�es
 * 
 * @author bjo
 * 
 */
public class CAAmortissementCiContainer {
    private Map<Integer, CAAmortissementCi> amortissementCiMap;

    public CAAmortissementCiContainer() {
        amortissementCiMap = new HashMap<Integer, CAAmortissementCi>();
    }

    /**
     * Ajoute un amortissementCi � la structure de donn�es
     * 
     * @param montantAmortissement
     * @param montantEtatCi
     * @param annee
     * @param genreDecision
     */
    public void addAmortissementCi(BigDecimal montantAmortissement, BigDecimal montantEtatCi, Integer annee,
            String genreDecision) {
        CAAmortissementCi amortissementCi = new CAAmortissementCi(montantAmortissement, montantEtatCi, annee,
                genreDecision);
        amortissementCiMap.put(annee, amortissementCi);
    }

    /**
     * Retourne la map amortissementCiMap
     * 
     * @return
     */
    protected Map<Integer, CAAmortissementCi> getAmortissementCiMap() {
        return amortissementCiMap;
    }

    /**
     * Retourne true si la structure de donn�e contient d�j� un amortissement pour l'ann�e fournie
     * 
     * @param annee
     * @return
     */
    public boolean hasAmortissementCiForAnnee(Integer annee) {
        if (amortissementCiMap.containsKey(annee)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        String result = "";
        for (Map.Entry<Integer, CAAmortissementCi> amortissementCiEntry : amortissementCiMap.entrySet()) {
            Integer key = amortissementCiEntry.getKey();
            CAAmortissementCi amortissementCi = amortissementCiEntry.getValue();
            result += "\nkey(annee) : " + key.intValue();
            result += "\n" + amortissementCi.toString();
            result += "###########################################################################";
        }
        return result;
    }
}
