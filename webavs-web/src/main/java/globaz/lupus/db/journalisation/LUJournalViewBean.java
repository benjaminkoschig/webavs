/*
 * Cr�� le 6 avr. 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.lupus.db.journalisation;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JAUtil;
import globaz.journalisation.constantes.JOConstantes;
import globaz.journalisation.db.common.access.IJOCommonGroupeJournalDefTable;
import globaz.journalisation.db.common.access.IJOCommonJournalisationDefTable;
import globaz.journalisation.db.journalisation.access.JOJournalisation;
import globaz.journalisation.db.journalisation.access.JOReferenceDestination;
import globaz.journalisation.db.journalisation.access.JOReferenceDestinationManager;
import globaz.lupus.db.handler.LUJournalDefaulthandler;
import globaz.pyxis.db.tiers.TITiers;
import globaz.pyxis.db.tiers.TITiersManager;

/**
 * @author ald
 * 
 *         Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class LUJournalViewBean extends JOJournalisation implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private LUComplementJournalListViewBean cplJourn;
    private String destinataire = "-";
    private LUReferenceProvenanceListViewBean refProvList;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_afterDelete(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterDelete(BTransaction transaction) throws Exception {
        LUJournalDefaulthandler jHandler = new LUJournalDefaulthandler();
        if (isJournalInitial()) {
            // si c'est le journal initial, on doit supprimer le groupe journal
            LUGroupeJournalViewBean groupeJ = jHandler
                    .getGroupeJournal(getIdGroupeJournal(), getSession(), transaction);
            groupeJ.delete(transaction);
            // et tous les journaux dans ce groupe
            LUJournalListViewBean listeJ = new LUJournalListViewBean();
            listeJ.setSession(getSession());
            listeJ.setForIdGroupeJournal(getIdGroupeJournal());
            listeJ.wantComplementJournal(false);
            listeJ.find(transaction);
            for (int i = 0; i < listeJ.size(); i++) {
                LUJournalViewBean j = (LUJournalViewBean) listeJ.getEntity(i);
                j.wantCallMethodAfter(false);
                j.retrieve(transaction);
                j.delete(transaction);
            }
        } else {
            // Mettre � jour les liens du journal pr�c�dent et du premier
            // journal et mettre � jour la date de rappel
            // journal pr�c�dent
            LUJournalViewBean jPrec = jHandler.getJournal(getIdPrecedent(), getSession(), transaction);
            // on a de toute fa�on pas affaire au journal initial -->
            // getIdInital > 0
            jPrec.wantCallMethodAfter(false);
            jPrec.setIdSuivant("0");
            jPrec.update(transaction);
            // mise � jour de la date de rappel
            jHandler.updateGroupeJournal(getDateRappel(), "", getSession(), transaction, jPrec);
            // et on supprime tous les journaux suivants
            LUJournalListViewBean listeJ = new LUJournalListViewBean();
            listeJ.wantComplementJournal(false);
            listeJ.wantGroupeJournal(false);
            listeJ.setSession(getSession());
            listeJ.setForIdGroupeJournal(getIdGroupeJournal());
            listeJ.setFromIdJournal(getIdJournalisation());
            listeJ.setOrderby(IJOCommonJournalisationDefTable.IDJOURNALISATION);
            listeJ.find(transaction);
            for (int i = 0; i < listeJ.size(); i++) {
                LUJournalViewBean j = (LUJournalViewBean) listeJ.getEntity(i);
                j.retrieve(transaction);
                j.wantCallMethodAfter(false);
                j.delete(transaction);
            }
        }
    }

    @Override
    protected void _afterRetrieve(BTransaction transaction) throws Exception {
        super._afterRetrieve(transaction);
        /** destinataire */
        JOReferenceDestinationManager refDestiManager = new JOReferenceDestinationManager();
        refDestiManager.setSession(transaction.getSession());
        refDestiManager.setForIdJournalisation(getIdJournalisation());
        refDestiManager.setForTypeReferenceDestination(JOConstantes.CS_JO_LIEN_TIERS_DESTINATAIRE);
        refDestiManager.find(transaction);
        if (refDestiManager.size() > 0) {
            setIdDestinataire(((JOReferenceDestination) refDestiManager.getEntity(0)).getIdCleReferenceDestination());
        }
        if (!"".equals(getIdDestinataire())) {
            TITiersManager tiersManager = new TITiersManager();
            tiersManager.setSession(transaction.getSession());
            tiersManager.setForIdTiers(getIdDestinataire());
            tiersManager.find(transaction);
            if (tiersManager.size() > 0) {
                setDestinataire(((TITiers) tiersManager.getEntity(0)).getNomPrenom());
            }
        }
        if (!isLoadedFromManager()) {
            // charge la liste des param�tres
            refProvList = new LUReferenceProvenanceListViewBean();
            refProvList.setSession(getSession());
            refProvList.setForIdJournalisation(getIdJournalisation());
            refProvList.find(transaction);
            // charge la liste des compl�ments journal
            cplJourn = new LUComplementJournalListViewBean();
            cplJourn.setSession(getSession());
            cplJourn.setForIdJournalisation(getIdJournalisation());
            cplJourn.find(transaction);
            // charge le groupe relatif au journal
            LUGroupeJournalViewBean groupe = new LUGroupeJournalViewBean();
            groupe.setSession(getSession());
            groupe.setIdGroupeJournal(getIdGroupeJournal());
            groupe.retrieve(transaction);
            if (!groupe.isNew()) {
                setDateRappel(groupe.getDateRappel());
                setDateReception(groupe.getDateReception());
            }
        }
    }

    @Override
    protected void _afterUpdate(BTransaction transaction) throws Exception {
        if (JAUtil.isStringEmpty(getDateRappel())) {
            // TODO: update date rappel dans groupe
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);
        setDateRappel(statement.dbReadDateAMJ(IJOCommonGroupeJournalDefTable.DATE_RAPPEL));
    }

    /**
     * @return
     */
    public LUComplementJournalListViewBean getCplJourn() {
        return cplJourn;
    }

    public String getDestinataire() {
        return destinataire;
    }

    /*
     * (non-Javadoc)
     * 
     * @seeglobaz.journalisation.db.journalisation.access.JOJournalisation# getFindUtilisateur()
     */
    public void getFindUtilisateur() throws Exception {
        // TODO : au besoin impl�menter cette m�thode pour l'AVS
    }

    /**
     * @return
     */
    public LUReferenceProvenanceListViewBean getRefProvList() {
        return refProvList;
    }

    public boolean isJournalInitial() {
        return super.getIdInitial().equals("0");
    }

    /**
     * @param bean
     */
    public void setCplJourn(LUComplementJournalListViewBean bean) {
        cplJourn = bean;
    }

    /**
     * @param string
     */
    public void setDestinataire(String string) {
        destinataire = string;
    }

    /**
     * @param bean
     */
    public void setRefProvList(LUReferenceProvenanceListViewBean bean) {
        refProvList = bean;
    }

}
