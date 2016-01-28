package globaz.pavo.print.list;

import java.math.BigDecimal;

/**
 * Classe container pour les sommes de la liste des écritures. Date de création : (08.01.2003 13:55:12)
 * 
 * @author: Administrator
 */
public class CISommesEcritures {
    private int compteur = 0;
    private BigDecimal sommeCts;
    private BigDecimal sommeSansCts;

    /**
     * Initialisation des compteurs.
     */
    public CISommesEcritures() {
        super();
        // init
        sommeCts = new BigDecimal("0.00");
        sommeSansCts = new BigDecimal("0");
        compteur = 0;
    }

    /**
     * Ajouter le montant donné. Date de création : (08.01.2003 13:59:55)
     * 
     * @param montant
     *            le montant signé
     */
    public void additionne(String montant) {
        sommeCts = sommeCts.add(new BigDecimal(montant));
        sommeSansCts = sommeSansCts.add(new BigDecimal(montant.substring(0, montant.length() - 3)));
        compteur++;
    }

    /**
     * Retourne le nombre de montants additionnés. Date de création : (08.01.2003 14:02:27)
     * 
     * @return le nombre de montants additionnés.
     */
    public int getCompteur() {
        return compteur;
    }

    /**
     * Retourne le total des montants avec les centimes. Date de création : (08.01.2003 14:02:27)
     * 
     * @return le total des montants avec les centimes
     */
    public String getMontantCts() {
        return sommeCts.toString();
    }

    /**
     * Retourne le total des montants sans les centimes. Date de création : (08.01.2003 14:02:27)
     * 
     * @return le total des montants sans les centimes
     */
    public String getMontantSansCts() {
        return sommeSansCts.toString();
    }
}
