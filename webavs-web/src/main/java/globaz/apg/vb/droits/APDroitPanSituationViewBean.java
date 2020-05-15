package globaz.apg.vb.droits;

import globaz.apg.db.droits.APDroitPanSituation;
import globaz.apg.enums.APModeEditionDroit;
import globaz.apg.util.TypePrestation;
import globaz.commons.nss.NSUtil;
import globaz.jade.client.util.JadeCodesSystemsUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.fx.PRGestionnaireHelper;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.vb.PRAbstractViewBeanSupport;

import java.util.Vector;

public class APDroitPanSituationViewBean extends PRAbstractViewBeanSupport {

    private APDroitPanSituation droitPanSituation;
    private APDroitPanDTO droitPanDTO;
    private APModeEditionDroit modeEditionDroit;
    private transient Vector responsables = null;
    private String email;

    public APDroitPanSituationViewBean() {
        droitPanSituation = new APDroitPanSituation();
        droitPanDTO = new APDroitPanDTO();
        modeEditionDroit = APModeEditionDroit.LECTURE;
    }

    public void setDroitPanDTO(APDroitPanDTO dto) {
        droitPanDTO = dto;
    }

    public APDroitPanDTO getDroitPanDTO() {
        return droitPanDTO;
    }

    public void setDroitPanSituation(APDroitPanSituation droitPanSituation) {
        this.droitPanSituation = droitPanSituation;
    }

    public APDroitPanSituation getDroitPanSituation() {
        return droitPanSituation;
    }

    public void setActiviteSalarie(boolean activiteSalarie) {
        getDroitPanSituation().setActiviteSalarie(activiteSalarie);
    }

    public boolean isActiviteSalarie() {
        return getDroitPanSituation().isActiviteSalarie();
    }

    public void setPaiementEmployeur(boolean paiementEmployeur) {
        getDroitPanSituation().setPaiementEmployeur(paiementEmployeur);
    }

    public boolean isPaiementEmployeur() {
        return getDroitPanSituation().isPaiementEmployeur();
    }

    public void setCopieDecompteEmployeur(boolean copieDecompteEmployeur) {
        getDroitPanSituation().setCopieDecompteEmployeur(copieDecompteEmployeur);
    }

    public boolean isCopieDecompteEmployeur() {
        return getDroitPanSituation().isCopieDecompteEmployeur();
    }

    public void setQuarantaineOrdonnee(boolean quarantaineOrdonnee) {
        getDroitPanSituation().setQuarantaineOrdonnee(quarantaineOrdonnee);
    }

    public boolean isQuarantaineOrdonnee() {
        return getDroitPanSituation().isQuarantaineOrdonnee();
    }

    public void setCategorieEntreprise(String categorieEntreprise) {
        getDroitPanSituation().setCategorieEntreprise(categorieEntreprise);
    }

    public String getCategorieEntreprise() {
        return getDroitPanSituation().getCategorieEntreprise();
    }

    public void setMotifGarde(String motifGarde) {
        getDroitPanSituation().setMotifGarde(motifGarde);
    }

    public String getMotifGarde() {
        return getDroitPanSituation().getMotifGarde();
    }

    public void setMotifGardeHandicap(String motifGardeHandicap) {
        getDroitPanSituation().setMotifGardeHandicap(motifGardeHandicap);
    }

    public String getMotifGardeHandicap() {
        return getDroitPanSituation().getMotifGardeHandicap();
    }

    public void setQuarantaineOrdonneePar(String quarantaineOrdonneePar) {
        getDroitPanSituation().setQuarantaineOrdonneePar(quarantaineOrdonneePar);
    }

    public String getQuarantaineOrdonneePar() {
        return getDroitPanSituation().getQuarantaineOrdonneePar();
    }

    public String getCategorieEntrepriseLibelle() {
        return getDroitPanSituation().getCategorieEntrepriseLibelle();
    }

    public void setCategorieEntrepriseLibelle(String categorieEntrepriseLibelle) {
        getDroitPanSituation().setCategorieEntrepriseLibelle(categorieEntrepriseLibelle);
    }

    public String getDateDebutFermeture() {
        return getDroitPanSituation().getDateFermetureEtablissementDebut();
    }

    public void setDateDebutFermeture(String dateDebutFermeture) {
        getDroitPanSituation().setDateFermetureEtablissementDebut(dateDebutFermeture);
    }

    public String getDateFinFermeture() {
        return getDroitPanSituation().getDateFermetureEtablissementFin();
    }

    public void setDateFinFermeture(String dateFinFermeture) {
        getDroitPanSituation().setDateFermetureEtablissementFin(dateFinFermeture);
    }

    public String getDateDebutManifAnnulee() {
        return getDroitPanSituation().getDateDebutManifestationAnnulee();
    }

    public void setDateDebutManifAnnulee(String dateDebutManifAnnulee) {
        getDroitPanSituation().setDateDebutManifestationAnnulee(dateDebutManifAnnulee);
    }

    public String getDateFinManifAnnulee() {
        return getDroitPanSituation().getDateFinManifestationAnnulee();
    }

    public void setDateFinManifAnnulee(String dateFinManifAnnulee) {
        getDroitPanSituation().setDateFinManifestationAnnulee(dateFinManifAnnulee);
    }

    public String getDateDebutPerteGains() {
        return getDroitPanSituation().getDateDebutPerteGains();
    }

    public void setDateDebutPerteGains(String dateDebutPerteGains) {
        getDroitPanSituation().setDateDebutPerteGains(dateDebutPerteGains);
    }

    public String getDateFinPerteGains() {
        return getDroitPanSituation().getDateFinPerteGains();
    }

    public void setDateFinPerteGains(String dateFinPerteGains) {
        getDroitPanSituation().setDateFinPerteGains(dateFinPerteGains);
    }

    public void setRemarqueRefus(String remarqueRefus) {
        getDroitPanSituation().setRemarque(remarqueRefus);
    }

    public String getRemarqueRefus() {
        return getDroitPanSituation().getRemarque();
    }

    public void setNoControlePers(String noControlePers) {
        droitPanDTO.setNoControlePers(noControlePers);
    }

    public String getNoControlePers() {
        return droitPanDTO.getNoControlePers();
    }

    public void setNoCompte(String noCompte) {
        droitPanDTO.setNoCompte(noCompte);
    }

    public String getNoCompte() {
        return droitPanDTO.getNoCompte();
    }


    /**
     * Méthode qui retourne le NNSS formaté sans le préfixe (756.) ou alors le NSS normal
     *
     * @return NNSS formaté sans préfixe ou NSS normal
     */
    public String getNumeroAvsFormateSansPrefixe() {
        return NSUtil.formatWithoutPrefixe(getNss(), isNNSS().equals("true") ? true : false);
    }

    /**
     * Méthode qui retourne une string avec true si le NSS dans le vb est un NNSS, sinon false
     *
     * @return String (true ou false)
     */
    public final String isNNSS() {
        if (JadeStringUtil.isBlankOrZero(getNss())) {
            return "";
        }
        if (getNss().length() > 14) {
            return "true";
        } else {
            return "false";
        }
    }

    public String getNss() {
        return droitPanDTO.getNoAVS();
    }

    public void setNss(String nss) {
        droitPanDTO.setNoAVS(nss);
    }

    public void setIdTiers(String idTiers) {
        droitPanDTO.setIdTiers(idTiers);
    }

    public String getIdTiers() {
        return droitPanDTO.getIdTiers();
    }

    public void setIdDroit(String idDroit) {
        droitPanDTO.setIdDroit(idDroit);
    }

    public String getIdDroit() {
        return droitPanDTO.getIdDroit();
    }

    public boolean isModifiable() {
        return droitPanDTO.isModifiable();
    }

    public void setEtat(String etat) {
        droitPanDTO.setEtat(etat);
    }

    public void setDateDebutDroit(String dateDebutDroit) {
        droitPanDTO.setDateDebutDroit(dateDebutDroit);
    }

    public String getDateDebutDroit() {
        return droitPanDTO.getDateDebutDroit();
    }

    public APModeEditionDroit getModeEditionDroit() {
        return modeEditionDroit;
    }

    public final void setModeEditionDroit(APModeEditionDroit modeEditionDroit) {
        if (JadeStringUtil.isEmpty(droitPanDTO.getEtat()) || ((modeEditionDroit != null) && isModifiable())) {
            this.modeEditionDroit = modeEditionDroit;
        } else {
            this.modeEditionDroit = APModeEditionDroit.LECTURE;
        }
    }

    public Vector getResponsableData() {
        if (responsables == null) {
            try {
                responsables = PRGestionnaireHelper.getIdsEtNomsGestionnaires(TypePrestation.TYPE_APG
                        .toGroupeGestionnaire());
            } catch (Exception e) {
                getSession().addError(
                        getSession().getLabel(APAbstractDroitProxyViewBean.ERREUR_GESTIONNAIRES_INTROUVABLE));
            }
        }
        // on veut une ligne vide
        responsables.insertElementAt(new String[]{"", ""}, 0);
        return responsables;
    }

    public String getGestionnaire() {
        return droitPanDTO.getIdGestionnaire();
    }

    public void setGestionnaire(String gestionnaire) {
        droitPanDTO.setIdGestionnaire(gestionnaire);
    }

    public String getNom() {
        return droitPanDTO.getNom();
    }

    public void setNom(String nom) {
        droitPanDTO.setNom(nom);
    }

    public String getPrenom() {
        return droitPanDTO.getPrenom();
    }

    public void setPrenom(String prenom) {
        droitPanDTO.setPrenom(prenom);
    }

    public String getNomPrenom() {
        return droitPanDTO.getNomPrenom();
    }

    public void setNomPrenom(String nomPrenom) {
        droitPanDTO.setNomPrenom(nomPrenom);
    }

    public String getCsSexe() {
        return droitPanDTO.getCsSexe();
    }

    public void setCsSexe(String csSexe) {
        droitPanDTO.setCsSexe(csSexe);
    }

    public String getCsEtatCivil() {
        return droitPanDTO.getCsEtatCivil();
    }

    public void setCsEtatCivil(String csEtatCivil) {
        droitPanDTO.setCsEtatCivil(csEtatCivil);
    }

    public String getDateNaissance() {
        return droitPanDTO.getDateNaissance();
    }

    public void setDateNaissance(String dateNaissance) {
        droitPanDTO.setDateNaissance(dateNaissance);
    }

    public String getNpa() {
        return droitPanDTO.getNpa();
    }

    public void setNpa(String npa) {
        droitPanDTO.setNpa(npa);
    }

    public Vector getTiPays() throws Exception {
        return PRTiersHelper.getPays(getSession());
    }

    public String getPays() {
        return droitPanDTO.getPays();
    }

    public void setPays(String pays) {
        droitPanDTO.setPays(pays);
    }

    @Override
    public boolean validate() {
        return true;
    }


    public String getIdDroitPanSituation() {
        return getDroitPanSituation().getIdApgPandemie();
    }

    public void setIdDroitPanSituation(String idDroitSituation) {
        getDroitPanSituation().setIdApgPandemie(idDroitSituation);
    }

    public String getCuGenreService() {
        return JadeCodesSystemsUtil.getCode(getSession(), getGenreService());
    }

    public String getGenreService() {
        return droitPanDTO.getGenreService();
    }

    public void setGenreService(String genreService) {
        droitPanDTO.setGenreService(genreService);
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    public final void setModeEditionDroit(String modeEditionDroit) {
        APModeEditionDroit mode = Enum.valueOf(APModeEditionDroit.class, modeEditionDroit);
        this.setModeEditionDroit(mode);
    }

    public void setModifiable(boolean modifiable){
        droitPanDTO.setModifiable(modifiable);
    }

}
