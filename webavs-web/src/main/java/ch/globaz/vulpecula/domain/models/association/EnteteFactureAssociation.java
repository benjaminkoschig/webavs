package ch.globaz.vulpecula.domain.models.association;

import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang.Validate;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.DomainEntity;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.external.models.musca.Passage;
import ch.globaz.vulpecula.external.models.pyxis.Administration;

/**
 * WARNING : Cette classe doit être utilisé uniquement dans une optique de persistence dans un repository. Elle n'a
 * aucune valeur métier et est donc remplacé par l'objet {@link FactureAssociation}.
 */
public class EnteteFactureAssociation implements DomainEntity {
    private String id;
    private String spy;

    private Employeur employeur;
    private Annee anneeFacture;
    private Taux reductionFacture;
    private EtatFactureAP etat;
    private Montant montantTotal;
    private Montant masseSalariale;
    private Montant montantTotalCotisationForcee = Montant.ZERO;
    private HashMap<String, Montant> montantTotalByAssociation;
    private String numeroSection;

    public String getNumeroSection() {
        return numeroSection;
    }

    public void setNumeroSection(String numeroSection) {
        this.numeroSection = numeroSection;
    }

    public Montant getMontantTotalCotisationForcee() {
        return montantTotalCotisationForcee;
    }

    public void setMontantTotalCotisationForcee(Montant montantTotalCotisationForcee) {
        this.montantTotalCotisationForcee = montantTotalCotisationForcee;
    }

    private ModeleEntete modele;
    private Administration associationProfessionnelleParent;
    private Passage passageFacturation;

    public Administration getAssociationProfessionnelleParent() {
        return associationProfessionnelleParent;
    }

    public void setAssociationProfessionnelleParent(Administration associationProfessionnelleParent) {
        this.associationProfessionnelleParent = associationProfessionnelleParent;
    }

    private List<LigneFactureAssociation> lignesFacture;

    public List<LigneFactureAssociation> getLignesFacture() {
        return lignesFacture;
    }

    public void setLignesFacture(List<LigneFactureAssociation> lignesFacture) {
        this.lignesFacture = lignesFacture;
    }

    @Override
    public String getId() {
        return id;
    }

    public String getIdAssociationProfessionnelleParent() {
        return associationProfessionnelleParent.getId();
    }

    public String getIdModeleEntete() {
        return modele.getId();
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public Employeur getEmployeur() {
        return employeur;
    }

    public void setEmployeur(Employeur employeur) {
        this.employeur = employeur;
    }

    @Override
    public String getSpy() {
        return spy;
    }

    @Override
    public void setSpy(String spy) {
        this.spy = spy;
    }

    public Annee getAnneeFacture() {
        return anneeFacture;
    }

    public void setAnneeFacture(Annee anneeFacture) {
        this.anneeFacture = anneeFacture;
    }

    public Taux getReductionFacture() {
        return reductionFacture;
    }

    public void setReductionFacture(Taux reductionFacture) {
        this.reductionFacture = reductionFacture;
    }

    public EtatFactureAP getEtat() {
        return etat;
    }

    public void setEtat(EtatFactureAP etat) {
        this.etat = etat;
    }

    public Montant getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(Montant montantTotal) {
        this.montantTotal = montantTotal;
    }

    public void addToMontantTotal(String idAssociation, Montant montant) {
        if (montantTotal == null) {
            montantTotal = Montant.ZERO;
        }
        if (!montantTotalByAssociation.containsKey(idAssociation)) {
            montantTotalByAssociation.put(idAssociation, Montant.ZERO);
        }

        montantTotal = montantTotal.add(montant);
        montantTotalByAssociation.put(idAssociation, montantTotalByAssociation.get(idAssociation).add(montant));
    }

    public Montant getMontantTotalForAssociation(String idAssociation) {
        Validate.notEmpty(idAssociation, "idAssociation must not empty");
        if (montantTotalByAssociation == null) {
            return null;
        }
        return montantTotalByAssociation.get(idAssociation);
    }

    public void initMontantTotal() {
        montantTotal = Montant.ZERO;
        montantTotalByAssociation = new HashMap<String, Montant>();
    }

    public Montant getMasseSalariale() {
        return masseSalariale;
    }

    public void setMasseSalariale(Montant masseSalariale) {
        this.masseSalariale = masseSalariale;
    }

    public ModeleEntete getModele() {
        return modele;
    }

    public void setModele(ModeleEntete modele) {
        this.modele = modele;
    }

    public String getIdPassageFacturation() {
        if (passageFacturation == null) {
            return null;
        }
        return passageFacturation.getId();
    }

    public Passage getPassageFacturation() {
        return passageFacturation;
    }

    public void setPassageFacturation(Passage passageFacturation) {
        this.passageFacturation = passageFacturation;
    }

    /**
     * @return le numéro d'affilié de l'employeur
     */
    public String getEmployeurAffilieNumero() {
        if (employeur == null) {
            throw new NullPointerException("L'employeur (" + getIdEmployeur() + ") de la facture " + getId()
                    + " est null !");
        }
        return employeur.getAffilieNumero();
    }

    public String getIdEmployeur() {
        return getEmployeur().getId();
    }

    public String getNomAssociation() {
        if (associationProfessionnelleParent == null) {
            return null;
        }
        return new StringBuilder(associationProfessionnelleParent.getDesignation1()).append(" ")
                .append(associationProfessionnelleParent.getDesignation2()).toString();
    }

    @Override
    public String toString() {
        return "EnteteFactureAssociation [id=" + id + ", spy=" + spy + ", employeur=" + employeur + ", anneeFacture="
                + anneeFacture + ", reductionFacture=" + reductionFacture + ", etat=" + etat + ", montantTotal="
                + montantTotal + ", masseSalariale=" + masseSalariale + ", modele=" + modele
                + ", associationProfessionnelleParent=" + associationProfessionnelleParent + ", lignesFacture="
                + lignesFacture + "]";
    }

    public void setIdPassageFacturation(String idPassage) {
        passageFacturation.setId(idPassage);
    }
}
