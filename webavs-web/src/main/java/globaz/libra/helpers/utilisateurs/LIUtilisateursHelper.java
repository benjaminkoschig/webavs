package globaz.libra.helpers.utilisateurs;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.client.util.JadeStringUtil;
import globaz.libra.db.utilisateurs.LIUtilisateurs;
import globaz.libra.db.utilisateurs.LIUtilisateursManager;
import globaz.libra.utils.LIJadeUserService;
import globaz.libra.vb.utilisateurs.LIUtilisateursViewBean;
import java.util.Iterator;

public class LIUtilisateursHelper extends FWHelper {

    @Override
    protected void _add(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        BITransaction transaction = null;
        LIUtilisateursViewBean vb = (LIUtilisateursViewBean) viewBean;

        try {

            // Si saisie comme utilisateur par défaut, vérifier qu'il n'existe
            // pas comme
            // utilisateur par défaut dans un autre groupe du domaine
            if (vb.getIsDefault().booleanValue()) {

                LIUtilisateursManager usrMgr = new LIUtilisateursManager();
                usrMgr.setSession((BSession) session);
                usrMgr.setForIdUserExterne(vb.getIdUserFX());
                usrMgr.setForIsDefault(Boolean.TRUE);
                usrMgr.find();

                if (!usrMgr.isEmpty()) {
                    throw new Exception("Cet utilisateur est déjà défini par défaut dans un groupe de ce domaine");
                }
            }

            // Vérifier si l'utilisateur existe dans fx
            JadeUser userFX = LIJadeUserService.getInstance().loadByUserId(vb.getIdUtilisateurExterne());

            if (JadeStringUtil.isBlankOrZero(userFX.getIdUser())) {
                throw new Exception("Cet utilisateur n'existe pas dans la gestion des droits");
            }

            vb.add(transaction);

        } catch (Exception e) {
            if (transaction != null) {
                transaction.setRollbackOnly();
            }
            vb.setMessage("Error " + e.getMessage());
            vb.setMsgType(FWViewBeanInterface.ERROR);
        } finally {
            if (transaction != null) {
                try {
                    if (transaction.hasErrors() || transaction.isRollbackOnly()) {
                        if (transaction.hasErrors()) {
                            vb.setMessage(transaction.getErrors().toString());
                            vb.setMsgType(FWViewBeanInterface.ERROR);
                        }
                        transaction.rollback();
                    } else {
                        transaction.commit();
                    }
                } catch (Exception e) {
                    throw e;
                } finally {
                    transaction.closeTransaction();
                }
            }
        }
    }

    @Override
    protected void _update(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        BITransaction transaction = null;
        LIUtilisateursViewBean vb = (LIUtilisateursViewBean) viewBean;

        try {

            // Si saisie comme utilisateur par défaut, vérifier qu'il n'existe
            // pas comme
            // utilisateur par défaut dans un autre groupe du domaine
            if (vb.getIsDefault().booleanValue()) {

                LIUtilisateursManager usrMgr = new LIUtilisateursManager();
                usrMgr.setSession((BSession) session);
                usrMgr.setForIdUserExterne(vb.getIdUtilisateurExterne());
                usrMgr.setForIsDefault(Boolean.TRUE);
                usrMgr.find();

                if (!usrMgr.isEmpty()) {
                    for (Iterator iterator = usrMgr.iterator(); iterator.hasNext();) {
                        LIUtilisateurs user = (LIUtilisateurs) iterator.next();

                        if (!user.getIdUtilisateur().equals(vb.getIdUtilisateur())) {
                            throw new Exception(
                                    "Cet utilisateur est déjà défini par défaut dans un groupe de ce domaine");
                        }
                    }
                }
            }

            // Vérifier si l'utilisateur existe dans fx
            JadeUser userFX = LIJadeUserService.getInstance().loadByUserId(vb.getIdUtilisateurExterne());

            if (JadeStringUtil.isBlankOrZero(userFX.getIdUser())) {
                throw new Exception("Cet utilisateur n'existe pas dans la gestion des droits");
            }

            vb.update(transaction);

        } catch (Exception e) {
            if (transaction != null) {
                transaction.setRollbackOnly();
            }
            vb.setMessage("Error " + e.getMessage());
            vb.setMsgType(FWViewBeanInterface.ERROR);
        } finally {
            if (transaction != null) {
                try {
                    if (transaction.hasErrors() || transaction.isRollbackOnly()) {
                        if (transaction.hasErrors()) {
                            vb.setMessage(transaction.getErrors().toString());
                            vb.setMsgType(FWViewBeanInterface.ERROR);
                        }
                        transaction.rollback();
                    } else {
                        transaction.commit();
                    }
                } catch (Exception e) {
                    throw e;
                } finally {
                    transaction.closeTransaction();
                }
            }
        }
    }
}
