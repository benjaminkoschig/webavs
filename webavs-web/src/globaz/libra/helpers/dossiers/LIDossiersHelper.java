package globaz.libra.helpers.dossiers;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.journalisation.db.journalisation.access.JOJournalisation;
import globaz.libra.db.groupes.LIGroupes;
import globaz.libra.db.journalisations.LIEcheancesJointDossiersManager;
import globaz.libra.db.journalisations.LIJournalisationsJointDossiers;
import globaz.libra.db.utilisateurs.LIUtilisateurs;
import globaz.libra.vb.dossiers.LIDossiersViewBean;
import java.util.Iterator;
import ch.globaz.libra.business.services.LibraServiceLocator;

/**
 * 
 * @author HPE
 * 
 */
public class LIDossiersHelper extends FWHelper {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected void _update(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        BITransaction transaction = null;
        LIDossiersViewBean vb = (LIDossiersViewBean) viewBean;

        try {
            transaction = ((BSession) session).newTransaction();
            transaction.openTransaction();

            LIDossiersViewBean oldEntity = new LIDossiersViewBean();
            oldEntity.setSession((BSession) session);
            oldEntity.setIdDossier(vb.getIdDossier());
            oldEntity.retrieve();

            // Mise à jour du dossier (uniquement Gestionnaire possible de
            // modifier)
            vb.update(transaction);

            // Prévoir modifications en cascade si nécessaire (Echéances)
            if (vb.isModifEcheances()) {

                // Modifier toutes les échéances pour ce dossier
                LIEcheancesJointDossiersManager echMgr = new LIEcheancesJointDossiersManager();
                echMgr.setSession((BSession) session);
                echMgr.setForIdDossier(vb.getIdDossier());
                echMgr.find(transaction);

                LIUtilisateurs user = new LIUtilisateurs();
                user.setSession((BSession) session);
                user.setIdUtilisateur(vb.getIdGestionnaire());
                user.retrieve();

                for (Iterator iterator = echMgr.iterator(); iterator.hasNext();) {

                    LIJournalisationsJointDossiers echE = (LIJournalisationsJointDossiers) iterator.next();

                    JOJournalisation ech = new JOJournalisation();
                    ech.setSession((BSession) session);
                    ech.setIdJournalisation(echE.getIdJournalisation());
                    ech.retrieve();

                    ech.setIdUtilisateur(user.getIdUtilisateurExterne());
                    ech.update(transaction);
                }

            }

            // Journalisation des changements
            StringBuffer bufferChange = new StringBuffer();

            if (!oldEntity.getIsUrgent().toString().equals(vb.getIsUrgent().toString())) {
                bufferChange.append(((BSession) session).getLabel("HELPER_DOSSIER_URGENCE")
                        + " "
                        + (oldEntity.getIsUrgent().booleanValue() ? ((BSession) session).getLabel("HELPER_DOSSIER_OUI")
                                : ((BSession) session).getLabel("HELPER_DOSSIER_NON"))
                        + " => "
                        + (vb.getIsUrgent().booleanValue() ? ((BSession) session).getLabel("HELPER_DOSSIER_OUI")
                                : ((BSession) session).getLabel("HELPER_DOSSIER_NON")));
                bufferChange.append("\n");
            }

            if (!oldEntity.getIdGroupe().equals(vb.getIdGroupe())) {

                LIGroupes groupe = new LIGroupes();
                groupe.setSession((BSession) session);
                groupe.setIdGroupe(vb.getIdGroupe());
                groupe.retrieve();

                LIGroupes oldGrp = new LIGroupes();
                oldGrp.setSession((BSession) session);
                oldGrp.setIdGroupe(oldEntity.getIdGroupe());
                oldGrp.retrieve();

                bufferChange.append(((BSession) session).getLabel("HELPER_DOSSIER_GROUPE") + " "
                        + (oldGrp.getLibelleGroupe()) + " => " + (groupe.getLibelleGroupe()));
                bufferChange.append("\n");
            }

            if (!oldEntity.getIdGestionnaire().equals(vb.getIdGestionnaire())) {

                LIUtilisateurs user = new LIUtilisateurs();
                user.setSession((BSession) session);
                user.setIdUtilisateur(vb.getIdGestionnaire());
                user.retrieve();

                LIUtilisateurs oldUser = new LIUtilisateurs();
                oldUser.setSession((BSession) session);
                oldUser.setIdUtilisateur(oldEntity.getIdGestionnaire());
                oldUser.retrieve();

                bufferChange.append(((BSession) session).getLabel("HELPER_DOSSIER_USER") + " "
                        + (oldUser.getIdUtilisateurExterne()) + " => " + (user.getIdUtilisateurExterne()));
                bufferChange.append("\n");

                if (vb.isModifEcheances()) {
                    bufferChange.append(((BSession) session).getLabel("HELPER_DOSSIER_MODIF_ECH"));
                }

                if (bufferChange.length() > 0) {
                    LibraServiceLocator.getJournalisationService().createJournalisationAvecRemarque(vb.getIdDossier(),
                            "Modification dans le dossier", bufferChange.toString(), true);
                }
            }

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
