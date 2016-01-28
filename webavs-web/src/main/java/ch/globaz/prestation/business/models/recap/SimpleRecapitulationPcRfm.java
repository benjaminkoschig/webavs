package ch.globaz.prestation.business.models.recap;

import globaz.jade.persistence.model.JadeSimpleModel;
import ch.globaz.prestation.business.constantes.CodeRecapitulationPcRfm;

public class SimpleRecapitulationPcRfm extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private CodeRecapitulationPcRfm code;
    private String id;
    private String mois;
    private String valeur;

    public SimpleRecapitulationPcRfm() {
        super();

        code = null;
        id = "";
        mois = "";
        valeur = "";
    }

    public CodeRecapitulationPcRfm getCode() {
        return code;
    }

    @Override
    public String getId() {
        return id;
    }

    public String getMois() {
        return mois;
    }

    public String getValeur() {
        return valeur;
    }

    public void setCode(CodeRecapitulationPcRfm code) {
        this.code = code;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public void setMois(String mois) {
        this.mois = mois;
    }

    public void setValeur(String valeur) {
        this.valeur = valeur;
    }

}
