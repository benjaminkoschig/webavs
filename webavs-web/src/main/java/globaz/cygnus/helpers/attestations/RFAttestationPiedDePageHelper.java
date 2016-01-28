package globaz.cygnus.helpers.attestations;

import globaz.cygnus.api.TypesDeSoins.IRFTypesDeSoins;
import globaz.cygnus.api.attestations.IRFAttestations;
import globaz.cygnus.db.attestations.RFAssAttestationDossier;
import globaz.cygnus.db.attestations.RFAttestation;
import globaz.cygnus.db.attestations.RFAttestationAVS;
import globaz.cygnus.db.attestations.RFAttestationJointDossier;
import globaz.cygnus.db.attestations.RFAttestationJointDossierManager;
import globaz.cygnus.db.attestations.RFFraisLivraison;
import globaz.cygnus.db.attestations.RFMaintienDomicile;
import globaz.cygnus.db.attestations.RFMoyensAuxiliairesBon;
import globaz.cygnus.db.attestations.RFMoyensAuxiliairesCertificat;
import globaz.cygnus.db.attestations.RFMoyensAuxiliairesDecision;
import globaz.cygnus.db.attestations.RFRegimeAlimentaire;
import globaz.cygnus.db.typeDeSoins.RFTypeDeSoinJointSousTypeDeSoin;
import globaz.cygnus.db.typeDeSoins.RFTypeDeSoinJointSousTypeDeSoinManager;
import globaz.cygnus.utils.RFUtils;
import globaz.cygnus.vb.attestations.RFAttestationPiedDePageViewBean;
import globaz.cygnus.vb.attestations.RFAttestationViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.helpers.PRAbstractHelper;
import java.util.Iterator;

public class RFAttestationPiedDePageHelper extends PRAbstractHelper {

    @Override
    protected void _add(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        if (!FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {

            BITransaction transaction = null;
            // récupération du viewBean
            RFAttestationPiedDePageViewBean attestationVB = (RFAttestationPiedDePageViewBean) viewBean;

            try {

                transaction = ((BSession) session).newTransaction();
                transaction.openTransaction();

                // récupération de l'idSousTypeSoin
                if (!JadeStringUtil.isEmpty(attestationVB.getCodeSousTypeDeSoinList())
                        && !JadeStringUtil.isEmpty(attestationVB.getCodeTypeDeSoinList())) {
                    RFTypeDeSoinJointSousTypeDeSoinManager typeSousTypeSoinManager = new RFTypeDeSoinJointSousTypeDeSoinManager();
                    typeSousTypeSoinManager.setSession((BSession) session);
                    typeSousTypeSoinManager.setForCodeTypeDeSoin(attestationVB.getCodeTypeDeSoinList());
                    typeSousTypeSoinManager.setForCodeSousTypeDeSoin(attestationVB.getCodeSousTypeDeSoinList());
                    typeSousTypeSoinManager.changeManagerSize(0);
                    typeSousTypeSoinManager.find();
                    attestationVB.setIdSousTypeSoin(((RFTypeDeSoinJointSousTypeDeSoin) typeSousTypeSoinManager
                            .getFirstEntity()).getIdSousTypeSoin());
                }

                validate(attestationVB, (BSession) session);
                // Si le viewBean n'a pas d'erreurs on enregistre la demande
                if (!FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {

                    RFAttestation rfAttestation = new RFAttestation();
                    rfAttestation.setSession((BSession) attestationVB.getISession());
                    rfAttestation.setIdGestionnaire(attestationVB.getIdGestionnaire());
                    rfAttestation.setTypeDocument(attestationVB.getCsTypeAttestation());
                    // on met la date du jour
                    rfAttestation.setDateCreation(JACalendar.today().toStr("."));

                    rfAttestation.add(transaction);

                    // ajouter un enregistrement à la table association
                    // dossier-attestation-sous type de soin
                    RFAssAttestationDossier rfAssAttestationDossier = new RFAssAttestationDossier();
                    rfAssAttestationDossier.setSession((BSession) attestationVB.getISession());
                    rfAssAttestationDossier.setIdAttestation(rfAttestation.getIdAttestation());
                    rfAssAttestationDossier.setDateDebut(attestationVB.getDateDebut());
                    rfAssAttestationDossier.setDateFin(attestationVB.getDateFin());
                    rfAssAttestationDossier.setIdDossier(attestationVB.getIdDossier());
                    rfAssAttestationDossier.setIdSousTypeSoin(attestationVB.getIdSousTypeSoin());

                    rfAssAttestationDossier.add(transaction);

                    // on est dans le régime alimentaire
                    if (RFAttestationViewBean.TYPE_ATTESTATION_REGIME_ALI2.equals(attestationVB.getTypeAttestation())
                            && validateRegimeAlimentaire(attestationVB, (BSession) session)) {

                        RFRegimeAlimentaire rfRegimeAlimentaire = new RFRegimeAlimentaire();

                        rfRegimeAlimentaire.setSession((BSession) attestationVB.getISession());
                        rfRegimeAlimentaire.setIdAttestationRegimeAlimentaire(rfAttestation.getIdAttestation());
                        rfRegimeAlimentaire.setDateReception(attestationVB.getDateReception());
                        rfRegimeAlimentaire.setDateDecision(attestationVB.getDateDecision());
                        rfRegimeAlimentaire.setDateDecisionRefus(attestationVB.getDateDecisionRefus());
                        rfRegimeAlimentaire.setDateEnvoiInfosMedecin(attestationVB.getDateEnvoiInfosMedecin());
                        rfRegimeAlimentaire.setDateRetourInfosMedecin(attestationVB.getDateRetourInfosMedecin());
                        rfRegimeAlimentaire.setDateEnvoiEvaluationCMS(attestationVB.getDateEnvoiEvaluationCMS());
                        rfRegimeAlimentaire.setDateRetourEvaluationCMS(attestationVB.getDateRetourEvaluationCMS());
                        rfRegimeAlimentaire.setTypeRegime(attestationVB.getTypeRegime());
                        rfRegimeAlimentaire.setMontantMensuelAccepte(attestationVB.getMontantMensuelAccepte());
                        rfRegimeAlimentaire.setEcheanceRevision(attestationVB.getEcheanceRevision());
                        rfRegimeAlimentaire.setCommentaire(attestationVB.getCommentaire());
                        rfRegimeAlimentaire.setIsRegimeAccepte(attestationVB.getIsRegimeAccepte());
                        rfRegimeAlimentaire.setAutre(attestationVB.getAutresFraisPourRegime());
                        rfRegimeAlimentaire.setDateAideDureeDetermineeDesLe(attestationVB
                                .getDateAideDureeDetermineeDesLe());
                        rfRegimeAlimentaire.setDateAideDureeDetermineeJusqua(attestationVB
                                .getDateAideDureeDetermineeJusqua());
                        rfRegimeAlimentaire.setDateAideDureeIndetermineeDesLe(attestationVB
                                .getDateAideDureeIndetermineeDesLe());
                        rfRegimeAlimentaire.setDateAideDureeIndetermineeReevaluation(attestationVB
                                .getDateAideDureeIndetermineeReevaluation());

                        rfRegimeAlimentaire.add(transaction);
                    }
                    // on est dans frais livraison
                    else if (RFAttestationViewBean.TYPE_ATTESTATION_FRAIS_LIVRAISON9.equals(attestationVB
                            .getTypeAttestation()) && validateFraisLivraison(attestationVB, (BSession) session)) {
                        RFFraisLivraison rfFraisLivraison = new RFFraisLivraison();

                        rfFraisLivraison.setSession((BSession) attestationVB.getISession());
                        rfFraisLivraison.setIdAttestationFraisLivraison(rfAttestation.getIdAttestation());
                        rfFraisLivraison.setCentreLocation(attestationVB.getCentreLocation());
                        rfFraisLivraison.setDate(attestationVB.getDateFraisLiv());
                        rfFraisLivraison.setOfficePC(attestationVB.getOfficePC());
                        rfFraisLivraison.setRemarque(attestationVB.getRemarque());
                        rfFraisLivraison.setAttestationLivraison(attestationVB.getAttestationLivraison());

                        rfFraisLivraison.add(transaction);
                    }
                    // on est dans maintien à domicile
                    else if (RFAttestationViewBean.TYPE_ATTESTATION_MAINTIEN_DOMICILE13.equals(attestationVB
                            .getTypeAttestation()) && validateMaintienDomicile(attestationVB, (BSession) session)) {
                        RFMaintienDomicile rfMaintienDomicile = new RFMaintienDomicile();

                        rfMaintienDomicile.setSession((BSession) attestationVB.getISession());
                        rfMaintienDomicile.setIdAttestationMaintienDomicile(rfAttestation.getIdAttestation());

                        rfMaintienDomicile.setaTroubleAge(attestationVB.getaTroubleAge());
                        rfMaintienDomicile.setaMaladie(attestationVB.getaMaladie());
                        rfMaintienDomicile.setaAccident(attestationVB.getaAccident());
                        rfMaintienDomicile.setaInvalidite(attestationVB.getaInvalidite());
                        rfMaintienDomicile.setDescriptionMotif(attestationVB.getDescriptionMotif());
                        rfMaintienDomicile.setAutrePersonneATroubleAge(attestationVB.getAutrePersonneATroubleAge());
                        rfMaintienDomicile.setAutrePersonneAMaladie(attestationVB.getAutrePersonneAMaladie());
                        rfMaintienDomicile.setAutrePersonneAAccident(attestationVB.getAutrePersonneAAccident());
                        rfMaintienDomicile.setAutrePersonneAInvalidite(attestationVB.getAutrePersonneAInvalidite());
                        rfMaintienDomicile.setAutrePersonneDescriptionMotif(attestationVB
                                .getAutrePersonneDescriptionMotif());
                        rfMaintienDomicile.setNombreTotalPiece(attestationVB.getNombreTotalPiece());
                        rfMaintienDomicile.setNombrePieceUtilise(attestationVB.getNombrePieceUtilise());
                        rfMaintienDomicile.setNombrePersonneLogement(attestationVB.getNombrePersonneLogement());
                        rfMaintienDomicile.setVeillesPresence(attestationVB.getVeillesPresence());
                        rfMaintienDomicile.setNettoyageRangement(attestationVB.getNettoyageRangement());
                        rfMaintienDomicile.setVaisselle(attestationVB.getVaisselle());
                        rfMaintienDomicile.setLits(attestationVB.getLits());
                        rfMaintienDomicile.setLessive(attestationVB.getLessive());
                        rfMaintienDomicile.setRepassage(attestationVB.getRepassage());
                        rfMaintienDomicile.setDureeAideRemunere(attestationVB.getDureeAideRemunere());
                        rfMaintienDomicile.setDureeAideRemunereTenueMenage(attestationVB
                                .getDureeAideRemunereTenueMenage());
                        rfMaintienDomicile.setRepasDomicileCMS(attestationVB.getRepasDomicileCMS());
                        rfMaintienDomicile.setRecoitRepasCMS(attestationVB.getRecoitRepasCMS());
                        rfMaintienDomicile.setRaisonPasRepas(attestationVB.getRaisonPasRepas());
                        rfMaintienDomicile.setAideRemunereNecessaire(attestationVB.getAideRemunereNecessaire());
                        rfMaintienDomicile.setHeuresMoisRemunere(attestationVB.getHeuresMoisRemunere());
                        rfMaintienDomicile.setAideDureeDeterminee(attestationVB.getAideDureeDeterminee());
                        rfMaintienDomicile.setAideDureeIndeterminee(attestationVB.getAideDureeIndeterminee());

                        rfMaintienDomicile.add(transaction);
                    }
                    // on est dans moyens auxiliaire bons
                    else if (RFAttestationViewBean.TYPE_ATTESTATION_MOYENS_AUX_BON11.equals(attestationVB
                            .getTypeAttestation()) && validateBonsMoyensAuxiliaire(attestationVB, (BSession) session)) {
                        RFMoyensAuxiliairesBon rfMoyenAuxBons = new RFMoyensAuxiliairesBon();

                        rfMoyenAuxBons.setSession((BSession) attestationVB.getISession());
                        rfMoyenAuxBons.setIdAttestationMoyenAuxBon(rfAttestation.getIdAttestation());
                        rfMoyenAuxBons.setMoyenAuxiliaire(attestationVB.getMoyenAuxiliaire());
                        rfMoyenAuxBons.setDate(attestationVB.getDateMoyAuxB());
                        rfMoyenAuxBons.setDateEntretienTel(attestationVB.getDateEntretienTel());
                        rfMoyenAuxBons.setIdTiers(attestationVB.getIdTiers());

                        rfMoyenAuxBons.add(transaction);
                    }
                    // on est dans moyens auxiliaire certificat
                    else if (IRFAttestations.TYPE_ATTESTATION_MOYENS_AUX_CERTIFICAT3.equals(attestationVB
                            .getTypeAttestation()) && validateBonsMoyensAuxiliaire(attestationVB, (BSession) session)) {
                        RFMoyensAuxiliairesCertificat rfMoyenAuxCert = new RFMoyensAuxiliairesCertificat();

                        rfMoyenAuxCert.setSession((BSession) attestationVB.getISession());
                        rfMoyenAuxCert.setIdAttestationMoyenAuxCertificat(rfAttestation.getIdAttestation());
                        rfMoyenAuxCert.setDateCertificat(attestationVB.getDateCertificat());

                        rfMoyenAuxCert.add(transaction);
                    }
                    // on est dans moyens auxiliaire décision
                    else if (IRFAttestations.TYPE_ATTESTATION_MOYENS_AUX_DECISION5.equals(attestationVB
                            .getTypeAttestation()) && validateBonsMoyensAuxiliaire(attestationVB, (BSession) session)) {
                        RFMoyensAuxiliairesDecision rfMoyenAuxDecision = new RFMoyensAuxiliairesDecision();

                        rfMoyenAuxDecision.setSession((BSession) attestationVB.getISession());
                        rfMoyenAuxDecision.setIdAttestationMoyenAuxDecision(rfAttestation.getIdAttestation());
                        rfMoyenAuxDecision.setLibelleMoyenAuxiliaireDecision(attestationVB
                                .getLibelleMoyensAuxDecision());
                        rfMoyenAuxDecision.setDateDecision(attestationVB.getDateDecisionOAI());

                        rfMoyenAuxDecision.add(transaction);
                    }
                    // on est dans attestation AVS
                    else if (IRFAttestations.TYPE_ATTESTATION_AVS.equals(attestationVB.getTypeAttestation())
                            && validateAttestationAVS(attestationVB, (BSession) session)) {
                        RFAttestationAVS rfAttestationAVS = new RFAttestationAVS();

                        rfAttestationAVS.setSession((BSession) attestationVB.getISession());
                        rfAttestationAVS.setIdAttestationAVS(rfAttestation.getIdAttestation());
                        rfAttestationAVS.setIdTiers(attestationVB.getIdTiersAVS());
                        rfAttestationAVS.setGenreTravaux(attestationVB.getCsGenreTravauxAVS());
                        rfAttestationAVS.setTauxHoraire(attestationVB.getTauxHoraireAVS());

                        rfAttestationAVS.add(transaction);
                    }
                }

            } catch (Exception e) {
                if (transaction != null) {
                    transaction.setRollbackOnly();
                }
                throw e;
            } finally {
                if (transaction != null) {
                    try {
                        if (FWViewBeanInterface.ERROR.equals(viewBean.getMsgType()) || transaction.hasErrors()
                                || transaction.isRollbackOnly()) {
                            transaction.rollback();
                        } else {
                            transaction.commit();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                    } finally {
                        transaction.closeTransaction();
                    }
                }
            }
        }
    }

    @Override
    protected void _delete(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        BITransaction transaction = null;

        RFAttestationPiedDePageViewBean attestationVB = (RFAttestationPiedDePageViewBean) viewBean;

        try {
            transaction = ((BSession) session).newTransaction();
            transaction.openTransaction();

            // il faut supprimer l'attestation - la jointure - l'attestation
            // spécifique
            RFAttestation attestation = new RFAttestation();
            attestation.setSession(attestationVB.getSession());
            attestation.setIdAttestation(attestationVB.getIdAttestation());
            attestation.retrieve();

            RFAssAttestationDossier attestationDossier = new RFAssAttestationDossier();
            attestationDossier.setSession(attestationVB.getSession());
            attestationDossier.setIdAttestationDossier(attestationVB.getIdAttestationDossier());
            attestationDossier.retrieve();

            // en fonction du type de document
            if (IRFAttestations.REGIME_ALIMENTAIRE.equals(attestationVB.getCsTypeAttestation())) {
                RFRegimeAlimentaire regimeAlimentaire = new RFRegimeAlimentaire();
                regimeAlimentaire.setSession(attestationVB.getSession());
                regimeAlimentaire.setIdAttestationRegimeAlimentaire(attestationVB.getIdAttestation());
                regimeAlimentaire.retrieve();
                // suppression des enregistrement des trois tables
                if (!attestationDossier.isNew() && !attestation.isNew() && !regimeAlimentaire.isNew()) {
                    attestationDossier.delete(transaction);
                    attestation.delete(transaction);
                    regimeAlimentaire.delete(transaction);
                } else {
                    RFUtils.setMsgErreurInattendueViewBean(attestationVB, "_delete()", "RFAttestationPiedDePageHelper");
                }

            } else if (IRFAttestations.MAINTIEN_DOMICILE.equals(attestationVB.getCsTypeAttestation())) {
                RFMaintienDomicile maintienDomicile = new RFMaintienDomicile();
                maintienDomicile.setSession(attestationVB.getSession());
                maintienDomicile.setIdAttestationMaintienDomicile(attestationVB.getIdAttestation());
                maintienDomicile.retrieve();
                // suppression des enregistrement des trois tables
                if (!attestationDossier.isNew() && !attestation.isNew() && !maintienDomicile.isNew()) {
                    attestationDossier.delete(transaction);
                    attestation.delete(transaction);
                    maintienDomicile.delete(transaction);
                } else {
                    RFUtils.setMsgErreurInattendueViewBean(attestationVB, "_delete()", "RFAttestationPiedDePageHelper");
                }
            } else if (IRFAttestations.FRAIS_LIVRAISON.equals(attestationVB.getCsTypeAttestation())) {
                RFFraisLivraison fraisLivraison = new RFFraisLivraison();
                fraisLivraison.setSession(attestationVB.getSession());
                fraisLivraison.setIdAttestationFraisLivraison(attestationVB.getIdAttestation());
                fraisLivraison.retrieve();
                // suppression des enregistrement des trois tables
                if (!attestationDossier.isNew() && !attestation.isNew() && !fraisLivraison.isNew()) {
                    attestationDossier.delete(transaction);
                    attestation.delete(transaction);
                    fraisLivraison.delete(transaction);
                } else {
                    RFUtils.setMsgErreurInattendueViewBean(attestationVB, "_delete()", "RFAttestationPiedDePageHelper");
                }
            } else if (IRFAttestations.BON_MOYENS_AUXILIAIRES.equals(attestationVB.getCsTypeAttestation())) {
                RFMoyensAuxiliairesBon moyensAuxBon = new RFMoyensAuxiliairesBon();
                moyensAuxBon.setSession(attestationVB.getSession());
                moyensAuxBon.setIdAttestationMoyenAuxBon(attestationVB.getIdAttestation());
                moyensAuxBon.retrieve();
                // suppression des enregistrement des trois tables
                if (!attestationDossier.isNew() && !attestation.isNew() && !moyensAuxBon.isNew()) {
                    attestationDossier.delete(transaction);
                    attestation.delete(transaction);
                    moyensAuxBon.delete(transaction);
                } else {
                    RFUtils.setMsgErreurInattendueViewBean(attestationVB, "_delete()", "RFAttestationPiedDePageHelper");
                }
            } else if (IRFAttestations.CERTIFICAT_MOYENS_AUXILIAIRES.equals(attestationVB.getCsTypeAttestation())) {
                RFMoyensAuxiliairesCertificat moyensAuxCert = new RFMoyensAuxiliairesCertificat();
                moyensAuxCert.setSession(attestationVB.getSession());
                moyensAuxCert.setIdAttestationMoyenAuxCertificat(attestationVB.getIdAttestation());
                moyensAuxCert.retrieve();
                // suppression des enregistrement des trois tables
                if (!attestationDossier.isNew() && !attestation.isNew() && !moyensAuxCert.isNew()) {
                    attestationDossier.delete(transaction);
                    attestation.delete(transaction);
                    moyensAuxCert.delete(transaction);
                } else {
                    RFUtils.setMsgErreurInattendueViewBean(attestationVB, "_delete()", "RFAttestationPiedDePageHelper");
                }
            } else if (IRFAttestations.DECISION_MOYENS_AUXILIAIRES.equals(attestationVB.getCsTypeAttestation())) {
                RFMoyensAuxiliairesDecision moyensAuxDecision = new RFMoyensAuxiliairesDecision();
                moyensAuxDecision.setSession(attestationVB.getSession());
                moyensAuxDecision.setIdAttestationMoyenAuxDecision(attestationVB.getIdAttestation());
                moyensAuxDecision.retrieve();
                // suppression des enregistrement des trois tables
                if (!attestationDossier.isNew() && !attestation.isNew() && !moyensAuxDecision.isNew()) {
                    attestationDossier.delete(transaction);
                    attestation.delete(transaction);
                    moyensAuxDecision.delete(transaction);
                } else {
                    RFUtils.setMsgErreurInattendueViewBean(attestationVB, "_delete()", "RFAttestationPiedDePageHelper");
                }
            } else if (IRFAttestations.ATTESTATION_AVS.equals(attestationVB.getCsTypeAttestation())) {
                RFAttestationAVS rfAttestationAVS = new RFAttestationAVS();
                rfAttestationAVS.setSession(attestationVB.getSession());
                rfAttestationAVS.setIdAttestationAVS(attestationVB.getIdAttestation());
                rfAttestationAVS.retrieve();
                // suppression des enregistrement des trois tables
                if (!attestationDossier.isNew() && !attestation.isNew() && !rfAttestationAVS.isNew()) {
                    attestationDossier.delete(transaction);
                    attestation.delete(transaction);
                    rfAttestationAVS.delete(transaction);
                } else {
                    RFUtils.setMsgErreurInattendueViewBean(attestationVB, "_delete()", "RFAttestationPiedDePageHelper");
                }
            }

        } catch (Exception e) {
            if (transaction != null) {
                transaction.setRollbackOnly();
            }
            throw e;
        } finally {
            if (transaction != null) {
                try {
                    if (transaction.hasErrors() || transaction.isRollbackOnly()
                            || FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {

                        transaction.rollback();
                    } else {
                        transaction.commit();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                } finally {
                    transaction.closeTransaction();
                }
            }
        }
        // }
    }

    @Override
    protected void _init(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        // selon la valeur du code systeme forCsTypeAttestation on init le
        // typeAttesation pour ajouter le bon type
        RFAttestationPiedDePageViewBean vb = (RFAttestationPiedDePageViewBean) viewBean;

        vb.setIdSousTypeSoin(RFUtils.getIdSousTypeDeSoin(vb.getCodeTypeDeSoinList(), vb.getCodeSousTypeDeSoinList(),
                (BSession) session));

        if (validerDateGestionnaireVides(vb, session) && validerUnicite(true, vb, session)
                && validerTypeDocumentSoin(vb, session)) {

            Integer typeAttestation = new Integer(vb.getCsTypeAttestation());

            switch (typeAttestation) {
                case IRFAttestations.INT_REGIME_ALIMENTAIRE:
                    vb.setTypeAttestation(RFAttestationViewBean.TYPE_ATTESTATION_REGIME_ALI2);
                    break;
                case IRFAttestations.INT_BON_MOYENS_AUXILIAIRES:
                    vb.setTypeAttestation(RFAttestationViewBean.TYPE_ATTESTATION_MOYENS_AUX_BON11);
                    break;
                case IRFAttestations.INT_FRAIS_LIVRAISON:
                    vb.setTypeAttestation(RFAttestationViewBean.TYPE_ATTESTATION_FRAIS_LIVRAISON9);
                    break;
                case IRFAttestations.INT_MAINTIEN_DOMICILE:
                    vb.setTypeAttestation(RFAttestationViewBean.TYPE_ATTESTATION_MAINTIEN_DOMICILE13);
                    break;
                case IRFAttestations.INT_CERTIFICAT_MOYENS_AUXILIAIRES:
                    vb.setTypeAttestation(IRFAttestations.TYPE_ATTESTATION_MOYENS_AUX_CERTIFICAT3);
                    break;
                case IRFAttestations.INT_DECISION_MOYENS_AUXILIAIRES:
                    vb.setTypeAttestation(IRFAttestations.TYPE_ATTESTATION_MOYENS_AUX_DECISION5);
                    break;
                case IRFAttestations.INT_ATTESTATION_AVS:
                    vb.setTypeAttestation(IRFAttestations.TYPE_ATTESTATION_AVS);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        // TODO Auto-generated method stub
        super._retrieve(viewBean, action, session);
    }

    // update d'une attestation
    @Override
    protected void _update(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        // récupération du viewBean
        RFAttestationPiedDePageViewBean attestationVB = (RFAttestationPiedDePageViewBean) viewBean;

        // Si le viewBean n'a pas d'erreurs on modifie l'attestation
        validate(attestationVB, (BSession) session);
        if (validerUnicite(false, attestationVB, session) && validerDateGestionnaireVides(attestationVB, session)
                && validerTypeDocumentSoin(attestationVB, session)) {
            if (!FWViewBeanInterface.ERROR.equals(attestationVB.getMsgType())) {

                BITransaction transaction = null;
                try {
                    transaction = ((BSession) session).newTransaction();
                    transaction.openTransaction();

                    // on retrieve RFAttestation
                    RFAttestation rfAttestation = new RFAttestation();
                    rfAttestation.setSession((BSession) attestationVB.getISession());
                    rfAttestation.setIdAttestation(attestationVB.getIdAttestation());
                    rfAttestation.retrieve();

                    if (!rfAttestation.isNew()) {
                        rfAttestation.setIdGestionnaire(attestationVB.getIdGestionnaire());
                        rfAttestation.setTypeDocument(attestationVB.getCsTypeAttestation());

                        rfAttestation.update(transaction);
                    }

                    // on retrieve RFAssAttestationDossier
                    RFAssAttestationDossier rfAssAttestationDossier = new RFAssAttestationDossier();
                    rfAssAttestationDossier.setSession((BSession) attestationVB.getISession());
                    rfAssAttestationDossier.setIdAttestationDossier(attestationVB.getIdAttestationDossier());
                    rfAssAttestationDossier.retrieve();

                    if (!rfAssAttestationDossier.isNew()) {
                        rfAssAttestationDossier.setIdAttestation(rfAttestation.getIdAttestation());
                        rfAssAttestationDossier.setDateDebut(attestationVB.getDateDebut());
                        rfAssAttestationDossier.setDateFin(attestationVB.getDateFin());
                        rfAssAttestationDossier.setIdDossier(attestationVB.getIdDossier());
                        rfAssAttestationDossier.setIdSousTypeSoin(attestationVB.getIdSousTypeSoin());

                        rfAssAttestationDossier.update(transaction);
                    }

                    if (IRFAttestations.REGIME_ALIMENTAIRE.equals(attestationVB.getCsTypeAttestation())
                            && validateRegimeAlimentaire(attestationVB, (BSession) session)) {
                        // on retrieve RFAssAttestationDossier
                        RFRegimeAlimentaire rfRegimeAlimentaire = new RFRegimeAlimentaire();
                        rfRegimeAlimentaire.setSession((BSession) attestationVB.getISession());
                        rfRegimeAlimentaire.setIdAttestationRegimeAlimentaire(attestationVB.getIdAttestation());
                        rfRegimeAlimentaire.retrieve();

                        if (!rfRegimeAlimentaire.isNew()) {
                            rfRegimeAlimentaire.setSession((BSession) attestationVB.getISession());
                            rfRegimeAlimentaire.setIdAttestationRegimeAlimentaire(rfAttestation.getIdAttestation());
                            rfRegimeAlimentaire.setDateReception(attestationVB.getDateReception());
                            rfRegimeAlimentaire.setDateDecision(attestationVB.getDateDecision());
                            rfRegimeAlimentaire.setDateDecisionRefus(attestationVB.getDateDecisionRefus());
                            rfRegimeAlimentaire.setDateEnvoiInfosMedecin(attestationVB.getDateEnvoiInfosMedecin());
                            rfRegimeAlimentaire.setDateRetourInfosMedecin(attestationVB.getDateRetourInfosMedecin());
                            rfRegimeAlimentaire.setDateEnvoiEvaluationCMS(attestationVB.getDateEnvoiEvaluationCMS());
                            rfRegimeAlimentaire.setDateRetourEvaluationCMS(attestationVB.getDateRetourEvaluationCMS());
                            rfRegimeAlimentaire.setTypeRegime(attestationVB.getTypeRegime());
                            rfRegimeAlimentaire.setMontantMensuelAccepte(attestationVB.getMontantMensuelAccepte());
                            rfRegimeAlimentaire.setEcheanceRevision(attestationVB.getEcheanceRevision());
                            rfRegimeAlimentaire.setCommentaire(attestationVB.getCommentaire());
                            rfRegimeAlimentaire.setIsRegimeAccepte(attestationVB.getIsRegimeAccepte());
                            rfRegimeAlimentaire.setAutre(attestationVB.getAutresFraisPourRegime());
                            rfRegimeAlimentaire.setDateAideDureeDetermineeDesLe(attestationVB
                                    .getDateAideDureeDetermineeDesLe());
                            rfRegimeAlimentaire.setDateAideDureeDetermineeJusqua(attestationVB
                                    .getDateAideDureeDetermineeJusqua());
                            rfRegimeAlimentaire.setDateAideDureeIndetermineeDesLe(attestationVB
                                    .getDateAideDureeIndetermineeDesLe());
                            rfRegimeAlimentaire.setDateAideDureeIndetermineeReevaluation(attestationVB
                                    .getDateAideDureeIndetermineeReevaluation());

                            rfRegimeAlimentaire.update(transaction);
                        }

                    }
                    // on est dans frais livraison
                    else if (IRFAttestations.FRAIS_LIVRAISON.equals(attestationVB.getCsTypeAttestation())
                            && validateFraisLivraison(attestationVB, (BSession) session)) {
                        RFFraisLivraison rfFraisLivraison = new RFFraisLivraison();
                        rfFraisLivraison.setSession((BSession) attestationVB.getISession());
                        rfFraisLivraison.setIdAttestationFraisLivraison(attestationVB.getIdAttestation());
                        rfFraisLivraison.retrieve();

                        if (!rfFraisLivraison.isNew()) {
                            rfFraisLivraison.setSession((BSession) attestationVB.getISession());
                            rfFraisLivraison.setIdAttestationFraisLivraison(rfAttestation.getIdAttestation());
                            rfFraisLivraison.setCentreLocation(attestationVB.getCentreLocation());
                            rfFraisLivraison.setDate(attestationVB.getDateFraisLiv());
                            rfFraisLivraison.setOfficePC(attestationVB.getOfficePC());
                            rfFraisLivraison.setRemarque(attestationVB.getRemarque());
                            rfFraisLivraison.setAttestationLivraison(attestationVB.getAttestationLivraison());

                            rfFraisLivraison.update(transaction);
                        }

                    }
                    // on est dans maintien à domicile
                    else if (IRFAttestations.MAINTIEN_DOMICILE.equals(attestationVB.getCsTypeAttestation())
                            && validateMaintienDomicile(attestationVB, (BSession) session)) {
                        RFMaintienDomicile rfMaintienDomicile = new RFMaintienDomicile();
                        rfMaintienDomicile.setSession((BSession) attestationVB.getISession());
                        rfMaintienDomicile.setIdAttestationMaintienDomicile(attestationVB.getIdAttestation());
                        rfMaintienDomicile.retrieve();

                        if (!rfMaintienDomicile.isNew()) {
                            rfMaintienDomicile.setaTroubleAge(attestationVB.getaTroubleAge());
                            rfMaintienDomicile.setaMaladie(attestationVB.getaMaladie());
                            rfMaintienDomicile.setaAccident(attestationVB.getaAccident());
                            rfMaintienDomicile.setaInvalidite(attestationVB.getaInvalidite());
                            rfMaintienDomicile.setDescriptionMotif(attestationVB.getDescriptionMotif());
                            rfMaintienDomicile.setAutrePersonneATroubleAge(attestationVB.getAutrePersonneATroubleAge());
                            rfMaintienDomicile.setAutrePersonneAMaladie(attestationVB.getAutrePersonneAMaladie());
                            rfMaintienDomicile.setAutrePersonneAAccident(attestationVB.getAutrePersonneAAccident());
                            rfMaintienDomicile.setAutrePersonneAInvalidite(attestationVB.getAutrePersonneAInvalidite());
                            rfMaintienDomicile.setAutrePersonneDescriptionMotif(attestationVB
                                    .getAutrePersonneDescriptionMotif());
                            rfMaintienDomicile.setNombreTotalPiece(attestationVB.getNombreTotalPiece());
                            rfMaintienDomicile.setNombrePieceUtilise(attestationVB.getNombrePieceUtilise());
                            rfMaintienDomicile.setNombrePersonneLogement(attestationVB.getNombrePersonneLogement());
                            rfMaintienDomicile.setVeillesPresence(attestationVB.getVeillesPresence());
                            rfMaintienDomicile.setNettoyageRangement(attestationVB.getNettoyageRangement());
                            rfMaintienDomicile.setVaisselle(attestationVB.getVaisselle());
                            rfMaintienDomicile.setLits(attestationVB.getLits());
                            rfMaintienDomicile.setLessive(attestationVB.getLessive());
                            rfMaintienDomicile.setRepassage(attestationVB.getRepassage());
                            rfMaintienDomicile.setDureeAideRemunere(attestationVB.getDureeAideRemunere());
                            rfMaintienDomicile.setDureeAideRemunereTenueMenage(attestationVB
                                    .getDureeAideRemunereTenueMenage());
                            rfMaintienDomicile.setRepasDomicileCMS(attestationVB.getRepasDomicileCMS());
                            rfMaintienDomicile.setRecoitRepasCMS(attestationVB.getRecoitRepasCMS());
                            rfMaintienDomicile.setRaisonPasRepas(attestationVB.getRaisonPasRepas());
                            rfMaintienDomicile.setAideRemunereNecessaire(attestationVB.getAideRemunereNecessaire());
                            rfMaintienDomicile.setHeuresMoisRemunere(attestationVB.getHeuresMoisRemunere());
                            rfMaintienDomicile.setAideDureeDeterminee(attestationVB.getAideDureeDeterminee());
                            rfMaintienDomicile.setAideDureeIndeterminee(attestationVB.getAideDureeIndeterminee());

                            rfMaintienDomicile.update(transaction);
                        }
                    }
                    // on est dans moyens auxiliaire bons
                    else if (IRFAttestations.BON_MOYENS_AUXILIAIRES.equals(attestationVB.getCsTypeAttestation())
                            && validateBonsMoyensAuxiliaire(attestationVB, (BSession) session)) {
                        RFMoyensAuxiliairesBon rfMoyenAuxBons = new RFMoyensAuxiliairesBon();
                        rfMoyenAuxBons.setSession((BSession) attestationVB.getISession());
                        rfMoyenAuxBons.setIdAttestationMoyenAuxBon(attestationVB.getIdAttestation());
                        rfMoyenAuxBons.retrieve();

                        if (!rfMoyenAuxBons.isNew()) {
                            rfMoyenAuxBons.setSession((BSession) attestationVB.getISession());
                            rfMoyenAuxBons.setIdAttestationMoyenAuxBon(rfAttestation.getIdAttestation());
                            rfMoyenAuxBons.setMoyenAuxiliaire(attestationVB.getMoyenAuxiliaire());
                            rfMoyenAuxBons.setDate(attestationVB.getDateMoyAuxB());
                            rfMoyenAuxBons.setDateEntretienTel(attestationVB.getDateEntretienTel());
                            rfMoyenAuxBons.setIdTiers(attestationVB.getIdTiers());

                            rfMoyenAuxBons.update(transaction);
                        }
                    }
                    // on est dans moyens auxiliaire certificats
                    else if (IRFAttestations.CERTIFICAT_MOYENS_AUXILIAIRES.equals(attestationVB.getCsTypeAttestation())
                            && validateBonsMoyensAuxiliaire(attestationVB, (BSession) session)) {
                        RFMoyensAuxiliairesCertificat rfMoyenAuxCert = new RFMoyensAuxiliairesCertificat();
                        rfMoyenAuxCert.setSession((BSession) attestationVB.getISession());
                        rfMoyenAuxCert.setIdAttestationMoyenAuxCertificat(attestationVB.getIdAttestation());
                        rfMoyenAuxCert.retrieve();

                        if (!rfMoyenAuxCert.isNew()) {
                            rfMoyenAuxCert.setSession((BSession) attestationVB.getISession());
                            rfMoyenAuxCert.setIdAttestationMoyenAuxCertificat(rfAttestation.getIdAttestation());
                            rfMoyenAuxCert.setDateCertificat(attestationVB.getDateCertificat());

                            rfMoyenAuxCert.update(transaction);
                        }
                    }
                    // on est dans moyens auxiliaire decision
                    else if (IRFAttestations.DECISION_MOYENS_AUXILIAIRES.equals(attestationVB.getCsTypeAttestation())
                            && validateBonsMoyensAuxiliaire(attestationVB, (BSession) session)) {
                        RFMoyensAuxiliairesDecision rfMoyenAuxDecision = new RFMoyensAuxiliairesDecision();
                        rfMoyenAuxDecision.setSession((BSession) attestationVB.getISession());
                        rfMoyenAuxDecision.setIdAttestationMoyenAuxDecision(attestationVB.getIdAttestation());
                        rfMoyenAuxDecision.retrieve();

                        if (!rfMoyenAuxDecision.isNew()) {
                            rfMoyenAuxDecision.setSession((BSession) attestationVB.getISession());
                            rfMoyenAuxDecision.setIdAttestationMoyenAuxDecision(rfAttestation.getIdAttestation());
                            rfMoyenAuxDecision.setLibelleMoyenAuxiliaireDecision(attestationVB
                                    .getLibelleMoyensAuxDecision());
                            rfMoyenAuxDecision.setDateDecision(attestationVB.getDateDecisionOAI());

                            rfMoyenAuxDecision.update(transaction);
                        }
                    }
                    // on est dans attestation AVS
                    else if (IRFAttestations.ATTESTATION_AVS.equals(attestationVB.getCsTypeAttestation())
                            && validateAttestationAVS(attestationVB, (BSession) session)) {
                        RFAttestationAVS rfAttestationAVS = new RFAttestationAVS();
                        rfAttestationAVS.setSession((BSession) attestationVB.getISession());
                        rfAttestationAVS.setIdAttestationAVS(attestationVB.getIdAttestation());
                        rfAttestationAVS.retrieve();

                        if (!rfAttestationAVS.isNew()) {
                            rfAttestationAVS.setSession((BSession) attestationVB.getISession());
                            rfAttestationAVS.setIdAttestationAVS(rfAttestation.getIdAttestation());
                            rfAttestationAVS.setIdTiers(attestationVB.getIdTiersAVS());
                            rfAttestationAVS.setGenreTravaux(attestationVB.getCsGenreTravauxAVS());
                            rfAttestationAVS.setTauxHoraire(attestationVB.getTauxHoraireAVS());

                            rfAttestationAVS.update(transaction);
                        }
                    }

                } catch (Exception e) {
                    if (transaction != null) {
                        transaction.setRollbackOnly();
                    }
                    throw e;
                } finally {
                    if (transaction != null) {
                        try {
                            if (FWViewBeanInterface.ERROR.equals(viewBean.getMsgType()) || transaction.hasErrors()
                                    || transaction.isRollbackOnly()) {
                                transaction.rollback();
                            } else {
                                transaction.commit();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            throw e;
                        } finally {
                            transaction.closeTransaction();
                        }
                    }
                }
            }
        } else {

        }

    }

    /*
     * test si le String en paramètre représente un nombre positif ou nul ou "" renvoi vrai si positif ou nul faux si
     * négatif
     * 
     * private Boolean isEntierPositifOuNul(String montant) throws Exception { // enlever le formatage : ' if
     * (JadeStringUtil.isEmpty(montant)) { return true; } montant = montant.replace("'", ""); try { Integer floatMontant
     * = Integer.parseInt(montant); return floatMontant >= 0; } catch (Exception e) { return false; } }
     */

    /**
     * test si le String en paramètre représente un nombre positif ou nul ou "" renvoi vrai si positif ou nul faux si
     * négatif
     */
    private Boolean isNumericPositifOuNul(String montant) throws Exception {
        // enlever le formatage : '
        montant = montant.replace("'", "");
        if (JadeStringUtil.isEmpty(montant)) {
            return true;
        }
        try {
            Float floatMontant = Float.parseFloat(montant);
            return floatMontant >= 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Méthode de validation des attestations
     * 
     * @param session
     * @param statement
     * @throws Exception
     */
    private void validate(FWViewBeanInterface viewBean, BSession session) throws Exception {

        RFAttestationPiedDePageViewBean attestationVB = (RFAttestationPiedDePageViewBean) viewBean;

        // construction du message d'erreur
        if (JadeStringUtil.isBlank(attestationVB.getDateDebut())) {
            RFUtils.setMsgErreurViewBean(attestationVB, "ERREUR_RF_SAISIE_ATTESTATION_CHAMP_DATE_DEBUT_VIDE");
        }
        String dateDeFin = attestationVB.getDateFin();
        if (JadeStringUtil.isBlank(attestationVB.getDateFin())) {
            dateDeFin = "01.01.9999";
        }
        if (JadeStringUtil.isBlank(attestationVB.getGestionnaire())) {
            RFUtils.setMsgErreurViewBean(attestationVB, "ERREUR_RF_SAISIE_ATTESTATION_CHAMP_GESTIONNAIRE");
        }
        if (JadeStringUtil.isBlank(attestationVB.getCodeSousTypeDeSoin())) {
            RFUtils.setMsgErreurViewBean(attestationVB, "ERREUR_RF_SAISIE_ATTESTATION_CHAMP_TYPE_SOIN");
        }
        if (JadeStringUtil.isBlank(attestationVB.getCodeSousTypeDeSoin())) {
            RFUtils.setMsgErreurViewBean(attestationVB, "ERREUR_RF_SAISIE_ATTESTATION_CHAMP_SOUS_TYPE_SOIN");
        }

        // Vérification que dateDebut < dateFin et au bon format
        if ((attestationVB.getDateDebut().length() != 10) || (dateDeFin.length() != 10)) {
            RFUtils.setMsgErreurViewBean(attestationVB, "ERREUR_RF_SAISIE_ATTESTATION_FORMAT_DATE");
        }
        // TODO utiliser isGlobazDate
        if (!JadeStringUtil.isEmpty(attestationVB.getDateDebut()) && !JadeStringUtil.isEmpty(dateDeFin)) {
            if (JadeDateUtil.isDateAfter(attestationVB.getDateDebut(), dateDeFin)) {
                RFUtils.setMsgErreurViewBean(attestationVB, "ERREUR_RF_SAISIE_ATTESTATION_COMPARAISON_DATE");
            }
        }
    }

    /**
     * ajouter les validations pour les différents types d'attestation
     */
    private boolean validateAttestationAVS(FWViewBeanInterface viewBean, BSession session) throws Exception {

        RFAttestationPiedDePageViewBean attestationVB = (RFAttestationPiedDePageViewBean) viewBean;

        if (!JadeNumericUtil.isNumericPositif(attestationVB.getTauxHoraireAVS())
                && !JadeNumericUtil.isEmptyOrZero(attestationVB.getTauxHoraireAVS())) {
            RFUtils.setMsgErreurViewBean(attestationVB, "ERREUR_RF_SAISIE_ATTESTATION_PAS_NUMERIQUE_ENTIER");
            return false;
        }
        return true;
    }

    /**
     * ajouter les validations pour les différents types d'attestation
     */
    private boolean validateBonsMoyensAuxiliaire(FWViewBeanInterface viewBean, BSession session) throws Exception {

        RFAttestationPiedDePageViewBean attestationVB = (RFAttestationPiedDePageViewBean) viewBean;
        // vérification des dates
        if ((!JadeStringUtil.isEmpty(attestationVB.getDateEntretienTel()) && (attestationVB.getDateEntretienTel()
                .length() != 10))
                || (!JadeStringUtil.isEmpty(attestationVB.getDateMoyAuxB()) && (attestationVB.getDateMoyAuxB().length() != 10))) {
            RFUtils.setMsgErreurViewBean(attestationVB, "ERREUR_RF_SAISIE_ATTESTATION_FORMAT_DATE");
            return false;
        }
        return true;
    }

    /**
     * ajouter les validations pour les différents types d'attestation
     */
    private boolean validateFraisLivraison(FWViewBeanInterface viewBean, BSession session) throws Exception {

        RFAttestationPiedDePageViewBean attestationVB = (RFAttestationPiedDePageViewBean) viewBean;

        // vérification des dates
        if ((!JadeStringUtil.isEmpty(attestationVB.getDateFraisLiv()) && (attestationVB.getDateFraisLiv().length() != 10))
                || (!JadeStringUtil.isEmpty(attestationVB.getAttestationLivraison()) && (attestationVB
                        .getAttestationLivraison().length() != 10))) {
            RFUtils.setMsgErreurViewBean(attestationVB, "ERREUR_RF_SAISIE_ATTESTATION_FORMAT_DATE");
            return false;
        }
        return true;
    }

    /**
     * ajouter les validations pour les différents types d'attestation
     */
    private boolean validateMaintienDomicile(FWViewBeanInterface viewBean, BSession session) throws Exception {

        RFAttestationPiedDePageViewBean attestationVB = (RFAttestationPiedDePageViewBean) viewBean;

        // durée de l'aide num. positif
        if (!isNumericPositifOuNul(attestationVB.getDureeAideRemunere())
                || !isNumericPositifOuNul(attestationVB.getDureeAideRemunereTenueMenage())
                || !isNumericPositifOuNul(attestationVB.getHeuresMoisRemunere())) {
            RFUtils.setMsgErreurViewBean(attestationVB, "ERREUR_RF_SAISIE_ATTESTATION_PAS_NUMERIQUE");
            return false;
        }
        return true;
    }

    /**
     * ajouter les validations pour les différents types d'attestation
     */
    private boolean validateRegimeAlimentaire(FWViewBeanInterface viewBean, BSession session) throws Exception {

        RFAttestationPiedDePageViewBean attestationVB = (RFAttestationPiedDePageViewBean) viewBean;

        // vérification des dates
        if ((!JadeDateUtil.isGlobazDate(attestationVB.getDateReception()) && !JadeStringUtil.isEmpty(attestationVB
                .getDateReception()))
                || (!JadeDateUtil.isGlobazDate(attestationVB.getDateDecision()) && !JadeStringUtil
                        .isEmpty(attestationVB.getDateDecision()))
                || (!JadeDateUtil.isGlobazDate(attestationVB.getDateDecisionRefus()) && !JadeStringUtil
                        .isEmpty(attestationVB.getDateDecisionRefus()))
                || (!JadeDateUtil.isGlobazDate(attestationVB.getDateEnvoiInfosMedecin()) && !JadeStringUtil
                        .isEmpty(attestationVB.getDateEnvoiInfosMedecin()))
                || (!JadeDateUtil.isGlobazDate(attestationVB.getDateEnvoiEvaluationCMS()) && !JadeStringUtil
                        .isEmpty(attestationVB.getDateEnvoiEvaluationCMS()))
                || (!JadeDateUtil.isGlobazDate(attestationVB.getDateRetourEvaluationCMS()) && !JadeStringUtil
                        .isEmpty(attestationVB.getDateRetourEvaluationCMS()))
                || (!JadeDateUtil.isGlobazDate(attestationVB.getDateRetourInfosMedecin()) && !JadeStringUtil
                        .isEmpty(attestationVB.getDateRetourInfosMedecin()))
                || (!JadeDateUtil.isGlobazDate(attestationVB.getEcheanceRevision()) && !JadeStringUtil
                        .isEmpty(attestationVB.getEcheanceRevision()))) {
            RFUtils.setMsgErreurViewBean(attestationVB, "ERREUR_RF_SAISIE_ATTESTATION_FORMAT_DATE");
            return false;
        }
        return true;
    }

    /*
     * on vérifie que les dates entrées sont correctes.
     */
    protected boolean validerDateGestionnaireVides(RFAttestationPiedDePageViewBean vb, BISession session) {
        // vérification des dates
        if (!JadeDateUtil.isGlobazDate(vb.getDateDebut())) {
            RFUtils.setMsgErreurViewBean(vb, "ERREUR_RF_SAISIE_ATTESTATION_VIDE_FORMAT_DATE");
            vb.setIsUpdate(Boolean.TRUE);
            return false;
        }
        String dateDeFin = vb.getDateFin();
        if (JadeStringUtil.isBlank(vb.getDateFin())) {
            dateDeFin = "01.01.9999";
        }

        if (JadeDateUtil.isDateAfter(vb.getDateDebut().toString(), dateDeFin)) {
            RFUtils.setMsgErreurViewBean(vb, "ERREUR_RF_SAISIE_ATTESTATION_PERIODE");
            vb.setIsUpdate(Boolean.TRUE);
            return false;
        }

        if (JadeStringUtil.isEmpty(vb.getIdGestionnaire())) {
            RFUtils.setMsgErreurViewBean(vb, "ERREUR_RF_SAISIE_ATTESTATION_VIDE_GESTIONNAIRE");
            vb.setIsUpdate(Boolean.TRUE);
            return false;
        }
        return true;
    }

    /*
     * on verifie que le type de document correspond bien au type de soin indiqué Incomplet !!
     */
    protected boolean validerTypeDocumentSoin(RFAttestationPiedDePageViewBean vb, BISession session) {

        if (JadeStringUtil.isEmpty(vb.getCsTypeAttestation()) || JadeStringUtil.isEmpty(vb.getIdSousTypeSoin())) {
            RFUtils.setMsgErreurViewBean(vb, "ERREUR_RF_ATTESTATION_CHAMP_MANQUANT");
            return false;
        } else {
            int idSousTypeSoin = new Integer(vb.getIdSousTypeSoin());

            if ((idSousTypeSoin == new Integer(IRFTypesDeSoins.st_2_REGIME_ALIMENTAIRE).intValue())
                    && IRFAttestations.REGIME_ALIMENTAIRE.equals(vb.getCsTypeAttestation())) {
                return true;
            } else if (IRFAttestations.CERTIFICAT_MOYENS_AUXILIAIRES.equals(vb.getCsTypeAttestation())
                    && (((idSousTypeSoin >= new Integer(IRFTypesDeSoins.st_3_CHAISES_PERCEES).intValue()) && (idSousTypeSoin <= new Integer(
                            IRFTypesDeSoins.st_3_FRAIS_D_ENDOPROTHESES).intValue()))

                            || (idSousTypeSoin == new Integer(IRFTypesDeSoins.st_2_REGIME_ALIMENTAIRE).intValue())

                            || (idSousTypeSoin == new Integer(IRFTypesDeSoins.st_2_REGIME_ALIMENTAIRE_DIABETIQUE)
                                    .intValue())

                            || ((idSousTypeSoin >= new Integer(IRFTypesDeSoins.st_9_LIT_ELECTRIQUE).intValue()) && (idSousTypeSoin <= new Integer(
                                    IRFTypesDeSoins.st_9_BARRIERES).intValue()))

                            || (idSousTypeSoin >= new Integer(IRFTypesDeSoins.st_8_LIT_ELECTRIQUE).intValue())

                            || (idSousTypeSoin == new Integer(IRFTypesDeSoins.st_13_AIDE_AU_MENAGE_PAR_AIDE_PRIVEE)
                                    .intValue())

                            || (idSousTypeSoin == new Integer(
                                    IRFTypesDeSoins.st_13_AIDE_AU_MENAGE_PAR_UN_MEMBRE_DE_LA_FAMILLE_13bOMPC)
                                    .intValue())

                    || (((idSousTypeSoin >= new Integer(IRFTypesDeSoins.st_16_AU_LIEU_DU_TRAITEMENT_MEDICAL).intValue()) && (idSousTypeSoin <= new Integer(
                            IRFTypesDeSoins.st_16_VISITE_CHEZ_LES_PARENTS_ENFANT_EN_EMS).intValue()))))) {

                return true;

            } else if ((idSousTypeSoin >= new Integer(IRFTypesDeSoins.st_5_APPAREIL_ACOUSTIQUE).intValue())
                    && (idSousTypeSoin <= new Integer(IRFTypesDeSoins.st_5_PROTHESE_FACIALE_EPITHESES).intValue())
                    && IRFAttestations.DECISION_MOYENS_AUXILIAIRES.equals(vb.getCsTypeAttestation())) {
                return true;
            } else if ((idSousTypeSoin >= new Integer(IRFTypesDeSoins.st_9_LIT_ELECTRIQUE).intValue())
                    && (idSousTypeSoin <= new Integer(IRFTypesDeSoins.st_10_REPRISE_DE_LIT_ELECTRIQUE).intValue())
                    && IRFAttestations.FRAIS_LIVRAISON.equals(vb.getCsTypeAttestation())) {
                return true;
            } else if ((idSousTypeSoin >= new Integer(IRFTypesDeSoins.st_11_MACHINE_A_ECRIRE_ELECTRIQUE).intValue())
                    && (idSousTypeSoin <= new Integer(IRFTypesDeSoins.st_11_MAGNETOPHONE_POUR_AVEUGLE).intValue())
                    && IRFAttestations.BON_MOYENS_AUXILIAIRES.equals(vb.getCsTypeAttestation())) {
                return true;
            } else if ((idSousTypeSoin >= new Integer(IRFTypesDeSoins.st_13_AIDE_AU_MENAGE_PAR_SPITEX_OMSV_CMS)
                    .intValue())
                    && (idSousTypeSoin <= new Integer(
                            IRFTypesDeSoins.st_13_AIDE_AU_MENAGE_PAR_UN_MEMBRE_DE_LA_FAMILLE_13bOMPC).intValue())
                    && IRFAttestations.MAINTIEN_DOMICILE.equals(vb.getCsTypeAttestation())) {
                return true;
            } else if ((idSousTypeSoin == new Integer(IRFTypesDeSoins.st_13_AIDE_AU_MENAGE_PAR_AIDE_PRIVEE).intValue())
                    && IRFAttestations.ATTESTATION_AVS.equals(vb.getCsTypeAttestation())) {
                return true;
            } else {
                RFUtils.setMsgErreurViewBean(vb, "ERREUR_RF_ATTESTATION_COHERANCE_DOCUMENT_SOIN");
                return false;
            }
        }
    }

    /*
     * on vérifie qu'il n'existe pas déjà un enregistrement pour le même couple type-sous type de soin, période et
     * dossier
     */
    protected boolean validerUnicite(Boolean isAjout, RFAttestationPiedDePageViewBean viewBean, BISession session)
            throws Exception {
        // manager qui renvoi les informations dossier, période attestation et
        // sts lié à l'attestation.
        RFAttestationJointDossierManager attestationManager = new RFAttestationJointDossierManager();
        attestationManager.setSession(viewBean.getSession());
        attestationManager.setDateDebut(viewBean.getDateDebut());
        attestationManager.setDateFin(viewBean.getDateFin());
        attestationManager.setForIdDossier(viewBean.getIdDossier());
        attestationManager.setForIdSousTypeSoin(viewBean.getIdSousTypeSoin());
        attestationManager.changeManagerSize(0);
        attestationManager.find();

        // résultats
        for (Iterator<RFAttestationJointDossier> it = attestationManager.iterator(); it.hasNext();) {

            RFAttestationJointDossier attestation = it.next();

            if (isAjout) {
                RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_SAISIE_PERIODE_EXISTE_DEJA");
                return false;
            } else {
                if (!attestation.getIdAttestation().equals(viewBean.getIdAttestation())) {
                    RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_SAISIE_PERIODE_EXISTE_DEJA");
                    viewBean.setIsUpdate(Boolean.TRUE);
                    return false;
                }
            }
        }
        return true;
    }
}
