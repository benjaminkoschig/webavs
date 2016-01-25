package ch.globaz.vulpecula.domain.models.servicemilitaire;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.decompte.TypeAssurance;
import ch.globaz.vulpecula.domain.models.decompte.TypeSalaire;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.domain.models.postetravail.Qualification;
import ch.globaz.vulpecula.domain.models.prestations.Beneficiaire;
import ch.globaz.vulpecula.domain.models.prestations.Etat;
import ch.globaz.vulpecula.domain.models.prestations.PrestationSMAJ;
import ch.globaz.vulpecula.domain.models.registre.Convention;
import ch.globaz.vulpecula.domain.specifications.servicemilitaire.SMPeriodeInActivitePoste;
import ch.globaz.vulpecula.domain.specifications.servicemilitaire.SMPeriodeRequiseSpecification;
import ch.globaz.vulpecula.external.models.musca.Passage;
import ch.globaz.vulpecula.external.models.osiris.Journal;

public class ServiceMilitaire implements PrestationSMAJ, Comparable<ServiceMilitaire> {
    private String id;
    private PosteTravail posteTravail;
    private GenreSM genre;
    private Periode periode;
    private Beneficiaire beneficiaire;
    private Passage passage;
    private Etat etat;
    private double nbJours;
    private double nbHeuresParJour;
    private Montant salaireHoraire;
    private Taux couvertureAPG;
    private Montant versementAPG;
    private Montant compensationAPG;
    private Montant montantBrut;
    private Montant montantAVerser;
    private String idPassageFacturation;
    private Montant baseSalaire;
    private Taux tauxCP;
    private Taux tauxGratification;
    private List<TauxServiceMilitaire> tauxServicesMilitaires;
    private Journal journal = new Journal();
    private String spy;

    public ServiceMilitaire() {
        tauxServicesMilitaires = new ArrayList<TauxServiceMilitaire>();
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
    public Periode getPeriode() {
        return periode;
    }

    @Override
    public void setPeriode(Periode periode) {
        this.periode = periode;
    }

    public Beneficiaire getBeneficiaire() {
        return beneficiaire;
    }

    public void setBeneficiaire(Beneficiaire beneficiaire) {
        this.beneficiaire = beneficiaire;
    }

    public Passage getPassage() {
        return passage;
    }

    public void setPassage(Passage passage) {
        this.passage = passage;
    }

    public Etat getEtat() {
        return etat;
    }

    public void setEtat(Etat etat) {
        this.etat = etat;
    }

    public double getNbJours() {
        return nbJours;
    }

    public void setNbJours(double nbJours) {
        this.nbJours = nbJours;
    }

    public double getNbHeuresParJour() {
        return nbHeuresParJour;
    }

    public void setNbHeuresParJour(double nbHeuresParJour) {
        this.nbHeuresParJour = nbHeuresParJour;
    }

    public Montant getSalaireHoraire() {
        return salaireHoraire;
    }

    public void setSalaireHoraire(Montant salaireHoraire) {
        this.salaireHoraire = salaireHoraire;
    }

    public Taux getCouvertureAPG() {
        return couvertureAPG;
    }

    public void setCouvertureAPG(Taux couvertureAPG) {
        this.couvertureAPG = couvertureAPG;
    }

    public Montant getMontantCouvertureAPG() {
        return getTotalSalaire().multiply(couvertureAPG);
    }

    public Montant getVersementAPG() {
        return versementAPG;
    }

    public void setVersementAPG(Montant versementAPG) {
        this.versementAPG = versementAPG;
    }

    public Montant getCompensationAPG() {
        return compensationAPG;
    }

    public void setCompensationAPG(Montant compensationAPG) {
        this.compensationAPG = compensationAPG;
    }

    public Montant getMontantBrut() {
        return montantBrut;
    }

    public Journal getJournal() {
        return journal;
    }

    public void setJournal(Journal journal) {
        this.journal = journal;
    }

    public Date getDateComptabilisation() {
        return journal.getDateComptabilisation();
    }

    public Montant getMontantAVS_AC() {
        return getMontantAVS().add(getMontantAC());
    }

    public Montant getMontantAVS() {
        return getMontantForAssurance(TypeAssurance.COTISATION_AVS_AI);
    }

    public Montant getMontantAC() {
        return getMontantForAssurance(TypeAssurance.ASSURANCE_CHOMAGE);
    }

    public Montant getMontantAF() {
        return getMontantForAssurance(TypeAssurance.COTISATION_AF);
    }

    public Montant getMontantForAssurance(TypeAssurance typeAssurance) {
        Montant montant = Montant.ZERO;
        for (TauxServiceMilitaire tauxServiceMilitaire : tauxServicesMilitaires) {
            if (typeAssurance.equals(tauxServiceMilitaire.getTypeAssurance())) {
                montant = montant.add(getMontantBrut().multiply(tauxServiceMilitaire.getTaux()));
            }
        }
        return montant;
    }

    public void setMontantBrut(Montant montantBrut) {
        this.montantBrut = montantBrut;
    }

    public Montant getMontantAVerser() {
        return montantAVerser;
    }

    public void setMontantAVerser(Montant montantAVerser) {
        this.montantAVerser = montantAVerser;
    }

    @Override
    public PosteTravail getPosteTravail() {
        return posteTravail;
    }

    public void setPosteTravail(PosteTravail posteTravail) {
        this.posteTravail = posteTravail;
    }

    public GenreSM getGenre() {
        return genre;
    }

    public void setGenre(GenreSM genre) {
        this.genre = genre;
    }

    /**
     * Retourne l'id du poste de travail
     * 
     * @return String représentant l'id du poste de travail
     */
    public String getIdPosteTravail() {
        if (posteTravail == null) {
            return null;
        }
        return posteTravail.getId();
    }

    /**
     * Retourne l'id du passage
     * 
     * @return String représentant l'id du passage de facturation
     */
    public String getIdPassage() {
        if (passage == null) {
            return null;
        }
        return passage.getId();
    }

    /**
     * Retourne la date du début
     * 
     * @return String représentant la date de début de la période
     */
    public String getDateDebutAsString() {
        if (periode == null) {
            return null;
        }
        return periode.getDateDebutAsSwissValue();
    }

    /**
     * Retourne la date du début
     * 
     * @return String représentant la date de début de la période
     */
    public String getDateFinAsString() {
        if (periode == null) {
            return null;
        }
        return periode.getDateFinAsSwissValue();
    }

    /**
     * Retourne si le service militaire est modifiable.
     * 
     * @return Retourne si l'absence est dans un état modifiable
     */
    @Override
    public boolean isModifiable() {
        return !Etat.COMPTABILISEE.equals(etat) && !Etat.TRAITEE.equals(etat);
    }

    /**
     * Retourne l'année de début du service militaire.
     * 
     * @return String représentant l'année
     */
    public String getAnneeDebut() {
        return periode.getAnneeDebut().toString();
    }

    /**
     * Retourne l'année de fin du service militaire.
     * 
     * @return String représentant l'année
     */
    public String getAnneeFin() {
        return periode.getAnneeFin().toString();
    }

    /**
     * Retourne l'id tiers rattaché à l'employeur.
     * 
     * @return String représentant l'id tiers du l'employeur.
     */
    public String getEmployeurIdTiers() {
        return posteTravail.getIdTiersEmployeur();
    }

    /**
     * Retourne le no° d'affilié rattaché à l'employeur.
     * 
     * @return String représentant le no° d'affilié de l'employeur.
     */
    @Override
    public String getNoAffilie() {
        return posteTravail.getAffilieNumero();
    }

    /**
     * Retourne la raison sociale de l'employeur.
     * 
     * @return String représentant la raison sociale de l'employeur
     */
    @Override
    public String getRaisonSocialeEmployeur() {
        return posteTravail.getRaisonSocialeEmployeur();
    }

    @Override
    public String getIdEmployeur() {
        return posteTravail.getIdEmployeur();
    }

    @Override
    public Convention getConventionEmployeur() {
        return posteTravail.getEmployeur().getConvention();
    }

    public String getTravailleurIdTiers() {
        return posteTravail.getTravailleurIdTiers();
    }

    @Override
    public String getIdTravailleur() {
        return posteTravail.getIdTravailleur();
    }

    @Override
    public String getNomPrenomTravailleur() {
        return posteTravail.getNomPrenomTravailleur();
    }

    @Override
    public Montant getMontant() {
        return montantAVerser;
    }

    /**
     * Retourne le total salaire, soit :
     * Total salaire =
     * (salaire horaire +montant grati) x nombre heures arrondi aux 5cts =
     * (27.30 + 2.58) * 90.70 = 2695.15
     * 
     * @return
     */
    public Montant getTotalSalaire() {
        return getSalaireHoraire().add(getMontantGratification()).multiply(nbHeuresParJour).normalize();
    }

    /**
     * Montant congé payé = salaire horaire x taux CP
     * 
     * @return Montant congé payé
     */
    public Montant getMontantCongePaye() {
        return salaireHoraire.multiply(tauxCP).decimal(2);
    }

    /**
     * Montant gratification : (salaire horaire + montant CP) x taux GR
     * 
     * @return Montant de gratification
     */
    public Montant getMontantGratification() {
        return getMontantCongePaye().add(salaireHoraire).multiply(tauxGratification).decimal(2);
    }

    @Override
    public Qualification getQualification() {
        return posteTravail.getQualification();
    }

    @Override
    public TypeSalaire getTypeSalaire() {
        return posteTravail.getTypeSalaire();
    }

    @Override
    public String getPeriodeAsString() {
        StringBuilder sb = new StringBuilder();
        sb.append(periode.getDateDebutAsSwissValue());
        sb.append(" - ");
        sb.append(periode.getDateFinAsSwissValue());
        return sb.toString();
    }

    @Override
    public String getTypePrestation() {
        return genre.getValue();
    }

    @Override
    public String getIdPassageFacturation() {
        return idPassageFacturation;
    }

    public void setIdPassageFacturation(String idPassageFacturation) {
        this.idPassageFacturation = idPassageFacturation;
    }

    public Montant getBaseSalaire() {
        return baseSalaire;
    }

    public void setBaseSalaire(Montant baseSalaire) {
        this.baseSalaire = baseSalaire;
    }

    public Taux getTauxCP() {
        return tauxCP;
    }

    public void setTauxCP(Taux tauxCP) {
        this.tauxCP = tauxCP;
    }

    public Taux getTauxGratification() {
        return tauxGratification;
    }

    public void setTauxGratification(Taux tauxGratification) {
        this.tauxGratification = tauxGratification;
    }

    public List<TauxServiceMilitaire> getTauxServicesMilitaires() {
        return tauxServicesMilitaires;
    }

    public void setTauxServicesMilitaires(List<TauxServiceMilitaire> tauxServicesMilitaires) {
        this.tauxServicesMilitaires = tauxServicesMilitaires;
    }

    /**
     * @return le NSS du travailleur
     */
    public String getTravailleurNss() {
        if (posteTravail == null) {
            return null;
        }
        return posteTravail.getTravailleurNss();
    }

    /**
     * Retourne la date de facturation du passage lié au service militaire.
     * 
     * @return String représentant la date de facturation
     */
    public String getDateFacturation() {
        return passage.getDateFacturation();
    }

    /**
     * Vérifie que le service militaire soit valide.
     * <ul>
     * 
     * @throws UnsatisfiedSpecificationException
     */
    public void validate() throws UnsatisfiedSpecificationException {
        final SMPeriodeRequiseSpecification periodeRequiseSpecification = new SMPeriodeRequiseSpecification();
        final SMPeriodeInActivitePoste periodeInActivitePoste = new SMPeriodeInActivitePoste();
        periodeRequiseSpecification.and(periodeInActivitePoste).isSatisfiedBy(this);
    }

    @Override
    public int compareTo(ServiceMilitaire o) {
        return id.compareTo(o.id);
    }

    @Override
    public Periode getPeriodeActivitePoste() {
        return posteTravail.getPeriodeActivite();
    }

    public Employeur getEmployeur() {
        return posteTravail.getEmployeur();
    }
}
