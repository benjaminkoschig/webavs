package globaz.draco.api.client;

import globaz.draco.api.IDSImportDonnees;
import globaz.jade.api.client.JadeApiClientFacade;
import java.io.Serializable;
import java.util.HashMap;

public class IDSImportDonneesImpl implements IDSImportDonnees {

    @Override
    public void checkAnnuleValidation(String idDeclaration) throws Exception {
        JadeApiClientFacade.call(this, "checkAnnuleValidation", new Class[] { String.class },
                new Serializable[] { idDeclaration });
    }

    @Override
    public void importDonnees(String noDecompte, String affiliationId, String idDeclarationDistante,
            String dateReception, String anneeMin, String anneeMax, String typeDeclaration, HashMap masseTotale,
            HashMap masseAc, HashMap masseAc2, HashMap donneeAssures, HashMap afParCanton) throws Exception {
        JadeApiClientFacade.call(this, "importDonnees", new Class[] { String.class, String.class, String.class,
                String.class, String.class, String.class, String.class, HashMap.class, HashMap.class, HashMap.class,
                HashMap.class, HashMap.class }, new Serializable[] { noDecompte, affiliationId, idDeclarationDistante,
                dateReception, anneeMin, anneeMax, typeDeclaration, masseTotale, masseAc, masseAc2, donneeAssures,
                afParCanton });
    }
}