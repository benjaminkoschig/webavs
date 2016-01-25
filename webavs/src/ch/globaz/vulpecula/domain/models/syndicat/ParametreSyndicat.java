package ch.globaz.vulpecula.domain.models.syndicat;

import java.util.List;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.DomainEntity;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.specifications.registre.PSMontantParTravailleurCorrecte;
import ch.globaz.vulpecula.domain.specifications.registre.PSPeriodeValide;
import ch.globaz.vulpecula.domain.specifications.registre.PSPourcentageCorrecte;
import ch.globaz.vulpecula.external.models.pyxis.Administration;

public class ParametreSyndicat implements DomainEntity, Comparable<ParametreSyndicat> {
    private String id;
    private Administration syndicat;
    private Administration caisseMetier;
    private Taux pourcentage;
    private Montant montantParTravailleur;
    private Date dateDebut;
    private Date dateFin;
    private String spy;

    public ParametreSyndicat() {
        syndicat = new Administration();
        caisseMetier = new Administration();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getIdSyndicat() {
        return syndicat.getId();
    }

    public Administration getSyndicat() {
        return syndicat;
    }

    public void setSyndicat(Administration syndicat) {
        this.syndicat = syndicat;
    }

    public Administration getCaisseMetier() {
        return caisseMetier;
    }

    public void setCaisseMetier(Administration caisseMetier) {
        this.caisseMetier = caisseMetier;
    }

    public void setIdCaisseMetier(String idCaisseMetier) {
        caisseMetier.setId(idCaisseMetier);
    }

    public String getIdCaisseMetier() {
        return caisseMetier.getId();
    }

    public Taux getPourcentage() {
        return pourcentage;
    }

    public void setPourcentage(Taux pourcentage) {
        this.pourcentage = pourcentage;
    }

    public Montant getMontantParTravailleur() {
        return montantParTravailleur;
    }

    public void setMontantParTravailleur(Montant montantParTravailleur) {
        this.montantParTravailleur = montantParTravailleur;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    @Override
    public String getSpy() {
        return spy;
    }

    @Override
    public void setSpy(String spy) {
        this.spy = spy;
    }

    public String getLibelleCaisseMetier() {
        return caisseMetier.getDesignation1();
    }

    public void validate() throws UnsatisfiedSpecificationException {
        PSPourcentageCorrecte pourcentageCorrecte = new PSPourcentageCorrecte();
        PSMontantParTravailleurCorrecte montantParTravailleurCorrecte = new PSMontantParTravailleurCorrecte();
        PSPeriodeValide periodeValide = new PSPeriodeValide();

        pourcentageCorrecte.and(montantParTravailleurCorrecte).and(periodeValide).isSatisfiedBy(this);
    }

    @Override
    public int compareTo(ParametreSyndicat other) {
        Periode periode = new Periode(dateDebut, dateFin);
        Periode otherPeriode = new Periode(other.dateDebut, other.dateFin);
        return periode.compareTo(otherPeriode);
    }

    public ParametreSyndicat chevauche(List<ParametreSyndicat> parametresSyndicats) {
        Periode periodeActuelle = new Periode(dateDebut, dateFin);
        for (ParametreSyndicat parametreSyndicat : parametresSyndicats) {
            Periode periode = new Periode(parametreSyndicat.getDateDebut(), parametreSyndicat.getDateFin());
            if (periodeActuelle.chevauche(periode)) {
                return parametreSyndicat;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "ParametreSyndicat [id=" + id + ", syndicat=" + syndicat + ", caisseMetier=" + caisseMetier
                + ", pourcentage=" + pourcentage + ", montantParTravailleur=" + montantParTravailleur + ", dateDebut="
                + dateDebut + ", dateFin=" + dateFin + ", spy=" + spy + "]";
    }
}
