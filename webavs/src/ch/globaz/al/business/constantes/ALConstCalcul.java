package ch.globaz.al.business.constantes;

import java.math.BigDecimal;

/**
 * Constantes liés au calcul des droits et prestations
 * 
 * @author jts
 */
public interface ALConstCalcul {
    /**
     * Permet de récupérer la liste des droit calculé
     */
    public static final String DROITS_CALCULES = "3";

    /**
     * Précision des arrondis (0.01)
     */
    public static final double PRECISION_001 = 0.01;
    /**
     * Précision des arrondis (0.05)
     */
    public static final double PRECISION_005 = 0.05;
    /**
     * Mode d'arrondi pour les BigDecimal
     */
    public static final int ROUNDING_MODE = BigDecimal.ROUND_HALF_EVEN;
    /**
     * Nombre de décimales pour les BigDecimal
     */
    public static final int SCALE = 4;
    /**
     * Permet de récupérer le montant total brut
     */
    public static final String TOTAL_BASE = "4";
    /**
     * Permet de récupérer le montant total
     */
    public static final String TOTAL_EFFECTIF = "1";
    /**
     * Permet de récupérer le montant journalier effectif
     */
    public static final String TOTAL_UNITE_EFFECTIF = "2";
}
