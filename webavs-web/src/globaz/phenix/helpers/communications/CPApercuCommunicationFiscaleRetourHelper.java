/*
 * Créé le 2 sept. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.phenix.helpers.communications;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourViewBean;
import globaz.phenix.db.communications.CPGenererUneDecisionViewBean;
import globaz.phenix.db.communications.CPJournalRetour;
import globaz.phenix.process.communications.CPProcessAbandonner;
import globaz.phenix.process.communications.CPProcessAbandonnerEnMasse;
import globaz.phenix.process.communications.CPProcessAbandonnerEnMasseViewBean;
import globaz.phenix.process.communications.CPProcessCommunicationRetourImprimer;
import globaz.phenix.process.communications.CPProcessCommunicationRetourReinitialiser;
import globaz.phenix.process.communications.CPProcessEnqueterEnMasse;
import globaz.phenix.process.communications.CPProcessEnqueterEnMasseViewBean;
import globaz.phenix.process.communications.CPProcessReceptionGenererDecision;

/**
 * @author mmu Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CPApercuCommunicationFiscaleRetourHelper extends FWHelper {

    /*
     * (non-Javadoc)
     * 
     * @seeglobaz.framework.controller.FWHelper#_add(globaz.framework.bean. FWViewBeanInterface,
     * globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _add(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        CPCommunicationFiscaleRetourViewBean vBean = (CPCommunicationFiscaleRetourViewBean) viewBean;
        // Met le status à réceptionné
        vBean.setStatus(CPCommunicationFiscaleRetourViewBean.CS_RECEPTIONNE);

        super._add(viewBean, action, session);
        CPJournalRetour journal = vBean.getJournalRetour();
        if (!journal.isNew()) {
            journal.incNbCommunication();
            journal.update();
        }
    }

    // protected void _retrieve(FWViewBeanInterface viewBean, FWAction action,
    // globaz.globall.api.BISession session) throws Exception {
    // if(viewBean instanceof CPCommunicationFiscaleRetourViewBean){
    // CPCommunicationFiscaleRetourViewBean vb =
    // (CPCommunicationFiscaleRetourViewBean) viewBean;
    // vb.retrieve();
    //
    // String canton = vb.getCantonJournal();
    //
    // ICommunicationRetour localViewBean = null;
    //
    // try{
    // localViewBean = (ICommunicationRetour) session.getAttribute("viewBean");
    // } catch(Exception e){
    // localViewBean = null;
    // }
    // if (canton.equalsIgnoreCase(IConstantes.CS_LOCALITE_CANTON_JURA)) {
    // localViewBean = new CPCommunicationFiscaleRetourJUViewBean();
    // } else if (canton.equalsIgnoreCase(IConstantes.CS_LOCALITE_CANTON_NEUCHATEL)) {
    // localViewBean = new CPCommunicationFiscaleRetourNEViewBean();
    // } else if (canton.equalsIgnoreCase(IConstantes.CS_LOCALITE_CANTON_GENEVE)) {
    // localViewBean = new CPCommunicationFiscaleRetourGEViewBean();
    // } else if(canton.equalsIgnoreCase(IConstantes.CS_LOCALITE_CANTON_VAUD)){
    // localViewBean = new CPCommunicationFiscaleRetourVDViewBean();
    // } else if (canton.equalsIgnoreCase(IConstantes.CS_LOCALITE_CANTON_VALAIS)){
    // localViewBean = new CPCommunicationFiscaleRetourVSViewBean();
    // }
    // localViewBean.setWantAfterRetrieve(true);
    // localViewBean.setSession(vb.getSession());
    // localViewBean.setIdRetour(vb.getIdRetour());
    // localViewBean.retrieve();
    // //viewBean = (FWViewBeanInterface) localViewBean;
    // }
    //
    // }

    /*
     * (non-Javadoc)
     * 
     * @seeglobaz.framework.controller.FWHelper#_delete(globaz.framework.bean. FWViewBeanInterface,
     * globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _delete(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        super._delete(viewBean, action, session);
        CPCommunicationFiscaleRetourViewBean vBean = (CPCommunicationFiscaleRetourViewBean) viewBean;
        CPJournalRetour journal = vBean.getJournalRetour();
        if (!journal.isNew()) {
            journal.decNbCommunication();
            journal.update();
        }
    }

    // Met la communication au status: invalidé (NON-VALIDE)
    protected void _invalider(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        CPCommunicationFiscaleRetourViewBean communication = (CPCommunicationFiscaleRetourViewBean) viewBean;
        communication.setStatus(CPCommunicationFiscaleRetourViewBean.CS_ABANDONNE);
        communication.update();
    }

    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        try {
            String actionPart = action.getActionPart();
            if (actionPart.equals("accepter")) {
                // _accepter(viewBean,action,session);
            } else if (actionPart.equals("invalider")) {
                _invalider(viewBean, action, session);
            } else if ("abandonner".equals(actionPart) && (viewBean instanceof CPCommunicationFiscaleRetourViewBean)) {
                CPCommunicationFiscaleRetourViewBean vb = (CPCommunicationFiscaleRetourViewBean) viewBean;

                CPProcessAbandonner abandon = new CPProcessAbandonner();
                abandon.setIdRetour(vb.getIdRetour());
                abandon.setISession(vb.getSession());
                abandon.setSendMailOnError(true);
                abandon.setSendCompletionMail(false);
                abandon.executeProcess();

                String idJournalRetour = abandon.getIdJournalRetour();
                ((CPCommunicationFiscaleRetourViewBean) viewBean).setIdJournalRetour(idJournalRetour);

            } else if ("reinitialiser".equals(actionPart) && (viewBean instanceof CPCommunicationFiscaleRetourViewBean)) {
                CPCommunicationFiscaleRetourViewBean vb = (CPCommunicationFiscaleRetourViewBean) viewBean;

                CPProcessCommunicationRetourReinitialiser reinit = new CPProcessCommunicationRetourReinitialiser();
                String listIdRetour[] = { vb.getIdRetour() };
                reinit.setListIdRetour(listIdRetour);
                reinit.setISession(vb.getSession());
                reinit.setSendMailOnError(false);
                reinit.setSendCompletionMail(false);
                reinit.executeProcess();

                String idJournalRetour = (String) session.getAttribute("idJournalRetour");
                ((CPCommunicationFiscaleRetourViewBean) viewBean).setIdJournalRetour(idJournalRetour);
            } else if ("processGenerer".equals(actionPart) && (viewBean instanceof CPGenererUneDecisionViewBean)) {
                CPGenererUneDecisionViewBean vb = (CPGenererUneDecisionViewBean) viewBean;

                CPProcessReceptionGenererDecision processGeneration = new CPProcessReceptionGenererDecision();
                String listIdRetour[] = { vb.getIdRetour() };
                processGeneration.setListIdRetour(listIdRetour);
                processGeneration.setISession(vb.getSession());
                processGeneration.setEMailAddress(vb.getEMailAddress());
                processGeneration.start();

            } else if ("enqueterEnMasse".equals(actionPart)) {
                CPProcessEnqueterEnMasseViewBean vb = (CPProcessEnqueterEnMasseViewBean) viewBean;

                CPProcessEnqueterEnMasse processEnquete = new CPProcessEnqueterEnMasse();
                processEnquete.setListIdRetour(vb.getListIdRetour());
                processEnquete.setISession(vb.getSession());
                processEnquete.setSendMailOnError(false);
                processEnquete.setSendCompletionMail(false);
                processEnquete.setEnvoiMail(true);
                processEnquete.executeProcess();

            } else if ("abandonnerEnMasse".equals(actionPart)) {
                CPProcessAbandonnerEnMasseViewBean vb = (CPProcessAbandonnerEnMasseViewBean) viewBean;

                CPProcessAbandonnerEnMasse processAbandon = new CPProcessAbandonnerEnMasse();
                processAbandon.setListIdRetour(vb.getListIdRetour());
                processAbandon.setISession(vb.getSession());
                processAbandon.setEMailAddress(vb.getEMailAddress());
                processAbandon.setEnvoiMail(true);
                processAbandon.executeProcess();

            } else if ("reinitialiserEnMasse".equals(actionPart)) {
                CPProcessAbandonnerEnMasseViewBean vb = (CPProcessAbandonnerEnMasseViewBean) viewBean;

                if (vb.getListIdRetour() != null) {
                    CPProcessCommunicationRetourReinitialiser process = new CPProcessCommunicationRetourReinitialiser();
                    process.setListIdRetour(vb.getListIdRetour());
                    process.setEMailAddress(vb.getEMailAddress());
                    process.setSession(vb.getSession());
                    process.setSendMailOnError(false);
                    process.setSendCompletionMail(false);
                    process.start();
                }
            } else if ("genererEnMasse".equals(actionPart)) {
                CPProcessAbandonnerEnMasseViewBean vb = (CPProcessAbandonnerEnMasseViewBean) viewBean;

                if (vb.getListIdRetour() != null) {
                    CPProcessReceptionGenererDecision processGeneration = new CPProcessReceptionGenererDecision();
                    processGeneration.setListIdRetour(vb.getListIdRetour());
                    processGeneration.setISession(vb.getSession());
                    processGeneration.setEMailAddress(vb.getEMailAddress());
                    processGeneration.start();
                }
            } else if ("executerImprimer".equals(actionPart)) {
                CPCommunicationFiscaleRetourViewBean vb = (CPCommunicationFiscaleRetourViewBean) viewBean;

                CPProcessCommunicationRetourImprimer process = new CPProcessCommunicationRetourImprimer();
                process.setEMailAddress(vb.getEMailAddress());
                process.setIdRetour(vb.getIdRetour());
                process.setIdJournalRetour(vb.getIdJournalRetour());
                process.setSession(vb.getSession());
                process.setImpression(vb.getImpression());
                process.setWantDetail(vb.getWantDetail());
                process.setIsTraitementUnitaire(Boolean.TRUE);
                process.executeProcess();

                if (process.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                    viewBean.setMsgType(process.getMsgType());
                    viewBean.setMessage(process.getMessage());
                }
            } else if ("imprimer".equals(actionPart)) {
                try {
                    ((CPCommunicationFiscaleRetourViewBean) viewBean).retrieve();
                } catch (Exception e) {
                    viewBean.setMessage(e.toString());
                    viewBean.setMsgType(FWViewBeanInterface.ERROR);
                }
            }

        } catch (Exception e) {
            viewBean.setMessage(e.toString());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
        return viewBean;
    }
}
