package globaz.ij.helpers.acor;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.ij.acor.IJACORBatchFilePrinter;
import globaz.ij.api.prestations.IIJIJCalculee;
import globaz.ij.api.prestations.IIJPetiteIJCalculee;
import globaz.ij.api.prononces.IIJMesure;
import globaz.ij.api.prononces.IIJPrononce;
import globaz.ij.db.prestations.IJGrandeIJCalculee;
import globaz.ij.db.prestations.IJIJCalculee;
import globaz.ij.db.prestations.IJIndemniteJournaliere;
import globaz.ij.db.prestations.IJPetiteIJCalculee;
import globaz.ij.db.prononces.IJGrandeIJ;
import globaz.ij.db.prononces.IJPetiteIJ;
import globaz.ij.db.prononces.IJPrononce;
import globaz.ij.db.prononces.IJPrononceAit;
import globaz.ij.regles.IJPrononceRegles;
import globaz.ij.vb.acor.IJCalculACORIJViewBean;
import globaz.ij.vb.prononces.IJSituationProfessionnelleViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.acor.PRAcorFileContent;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.helpers.PRAbstractHelper;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRDateFormater;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * helper pour la premiere phase de calcul avec globaz.ij.acor.
 * </p>
 * 
 * <p>
 * importation et exportation des fichiers globaz.ij.acor du calcul des ij.
 * </p>
 * 
 * @author vre
 */
public class IJCalculACORIJHelper extends PRAbstractHelper {

    private static final String ZERO = "0";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * cette methode est redefinie pour charger le prononce pour son affichage dans l'ecran de.
     * 
     * @param viewBean
     *            DOCUMENT ME!
     * @param action
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        IJCalculACORIJViewBean caViewBean = (IJCalculACORIJViewBean) viewBean;
        PRTiersWrapper tiers = caViewBean.loadPrononce(null).loadDemande(null).loadTiers();

        caViewBean.setNoAVSAssure(tiers.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));
        caViewBean.setNomPrenomAssure(tiers.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                + tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM));

        // on verifie que l'on peut calculer les ij
        caViewBean.setCalculable(IJPrononceRegles.isCalculerPermis(caViewBean.loadPrononce(null)));
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
    // IJCalculACORIJViewBean caViewBean = (IJCalculACORIJViewBean) viewBean;
    //
    // IJACORBatchFilePrinter.getInstance().printBatchFileIJ(caViewBean.getWriter(), session,
    // caViewBean.loadPrononce(null), PRACORConst.dossierACOR(session));
    //
    // return viewBean;
    // }

    public FWViewBeanInterface actionExporterScriptACOR2(FWViewBeanInterface viewBean, FWAction action, BSession session)
            throws Exception {
        IJCalculACORIJViewBean caViewBean = (IJCalculACORIJViewBean) viewBean;

        Map filesContent = new HashMap();
        IJACORBatchFilePrinter.getInstance().printBatchFileIJ(filesContent, session, caViewBean.loadPrononce(null),
                PRACORConst.dossierACOR(session));
        // on verifie que l'on peut calculer les ij
        caViewBean.setCalculable(IJPrononceRegles.isCalculerPermis(caViewBean.loadPrononce(null)));

        filtrerLigneVide(filesContent);

        caViewBean.setFilesContent(filesContent);
        caViewBean.setIsFileContent(true);

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
     * DOCUMENT ME!
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
    public FWViewBeanInterface actionImporterIJ(FWViewBeanInterface viewBean, FWAction action, BSession session)
            throws Exception {
        IJCalculACORIJViewBean caViewbean = (IJCalculACORIJViewBean) viewBean;
        BITransaction transaction = null;

        // charger le prononce
        IJPrononce prononce = caViewbean.loadPrononce(null);
        if (prononce == null || prononce.isNew()) {
            throw new IllegalArgumentException("Unable to find the IJPrononce with id [" + caViewbean.getIdPrononce()
                    + "]");
        }

        try {
            // HACK: on cree une transaction pour etre sur que tous les ajouts
            // peuvent etre rollbackes
            // note: la transaction est enregistree dans la session est sera
            // utilisee dans tous les entity qui l'utilise
            transaction = session.newTransaction();
            transaction.openTransaction();

            // importation des ij calculees
            LinkedList ijs = new LinkedList();

            // importation des ij de troisieme revision
            if (!JadeStringUtil.isEmpty(caViewbean.getContenuAnnoncePay())) {
                ijs.addAll(globaz.ij.acor.parser.rev3.IJACORBasesCalculParser.parse(session,
                        caViewbean.loadPrononce(null), new StringReader(caViewbean.getContenuAnnoncePay())));
            }

            // importation des ij de quatrieme/cinquième revision
            if (!JadeStringUtil.isEmpty(caViewbean.getContenuFCalculXML())) {

                // workaround : la feuille de calcul acor contient le caractère
                // : &label;
                // Ce caractère fait planter l'importation des données sur AIX.
                final String UNRESOLVED_CHAR_LABEL = "&labels;";
                caViewbean.setContenuFCalculXML(JadeStringUtil.change(caViewbean.getContenuFCalculXML(),
                        UNRESOLVED_CHAR_LABEL, ""));

                ijs.addAll(globaz.ij.acor.parser.rev5.IJACORBasesCalculParser.parse(session,
                        (BTransaction) transaction, caViewbean.loadPrononce(null),
                        new StringReader(caViewbean.getContenuFCalculXML())));

                // En cas de plusieurs IJ Calculée, certaines n'ont pas forcément de date de fin, car non
                // retrounée par ACOR
                // -> MAJ des dates de fin avec date de début de la prestations suivant -1 jour
                // Les prestations sont dans l'ordre chronologie...
                try {
                    class IJUtil {
                        String dateDebut;
                        String dateFin;
                        String id;
                    }

                    if (ijs.size() > 1) {
                        Map<String, IJUtil> m = new TreeMap();

                        for (Iterator<IJIJCalculee> iterator = ijs.iterator(); iterator.hasNext();) {
                            IJIJCalculee ij = iterator.next();
                            IJUtil u = new IJUtil();
                            u.id = ij.getIdIJCalculee();
                            u.dateDebut = ij.getDateDebutDroit();
                            u.dateFin = ij.getDateFinDroit();
                            m.put(PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(ij.getDateDebutDroit()), u);
                        }

                        Set<String> s = m.keySet();

                        Iterator<String> it1 = s.iterator();

                        String currentKey = null;
                        String nextKey = null;

                        do {
                            if (currentKey == null) {
                                if (it1.hasNext()) {
                                    currentKey = it1.next();
                                }
                            }
                            if (it1.hasNext()) {
                                nextKey = it1.next();
                            }

                            IJUtil u = m.get(currentKey);
                            if (JadeStringUtil.isBlankOrZero(u.dateFin)) {
                                if (nextKey != null) {
                                    IJUtil nextU = m.get(nextKey);
                                    // Calcul de la date de fin....
                                    IJIJCalculee ij = new IJIJCalculee();
                                    ij.setSession(session);
                                    ij.setIdIJCalculee(u.id);
                                    ij.retrieve();

                                    JADate dd = new JADate(nextU.dateDebut);
                                    JACalendar cal = new JACalendarGregorian();
                                    JADate df = cal.addDays(dd, -1);
                                    ij.setDateFinDroit(PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(df.toStrAMJ()));
                                    ij.update();
                                }
                            }
                            currentKey = nextKey;
                        } while (it1.hasNext());
                    }
                } catch (Exception e) {
                    // Erreur lors de la tentative de recalcul de la date de fin de l'IJCalculée
                    e.printStackTrace();
                }
            }



            // restituer ou annuler le prononcé d'origine si celui-ci est une
            // correction
            if (!JadeStringUtil.isIntegerEmpty(prononce.getIdCorrection())) {
                IJPrononce prononceOrigine = IJPrononce.loadPrononce(session, null, prononce.getIdCorrection(),
                        prononce.getCsTypeIJ());

                IJPrononceRegles.restitutionEtCorrection(session, transaction, prononceOrigine, prononce, ijs);
                prononceOrigine.update(transaction);
            }

            // Si pas de decision, on passe le prononce dans l'etat decide
            // Sinon on passe dans l'etat valide
            if (prononce.getAvecDecision().booleanValue()) {
                prononce.setCsEtat(IIJPrononce.CS_VALIDE);
            } else {
                prononce.setCsEtat(IIJPrononce.CS_DECIDE);
            }
            prononce.update(transaction);
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

    private IJIJCalculee creerIJcaluleeA0(BSession session, IJPrononce prononce) throws Exception {
        IJIJCalculee ijCalculee = new IJIJCalculee();
        ijCalculee.setSession(session);

        String typeIJ = prononce.getCsTypeIJ();
        if (JadeStringUtil.isBlankOrZero(typeIJ)) {
            throw new IllegalArgumentException("Le type d'IJ est invlaide [" + typeIJ + "]");
        }

        // IJCALCUL.XNTGRE -> IJPRONAI.XBTGEN
        ijCalculee.setCsGenreReadaptation(prononce.getCsGenre());
        // IJCALCUL.XNTSTA -> IJPRONAI.
        ijCalculee.setCsStatutProfessionnel(prononce.getCsStatutProfessionnel());
        // IJCALCUL.XNTTBA -> IJPRONAI.
        ijCalculee.setCsTypeBase(prononce.getCsTypeHebergement());
        // IJCALCUL.XNTTIJ -> IJPRONAI.
        ijCalculee.setCsTypeIJ(typeIJ);
        // IJCALCUL.XNDDEB -> IJPRONAI.
        ijCalculee.setDateDebutDroit(prononce.getDateDebutPrononce());
        // IJCALCUL.XNDFIN -> IJPRONAI.
        ijCalculee.setDateFinDroit(prononce.getDateFinPrononce());
        // IJCALCUL.XNDPRO -> IJPRONAI.
        ijCalculee.setDatePrononce(prononce.getDatePrononce());
        // IJCALCUL.XNIPAI ->
        ijCalculee.setIdPrononce(prononce.getIdPrononce());
        // IJCALCUL.XNLOAI -> IJPRONAI.XBNOAI
        ijCalculee.setOfficeAI(prononce.getOfficeAI());

        // IJCALCUL.XNDREV -> IJPRONAI.
        ijCalculee.setDateRevenu(ZERO);
        // IJCALCUL.XNMDRE -> IJPRONAI.
        ijCalculee.setDifferenceRevenu(ZERO);
        // IJCALCUL.XNBDPE -> IJPRONAI.
        ijCalculee.setIsDroitPrestationPourEnfant(false);
        // IJCALCUL.XNMMBA -> IJPRONAI.
        ijCalculee.setMontantBase(ZERO);
        // IJCALCUL.XNLNOR -> IJPRONAI.
        ijCalculee.setNoRevision("5");
        // IJCALCUL.XNMINP -> IJPRONAI.
        ijCalculee.setPourcentDegreIncapaciteTravail(ZERO);
        // IJCALCUL.XNMRED -> IJPRONAI.
        ijCalculee.setRevenuDeterminant(ZERO);
        // IJCALCUL.XNMRJR -> IJPRONAI.
        ijCalculee.setRevenuJournalierReadaptation(ZERO);
        // IJCALCUL.XNMSPS -> IJPRONAI.
        ijCalculee.setSupplementPersonneSeule(ZERO);
        // IJCALCUL.XNMDIJ -> IJPRONAI.
        ijCalculee.setDemiIJACBrut(ZERO);

        PRDemande demande = new PRDemande();
        demande.setSession(session);
        demande.setIdDemande(prononce.getIdDemande());
        demande.retrieve();
        if (demande.isNew()) {
            throw new IllegalArgumentException("Unable to find the PRDemande with id [" + prononce.getIdDemande() + "]");
        }

        PRTiersWrapper tiers = PRTiersHelper.getTiersById(session, demande.getIdTiers());
        if (tiers == null) {
            throw new IllegalArgumentException("Unable to find the PRTiers with id [" + demande.getIdTiers() + "]");
        }
        // IJCALCUL.XNNAVS -> IJPRONAI.
        // TODO doit ne pas être formaté 756923283421
        ijCalculee.setNoAVS(tiers.getNSS());

        ijCalculee.add();

        // --------------------------------//
        if (IIJIJCalculee.CS_TYPE_GRANDE_IJ.equals(typeIJ)) {
            creerGrandeIJ(ijCalculee.getIdIJCalculee(), session);
        } else if (IIJIJCalculee.CS_TYPE_PETITE_IJ.equals(typeIJ)) {
            creerPetiteIJ(ijCalculee.getIdIJCalculee(), session);
        }
        // ------------------------------//

        creerIJIndemniteJournaliere(ijCalculee.getIdIJCalculee(), session);

        return ijCalculee;

    }

    /**
     * @param session
     * @param ijCalculee
     * @throws Exception
     */
    private void creerIJIndemniteJournaliere(String idIJCalculee, BSession session) throws Exception {
        IJIndemniteJournaliere indemniteJournaliere = null;
        // ------------------------------------------//

        // 1ère IJ de type interne
        indemniteJournaliere = creerIndemniteJournaliereSansType();
        indemniteJournaliere.setSession(session);
        indemniteJournaliere.setIdIJCalculee(idIJCalculee);
        // IJINDJRN.XWTTIN
        indemniteJournaliere.setCsTypeIndemnisation(IIJMesure.CS_INTERNE);
        indemniteJournaliere.add();
        // ------------------------------------------//

        // 2ème IJ de type externe
        indemniteJournaliere = creerIndemniteJournaliereSansType();
        indemniteJournaliere.setSession(session);
        indemniteJournaliere.setIdIJCalculee(idIJCalculee);
        // IJINDJRN.XWTTIN
        indemniteJournaliere.setCsTypeIndemnisation(IIJMesure.CS_EXTERNE);
        indemniteJournaliere.add();
    }

    /**
     * @param indemniteJournaliere
     */
    private IJIndemniteJournaliere creerIndemniteJournaliereSansType() {
        IJIndemniteJournaliere indemniteJournaliere = new IJIndemniteJournaliere();
        // IJINDJRN.XWMDRA
        indemniteJournaliere.setDeductionRenteAI(ZERO);
        // IJINDJRN.XWMFRR
        indemniteJournaliere.setFractionReductionSiRevenuAvantReadaptation(ZERO);
        // IJINDJRN.XWMIAR
        indemniteJournaliere.setIndemniteAvantReduction(ZERO);
        // IJINDJRN.XWMMCO
        indemniteJournaliere.setMontantComplet(ZERO);
        // IJINDJRN.XWMGNR
        indemniteJournaliere.setMontantGarantiAANonReduit(ZERO);
        // IJINDJRN.XWMGAR
        indemniteJournaliere.setMontantGarantiAAReduit(ZERO);
        // IJINDJRN.XWMMJI
        indemniteJournaliere.setMontantJournalierIndemnite(ZERO);
        // IJINDJRN.XWMMPL
        indemniteJournaliere.setMontantPlafonne(ZERO);
        // IJINDJRN.XWMMPM
        indemniteJournaliere.setMontantPlafonneMinimum(ZERO);
        // IJINDJRN.XWMMRR
        indemniteJournaliere.setMontantReductionSiRevenuAvantReadaptation(ZERO);
        // IJINDJRN.XWMMSR
        indemniteJournaliere.setMontantSupplementaireReadaptation(ZERO);
        return indemniteJournaliere;
    }

    private void creerGrandeIJ(String idIJCalculee, BSession session) throws Exception {
        IJGrandeIJCalculee grandeIJ = new IJGrandeIJCalculee();
        grandeIJ.setSession(session);
        // IJGIJCAL.XUIIJC
        grandeIJ.setIdIJCalculee(idIJCalculee);
        // IJGIJCAL.XUMMIA
        grandeIJ.setMontantIndemniteAssistance(ZERO);
        // IJGIJCAL.XUMMIE
        grandeIJ.setMontantIndemniteEnfant(ZERO);
        // IJGIJCAL.XUMMEX
        grandeIJ.setMontantIndemniteExploitation(ZERO);
        // IJGIJCAL.XUNNBE
        grandeIJ.setNbEnfants(ZERO);

        grandeIJ.add();
    }

    private void creerPetiteIJ(String idIJCalculee, BSession session) throws Exception {
        IJPetiteIJCalculee petiteIJ = new IJPetiteIJCalculee();
        petiteIJ.setSession(session);
        // IJPIJCAL.XTIIJC
        petiteIJ.setIdIJCalculee(idIJCalculee);
        // TODO
        // IJPIJCAL.XTTMCA --> IIJPetiteIJCalculee.CS_????
        petiteIJ.setCsModeCalcul(IIJPetiteIJCalculee.CS_ECOLE_SPECIALE);
        // ---------------------------------//

        petiteIJ.add();
    }

    private IJPetiteIJ creerPetiteIJ(BSession session) {
        // TODO Auto-generated method stub
        return null;
    }

    private IJGrandeIJ creerGrandeIJ(BSession session) {
        // TODO Auto-generated method stub
        return null;
    }

    private boolean hasIJCalculee() {
        // TODO Auto-generated method stub
        return true;
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
    public FWViewBeanInterface calculerAit(FWViewBeanInterface viewBean, FWAction action, BSession session)
            throws Exception {

        IJSituationProfessionnelleViewBean spViewBean = (IJSituationProfessionnelleViewBean) viewBean;

        BITransaction transaction = null;
        LinkedList<IJIJCalculee> ijs = new LinkedList<IJIJCalculee>();

        try {
            // HACK: on cree une transaction pour etre sur que tous les ajouts
            // peuvent etre rollbackes
            // note: la transaction est enregistree dans la session est sera
            // utilisee dans tous les entity qui l'utilise
            transaction = session.newTransaction();
            transaction.openTransaction();

            // on cherche le prononce AIT
            IJPrononceAit prononceAit = new IJPrononceAit();
            prononceAit.setSession(session);
            prononceAit.setIdPrononce(spViewBean.loadPrononce().getIdPrononce());
            prononceAit.retrieve(transaction);

            // creation de l'ij calculee
            IJIJCalculee ijCal = new IJIJCalculee();
            ijCal.setSession(session);
            ijCal.setCsTypeIJ(spViewBean.getCsTypeIJ());
            ijCal.setDateDebutDroit(prononceAit.getDateDebutPrononce());
            ijCal.setDatePrononce(prononceAit.getDateDebutPrononce());
            ijCal.setOfficeAI(prononceAit.getOfficeAI());
            ijCal.setIdPrononce(prononceAit.getIdPrononce());
            ijCal.add(transaction);
            ijs.add(ijCal);

            // creation de l'indemnite journaliere
            IJIndemniteJournaliere indJour = new IJIndemniteJournaliere();
            indJour.setSession(session);
            indJour.setIdIJCalculee(ijCal.getIdIJCalculee());
            indJour.setMontantJournalierIndemnite(prononceAit.getMontant());
            indJour.add(transaction);

            // on passe le prononce dans l'etat décidé
            prononceAit.setCsEtat(IIJPrononce.CS_DECIDE);
            prononceAit.update(transaction);

            // restituer ou annuler le prononcé d'origine si celui-ci est une
            // correction
            if (!JadeStringUtil.isIntegerEmpty(prononceAit.getIdCorrection())) {
                IJPrononce prononceOrigine = IJPrononce.loadPrononce(session, null, prononceAit.getIdCorrection(),
                        prononceAit.getCsTypeIJ());

                IJPrononceRegles.restitutionEtCorrection(session, transaction, prononceOrigine, prononceAit, ijs);
                prononceOrigine.update(transaction);
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

        return spViewBean;
    }

    /**
     * trouver par introspection la methode de ced helper a utiliser si l'on arrive avec une action custom.
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
