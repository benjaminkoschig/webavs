package globaz.corvus.vb.annonces;

import globaz.corvus.db.annonces.REAnnonceInscriptionCI;

public class REAnnonceInscriptionsCIViewBean extends REAbstractAnnonceHeaderProxyViewBean {

    REAnnonceInscriptionCI annonce = null;

    public REAnnonceInscriptionsCIViewBean() {
        super(new REAnnonceInscriptionCI());

        annonce = (REAnnonceInscriptionCI) getAnnonce();
    }

    /**
     * @return
     */
    public String getAnneeCotisations() {
        return annonce.getAnneeCotisations();
    }

    /**
     * @return
     */
    public String getBrancheEconomique() {
        return annonce.getBrancheEconomique();
    }

    /**
     * @return
     * @see globaz.corvus.db.annonces.REAnnonceInscriptionCI#getCiAdditionnel()
     */
    public Boolean getCiAdditionnel() {
        return annonce.getCiAdditionnel();
    }

    /**
     * @return
     * @see globaz.corvus.db.annonces.REAnnonceHeader#getCodeApplication()
     */
    @Override
    public String getCodeApplication() {
        return annonce.getCodeApplication();
    }

    /**
     * @return
     * @see globaz.corvus.db.annonces.REAnnonceInscriptionCI#getCodeAmortissement()
     */
    public String getCodeDS() {
        return annonce.getCodeADS();
    }

    /**
     * @return
     * @see globaz.corvus.db.annonces.REAnnonceHeader#getCodeEnregistrement01()
     */
    @Override
    public String getCodeEnregistrement01() {
        return annonce.getCodeEnregistrement01();
    }

    /**
     * @return
     * @see globaz.corvus.db.annonces.REAnnonceInscriptionCI#getCodeExtourne()
     */
    public String getCodeExtourne() {
        return annonce.getCodeExtourne();
    }

    /**
     * @return
     * @see globaz.corvus.db.annonces.REAnnonceInscriptionCI#getCodeParticulier()
     */
    public String getCodeParticulier() {
        return annonce.getCodeParticulier();
    }

    /**
     * @return
     * @see globaz.corvus.db.annonces.REAnnonceInscriptionCI#getCodeSpeciale()
     */
    public String getCodeSpeciale() {
        return annonce.getCodeSpecial();
    }

    /**
     * @return
     * @see globaz.corvus.db.annonces.REAnnonceInscriptionCI#getDateCloture()
     */
    public String getDateCloture() {
        return annonce.getDateCloture();
    }

    /**
     * @return
     * @see globaz.corvus.db.annonces.REAnnonceInscriptionCI#getDateOrdre()
     */
    public String getDateOrdre() {
        return annonce.getDateOrdre();
    }

    /**
     * @return
     * @see globaz.corvus.db.annonces.REAnnonceHeader#getEtat()
     */
    @Override
    public String getEtat() {
        return annonce.getEtat();
    }

    /**
     * @return
     * @see globaz.corvus.db.annonces.REAnnonceInscriptionCI#getGenreCotisation()
     */
    public String getGenreCotisation() {
        return annonce.getGenreCotisation();
    }

    /**
     * @return
     * @see globaz.corvus.db.annonces.REAnnonceHeader#getIdAnnonce()
     */
    @Override
    public String getIdAnnonce() {
        return annonce.getIdAnnonce();
    }

    /**
     * @return
     * @see globaz.corvus.db.annonces.REAnnonceInscriptionCI#getIdTiers()
     */
    public String getIdTiers() {
        return annonce.getIdTiers();
    }

    /**
     * @return
     * @see globaz.corvus.db.annonces.REAnnonceInscriptionCI#getIdTiersAyantDroit()
     */
    public String getIdTiersAyantDroit() {
        return annonce.getIdTiersAyantDroit();
    }

    /**
     * @return
     */
    public String getMoisDebutCotisations() {
        return annonce.getMoisDebutCotisations();
    }

    /**
     * @return
     */
    public String getMoisFinCotisations() {
        return annonce.getMoisFinCotisations();
    }

    /**
     * @return
     * @see globaz.corvus.db.annonces.REAnnonceInscriptionCI#getMotif()
     */
    public String getMotif() {
        return annonce.getMotif();
    }

    /**
     * @return
     * @see globaz.corvus.db.annonces.REAnnonceInscriptionCI#getNoAffilie()
     */
    public String getNoAffilie() {
        return annonce.getNoAffilie();
    }

    /**
     * @return
     * @see globaz.corvus.db.annonces.REAnnonceInscriptionCI#getNoAgenceTenantCI()
     */
    public String getNoAgenceTenantCI() {
        return annonce.getNoAgenceTenantCI();
    }

    /**
     * @return
     * @see globaz.corvus.db.annonces.REAnnonceInscriptionCI#getNoCaisseTenantCI()
     */
    public String getNoCaisseTenantCI() {
        return annonce.getNoCaisseTenantCI();
    }

    /**
     * @return
     */
    public String getNoPostalEmployeur() {
        return annonce.getNoPostalEmployeur();
    }

    /**
     * @return
     * @see globaz.corvus.db.annonces.REAnnonceHeader#getNumeroAgence()
     */
    @Override
    public String getNumeroAgence() {
        return annonce.getNumeroAgence();
    }

    /**
     * @return
     * @see globaz.corvus.db.annonces.REAnnonceHeader#getNumeroCaisse()
     */
    @Override
    public String getNumeroCaisse() {
        return annonce.getNumeroCaisse();
    }

    /**
     * @return
     * @see globaz.corvus.db.annonces.REAnnonceInscriptionCI#getPartBonifAssist()
     */
    public String getPartBonifAssist() {
        return annonce.getPartBonifAssist();
    }

    /**
     * @return
     */
    public String getPartieInformation() {
        return annonce.getPartieInformation();
    }

    /**
     * @return
     * @see globaz.corvus.db.annonces.REAnnonceInscriptionCI#getProvenance()
     */
    public String getProvenance() {
        return annonce.getProvenance();
    }

    /**
     * @return
     * @see globaz.corvus.db.annonces.REAnnonceInscriptionCI#getRefInterneCaisse()
     */
    public String getRefInterneCaisse() {
        return annonce.getRefInterneCaisse();
    }

    /**
     * @return
     * @see globaz.corvus.db.annonces.REAnnonceInscriptionCI#getRevenu()
     */
    public String getRevenu() {
        return annonce.getRevenu();
    }

    /**
     * @return
     * @see globaz.corvus.db.annonces.REAnnonceInscriptionCI#hasSpy()
     */
    @Override
    public boolean hasSpy() {
        return annonce.hasSpy();
    }

    /**
     * @param string
     */
    public void setAnneeCotisations(String string) {
        annonce.setAnneeCotisations(string);
    }

    /**
     * @param string
     */
    public void setBrancheEconomique(String string) {
        annonce.setBrancheEconomique(string);
    }

    /**
     * @param ciAdditionnel
     * @see globaz.corvus.db.annonces.REAnnonceInscriptionCI#setCiAdditionnel(java.lang.Boolean)
     */
    public void setCiAdditionnel(Boolean ciAdditionnel) {
        annonce.setCiAdditionnel(ciAdditionnel);
    }

    /**
     * @param codeAmortissement
     * @see globaz.corvus.db.annonces.REAnnonceInscriptionCI#setCodeAmortissement(java.lang.String)
     */
    public void setCodeADS(String codeADS) {
        annonce.setCodeADS(codeADS);
    }

    /**
     * @param codeApplication
     * @see globaz.corvus.db.annonces.REAnnonceHeader#setCodeApplication(java.lang.String)
     */
    @Override
    public void setCodeApplication(String codeApplication) {
        annonce.setCodeApplication(codeApplication);
    }

    /**
     * @param codeEnregistrement01
     * @see globaz.corvus.db.annonces.REAnnonceHeader#setCodeEnregistrement01(java.lang.String)
     */
    @Override
    public void setCodeEnregistrement01(String codeEnregistrement01) {
        annonce.setCodeEnregistrement01(codeEnregistrement01);
    }

    /**
     * @param codeExtourne
     * @see globaz.corvus.db.annonces.REAnnonceInscriptionCI#setCodeExtourne(java.lang.String)
     */
    public void setCodeExtourne(String codeExtourne) {
        annonce.setCodeExtourne(codeExtourne);
    }

    /**
     * @param codeParticulier
     * @see globaz.corvus.db.annonces.REAnnonceInscriptionCI#setCodeParticulier(java.lang.String)
     */
    public void setCodeParticulier(String codeParticulier) {
        annonce.setCodeParticulier(codeParticulier);
    }

    /**
     * @param codeSpeciale
     * @see globaz.corvus.db.annonces.REAnnonceInscriptionCI#setCodeSpeciale(java.lang.String)
     */
    public void setCodeSpeciale(String codeSpeciale) {
        annonce.setCodeSpeciale(codeSpeciale);
    }

    /**
     * @param dateCloture
     * @see globaz.corvus.db.annonces.REAnnonceInscriptionCI#setDateCloture(java.lang.String)
     */
    public void setDateCloture(String dateCloture) {
        annonce.setDateCloture(dateCloture);
    }

    /**
     * @param dateOrdre
     * @see globaz.corvus.db.annonces.REAnnonceInscriptionCI#setDateOrdre(java.lang.String)
     */
    public void setDateOrdre(String dateOrdre) {
        annonce.setDateOrdre(dateOrdre);
    }

    /**
     * @param etat
     * @see globaz.corvus.db.annonces.REAnnonceHeader#setEtat(java.lang.String)
     */
    @Override
    public void setEtat(String etat) {
        annonce.setEtat(etat);
    }

    /**
     * @param genreCotisation
     * @see globaz.corvus.db.annonces.REAnnonceInscriptionCI#setGenreCotisation(java.lang.String)
     */
    public void setGenreCotisation(String genreCotisation) {
        annonce.setGenreCotisation(genreCotisation);
    }

    /**
     * @param idAnnonce
     * @see globaz.corvus.db.annonces.REAnnonceHeader#setIdAnnonce(java.lang.String)
     */
    @Override
    public void setIdAnnonce(String idAnnonce) {
        annonce.setIdAnnonce(idAnnonce);
    }

    /**
     * @param idTiers
     * @see globaz.corvus.db.annonces.REAnnonceInscriptionCI#setIdTiers(java.lang.String)
     */
    public void setIdTiers(String idTiers) {
        annonce.setIdTiers(idTiers);
    }

    /**
     * @param idTiersAyantDroit
     * @see globaz.corvus.db.annonces.REAnnonceInscriptionCI#setIdTiersAyantDroit(java.lang.String)
     */
    public void setIdTiersAyantDroit(String idTiersAyantDroit) {
        annonce.setIdTiersAyantDroit(idTiersAyantDroit);
    }

    /**
     * @param string
     */
    public void setMoisDebutCotisations(String string) {
        annonce.setMoisDebutCotisations(string);
    }

    /**
     * @param string
     */
    public void setMoisFinCotisations(String string) {
        annonce.setMoisFinCotisations(string);
    }

    /**
     * @param motif
     * @see globaz.corvus.db.annonces.REAnnonceInscriptionCI#setMotif(java.lang.String)
     */
    public void setMotif(String motif) {
        annonce.setMotif(motif);
    }

    /**
     * @param noAffilie
     * @see globaz.corvus.db.annonces.REAnnonceInscriptionCI#setNoAffilie(java.lang.String)
     */
    public void setNoAffilie(String noAffilie) {
        annonce.setNoAffilie(noAffilie);
    }

    /**
     * @param noAgenceTenantCI
     * @see globaz.corvus.db.annonces.REAnnonceInscriptionCI#setNoAgenceTenantCI(java.lang.String)
     */
    public void setNoAgenceTenantCI(String noAgenceTenantCI) {
        annonce.setNoAgenceTenantCI(noAgenceTenantCI);
    }

    /**
     * @param noCaisseTenantCI
     * @see globaz.corvus.db.annonces.REAnnonceInscriptionCI#setNoCaisseTenantCI(java.lang.String)
     */
    public void setNoCaisseTenantCI(String noCaisseTenantCI) {
        annonce.setNoCaisseTenantCI(noCaisseTenantCI);
    }

    /**
     * @param string
     */
    public void setNoPostalEmployeur(String string) {
        annonce.setNoPostalEmployeur(string);
    }

    /**
     * @param numeroAgence
     * @see globaz.corvus.db.annonces.REAnnonceHeader#setNumeroAgence(java.lang.String)
     */
    @Override
    public void setNumeroAgence(String numeroAgence) {
        annonce.setNumeroAgence(numeroAgence);
    }

    /**
     * @param numeroCaisse
     * @see globaz.corvus.db.annonces.REAnnonceHeader#setNumeroCaisse(java.lang.String)
     */
    @Override
    public void setNumeroCaisse(String numeroCaisse) {
        annonce.setNumeroCaisse(numeroCaisse);
    }

    /**
     * @param partBonifAssist
     * @see globaz.corvus.db.annonces.REAnnonceInscriptionCI#setPartBonifAssist(java.lang.String)
     */
    public void setPartBonifAssist(String partBonifAssist) {
        annonce.setPartBonifAssist(partBonifAssist);
    }

    /**
     * @param string
     */
    public void setPartieInformation(String string) {
        annonce.setPartieInformation(string);
    }

    /**
     * @param provenance
     * @see globaz.corvus.db.annonces.REAnnonceInscriptionCI#setProvenance(java.lang.String)
     */
    public void setProvenance(String provenance) {
        annonce.setProvenance(provenance);
    }

    /**
     * @param refInterneCaisse
     * @see globaz.corvus.db.annonces.REAnnonceInscriptionCI#setRefInterneCaisse(java.lang.String)
     */
    public void setRefInterneCaisse(String refInterneCaisse) {
        annonce.setRefInterneCaisse(refInterneCaisse);
    }

    /**
     * @param revenu
     * @see globaz.corvus.db.annonces.REAnnonceInscriptionCI#setRevenu(java.lang.String)
     */
    public void setRevenu(String revenu) {
        annonce.setRevenu(revenu);
    }

}
