package ch.globaz.corvus.utils.rentesverseesatort;

import globaz.jade.client.util.JadeDateUtil;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import ch.globaz.common.domaine.Checkers;
import ch.globaz.corvus.domaine.constantes.TypeRenteVerseeATort;
import ch.globaz.pyxis.domaine.NumeroSecuriteSociale;

/**
 * <p>
 * Objet immuable contenant le d�tail complet du calcul d'une rente vers�e � tort
 * </p>
 * <p>
 * Toutes les donn�es contenues dans cet objet ne peuvent pas �tre <code>null</code> car le constructeur v�rifie chaque
 * entr�e
 * </p>
 */
public class REDetailCalculRenteVerseeATort implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateDeces;
    private String dateNaissance;
    private Long idDemandeRente;
    private Long idRenteAccordeeAncienDroit;
    private Long idRenteAccordeeNouveauDroit;
    private Long idRenteVerseeATort;
    private Long idTiers;
    private List<RELigneDetailCalculRenteVerseeATort> lignesDetail;
    private String nationalite;
    private String nom;
    private NumeroSecuriteSociale nss;
    private String prenom;
    private String sexe;
    private TypeRenteVerseeATort type;

    /**
     * Construit cet objet en v�rifiant que tous les param�tres ne soient pas null, si un des param�tre est null une
     * {@link IllegalArgumentException} sera lev�e
     * 
     * @param idRenteVerseeATort
     *            l'ID en BDD de cette rente vers�e � tort
     * @param idRenteAccordeeNouveauDroit
     *            l'ID en BDD de la rente de l'ancien droit utilis�e pour le calcul de cette rente vers�e � tort
     * @param idRenteAccordeeAncienDroit
     *            l'ID en BDD de la rente du nouveau droit utilis�e pour le calcul de cette rente vers�e � tort
     * @param idDemandeRente
     *            l'ID de la demande dans laquel est li�e cette rente vers�e � tort
     * @param lignesDetail
     *            le d�tail du calcul de cette rente vers�e � tort, prestation due par prestation due
     * @param idTiers
     *            l'ID du tiers � qui on a trop pay�
     * @param nss
     *            le NSS du tiers � qui on a trop pay�
     * @param prenom
     *            le pr�nom du tiers � qui on a trop pay�
     * @param nom
     *            le nom du tiers � qui on a trop pay�
     * @param dateNaissance
     *            la date de naissance du tiers � qui on a trop pay�
     * @param dateDeces
     *            la date de d�c�s du tiers � qui on a trop pay�
     * @param sexe
     *            le sexe (au format texte) du tiers � qui on a trop pay�
     * @param nationalite
     *            la nationalit� (au format texte) du tiers � qui on a trop pay�
     * @param type
     *            le type de rente vers�e � tort
     */
    public REDetailCalculRenteVerseeATort(Long idRenteVerseeATort, Long idRenteAccordeeNouveauDroit,
            Long idRenteAccordeeAncienDroit, Long idDemandeRente,
            List<RELigneDetailCalculRenteVerseeATort> lignesDetail, Long idTiers, NumeroSecuriteSociale nss,
            String prenom, String nom, String dateNaissance, String dateDeces, String sexe, String nationalite,
            TypeRenteVerseeATort type) {
        super();

        Checkers.checkNotNull(idDemandeRente, "idDemandeRente");
        Checkers.checkNotNull(lignesDetail, "lignesDetail");
        Checkers.checkNotNull(idTiers, "idTiers");
        Checkers.checkNotNull(nss, "nss");
        Checkers.checkNotNull(prenom, "prenom");
        Checkers.checkNotNull(nom, "nom");
        Checkers.checkNotNull(dateNaissance, "dateNaissance");
        Checkers.checkNotNull(dateDeces, "dateDeces");
        Checkers.checkNotNull(sexe, "sexe");
        Checkers.checkNotNull(nationalite, "nationalite");
        Checkers.checkNotNull(type, "type");

        this.idRenteVerseeATort = idRenteVerseeATort;
        this.idRenteAccordeeNouveauDroit = idRenteAccordeeNouveauDroit;
        this.idRenteAccordeeAncienDroit = idRenteAccordeeAncienDroit;
        this.idDemandeRente = idDemandeRente;
        this.lignesDetail = lignesDetail;
        this.idTiers = idTiers;
        this.nss = nss;
        this.prenom = prenom;
        this.nom = nom;
        this.dateNaissance = dateNaissance;
        this.dateDeces = dateDeces;
        this.nationalite = nationalite;
        this.sexe = sexe;
        this.type = type;

        Collections.sort(this.lignesDetail, new Comparator<RELigneDetailCalculRenteVerseeATort>() {

            @Override
            public int compare(RELigneDetailCalculRenteVerseeATort o1, RELigneDetailCalculRenteVerseeATort o2) {
                return o1.getPeriode().compareTo(o2.getPeriode());
            }

        });
    }

    /**
     * @return la plus petite date de d�but des prestations dues contenues dans le d�tail du calcul<br/>
     *         Si aucune date de d�but n'est d�finie dans ces prestations dues, retournera <code>null</code>
     */
    public String getDateDebutPeriode() {
        if (lignesDetail == null) {
            return null;
        }
        String plusPetiteDate = "12.2999";

        for (RELigneDetailCalculRenteVerseeATort ligne : lignesDetail) {
            if (JadeDateUtil.isDateMonthYearBefore(ligne.getDateDebut(), plusPetiteDate)) {
                plusPetiteDate = ligne.getDateDebut();
            }
        }
        if (!"12.2999".equals(plusPetiteDate)) {
            return plusPetiteDate;
        }
        return null;
    }

    /**
     * @return la date de d�c�s du tiers � qui on a trop pay�
     */
    public String getDateDeces() {
        return dateDeces;
    }

    /**
     * @return la plus grande date de fin des prestations dues contenues dans le d�tail du calcul<br/>
     *         Si aucune date de fin n'est d�finie dans les prestations dues, retournera <code>null</code>
     */
    public String getDateFinPeriode() {
        if (lignesDetail == null) {
            return null;
        }
        String plusGrandeDate = "01.1970";

        for (RELigneDetailCalculRenteVerseeATort ligne : lignesDetail) {
            if (JadeDateUtil.isDateMonthYearAfter(ligne.getDateFin(), plusGrandeDate)) {
                plusGrandeDate = ligne.getDateFin();
            }
        }
        if (!"01.1970".equals(plusGrandeDate)) {
            return plusGrandeDate;
        }
        return null;
    }

    /**
     * @return la date de naissance du tiers � qui on a trop pay�
     */
    public String getDateNaissance() {
        return dateNaissance;
    }

    /**
     * @return l'ID (en BDD) de la demande � laquelle est li�e � cette rente vers�e � tort
     */
    public Long getIdDemandeRente() {
        return idDemandeRente;
    }

    /**
     * @return l'ID (en BDD) de la rente de l'ancien droit ayant �t� utilis�e pour le calcul de cette rente vers�e �
     *         tort
     */
    public Long getIdRenteAccordeeAncienDroit() {
        return idRenteAccordeeAncienDroit;
    }

    /**
     * @return l'ID (en BDD) de la rente du nouveau droit ayant �t� utilis�e pour le calcul de cette rente vers�e � tort
     */
    public Long getIdRenteAccordeeNouveauDroit() {
        return idRenteAccordeeNouveauDroit;
    }

    /**
     * @return l'ID (en BDD) de cette rente vers�e � tort
     */
    public Long getIdRenteVerseeATort() {
        return idRenteVerseeATort;
    }

    /**
     * @return l'ID tiers sur qui est li� cette rente vers�e � tort
     */
    public Long getIdTiers() {
        return idTiers;
    }

    /**
     * @return le d�tail du calcul de cette rente vers�e � tort dans une liste immuable
     */
    public List<RELigneDetailCalculRenteVerseeATort> getLignesDetail() {
        return Collections.unmodifiableList(lignesDetail);
    }

    /**
     * @return le montant total vers�e � tort (somme des prestations dues fois le nombre de mois vers�e � tort)
     */
    public BigDecimal getMontantTotalVerseeATort() {
        BigDecimal sommeTotal = BigDecimal.ZERO;
        for (RELigneDetailCalculRenteVerseeATort uneLigne : lignesDetail) {
            sommeTotal = sommeTotal.add(uneLigne.getMontantTotalPourLaPeriode());
        }
        return sommeTotal;
    }

    /**
     * @return la nationalit� (au format texte) du tiers � qui on a trop pay� (traduction selon la langue du
     *         gestionnaire)
     */
    public String getNationalite() {
        return nationalite;
    }

    /**
     * @return le nom du tiers � qui on a trop pay�
     */
    public String getNom() {
        return nom;
    }

    /**
     * @return le NSS du tiers � qui on a trop pay�
     */
    public NumeroSecuriteSociale getNss() {
        return nss;
    }

    /**
     * @return le pr�nom du tiers � qui on a trop pay�
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * @return le sexe (au format texte) du tiers � qui on a trop pay�
     */
    public String getSexe() {
        return sexe;
    }

    /**
     * @return le type de cette rente vers�e � tort
     */
    public TypeRenteVerseeATort getType() {
        return type;
    }
}
