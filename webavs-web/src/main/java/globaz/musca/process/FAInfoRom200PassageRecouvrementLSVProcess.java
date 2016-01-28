package globaz.musca.process;

import globaz.osiris.db.comptes.CASection;

/**
 * @author: MMO 11.10.2010
 */
public class FAInfoRom200PassageRecouvrementLSVProcess extends FAInfoRom200RefontePassageRecouvrementLSVProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected boolean isSectionContentieux(CASection section) {
        return true;
    }

}
