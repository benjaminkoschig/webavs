package globaz.corvus.helpers.adaptation;

import java.util.Iterator;

import ch.globaz.corvus.domaine.constantes.CodeCasSpecialRente;
import globaz.commons.nss.NSUtil;
import globaz.corvus.anakin.AnakinValidationException;
import globaz.corvus.annonce.REAnnoncesAPersister;
import globaz.corvus.annonce.domain.annonce.REAnnoncePonctuelle10EmeRevision;
import globaz.corvus.annonce.domain.annonce.REAnnoncePonctuelle9EmeRevision;
import globaz.corvus.annonce.reader.REBaseDeCalcul10EmeRevisionReader;
import globaz.corvus.annonce.reader.REBaseDeCalcul9EmeRevisionReader;
import globaz.corvus.annonce.reader.RECreationAnnonceGeneralReader;
import globaz.corvus.annonce.reader.RERenteAccordeeReader;
import globaz.corvus.annonce.service.RECreationAnnonceService;
import globaz.corvus.api.adaptation.IREAdaptationRente;
import globaz.corvus.api.annonces.IREAnnonces;
import globaz.corvus.api.basescalcul.IREPrestationDue;
import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.corvus.db.adaptation.RERentesAdaptees;
import globaz.corvus.db.adaptation.RERentesAdapteesJointRATiers;
import globaz.corvus.db.annonces.REAnnonceRente;
import globaz.corvus.db.annonces.REAnnoncesAugmentationModification10Eme;
import globaz.corvus.db.annonces.REAnnoncesAugmentationModification10EmeManager;
import globaz.corvus.db.annonces.REAnnoncesAugmentationModification9Eme;
import globaz.corvus.db.annonces.REAnnoncesAugmentationModification9EmeManager;
import globaz.corvus.db.basescalcul.REBasesCalculDixiemeRevision;
import globaz.corvus.db.basescalcul.REBasesCalculNeuviemeRevision;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.demandes.REDemandeRenteAPI;
import globaz.corvus.db.demandes.REDemandeRenteManager;
import globaz.corvus.db.rentesaccordees.REPrestationDue;
import globaz.corvus.db.rentesaccordees.REPrestationsDuesManager;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.utils.REPmtMensuel;
import globaz.corvus.utils.enumere.genre.prestations.REGenrePrestationEnum;
import globaz.corvus.utils.enumere.genre.prestations.REGenresPrestations;
import globaz.corvus.vb.adaptation.REAdaptationManuelleViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.framework.util.FWCurrency;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.hera.api.ISFMembreFamille;
import globaz.hera.api.ISFMembreFamilleRequerant;
import globaz.hera.api.ISFRelationFamiliale;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.hera.db.famille.SFApercuEnfant;
import globaz.hera.db.famille.SFMembreFamille;
import globaz.hera.external.SFSituationFamilialeFactory;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.enums.codeprestation.type.PRCodePrestationInvalidite;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRDateFormater;

public class REAdaptationManuelleHelper extends FWHelper {
    private final String EMPTY_NSS = "00000000000";

    class NSSComplementaire {
        private String nssComplementaire1 = "";
        private String nssComplementaire2 = "";

        /**
         * @return the nssComplementaire1
         */
        public final String getNssComplementaire1() {
            return nssComplementaire1;
        }

        /**
         * @param nssComplementaire1 the nssComplementaire1 to set
         */
        public final void setNssComplementaire1(String nssComplementaire1) {
            this.nssComplementaire1 = nssComplementaire1;
        }

        /**
         * @return the nssComplementaire2
         */
        public final String getNssComplementaire2() {
            return nssComplementaire2;
        }

        /**
         * @param nssComplementaire2 the nssComplementaire2 to set
         */
        public final void setNssComplementaire2(String nssComplementaire2) {
            this.nssComplementaire2 = nssComplementaire2;
        }

    }

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    public REAdaptationManuelleHelper() {
        super();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @SuppressWarnings("deprecation")
    @Override
    protected void _add(FWViewBeanInterface viewBean, FWAction action, BISession iSession) throws Exception {

        BTransaction transaction = null;
        BSession session = (BSession) iSession;
        try {
            transaction = (BTransaction) session.newTransaction();
            transaction.openTransaction();

            REAdaptationManuelleViewBean vb = (REAdaptationManuelleViewBean) viewBean;

            // RECUPERATION DE LA RENTE ACCORDEE
            RERenteAccordee ra = new RERenteAccordee();
            ra.setSession(session);
            ra.setIdPrestationAccordee(vb.getIdPrestationAccordee());
            ra.retrieve();
            if (ra.isNew()) {
                String message = session.getCodeLibelle("ERREUR_IMPOSSIBLE_RETROUVER_RENTE_ACCORDEE_AVEC_ID");
                message = message.replace("{id}", vb.getIdPrestationAccordee());
                throw new Exception(message);
            }

            // RECUPERATION DE LA DATE DERNIER PAIEMENT MENSUEL AU FORMAT MM.AAAA
            String dateDernierPaiementMensuel = REPmtMensuel.getDateDernierPmt(session);
            if (REPmtMensuel.DATE_NON_TROUVEE_POUR_DERNIER_PAIEMENT.equals(dateDernierPaiementMensuel)) {
                String message = session.getLabel("ERREUR_IMPOSSIBLE_RETROUVER_DATE_DERNIER_PAIEMENT");
                throw new Exception(message);
            }

            // MAJ DE LA BASE DE CALCUL ET DE LA RENTE ACCORDEE
            int versionDroit = miseAjourBaseCalculEtRenteAccordee(session, transaction, vb, ra,
                    dateDernierPaiementMensuel);
            if (versionDroit == 0) {
                String message = session.getLabel("ERREUR_IMPOSSIBLE_RECUPERER_VERSION_DROIT_APPLIQUE_BASE_CALCUL");
                throw new Exception(message);
            }

            // CREATION DES ANNONCES SUBSEQUENTES SI BESOIN
            if (vb.getIsCreateAnnonceSub()) {
                PRTiersWrapper tier = PRTiersHelper.getTiersAdresseParId(session, ra.getIdTiersBeneficiaire());

                switch (versionDroit) {
                    case 9:
                        creerAnnonces9emeRevision(session, transaction, vb, tier, ra, dateDernierPaiementMensuel);
                        break;
                    case 10:
                        creerAnnonces10emeRevision(session, transaction, vb, tier, ra, dateDernierPaiementMensuel);
                        break;
                    // On ne sait pas de quelle révision on parle..
                    default:
                        String message = session
                                .getLabel("ERREUR_IMPOSSIBLE_RECUPERER_VERSION_DROIT_APPLIQUE_BASE_CALCUL");
                        throw new Exception(message);

                }
            }

            // Changement du type de la rente adaptée
            RERentesAdaptees renteAdaptees = new RERentesAdaptees();
            renteAdaptees.setSession(session);
            renteAdaptees.setIdRenteAdaptee(vb.getIdRenteAdaptee());
            renteAdaptees.retrieve();

            // Mise à jour des données
            renteAdaptees.setNouveauRAM(vb.getNouveauRam());
            renteAdaptees.setNouveauMontantPrestation(vb.getNouveauMontantPrestation());
            renteAdaptees.setNouveauAnneeMontantRAM(vb.getNouveauAnneeMontantRAM());
            renteAdaptees.setNouveauMntReductionAnticipation(vb.getNouveauMntReductionAnticipation());
            renteAdaptees.setNouveauMntRenteOrdinaireRempl(vb.getNouveauMntRenteOrdinaireRempl());
            renteAdaptees.setNouveauSupplementAjournement(vb.getNouveauSupplementAjournement());

            renteAdaptees.setCsTypeAdaptation(IREAdaptationRente.CS_TYPE_AUG_TRAITEMENT_MANUEL);
            renteAdaptees.update(transaction);

        } catch (Exception e) {
            if (transaction != null) {
                transaction.setRollbackOnly();
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
                viewBean.setMessage(e.toString());
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
    protected void _init(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        // Chargement du viewBean pour affichage de l'écran d'adaptation manuelle
        REAdaptationManuelleViewBean vb = (REAdaptationManuelleViewBean) viewBean;
        String idRenteAdaptee = vb.getId();
        vb.setIdRenteAdaptee(idRenteAdaptee);

        // Chargement de la rente adaptée
        RERentesAdapteesJointRATiers rentesAdap = new RERentesAdapteesJointRATiers();
        rentesAdap.setSession(vb.getSession());
        rentesAdap.setIdRenteAdaptee(idRenteAdaptee);
        rentesAdap.retrieve();

        vb.setCsTypeAdaptation(rentesAdap.getCsTypeAdaptation());

        // Chargement de la rente accordée
        RERenteAccordee ra = new RERenteAccordee();
        ra.setSession(vb.getSession());
        ra.setIdPrestationAccordee(rentesAdap.getIdPrestationAccordee());
        ra.retrieve();

        vb.setDescriptionTiers(rentesAdap.getNssRA() + " / " + rentesAdap.getNomRA() + " " + rentesAdap.getPrenomRA());
        vb.setGenrePrestation(ra.getCodePrestation());
        vb.setFractionRente(ra.getFractionRente());

        vb.setAncienRam(rentesAdap.getAncienRAM());
        vb.setNouveauRam(rentesAdap.getNouveauRAM());
        vb.setAncienMontantPrestation(rentesAdap.getAncienMontantPrestation());
        vb.setNouveauMontantPrestation(rentesAdap.getNouveauMontantPrestation());
        vb.setAncienMntRenteOrdinaireRempl(rentesAdap.getAncienMntRenteOrdinaireRempl());
        vb.setNouveauMntRenteOrdinaireRempl(rentesAdap.getNouveauMntRenteOrdinaireRempl());
        vb.setAncienSupplementAjournement(rentesAdap.getAncienSupplementAjournement());
        vb.setNouveauSupplementAjournement(rentesAdap.getNouveauSupplementAjournement());
        vb.setAncienMntReductionAnticipation(rentesAdap.getAncienMntReductionAnticipation());
        vb.setNouveauMntReductionAnticipation(rentesAdap.getNouveauMntReductionAnticipation());
        vb.setAncienAnneeMontantRAM(rentesAdap.getAncienAnneeMontantRAM());
        vb.setNouveauAnneeMontantRAM(rentesAdap.getNouveauAnneeMontantRAM());

        vb.setAncienCCS1(ra.getCodeCasSpeciaux1());
        vb.setAncienCCS2(ra.getCodeCasSpeciaux2());
        vb.setAncienCCS3(ra.getCodeCasSpeciaux3());
        vb.setAncienCCS4(ra.getCodeCasSpeciaux4());
        vb.setAncienCCS5(ra.getCodeCasSpeciaux5());

        vb.setNouveauCCS1(ra.getCodeCasSpeciaux1());
        vb.setNouveauCCS2(ra.getCodeCasSpeciaux2());
        vb.setNouveauCCS3(ra.getCodeCasSpeciaux3());
        vb.setNouveauCCS4(ra.getCodeCasSpeciaux4());
        vb.setNouveauCCS5(ra.getCodeCasSpeciaux5());

        vb.setIdPrestationAccordee(ra.getIdPrestationAccordee());

        REBasesCalculNeuviemeRevision bc = new REBasesCalculNeuviemeRevision();
        bc.setSession(vb.getSession());
        bc.setIdBasesCalcul(ra.getIdBaseCalcul());
        bc.retrieve();

        vb.setAncienBTEFrancs(bc.getBonificationTacheEducative());

        viewBean = vb;
    }

    /**
     *
     * @param session
     * @param transaction
     * @param vb
     * @param ra
     * @param dateDernierPaiementMensuel
     * @return la version du droit de la base de calcul. Retourne 0 si la version du droit n'as pas été trouvé
     * @throws Exception
     */
    private int miseAjourBaseCalculEtRenteAccordee(BSession session, BTransaction transaction,
            REAdaptationManuelleViewBean vb, RERenteAccordee ra, String dateDernierPaiementMensuel) throws Exception {
        int versionDroit = 0;

        boolean isDejaAdaptee = vb.getIsDejaAdaptee();

        // Mise à jour de la base de calcul
        REBasesCalculNeuviemeRevision bc9 = new REBasesCalculNeuviemeRevision();

        REBasesCalculDixiemeRevision bc10 = new REBasesCalculDixiemeRevision();
        bc10.setSession(session);
        bc10.setIdBasesCalcul(ra.getIdBaseCalcul());
        bc10.retrieve();

        // 9ème révision
        if (bc10.isNew()) {
            versionDroit = 9;

            bc9.setSession(session);
            bc9.setIdBasesCalcul(ra.getIdBaseCalcul());
            bc9.retrieve();

            REDemandeRente demande = new REDemandeRente();
            demande.setSession(session);
            demande.setAlternateKey(REDemandeRente.ALTERNATE_KEY_ID_RENTE_CALCULEE);
            demande.setIdRenteCalculee(bc9.getIdRenteCalculee());
            demande.retrieve();

            if (demande.getCsTypeDemandeRente().equals(IREDemandeRente.CS_TYPE_DEMANDE_RENTE_API)) {
                bc9.setIsDemandeRenteAPI(true);
            }

            bc9.setRevenuAnnuelMoyen(vb.getNouveauRam());
            bc9.setAnneeTraitement(vb.getNouveauAnneeMontantRAM());
            bc9.setBonificationTacheEducative(vb.getNouveauBTEFrancs());
            bc9.update(transaction);

        }
        // 10ème révision
        else {
            versionDroit = 10;

            REDemandeRente demande = new REDemandeRente();
            demande.setSession(session);
            demande.setAlternateKey(REDemandeRente.ALTERNATE_KEY_ID_RENTE_CALCULEE);
            demande.setIdRenteCalculee(bc10.getIdRenteCalculee());
            demande.retrieve();

            if (demande.getCsTypeDemandeRente().equals(IREDemandeRente.CS_TYPE_DEMANDE_RENTE_API)) {
                bc10.setIsDemandeRenteAPI(true);
            }

            bc10.setAnneeTraitement(vb.getNouveauAnneeMontantRAM());
            bc10.setRevenuAnnuelMoyen(vb.getNouveauRam());
            bc10.update(transaction);

        }

        // Mise à jour de la rente accordée
        ra.setMontantPrestation(vb.getNouveauMontantPrestation());
        ra.setMontantRenteOrdiRemplacee(vb.getNouveauMntRenteOrdinaireRempl());
        ra.setSupplementAjournement(vb.getNouveauSupplementAjournement());
        ra.setMontantReducationAnticipation(vb.getNouveauMntReductionAnticipation());
        ra.setAnneeMontantRAM(vb.getNouveauAnneeMontantRAM());
        ra.setCodeCasSpeciaux1(vb.getNouveauCCS1());
        ra.setCodeCasSpeciaux2(vb.getNouveauCCS2());
        ra.setCodeCasSpeciaux3(vb.getNouveauCCS3());
        ra.setCodeCasSpeciaux4(vb.getNouveauCCS4());
        ra.setCodeCasSpeciaux5(vb.getNouveauCCS5());
        ra.update(transaction);

        if (isDejaAdaptee) {
            // Mise à jour de la prestation due
            REPrestationsDuesManager prstduMgr = new REPrestationsDuesManager();
            prstduMgr.setSession(session);
            prstduMgr.setForIdRenteAccordes(ra.getIdPrestationAccordee());
            prstduMgr.setForCsType(IREPrestationDue.CS_TYPE_PMT_MENS);
            prstduMgr.setForPeriodePDInMoisAnnee(
                    PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(REPmtMensuel.getDateProchainPmt(session)));
            prstduMgr.find();

            if (prstduMgr.isEmpty() || (prstduMgr.size() > 1)) {
                throw new Exception(session.getLabel("ERREUR_MAJ_PRST_DU_ZERO_PLUS"));
            } else {

                REPrestationDue pd = (REPrestationDue) prstduMgr.getFirstEntity();
                pd.setRam(vb.getNouveauRam());
                pd.setMontantSupplementAjournement(ra.getSupplementAjournement());
                pd.setMontantReductionAnticipation(ra.getMontantReducationAnticipation());
                pd.setMontant(ra.getMontantPrestation());
                pd.update(transaction);

                // Mise à jour de la rente adaptée
                RERentesAdaptees rad = new RERentesAdaptees();
                rad.setSession(session);
                rad.setIdRenteAdaptee(vb.getIdRenteAdaptee());
                rad.retrieve();

                rad.setCsTypeAdaptation(IREAdaptationRente.CS_TYPE_AUG_TRAITEMENT_MANUEL);
                rad.update(transaction);

            }

        } else {
            // Mise à jour des prestations dues et création nouvelle
            REPrestationsDuesManager prstduMgr = new REPrestationsDuesManager();
            prstduMgr.setSession(session);
            prstduMgr.setForIdRenteAccordes(ra.getIdPrestationAccordee());
            prstduMgr.setForCsType(IREPrestationDue.CS_TYPE_PMT_MENS);
            prstduMgr.setForPeriodePDInMoisAnnee(
                    PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(REPmtMensuel.getDateDernierPmt(session)));
            prstduMgr.find();

            if (prstduMgr.isEmpty() || (prstduMgr.size() > 1)) {
                throw new Exception(session.getLabel("ERREUR_MAJ_PRST_DU_ZERO_PLUS"));
            } else {
                REPrestationDue pd = (REPrestationDue) prstduMgr.getFirstEntity();

                // si date de fin, créer seulement la nouvelle période
                if (JadeStringUtil.isBlankOrZero(pd.getDateFinPaiement())) {
                    pd.setDateFinPaiement(dateDernierPaiementMensuel);
                    pd.update();
                }

                // - Création d'une nouvelle période avec :
                // - Date début paiement
                // - RAM
                // - Supplément ajournement
                // - Réduction anticipation
                // - Montant
                // - Etat (partiel)
                // - Date de fin (vide)
                // - Taux réduction anticipation (reprendre idem précédent)
                // - Type (paiement mensuel)
                REPrestationDue newPd = new REPrestationDue();
                newPd.setSession(session);
                newPd.setDateDebutPaiement(REPmtMensuel.getDateProchainPmt(session));

                newPd.setRam(vb.getNouveauRam());
                newPd.setMontantSupplementAjournement(ra.getSupplementAjournement());
                newPd.setMontantReductionAnticipation(ra.getMontantReducationAnticipation());
                newPd.setMontant(ra.getMontantPrestation());
                newPd.setCsEtat(IREPrestationDue.CS_ETAT_ACTIF);
                newPd.setDateFinPaiement("");
                newPd.setCsType(pd.getCsType());
                newPd.setIdRenteAccordee(ra.getIdPrestationAccordee());
                newPd.add(transaction);

            }
        }
        return versionDroit;
    }

    /**
     * Créer les annonces pour la 10ème révision.</br>
     * Une annonce de diminution doit être créer et une ponctuelle. Les règles sont les suivantes :</br>
     * </br>
     * Annonce de diminution existante ?</br>
     * Si non, créer</br>
     * Si oui, ne rien faire</br>
     * </br>
     * Créer annonce ponctuelle 10ème</br>
     *
     * @param transaction
     * @param vb
     * @param ra
     * @param session
     * @param dateRapportEtFin
     * @param tier
     * @throws Exception
     */
    private void creerAnnonces10emeRevision(BSession session, BTransaction transaction, REAdaptationManuelleViewBean vb,
            PRTiersWrapper tier, RERenteAccordee ra, String dateDernierPaiement) throws Exception {

        // On va d'abord contrôler si une annonce de diminution à déjà été créée.
        int genrePrestation = 0;
        try {
            genrePrestation = Integer.valueOf(vb.getGenrePrestation());
        } catch (NumberFormatException e) {
            String message = session
                    .getLabel("ERREUR_IMPOSSIBLE_RETROUVER_LE_GENRE_DE_PRESTATION_RENTE_ACCORDEE_AVEC_ID");
            message = message.replace("{0}", vb.getGenrePrestation());
            message = message.replace("{1}", vb.getIdPrestationAccordee());
        }

        boolean annonceDiminutionExistante = isAnnonceDiminution10emeRevisionExistante(session, transaction,
                genrePrestation, dateDernierPaiement, tier);
        if (!annonceDiminutionExistante) {
            creerAnnonceDiminution10emeRevision(session, transaction, vb, ra, dateDernierPaiement, tier);
        }

        // Suppression des annonces ponctuelles si déjà existante
        suppressionAnnoncePonctuelle10emeRevisionExistante(session, transaction, genrePrestation, dateDernierPaiement,
                tier);

        // On créer l'annonce d'augmentation dans tous les cas...
        creerAnnoncePonctuelle10emeRevision(session, transaction, vb, ra, dateDernierPaiement);
    }

    /**
     * @param session
     * @param transaction
     * @param vb
     * @param tier
     * @param annonceDiminutionExistante
     * @return
     * @throws Exception
     */
    private void suppressionAnnoncePonctuelle10emeRevisionExistante(BSession session, BTransaction transaction,
            int genrePrestation, String dateDernierPaiement, PRTiersWrapper tier) throws Exception {

        String moisRapport = JadeDateUtil.addMonths("01." + dateDernierPaiement, 1);
        moisRapport = moisRapport.substring(3);

        REAnnoncesAugmentationModification10EmeManager annAug10Mgr = new REAnnoncesAugmentationModification10EmeManager();
        annAug10Mgr.setForMoisRapport(moisRapport);
        annAug10Mgr.setForNss(NSUtil.unFormatAVS(tier.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL)));
        annAug10Mgr.setForCodePrestation(String.valueOf(genrePrestation));
        annAug10Mgr.setSession(session);
        annAug10Mgr.find();

        if (annAug10Mgr.size() > 0) {
            for (@SuppressWarnings("unchecked")
            Iterator<REAnnoncesAugmentationModification10Eme> iterator = annAug10Mgr.iterator(); iterator.hasNext();) {
                REAnnoncesAugmentationModification10Eme ann10eme = iterator.next();
                if (JadeStringUtil.isBlankOrZero(ann10eme.getFinDroit()) && ann10eme.getCodeMutation().equals("78")) {
                    ann10eme.delete(transaction);
                }
            }
        }
    }

    /**
     * @param session
     * @param transaction
     * @param vb
     * @param tier
     * @param annonceDiminutionExistante
     * @return
     * @throws Exception
     */
    private boolean isAnnonceDiminution10emeRevisionExistante(BSession session, BTransaction transaction,
            int genrePrestation, String dateDernierPaiement, PRTiersWrapper tier) throws Exception {

        // Mois rapport
        JADate moisRapport = new JADate(new JACalendarGregorian().addMonths(dateDernierPaiement, 1));
        String moisRapportFormatte = String.valueOf(moisRapport.getMonth()) + "."
                + String.valueOf(moisRapport.getYear());

        boolean annonceDiminutionExistante = false;

        REAnnoncesAugmentationModification10EmeManager annAug10Mgr = new REAnnoncesAugmentationModification10EmeManager();
        annAug10Mgr.setForMoisRapport(moisRapportFormatte);
        annAug10Mgr.setForNss(NSUtil.unFormatAVS(tier.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL)));
        annAug10Mgr.setForCodePrestation(String.valueOf(genrePrestation));
        annAug10Mgr.setSession(session);
        annAug10Mgr.find();

        if (annAug10Mgr.size() > 0) {
            for (@SuppressWarnings("unchecked")
            Iterator<REAnnoncesAugmentationModification10Eme> iterator = annAug10Mgr.iterator(); iterator.hasNext();) {
                REAnnoncesAugmentationModification10Eme ann10eme = iterator.next();
                if (!JadeStringUtil.isBlankOrZero(ann10eme.getFinDroit()) && ann10eme.getCodeMutation().equals("77")) {
                    annonceDiminutionExistante = true;
                }
            }
        }
        return annonceDiminutionExistante;
    }

    private void creerAnnoncePonctuelle9emeRevision(BSession session, BTransaction transaction,
            REAdaptationManuelleViewBean vb, RERenteAccordee ra, String dateDernierPaiement) throws Exception {

        if (ra == null) {
            throw new Exception("RERenteAccordee is null");
        }
        if (ra.isNew()) {
            String message = session.getCodeLibelle("ERREUR_IMPOSSIBLE_RETROUVER_RENTE_ACCORDEE_AVEC_ID");
            message = message.replace("{id}", vb.getIdPrestationAccordee());
            throw new Exception(message);
        }

        REBasesCalculNeuviemeRevision bc = new REBasesCalculNeuviemeRevision();
        bc.setSession(session);
        bc.setIdBasesCalcul(ra.getIdBaseCalcul());
        bc.retrieve(transaction);
        if (bc.isNew()) {
            throw new Exception("REBasesCalculNeuviemeRevision is null");
        }
        if (!"9".equals(bc.getDroitApplique())) {
            String message = session.getCodeLibelle("ERREUR_INCOHERENCE_DROIT_APPLIQUE_BASE_CALCUL_9EME_REVISION");
            message = message.replace("{0}", ra.getIdBaseCalcul());
            throw new Exception(message);
        }

        REDemandeRenteManager drManager = new REDemandeRenteManager();
        drManager.setSession(session);
        drManager.setForIdRenteCalculee(bc.getIdRenteCalculee());
        drManager.find(transaction);

        REDemandeRente dr = (REDemandeRente) drManager.getFirstEntity();
        if (dr == null) {
            throw new Exception("REDemandeRente is null");
        }
        if (dr.isNew()) {
            String message = session.getCodeLibelle("ERREUR_IMPOSSIBLE_RETROUVER_DEMANDE_RENTE_AVEC_ID");
            message = message.replace("{id}", bc.getIdRenteCalculee());
            throw new Exception(message);
        }

        PRTiersWrapper tierBeneficiaire = PRTiersHelper.getTiersParId(session, ra.getIdTiersBeneficiaire());
        if (tierBeneficiaire == null) {
            String message = session.getCodeLibelle("ERREUR_IMPOSSIBLE_RETROUVER_TIERS_AVEC_ID");
            message = message.replace("{id}", ra.getIdTiersBeneficiaire());
            throw new Exception(message);
        }

        REAnnoncePonctuelle9EmeRevision annoncePonctuelle9Eme = new REAnnoncePonctuelle9EmeRevision();
        RECreationAnnonceGeneralReader generalReader = new RECreationAnnonceGeneralReader();
        RERenteAccordeeReader renteAccordeeReader = new RERenteAccordeeReader();
        REBaseDeCalcul9EmeRevisionReader baseDeCalculReader = new REBaseDeCalcul9EmeRevisionReader();
        // ---------------------------------------------------------------------------//

        annoncePonctuelle9Eme
                .setNumeroCaisse(generalReader.convertNumeroCaisse(session.getApplication().getProperty("noCaisse")));
        annoncePonctuelle9Eme
                .setNumeroAgence(generalReader.convertNumeroAgence(session.getApplication().getProperty("noAgence")));
        annoncePonctuelle9Eme.setUtilisateurPourReferenceCaisseInterne(session.getUserId().toUpperCase());

        annoncePonctuelle9Eme.setDebutDroit(new JADate(new JACalendarGregorian().addMonths(dateDernierPaiement, 1)));
        annoncePonctuelle9Eme.setFinDroit(null);
        annoncePonctuelle9Eme.setMoisRapport(new JADate(new JACalendarGregorian().addMonths(dateDernierPaiement, 1)));
        // rapport
        annoncePonctuelle9Eme.setCodeMutation(78);

        annoncePonctuelle9Eme.setNoAssAyantDroit(
                generalReader.convertFormatedNss(tierBeneficiaire.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL)));
        annoncePonctuelle9Eme.setMensualitePrestationsFrancs(
                generalReader.convertMensualitePrestationsFrancs(vb.getNouveauMontantPrestation()));

        // Valeurs récupéré depuis les rentes accordées
        annoncePonctuelle9Eme.setIdTiers(renteAccordeeReader.convertIdTiers(ra.getIdTiersBeneficiaire()));
        annoncePonctuelle9Eme.setRefugie(renteAccordeeReader.convertIsRefugie(ra.getCodeRefugie()));
        annoncePonctuelle9Eme.setGenrePrestation(renteAccordeeReader.convertGenrePrestation(ra.getCodePrestation()));
        annoncePonctuelle9Eme.setSupplementAjournement(
                renteAccordeeReader.convertSupplementAjournement(ra.getSupplementAjournement()));
        // annoncePonctuelle10Eme.setDateDebutAnticipation(renteAccordeeReader.convertDateDebutAnticipation(ra
        // .getDateDebutAnticipation()));
        // annoncePonctuelle10Eme.setNbreAnneeAnticipation(renteAccordeeReader.convertNbreAnneeAnticipation(ra
        // .getAnneeAnticipation()));
        // annoncePonctuelle10Eme.setReductionAnticipation(renteAccordeeReader.convertReductionAnticipation(ra
        // .getMontantReducationAnticipation()));
        // annoncePonctuelle10Eme.setSurvivant(renteAccordeeReader.convertIsSurvivant(ra.getCodeSurvivantInvalide()));
        annoncePonctuelle9Eme.setReduction(renteAccordeeReader.convertReduction(ra.getReductionFauteGrave()));
        annoncePonctuelle9Eme.setCasSpecial1(renteAccordeeReader.convertCasSpecial(ra.getCodeCasSpeciaux1()));
        annoncePonctuelle9Eme.setCasSpecial2(renteAccordeeReader.convertCasSpecial(ra.getCodeCasSpeciaux2()));
        annoncePonctuelle9Eme.setCasSpecial3(renteAccordeeReader.convertCasSpecial(ra.getCodeCasSpeciaux3()));
        annoncePonctuelle9Eme.setCasSpecial4(renteAccordeeReader.convertCasSpecial(ra.getCodeCasSpeciaux4()));
        annoncePonctuelle9Eme.setCasSpecial5(renteAccordeeReader.convertCasSpecial(ra.getCodeCasSpeciaux5()));
        annoncePonctuelle9Eme.setDateRevocationAjournement(
                renteAccordeeReader.convertDateRevocationAjournement(ra.getDateRevocationAjournement()));
        annoncePonctuelle9Eme.setDureeAjournement(
                renteAccordeeReader.convertDureeAjournementValeurEntiere(ra.getDureeAjournement()),
                renteAccordeeReader.convertDureeAjournementValeurDecimal(ra.getDureeAjournement()));

        Integer etatCivil = renteAccordeeReader
                .convertEtatCivil(PRACORConst.csEtatCivilToAcorForRentes(ra.getCsEtatCivil()));
        annoncePonctuelle9Eme.setEtatCivil(etatCivil);

        // CantonEtatDomicile
        // On essaye avec le tier requérant
        PRTiersWrapper tierRequerantAdr = PRTiersHelper.getTiersAdresseDomicileParId(session, ra.getIdTiersBaseCalcul(),
                JACalendar.todayJJsMMsAAAA());
        if (tierRequerantAdr != null) {
            annoncePonctuelle9Eme.setCantonEtatDomicile(generalReader.convertCantonEtatDomicile(
                    PRACORConst.csCantonToAcor(tierRequerantAdr.getProperty(PRTiersWrapper.PROPERTY_ID_CANTON))));
        }

        // sinon on test avec le tier bénéficiaire
        else {
            PRTiersWrapper tierBeneficiaireAdr = PRTiersHelper.getTiersAdresseDomicileParId(session,
                    ra.getIdTiersBeneficiaire(), JACalendar.todayJJsMMsAAAA());
            if (tierBeneficiaireAdr != null) {
                annoncePonctuelle9Eme.setCantonEtatDomicile(generalReader.convertCantonEtatDomicile(PRACORConst
                        .csCantonToAcor(tierBeneficiaireAdr.getProperty(PRTiersWrapper.PROPERTY_ID_CANTON))));
            }
        }

        // PremierNoAssComplementaire
        boolean isNSS1Obligatoire = false;
        if (REGenrePrestationEnum.groupe1.contains(ra.getCodePrestation())) {
            if (etatCivil != null) {
                if (etatCivil == 2 || etatCivil == 3) {
                    isNSS1Obligatoire = true;
                }
            }
        }
        NSSComplementaire nssComplementaire = null;

        PRTiersWrapper tierComp1 = PRTiersHelper.getTiersParId(session, ra.getIdTiersComplementaire1());
        if (tierComp1 != null) {
            annoncePonctuelle9Eme.setPremierNoAssComplementaire(
                    generalReader.convertFormatedNss(tierComp1.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL)));
        } else {
            if (REGenrePrestationEnum.groupe5.contains(ra.getCodePrestation()) || isNSS1Obligatoire) {
                nssComplementaire = majTiersComplementaires(session, transaction, ra);
                annoncePonctuelle9Eme.setPremierNoAssComplementaire(
                        generalReader.convertFormatedNss(nssComplementaire.getNssComplementaire1()));
            }
        }

        // SecondNoAssComplementaire
        PRTiersWrapper tierComp2 = PRTiersHelper.getTiersParId(session, ra.getIdTiersComplementaire2());
        if (tierComp2 != null) {
            annoncePonctuelle9Eme.setSecondNoAssComplementaire(
                    generalReader.convertFormatedNss(tierComp2.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL)));
        } else {
            if (REGenrePrestationEnum.groupe5.contains(ra.getCodePrestation())) {
                nssComplementaire = majTiersComplementaires(session, transaction, ra);
                if (!JadeStringUtil.isBlankOrZero(nssComplementaire.getNssComplementaire2())) {
                    annoncePonctuelle9Eme.setSecondNoAssComplementaire(
                            generalReader.convertFormatedNss(nssComplementaire.getNssComplementaire2()));
                } else {
                    annoncePonctuelle9Eme.setSecondNoAssComplementaire(generalReader.convertFormatedNss(EMPTY_NSS));
                }
            }
        }

        // Si API Saisir le genre droit API
        if (isAPI(ra.getCodePrestation())) {
            REDemandeRenteAPI demAPI = new REDemandeRenteAPI();
            demAPI.setSession(session);
            demAPI.setIdDemandeRente(dr.getIdDemandeRente());
            demAPI.retrieve();
            if (demAPI.isNew()) {
                throw new Exception(
                        "Unable to retrieve the REDemandeRenteAPI with id [" + dr.getIdDemandeRente() + "]");
            }
            annoncePonctuelle9Eme
                    .setGenreDroitAPI(generalReader.convertGenreDroitAPI(session.getCode(ra.getCsGenreDroitApi())));
        }

        // Pour tous les autre type de rentes sauf API
        else {
            annoncePonctuelle9Eme.setEchelleRente(baseDeCalculReader.readEchelleRente(bc.getEchelleRente()));

            annoncePonctuelle9Eme.setDureeCotManquante48_72(
                    baseDeCalculReader.readDureeCotManquante48_72(bc.getMoisAppointsAvant73()));
            annoncePonctuelle9Eme.setDureeCotManquante73_78(
                    baseDeCalculReader.readDureeCotManquante73_78(bc.getMoisAppointsDes73()));
            annoncePonctuelle9Eme.setRamDeterminant(baseDeCalculReader.readRamDeterminant(bc.getRevenuAnnuelMoyen()));
            annoncePonctuelle9Eme
                    .setAnneeCotClasseAge(baseDeCalculReader.readAnneeCotClasseAge(bc.getAnneeCotiClasseAge()));
            annoncePonctuelle9Eme.setAnneeNiveau(baseDeCalculReader.readAnneeNiveau(bc.getAnneeDeNiveau()));

            //
            annoncePonctuelle9Eme.setDureeCoEchelleRenteAv73(
                    baseDeCalculReader.readDureeCoEchelleRenteAv73_nombreAnnee(bc.getDureeCotiAvant73()),
                    baseDeCalculReader.readDureeCoEchelleRenteAv73_nombreMois(bc.getDureeCotiAvant73()));

            annoncePonctuelle9Eme.setDureeCoEchelleRenteDes73(
                    baseDeCalculReader.readDureeCoEchelleRenteDes73_nombreAnnee(bc.getDureeCotiDes73()),
                    baseDeCalculReader.readDureeCoEchelleRenteDes73_nombreMois(bc.getDureeCotiDes73()));

            annoncePonctuelle9Eme.setDureeCotPourDetRAM(
                    baseDeCalculReader.readDureeCotPourDetRAM_nombreAnnee(bc.getDureeRevenuAnnuelMoyen()),
                    baseDeCalculReader.readDureeCotPourDetRAM_nombreMois(bc.getDureeRevenuAnnuelMoyen()));

            annoncePonctuelle9Eme.setNombreAnneeBTE(
                    baseDeCalculReader.readNombreAnneeBTE_valeurEntiere(bc.getNbrAnneeEducation()),
                    baseDeCalculReader.readNombreAnneeBTE_valeurDecimal(bc.getNbrAnneeEducation()));

            // Spécifique 9ème
            // REANN41
            annoncePonctuelle9Eme.setMontantRenteOrdinaireRemplace(
                    renteAccordeeReader.readMontantRenteOrdinaireRemplace(ra.getMontantRenteOrdiRemplacee()));
            // on ne le veut pas
            // annoncePonctuelle9Eme.setNouveauNoAssureAyantDroit(baseDeCalculReader.readNouveauNoAssureAyantDroit());
            // REAAL3B
            annoncePonctuelle9Eme
                    .setRevenuPrisEnCompte(baseDeCalculReader.readRevenuPrisEnCompte(bc.getRevenuPrisEnCompte()));
            annoncePonctuelle9Eme.setIsLimiteRevenu(baseDeCalculReader.readIsLimiteRevenu(bc.isLimiteRevenu()));
            annoncePonctuelle9Eme.setIsMinimumGaranti(baseDeCalculReader.readIsMinimumGaranti(bc.isMinimuGaranti()));
            // #
            // annoncePonctuelle9Eme.setRevenuAnnuelMoyen(baseDeCalculReader.readRevenuAnnuelMoyen(bc
            // .getRevenuAnnuelMoyen()));
            annoncePonctuelle9Eme.setBteMoyennePrisEnCompte(
                    baseDeCalculReader.readBonificationTacheEducative(bc.getBonificationTacheEducative()));

        }

        // Que pour des rente invalidité
        if (PRCodePrestationInvalidite.isCodePrestationInvalidite(ra.getCodePrestation())) {
            annoncePonctuelle9Eme.setAgeDebutInvalidite(bc.isInvaliditePrecoce());
            annoncePonctuelle9Eme.setDegreInvalidite(baseDeCalculReader.readDegreInvalidite(bc.getDegreInvalidite()));
        }

        annoncePonctuelle9Eme.setOfficeAICompetent(baseDeCalculReader.readOfficeAICompetent(bc.getCodeOfficeAi()));
        annoncePonctuelle9Eme.setSurvenanceEvenAssure(
                baseDeCalculReader.readSurvenanceEvenAssure(bc.getSurvenanceEvtAssAyantDroit()));

        annoncePonctuelle9Eme.setCodeInfirmite(baseDeCalculReader.readCodeInfirmite(bc.getCleInfirmiteAyantDroit()));
        annoncePonctuelle9Eme.setCodeAtteinteFonctionnelle(
                baseDeCalculReader.readCodeAtteinteFonctionnelle(bc.getCleInfirmiteAyantDroit()));

        // ---------------------------------------------------------------------------//
        RECreationAnnonceService serviceCreationAnnonce = new RECreationAnnonceService();
        try {
            REAnnoncesAPersister annoncesAPersister = serviceCreationAnnonce.creerAnnonce(annoncePonctuelle9Eme);
            // FOR DEBUG ONLY : impression des annonces : new REAnnonceRentePrinter().print(annoncePonctuelle9Eme,
            // annoncesAPersister);
            serviceCreationAnnonce.persisterAnnonce9EmeRevision(annoncesAPersister, Long.valueOf(ra.getId()),
                    dateDernierPaiement, session, transaction);

        } catch (IllegalArgumentException e) {
            throw new Exception("Des erreurs sont apparues lors de la persistence des annonces : " + e.toString());
        } catch (AnakinValidationException e) {
            e.printStackTrace();
            throw new Exception("Des erreurs sont apparues lors de la validation des annonces : " + e.toString());
        }

    }

    private void creerAnnoncePonctuelle10emeRevision(BSession session, BTransaction transaction,
            REAdaptationManuelleViewBean vb, RERenteAccordee ra, String dateDernierPaiement) throws Exception {

        if (ra == null) {
            throw new Exception("RERenteAccordee is null");
        }
        if (ra.isNew()) {
            String message = session.getCodeLibelle("ERREUR_IMPOSSIBLE_RETROUVER_RENTE_ACCORDEE_AVEC_ID");
            message = message.replace("{id}", vb.getIdPrestationAccordee());
            throw new Exception(message);
        }

        REBasesCalculDixiemeRevision bc = new REBasesCalculDixiemeRevision();
        bc.setSession(session);
        bc.setIdBasesCalcul(ra.getIdBaseCalcul());
        bc.retrieve(transaction);
        if (bc.isNew()) {
            throw new Exception("REBasesCalculDixiemeRevision is null");
        }

        REDemandeRenteManager drManager = new REDemandeRenteManager();
        drManager.setSession(session);
        drManager.setForIdRenteCalculee(bc.getIdRenteCalculee());
        drManager.find(transaction);

        REDemandeRente dr = (REDemandeRente) drManager.getFirstEntity();
        if (dr == null) {
            throw new Exception("REDemandeRente is null");
        }
        if (dr.isNew()) {
            String message = session.getCodeLibelle("ERREUR_IMPOSSIBLE_RETROUVER_DEMANDE_RENTE_AVEC_ID");
            message = message.replace("{id}", bc.getIdRenteCalculee());
            throw new Exception(message);
        }

        if (!"10".equals(bc.getDroitApplique())) {
            String message = session.getCodeLibelle("ERREUR_INCOHERENCE_DROIT_APPLIQUE_BASE_CALCUL_10EME_REVISION");
            message = message.replace("{0}", ra.getIdBaseCalcul());
            throw new Exception(message);
        }

        PRTiersWrapper tierBeneficiaire = PRTiersHelper.getTiersParId(session, ra.getIdTiersBeneficiaire());
        if (tierBeneficiaire == null) {
            String message = session.getCodeLibelle("ERREUR_IMPOSSIBLE_RETROUVER_TIERS_AVEC_ID");
            message = message.replace("{id}", ra.getIdTiersBeneficiaire());
            throw new Exception(message);
        }

        REAnnoncePonctuelle10EmeRevision annoncePonctuelle10Eme = new REAnnoncePonctuelle10EmeRevision();
        RECreationAnnonceGeneralReader converter = new RECreationAnnonceGeneralReader();
        RERenteAccordeeReader renteAccordeeReader = new RERenteAccordeeReader();
        REBaseDeCalcul10EmeRevisionReader baseDeCalculReader = new REBaseDeCalcul10EmeRevisionReader();
        // ---------------------------------------------------------------------------//

        annoncePonctuelle10Eme
                .setNumeroCaisse(converter.convertNumeroCaisse(session.getApplication().getProperty("noCaisse")));
        annoncePonctuelle10Eme
                .setNumeroAgence(converter.convertNumeroAgence(session.getApplication().getProperty("noAgence")));
        annoncePonctuelle10Eme.setUtilisateurPourReferenceCaisseInterne(session.getUserId().toUpperCase());

        annoncePonctuelle10Eme.setDebutDroit(new JADate(new JACalendarGregorian().addMonths(dateDernierPaiement, 1)));
        annoncePonctuelle10Eme.setFinDroit(null);
        annoncePonctuelle10Eme.setMoisRapport(new JADate(new JACalendarGregorian().addMonths(dateDernierPaiement, 1)));
        // rapport
        annoncePonctuelle10Eme.setCodeMutation(78);

        annoncePonctuelle10Eme.setNoAssAyantDroit(
                converter.convertFormatedNss(tierBeneficiaire.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL)));
        annoncePonctuelle10Eme.setMensualitePrestationsFrancs(
                converter.convertMensualitePrestationsFrancs(vb.getNouveauMontantPrestation()));

        // Valeurs récupéré depuis les rentes accordées
        annoncePonctuelle10Eme.setIdTiers(renteAccordeeReader.convertIdTiers(ra.getIdTiersBeneficiaire()));
        annoncePonctuelle10Eme.setRefugie(renteAccordeeReader.convertIsRefugie(ra.getCodeRefugie()));
        annoncePonctuelle10Eme.setGenrePrestation(renteAccordeeReader.convertGenrePrestation(ra.getCodePrestation()));
        annoncePonctuelle10Eme.setSupplementAjournement(
                renteAccordeeReader.convertSupplementAjournement(ra.getSupplementAjournement()));
        annoncePonctuelle10Eme.setDateDebutAnticipation(
                renteAccordeeReader.convertDateDebutAnticipation(ra.getDateDebutAnticipation()));
        annoncePonctuelle10Eme
                .setNbreAnneeAnticipation(renteAccordeeReader.convertNbreAnneeAnticipation(ra.getAnneeAnticipation()));
        annoncePonctuelle10Eme.setReductionAnticipation(
                renteAccordeeReader.convertReductionAnticipation(ra.getMontantReducationAnticipation()));
        annoncePonctuelle10Eme.setSurvivant(renteAccordeeReader.convertIsSurvivant(ra.getCodeSurvivantInvalide()));
        annoncePonctuelle10Eme.setReduction(renteAccordeeReader.convertReduction(ra.getReductionFauteGrave()));
        annoncePonctuelle10Eme.setCasSpecial1(renteAccordeeReader.convertCasSpecial(ra.getCodeCasSpeciaux1()));
        annoncePonctuelle10Eme.setCasSpecial2(renteAccordeeReader.convertCasSpecial(ra.getCodeCasSpeciaux2()));
        annoncePonctuelle10Eme.setCasSpecial3(renteAccordeeReader.convertCasSpecial(ra.getCodeCasSpeciaux3()));
        annoncePonctuelle10Eme.setCasSpecial4(renteAccordeeReader.convertCasSpecial(ra.getCodeCasSpeciaux4()));
        annoncePonctuelle10Eme.setCasSpecial5(renteAccordeeReader.convertCasSpecial(ra.getCodeCasSpeciaux5()));
        annoncePonctuelle10Eme.setDateRevocationAjournement(
                renteAccordeeReader.convertDateRevocationAjournement(ra.getDateRevocationAjournement()));
        annoncePonctuelle10Eme.setDureeAjournement(
                renteAccordeeReader.convertDureeAjournementValeurEntiere(ra.getDureeAjournement()),
                renteAccordeeReader.convertDureeAjournementValeurDecimal(ra.getDureeAjournement()));

        Integer etatCivil = renteAccordeeReader
                .convertEtatCivil(PRACORConst.csEtatCivilToAcorForRentes(ra.getCsEtatCivil()));
        annoncePonctuelle10Eme.setEtatCivil(etatCivil);

        // CantonEtatDomicile
        // On essaye avec le tier requérant
        PRTiersWrapper tierRequerantAdr = PRTiersHelper.getTiersAdresseDomicileParId(session, ra.getIdTiersBaseCalcul(),
                JACalendar.todayJJsMMsAAAA());
        if (tierRequerantAdr != null) {
            annoncePonctuelle10Eme.setCantonEtatDomicile(converter.convertCantonEtatDomicile(
                    PRACORConst.csCantonToAcor(tierRequerantAdr.getProperty(PRTiersWrapper.PROPERTY_ID_CANTON))));
        }

        // sinon on test avec le tier bénéficiaire
        else {
            PRTiersWrapper tierBeneficiaireAdr = PRTiersHelper.getTiersAdresseDomicileParId(session,
                    ra.getIdTiersBeneficiaire(), JACalendar.todayJJsMMsAAAA());
            if (tierBeneficiaireAdr != null) {
                annoncePonctuelle10Eme.setCantonEtatDomicile(converter.convertCantonEtatDomicile(PRACORConst
                        .csCantonToAcor(tierBeneficiaireAdr.getProperty(PRTiersWrapper.PROPERTY_ID_CANTON))));
            }
        }

        // CodePrestation codePrestation = CodePrestation.getCodePrestation(Integer.parseInt(vb.getGenrePrestation()));

        // PremierNoAssComplementaire
        boolean isNSS1Obligatoire = false;
        if (REGenrePrestationEnum.groupe1.contains(ra.getCodePrestation())) {
            if (etatCivil != null) {
                if (etatCivil == 2 || etatCivil == 3) {
                    isNSS1Obligatoire = true;
                }
            }
        }
        NSSComplementaire nssComplementaire = null;

        PRTiersWrapper tierComp1 = PRTiersHelper.getTiersParId(session, ra.getIdTiersComplementaire1());
        if (tierComp1 != null) {
            annoncePonctuelle10Eme.setPremierNoAssComplementaire(
                    converter.convertFormatedNss(tierComp1.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL)));
        } else {
            if (REGenrePrestationEnum.groupe5.contains(ra.getCodePrestation()) || isNSS1Obligatoire) {
                nssComplementaire = majTiersComplementaires(session, transaction, ra);
                annoncePonctuelle10Eme.setPremierNoAssComplementaire(
                        converter.convertFormatedNss(nssComplementaire.getNssComplementaire1()));
            }
        }

        // SecondNoAssComplementaire
        PRTiersWrapper tierComp2 = PRTiersHelper.getTiersParId(session, ra.getIdTiersComplementaire2());
        if (tierComp2 != null) {
            annoncePonctuelle10Eme.setSecondNoAssComplementaire(
                    converter.convertFormatedNss(tierComp2.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL)));
        } else {
            if (REGenrePrestationEnum.groupe5.contains(ra.getCodePrestation())) {
                nssComplementaire = majTiersComplementaires(session, transaction, ra);
                if (!JadeStringUtil.isBlankOrZero(nssComplementaire.getNssComplementaire2())) {
                    annoncePonctuelle10Eme.setSecondNoAssComplementaire(
                            converter.convertFormatedNss(nssComplementaire.getNssComplementaire2()));
                } else {
                    annoncePonctuelle10Eme.setSecondNoAssComplementaire(converter.convertFormatedNss(EMPTY_NSS));
                }
            }

        }

        // Si API Saisir le genre droit API
        if (isAPI(ra.getCodePrestation())) {
            REDemandeRenteAPI demAPI = new REDemandeRenteAPI();
            demAPI.setSession(session);
            demAPI.setIdDemandeRente(dr.getIdDemandeRente());
            demAPI.retrieve();
            if (demAPI.isNew()) {
                throw new Exception(
                        "Unable to retrieve the REDemandeRenteAPI with id [" + dr.getIdDemandeRente() + "]");
            }
            annoncePonctuelle10Eme
                    .setGenreDroitAPI(converter.convertGenreDroitAPI(session.getCode(ra.getCsGenreDroitApi())));
        }

        // Pour tous les autre type de rentes sauf API
        else {
            annoncePonctuelle10Eme.setEchelleRente(baseDeCalculReader.readEchelleRente(bc.getEchelleRente()));
            annoncePonctuelle10Eme.setCodeRevenuSplitte(bc.getIsPartageRevenuCalcul());
            annoncePonctuelle10Eme.setDureeCotManquante48_72(
                    baseDeCalculReader.readDureeCotManquante48_72(bc.getMoisAppointsAvant73()));
            annoncePonctuelle10Eme.setDureeCotManquante73_78(
                    baseDeCalculReader.readDureeCotManquante73_78(bc.getMoisAppointsDes73()));
            annoncePonctuelle10Eme.setRamDeterminant(baseDeCalculReader.readRamDeterminant(bc.getRevenuAnnuelMoyen()));
            annoncePonctuelle10Eme
                    .setAnneeCotClasseAge(baseDeCalculReader.readAnneeCotClasseAge(bc.getAnneeCotiClasseAge()));
            annoncePonctuelle10Eme.setAnneeNiveau(baseDeCalculReader.readAnneeNiveau(bc.getAnneeDeNiveau()));

            //
            annoncePonctuelle10Eme.setDureeCoEchelleRenteAv73(
                    baseDeCalculReader.readDureeCoEchelleRenteAv73_nombreAnnee(bc.getDureeCotiAvant73()),
                    baseDeCalculReader.readDureeCoEchelleRenteAv73_nombreMois(bc.getDureeCotiAvant73()));

            annoncePonctuelle10Eme.setDureeCoEchelleRenteDes73(
                    baseDeCalculReader.readDureeCoEchelleRenteDes73_nombreAnnee(bc.getDureeCotiDes73()),
                    baseDeCalculReader.readDureeCoEchelleRenteDes73_nombreMois(bc.getDureeCotiDes73()));

            annoncePonctuelle10Eme.setDureeCotPourDetRAM(
                    baseDeCalculReader.readDureeCotPourDetRAM_nombreAnnee(bc.getDureeRevenuAnnuelMoyen()),
                    baseDeCalculReader.readDureeCotPourDetRAM_nombreMois(bc.getDureeRevenuAnnuelMoyen()));

            annoncePonctuelle10Eme.setNombreAnneeBTE(
                    baseDeCalculReader.readNombreAnneeBTE_valeurEntiere(bc.getAnneeBonifTacheEduc()),
                    baseDeCalculReader.readNombreAnneeBTE_valeurDecimal(bc.getAnneeBonifTacheEduc()));

            annoncePonctuelle10Eme.setNbreAnneeBTA(
                    baseDeCalculReader.readNbreAnneeBTA_valeurEntiere(bc.getAnneeBonifTacheAssistance()),
                    baseDeCalculReader.readNbreAnneeBTA_valeurDecimal(bc.getAnneeBonifTacheAssistance()));

            annoncePonctuelle10Eme.setNbreAnneeBonifTrans(
                    baseDeCalculReader.readNbreAnneeBonifTrans_nombreAnnee(bc.getAnneeBonifTransitoire()),
                    baseDeCalculReader.readNbreAnneeBonifTrans_isDemiAnnee(bc.getAnneeBonifTransitoire()));
        }

        // Que pour des rente invalidité
        if (PRCodePrestationInvalidite.isCodePrestationInvalidite(ra.getCodePrestation())) {
            annoncePonctuelle10Eme.setAgeDebutInvalidite(bc.isInvaliditePrecoce());
            annoncePonctuelle10Eme.setDegreInvalidite(baseDeCalculReader.readDegreInvalidite(bc.getDegreInvalidite()));
        }

        annoncePonctuelle10Eme.setOfficeAICompetent(baseDeCalculReader.readOfficeAICompetent(bc.getCodeOfficeAi()));
        annoncePonctuelle10Eme.setSurvenanceEvenAssure(
                baseDeCalculReader.readSurvenanceEvenAssure(bc.getSurvenanceEvtAssAyantDroit()));

        annoncePonctuelle10Eme.setCodeInfirmite(baseDeCalculReader.readCodeInfirmite(bc.getCleInfirmiteAyantDroit()));
        annoncePonctuelle10Eme.setCodeAtteinteFonctionnelle(
                baseDeCalculReader.readCodeAtteinteFonctionnelle(bc.getCleInfirmiteAyantDroit()));

        // ---------------------------------------------------------------------------//
        RECreationAnnonceService serviceCreationAnnonce = new RECreationAnnonceService();
        try {
            REAnnoncesAPersister annoncesAPersister = serviceCreationAnnonce.creerAnnonce(annoncePonctuelle10Eme);
            // FOR DEBUG ONLY : impression des annonces : new REAnnonceRentePrinter().print(annoncePonctuelle10Eme,
            // annoncesAPersister);

            serviceCreationAnnonce.persisterAnnonce10EmeRevision(annoncesAPersister, Long.valueOf(ra.getId()),
                    dateDernierPaiement, session, transaction);
        } catch (IllegalArgumentException e) {
            throw new Exception("Des erreurs sont apparues lors de la persistence des annonces : " + e.toString());
        } catch (AnakinValidationException e) {
            e.printStackTrace();
            throw new Exception("Des erreurs sont apparues lors de la validation des annonces : " + e.toString());
        }
    }

    /**
     * @param transaction
     * @param vb
     * @param ra
     * @param session
     * @param dateRapportEtFin
     * @param tier
     * @throws Exception
     */
    private void creerAnnonceDiminution10emeRevision(BSession session, BTransaction transaction,
            REAdaptationManuelleViewBean vb, RERenteAccordee ra, String dateRapportEtFin, PRTiersWrapper tier)
            throws Exception {
        // Annonce de diminution
        String idAnnonce = "";

        REAnnoncesAugmentationModification10Eme ann46dim = new REAnnoncesAugmentationModification10Eme();
        ann46dim.setSession(session);
        ann46dim.setCodeApplication("46");
        ann46dim.setCodeEnregistrement01("01");
        ann46dim.setNumeroCaisse(session.getApplication().getProperty("noCaisse"));
        ann46dim.setNumeroAgence(session.getApplication().getProperty("noAgence"));
        ann46dim.setNumeroAnnonce("");
        ann46dim.setReferenceCaisseInterne("DIM" + session.getUserId().toUpperCase());

        ann46dim.setNoAssAyantDroit(NSUtil.unFormatAVS(tier.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL)));

        ann46dim.setGenrePrestation(ra.getCodePrestation());

        int lengthMontant = String.valueOf(new FWCurrency(vb.getAncienMontantPrestation()).intValue()).length();
        int nbZeroAajouter = 5 - lengthMontant;

        String montant = String.valueOf(new FWCurrency(vb.getAncienMontantPrestation()).intValue());

        for (int i = 0; i < nbZeroAajouter; i++) {
            montant = "0" + montant;
        }
        ann46dim.setMensualitePrestationsFrancs(montant);
        ann46dim.setFinDroit(PRDateFormater
                .convertDate_AAAAMM_to_MMAA(PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(dateRapportEtFin)));

        // Mois rapport
        JADate moisRapport = new JADate(new JACalendarGregorian().addMonths(dateRapportEtFin, 1));
        String moisRapportFormatte;
        if (moisRapport.getMonth() > 9) {
            moisRapportFormatte = String.valueOf(moisRapport.getYear()) + String.valueOf(moisRapport.getMonth());
        } else {
            moisRapportFormatte = String.valueOf(moisRapport.getYear()) + "0" + String.valueOf(moisRapport.getMonth());

        }
        moisRapportFormatte = PRDateFormater.convertDate_AAAAMM_to_MMAA(moisRapportFormatte);
        ann46dim.setMoisRapport(moisRapportFormatte);

        ann46dim.setCodeMutation("77");
        ann46dim.setEtat(IREAnnonces.CS_ETAT_OUVERT);
        ann46dim.setIdTiers(tier.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
        ann46dim.add(transaction);

        idAnnonce = ann46dim.getIdAnnonce();

        // Création de l'annonce de rente pour la liaison entre les ra et les annonces
        REAnnonceRente annonceRente = new REAnnonceRente();
        annonceRente.setSession(session);
        annonceRente.setIdAnnonceHeader(idAnnonce);
        annonceRente.setCsEtat(IREAnnonces.CS_ETAT_OUVERT);
        annonceRente.setCsTraitement(IREAnnonces.CS_CODE_EN_COURS);
        annonceRente.setIdRenteAccordee(ra.getIdPrestationAccordee());
        annonceRente.add(transaction);
    }

    /**
     * Créer les annonces pour la 9ème révision.</br>
     * Une annonce de diminution doit être créer et une ponctuelle. Les règles sont les suivantes :</br>
     * </br>
     * Annonce de diminution existante ?</br>
     * Si non, créer</br>
     * Si oui, ne rien faire</br>
     * </br>
     * Créer annonce ponctuelle 9ème</br>
     *
     * @param transaction
     * @param vb
     * @param ra
     * @param session
     * @param dateRapportEtFin
     * @param tier
     * @throws Exception
     */
    private void creerAnnonces9emeRevision(BSession session, BTransaction transaction, REAdaptationManuelleViewBean vb,
            PRTiersWrapper tier, RERenteAccordee ra, String dateDernierPaiement) throws Exception {

        // On va d'abord contrôler si une annonce de diminution à déjà été créée.
        int genrePrestation = 0;
        try {
            genrePrestation = Integer.valueOf(vb.getGenrePrestation());
        } catch (NumberFormatException e) {
            String message = session
                    .getLabel("ERREUR_IMPOSSIBLE_RETROUVER_LE_GENRE_DE_PRESTATION_RENTE_ACCORDEE_AVEC_ID");
            message = message.replace("{0}", vb.getGenrePrestation());
            message = message.replace("{1}", vb.getIdPrestationAccordee());
            throw new Exception(message);
        }

        boolean annonceDiminutionExistante = isAnnonceDiminution9emeRevisionExistante(session, transaction,
                genrePrestation, dateDernierPaiement, tier);

        if (!annonceDiminutionExistante) {
            creerAnnonceDiminution9emeRevision(session, transaction, vb, ra, dateDernierPaiement, tier);
        }
        // Suppression des annonces ponctuelles si déjà existante
        suppressionAnnoncePonctuelle9emeRevisionExistante(session, transaction, genrePrestation, dateDernierPaiement,
                tier);

        // Créer l'annonce ponctuelle dans tous les cas...
        creerAnnoncePonctuelle9emeRevision(session, transaction, vb, ra, dateDernierPaiement);

    }

    /**
     * @param transaction
     * @param vb
     * @param ra
     * @param session
     * @param dateRapportEtFin
     * @param tier
     * @throws Exception
     */
    private void creerAnnonceDiminution9emeRevision(BSession session, BTransaction transaction,
            REAdaptationManuelleViewBean vb, RERenteAccordee ra, String dateRapportEtFin, PRTiersWrapper tier)
            throws Exception {
        String idAnnonce = "";

        REAnnoncesAugmentationModification9Eme ann43dim = new REAnnoncesAugmentationModification9Eme();
        ann43dim.setSession(session);
        ann43dim.setCodeApplication("43");
        ann43dim.setCodeEnregistrement01("01");
        ann43dim.setNumeroCaisse(session.getApplication().getProperty("noCaisse"));
        ann43dim.setNumeroAgence(session.getApplication().getProperty("noAgence"));
        ann43dim.setNumeroAnnonce("");
        ann43dim.setReferenceCaisseInterne("DIM" + session.getUserId().toUpperCase());

        ann43dim.setNoAssAyantDroit(NSUtil.unFormatAVS(tier.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL)));

        ann43dim.setGenrePrestation(ra.getCodePrestation());

        int lengthMontant = String.valueOf(new FWCurrency(vb.getAncienMontantPrestation()).intValue()).length();
        int nbZeroAajouter = 5 - lengthMontant;

        String montant = String.valueOf(new FWCurrency(vb.getAncienMontantPrestation()).intValue());

        for (int i = 0; i < nbZeroAajouter; i++) {
            montant = "0" + montant;
        }

        ann43dim.setMensualitePrestationsFrancs(montant);
        ann43dim.setFinDroit(PRDateFormater
                .convertDate_AAAAMM_to_MMAA(PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(dateRapportEtFin)));

        // Mois rapport
        JADate moisRapport = new JADate(new JACalendarGregorian().addMonths(dateRapportEtFin, 1));
        String moisRapportFormatte;
        if (moisRapport.getMonth() > 9) {
            moisRapportFormatte = String.valueOf(moisRapport.getYear()) + String.valueOf(moisRapport.getMonth());
        } else {
            moisRapportFormatte = String.valueOf(moisRapport.getYear()) + "0" + String.valueOf(moisRapport.getMonth());
        }
        moisRapportFormatte = PRDateFormater.convertDate_AAAAMM_to_MMAA(moisRapportFormatte);
        ann43dim.setMoisRapport(moisRapportFormatte);

        ann43dim.setCodeMutation("77");
        ann43dim.setIdTiers(tier.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
        ann43dim.setEtat(IREAnnonces.CS_ETAT_OUVERT);

        ann43dim.add(transaction);

        idAnnonce = ann43dim.getIdAnnonce();

        // Création de l'annonce de rente pour la liaison entre les ra et les annonces
        REAnnonceRente annonceRente = new REAnnonceRente();
        annonceRente.setSession(session);
        annonceRente.setIdAnnonceHeader(idAnnonce);
        annonceRente.setCsEtat(IREAnnonces.CS_ETAT_OUVERT);
        annonceRente.setCsTraitement(IREAnnonces.CS_CODE_EN_COURS);
        annonceRente.setIdRenteAccordee(ra.getIdPrestationAccordee());
        annonceRente.add(transaction);
    }

    /**
     * @param transaction
     * @param vb
     * @param session
     * @param tier
     * @param annonceDiminutionExistante
     * @return
     * @throws Exception
     */
    private void suppressionAnnoncePonctuelle9emeRevisionExistante(BSession session, BTransaction transaction,
            int genrePrestation, String dateDernierPaiement, PRTiersWrapper tier) throws Exception {

        String moisRapport = JadeDateUtil.addMonths("01." + dateDernierPaiement, 1);
        moisRapport = moisRapport.substring(3);

        REAnnoncesAugmentationModification9EmeManager annAug9Mgr = new REAnnoncesAugmentationModification9EmeManager();
        annAug9Mgr.setForMoisRapport(moisRapport);
        annAug9Mgr.setForNss(NSUtil.unFormatAVS(tier.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL)));
        annAug9Mgr.setForCodePrestation(String.valueOf(genrePrestation));
        annAug9Mgr.setSession(session);
        annAug9Mgr.find();

        if (annAug9Mgr.size() > 0) {
            for (@SuppressWarnings("unchecked")
            Iterator<REAnnoncesAugmentationModification9Eme> iterator = annAug9Mgr.iterator(); iterator.hasNext();) {
                REAnnoncesAugmentationModification9Eme ann9eme = iterator.next();
                if (JadeStringUtil.isBlankOrZero(ann9eme.getFinDroit()) && ann9eme.getCodeMutation().equals("78")) {
                    ann9eme.delete(transaction);
                }
            }
        }
    }

    /**
     * @param transaction
     * @param vb
     * @param session
     * @param tier
     * @param annonceDiminutionExistante
     * @return
     * @throws Exception
     */
    private boolean isAnnonceDiminution9emeRevisionExistante(BSession session, BTransaction transaction,
            int genrePrestation, String dateDernierPaiement, PRTiersWrapper tier) throws Exception {

        // Mois rapport
        JADate moisRapport = new JADate(new JACalendarGregorian().addMonths(dateDernierPaiement, 1));
        String moisRapportFormatte = String.valueOf(moisRapport.getMonth()) + "."
                + String.valueOf(moisRapport.getYear());

        boolean annonceDiminutionExistante = false;
        REAnnoncesAugmentationModification9EmeManager annAug9Mgr = new REAnnoncesAugmentationModification9EmeManager();
        annAug9Mgr.setForMoisRapport(moisRapportFormatte);
        annAug9Mgr.setForNss(NSUtil.unFormatAVS(tier.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL)));
        annAug9Mgr.setForCodePrestation(String.valueOf(genrePrestation));
        annAug9Mgr.setSession(session);
        annAug9Mgr.find();

        if (annAug9Mgr.size() > 0) {
            for (@SuppressWarnings("unchecked")
            Iterator<REAnnoncesAugmentationModification9Eme> iterator = annAug9Mgr.iterator(); iterator.hasNext();) {
                REAnnoncesAugmentationModification9Eme ann9eme = iterator.next();
                if (!JadeStringUtil.isBlankOrZero(ann9eme.getFinDroit()) && ann9eme.getCodeMutation().equals("77")) {
                    annonceDiminutionExistante = true;
                }
            }
        }
        return annonceDiminutionExistante;
    }

    private boolean isAPI(String codeRA) {
        if (REGenrePrestationEnum.groupe2.contains(codeRA)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Mise a jours des tiers complementaire a l'aide de la situation familiale et de du tiersBeneficiaire de la RA
     *
     * Pour ayant droit enfant =======================
     *
     * rentes 14, 24, 34, 54, 74, 16, 26, 56, 76 -> tiersComplementaire1 pere (si conj. inc. 00000000000) ->
     * tiersComplementaire2 mere
     *
     * rentes 15, 25, 35, 45, 55, 75 -> tiersComplementaire1 mere -> tiersComplementaire2 pere (si conj. inc.
     * 00000000000)
     *
     * Autres ======
     *
     * 13, 23, 33, 53, 73 -> tiersComplementaire1 l'autre conjoint
     *
     *
     * 10, 20, 50, 70 -> si celibataire -> tiersComplementaire1 rien -> si marie ou veuf -> tiersComplementaire1 l'autre
     * conjoint -> si divorce -> tiersComplementaire1 le dernier ex-conjoint
     *
     *
     * @param session
     * @param transaction
     * @param ra
     */
    private NSSComplementaire majTiersComplementaires(BSession session, BTransaction transaction, RERenteAccordee ra)
            throws Exception {

        NSSComplementaire nssComplementaire = new NSSComplementaire();

        ISFSituationFamiliale sf = SFSituationFamilialeFactory.getSituationFamiliale(session,
                ISFSituationFamiliale.CS_DOMAINE_RENTES, ra.getIdTiersBeneficiaire());
        String csCantonDomicile = null;

        String idMembreFamille = "";
        ISFMembreFamilleRequerant[] mf = sf.getMembresFamille(ra.getIdTiersBeneficiaire());
        for (int i = 0; i < mf.length; i++) {
            ISFMembreFamilleRequerant membre = mf[i];
            // On récupère le bénéficiaire en tant que membre de famille
            if (ra.getIdTiersBeneficiaire().equals(membre.getIdTiers())) {
                csCantonDomicile = membre.getCsCantonDomicile();
                if (csCantonDomicile == null) {
                    csCantonDomicile = session.getCode(membre.getCsNationalite());
                }
                idMembreFamille = membre.getIdMembreFamille();
                break;
            }
        }


        if(ra.contientCodeCasSpecial("60")){

            SFApercuEnfant enf = new SFApercuEnfant();
            enf.setSession(session);
            enf.setIdMembreFamille(idMembreFamille);
            enf.retrieve();

            SFMembreFamille parentLPart1 = null;
            SFMembreFamille parentLPart2 = null;
            if (enf.getPere() != null) {
                if (ISFSituationFamiliale.ID_MEMBRE_FAMILLE_CONJOINT_INCONNU
                        .equals(enf.getPere().getIdMembreFamille())) {
                    // oui mais on a pas de tiers pour le conjoint inconnu
                } else {
                    parentLPart1 = enf.getMere();
                }
            }

            // tiersComplementaire2 mere
            if (enf.getMere() != null) {
                if (ISFSituationFamiliale.ID_MEMBRE_FAMILLE_CONJOINT_INCONNU
                        .equals(enf.getMere().getIdMembreFamille())) {
                    // oui mais on a pas de tiers pour le conjoint inconnu
                } else {
                    parentLPart2 = enf.getPere();
                }
            }
            if((parentLPart1 != null && parentLPart2 != null) && parentLPart1.getCsSexe().equals(parentLPart2.getCsSexe()) ){
                if(parentLPart1.getIdTiers().equals(ra.getIdTiersBaseCalcul())){
                    PRTiersWrapper tw = PRTiersHelper.getTiersParId(session,parentLPart1.getIdTiers());
                    nssComplementaire.setNssComplementaire1(tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));
                    tw = PRTiersHelper.getTiersParId(session,parentLPart2.getIdTiers());
                    nssComplementaire.setNssComplementaire2(tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));
                    return nssComplementaire;
                }
                if(parentLPart2.getIdTiers().equals(ra.getIdTiersBaseCalcul())){
                    PRTiersWrapper tw = PRTiersHelper.getTiersParId(session,parentLPart2.getIdTiers());
                    nssComplementaire.setNssComplementaire1(tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));
                    tw = PRTiersHelper.getTiersParId(session,parentLPart1.getIdTiers());
                    nssComplementaire.setNssComplementaire2(tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));
                    return nssComplementaire;
                }
            }

        }
        if (REGenresPrestations.GENRE_14.equals(ra.getCodePrestation())
                || REGenresPrestations.GENRE_24.equals(ra.getCodePrestation())
                || REGenresPrestations.GENRE_34.equals(ra.getCodePrestation())
                || REGenresPrestations.GENRE_36.equals(ra.getCodePrestation())
                || REGenresPrestations.GENRE_54.equals(ra.getCodePrestation())
                || REGenresPrestations.GENRE_74.equals(ra.getCodePrestation())
                || REGenresPrestations.GENRE_16.equals(ra.getCodePrestation())
                || REGenresPrestations.GENRE_26.equals(ra.getCodePrestation())
                || REGenresPrestations.GENRE_56.equals(ra.getCodePrestation())
                || REGenresPrestations.GENRE_76.equals(ra.getCodePrestation())) {

            SFApercuEnfant enf = new SFApercuEnfant();
            enf.setSession(session);
            enf.setIdMembreFamille(idMembreFamille);
            enf.retrieve();

            // tiersComplementaire1 pere (si conj. inc. 00000000000)
            if (enf.getPere() != null) {
                enf.getPere().getCsCantonDomicile();
                if (ISFSituationFamiliale.ID_MEMBRE_FAMILLE_CONJOINT_INCONNU
                        .equals(enf.getPere().getIdMembreFamille())) {
                    // oui mais on a pas de tiers pour le conjoint inconnu
                } else {
                    PRTiersWrapper tw = PRTiersHelper.getTiersParId(session, enf.getPere().getIdTiers());
                    nssComplementaire.setNssComplementaire1(tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));
                }
            }

            // tiersComplementaire2 mere
            if (enf.getMere() != null) {
                enf.getMere().getCsCantonDomicile();
                if (ISFSituationFamiliale.ID_MEMBRE_FAMILLE_CONJOINT_INCONNU
                        .equals(enf.getMere().getIdMembreFamille())) {
                    // oui mais on a pas de tiers pour le conjoint inconnu
                } else {
                    PRTiersWrapper tw = PRTiersHelper.getTiersParId(session, enf.getMere().getIdTiers());
                    nssComplementaire.setNssComplementaire2(tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));
                }
            }

            // Pour ayant droit enfant
        } else if (REGenresPrestations.GENRE_15.equals(ra.getCodePrestation())
                || REGenresPrestations.GENRE_25.equals(ra.getCodePrestation())
                || REGenresPrestations.GENRE_35.equals(ra.getCodePrestation())
                || REGenresPrestations.GENRE_45.equals(ra.getCodePrestation())
                || REGenresPrestations.GENRE_55.equals(ra.getCodePrestation())
                || REGenresPrestations.GENRE_75.equals(ra.getCodePrestation())) {

            SFApercuEnfant enf = new SFApercuEnfant();
            enf.setSession(session);
            enf.setIdMembreFamille(idMembreFamille);
            enf.retrieve();

            // tiersComplementaire1 mere
            if (enf.getMere() != null) {
                enf.getMere().getCsCantonDomicile();
                if (ISFSituationFamiliale.ID_MEMBRE_FAMILLE_CONJOINT_INCONNU
                        .equals(enf.getMere().getIdMembreFamille())) {
                    // oui mais on a pas de tiers pour le conjoint inconnu
                } else {
                    if (!JadeStringUtil.isBlankOrZero(enf.getMere().getIdTiers())) {
                        PRTiersWrapper tw = PRTiersHelper.getTiersParId(session, enf.getMere().getIdTiers());
                        nssComplementaire.setNssComplementaire1(tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));
                    }
                }
            }

            // tiersComplementaire2 pere (si conj. inc. 00000000000)
            if (enf.getPere() != null) {
                enf.getPere().getCsCantonDomicile();
                if (ISFSituationFamiliale.ID_MEMBRE_FAMILLE_CONJOINT_INCONNU
                        .equals(enf.getPere().getIdMembreFamille())) {
                    // oui mais on a pas de tiers pour le conjoint inconnu
                } else {
                    if (!JadeStringUtil.isBlankOrZero(enf.getPere().getIdTiers())) {
                        PRTiersWrapper tw = PRTiersHelper.getTiersParId(session, enf.getPere().getIdTiers());
                        nssComplementaire.setNssComplementaire2(tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));
                    }

                }
            }

        } else if (REGenresPrestations.GENRE_13.equals(ra.getCodePrestation())
                || REGenresPrestations.GENRE_23.equals(ra.getCodePrestation())
                || REGenresPrestations.GENRE_33.equals(ra.getCodePrestation())
                || REGenresPrestations.GENRE_53.equals(ra.getCodePrestation())
                || REGenresPrestations.GENRE_73.equals(ra.getCodePrestation())) {

            // Reprendre la situation familiale
            sf = SFSituationFamilialeFactory.getSituationFamiliale(session, ISFSituationFamiliale.CS_DOMAINE_RENTES,
                    ra.getIdTiersBeneficiaire());

            // Reprendre les relations familiales
            ISFRelationFamiliale[] rfs = sf.getRelationsConjoints(ra.getIdTiersBeneficiaire(),
                    JACalendar.todayJJsMMsAAAA());

            // tiers complementaire1 = l'autre conjoint
            for (int j = 0; (rfs != null) && (j < rfs.length); j++) {

                ISFRelationFamiliale rf = rfs[j];

                String idMbrFamille1 = rf.getIdMembreFamilleHomme();
                ISFMembreFamille mf1 = sf.getMembreFamille(idMbrFamille1);
                String idTiers1 = mf1.getIdTiers();

                if (!idTiers1.equals(ra.getIdTiersBeneficiaire())) {
                    PRTiersWrapper tw = PRTiersHelper.getTiersParId(session, idTiers1);
                    nssComplementaire.setNssComplementaire1(tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));
                } else {
                    String idMbrFamille2 = rf.getIdMembreFamilleFemme();
                    ISFMembreFamille mf2 = sf.getMembreFamille(idMbrFamille2);
                    String idTiers2 = mf2.getIdTiers();

                    if (!idTiers2.equals(ra.getIdTiersBeneficiaire())) {
                        PRTiersWrapper tw = PRTiersHelper.getTiersParId(session, idTiers2);
                        nssComplementaire.setNssComplementaire1(tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));
                    }
                }
                // BZ 9926
                break;
            }

        } else if (REGenresPrestations.GENRE_10.equals(ra.getCodePrestation())
                || REGenresPrestations.GENRE_20.equals(ra.getCodePrestation())
                || REGenresPrestations.GENRE_50.equals(ra.getCodePrestation())
                || REGenresPrestations.GENRE_70.equals(ra.getCodePrestation())) {

            // si marie ou veuf -> tiersComplementaire1 l'autre conjoint
            // si divorce -> tiersComplementaire1 le dernier ex-conjoint

            // Reprendre la situation familiale
            sf = SFSituationFamilialeFactory.getSituationFamiliale(session, ISFSituationFamiliale.CS_DOMAINE_RENTES,
                    ra.getIdTiersBeneficiaire());

            // Reprendre les relations familiales
            ISFRelationFamiliale[] rfs = sf.getRelationsConjoints(ra.getIdTiersBeneficiaire(),
                    JACalendar.todayJJsMMsAAAA());

            // tiers complementaire1 = l'autre conjoint
            for (int j = 0; (rfs != null) && (j < rfs.length); j++) {

                ISFRelationFamiliale rf = rfs[j];

                String idMbrFamille1 = rf.getIdMembreFamilleHomme();
                ISFMembreFamille mf1 = sf.getMembreFamille(idMbrFamille1);
                String idTiers1 = mf1.getIdTiers();

                if (!idTiers1.equals(ra.getIdTiersBeneficiaire())) {
                    PRTiersWrapper tw = PRTiersHelper.getTiersParId(session, idTiers1);
                    nssComplementaire.setNssComplementaire1(tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));
                } else {
                    String idMbrFamille2 = rf.getIdMembreFamilleFemme();
                    ISFMembreFamille mf2 = sf.getMembreFamille(idMbrFamille2);
                    String idTiers2 = mf2.getIdTiers();

                    if (!idTiers2.equals(ra.getIdTiersBeneficiaire())) {
                        PRTiersWrapper tw = PRTiersHelper.getTiersParId(session, idTiers2);
                        nssComplementaire.setNssComplementaire1(tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));
                    }
                }
                // BZ 9926
                break;
            }

            // si celibataire -> tiersComplementaire1 rien
            if (rfs != null) {
                if (rfs.length == 0) {
                    nssComplementaire.setNssComplementaire1(null);
                }
            }
        }
        return nssComplementaire;
    }

}
