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
 * Objet immuable contenant le détail complet du calcul d'une rente versée à tort
 * </p>
 * <p>
 * Toutes les données contenues dans cet objet ne peuvent pas être <code>null</code> car le constructeur vérifie chaque
 * entrée
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
     * Construit cet objet en vérifiant que tous les paramètres ne soient pas null, si un des paramètre est null une
     * {@link IllegalArgumentException} sera levée
     * 
     * @param idRenteVerseeATort
     *            l'ID en BDD de cette rente versée à tort
     * @param idRenteAccordeeNouveauDroit
     *            l'ID en BDD de la rente de l'ancien droit utilisée pour le calcul de cette rente versée à tort
     * @param idRenteAccordeeAncienDroit
     *            l'ID en BDD de la rente du nouveau droit utilisée pour le calcul de cette rente versée à tort
     * @param idDemandeRente
     *            l'ID de la demande dans laquel est liée cette rente versée à tort
     * @param lignesDetail
     *            le détail du calcul de cette rente versée à tort, prestation due par prestation due
     * @param idTiers
     *            l'ID du tiers à qui on a trop payé
     * @param nss
     *            le NSS du tiers à qui on a trop payé
     * @param prenom
     *            le prénom du tiers à qui on a trop payé
     * @param nom
     *            le nom du tiers à qui on a trop payé
     * @param dateNaissance
     *            la date de naissance du tiers à qui on a trop payé
     * @param dateDeces
     *            la date de décès du tiers à qui on a trop payé
     * @param sexe
     *            le sexe (au format texte) du tiers à qui on a trop payé
     * @param nationalite
     *            la nationalité (au format texte) du tiers à qui on a trop payé
     * @param type
     *            le type de rente versée à tort
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
     * @return la plus petite date de début des prestations dues contenues dans le détail du calcul<br/>
     *         Si aucune date de début n'est définie dans ces prestations dues, retournera <code>null</code>
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
     * @return la date de décès du tiers à qui on a trop payé
     */
    public String getDateDeces() {
        return dateDeces;
    }

    /**
     * @return la plus grande date de fin des prestations dues contenues dans le détail du calcul<br/>
     *         Si aucune date de fin n'est définie dans les prestations dues, retournera <code>null</code>
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
     * @return la date de naissance du tiers à qui on a trop payé
     */
    public String getDateNaissance() {
        return dateNaissance;
    }

    /**
     * @return l'ID (en BDD) de la demande à laquelle est liée à cette rente versée à tort
     */
    public Long getIdDemandeRente() {
        return idDemandeRente;
    }

    /**
     * @return l'ID (en BDD) de la rente de l'ancien droit ayant été utilisée pour le calcul de cette rente versée à
     *         tort
     */
    public Long getIdRenteAccordeeAncienDroit() {
        return idRenteAccordeeAncienDroit;
    }

    /**
     * @return l'ID (en BDD) de la rente du nouveau droit ayant été utilisée pour le calcul de cette rente versée à tort
     */
    public Long getIdRenteAccordeeNouveauDroit() {
        return idRenteAccordeeNouveauDroit;
    }

    /**
     * @return l'ID (en BDD) de cette rente versée à tort
     */
    public Long getIdRenteVerseeATort() {
        return idRenteVerseeATort;
    }

    /**
     * @return l'ID tiers sur qui est lié cette rente versée à tort
     */
    public Long getIdTiers() {
        return idTiers;
    }

    /**
     * @return le détail du calcul de cette rente versée à tort dans une liste immuable
     */
    public List<RELigneDetailCalculRenteVerseeATort> getLignesDetail() {
        return Collections.unmodifiableList(lignesDetail);
    }

    /**
     * @return le montant total versée à tort (somme des prestations dues fois le nombre de mois versée à tort)
     */
    public BigDecimal getMontantTotalVerseeATort() {
        BigDecimal sommeTotal = BigDecimal.ZERO;
        for (RELigneDetailCalculRenteVerseeATort uneLigne : lignesDetail) {
            sommeTotal = sommeTotal.add(uneLigne.getMontantTotalPourLaPeriode());
        }
        return sommeTotal;
    }

    /**
     * @return la nationalité (au format texte) du tiers à qui on a trop payé (traduction selon la langue du
     *         gestionnaire)
     */
    public String getNationalite() {
        return nationalite;
    }

    /**
     * @return le nom du tiers à qui on a trop payé
     */
    public String getNom() {
        return nom;
    }

    /**
     * @return le NSS du tiers à qui on a trop payé
     */
    public NumeroSecuriteSociale getNss() {
        return nss;
    }

    /**
     * @return le prénom du tiers à qui on a trop payé
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * @return le sexe (au format texte) du tiers à qui on a trop payé
     */
    public String getSexe() {
        return sexe;
    }

    /**
     * @return le type de cette rente versée à tort
     */
    public TypeRenteVerseeATort getType() {
        return type;
    }
}
