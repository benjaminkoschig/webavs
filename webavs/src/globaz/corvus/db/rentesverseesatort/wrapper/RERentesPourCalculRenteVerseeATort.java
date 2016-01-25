package globaz.corvus.db.rentesverseesatort.wrapper;

import java.util.Set;
import ch.globaz.corvus.domaine.RenteAccordee;
import ch.globaz.corvus.utils.rentesverseesatort.RECalculRentesVerseesATort;
import ch.globaz.prestation.domaine.CodePrestation;

/**
 * Contient toutes les informations n�cessaire concernant une rente accord�e pour le calcul de rentes vers�es � tort par
 * {@link RECalculRentesVerseesATort}
 * 
 * @see RECalculRentesVerseesATortWrapper
 * @author PBA
 */
public class RERentesPourCalculRenteVerseeATort {

    public static RERentesPourCalculRenteVerseeATort creerRenteAncienDroit(Long idRente, String dateDebutDroit,
            String dateFinDroit, CodePrestation codePrestation, Long idTiersBeneficiaire,
            Set<REPrestationDuePourCalculRenteVerseeATort> prestationsDues) {
        RERentesPourCalculRenteVerseeATort ancienneRente = RERentesPourCalculRenteVerseeATort.creerRenteNouveauDroit(
                idRente, dateDebutDroit, dateFinDroit, codePrestation, idTiersBeneficiaire);
        ancienneRente.setPrestationsDues(prestationsDues);
        return ancienneRente;
    }

    public static RERentesPourCalculRenteVerseeATort creerRenteNouveauDroit(Long idRente, String dateDebutDroit,
            String dateFinDroit, CodePrestation codePrestation, Long idTiersBeneficiaire) {
        RERentesPourCalculRenteVerseeATort nouvelleRente = new RERentesPourCalculRenteVerseeATort();
        nouvelleRente.setIdRenteAccordee(idRente);
        nouvelleRente.setDateDebutDroit(dateDebutDroit);
        nouvelleRente.setDateFinDroit(dateFinDroit);
        nouvelleRente.setCodePrestation(codePrestation);
        nouvelleRente.setIdTiersBeneficiaire(idTiersBeneficiaire);
        return nouvelleRente;
    }

    private CodePrestation codePrestation;
    private String dateDebutDroit;
    private String dateFinDroit;
    private Long idRenteAccordee;
    private Long idRenteVerseeATort;
    private Long idTiersBeneficiaire;
    private Set<REPrestationDuePourCalculRenteVerseeATort> prestationsDues;

    public RERentesPourCalculRenteVerseeATort() {
        super();

        codePrestation = null;
        dateDebutDroit = null;
        dateFinDroit = null;
        idRenteAccordee = null;
        idRenteVerseeATort = null;
        idTiersBeneficiaire = null;
        prestationsDues = null;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RERentesPourCalculRenteVerseeATort) {
            RERentesPourCalculRenteVerseeATort uneAutreRente = (RERentesPourCalculRenteVerseeATort) obj;
            return ((idRenteAccordee == null) && (uneAutreRente.idRenteAccordee == null))
                    || ((idRenteAccordee != null) && idRenteAccordee.equals(uneAutreRente.idRenteAccordee));
        }
        return false;
    }

    public CodePrestation getCodePrestation() {
        return codePrestation;
    }

    public String getDateDebutDroit() {
        return dateDebutDroit;
    }

    public String getDateFinDroit() {
        return dateFinDroit;
    }

    public Long getIdRenteAccordee() {
        return idRenteAccordee;
    }

    public Long getIdRenteVerseeATort() {
        return idRenteVerseeATort;
    }

    public Long getIdTiersBeneficiaire() {
        return idTiersBeneficiaire;
    }

    public Set<REPrestationDuePourCalculRenteVerseeATort> getPrestationsDues() {
        return prestationsDues;
    }

    @Override
    public int hashCode() {
        StringBuilder hashCodeBuilder = new StringBuilder();
        hashCodeBuilder.append(this.getClass().getName()).append("(").append(idRenteAccordee).append(")");
        return hashCodeBuilder.toString().hashCode();
    }

    /**
     * Permet de d�finir si la rente pass�e en param�tre fait parti du m�me groupe de prestation (AVS/AI ou API) afin de
     * determiner si la rente doit �tre prise dans le calcul d'une rente vers�e � tort dans
     * {@link RECalculRentesVerseesATort}
     * 
     * @param uneAutreRente
     * @return
     */
    public boolean isMemeFamilleGenrePrestation(RERentesPourCalculRenteVerseeATort uneAutreRente) {
        if ((codePrestation != null) && (uneAutreRente != null) && (uneAutreRente.codePrestation != null)) {

            /*
             * Il �tait trop risqu� de refactor tout le calcul des rentes vers�e � tort quand j'ai d� utiliser le code
             * de ce wrapper (sinon cette classe aurait disparu au profit de l'entit� de domaine)
             * Du coup ce code n'est pas propre, mais fonctionnel. Il serait bon de refactor le calcul des rentes
             * vers�es afin que �a utilise les entit�s de domaines et plus les entit�s sp�cifiques
             */
            RenteAccordee wrapperRente = new RenteAccordee();
            wrapperRente.setCodePrestation(codePrestation);

            RenteAccordee wrapperUnAutreRente = new RenteAccordee();
            wrapperUnAutreRente.setCodePrestation(uneAutreRente.codePrestation);

            return wrapperRente.estDeLaMemeFamilleDePrestationQue(wrapperUnAutreRente);
        }
        return false;
    }

    public void setCodePrestation(CodePrestation codePrestation) {
        this.codePrestation = codePrestation;
    }

    public void setDateDebutDroit(String dateDebutDroit) {
        this.dateDebutDroit = dateDebutDroit;
    }

    public void setDateFinDroit(String dateFinDroit) {
        this.dateFinDroit = dateFinDroit;
    }

    public void setIdRenteAccordee(Long idRenteAccordee) {
        this.idRenteAccordee = idRenteAccordee;
    }

    public void setIdRenteVerseeATort(Long idRenteVerseeATort) {
        this.idRenteVerseeATort = idRenteVerseeATort;
    }

    public void setIdTiersBeneficiaire(Long idTiersBeneficiaire) {
        this.idTiersBeneficiaire = idTiersBeneficiaire;
    }

    public void setPrestationsDues(Set<REPrestationDuePourCalculRenteVerseeATort> prestationsDues) {
        this.prestationsDues = prestationsDues;
    }
}
