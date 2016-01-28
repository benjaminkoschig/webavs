package globaz.hercule.helpers.controleEmployeur;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JATime;
import globaz.hercule.db.controleEmployeur.CEAttributionPts;
import globaz.hercule.db.controleEmployeur.CEGestionAttributionPts;
import globaz.hercule.db.controleEmployeur.CEGestionAttributionPtsViewBean;
import globaz.hercule.service.CEAttributionPtsService;
import globaz.hercule.service.CEControleEmployeurService;
import globaz.hercule.utils.CEUtils;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author SCO
 * @since 8 sept. 2010
 */
public class CEGestionAttributionPtsHelper extends FWHelper {

    /**
     * @see globaz.framework.controller.FWHelper#_add(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _add(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        CEGestionAttributionPtsViewBean vb = (CEGestionAttributionPtsViewBean) viewBean;
        CEAttributionPts attrPts = new CEAttributionPts();

        CEAttributionPtsService.fillFields(attrPts, vb);

        JATime heure = new JATime(JACalendar.now());
        attrPts.setLastModification(CEUtils.giveToday() + " / " + heure.toStr(":"));
        attrPts.setIsAttributionActive(Boolean.TRUE);
        attrPts.setLastUser(session.getUserId());
        attrPts.setSession((BSession) session);
        attrPts.setIdAttributionPts(null);

        BTransaction transaction = null;
        try {

            // Création d'une transation pour ne pas avoir des incoherences dans
            // les points
            transaction = new BTransaction((BSession) session);
            transaction.openTransaction();

            // 1. Ajout de l'attribution des points

            attrPts.add(transaction);

            // 2. Recherche si il existe une attribution des points pour ce
            // controle.
            // Si oui, on le met a jour et on le met non actif.

            CEAttributionPts oldAttrPts = CEAttributionPtsService.retrieveAttributionPtsActif((BSession) session,
                    transaction, attrPts.getIdAttributionPts(), vb.getNumAffilie(), vb.getPeriodeDebut(),
                    vb.getPeriodeFin());

            if (oldAttrPts != null) {

                oldAttrPts.setIsAttributionActive(false);
                oldAttrPts.update(transaction);
            }

            // 3. Calcul de la note de collaboration et du nombre de note
            if (!transaction.hasErrors()) {
                CEControleEmployeurService.updateNoteAndCouverture((BSession) session, transaction, vb.getIdControle(),
                        attrPts.getIdAttributionPts(), vb.getDerniereRevision(), vb.getQualiteRH(),
                        vb.getCriteresEntreprise());
            }

            if (transaction.hasErrors()) {
                vb.setMsgType(FWViewBeanInterface.ERROR);
                vb.setMessage(transaction.getErrors().toString());
                transaction.rollback();
            } else {
                transaction.commit();
            }

        } catch (Exception e) {
            transaction.rollback();
            vb.setMessage(e.toString());
            vb.setMsgType(FWViewBeanInterface.ERROR);
        } finally {
            if (transaction != null && transaction.isOpened()) {
                transaction.closeTransaction();
            }
        }

    }

    /**
     * @see globaz.framework.controller.FWHelper#_retrieve(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        CEGestionAttributionPtsViewBean vb = (CEGestionAttributionPtsViewBean) viewBean;

        try {
            CEGestionAttributionPts attrPts = CEAttributionPtsService.retrieveGestionAttributionPts((BSession) session,
                    vb.getIdAttributionPts());
            CEAttributionPtsService.fillFields(vb, attrPts);

        } catch (Exception e) {
            viewBean.setMessage(e.getMessage());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
    }

    /**
     * @see globaz.framework.controller.FWHelper#_update(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _update(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        CEGestionAttributionPtsViewBean vb = (CEGestionAttributionPtsViewBean) viewBean;
        CEAttributionPts attrPts = new CEAttributionPts();

        // On rempli l'entité attribution points avec les données du viewBean.
        CEAttributionPtsService.fillFields(attrPts, vb);

        BTransaction transaction = null;
        try {

            // Création d'une transation pour ne pas avoir des incoherences dans
            // les points
            transaction = new BTransaction((BSession) session);
            transaction.openTransaction();

            // 1. Création d'une nouvelle attribution des points
            JATime heure = new JATime(JACalendar.now());
            attrPts.setLastModification(CEUtils.giveToday() + " / " + heure.toStr(":"));
            attrPts.setIsAttributionActive(Boolean.TRUE);
            attrPts.setLastUser(session.getUserId());
            attrPts.setSession((BSession) session);
            attrPts.setIdAttributionPts(null);

            attrPts.add(transaction);

            // 2. Si cet attribut est renseigné, on doit mettre a jour
            // l'ancienne attribution des notes inactive (pour archivage)
            if (!JadeStringUtil.isEmpty(vb.getOldIdAttributionPts())) {

                CEAttributionPts attPts = new CEAttributionPts();
                attPts.setSession((BSession) session);
                attPts.setIdAttributionPts(vb.getOldIdAttributionPts());

                attPts.retrieve(transaction);

                attPts.setIsAttributionActive(Boolean.FALSE);
                attPts.update(transaction);
            }

            // 3. Calcul de la note de collaboration et du nombre de note
            if (!transaction.hasErrors()) {
                CEControleEmployeurService.updateNoteAndCouverture((BSession) session, transaction, vb.getIdControle(),
                        attrPts.getIdAttributionPts(), vb.getDerniereRevision(), vb.getQualiteRH(),
                        vb.getCriteresEntreprise());
            }

            if (transaction.hasErrors()) {
                vb.setMsgType(FWViewBeanInterface.ERROR);
                vb.setMessage(transaction.getErrors().toString());
                transaction.rollback();

            } else {
                transaction.commit();
            }

        } catch (Exception e) {
            transaction.rollback();
            vb.setMessage(e.toString());
            vb.setMsgType(FWViewBeanInterface.ERROR);

        } finally {
            if (transaction != null && transaction.isOpened()) {
                transaction.closeTransaction();
            }
        }
    }
}
