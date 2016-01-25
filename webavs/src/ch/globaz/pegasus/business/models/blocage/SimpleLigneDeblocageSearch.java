package ch.globaz.pegasus.business.models.blocage;

import globaz.jade.persistence.model.JadeSearchSimpleModel;
import java.util.List;

public class SimpleLigneDeblocageSearch extends JadeSearchSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static String WITH_IDPCA_NOT_EQUALS = "withIdPcaNotEqual";

    private String forCsEtat;
    private String forCsTypeDeblocage;
    private String forIdPca;
    private String forIdSectionDetteEnCompta;
    private List<String> forInIdPrestation = null;

    public List<String> getForInIdPrestation() {
        return forInIdPrestation;
    }

    public void setForInIdPrestation(List<String> forInIdPrestation) {
        this.forInIdPrestation = forInIdPrestation;
    }

    public String getForCsEtat() {
        return forCsEtat;
    }

    public String getForCsTypeDeblocage() {
        return forCsTypeDeblocage;
    }

    public String getForIdPca() {
        return forIdPca;
    }

    public String getForIdSectionDetteEnCompta() {
        return forIdSectionDetteEnCompta;
    }

    public void setForCsEtat(String forCsEtat) {
        this.forCsEtat = forCsEtat;
    }

    public void setForCsTypeDeblocage(String forCsTypeDeblocage) {
        this.forCsTypeDeblocage = forCsTypeDeblocage;
    }

    public void setForIdPca(String forIdPca) {
        this.forIdPca = forIdPca;
    }

    public void setForIdSectionDetteEnCompta(String forIdSectionDetteEnCompta) {
        this.forIdSectionDetteEnCompta = forIdSectionDetteEnCompta;
    }

    @Override
    public Class<SimpleLigneDeblocage> whichModelClass() {
        return SimpleLigneDeblocage.class;
    }

}
