package globaz.corvus.db.rentesverseesatort.wrapper;

import globaz.corvus.db.rentesverseesatort.RECalculRentesVerseesATortManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Conteneur de données ordonnées afin d'arranger les données remontées par {@link RECalculRentesVerseesATortManager} en
 * quelque chose de plus utilisable en Java
 * 
 * @author PBA
 */
public class RECalculRentesVerseesATortWrapper {

    private Long idDemandeRente;
    private Collection<RETiersPourCalculRenteVerseeATortWrapper> tiers;

    public RECalculRentesVerseesATortWrapper() {
        super();

        idDemandeRente = null;
        tiers = null;
    }

    public void addTiers(RETiersPourCalculRenteVerseeATortWrapper unTiers) {
        if (tiers != null) {
            tiers.add(unTiers);
        } else {
            tiers = new ArrayList<RETiersPourCalculRenteVerseeATortWrapper>(Arrays.asList(unTiers));
        }
    }

    public Long getIdDemandeRente() {
        return idDemandeRente;
    }

    public Collection<RETiersPourCalculRenteVerseeATortWrapper> getTiers() {
        return tiers;
    }

    public RETiersPourCalculRenteVerseeATortWrapper getTiers(Long idTiers) {
        if ((tiers == null) || (idTiers == null)) {
            return null;
        }
        for (RETiersPourCalculRenteVerseeATortWrapper unTiers : tiers) {
            if (idTiers.equals(unTiers.getIdTiers())) {
                return unTiers;
            }
        }
        return null;
    }

    public void setIdDemandeRente(Long idDemandeRente) {
        this.idDemandeRente = idDemandeRente;
    }

    public void setTiers(Collection<RETiersPourCalculRenteVerseeATortWrapper> tiers) {
        this.tiers = tiers;
    }
}
