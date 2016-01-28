package globaz.corvus.db.rentesverseesatort.wrapper;

import java.math.BigDecimal;

/**
 * Contient toutes les informations nécessaire concernant une prestation due pour le calcul de rentes versées à tort par
 * {@link RERenteVerseeATortUtil}
 * 
 * @see RECalculRentesVerseesATortWrapper
 * @author PBA
 */
public class REPrestationDuePourCalculRenteVerseeATort {

    public static REPrestationDuePourCalculRenteVerseeATort creerPrestationDue(Long idPrestationDue,
            Long idRenteAccordee, String dateDebutPaiement, String dateFinPaiement, BigDecimal montant) {
        REPrestationDuePourCalculRenteVerseeATort prestationDue = new REPrestationDuePourCalculRenteVerseeATort();
        prestationDue.setIdPrestationDue(idPrestationDue);
        prestationDue.setIdRenteAccordee(idRenteAccordee);
        prestationDue.setDateDebutPaiement(dateDebutPaiement);
        prestationDue.setDateFinPaiement(dateFinPaiement);
        prestationDue.setMontant(montant);
        return prestationDue;
    }

    private String dateDebutPaiement;
    private String dateFinPaiement;
    private Long idPrestationDue;
    private Long idRenteAccordee;
    private BigDecimal montant;

    public REPrestationDuePourCalculRenteVerseeATort() {
        super();

        dateDebutPaiement = null;
        dateFinPaiement = null;
        idPrestationDue = null;
        idRenteAccordee = null;
        montant = null;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof REPrestationDuePourCalculRenteVerseeATort) {
            REPrestationDuePourCalculRenteVerseeATort uneAutrePrestationDue = (REPrestationDuePourCalculRenteVerseeATort) obj;
            return ((idPrestationDue == null) && (uneAutrePrestationDue.idPrestationDue == null))
                    || ((idPrestationDue != null) && idPrestationDue.equals(uneAutrePrestationDue.idPrestationDue));
        }
        return false;
    }

    public String getDateDebutPaiement() {
        return dateDebutPaiement;
    }

    public String getDateFinPaiement() {
        return dateFinPaiement;
    }

    public Long getIdPrestationDue() {
        return idPrestationDue;
    }

    public Long getIdRenteAccordee() {
        return idRenteAccordee;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    @Override
    public int hashCode() {
        StringBuilder hashCodeBuilder = new StringBuilder();

        hashCodeBuilder.append(this.getClass().getName()).append("(").append(idPrestationDue).append(")");

        return hashCodeBuilder.toString().hashCode();
    }

    public void setDateDebutPaiement(String dateDebutPaiement) {
        this.dateDebutPaiement = dateDebutPaiement;
    }

    public void setDateFinPaiement(String dateFinPaiement) {
        this.dateFinPaiement = dateFinPaiement;
    }

    public void setIdPrestationDue(Long idPrestationDue) {
        this.idPrestationDue = idPrestationDue;
    }

    public void setIdRenteAccordee(Long idRenteAccordee) {
        this.idRenteAccordee = idRenteAccordee;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }
}
