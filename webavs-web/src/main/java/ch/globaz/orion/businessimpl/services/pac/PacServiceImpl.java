package ch.globaz.orion.businessimpl.services.pac;

import globaz.globall.db.BSession;
import java.util.List;
import ch.globaz.orion.businessimpl.services.ServicesProviders;
import ch.globaz.xmlns.eb.pac.EBPacException_Exception;
import ch.globaz.xmlns.eb.pac.MassesMensuellesPourAVS;
import ch.globaz.xmlns.eb.pac.StatusSaisiePAC;

public class PacServiceImpl {

    public static void changeStatus(int idPac, StatusSaisiePAC status, BSession session)
            throws EBPacException_Exception {
        ServicesProviders.pacServiceProvide(session).updateStatusForSaisieForWebAVS(idPac, status);
    }

    public static List<MassesMensuellesPourAVS> listPacSaisies(BSession session) throws EBPacException_Exception {
        return ServicesProviders.pacServiceProvide(session).listerNouvellesMassesSaisiesForAVS();
    }

}
