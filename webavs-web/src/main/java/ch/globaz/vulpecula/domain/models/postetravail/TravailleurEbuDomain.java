package ch.globaz.vulpecula.domain.models.postetravail;

import globaz.commons.nss.NSUtil;
import globaz.jade.client.util.JadeStringUtil;
import ch.globaz.pyxis.domaine.EtatCivil;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.DomainEntity;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.decompte.TypeSalaire;
import ch.globaz.vulpecula.ws.bean.AdresseEbu;
import ch.globaz.vulpecula.ws.bean.CompteBanquaireEbu;
import ch.globaz.vulpecula.ws.bean.ContratAssuranceMaladieEbu;
import ch.globaz.vulpecula.ws.bean.ConventionEbu;
import ch.globaz.vulpecula.ws.bean.DureeContratEbu;
import ch.globaz.vulpecula.ws.bean.PermisTravailEbu;
import ch.globaz.vulpecula.ws.bean.StatusAnnonceTravailleurEbu;
import ch.globaz.vulpecula.ws.bean.StatusEbu;
import ch.globaz.vulpecula.ws.bean.TravailleurEbu;

/***
 * Le travailleur correspond à un Tiers (Pyxis).
 * 
 */
public class TravailleurEbuDomain implements DomainEntity {
    private String id;
    private String correlationId;
    private String posteCorrelationId;
    private String synchroId;
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
    private double tauxActivite;
    private String dateTauxActivite;
    private DureeContratEbu dureeContrat;
    private Qualification codeProfessionnel;
    private ContratAssuranceMaladieEbu contratCollectif;
    private String dateInscription;
    private ConventionEbu convention;
    private String idTravailleur;
    private Montant salaire;
    private TypeSalaire typeSalaire;
    private String idEmployeur;
    private String nomUpper;
    private String prenomUpper;
    private String idDecompte;
    private boolean traite;
    private String sexe;
    private String dateDebutActivite;
    private String dateFinActivite;
    private boolean modification;
    private StatusEbu tiersStatus;
    private StatusEbu posteStatus;
    private StatusEbu travailleurStatus;
    private StatusEbu banqueStatus;
    private StatusAnnonceTravailleurEbu status;
    // private Boolean isNewAnnonce;

    private String spy;

    public TravailleurEbuDomain() {
        // Constructeur par defaut obligatoire pour le bon fonctionnement du framework
    }

    public TravailleurEbuDomain(TravailleurEbu travailleurEbu) {
        id = travailleurEbu.getId();
        correlationId = travailleurEbu.getCorrelationId();
        posteCorrelationId = travailleurEbu.getPosteCorrelationId();
        idDecompte = travailleurEbu.getIdDecompte();
        idEmployeur = travailleurEbu.getIdEmployeur();
        nom = travailleurEbu.getNom();
        prenom = travailleurEbu.getPrenom();
        telephone = travailleurEbu.getTelephone();
        nationalite = travailleurEbu.getNationalite();
        dateNaissance = travailleurEbu.getDateNaissance();
        nss = formatNSS(travailleurEbu.getNss());
        nombreEnfants = travailleurEbu.getNombreEnfants();
        numeroEntreprise = travailleurEbu.getNumeroEntreprise();
        nomEntreprise = travailleurEbu.getNomEntreprise();
        profession = travailleurEbu.getProfession();
        dateDebutActivite = travailleurEbu.getDateDebutActivite();

        if (travailleurEbu.getDureeContrat() != null
                && !JadeStringUtil.isEmpty(travailleurEbu.getDureeContrat().getDateFin())) {
            dateFinActivite = travailleurEbu.getDureeContrat().getDateFin();
        }
        if (JadeStringUtil.isEmpty(dateFinActivite)) {
            dateFinActivite = travailleurEbu.getDateFinActivite();
        }
        sexe = travailleurEbu.getSexe();
        tauxActivite = travailleurEbu.getTauxActivite();
        dateTauxActivite = travailleurEbu.getDateTauxActivite();
        if (travailleurEbu.getDureeContrat() != null) {
            dureeContrat = new DureeContratEbu();
            dureeContrat.setDeterminee(travailleurEbu.getDureeContrat().isDeterminee());
            dureeContrat.setDateFin(travailleurEbu.getDureeContrat().getDateFin());
        }

        codeProfessionnel = travailleurEbu.getCodeProfessionnel();

        contratCollectif = travailleurEbu.getContratCollectif();
        contratCollectif.setAssureur(travailleurEbu.getContratCollectif().getAssureur());
        dateInscription = travailleurEbu.getDateInscription();
        convention = travailleurEbu.getConvention();
        salaire = new Montant(travailleurEbu.getSalaire());

        typeSalaire = travailleurEbu.getTypeSalaire();

        adresse = travailleurEbu.getAdresse();
        etatCivil = travailleurEbu.getEtatCivil();
        permisSejour = travailleurEbu.getPermisSejour();
        compteBancaire = travailleurEbu.getCompteBancaire();
        modification = checkIfModification();
        idTravailleur = travailleurEbu.getIdTravailleur();
        idPosteTravail = travailleurEbu.getIdPosteTravail();
        status = travailleurEbu.getStatus();
        // isNewAnnonce = travailleurEbu.getIsNewAnnonce();

    }

    private String formatNSS(String p_nss) {
        String s = "";
        if (!JadeStringUtil.isEmpty(p_nss) && !"756.".equals(p_nss)) {
            s = NSUtil.formatAVSNew(p_nss, true);
        }
        return s;
    }

    private boolean checkIfModification() {
        Travailleur travailleur = null;
        if (getNss() != null && !getNss().isEmpty()) {
            travailleur = VulpeculaRepositoryLocator.getTravailleurRepository().findByNss(getNss());
        }

        if (travailleur == null) {
            if (getIdTravailleur() != null && !getIdTravailleur().isEmpty()) {
                travailleur = VulpeculaRepositoryLocator.getTravailleurRepository().findById(getIdTravailleur());
            }
        }

        if (travailleur == null && getCorrelationId() != null && !getCorrelationId().isEmpty()) {
            travailleur = VulpeculaRepositoryLocator.getTravailleurRepository().findByCorrelationId(getCorrelationId());
        }
        if (travailleur == null) {
            if (getNom() != null && !getNom().isEmpty() && getPrenom() != null && !getPrenom().isEmpty()
                    && getDateNaissance() != null && !getDateNaissance().isEmpty()) {
                travailleur = VulpeculaServiceLocator.getTravailleurService().findByNomPrenomDateNaissanceEmployeur(
                        getNom(), getPrenom(), getDateNaissance(), getIdEmployeur());
            }
        }

        if (travailleur == null) {
            travailleur = VulpeculaServiceLocator.getTravailleurService().findByNomPrenomDateNaissance(getNom(),
                    getPrenom(), getDateNaissance());
        }

        return travailleur != null;
    }

    public PosteTravail getPosteTravailFictif() {
        PosteTravail poste = new PosteTravail();
        poste.setCorrelationId(correlationId);
        poste.setEmployeur(new Employeur(idEmployeur));
        poste.setQualification(codeProfessionnel);
        Travailleur travailleur = new Travailleur();
        travailleur.setDesignation1(nom);
        travailleur.setDesignation2(prenom);
        travailleur.setNumAvsActuel(nss);
        travailleur.setDateNaissance(dateNaissance);
        poste.setTravailleur(travailleur);
        poste.setPeriodeActivite(new Periode(dateDebutActivite, dateFinActivite));
        return poste;
    }

    public PosteTravail getPosteTravailFictifForSynchro() {
        PosteTravail poste = new PosteTravail();
        Occupation occupation = new Occupation();
        occupation.setTaux(new Taux(tauxActivite));
        if (!Date.isNull(dateTauxActivite)) {
            Date dateValiditeTaux = new Date(dateTauxActivite);
            occupation.setDateValidite(dateValiditeTaux);
        } else {
            Date dateValiditeTaux = new Date(new java.util.Date());
            occupation.setDateValidite(dateValiditeTaux);
        }
        poste.addTauxOccupation(occupation);
        poste.setCorrelationId(correlationId);
        poste.setEmployeur(new Employeur(idEmployeur));
        poste.setQualification(codeProfessionnel);
        poste.setTypeSalaire(typeSalaire);
        Travailleur travailleur = new Travailleur();
        travailleur.setDesignation1(nom);
        travailleur.setDesignation2(prenom);
        travailleur.setNumAvsActuel(nss);
        travailleur.setDateNaissance(dateNaissance);
        poste.setTravailleur(travailleur);
        poste.setPeriodeActivite(new Periode(dateDebutActivite, dateFinActivite));
        return poste;
    }

    public StatusAnnonceTravailleurEbu getStatus() {
        return status;
    }

    public void setStatus(StatusAnnonceTravailleurEbu status) {
        this.status = status;
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

    public String getIdDecompte() {
        return idDecompte;
    }

    public void setIdDecompte(String idDecompte) {
        this.idDecompte = idDecompte;
    }

    public Montant getSalaire() {
        return salaire;
    }

    public void setSalaire(Montant salaire) {
        this.salaire = salaire;
    }

    public TypeSalaire getTypeSalaire() {
        return typeSalaire;
    }

    public void setTypeSalaire(TypeSalaire typeSalaire) {
        this.typeSalaire = typeSalaire;
    }

    public String getIdEmployeur() {
        return idEmployeur;
    }

    public void setIdEmployeur(String idEmployeur) {
        this.idEmployeur = idEmployeur;
    }

    public boolean isTraite() {
        return status.equals(StatusAnnonceTravailleurEbu.TRAITE) || status.equals(StatusAnnonceTravailleurEbu.REFUSE)
                || status.equals(StatusAnnonceTravailleurEbu.AUTO);
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getDateDebutActivite() {
        return dateDebutActivite;
    }

    public void setDateDebutActivite(String dateDebutActivite) {
        this.dateDebutActivite = dateDebutActivite;
    }

    public String getDateFinActivite() {
        return dateFinActivite;
    }

    public void setDateFinActivite(String dateFinActivite) {
        this.dateFinActivite = dateFinActivite;
    }

    public void setTraite(boolean traite) {
        this.traite = traite;
    }

    public String getNomUpper() {
        return nomUpper;
    }

    public void setNomUpper(String nomUpper) {
        this.nomUpper = nomUpper;
    }

    public String getPrenomUpper() {
        return prenomUpper;
    }

    public void setPrenomUpper(String prenomUpper) {
        this.prenomUpper = prenomUpper;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getAdresseFormatte() {
        StringBuilder adressAsString = new StringBuilder();
        adressAsString.append(adresse.getDescription1());
        adressAsString.append(" ");
        adressAsString.append(adresse.getDescription2());

        adressAsString.append("<br/>");
        adressAsString.append(adresse.getRue());
        adressAsString.append(" ");
        adressAsString.append(adresse.getRueNumero());

        adressAsString.append("<br/>");
        adressAsString.append(adresse.getNpa());
        adressAsString.append(" ");
        adressAsString.append(adresse.getLocalite());

        return adressAsString.toString();
    }

    public AdresseEbu getAdresse() {
        return adresse;
    }

    public void setAdresse(AdresseEbu adresse) {
        this.adresse = adresse;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getNationalite() {
        return nationalite;
    }

    public void setNationalite(String nationalite) {
        this.nationalite = nationalite;
    }

    public EtatCivil getEtatCivil() {
        return etatCivil;
    }

    public void setEtatCivil(EtatCivil etatCivil) {
        this.etatCivil = etatCivil;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getNss() {
        return nss;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public PermisTravailEbu getPermisSejour() {
        return permisSejour;
    }

    public void setPermisSejour(PermisTravailEbu permisSejour) {
        this.permisSejour = permisSejour;
    }

    public int getNombreEnfants() {
        return nombreEnfants;
    }

    public void setNombreEnfants(int nombreEnfants) {
        this.nombreEnfants = nombreEnfants;
    }

    public CompteBanquaireEbu getCompteBancaire() {
        return compteBancaire;
    }

    public void setCompteBancaire(CompteBanquaireEbu compteBancaire) {
        this.compteBancaire = compteBancaire;
    }

    public String getNumeroEntreprise() {
        return numeroEntreprise;
    }

    public void setNumeroEntreprise(String numeroEntreprise) {
        this.numeroEntreprise = numeroEntreprise;
    }

    public String getNomEntreprise() {
        return nomEntreprise;
    }

    public void setNomEntreprise(String nomEntreprise) {
        this.nomEntreprise = nomEntreprise;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public double getTauxActivite() {
        return tauxActivite;
    }

    public void setTauxActivite(double tauxActivite) {
        this.tauxActivite = tauxActivite;
    }

    public String getDateTauxActivite() {
        return dateTauxActivite;
    }

    public void setDateTauxActivite(String dateTauxActivite) {
        this.dateTauxActivite = dateTauxActivite;
    }

    public DureeContratEbu getDureeContrat() {
        return dureeContrat;
    }

    public void setDureeContrat(DureeContratEbu dureeContrat) {
        this.dureeContrat = dureeContrat;
    }

    public Qualification getCodeProfessionnel() {
        return codeProfessionnel;
    }

    public void setCodeProfessionnel(Qualification codeProfessionnel) {
        this.codeProfessionnel = codeProfessionnel;
    }

    public ContratAssuranceMaladieEbu getContratCollectif() {
        return contratCollectif;
    }

    public void setContratCollectif(ContratAssuranceMaladieEbu contratCollectif) {
        this.contratCollectif = contratCollectif;
    }

    public String getDateInscription() {
        return dateInscription;
    }

    public void setDateInscription(String dateInscription) {
        this.dateInscription = dateInscription;
    }

    public ConventionEbu getConvention() {
        return convention;
    }

    public void setConvention(ConventionEbu convention) {
        this.convention = convention;
    }

    public String getIdTravailleur() {
        return idTravailleur;
    }

    public void setIdTravailleur(String idTravailleur) {
        this.idTravailleur = idTravailleur;
    }

    public boolean isModification() {
        return modification;
    }

    public void setModification(boolean modification) {
        this.modification = modification;
    }

    public StatusEbu getTiersStatus() {
        return tiersStatus;
    }

    public void setTiersStatus(StatusEbu tiersStatus) {
        this.tiersStatus = tiersStatus;
    }

    public StatusEbu getPosteStatus() {
        return posteStatus;
    }

    public void setPosteStatus(StatusEbu posteStatus) {
        this.posteStatus = posteStatus;
    }

    public StatusEbu getTravailleurStatus() {
        return travailleurStatus;
    }

    public void setTravailleurStatus(StatusEbu travailleurStatus) {
        this.travailleurStatus = travailleurStatus;
    }

    public StatusEbu getBanqueStatus() {
        return banqueStatus;
    }

    public void setBanqueStatus(StatusEbu banqueStatus) {
        this.banqueStatus = banqueStatus;
    }

    public String getSynchroId() {
        return synchroId;
    }

    public void setSynchroId(String synchroId) {
        this.synchroId = synchroId;
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

}
