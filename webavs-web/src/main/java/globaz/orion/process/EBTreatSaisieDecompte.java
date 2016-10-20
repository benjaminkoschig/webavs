package globaz.orion.process;

import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;

public class EBTreatSaisieDecompte extends BProcess {

    private static final long serialVersionUID = 1L;

    @Override
    protected boolean _executeProcess() throws Exception {

        // System.out.println("Lancement process EBTreatSaisieDecompte");

        // Parcours des saisies des d�comptes

        // 1 - recherche de l'affiliation

        // 2 - Recherche si pas d�j� un relev� pour ce mois
        // Si oui, on fixe � relev� compl�mentaire
        // TODO Gestion de l'idexterne facture
        //

        // 3 - Cr�ation du d�compte

        // 4 - Cr�ation des lignes de relev�
        // 4.1 - Recherche si cotisation bien dans la liste

        listeExcelSaisieDecompte();

        return false;
    }

    protected void listeExcelSaisieDecompte() throws Exception {

        EBImprimerSaisieDecompteProcess listeRecap = new EBImprimerSaisieDecompteProcess();
        listeRecap.setParentWithCopy(this);
        listeRecap.setEMailAddress(getEMailAddress());
        listeRecap.executeProcess();

    }

    @Override
    protected String getEMailObject() {
        if (isOnError() || getSession().hasErrors() || isAborted() || getMemoryLog().hasErrors()) {
            return getSession().getLabel("SAISIE_DECOMPTE_KO");
        } else {
            return getSession().getLabel("SAISIE_DECOMPTE_OK");
        }
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    @Override
    protected void _executeCleanUp() {
        // do nothing
    }
}
