package globaz.libra.interfaces;

import globaz.globall.db.BTransaction;
import globaz.libra.db.domaines.LIDomaines;
import globaz.libra.db.domaines.LIDomainesManager;
import globaz.libra.db.dossiers.LIDossiers;
import globaz.libra.db.groupes.LIGroupes;
import globaz.libra.db.groupes.LIGroupesManager;
import globaz.libra.db.utilisateurs.LIUtilisateurs;
import globaz.libra.db.utilisateurs.LIUtilisateursManager;
import java.util.Iterator;
import ch.globaz.libra.business.services.LibraServiceLocator;
import ch.globaz.libra.constantes.ILIConstantesExternes;

/**
 * Fait :
 * 
 * [1] > Création de dossier [2] > Clôture de dossier [3] > Réactivation du dossier
 * 
 * Attente besoin pour autres interfaces
 */
public class LIDossiersInterfaces {

    // ~Constructor------------------------------------------------------------------------------------------------------

    public LIDossiersInterfaces() {
        super();
    }

    // ~Methods----------------------------------------------------------------------------------------------------------

    /**
     * [2] Clôture d'un dossier
     * 
     * -> Changement de l'état en "Clôturé"
     * 
     * @param transaction
     * @param idExterne
     * 
     * @return BTransaction
     */
    public LIDossiers clotureDossier(BTransaction transaction, String idExterne) {

        LIDossiers dossier = new LIDossiers();
        try {

            // Retrieve du dossier
            dossier.setSession(transaction.getSession());
            dossier.setAlternateKey(LIDossiers.ALTERNATE_KEY_ID_EXTERNE);
            dossier.setIdExterne(idExterne);
            dossier.retrieve();

            if (dossier.isNew()) {
                throw new Exception("Dossier inexistant dans la base de données");
            }

            dossier.setCsEtat(ILIConstantesExternes.CS_ETAT_CLOS);
            dossier.update(transaction);

            // Journalisation du changement
            LibraServiceLocator.getJournalisationService().createJournalisation(dossier.getIdExterne(),
                    transaction.getSession().getLabel("INTER_DOSSIER_CLOTURE"), true);

        } catch (Exception e) {
            transaction.getSession().addError(e.toString());
            transaction.addErrors(e.toString());
        }

        return dossier;
    }

    /**
     * [1] Création de dossier simple
     * 
     * -> Dossier de base -> Groupe et Utilisateur par défaut selon domaine fourni -> Si dossier déjà existant,
     * réactivation si clos, si déjà ouvert, msg d'erreur
     * 
     * @param transaction
     * @param idTiers
     * @param csDomaine
     * @param idExterne
     * 
     * @return LIDossiers
     */
    public LIDossiers createDossier(BTransaction transaction, String idTiers, String csDomaine, String idExterne) {

        LIDossiers dossier = new LIDossiers();

        try {

            // Dossier existe déjà ? Clôturé ?

            dossier.setSession(transaction.getSession());
            dossier.setAlternateKey(LIDossiers.ALTERNATE_KEY_ID_EXTERNE);
            dossier.setIdExterne(idExterne);
            dossier.retrieve();

            if (!dossier.isNew()) {
                if (dossier.getCsEtat().equals(ILIConstantesExternes.CS_ETAT_CLOS)) {
                    reactivationDossier(transaction, idExterne);
                } else {
                    throw new Exception("Dossier déjà existant pour l'idExterne : " + idExterne);
                }
            } else {
                dossier = new LIDossiers();
                dossier.setSession(transaction.getSession());
                dossier.setIdTiers(idTiers);
                dossier.setCsEtat(ILIConstantesExternes.CS_ETAT_OUVERT);
                dossier.setIsUrgent(Boolean.FALSE);
                dossier.setIdExterne(idExterne);

                // Recherche du domaine / groupe / utilisateur par défaut
                LIDomainesManager domMgr = new LIDomainesManager();
                domMgr.setSession(transaction.getSession());
                domMgr.setForCsDomaine(csDomaine);
                domMgr.find();

                if (!domMgr.isEmpty()) {
                    LIDomaines domaine = (LIDomaines) domMgr.getFirstEntity();

                    LIGroupesManager grpMgr = new LIGroupesManager();
                    grpMgr.setSession(transaction.getSession());
                    grpMgr.setForIdDomaine(domaine.getIdDomaine());
                    grpMgr.find();

                    LIUtilisateurs firstUser = null;
                    LIUtilisateurs userDefault = null;

                    String idDomaine = "";
                    String idGroupe = "";
                    String idUser = "";

                    if (!grpMgr.isEmpty()) {

                        String idUserSession = transaction.getSession().getUserId();

                        for (Iterator iterator = grpMgr.iterator(); iterator.hasNext();) {
                            LIGroupes groupe = (LIGroupes) iterator.next();

                            LIUtilisateursManager userMgr = new LIUtilisateursManager();
                            userMgr.setSession(transaction.getSession());
                            userMgr.setForIdGroupe(groupe.getIdGroupe());
                            userMgr.find();

                            if (!userMgr.isEmpty()) {
                                for (Iterator iterator2 = userMgr.iterator(); iterator2.hasNext();) {
                                    LIUtilisateurs user = (LIUtilisateurs) iterator2.next();

                                    // Si user.getIdUtilisateurExterne =
                                    // idUserSession
                                    if (user.getIdUtilisateurExterne().equals(idUserSession)) {

                                        firstUser = user;

                                        if (user.getIsDefault().booleanValue()) {
                                            userDefault = user;
                                        }

                                    }
                                }
                            }
                        }

                        if (firstUser == null && userDefault == null) {
                            throw new Exception("Aucun user trouvé pour le domaine : " + csDomaine);
                        } else {

                            if (userDefault == null) {
                                userDefault = firstUser;
                            }

                            idDomaine = domaine.getIdDomaine();
                            idGroupe = userDefault.getIdGroupe();
                            idUser = userDefault.getIdUtilisateur();
                        }

                    } else {
                        throw new Exception("Pas de groupe trouvé pour le domaine : " + csDomaine);
                    }

                    dossier.setIdDomaine(idDomaine);
                    dossier.setIdGroupe(idGroupe);
                    dossier.setIdGestionnaire(idUser);
                    dossier.add(transaction);

                    // Création de la journalisation
                    LibraServiceLocator.getJournalisationService().createJournalisation(dossier.getIdExterne(),
                            transaction.getSession().getLabel("INTER_DOSSIER_CREATION"), true);

                } else {
                    throw new Exception("Pas de domaine trouvé pour le domaine : " + csDomaine);
                }
            }
        } catch (Exception e) {
            transaction.getSession().addError(e.toString());
            transaction.addErrors(e.toString());
        }

        return dossier;

    }

    /**
     * [3] Réactivation d'un dossier
     * 
     * -> Changement de l'état "Clôturé" en "Ouvert"
     * 
     * @param transaction
     * @param idExterne
     * 
     * @return BTransaction
     */
    public LIDossiers reactivationDossier(BTransaction transaction, String idExterne) {

        LIDossiers dossier = new LIDossiers();
        try {

            // Retrieve du dossier
            dossier.setSession(transaction.getSession());
            dossier.setAlternateKey(LIDossiers.ALTERNATE_KEY_ID_EXTERNE);
            dossier.setIdExterne(idExterne);
            dossier.retrieve();

            if (dossier.isNew()) {
                throw new Exception("Dossier inexistant dans la base de données");
            }

            dossier.setCsEtat(ILIConstantesExternes.CS_ETAT_OUVERT);
            dossier.update(transaction);

            // Journalisation du changement
            LibraServiceLocator.getJournalisationService().createJournalisation(dossier.getIdExterne(),
                    transaction.getSession().getLabel("INTER_DOSSIER_REACTIVATION"), true);

        } catch (Exception e) {
            transaction.getSession().addError(e.toString());
            transaction.addErrors(e.toString());
        }

        return dossier;
    }

}
