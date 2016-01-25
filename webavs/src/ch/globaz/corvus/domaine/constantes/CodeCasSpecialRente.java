package ch.globaz.corvus.domaine.constantes;

import globaz.jade.client.util.JadeStringUtil;
import ch.globaz.corvus.domaine.RenteAccordee;

/**
 * <p>
 * Code cas spéciaux des rentes accordées (permet de décrire un cas particulier métier par l'intermédiaire d'un code).
 * </p>
 * <p>
 * Tiré de l'annexe V du document
 * "Directives concernant les rentes (DR) de l'assurance vieillesse, survivants et invalidité fédérale" (état au 1er
 * janvier 2014)<br/>
 * <a href="http://www.bsv.admin.ch/vollzug/documents/view/75/lang:fre/category:23">Le document sur le site de la
 * confédération</a>
 * </p>
 */
public enum CodeCasSpecialRente {

    /** Prestation réduite pour faute grave de l'assuré */
    CODE_CAS_SPECIAL_01(52822001),
    /** Rente d'orphelin ou pour enfant réduite en raison de la surassurance */
    CODE_CAS_SPECIAL_02(52822002),
    /**
     * Rente d'invalidité ou allocation pour impotent de l'AI momentanément réduite pour violation légère ou grave de
     * ses obligations
     */
    CODE_CAS_SPECIAL_03(52822003),
    /** Rente de vieillesse réduite en proportion du montant de la rente d'invalidité norvégienne */
    CODE_CAS_SPECIAL_04(52822004),
    /** Rente plafonnée */
    CODE_CAS_SPECIAL_05(52822005),
    /** Montant différentiel de la Caisse suisse sous la forme d'une rente pour enfant ou d'orphelin */
    CODE_CAS_SPECIAL_06(52822006),
    /** La rente AI suspendue durant l'exécution d'une peine privative de liberté ou d'une mesure */
    CODE_CAS_SPECIAL_07(52822007),
    /** Rente de vieillesse ajournée dont l'ajournement n'a pas encore été révoqué */
    CODE_CAS_SPECIAL_08(52822008),
    /**
     * Rente extraordinaire d'invalides de naissance ou depuis leur enfance avec supplément ainsi que les rentes AVS
     * ordinaires qui leur succèdent
     */
    CODE_CAS_SPECIAL_21(52822021),
    /** Rente ordinaire d'invalides précoces avec montant minimum majoré ainsi que les rentes AVS qui leur succèdent */
    CODE_CAS_SPECIAL_22(52822022),
    /** Rente AI d'un montant équivalent à celui de la rente virtuelle de survivant */
    CODE_CAS_SPECIAL_23(52822023),
    /**
     * Rente pour enfant de l'AVS d'un montant équivalent à celui de la rente d'orphelin supprimée ainsi que la rente
     * d'orphelin double servie au montant de l'ancienne rente d'orphelin de mère
     */
    CODE_CAS_SPECIAL_24(52822024),
    /**
     * Garantie des droits acquis dès le 1er janvier 1964 (rente partielle reposant sur une ancienne cotisation annuelle
     * moyenne «A»)
     */
    CODE_CAS_SPECIAL_28(52822028),
    /**
     * Rente pour cas pénible en cours sur un degré d'invalidité inférieur à 50 pour cent (garantie des droits acquis
     * dès le 1er janvier 2004)
     */
    CODE_CAS_SPECIAL_29(52822029),
    /**
     * Rente AI entière reposant sur un degré d'invalidité inférieur à 70 pour cent: garantie des droits acquis pour
     * personnes âgées de plus de 50 ans
     */
    CODE_CAS_SPECIAL_30(52822030),
    /**
     * Rente transférée d'une personne veuve remariée avec montant égal à celui de la rente calculée selon les
     * dispositions de la 9e révision de l'AVS
     */
    CODE_CAS_SPECIAL_31(52822031),
    /** Garantie des droits acquis dès le 1er janvier 1979 */
    CODE_CAS_SPECIAL_32(52822032),
    /**
     * Ancienne rente servie dans un cas pénible, qui repose sur un degré d'invalidité inférieur à 40 pour cent
     * (garantie des droits acquis dès le 1er janvier 1988)
     */
    CODE_CAS_SPECIAL_34(52822034),
    /**
     * Rente d'orphelin d'un montant équivalent à celui de la rente d'orphelin déterminée selon les dispositions de la
     * 9e révision de l'AVS
     */
    CODE_CAS_SPECIAL_36(52822036),
    /** Trois-quarts de rente AI sur un degré d'invalidité inférieur à 60 pour cent */
    CODE_CAS_SPECIAL_37(52822037),
    /** Rente AI entière reposant sur un degré d'invalidité inférieur à 70 pour cent */
    CODE_CAS_SPECIAL_38(52822038),
    /** Demi-rente AI reposant sur un degré d'invalidité inférieur à 50 pour cent */
    CODE_CAS_SPECIAL_39(52822039),

    CODE_CAS_SPECIAL_40(52822040),
    /** Rente déterminée en tenant compte des durées de cotisations espagnoles */
    CODE_CAS_SPECIAL_44(52822044),
    /** Rente déterminée en tenant compte des durées de cotisations néerlandaises */
    CODE_CAS_SPECIAL_45(52822045),
    /** Rente déterminée en tenant compte des durées de cotisations turques */
    CODE_CAS_SPECIAL_46(52822046),
    /**
     * Rente extraordinaire en cas de minimum garanti, lorsque le montant de la rente ordinaire remplacée ne correspond
     * pas à la valeur des tables (rente ordinaire réduite ou augmentée)
     */
    CODE_CAS_SPECIAL_47(52822047),
    /** Rente déterminée en tenant compte des durées de cotisations grecques */
    CODE_CAS_SPECIAL_48(52822048),
    /** Rente déterminée en tenant compte des durées de cotisations françaises */
    CODE_CAS_SPECIAL_49(52822049),
    /** Rente déterminée en tenant compte des durées de cotisations portugaises */
    CODE_CAS_SPECIAL_50(52822050),
    /** Rente déterminée en tenant compte des durées de cotisations belges */
    CODE_CAS_SPECIAL_51(52822051),
    /** Rente déterminée en tenant compte des durées de cotisations norvégiennes */
    CODE_CAS_SPECIAL_52(52822052),
    /** Rente déterminée en tenant compte des durées de cotisations britanniques */
    CODE_CAS_SPECIAL_53(52822053),
    /** Rente pour enfant déterminée en tenant compte de périodes d'assurances étrangères */
    CODE_CAS_SPECIAL_54(52822054),
    /** Rente AVS/AI avec périodes d'assurances UE/AELE inférieures à une année */
    CODE_CAS_SPECIAL_55(52822055),
    /**
     * Rente sujette à mutation dès le 1er janvier 1979, pour laquelle les mois d'appoint ont été pris en compte en
     * vertu des règles applicables avant cette date
     */
    CODE_CAS_SPECIAL_61(52822061),
    /**
     * Introduction de l'échelle de rentes linéaire. Garantie des droits acquis de l'échelle de rentes jusqu'ici
     * déterminante
     */
    CODE_CAS_SPECIAL_63(52822063),
    /** Garantie des droits acquis selon l'Avenant à la Convention avec la Principauté du Liechtenstein */
    CODE_CAS_SPECIAL_78(52822078),
    /**
     * Rente de vieillesse avec complément différentiel jusqu'à concurrence du montant de l'ancienne rente AI déterminée
     * en tenant compte des périodes de cotisations françaises
     */
    CODE_CAS_SPECIAL_79(52822079),
    /**
     * Rente sujette à mutation dès le 1er janvier 1997, pour laquelle l'échelle a été déterminée en vertu des règles
     * applicables avant cette date
     */
    CODE_CAS_SPECIAL_80(52822080),
    /** Rente avec supplément d'ajournement selon les dispositions de la 9e révision de l'AVS */
    CODE_CAS_SPECIAL_81(52822081),
    /** Rente transférée, changement de registre sans modification des bases de calcul */
    CODE_CAS_SPECIAL_82(52822082),
    /** Rente AI pas encore révisée en raison de la 4e révision de l'AI */
    CODE_CAS_SPECIAL_83(52822083),
    /** Prestation transitoire sous forme de rente AI */
    CODE_CAS_SPECIAL_84(52822084),
    /** Prestation réduite pour d'autres raisons */
    CODE_CAS_SPECIAL_91(52822091),
    /** Prestation augmentée pour d'autres raisons */
    CODE_CAS_SPECIAL_92(52822092),
    /**
     * Prestation allouée ou déterminée - pour d'autres raisons - en vertu d'une réglementation spéciale (montant
     * mensuel = valeur des tables)
     */
    CODE_CAS_SPECIAL_93(52822093),
    /** Allocation pour impotent (devenue caduque) de l'AVS/AI, dont le montant est versé à un assureur-accidents */
    CODE_CAS_SPECIAL_99(52822099),
    /**
     * Il s'est avéré que des codes cas spéciaux ont été retirés des directives sur les rentes mais sont toujours
     * présent dans l'historique de certain client (le code cas spécial 27 par exemple). Pour pallier à une erreur
     * lorsqu'on devra recalculer ces cas, un code cas spécial inconnu a été ajouté afin d'être retourné quand on ne
     * connaît pas le code système du code cas spécial
     */
    CODE_CAS_SPECIAL_INCONNU(0);

    /**
     * Retourne l'énuméré correspondant au code système. </br><strong>Renvoie une {@link IllegalArgumentException} si la
     * chaîne de caractère passée en paramètre est invalide (null ou vide) ou si le code système ne correspond pas à un
     * valeur de cette énuméré.</strong>
     * 
     * @param codeSysteme la valeur du code système à rechercher
     * @throws IllegalArgumentException si le paramètre codeSystem est null, une chaîne vide ou ne correspond pas à une
     *             valeur connue
     * @return l'énuméré correspondant au code système. </br><strong>Si la valeur n'est pas trouvée une
     *         IllegalArgumentException sera lancée</strong>
     */
    public static CodeCasSpecialRente parse(final String codeSysteme) {
        if (!JadeStringUtil.isDigit(codeSysteme)) {
            throw new IllegalArgumentException("The value [" + codeSysteme
                    + "] is not valid for the systemCode of type [" + CodeCasSpecialRente.class.getName() + "]");
        }
        if (codeSysteme.length() == 2) {
            // FIXME LGA 02.2015 : WTF ??? C'est quoi cette complétion du code système ?
            return CodeCasSpecialRente.valueOf(Integer.parseInt("528220" + codeSysteme));
        }
        return CodeCasSpecialRente.valueOf(Integer.parseInt(codeSysteme));
    }

    /**
     * Retourne l'énuméré correspondant au code système. </br><strong>Renvoie une {@link IllegalArgumentException} si le
     * paramètre <code>codeSysteme</code> est null
     * 
     * @param codeSysteme la valeur du code système à rechercher
     * @throws IllegalArgumentException si le paramètre codeSystem est null
     * @return l'énuméré correspondant au code système. </br><strong>Si la valeur n'est pas trouvée, l'énuméré
     *         CodeCasSpecialRente.CODE_CAS_SPECIAL_INCONNU sera retourné !!</strong>
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
     *            une rente accordée
     * @return vrai si le code cas spécial est présent dans cette rente accordée
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
