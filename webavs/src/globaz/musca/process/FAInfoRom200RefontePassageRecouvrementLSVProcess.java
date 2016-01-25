package globaz.musca.process;

import globaz.pyxis.api.osiris.TITiersOSI;

/**
 * @author: MMO 11.10.2010
 */
public class FAInfoRom200RefontePassageRecouvrementLSVProcess extends FAPassageRemboursementProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected String giveDomaineForAdressePaiement() {
        return TITiersOSI.DOMAINE_RECOUVREMENT;
    }

}
