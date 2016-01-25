package ch.globaz.vulpecula.business.models.traduction;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * @author Arnaud Geiser (AGE) | Créé le 31 janv. 2014
 * 
 */
public class TraductionSimpleModel extends JadeSimpleModel {

    private String id;
    private String codeIsoLangue;
    private String libelle;
    private String libelleUpper;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getCodeIsoLangue() {
        return codeIsoLangue;
    }

    public void setCodeIsoLangue(String codeIsoLangue) {
        this.codeIsoLangue = codeIsoLangue;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelleUpper() {
        return libelleUpper;
    }

    public void setLibelleUpper(String libelleUpper) {
        this.libelleUpper = libelleUpper;
    }
}
