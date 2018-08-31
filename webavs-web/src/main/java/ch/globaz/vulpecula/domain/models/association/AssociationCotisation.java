package ch.globaz.vulpecula.domain.models.association;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.DomainEntity;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.registre.CategorieFactureAssociationProfessionnelle;
import ch.globaz.vulpecula.domain.models.registre.GenreCotisationAssociationProfessionnelle;
import ch.globaz.vulpecula.external.models.pyxis.Administration;
import com.google.common.base.Function;
import com.google.common.collect.Multimaps;

public class AssociationCotisation implements DomainEntity, Comparable<AssociationCotisation> {
    private String id;
    private String spy;

    private String idEmployeur;
    private GenreCotisationAssociationProfessionnelle genre;
    private CotisationAssociationProfessionnelle cotisationAssociationProfessionnelle;
    private AssociationEmployeur associationEmployeur;
    private Periode periode;
    private Taux masseSalariale;
    private Montant forfait;
    private Taux reductionFacture;
    // private CategorieFactureAssociationProfessionnelle facturer;
    private boolean utiliseDansFacture = false;

    public static final int LIMITE_COTISATION_NON_MEMBRE = 1;
    public static final double MASSE_SALARIALE_DEFAUT = 100;
    public static final int SOMME_MASSE_SALARIALE_IMPOSEE = 100;
    public static final double REDUCTION_FACTURE_DEFAUT = 0;

    /**
     * Retourne l'id de la caisse métier.
     * 
     * @return String représentant l'id de la caisse métier
     */
    public String getIdCotisationAssociationProfessionnelle() {
        return cotisationAssociationProfessionnelle.getId();
    }

    public AssociationEmployeur getAssociationEmployeur() {
        return associationEmployeur;
    }

    public void setAssociationEmployeur(AssociationEmployeur associationEmployeur) {
        this.associationEmployeur = associationEmployeur;
    }

    public String getIdAssociationProfessionnelle() {
        return cotisationAssociationProfessionnelle.getIdAssociationProfessionnelle();
    }

    public CotisationAssociationProfessionnelle getCotisationAssociationProfessionnelle() {
        return cotisationAssociationProfessionnelle;
    }

    public void setCotisationAssociationProfessionnelle(
            CotisationAssociationProfessionnelle cotisationAssociationProfessionnelle) {
        this.cotisationAssociationProfessionnelle = cotisationAssociationProfessionnelle;
    }

    public String getIdEmployeur() {
        return idEmployeur;
    }

    public void setIdEmployeur(String idEmployeur) {
        this.idEmployeur = idEmployeur;
    }

    public GenreCotisationAssociationProfessionnelle getGenre() {
        return genre;
    }

    public void setGenre(GenreCotisationAssociationProfessionnelle genre) {
        this.genre = genre;
    }

    public CategorieFactureAssociationProfessionnelle getFacturer() {
        return cotisationAssociationProfessionnelle.getFacturerDefaut();
    }

    public Periode getPeriode() {
        return periode;
    }

    public Date getPeriodeDebut() {
        return periode.getDateDebut();
    }

    public Annee getAnneeDebut() {
        return new Annee(getPeriodeDebut().getAnnee());
    }

    public Annee getAnneeFin() {
        if (getPeriodeFin() == null) {
            return null;
        }
        if (getPeriodeFin().getAnnee() == null) {
            return null;
        }
        return new Annee(getPeriodeFin().getAnnee());
    }

    public Date getPeriodeFin() {
        return periode.getDateFin();
    }

    public String getPeriodeDebutAsValue() {
        return periode.getDateDebutAsSwissValue();
    }

    public String getPeriodeFinAsValue() {
        return periode.getDateFinAsSwissValue();
    }

    public void setPeriode(Periode periode) {
        this.periode = periode;
    }

    public Taux getMasseSalariale() {
        return masseSalariale;
    }

    public void setMasseSalariale(Taux masseSalariale) {
        this.masseSalariale = masseSalariale;
    }

    public Montant getForfait() {
        return forfait;
    }

    public void setForfait(Montant forfait) {
        this.forfait = forfait;
    }

    public Taux getReductionFacture() {
        return reductionFacture;
    }

    public void setReductionFacture(Taux reductionFacture) {
        this.reductionFacture = reductionFacture;
    }

    /**
     * Retourne si la cotisation est active pour l'année passe en paramètre.
     * 
     * @param annee Année de contrôle
     * @return true si cotisation active durant l'année
     */
    public boolean isActive(Annee annee) {
        if (periode == null) {
            throw new IllegalStateException("La période de la cotisation n'est pas renseignée");
        }

        if (periode.getAnneeDebut().isBeforeOrEquals(annee)
                && (periode.sansFin() || periode.getAnneeFin().isAfterOrEquals(annee))) {
            return true;
        }
        return false;
    }

    /**
     * Retourne true si la cotisation est active pour la date du jour
     * 
     * @return true si cotisation active par rapport à la date du jour ou si pas de date de fin
     */
    public boolean isActive() {
        return periode.getDateFin() == null || periode.getDateFin().afterOrEquals(Date.now());
    }

    public boolean sansPeriodeFin() {
        return periode.getDateFin() == null;
    }

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

    @Override
    public int compareTo(AssociationCotisation other) {
        return periode.compareTo(other.getPeriode());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AssociationCotisation) {
            AssociationCotisation associationCotisation = (AssociationCotisation) obj;

            if (associationCotisation.getId() == null) {
                return false;
            }

            return associationCotisation.getId().equals(getId());
        }
        return false;
    }

    public Administration getAssociationProfessionnelle() {
        return cotisationAssociationProfessionnelle.getAssociationProfessionnelle();
    }

    public String getLibelleCotisation() {
        return cotisationAssociationProfessionnelle.getLibelle();
    }

    public boolean isNonTaxe() {
        return GenreCotisationAssociationProfessionnelle.NON_TAXE.equals(genre);
    }

    public String getLibelleAssociationProfessionnelle() {
        return getAssociationProfessionnelle().getDesignation1();
    }

    public static Map<AssociationGenre, List<AssociationCotisation>> groupByAssociationGenre(
            List<AssociationCotisation> cotisations, final Map<String, AssociationEmployeur> mapAssocEmpl) {
        Map<AssociationGenre, List<AssociationCotisation>> associationCotisationOrdered = new HashMap<AssociationGenre, List<AssociationCotisation>>();

        Map<AssociationGenre, Collection<AssociationCotisation>> associationsCotisations = Multimaps.index(cotisations,
                new Function<AssociationCotisation, AssociationGenre>() {
                    @Override
                    public AssociationGenre apply(AssociationCotisation associationCotisation) {
                        return new AssociationGenre(associationCotisation.getAssociationProfessionnelle(),
                                associationCotisation.getGenre(), mapAssocEmpl.get(associationCotisation
                                        .getIdAssociationProfessionnelle()));
                    }
                }).asMap();

        for (Map.Entry<AssociationGenre, Collection<AssociationCotisation>> entry : associationsCotisations.entrySet()) {
            List<AssociationCotisation> liste = new ArrayList<AssociationCotisation>(entry.getValue());
            Collections.sort(liste);
            associationCotisationOrdered.put(entry.getKey(), liste);
        }
        return associationCotisationOrdered;
    }

    public boolean isRabaisSpecial() {
        return CategorieFactureAssociationProfessionnelle.RABAIS_SPECIAL.equals(cotisationAssociationProfessionnelle
                .getFacturerDefaut());
    }

    public String getIdRubriqueCotisation() {
        if (getCotisationAssociationProfessionnelle() != null) {
            return getCotisationAssociationProfessionnelle().getIdRubrique();
        }
        return null;
    }

    public boolean isUtiliseDansFacture() {
        return utiliseDansFacture;
    }

    public void setUtiliseDansFacture(boolean utiliseDansFacture) {
        this.utiliseDansFacture = utiliseDansFacture;
    }
    
    public boolean isMembre() {
    	return GenreCotisationAssociationProfessionnelle.MEMBRE.getValue().equals(getGenre().getValue());
    }
    
    public boolean isNonMembre() {
    	return GenreCotisationAssociationProfessionnelle.NON_MEMBRE.getValue().equals(getGenre().getValue());
    }

}
