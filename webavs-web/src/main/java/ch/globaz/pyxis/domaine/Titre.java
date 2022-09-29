package ch.globaz.pyxis.domaine;

import globaz.jade.client.util.JadeStringUtil;

public enum Titre {
    //Il existe plusieurs codes systèmes pour le même titre...
    //Codes tirés de ceux présent en DB qualité sur la CCB, table FWCOSP
    ASSOCIATION(19150024),
    Association(19200004),
    BUREAU(19150017),
    COMMUNAUTE(19150028),
    CONFIDENTIEL(19140012),
    //    CONFIDENTIEL(19150023),
    COPROPRIETE(19150021),
    Caisse105(19180016),
    Caisse99(19180017),
    Confidentiel(19180018),
    //    Confidentiel(19190004),
    DIRECTION(19150022),
    DOCTEUR(19120006),
    DOCTEURS(19120007),
    Direction(19180015),
    ENTREPRISE(19150018),
    Entreprise(19190005),
    //    Entreprise(19200005),
    Etude_Maitre(19180019),
    FIDUCIAIRE(19150019),
    FONDATION(19150025),
    FONDS_DE_PREVOYANCE(19150026),
    HOIRIE(502004),
    Immeuble(19180020),
    Leiter_Personnal(19180021),
    MADAME(502002),
    MADAME_ET_MESSIEURS(19130009),
    MADAME_ET_MONSIEUR(19130003),
    //    MADAME_ET_MONSIEUR(19150010),
    MADAME_LE_DOCTEUR(19150013),
    MADAME_MONSIEUR(19120003),
    MADEMOISELLE(19140013),
    //    MADEMOISELLE(19120005),
//    MADEMOISELLE(19130005),
//    MADEMOISELLE(19150007),
    MAITRE(19120008),
    //    MAITRE(19150016),
    MAITRES(19120009),
    MESDAMES(19120002),
    //    MESDAMES(19130002),
//    MESDAMES(19150008),
    MESDAMES_ET_MESSIEURS(19130004),
    MESDAMES_ET_MONSIEUR(19130011),
    MESDAMES_MESSIEURS(19120004),
    MESDEMOISELLES(19130006),
    MESSIEURS(19120001),
    //    MESSIEURS(19130001),
//    MESSIEURS(19150009),
//    MESSIEURS(19140014),
    MESSIEURS_ET_MADAME(19130010),
    MESSIEURS_ET_MESDAMES(19130007),
    MM_LES_DOCTEURS(19150015),
    MONSIEUR(502001),
    MONSIEUR_ET_MADAME(19130008),
    MONSIEUR_ET_MESDAMES(19130012),
    MONSIEUR_LE_DOCTEUR(19150014),
    Madame_Monsieur(19200006),
    Mademoiselle(19190002),
    //    Mademoiselle(19180023),
//    Mademoiselle(19200002),
    Maître(19180022),
    Mesdames(19200007),
    Messieurs(19190001),
    //    Messieurs(19200001),
    OPTIQUE(19130014),
    ORFEVRERIE(19130015),
    PAPETERIE(19130016),
    SERVICE_REGIONAL(19150027),
    SOCIETE(19150020),
    SUCCESSION_DE_FEU(19130013),
    SUCC_DE_MADAME(19150011),
    SUCC_DE_MONSIEUR(19150012),
    Succession_de_feu(19190003),
    //    Succession_de_feu(19180024),
//    Succession_de_feu(19200003),
    Technique_compta(19180014),
    Titre(10500002),

    UNDEFINED(0);

    private Integer codeSystem;

    Titre(int codeSystem) {
        this.codeSystem = codeSystem;
    }

    public Integer getCodeSysteme() {
        return codeSystem;
    }


    /**
     * Retourne l'énuméré correspondant au code système. </br><strong>Renvoie une {@link IllegalArgumentException} si la
     * chaîne de caractère passée en paramètre est invalide (null ou vide) ou si le code système ne correspond pas à un
     * valeur de cette énuméré.</strong>
     *
     * @param codeSysteme la valeur du code système à rechercher
     * @return l'énuméré correspondant au code système. </br><strong>Si la valeur n'est pas trouvée une
     * IllegalArgumentException sera lancée</strong>
     * @throws IllegalArgumentException si le paramètre codeSystem est null, une chaîne vide ou ne correspond pas à une
     *                                  valeur connue
     */
    public static ch.globaz.pyxis.domaine.Titre parse(String codeSysteme) {
        if (!JadeStringUtil.isDigit(codeSysteme)) {
            throw new IllegalArgumentException("The value [" + codeSysteme
                    + "] is not valid for the systemCode of type [" + ch.globaz.pyxis.domaine.Titre.class.getName() + "]");
        }

        Integer intCodeSysteme = Integer.parseInt(codeSysteme);
        return ch.globaz.pyxis.domaine.Titre.valueOf(intCodeSysteme);
    }


    /**
     * Retourne l'énuméré correspondant au code système.
     * </br><strong>Renvoie une {@link IllegalArgumentException} si la la valeur du paramètre <code>codeSystem</code>
     * est null ou si le code système ne correspond pas à une valeur de cette énuméré.</strong>
     *
     * @param codeSysteme la valeur du code système à rechercher
     * @return l'énuméré correspondant au code système. </br><strong>Si la valeur n'est pas trouvée une
     * IllegalArgumentException sera lancée</strong>
     * @throws IllegalArgumentException si le paramètre codeSystem est null, ou ne correspond pas à une
     *                                  valeur connue
     */
    public static ch.globaz.pyxis.domaine.Titre valueOf(Integer codeSysteme) {
        if (codeSysteme != null) {
            for (ch.globaz.pyxis.domaine.Titre titre : Titre.values()) {
                if (titre.getCodeSysteme().equals(codeSysteme)) {
                    return titre;
                }
            }
        }
        throw new IllegalArgumentException("The value [" + codeSysteme + "] is not valid for the systemCode of type ["
                + ch.globaz.pyxis.domaine.Titre.class.getName() + "]");
    }
}
