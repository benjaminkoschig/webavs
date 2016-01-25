package ch.globaz.corvus.domaine.constantes;

import globaz.jade.client.util.JadeStringUtil;

/**
 * <p>
 * Codes pour les atteintes.
 * </p>
 * <p>
 * Tir� du manuel de la conf�d�ration <a href="www.bsv.admin.ch/vollzug/storage/documents/3952/3952_6_fr.pdf">Codes pour
 * la statistique des infirmit�s et des prestations</a> (Version du 1er janvier 2012)
 * </p>
 */
public enum Atteinte {
    // @formatter:off
	/** Aucune atteinte fonctionnelle */
	ATTEINTE_00(52808003),
	/** Parapl�gie et t�trapl�gie */
	ATTEINTE_01(52808004),
	/**
	 * Atteintes fonctionnelles des extr�mit�s sup�rieures (notamment amputations, autres mutilations, arthroses,
	 * paralysies p�riph�riques)
	 */
	ATTEINTE_02(52808005),
	/** Atteintes fonctionnelles des extr�mit�s inf�rieures (amputations etc., comme plus haut) */
	ATTEINTE_03(52808006),
	/** Atteintes fonctionnelles des extr�mit�s sup�rieures et inf�rieures (amputations, etc.) */
	ATTEINTE_04(52808007),
	/** Atteintes fonctionnelles dans la r�gion du tronc */
	ATTEINTE_05(52808008),
	/** Autres atteintes fonctionnelles de l�appareil locomoteur */
	ATTEINTE_08(52808009),
	/** Atteintes de l��tat g�n�ral */
	ATTEINTE_10(52808010),
	/** C�cit� bilat�rale */
	ATTEINTE_21(52808011),
	/** Grave faiblesse bilat�rale de la vue */
	ATTEINTE_22(52808012),
	/**
	 * Autres atteintes des organes de la vue (p.ex. c�cit� unilat�rale, amblyopie, strabisme, daltonisme, c�cit�
	 * nocturne, etc.)
	 */
	ATTEINTE_28(52808013),
	/** Surdit� */
	ATTEINTE_30(52808014),
	/** Duret� bilat�rale de l�ou�e */
	ATTEINTE_31(52808015),
	/** Autres atteintes de l�ou�e (surdit� unilat�rale, tintement de l�oreille) */
	ATTEINTE_32(52808016),
	/** Atteintes fonctionnelles de la m�choire et de la bouche */
	ATTEINTE_33(52808017),
	/** Troubles du langage (b�gaiement, bredouillement, aphasie, etc.) */
	ATTEINTE_41(52808018),
	/** Troubles dans la langue �crite (dyslexie, dysorthographie, etc.) */
	ATTEINTE_42(52808019),
	/** Troubles moteurs par l�sion organique du cerveau (h�mipl�gies, ataxies, h�mipar�sies, dyskin�sies, etc.) */
	ATTEINTE_50(52808020),
	/** D�bilit� mentale (oligophr�nie et d�mence) */
	ATTEINTE_52(52808021),
	/** Syndrome psycho-organique */
	ATTEINTE_55(52808022),
	/** Troubles du comportement */
	ATTEINTE_61(52808023),
	/** Atteintes fonctionnelles combin�es d�ordre mental et psychique */
	ATTEINTE_65(52808024),
	/** Troubles de la respiration et des �changes gazeux sanguins */
	ATTEINTE_70(52808025),
	/** Atteintes fonctionnelles des reins */
	ATTEINTE_72(52808026),
	/** Atteintes fonctionnelles de l�appareil digestif */
	ATTEINTE_73(52808027),
	/** Atteintes fonctionnelles du foie */
	ATTEINTE_74(52808028),
	/** Troubles de la circulation (insuffisance cardiaque, hypertension) */
	ATTEINTE_75(52808029),
	/** Atteintes fonctionnelles combin�es d�ordre physique */
	ATTEINTE_81(52808030),
	/** Atteintes fonctionnelles combin�es d�ordre mental, psychique et physique */
	ATTEINTE_91(52808031);
	// @formatter:on

    /**
     * Retourne l'�num�r� correspondant au code syst�me. </br><strong>Renvoie une {@link IllegalArgumentException} si la
     * cha�ne de
     * caract�re pass�e en param�tre est invalide (null ou vide) ou si le code syst�me ne correspond pas � un valeur de
     * cette �num�r�.</strong>
     * 
     * @param codeSysteme la valeur du code syst�me � rechercher
     * @throws IllegalArgumentException si le param�tre codeSystem est null, une cha�ne vide ou ne correspond pas � une
     *             valeur connue
     * @return l'�num�r� correspondant au code syst�me. </br><strong>Si la valeur n'est pas trouv�e une
     *         IllegalArgumentException sera lanc�e</strong>
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
     * Retourne l'�num�r� correspondant au code syst�me.
     * </br><strong>Renvoie une {@link IllegalArgumentException} si la la valeur du param�tre <code>codeSystem</code>
     * est null ou si le code syst�me ne correspond pas � une valeur de cette �num�r�.</strong>
     * 
     * @param codeSysteme la valeur du code syst�me � rechercher
     * @throws IllegalArgumentException si le param�tre codeSystem est null, ou ne correspond pas � une
     *             valeur connue
     * @return l'�num�r� correspondant au code syst�me. </br><strong>Si la valeur n'est pas trouv�e une
     *         IllegalArgumentException sera lanc�e</strong>
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
     * @return le code syst�me correspondant, sous la forme d'un entier
     */
    public Integer getCodeSysteme() {
        return codeSysteme;
    }
}
