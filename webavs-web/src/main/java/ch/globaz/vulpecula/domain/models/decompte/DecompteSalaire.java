package ch.globaz.vulpecula.domain.models.decompte;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
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
import ch.globaz.vulpecula.ws.bean.StatusLine;

/**
 * @author Arnaud Geiser (AGE) | Cr?? le 20 f?vr. 2014
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
    private Taux tauxForEbu;
    private String genreCotisations;
    private String remarque;
    private boolean aTraiter;
    private boolean majFinPoste = false;
    private String correlationId;
    private String lineCorrelationId;
    private String posteCorrelationId;
    private List<CodeErreurDecompteSalaire> listeCodeErreur;
    // D?compte compl?mentaire pour e-business
    private Montant vacancesFeries;
    private Montant gratifications;
    private Montant absencesJustifiees;
    private Montant apgComplementaireSM;
    private Annee anneeCotisations;
    private String tauxAfficheErreur;
    private String tauxSaisieEbu;
    private StatusLine status;
    private boolean forcerFranchise0;

    public DecompteSalaire() {
        absences = new ArrayList<Absence>();
        cotisationsDecompte = new ArrayList<CotisationDecompte>();
        listeCodeErreur = new ArrayList<CodeErreurDecompteSalaire>();
    }

    public Taux getTauxForEbu() {
        return tauxForEbu;
    }

    public void setTauxForEbu(Taux tauxForEbu) {
        this.tauxForEbu = tauxForEbu;
    }

    public String getTauxAfficheErreur() {
        return tauxAfficheErreur;
    }

    public void setTauxAfficheErreur(String tauxAfficheErreur) {
        this.tauxAfficheErreur = tauxAfficheErreur;
    }

    public List<Absence> getAbsences() {
        return absences;
    }

    public boolean isMajFinPoste() {
        return majFinPoste;
    }

    public void setMajFinPoste(boolean majFinPoste) {
        this.majFinPoste = majFinPoste;
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

    public Montant getMasseAVS() {
        return getMontantForAssurance(TypeAssurance.COTISATION_AVS_AI);
    }

    public Montant getMasseAF() {
        return getMontantForAssurances(CotisationDecompte.getAssurancesWithReductionRentier());
    }

    public Montant getMasseAFPourReviseur() {
        if (VulpeculaServiceLocator.getDecompteSalaireService().isPosteTravailRentier(this)) {
            return getMontantForAssurances(CotisationDecompte.getAssurancesWithReductionRentier());
        } else {
            return getMontantForAssurance(TypeAssurance.COTISATION_AF);
        }
    }

    /**
     * somme des montant dans le cas ou l'on demande un seul type d'assurance
     * 
     * @param typeAssurance
     * @return
     */
    public Montant getMontantForAssurance(TypeAssurance typeAssurance) {
        Montant montant = Montant.ZERO;
        for (CotisationDecompte cotisationDecompte : cotisationsDecompte) {
            if (cotisationDecompte.getTypeAssurance() == typeAssurance) {
                montant = montant.add(cotisationDecompte.getMasse(salaireTotal));
            }
        }
        return montant;
    }

    /**
     * cas de'AF, on a besoin d'avoir une coti d?s que une coti correspond a la liste d'AF
     * 
     * @param typesAssurance
     * @return
     */
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

    public String getTauxContribuableForCaissesSocialesAsValue(boolean ebusiness) {
        return getTauxContribuableForCaissesSociales(ebusiness).getValue();
    }

    public Taux getTauxContribuableForCaissesSociales() {
        return getTauxContribuableForCaissesSociales(false);
    }

    public Taux getTauxContribuableForCaissesSociales(boolean ebusiness) {
        Taux taux = new Taux(0);

        for (CotisationDecompte cotisationDecompte : cotisationsDecompte) {
            if (!Assurance.NOT_CAISSES_SOCIALES.contains(cotisationDecompte.getTypeAssurance())) {
                taux = taux.addTaux(cotisationDecompte.getTaux());
            }
        }
        if (ebusiness && taux.isZero() && tauxContribuableAffiche != null && !tauxContribuableAffiche.isZero()) {
            return tauxContribuableAffiche;
        }
        return taux;
    }

    private Taux getTauxContribuableForLineApgHigh() {
        Taux tauxToReturn = getTauxContribuableForTypeAssurance(TypeAssurance.COTISATION_AVS_AI);
        tauxToReturn = tauxToReturn.addTaux(getTauxContribuableForTypeAssurance(TypeAssurance.COTISATION_AC2));
        tauxToReturn = tauxToReturn.addTaux(getTauxContribuableForTypeAssurance(TypeAssurance.ASSURANCE_CHOMAGE));
        tauxToReturn = tauxToReturn.addTaux(getTauxContribuableForTypeAssurance(TypeAssurance.FRAIS_ADMINISTRATION));
        return tauxToReturn;
    }

    private Taux getTauxContribuableForLineApgLow() {
        Taux tauxToReturn = getTauxContribuableForTypeAssurance(TypeAssurance.COTISATION_AVS_AI);
        tauxToReturn = tauxToReturn.addTaux(getTauxContribuableForTypeAssurance(TypeAssurance.ASSURANCE_CHOMAGE));
        tauxToReturn = tauxToReturn.addTaux(getTauxContribuableForTypeAssurance(TypeAssurance.FRAIS_ADMINISTRATION));
        return tauxToReturn;
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
     * des absences de la ligne de d?compte SANS DOUBLONS.
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

    public void addCodeErreur(final CodeErreur codeErreur) {
        setaTraiter(true);
        CodeErreurDecompteSalaire codeErreurDS = new CodeErreurDecompteSalaire(codeErreur);
        listeCodeErreur.add(codeErreurDS);
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
     * @return String repr?sentant l'id du poste de travail, ou null si inexistant
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
     * @return String repr?sentant l'id de l'employeur, ou null si inexistant
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
     * @return String repr?sentant l'id du travailleur, ou null si inexistant
     */
    public String getIdTravailleur() {
        if (posteTravail == null) {
            return null;
        }
        return posteTravail.getIdTravailleur();
    }

    /**
     * Retourne l'id du d?compte auquel le decompte salaire est associ?.
     * 
     * @return String repr?sentant l'id du d?compte, ou null si inexistant
     */
    public String getIdDecompte() {
        if (decompte == null) {
            return null;
        }
        return decompte.getId();
    }

    /**
     * Retourne le nom et le pr?nom du travailleur du poste de travail.
     * 
     * @return String repr?sentant le nom et le pr?nom du travailleur ou null si inexistant
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
     * @return String repr?sentant la date de naissance du travailleur ou null si inexistante
     */
    public String getDateNaissanceTravailleur() {
        if (posteTravail == null) {
            return null;
        }
        return posteTravail.getDateNaissanceTravailleur();
    }

    /**
     * Retourne le code syst?me correspondant ? la qualification du poste de travail.
     * 
     * @return String repr?sentant un code syst?me ou null si inexistant
     */
    public String getQualificationPoste() {
        if (posteTravail == null) {
            return null;
        }
        return posteTravail.getQualificationAsValue();
    }

    /**
     * Retourne l'employeur associ? au poste de travail de cette ligne de d?compte.
     * 
     * @return L'employeur rattach? ? cette ligne de d?compte ou null si poste de travail inexistant
     */
    public Employeur getEmployeur() {
        if (posteTravail == null) {
            return null;
        }
        return posteTravail.getEmployeur();
    }

    /**
     * Retourne si l'employeur rattach? au poste de travail de cette ligne de salaire est soumis ? l'AVS.
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
     * Retourne si l'employeur rattach? au poste de travail de cette ligne de salaire est soumis ? l'AC.
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
     * Retourne si l'employeur rattach? au poste de travail de cette ligne de salaire est soumis ? l'AC2.
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
     * Retourne si le poste de travail associ? au d?compte salaire est mensuel.
     * 
     * @return true si poste de travail mensuel
     */
    public boolean isMensuel() {
        return posteTravail.isMensuel();
    }

    /**
     * Retourne le montant des cotisations total pour le d?compte salaire (AVS, AC, AC2 y compris).
     * 
     * @return Montant repr?sentant le total des cotisations
     */
    public Montant getMontantCotisations() {
        Montant montant = new Montant(0);
        for (CotisationCalculee cotisationCalculee : getCotisationCalculees()) {
            montant.add(cotisationCalculee.getMontantCalculee());
        }
        return montant;
    }

    /**
     * @return la liste des cotisations calcul?es du d?compte salaire en fonction du salaire total et en tenant compte
     *         d'une ?ventuelle masse forc?e
     */
    public List<CotisationCalculee> getCotisationCalculees() {
        List<CotisationCalculee> cotisationsCalculees = new ArrayList<CotisationCalculee>();
        for (CotisationDecompte cotisationDecompte : cotisationsDecompte) {
            if (salaireTotal != null) {
                CotisationCalculee cotisationCalculee = new CotisationCalculee(cotisationDecompte.getCotisation(),
                        cotisationDecompte.getMasse(salaireTotal), cotisationDecompte.getTaux(), getAnnee());
                cotisationsCalculees.add(cotisationCalculee);
            }
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
     * Calcul du d?compte salaire si le nombre d'heures et si le salaire horaire est saisi. Il faut ?galement que le
     * salaire total soit ? 0 pour ne pas l'?craser.
     */
    public void calculChampSalaire() {
        if (getSalaireHoraire().isPositive() && heures > 0 && getSalaireTotal().isZero()) {
            setSalaireTotal(salaireHoraire.multiply(new Montant(String.valueOf(heures))));
        } else if (getSalaireHoraire().isZero() && heures > 0 && getSalaireTotal().isPositive()) {
            setSalaireHoraire(new Montant(salaireTotal.doubleValue() / heures));
        } else if (getSalaireHoraire().isPositive() && getHeures() == 0 && getSalaireTotal().isPositive()) {
            setHeures(salaireTotal.doubleValue() / salaireHoraire.doubleValue());
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
     * Retourne si le d?compte est comptabilis?
     * 
     * @return true si comptabilis?
     */
    public boolean isComptabilise() {
        return decompte.isComptabilise();
    }

    public boolean isValideOuComptabilise() {
        return decompte.isValideOuComptablise();
    }

    public Annee getAnnee() {
        return new Annee(periode.getDateDebut().getAnnee());
    }

    public boolean isCPP() {
        return decompte.isCPP();
    }

    /**
     * Retourne si le d?compte salaire appartient ? la m?me ann?e que le d?compte auquel. On se base sur la date de
     * d?but de
     * d?compte salaire pour d?terminer cette p?riode.
     * 
     * @return true si le d?compte salaire est sur la m?me ann?e que le d?compte.
     */
    public boolean isMemeAnneeDecompte() {
        return isMemeAnnee(decompte.getAnnee());
    }

    /**
     * Retourne si l'ann?e du d?compte salaire appartient ? la m?me ann?e que celle pass?e en param?tre. On se base
     * sur la date de d?but de d?compte salaire pour d?terminer cette p?riode.
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
     * Met la cotisation AC2 ? z?ro
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

    @Deprecated
    /**
     * utiliser uniquement dans les cas de reprise ou lorsque l'on souhaite forcer la masse de la franchise
     *
     * @return
     */
    public Montant getMontantFranchise() {
        return montantFranchise;
    }

    public void setMontantFranchise(Montant montantFranchise) {
        this.montantFranchise = montantFranchise;
    }

    public String getGenreCotisations() {
        return genreCotisations;
    }

    public void setGenreCotisations(String genreCotisations) {
        this.genreCotisations = genreCotisations;
    }

    public boolean isComplementaire() {
        return decompte.isComplementaire();
    }

    // /**
    // * @return the status
    // */
    // public StatusDecompteSalaire getStatus() {
    // return status;
    // }
    //
    // /**
    // * @param status the status to set
    // */
    // public void setStatus(StatusDecompteSalaire status) {
    // this.status = status;
    // }

    /**
     * @return the remarque
     */
    public String getRemarque() {
        return remarque;
    }

    /**
     * @param remarque the remarque to set
     */
    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }

    /**
     * @return the listeCodeErreur
     */
    public List<CodeErreurDecompteSalaire> getListeCodeErreur() {
        return listeCodeErreur;
    }

    /**
     * @param listeCodeErreur the listeCodeErreur to set
     */
    public void setListeCodeErreur(List<CodeErreurDecompteSalaire> listeCodeErreur) {
        this.listeCodeErreur = listeCodeErreur;
    }

    /**
     * @return the aTraiter
     */
    public boolean isaTraiter() {
        return aTraiter;
    }

    public boolean isLigneSupprimmee() {
        if (!isaTraiter()) {
            for (CodeErreurDecompteSalaire code : getListeCodeErreur()) {
                if (code.getCodeErreur().equals(CodeErreur.LIGNE_SUPPRIMEE)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @return the aTraiter
     */
    public boolean getaTraiter() {
        return aTraiter;
    }

    /**
     * @param aTraiter the aTraiter to set
     */
    public void setaTraiter(boolean aTraiter) {
        this.aTraiter = aTraiter;
    }

    /**
     * @return the correlationId
     */
    public String getCorrelationId() {
        return correlationId;
    }

    /**
     * @param correlationId the correlationId to set
     */
    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public boolean isTravailleurRetraiter(Date dateReference) {
        return getTravailleur().isRetraiter(dateReference);
    }

    /**
     * @return the vacancesFeries
     */
    public Montant getVacancesFeries() {
        return vacancesFeries;
    }

    /**
     * @param vacancesFeries the vacancesFeries to set
     */
    public void setVacancesFeries(Montant vacancesFeries) {
        this.vacancesFeries = vacancesFeries;
    }

    /**
     * @return the gratifications
     */
    public Montant getGratifications() {
        return gratifications;
    }

    /**
     * @param gratifications the gratifications to set
     */
    public void setGratifications(Montant gratifications) {
        this.gratifications = gratifications;
    }

    /**
     * @return the absencesJustifiees
     */
    public Montant getAbsencesJustifiees() {
        return absencesJustifiees;
    }

    /**
     * @param absencesJustifiees the absencesJustifiees to set
     */
    public void setAbsencesJustifiees(Montant absencesJustifiees) {
        this.absencesJustifiees = absencesJustifiees;
    }

    /**
     * @return the apgComplementaireSM
     */
    public Montant getApgComplementaireSM() {
        return apgComplementaireSM;
    }

    /**
     * @param apgComplementaireSM the apgComplementaireSM to set
     */
    public void setApgComplementaireSM(Montant apgComplementaireSM) {
        this.apgComplementaireSM = apgComplementaireSM;
    }

    public String getVacancesFeriesAsValue() {
        if (vacancesFeries != null) {
            return vacancesFeries.getValueNormalisee();
        }
        return Montant.ZERO.getValue();
    }

    public String getGratificationsAsValue() {
        if (gratifications != null) {
            return gratifications.getValueNormalisee();
        }
        return Montant.ZERO.getValue();
    }

    public String getAbsencesJustifieesAsValue() {
        if (absencesJustifiees != null) {
            return absencesJustifiees.getValueNormalisee();
        }
        return Montant.ZERO.getValue();
    }

    public String getApgComplementaireSMAsValue() {
        if (apgComplementaireSM != null) {
            return apgComplementaireSM.getValueNormalisee();
        }
        return Montant.ZERO.getValue();
    }

    public boolean isNewTravailleur() {
        return (getIdPosteTravail() == null || getIdPosteTravail().isEmpty()) && !correlationId.isEmpty();
    }

    /**
     * Ann?e de prise en charge des cotisations pour les d?comptes auxquels le temps peut ?tre forc?.
     * 
     * @return L'ann?e de prise en charge des taux de cotisations
     */
    public Annee getAnneeCotisations() {
        return anneeCotisations;
    }

    public void setAnneeCotisations(Annee anneeCotisations) {
        this.anneeCotisations = anneeCotisations;
    }

    public Date getDateCalculTaux() {
        if (anneeCotisations != null) {
            return anneeCotisations.getFirstDayOfYear();
        } else {
            if (isComplementaire()) {
                return periode.getDateFin();
            } else {
                return periode.getDateDebut();
            }
        }
    }

    public boolean isSameAnneeCotisations(Annee annee) {
        return (annee == null && anneeCotisations == null)
                || (anneeCotisations != null && anneeCotisations.equals(annee));
    }

    public String getLineCorrelationId() {
        return lineCorrelationId;
    }

    public void setLineCorrelationId(String lineCorrelationId) {
        this.lineCorrelationId = lineCorrelationId;
    }

    public String getPosteCorrelationId() {
        return posteCorrelationId;
    }

    public void setPosteCorrelationId(String posteCorrelationId) {
        this.posteCorrelationId = posteCorrelationId;
    }

    public String getTauxSaisieEbu() {
        return tauxSaisieEbu;
    }

    public void setTauxSaisieEbu(String tauxSaisieEbu) {
        this.tauxSaisieEbu = tauxSaisieEbu;
    }

    public StatusLine getStatus() {
        return status;
    }

    public void setStatus(StatusLine status) {
        this.status = status;
    }

    public boolean isForcerFranchise0() {
        return forcerFranchise0;
    }

    public void setForcerFranchise0(boolean forcerFranchise0) {
        this.forcerFranchise0 = forcerFranchise0;
    }

}
