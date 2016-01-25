package globaz.draco.api;

import java.util.HashMap;

public interface IDSImportDonnees {
    public void checkAnnuleValidation(String idDeclaration) throws Exception;

    /**
     * Importe les données de ...
     * 
     * @param affiliationId
     * @param annee
     * @param typeDeclaration
     * @param masseTotale
     * @param masseAc
     * @param masseAc2
     * @param donneeAssures
     * @param afParCanton
     * @exception Exception
     *                en cas d'erreur
     */
    public void importDonnees(String noDecompte, String affiliationId, String idDeclarationDistante,
            String dateReception, String anneeMin, String anneeMax, String typeDeclaration, HashMap masseTotale,
            HashMap masseAc, HashMap masseAc2, HashMap donneeAssures, HashMap afParCanton) throws Exception;
}
