package globaz.phenix.helpers.communications;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.phenix.db.communications.CPJournalRetourViewBean;
import globaz.phenix.process.communications.CPProcessAbandonner;
import globaz.phenix.process.communications.CPProcessAbandonnerEnMasse;
import globaz.phenix.process.communications.CPProcessAbandonnerEnMasseViewBean;
import globaz.phenix.process.communications.CPProcessCommunicationRetourImprimer;
import globaz.phenix.process.communications.CPProcessCommunicationRetourReinitialiser;
import globaz.phenix.process.communications.CPProcessCommunicationRetourSupprimer;
import globaz.phenix.process.communications.CPProcessCommunicationRetourner;
import globaz.phenix.process.communications.CPProcessReceptionCommunication;
import globaz.phenix.process.communications.CPProcessReceptionFermerJournal;
import globaz.phenix.process.communications.CPProcessReceptionGenererDecision;

public class CPJournalRetourHelper extends FWHelper {

    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        if ("fermer".equals(action.getActionPart()) && (viewBean instanceof CPJournalRetourViewBean)) {
            CPJournalRetourViewBean vb = (CPJournalRetourViewBean) viewBean;

            CPProcessReceptionFermerJournal process = new CPProcessReceptionFermerJournal();
            process.setEMailAddress(vb.getEMailAddress());
            process.setIdJournalRetour(vb.getIdJournalRetour());
            process.setSession(vb.getSession());
            // process.setTransaction(vb.getSession().getCurrentThreadTransaction());
            process.start();

            if (process.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                viewBean.setMsgType(process.getMsgType());
                viewBean.setMessage(process.getMessage());
            }
        } else if ("executerGenerer".equals(action.getActionPart()) && (viewBean instanceof CPJournalRetourViewBean)) {
            CPJournalRetourViewBean vb = (CPJournalRetourViewBean) viewBean;

            CPProcessReceptionGenererDecision process = new CPProcessReceptionGenererDecision();
            process.setEMailAddress(vb.getEMailAddress());
            String listIdJournalRetour[] = { vb.getIdJournalRetour() };
            process.setListIdJournalRetour(listIdJournalRetour);
            process.setIdPassage(vb.getIdJournalFacturation());
            process.setFromNumAffilie(vb.getFromNumAffilie());
            process.setTillNumAffilie(vb.getTillNumAffilie());
            process.setForStatus(vb.getStatus());
            process.setForIdPlausibilite(vb.getForIdPlausibilite());
            process.setSession(vb.getSession());
            process.start();

            if (process.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                viewBean.setMsgType(process.getMsgType());
                viewBean.setMessage(process.getMessage());
            }
        }

        else if ("executerGenererEnMasse".equals(action.getActionPart())) {
            CPJournalRetourViewBean vb = (CPJournalRetourViewBean) viewBean;

            if (vb.getListIdJournalRetour() == null) {
                viewBean.setMsgType(FWViewBeanInterface.WARNING);
                viewBean.setMessage(vb.getSession().getLabel("GENERER_JOURNAL_AUCUN"));
            } else {
                CPProcessReceptionGenererDecision process = new CPProcessReceptionGenererDecision();
                process.setEMailAddress(vb.getEMailAddress());
                process.setListIdJournalRetour(vb.getListIdJournalRetour());
                process.setIdPassage(vb.getIdJournalFacturation());
                process.setFromNumAffilie(vb.getFromNumAffilie());
                process.setTillNumAffilie(vb.getTillNumAffilie());
                process.setForStatus(vb.getStatus());
                process.setForIdPlausibilite(vb.getForIdPlausibilite());
                process.setSession(vb.getSession());
                process.start();
            }
        }

        else if ("executerReinitialiserEnMasse".equals(action.getActionPart())) {
            CPJournalRetourViewBean vb = (CPJournalRetourViewBean) viewBean;

            if (vb.getListIdJournalRetour() == null) {
                viewBean.setMsgType(FWViewBeanInterface.WARNING);
                viewBean.setMessage(vb.getSession().getLabel("REINIT_JOURNAL_AUCUN"));
            } else {
                try {
                    CPProcessCommunicationRetourReinitialiser process = new CPProcessCommunicationRetourReinitialiser();
                    process.setListIdJournalRetour(vb.getListIdJournalRetour());
                    process.setEMailAddress(vb.getEMailAddress());
                    process.setFromNumAffilie(vb.getFromNumAffilie());
                    process.setTillNumAffilie(vb.getTillNumAffilie());
                    process.setForStatus(vb.getStatus());
                    process.setForIdPlausibilite(vb.getForIdPlausibilite());
                    process.setSession(vb.getSession());
                    process.start();

                } catch (Exception e) {
                    viewBean.setMsgType(FWViewBeanInterface.ERROR);
                    viewBean.setMessage(e.getMessage());
                }
            }
        }

        else if ("generer".equals(action.getActionPart()) && (viewBean instanceof CPJournalRetourViewBean)) {
            CPJournalRetourViewBean vb = (CPJournalRetourViewBean) viewBean;

            try {
                vb.retrieve();

                if ("OK".equals(vb.getValidationDecision())) {
                    vb.rechercheProchainJournalFacturation();
                }

                viewBean = vb;
            } catch (Exception e) {
                viewBean.setMessage(e.toString());
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            }
        }

        else if ("executerSupprimer".equals(action.getActionPart()) && (viewBean instanceof CPJournalRetourViewBean)) {
            CPJournalRetourViewBean vb = (CPJournalRetourViewBean) viewBean;

            CPProcessCommunicationRetourSupprimer process = new CPProcessCommunicationRetourSupprimer();
            process.setEMailAddress(vb.getEMailAddress());
            process.setIdJournalRetour(vb.getIdJournalRetour());
            process.setFromNumAffilie(vb.getFromNumAffilie());
            process.setTillNumAffilie(vb.getTillNumAffilie());
            process.setForStatus(vb.getStatus());
            process.setSession(vb.getSession());
            process.setForIdPlausibilite(vb.getForIdPlausibilite());
            process.start();

            if (process.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                viewBean.setMsgType(process.getMsgType());
                viewBean.setMessage(process.getMessage());
            }
        }

        else if ("abandonner".equals(action.getActionPart()) && (viewBean instanceof CPJournalRetourViewBean)) {
            try {
                ((CPJournalRetourViewBean) viewBean).retrieve();
            } catch (Exception e) {
                viewBean.setMessage(e.toString());
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            }
        }

        else if ("supprimerCommunication".equals(action.getActionPart())
                && (viewBean instanceof CPJournalRetourViewBean)) {
            try {
                ((CPJournalRetourViewBean) viewBean).retrieve();
            } catch (Exception e) {
                viewBean.setMessage(e.toString());
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            }
        }

        else if ("retourner".equals(action.getActionPart()) && (viewBean instanceof CPJournalRetourViewBean)) {
            try {
                ((CPJournalRetourViewBean) viewBean).retrieve();
            } catch (Exception e) {
                viewBean.setMessage(e.toString());
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            }
        }

        else if ("receptionner".equals(action.getActionPart()) && (viewBean instanceof CPJournalRetourViewBean)) {
            try {
                ((CPJournalRetourViewBean) viewBean).retrieve();
            } catch (Exception e) {
                viewBean.setMessage(e.toString());
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            }
        }

        else if ("imprimer".equals(action.getActionPart()) && (viewBean instanceof CPJournalRetourViewBean)) {
            try {
                ((CPJournalRetourViewBean) viewBean).retrieve();
            } catch (Exception e) {
                viewBean.setMessage(e.toString());
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            }
        }

        else if ("reinitialiser".equals(action.getActionPart()) && (viewBean instanceof CPJournalRetourViewBean)) {
            try {
                ((CPJournalRetourViewBean) viewBean).retrieve();
            } catch (Exception e) {
                viewBean.setMessage(e.toString());
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            }
        }

        else if ("executerReinitialiser".equals(action.getActionPart())
                && (viewBean instanceof CPJournalRetourViewBean)) {
            CPJournalRetourViewBean vb = (CPJournalRetourViewBean) viewBean;

            CPProcessCommunicationRetourReinitialiser process = new CPProcessCommunicationRetourReinitialiser();
            process.setListIdJournalRetour(vb.getListIdJournalRetour());
            process.setEMailAddress(vb.getEMailAddress());
            process.setFromNumAffilie(vb.getFromNumAffilie());
            process.setTillNumAffilie(vb.getTillNumAffilie());
            process.setForStatus(vb.getStatus());
            process.setForIdPlausibilite(vb.getForIdPlausibilite());
            process.setSession(vb.getSession());
            process.start();

            if (process.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                viewBean.setMsgType(process.getMsgType());
                viewBean.setMessage(process.getMessage());
            }
        }

        else if ("executerAbandonner".equals(action.getActionPart()) && (viewBean instanceof CPJournalRetourViewBean)) {
            CPJournalRetourViewBean vb = (CPJournalRetourViewBean) viewBean;

            CPProcessAbandonner process = new CPProcessAbandonner();
            process.setIdJournalRetour(vb.getIdJournalRetour());
            process.setEMailAddress(vb.getEMailAddress());
            process.setFromNumAffilie(vb.getFromNumAffilie());
            process.setTillNumAffilie(vb.getTillNumAffilie());
            process.setForStatus(vb.getStatus());
            process.setForIdPlausibilite(vb.getForIdPlausibilite());
            process.setSession(vb.getSession());
            process.start();

            if (process.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                viewBean.setMsgType(process.getMsgType());
                viewBean.setMessage(process.getMessage());
            }
        }

        else if ("executerAbandonnerEnMasse".equals(action.getActionPart())
                && (viewBean instanceof CPProcessAbandonnerEnMasseViewBean)) {
            CPProcessAbandonnerEnMasseViewBean vb = (CPProcessAbandonnerEnMasseViewBean) viewBean;

            // Execution du process
            CPProcessAbandonnerEnMasse process = new CPProcessAbandonnerEnMasse();
            process.setListIdJournalRetour(vb.getListIdJournalRetour());
            process.setEMailAddress(vb.getEMailAddress());
            process.setFromNumAffilie(vb.getFromNumAffilie());
            process.setTillNumAffilie(vb.getTillNumAffilie());
            process.setForStatus(vb.getForStatus());
            process.setForIdPlausibilite(vb.getForIdPlausibilite());
            process.setSession(vb.getSession());
            process.start();

            if (process.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                viewBean.setMsgType(process.getMsgType());
                viewBean.setMessage(process.getMessage());
            }
        }

        else if ("executerRetourner".equals(action.getActionPart()) && (viewBean instanceof CPJournalRetourViewBean)) {
            CPJournalRetourViewBean vb = (CPJournalRetourViewBean) viewBean;

            CPProcessCommunicationRetourner process = new CPProcessCommunicationRetourner();
            process.setEMailAddress(vb.getEMailAddress());
            process.setIdJournalRetour(vb.getIdJournalRetour());
            process.setFromNumAffilie(vb.getFromNumAffilie());
            process.setTillNumAffilie(vb.getTillNumAffilie());
            process.setForStatus(vb.getStatus());
            process.setForIdPlausibilite(vb.getForIdPlausibilite());
            process.setDateFichier(vb.getDateFichier());
            if ("on".equals(vb.getSimulation())) {
                process.setSimulation(Boolean.TRUE);
            } else {
                process.setSimulation(Boolean.FALSE);
            }
            if ("on".equals(vb.getListeExcel())) {
                process.setListeExcel(Boolean.TRUE);
            } else {
                process.setListeExcel(Boolean.FALSE);
            }
            process.setSession(vb.getSession());
            process.start();
            if (process.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                viewBean.setMsgType(process.getMsgType());
                viewBean.setMessage(process.getMessage());
            }
        }

        else if ("executerReceptionner".equals(action.getActionPart()) && (viewBean instanceof CPJournalRetourViewBean)) {
            CPJournalRetourViewBean vb = (CPJournalRetourViewBean) viewBean;

            CPProcessReceptionCommunication process = new CPProcessReceptionCommunication();
            process.setSession(vb.getSession());
            process.setEMailAddress(vb.getEMailAddress());
            process.setTypeReception(vb.getTypeReception());
            // Type de Reception = receptionner encore
            if (CPProcessReceptionCommunication.RECEPTIONNER_ENCORE.equals(vb.getTypeReception())) {
                process.setIdJournalRetour(vb.getIdJournalRetour());
            } else {
                process.setInputFileName(vb.getReceptionFileName());
                process.setCsCanton(vb.getCanton());
                if (CPProcessReceptionCommunication.RECEPTION_TOTAL == vb.getTypeReception()) {
                    process.setIdJournalRetour(vb.getIdJournalRetour());
                } else { // type de reception = receptionner créer journal
                    process.setLibelleJournal(vb.getLibelleJournal());
                }
            }
            process.start();
            if (process.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                viewBean.setMsgType(process.getMsgType());
                viewBean.setMessage(process.getMessage());
            }
        }

        else if ("executerImprimer".equals(action.getActionPart()) && (viewBean instanceof CPJournalRetourViewBean)) {
            CPJournalRetourViewBean vb = (CPJournalRetourViewBean) viewBean;

            CPProcessCommunicationRetourImprimer process = new CPProcessCommunicationRetourImprimer();
            process.setEMailAddress(vb.getEMailAddress());
            process.setIdJournalRetour(vb.getIdJournalRetour());
            process.setFromNumAffilie(vb.getFromNumAffilie());
            process.setForGenreAffilie(vb.getForGenreAffilie());
            process.setTillNumAffilie(vb.getTillNumAffilie());
            process.setForStatus(vb.getStatus());
            process.setForIdPlausibilite(vb.getForIdPlausibilite());
            process.setSession(vb.getSession());
            process.setOrderBy(vb.getOrderBy());
            process.setImpression(vb.getImpression());
            process.setWantDetail(vb.getWantDetail());
            process.start();

            if (process.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                viewBean.setMsgType(process.getMsgType());
                viewBean.setMessage(process.getMessage());
            }
        }
        return super.execute(viewBean, action, session);
    }
}
