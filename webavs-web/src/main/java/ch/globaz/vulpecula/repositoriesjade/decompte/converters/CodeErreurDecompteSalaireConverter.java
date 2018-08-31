package ch.globaz.vulpecula.repositoriesjade.decompte.converters;

import globaz.jade.client.util.JadeStringUtil;

import java.util.ArrayList;
import java.util.List;

import ch.globaz.vulpecula.business.models.decomptes.CodeErreurDecompteSalaireComplexModel;
import ch.globaz.vulpecula.business.models.decomptes.CodeErreurDecompteSalaireSimpleModel;
import ch.globaz.vulpecula.domain.models.decompte.CodeErreur;
import ch.globaz.vulpecula.domain.models.decompte.CodeErreurDecompteSalaire;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;

/**
 * Convertisseur d'objet {@link CodeErreurDecompteSalaire} <--> {@link CodeErreurDecompteSalaireComplexModel}
 * 
 * 
 */
public final class CodeErreurDecompteSalaireConverter {

    public static CodeErreurDecompteSalaireSimpleModel convertToPersistence(CodeErreurDecompteSalaire codeErreur, DecompteSalaire decompteSalaire) {
    	CodeErreurDecompteSalaireSimpleModel simpleModel = new CodeErreurDecompteSalaireSimpleModel();
        simpleModel.setId(codeErreur.getId());
        simpleModel.setIdLigneDecompte(decompteSalaire.getId());
        simpleModel.setCodeErreur(codeErreur.getCodeErreurAsValue());
        simpleModel.setSpy(codeErreur.getSpy());
        return simpleModel;
    }

    public static List<CodeErreurDecompteSalaire> convertToDomain(List<CodeErreurDecompteSalaireComplexModel> listComplexModel,
            DecompteSalaire decompteSalaire) {
        List<CodeErreurDecompteSalaire> list = new ArrayList<CodeErreurDecompteSalaire>();
        for (CodeErreurDecompteSalaireComplexModel complexModel : listComplexModel) {
        	CodeErreurDecompteSalaire codeErreur = convertToDomain(complexModel, decompteSalaire);
            list.add(codeErreur);
        }
        return list;
    }

    public static CodeErreurDecompteSalaire convertToDomain(CodeErreurDecompteSalaireComplexModel complexModel, DecompteSalaire decompteSalaire) {
    	CodeErreurDecompteSalaireSimpleModel simpleModel = complexModel.getCodeErreurDecompteSalaireSimpleModel();

    	CodeErreurDecompteSalaire codeErreur = convertToDomain(simpleModel);

        return codeErreur;
    }

    public static CodeErreurDecompteSalaire convertToDomain(CodeErreurDecompteSalaireSimpleModel simpleModel) {
        if (JadeStringUtil.isEmpty(simpleModel.getId())) {
            return null;
        }
        CodeErreurDecompteSalaire codeErreur = new CodeErreurDecompteSalaire();
        codeErreur.setId(simpleModel.getId());
        codeErreur.setCodeErreur(CodeErreur.fromValue(simpleModel.getCodeErreur()));
        codeErreur.setSpy(simpleModel.getSpy());
        return codeErreur;
    }
}
