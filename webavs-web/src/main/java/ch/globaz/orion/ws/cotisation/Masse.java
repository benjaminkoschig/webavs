package ch.globaz.orion.ws.cotisation;

import java.math.BigDecimal;

/**
 * Représente une cotisation, ses libellés dans les 3 langues et sa masse
 * 
 * @author sco
 * @since 18/09/2013
 */
public class Masse {
    public BigDecimal valeur = BigDecimal.ZERO;
    public String libelle_fr = null;
    public String libelle_de = null;
    public String libelle_it = null;
    public int idCotisation = 0;
    public int typeCotisation = 0;
    public int codeCanton = 0;
}
