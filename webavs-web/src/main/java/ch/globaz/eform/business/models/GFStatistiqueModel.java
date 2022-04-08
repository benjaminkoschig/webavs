package ch.globaz.eform.business.models;

import ch.globaz.eform.constant.GFStatusEForm;
import ch.globaz.eform.constant.GFTypeEForm;
import globaz.jade.persistence.model.JadeSimpleModel;

import java.util.HashMap;
import java.util.Map;

public class GFStatistiqueModel extends JadeSimpleModel{
    private GFTypeEForm type;
    private Map<GFStatusEForm, Integer> recensement;

    public GFStatistiqueModel() {
        this.type = null;
        recensement = new HashMap<>();
    }

    public GFStatistiqueModel(GFTypeEForm type) {
        this();
        this.type = type;
    }

    @Override
    public String getId() {
        return getType().getCodeEForm();
    }

    @Override
    public void setId(String id) {
        setType(GFTypeEForm.getGFTypeEForm(id));
    }

    public GFTypeEForm getType() {
        return type;
    }

    public void setType(GFTypeEForm type) {
        this.type = type;
    }

    public Map<GFStatusEForm, Integer> getRecensement() {
        return recensement;
    }

    public void setRecensement(Map<GFStatusEForm, Integer> recensement) {
        this.recensement = recensement;
    }

    public void addStatus(GFStatusEForm status) {
        recensement.put(status, recensement.get(status) + 1);
    }
}
