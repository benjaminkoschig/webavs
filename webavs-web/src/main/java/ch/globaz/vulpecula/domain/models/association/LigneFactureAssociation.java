package ch.globaz.vulpecula.domain.models.association;

import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.DomainEntity;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.registre.CategorieFactureAssociationProfessionnelle;
import ch.globaz.vulpecula.domain.models.registre.TypeParamCotisationAP;
import ch.globaz.vulpecula.external.models.musca.Passage;
import ch.globaz.vulpecula.external.models.pyxis.Administration;

public class LigneFactureAssociation implements DomainEntity, Comparable<LigneFactureAssociation> {
    private String id;
    private String spy;

    private EnteteFactureAssociation enteteFacture;
    private Date periodeDebut;
    private Date periodeFin;
    private AssociationCotisation associationCotisation;
    private Montant fourchetteDebut = Montant.ZERO;
    private Montant fourchetteFin = Montant.ZERO;
    private Montant montantCotisation = Montant.ZERO;
    private TypeParamCotisationAP typeParametre;
    private Taux tauxCotisation;
    private Montant massePourCotisation = Montant.ZERO;
    private Double facteur;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getSpy() {
        return spy;
    }

    @Override
    public void setSpy(String spy) {
        this.spy = spy;
    }

    public EnteteFactureAssociation getEnteteFacture() {
        return enteteFacture;
    }

    public void setEnteteFacture(EnteteFactureAssociation enteteFacture) {
        this.enteteFacture = enteteFacture;
    }

    public Date getPeriodeDebut() {
        return periodeDebut;
    }

    public void setPeriodeDebut(Date periodeDebut) {
        this.periodeDebut = periodeDebut;
    }

    public Date getPeriodeFin() {
        return periodeFin;
    }

    public void setPeriodeFin(Date periodeFin) {
        this.periodeFin = periodeFin;
    }

    public AssociationCotisation getAssociationCotisation() {
        return associationCotisation;
    }

    public void setAssociationCotisation(AssociationCotisation associationCotisation) {
        this.associationCotisation = associationCotisation;
    }

    public Montant getFourchetteDebut() {
        return fourchetteDebut;
    }

    public void setFourchetteDebut(Montant fourchetteDebut) {
        this.fourchetteDebut = fourchetteDebut;
    }

    public Montant getFourchetteFin() {
        return fourchetteFin;
    }

    public void setFourchetteFin(Montant fourchetteFin) {
        this.fourchetteFin = fourchetteFin;
    }

    public Montant getMontantCotisation() {
        return montantCotisation;
    }

    public void setMontantCotisation(Montant montantCotisation) {
        this.montantCotisation = montantCotisation;
    }

    public TypeParamCotisationAP getTypeParametre() {
        return typeParametre;
    }

    public void setTypeParametre(TypeParamCotisationAP typeParametre) {
        this.typeParametre = typeParametre;
    }

    public Taux getTauxCotisation() {
        return tauxCotisation;
    }

    public void setTauxCotisation(Taux tauxCotisation) {
        this.tauxCotisation = tauxCotisation;
    }

    public Montant getMassePourCotisation() {
        return massePourCotisation;
    }

    public void setMassePourCotisation(Montant massePourCotisation) {
        this.massePourCotisation = massePourCotisation;
    }

    public String getIdRubriqueCotisation() {
        if (getAssociationCotisation() != null) {
            return getAssociationCotisation().getIdRubriqueCotisation();
        }
        return null;
    }

    public Administration getAssociationProfessionnelleParent() {
        return enteteFacture.getAssociationProfessionnelleParent();
    }

    public String getIdAssociationProfessionnelleParent() {
        return enteteFacture.getIdAssociationProfessionnelleParent();
    }

    public Annee getAnnee() {
        return enteteFacture.getAnneeFacture();
    }

    public Administration getAssociation() {
        return associationCotisation.getAssociationProfessionnelle();
    }

    public String getIdCotisation() {
        return associationCotisation.getIdCotisationAssociationProfessionnelle();

    }

    public String getIdAssociationProfessionnelle() {
        return associationCotisation.getAssociationProfessionnelle().getId();
    }

    public Administration getAssociationProfessionnelle() {
        return associationCotisation.getAssociationProfessionnelle();
    }

    public CotisationAssociationProfessionnelle getCotisationAssociationProfessionnelle() {
        return associationCotisation.getCotisationAssociationProfessionnelle();
    }

    public EtatFactureAP getEtat() {
        return enteteFacture.getEtat();
    }

    public String getIdEnteteFacture() {
        return enteteFacture.getId();
    }

    public ModeleEntete getModeleEntete() {
        return enteteFacture.getModele();
    }

    public Passage getPassageFacturation() {
        return enteteFacture.getPassageFacturation();
    }

    public boolean isRabaisSpecial() {
        return CategorieFactureAssociationProfessionnelle.RABAIS_SPECIAL.equals(associationCotisation
                .getCotisationAssociationProfessionnelle().getFacturerDefaut());
    }

    public Double getFacteur() {
        return facteur;
    }

    public void setFacteur(Double facteur) {
        this.facteur = facteur;
    }

    public boolean isLigneFacteur() {
        return TypeParamCotisationAP.FACTEUR.equals(typeParametre);
    }

    public boolean isLigneRabais() {
        return TypeParamCotisationAP.RABAIS.equals(typeParametre);
    }

    @Override
    public int compareTo(LigneFactureAssociation other) {
        if (getTypeParametre().getPriority().compareTo(other.getTypeParametre().getPriority()) == 0) {
            return getFourchetteDebut().compareTo(other.getFourchetteDebut());
        }
        return getTypeParametre().getPriority().compareTo(other.getTypeParametre().getPriority());
    }
}