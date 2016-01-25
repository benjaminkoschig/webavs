package ch.globaz.vulpecula.domain.models.congepaye;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;
import ch.globaz.specifications.Specification;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.decompte.TypeAssurance;
import ch.globaz.vulpecula.domain.models.decompte.TypeSalaire;
import ch.globaz.vulpecula.domain.models.parametrage.TableParametrage;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.domain.models.postetravail.Qualification;
import ch.globaz.vulpecula.domain.models.prestations.Beneficiaire;
import ch.globaz.vulpecula.domain.models.prestations.Etat;
import ch.globaz.vulpecula.domain.models.prestations.Prestation;
import ch.globaz.vulpecula.domain.models.registre.Convention;
import ch.globaz.vulpecula.domain.specifications.congepaye.CPPeriodeAnnuelleRequise;
import ch.globaz.vulpecula.domain.specifications.congepaye.CPPeriodeInActivitePoste;
import ch.globaz.vulpecula.domain.specifications.congepaye.CPSalaireDeclareSpecification;
import ch.globaz.vulpecula.external.models.musca.Passage;
import ch.globaz.vulpecula.external.models.osiris.Journal;

public class CongePaye implements Prestation, Comparable<CongePaye> {

    private String idCongePaye;
    private PosteTravail posteTravail;
    private Montant salaireNonDeclare;
    private Date dateSalaireNonDeclare;
    private Taux tauxCP;
    private List<TauxCongePaye> tauxCongePayes;
    private Annee anneeDebut;
    private Annee anneeFin;
    private Beneficiaire beneficiaire;
    private Etat etat;
    private String spy;
    private Passage passage = new Passage();
    private Journal journal = new Journal();

    private Montant salaireDeclare;
    private Montant montantNet;

    public CongePaye() {
        this(null);
    }

    public CongePaye(String id) {
        idCongePaye = id;
        tauxCongePayes = new ArrayList<TauxCongePaye>();
    }

    public Passage getPassage() {
        return passage;
    }

    public void setPassage(Passage passage) {
        this.passage = passage;
    }

    @Override
    public String getId() {
        return idCongePaye;
    }

    @Override
    public void setId(String id) {
        idCongePaye = id;
    }

    @Override
    public String getSpy() {
        return spy;
    }

    @Override
    public void setSpy(String spy) {
        this.spy = spy;
    }

    public String getIdCongePaye() {
        return idCongePaye;
    }

    public String getIdPosteTravail() {
        return posteTravail.getId();
    }

    public void setIdCongePaye(String idCongePaye) {
        this.idCongePaye = idCongePaye;
    }

    @Override
    public PosteTravail getPosteTravail() {
        return posteTravail;
    }

    public void setPosteTravail(PosteTravail posteTravail) {
        this.posteTravail = posteTravail;
    }

    public Beneficiaire getBeneficiaire() {
        return beneficiaire;
    }

    public void setBeneficiaire(Beneficiaire beneficiaire) {
        this.beneficiaire = beneficiaire;
    }

    public Etat getEtat() {
        return etat;
    }

    public void setEtat(Etat etat) {
        this.etat = etat;
    }

    public Annee getAnneeDebut() {
        return anneeDebut;
    }

    public void setAnneeDebut(Annee anneeDebut) {
        this.anneeDebut = anneeDebut;
    }

    public Annee getAnneeFin() {
        return anneeFin;
    }

    public Periode getPeriode() {
        if (anneeFin == null) {
            return new Periode(anneeDebut.getFirstDayOfYear(), null);
        } else {
            return new Periode(anneeDebut.getFirstDayOfYear(), anneeFin.getFirstDayOfYear());
        }
    }

    public String getPeriodeAsValue() {
        return anneeDebut.getValue() + " - " + anneeFin.getValue();
    }

    public void setAnneeFin(Annee anneeFin) {
        this.anneeFin = anneeFin;
    }

    public List<TauxCongePaye> getTauxCongePayes() {
        return tauxCongePayes;
    }

    public void setTauxCongePayes(List<TauxCongePaye> tauxCongePayes) {
        this.tauxCongePayes = tauxCongePayes;
    }

    /**
     * Retourne le no� d'affili� de l'employeur du poste de travail.
     */
    @Override
    public String getNoAffilie() {
        if (posteTravail == null) {
            return null;
        }
        return posteTravail.getAffilieNumero();
    }

    /**
     * Retourne l'id du tiers associ� � l'employeur du poste de travail.
     */
    public String getEmployeurIdTiers() {
        if (posteTravail == null) {
            return null;
        }
        return posteTravail.getEmployeurIdTiers();
    }

    /**
     * Retourne la raison sociale de l'affili�e auquel l'absence justifi�e est rattach�e.
     * 
     * @return String repr�sentant le nom de la raison sociale
     */
    public String getRaisonSocialeEmployeur() {
        if (posteTravail == null) {
            return null;
        }
        return posteTravail.getRaisonSocialeEmployeur();
    }

    /**
     * Retourne le type de salaire du poste de travail attach� � l'absence justifi�e.
     * 
     * @return TypeSalaire repr�sentant un code syst�me, null si poste inexistant
     */
    @Override
    public TypeSalaire getTypeSalaire() {
        if (posteTravail == null) {
            return null;
        }
        return posteTravail.getTypeSalaire();
    }

    /**
     * Retourne la qualification du poste de travail attach� � l'absence justifi�e.
     * 
     * @return Qualification repr�sentant un code syst�me, null si poste inexistant
     */
    @Override
    public Qualification getQualification() {
        if (posteTravail == null) {
            return null;
        }
        return posteTravail.getQualification();
    }

    /**
     * Retourne TRUE si le cong� pay� est modifiable.
     * 
     * @return Retourne TRUE si le cong� pay� est modifiable.
     */
    public boolean isModifiable() {
        return !Etat.COMPTABILISEE.equals(etat) && !Etat.TRAITEE.equals(etat);
    }

    /**
     * Retourne l'addition du salaire non d�clar� et du salaire d�clar�.
     * 
     * @return Montant des salaires
     */
    public Montant getSalaires() {
        return salaireDeclare.add(salaireNonDeclare);
    }

    /**
     * @return the salaireNonDeclare
     */
    public Montant getSalaireNonDeclare() {
        return salaireNonDeclare;
    }

    /**
     * @return the dateSalaireNonDeclare
     */
    public Date getDateSalaireNonDeclare() {
        return dateSalaireNonDeclare;
    }

    /**
     * @return the tauxCP
     */
    public Taux getTauxCP() {
        return tauxCP;
    }

    /**
     * @return le taux du cong� pay� en String et avec deux chiffres apr�s la virgule
     */
    public String getTauxCPValueScale2() {
        if (tauxCP == null) {
            return "0.00";
        }
        return tauxCP.getValueWith(2);
    }

    /**
     * @param noCaisseMetier num�ro de la caisse m�tier
     * @param age �ge du b�n�ficiaire
     * @return le taux
     * @throws GlobazTechnicalException Quand le fichier de param�trage ne peut pas �tre charg�
     */
    public Taux getTauxCPParametrage(int noCaisseMetier) {
        if (posteTravail == null) {
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE);
        }
        return new Taux(TableParametrage.getInstance().getNbJoursTaux(noCaisseMetier, posteTravail.getAgeTravailleur())
                .getTaux());
    }

    /**
     * Retourne le nombre de jours par semaine pour la caisse m�tier
     * 
     * @param noCaisseMetier No de la caisse m�tier
     * @return double repr�sentant le nombre de jours
     * @throws GlobazTechnicalException
     */
    public double getNbJoursParSemaine(int noCaisseMetier) {
        if (posteTravail == null) {
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE);
        }
        return TableParametrage.getInstance().getHeuresTravailSemaine(noCaisseMetier);
    }

    /**
     * @return the idPassageFacturation
     */
    public String getIdPassageFacturation() {
        return getPassage().getId();
    }

    /**
     * @param salaireNonDeclare the salaireNonDeclare to set
     */
    public void setSalaireNonDeclare(Montant salaireNonDeclare) {
        this.salaireNonDeclare = salaireNonDeclare;
    }

    /**
     * @param dateSalaireNonDeclare the dateSalaireNonDeclare to set
     */
    public void setDateSalaireNonDeclare(Date dateSalaireNonDeclare) {
        this.dateSalaireNonDeclare = dateSalaireNonDeclare;
    }

    /**
     * @param tauxCP the tauxCP to set
     */
    public void setTauxCP(Taux tauxCP) {
        this.tauxCP = tauxCP;
    }

    /**
     * @param idPassageFacturation the idPassageFacturation to set
     */
    public void setIdPassageFacturation(String idPassageFacturation) {
        getPassage().setId(idPassageFacturation);
    }

    /**
     * @return Affiche le montant des salaires (d�clar� et non d�clar�).
     */
    public Montant getTotalSalaire() {
        if (getSalaireDeclare() != null && getSalaireNonDeclare() != null) {
            return getSalaireDeclare().add(getSalaireNonDeclare());
        } else {
            return new Montant(0);
        }

    }

    /**
     * Affiche le montant du cong� pay�.
     * Salaire d�clar� + non d�clar� x taux CP
     * Montant effectif qui sera vers�
     * Arrondi aux 5cts
     * 
     * @return
     */
    public Montant getMontantNet() {
        if (montantNet != null && montantNet.getValue().length() > 0) {
            return montantNet;
        }
        if (getTotalSalaire() != null && getTauxCP() != null) {
            return getTotalSalaire().multiply(getTauxCP()).normalize();
        } else {
            return Montant.ZERO;
        }
    }

    /**
     * Retourne le montant relatif � l'AVS.
     * Si le cong� pay� ne dispose pas de cotisation, 0 sera retourn�.
     * 
     * @return Montant repr�sentant la somme des montant AVS
     */
    public Montant getMontantAVS() {
        return getMontantFor(TypeAssurance.COTISATION_AVS_AI);
    }

    public Montant getMontantAC() {
        return getMontantFor(TypeAssurance.ASSURANCE_CHOMAGE);
    }

    public Montant getMontantAF() {
        return getMontantFor(TypeAssurance.COTISATION_AF);
    }

    public Montant getMontantFCFP() {
        return getMontantFor(TypeAssurance.COTISATION_FFPP_MASSE);
    }

    public Montant getMontantRetaval() {
        return getMontantFor(TypeAssurance.COTISATION_RETAVAL);
    }

    public Montant getMontantLPP() {
        return getMontantFor(TypeAssurance.COTISATION_LPP);
    }

    public Montant getMontantMal() {
        return getMontantFor(TypeAssurance.ASSURANCE_MALADIE);
    }

    public Montant getMontantFor(TypeAssurance typeAssurance) {
        Montant montant = Montant.ZERO;
        for (TauxCongePaye tauxCongePaye : tauxCongePayes) {
            if (typeAssurance.equals(tauxCongePaye.getTypeAssurance())) {
                montant = montant.add(getMontantBrut().multiply(tauxCongePaye.getTaux()));
            }
        }
        return montant;
    }

    public void setSalaireDeclare(Montant salaireDeclare) {
        this.salaireDeclare = salaireDeclare;
    }

    public void setMontantNet(Montant montantNet) {
        this.montantNet = montantNet;
    }

    public Montant getSalaireDeclare() {
        return salaireDeclare;
    }

    /**
     * Retourne la p�riode de d�but compl�te (01.01.yyyy) d�duite � partir de l'ann�e de d�but.
     * 
     * @return String repr�sentant le premier jour de l'ann�e
     */
    public String getPeriodeDebutAsSwissValue() {
        return anneeDebut.getFirstDayOfYear().getSwissValue();
    }

    /**
     * Retourne la p�riode de fin compl�te (31.12.yyyy) d�duite � partir de l'ann�e de fin.
     * 
     * @return String repr�sentant le dernier jour de l'ann�ee
     */
    public String getPeriodeFinAsSwissValue() {
        return anneeFin.getLastDayOfYear().getSwissValue();
    }

    /**
     * 
     * Retourne l'ann�e du d�but du cong� pay� en String.
     * 
     * @return String repr�sentant l'ann�e du d�but
     */
    public String getAnneeDebutAsValue() {
        return String.valueOf(getAnneeDebut().getValue());
    }

    /**
     * Retourne la date du poste de travail li� au passage de facturation.
     * 
     * @return String repr�sentant la date de facturation
     */
    public String getDateFacturation() {
        return passage.getDateFacturation();
    }

    public Date getDateComptabilisation() {
        return journal.getDateComptabilisation();
    }

    public boolean validate() throws UnsatisfiedSpecificationException {
        final CPPeriodeAnnuelleRequise cpPeriodeAnnuelleRequise = new CPPeriodeAnnuelleRequise();
        final CPSalaireDeclareSpecification cpSalaireDeclareSpecification = new CPSalaireDeclareSpecification();
        final CPPeriodeInActivitePoste cpPeriodeInActivitePoste = new CPPeriodeInActivitePoste();

        Specification<CongePaye> specification = cpPeriodeAnnuelleRequise.and(cpSalaireDeclareSpecification).and(
                cpPeriodeInActivitePoste);

        return specification.isSatisfiedBy(this);
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
        return montantNet;
    }

    @Override
    public String getPeriodeAsString() {
        StringBuilder sb = new StringBuilder();
        sb.append(anneeDebut.getValue());
        sb.append(" - ");
        sb.append(anneeFin.getValue());
        return sb.toString();
    }

    @Override
    public String getTypePrestation() {
        return null;
    }

    /**
     * Retourne l'id tiers li� au travailleur
     * 
     * @return String repr�sentant l'id tiers li� au travailleur, si pas de travailleur ou poste de travail, retourne
     *         null
     */
    public String getTravailleurIdTiers() {
        if (posteTravail == null) {
            return null;
        }
        return posteTravail.getTravailleurIdTiers();
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
     * Retourne la convention de l'employeur
     */
    @Override
    public Convention getConventionEmployeur() {
        return posteTravail.getEmployeur().getConvention();
    }

    @Override
    public int compareTo(CongePaye o) {
        return idCongePaye.compareTo(o.idCongePaye);
    }

    /**
     * Retourne la date de naissance du travailleur
     * 
     * @return Date de naissance du travailleur
     */
    public String getDateNaissanceTravailleur() {
        return posteTravail.getDateNaissanceTravailleur();
    }

    /**
     * Retourne le montant brut (total salaire x tauxCP)
     * 
     * @return Montant le montant brut du CP
     */
    public Montant getMontantBrut() {
        return getTotalSalaire().multiply(getTauxCP()).normalize();
    }

    @Override
    public Periode getPeriodeActivitePoste() {
        return posteTravail.getPeriodeActivite();
    }

    /**
     * D�finit si le cong� pay� est charg�. Un cong� pay� qui doit �tre charg� contient un id mais pas de spy
     * 
     * @return true si doit �tre charg�
     */
    public boolean mustBeFetched() {
        return idCongePaye != null && spy == null;
    }

    public Taux getSommeTauxCongePaye() {
        Taux taux = Taux.ZERO();
        for (TauxCongePaye tauxCongePaye : tauxCongePayes) {
            taux = taux.addTaux(tauxCongePaye.getTaux());
        }
        return taux;
    }

    public Journal getJournal() {
        return journal;
    }

    public void setJournal(Journal journal) {
        this.journal = journal;
    }

    public Employeur getEmployeur() {
        return posteTravail.getEmployeur();
    }
}
