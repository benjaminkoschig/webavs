package globaz.draco.db.declaration;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class DSStructureSyncroAgrivit implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String ac2 = "";
    private Map afParCanton = new HashMap();
    private String catPersonnel = "";
    private String montantAc = "";
    private String montantAvs = "";

    public String getAc2() {
        return ac2;
    }

    public Map getAfParCanton() {
        return afParCanton;
    }

    public String getCatPersonnel() {
        return catPersonnel;
    }

    /**
     * Permet de récupérer la masse af exclue pour un caton
     * 
     * @param le
     *            canton concaténer à l'année séparé par un "/"
     * @return la masse pour le canton
     */
    public String getMasseAfParCanton(String canton) {
        if (afParCanton.containsKey(canton)) {
            return (String) afParCanton.get(canton);
        }
        return "0";
    }

    public String getMontantAc() {
        return montantAc;
    }

    public String getMontantAvs() {
        return montantAvs;
    }

    public void setAc2(String ac2) {
        this.ac2 = ac2;
    }

    public void setAfParCanton(Map afParCanton) {
        this.afParCanton = afParCanton;
    }

    public void setCatPersonnel(String catPersonnel) {
        this.catPersonnel = catPersonnel;
    }

    public void setMontantAc(String montantAc) {
        this.montantAc = montantAc;
    }

    public void setMontantAvs(String montantAvs) {
        this.montantAvs = montantAvs;
    }

    /**
     * Permet de setter le total exclu par canton pour l'af
     * 
     * @param canton
     * @param montant
     */
    public void setMontantParCanton(String canton, String montant) {
        afParCanton.put(canton, montant);
    }

}
