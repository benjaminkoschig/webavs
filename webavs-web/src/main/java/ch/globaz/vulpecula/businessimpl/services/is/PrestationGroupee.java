package ch.globaz.vulpecula.businessimpl.services.is;

import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import java.io.Serializable;
import ch.globaz.vulpecula.business.models.is.EntetePrestationComplexModel;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.external.models.pyxis.Adresse;
import ch.globaz.vulpecula.external.models.pyxis.CodeLangue;

public class PrestationGroupee implements Serializable {
    private static final long serialVersionUID = -1885682375645335980L;

    private String nss;
    private String idTiersBeneficiaire;
    private String idAssurance;
    private String titre;
    private String nom;
    private String prenom;
    private Date dateNaissance;
    private String npa;
    private String localite;
    private String localiteAffilie;
    private Date debutVersement;
    private Date finVersement;
    private CodeLangue langue;
    private Montant impots;
    private Montant montantPrestations;
    private Montant frais;
    private String referencePermis;
    private String libelleCaisseAF;
    private String codeCaisseAF;
    private String adresseFormattee;
    private String raisonSociale;
    private Date date;
    private String cantonResidence;

    public String getNss() {
        return nss;
    }

    public void setNss(String nss) {
        this.nss = nss;
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

    public Date getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getNpa() {
        return npa;
    }

    public void setNpa(String npa) {
        this.npa = npa;
    }

    public String getLocalite() {
        return localite;
    }

    public void setLocalite(String localite) {
        this.localite = localite;
    }

    public Date getDebutVersement() {
        return debutVersement;
    }

    public void setDebutVersement(Date debutVersement) {
        this.debutVersement = debutVersement;
    }

    public Date getFinVersement() {
        return finVersement;
    }

    public void setFinVersement(Date finVersement) {
        this.finVersement = finVersement;
    }

    public Montant getImpots() {
        return impots;
    }

    public void setImpots(Montant impots) {
        this.impots = impots;
    }

    public Montant getMontantPrestations() {
        return montantPrestations;
    }

    public String getMontantPrestationsFormatte() {
        return JANumberFormatter.fmt(montantPrestations.getValue(), true, true, false, 2);
    }

    public void setMontantPrestations(Montant montantPrestations) {
        this.montantPrestations = montantPrestations;
    }

    public Montant getFrais() {
        return frais;
    }

    public void setFrais(Montant frais) {
        this.frais = frais;
    }

    public String getReferencePermis() {
        return referencePermis;
    }

    public void setReferencePermis(String referencePermis) {
        this.referencePermis = referencePermis;
    }

    public String getLibelleCaisseAF() {
        return libelleCaisseAF;
    }

    public void setLibelleCaisseAF(String libelleCaisseAF) {
        this.libelleCaisseAF = libelleCaisseAF;
    }

    public String getCodeCaisseAF() {
        return codeCaisseAF;
    }

    public void setCodeCaisseAF(String codeCaisseAF) {
        this.codeCaisseAF = codeCaisseAF;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getAdresseFormattee() {
        return adresseFormattee;
    }

    public void setAdresseFormattee(String adresseFormattee) {
        this.adresseFormattee = adresseFormattee;
    }

    public String getRaisonSociale() {
        return raisonSociale;
    }

    public void setRaisonSociale(String raisonSociale) {
        this.raisonSociale = raisonSociale;
    }

    public CodeLangue getLangue() {
        return langue;
    }

    public void setLangue(CodeLangue langue) {
        this.langue = langue;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setCantonResidence(String cantonResidence) {
        this.cantonResidence = cantonResidence;
    }

    public String getCantonResidence() {
        return cantonResidence;
    }

    /**
     * Création d'une prestation groupée. Une prestation groupée correspond à un ensemble de prestations AF dont les
     * périodes ont été regroupées.
     * 
     * @param montantTotal Montant total des prestations sur une période
     * @param montantImpot Montant total des montants IS sur une période
     * @param dateDebut Date de début regroupant les montantTotal et montantImpot
     * @param dateFin Date de fin regroupant les montantTotal et montantImpot
     * @param adresse Adresse de l'allocataire
     * @param prestationComplexModel Une opération contenant les informations de base.
     * @return
     */
    public static PrestationGroupee create(Montant montantTotal, Montant montantImpot, Montant frais, Date dateDebut,
            Date dateFin, Adresse adresse, String libelleCaisseAF, String codeCaisseAF,
            EntetePrestationComplexModel prestationComplexModel) {
        return create(prestationComplexModel.getNumAvsActuel(), prestationComplexModel.getNom(),
                prestationComplexModel.getPrenom(), prestationComplexModel.getDateNaissance(),
                prestationComplexModel.getReferencePermis(), adresse, montantImpot, montantTotal, frais,
                libelleCaisseAF, codeCaisseAF, prestationComplexModel.getTitre(),
                prestationComplexModel.getRaisonSociale(), prestationComplexModel.getLangue(), dateDebut, dateFin,
                new Date(prestationComplexModel.getPeriodeDe()));
    }

    public static PrestationGroupee create(String numAVS, String nom, String prenom, String dateNaissance,
            String referencePermis, Adresse adresse, Montant montantImpot, Montant montantTotal, Montant frais,
            String libelleCaisseAF, String codeCaisseAF, String titre, String raisonSociale, String langue,
            Date dateDebut, Date dateFin, Date date) {
        PrestationGroupee entetePrestation = new PrestationGroupee();
        entetePrestation.setNss(numAVS);
        entetePrestation.setNom(nom);
        entetePrestation.setPrenom(prenom);
        if (!JadeStringUtil.isEmpty(dateNaissance)) {
            entetePrestation.setDateNaissance(new Date(dateNaissance));
        }
        entetePrestation.setReferencePermis(referencePermis);
        entetePrestation.setDebutVersement(dateDebut);
        entetePrestation.setFinVersement(dateFin);
        entetePrestation.setMontantPrestations(montantTotal);
        entetePrestation.setImpots(montantImpot);
        entetePrestation.setFrais(frais);

        entetePrestation.setLibelleCaisseAF(libelleCaisseAF);
        entetePrestation.setCodeCaisseAF(codeCaisseAF);
        entetePrestation.setDate(date);
        if (adresse != null) {
            entetePrestation.setNpa(adresse.getNpa());
            entetePrestation.setLocalite(adresse.getLocalite());
            entetePrestation.setAdresseFormattee(adresse.getAdresseFormatte());
        }
        entetePrestation.setTitre(titre);
        entetePrestation.setRaisonSociale(raisonSociale);
        if (!JadeStringUtil.isEmpty(langue)) {
            entetePrestation.setLangue(CodeLangue.fromValue(langue));
        }
        return entetePrestation;
    }

    public String getIdTiersBeneficiaire() {
        return idTiersBeneficiaire;
    }

    public void setIdTiersBeneficiaire(String nssTiersBeneficiaire) {
        idTiersBeneficiaire = nssTiersBeneficiaire;
    }

    public String getLocaliteAffilie() {
        return localiteAffilie;
    }

    public void setLocaliteAffilie(String localiteAffilie) {
        this.localiteAffilie = localiteAffilie;
    }

    public String getIdAssurance() {
        return idAssurance;
    }

    public void setIdAssurance(String idAssurance) {
        this.idAssurance = idAssurance;
    }
}
