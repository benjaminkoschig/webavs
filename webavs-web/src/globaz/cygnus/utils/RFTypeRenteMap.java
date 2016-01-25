package globaz.cygnus.utils;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCRenteAvsAi;

/**
 * 
 * @author JJE
 * 
 *         TODO: A completer
 */
public final class RFTypeRenteMap {

    public final static List<String> listeCsRenteComplementaireConjointAIAV = new ArrayList<String>() {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        {
            this.add(IPCRenteAvsAi.CS_TYPE_RENTE_50);
        }
    };

    public final static List<String> listeCsRenteComplementaireEnfantAIAV = new ArrayList<String>() {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        {
            this.add(IPCRenteAvsAi.CS_TYPE_RENTE_50);

        }
    };

    public final static List<String> listeCsRentePrincipaleAIAV = new ArrayList<String>() {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        {
            this.add(IPCRenteAvsAi.CS_TYPE_RENTE_50);
        }
    };

}
