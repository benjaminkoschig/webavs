/*
 * Créé le 10 nov. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.musca.api.musca;

import globaz.globall.db.BProcess;
import globaz.musca.api.IFAPassage;
import globaz.musca.process.FAInfoRom200RefontePassageRecouvrementLSVProcess;

/**
 * @author MMO 12.10.2010
 */
public class FAInfoRom200RefonteRecouvrementLSVImpl extends FARembourserImpl {

    /**
	 *   
	 */
    public FAInfoRom200RefonteRecouvrementLSVImpl() {
        super();
    }

    /**
     * /**
     * 
     * @see globaz.musca.external.IntModuleFacturation#generer(globaz.musca.api.IFAPassage,
     *      globaz.framework.process.BProcess)
     */
    @Override
    public boolean generer(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {
        FAInfoRom200RefontePassageRecouvrementLSVProcess cptProc = new FAInfoRom200RefontePassageRecouvrementLSVProcess();
        // copier le process parent
        cptProc.setParentWithCopy(context);

        cptProc.setIdPassage(passage.getIdPassage());
        cptProc.setEMailAddress(context.getEMailAddress());
        return cptProc._executeRemboursementProcess(passage);
    }

}
