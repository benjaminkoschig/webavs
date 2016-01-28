package ch.globaz.al.business.constantes;

import java.math.BigDecimal;

/**
 * Constantes li�s au calcul des droits et prestations
 * 
 * @author jts
 */
public interface ALConstCalcul {
    /**
     * Permet de r�cup�rer la liste des droit calcul�
     */
    public static final String DROITS_CALCULES = "3";

    /**
     * Pr�cision des arrondis (0.01)
     */
    public static final double PRECISION_001 = 0.01;
    /**
     * Pr�cision des arrondis (0.05)
     */
    public static final double PRECISION_005 = 0.05;
    /**
     * Mode d'arrondi pour les BigDecimal
     */
    public static final int ROUNDING_MODE = BigDecimal.ROUND_HALF_EVEN;
    /**
     * Nombre de d�cimales pour les BigDecimal
     */
    public static final int SCALE = 4;
    /**
     * Permet de r�cup�rer le montant total brut
     */
    public static final String TOTAL_BASE = "4";
    /**
     * Permet de r�cup�rer le montant total
     */
    public static final String TOTAL_EFFECTIF = "1";
    /**
     * Permet de r�cup�rer le montant journalier effectif
     */
    public static final String TOTAL_UNITE_EFFECTIF = "2";
}
