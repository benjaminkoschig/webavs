/*
 * Créé le 2 mars 06
 */
package globaz.aquila.helpers.plaintes;

import globaz.aquila.api.ICOApplication;
import globaz.aquila.application.COApplication;
import globaz.aquila.db.plaintes.COPlaintePenaleViewBean;
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
public class COPlaintePenaleHelper extends FWHelper {

    /**
     * @see globaz.framework.controller.FWHelper#_init(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _init(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        super._init(viewBean, action, session);
        String msgError = "";
        COPlaintePenaleViewBean plaintePenaleViewBean = (COPlaintePenaleViewBean) viewBean;
        try {

            BSession sessionOsiris = (BSession) GlobazSystem.getApplication(CAApplication.DEFAULT_APPLICATION_OSIRIS)
                    .newSession();
            session.connectSession(sessionOsiris);

            CACompteAnnexe compteAnnexeAdministrateur = new CACompteAnnexe();
            compteAnnexeAdministrateur.setSession(sessionOsiris);
            compteAnnexeAdministrateur.setIdCompteAnnexe(plaintePenaleViewBean.getIdCompteAuxiliaire());
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

            plaintePenaleViewBean.setNumeroAdministrateur(compteAnnexeAdministrateur.getIdExterneRole());
            plaintePenaleViewBean.setNomPrenom(compteAnnexeAdministrateur.getTiers().getNom());
            plaintePenaleViewBean.setNumeroEmployeur(idExterne);

            // recherche du nom de la société
            COApplication application = (COApplication) GlobazSystem
                    .getApplication(ICOApplication.DEFAULT_APPLICATION_AQUILA);
            CACompteAnnexeManager compteAnnexeSocieteManager = new CACompteAnnexeManager();
            compteAnnexeSocieteManager.setSession(sessionOsiris);
            if (application.numeroAdministrateurReplaceTiret()) {
                compteAnnexeSocieteManager
                        .setLikeIdExterneRole(JadeStringUtil.split(idExterne, '-', Integer.MAX_VALUE)[0]);
            } else {
                compteAnnexeSocieteManager.setLikeIdExterneRole(idExterne.substring(0, idExterne.lastIndexOf('-')));
            }
            compteAnnexeSocieteManager.find();

            if (compteAnnexeSocieteManager.isEmpty() || compteAnnexeSocieteManager.hasErrors()) {
                plaintePenaleViewBean.setMsgType(FWViewBeanInterface.ERROR);
                plaintePenaleViewBean.setMessage(msgError);
            } else if (compteAnnexeSocieteManager.size() > 1) {
                for (int i = 0; i < compteAnnexeSocieteManager.size(); i++) {
                    if (((CACompteAnnexe) compteAnnexeSocieteManager.getEntity(i)).getIdRole().equals(
                            IntRole.ROLE_AFFILIE_PARITAIRE)
                            || ((CACompteAnnexe) compteAnnexeSocieteManager.getEntity(i)).getIdRole().equals(
                                    IntRole.ROLE_AFFILIE)) {
                        plaintePenaleViewBean.setSociete(((CACompteAnnexe) compteAnnexeSocieteManager.getEntity(i))
                                .getTiers().getNom());
                        break;
                    }
                }
            } else {
                plaintePenaleViewBean.setSociete(((CACompteAnnexe) compteAnnexeSocieteManager.getEntity(0)).getTiers()
                        .getNom());
            }
        } catch (Exception e) {
            plaintePenaleViewBean.setMsgType(FWViewBeanInterface.ERROR);
            plaintePenaleViewBean.setMessage(msgError);
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
