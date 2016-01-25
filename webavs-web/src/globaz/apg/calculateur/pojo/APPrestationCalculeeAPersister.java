package globaz.apg.calculateur.pojo;

import globaz.apg.calculateur.IAPPrestationCalculateur;
import globaz.apg.db.prestation.APPrestation;
import java.util.ArrayList;
import java.util.List;

/**
 * Représente le résultat d'un calcul de prestation par un calculateur implémentant l'interface @see
 * {@link IAPPrestationCalculateur} </br> Un calculateur retourne une liste d'objets de domaine de type inconnu. La
 * classe suivante représente un des ces objets de domaine convertis par la méthode domainToPersistence(...) du
 * calculateur afin de persister le résultat du calcul.
 */
public class APPrestationCalculeeAPersister {

    private APPrestation prestation;
    private List<APRepartitionCalculeeAPersister> repartitions = new ArrayList<APRepartitionCalculeeAPersister>();

    public APPrestation getPrestation() {
        return prestation;
    }

    public List<APRepartitionCalculeeAPersister> getRepartitions() {
        return repartitions;
    }

    public void setPrestation(final APPrestation prestation) {
        this.prestation = prestation;
    }

    public void setRepartitions(final List<APRepartitionCalculeeAPersister> repartitions) {
        this.repartitions = repartitions;
    }

}
