package globaz.cygnus.services;

import globaz.cygnus.api.demandes.IRFDemande;
import globaz.cygnus.db.demandes.RFAssDemandeDev19Ftd15;
import globaz.cygnus.db.demandes.RFCorrectionDemande;
import globaz.cygnus.db.demandes.RFCorrectionDemandeManager;
import globaz.cygnus.db.demandes.RFDemande;
import globaz.cygnus.db.demandes.RFDemandeDev19;
import globaz.cygnus.db.demandes.RFDemandeFra16;
import globaz.cygnus.db.demandes.RFDemandeFrq17Fra18;
import globaz.cygnus.db.demandes.RFDemandeFtd15;
import globaz.cygnus.db.demandes.RFDemandeMai13;
import globaz.cygnus.db.demandes.RFDemandeMoy5_6_7;
import globaz.cygnus.db.motifsDeRefus.RFAssMotifsRefusDemande;
import globaz.cygnus.utils.RFUtils;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 
 * @author FHA
 * 
 */
public class RFCorrectionDemandeService {

    static private String listIdDevis = "";

    static private Map<String, String> montantsMotifsRefus = null;

    /*
     * recherche de la derniere correction de la demande en paramètre renvoi l'idDemande de la demande qui correspond à
     * la derniere correction
     */
    static private String chercheDerniereCorrection(String idDemande, BSession session) throws Exception {

        RFCorrectionDemandeManager correctionDemandeManager = new RFCorrectionDemandeManager();
        correctionDemandeManager.setSession(session);
        correctionDemandeManager.setForIdDemande(idDemande);
        correctionDemandeManager.changeManagerSize(0);
        correctionDemandeManager.find();

        RFCorrectionDemande correctionDemande = ((RFCorrectionDemande) correctionDemandeManager.getFirstEntity());

        // si il a un fils non payé non annulé on ne peut pas faire de correction!
        // TODO ETAT ANNULE !!
        if ((correctionDemandeManager.getSize() == 1)) {
            if (IRFDemande.PAYE.equals(correctionDemande.getCsEtat())
                    || IRFDemande.ANNULE.equals(correctionDemande.getCsEtat())) {
                // on fait pareil sur le fils
                return RFCorrectionDemandeService.chercheDerniereCorrection(correctionDemande.getIdDemande(), session);
            } else {
                return "-1";
            }
        } else { // pas de fils : on renvoi l'idDemande
            return idDemande;
        }
    }

    static public void correctionDemande(BSession session, String idDemande, String codeTypeDeSoin) throws Exception {

        // Date d1 = new Date();

        // quelle est la dernière demande corrigé
        idDemande = RFCorrectionDemandeService.chercheDerniereCorrection(idDemande, session);

        if (!"-1".equals(idDemande)) {
            // Date d2 = new Date();
            // System.out.println("Durée pour le tout: " + (d2.getTime() - d1.getTime()) + "\n");

            BITransaction transaction = null;
            try {
                transaction = (session.newTransaction());
                transaction.openTransaction();

                // TODO on ne peut pas corriger le type de soin donc on peut garder ce code
                Integer CodeTypeDeSoin = new Integer(codeTypeDeSoin);

                // set de la demande
                RFDemande rfDemande = new RFDemande();
                rfDemande.setIdDemande(idDemande);

                rfDemande.setSession(session);

                rfDemande.retrieve();

                RFDemande rfDemandeAjout = new RFDemande();
                if (!rfDemande.isNew()) {
                    RFCorrectionDemandeService.setNouvelleDemande(rfDemande, rfDemandeAjout, session);
                    rfDemandeAjout.add(transaction);
                } else {
                    throw new Exception();
                }

                // rfDemande.add(transaction);

                if ((CodeTypeDeSoin.intValue() == RFUtils.CODE_TYPE_DE_SOIN_MOYENS_AUXILIAIRES_5)
                        || (CodeTypeDeSoin.intValue() == RFUtils.CODE_TYPE_DE_SOIN_MOYENS_AUXILIAIRES_6)
                        || (CodeTypeDeSoin.intValue() == RFUtils.CODE_TYPE_DE_SOIN_MOYENS_AUXILIAIRES_7)) {

                    RFDemandeMoy5_6_7 rfDemandeMoy5 = new RFDemandeMoy5_6_7();
                    rfDemandeMoy5.setSession(session);
                    rfDemandeMoy5.setIdDemandeMoyensAux(idDemande);
                    rfDemandeMoy5.retrieve();

                    if (!rfDemandeMoy5.isNew()) {
                        RFDemandeMoy5_6_7 rfDemandeMoy5Ajout = new RFDemandeMoy5_6_7();
                        RFCorrectionDemandeService.setNouvelleDemandeMoyAux(rfDemandeMoy5, rfDemandeMoy5Ajout, session);
                        rfDemandeMoy5Ajout.setIdDemandeMoyensAux(rfDemandeAjout.getIdDemande());
                        rfDemandeMoy5Ajout.add(transaction);
                    } else {
                        throw new Exception();
                    }

                } else if (CodeTypeDeSoin.intValue() == RFUtils.CODE_TYPE_DE_SOIN_MAINTIEN_A_DOMICILE) {

                    RFDemandeMai13 rfDemandeMai13 = new RFDemandeMai13();
                    rfDemandeMai13.setSession(session);
                    rfDemandeMai13.setIdDemandeMaintienDom13(idDemande);
                    rfDemandeMai13.retrieve();

                    if (!rfDemandeMai13.isNew()) {
                        RFDemandeMai13 rfDemandeMai13Ajout = new RFDemandeMai13();
                        RFCorrectionDemandeService.setViewBeanDemandeMail13Properties(rfDemandeMai13,
                                rfDemandeMai13Ajout, session);
                        rfDemandeMai13Ajout.setIdDemandeMaintienDom13(rfDemandeAjout.getIdDemande());
                        rfDemandeMai13Ajout.add(transaction);
                    } else {
                        throw new Exception();
                    }

                } else if ((CodeTypeDeSoin.intValue() == RFUtils.CODE_TYPE_DE_SOIN_FRQP)
                        || (CodeTypeDeSoin.intValue() == RFUtils.CODE_TYPE_DE_SOIN_FRAIS_REFUSES)) {

                    RFDemandeFrq17Fra18 rfDemandeFrq17Fra18 = new RFDemandeFrq17Fra18();
                    rfDemandeFrq17Fra18.setSession(session);
                    rfDemandeFrq17Fra18.setIdDemande1718(idDemande);
                    rfDemandeFrq17Fra18.retrieve();

                    if (!rfDemandeFrq17Fra18.isNew()) {
                        RFDemandeFrq17Fra18 rfDemandeFrq17Fra18Ajout = new RFDemandeFrq17Fra18();
                        RFCorrectionDemandeService.setViewBeanDemandeFrq17Fra18Properties(rfDemandeFrq17Fra18,
                                rfDemandeFrq17Fra18Ajout, session);
                        rfDemandeFrq17Fra18Ajout.setIdDemande1718(rfDemandeAjout.getIdDemande());
                        rfDemandeFrq17Fra18Ajout.add(transaction);
                    } else {
                        throw new Exception();
                    }

                } else if (CodeTypeDeSoin.intValue() == RFUtils.CODE_TYPE_DE_SOIN_DEVIS_DENTAIRE) {

                    RFDemandeDev19 rfDemandeDev19 = new RFDemandeDev19();
                    rfDemandeDev19.setSession(session);
                    rfDemandeDev19.setIdDemandeDevis19(idDemande);
                    rfDemandeDev19.retrieve();

                    if (!rfDemandeDev19.isNew()) {
                        RFDemandeDev19 rfDemandeDev19Ajout = new RFDemandeDev19();
                        RFCorrectionDemandeService.setViewBeanDemandeDev19Properties(rfDemandeDev19,
                                rfDemandeDev19Ajout, session);
                        rfDemandeDev19Ajout.setIdDemandeDevis19(rfDemandeAjout.getIdDemande());
                        rfDemandeDev19Ajout.add(transaction);
                    } else {
                        throw new Exception();
                    }

                } else if (CodeTypeDeSoin.intValue() == RFUtils.CODE_TYPE_DE_SOIN_FRAIS_DENTAIRE) {

                    RFDemandeFtd15 rfDemandeFtd15 = new RFDemandeFtd15();
                    rfDemandeFtd15.setSession(session);
                    rfDemandeFtd15.setIdDemandeFtd15(idDemande);
                    rfDemandeFtd15.retrieve();

                    if (!rfDemandeFtd15.isNew()) {
                        RFDemandeFtd15 rfDemandeFtd15Ajout = new RFDemandeFtd15();
                        RFCorrectionDemandeService.setNouvelleDemande(rfDemandeFtd15, rfDemandeFtd15Ajout, session);
                        rfDemandeFtd15Ajout.setIdDemandeFtd15(rfDemandeAjout.getIdDemande());
                        rfDemandeFtd15Ajout.add(transaction);

                        // Sauvegarde des devis
                        String[] idDevisTab = RFCorrectionDemandeService.getListIdDevis().split(",");

                        for (int i = 0; i < idDevisTab.length; i++) {
                            if (!JadeStringUtil.isBlankOrZero(idDevisTab[i])) {

                                RFAssDemandeDev19Ftd15 rfDemDev19JointFtd15 = new RFAssDemandeDev19Ftd15();
                                rfDemDev19JointFtd15.setSession(session);
                                rfDemDev19JointFtd15.setIdDemandeDevis19(idDevisTab[i]);
                                rfDemDev19JointFtd15.setIdDemandeFtd15(rfDemande.getIdDemande());

                                rfDemDev19JointFtd15.add(transaction);

                            }
                        }
                    } else {
                        throw new Exception();
                    }

                } else if (CodeTypeDeSoin.intValue() == RFUtils.CODE_TYPE_DE_SOIN_TRANSPORT_16) {

                    RFDemandeFra16 rfDemandeFra16 = new RFDemandeFra16();
                    rfDemandeFra16.setSession(session);
                    rfDemandeFra16.setIdDemandeFra16(idDemande);
                    rfDemandeFra16.retrieve();

                    if (!rfDemandeFra16.isNew()) {
                        RFDemandeFra16 rfDemandeFra16Ajout = new RFDemandeFra16();
                        RFCorrectionDemandeService
                                .setNouvelleDemandeFra16(rfDemandeFra16, rfDemandeFra16Ajout, session);
                        rfDemandeFra16Ajout.setIdDemandeFra16(rfDemandeAjout.getIdDemande());
                        rfDemandeFra16Ajout.add(transaction);

                    } else {
                        throw new Exception();
                    }

                }
                // Enregistrement des motifs de refus

                Iterator<Map.Entry<String, String>> montantMotifsRefusKeysIter = RFCorrectionDemandeService
                        .getMontantsMotifsRefus().entrySet().iterator();

                while (montantMotifsRefusKeysIter.hasNext()) {
                    Map.Entry<String, String> montantMotifsRefusKeys = montantMotifsRefusKeysIter.next();
                    RFAssMotifsRefusDemande rfAssMotifsDeRefusDem = new RFAssMotifsRefusDemande();
                    rfAssMotifsDeRefusDem.setSession(session);
                    rfAssMotifsDeRefusDem.setIdDemande(rfDemande.getIdDemande());
                    rfAssMotifsDeRefusDem.setIdMotifsRefus(montantMotifsRefusKeys.getKey());
                    if (!JadeStringUtil.isEmpty(montantMotifsRefusKeys.getValue())) {
                        if (!JadeStringUtil.isEmpty(montantMotifsRefusKeys.getValue())) {
                            rfAssMotifsDeRefusDem.setMntMotifsDeRefus(montantMotifsRefusKeys.getValue());
                        }
                    }

                    rfAssMotifsDeRefusDem.add(transaction);
                }

                // mise à jour de la demande qui a maintenant un fils
                if (!rfDemande.isNew()) {
                    rfDemande.update(transaction);
                } else {
                    throw new Exception();
                }

            } catch (Exception e) {
                if (transaction != null) {
                    transaction.setRollbackOnly();
                }
                throw e;
            } finally {
                if (transaction != null) {
                    try {
                        if (transaction.hasErrors() || transaction.isRollbackOnly()) {
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

    static public String getListIdDevis() {
        return RFCorrectionDemandeService.listIdDevis;
    }

    static public Map<String, String> getMontantsMotifsRefus() {
        if (null == RFCorrectionDemandeService.montantsMotifsRefus) {
            RFCorrectionDemandeService.montantsMotifsRefus = new HashMap<String, String>();
        }
        return RFCorrectionDemandeService.montantsMotifsRefus;
    }

    static private void setNouvelleDemande(RFDemande demande, RFDemande rfDemandeAjout, BSession session) {

        // rfDemandeAjout.setIdDemande(demande.getIdDemande());

        rfDemandeAjout.setDateReception(demande.getDateReception());
        rfDemandeAjout.setDateFacture(demande.getDateFacture());
        rfDemandeAjout.setDateDebutTraitement(demande.getDateDebutTraitement());
        rfDemandeAjout.setDateFinTraitement(demande.getDateFinTraitement());
        rfDemandeAjout.setDateImputation(demande.getDateImputation());
        rfDemandeAjout.setNumeroFacture(demande.getNumeroFacture());
        rfDemandeAjout.setMontantFacture(demande.getMontantFacture());
        rfDemandeAjout.setMontantAPayer(demande.getMontantAPayer());
        rfDemandeAjout.setIsForcerPaiement(demande.getIsForcerPaiement());
        rfDemandeAjout.setIsContratDeTravail(demande.getIsContratDeTravail());
        // rfDemandeAjout.setIsPP(demande.getIsPP());
        rfDemandeAjout.setCsEtat(IRFDemande.ENREGISTRE);
        rfDemandeAjout.setCsSource(demande.getCsSource());
        rfDemandeAjout.setCsStatut(demande.getCsStatut());
        rfDemandeAjout.setIdFournisseur(demande.getIdFournisseur());
        rfDemandeAjout.setRemarqueFournisseur(demande.getRemarqueFournisseur());
        // rfDemandeAjout.setIdGestionnaire(demande.getIdGestionnaire());
        rfDemandeAjout.setIdGestionnaire(session.getUserId());

        rfDemandeAjout.setIdDossier(demande.getIdDossier());
        rfDemandeAjout.setIdAdressePaiement(demande.getIdAdressePaiement());
        rfDemandeAjout.setIdSousTypeDeSoin(demande.getIdSousTypeDeSoin());
        // rfDemandeAjout.setIdQdPrincipale(demande.getIdQdPrincipale());
        // rfDemandeAjout.setIdDecision(demande.getIdDecision());
        rfDemandeAjout.setSpy(demande.getSpy());
        rfDemandeAjout.setCreationSpy(demande.getCreationSpy());

        rfDemandeAjout.setMontantMensuel(demande.getMontantMensuel());
        rfDemandeAjout.setIdDemandeParent(demande.getIdDemande());

    }

    static private void setNouvelleDemandeFra16(RFDemandeFra16 demande, RFDemandeFra16 rfDemandeAjout, BSession session) {

        RFCorrectionDemandeService.setNouvelleDemande(demande, rfDemandeAjout, session);

        rfDemandeAjout.setNombreKilometres(demande.getNombreKilometres());
        rfDemandeAjout.setTva(demande.getIsTva());
        rfDemandeAjout.setPrixKilometre(demande.getPrixKilometre());
        rfDemandeAjout.setCsTypeVhc(demande.getCsTypeVhc());
    }

    static private void setNouvelleDemandeMoyAux(RFDemandeMoy5_6_7 demande, RFDemandeMoy5_6_7 rfDemandeAjout,
            BSession session) {

        RFCorrectionDemandeService.setNouvelleDemande(demande, rfDemandeAjout, session);

        // rfDemandeAjout.setIdDemandeMoyensAux(demande.getIdDemandeMoyensAux());

        rfDemandeAjout.setDateDecisionOAI(demande.getDateDecisionOAI());
        rfDemandeAjout.setMontantVerseOAI(demande.getMontantVerseOAI());
        rfDemandeAjout.setMontantFacture44(demande.getMontantFacture44());

    }

    static private void setViewBeanDemandeDev19Properties(RFDemandeDev19 demande, RFDemandeDev19 rfDemandeAjout,
            BSession session) {

        RFCorrectionDemandeService.setNouvelleDemande(demande, rfDemandeAjout, session);

        // rfDemandeAjout.setIdDemandeDevis19(demande.getIdDemande());

        rfDemandeAjout.setDateEnvoiMDT(demande.getDateEnvoiMDT());
        rfDemandeAjout.setDateEnvoiMDC(demande.getDateEnvoiMDC());
        rfDemandeAjout.setDateReceptionPreavis(demande.getDateReceptionPreavis());
        rfDemandeAjout.setMontantAcceptation(demande.getMontantAcceptation());
        rfDemandeAjout.setDateEnvoiAcceptation(demande.getDateEnvoiAcceptation());

    }

    static private void setViewBeanDemandeFrq17Fra18Properties(RFDemandeFrq17Fra18 demande,
            RFDemandeFrq17Fra18 rfDemandeAjout, BSession session) {

        RFCorrectionDemandeService.setNouvelleDemande(demande, rfDemandeAjout, session);

        // rfDemandeAjout.setIdDemande1718(demande.getIdDemande());

        rfDemandeAjout.setDateDecompte(demande.getDateDecompte());
        rfDemandeAjout.setMontantDecompte(demande.getMontantDecompte());
        rfDemandeAjout.setNumeroDecompte(demande.getNumeroDecompte());
        rfDemandeAjout.setCsGenreDeSoin(demande.getCsGenreDeSoin());

    }

    static private void setViewBeanDemandeMail13Properties(RFDemandeMai13 demande, RFDemandeMai13 rfDemandeAjout,
            BSession session) {

        RFCorrectionDemandeService.setNouvelleDemande(demande, rfDemandeAjout, session);

        // rfDemandeAjout.setIdDemandeMaintienDom13(demande.getIdDemandeMaintienDom13());

        rfDemandeAjout.setNombreHeure(demande.getNombreHeure());

    }

}
