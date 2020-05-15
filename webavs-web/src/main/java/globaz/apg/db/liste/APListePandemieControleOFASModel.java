package globaz.apg.db.liste;

public class APListePandemieControleOFASModel {
    String nss = "";
    String nom = "";
    String prenom = "";
    String genreService = "";
    String de ="";
    String a = "";
    String nbreJours = "";
    String montantJournalier = "";
    String montantBrut = "";
    String montantNet = "";
    String deductionSource = "";
    String dateComptable = "";
    String datePaiement ="";
    String beneficiaire = "";
    String IBANBenef = "";
    String id = "";

    String adresseNomProf = "";
    String rueProf = "";
    String npaProf = "";
    String localiteProf ="";

    String adresseNomDom ="";
    String rueDom = "";
    String npaDom = "";
    String localiteDom = "";

    String idTiers = "";
    String idTiersAff = "";
    String idAff= "";
    String numAffilie ="";

    String isVerseEmployeur="";

    String numCaisse ="";
    String nomCaisse ="";

    public String getNumAffilie() {
        return numAffilie;
    }

    public void setNumAffilie(String numAffilie) {
        this.numAffilie = numAffilie;
    }
    public String getIdTiers() {
        return idTiers;
    }

    public String getIdAff() {
        return idAff;
    }

    public void setIdAff(String idAff) {
        this.idAff = idAff;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }
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

    public String getGenreService() {
        return genreService;
    }

    public void setGenreService(String genreService) {
        this.genreService = genreService;
    }

    public String getDe() {
        return de;
    }

    public void setDe(String de) {
        this.de = de;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getNbreJours() {
        return nbreJours;
    }

    public void setNbreJours(String nbreJours) {
        this.nbreJours = nbreJours;
    }

    public String getMontantJournalier() {
        return montantJournalier;
    }

    public void setMontantJournalier(String montantJournalier) {
        this.montantJournalier = montantJournalier;
    }

    public String getMontantBrut() {
        return montantBrut;
    }

    public void setMontantBrut(String montantBrut) {
        this.montantBrut = montantBrut;
    }

    public String getMontantNet() {
        return montantNet;
    }

    public void setMontantNet(String montantNet) {
        this.montantNet = montantNet;
    }

    public String getDeductionSource() {
        return deductionSource;
    }

    public void setDeductionSource(String deductionSource) {
        this.deductionSource = deductionSource;
    }

    public String getBeneficiaire() {
        return beneficiaire;
    }

    public void setBeneficiaire(String beneficiaire) {
        this.beneficiaire = beneficiaire;
    }

    public String getIBANBenef() {
        return IBANBenef;
    }

    public void setIBANBenef(String IBANBenef) {
        this.IBANBenef = IBANBenef;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdresseNomProf() {
        return adresseNomProf;
    }

    public void setAdresseNomProf(String adresseNomProf) {
        this.adresseNomProf = adresseNomProf;
    }

    public String getRueProf() {
        return rueProf;
    }

    public void setRueProf(String rueProf) {
        this.rueProf = rueProf;
    }

    public String getNpaProf() {
        return npaProf;
    }

    public void setNpaProf(String npaProf) {
        this.npaProf = npaProf;
    }

    public String getLocaliteProf() {
        return localiteProf;
    }

    public void setLocaliteProf(String localiteProf) {
        this.localiteProf = localiteProf;
    }

    public String getAdresseNomDom() {
        return adresseNomDom;
    }

    public void setAdresseNomDom(String adresseNomDom) {
        this.adresseNomDom = adresseNomDom;
    }

    public String getRueDom() {
        return rueDom;
    }

    public void setRueDom(String rueDom) {
        this.rueDom = rueDom;
    }

    public String getNpaDom() {
        return npaDom;
    }

    public void setNpaDom(String npaDom) {
        this.npaDom = npaDom;
    }

    public String getLocaliteDom() {
        return localiteDom;
    }

    public void setLocaliteDom(String localiteDom) {
        this.localiteDom = localiteDom;
    }
    public String getDateComptable() {
        return dateComptable;
    }

    public void setDateComptable(String dateComptable) {
        this.dateComptable = dateComptable;
    }

    public String getIdTiersAff() {
        return idTiersAff;
    }

    public void setIdTiersAff(String idTiersAff) {
        this.idTiersAff = idTiersAff;
    }

    public String getLineCSV() {
        if(isVerseEmployeur.equals("1")){
            return numAffilie+";"+idTiersAff+";"+nss+";"+dateComptable+";EMPLOYEUR";
        }else{
            return numAffilie+";"+idTiers+";"+nss+";"+dateComptable+";ASSURE";
        }
    }
    public String getIsVerseEmployeur() {
        return isVerseEmployeur;
    }

    public void setIsVerseEmployeur(String isVerseEmployeur) {
        this.isVerseEmployeur = isVerseEmployeur;
    }
    public String getNumCaisse() {
        return numCaisse;
    }

    public void setNumCaisse(String numCaisse) {
        this.numCaisse = numCaisse;
    }

    public String getNomCaisse() {
        return nomCaisse;
    }

    public void setNomCaisse(String nomCaisse) {
        this.nomCaisse = nomCaisse;
    }

    public String getDatePaiement() {
        return datePaiement;
    }

    public void setDatePaiement(String datePaiement) {
        this.datePaiement = datePaiement;
    }

}
