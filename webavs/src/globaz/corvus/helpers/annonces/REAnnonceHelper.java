/*
 * Créé le 20 août 07
 */
package globaz.corvus.helpers.annonces;

import globaz.corvus.anakin.REAnakinParser;
import globaz.corvus.api.annonces.IREAnnonces;
import globaz.corvus.db.annonces.REAnnonceRente;
import globaz.corvus.db.annonces.REAnnonceRenteManager;
import globaz.corvus.db.annonces.REAnnoncesAbstractLevel1A;
import globaz.corvus.db.annonces.REAnnoncesAugmentationModification10Eme;
import globaz.corvus.db.annonces.REAnnoncesAugmentationModification9Eme;
import globaz.corvus.db.annonces.REAnnoncesDiminution10Eme;
import globaz.corvus.db.annonces.REAnnoncesDiminution9Eme;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.db.rentesaccordees.RERenteAccordeeManager;
import globaz.corvus.tools.REArcFrenchValidator;
import globaz.corvus.vb.annonces.REAnnoncesAugmentationModification10EmeViewBean;
import globaz.corvus.vb.annonces.REAnnoncesAugmentationModification9EmeViewBean;
import globaz.corvus.vb.annonces.REAnnoncesDiminution10EmeViewBean;
import globaz.corvus.vb.annonces.REAnnoncesDiminution9EmeViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.helpers.PRAbstractHelper;
import java.util.Enumeration;
import java.util.Iterator;
import ch.admin.ofit.anakin.donnee.AnnonceErreur;

/**
 * @author HPE
 */
public class REAnnonceHelper extends PRAbstractHelper {

    public FWViewBeanInterface actionAjouterAnnonce(FWViewBeanInterface viewBean, FWAction action, BSession session)
            throws Exception {

        BITransaction transaction = null;

        try {
            transaction = (session).newTransaction();
            transaction.openTransaction();

            // on cherche l'idTiers dans la rente accordée
            String idRenteAccordee = ((REAnnoncesAbstractLevel1A) viewBean).getIdRenteAccordee();
            String idTiers = "";

            RERenteAccordeeManager raManager = new RERenteAccordeeManager();
            raManager.setSession(session);
            raManager.setForIdRenteAccordee(idRenteAccordee);
            raManager.find(transaction);

            RERenteAccordee ra = (RERenteAccordee) raManager.getFirstEntity();
            if (ra != null) {
                idTiers = ra.getIdTiersBeneficiaire();
            }

            if (viewBean instanceof REAnnoncesDiminution10EmeViewBean) {

                REAnnoncesDiminution10EmeViewBean vb = (REAnnoncesDiminution10EmeViewBean) viewBean;

                // pour la mise à jour des classes parentes, il ne faut pas utiliser le viewBean
                REAnnoncesDiminution10Eme b = new REAnnoncesDiminution10Eme();
                b.setSession(session);

                b.setCantonEtatDomicile(vb.getCantonEtatDomicile());
                b.setCodeApplication(vb.getCodeApplication());
                b.setCodeEnregistrement01(vb.getCodeEnregistrement01());
                b.setCodeMutation(vb.getCodeMutation());
                b.setDebutDroit(vb.getDebutDroit());
                b.setEtatCivil(vb.getEtatCivil());
                b.setFinDroit(vb.getFinDroit());
                b.setGenrePrestation(vb.getGenrePrestation());
                b.setIsRefugie(vb.getIsRefugie());
                // si code 46, on ne remplit pas le champ des mensualités avec des zéros
                // correction bug 5171
                if ("46".equalsIgnoreCase(vb.getCodeApplication())) {
                    b.setMensualitePrestationsFrancs(vb.getMensualitePrestationsFrancs());
                } else {
                    b.setMensualitePrestationsFrancs(JadeStringUtil.fillWithZeroes(vb.getMensualitePrestationsFrancs(),
                            5));
                }
                b.setMoisRapport(vb.getMoisRapport());
                b.setNoAssAyantDroit(vb.getNoAssAyantDroit());
                b.setNouveauNumeroAssureAyantDroit(vb.getNouveauNumeroAssureAyantDroit());
                b.setNumeroAgence(vb.getNumeroAgence());
                b.setNumeroAnnonce(vb.getNumeroAnnonce());
                b.setNumeroCaisse(vb.getNumeroCaisse());
                b.setPremierNoAssComplementaire(vb.getPremierNoAssComplementaire());
                b.setReferenceCaisseInterne(vb.getReferenceCaisseInterne().toUpperCase());
                b.setSecondNoAssComplementaire(vb.getSecondNoAssComplementaire());

                b.setEtat(IREAnnonces.CS_ETAT_OUVERT);

                // id tiers bénéficiaire rente accordée
                b.setIdTiers(idTiers);

                Enumeration<AnnonceErreur> erreurs = REAnakinParser.getInstance().parse(session, b, null,
                        vb.getMoisRapport());
                StringBuffer buff = new StringBuffer();
                while ((erreurs != null) && erreurs.hasMoreElements()) {
                    AnnonceErreur erreur = erreurs.nextElement();
                    buff.append(erreur.getMessage()).append("\n");
                }

                if (buff.length() > 0) {
                    viewBean.setMessage(buff.toString());
                    viewBean.setMsgType(FWViewBeanInterface.ERROR);
                    throw new Exception(buff.toString());
                }

                b.add(transaction);

                // création de l'annonce de rente
                REAnnonceRente ar = new REAnnonceRente();
                ar.setSession(session);
                ar.setIdAnnonceHeader(b.getIdAnnonce());
                ar.setCsEtat(IREAnnonces.CS_ETAT_OUVERT);
                ar.setCsTraitement(IREAnnonces.CS_CODE_EN_COURS);
                ar.setIdRenteAccordee(vb.getIdRenteAccordee());

                ar.add(transaction);

            } else if (viewBean instanceof REAnnoncesDiminution9EmeViewBean) {

                REAnnoncesDiminution9EmeViewBean vb = (REAnnoncesDiminution9EmeViewBean) viewBean;

                // pour la mise à jour des classes parentes, il ne faut pas utiliser le viewBean
                REAnnoncesDiminution9Eme b = new REAnnoncesDiminution9Eme();
                b.setSession(session);

                b.setCantonEtatDomicile(vb.getCantonEtatDomicile());
                b.setCodeApplication(vb.getCodeApplication());
                b.setCodeEnregistrement01(vb.getCodeEnregistrement01());
                b.setCodeMutation(vb.getCodeMutation());
                b.setCodeTraitement(vb.getCodeTraitement());
                b.setDateLiquidation(vb.getDateLiquidation());
                b.setDebutDroit(vb.getDebutDroit());
                b.setEtatCivil(vb.getEtatCivil());
                b.setFinDroit(vb.getFinDroit());
                b.setGenrePrestation(vb.getGenrePrestation());
                b.setIsRefugie(vb.getIsRefugie());
                // si code 46, on ne remplit pas le champ des mensualités avec des zéros
                // correction bug 5171
                if ("46".equalsIgnoreCase(vb.getCodeApplication())) {
                    b.setMensualitePrestationsFrancs(vb.getMensualitePrestationsFrancs());
                } else {
                    b.setMensualitePrestationsFrancs(JadeStringUtil.fillWithZeroes(vb.getMensualitePrestationsFrancs(),
                            5));
                }
                b.setMensualiteRenteOrdinaire(vb.getMensualiteRenteOrdinaire());
                b.setMoisRapport(vb.getMoisRapport());
                b.setNoAssAyantDroit(vb.getNoAssAyantDroit());
                b.setNouveauNumeroAssureAyantDroit(vb.getNouveauNumeroAssureAyantDroit());
                b.setNumeroAgence(vb.getNumeroAgence());
                b.setNumeroAnnonce(vb.getNumeroAnnonce());
                b.setNumeroCaisse(vb.getNumeroCaisse());
                b.setPremierNoAssComplementaire(vb.getPremierNoAssComplementaire());
                b.setReferenceCaisseInterne(vb.getReferenceCaisseInterne().toUpperCase());
                b.setSecondNoAssComplementaire(vb.getSecondNoAssComplementaire());

                b.setEtat(IREAnnonces.CS_ETAT_OUVERT);

                // id tiers bénéficiaire rente accordée
                b.setIdTiers(idTiers);

                Enumeration<AnnonceErreur> erreurs = REAnakinParser.getInstance().parse(session, b, null,
                        vb.getMoisRapport());
                StringBuffer buff = new StringBuffer();
                while ((erreurs != null) && erreurs.hasMoreElements()) {
                    AnnonceErreur erreur = erreurs.nextElement();
                    buff.append(erreur.getMessage()).append("\n");
                }

                if (buff.length() > 0) {
                    viewBean.setMessage(buff.toString());
                    viewBean.setMsgType(FWViewBeanInterface.ERROR);
                    throw new Exception(buff.toString());
                }
                b.add(transaction);

                // création de l'annonce de rente
                REAnnonceRente ar = new REAnnonceRente();
                ar.setSession(session);
                ar.setIdAnnonceHeader(b.getIdAnnonce());
                ar.setCsEtat(IREAnnonces.CS_ETAT_OUVERT);
                ar.setCsTraitement(IREAnnonces.CS_CODE_EN_COURS);
                ar.setIdRenteAccordee(vb.getIdRenteAccordee());
                ar.add(transaction);

            } else if (viewBean instanceof REAnnoncesAugmentationModification10EmeViewBean) {

                REAnnoncesAugmentationModification10EmeViewBean vb = (REAnnoncesAugmentationModification10EmeViewBean) viewBean;

                // création de l'enregistrement 02
                REAnnoncesAugmentationModification10Eme enregistrement02 = vb.getCodeEnregistrement02();
                enregistrement02.setSession(session);

                enregistrement02.setAgeDebutInvalidite(vb.getAgeDebutInvalidite());
                enregistrement02.setAnneeCotClasseAge(vb.getAnneeCotClasseAge());
                enregistrement02.setAnneeNiveau(vb.getAnneeNiveau());
                enregistrement02.setCasSpecial1(vb.getCasSpecial1());
                enregistrement02.setCasSpecial2(vb.getCasSpecial2());
                enregistrement02.setCasSpecial3(vb.getCasSpecial3());
                enregistrement02.setCasSpecial4(vb.getCasSpecial4());
                enregistrement02.setCasSpecial5(vb.getCasSpecial5());
                enregistrement02.setCodeApplication(vb.getCodeApplication());
                enregistrement02.setCodeEnregistrement01(vb.getCodeEnr02CodeEnregistrement01());
                enregistrement02.setCodeInfirmite(vb.getCodeInfirmite());
                enregistrement02.setCodeRevenuSplitte(vb.getCodeRevenuSplitte());
                enregistrement02.setDateDebutAnticipation(vb.getDateDebutAnticipation());
                enregistrement02.setDateRevocationAjournement(vb.getDateRevocationAjournement());
                enregistrement02.setDegreInvalidite(vb.getDegreInvalidite());
                enregistrement02.setDureeAjournement(vb.getDureeAjournement());
                enregistrement02.setDureeCoEchelleRenteAv73(vb.getDureeCoEchelleRenteAv73());
                enregistrement02.setDureeCoEchelleRenteDes73(vb.getDureeCoEchelleRenteDes73());
                enregistrement02.setDureeCotManquante48_72(vb.getDureeCotManquante48_72());
                enregistrement02.setDureeCotManquante73_78(vb.getDureeCotManquante73_78());
                enregistrement02.setDureeCotPourDetRAM(vb.getDureeCotPourDetRAM());
                enregistrement02.setEchelleRente(vb.getEchelleRente());
                enregistrement02.setGenreDroitAPI(vb.getGenreDroitAPI());
                enregistrement02.setNbreAnneeAnticipation(vb.getNbreAnneeAnticipation());
                enregistrement02.setNbreAnneeBonifTrans(vb.getNbreAnneeBonifTrans());
                enregistrement02.setNbreAnneeBTA(vb.getNbreAnneeBTA());
                enregistrement02.setNombreAnneeBTE(vb.getNombreAnneeBTE());
                enregistrement02.setOfficeAICompetent(vb.getOfficeAICompetent());
                enregistrement02.setRamDeterminant(vb.getRamDeterminant());
                enregistrement02.setReduction(vb.getReduction());
                enregistrement02.setReductionAnticipation(vb.getReductionAnticipation());
                enregistrement02.setSupplementAjournement(vb.getSupplementAjournement());
                enregistrement02.setSurvenanceEvenAssure(vb.getSurvenanceEvenAssure());

                enregistrement02.setEtat(IREAnnonces.CS_ETAT_OUVERT);

                // id tiers bénéficiaire rente accordée
                enregistrement02.setIdTiers(idTiers);

                enregistrement02.add(transaction);

                // création de l'enregistrement 03
                REAnnoncesAugmentationModification10Eme enregistrement03 = vb.getCodeEnregistrement03();
                if (!enregistrement03.isNew()) {
                    // pas utilise pour le moment
                }

                // creation de l'enregistrement 01
                // pour la mise à jour des classes parentes, il ne faut pas utiliser le viewBean
                REAnnoncesAugmentationModification10Eme b = new REAnnoncesAugmentationModification10Eme();
                b.setSession(session);

                b.setCantonEtatDomicile(vb.getCantonEtatDomicile());
                b.setCodeApplication(vb.getCodeApplication());
                b.setCodeEnregistrement01(vb.getCodeEnregistrement01());
                b.setCodeMutation(vb.getCodeMutation());
                b.setDebutDroit(vb.getDebutDroit());
                b.setEtatCivil(vb.getEtatCivil());
                b.setFinDroit(vb.getFinDroit());
                b.setGenrePrestation(vb.getGenrePrestation());
                b.setIsRefugie(vb.getIsRefugie());
                // si code 46, on ne remplit pas le champ des mensualités avec des zéros
                // correction bug 5171
                if ("46".equalsIgnoreCase(vb.getCodeApplication())) {
                    b.setMensualitePrestationsFrancs(vb.getMensualitePrestationsFrancs());
                } else {
                    b.setMensualitePrestationsFrancs(JadeStringUtil.fillWithZeroes(vb.getMensualitePrestationsFrancs(),
                            5));
                }
                b.setMoisRapport(vb.getMoisRapport());
                b.setNoAssAyantDroit(vb.getNoAssAyantDroit());
                b.setNouveauNoAssureAyantDroit(vb.getNouveauNoAssureAyantDroit());
                b.setNumeroAgence(vb.getNumeroAgence());
                b.setNumeroAnnonce(vb.getNumeroAnnonce());
                b.setNumeroCaisse(vb.getNumeroCaisse());
                b.setPremierNoAssComplementaire(vb.getPremierNoAssComplementaire());
                b.setReferenceCaisseInterne(vb.getReferenceCaisseInterne().toUpperCase());
                b.setSecondNoAssComplementaire(vb.getSecondNoAssComplementaire());
                b.setEtat(IREAnnonces.CS_ETAT_OUVERT);

                // id tiers bénéficiaire rente accordée
                b.setIdTiers(idTiers);

                // le liens de l'enregistrement 01 -> enregistrement 02
                b.setIdLienAnnonce(enregistrement02.getIdAnnonce());

                Enumeration<AnnonceErreur> erreurs = REAnakinParser.getInstance().parse(session, b, enregistrement02,
                        vb.getMoisRapport());
                StringBuffer buff = new StringBuffer();
                while ((erreurs != null) && erreurs.hasMoreElements()) {
                    AnnonceErreur erreur = erreurs.nextElement();
                    buff.append(erreur.getMessage()).append("\n");
                }

                if (buff.length() > 0) {
                    viewBean.setMessage(buff.toString());
                    viewBean.setMsgType(FWViewBeanInterface.ERROR);
                    throw new Exception(buff.toString());
                }

                b.add(transaction);

                // création de l'annonce de rente
                REAnnonceRente ar = new REAnnonceRente();
                ar.setSession(session);
                ar.setIdAnnonceHeader(b.getIdAnnonce());
                ar.setCsEtat(IREAnnonces.CS_ETAT_OUVERT);
                ar.setCsTraitement(IREAnnonces.CS_CODE_EN_COURS);
                ar.setIdRenteAccordee(vb.getIdRenteAccordee());

                ar.add(transaction);

            } else if (viewBean instanceof REAnnoncesAugmentationModification9EmeViewBean) {

                REAnnoncesAugmentationModification9EmeViewBean vb = (REAnnoncesAugmentationModification9EmeViewBean) viewBean;

                // update enregistrement 02
                REAnnoncesAugmentationModification9Eme enregistrement02 = vb.getCodeEnregistrement02();
                enregistrement02.setSession(session);

                enregistrement02.setAgeDebutInvalidite(vb.getAgeDebutInvalidite());
                enregistrement02.setAgeDebutInvaliditeEpouse(vb.getAgeDebutInvaliditeEpouse());
                enregistrement02.setAnneeCotClasseAge(vb.getAnneeCotClasseAge());
                enregistrement02.setAnneeNiveau(vb.getAnneeNiveau());
                enregistrement02.setBteMoyennePrisEnCompte(vb.getBteMoyennePrisEnCompte());
                enregistrement02.setCasSpecial1(vb.getCasSpecial1());
                enregistrement02.setCasSpecial2(vb.getCasSpecial2());
                enregistrement02.setCasSpecial3(vb.getCasSpecial3());
                enregistrement02.setCasSpecial4(vb.getCasSpecial4());
                enregistrement02.setCasSpecial5(vb.getCasSpecial5());
                enregistrement02.setCodeApplication(vb.getCodeApplication());
                enregistrement02.setCodeEnregistrement01(vb.getCodeEnr02CodeEnregistrement01());
                enregistrement02.setCodeInfirmite(vb.getCodeInfirmite());
                enregistrement02.setCodeInfirmiteEpouse(vb.getCodeInfirmiteEpouse());
                enregistrement02.setDateLiquidation(vb.getDateLiquidation());
                enregistrement02.setDateRevocationAjournement(vb.getDateRevocationAjournement());
                enregistrement02.setDegreInvalidite(vb.getDegreInvalidite());
                enregistrement02.setDegreInvaliditeEpouse(vb.getDegreInvaliditeEpouse());
                enregistrement02.setDureeAjournement(vb.getDureeAjournement());
                enregistrement02.setDureeCoEchelleRenteAv73(vb.getDureeCoEchelleRenteAv73());
                enregistrement02.setDureeCoEchelleRenteDes73(vb.getDureeCoEchelleRenteDes73());
                enregistrement02.setDureeCotManquante48_72(vb.getDureeCotManquante48_72());
                enregistrement02.setDureeCotManquante73_78(vb.getDureeCotManquante73_78());
                enregistrement02.setDureeCotPourDetRAM(vb.getDureeCotPourDetRAM());
                enregistrement02.setEchelleRente(vb.getEchelleRente());
                enregistrement02.setGenreDroitAPI(vb.getGenreDroitAPI());
                enregistrement02.setIsLimiteRevenu(vb.getIsLimiteRevenu());
                enregistrement02.setIsMinimumGaranti(vb.getIsMinimumGaranti());
                enregistrement02.setNombreAnneeBTE(vb.getNombreAnneeBTE());

                // Les codes OAI 9ème doivent être sur 2 positions
                if (!JadeStringUtil.isBlankOrZero(vb.getOfficeAICompetent())) {
                    String s = vb.getOfficeAICompetent();
                    if (s.length() == 3) {
                        s = s.substring(1, 3);
                    }
                    enregistrement02.setOfficeAICompetent(s);
                }

                if (!JadeStringUtil.isBlankOrZero(vb.getOfficeAiCompEpouse())) {
                    String s = vb.getOfficeAiCompEpouse();
                    if (s.length() == 3) {
                        s = s.substring(1, 3);
                    }
                    enregistrement02.setOfficeAiCompEpouse(s);
                }

                enregistrement02.setOfficeAICompetent(vb.getOfficeAICompetent());
                enregistrement02.setRamDeterminant(vb.getRamDeterminant());
                enregistrement02.setReduction(vb.getReduction());
                enregistrement02.setRevenuAnnuelMoyenSansBTE(vb.getRevenuAnnuelMoyenSansBTE());
                enregistrement02.setRevenuPrisEnCompte(vb.getRevenuPrisEnCompte());
                enregistrement02.setSupplementAjournement(vb.getSupplementAjournement());
                enregistrement02.setSurvenanceEvenAssure(vb.getSurvenanceEvenAssure());
                enregistrement02.setSurvenanceEvtAssureEpouse(vb.getSurvenanceEvtAssureEpouse());

                enregistrement02.setEtat(IREAnnonces.CS_ETAT_OUVERT);

                // id tiers bénéficiaire rente accordée
                enregistrement02.setIdTiers(idTiers);

                if ("41".equals(vb.getCodeApplication())) {
                    REArcFrenchValidator frenchValidator = new REArcFrenchValidator();
                    enregistrement02 = frenchValidator.validateARC_41_02(enregistrement02);
                }

                enregistrement02.add(transaction);

                // création enregistrement 03
                REAnnoncesAugmentationModification9Eme enregistrement03 = vb.getCodeEnregistrement03();
                if (!enregistrement03.isNew()) {
                    // pas utilise pour le moment
                }

                // update de l'enregistrement 01
                // pour la mise à jour des classes parentes, il ne faut pas utiliser le viewBean
                REAnnoncesAugmentationModification9Eme b = new REAnnoncesAugmentationModification9Eme();
                b.setSession(session);

                b.setCantonEtatDomicile(vb.getCantonEtatDomicile());
                b.setCodeApplication(vb.getCodeApplication());
                b.setCodeEnregistrement01(vb.getCodeEnregistrement01());
                b.setCodeMutation(vb.getCodeMutation());
                b.setDebutDroit(vb.getDebutDroit());
                b.setEtatCivil(vb.getEtatCivil());
                b.setFinDroit(vb.getFinDroit());
                b.setGenrePrestation(vb.getGenrePrestation());
                b.setIsRefugie(vb.getIsRefugie());

                if ("43".equals(vb.getCodeApplication())) {
                    b.setMensualitePrestationsFrancs(vb.getMensualitePrestationsFrancs());
                }
                // 41
                else {
                    b.setMensualitePrestationsFrancs(JadeStringUtil.fillWithZeroes(vb.getMensualitePrestationsFrancs(),
                            5));
                }

                b.setMensualiteRenteOrdRemp(vb.getMensualiteRenteOrdRemp());
                b.setMoisRapport(vb.getMoisRapport());
                b.setNoAssAyantDroit(vb.getNoAssAyantDroit());
                b.setNouveauNoAssureAyantDroit(vb.getNouveauNoAssureAyantDroit());
                b.setNumeroAgence(vb.getNumeroAgence());
                b.setNumeroAnnonce(vb.getNumeroAnnonce());
                b.setNumeroCaisse(vb.getNumeroCaisse());
                b.setPremierNoAssComplementaire(vb.getPremierNoAssComplementaire());
                b.setReferenceCaisseInterne(vb.getReferenceCaisseInterne().toUpperCase());
                b.setSecondNoAssComplementaire(vb.getSecondNoAssComplementaire());
                b.setEtat(IREAnnonces.CS_ETAT_OUVERT);

                // id tiers bénéficiaire rente accordée
                b.setIdTiers(idTiers);

                // le liens de l'enregistrement 01 -> enregistrement 02
                b.setIdLienAnnonce(enregistrement02.getIdAnnonce());

                Enumeration<AnnonceErreur> erreurs = REAnakinParser.getInstance().parse(session, b, enregistrement02,
                        vb.getMoisRapport());
                StringBuffer buff = new StringBuffer();
                while ((erreurs != null) && erreurs.hasMoreElements()) {
                    AnnonceErreur erreur = erreurs.nextElement();
                    buff.append(erreur.getMessage()).append("\n");
                }

                if (buff.length() > 0) {
                    viewBean.setMessage(buff.toString());
                    viewBean.setMsgType(FWViewBeanInterface.ERROR);
                    throw new Exception(buff.toString());
                }

                b.add(transaction);

                // création de l'annonce de rente
                REAnnonceRente ar = new REAnnonceRente();
                ar.setSession(session);
                ar.setIdAnnonceHeader(b.getIdAnnonce());
                ar.setCsEtat(IREAnnonces.CS_ETAT_OUVERT);
                ar.setCsTraitement(IREAnnonces.CS_CODE_EN_COURS);
                ar.setIdRenteAccordee(vb.getIdRenteAccordee());

                ar.add(transaction);
            }

            return viewBean;

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

    /**
     * 
     * @param viewBean
     * @param action
     * @param session
     * @return
     * @throws Exception
     */
    public FWViewBeanInterface actionModifierAnnonce(FWViewBeanInterface viewBean, FWAction action, BSession session)
            throws Exception {

        BITransaction transaction = null;
        Enumeration<AnnonceErreur> erreurs = null;
        try {
            transaction = (session).newTransaction();
            transaction.openTransaction();

            if (viewBean instanceof REAnnoncesDiminution10EmeViewBean) {

                REAnnoncesDiminution10EmeViewBean vb = (REAnnoncesDiminution10EmeViewBean) viewBean;

                // pour la mise à jour des classes parentes, il ne faut pas utiliser le viewBean
                REAnnoncesDiminution10Eme b = new REAnnoncesDiminution10Eme();
                b.setSession(session);
                b.setIdAnnonce(vb.getIdAnnonce());
                b.retrieve(transaction);

                b.setCantonEtatDomicile(vb.getCantonEtatDomicile());
                b.setCodeApplication(vb.getCodeApplication());
                b.setCodeEnregistrement01(vb.getCodeEnregistrement01());
                b.setCodeMutation(vb.getCodeMutation());
                b.setDebutDroit(vb.getDebutDroit());
                b.setEtatCivil(vb.getEtatCivil());
                b.setFinDroit(vb.getFinDroit());
                b.setGenrePrestation(vb.getGenrePrestation());
                b.setIsRefugie(vb.getIsRefugie());
                // si code 46, on ne remplit pas le champ des mensualités avec des zéros
                // correction bug 5171
                if ("46".equalsIgnoreCase(vb.getCodeApplication())) {
                    b.setMensualitePrestationsFrancs(vb.getMensualitePrestationsFrancs());
                } else {
                    b.setMensualitePrestationsFrancs(JadeStringUtil.fillWithZeroes(vb.getMensualitePrestationsFrancs(),
                            5));
                }
                b.setMoisRapport(vb.getMoisRapport());
                b.setNoAssAyantDroit(vb.getNoAssAyantDroit());
                b.setNouveauNumeroAssureAyantDroit(vb.getNouveauNumeroAssureAyantDroit());
                b.setNumeroAgence(vb.getNumeroAgence());
                b.setNumeroAnnonce(vb.getNumeroAnnonce());
                b.setNumeroCaisse(vb.getNumeroCaisse());
                b.setPremierNoAssComplementaire(vb.getPremierNoAssComplementaire());
                b.setReferenceCaisseInterne(vb.getReferenceCaisseInterne().toUpperCase());
                b.setSecondNoAssComplementaire(vb.getSecondNoAssComplementaire());

                erreurs = REAnakinParser.getInstance().parse(session, b, null, vb.getMoisRapport());
                StringBuffer buff = new StringBuffer();
                while ((erreurs != null) && erreurs.hasMoreElements()) {
                    AnnonceErreur erreur = erreurs.nextElement();
                    buff.append(erreur.getMessage()).append("\n");
                }

                if (buff.length() > 0) {
                    viewBean.setMessage(buff.toString());
                    viewBean.setMsgType(FWViewBeanInterface.ERROR);
                    throw new Exception(buff.toString());
                }
                b.update(transaction);

            } else if (viewBean instanceof REAnnoncesDiminution9EmeViewBean) {

                REAnnoncesDiminution9EmeViewBean vb = (REAnnoncesDiminution9EmeViewBean) viewBean;

                // pour la mise à jour des classes parentes, il ne faut pas utiliser le viewBean
                REAnnoncesDiminution9Eme b = new REAnnoncesDiminution9Eme();
                b.setSession(session);
                b.setIdAnnonce(vb.getIdAnnonce());
                b.retrieve(transaction);

                b.setCantonEtatDomicile(vb.getCantonEtatDomicile());
                b.setCodeApplication(vb.getCodeApplication());
                b.setCodeEnregistrement01(vb.getCodeEnregistrement01());
                b.setCodeMutation(vb.getCodeMutation());
                b.setCodeTraitement(vb.getCodeTraitement());
                b.setDateLiquidation(vb.getDateLiquidation());
                b.setDebutDroit(vb.getDebutDroit());
                b.setEtatCivil(vb.getEtatCivil());
                b.setFinDroit(vb.getFinDroit());
                b.setGenrePrestation(vb.getGenrePrestation());
                b.setIsRefugie(vb.getIsRefugie());
                // si code 46, on ne remplit pas le champ des mensualités avec des zéros
                // correction bug 5171
                if ("46".equalsIgnoreCase(vb.getCodeApplication())) {
                    b.setMensualitePrestationsFrancs(vb.getMensualitePrestationsFrancs());
                } else {
                    b.setMensualitePrestationsFrancs(JadeStringUtil.fillWithZeroes(vb.getMensualitePrestationsFrancs(),
                            5));
                }
                b.setMensualiteRenteOrdinaire(vb.getMensualiteRenteOrdinaire());
                b.setMoisRapport(vb.getMoisRapport());
                b.setNoAssAyantDroit(vb.getNoAssAyantDroit());
                b.setNouveauNumeroAssureAyantDroit(vb.getNouveauNumeroAssureAyantDroit());
                b.setNumeroAgence(vb.getNumeroAgence());
                b.setNumeroAnnonce(vb.getNumeroAnnonce());
                b.setNumeroCaisse(vb.getNumeroCaisse());
                b.setPremierNoAssComplementaire(vb.getPremierNoAssComplementaire());
                b.setReferenceCaisseInterne(vb.getReferenceCaisseInterne().toUpperCase());
                b.setSecondNoAssComplementaire(vb.getSecondNoAssComplementaire());

                erreurs = REAnakinParser.getInstance().parse(session, b, null, vb.getMoisRapport());
                StringBuffer buff = new StringBuffer();
                while ((erreurs != null) && erreurs.hasMoreElements()) {
                    AnnonceErreur erreur = erreurs.nextElement();
                    buff.append(erreur.getMessage()).append("\n");
                }

                if (buff.length() > 0) {
                    viewBean.setMessage(buff.toString());
                    viewBean.setMsgType(FWViewBeanInterface.ERROR);
                    throw new Exception(buff.toString());
                }

                b.update(transaction);

            } else if (viewBean instanceof REAnnoncesAugmentationModification10EmeViewBean) {

                REAnnoncesAugmentationModification10EmeViewBean vb = (REAnnoncesAugmentationModification10EmeViewBean) viewBean;

                boolean isCodeEnregistrement02 = false;

                // update de l'enregistrement 02
                REAnnoncesAugmentationModification10Eme enregistrement02 = vb.getCodeEnregistrement02();
                if (!enregistrement02.isNew()) {

                    isCodeEnregistrement02 = true;

                    enregistrement02.setAgeDebutInvalidite(vb.getAgeDebutInvalidite());
                    enregistrement02.setAnneeCotClasseAge(vb.getAnneeCotClasseAge());
                    enregistrement02.setAnneeNiveau(vb.getAnneeNiveau());
                    enregistrement02.setCasSpecial1(vb.getCasSpecial1());
                    enregistrement02.setCasSpecial2(vb.getCasSpecial2());
                    enregistrement02.setCasSpecial3(vb.getCasSpecial3());
                    enregistrement02.setCasSpecial4(vb.getCasSpecial4());
                    enregistrement02.setCasSpecial5(vb.getCasSpecial5());
                    enregistrement02.setCodeApplication(vb.getCodeApplication());
                    enregistrement02.setCodeEnregistrement01(vb.getCodeEnr02CodeEnregistrement01());
                    enregistrement02.setCodeInfirmite(vb.getCodeInfirmite());
                    enregistrement02.setCodeRevenuSplitte(vb.getCodeRevenuSplitte());
                    enregistrement02.setDateDebutAnticipation(vb.getDateDebutAnticipation());
                    enregistrement02.setDateRevocationAjournement(vb.getDateRevocationAjournement());
                    enregistrement02.setDegreInvalidite(vb.getDegreInvalidite());
                    enregistrement02.setDureeAjournement(vb.getDureeAjournement());
                    enregistrement02.setDureeCoEchelleRenteAv73(vb.getDureeCoEchelleRenteAv73());
                    enregistrement02.setDureeCoEchelleRenteDes73(vb.getDureeCoEchelleRenteDes73());
                    enregistrement02.setDureeCotManquante48_72(vb.getDureeCotManquante48_72());
                    enregistrement02.setDureeCotManquante73_78(vb.getDureeCotManquante73_78());
                    enregistrement02.setDureeCotPourDetRAM(vb.getDureeCotPourDetRAM());
                    enregistrement02.setEchelleRente(vb.getEchelleRente());
                    enregistrement02.setGenreDroitAPI(vb.getGenreDroitAPI());
                    enregistrement02.setIsSurvivant(vb.getIsSurvivant());
                    enregistrement02.setNbreAnneeAnticipation(vb.getNbreAnneeAnticipation());
                    enregistrement02.setNbreAnneeBonifTrans(vb.getNbreAnneeBonifTrans());
                    enregistrement02.setNbreAnneeBTA(vb.getNbreAnneeBTA());
                    enregistrement02.setNombreAnneeBTE(vb.getNombreAnneeBTE());
                    enregistrement02.setOfficeAICompetent(vb.getOfficeAICompetent());
                    enregistrement02.setRamDeterminant(vb.getRamDeterminant());
                    enregistrement02.setReduction(vb.getReduction());
                    enregistrement02.setReductionAnticipation(vb.getReductionAnticipation());
                    enregistrement02.setSupplementAjournement(vb.getSupplementAjournement());
                    enregistrement02.setSurvenanceEvenAssure(vb.getSurvenanceEvenAssure());

                }

                // update de l'enregistrement 03
                REAnnoncesAugmentationModification10Eme enregistrement03 = vb.getCodeEnregistrement03();
                if (!enregistrement03.isNew()) {
                    // pas utilise pour le moment
                }

                // update de l'enregistrement 01
                // pour la mise à jour des classes parentes, il ne faut pas utiliser le viewBean
                REAnnoncesAugmentationModification10Eme b = new REAnnoncesAugmentationModification10Eme();
                b.setSession(session);
                b.setIdAnnonce(vb.getIdAnnonce());
                b.retrieve(transaction);

                b.setCantonEtatDomicile(vb.getCantonEtatDomicile());
                b.setCodeApplication(vb.getCodeApplication());
                b.setCodeEnregistrement01(vb.getCodeEnregistrement01());
                b.setCodeMutation(vb.getCodeMutation());
                b.setDebutDroit(vb.getDebutDroit());
                b.setEtatCivil(vb.getEtatCivil());
                b.setFinDroit(vb.getFinDroit());
                b.setGenrePrestation(vb.getGenrePrestation());
                b.setIsRefugie(vb.getIsRefugie());
                // si code 46, on ne remplit pas le champ des mensualités avec des zéros
                // correction bug 5171
                if ("46".equalsIgnoreCase(vb.getCodeApplication())) {
                    b.setMensualitePrestationsFrancs(vb.getMensualitePrestationsFrancs());
                } else {
                    b.setMensualitePrestationsFrancs(JadeStringUtil.fillWithZeroes(vb.getMensualitePrestationsFrancs(),
                            5));
                }
                b.setMoisRapport(vb.getMoisRapport());
                b.setNoAssAyantDroit(vb.getNoAssAyantDroit());
                b.setNouveauNoAssureAyantDroit(vb.getNouveauNoAssureAyantDroit());
                b.setNumeroAgence(vb.getNumeroAgence());
                b.setNumeroAnnonce(vb.getNumeroAnnonce());
                b.setNumeroCaisse(vb.getNumeroCaisse());
                b.setPremierNoAssComplementaire(vb.getPremierNoAssComplementaire());
                b.setReferenceCaisseInterne(vb.getReferenceCaisseInterne().toUpperCase());
                b.setSecondNoAssComplementaire(vb.getSecondNoAssComplementaire());

                if (isCodeEnregistrement02) {
                    erreurs = REAnakinParser.getInstance().parse(session, b, enregistrement02, vb.getMoisRapport());
                } else {
                    erreurs = REAnakinParser.getInstance().parse(session, b, null, vb.getMoisRapport());
                }

                StringBuffer buff = new StringBuffer();
                while ((erreurs != null) && erreurs.hasMoreElements()) {
                    AnnonceErreur erreur = erreurs.nextElement();
                    buff.append(erreur.getMessage()).append("\n");
                }

                if (buff.length() > 0) {
                    viewBean.setMessage(buff.toString());
                    viewBean.setMsgType(FWViewBeanInterface.ERROR);
                    throw new Exception(buff.toString());
                }

                if (isCodeEnregistrement02) {
                    enregistrement02.update(transaction);
                }
                b.update(transaction);

            } else if (viewBean instanceof REAnnoncesAugmentationModification9EmeViewBean) {

                REAnnoncesAugmentationModification9EmeViewBean vb = (REAnnoncesAugmentationModification9EmeViewBean) viewBean;

                // update enregistrement 02
                REAnnoncesAugmentationModification9Eme enregistrement02 = vb.getCodeEnregistrement02();
                boolean isCodeEnregistrement02 = false;

                if (!enregistrement02.isNew()) {

                    isCodeEnregistrement02 = true;

                    enregistrement02.setAgeDebutInvalidite(vb.getAgeDebutInvalidite());
                    enregistrement02.setAgeDebutInvaliditeEpouse(vb.getAgeDebutInvaliditeEpouse());
                    enregistrement02.setAnneeCotClasseAge(vb.getAnneeCotClasseAge());
                    enregistrement02.setAnneeNiveau(vb.getAnneeNiveau());
                    enregistrement02.setBteMoyennePrisEnCompte(vb.getBteMoyennePrisEnCompte());
                    enregistrement02.setCasSpecial1(vb.getCasSpecial1());
                    enregistrement02.setCasSpecial2(vb.getCasSpecial2());
                    enregistrement02.setCasSpecial3(vb.getCasSpecial3());
                    enregistrement02.setCasSpecial4(vb.getCasSpecial4());
                    enregistrement02.setCasSpecial5(vb.getCasSpecial5());
                    enregistrement02.setCodeApplication(vb.getCodeApplication());
                    enregistrement02.setCodeEnregistrement01(vb.getCodeEnr02CodeEnregistrement01());
                    enregistrement02.setCodeInfirmite(vb.getCodeInfirmite());
                    enregistrement02.setCodeInfirmiteEpouse(vb.getCodeInfirmiteEpouse());
                    enregistrement02.setDateLiquidation(vb.getDateLiquidation());
                    enregistrement02.setDateRevocationAjournement(vb.getDateRevocationAjournement());
                    enregistrement02.setDegreInvalidite(vb.getDegreInvalidite());
                    enregistrement02.setDegreInvaliditeEpouse(vb.getDegreInvaliditeEpouse());
                    enregistrement02.setDureeAjournement(vb.getDureeAjournement());
                    enregistrement02.setDureeCoEchelleRenteAv73(vb.getDureeCoEchelleRenteAv73());
                    enregistrement02.setDureeCoEchelleRenteDes73(vb.getDureeCoEchelleRenteDes73());
                    enregistrement02.setDureeCotManquante48_72(vb.getDureeCotManquante48_72());
                    enregistrement02.setDureeCotManquante73_78(vb.getDureeCotManquante73_78());
                    enregistrement02.setDureeCotPourDetRAM(vb.getDureeCotPourDetRAM());
                    enregistrement02.setEchelleRente(vb.getEchelleRente());
                    enregistrement02.setGenreDroitAPI(vb.getGenreDroitAPI());
                    enregistrement02.setIsLimiteRevenu(vb.getIsLimiteRevenu());
                    enregistrement02.setIsMinimumGaranti(vb.getIsMinimumGaranti());
                    enregistrement02.setNombreAnneeBTE(vb.getNombreAnneeBTE());

                    // Les codes OAI 9ème doivent être sur 2 positions
                    if (!JadeStringUtil.isBlankOrZero(vb.getOfficeAICompetent())) {
                        String s = vb.getOfficeAICompetent();
                        if (s.length() == 3) {
                            s = s.substring(1, 3);
                        }
                        enregistrement02.setOfficeAICompetent(s);
                    }

                    if (!JadeStringUtil.isBlankOrZero(vb.getOfficeAiCompEpouse())) {
                        String s = vb.getOfficeAiCompEpouse();
                        if (s.length() == 3) {
                            s = s.substring(1, 3);
                        }
                        enregistrement02.setOfficeAiCompEpouse(s);
                    }

                    enregistrement02.setRamDeterminant(vb.getRamDeterminant());
                    enregistrement02.setReduction(vb.getReduction());
                    enregistrement02.setRevenuAnnuelMoyenSansBTE(vb.getRevenuAnnuelMoyenSansBTE());
                    enregistrement02.setRevenuPrisEnCompte(vb.getRevenuPrisEnCompte());
                    enregistrement02.setSupplementAjournement(vb.getSupplementAjournement());
                    enregistrement02.setSurvenanceEvenAssure(vb.getSurvenanceEvenAssure());
                    enregistrement02.setSurvenanceEvtAssureEpouse(vb.getSurvenanceEvtAssureEpouse());

                    if ("41".equals(vb.getCodeApplication())) {
                        REArcFrenchValidator frenchValidator = new REArcFrenchValidator();
                        enregistrement02 = frenchValidator.validateARC_41_02(enregistrement02);

                    }
                }

                // update enregistrement 03
                REAnnoncesAugmentationModification9Eme enregistrement03 = vb.getCodeEnregistrement03();
                if (!enregistrement03.isNew()) {
                    // pas utilise pour le moment
                }

                // update de l'enregistrement 01
                // pour la mise à jour des classes parentes, il ne faut pas utiliser le viewBean
                REAnnoncesAugmentationModification9Eme b = new REAnnoncesAugmentationModification9Eme();
                b.setSession(session);
                b.setIdAnnonce(vb.getIdAnnonce());
                b.retrieve(transaction);

                b.setCantonEtatDomicile(vb.getCantonEtatDomicile());
                b.setCodeApplication(vb.getCodeApplication());
                b.setCodeEnregistrement01(vb.getCodeEnregistrement01());
                b.setCodeMutation(vb.getCodeMutation());
                b.setDebutDroit(vb.getDebutDroit());
                b.setEtatCivil(vb.getEtatCivil());
                b.setFinDroit(vb.getFinDroit());
                b.setGenrePrestation(vb.getGenrePrestation());
                b.setIsRefugie(vb.getIsRefugie());

                if ("43".equals(vb.getCodeApplication())) {
                    b.setMensualitePrestationsFrancs(vb.getMensualitePrestationsFrancs());
                }
                // 41
                else {
                    b.setMensualitePrestationsFrancs(JadeStringUtil.fillWithZeroes(vb.getMensualitePrestationsFrancs(),
                            5));
                }

                b.setMensualiteRenteOrdRemp(vb.getMensualiteRenteOrdRemp());
                b.setMoisRapport(vb.getMoisRapport());
                b.setNoAssAyantDroit(vb.getNoAssAyantDroit());
                b.setNouveauNoAssureAyantDroit(vb.getNouveauNoAssureAyantDroit());
                b.setNumeroAgence(vb.getNumeroAgence());
                b.setNumeroAnnonce(vb.getNumeroAnnonce());
                b.setNumeroCaisse(vb.getNumeroCaisse());
                b.setPremierNoAssComplementaire(vb.getPremierNoAssComplementaire());
                b.setReferenceCaisseInterne(vb.getReferenceCaisseInterne().toUpperCase());
                b.setSecondNoAssComplementaire(vb.getSecondNoAssComplementaire());

                if (isCodeEnregistrement02) {
                    erreurs = REAnakinParser.getInstance().parse(session, b, enregistrement02, vb.getMoisRapport());
                } else {
                    erreurs = REAnakinParser.getInstance().parse(session, b, null, vb.getMoisRapport());
                }

                StringBuffer buff = new StringBuffer();
                while ((erreurs != null) && erreurs.hasMoreElements()) {
                    AnnonceErreur erreur = erreurs.nextElement();
                    buff.append(erreur.getMessage()).append("\n");
                }

                if (buff.length() > 0) {
                    viewBean.setMessage(buff.toString());
                    viewBean.setMsgType(FWViewBeanInterface.ERROR);
                    throw new Exception(buff.toString());
                }

                if (isCodeEnregistrement02) {
                    enregistrement02.update(transaction);
                }
                b.update(transaction);
            }

            return viewBean;

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

    public FWViewBeanInterface actionSupprimerAnnonce(FWViewBeanInterface viewBean, FWAction action, BSession session)
            throws Exception {

        BITransaction transaction = null;

        try {
            transaction = (session).newTransaction();
            transaction.openTransaction();

            if (viewBean instanceof REAnnoncesDiminution10EmeViewBean) {

                // suppression enregistrement 01
                REAnnoncesDiminution10EmeViewBean vb = (REAnnoncesDiminution10EmeViewBean) viewBean;

                // suppression des annonces de rentes liées
                supprimerAnnonceRente(session, vb.getIdAnnonce());

                // pour la suppression des classes parentes, il ne faut pas utiliser le viewBean
                REAnnoncesDiminution10Eme b = new REAnnoncesDiminution10Eme();
                b.setSession(session);
                b.setIdAnnonce(vb.getIdAnnonce());
                b.retrieve(transaction);
                b.delete(transaction);

            } else if (viewBean instanceof REAnnoncesDiminution9EmeViewBean) {

                // suppression enregistrement 01
                REAnnoncesDiminution9EmeViewBean vb = (REAnnoncesDiminution9EmeViewBean) viewBean;

                // suppression des annonces de rentes liées
                supprimerAnnonceRente(session, vb.getIdAnnonce());

                // pour la suppression des classes parentes, il ne faut pas utiliser le viewBean
                REAnnoncesDiminution9Eme b = new REAnnoncesDiminution9Eme();
                b.setSession(session);
                b.setIdAnnonce(vb.getIdAnnonce());
                b.retrieve(transaction);
                b.delete(transaction);

            } else if (viewBean instanceof REAnnoncesAugmentationModification10EmeViewBean) {

                REAnnoncesAugmentationModification10EmeViewBean vb = (REAnnoncesAugmentationModification10EmeViewBean) viewBean;

                // suppression des annonces de rentes liées
                supprimerAnnonceRente(session, vb.getIdAnnonce());

                // suppression enregistrement 02
                REAnnoncesAugmentationModification10Eme enregistrement02 = vb.getCodeEnregistrement02();
                if (!enregistrement02.isNew()) {
                    enregistrement02.delete(transaction);
                }

                // suppression enregistrement 03
                REAnnoncesAugmentationModification10Eme enregistrement03 = vb.getCodeEnregistrement03();
                if (!enregistrement03.isNew()) {
                    enregistrement03.delete(transaction);
                }

                // suppression enregistrement 01
                // pour la suppression des classes parentes, il ne faut pas utiliser le viewBean
                REAnnoncesAugmentationModification10Eme b = new REAnnoncesAugmentationModification10Eme();
                b.setSession(session);
                b.setIdAnnonce(vb.getIdAnnonce());
                b.retrieve(transaction);
                b.delete(transaction);

            } else if (viewBean instanceof REAnnoncesAugmentationModification9EmeViewBean) {

                REAnnoncesAugmentationModification9EmeViewBean vb = (REAnnoncesAugmentationModification9EmeViewBean) viewBean;

                // suppression des annonces de rentes liées
                supprimerAnnonceRente(session, vb.getIdAnnonce());

                // suppression enregistrement 02
                REAnnoncesAugmentationModification9Eme enregistrement02 = vb.getCodeEnregistrement02();
                if (!enregistrement02.isNew()) {
                    enregistrement02.delete(transaction);
                }

                // suppression enregistrement 03
                REAnnoncesAugmentationModification9Eme enregistrement03 = vb.getCodeEnregistrement03();
                if (!enregistrement03.isNew()) {
                    enregistrement03.delete(transaction);
                }

                // suppression enregistrement 01
                // pour la suppression des classes parentes, il ne faut pas utiliser le viewBean
                REAnnoncesAugmentationModification9Eme b = new REAnnoncesAugmentationModification9Eme();
                b.setSession(session);
                b.setIdAnnonce(vb.getIdAnnonce());
                b.retrieve(transaction);
                b.delete(transaction);
            }

            return viewBean;

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

    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        return deleguerExecute(viewBean, action, session);
    }

    /**
     * Suppression d'annonce de rente : la table de liaison (REANREN) est désormais aussi supprimée, le fait qu'elle
     * existe encore après la suppression empêchait de dévalider et revalider correctement une décision
     * 
     * @param session
     *            session utilisateur
     * @param idHeaderAnnonce
     *            l'ID de l'header d'annonce (qui fait la liaison entre l'annonce et l'annonce de rente)
     * @throws Exception
     *             si une erreur survient lors de la recherche des annonces de rente
     */
    private void supprimerAnnonceRente(BSession session, String idHeaderAnnonce) throws Exception {

        // on recherche les liaison entre annonces et rente/décision (table REANREN)
        REAnnonceRenteManager manager = new REAnnonceRenteManager();
        manager.setSession(session);
        manager.setForIdAnnonceHeader(idHeaderAnnonce);
        manager.find();

        for (Iterator<REAnnonceRente> iterator = manager.iterator(); iterator.hasNext();) {
            REAnnonceRente annonce = iterator.next();
            annonce.delete();
        }
    }

}
