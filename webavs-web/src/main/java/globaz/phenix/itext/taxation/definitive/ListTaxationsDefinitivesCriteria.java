package globaz.phenix.itext.taxation.definitive;

import java.io.Serializable;

public class ListTaxationsDefinitivesCriteria implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String noPassage;

    public String getNoPassage() {
        return noPassage;
    }

    public void setNoPassage(String noPassage) {
        this.noPassage = noPassage;
    }

    @Override
    public String toString() {
        return "ListTaxationsDefinitivesCriteria [noPassage=" + noPassage + "]";
    }

}
