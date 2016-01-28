package ch.globaz.corvus.utils.rentesverseesatort;

import java.util.Collection;
import ch.globaz.common.domaine.Checkers;
import ch.globaz.pyxis.domaine.NumeroSecuriteSociale;

/**
 * <p>
 * Objet immuable contenant les donn�es pour l'affichage des informations concernant une personne dans l'�cran des
 * rentes vers�e � tort
 * </p>
 * <p>
 * Toutes les donn�es contenues dans cet objet sont v�rifi�es � la cr�ation de celui-ci et ne peuvent pas �tre
 * <code>null</code>
 * </p>
 */
public class REApercuRenteVerseeATort {

    private String dateDeces;
    private String dateNaissance;
    private Long idTiers;
    private Collection<RELigneApercuRenteVerseeATort> lignesApercu;
    private String nationalite;
    private String nom;
    private NumeroSecuriteSociale nss;
    private String prenom;
    private String sexe;

    /**
     * Construit un objet en v�rifiant qu'aucun des param�tres pass�s n'est null
     * 
     * @param idTiers
     *            l'id du tiers auquel on a pay� des rentes en trop
     * @param nom
     *            le nom du tiers auquel on a pay� des rentes en trop
     * @param prenom
     *            le pr�nom du tiers auquel on a pay� des rentes en trop
     * @param nss
     *            le num�ro de s�curit� sociale du tiers auquel on a pay� des rentes en trop
     * @param dateNaissance
     *            la date de naissance du tiers auquel on a pay� des rentes en trop
     * @param dateDeces
     *            la date de d�c�s du tiers auquel on a pay� des rentes en trop
     * @param nationalite
     *            la nationalit� (pas sous forme d'ID, mais texte) du tiers auquel on a pay� des rentes en trop
     * @param sexe
     *            le sexe (pas en code syst�me, mais en texte) du tiers auquel on a pay� des rentes en trop
     * @param lignesApercu
     *            les diff�rentes rente vers�e � tort du tiers, ne peut pas �tre vide
     */
    public REApercuRenteVerseeATort(Long idTiers, String nom, String prenom, NumeroSecuriteSociale nss,
            String dateNaissance, String dateDeces, String nationalite, String sexe,
            Collection<RELigneApercuRenteVerseeATort> lignesApercu) {
        super();

        Checkers.checkNotNull(idTiers, "idTiers");
        Checkers.checkNotNull(nom, "nom");
        Checkers.checkNotNull(prenom, "prenom");
        Checkers.checkNotNull(nss, "nss");
        Checkers.checkNotNull(dateNaissance, "dateNaissance");
        Checkers.checkNotNull(dateDeces, "dateDeces");
        Checkers.checkNotNull(nationalite, "nationalite");
        Checkers.checkNotNull(lignesApercu, "lignesApercu");
        Checkers.checkNotEmpty(lignesApercu, "lignesApercu");
        Checkers.checkNotNull(sexe, "sexe");

        this.dateDeces = dateDeces;
        this.dateNaissance = dateNaissance;
        this.idTiers = idTiers;
        this.lignesApercu = lignesApercu;
        this.nationalite = nationalite;
        this.nom = nom;
        this.prenom = prenom;
        this.nss = nss;
        this.sexe = sexe;
    }

    /**
     * @return la date de d�c�s du tiers auquel on a pay� des rentes en trop
     */
    public String getDateDeces() {
        return dateDeces;
    }

    /**
     * @return la date de naissance du tiers auquel on a pay� des rentes en trop
     */
    public String getDateNaissance() {
        return dateNaissance;
    }

    /**
     * @return l'ID du tiers auquel on a pay� des rentes en trop
     */
    public Long getIdTiers() {
        return idTiers;
    }

    /**
     * @return les diff�rentes rentes vers�e � tort du tiers
     */
    public Collection<RELigneApercuRenteVerseeATort> getLignesApercu() {
        return lignesApercu;
    }

    /**
     * @return la nationalit� du tiers auquel on a pay� des rentes en trop (au format texte, ce n'est pas d'ID ou de
     *         code syst�me, la traduction d�pend de la langue du gestionnaire)
     */
    public String getNationalite() {
        return nationalite;
    }

    /**
     * @return le nom du tiers auquel on a pay� des rentes en trop
     */
    public String getNom() {
        return nom;
    }

    /**
     * @return le NSS du tiers auquel on a pay� des rentes en trop
     */
    public NumeroSecuriteSociale getNss() {
        return nss;
    }

    /**
     * @return le pr�nom du tiers auquel on a pay� des rentes en trop
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * @return le sexe du tiers auquel on a pay� des rentes en trop (au format texte, ce n'est pas un cod syst�me, la
     *         traduction d�pend de la langue du gestionnaire)
     */
    public String getSexe() {
        return sexe;
    }
}
