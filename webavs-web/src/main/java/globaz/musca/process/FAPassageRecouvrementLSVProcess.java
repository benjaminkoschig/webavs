/*
 * Créé le 9 nov. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.musca.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.jade.client.util.JadeStringUtil;
import globaz.musca.api.IFAPassage;
import globaz.musca.application.FAApplication;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.musca.db.facturation.FAEnteteFactureManager;
import globaz.musca.db.facturation.FAPassage;
import globaz.pyxis.api.osiris.TITiersOSI;

/**
 * @author mmu <BR>
 *         Module implementant le recouvrement LSV: <BR>
 *         Recherche les entetes de factures du passage qui sont en mode de recouvrement directe ou automatiquement et
 *         recherche une adresse de payement valide. Si une adresse de payement valide est trouvée, l'adresse est
 *         ajoutée à l'entête et l'entete passe en mode recouvrement directe. <BR>
 *         Lors de la comptabilisation, un ordre de recouvrement sera créé pour chaque entete.
 */
public class FAPassageRecouvrementLSVProcess extends FAGenericProcess {

    private static final long serialVersionUID = -7541845733599791541L;
    /**
     * Domaine d'application de pour la recherche de d'adresse de payement (Débit direct = 519012)
     */
    public static final String DOMAINE_APPLICATION_DEFAUT = TITiersOSI.DOMAINE_RECOUVREMENT;

    /**
     * Lance la génération du module
     */
    public static String updateEnteteForLSV(IFAPassage passage, BSession session, BTransaction transaction,
            String fromNumAffilie, String tillNumAffilie) {
        String errorMessage = "";

        FAEnteteFactureManager enteteMgr = new FAEnteteFactureManager();
        BStatement statement = null;
        String lastIdEnteteFacture = null;

        try {
            // dans le manager on ne recherche que les entetes qui ont une
            // addresse de LSV
            FAApplication app = (FAApplication) GlobazServer.getCurrentSystem().getApplication(
                    FAApplication.DEFAULT_APPLICATION_MUSCA);
            enteteMgr.setSession(session);
            enteteMgr.setUseManagerForLSV(true); // on utilise un manager
            // specifique pour retrouver
            // les adresse de payement
            enteteMgr.setForIdPassage(passage.getIdPassage());
            enteteMgr.setForTotalFactureBigger(app.getMontantMinimePos()); // entête
            // de
            // facture
            // positive
            enteteMgr.setWantModeRecouvrementAutomatiqueOuDirect(true); // mode
            // recouvrement
            // direct
            // et
            // automatique
            enteteMgr.setFromDateFacturation(passage.getDateFacturation());
            if (!JadeStringUtil.isEmpty(fromNumAffilie)) {
                enteteMgr.setFromIdExterneRole(fromNumAffilie);
            }
            if (!JadeStringUtil.isEmpty(tillNumAffilie)) {
                enteteMgr.setForTillIdExterneRole(tillNumAffilie);
            }

            // Commit les validation faites par d'autres modules,
            transaction.commit();

            statement = enteteMgr.cursorOpen(transaction);
            FAEnteteFacture entete = null;
            while ((entete = (FAEnteteFacture) enteteMgr.cursorReadNext(statement)) != null) {

                // Si un tiers a deux adresses de paiement dans le meme domaine
                if (entete.getIdEntete().equals(lastIdEnteteFacture)) {
                    continue;
                }

                lastIdEnteteFacture = entete.getIdEntete();

                String adressePaie = entete.getIdTiAdressePaiement();
                if (JadeStringUtil.isIntegerEmpty(adressePaie)) {
                    // Si l'adresse payement n'a pas pu être retrouvé bien qu'un
                    // domaine ait été renseigné, on remet le champ à null
                    continue; // passe à l'entete suivante
                }

                // Une adresse active a été trouvée:
                // placer l'entete en mode recouvrement directe
                entete.setIdModeRecouvrement(FAEnteteFacture.CS_MODE_RECOUVREMENT_DIRECT);

                // stocker l'identifiant dans l'entete de facture
                entete.setIdAdressePaiement(entete.getIdTiAdressePaiement());
                entete.update(transaction);
            }

            transaction.commit();

        } catch (Exception e) {

            try {
                transaction.rollback();
                enteteMgr.cursorClose(statement);
            } catch (Exception e1) {
            }
            errorMessage = e.getMessage();

            transaction.addErrors("FAPassageRecouvrementLSVProcess: " + e.getMessage());

        }
        // test du passage
        return errorMessage;
    }

    /**
	 * 
	 */
    public FAPassageRecouvrementLSVProcess() {
        super();
    }

    /**
     * @param parent
     */
    public FAPassageRecouvrementLSVProcess(BProcess parent) {
        super(parent);
    }

    /**
     * @param session
     */
    public FAPassageRecouvrementLSVProcess(BSession session) {
        super(session);
    }

    public boolean _executeGenererLSVProcess(IFAPassage passage) {
        boolean succes = true;
        setState(getSession().getLabel("PROCESSSTATE_TRAITEMENT_PASSAGE"));

        if ((passage == null) || passage.isNew()) {
            passage = new FAPassage();
            passage.setIdPassage(getIdPassage());
            passage.setISession(getSession());
            try {
                passage.retrieve(getTransaction());
                this.setPassage((FAPassage) passage);
            } catch (Exception e) {
                getMemoryLog().logMessage("Impossible de retourner le passage: " + e.getMessage(),
                        FWViewBeanInterface.ERROR, passage.getClass().getName());
            }
            ;
        }

        String errorMessage = FAPassageRecouvrementLSVProcess.updateEnteteForLSV(passage, getSession(),
                getTransaction(), "", "");
        if (!JadeStringUtil.isEmpty(errorMessage)) {
            getMemoryLog().logMessage(errorMessage, FWMessage.ERREUR, "");
            succes = false;
        }
        return succes;
    }

    @Override
    protected boolean _executeProcess() {
        return false;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

}
