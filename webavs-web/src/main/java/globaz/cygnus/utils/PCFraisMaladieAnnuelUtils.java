package globaz.cygnus.utils;

import globaz.cygnus.api.TypesDeSoins.IRFTypesDeSoins;
import globaz.cygnus.api.motifsRefus.IRFMotifsRefus;
import globaz.cygnus.api.paiement.IRFPrestations;
import globaz.cygnus.api.paiement.IRFTypePaiement;
import globaz.cygnus.db.paiement.RFPrestationAccordeeJointREPrestationAccordee;
import globaz.cygnus.db.pegasus.PCDemandeJointQdJointDossierJointTiers;
import globaz.cygnus.db.pegasus.PCDemandeJointQdJointDossierJointTiersManager;
import globaz.cygnus.db.pegasus.PCPrestationAccordeeJointREPrestationAccordeeManager;
import globaz.globall.db.BSession;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import ch.globaz.pegasus.business.constantes.IPCPCAccordee;

public class PCFraisMaladieAnnuelUtils {

    /**
     * Récupère et calcule les montants des frais pour une liste de personnes.
     * 
     * @param session
     *            Session
     * @param listIdTiersFamille
     *            liste de personnes concernés, sous forme de idTiers de la table des Tiers séparé par des virgules
     * @param annee
     *            Année concernée
     * @return tableau de montants au format suivant [Total frais maladie, total frais invalidite]
     * @throws Exception
     */
    public static String[] calculeMontantsRFMBeneficiaire(BSession session, String listIdTiersFamille, String annee)
            throws Exception {
        PCDemandeJointQdJointDossierJointTiersManager rfDemMgr = new PCDemandeJointQdJointDossierJointTiersManager();
        rfDemMgr.setSession(session);
        rfDemMgr.setForInIdTiers(listIdTiersFamille);
        rfDemMgr.setForAnneeDemande(annee);
        rfDemMgr.changeManagerSize(0);
        rfDemMgr.find();

        double totalAI = 0;
        double totalFraisMaladie = 0;

        final List<String> listCsTypeMotifRefus = new ArrayList<String>() {
            /**
             * 
             */
            private static final long serialVersionUID = 1L;

            {
                this.add(IRFMotifsRefus.ID_FRQP_MAXIMUM_N_FRANC_PAR_ASSURE); // tous types de soin
            }
        };

        // parcourt les demandes trouvées
        for (Iterator iterator = rfDemMgr.iterator(); iterator.hasNext();) {
            PCDemandeJointQdJointDossierJointTiers demande = (PCDemandeJointQdJointDossierJointTiers) iterator.next();

            String montantFactureAPayer = demande.getMontantFactureAPayer();
            String montantRefuse = demande.getMontantRefuse();
            totalFraisMaladie += Double.parseDouble(montantFactureAPayer);
            if (IPCPCAccordee.CS_TYPE_PC_INVALIDITE.equals(demande.getCsTypeQdPrincipal())) {
                totalAI += Double.parseDouble(montantFactureAPayer);
            }

        }

        return new String[] { String.valueOf(totalFraisMaladie), String.valueOf(totalAI) };

    }

    public static String[] calculeRFMontantsFicheTransfert(BSession session, String listIdTiersFamille, String annee)
            throws Exception {

        // phase 1 : parcourir les RF accordées *************************************

        PCPrestationAccordeeJointREPrestationAccordeeManager mgr = new PCPrestationAccordeeJointREPrestationAccordeeManager();
        mgr.setSession(session);
        mgr.setForInIdTiers(listIdTiersFamille);
        mgr.setForAnnee(annee);
        mgr.changeManagerSize(0);
        mgr.find();

        double montantRegime = 0;

        for (Iterator iterator = mgr.iterator(); iterator.hasNext();) {
            RFPrestationAccordeeJointREPrestationAccordee prestation = (RFPrestationAccordeeJointREPrestationAccordee) iterator
                    .next();

            if (IRFPrestations.CS_SOURCE_RFACCORDEES_REGIME.equals(prestation.getCs_source())) {
                montantRegime += Double.parseDouble(prestation.getMontantPrestation());
            }
        }

        // phase 2 : parcourir les demandes ***********************************

        PCDemandeJointQdJointDossierJointTiersManager rfDemMgr = new PCDemandeJointQdJointDossierJointTiersManager();
        rfDemMgr.setSession(session);
        rfDemMgr.setForInIdTiers(listIdTiersFamille);
        rfDemMgr.setForAnneeDemande(annee);
        rfDemMgr.setForTypeSoin(IRFTypesDeSoins.CS_REGIME_ALIMENTAIRE_02);
        rfDemMgr.changeManagerSize(0);
        rfDemMgr.find();
        // parcourt les demandes trouvées
        for (Iterator iterator = rfDemMgr.iterator(); iterator.hasNext();) {
            PCDemandeJointQdJointDossierJointTiers demande = (PCDemandeJointQdJointDossierJointTiers) iterator.next();

            // si type de décision courant
            if (IRFTypePaiement.PAIEMENT_COURANT.equals(demande.getCsTypePaiement())) {
                // prendre montant retro de la decision
                montantRegime += Double.parseDouble(demande.getMontantRetroactifPaiementCourant());
            } else if (IRFTypePaiement.PAIEMENT_RETROACTIF.equals(demande.getCsTypePaiement())) {
                // sinon si type de décision retroactif, prendre le montant habituel
                montantRegime += Double.parseDouble(demande.getMontantFactureAPayer());
            }
        }

        return new String[] { String.valueOf(montantRegime) };
    }
}
