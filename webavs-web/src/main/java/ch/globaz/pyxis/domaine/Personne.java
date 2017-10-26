package ch.globaz.pyxis.domaine;

import ch.globaz.common.domaine.Checkers;

/**
 * <p>
 * Objet de domaine représentant une personne physique mais dont on ne sait pas si elle est déjà enregistrée dans le
 * registre des NSS
 * </p>
 * <p>
 * Comprend des informations propres aux personnes physiques comme la date de naissance, l'état civil ou la date de
 * décès
 * </p>
 */
public class Personne extends Tiers {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** JJ.MM.AAAA */
    private String dateDeces;
    /** JJ.MM.AAAA */
    private String dateNaissance;
    private Sexe sexe;
    private EtatCivil etatCivil = EtatCivil.UNDEFINED;

    public Personne() {
        super();
        dateDeces = "";
        dateNaissance = "";
        sexe = null;
    }

    public EtatCivil getEtatCivil() {
        return etatCivil;
    }

    public void setEtatCivil(EtatCivil etatCivil) {
        Checkers.checkNotNull(etatCivil, "personne.etatCivil");
        this.etatCivil = etatCivil;
    }

    /**
     * @return la date de décès de cette personne (format JJ.MM.AAAA) ou une chaîne vide si elle n'en a pas
     */
    public String getDateDeces() {
        return dateDeces;
    }

    public boolean isDecede() {
        return !(dateDeces == null || dateDeces.isEmpty());
    }

    /**
     * @return la date de naissance de cette personne (format JJ.MM.AAAA)
     */
    public String getDateNaissance() {
        return dateNaissance;
    }

    /**
     * @return le nom de famille de la personne
     */
    public String getNom() {
        return getDesignation1();
    }

    /**
     * @return le prénom de la personne
     */
    public String getPrenom() {
        return getDesignation2();
    }

    /**
     * @return le sexe de cette personne
     */
    public Sexe getSexe() {
        return sexe;
    }

    /**
     * (re-)défini la date de décès de cette personne (peut être vide)
     * 
     * @param dateDeces
     *            une date, sous forme de chaîne de caractère, au format JJ.MM.AAAA
     * @throws NullPointerException
     *             si la date passée en paramètre est null
     * @throws IllegalArgumentException
     *             si la date passée en paramètre n'est pas vide et n'est pas au bon format
     */
    public void setDateDeces(final String dateDeces) {
        Checkers.checkNotNull(dateDeces, "personne.dateDeces");
        Checkers.checkFullDate(dateDeces, "personne.dateDeces", true);
        this.dateDeces = dateDeces;
    }

    /**
     * (re-)défini la date de naissance de cette personne
     * 
     * @param dateNaissance
     *            une date, sous forme de chaîne de caractère, au format JJ.MM.AAAA, possibilité d'avoir une date au
     *            format 00.00.AAAA
     * @throws NullPointerException
     *             si la date passée en paramètre est null
     * @throws IllegalArgumentException
     *             si la date passée en paramètre est vide ou n'est pas au bon format
     */
    public void setDateNaissance(final String dateNaissance) {
        Checkers.checkNotNull(dateNaissance, "personne.dateNaissance");
        Checkers.checkDateAvs(dateNaissance, "personne.dateNaissance", true);
        this.dateNaissance = dateNaissance;
    }

    /**
     * (re-)défini le nom de famille de cette personne
     * 
     * @param nom
     *            une chaîne de caractère non nulle
     * @throws NullPointerException
     *             si le nom passé en paramètre est null
     */
    public void setNom(final String nom) {
        Checkers.checkNotNull(nom, "personne.nom");
        setDesignation1(nom);
    }

    /**
     * (re-)défini le prénom de cette personne
     * 
     * @param prenom
     *            une chaîne de caractère non nulle
     * @throws NullPointerException
     *             si le prénom passé en paramètre est null
     */
    public void setPrenom(final String prenom) {
        Checkers.checkNotNull(prenom, "personne.prenom");
        setDesignation2(prenom);
    }

    /**
     * (re-)défini le sexe de cette personne
     * 
     * @param sexe
     *            un énuméré représentant le sexe de cette personne
     * @throws NullPointerException
     *             si le sexe passé en paramètre est null
     */
    public void setSexe(final Sexe sexe) {
        Checkers.checkNotNull(sexe, "sexe");
        this.sexe = sexe;
    }
}
