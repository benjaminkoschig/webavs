package ch.globaz.vulpecula.business.models.decomptes;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * @author Arnaud Geiser (AGE) | Cr�� le 20 f�vr. 2014
 * 
 */
public class AbsenceSimpleModel extends JadeSimpleModel {
    private static final long serialVersionUID = -9144176992675015596L;
    private String id;
    private String idLigneDecompte;
    private String type;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIdLigneDecompte() {
        return idLigneDecompte;
    }

    public void setIdLigneDecompte(String idLigneDecompte) {
        this.idLigneDecompte = idLigneDecompte;
    }
}
