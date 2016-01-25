package globaz.draco.api.remote;

import globaz.draco.api.IDSImportDonnees;
import globaz.draco.process.DSCheckAnnuleValidation;
import globaz.draco.process.DSImportDonneesValidationProcess;
import java.util.HashMap;

public class IDSImportDonneesImpl implements IDSImportDonnees {

    @Override
    public void checkAnnuleValidation(String idDeclaration) throws Exception {
        DSCheckAnnuleValidation process = new DSCheckAnnuleValidation();
        process.setIdDeclarationDistante(idDeclaration);
        process.executeProcess();
        process.throwErrors();
    }

    @Override
    public void importDonnees(String noDecompte, String affiliationId, String idDeclarationDistante,
            String dateReception, String anneeMin, String anneeMax, String typeDeclaration, HashMap masseTotale,
            HashMap masseAc, HashMap masseAc2, HashMap donneeAssures, HashMap afParCanton) throws Exception {
        DSImportDonneesValidationProcess process = new DSImportDonneesValidationProcess();
        process.setAffiliationId(affiliationId);
        process.setAnneeMaxStr(anneeMax);
        process.setAnneeMinStr(anneeMin);
        process.setMasseAc(masseAc);
        process.setMasseAc2(masseAc2);
        process.setMasseTotale(masseTotale);
        process.setIdDeclarationDistante(idDeclarationDistante);
        process.setDateReception(dateReception);
        process.setTypeDeclaration(typeDeclaration);
        process.setDonneeAssures(donneeAssures);
        process.setAfParCanton(afParCanton);
        process.setNoDecompte(noDecompte);
        process.executeProcess();
        process.throwErrors();
    }

}