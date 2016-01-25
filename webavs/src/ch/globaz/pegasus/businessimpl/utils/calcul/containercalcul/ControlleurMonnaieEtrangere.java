package ch.globaz.pegasus.businessimpl.utils.calcul.containercalcul;

import java.util.Map;
import java.util.TreeMap;

/**
 * Controlleur pour la gestion des monnaies étrangères destinés au calculateur PC
 * 
 * @author sce, modif -> 1.12
 * 
 */
public class ControlleurMonnaieEtrangere {

    private MonnaieEtrangere monnaie = null;
    /* Timestamp de la date de debut de la periode courante de la monnaie */
    private long monnaieCourante = 0;

    public ControlleurMonnaieEtrangere(MonnaieEtrangere monnaie, long debutPeriode) {
        // TODO Auto-generated constructor stub

        this.monnaie = monnaie;
        monnaieCourante = setMonnaieCourante(debutPeriode);

    }

    public MonnaieEtrangere getMonnaie() {
        return monnaie;
    }

    public String getMonnaieCourante() {
        return monnaie.getMonnaiesEtrangeres().get(monnaieCourante);
    }

    public void setMonnaie(MonnaieEtrangere monnaie) {
        this.monnaie = monnaie;
    }

    private long setMonnaieCourante(long debutPeriode) {
        Map<Long, String> datesOrdonnees = new TreeMap<Long, String>(monnaie.getMonnaiesEtrangeres());

        long dateDebutMonnaie = debutPeriode;

        // iteration sur les clés
        for (Long monnaieEt : datesOrdonnees.keySet()) {
            // Si la date de la periode est plus grande ou egale a la varaiable metier
            if (debutPeriode >= monnaieEt) {
                dateDebutMonnaie = monnaieEt;
            } else {
                return dateDebutMonnaie;
            }
        }
        return dateDebutMonnaie;
    }

}
