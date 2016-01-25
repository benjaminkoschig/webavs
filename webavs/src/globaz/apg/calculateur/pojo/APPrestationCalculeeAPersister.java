package globaz.apg.calculateur.pojo;

import globaz.apg.calculateur.IAPPrestationCalculateur;
import globaz.apg.db.prestation.APPrestation;
import java.util.ArrayList;
import java.util.List;

/**
 * Repr�sente le r�sultat d'un calcul de prestation par un calculateur impl�mentant l'interface @see
 * {@link IAPPrestationCalculateur} </br> Un calculateur retourne une liste d'objets de domaine de type inconnu. La
 * classe suivante repr�sente un des ces objets de domaine convertis par la m�thode domainToPersistence(...) du
 * calculateur afin de persister le r�sultat du calcul.
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
