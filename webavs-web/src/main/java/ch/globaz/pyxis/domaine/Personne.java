package ch.globaz.pyxis.domaine;

import ch.globaz.common.domaine.Checkers;

/**
 * <p>
 * Objet de domaine repr�sentant une personne physique mais dont on ne sait pas si elle est d�j� enregistr�e dans le
 * registre des NSS
 * </p>
 * <p>
 * Comprend des informations propres aux personnes physiques comme la date de naissance, l'�tat civil ou la date de
 * d�c�s
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
     * @return la date de d�c�s de cette personne (format JJ.MM.AAAA) ou une cha�ne vide si elle n'en a pas
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
     * @return le pr�nom de la personne
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
     * (re-)d�fini la date de d�c�s de cette personne (peut �tre vide)
     * 
     * @param dateDeces
     *            une date, sous forme de cha�ne de caract�re, au format JJ.MM.AAAA
     * @throws NullPointerException
     *             si la date pass�e en param�tre est null
     * @throws IllegalArgumentException
     *             si la date pass�e en param�tre n'est pas vide et n'est pas au bon format
     */
    public void setDateDeces(final String dateDeces) {
        Checkers.checkNotNull(dateDeces, "personne.dateDeces");
        Checkers.checkFullDate(dateDeces, "personne.dateDeces", true);
        this.dateDeces = dateDeces;
    }

    /**
     * (re-)d�fini la date de naissance de cette personne
     * 
     * @param dateNaissance
     *            une date, sous forme de cha�ne de caract�re, au format JJ.MM.AAAA, possibilit� d'avoir une date au
     *            format 00.00.AAAA
     * @throws NullPointerException
     *             si la date pass�e en param�tre est null
     * @throws IllegalArgumentException
     *             si la date pass�e en param�tre est vide ou n'est pas au bon format
     */
    public void setDateNaissance(final String dateNaissance) {
        Checkers.checkNotNull(dateNaissance, "personne.dateNaissance");
        Checkers.checkDateAvs(dateNaissance, "personne.dateNaissance", true);
        this.dateNaissance = dateNaissance;
    }

    /**
     * (re-)d�fini le nom de famille de cette personne
     * 
     * @param nom
     *            une cha�ne de caract�re non nulle
     * @throws NullPointerException
     *             si le nom pass� en param�tre est null
     */
    public void setNom(final String nom) {
        Checkers.checkNotNull(nom, "personne.nom");
        setDesignation1(nom);
    }

    /**
     * (re-)d�fini le pr�nom de cette personne
     * 
     * @param prenom
     *            une cha�ne de caract�re non nulle
     * @throws NullPointerException
     *             si le pr�nom pass� en param�tre est null
     */
    public void setPrenom(final String prenom) {
        Checkers.checkNotNull(prenom, "personne.prenom");
        setDesignation2(prenom);
    }

    /**
     * (re-)d�fini le sexe de cette personne
     * 
     * @param sexe
     *            un �num�r� repr�sentant le sexe de cette personne
     * @throws NullPointerException
     *             si le sexe pass� en param�tre est null
     */
    public void setSexe(final Sexe sexe) {
        Checkers.checkNotNull(sexe, "sexe");
        this.sexe = sexe;
    }
}
