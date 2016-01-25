package ch.globaz.corvus.domaine.constantes;

import globaz.jade.client.util.JadeStringUtil;

/**
 * <p>
 * Codes pour les atteintes.
 * </p>
 * <p>
 * Tiré du manuel de la confédération <a href="www.bsv.admin.ch/vollzug/storage/documents/3952/3952_6_fr.pdf">Codes pour
 * la statistique des infirmités et des prestations</a> (Version du 1er janvier 2012)
 * </p>
 */
public enum Atteinte {
    // @formatter:off
	/** Aucune atteinte fonctionnelle */
	ATTEINTE_00(52808003),
	/** Paraplégie et tétraplégie */
	ATTEINTE_01(52808004),
	/**
	 * Atteintes fonctionnelles des extrémités supérieures (notamment amputations, autres mutilations, arthroses,
	 * paralysies périphériques)
	 */
	ATTEINTE_02(52808005),
	/** Atteintes fonctionnelles des extrémités inférieures (amputations etc., comme plus haut) */
	ATTEINTE_03(52808006),
	/** Atteintes fonctionnelles des extrémités supérieures et inférieures (amputations, etc.) */
	ATTEINTE_04(52808007),
	/** Atteintes fonctionnelles dans la région du tronc */
	ATTEINTE_05(52808008),
	/** Autres atteintes fonctionnelles de l’appareil locomoteur */
	ATTEINTE_08(52808009),
	/** Atteintes de l’état général */
	ATTEINTE_10(52808010),
	/** Cécité bilatérale */
	ATTEINTE_21(52808011),
	/** Grave faiblesse bilatérale de la vue */
	ATTEINTE_22(52808012),
	/**
	 * Autres atteintes des organes de la vue (p.ex. cécité unilatérale, amblyopie, strabisme, daltonisme, cécité
	 * nocturne, etc.)
	 */
	ATTEINTE_28(52808013),
	/** Surdité */
	ATTEINTE_30(52808014),
	/** Dureté bilatérale de l’ouïe */
	ATTEINTE_31(52808015),
	/** Autres atteintes de l’ouïe (surdité unilatérale, tintement de l’oreille) */
	ATTEINTE_32(52808016),
	/** Atteintes fonctionnelles de la mâchoire et de la bouche */
	ATTEINTE_33(52808017),
	/** Troubles du langage (bégaiement, bredouillement, aphasie, etc.) */
	ATTEINTE_41(52808018),
	/** Troubles dans la langue écrite (dyslexie, dysorthographie, etc.) */
	ATTEINTE_42(52808019),
	/** Troubles moteurs par lésion organique du cerveau (hémiplégies, ataxies, hémiparésies, dyskinésies, etc.) */
	ATTEINTE_50(52808020),
	/** Débilité mentale (oligophrénie et démence) */
	ATTEINTE_52(52808021),
	/** Syndrome psycho-organique */
	ATTEINTE_55(52808022),
	/** Troubles du comportement */
	ATTEINTE_61(52808023),
	/** Atteintes fonctionnelles combinées d’ordre mental et psychique */
	ATTEINTE_65(52808024),
	/** Troubles de la respiration et des échanges gazeux sanguins */
	ATTEINTE_70(52808025),
	/** Atteintes fonctionnelles des reins */
	ATTEINTE_72(52808026),
	/** Atteintes fonctionnelles de l’appareil digestif */
	ATTEINTE_73(52808027),
	/** Atteintes fonctionnelles du foie */
	ATTEINTE_74(52808028),
	/** Troubles de la circulation (insuffisance cardiaque, hypertension) */
	ATTEINTE_75(52808029),
	/** Atteintes fonctionnelles combinées d’ordre physique */
	ATTEINTE_81(52808030),
	/** Atteintes fonctionnelles combinées d’ordre mental, psychique et physique */
	ATTEINTE_91(52808031);
	// @formatter:on

    /**
     * Retourne l'énuméré correspondant au code système. </br><strong>Renvoie une {@link IllegalArgumentException} si la
     * chaîne de
     * caractère passée en paramètre est invalide (null ou vide) ou si le code système ne correspond pas à un valeur de
     * cette énuméré.</strong>
     * 
     * @param codeSysteme la valeur du code système à rechercher
     * @throws IllegalArgumentException si le paramètre codeSystem est null, une chaîne vide ou ne correspond pas à une
     *             valeur connue
     * @return l'énuméré correspondant au code système. </br><strong>Si la valeur n'est pas trouvée une
     *         IllegalArgumentException sera lancée</strong>
     */
    public static Atteinte parse(final String codeSysteme) {
        if (!JadeStringUtil.isDigit(codeSysteme)) {
            throw new IllegalArgumentException("The value [" + codeSysteme
                    + "] is not valid for the systemCode of type [" + Atteinte.class.getName() + "]");
        }
        Integer intCodeSysteme = Integer.parseInt(codeSysteme);
        return Atteinte.valueOf(intCodeSysteme);
    }

    /**
     * Retourne l'énuméré correspondant au code système.
     * </br><strong>Renvoie une {@link IllegalArgumentException} si la la valeur du paramètre <code>codeSystem</code>
     * est null ou si le code système ne correspond pas à une valeur de cette énuméré.</strong>
     * 
     * @param codeSysteme la valeur du code système à rechercher
     * @throws IllegalArgumentException si le paramètre codeSystem est null, ou ne correspond pas à une
     *             valeur connue
     * @return l'énuméré correspondant au code système. </br><strong>Si la valeur n'est pas trouvée une
     *         IllegalArgumentException sera lancée</strong>
     */
    public static Atteinte valueOf(final Integer codeSysteme) {
        if (codeSysteme != null) {
            for (Atteinte uneAtteinte : Atteinte.values()) {
                if (uneAtteinte.getCodeSysteme().equals(codeSysteme)) {
                    return uneAtteinte;
                }
            }
        }
        throw new IllegalArgumentException("The value [" + codeSysteme + "] is not valid for the systemCode of type ["
                + Atteinte.class.getName() + "]");
    }

    private Integer codeSysteme;

    private Atteinte(final Integer codeSysteme) {
        this.codeSysteme = codeSysteme;
    }

    /**
     * @return le code système correspondant, sous la forme d'un entier
     */
    public Integer getCodeSysteme() {
        return codeSysteme;
    }
}
