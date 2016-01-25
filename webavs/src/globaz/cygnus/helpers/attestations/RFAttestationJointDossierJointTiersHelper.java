package globaz.cygnus.helpers.attestations;

import globaz.cygnus.api.attestations.IRFAttestations;
import globaz.cygnus.db.attestations.RFAttestationAVS;
import globaz.cygnus.db.attestations.RFFraisLivraison;
import globaz.cygnus.db.attestations.RFMaintienDomicile;
import globaz.cygnus.db.attestations.RFMoyensAuxiliairesBon;
import globaz.cygnus.db.attestations.RFMoyensAuxiliairesCertificat;
import globaz.cygnus.db.attestations.RFMoyensAuxiliairesDecision;
import globaz.cygnus.db.attestations.RFRegimeAlimentaire;
import globaz.cygnus.utils.RFUtils;
import globaz.cygnus.vb.attestations.RFAttestationPiedDePageViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.prestation.helpers.PRAbstractHelper;

/**
 * author fha
 */
public class RFAttestationJointDossierJointTiersHelper extends PRAbstractHelper {

    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        // on retrieve la table qui nous intéresse en fonction du type
        // d'attestation et de l'idAttestation
        try {

            RFAttestationPiedDePageViewBean outputViewBean = (RFAttestationPiedDePageViewBean) viewBean;

            // Selon le type d'attestation indiqué par le viewBean
            if (IRFAttestations.REGIME_ALIMENTAIRE.equals(outputViewBean.getCsTypeAttestation())) {
                RFRegimeAlimentaire rfRegimeAlimentaire = new RFRegimeAlimentaire();
                rfRegimeAlimentaire.setSession((BSession) outputViewBean.getISession());
                rfRegimeAlimentaire.setIdAttestationRegimeAlimentaire(outputViewBean.getIdAttestation());

                rfRegimeAlimentaire.retrieve();
                if (!rfRegimeAlimentaire.isNew()) {
                    // on set notre viewBean
                    outputViewBean.setDateDecision(rfRegimeAlimentaire.getDateDecision());
                    outputViewBean.setDateDecisionRefus(rfRegimeAlimentaire.getDateDecisionRefus());
                    outputViewBean.setDateReception(rfRegimeAlimentaire.getDateReception());
                    outputViewBean.setDateEnvoiEvaluationCMS(rfRegimeAlimentaire.getDateEnvoiEvaluationCMS());
                    outputViewBean.setDateEnvoiInfosMedecin(rfRegimeAlimentaire.getDateEnvoiInfosMedecin());
                    outputViewBean.setDateRetourEvaluationCMS(rfRegimeAlimentaire.getDateRetourEvaluationCMS());
                    outputViewBean.setDateRetourInfosMedecin(rfRegimeAlimentaire.getDateRetourInfosMedecin());
                    outputViewBean.setTypeRegime(rfRegimeAlimentaire.getTypeRegime());
                    outputViewBean.setMontantMensuelAccepte(rfRegimeAlimentaire.getMontantMensuelAccepte());
                    outputViewBean.setEcheanceRevision(rfRegimeAlimentaire.getEcheanceRevision());
                    outputViewBean.setCommentaire(rfRegimeAlimentaire.getCommentaire());
                    outputViewBean.setIsRegimeAccepte(rfRegimeAlimentaire.getIsRegimeAccepte());
                    outputViewBean.setAutresFraisPourRegime(rfRegimeAlimentaire.getAutre());
                    outputViewBean.setDateAideDureeDetermineeDesLe(rfRegimeAlimentaire
                            .getDateAideDureeDetermineeDesLe());
                    outputViewBean.setDateAideDureeDetermineeJusqua(rfRegimeAlimentaire
                            .getDateAideDureeDetermineeJusqua());
                    outputViewBean.setDateAideDureeIndetermineeDesLe(rfRegimeAlimentaire
                            .getDateAideDureeIndetermineeDesLe());
                    outputViewBean.setDateAideDureeIndetermineeReevaluation(rfRegimeAlimentaire
                            .getDateAideDureeIndetermineeReevaluation());
                } else {
                    RFUtils.setMsgErreurInattendueViewBean(rfRegimeAlimentaire, "_retrieve()",
                            "RFAttestationJointDossierJointTiersHelper");
                }
            } else if (IRFAttestations.BON_MOYENS_AUXILIAIRES.equals(outputViewBean.getCsTypeAttestation())) {
                RFMoyensAuxiliairesBon rfMoyensAuxiliairesBon = new RFMoyensAuxiliairesBon();
                rfMoyensAuxiliairesBon.setSession((BSession) outputViewBean.getISession());
                rfMoyensAuxiliairesBon.setIdAttestationMoyenAuxBon(outputViewBean.getIdAttestation());

                rfMoyensAuxiliairesBon.retrieve();

                if (!rfMoyensAuxiliairesBon.isNew()) {
                    // on set notre viewBean
                    outputViewBean.setMoyenAuxiliaire(rfMoyensAuxiliairesBon.getMoyenAuxiliaire());
                    outputViewBean.setDateMoyAuxB(rfMoyensAuxiliairesBon.getDate());
                    outputViewBean.setDateEntretienTel(rfMoyensAuxiliairesBon.getDateEntretienTel());
                    outputViewBean.setIdTiers(rfMoyensAuxiliairesBon.getIdTiers());
                } else {
                    RFUtils.setMsgErreurInattendueViewBean(rfMoyensAuxiliairesBon, "_retrieve()",
                            "RFAttestationJointDossierJointTiersHelper");
                }
            } else if (IRFAttestations.CERTIFICAT_MOYENS_AUXILIAIRES.equals(outputViewBean.getCsTypeAttestation())) {
                RFMoyensAuxiliairesCertificat rfMoyensAuxiliairesCertificat = new RFMoyensAuxiliairesCertificat();
                rfMoyensAuxiliairesCertificat.setSession((BSession) outputViewBean.getISession());
                rfMoyensAuxiliairesCertificat.setIdAttestationMoyenAuxCertificat(outputViewBean.getIdAttestation());

                rfMoyensAuxiliairesCertificat.retrieve();

                if (!rfMoyensAuxiliairesCertificat.isNew()) {
                    // on set notre viewBean
                    outputViewBean.setDateCertificat(rfMoyensAuxiliairesCertificat.getDateCertificat());
                } else {
                    RFUtils.setMsgErreurInattendueViewBean(rfMoyensAuxiliairesCertificat, "_retrieve()",
                            "RFAttestationJointDossierJointTiersHelper");
                }
            } else if (IRFAttestations.DECISION_MOYENS_AUXILIAIRES.equals(outputViewBean.getCsTypeAttestation())) {
                RFMoyensAuxiliairesDecision rfMoyensAuxiliairesDecision = new RFMoyensAuxiliairesDecision();
                rfMoyensAuxiliairesDecision.setSession((BSession) outputViewBean.getISession());
                rfMoyensAuxiliairesDecision.setIdAttestationMoyenAuxDecision(outputViewBean.getIdAttestation());

                rfMoyensAuxiliairesDecision.retrieve();

                if (!rfMoyensAuxiliairesDecision.isNew()) {
                    // on set notre viewBean
                    outputViewBean.setLibelleMoyensAuxDecision(rfMoyensAuxiliairesDecision
                            .getLibelleMoyenAuxiliaireDecision());
                    outputViewBean.setDateDecisionOAI(rfMoyensAuxiliairesDecision.getDateDecision());
                } else {
                    RFUtils.setMsgErreurInattendueViewBean(rfMoyensAuxiliairesDecision, "_retrieve()",
                            "RFAttestationJointDossierJointTiersHelper");
                }

            } else if (IRFAttestations.FRAIS_LIVRAISON.equals(outputViewBean.getCsTypeAttestation())) {
                RFFraisLivraison rfFraisLivraison = new RFFraisLivraison();
                rfFraisLivraison.setSession((BSession) outputViewBean.getISession());
                rfFraisLivraison.setIdAttestationFraisLivraison(outputViewBean.getIdAttestation());

                rfFraisLivraison.retrieve();

                if (!rfFraisLivraison.isNew()) {
                    // on set notre viewBean
                    outputViewBean.setCentreLocation(rfFraisLivraison.getCentreLocation());
                    outputViewBean.setDateFraisLiv(rfFraisLivraison.getDate());
                    outputViewBean.setOfficePC(rfFraisLivraison.getOfficePC());
                    outputViewBean.setRemarque(rfFraisLivraison.getRemarque());
                    outputViewBean.setAttestationLivraison(rfFraisLivraison.getAttestationLivraison());
                } else {
                    RFUtils.setMsgErreurInattendueViewBean(rfFraisLivraison, "_retrieve()",
                            "RFAttestationJointDossierJointTiersHelper");
                }

            } else if (IRFAttestations.MAINTIEN_DOMICILE.equals(outputViewBean.getCsTypeAttestation())) {
                RFMaintienDomicile rfMaintienDomicile = new RFMaintienDomicile();
                rfMaintienDomicile.setSession((BSession) outputViewBean.getISession());
                rfMaintienDomicile.setIdAttestationMaintienDomicile(outputViewBean.getIdAttestation());

                rfMaintienDomicile.retrieve();

                if (!rfMaintienDomicile.isNew()) {
                    // on set notre viewBean
                    outputViewBean.setaTroubleAge(rfMaintienDomicile.getaTroubleAge());
                    outputViewBean.setaMaladie(rfMaintienDomicile.getaMaladie());
                    outputViewBean.setaAccident(rfMaintienDomicile.getaAccident());
                    outputViewBean.setaInvalidite(rfMaintienDomicile.getaInvalidite());
                    outputViewBean.setDescriptionMotif(rfMaintienDomicile.getDescriptionMotif());
                    outputViewBean.setAutrePersonneATroubleAge(rfMaintienDomicile.getAutrePersonneATroubleAge());
                    outputViewBean.setAutrePersonneAMaladie(rfMaintienDomicile.getAutrePersonneAMaladie());
                    outputViewBean.setAutrePersonneAAccident(rfMaintienDomicile.getAutrePersonneAAccident());
                    outputViewBean.setAutrePersonneAInvalidite(rfMaintienDomicile.getAutrePersonneAInvalidite());
                    outputViewBean.setAutrePersonneDescriptionMotif(rfMaintienDomicile
                            .getAutrePersonneDescriptionMotif());
                    outputViewBean.setNombreTotalPiece(rfMaintienDomicile.getNombreTotalPiece());
                    outputViewBean.setNombrePieceUtilise(rfMaintienDomicile.getNombrePieceUtilise());
                    outputViewBean.setNombrePersonneLogement(rfMaintienDomicile.getNombrePersonneLogement());

                    outputViewBean.setVeillesPresence(rfMaintienDomicile.getVeillesPresence());
                    outputViewBean.setNettoyageRangement(rfMaintienDomicile.getNettoyageRangement());
                    outputViewBean.setVaisselle(rfMaintienDomicile.getVaisselle());
                    outputViewBean.setLits(rfMaintienDomicile.getLits());
                    outputViewBean.setLessive(rfMaintienDomicile.getLessive());
                    outputViewBean.setRepassage(rfMaintienDomicile.getRepassage());
                    outputViewBean.setDureeAideRemunere(rfMaintienDomicile.getDureeAideRemunere());
                    outputViewBean
                            .setDureeAideRemunereTenueMenage(rfMaintienDomicile.getDureeAideRemunereTenueMenage());
                    outputViewBean.setRepasDomicileCMS(rfMaintienDomicile.getRepasDomicileCMS());
                    outputViewBean.setRecoitRepasCMS(rfMaintienDomicile.getRecoitRepasCMS());
                    outputViewBean.setRaisonPasRepas(rfMaintienDomicile.getRaisonPasRepas());
                    outputViewBean.setAideRemunereNecessaire(rfMaintienDomicile.getAideRemunereNecessaire());
                    outputViewBean.setHeuresMoisRemunere(rfMaintienDomicile.getHeuresMoisRemunere());
                    outputViewBean.setAideDureeDeterminee(rfMaintienDomicile.getAideDureeDeterminee());
                    outputViewBean.setAideDureeIndeterminee(rfMaintienDomicile.getAideDureeIndeterminee());
                } else {
                    RFUtils.setMsgErreurInattendueViewBean(rfMaintienDomicile, "_retrieve()",
                            "RFAttestationJointDossierJointTiersHelper");
                }
            } else if (IRFAttestations.ATTESTATION_AVS.equals(outputViewBean.getCsTypeAttestation())) {
                RFAttestationAVS rfAttestationAVS = new RFAttestationAVS();
                rfAttestationAVS.setSession((BSession) outputViewBean.getISession());
                rfAttestationAVS.setIdAttestationAVS(outputViewBean.getIdAttestation());

                rfAttestationAVS.retrieve();

                if (!rfAttestationAVS.isNew()) {
                    // on set notre viewBean
                    outputViewBean.setIdTiersAVS(rfAttestationAVS.getIdTiers());
                    outputViewBean.setCsGenreTravauxAVS(rfAttestationAVS.getGenreTravaux());
                    outputViewBean.setTauxHoraireAVS(rfAttestationAVS.getTauxHoraire());
                } else {
                    RFUtils.setMsgErreurInattendueViewBean(rfAttestationAVS, "_retrieve()",
                            "RFAttestationJointDossierJointTiersHelper");
                }

            }

        } catch (Exception e) {
            RFUtils.setMsgErreurViewBean(viewBean, e.getMessage());
        }
    }

    /**
     * @see globaz.framework.controller.FWHelper#execute(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        return deleguerExecute(viewBean, action, session);
    }

}
