package globaz.osiris.db.irrecouvrable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Contient l'ensemble des montants disponibles pour la ventilation.Cette classe sert de facade à la liste des montant a
 * ventiler. Elle offre un ensemble de méthodes permettant la manipulation des données
 * 
 * @author bjo
 * 
 */
public class CAMontantAVentilerContainer {
    private Map<Integer, CAMontantAVentiler> montantAVentilerMap;// key = année

    public CAMontantAVentilerContainer() {
        montantAVentilerMap = new TreeMap<Integer, CAMontantAVentiler>();
    }

    /**
     * Permet d'ajouter un montant a ventiler dans la structure de donnees. Si un montant existe deja pour l'annee le
     * montant est alors additioner. Sinon un nouveau montant est cree
     * 
     * @param annee
     * @param montant
     */
    public void addMontantInMontantAVentilerContainer(Integer annee, BigDecimal montant) {
        if (montantAVentilerMap.containsKey(annee)) {
            CAMontantAVentiler montantAVentiler = montantAVentilerMap.get(annee);
            montantAVentiler.additionnerToMontant(montant);
        } else {
            CAMontantAVentiler montantAVentiler = new CAMontantAVentiler(annee, montant);
            montantAVentilerMap.put(annee, montantAVentiler);
        }
    }

    /**
     * Parcours tous les montants a ventiler et retourne le total
     * 
     * @return
     */
    public BigDecimal calculerMontantAVentilerTotal() {
        BigDecimal montantAVentilerTotal = new BigDecimal(0);
        for (CAMontantAVentiler montantAVentiler : montantAVentilerMap.values()) {
            montantAVentilerTotal = montantAVentilerTotal.add(montantAVentiler.getMontant());
        }
        return montantAVentilerTotal;
    }

    /**
     * Supprimer tous les montants à ventiler négatifs et retourne le montant total supprime /**
     * 
     * @return
     */
    public BigDecimal deleteAllMontantsAVentilerNegatifs() {
        BigDecimal montantTotalSupprime = new BigDecimal(0);
        Iterator<Map.Entry<Integer, CAMontantAVentiler>> montantAVentilerIterator = montantAVentilerMap.entrySet()
                .iterator();
        while (montantAVentilerIterator.hasNext()) {
            Map.Entry<Integer, CAMontantAVentiler> montantAVentilerEntry = montantAVentilerIterator.next();
            int annee = montantAVentilerEntry.getKey();
            CAMontantAVentiler montantAVentiler = montantAVentilerEntry.getValue();
            if (annee == 0) {
                continue;
            }
            if (montantAVentiler.getMontant().signum() == -1) {
                montantTotalSupprime = montantTotalSupprime.add(montantAVentiler.getMontant());
                montantAVentilerIterator.remove();
            }
        }

        return montantTotalSupprime;
    }

    /**
     * retourne une liste contenant toutes les années contenues dans la map. Chaque année n'apparaît qu'une seule fois.
     * 
     * @return
     */
    public List<Integer> findAnneesContenues() {
        List<Integer> anneesContenuesList = new ArrayList<Integer>();

        for (Map.Entry<Integer, CAMontantAVentiler> montantAVentilerEntry : montantAVentilerMap.entrySet()) {
            Integer annee = montantAVentilerEntry.getKey();
            if (!anneesContenuesList.contains(annee)) {
                anneesContenuesList.add(annee);
            }
        }

        return anneesContenuesList;
    }

    /**
     * Retourne le montant a ventiler pour l'annee et 0 si annee non trouvee
     * 
     * @param annee
     * @return
     */
    public BigDecimal getMontantAVentilerForAnnee(Integer annee) {
        CAMontantAVentiler montantAVentiler = montantAVentilerMap.get(annee);
        if (montantAVentiler != null) {
            return montantAVentiler.getMontant();
        } else {
            return new BigDecimal(0);
        }
    }

    /**
     * Permet de repartir un montant sur les autres montants a ventiler
     * 
     * @param montantARepartir
     */
    public void repartirMontantSurAutreMontantAVentiler(BigDecimal montantARepartir) {
        for (Map.Entry<Integer, CAMontantAVentiler> montantAVentilerEntry : montantAVentilerMap.entrySet()) {
            int annee = montantAVentilerEntry.getKey();
            CAMontantAVentiler montantAVentiler = montantAVentilerEntry.getValue();
            if (annee == 0) {
                continue;
            } else {
                BigDecimal montant = montantAVentiler.getMontant();
                // si montant à répartir > montant => on met à 0 le montant et met à jour montantARepartir
                if (montantARepartir.compareTo(montant) >= 0) {
                    montantAVentiler.additionnerToMontant(montant.negate());
                    montantARepartir = montantARepartir.add(montant.negate());
                } else {
                    montantAVentiler.additionnerToMontant(montantARepartir);
                    montantARepartir = montantARepartir.add(montantARepartir.negate());
                }
            }
        }
        // si il reste du montant à répartir on le met dans l'année 0
        if (montantARepartir.signum() != 0) {
            addMontantInMontantAVentilerContainer(0, montantARepartir);
        }
    }

    public void setMontantAVentiler(Integer annee, BigDecimal montant) {
        CAMontantAVentiler montantAVentiler = new CAMontantAVentiler(annee, montant);
        montantAVentilerMap.put(annee, montantAVentiler);

    }

    @Override
    public String toString() {
        String result = "--------------------------- AFFICHAGE DES MONTANT A VENTILER ---------------------------";
        for (Map.Entry<Integer, CAMontantAVentiler> montantAVentilerEntry : montantAVentilerMap.entrySet()) {
            Integer key = montantAVentilerEntry.getKey();
            CAMontantAVentiler montantAVentiler = montantAVentilerEntry.getValue();
            result += "\nkey (annee) :" + key;
            result += "\n" + montantAVentiler.toString();
        }
        return result;
    }
}
