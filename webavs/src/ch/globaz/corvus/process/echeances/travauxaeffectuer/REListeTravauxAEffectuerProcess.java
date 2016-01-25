package ch.globaz.corvus.process.echeances.travauxaeffectuer;

import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.corvus.db.demandes.REDemandeRenteJointDemande;
import globaz.corvus.db.demandes.REDemandeRenteJointDemandeManager;
import globaz.corvus.db.demandes.REDemandeRenteVieillesse;
import globaz.corvus.db.rentesaccordees.REListerEcheanceRenteJoinMembresFamille;
import globaz.corvus.topaz.REEcheanceRenteOO;
import globaz.globall.db.BProcessLauncher;
import globaz.hera.api.ISFMembreFamille;
import globaz.hera.api.ISFRelationFamiliale;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.hera.external.SFSituationFamilialeFactory;
import globaz.prestation.acor.PRACORConst;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.corvus.process.echeances.REListeEcheanceProcess;

public class REListeTravauxAEffectuerProcess extends REListeEcheanceProcess<REListeTravauxAEffectuerDocumentGenerator> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forAnneeImpression = "";
    private String forMoisImpression = "";
    private Boolean isAjournement = Boolean.FALSE;
    private Boolean isAjournementDOC = Boolean.FALSE;
    private Boolean isAjournementGED = Boolean.FALSE;
    private Boolean isAutresEcheance = Boolean.FALSE;
    private Boolean isCertificatDeVie = Boolean.FALSE;
    private Boolean isEcheanceEtude = Boolean.FALSE;
    private Boolean isEcheanceEtudeDOC = Boolean.FALSE;
    private Boolean isEcheanceEtudeGED = Boolean.FALSE;
    private Boolean isEnfantDe18ans = Boolean.FALSE;
    private Boolean isEnfantDe18ansDOC = Boolean.FALSE;
    private Boolean isEnfantDe18ansGED = Boolean.FALSE;
    private Boolean isEnfantDe25ans = Boolean.FALSE;
    private Boolean isEnfantDe25ansDOC = Boolean.FALSE;
    private Boolean isEnfantDe25ansGED = Boolean.FALSE;
    private Boolean isFemmeArrivantAgeVieillesse = Boolean.FALSE;
    private Boolean isFemmeArrivantAgeVieillesseDOC = Boolean.FALSE;
    private Boolean isFemmeArrivantAgeVieillesseGED = Boolean.FALSE;
    private Boolean isHommeArrivantAgeVieillesse = Boolean.FALSE;
    private Boolean isHommeArrivantAgeVieillesseDOC = Boolean.FALSE;
    private Boolean isHommeArrivantAgeVieillesseGED = Boolean.FALSE;
    private Boolean isRenteDeVeuf = Boolean.FALSE;
    private Boolean isRenteDeVeufDOC = Boolean.FALSE;
    private Boolean isRenteDeVeufGED = Boolean.FALSE;

    private REListeTravauxAEffectuerDocumentGenerator listGenerator;

    public REListeTravauxAEffectuerProcess() {
        super();
    }

    @Override
    protected void afterExecute() throws Exception {
        genererLettresOpenOffice(filterLesEcheancesAvantImpression(listGenerator.getEcheancesRetenues()));
        super.afterExecute();
    }

    private boolean asDemandeVieillesseAnticipe(String idTiersRequerant) throws Exception {

        // Cette méthode m'indique si un tiers a une demande de vieillesse de type calcul standard anticipée
        boolean asConjointDemandeVieillesseAnticipe = false;

        // Je recherche toutes les rentes en cours de type de calcul standard pour le conjoint
        REDemandeRenteJointDemandeManager demrenteMgr = new REDemandeRenteJointDemandeManager();
        demrenteMgr.setSession(getSession());
        demrenteMgr.setForIdTiersRequ(idTiersRequerant);
        demrenteMgr.setEnCours(Boolean.TRUE);
        demrenteMgr.find();

        for (int i = 0; i < demrenteMgr.size(); i++) {
            REDemandeRenteJointDemande redemrente = (REDemandeRenteJointDemande) demrenteMgr.get(i);

            if (redemrente != null) {
                // test si la rente trouvé est de type vieillesse
                if (IREDemandeRente.CS_TYPE_DEMANDE_RENTE_VIEILLESSE.equals(redemrente.getCsTypeDemande())) {
                    REDemandeRenteVieillesse demv = new REDemandeRenteVieillesse();
                    demv.setSession(getSession());
                    demv.setIdDemandeRente(redemrente.getIdDemandeRente());
                    demv.retrieve();
                    // Si le calcul de la demande est de type standard, je test si la rente est anticipée
                    if (IREDemandeRente.CS_TYPE_CALCUL_STANDARD.equals(demv.getCsTypeCalcul())) {
                        asConjointDemandeVieillesseAnticipe = true;
                    }
                }
            }
        }
        return asConjointDemandeVieillesseAnticipe;
    }

    @Override
    protected REListeTravauxAEffectuerDocumentGenerator buildListGenerator() throws Exception {
        listGenerator = new REListeTravauxAEffectuerDocumentGenerator(getSession(), getMoisTraitement());
        listGenerator.setAjournement(isAjournement);
        listGenerator.setAutresEcheance(isAutresEcheance);
        listGenerator.setCertificatDeVie(isCertificatDeVie);
        listGenerator.setEcheanceEtude(isEcheanceEtude);
        listGenerator.setEnfantDe18ans(isEnfantDe18ans);
        listGenerator.setEnfantDe25ans(isEnfantDe25ans);
        listGenerator.setFemmeArrivantAgeVieillesse(isFemmeArrivantAgeVieillesse);
        listGenerator.setHommeArrivantAgeVieillesse(isHommeArrivantAgeVieillesse);
        listGenerator.setRenteDeVeuf(isRenteDeVeuf);
        return listGenerator;
    }

    /**
     * Filtrage des échéances à imprimer en fonction des case à cocher sélectionnées
     * 
     * @param echeances
     * @return
     */
    private List<REListerEcheanceRenteJoinMembresFamille> filterLesEcheancesAvantImpression(
            List<REListerEcheanceRenteJoinMembresFamille> echeances) throws Exception {
        List<REListerEcheanceRenteJoinMembresFamille> echeancesAImprimer = new ArrayList<REListerEcheanceRenteJoinMembresFamille>();
        for (REListerEcheanceRenteJoinMembresFamille echeance : echeances) {

            switch (echeance.getMotifEcheance()) {
                case Echeance18ans:
                    if (isEnfantDe18ansDOC) {
                        echeancesAImprimer.add(echeance);
                    }
                    break;
                case Echeance25ans:
                    if (isEnfantDe25ansDOC) {
                        echeancesAImprimer.add(echeance);
                    }
                    break;
                case EcheanceFinEtudes:
                case EnqueteIntermediaire:
                    if (isEcheanceEtudeDOC) {
                        echeancesAImprimer.add(echeance);
                    }
                    break;
                case ConjointArrivantAgeAvs:
                    if (isHommeArrivantAgeVieillesseDOC || isFemmeArrivantAgeVieillesseDOC) {
                        if (!asDemandeVieillesseAnticipe(findConjoint(echeance).getIdTiers())) {
                            echeancesAImprimer.add(echeance);
                        }
                    }
                    break;
                case HommeArrivantAgeAvs:
                case HommeArrivantAgeAvsRenteAnticipee:
                case HommeArrivantAgeAvsAvecApiAi:
                    if (isHommeArrivantAgeVieillesseDOC) {
                        if (!asDemandeVieillesseAnticipe(echeance.getIdTiers())) {
                            echeancesAImprimer.add(echeance);
                        }
                    }
                    break;
                case FemmeArrivantAgeAvs:
                case FemmeArrivantAgeAvsRenteAnticipee:
                case FemmeArrivantAgeAvsAvecApiAi:
                    if (isFemmeArrivantAgeVieillesseDOC) {
                        if (!asDemandeVieillesseAnticipe(echeance.getIdTiers())) {
                            echeancesAImprimer.add(echeance);
                        }
                    }
                    break;

                case Ajournement:
                    if (isAjournementDOC) {
                        echeancesAImprimer.add(echeance);
                    }
                    break;
                case RenteDeVeuf:
                case RenteDeVeufSansEnfant:
                    if (isRenteDeVeufDOC) {
                        echeancesAImprimer.add(echeance);
                    }
                    break;
                default:
                    break;
            }
        }
        return echeancesAImprimer;
    }

    private ISFMembreFamille findConjoint(REListerEcheanceRenteJoinMembresFamille entity) throws Exception {

        // Cette méthode me retourne le dernier conjoint d'un tiers
        ISFSituationFamiliale situationFamiliale = SFSituationFamilialeFactory.getSituationFamiliale(getSession(),
                ISFSituationFamiliale.CS_DOMAINE_RENTES, entity.getIdTiers());

        ISFRelationFamiliale[] relfam = situationFamiliale.getRelationsConjoints(entity.getIdTiers(), null);

        if ((relfam != null) && (relfam.length > 0)) {
            ISFRelationFamiliale rf = relfam[0];
            if (PRACORConst.CS_FEMME.equals(entity.getCsSexe())) {
                return situationFamiliale.getMembreFamille(rf.getIdMembreFamilleHomme());
            } else {
                return situationFamiliale.getMembreFamille(rf.getIdMembreFamilleFemme());
            }
        }
        return null;
    }

    /**
     * Lancement du processus d'impression si la liste n'est pas vide
     * 
     * @param echeances
     *            Les échéances à imprimer
     */
    private void genererLettresOpenOffice(List<REListerEcheanceRenteJoinMembresFamille> echeances) {
        if ((echeances != null) && (echeances.size() > 0)) {
            try {
                REEcheanceRenteOO reEcheanceRenteOO = new REEcheanceRenteOO();
                reEcheanceRenteOO.setEcheances(echeances);
                reEcheanceRenteOO.setMoisEcheance(getMoisTraitement());
                reEcheanceRenteOO.setEMailAddress(getEmailAddress());
                reEcheanceRenteOO.setSession(getSession());

                // Propriété de la GED
                if (getIsAjournementGED()) {
                    reEcheanceRenteOO.setAjournementGED(true);
                }
                if (getIsEnfantDe18ansGED()) {
                    reEcheanceRenteOO.setEnfantDe18ansGED(true);
                }
                if (getIsEnfantDe25ansGED()) {
                    reEcheanceRenteOO.setEnfantDe25ansGED(true);
                }
                if (getIsEcheanceEtudeGED()) {
                    reEcheanceRenteOO.setEcheanceEtudeGED(true);
                }
                if (getIsHommeArrivantAgeVieillesseGED()) {
                    reEcheanceRenteOO.setHommeArrivantAgeVieillesseGED(true);
                }
                if (getIsFemmeArrivantAgeVieillesseGED()) {
                    reEcheanceRenteOO.setFemmeArrivantAgeVieillesseGED(true);
                }
                if (getIsRenteDeVeufGED()) {
                    reEcheanceRenteOO.setRenteDeVeufGED(true);
                }
                BProcessLauncher.start(reEcheanceRenteOO, false);
            } catch (Exception e) {
                getLogSession().error(this.getClass().getName(), e.toString());
                abort();
            }
        }
    }

    public final String getForAnneeImpression() {
        return forAnneeImpression;
    }

    public final String getForMoisImpression() {
        return forMoisImpression;
    }

    public final Boolean getIsAjournement() {
        return isAjournement;
    }

    public final Boolean getIsAjournementDOC() {
        return isAjournementDOC;
    }

    public final Boolean getIsAjournementGED() {
        return isAjournementGED;
    }

    public final Boolean getIsAutresEcheance() {
        return isAutresEcheance;
    }

    public final Boolean getIsCertificatDeVie() {
        return isCertificatDeVie;
    }

    public final Boolean getIsEcheanceEtude() {
        return isEcheanceEtude;
    }

    public final Boolean getIsEcheanceEtudeDOC() {
        return isEcheanceEtudeDOC;
    }

    public final Boolean getIsEcheanceEtudeGED() {
        return isEcheanceEtudeGED;
    }

    public final Boolean getIsEnfantDe18ans() {
        return isEnfantDe18ans;
    }

    public final Boolean getIsEnfantDe18ansDOC() {
        return isEnfantDe18ansDOC;
    }

    public final Boolean getIsEnfantDe18ansGED() {
        return isEnfantDe18ansGED;
    }

    public final Boolean getIsEnfantDe25ans() {
        return isEnfantDe25ans;
    }

    public final Boolean getIsEnfantDe25ansDOC() {
        return isEnfantDe25ansDOC;
    }

    public final Boolean getIsEnfantDe25ansGED() {
        return isEnfantDe25ansGED;
    }

    public final Boolean getIsFemmeArrivantAgeVieillesse() {
        return isFemmeArrivantAgeVieillesse;
    }

    public final Boolean getIsFemmeArrivantAgeVieillesseDOC() {
        return isFemmeArrivantAgeVieillesseDOC;
    }

    public final Boolean getIsFemmeArrivantAgeVieillesseGED() {
        return isFemmeArrivantAgeVieillesseGED;
    }

    public final Boolean getIsHommeArrivantAgeVieillesse() {
        return isHommeArrivantAgeVieillesse;
    }

    public final Boolean getIsHommeArrivantAgeVieillesseDOC() {
        return isHommeArrivantAgeVieillesseDOC;
    }

    public final Boolean getIsHommeArrivantAgeVieillesseGED() {
        return isHommeArrivantAgeVieillesseGED;
    }

    public final Boolean getIsRenteDeVeuf() {
        return isRenteDeVeuf;
    }

    public final Boolean getIsRenteDeVeufDOC() {
        return isRenteDeVeufDOC;
    }

    public final Boolean getIsRenteDeVeufGED() {
        return isRenteDeVeufGED;
    }

    @Override
    public String getName() {
        return REListeTravauxAEffectuerProcess.class.getName();
    }

    @Override
    protected void preparerListGenerator(REListeTravauxAEffectuerDocumentGenerator listGenerator) throws Exception {
        // Nothing to do
    }

    public final void setForAnneeImpression(String forAnneeImpression) {
        this.forAnneeImpression = forAnneeImpression;
    }

    public final void setForMoisImpression(String forMoisImpression) {
        this.forMoisImpression = forMoisImpression;
    }

    public final void setIsAjournement(Boolean isAjournement) {
        this.isAjournement = isAjournement;
    }

    public final void setIsAjournementDOC(Boolean isAjournementDOC) {
        this.isAjournementDOC = isAjournementDOC;
    }

    public final void setIsAjournementGED(Boolean isAjournementGED) {
        this.isAjournementGED = isAjournementGED;
    }

    public final void setIsAutresEcheance(Boolean isAutresEcheance) {
        this.isAutresEcheance = isAutresEcheance;
    }

    public final void setIsCertificatDeVie(Boolean isCertificatDeVie) {
        this.isCertificatDeVie = isCertificatDeVie;
    }

    public final void setIsEcheanceEtude(Boolean isEcheanceEtude) {
        this.isEcheanceEtude = isEcheanceEtude;
    }

    public final void setIsEcheanceEtudeDOC(Boolean isEcheanceEtudeDOC) {
        this.isEcheanceEtudeDOC = isEcheanceEtudeDOC;
    }

    public final void setIsEcheanceEtudeGED(Boolean isEcheanceEtudeGED) {
        this.isEcheanceEtudeGED = isEcheanceEtudeGED;
    }

    public final void setIsEnfantDe18ans(Boolean isEnfantDe18ans) {
        this.isEnfantDe18ans = isEnfantDe18ans;
    }

    public final void setIsEnfantDe18ansDOC(Boolean isEnfantDe18ansDOC) {
        this.isEnfantDe18ansDOC = isEnfantDe18ansDOC;
    }

    public final void setIsEnfantDe18ansGED(Boolean isEnfantDe18ansGED) {
        this.isEnfantDe18ansGED = isEnfantDe18ansGED;
    }

    public final void setIsEnfantDe25ans(Boolean isEnfantDe25ans) {
        this.isEnfantDe25ans = isEnfantDe25ans;
    }

    public final void setIsEnfantDe25ansDOC(Boolean isEnfantDe25ansDOC) {
        this.isEnfantDe25ansDOC = isEnfantDe25ansDOC;
    }

    public final void setIsEnfantDe25ansGED(Boolean isEnfantDe25ansGED) {
        this.isEnfantDe25ansGED = isEnfantDe25ansGED;
    }

    public final void setIsFemmeArrivantAgeVieillesse(Boolean isFemmeArrivantAgeVieillesse) {
        this.isFemmeArrivantAgeVieillesse = isFemmeArrivantAgeVieillesse;
    }

    public final void setIsFemmeArrivantAgeVieillesseDOC(Boolean isFemmeArrivantAgeVieillesseDOC) {
        this.isFemmeArrivantAgeVieillesseDOC = isFemmeArrivantAgeVieillesseDOC;
    }

    public final void setIsFemmeArrivantAgeVieillesseGED(Boolean isFemmeArrivantAgeVieillesseGED) {
        this.isFemmeArrivantAgeVieillesseGED = isFemmeArrivantAgeVieillesseGED;
    }

    public final void setIsHommeArrivantAgeVieillesse(Boolean isHommeArrivantAgeVieillesse) {
        this.isHommeArrivantAgeVieillesse = isHommeArrivantAgeVieillesse;
    }

    public final void setIsHommeArrivantAgeVieillesseDOC(Boolean isHommeArrivantAgeVieillesseDOC) {
        this.isHommeArrivantAgeVieillesseDOC = isHommeArrivantAgeVieillesseDOC;
    }

    public final void setIsHommeArrivantAgeVieillesseGED(Boolean isHommeArrivantAgeVieillesseGED) {
        this.isHommeArrivantAgeVieillesseGED = isHommeArrivantAgeVieillesseGED;
    }

    public final void setIsRenteDeVeuf(Boolean isRenteDeVeuf) {
        this.isRenteDeVeuf = isRenteDeVeuf;
    }

    public final void setIsRenteDeVeufDOC(Boolean isRenteDeVeufDOC) {
        this.isRenteDeVeufDOC = isRenteDeVeufDOC;
    }

    public final void setIsRenteDeVeufGED(Boolean isRenteDeVeufGED) {
        this.isRenteDeVeufGED = isRenteDeVeufGED;
    }
}
