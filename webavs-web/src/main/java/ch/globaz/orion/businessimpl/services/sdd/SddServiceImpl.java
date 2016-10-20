package ch.globaz.orion.businessimpl.services.sdd;

import globaz.globall.db.BSession;
import java.util.List;
import ch.globaz.orion.businessimpl.services.ServicesProviders;
import ch.globaz.xmlns.eb.sdd.DecompteAndLignes;
import ch.globaz.xmlns.eb.sdd.DecompteStatutEnum;

public class SddServiceImpl {

    public static void changeStatus(int idSdd, DecompteStatutEnum status, BSession session) {
        ServicesProviders.sddServiceProvide(session).updateStatutDecompte(idSdd, status);
    }

    public static List<DecompteAndLignes> listSddSaisies(BSession session) {
        return ServicesProviders.sddServiceProvide(session).listerNouveauxDecomptesSaisis();
    }
}
