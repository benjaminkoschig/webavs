package ch.globaz.orion.ws.cotisation;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente un affilié et sa liste de masses par cotisations
 * 
 * @author sco
 * @since 18/09/2013
 */
public class MassesForAffilie {

    public String noAffilieFormatte = null;
    public int idAffiliation = 0;
    public String raisonSociale = null;
    public final List<Masse> masses = new ArrayList<Masse>();
}
