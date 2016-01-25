package ch.globaz.vulpecula.domain.models.absencejustifiee;

import java.io.Serializable;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.decompte.TypeSalaire;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.domain.models.postetravail.Qualification;
import ch.globaz.vulpecula.domain.models.prestations.Beneficiaire;
import ch.globaz.vulpecula.domain.models.prestations.Etat;
import ch.globaz.vulpecula.domain.models.prestations.PrestationSMAJ;
import ch.globaz.vulpecula.domain.models.registre.Convention;
import ch.globaz.vulpecula.domain.specifications.absencesjustifiees.AJPeriodeInActivitePoste;
import ch.globaz.vulpecula.domain.specifications.absencesjustifiees.AJPeriodeRequiseSpecification;
import ch.globaz.vulpecula.external.models.musca.Passage;
import ch.globaz.vulpecula.external.models.osiris.Journal;
import ch.globaz.vulpecula.external.models.pyxis.CodeLangue;

/**
 * Une absence justifiée est une prestation accordée en cas de :
 * <ul>
 * <li>deuil</li>
 * <li>déménagement</li>
 * <li>inspection</li>
 * <li>mariage</li>
 * <li>naissance</li>
 * <li>info-recrutement</li>
 * </ul>
 * 
 */
public class AbsenceJustifiee implements PrestationSMAJ, Comparable<AbsenceJustifiee>, Serializable {
    private String idAbsenceJustifiee;
    private PosteTravail posteTravail;
    private TypeAbsenceJustifiee type;
    private Etat etat;
    private Periode periode;
    private Montant montantBrut;
    private Montant montantVerse;
    private Passage passage;
    private String spy;
    private Taux tauxAVS;
    private Taux tauxAC;
    private Beneficiaire beneficiaire;
    private LienParente lienParente;
    private double nombreDeJours;
    private double nombreHeuresParJour;
    private Montant salaireHoraire;
    private Journal journal = new Journal();

    public AbsenceJustifiee() {
    }

    @Override
    public String getId() {
        return idAbsenceJustifiee;
    }

    @Override
    public void setId(String id) {
        idAbsenceJustifiee = id;
    }

    @Override
    public String getSpy() {
        return spy;
    }

    @Override
    public void setSpy(String spy) {
        this.spy = spy;
    }

    public Journal getJournal() {
        return journal;
    }

    public void setJournal(Journal journal) {
        this.journal = journal;
    }

    /**
     * Retourne le poste de travail auquel est associée l'absence justifiée.
     * 
     * @return le poste de travail associé
     */
    @Override
    public PosteTravail getPosteTravail() {
        return posteTravail;
    }

    /**
     * Associe le poste de travail passé en paramètre à l'absence justifiée.
     * 
     * @param posteTravail le poste de travail à associer avec cette absence justifiée
     */
    public void setPosteTravail(PosteTravail posteTravail) {
        this.posteTravail = posteTravail;
    }

    /**
     * Retourne le type de l'absence justifiée.
     * 
     * @return le type de l'absence justifiée
     */
    public TypeAbsenceJustifiee getType() {
        return type;
    }

    /**
     * Assigne le type d'absence justifiée passé en paramètre.
     * 
     * @param type un type d'absence justifiée
     */
    public void setType(TypeAbsenceJustifiee type) {
        this.type = type;
    }

    /**
     * Retourne l'état de l'absence justifiée.
     * 
     * @return l'état de l'absence justifiée
     */
    public Etat getEtat() {
        return etat;
    }

    /**
     * Assigne l'état d'absence justifiée passé en paramètre.
     * 
     * @param etat un état d'absence justifiée
     */
    public void setEtat(Etat etat) {
        this.etat = etat;
    }

    /**
     * La période à laquelle l'absence justifiée se rapporte.
     * 
     * @return la période en question
     */
    @Override
    public Periode getPeriode() {
        return periode;
    }

    /**
     * Assigne la période à laquelle l'absence justifiée se rapporte.
     * 
     * @param periode la période en question
     */
    @Override
    public void setPeriode(Periode periode) {
        this.periode = periode;
    }

    /**
     * Le montant brut associé à cette absence justifiée.
     * 
     * @return le montant
     */
    public Montant getMontantBrut() {
        return montantBrut;
    }

    /**
     * Assigne le montant brut associé à cette absence justifiée.
     * 
     * @param montant le montant brut à associer à cette absence justifiée
     */
    public void setMontantBrut(Montant montant) {
        montantBrut = montant;
    }

    /**
     * Le montant versé associé à cette absence justifiée.
     * 
     * @return le montant
     */
    public Montant getMontantVerse() {
        return montantVerse;
    }

    /**
     * Assigne le montant versé associé à cette absence justifiée.
     * 
     * @param montant le montant brut à associer à cette absence justifiée
     */
    public void setMontantVerse(Montant montant) {
        montantVerse = montant;
    }

    /**
     * Retourne le no° d'affilié de l'employeur du poste de travail.
     */
    @Override
    public String getNoAffilie() {
        if (posteTravail == null) {
            return null;
        }
        return posteTravail.getAffilieNumero();
    }

    /**
     * Retourne l'id du poste de travail auquel l'absence justifiée est rattachée.
     * 
     * @return Id du poste de travail ou null si inexistant
     */
    public String getIdPosteTravail() {
        if (posteTravail == null) {
            return null;
        }
        return posteTravail.getId();
    }

    /**
     * Retourne le type de salaire du poste de travail attaché à l'absence justifiée.
     * 
     * @return TypeSalaire représentant un code système, null si poste inexistant
     */
    @Override
    public TypeSalaire getTypeSalaire() {
        if (posteTravail == null) {
            return null;
        }
        return posteTravail.getTypeSalaire();
    }

    /**
     * Retourne la qualification du poste de travail attaché à l'absence justifiée.
     * 
     * @return Qualification représentant un code système, null si poste inexistant
     */
    @Override
    public Qualification getQualification() {
        if (posteTravail == null) {
            return null;
        }
        return posteTravail.getQualification();
    }

    /**
     * Retourne la date de début de l'absence.
     * 
     * @return String au format suisse (dd.MM.yyyy)
     */
    public String getDateDebutAbsence() {
        if (periode == null) {
            return null;
        }
        return periode.getDateDebutAsSwissValue();
    }

    /**
     * Retourne la date de fin de l'absence
     * 
     * @return String au format suisse (dd.MM.yyyy)
     */
    public String getDateFinAbsence() {
        if (periode == null) {
            return null;
        }
        return periode.getDateFinAsSwissValue();
    }

    /**
     * Retourne la raison sociale de l'affiliée auquel l'absence justifiée est rattachée.
     * 
     * @return String représentant le nom de la raison sociale
     */
    public String getRaisonSocialeEmployeur() {
        if (posteTravail == null) {
            return null;
        }
        return posteTravail.getRaisonSocialeEmployeur();
    }

    /**
     * Retourne si l'absence justifiée est modifiable.
     * 
     * @return Retourne si l'absence est dans un état modifiable
     */
    public boolean isModifiable() {
        return !Etat.COMPTABILISEE.equals(etat) && !Etat.TRAITEE.equals(etat);
    }

    public Taux getTauxAVS() {
        return tauxAVS;
    }

    public void setTauxAVS(Taux tauxAVS) {
        this.tauxAVS = tauxAVS;
    }

    public Taux getTauxAC() {
        return tauxAC;
    }

    public void setTauxAC(Taux tauxAC) {
        this.tauxAC = tauxAC;
    }

    public LienParente getLienParente() {
        return lienParente;
    }

    public void setLienParente(LienParente lienParente) {
        this.lienParente = lienParente;
    }

    public String getIdPassageFacturation() {
        if (passage == null) {
            return null;
        }
        return passage.getId();
    }

    public void setIdPassageFacturation(String idPassageFacturation) {
        if (passage == null) {
            passage = new Passage();
        }
        passage.setId(idPassageFacturation);
    }

    public double getNombreDeJours() {
        return nombreDeJours;
    }

    public void setNombreDeJours(double nombreDeJours) {
        this.nombreDeJours = nombreDeJours;
    }

    public double getNombreHeuresParJour() {
        return nombreHeuresParJour;
    }

    public void setNombreHeuresParJour(double nombreHeuresParJour) {
        this.nombreHeuresParJour = nombreHeuresParJour;
    }

    public void setBeneficiaire(Beneficiaire beneficiaire) {
        this.beneficiaire = beneficiaire;
    }

    public Beneficiaire getBeneficiaire() {
        return beneficiaire;
    }

    public Date getDateComptabilisation() {
        return journal.getDateComptabilisation();
    }

    /**
     * Retourne le passage de facturation attachée à l'absence justifiée.
     * 
     * @return Passage de facturation
     */
    public Passage getPassage() {
        return passage;
    }

    /**
     * Assigne le passage de facturation à l'absence justifiée.
     * 
     * @param passage Passage de facturation à attacher
     */
    public void setPassage(Passage passage) {
        this.passage = passage;
    }

    public Montant getSalaireHoraire() {
        return salaireHoraire;
    }

    public void setSalaireHoraire(Montant salaireHoraire) {
        this.salaireHoraire = salaireHoraire;
    }

    public String getAnnee() {
        return getPeriode().getDateFin().getAnnee();
    }

    private boolean hasPosteTravail() {
        return posteTravail != null;
    }

    /**
     * Retourne l'id tiers lié à l'employeur
     * 
     * @return String représentant l'id tiers lié à l'employeur, si pas d'employeur ou poste de travail, retourne null
     */
    public String getEmployeurIdTiers() {
        if (!hasPosteTravail()) {
            return null;
        }
        return posteTravail.getEmployeurIdTiers();
    }

    /**
     * Retourne la langue de l'employeur
     * 
     * @return langue de l'employeur
     */

    /**
     * Retourne l'id tiers lié au travailleur
     * 
     * @return String représentant l'id tiers lié au travailleur, si pas de travailleur ou poste de travail, retourne
     *         null
     */
    public String getTravailleurIdTiers() {
        if (!hasPosteTravail()) {
            return null;
        }
        return posteTravail.getTravailleurIdTiers();
    }

    /**
     * Retourne le NSS du travailleur
     * 
     * @return String représentant le NSS du travailleur, si pas de travailleur ou poste de travail, retourne
     *         null
     */
    public String getTravailleurNss() {
        if (!hasPosteTravail()) {
            return null;
        }
        return posteTravail.getTravailleurNss();
    }

    /**
     * Retourne la date de facturation.
     * 
     * @return String représentant la date de facturation
     */
    public String getDateFacturation() {
        return passage.getDateFacturation();
    }

    /**
     * Vérifie que l'absence justifiée soit valide.
     * <ul>
     * <li>Période correcte (date début et date fin valide)
     * 
     * @throws UnsatisfiedSpecificationException
     */
    public void validate() throws UnsatisfiedSpecificationException {
        final AJPeriodeRequiseSpecification periodeRequiseSpecification = new AJPeriodeRequiseSpecification();
        final AJPeriodeInActivitePoste periodeInActivitePosteSpecification = new AJPeriodeInActivitePoste();

        periodeRequiseSpecification.and(periodeInActivitePosteSpecification).isSatisfiedBy(this);
    }

    /**
     * Retourne le montant de la part patronale, soit le montant brut multiplié par le taux AC et taux AVS
     * 
     * @return Montant de la part patronale
     */
    public Montant getPartPatronale() {
        Taux taux = tauxAC.addTaux(tauxAVS);
        return montantBrut.multiply(taux);
    }

    @Override
    public String getIdEmployeur() {
        return posteTravail.getIdEmployeur();
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
        return getMontantVerse();
    }

    /**
     * Retourne le montant brut relatif à l'absence justifiée.
     * Si la qualification est de type "ouvrier", le montant brut sera retourné. Si autre, 0.
     * 
     * @return Montant brut si salaire ouvrier, 0 dans le cas contraire
     */
    public Montant getMontantBrutOuvrier() {
        if (isQualificationOuvrier()) {
            return montantBrut;
        } else {
            return Montant.ZERO;
        }
    }

    /**
     * Retourne le montant brut relatif à l'absence justifiée.
     * Si la qualification est de type "employe", le montant brut sera retourné. Si autre, 0.
     * 
     * @return Montant brut si salaire ouvrier, 0 dans le cas contraire
     */
    public Montant getMontantBrutEmploye() {
        if (isQualificationEmploye()) {
            return montantBrut;
        } else {
            return Montant.ZERO;
        }
    }

    public boolean isQualificationEmploye() {
        if (posteTravail == null) {
            throw new NullPointerException("Le poste de travail lié est null");
        }
        return posteTravail.isQualificationEmploye();
    }

    public boolean isQualificationOuvrier() {
        if (posteTravail == null) {
            throw new NullPointerException("Le poste de travail lié est null");
        }
        return posteTravail.isQualificationOuvrier();
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
        return type.getValue();
    }

    @Override
    public Convention getConventionEmployeur() {
        return posteTravail.getEmployeur().getConvention();
    }

    @Override
    public int compareTo(AbsenceJustifiee o) {
        if (idAbsenceJustifiee == null && o.idAbsenceJustifiee == null) {
            return 0;
        }
        return idAbsenceJustifiee.compareTo(o.idAbsenceJustifiee);
    }

    @Override
    public Periode getPeriodeActivitePoste() {
        return posteTravail.getPeriodeActivite();
    }

    public CodeLangue getLangue() {
        return CodeLangue.fromValue(posteTravail.getEmployeur().getLangue());
    }

    public Employeur getEmployeur() {
        return posteTravail.getEmployeur();
    }

    public Montant getMontantAVS() {
        return montantBrut.multiply(tauxAVS).normalize();
    }

    public Montant getMontantAC() {
        return montantBrut.multiply(tauxAC).normalize();
    }
}
