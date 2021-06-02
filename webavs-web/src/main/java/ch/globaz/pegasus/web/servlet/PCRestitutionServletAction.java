package ch.globaz.pegasus.web.servlet;

import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.domaine.Personne;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.http.JSPUtils;
import globaz.jade.client.util.JadeDateUtil;
import globaz.pegasus.process.comptabilisation.PCRestiPCLegalComptaProcess;
import globaz.pegasus.vb.droit.PCCorrigerDroitViewBean;
import globaz.pegasus.vb.droit.PCDroitViewBean;
import globaz.pegasus.vb.restitution.PCRestitutionViewBean;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;

public class PCRestitutionServletAction extends PCAbstractServletAction {

    private static final Logger LOG = Logger.getLogger(PCRestitutionServletAction.class);

    public PCRestitutionServletAction(FWServlet aServlet) {
        super(aServlet);
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.framework.controller.FWDefaultServletAction#beforeAfficher(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
                                                 HttpServletResponse response, FWViewBeanInterface viewBean) {


        if ((viewBean instanceof PCRestitutionViewBean)) {
            PCRestitutionViewBean vb = (PCRestitutionViewBean) viewBean;

            String idDossier = request.getParameter("idDossier");
            vb.setIdDossier(idDossier);
        }

        return viewBean;

    }

    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
                                           HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof PCRestitutionViewBean) {
            PCRestitutionViewBean vb = (PCRestitutionViewBean) viewBean;
            return getActionFullURL() + ".afficher&idDossier=" + vb.getIdDossier();
        } else {
            return super._getDestAjouterSucces(session, request, response, viewBean);
        }
    }

    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
                                            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof PCRestitutionViewBean) {
            PCRestitutionViewBean vb = (PCRestitutionViewBean) viewBean;
            return getActionFullURL() + ".afficher&idDossier=" + vb.getIdDossier();
        } else {
            return super._getDestAjouterSucces(session, request, response, viewBean);
        }
    }


    @Override
    protected void actionExecuter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
                                  FWDispatcher mainDispatcher) throws ServletException, IOException {
        FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");
        String destination = "";
        if (viewBean instanceof PCRestitutionViewBean) {
            PCRestitutionViewBean vb = (PCRestitutionViewBean) viewBean;
            try {
                JSPUtils.setBeanProperties(request, vb);
                PersonneEtendueComplexModel personne = vb.getPersonne();
                BSession sessionPC = BSessionUtil.getSessionFromThreadContext();
                String dateComptable = JadeDateUtil.getGlobazFormattedDate(new Date());
                PCRestiPCLegalComptaProcess process = new PCRestiPCLegalComptaProcess();
                process.setSession(sessionPC);
                process.setNss(personne.getPersonneEtendue().getNumAvsActuel());
                process.setIdTiers(personne.getTiers().getIdTiers());
                process.setSimpleRestitution(vb.getSimpleRestitution());
                process.setDateComptable(dateComptable);
                process.setEMailAddress(vb.getMailGest());
                process.setSendCompletionMail(false);
                process.setSendMailOnError(true);
                process.executeProcess();
                if(!process.getMemoryLog().hasErrors()){
                    destination = _getDestExecuterSucces(session, request, response, vb);
                }else{
                    vb.setMsgType(FWViewBeanInterface.ERROR);
                    vb.setMessage(process.getMemoryLog().getMessagesInString());

                    destination = _getDestExecuterEchec(session, request, response, vb);
                }
            } catch (Exception e) {
                throw new ServletException(e);
            }
        }
        goSendRedirect(destination, request, response);
    }


}
