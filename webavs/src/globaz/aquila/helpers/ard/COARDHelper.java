/*
 * Créé le 2 mars 06
 */
package globaz.aquila.helpers.ard;

import globaz.aquila.api.ICOApplication;
import globaz.aquila.application.COApplication;
import globaz.aquila.db.ard.COARDViewBean;
import globaz.aquila.util.COAdministrateurUtil;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexeManager;
import globaz.osiris.external.IntRole;

/**
 * @author dvh
 */
public class COARDHelper extends FWHelper {

    /**
     * @see globaz.framework.controller.FWHelper#_init(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _init(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        super._init(viewBean, action, session);
        String msgError = "";
        COARDViewBean detailARDViewBean = (COARDViewBean) viewBean;
        try {

            BSession sessionOsiris = (BSession) GlobazSystem.getApplication(CAApplication.DEFAULT_APPLICATION_OSIRIS)
                    .newSession();
            session.connectSession(sessionOsiris);

            CACompteAnnexe compteAnnexeAdministrateur = new CACompteAnnexe();
            compteAnnexeAdministrateur.setSession(sessionOsiris);
            compteAnnexeAdministrateur.setIdCompteAnnexe(detailARDViewBean.getIdCompteAnnexe());
            compteAnnexeAdministrateur.retrieve();

            String idExterne = compteAnnexeAdministrateur.getIdExterneRole();
            // cherche le format dans les propriétés d'Osiris. Ex: XXX.XXX-XX.
            String format = CAApplication.getApplicationOsiris().getCAParametres().getFormatAdminNumAffilie();
            // Si l'idExterne correspond au format, alors on formate
            // l'idExterne(exemple pour la FER: 000.309-01 => 000.309-00).
            if (COAdministrateurUtil.isFormatNumAffilie(idExterne, format)) {
                idExterne = JadeStringUtil.remove(idExterne, idExterne.length() - 2, 2);
                idExterne += "00";
            } else {
                idExterne = JadeStringUtil.split(idExterne, '-', Integer.MAX_VALUE)[0];
            }

            // Gestion du message d'erreur.
            if (JadeStringUtil.isBlank(format) || format.equals("false")) {
                // si la propiete n'est pas présente.
                msgError = sessionOsiris.getLabel("AQUILA_ADMIN_ERREUR_PROPRIETE") + " / ";
            }
            msgError += sessionOsiris.getLabel("AQUILA_ADMIN_ERREUR_SOCIETE");

            detailARDViewBean.setNumeroAdministrateur(compteAnnexeAdministrateur.getIdExterneRole());
            detailARDViewBean.setNomPrenom(compteAnnexeAdministrateur.getTiers().getNom());
            detailARDViewBean.setNumeroEmployeur(idExterne);

            // recherche du nom de la société
            COApplication application = (COApplication) GlobazSystem
                    .getApplication(ICOApplication.DEFAULT_APPLICATION_AQUILA);
            CACompteAnnexeManager compteAnnexeManager = new CACompteAnnexeManager();
            compteAnnexeManager.setSession(sessionOsiris);
            if (application.numeroAdministrateurReplaceTiret()) {
                compteAnnexeManager.setLikeIdExterneRole(JadeStringUtil.split(idExterne, '-', Integer.MAX_VALUE)[0]);
            } else {
                compteAnnexeManager.setLikeIdExterneRole(idExterne.substring(0, idExterne.lastIndexOf('-')));
            }
            compteAnnexeManager.find();

            if (compteAnnexeManager.isEmpty() || compteAnnexeManager.hasErrors()) {
                detailARDViewBean.setMsgType(FWViewBeanInterface.ERROR);
                detailARDViewBean.setMessage(msgError);
            } else if (compteAnnexeManager.size() > 1) {
                for (int i = 0; i < compteAnnexeManager.size(); i++) {
                    if (((CACompteAnnexe) compteAnnexeManager.getEntity(i)).getIdRole().equals(
                            IntRole.ROLE_AFFILIE_PARITAIRE)
                            || ((CACompteAnnexe) compteAnnexeManager.getEntity(i)).getIdRole().equals(
                                    IntRole.ROLE_AFFILIE)) {
                        detailARDViewBean.setSociete(((CACompteAnnexe) compteAnnexeManager.getEntity(i)).getTiers()
                                .getNom());
                        break;
                    }
                }
            } else {
                detailARDViewBean.setSociete(((CACompteAnnexe) compteAnnexeManager.getEntity(0)).getTiers().getNom());
            }
        } catch (Exception e) {
            detailARDViewBean.setMsgType(FWViewBeanInterface.ERROR);
            detailARDViewBean.setMessage(msgError);
        }
    }

    /**
     * @see globaz.framework.controller.FWHelper#_retrieve(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        _init(viewBean, action, session);
        super._retrieve(viewBean, action, session);
    }

}
