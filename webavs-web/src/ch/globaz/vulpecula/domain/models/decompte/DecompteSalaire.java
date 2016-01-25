package ch.globaz.vulpecula.domain.models.decompte;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.DomainEntity;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.domain.models.postetravail.Travailleur;
import ch.globaz.vulpecula.domain.specifications.decompte.DecompteSalaireInPeriodeActivitePoste;
import ch.globaz.vulpecula.domain.specifications.decompte.DecompteSalairePeriodeMemeAnnee;
import ch.globaz.vulpecula.external.models.affiliation.Assurance;

/**
 * @author Arnaud Geiser (AGE) | Créé le 20 févr. 2014
 * 
 */
public class DecompteSalaire implements DomainEntity {
    private String idDecompteSalaire;
    private List<Absence> absences;
    private List<CotisationDecompte> cotisationsDecompte;
    private PosteTravail posteTravail;
    private Decompte decompte;
    private Periode periode;
    private double heures;
    private Montant salaireHoraire;
    private Montant salaireTotal;
    private String sequence;
    private String spy;
    private String dateAnnonce;
    private Date dateAnnonceLPP;
    private Montant montantFranchise;
    private Taux tauxContribuableAffiche;

    public DecompteSalaire() {
        absences = new ArrayList<Absence>();
        cotisationsDecompte = new ArrayList<CotisationDecompte>();
    }

    public List<Absence> getAbsences() {
        return absences;
    }

    public List<CotisationDecompte> getCotisationsDecompte() {
        return cotisationsDecompte;
    }

    public void setCotisationsDecompte(final List<CotisationDecompte> cotisationsDecompte) {
        this.cotisationsDecompte = cotisationsDecompte;
    }

    public void addCotisationDecompte(final CotisationDecompte cotisationDecompte) {
        cotisationsDecompte.add(cotisationDecompte);
    }

    public void setAbsences(final List<Absence> absences) {
        this.absences = absences;
    }

    public PosteTravail getPosteTravail() {
        return posteTravail;
    }

    public void setPosteTravail(final PosteTravail posteTravail) {
        this.posteTravail = posteTravail;
    }

    public Decompte getDecompte() {
        return decompte;
    }

    public void setDecompte(final Decompte decompte) {
        this.decompte = decompte;
    }

    public double getHeures() {
        return heures;
    }

    public void setHeures(final double string) {
        heures = string;
    }

    public String getSalaireHoraireAsValue() {
        if (salaireHoraire != null) {
            return salaireHoraire.getValue();
        }
        return Montant.ZERO.getValue();
    }

    public Montant getSalaireHoraire() {
        if (salaireHoraire != null) {
            return salaireHoraire;
        }
        return Montant.ZERO;
    }

    public void setSalaireHoraire(final Montant salaireHoraire) {
        this.salaireHoraire = salaireHoraire;
    }

    public String getSalaireTotalAsValue() {
        if (salaireTotal != null) {
            return salaireTotal.getValueNormalisee();
        }
        return Montant.ZERO.getValue();
    }

    public Montant getSalaireTotal() {
        if (salaireTotal != null) {
            return salaireTotal;
        }
        return Montant.ZERO;
    }

    public Taux getTauxContribuableAffiche() {
        return tauxContribuableAffiche;
    }

    public String getTauxContribuableAfficheAsValue() {
        return tauxContribuableAffiche.getValue();
    }

    public void setTauxContribuableAffiche(Taux tauxContribuableAffiche) {
        this.tauxContribuableAffiche = tauxContribuableAffiche;
    }

    public Montant getMasseAC() {
        return getMontantForAssurance(TypeAssurance.ASSURANCE_CHOMAGE);
    }

    public Montant getMasseAC2() {
        return getMontantForAssurance(TypeAssurance.COTISATION_AC2);
    }

    public Montant getMasseAF() {
        return getMontantForAssurances(CotisationDecompte.getAssurancesWithReductionRentier());
    }

    public Montant getMontantForAssurance(TypeAssurance typeAssurance) {
        return getMontantForAssurances(Arrays.asList(typeAssurance));
    }

    private Montant getMontantForAssurances(List<TypeAssurance> typesAssurance) {
        Montant montant = Montant.ZERO;
        for (CotisationDecompte cotisationDecompte : cotisationsDecompte) {
            if (typesAssurance.contains(cotisationDecompte.getTypeAssurance())) {
                montant = cotisationDecompte.getMasse(salaireTotal);
            }
        }
        return montant;
    }

    public void setSalaireTotal(final Montant salaireTotal) {
        this.salaireTotal = salaireTotal;
    }

    public String getTauxContribuableAsValue() {
        return getTauxContribuable().getValue();
    }

    public Taux getTauxContribuable() {
        Taux taux = new Taux(0);
        for (CotisationDecompte cotisationDecompte : cotisationsDecompte) {
            taux = taux.addTaux(cotisationDecompte.getTaux());
        }
        return taux;
    }

    public String getTauxContribuableForCaissesSocialesAsValue() {
        return getTauxContribuableForCaissesSociales().getValue();
    }

    public Taux getTauxContribuableForCaissesSociales() {
        Taux taux = new Taux(0);
        for (CotisationDecompte cotisationDecompte : cotisationsDecompte) {
            if (!Assurance.NOT_CAISSES_SOCIALES.contains(cotisationDecompte.getTypeAssurance())) {
                taux = taux.addTaux(cotisationDecompte.getTaux());
            }
        }
        return taux;
    }

    public Taux getTauxContribuableForAF() {
        Taux taux = new Taux(0);
        for (CotisationDecompte cotisationDecompte : cotisationsDecompte) {
            if (Assurance.AF.contains(cotisationDecompte.getTypeAssurance())) {
                taux = taux.addTaux(cotisationDecompte.getTaux());
            }
        }
        return taux;
    }

    public Taux getTauxContribuableForAVS() {
        return getTauxContribuableForTypeAssurance(TypeAssurance.COTISATION_AVS_AI);
    }

    public Taux getTauxContribuableForAC() {
        return getTauxContribuableForTypeAssurance(TypeAssurance.ASSURANCE_CHOMAGE);
    }

    public Taux getTauxContribuableForAC2() {
        return getTauxContribuableForTypeAssurance(TypeAssurance.COTISATION_AC2);
    }

    public Taux getTauxContribuableForTypeAssurance(TypeAssurance typeAssurance) {
        Taux taux = new Taux(0);
        for (CotisationDecompte cotisationDecompte : cotisationsDecompte) {
            if (typeAssurance.equals(cotisationDecompte.getTypeAssurance())) {
                taux = taux.addTaux(cotisationDecompte.getTaux());
            }
        }
        return taux;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(final String sequence) {
        this.sequence = sequence;
    }

    /**
     * Retourne la liste des types d'absences qu sont contenues dans la liste
     * des absences de la ligne de décompte SANS DOUBLONS.
     * 
     * @return Liste de {@link TypeAbsence} sans doublons
     */
    public Set<TypeAbsence> getTypesAbsences() {
        Set<TypeAbsence> typesAbsence = new HashSet<TypeAbsence>();
        for (Absence absence : absences) {
            typesAbsence.add(absence.getType());
        }
        return typesAbsence;
    }

    public void addAbsence(final Absence absence) {
        absences.add(absence);
    }

    /**
     * @return the periode
     */
    public Periode getPeriode() {
        return periode;
    }

    public String getAnneeCotisation() {
        return periode.getDateDebut().getAnnee();
    }

    /**
     * @param periode
     *            the periode to set
     */
    public void setPeriode(final Periode periode) {
        this.periode = periode;
    }

    /**
     * Retourne l'id du poste de travail.
     * 
     * @return String représentant l'id du poste de travail, ou null si inexistant
     */
    public String getIdPosteTravail() {
        if (posteTravail == null) {
            return null;
        }
        return posteTravail.getId();
    }

    public String getDateAnnonce() {
        return dateAnnonce;
    }

    public void setDateAnnonce(String dateAnnonce) {
        this.dateAnnonce = dateAnnonce;
    }

    /**
     * Retourne l'id de l'employeur.
     * 
     * @return String représentant l'id de l'employeur, ou null si inexistant
     */
    public String getIdEmployeur() {
        if (posteTravail == null) {
            return null;
        }
        return posteTravail.getIdEmployeur();
    }

    /**
     * Retourne l'id du travailleur.
     * 
     * @return String représentant l'id du travailleur, ou null si inexistant
     */
    public String getIdTravailleur() {
        if (posteTravail == null) {
            return null;
        }
        return posteTravail.getIdTravailleur();
    }

    /**
     * Retourne l'id du décompte auquel le decompte salaire est associé.
     * 
     * @return String représentant l'id du décompte, ou null si inexistant
     */
    public String getIdDecompte() {
        if (decompte == null) {
            return null;
        }
        return decompte.getId();
    }

    /**
     * Retourne le nom et le prénom du travailleur du poste de travail.
     * 
     * @return String représentant le nom et le prénom du travailleur ou null si inexistant
     */
    public String getNomPrenomTravailleur() {
        if (posteTravail == null) {
            return null;
        }
        return posteTravail.getNomPrenomTravailleur();
    }

    /**
     * Retourne la date de naissance du travailleur du poste de travail.
     * 
     * @return String représentant la date de naissance du travailleur ou null si inexistante
     */
    public String getDateNaissanceTravailleur() {
        if (posteTravail == null) {
            return null;
        }
        return posteTravail.getDateNaissanceTravailleur();
    }

    /**
     * Retourne le code système correspondant à la qualification du poste de travail.
     * 
     * @return String représentant un code système ou null si inexistant
     */
    public String getQualificationPoste() {
        if (posteTravail == null) {
            return null;
        }
        return posteTravail.getQualificationAsValue();
    }

    /**
     * Retourne l'employeur associé au poste de travail de cette ligne de décompte.
     * 
     * @return L'employeur rattaché à cette ligne de décompte ou null si poste de travail inexistant
     */
    public Employeur getEmployeur() {
        if (posteTravail == null) {
            return null;
        }
        return posteTravail.getEmployeur();
    }

    /**
     * Retourne si l'employeur rattaché au poste de travail de cette ligne de salaire est soumis à l'AVS.
     * 
     * @return true si soumis, false si le poste de travail est inexistant ou l'employeur non soumis
     */
    public boolean isEmployeurSoumisAVS() {
        if (getEmployeur() != null) {
            return getEmployeur().isSoumisAVS();
        }
        return false;
    }

    /**
     * Retourne si l'employeur rattaché au poste de travail de cette ligne de salaire est soumis à l'AC.
     * 
     * @return true si soumis, false si le poste de travail est inexistant ou l'employeur non soumis
     */
    public boolean isEmployeurSoumisAC() {
        if (getEmployeur() != null) {
            return getEmployeur().isSoumisAC();
        }
        return false;
    }

    /**
     * Retourne si l'employeur rattaché au poste de travail de cette ligne de salaire est soumis à l'AC2.
     * 
     * @return true si soumis, false si le poste de travail est inexistant ou l'employeur non soumis
     */
    public boolean isEmployeurSoumisAC2() {
        if (getEmployeur() != null) {
            return getEmployeur().isSoumisAC2();
        }
        return false;
    }

    /**
     * Retourne si le poste de travail associé au décompte salaire est mensuel.
     * 
     * @return true si poste de travail mensuel
     */
    public boolean isMensuel() {
        return posteTravail.isMensuel();
    }

    /**
     * Retourne le montant des cotisations total pour le décompte salaire (AVS, AC, AC2 y compris).
     * 
     * @return Montant représentant le total des cotisations
     */
    public Montant getMontantCotisations() {
        Montant montant = new Montant(0);
        for (CotisationCalculee cotisationCalculee : getCotisationCalculees()) {
            montant.add(cotisationCalculee.getMontantCalculee());
        }
        return montant;
    }

    /**
     * @return la liste des cotisations calculées du décompte salaire en fonction du salaire total et en tenant compte
     *         d'une éventuelle masse forcée
     */
    public List<CotisationCalculee> getCotisationCalculees() {
        List<CotisationCalculee> cotisationsCalculees = new ArrayList<CotisationCalculee>();
        for (CotisationDecompte cotisationDecompte : cotisationsDecompte) {
            CotisationCalculee cotisationCalculee = new CotisationCalculee(cotisationDecompte.getCotisation(),
                    cotisationDecompte.getMasse(salaireTotal), cotisationDecompte.getTaux(), getAnnee());
            cotisationsCalculees.add(cotisationCalculee);
        }
        return cotisationsCalculees;
    }

    @Override
    public String getId() {
        return idDecompteSalaire;
    }

    @Override
    public void setId(final String id) {
        idDecompteSalaire = id;
    }

    @Override
    public String getSpy() {
        return spy;
    }

    @Override
    public void setSpy(final String spy) {
        this.spy = spy;
    }

    /**
     * Calcul du décompte salaire si le nombre d'heures et si le salaire horaire est saisi. Il faut également que le
     * salaire total soit à 0 pour ne pas l'écraser.
     */
    public void calculSalaireTotalSiNecessaire() {
        if (getSalaireHoraire().isPositive() && heures > 0 && getSalaireTotal().isZero()) {
            setSalaireTotal(salaireHoraire.multiply(new Montant(String.valueOf(heures))));
        }
    }

    /**
     * Check the entity
     * 
     * @return true if entity is valid
     * @throws UnsatisfiedSpecificationException
     */
    public void validate() throws UnsatisfiedSpecificationException {
        final DecompteSalaireInPeriodeActivitePoste decompteSalaireInPeriodeActivitePoste = new DecompteSalaireInPeriodeActivitePoste();
        final DecompteSalairePeriodeMemeAnnee decompteSalairePeriodeMemeAnnee = new DecompteSalairePeriodeMemeAnnee();
        decompteSalaireInPeriodeActivitePoste.and(decompteSalairePeriodeMemeAnnee).isSatisfiedBy(this);
    }

    public boolean controleAC2() {
        return decompte.isControleAC2();
    }

    public Annee getAnneeDecompte() {
        return decompte.getAnnee();
    }

    public CotisationDecompte getCotisationAC() {
        return getCotisation(TypeAssurance.ASSURANCE_CHOMAGE);
    }

    public CotisationDecompte getCotisationAC2() {
        return getCotisation(TypeAssurance.COTISATION_AC2);
    }

    public CotisationDecompte getCotisationAVS() {
        return getCotisation(TypeAssurance.COTISATION_AVS_AI);
    }

    public CotisationDecompte getCotisationAF() {
        return getCotisation(TypeAssurance.COTISATION_AF);
    }

    public boolean isAFFranchise() {
        return isFranchise(getCotisationAF());
    }

    public boolean isFranchise() {
        for (CotisationDecompte cotisation : cotisationsDecompte) {
            if (isFranchise(cotisation)) {
                return true;
            }
        }
        return false;
    }

    private boolean isFranchise(CotisationDecompte cotisation) {
        if (CotisationDecompte.getAssurancesWithReductionRentier().contains(cotisation.getTypeAssurance())) {
            if (cotisation != null && cotisation.getMasseForcee() && !cotisation.getMasse().equals(salaireTotal)) {
                return true;
            }
        }
        return false;
    }

    public Date getPeriodeDebutDecompte() {
        return decompte.getPeriodeDebut();
    }

    public Date getPeriodeFinDecompte() {
        return decompte.getPeriodeFin();
    }

    public Date getPeriodeDebut() {
        return periode.getDateDebut();
    }

    public String getPeriodeDebutAsSwissValue() {
        return periode.getDateDebutAsSwissValue();
    }

    public Date getPeriodeFin() {
        return periode.getDateFin();
    }

    public int getAnneeFin() {
        return Integer.valueOf(periode.getDateFin().getAnnee());
    }

    public String getPeriodeFinAsSwissValue() {
        return periode.getDateFinAsSwissValue();
    }

    private CotisationDecompte getCotisation(TypeAssurance typeAssurance) {
        for (CotisationDecompte cotisation : cotisationsDecompte) {
            if (cotisation.getTypeAssurance().equals(typeAssurance)) {
                return cotisation;
            }
        }
        return null;
    }

    /**
     * Retourne si le décompte est comptabilisé
     * 
     * @return true si comptabilisé
     */
    public boolean isComptabilise() {
        return decompte.isComptabilise();
    }

    public Annee getAnnee() {
        return new Annee(periode.getDateDebut().getAnnee());
    }

    /**
     * Retourne si le décompte salaire appartient à la même année que le décompte auquel. On se base sur la date de
     * début de
     * décompte salaire pour déterminer cette période.
     * 
     * @return true si le décompte salaire est sur la même année que le décompte.
     */
    public boolean isMemeAnneeDecompte() {
        return isMemeAnnee(decompte.getAnnee());
    }

    /**
     * Retourne si l'année du décompte salaire appartient à la même année que celle passée en paramètre. On se base
     * sur la date de début de décompte salaire pour déterminer cette période.
     */
    public boolean isMemeAnnee(Annee anneeDecompte) {
        if (anneeDecompte.equals(getAnnee())) {
            return true;
        }
        return false;
    }

    public boolean hasTypeAssurance(TypeAssurance type) {
        for (CotisationDecompte cotisationDecompte : cotisationsDecompte) {
            if (type.equals(cotisationDecompte.getTypeAssurance())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((absences == null) ? 0 : absences.hashCode());
        result = prime * result + ((cotisationsDecompte == null) ? 0 : cotisationsDecompte.hashCode());
        result = prime * result + ((dateAnnonce == null) ? 0 : dateAnnonce.hashCode());
        result = prime * result + ((decompte == null) ? 0 : decompte.hashCode());
        long temp;
        temp = Double.doubleToLongBits(heures);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((idDecompteSalaire == null) ? 0 : idDecompteSalaire.hashCode());
        result = prime * result + ((periode == null) ? 0 : periode.hashCode());
        result = prime * result + ((posteTravail == null) ? 0 : posteTravail.hashCode());
        result = prime * result + ((salaireHoraire == null) ? 0 : salaireHoraire.hashCode());
        result = prime * result + ((salaireTotal == null) ? 0 : salaireTotal.hashCode());
        result = prime * result + ((sequence == null) ? 0 : sequence.hashCode());
        result = prime * result + ((spy == null) ? 0 : spy.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        DecompteSalaire other = (DecompteSalaire) obj;
        if (getId() == null) {
            return false;
        }
        if (getId().equals(other.getId())) {
            return true;
        } else {
            return false;
        }
    }

    public Periode getPeriodeActivitePoste() {
        return posteTravail.getPeriodeActivite();
    }

    public boolean isStartThisYear(Annee annee) {
        return getAnnee().equals(annee);
    }

    /**
     * Met la cotisation AC2 à zéro
     */
    public void resetCotisationAC2() {
        CotisationDecompte cotisationAC2 = getCotisationAC2();
        if (cotisationAC2 != null) {
            cotisationAC2.setMasseForcee(true);
            cotisationAC2.setMasse(Montant.ZERO);
        }
    }

    public TypeDecompte getTypeDecompte() {
        return decompte.getType();
    }

    public Travailleur getTravailleur() {
        return posteTravail.getTravailleur();
    }

    public final Date getDateAnnonceLPP() {
        return dateAnnonceLPP;
    }

    public final void setDateAnnonceLPP(Date dateAnnonceLPP) {
        this.dateAnnonceLPP = dateAnnonceLPP;
    }

    public Montant getMontantFranchise() {
        return montantFranchise;
    }

    public void setMontantFranchise(Montant montantFranchise) {
        this.montantFranchise = montantFranchise;
    }
}
