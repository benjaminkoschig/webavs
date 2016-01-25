package ch.globaz.vulpecula.domain.models.taxationoffice;

import java.io.Serializable;
import java.util.Locale;
import ch.globaz.vulpecula.domain.models.common.DomainEntity;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.external.models.affiliation.Cotisation;

/**
 * Réprésente une ligne de taxation dans une taxation d'office, soit un une cotisation son montant et son taux.
 * 
 * @author age
 * 
 */
public class LigneTaxation implements DomainEntity, Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private TaxationOffice taxationOffice;
    private Cotisation cotisation;
    private Montant masse;
    private Taux taux;
    private Montant montant;
    private String spy;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public TaxationOffice getTaxationOffice() {
        return taxationOffice;
    }

    public void setTaxationOffice(TaxationOffice taxationOffice) {
        this.taxationOffice = taxationOffice;
    }

    public Cotisation getCotisation() {
        return cotisation;
    }

    public void setCotisation(Cotisation cotisation) {
        this.cotisation = cotisation;
    }

    public Montant getMasse() {
        return masse;
    }

    public void setMasse(Montant masse) {
        this.masse = masse;
    }

    public Taux getTaux() {
        return taux;
    }

    public void setTaux(Taux taux) {
        this.taux = taux;
    }

    public Montant getMontant() {
        if (montant != null) {
            return montant;
        }
        return Montant.ZERO;
    }

    public void setMontant(Montant montant) {
        this.montant = montant;
    }

    public String getIdTaxationOffice() {
        return taxationOffice.getId();
    }

    public String getIdCotisation() {
        return cotisation.getId();
    }

    public String getMasseValue() {
        return masse.getValue();
    }

    public String getTauxValue() {
        return taux.getValue();
    }

    public String getMontantValue() {
        if (montant == null) {
            return Montant.ZERO.getValue();
        }
        return montant.getValue();
    }

    public String getAssuranceLibelle(Locale locale) {
        return cotisation.getAssuranceLibelle(locale);
    }

    @Override
    public String getSpy() {
        return spy;
    }

    @Override
    public void setSpy(String spy) {
        this.spy = spy;
    }

    /**
     * Retourne le montant de la ligne de taxation, soit la multiplication de la masse avec le taux.
     * 
     * @return Montant représentant le résultat de la multiplication,
     *         si la masse où le taux sont null, on retourne 0.
     */
    public Montant calculerMontant() {
        if (masse == null || taux == null) {
            return new Montant(0);
        }
        return masse.multiply(taux);
    }

    /**
     * Retourne le montant de la ligne de taxation, soit la mulitplication de la masse avec le taux.
     * 
     * @return double représentant le taux de la mulitplication
     *         si la masse où le taux sont null, on retourne 0.
     */
    public String getMontantCalculeeValue() {
        return calculerMontant().getValue();
    }
}
