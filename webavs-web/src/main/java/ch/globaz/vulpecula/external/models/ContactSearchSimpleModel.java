package ch.globaz.vulpecula.external.models;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class ContactSearchSimpleModel extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = -4526421796189673418L;
    private String forId;
    private String forPrenom;
    private String forNom;

    public String getForId() {
        return forId;
    }

    public void setForId(String forId) {
        this.forId = forId;
    }

    public String getForPrenom() {
        return forPrenom;
    }

    public void setForPrenom(String forPrenom) {
        this.forPrenom = forPrenom;
    }

    public String getForNom() {
        return forNom;
    }

    public void setForNom(String forNom) {
        this.forNom = forNom;
    }

    @Override
    public Class<ContactSimpleModel> whichModelClass() {
        return ContactSimpleModel.class;
    }
}
