package ch.globaz.vulpecula.ws.bean;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import ch.globaz.pyxis.domaine.EtatCivil;
import ch.globaz.vulpecula.domain.models.decompte.TypeSalaire;
import ch.globaz.vulpecula.domain.models.postetravail.Qualification;
import ch.globaz.vulpecula.domain.models.postetravail.TravailleurEbuDomain;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "travailleur")
public class TravailleurEbu {
    // Travailleur
    // @XmlElement(required = true)
    private String id;
    private String correlationId;
    private String posteCorrelationId;
    private String synchroId;
    private String idEmployeur;
    private String idPosteTravail;
    private String nom;
    private String prenom;
    private AdresseEbu adresse;
    private String telephone;
    private String nationalite;
    private EtatCivil etatCivil;
    private String dateNaissance;
    private String nss;
    private PermisTravailEbu permisSejour;
    private int nombreEnfants;
    private CompteBanquaireEbu compteBancaire;
    private String numeroEntreprise;
    private String nomEntreprise;
    private String profession;
    private String dateDebutActivite;
    private String dateFinActivite;
    private double tauxActivite;
    private String dateTauxActivite;
    private DureeContratEbu dureeContrat;
    private Qualification codeProfessionnel;
    private String dateValiditeQualification;
    private ContratAssuranceMaladieEbu contratCollectif;
    private String dateInscription;
    private ConventionEbu convention;
    private double salaire;
    private TypeSalaire typeSalaire;
    private String dateValiditeTypeSalaire;
    private String sexe;
    private String idDecompte;
    private String idTravailleur;
    private String dateLimiteReactivation;
    private StatusAnnonceTravailleurEbu status;
    // private Boolean isNewAnnonce;

    private static final int NB_MONTH_LIMIT_REACTIVATION_POSTE = 9;

    public TravailleurEbu() {
        // Constructeur par defaut obligatoire pour le bon fonctionnement du framework
    }

    public TravailleurEbu(TravailleurEbuDomain tra) {
        idTravailleur = tra.getIdTravailleur();
        id = tra.getId();
        correlationId = tra.getCorrelationId();
        posteCorrelationId = tra.getPosteCorrelationId();
        synchroId = tra.getSynchroId();
        idEmployeur = tra.getIdEmployeur();
        idPosteTravail = tra.getIdPosteTravail();
        nom = tra.getNom();
        prenom = tra.getPrenom();
        sexe = tra.getSexe();
        adresse = tra.getAdresse();
        telephone = tra.getTelephone();
        nationalite = tra.getNationalite();
        etatCivil = tra.getEtatCivil();
        dateNaissance = tra.getDateNaissance();
        nss = tra.getNss();
        permisSejour = tra.getPermisSejour();
        nombreEnfants = tra.getNombreEnfants();
        compteBancaire = tra.getCompteBancaire();
        numeroEntreprise = tra.getNumeroEntreprise();
        nomEntreprise = tra.getNomEntreprise();
        profession = tra.getProfession();
        dateDebutActivite = tra.getDateDebutActivite();
        // La date de fin activité correspond à la date de fin du contrat
        if (tra.getDureeContrat() != null && !JadeStringUtil.isEmpty(tra.getDureeContrat().getDateFin())) {
            dateFinActivite = tra.getDateFinActivite();
            dateLimiteReactivation = JadeDateUtil
                    .addMonths(tra.getDateFinActivite(), NB_MONTH_LIMIT_REACTIVATION_POSTE);
        }
        tauxActivite = tra.getTauxActivite();
        dateTauxActivite = tra.getDateTauxActivite();
        dureeContrat = new DureeContratEbu();
        dureeContrat.setDeterminee(false);
        if (tra.getDureeContrat() != null && tra.getDureeContrat().isDeterminee()) {
            dureeContrat.setDeterminee(true);
            dureeContrat.setDateFin(tra.getDureeContrat().getDateFin());
        }

        codeProfessionnel = tra.getCodeProfessionnel();

        contratCollectif = tra.getContratCollectif();
        dateInscription = tra.getDateInscription();
        convention = tra.getConvention();
        if (tra.getSalaire() == null) {
            salaire = 0;
        } else {
            salaire = tra.getSalaire().normalize().doubleValue();
        }

        typeSalaire = tra.getTypeSalaire();
        status = tra.getStatus();
        idDecompte = tra.getIdDecompte();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDateDebutActivite() {
        return dateDebutActivite;
    }

    public String getDateFinActivite() {
        return dateFinActivite;
    }

    public String getSexe() {
        return sexe;
    }

    /**
     * @return the correlationId
     */
    public String getCorrelationId() {
        return correlationId;
    }

    /**
     * @return the correlationId
     */
    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    /**
     * @return the nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * @return the prenom
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * @return the adresse
     */
    public AdresseEbu getAdresse() {
        return adresse;
    }

    /**
     * @return the telephone
     */
    public String getTelephone() {
        return telephone;
    }

    /**
     * @return the nationalite
     */
    public String getNationalite() {
        return nationalite;
    }

    /**
     * @return the etatCivil
     */
    public EtatCivil getEtatCivil() {
        return etatCivil;
    }

    /**
     * @return the dateNaissance
     */
    public String getDateNaissance() {
        return dateNaissance;
    }

    /**
     * @return the nss
     */
    public String getNss() {
        return nss;
    }

    /**
     * @return the permisSejour
     */
    public PermisTravailEbu getPermisSejour() {
        return permisSejour;
    }

    /**
     * @return the nombreEnfants
     */
    public int getNombreEnfants() {
        return nombreEnfants;
    }

    /**
     * @return the compteBancaire
     */
    public CompteBanquaireEbu getCompteBancaire() {
        return compteBancaire;
    }

    /**
     * @return the numeroEntreprise
     */
    public String getNumeroEntreprise() {
        return numeroEntreprise;
    }

    /**
     * @return the nomEntreprise
     */
    public String getNomEntreprise() {
        return nomEntreprise;
    }

    /**
     * @return the profession
     */
    public String getProfession() {
        return profession;
    }

    /**
     * @return the tauxActivite
     */
    public double getTauxActivite() {
        return tauxActivite;
    }

    /**
     * @return the dateTauxActivite
     */
    public String getDateTauxActivite() {
        return dateTauxActivite;
    }

    /**
     * @return the dureeContrat
     */
    public DureeContratEbu getDureeContrat() {
        return dureeContrat;
    }

    /**
     * @return the codeProfessionnel
     */
    public Qualification getCodeProfessionnel() {
        return codeProfessionnel;
    }

    /**
     * @return the contratCollectif
     */
    public ContratAssuranceMaladieEbu getContratCollectif() {
        return contratCollectif;
    }

    /**
     * @return the dateInscription
     */
    public String getDateInscription() {
        return dateInscription;
    }

    /**
     * @return the convention
     */
    public ConventionEbu getConvention() {
        return convention;
    }

    /**
     * @return the salaire
     */
    public double getSalaire() {
        return salaire;
    }

    /**
     * @return the typeSalaire
     */
    public TypeSalaire getTypeSalaire() {
        return typeSalaire;
    }

    /**
     * @return the idEmployeur
     */
    public String getIdEmployeur() {
        return idEmployeur;
    }

    /**
     * @return the idDecompte
     */
    public String getIdDecompte() {
        return idDecompte;
    }

    public String getSynchroId() {
        return synchroId;
    }

    public void setSynchroId(String synchroId) {
        this.synchroId = synchroId;
    }

    public String getIdTravailleur() {
        return idTravailleur;
    }

    public void setIdTravailleur(String idTravailleur) {
        this.idTravailleur = idTravailleur;
    }

    public String getIdPosteTravail() {
        return idPosteTravail;
    }

    public void setIdPosteTravail(String idPosteTravail) {
        this.idPosteTravail = idPosteTravail;
    }

    public String getPosteCorrelationId() {
        return posteCorrelationId;
    }

    public void setPosteCorrelationId(String posteCorrelationId) {
        this.posteCorrelationId = posteCorrelationId;
    }

    public String getDateLimiteReactivation() {
        return dateLimiteReactivation;
    }

    public void setDateLimiteReactivation(String dateLimiteReactivation) {
        this.dateLimiteReactivation = dateLimiteReactivation;
    }

    public String getDateValiditeQualification() {
        return dateValiditeQualification;
    }

    public void setDateValiditeQualification(String dateValiditeQualification) {
        this.dateValiditeQualification = dateValiditeQualification;
    }

    public String getDateValiditeTypeSalaire() {
        return dateValiditeTypeSalaire;
    }

    public void setDateValiditeTypeSalaire(String dateValiditeTypeSalaire) {
        this.dateValiditeTypeSalaire = dateValiditeTypeSalaire;
    }

    public StatusAnnonceTravailleurEbu getStatus() {
        return status;
    }

    public void setStatus(StatusAnnonceTravailleurEbu status) {
        this.status = status;
    }

    // public Boolean getIsNewAnnonce() {
    // return isNewAnnonce;
    // }
    //
    // public void setIsNewAnnonce(Boolean isNewAnnonce) {
    // this.isNewAnnonce = isNewAnnonce;
    // }

}
