package globaz.hera.tools;

import globaz.globall.api.BITransaction;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.hera.db.famille.SFApercuRelationFamilialeRequerant;
import globaz.hera.db.famille.SFApercuRelationFamilialeRequerantManager;
import globaz.hera.db.famille.SFConjoint;
import globaz.hera.db.famille.SFConjointManager;
import globaz.hera.db.famille.SFEnfant;
import globaz.hera.db.famille.SFMembreFamille;
import globaz.hera.db.famille.SFPeriode;
import globaz.hera.db.famille.SFPeriodeManager;
import globaz.hera.db.famille.SFRelationConjoint;
import globaz.hera.db.famille.SFRelationConjointManager;
import globaz.hera.db.famille.SFRelationFamilialeRequerant;
import globaz.hera.db.famille.SFRequerant;
import globaz.prestation.tools.PRAssert;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Descpription
 * 
 * @author scr
 */

public class SFCloneFamilleTools {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * A revoir, car désormais, le domaineApplication est également dans la table du membre de famille.
     */
    public static SFRequerant cloneFamilleRequerant(BSession session, SFRequerant requerantSource, String csDomaine)
            throws Exception {

        BITransaction transaction = null;

        try {
            transaction = session.newTransaction();

            // On contrôle que le requérant dont on veut copier la famille n'a
            // pas déjà une
            // famille dans le domaine destination.
            requerantSource.getIdMembreFamille();
            SFMembreFamille mf = new SFMembreFamille();
            mf.setIdMembreFamille(requerantSource.getIdMembreFamille());
            mf.setSession(session);
            mf.retrieve();
            PRAssert.notIsNew(mf, null);

            SFApercuRelationFamilialeRequerantManager mgr = new SFApercuRelationFamilialeRequerantManager();
            mgr.setSession(session);
            mgr.setForIdTiers(mf.getIdTiers());
            mgr.find(BManager.SIZE_NOLIMIT);

            // DEad code
            // for (int i = 0; i < mgr.size(); i++) {
            // //SFApercuRelationFamilialeRequerant elm =
            // (SFApercuRelationFamilialeRequerant)mgr.get(i);
            // //if (csDomaine.equals(elm.getCsDomaine())) {
            // throw new Exception
            // (session.getLabel("ERROR_REQ_DEJA_FAMILLE_DOMAINE") +
            // session.getCodeLibelle(csDomaine));
            // //}
            // }

            if (mgr.size() > 0) {
                throw new Exception(session.getLabel("ERROR_REQ_DEJA_FAMILLE_DOMAINE")
                        + session.getCodeLibelle(csDomaine));
            }

            // On récupère tous les membres de la famille du requérant, ainsi
            // que le requérant lui-même !!!
            SFApercuRelationFamilialeRequerantManager mgr2 = new SFApercuRelationFamilialeRequerantManager();
            mgr2.setSession(session);
            mgr2.setForIdRequerant(requerantSource.getIdRequerant());
            mgr2.setWantConjointInconnu(true);
            mgr2.find(transaction);

            List idsMembreFamille = new ArrayList();

            // Key : oldIdMF Value : newIdMF
            Map idsEnfant = new HashMap();
            // Key : oldIdConjoint Value : newIdConjoint
            Map idsConjoints = new HashMap();

            String idMFRequerant = null;
            for (int i = 0; i < mgr2.size(); i++) {

                SFApercuRelationFamilialeRequerant elm = (SFApercuRelationFamilialeRequerant) mgr2.getEntity(i);
                String idMembreFamille = elm.getIdMembreFamille();

                if (ISFSituationFamiliale.ID_MEMBRE_FAMILLE_CONJOINT_INCONNU.equals(elm.getIdMembreFamille())) {
                    idsMembreFamille.add(elm.getIdMembreFamille());
                } else {
                    SFMembreFamille newMembre = new SFMembreFamille();
                    newMembre.setCsCantonDomicile(elm.getCsCantonDomicile());
                    newMembre.setCsEtatCivil(elm.getCsEtatCivil());
                    newMembre.setCsNationalite(elm.getCsNationalite());
                    newMembre.setCsSexe(elm.getCsSexe());
                    newMembre.setDateDeces(elm.getDateDeces());
                    newMembre.setDateNaissance(elm.getDateNaissance());
                    newMembre.setIdTiers(elm.getIdTiers());
                    newMembre.setNom(elm.getNom());
                    newMembre.setNss(elm.getNss());
                    newMembre.setPays(elm.getPays());
                    newMembre.setPrenom(elm.getPrenom());
                    newMembre.setPays(elm.getPays());
                    newMembre.add(transaction);

                    if (requerantSource.getIdMembreFamille().equals(elm.getIdMembreFamille())) {
                        idMFRequerant = newMembre.getIdMembreFamille();
                    }
                    idsMembreFamille.add(newMembre.getIdMembreFamille());

                    // Copies des périodes ???? voir si une période par
                    // domaine(mf) ou par tiers
                    // Pour le moment, fait par domaine !!!
                    SFPeriodeManager mgr10 = new SFPeriodeManager();
                    mgr10.setSession(session);
                    mgr10.setForIdMembreFamille(elm.getIdMembreFamille());
                    mgr10.find(transaction);
                    for (int j = 0; j < mgr10.size(); j++) {
                        SFPeriode periode = (SFPeriode) mgr10.getEntity(j);

                        SFPeriode newPeriode = new SFPeriode();
                        newPeriode.setSession(session);
                        newPeriode.setDateDebut(periode.getDateDebut());
                        newPeriode.setDateFin(periode.getDateFin());
                        newPeriode.setIdDetenteurBTE(periode.getIdDetenteurBTE());
                        newPeriode.setCsTypeDeDetenteur(periode.getCsTypeDeDetenteur());
                        newPeriode.setIdMembreFamille(newMembre.getIdMembreFamille());
                        newPeriode.setPays(periode.getPays());
                        newPeriode.setType(periode.getType());
                        newPeriode.add(transaction);
                    }

                    // On regarde si le mf est un enfant !!!
                    SFEnfant enfant = new SFEnfant();
                    enfant.setSession(session);
                    enfant.setAlternateKey(SFEnfant.ALTERNATE_KEY_IDMEMBREFAMILLE);
                    enfant.setIdMembreFamille(elm.getIdMembreFamille());
                    enfant.retrieve(transaction);
                    if (!enfant.isNew()) {
                        idsEnfant.put(enfant.getIdMembreFamille(), newMembre.getIdMembreFamille());
                    }
                }

                // Récupération des conjoints du requérant
                SFConjointManager mgr3 = new SFConjointManager();
                mgr3.setSession(session);
                mgr3.setForIdsConjoints(requerantSource.getIdMembreFamille(), elm.getIdMembreFamille());
                mgr3.find(transaction);

                for (int j = 0; j < mgr3.size(); j++) {
                    SFConjoint conjoint = (SFConjoint) mgr3.getEntity(j);
                    // Copie de la relation de conjoint !!!
                    SFConjoint newConjoint = new SFConjoint();
                    newConjoint.setSession(session);
                    newConjoint.setIdConjoint1(idMembreFamille);
                    newConjoint.setIdConjoint2(requerantSource.getIdMembreFamille());
                    newConjoint.add(transaction);

                    idsConjoints.put(conjoint.getIdConjoints(), newConjoint.getIdConjoints());

                    // Pour chaque conjoint trouvé, récupérer ses relations
                    SFRelationConjointManager mgr4 = new SFRelationConjointManager();
                    mgr4.setSession(session);
                    mgr4.setForIdDesConjoints(conjoint.getIdConjoints());
                    for (int k = 0; k < mgr4.size(); k++) {
                        SFRelationConjoint relcon = (SFRelationConjoint) mgr4.getEntity(k);
                        SFRelationConjoint newRelcon = new SFRelationConjoint();
                        newRelcon.setSession(session);
                        newRelcon.setDateDebut(relcon.getDateDebut());
                        newRelcon.setDateFin(relcon.getDateFin());
                        newRelcon.setIdConjoints(newConjoint.getIdConjoints());
                        newRelcon.setTypeRelation(relcon.getTypeRelation());
                        newRelcon.add(transaction);
                    }
                }
            }

            if (idMFRequerant == null) {
                throw new Exception(session.getLabel("ERROR_REQ_PAS_TROUVE_DANS_LISTE_MEMBRES"));
            }
            // On créé le requérant
            SFRequerant newRequerant = new SFRequerant();
            newRequerant.setSession(session);
            newRequerant.setIdDomaineApplication(csDomaine);
            newRequerant.setIdMembreFamille(idMFRequerant);
            newRequerant.add(transaction);

            // Création de la table des relations MF<->Requerant (SFREFARE)
            for (Iterator iterator = idsMembreFamille.iterator(); iterator.hasNext();) {
                String idMF = (String) iterator.next();
                SFRelationFamilialeRequerant refare = new SFRelationFamilialeRequerant();
                refare.setSession(session);
                refare.setIdMembreFamille(idMF);
                refare.setIdRequerant(newRequerant.getIdRequerant());
                refare.add(transaction);
            }

            // On créé les enfants
            Set keys = idsEnfant.keySet();
            for (Iterator iterator = keys.iterator(); iterator.hasNext();) {
                String oldIdMFEnfant = (String) iterator.next();

                SFEnfant oldEnfant = new SFEnfant();
                oldEnfant.setSession(session);
                oldEnfant.setIdMembreFamille(oldIdMFEnfant);
                oldEnfant.setAlternateKey(SFEnfant.ALTERNATE_KEY_IDMEMBREFAMILLE);
                oldEnfant.retrieve(transaction);
                PRAssert.notIsNew(oldEnfant, session.getLabel("ERROR_INCOHERENCE_DONNES_ENFANT_INTROUVABLE"));

                String newIdMF = (String) idsEnfant.get(oldIdMFEnfant);

                // Récupération du nouvel idConjoint
                String newIdConjoint = (String) idsConjoints.get(oldEnfant.getIdConjoint());

                SFEnfant newEnfant = new SFEnfant();
                newEnfant.setIdConjoint(newIdConjoint);
                newEnfant.setIdMembreFamille(newIdMF);
                newEnfant.add(transaction);
            }

            transaction.commit();
            return newRequerant;

        } catch (Exception e) {
            transaction.rollback();
            return null;
        } finally {
            if (transaction != null) {
                transaction.closeTransaction();
            }
        }

    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private SFCloneFamilleTools() {
        super();
    }
}
