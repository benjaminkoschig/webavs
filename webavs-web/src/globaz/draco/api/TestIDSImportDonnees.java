package globaz.draco.api;

import globaz.globall.db.BSession;
import globaz.jade.api.client.JadeApiClientFacade;
import globaz.jade.common.JadeCodingUtil;
import java.util.HashMap;

public class TestIDSImportDonnees {

    public void checkAnnuleValidation(BSession session, String adapterName, String idDeclaration) throws Exception {
        IDSImportDonnees api = null;
        try {
            api = (IDSImportDonnees) JadeApiClientFacade.openExternalApiSession(adapterName, IDSImportDonnees.class,
                    "DRACO");
            api.checkAnnuleValidation(idDeclaration);
            JadeApiClientFacade.closeExternalApiSession(api);
        } catch (Exception e) {
            try {
                JadeApiClientFacade.closeExternalApiSession(api);
            } catch (Exception ex) {
                JadeCodingUtil.catchException(this, "checkAnnuleValidation", ex);
            }
            throw e;
        }

    }

    /**
     * Executes the program.
     * 
     * @param args
     *            the command line arguments
     */
    public void importDonnees(BSession session, String adapterName, String noDecompte, String affiliationId,
            String idDeclaration, String dateReception, String anneeMin, String anneeMax, String typeDeclaration,
            HashMap masseTotale, HashMap masseAc, HashMap masseAc2, HashMap donneeAssures, HashMap afParCanton)
            throws Exception {
        IDSImportDonnees api = null;
        try {
            api = (IDSImportDonnees) JadeApiClientFacade.openExternalApiSession(adapterName, IDSImportDonnees.class,
                    "DRACO");
            api.importDonnees(noDecompte, affiliationId, idDeclaration, dateReception, anneeMin, anneeMax,
                    typeDeclaration, masseTotale, masseAc, masseAc2, donneeAssures, afParCanton);
            JadeApiClientFacade.closeExternalApiSession(api);
        } catch (Exception e) {
            try {
                JadeApiClientFacade.closeExternalApiSession(api);
            } catch (Exception ex) {
                JadeCodingUtil.catchException(this, "importDonnees", ex);
            }
            throw e;
        }
    }
}
