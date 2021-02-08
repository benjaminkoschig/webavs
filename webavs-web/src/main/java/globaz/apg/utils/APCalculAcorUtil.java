/*
 * Créé le 25 janv. 06
 *
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.apg.utils;

import java.util.Iterator;

import globaz.apg.db.droits.*;
import globaz.apg.exceptions.APRulesException;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;

/**
 * @author scr
 *
 *         Cette classe contient les règles d'allocation du calcul APG par ACOR. Condition stipulant que le calcul doit
 *         s'effectuer avec ACOR
 *
 */
public class APCalculAcorUtil {

    // Si plus d'un employeurs, le calcul doit se faire par ACOR
    private static void apgRules_1(BSession session, APDroitLAPG droit) throws APRulesException, Exception {

        APSituationProfessionnelleManager mgr = new APSituationProfessionnelleManager();
        mgr.setForIdDroit(droit.getIdDroit());
        mgr.setSession(session);
        mgr.find(session.getCurrentThreadTransaction(), 2);
        if (mgr.getCount() > 1) {
            throw new APRulesException();
        }
    }

    // si l'employeur à une date ddébut/fin de versement ou date de fin de
    // contrat
    private static void apgRules_2(BSession session, APDroitLAPG droit) throws APRulesException, Exception {
        APSituationProfessionnelleManager mgr = new APSituationProfessionnelleManager();
        mgr.setForIdDroit(droit.getIdDroit());
        mgr.setSession(session);
        mgr.find();
        for (Iterator iter = mgr.iterator(); iter.hasNext();) {
            APSituationProfessionnelle sp = (APSituationProfessionnelle) iter.next();
            if (sp.getDateFin() != null && sp.getDateFin().trim().length() > 6) {
                throw new APRulesException();
            } else if (sp.getDateFinContrat() != null && sp.getDateFinContrat().trim().length() > 6) {
                throw new APRulesException();
            } else if (sp.getDateDebut() != null && sp.getDateDebut().trim().length() > 6) {
                throw new APRulesException();
            }
        }
    }

    // Si l'employeur verse un salaire différent durant la période de droit
    // On ne contrôle que sur le 1er employeur, car si plus d'un employeur, on
    // passe de toute façon par ACOR (apgRules_1())
    private static void apgRules_3(BSession session, APDroitLAPG droit) throws APRulesException, Exception {

        APSituationProfessionnelleManager mgr = new APSituationProfessionnelleManager();
        mgr.setForIdDroit(droit.getIdDroit());
        mgr.setSession(session);
        mgr.find(session.getCurrentThreadTransaction(), 2);
        if (mgr.getCount() >= 1) {
            APSituationProfessionnelle sitProf = (APSituationProfessionnelle) mgr.get(0);
            if (!JadeStringUtil.isIntegerEmpty(sitProf.getMontantVerse())) {
                throw new APRulesException();
            }
        }
    }

    /**
     * @param basesCalcul
     * @return true si le calcul doit s'effectuer par ACOR, false sinon
     */
    public static boolean grantCalulAcorAPG(BSession session, APDroitAPG droit) throws Exception {
        if (APGUtils.isTypeAllocationJourIsole(droit.getGenreService())) {
            return false;
        }
        try {

            // -------------------------------------------------------------------
            // -- Condition stipulant que le calcul doit s'effectuer avec ACOR
            // -------------------------------------------------------------------
            apgRules_1(session, droit);
            apgRules_2(session, droit);
            apgRules_3(session, droit);

        } catch (APRulesException e) {
            return true;
        }
        return false;
    }

    public static final boolean grantCalulAcorMaternite(BSession session, APDroitLAPG droit) throws Exception {
        try {

            // -------------------------------------------------------------------
            // -- Condition stipulant que le calcul doit s'effectuer avec ACOR
            // -------------------------------------------------------------------
            materniteRules_1(session, droit);
            materniteRules_2(session, droit);
            if(droit instanceof APDroitMaternite) {
                materniteRules_3(session, (APDroitMaternite) droit);
            }
            materniteRules_4(session, droit);
            materniteRules_5(session, droit);
        } catch (APRulesException e) {
            return true;
        }
        return false;

    }

    // Si plus d'un employeurs, le calcul doit se faire par ACOR
    private static void materniteRules_1(BSession session, APDroitLAPG droit) throws APRulesException, Exception {
        apgRules_1(session, droit);
    }

    // si date de décès, le calcul doit se faire par ACOR
    private static void materniteRules_2(BSession session, APDroitLAPG droit) throws APRulesException, Exception {

        PRTiersWrapper wrapper = PRTiersHelper.getTiersParId(session, droit.loadDemande().getIdTiers());
        String dateDeces = wrapper.getProperty(PRTiersWrapper.PROPERTY_DATE_DECES);
        if (dateDeces != null && dateDeces.trim().length() > 6) {
            throw new APRulesException();
        }
    }

    // si date de reprise d'activité, le calcul doit se faire par ACOR
    private static void materniteRules_3(BSession session, APDroitMaternite droit) throws APRulesException, Exception {
        if (droit.getDateRepriseActiv() != null && droit.getDateRepriseActiv().trim().length() > 6) {
            throw new APRulesException();
        }
    }

    // si l'employeur à une date de fin de contrat
    private static void materniteRules_4(BSession session, APDroitLAPG droit) throws APRulesException, Exception {
        apgRules_2(session, droit);
    }

    // Si l'employeur verse un salaire différent durant la période de droit
    // On ne contrôle que sur le 1er employeur, car si plus d'un employeur, on
    // passe de toute façon par ACOR (materniteRules_1())
    private static void materniteRules_5(BSession session, APDroitLAPG droit) throws APRulesException, Exception {
        apgRules_3(session, droit);
    }

}
