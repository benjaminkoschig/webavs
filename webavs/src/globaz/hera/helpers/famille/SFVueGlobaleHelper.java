/*
 * Créé le 27 oct. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.hera.helpers.famille;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.hera.db.famille.SFApercuEnfant;
import globaz.hera.db.famille.SFApercuEnfantManager;
import globaz.hera.db.famille.SFConjoint;
import globaz.hera.db.famille.SFConjointManager;
import globaz.hera.db.famille.SFMembreFamille;
import globaz.hera.db.famille.SFPeriode;
import globaz.hera.db.famille.SFPeriodeManager;
import globaz.hera.db.famille.SFRelationConjoint;
import globaz.hera.db.famille.SFRelationConjointManager;
import globaz.hera.db.famille.SFRequerant;
import globaz.hera.vb.famille.SFApercuRelationConjointListViewBean;
import globaz.hera.vb.famille.SFApercuRelationConjointViewBean;
import globaz.hera.vb.famille.SFConjointVO;
import globaz.hera.vb.famille.SFMembreVO;
import globaz.hera.vb.famille.SFPeriodeVO;
import globaz.hera.vb.famille.SFRelationVO;
import globaz.hera.vb.famille.SFVueGlobaleViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.prestation.helpers.PRAbstractHelper;
import globaz.prestation.tools.PRAssert;
import globaz.prestation.tools.PRDateFormater;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author scr
 * 
 */
public class SFVueGlobaleHelper extends PRAbstractHelper {

    /**
     * 
     * Permet de trier les familles ordonnées par date des relations.
     * 
     * @author SCR
     * 
     */
    private class RelationsComparator implements Comparator {

        private BSession session = null;

        RelationsComparator(BSession session) {
            this.session = session;
        }

        @Override
        public int compare(Object o1, Object o2) {

            String key1 = (String) o1;
            String key2 = (String) o2;

            try {
                SFConjointManager mgr = new SFConjointManager();
                String s1 = ((String) o1).substring(0, ((String) o1).indexOf('-'));
                String s2 = ((String) o1).substring(s1.length() + 1, ((String) o1).length());
                mgr.setForIdsConjoints(s1, s2);
                mgr.setSession(session);
                mgr.find(1);
                SFConjoint co1 = (SFConjoint) mgr.getFirstEntity();

                SFRelationConjointManager mgr2 = new SFRelationConjointManager();
                mgr2.setSession(session);
                mgr2.setForIdDesConjoints(co1.getIdConjoints());
                mgr2.setOrderByDateDebutDsc(true);
                mgr2.find(1);
                // On récupère la relation la plus récente
                SFRelationConjoint rc1 = (SFRelationConjoint) mgr2.getFirstEntity();

                s1 = ((String) o2).substring(0, ((String) o2).indexOf('-'));
                s2 = ((String) o2).substring(s1.length() + 1, ((String) o2).length());
                mgr.setForIdsConjoints(s1, s2);
                mgr.find(1);
                SFConjoint co2 = (SFConjoint) mgr.getFirstEntity();
                mgr2 = new SFRelationConjointManager();
                mgr2.setSession(session);
                mgr2.setForIdDesConjoints(co2.getIdConjoints());
                mgr2.setOrderByDateDebutDsc(true);
                mgr2.find(1);
                // On récupère la relation la plus récente
                SFRelationConjoint rc2 = (SFRelationConjoint) mgr2.getFirstEntity();

                String d1AMJ = "31.12.2999";
                if (rc1 != null) {
                    d1AMJ = PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(rc1.getDateDebut());
                }
                String d2AMJ = "31.12.2999";
                if (rc2 != null) {
                    d2AMJ = PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(rc2.getDateDebut());
                }

                // Ce cas arrive avec des relations de type enfant commun ou
                // indéfinie...
                if (JadeStringUtil.isBlankOrZero(d1AMJ)) {
                    d1AMJ = "01.01.1699-";
                }

                if (JadeStringUtil.isBlankOrZero(d2AMJ)) {
                    d2AMJ = "01.01.1699-";
                }

                // Si l'on compare une relation indéfinie avec une relation
                // enfant_commun,
                // d1AMJ sera égal à d2AMJ, et la méthode containsKey(..) va
                // retrouner true, alors
                // qu'il s'agit de 2 relations différentes, ce qui aura pour
                // effet de rajouter des enfants dans
                // la map pour les mauvais conjoints.
                // Pour éviter ceci, on rajoute la clé de comparaison.
                d1AMJ += key1;
                d2AMJ += key2;

                return d2AMJ.compareTo(d1AMJ);

            } catch (Exception e) {
                return -1;
            }
        }
    }

    public FWViewBeanInterface afficherFamilleMembre(FWViewBeanInterface viewBean, FWAction action, BSession session)
            throws Exception {

        return afficherFamilleRequerant(viewBean, action, session);

    }

    public FWViewBeanInterface afficherFamilleRequerant(FWViewBeanInterface viewBean, FWAction action, BSession session)
            throws Exception {

        SFVueGlobaleViewBean vb = (SFVueGlobaleViewBean) viewBean;
        vb.clearListNssMembreFamille();

        vb.setParent1Liant(null);
        vb.setParent2Liant(null);
        BITransaction transaction = session.newTransaction();
        try {
            transaction.openTransaction();

            /*
             * 
             * phase préliminaire On récupère les parents du liant....
             */
            SFApercuEnfantManager mgr0 = new SFApercuEnfantManager();
            mgr0.setSession(session);
            mgr0.setForIdMembreFamille(vb.getLiant().getMembreFamille().getIdMembreFamille());
            mgr0.find(transaction);

            if (mgr0.size() > 1) {
                throw new Exception(session.getLabel("ERROR_INCOHERENCE_PLUSIEURS_ENFANTS")
                        + vb.getLiant().getMembreFamille().getIdMembreFamille());
            }

            try {
                if ((vb.getLiant() != null) && (vb.getLiant().getMembreFamille() != null)
                        && !JadeStringUtil.isBlankOrZero(vb.getLiant().getMembreFamille().getNssFormatte())) {
                    vb.addNssMembreFamille(vb.getLiant().getMembreFamille().getNssFormatte());
                }
            } catch (Exception e) {
                ;
            }

            if (!mgr0.isEmpty()) {

                SFApercuEnfant enfant = (SFApercuEnfant) mgr0.getFirstEntity();

                SFConjoint conjoint = new SFConjoint();
                conjoint.setIdConjoints(enfant.getIdConjoint());
                conjoint.setSession(session);
                conjoint.retrieve(transaction);
                PRAssert.notIsNew(conjoint, null);

                SFMembreFamille p1 = new SFMembreFamille();
                p1.setSession(session);
                p1.setIdMembreFamille(conjoint.getIdConjoint1());
                p1.retrieve(transaction);

                SFMembreFamille p2 = new SFMembreFamille();
                p2.setSession(session);
                p2.setIdMembreFamille(conjoint.getIdConjoint2());
                p2.retrieve(transaction);

                SFMembreVO mvo = new SFMembreVO();
                mvo.setCsDomaine(p1.getCsDomaineApplication());
                mvo.setCsNationalite(p1.getCsNationalite());
                mvo.setCsSexe(p1.getCsSexe());
                mvo.setDateDeces(p1.getDateDeces());
                mvo.setDateNaissance(p1.getDateNaissance());
                mvo.setIdMembreFamille(p1.getIdMembreFamille());
                mvo.setIdTiers(p1.getIdTiers());
                mvo.setNom(p1.getNom());
                mvo.setNssFormatte(p1.getNss());
                mvo.setPrenom(p1.getPrenom());
                vb.setParent1Liant(mvo);
                vb.addNssMembreFamille(p1.getNss());

                mvo = new SFMembreVO();
                mvo.setCsDomaine(p2.getCsDomaineApplication());
                mvo.setCsNationalite(p2.getCsNationalite());
                mvo.setCsSexe(p2.getCsSexe());
                mvo.setDateDeces(p2.getDateDeces());
                mvo.setDateNaissance(p2.getDateNaissance());
                mvo.setIdMembreFamille(p2.getIdMembreFamille());
                mvo.setIdTiers(p2.getIdTiers());
                mvo.setNom(p2.getNom());
                mvo.setNssFormatte(p2.getNss());
                mvo.setPrenom(p2.getPrenom());
                vb.setParent2Liant(mvo);
                vb.addNssMembreFamille(p2.getNss());

            }

            /*
             * 
             * 1ère passe, construction de la famille proche
             */

            // On ajoute les périodes du requérant !!!
            SFPeriodeManager periodes = new SFPeriodeManager();
            periodes.setSession(session);
            periodes.setForIdMembreFamille(vb.getLiant().getMembreFamille().getIdMembreFamille());

            periodes.find(transaction);

            for (int i = 0; i < periodes.size(); i++) {
                SFPeriode periode = (SFPeriode) periodes.get(i);
                SFPeriodeVO pvo = periode.toValueObject(session);
                vb.getLiant().getMembreFamille().addPeriode(pvo);
            }

            SFApercuRelationConjointListViewBean mgr = new SFApercuRelationConjointListViewBean();
            mgr.setSession(session);
            mgr.setForIdConjoint(vb.getLiant().getMembreFamille().getIdMembreFamille());

            mgr.find(transaction);

            JADate minDate = new JADate("01.01.2999");
            JADate maxDate = new JADate("01.01.1900");
            JACalendar cal = new JACalendarGregorian();

            Map mapConjoints = new TreeMap(new RelationsComparator(session));

            int nombreRelationMaxParConjoint = 0;

            for (Iterator iterator = mgr.iterator(); iterator.hasNext();) {
                SFApercuRelationConjointViewBean elm = (SFApercuRelationConjointViewBean) iterator.next();
                SFConjointVO conjVO = new SFConjointVO();
                JADate dd = new JADate(elm.getDateDebut());
                JADate df = null;
                if (!JadeStringUtil.isBlankOrZero(elm.getDateFin())) {
                    df = new JADate(elm.getDateFin());
                }

                if ((cal.compare(minDate, dd) == JACalendar.COMPARE_FIRSTUPPER)
                        && !ISFSituationFamiliale.CS_REL_CONJ_ENFANT_COMMUN.equals(elm.getTypeRelation())
                        && !ISFSituationFamiliale.CS_REL_CONJ_RELATION_INDEFINIE.equals(elm.getTypeRelation())) {

                    minDate = new JADate(elm.getDateDebut());
                }

                if (df == null) {
                    maxDate = null;
                } else {
                    if (cal.compare(maxDate, df) == JACalendar.COMPARE_FIRSTLOWER) {
                        maxDate = new JADate(elm.getDateFin());
                    }
                }

                conjVO.setIdMembreFamille1(elm.getIdConjoint1());
                conjVO.setIdMembreFamille2(elm.getIdConjoint2());
                conjVO.setIdTiers1(elm.getIdTiers1());
                conjVO.setIdTiers2(elm.getIdTiers2());
                conjVO.setNss1(elm.getNoAvs1());
                conjVO.setNss2(elm.getNoAvs2());
                conjVO.setIdRelationConjoint(elm.getIdRelationConjoint());
                conjVO.setCsSexe1(elm.getSexe1());
                conjVO.setCsSexe2(elm.getSexe2());
                conjVO.setDateDeces1(elm.getDateDeces1());
                conjVO.setDateDeces2(elm.getDateDeces2());

                vb.addNssMembreFamille(elm.getNoAvs1());
                vb.addNssMembreFamille(elm.getNoAvs2());

                // On inverse les id, car retourne les info pour l'autre
                // conjoint !!!
                // Merci MMU !??&//(%&*/*&ç*
                conjVO.setDescriptionConjoint1(elm.getDetailMembreFamilleVG(elm.getIdConjoint2()));
                conjVO.setDescriptionConjoint2(elm.getDetailMembreFamilleVG(elm.getIdConjoint1()));

                if (mapConjoints.containsKey(conjVO.getKey())) {
                    conjVO = (SFConjointVO) mapConjoints.get(conjVO.getKey());
                }
                // On ajoute les enfants, 1 seule fois !!!
                else {
                    for (Iterator iter = elm.getEnfants(transaction); iter.hasNext();) {
                        SFApercuEnfant enfant = (SFApercuEnfant) iter.next();
                        SFMembreVO mvo = new SFMembreVO();

                        mvo.setCsDomaine(enfant.getCsDomaineApplication());
                        mvo.setCsNationalite(enfant.getCsNationalite());
                        mvo.setCsSexe(enfant.getCsSexe());
                        mvo.setDateDeces(enfant.getDateDeces());
                        mvo.setDateNaissance(enfant.getDateNaissance());
                        mvo.setIdMembreFamille(enfant.getIdMembreFamille());
                        mvo.setIdTiers(enfant.getIdTiers());
                        mvo.setNom(enfant.getNom());
                        mvo.setNssFormatte(enfant.getNss());
                        mvo.setPrenom(enfant.getPrenom());
                        conjVO.addEnfant(mvo);
                        vb.addNssMembreFamille(enfant.getNss());
                    }

                    // On ajoute les périodes (1 seule fois) !!!
                    conjVO.initPeriodes(session, (BTransaction) transaction);

                }

                SFRelationVO relVO = new SFRelationVO();
                relVO.setDateDebut(elm.getDateDebut());
                relVO.setDateFin(elm.getDateFin());
                relVO.setCsTypeRelation(elm.getTypeRelation());
                relVO.setIdRelation(elm.getIdRelationConjoint());
                conjVO.addRelation(relVO);

                // On écrase, le cas échéant.
                mapConjoints.put(conjVO.getKey(), conjVO);
            }

            nombreRelationMaxParConjoint = mgr.getSize();

            Set keys = mapConjoints.keySet();
            for (Iterator iterator = keys.iterator(); iterator.hasNext();) {
                String key = (String) iterator.next();

                SFConjointVO cvo = (SFConjointVO) mapConjoints.get(key);
                vb.addConjointDuLiant(cvo);
            }

            /*
             * 
             * 2ème passe, construction de la famille étendue
             * 
             * Conjoints des conjoints du liant !!!
             */

            String idMembreFamilleDuLiant = vb.getLiant().getMembreFamille().getIdMembreFamille();
            // Pour chaque conjoint, on récupère ces ex-conjoints.
            for (Iterator iterator = keys.iterator(); iterator.hasNext();) {

                String key = (String) iterator.next();

                SFConjointVO cvo = (SFConjointVO) mapConjoints.get(key);

                String idDuConjointDuLiant = "";
                if (idMembreFamilleDuLiant.equals(cvo.getIdMembreFamille1())) {
                    idDuConjointDuLiant = cvo.getIdMembreFamille2();
                } else {
                    idDuConjointDuLiant = cvo.getIdMembreFamille1();
                }

                // On skip le conjoint inconnu...
                if (ISFSituationFamiliale.ID_MEMBRE_FAMILLE_CONJOINT_INCONNU.equals(idDuConjointDuLiant)) {
                    continue;
                }

                mgr = new SFApercuRelationConjointListViewBean();
                mgr.setSession(session);
                mgr.setForIdConjoint(idDuConjointDuLiant);

                mgr.find(transaction);

                if (nombreRelationMaxParConjoint < mgr.getSize()) {
                    nombreRelationMaxParConjoint = mgr.getSize();
                }

                Map mapConjointsDesConjointsDuLiant = new TreeMap(new RelationsComparator(session));

                for (Iterator iterator2 = mgr.iterator(); iterator2.hasNext();) {
                    SFApercuRelationConjointViewBean elm = (SFApercuRelationConjointViewBean) iterator2.next();

                    if (idMembreFamilleDuLiant.equals(elm.getIdConjoint1())
                            || idMembreFamilleDuLiant.equals(elm.getIdConjoint2())) {

                        // On ne reprend pas le liant, car déjà traité dans
                        // la phase 1
                        continue;

                    }

                    SFConjointVO conjVO = new SFConjointVO();
                    JADate dd = new JADate(elm.getDateDebut());
                    JADate df = null;
                    if (!JadeStringUtil.isBlankOrZero(elm.getDateFin())) {
                        df = new JADate(elm.getDateFin());
                    }

                    if ((cal.compare(minDate, dd) == JACalendar.COMPARE_FIRSTUPPER)
                            && !ISFSituationFamiliale.CS_REL_CONJ_ENFANT_COMMUN.equals(elm.getTypeRelation())
                            && !ISFSituationFamiliale.CS_REL_CONJ_RELATION_INDEFINIE.equals(elm.getTypeRelation())) {

                        minDate = new JADate(elm.getDateDebut());
                    }

                    if (df == null) {
                        maxDate = null;
                    } else {
                        if (cal.compare(maxDate, df) == JACalendar.COMPARE_FIRSTLOWER) {
                            maxDate = new JADate(elm.getDateFin());
                        }
                    }

                    conjVO.setIdMembreFamille1(elm.getIdConjoint1());
                    conjVO.setIdMembreFamille2(elm.getIdConjoint2());
                    conjVO.setIdTiers1(elm.getIdTiers1());
                    conjVO.setIdTiers2(elm.getIdTiers2());
                    conjVO.setCsSexe1(elm.getSexe1());
                    conjVO.setCsSexe2(elm.getSexe2());
                    conjVO.setNss1(elm.getNoAvs1());
                    conjVO.setNss2(elm.getNoAvs2());
                    conjVO.setDateDeces1(elm.getDateDeces1());
                    conjVO.setDateDeces2(elm.getDateDeces2());
                    conjVO.setIdRelationConjoint(elm.getIdRelationConjoint());

                    vb.addNssMembreFamille(elm.getNoAvs1());
                    vb.addNssMembreFamille(elm.getNoAvs2());

                    // On inverse les id, car retourne les info pour l'autre
                    // conjoint !!!
                    // Merci MMU !??&//(%&*/*&ç*
                    conjVO.setDescriptionConjoint1(elm.getDetailMembreFamilleVG(elm.getIdConjoint2()));
                    conjVO.setDescriptionConjoint2(elm.getDetailMembreFamilleVG(elm.getIdConjoint1()));

                    if (mapConjointsDesConjointsDuLiant.containsKey(conjVO.getKey())) {
                        conjVO = (SFConjointVO) mapConjointsDesConjointsDuLiant.get(conjVO.getKey());
                    }
                    // On ajoute les enfants, 1 seule fois !!!
                    else {
                        for (Iterator iter = elm.getEnfants(transaction); iter.hasNext();) {
                            SFApercuEnfant enfant = (SFApercuEnfant) iter.next();
                            SFMembreVO mvo = new SFMembreVO();

                            mvo.setCsDomaine(enfant.getCsDomaineApplication());
                            mvo.setCsNationalite(enfant.getCsNationalite());
                            mvo.setCsSexe(enfant.getCsSexe());
                            mvo.setDateDeces(enfant.getDateDeces());
                            mvo.setDateNaissance(enfant.getDateNaissance());
                            mvo.setIdMembreFamille(enfant.getIdMembreFamille());
                            mvo.setIdTiers(enfant.getIdTiers());
                            mvo.setNom(enfant.getNom());
                            mvo.setNssFormatte(enfant.getNss());
                            mvo.setPrenom(enfant.getPrenom());
                            conjVO.addEnfant(mvo);
                            vb.addNssMembreFamille(enfant.getNss());
                        }

                        conjVO.initPeriodes(session, (BTransaction) transaction);
                    }

                    SFRelationVO relVO = new SFRelationVO();
                    relVO.setDateDebut(elm.getDateDebut());
                    relVO.setDateFin(elm.getDateFin());
                    relVO.setCsTypeRelation(elm.getTypeRelation());
                    relVO.setIdRelation(elm.getIdRelationConjoint());
                    conjVO.addRelation(relVO);

                    // On écrase, le cas échéant.
                    mapConjointsDesConjointsDuLiant.put(conjVO.getKey(), conjVO);
                }

                Set innerKeys = mapConjointsDesConjointsDuLiant.keySet();
                for (Iterator iterator3 = innerKeys.iterator(); iterator3.hasNext();) {
                    String innerKey = (String) iterator3.next();

                    cvo = (SFConjointVO) mapConjointsDesConjointsDuLiant.get(innerKey);
                    vb.addConjointDesConjointsDuLiant(cvo);
                }

            }

            vb.setNombreRelation(nombreRelationMaxParConjoint);
            vb.setMaxDateFinRelations(maxDate);
            vb.setMinDateDebutRelations(minDate);

            vb.initTimeLines();
            transaction.commit();

        } catch (Exception e) {
            JadeLogger.error(this, e);
            transaction.rollback();
        } finally {
            if (transaction != null) {
                transaction.closeTransaction();
            }
        }
        return vb;
    }

    public FWViewBeanInterface afficherPeriodesVGMembreFamille(FWViewBeanInterface viewBean, FWAction action,
            BSession session) throws Exception {

        return afficherFamilleRequerant(viewBean, action, session);

    }

    public FWViewBeanInterface afficherPeriodesVGRequerant(FWViewBeanInterface viewBean, FWAction action,
            BSession session) throws Exception {

        return afficherFamilleRequerant(viewBean, action, session);

    }

    @Override
    public void beforeExecute(FWViewBeanInterface viewBean, FWAction action, globaz.globall.api.BISession session)
            throws Exception {
        if (session == null) {
            throw new Exception("invalid session");
        }
        if (action == null) {
            throw new Exception("invalid action");
        }
        viewBean.setISession(session);

        if (!FWViewBeanInterface.WARNING.equals(viewBean.getMsgType())) {
            viewBean.setMessage("");
            viewBean.setMsgType(FWViewBeanInterface.OK);
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

    public FWViewBeanInterface initialiserProvenance(FWViewBeanInterface viewBean, FWAction action, BSession session)
            throws Exception {

        SFVueGlobaleViewBean vb = (SFVueGlobaleViewBean) viewBean;
        // Doit toujours être le cas !!!!
        if (vb.getLiant() != null) {
            String idRequerant = vb.getLiant().getIdRequerant();
            SFRequerant req = new SFRequerant();
            req.setSession(session);
            req.setIdRequerant(idRequerant);
            req.retrieve();
            if (!req.isNew()) {
                req.setProvenance("");
                req.update();
                vb.getLiant().setProvenance("");
            }
        }
        return afficherFamilleRequerant(viewBean, action, session);
    }

}
