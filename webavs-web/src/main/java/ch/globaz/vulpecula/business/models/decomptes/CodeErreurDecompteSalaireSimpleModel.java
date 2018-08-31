package ch.globaz.vulpecula.business.models.decomptes;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * 
 */
public class CodeErreurDecompteSalaireSimpleModel extends JadeSimpleModel {
	private static final long serialVersionUID = -3002595130016493817L;
	private String id;
    private String idLigneDecompte;
    private String codeErreur;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getCodeErreur() {
        return codeErreur;
    }

    public void setCodeErreur(String codeErreur) {
        this.codeErreur = codeErreur;
    }

    public String getIdLigneDecompte() {
        return idLigneDecompte;
    }

    public void setIdLigneDecompte(String idLigneDecompte) {
        this.idLigneDecompte = idLigneDecompte;
    }
}
