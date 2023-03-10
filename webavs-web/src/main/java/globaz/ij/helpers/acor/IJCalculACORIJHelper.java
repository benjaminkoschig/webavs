package globaz.ij.helpers.acor;

import globaz.corvus.vb.acor.RECalculACORDemandeRenteViewBean;
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
import globaz.ij.api.prononces.IIJPrononce;
import globaz.ij.db.prestations.IJIJCalculee;
import globaz.ij.db.prestations.IJIndemniteJournaliere;
import globaz.ij.db.prononces.IJPrononce;
import globaz.ij.db.prononces.IJPrononceAit;
import globaz.ij.regles.IJBaseIndemnisationRegles;
import globaz.ij.regles.IJPrononceRegles;
import globaz.ij.vb.acor.IJCalculACORIJViewBean;
import globaz.ij.vb.prononces.IJSituationProfessionnelleViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.acor.PRAcorFileContent;
import globaz.prestation.helpers.PRAbstractHelper;
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

            // importation des ij de quatrieme/cinqui?me revision
            if (!JadeStringUtil.isEmpty(caViewbean.getContenuFCalculXML())) {

                // workaround : la feuille de calcul acor contient le caract?re
                // : &label;
                // Ce caract?re fait planter l'importation des donn?es sur AIX.
                final String UNRESOLVED_CHAR_LABEL = "&labels;";
                caViewbean.setContenuFCalculXML(JadeStringUtil.change(caViewbean.getContenuFCalculXML(),
                        UNRESOLVED_CHAR_LABEL, ""));

                ijs.addAll(globaz.ij.acor.parser.rev5.IJACORBasesCalculParser.parse(session,
                        (BTransaction) transaction, caViewbean.loadPrononce(null),
                        new StringReader(caViewbean.getContenuFCalculXML())));

                // En cas de plusieurs IJ Calcul?e, certaines n'ont pas forc?ment de date de fin, car non
                // retroun?e par ACOR
                // -> MAJ des dates de fin avec date de d?but de la prestations suivant -1 jour
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
                    // Erreur lors de la tentative de recalcul de la date de fin de l'IJCalcul?e
                    e.printStackTrace();
                }
            }



            // restituer ou annuler le prononc? d'origine si celui-ci est une
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

            // on passe le prononce dans l'etat d?cid?
            prononceAit.setCsEtat(IIJPrononce.CS_DECIDE);
            prononceAit.update(transaction);

            // restituer ou annuler le prononc? d'origine si celui-ci est une
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


    public FWViewBeanInterface actionCallACORWeb(FWViewBeanInterface viewBean, FWAction action, BSession session) throws Exception {


        IJCalculACORIJViewBean ijViewBean = (IJCalculACORIJViewBean) viewBean;

        if (!viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
            ijViewBean.setAcorV4Web(true);
        }

        // TODO : voir s'il faut contr?ler le setCalculable
        ijViewBean.setCalculable(IJPrononceRegles.isCalculerPermis(ijViewBean.loadPrononce(null)));

        return viewBean;
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
