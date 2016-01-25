/*
 * Cr�� le 17 ao�t 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.naos.db.beneficiairepc;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.naos.process.AFQuittancePCGInscriptionCIProcess;

/**
 * @author mmu Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class AFQuittancePCGComptabilisationCIProcessViewBean extends AFQuittancePCGInscriptionCIProcess implements
        FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idPassageFacturation = "";
    private String libelleJournal = "";
    private String libellePassageFacturation = "";

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected void _validate() throws Exception {
        // init le job queue du process
        super._validate();
    }

    @Override
    protected String getEMailObject() {
        return "Comptabilisation CI des quittances PCG";
    }

    public String getIdPassageFacturation() {
        return idPassageFacturation;
    }

    public String getLibelleJournal() {
        return libelleJournal;
    }

    public String getLibellePassageFacturation() {
        return libellePassageFacturation;
    }

    public void setIdPassageFacturation(String idPassageFacturation) {
        this.idPassageFacturation = idPassageFacturation;
    }

    public void setLibelleJournal(String libelleJournal) {
        this.libelleJournal = libelleJournal;
    }

    public void setLibellePassageFacturation(String libellePassageFacturation) {
        this.libellePassageFacturation = libellePassageFacturation;
    }
}
