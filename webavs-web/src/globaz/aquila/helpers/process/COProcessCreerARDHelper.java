/*
 * Créé le 8 févr. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.aquila.helpers.process;

import globaz.aquila.api.ICOApplication;
import globaz.aquila.api.ICOSequenceConstante;
import globaz.aquila.application.COApplication;
import globaz.aquila.db.access.batch.COSequenceManager;
import globaz.aquila.process.COProcessCreerARD;
import globaz.aquila.vb.process.COProcessCreerARDViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexeManager;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.external.IntRole;
import java.util.Iterator;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class COProcessCreerARDHelper extends FWHelper {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe COProcessCreerARDHelper.
     */
    public COProcessCreerARDHelper() {
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.framework.controller.FWHelper#_retrieve(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        COProcessCreerARDViewBean caViewBean = (COProcessCreerARDViewBean) viewBean;

        caViewBean.setISession(session);

        // creer une session Osiris
        BSession sessionOsiris = (BSession) GlobazSystem.getApplication(CAApplication.DEFAULT_APPLICATION_OSIRIS)
                .newSession();

        session.connectSession(sessionOsiris);

        // charger la section principal et le compte annexe de la societe
        CASection section = new CASection();

        section.setIdSection(caViewBean.getIdSectionPrincipale());
        section.setSession(sessionOsiris);
        section.retrieve();

        caViewBean.setSection(section);

        String idExterne = section.getCompteAnnexe().getIdExterneRole();

        // charger les comptes admin
        CACompteAnnexeManager caMgr = new CACompteAnnexeManager();

        COApplication application = (COApplication) GlobazSystem
                .getApplication(ICOApplication.DEFAULT_APPLICATION_AQUILA);
        if (application.numeroAdministrateurReplaceTiret()) {
            caMgr.setLikeIdExterneRole(JadeStringUtil.split(idExterne, '-', Integer.MAX_VALUE)[0]);
        } else {
            caMgr.setLikeIdExterneRole(idExterne.substring(0, idExterne.lastIndexOf('-')));
        }
        caMgr.setISession(sessionOsiris);
        caMgr.find();

        // ôter le compte de la société elle-même pour n'avoir que les
        // administrateurs
        Iterator<CACompteAnnexe> caIter = caMgr.iterator();
        while (caIter.hasNext()) {
            CACompteAnnexe ca = caIter.next();

            if (!section.getCompteAnnexe().getIdCompteAnnexe().equals(ca.getIdCompteAnnexe())) {
                if (ca.getIdRole().equals(IntRole.ROLE_ADMINISTRATEUR)) {
                    caViewBean.addCompte(ca);
                }
            }
        }

        // charger la liste des séquences ARD
        COSequenceManager sequences = new COSequenceManager();

        sequences.setForLibSequence(ICOSequenceConstante.CS_SEQUENCE_ARD);
        sequences.setISession(session);
        sequences.find();

        caViewBean.setSequences(sequences.getContainer());

        // l'email de l'utilisateur courant
        caViewBean.setEMailAddress(session.getUserEMail());
    }

    /**
     * @see globaz.framework.controller.FWHelper#_start(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        try {
            COProcessCreerARDViewBean processContentieuxViewBean = (COProcessCreerARDViewBean) viewBean;
            COProcessCreerARD processCreerARD = new COProcessCreerARD();

            processCreerARD.setMontantsParAdmin(processContentieuxViewBean.getMontantsParAdmin());
            processCreerARD.setSequencesParAdmin(processContentieuxViewBean.getSequencesParAdmin());
            processCreerARD.setIdSectionPrincipale(processContentieuxViewBean.getIdSectionPrincipale());
            processCreerARD.setLibelleJournal(processContentieuxViewBean.getLibelleJournal());

            processCreerARD.setEMailAddress(processContentieuxViewBean.getEMailAddress());
            processCreerARD.setControleTransaction(true);
            processCreerARD.setISession(session);

            BProcessLauncher.start(processCreerARD);
        } catch (Exception e) {
            ((BSession) session).addError(e.getMessage());
        }
    }
}
