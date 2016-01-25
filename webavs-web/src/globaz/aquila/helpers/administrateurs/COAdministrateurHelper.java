/*
 * Créé le 22 févr. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.aquila.helpers.administrateurs;

import globaz.aquila.api.ICOApplication;
import globaz.aquila.application.COApplication;
import globaz.aquila.db.administrateurs.COAdministrateurViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexeManager;
import globaz.osiris.external.IntRole;
import globaz.osiris.translation.CACodeSystem;

/**
 * @author dvh
 */
public class COAdministrateurHelper extends FWHelper {

    /**
	 *
	 */
    public COAdministrateurHelper() {
        super();
    }

    /**
     * @see globaz.framework.controller.FWHelper#_add(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _add(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        COAdministrateurViewBean administrateurViewBean = (COAdministrateurViewBean) viewBean;
        // il faut trouver un idExterneRole qui convient
        COApplication application = (COApplication) GlobazSystem
                .getApplication(ICOApplication.DEFAULT_APPLICATION_AQUILA);
        String adr = "";
        if (application.numeroAdministrateurReplaceTiret()) {
            adr = administrateurViewBean.getForIdExterneLike().split("-")[0];
        } else {
            adr = administrateurViewBean.getForIdExterneLike();
        }

        CACompteAnnexeManager compteAnnexeManager = new CACompteAnnexeManager();
        compteAnnexeManager.setSession(administrateurViewBean.getSession());
        compteAnnexeManager.setLikeIdExterneRole(adr);
        compteAnnexeManager.setForIdGenreCompte(CACodeSystem.COMPTE_AUXILIAIRE);
        compteAnnexeManager.find(BManager.SIZE_NOLIMIT);

        // recherche d'un numero de compte auxiliaire qu'on peut donner
        int compteur = 0;
        for (int i = 0; i < compteAnnexeManager.size(); i++) {
            CACompteAnnexe compteAnnexe = (CACompteAnnexe) compteAnnexeManager.getEntity(i);
            String[] idexterneRole = JadeStringUtil.split(compteAnnexe.getIdExterneRole(), '-', Integer.MAX_VALUE);
            if (idexterneRole.length == 2) {
                compteur = Integer.parseInt(idexterneRole[1]);
                if (compteur != i + 1) {
                    compteur = i;
                    break;
                }
                // bug 7406
            } else if (idexterneRole.length == 3) {
                compteur = Integer.parseInt(idexterneRole[2]);
                if (compteur != i + 1) {
                    compteur = i;
                    break;
                }
            }
        }
        String numero = Integer.toString(compteur + 1);
        if (numero.length() == 1) {
            numero = "0" + numero;
        }

        String idExterneRole = adr + "-" + numero;

        administrateurViewBean.setIdExterneRole(idExterneRole);
        administrateurViewBean.setIdRole(IntRole.ROLE_ADMINISTRATEUR);
        administrateurViewBean.setIdGenreCompte(CACodeSystem.COMPTE_AUXILIAIRE);

        BSession caSession = (BSession) GlobazSystem.getApplication(CAApplication.DEFAULT_APPLICATION_OSIRIS)
                .newSession();
        session.connectSession(caSession);

        administrateurViewBean.setSession(caSession);

        super._add(viewBean, action, session);
        administrateurViewBean.setISession(session);
    }

    /**
     * @see globaz.framework.controller.FWHelper#_update(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _update(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        // on efface le compteAnnexe pour en recréer un nouveau avec le même
        // numéro.
        COAdministrateurViewBean administrateurViewBean = (COAdministrateurViewBean) viewBean;
        COAdministrateurViewBean nouveauCompte = new COAdministrateurViewBean();
        nouveauCompte.copyDataFromEntity(administrateurViewBean);
        BTransaction transaction = (BTransaction) administrateurViewBean.getSession().newTransaction();
        try {
            transaction.openTransaction();
            administrateurViewBean.delete(transaction);
            BSession caSession = (BSession) GlobazSystem.getApplication(CAApplication.DEFAULT_APPLICATION_OSIRIS)
                    .newSession();
            session.connectSession(caSession);
            nouveauCompte.setSession(caSession);
            nouveauCompte.add(transaction);
            nouveauCompte.setISession(session);
            if (transaction.hasErrors()) {
                transaction.rollback();
            } else {
                transaction.commit();
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        } finally {
            if (transaction != null) {
                transaction.closeTransaction();
            }
        }
    }

}
