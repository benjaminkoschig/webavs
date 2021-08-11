package globaz.ij.helpers.acor;

import globaz.corvus.vb.acor.RECalculACORDemandeRenteViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.util.FWCurrency;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JANumberFormatter;
import globaz.ij.acor.IJACORBatchFilePrinter;
import globaz.ij.acor.adapter.IJAttestationsJoursAdapter;
import globaz.ij.api.basseindemnisation.IIJBaseIndemnisation;
import globaz.ij.api.basseindemnisation.IIJFormulaireIndemnisation;
import globaz.ij.api.prononces.IIJMesure;
import globaz.ij.api.prononces.IIJPrononce;
import globaz.ij.db.basesindemnisation.IJBaseIndemnisation;
import globaz.ij.db.basesindemnisation.IJFormulaireIndemnisation;
import globaz.ij.db.basesindemnisation.IJFormulaireIndemnisationManager;
import globaz.ij.db.prestations.IJGrandeIJCalculeeManager;
import globaz.ij.db.prestations.IJIJCalculee;
import globaz.ij.db.prestations.IJIJCalculeeManager;
import globaz.ij.db.prestations.IJIndemniteJournaliere;
import globaz.ij.db.prestations.IJIndemniteJournaliereManager;
import globaz.ij.db.prestations.IJPetiteIJCalculeeManager;
import globaz.ij.db.prestations.IJPrestation;
import globaz.ij.db.prononces.IJPrononce;
import globaz.ij.module.IJRepartitionPaiementBuilder;
import globaz.ij.regles.IJBaseIndemnisationRegles;
import globaz.ij.vb.acor.IJCalculACORDecompteViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.acor.PRACORException;
import globaz.prestation.acor.PRAcorFileContent;
import globaz.prestation.helpers.PRAbstractHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRCalcul;
import globaz.prestation.tools.PRDateFormater;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * helper pour l'exportation et l'importation des fichier ACOR lors de la deuxieme phase du calcul, a savoir les
 * decomptes.
 * </p>
 * 
 * @deprecated Le calcul des décompte IJAI ne doit plus passer par ACOR
 * 
 * @author vre
 */
@Deprecated
public class IJCalculACORDecompteHelper extends PRAbstractHelper {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * multiplie deux nombres, arrondit le resultat a 2 decimales et 5 centimes et retourne cette valeur en tant que
     * chaine.
     * 
     * @param op1
     * @param op2
     * 
     * @return
     */
    private static final String multiply(String op1, String op2) {

        if (JadeStringUtil.isEmpty(op1)) {
            op1 = "0.0";
        }
        if (JadeStringUtil.isEmpty(op2)) {
            op2 = "0.0";
        }

        return JANumberFormatter.format(PRCalcul.multiply(op1, op2), 0.05, 2, JANumberFormatter.NEAR);
    }

    /**
     * Cette methode est redefinie afin de charger toutes les ijcalculees pour la periode de la base d'indemnisation que
     * l'on veut utiliser pour calculer le decompte avant l'affichage de l'ecran de ACOR.
     * 
     * @param viewBean
     *            DOCUMENT ME!
     * @param action
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        IJCalculACORDecompteViewBean caViewBean = (IJCalculACORDecompteViewBean) viewBean;

        caViewBean.setIdBaseIndemnisation(caViewBean.getId()); // action
        // afficher par
        // defaut n'a
        // renseigne que
        // id

        // charger la base d'indemnisation
        IJBaseIndemnisation baseIndemnisation = caViewBean.loadBaseIndemnisation();

        // charger les ij calculees pour la periode de la base d'indemnisation.
        IJIJCalculeeManager calculeeManager;

        if (IIJPrononce.CS_GRANDE_IJ.equals(baseIndemnisation.getCsTypeIJ())) {
            calculeeManager = new IJGrandeIJCalculeeManager();
        } else {
            calculeeManager = new IJPetiteIJCalculeeManager();
        }

        calculeeManager.setForIdPrononce(baseIndemnisation.getIdPrononce());
        calculeeManager.setForPeriode(baseIndemnisation.getDateDebutPeriode(), baseIndemnisation.getDateFinPeriode());
        calculeeManager.setISession(session);
        // Trié dans cet ordre pour avoir les ijcalculé correctement triés
        // dans l'écran PIJ3005 (pour les cas avec boutons suivant - précédent)
        calculeeManager.setOrderBy(IJIJCalculee.FIELDNAME_DATE_DEBUT_DROIT + " ASC");
        calculeeManager.find();

        // construire la liste des id des ij calculees pour les stocker dans le
        // viewBean
        List idsIJCalculees = null;

        if ((calculeeManager == null) || (calculeeManager.size() == 0)) {
            throw new Exception(((BSession) session).getLabel("PERIODE_BI_ERREUR"));
        }

        if (calculeeManager.size() > 1) {
            /*
             * il y a plus d'une ij pour cette base, il y a alors 3 possibilites:
             * 
             * 1. Une changement de situation de l'assuré (par exemple la naissance d'un enfant) a provoqué le calcul de
             * plusieurs IJ. Dans ce cas il va falloir que l'utilisateur calcule les prestations pour toutes les ij a la
             * suite. 2. Une ij calculee de 4eme revision est en defaveur de l'assure par rapport la meme ij calculee
             * pour la 3eme revision. Dans ce cas, il faut proposer un seul calcul a l'utilisateur mais avec la
             * possibilite de choisir s'il veut garantir la 3eme rev. 3. Un mélange des deux precedents
             * 
             * Il est facile de distinguer les couple d'ij-ijgarantie3 car elles ont les memes dates de debut et de fin
             */
            ArrayList ijs = new ArrayList(calculeeManager.getContainer());

            idsIJCalculees = new ArrayList(calculeeManager.size());

            List idsIJCalculeInseree = new ArrayList();

            for (int idIj = 0; idIj < ijs.size(); ++idIj) {
                IJIJCalculee ref = (IJIJCalculee) ijs.get(idIj);
                boolean trouve = false;

                for (int idComp = idIj + 1; idComp < ijs.size(); ++idComp) {
                    IJIJCalculee comp = (IJIJCalculee) ijs.get(idComp);

                    if (ref.getDateDebutDroit().equals(comp.getDateDebutDroit())
                            && ref.getDateFinDroit().equals(comp.getDateFinDroit())) {

                        IJRevisions revisions = new IJRevisions();

                        IJRevision revision = new IJRevision();
                        revision.setIdIJCalculee(ref.getIdIJCalculee());
                        revision.setNoRevision(ref.getNoRevision());

                        String montantDsc = "";

                        IJIndemniteJournaliereManager mgr = new IJIndemniteJournaliereManager();
                        mgr.setSession((BSession) session);
                        mgr.setForIdIJCalculee(ref.getIdIJCalculee());
                        mgr.setForCsTypeIndemnite(IIJMesure.CS_INTERNE);
                        mgr.find();
                        if (!mgr.isEmpty()) {
                            montantDsc = ((IJIndemniteJournaliere) mgr.getFirstEntity())
                                    .getMontantJournalierIndemnite();
                            montantDsc += " / ";
                        }
                        mgr.setForCsTypeIndemnite(IIJMesure.CS_EXTERNE);
                        mgr.find();
                        if (!mgr.isEmpty()) {
                            if (JadeStringUtil.isBlankOrZero(montantDsc)) {
                                montantDsc = "na / "
                                        + ((IJIndemniteJournaliere) mgr.getFirstEntity())
                                                .getMontantJournalierIndemnite();
                            } else {
                                montantDsc += ((IJIndemniteJournaliere) mgr.getFirstEntity())
                                        .getMontantJournalierIndemnite();
                            }
                        } else {
                            montantDsc += "na";
                        }

                        revision.setMontant(montantDsc);
                        revisions.addRevision(revision);
                        idsIJCalculeInseree.add(ref.getIdIJCalculee());

                        revision = new IJRevision();
                        revision.setIdIJCalculee(comp.getIdIJCalculee());
                        revision.setNoRevision(comp.getNoRevision());

                        mgr = new IJIndemniteJournaliereManager();
                        mgr.setSession((BSession) session);
                        mgr.setForIdIJCalculee(comp.getIdIJCalculee());
                        mgr.setForCsTypeIndemnite(IIJMesure.CS_INTERNE);
                        mgr.find();
                        if (!mgr.isEmpty()) {
                            montantDsc = ((IJIndemniteJournaliere) mgr.getFirstEntity())
                                    .getMontantJournalierIndemnite();
                            montantDsc += " / ";
                        }
                        mgr.setForCsTypeIndemnite(IIJMesure.CS_EXTERNE);
                        mgr.find();
                        if (!mgr.isEmpty()) {
                            if (JadeStringUtil.isBlankOrZero(montantDsc)) {
                                montantDsc = "na / "
                                        + ((IJIndemniteJournaliere) mgr.getFirstEntity())
                                                .getMontantJournalierIndemnite();
                            } else {
                                montantDsc += ((IJIndemniteJournaliere) mgr.getFirstEntity())
                                        .getMontantJournalierIndemnite();
                            }
                        } else {
                            montantDsc += "na";
                        }

                        revision.setMontant(montantDsc);
                        revisions.addRevision(revision);
                        idsIJCalculeInseree.add(comp.getIdIJCalculee());

                        idsIJCalculees.add(revisions);
                        trouve = true;
                        break;
                    }
                }

                if (!trouve) {
                    IJRevisions revisions = new IJRevisions();

                    if (!idsIJCalculeInseree.contains(ref.getIdIJCalculee())) {
                        IJRevision revision = new IJRevision();
                        revision.setIdIJCalculee(ref.getIdIJCalculee());
                        revision.setNoRevision(ref.getNoRevision());

                        String montantDsc = "";

                        IJIndemniteJournaliereManager mgr = new IJIndemniteJournaliereManager();
                        mgr.setSession((BSession) session);
                        mgr.setForIdIJCalculee(ref.getIdIJCalculee());
                        mgr.setForCsTypeIndemnite(IIJMesure.CS_INTERNE);
                        mgr.find();
                        if (!mgr.isEmpty()) {
                            montantDsc = ((IJIndemniteJournaliere) mgr.getFirstEntity())
                                    .getMontantJournalierIndemnite();
                            montantDsc += " / ";
                        }
                        mgr.setForCsTypeIndemnite(IIJMesure.CS_EXTERNE);
                        mgr.find();
                        if (!mgr.isEmpty()) {
                            if (JadeStringUtil.isBlankOrZero(montantDsc)) {
                                montantDsc = "na / "
                                        + ((IJIndemniteJournaliere) mgr.getFirstEntity())
                                                .getMontantJournalierIndemnite();
                            } else {
                                montantDsc += ((IJIndemniteJournaliere) mgr.getFirstEntity())
                                        .getMontantJournalierIndemnite();
                            }
                        } else {
                            montantDsc += "na";
                        }

                        revision.setMontant(montantDsc);

                        revisions.addRevision(revision);
                        idsIJCalculees.add(revisions);
                        idsIJCalculeInseree.add(ref.getIdIJCalculee());
                    }
                }
            }
        } else {
            idsIJCalculees = new ArrayList(1);

            IJIJCalculee ref = (IJIJCalculee) calculeeManager.getFirstEntity();

            IJRevisions revisions = new IJRevisions();
            IJRevision revision = new IJRevision();
            revision.setIdIJCalculee(ref.getIdIJCalculee());
            revision.setNoRevision(ref.getNoRevision());

            String montantDsc = "";

            IJIndemniteJournaliereManager mgr = new IJIndemniteJournaliereManager();
            mgr.setSession((BSession) session);
            mgr.setForIdIJCalculee(ref.getIdIJCalculee());
            mgr.setForCsTypeIndemnite(IIJMesure.CS_INTERNE);
            mgr.find();
            if (!mgr.isEmpty()) {
                montantDsc = ((IJIndemniteJournaliere) mgr.getFirstEntity()).getMontantJournalierIndemnite();
                montantDsc += " / ";
            }
            mgr.setForCsTypeIndemnite(IIJMesure.CS_EXTERNE);
            mgr.find();
            if (!mgr.isEmpty()) {
                if (JadeStringUtil.isBlankOrZero(montantDsc)) {
                    montantDsc = "na / "
                            + ((IJIndemniteJournaliere) mgr.getFirstEntity()).getMontantJournalierIndemnite();
                } else {
                    montantDsc += ((IJIndemniteJournaliere) mgr.getFirstEntity()).getMontantJournalierIndemnite();
                }
            } else {
                montantDsc += "na";
            }

            revision.setMontant(montantDsc);

            revisions.addRevision(revision);
            idsIJCalculees.add(revisions);
        }

        caViewBean.setIdsIJCalculees(idsIJCalculees);

        // charger les informations necessaires à l'affichage dans l'ecran
        PRTiersWrapper tiers = caViewBean.loadPrononce().loadDemande(null).loadTiers();

        caViewBean.setNoAVSAssure(tiers.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));
        caViewBean.setNomPrenomAssure(tiers.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                + tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM));
        caViewBean.setDatePrononce(caViewBean.loadPrononce().getDatePrononce());

        caViewBean.setDateDebutBaseIndemnisation(baseIndemnisation.getDateDebutPeriode());
        caViewBean.setDateFinBaseIndemnisation(baseIndemnisation.getDateFinPeriode());

        // renseigner le champ calculable
        caViewBean.setCalculable(IJBaseIndemnisationRegles.isCalculerPermis(baseIndemnisation,
                caViewBean.loadPrononce()));
    }

    /**
     * 
     * Calcul des prestations IJ sans passer par ACOR.
     * 
     * @param viewBean
     * @param action
     * @param session
     * @return
     * @throws Exception
     */
    public FWViewBeanInterface actionCalculerPrestation(FWViewBeanInterface viewBean, FWAction action, BSession session)
            throws Exception {
        IJCalculACORDecompteViewBean caViewBean = (IJCalculACORDecompteViewBean) viewBean;
        BITransaction transaction = null;

        try {
            transaction = session.newTransaction();
            transaction.openTransaction();

            // charger la base
            IJBaseIndemnisation base = caViewBean.loadBaseIndemnisation();

            // Dans le cas d'une base d'indemnistation ayant 2 prestations, on
            // itère sur chaque prestations
            // pour importer le résultat de acor.
            // Lors du traitement de la 2ème prestation, il ne faut pas annuler
            // ou restituer
            // La base parent, car déjà fait...
            //
            if (IIJBaseIndemnisation.CS_VALIDE.equals(base.getCsEtat()) && (caViewBean.getIdsIJCalculees().size() > 1)
                    && !JadeStringUtil.isIntegerEmpty(base.getIdParent())) {
                ;
            } else {
                // restituer ou annuler la base d'origine si celle-ci est une
                // correction
                IJBaseIndemnisationRegles.correction(session, transaction, base);
            }

            // L'état à p'têtre changé...
            base.retrieve();

            // reinitialiser la base si des prestations ont deja ete calculees
            if (IIJBaseIndemnisation.CS_VALIDE.equals(base.getCsEtat())) {
                IJBaseIndemnisationRegles.reinitialiser(session, transaction, base, caViewBean.loadIJCalculee()
                        .getIdIJCalculee());
            }

            // importer les decomptes
            LinkedList decomptes = new LinkedList();

            decomptes.addAll(calculPrestationsSansAcor(session, (BTransaction) transaction, caViewBean.loadPrononce(),
                    base, caViewBean.loadIJCalculee()));

            // repartir les paiements de la prestation
            IJRepartitionPaiementBuilder.getInstance().buildRepartitionPaiements(session, transaction,
                    caViewBean.loadPrononce(), base, decomptes);
            // mettre la base d'indemnisation dans l'etat valide
            base.setCsEtat(IIJBaseIndemnisation.CS_VALIDE);
            // Peut paraître stupide, mais ne l'est pas !!!
            // Si la base n'a pas été modifiée, et générée par l'impression des
            // attestations...
            // son taux d'IS est à zéro. Si tel est le cas, la ligne ci-dessous
            // va le setté avec le taux défini dans le prononcé.
            base.setTauxImpotSource(base.getTauxImpotSource());
            base.setCsCantonImpotSource(base.getCsCantonImpotSource());
            base.update(transaction);

            // // mettre le prononcé dans l'état valide
            // point ouvert 00658
            // ajout de l'etat decide
            if (/*
                 * !IIJPrononce.CS_VALIDE.equals(caViewBean.loadPrononce().getCsEtat ()) &&
                 */!IIJPrononce.CS_COMMUNIQUE.equals(caViewBean.loadPrononce().getCsEtat())) {
                // caViewBean.loadPrononce().setCsEtat(IIJPrononce.CS_VALIDE);

                if (caViewBean.loadPrononce().getAvecDecision().booleanValue()) {
                    caViewBean.loadPrononce().setCsEtat(IIJPrononce.CS_VALIDE);
                } else {
                    caViewBean.loadPrononce().setCsEtat(IIJPrononce.CS_DECIDE);
                }
                caViewBean.loadPrononce().update(transaction);
            }

            // On met a jours les formulaires d'indemnistation dans l'état RECU
            // avec la date du jour comme date de réception.
            // Seul les formulaire dans l'état envoyé sont mis à jours.
            try {
                doMAJEtatFormulaireIndemnisation(session, caViewBean.getIdBaseIndemnisation());
            } catch (Exception e) {
                // Ne devrait jamais arriver !!! Dans tous les cas, en cas
                // d'erreur, elle est ignorée !!!
                e.printStackTrace();
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
                } finally {
                    transaction.closeTransaction();
                }
            }
        }

        return viewBean;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param viewBean
     *            DOCUMENT ME!
     * @param action
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    // public FWViewBeanInterface actionExporterScriptACOR(FWViewBeanInterface viewBean, FWAction action, BSession
    // session)
    // throws Exception {
    // IJCalculACORDecompteViewBean caViewBean = (IJCalculACORDecompteViewBean) viewBean;
    //
    // IJIJCalculee ijc = caViewBean.loadIJCalculee();
    // if (JadeStringUtil.isBlankOrZero(caViewBean.getNoRevisionGaranti())) {
    // ijc.setIJEnCoursACOR(false);
    // } else {
    // ijc.setIJEnCoursACOR(true);
    // }
    //
    // // // si garantie, changer la date de début du droit, il
    // // // faut setter la date de début de l'IJCalc avant celle de 2008
    // // if (ijc.getNoRevision().equals("4")){
    // //
    // // IJIJCalculeeManager ijcalMgr = new IJIJCalculeeManager();
    // // ijcalMgr.setSession(session);
    // // ijcalMgr.setForPeriode("01.01.2004", "31.12.2007");
    // // ijcalMgr.setForNoRevision(ijc.getNoRevision());
    // // ijcalMgr.setForIdPrononce(caViewBean.loadPrononce().getIdPrononce());
    // // ijcalMgr.find(1);
    // //
    // // if (!ijcalMgr.isEmpty()){
    // // ijc.setDateDebutDroit(((IJIJCalculee)ijcalMgr.get(0)).getDateDebutDroit());
    // // } else {
    // // ijc.setDateDebutDroit("01.01.2007");
    // // }
    // //
    // // }
    //
    // IJACORBatchFilePrinter.getInstance().printBatchFileDecomptes(caViewBean.getWriter(), session,
    // caViewBean.loadBaseIndemnisation(), ijc, PRACORConst.dossierACOR(session));
    //
    // return viewBean;
    // }

    public FWViewBeanInterface actionExporterScriptACOR2(FWViewBeanInterface viewBean, FWAction action, BSession session)
            throws Exception {

        IJCalculACORDecompteViewBean caViewBean = (IJCalculACORDecompteViewBean) viewBean;

        IJIJCalculee ijc = caViewBean.loadIJCalculee();
        // if (JadeStringUtil.isBlankOrZero(caViewBean.getNoRevisionGaranti())) {
        // ijc.setIJEnCoursACOR(false);
        // } else {
        // ijc.setIJEnCoursACOR(true);
        // }

        Map filesContent = new HashMap();
        IJACORBatchFilePrinter.getInstance().printBatchFileDecomptes(filesContent, session,
                caViewBean.loadBaseIndemnisation(), ijc, PRACORConst.dossierACOR(session));

        filtrerLigneVide(filesContent);

        caViewBean.setFilesContent(filesContent);
        caViewBean.setIsFileContent(true);
        caViewBean.setCalculable(IJBaseIndemnisationRegles.isCalculerPermis(caViewBean.loadBaseIndemnisation(),
                caViewBean.loadPrononce()));

        return caViewBean;
    }

    /**
     * Supprime les lignes vides de tous les PRAcorFileContent
     * 
     * @param filesContent
     */
    private void filtrerLigneVide(Map filesContent) {
        for (Object key : filesContent.keySet()) {
            PRAcorFileContent f = (PRAcorFileContent) filesContent.get(key);
            List<String> newContent = null;
            List<String> content = f.getContents();
            if (content != null) {
                newContent = new LinkedList<String>();

                for (String line : content) {
                    if (!JadeStringUtil.isEmpty(line)) {
                        newContent.add(line);
                    }
                }
            }
            f.setContents(newContent);
        }
    }

    /**
     * importe les decomptes retournes par ACOR, genere la repartition des paiements et les cotisations, importe les
     * annonces et insere le tout dans la base.
     * 
     * @param viewBean
     *            DOCUMENT ME!
     * @param action
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    public FWViewBeanInterface actionImporterDecompte(FWViewBeanInterface viewBean, FWAction action, BSession session)
            throws Exception {
        IJCalculACORDecompteViewBean caViewBean = (IJCalculACORDecompteViewBean) viewBean;
        BITransaction transaction = null;

        try {
            transaction = session.newTransaction();
            transaction.openTransaction();

            // charger la base
            IJBaseIndemnisation base = caViewBean.loadBaseIndemnisation();

            // Dans le cas d'une base d'indemnistation ayant 2 prestations, on
            // itère sur chaque prestations
            // pour importer le résultat de acor.
            // Lors du traitement de la 2ème prestation, il ne faut pas annuler
            // ou restituer
            // La base parent, car déjà fait...
            //
            if (IIJBaseIndemnisation.CS_VALIDE.equals(base.getCsEtat()) && (caViewBean.getIdsIJCalculees().size() > 1)
                    && !JadeStringUtil.isIntegerEmpty(base.getIdParent())) {
                ;
            } else {
                // restituer ou annuler la base d'origine si celle-ci est une
                // correction
                IJBaseIndemnisationRegles.correction(session, transaction, base);
            }

            // L'état à p'têtre changé...
            base.retrieve();

            // reinitialiser la base si des prestations ont deja ete calculees
            if (IIJBaseIndemnisation.CS_VALIDE.equals(base.getCsEtat())) {
                IJBaseIndemnisationRegles.reinitialiser(session, transaction, base, caViewBean.loadIJCalculee()
                        .getIdIJCalculee());
            }

            // importer les decomptes
            LinkedList decomptes = new LinkedList();

            // importer les decomptes de troisieme revision
            if (!JadeStringUtil.isEmpty(caViewBean.getContenuAnnoncePay())) {
                decomptes.addAll(globaz.ij.acor.parser.rev3.IJACORPrestationParser.parse(session,
                        caViewBean.loadPrononce(), base, caViewBean.loadIJCalculee(),
                        new StringReader(caViewBean.getContenuAnnoncePay())));
            } else

            // importer les decompte de 4eme/5ème revision
            if (!JadeStringUtil.isEmpty(caViewBean.getContenuFCalculXML())) {

                final String UNRESOLVED_CHAR_LABEL = "&labels;";
                caViewBean.setContenuFCalculXML(JadeStringUtil.change(caViewBean.getContenuFCalculXML(),
                        UNRESOLVED_CHAR_LABEL, ""));

                decomptes.addAll(globaz.ij.acor.parser.rev5.IJACORPrestationParser.parse(session,
                        (BTransaction) transaction, caViewBean.loadPrononce(), base, caViewBean.loadIJCalculee(),
                        new StringReader(caViewBean.getContenuFCalculXML())));

                // decomptes.addAll(globaz.ij.acor.parser.rev4.IJACORPrestationParser.parse(session,
                // caViewBean.loadPrononce(),
                // base,
                // caViewBean.loadIJCalculee(),
                // new StringReader(caViewBean
                // .getContenuFCalculXML())));
                //
                // }

            }

            // repartir les paiements de la prestation
            IJRepartitionPaiementBuilder.getInstance().buildRepartitionPaiements(session, transaction,
                    caViewBean.loadPrononce(), base, decomptes);
            // mettre la base d'indemnisation dans l'etat valide
            base.setCsEtat(IIJBaseIndemnisation.CS_VALIDE);

            // Peut paraître stupide, mais ne l'est pas !!!
            // Si la base n'a pas été modifiée, et générée par l'impression des
            // attestations...
            // son taux d'IS est à zéro. Si tel est le cas, la ligne ci-dessous
            // va le setté avec le taux défini dans le prononcé.
            base.setTauxImpotSource(base.getTauxImpotSource());
            base.setCsCantonImpotSource(base.getCsCantonImpotSource());
            base.update(transaction);

            // // mettre le prononcé dans l'état valide
            // point ouvert 00658
            // ajout de l'etat decide
            if (/*
                 * !IIJPrononce.CS_VALIDE.equals(caViewBean.loadPrononce().getCsEtat ()) &&
                 */!IIJPrononce.CS_COMMUNIQUE.equals(caViewBean.loadPrononce().getCsEtat())) {
                // caViewBean.loadPrononce().setCsEtat(IIJPrononce.CS_VALIDE);

                if (caViewBean.loadPrononce().getAvecDecision().booleanValue()) {
                    caViewBean.loadPrononce().setCsEtat(IIJPrononce.CS_VALIDE);
                } else {
                    caViewBean.loadPrononce().setCsEtat(IIJPrononce.CS_DECIDE);
                }
                caViewBean.loadPrononce().update(transaction);
            }

            // On met a jours les formulaires d'indemnistation dans l'état RECU
            // avec la date du jour comme date de réception.
            // Seul les formulaire dans l'état envoyé sont mis à jours.
            try {
                doMAJEtatFormulaireIndemnisation(session, caViewBean.getIdBaseIndemnisation());
            } catch (Exception e) {
                // Ne devrait jamais arriver !!! Dans tous les cas, en cas
                // d'erreur, elle est ignorée !!!
                e.printStackTrace();
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
                } finally {
                    transaction.closeTransaction();
                }
            }
        }

        return viewBean;
    }

    private List calculPrestationsSansAcor(BSession session, BTransaction transaction, IJPrononce prononce,
            IJBaseIndemnisation baseIndemnisation, IJIJCalculee ijCalculee) throws Exception {

        List retValue = new ArrayList();

        String indemniteExt = "0.0";
        String indemniteInt = "0.0";
        String montantBrutExt = "0.0";
        String montantBrutInt = "0.0";

        // creation de la prestation, d'apres le schema, il y a au maximum UN
        // element paiement
        IJPrestation prestation = new IJPrestation();

        JADate dateDebutPrestation = new JADate(baseIndemnisation.getDateDebutPeriode());
        JADate dateFinPrestation = new JADate(baseIndemnisation.getDateFinPeriode());

        JADate dateDebutIJCalculee = new JADate(ijCalculee.getDateDebutDroit());
        JADate dateFinIJCalculee = null;

        if (!JadeStringUtil.isBlankOrZero(ijCalculee.getDateFinDroit())) {
            dateFinIJCalculee = new JADate(ijCalculee.getDateFinDroit());
        }

        JACalendar cal = new JACalendarGregorian();

        // On prend la plus grande date de début entre la base d'indemnisation
        // et de l'ijCalculee
        if (cal.compare(dateDebutPrestation, dateDebutIJCalculee) == JACalendar.COMPARE_FIRSTLOWER) {
            dateDebutPrestation = dateDebutIJCalculee;
        }

        // On prend la plus petite date de fin entre la base d'indemnisation et
        // de l'ijCalculee
        if (dateFinIJCalculee == null) {
            ;
        } else if (cal.compare(dateFinPrestation, dateFinIJCalculee) == JACalendar.COMPARE_FIRSTUPPER) {
            dateFinPrestation = dateFinIJCalculee;
        }

        prestation.setDateDebut(PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(dateDebutPrestation.toStrAMJ()));
        prestation.setDateFin(PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(dateFinPrestation.toStrAMJ()));
        prestation.setIdIJCalculee(ijCalculee.getIdIJCalculee());
        prestation.setIdBaseIndemnisation(baseIndemnisation.getIdBaseIndemisation());

        IJIndemniteJournaliereManager mgr = new IJIndemniteJournaliereManager();
        mgr.setSession(session);
        mgr.setForIdIJCalculee(ijCalculee.getIdIJCalculee());
        mgr.find(transaction);
        for (int i = 0; i < mgr.size(); i++) {
            IJIndemniteJournaliere elm = (IJIndemniteJournaliere) mgr.getEntity(i);
            if (IIJMesure.CS_INTERNE.equals(elm.getCsTypeIndemnisation())) {
                indemniteInt = elm.getMontantJournalierIndemnite();
            } else {
                indemniteExt = elm.getMontantJournalierIndemnite();
            }
        }

        if ((indemniteExt == null) && (indemniteInt == null)) {
            throw new PRACORException(session.getLabel("AUCUNE_IJ_CALCULEE"));
        }

        IJAttestationsJoursAdapter attestationsJours = new IJAttestationsJoursAdapter(baseIndemnisation, ijCalculee);

        prestation.setMontantBrutExterne(IJCalculACORDecompteHelper.multiply(indemniteExt,
                attestationsJours.getNbJoursExternes()));
        prestation.setMontantBrutInterne(IJCalculACORDecompteHelper.multiply(indemniteInt,
                attestationsJours.getNbJoursInternes()));
        prestation.setNombreJoursExt(attestationsJours.getNbJoursExternes());
        prestation.setNombreJoursInt(attestationsJours.getNbJoursInternes());
        prestation.setDateDecompte(JACalendar.todayJJsMMsAAAA());
        FWCurrency mbr = new FWCurrency(prestation.getMontantBrutInterne());
        mbr.add(prestation.getMontantBrutExterne());
        prestation.setMontantBrut(mbr.toString());

        // on recopie les montants journalier
        prestation.setMontantJournalierExterne(indemniteExt);
        prestation.setMontantJournalierInterne(indemniteInt);

        // sauver la sous prestation dans la base
        prestation.setSession(session);
        prestation.add(transaction);

        retValue.add(prestation);
        return retValue;

    }

    /**
     * 
     * Mise à jour de l'état des formulaires d'indemnisations liées à cette base. Passage de l'état de envoyé à reçu.
     * 
     * @param session
     * @throws Exception
     */
    protected void doMAJEtatFormulaireIndemnisation(BSession session, String idBaseIndemnisation) throws Exception {

        IJFormulaireIndemnisationManager mgr = new IJFormulaireIndemnisationManager();
        mgr.setSession(session);
        mgr.setForIdBaseIndemnisation(idBaseIndemnisation);
        mgr.find();
        for (int i = 0; i < mgr.size(); i++) {
            IJFormulaireIndemnisation entity = (IJFormulaireIndemnisation) mgr.get(i);
            if (IIJFormulaireIndemnisation.CS_ENVOYE.equals(entity.getEtat())) {
                entity.setEtat(IIJFormulaireIndemnisation.CS_RECU);
                entity.setDateReception(JACalendar.todayJJsMMsAAAA());
                entity.update();
            }
        }
    }

    /**
     * Helper de l'appel au service Web ACOR
     *
     * @param viewBean
     * @param action
     * @param session
     * @return
     * @throws Exception
     */
    public FWViewBeanInterface actionCallACORWeb(final FWViewBeanInterface viewBean, final FWAction action,
                                                 final BSession session) throws Exception {

        IJCalculACORDecompteViewBean ijViewBean = (IJCalculACORDecompteViewBean) viewBean;

        if (!viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
            ijViewBean.setAcorV4Web(true);
        }

        // TODO : vérifier s'il faut contrôler le calculable.
        ijViewBean.setCalculable(IJBaseIndemnisationRegles.isCalculerPermis(ijViewBean.loadBaseIndemnisation(),
                ijViewBean.loadPrononce()));

        return viewBean;
    }

    /**
     * retrouve par introspection la methode a executer quand on arrive dans ce helper avec une action custom.
     * 
     * @param viewBean
     *            DOCUMENT ME!
     * @param action
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        return deleguerExecute(viewBean, action, session);
    }

}
