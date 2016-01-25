package ch.globaz.corvus.domaine.constantes;

import globaz.jade.client.util.JadeStringUtil;
import ch.globaz.corvus.domaine.RenteAccordee;

/**
 * <p>
 * Code cas sp�ciaux des rentes accord�es (permet de d�crire un cas particulier m�tier par l'interm�diaire d'un code).
 * </p>
 * <p>
 * Tir� de l'annexe V du document
 * "Directives concernant les rentes (DR) de l'assurance vieillesse, survivants et invalidit� f�d�rale" (�tat au 1er
 * janvier 2014)<br/>
 * <a href="http://www.bsv.admin.ch/vollzug/documents/view/75/lang:fre/category:23">Le document sur le site de la
 * conf�d�ration</a>
 * </p>
 */
public enum CodeCasSpecialRente {

    /** Prestation r�duite pour faute grave de l'assur� */
    CODE_CAS_SPECIAL_01(52822001),
    /** Rente d'orphelin ou pour enfant r�duite en raison de la surassurance */
    CODE_CAS_SPECIAL_02(52822002),
    /**
     * Rente d'invalidit� ou allocation pour impotent de l'AI momentan�ment r�duite pour violation l�g�re ou grave de
     * ses obligations
     */
    CODE_CAS_SPECIAL_03(52822003),
    /** Rente de vieillesse r�duite en proportion du montant de la rente d'invalidit� norv�gienne */
    CODE_CAS_SPECIAL_04(52822004),
    /** Rente plafonn�e */
    CODE_CAS_SPECIAL_05(52822005),
    /** Montant diff�rentiel de la Caisse suisse sous la forme d'une rente pour enfant ou d'orphelin */
    CODE_CAS_SPECIAL_06(52822006),
    /** La rente AI suspendue durant l'ex�cution d'une peine privative de libert� ou d'une mesure */
    CODE_CAS_SPECIAL_07(52822007),
    /** Rente de vieillesse ajourn�e dont l'ajournement n'a pas encore �t� r�voqu� */
    CODE_CAS_SPECIAL_08(52822008),
    /**
     * Rente extraordinaire d'invalides de naissance ou depuis leur enfance avec suppl�ment ainsi que les rentes AVS
     * ordinaires qui leur succ�dent
     */
    CODE_CAS_SPECIAL_21(52822021),
    /** Rente ordinaire d'invalides pr�coces avec montant minimum major� ainsi que les rentes AVS qui leur succ�dent */
    CODE_CAS_SPECIAL_22(52822022),
    /** Rente AI d'un montant �quivalent � celui de la rente virtuelle de survivant */
    CODE_CAS_SPECIAL_23(52822023),
    /**
     * Rente pour enfant de l'AVS d'un montant �quivalent � celui de la rente d'orphelin supprim�e ainsi que la rente
     * d'orphelin double servie au montant de l'ancienne rente d'orphelin de m�re
     */
    CODE_CAS_SPECIAL_24(52822024),
    /**
     * Garantie des droits acquis d�s le 1er janvier 1964 (rente partielle reposant sur une ancienne cotisation annuelle
     * moyenne �A�)
     */
    CODE_CAS_SPECIAL_28(52822028),
    /**
     * Rente pour cas p�nible en cours sur un degr� d'invalidit� inf�rieur � 50 pour cent (garantie des droits acquis
     * d�s le 1er janvier 2004)
     */
    CODE_CAS_SPECIAL_29(52822029),
    /**
     * Rente AI enti�re reposant sur un degr� d'invalidit� inf�rieur � 70 pour cent: garantie des droits acquis pour
     * personnes �g�es de plus de 50 ans
     */
    CODE_CAS_SPECIAL_30(52822030),
    /**
     * Rente transf�r�e d'une personne veuve remari�e avec montant �gal � celui de la rente calcul�e selon les
     * dispositions de la 9e r�vision de l'AVS
     */
    CODE_CAS_SPECIAL_31(52822031),
    /** Garantie des droits acquis d�s le 1er janvier 1979 */
    CODE_CAS_SPECIAL_32(52822032),
    /**
     * Ancienne rente servie dans un cas p�nible, qui repose sur un degr� d'invalidit� inf�rieur � 40 pour cent
     * (garantie des droits acquis d�s le 1er janvier 1988)
     */
    CODE_CAS_SPECIAL_34(52822034),
    /**
     * Rente d'orphelin d'un montant �quivalent � celui de la rente d'orphelin d�termin�e selon les dispositions de la
     * 9e r�vision de l'AVS
     */
    CODE_CAS_SPECIAL_36(52822036),
    /** Trois-quarts de rente AI sur un degr� d'invalidit� inf�rieur � 60 pour cent */
    CODE_CAS_SPECIAL_37(52822037),
    /** Rente AI enti�re reposant sur un degr� d'invalidit� inf�rieur � 70 pour cent */
    CODE_CAS_SPECIAL_38(52822038),
    /** Demi-rente AI reposant sur un degr� d'invalidit� inf�rieur � 50 pour cent */
    CODE_CAS_SPECIAL_39(52822039),

    CODE_CAS_SPECIAL_40(52822040),
    /** Rente d�termin�e en tenant compte des dur�es de cotisations espagnoles */
    CODE_CAS_SPECIAL_44(52822044),
    /** Rente d�termin�e en tenant compte des dur�es de cotisations n�erlandaises */
    CODE_CAS_SPECIAL_45(52822045),
    /** Rente d�termin�e en tenant compte des dur�es de cotisations turques */
    CODE_CAS_SPECIAL_46(52822046),
    /**
     * Rente extraordinaire en cas de minimum garanti, lorsque le montant de la rente ordinaire remplac�e ne correspond
     * pas � la valeur des tables (rente ordinaire r�duite ou augment�e)
     */
    CODE_CAS_SPECIAL_47(52822047),
    /** Rente d�termin�e en tenant compte des dur�es de cotisations grecques */
    CODE_CAS_SPECIAL_48(52822048),
    /** Rente d�termin�e en tenant compte des dur�es de cotisations fran�aises */
    CODE_CAS_SPECIAL_49(52822049),
    /** Rente d�termin�e en tenant compte des dur�es de cotisations portugaises */
    CODE_CAS_SPECIAL_50(52822050),
    /** Rente d�termin�e en tenant compte des dur�es de cotisations belges */
    CODE_CAS_SPECIAL_51(52822051),
    /** Rente d�termin�e en tenant compte des dur�es de cotisations norv�giennes */
    CODE_CAS_SPECIAL_52(52822052),
    /** Rente d�termin�e en tenant compte des dur�es de cotisations britanniques */
    CODE_CAS_SPECIAL_53(52822053),
    /** Rente pour enfant d�termin�e en tenant compte de p�riodes d'assurances �trang�res */
    CODE_CAS_SPECIAL_54(52822054),
    /** Rente AVS/AI avec p�riodes d'assurances UE/AELE inf�rieures � une ann�e */
    CODE_CAS_SPECIAL_55(52822055),
    /**
     * Rente sujette � mutation d�s le 1er janvier 1979, pour laquelle les mois d'appoint ont �t� pris en compte en
     * vertu des r�gles applicables avant cette date
     */
    CODE_CAS_SPECIAL_61(52822061),
    /**
     * Introduction de l'�chelle de rentes lin�aire. Garantie des droits acquis de l'�chelle de rentes jusqu'ici
     * d�terminante
     */
    CODE_CAS_SPECIAL_63(52822063),
    /** Garantie des droits acquis selon l'Avenant � la Convention avec la Principaut� du Liechtenstein */
    CODE_CAS_SPECIAL_78(52822078),
    /**
     * Rente de vieillesse avec compl�ment diff�rentiel jusqu'� concurrence du montant de l'ancienne rente AI d�termin�e
     * en tenant compte des p�riodes de cotisations fran�aises
     */
    CODE_CAS_SPECIAL_79(52822079),
    /**
     * Rente sujette � mutation d�s le 1er janvier 1997, pour laquelle l'�chelle a �t� d�termin�e en vertu des r�gles
     * applicables avant cette date
     */
    CODE_CAS_SPECIAL_80(52822080),
    /** Rente avec suppl�ment d'ajournement selon les dispositions de la 9e r�vision de l'AVS */
    CODE_CAS_SPECIAL_81(52822081),
    /** Rente transf�r�e, changement de registre sans modification des bases de calcul */
    CODE_CAS_SPECIAL_82(52822082),
    /** Rente AI pas encore r�vis�e en raison de la 4e r�vision de l'AI */
    CODE_CAS_SPECIAL_83(52822083),
    /** Prestation transitoire sous forme de rente AI */
    CODE_CAS_SPECIAL_84(52822084),
    /** Prestation r�duite pour d'autres raisons */
    CODE_CAS_SPECIAL_91(52822091),
    /** Prestation augment�e pour d'autres raisons */
    CODE_CAS_SPECIAL_92(52822092),
    /**
     * Prestation allou�e ou d�termin�e - pour d'autres raisons - en vertu d'une r�glementation sp�ciale (montant
     * mensuel = valeur des tables)
     */
    CODE_CAS_SPECIAL_93(52822093),
    /** Allocation pour impotent (devenue caduque) de l'AVS/AI, dont le montant est vers� � un assureur-accidents */
    CODE_CAS_SPECIAL_99(52822099),
    /**
     * Il s'est av�r� que des codes cas sp�ciaux ont �t� retir�s des directives sur les rentes mais sont toujours
     * pr�sent dans l'historique de certain client (le code cas sp�cial 27 par exemple). Pour pallier � une erreur
     * lorsqu'on devra recalculer ces cas, un code cas sp�cial inconnu a �t� ajout� afin d'�tre retourn� quand on ne
     * conna�t pas le code syst�me du code cas sp�cial
     */
    CODE_CAS_SPECIAL_INCONNU(0);

    /**
     * Retourne l'�num�r� correspondant au code syst�me. </br><strong>Renvoie une {@link IllegalArgumentException} si la
     * cha�ne de caract�re pass�e en param�tre est invalide (null ou vide) ou si le code syst�me ne correspond pas � un
     * valeur de cette �num�r�.</strong>
     * 
     * @param codeSysteme la valeur du code syst�me � rechercher
     * @throws IllegalArgumentException si le param�tre codeSystem est null, une cha�ne vide ou ne correspond pas � une
     *             valeur connue
     * @return l'�num�r� correspondant au code syst�me. </br><strong>Si la valeur n'est pas trouv�e une
     *         IllegalArgumentException sera lanc�e</strong>
     */
    public static CodeCasSpecialRente parse(final String codeSysteme) {
        if (!JadeStringUtil.isDigit(codeSysteme)) {
            throw new IllegalArgumentException("The value [" + codeSysteme
                    + "] is not valid for the systemCode of type [" + CodeCasSpecialRente.class.getName() + "]");
        }
        if (codeSysteme.length() == 2) {
            // FIXME LGA 02.2015 : WTF ??? C'est quoi cette compl�tion du code syst�me ?
            return CodeCasSpecialRente.valueOf(Integer.parseInt("528220" + codeSysteme));
        }
        return CodeCasSpecialRente.valueOf(Integer.parseInt(codeSysteme));
    }

    /**
     * Retourne l'�num�r� correspondant au code syst�me. </br><strong>Renvoie une {@link IllegalArgumentException} si le
     * param�tre <code>codeSysteme</code> est null
     * 
     * @param codeSysteme la valeur du code syst�me � rechercher
     * @throws IllegalArgumentException si le param�tre codeSystem est null
     * @return l'�num�r� correspondant au code syst�me. </br><strong>Si la valeur n'est pas trouv�e, l'�num�r�
     *         CodeCasSpecialRente.CODE_CAS_SPECIAL_INCONNU sera retourn� !!</strong>
     */
    public static CodeCasSpecialRente valueOf(final Integer codeSysteme) {
        if (codeSysteme == null) {
            throw new IllegalArgumentException("The value [" + codeSysteme
                    + "] is not valid for the systemCode of type [" + CodeCasSpecialRente.class.getName() + "]");
        }
        for (CodeCasSpecialRente unCodeCasSpecial : CodeCasSpecialRente.values()) {
            if (unCodeCasSpecial.getCodeSysteme().equals(codeSysteme)) {
                return unCodeCasSpecial;
            }
        }
        return CODE_CAS_SPECIAL_INCONNU;
    }

    private Integer codeSysteme;

    private CodeCasSpecialRente(final Integer codeSysteme) {
        this.codeSysteme = codeSysteme;
    }

    /**
     * @param renteAccordee
     *            une rente accord�e
     * @return vrai si le code cas sp�cial est pr�sent dans cette rente accord�e
     */
    public boolean estPresentDans(final RenteAccordee renteAccordee) {
        if (renteAccordee == null) {
            throw new IllegalArgumentException("[renteAccordee] can't be null");
        }
        return renteAccordee.comporteCodeCasSpecial(this);
    }

    public Integer getCodeSysteme() {
        return codeSysteme;
    }
}
