package globaz.corvus.process;

import globaz.commons.nss.NSUtil;
import globaz.corvus.anakin.REAnakinParser;
import globaz.corvus.api.annonces.IREAnnonces;
import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.corvus.api.diminution.IREDiminution;
import globaz.corvus.api.lots.IRELot;
import globaz.corvus.application.REApplication;
import globaz.corvus.db.annonces.REAnnonceRente;
import globaz.corvus.db.annonces.REAnnoncesDiminution10Eme;
import globaz.corvus.db.annonces.REAnnoncesDiminution9Eme;
import globaz.corvus.db.basescalcul.REBasesCalcul;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.demandes.REPeriodeAPI;
import globaz.corvus.db.demandes.REPeriodeAPIManager;
import globaz.corvus.db.demandes.REPeriodeInvalidite;
import globaz.corvus.db.demandes.REPeriodeInvaliditeManager;
import globaz.corvus.db.lots.RELot;
import globaz.corvus.db.recap.access.RERecapInfo;
import globaz.corvus.db.recap.access.RERecapInfoManager;
import globaz.corvus.db.rentesaccordees.REInformationsComptabilite;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.db.rentesaccordees.RERenteCalculee;
import globaz.corvus.utils.REPmtMensuel;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BIMessage;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.process.journal.CAProcessAnnulerJournal;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRAssert;
import globaz.prestation.tools.PRDateFormater;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;
import ch.admin.ofit.anakin.donnee.AnnonceErreur;

public class REAnnulerDiminutionRenteAccordeeProcess extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String codeMutation = "";

    private String dateDebutDroit = "";
    private String dateFinDroit = "";
    private String dateFinDroitModifiee = "";
    private String emailObject = "";
    private String errorMsg = "";
    private String genreRente = "";
    private String idRenteAccordee = "";
    private String idTiersBeneficiaire = "";

    private String montant = "";

    private String tiersBeneficiaireInfo = "";

    /**
     * Constructor for REImprimerDecisionProcess.
     */
    public REAnnulerDiminutionRenteAccordeeProcess() throws Exception {
        this(new BSession(REApplication.DEFAULT_APPLICATION_CORVUS));
    }

    /**
     * Constructor for REImprimerDecisionProcess.
     * 
     * @param parent
     */
    public REAnnulerDiminutionRenteAccordeeProcess(BProcess parent) throws Exception {
        super(parent);
    }

    /**
     * Constructor for REImprimerDecisionProcess.
     * 
     * @param session
     */
    public REAnnulerDiminutionRenteAccordeeProcess(BSession session) throws Exception {
        super(session);
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {

        boolean isError = false;

        BTransaction transaction = getTransaction();

        try {
            _validate();

            if (!JadeStringUtil.isBlankOrZero(getErrorMsg())) {
                getMemoryLog().logMessage(getErrorMsg(), FWMessage.ERREUR, "REAnnulerDiminutionRenteAccordeeProcess");
                throw new Exception(getErrorMsg());
            }
            doTraitement(transaction);

            if (getSession().hasErrors()) {
                throw new Exception(getSession().getErrors().toString());
            }

            emailObject = getSession().getLabel("MAIL_TITRE_ANNULERDIMINUTIONRENTEPROCESS");

        } catch (Exception e) {
            this._addError(transaction, e.toString());
            setSendCompletionMail(true);
            isError = true;
            this._addError(getSession().getLabel("ERREUR_ANNULER_DIMINUTION_RA") + " : " + e.toString());
            getMemoryLog().logMessage(getSession().getLabel("ERREUR_ANNULER_DIMINUTION_RA") + " : " + e.toString(),
                    FWMessage.ERREUR, "REAnnulerDiminutionRenteAccordeeProcess");
            emailObject = getSession().getCodeLibelle(FWMessage.ERREUR) + " : " + emailObject;
            return false;
        } finally {
            if (transaction != null) {
                try {
                    if (isError || transaction.hasErrors() || transaction.isRollbackOnly()) {
                        transaction.rollback();
                    } else {
                        transaction.commit();
                    }
                } finally {
                    transaction.closeTransaction();
                }
            }
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_validate()
     */
    @Override
    protected void _validate() throws Exception {

        setSendCompletionMail(false);
        setSendMailOnError(true);
        setControleTransaction(getTransaction() == null);
    }

    private void createAnnonceDiminution(BITransaction transaction, String codeApplication, RERenteAccordee ra)
            throws Exception {

        String idAnnonce = "";
        PRTiersWrapper tiers = PRTiersHelper.getTiersParId(getSession(), ra.getIdTiersBeneficiaire());

        if (null == tiers) {
            throw new Exception("Tier introuvable (REDiminutionRenteAccordeeProcess)");
        }

        // dateMoisRapport = date du dernier paiement + 1
        // mois.
        String dateMoisRapport = REPmtMensuel.getDateDernierPmt(getSession());
        JADate dmr = new JADate(dateMoisRapport);
        JACalendar cal = new JACalendarGregorian();
        dmr = cal.addMonths(dmr, 1);

        dateMoisRapport = PRDateFormater.convertDate_AAAAMMJJ_to_AAAAMM(dmr.toStrAMJ());
        dateMoisRapport = PRDateFormater.convertDate_AAAAMM_to_MMAA(dateMoisRapport);

        if ("45".equals(codeApplication)) {
            // Créer annonce pour 10 ème révision si bc =
            // 10ème révision
            REAnnoncesDiminution10Eme annonce10Eme = new REAnnoncesDiminution10Eme();
            annonce10Eme.setSession(getSession());
            annonce10Eme.setCodeApplication("45");
            annonce10Eme.setCodeEnregistrement01("01");
            annonce10Eme.setNumeroCaisse(getSession().getApplication().getProperty("noCaisse"));
            annonce10Eme.setNumeroAgence(getSession().getApplication().getProperty("noAgence"));
            annonce10Eme.setNumeroAnnonce("");
            annonce10Eme.setReferenceCaisseInterne("DIM" + getSession().getUserId().toUpperCase());
            annonce10Eme.setNoAssAyantDroit(NSUtil.unFormatAVS(tiers
                    .getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL)));
            annonce10Eme.setIdTiers(tiers.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
            annonce10Eme.setGenrePrestation(ra.getCodePrestation());

            int lengthMontant = String.valueOf(new FWCurrency(ra.getMontantPrestation()).intValue()).length();
            int nbZeroAajouter = 5 - lengthMontant;

            String montant = String.valueOf(new FWCurrency(ra.getMontantPrestation()).intValue());

            for (int j = 0; j < nbZeroAajouter; j++) {
                montant = "0" + montant;
            }

            annonce10Eme.setMensualitePrestationsFrancs(montant);
            annonce10Eme.setFinDroit(PRDateFormater.convertDate_AAAAMM_to_MMAA(PRDateFormater
                    .convertDate_MMxAAAA_to_AAAAMM(ra.getDateFinDroit())));
            annonce10Eme.setMoisRapport(dateMoisRapport);
            annonce10Eme.setCodeMutation(getCodeMutation());
            annonce10Eme.setEtat(IREAnnonces.CS_ETAT_OUVERT);

            Enumeration erreurs = REAnakinParser.getInstance().parse(getSession(), annonce10Eme, null, dateMoisRapport);
            StringBuffer buff = new StringBuffer();
            while ((erreurs != null) && erreurs.hasMoreElements()) {
                AnnonceErreur erreur = (AnnonceErreur) erreurs.nextElement();
                buff.append(erreur.getMessage()).append("\n");
            }
            if (buff.length() > 0) {
                throw new Exception(buff.toString());
            }

            annonce10Eme.add(transaction);
            idAnnonce = annonce10Eme.getIdAnnonce();
        } else if ("42".equals(codeApplication)) {
            // Créer annonce pour 9 ème révision si bc =
            // 9ème révision

            REAnnoncesDiminution9Eme annonce9Eme = new REAnnoncesDiminution9Eme();
            annonce9Eme.setSession(getSession());
            annonce9Eme.setCodeApplication("42");
            annonce9Eme.setCodeEnregistrement01("01");
            annonce9Eme.setNumeroCaisse(getSession().getApplication().getProperty("noCaisse"));
            annonce9Eme.setNumeroAgence(getSession().getApplication().getProperty("noAgence"));
            annonce9Eme.setNumeroAnnonce("");
            annonce9Eme.setReferenceCaisseInterne("DIM" + getSession().getUserId().toUpperCase());
            annonce9Eme
                    .setNoAssAyantDroit(NSUtil.unFormatAVS(tiers.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL)));
            annonce9Eme.setIdTiers(tiers.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
            annonce9Eme.setGenrePrestation(ra.getCodePrestation());

            int lengthMontant = String.valueOf(new FWCurrency(ra.getMontantPrestation()).intValue()).length();
            int nbZeroAajouter = 5 - lengthMontant;

            String montant = String.valueOf(new FWCurrency(ra.getMontantPrestation()).intValue());
            for (int j = 0; j < nbZeroAajouter; j++) {
                montant = "0" + montant;
            }
            annonce9Eme.setMensualitePrestationsFrancs(montant);
            annonce9Eme.setFinDroit(PRDateFormater.convertDate_AAAAMM_to_MMAA(PRDateFormater
                    .convertDate_MMxAAAA_to_AAAAMM(ra.getDateFinDroit())));
            annonce9Eme.setMoisRapport(dateMoisRapport);
            annonce9Eme.setCodeMutation(ra.getCodeMutation());
            annonce9Eme.setEtat(IREAnnonces.CS_ETAT_OUVERT);
            Enumeration erreurs = REAnakinParser.getInstance().parse(getSession(), annonce9Eme, null, dateMoisRapport);
            StringBuffer buff = new StringBuffer();
            while ((erreurs != null) && erreurs.hasMoreElements()) {
                AnnonceErreur erreur = (AnnonceErreur) erreurs.nextElement();
                buff.append(erreur.getMessage()).append("\n");
            }
            if (buff.length() > 0) {
                throw new Exception(buff.toString());
            }
            annonce9Eme.add(transaction);
            idAnnonce = annonce9Eme.getIdAnnonce();
        } else {
            throw new Exception("Erreur dans la diminution des rentes accordées, code application invalide : "
                    + codeApplication);
        }

        // Création de l'annonce de rente pour la liaison
        // entre les ra et les annonces
        REAnnonceRente annonceRente = new REAnnonceRente();
        annonceRente.setSession(getSession());
        annonceRente.setIdAnnonceHeader(idAnnonce);
        annonceRente.setCsEtat(IREAnnonces.CS_ETAT_OUVERT);
        annonceRente.setCsTraitement(IREAnnonces.CS_CODE_EN_COURS);
        annonceRente.setIdRenteAccordee(ra.getIdPrestationAccordee());
        annonceRente.add(transaction);

    }

    private void deleteAnnonce42(BITransaction transaction, String idAnnonce) throws Exception {
        REAnnoncesDiminution9Eme annonce42 = new REAnnoncesDiminution9Eme();
        annonce42.setSession(getSession());
        annonce42.setIdAnnonce(idAnnonce);
        annonce42.retrieve(transaction);

        String idLienAnnonce = "";
        if (!annonce42.isNew()) {
            idLienAnnonce = annonce42.getIdLienAnnonce();
            annonce42.delete(transaction);
        } else {
            throw new Exception("Aucune annonce trouvée. idArc = " + idAnnonce);
        }

        // Tout pendant que idLienAnnonce n'est pas vide, on efface les
        // annonces...
        while (!JadeStringUtil.isBlankOrZero(idLienAnnonce)) {

            REAnnoncesDiminution9Eme annonce42_ = new REAnnoncesDiminution9Eme();
            annonce42_.setSession(getSession());
            annonce42_.setIdAnnonce(idLienAnnonce);
            annonce42_.retrieve(transaction);

            if (!annonce42_.isNew()) {
                idLienAnnonce = annonce42_.getIdLienAnnonce();
                annonce42_.delete(transaction);
            } else {
                throw new Exception("Aucune annonce trouvée. idArc = " + idAnnonce);
            }
        }
    }

    private void deleteAnnonce45(BITransaction transaction, String idAnnonce) throws Exception {
        REAnnoncesDiminution10Eme annonce45 = new REAnnoncesDiminution10Eme();
        annonce45.setSession(getSession());
        annonce45.setIdAnnonce(idAnnonce);
        annonce45.retrieve(transaction);

        String idLienAnnonce = "";
        if (!annonce45.isNew()) {
            idLienAnnonce = annonce45.getIdLienAnnonce();
            annonce45.delete(transaction);
        } else {
            throw new Exception("Aucune annonce trouvée. idArc = " + idAnnonce);
        }

        // Tout pendant que idLienAnnonce n'est pas vide, on efface les
        // annonces...
        while (!JadeStringUtil.isBlankOrZero(idLienAnnonce)) {

            REAnnoncesDiminution10Eme annonce45_ = new REAnnoncesDiminution10Eme();
            annonce45_.setSession(getSession());
            annonce45_.setIdAnnonce(idLienAnnonce);
            annonce45_.retrieve(transaction);

            if (!annonce45_.isNew()) {
                idLienAnnonce = annonce45_.getIdLienAnnonce();
                annonce45_.delete(transaction);
            } else {
                throw new Exception("Aucune annonce trouvée. idArc = " + idAnnonce);
            }
        }
    }

    private void doEcritureRecap(BITransaction transaction, String codeRecap, RERenteAccordee ra, String idLot)
            throws Exception {
        RERecapInfo ir = new RERecapInfo();
        ir.setSession(getSession());
        ir.setCodeRecap(codeRecap);
        ir.setIdTiers(ra.getIdTiersBeneficiaire());
        ir.setMontant(montant.toString());
        ir.setRestoreTag(idLot);
        ir.add(transaction);
    }

    private void doMAJPeriodeDemandeRente(BITransaction transaction, String idRenteCalculee,
            RERenteAccordee raAvantMAJ, String dateFin) throws Exception {

        REDemandeRente dem = new REDemandeRente();
        dem.setSession(getSession());
        dem.setIdRenteCalculee(idRenteCalculee);
        dem.setAlternateKey(REDemandeRente.ALTERNATE_KEY_ID_RENTE_CALCULEE);
        dem.retrieve(transaction);

        if (IREDemandeRente.CS_TYPE_DEMANDE_RENTE_API.equals(dem.getCsTypeDemandeRente())) {
            REPeriodeAPIManager mgr = new REPeriodeAPIManager();
            mgr.setSession(getSession());
            mgr.setForIdDemandeRente(dem.getIdDemandeRente());
            mgr.find(transaction);

            JACalendar cal = new JACalendarGregorian();
            for (int i = 0; i < mgr.size(); i++) {
                REPeriodeAPI p = (REPeriodeAPI) mgr.getEntity(i);
                JADate dfPeriode = new JADate(p.getDateFinInvalidite());
                JADate dfRA = new JADate(raAvantMAJ.getDateFinDroit());
                if (cal.compare(dfRA, dfPeriode) == JACalendar.COMPARE_EQUALS) {
                    p.setDateFinInvalidite(dateFin);
                    p.update(transaction);
                    break;
                }
            }
        } else if (IREDemandeRente.CS_TYPE_DEMANDE_RENTE_INVALIDITE.equals(dem.getCsTypeDemandeRente())) {
            REPeriodeInvaliditeManager mgr = new REPeriodeInvaliditeManager();
            mgr.setSession(getSession());
            mgr.setForIdDemandeRente(dem.getIdDemandeRente());
            mgr.find(transaction);

            JACalendar cal = new JACalendarGregorian();
            for (int i = 0; i < mgr.size(); i++) {
                REPeriodeInvalidite p = (REPeriodeInvalidite) mgr.getEntity(i);
                JADate dfPeriode = new JADate(p.getDateFinInvalidite());
                JADate dfRA = new JADate(raAvantMAJ.getDateFinDroit());
                if (cal.compare(dfRA, dfPeriode) == JACalendar.COMPARE_EQUALS) {
                    p.setDateFinInvalidite(dateFin);
                    p.update(transaction);
                    break;
                }
            }
        }
        dem.setDateFin(dateFin);
        if (IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TRANSFERE.equals(dem.getCsEtat())) {
            dem.setCsEtat(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_VALIDE);
        }
        dem.update(transaction);

    }

    public void doTraitement(BITransaction transaction) throws Exception {

        RERenteAccordee ra = new RERenteAccordee();
        ra.setSession(getSession());
        ra.setIdPrestationAccordee(getIdRenteAccordee());
        ra.retrieve(transaction);
        PRAssert.notIsNew(ra, null);
        REAnnulerDiminutionRAHandler h = new REAnnulerDiminutionRAHandler();
        String noRevision = h.getNoRevision(getSession(), transaction, ra);
        REAnnulerDiminutionRAHandler.AnnonceDiminutionContainer adc = h.getAnnonceDiminution(getSession(), transaction,
                ra, noRevision);

        RERecapInfo ri = null;

        // Aucune annonce de diminution trouvée
        if (adc == null) {
            throw new Exception(getSession().getLabel("ERREUR_ANNONCE_DIMINUTION_NON_TROUVEE"));
        }
        /*
         * Test... Si « dateDiminution » est égale ou plus grande que le mois et année du dernier paiement. Récupérer
         * REINFREC avec… - date de fin + montant de l’annonce de diminution - idTiers du compta annexe du bénéficiaire
         * de la RA (REINCOM) égal idTiers de REINFREC - code dans REINFREC doit se terminer par 4 (diminution) - Le lot
         * référencé dans REINFREC doit être un lot avec type = DIMINUTION
         * 
         * 
         * Si REINFREC non trouvé, selon les critères ci-dessus, afficher message d’erreur. Si plusieurs REINFREC pour
         * le même lot, bloquer avec message d’erreur (rajouter pour tests future un nouveau champ dans REINFREC pour
         * stocker l’id de la rente accordée).
         */
        else {

            String idJournalCADeLaRADiminuee = "";

            // Date de la diminution de la RA == moisRapport de l'annonce moins 1 mois.
            JADate dateDiminution = new JADate("01." + PRDateFormater.convertDate_MMAA_to_MMxAAAA(adc.moisRapport));
            JACalendar cal = new JACalendarGregorian();
            dateDiminution = cal.addMonths(dateDiminution, -1);
            JADate dateDernierPaiement = new JADate(REPmtMensuel.getDateDernierPmt(getSession()));

            // La date de la diminution est >= à la date du dernier pmt
            if ((cal.compare(dateDiminution, dateDernierPaiement) == JACalendar.COMPARE_FIRSTUPPER)
                    || (cal.compare(dateDiminution, dateDernierPaiement) == JACalendar.COMPARE_EQUALS)) {
                // Récupération de REINFREC
                REInformationsComptabilite ic = new REInformationsComptabilite();
                ic.setSession(getSession());
                ic.setIdInfoCompta(ra.getIdInfoCompta());
                ic.retrieve(getTransaction());
                PRAssert.notIsNew(ic, "Entity REInformationsComptabilite not found : " + ra.getIdInfoCompta());

                // Récupération de REINFREC

                CACompteAnnexe ca = new CACompteAnnexe();
                ca.setSession(getSession());
                ca.setIdCompteAnnexe(ic.getIdCompteAnnexe());
                ca.retrieve(getTransaction());
                PRAssert.notIsNew(ca, "Entity CACompteAnnexe not found : " + ic.getIdCompteAnnexe());

                RERecapInfoManager mgr = new RERecapInfoManager();
                mgr.setSession(getSession());
                mgr.setForIdTiers(ca.getIdTiers());
                mgr.setForDatePmt(PRDateFormater.convertDate_MMAA_to_MMxAAAA(adc.moisRapport));
                mgr.find(getTransaction());
                if (mgr.isEmpty()) {
                    throw new Exception("Incohérance dans les données, aucune information de la RECAP trouvée");
                } else {
                    boolean found = false;
                    for (int i = 0; i < mgr.size(); i++) {
                        ri = (RERecapInfo) mgr.getEntity(i);
                        FWCurrency m1 = new FWCurrency(ri.getMontant());
                        FWCurrency m2 = new FWCurrency(adc.montant);

                        if (m1.compareTo(m2) != 0) {
                            continue;
                        }
                        // Montant identique
                        else {
                            // On contrôle que le code se termine par 4, cad, de type diminution
                            if (!ri.getCodeRecap().endsWith("4")) {
                                continue;
                            } else {
                                // le lot de référence doit être un lot de type diminution
                                RELot lot = new RELot();
                                lot.setSession(getSession());
                                lot.setIdLot(ri.getRestoreTag());
                                lot.retrieve(transaction);
                                if (!IRELot.CS_TYP_LOT_DIMINUTION.equals(lot.getCsTypeLot())) {
                                    continue;
                                } else {
                                    // Final check, on contrôle qu'il n'y ait qu'un seul REInfoRecap pour ce lot
                                    RERecapInfoManager mgr2 = new RERecapInfoManager();
                                    mgr2.setForRestoreTag(lot.getIdLot());
                                    mgr.find(transaction);
                                    // Pour les cas avec 2 RA diminuées en même temps. Diminution d'une RA 10, diminue
                                    // également celle des enfants !!
                                    if (mgr2.size() > 1) {
                                        throw new Exception(
                                                "Erreur : Plusieurs Recap Info trouvé pour le même lot, le traitement ne peut s'effectuer automatiquement.");
                                    }
                                    found = true;
                                    idJournalCADeLaRADiminuee = lot.getIdJournalCA();
                                    break;
                                }
                            }
                        }
                    }

                    if (!found) {
                        throw new Exception("Aucune info récap trouvé. Traitement annulé");
                    } else {

                        // On recalcul la période de la demande de rente
                        REBasesCalcul bc = new REBasesCalcul();
                        bc.setSession(getSession());
                        bc.setIdBasesCalcul(ra.getIdBaseCalcul());
                        bc.retrieve(transaction);

                        RERenteCalculee rc = new RERenteCalculee();
                        rc.setSession(getSession());
                        rc.setIdRenteCalculee(bc.getIdRenteCalculee());
                        rc.retrieve(transaction);

                        RELot lot = new RELot();
                        lot.setSession(getSession());
                        lot.setCsTypeLot(IRELot.CS_TYP_LOT_DIMINUTION);
                        lot.setCsLotOwner(IRELot.CS_LOT_OWNER_RENTES);
                        lot.setDescription("Diminution de rente");
                        lot.add(transaction);

                        JADate dateFinDroit = new JADate(ra.getDateFinDroit());

                        // Suppression de l'annonce de diminution
                        if ("42".equals(adc.codeApplication)) {
                            deleteAnnonce42(transaction, adc.idAnnonceHeader);
                        } else if ("45".equals(adc.codeApplication)) {
                            deleteAnnonce45(transaction, adc.idAnnonceHeader);
                        }

                        // Suppression de l'écriture de récap (REINFREC)
                        ri.delete(transaction);

                        // DateFindroit >= dateDernierPaiement
                        if ((cal.compare(dateFinDroit, dateDernierPaiement) == JACalendar.COMPARE_EQUALS)
                                || (cal.compare(dateFinDroit, dateDernierPaiement) == JACalendar.COMPARE_FIRSTUPPER)) {

                            doTraitementStandard(transaction, ra, rc.getIdRenteCalculee(), adc.codeApplication,
                                    lot.getIdLot(), cal, dateDernierPaiement, idTiersBeneficiaire);

                            // DateFinDroit < dateDernierPaiement
                        } else {

                            StringBuffer sb = new StringBuffer();
                            if (getSession().hasErrors()) {
                                sb.append(getSession().getErrors());
                            }
                            if (getTransaction().hasErrors()) {
                                sb.append(getTransaction().getErrors());
                            }

                            if (sb.toString().length() > 0) {
                                throw new Exception(sb.toString());
                            }

                            doTraitementStandard(transaction, ra, rc.getIdRenteCalculee(), adc.codeApplication,
                                    lot.getIdLot(), cal, dateDernierPaiement, idTiersBeneficiaire);

                            CAProcessAnnulerJournal proc = new CAProcessAnnulerJournal(this);
                            proc.setIdJournal(idJournalCADeLaRADiminuee);
                            proc.setTransaction(transaction);
                            proc.executeProcess();

                            if (getMemoryLog().hasErrors()) {
                                Vector<BIMessage> v = getMemoryLog().getMessagesToVector();
                                for (Iterator<BIMessage> iterator = v.iterator(); iterator.hasNext();) {
                                    BIMessage biMessage = iterator.next();
                                    if (FWMessage.ERREUR.equals(biMessage.getTypeMessage())
                                            || FWMessage.FATAL.equals(biMessage.getTypeMessage())) {
                                        throw new Exception(biMessage.getFullMessage());
                                    }
                                }
                            }
                            if (getTransaction().hasErrors()) {
                                throw new Exception(getTransaction().getErrors().toString());
                            }
                            if (getSession().hasErrors()) {
                                throw new Exception(getSession().getErrors().toString());
                            }

                        }
                    }
                }
            } else {
                throw new Exception("Erreur, impossible de traiter ce cas automatiquement.");
            }
        }
    }

    private void doTraitementStandard(BITransaction transaction, RERenteAccordee ra, String idRenteCalculee,
            String codeApplication, String idLot, JACalendar cal, JADate dateDernierPmt, String idTiersBeneficiaire)
            throws Exception {

        // L'utilisateur vide les champs date de fin de droit et code mutation....
        if (JadeStringUtil.isBlankOrZero(getDateFinDroitModifiee()) && JadeStringUtil.isBlankOrZero(getCodeMutation())) {

            doMAJPeriodeDemandeRente(transaction, idRenteCalculee, ra, getDateFinDroitModifiee());

            // MAJ de la ra
            ra.retrieve(transaction);
            ra.setDateFinDroit("");
            ra.setCodeMutation("");
            ra.setCsEtat(IREPrestationAccordee.CS_ETAT_VALIDE);
            ra.update(transaction);

        } else if (!JadeStringUtil.isBlankOrZero(getDateFinDroitModifiee())
                && !JadeStringUtil.isBlankOrZero(getCodeMutation())) {

            // Si pas de changement, on ne fait rien
            if (ra.getCodeMutation().equals(getCodeMutation())
                    && ra.getDateFinDroit().equals(getDateFinDroitModifiee())) {
                return;
            }
            // Si changement
            else {
                ra.retrieve(transaction);
                ra.setDateFinDroit(getDateFinDroitModifiee());
                ra.setCodeMutation(getCodeMutation());
                ra.setCsEtat(IREPrestationAccordee.CS_ETAT_VALIDE);
                ra.update(transaction);

                REDiminutionRenteAccordeeProcess process = new REDiminutionRenteAccordeeProcess();
                process.setSession(getSession());
                process.setTransaction(transaction);
                process.setIdRenteAccordee(ra.getIdPrestationAccordee());
                process.setCsCodeMutation(getCodeMutation());

                JADate newDate = new JADate(getDateFinDroitModifiee());

                if (cal.compare(newDate, dateDernierPmt) == JACalendar.COMPARE_FIRSTLOWER) {
                    process.setCsCodeTraitement(IREDiminution.CS_GENRE_TRATEMENT_DIM_RESTITUTION);
                }

                process.setDateFinDroit(getDateFinDroitModifiee());
                process.setEMailAddress(getSession().getUserEMail());
                process.executeProcess();

                // Contrôle des erreures
                if (getTransaction().hasErrors()) {
                    throw new Exception(getTransaction().getErrors().toString());
                }
                if (getSession().hasErrors()) {
                    throw new Exception(getSession().getErrors().toString());
                }

            }
        }
    }

    public String getCodeMutation() {
        return codeMutation;
    }

    /**
     * Récupère la date de début du droit à la rente
     * 
     * @return
     */
    public String getDateDebutDroit() {
        return dateDebutDroit;
    }

    /**
     * Récuppère la date de fin du droit à la rente
     * 
     * @return
     */
    public String getDateFinDroit() {
        return dateFinDroit;
    }

    public String getDateFinDroitModifiee() {
        return dateFinDroitModifiee;
    }

    @Override
    public String getEMailObject() {
        return emailObject;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public String getGenreRente() {
        return genreRente;
    }

    /**
     * Récupère l'id de la rente accordée
     * 
     * @return
     */
    public String getIdRenteAccordee() {
        return idRenteAccordee;
    }

    /**
     * Récupère l'id du tiers bénéficiaire
     * 
     * @return
     */
    public String getIdTiersBeneficiaire() {
        return idTiersBeneficiaire;
    }

    /**
     * Récupère le montant de la rente accordée
     * 
     * @return
     */
    public String getMontant() {
        return montant;
    }

    /**
     * @return
     */
    public BSpy getSpy() {
        return null;
    }

    /**
     * Récupération de la ligne info sur le tiers bénéficaire
     * 
     * @return
     */
    public String getTiersBeneficiaireInfo() {
        return tiersBeneficiaireInfo;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    public void setCodeMutation(String codeMutation) {
        this.codeMutation = codeMutation;
    }

    /**
     * Modifie la date de début du droit à la rente
     * 
     * @param newDateDebutDroit
     */
    public void setDateDebutDroit(String newDateDebutDroit) {
        dateDebutDroit = newDateDebutDroit;
    }

    /**
     * Modifie la date de fin du droit à la rente
     * 
     * @param newDateFinDroit
     */
    public void setDateFinDroit(String newDateFinDroit) {
        dateFinDroit = newDateFinDroit;
    }

    public void setDateFinDroitModifiee(String dateFinDroitModifiee) {
        this.dateFinDroitModifiee = dateFinDroitModifiee;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public void setGenreRente(String genreRente) {
        this.genreRente = genreRente;
    }

    /**
     * Modification de l'id rente accordée
     * 
     * @param newIdRenteAccordee
     */
    public void setIdRenteAccordee(String newIdRenteAccordee) {
        idRenteAccordee = newIdRenteAccordee;
    }

    /**
     * Modifie l'id du tiers bénéficiaire
     * 
     * @param newIdTiersBeneficiaire
     */
    public void setIdTiersBeneficiaire(String newIdTiersBeneficiaire) {
        idTiersBeneficiaire = newIdTiersBeneficiaire;
    }

    /**
     * Modifie le montant de la rente accordée
     * 
     * @param newMontant
     */
    public void setMontant(String newMontant) {
        montant = newMontant;
    }

    /**
     * Modification de la ligne info sur le tiers bénéficiaire
     * 
     * @param newTiersBeneficiaireInfo
     */
    public void setTiersBeneficiaireInfo(String newTiersBeneficiaireInfo) {
        tiersBeneficiaireInfo = newTiersBeneficiaireInfo;
    }
}
