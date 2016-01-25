package globaz.corvus.utils.pmt.mensuel;

import globaz.framework.util.FWCurrency;

/**
 * 
 * @author SCR
 * 
 */

public class RECumulPrstParRubrique {

    public static final String RUBRIQUE_FICTIVE_OV_PMT_BLOCAGE_RETENUE = "-1";
    public static final String TYPE_BLOCAGE_RETENUE = "blk";

    public static final String TYPE_STANDARD = "std";

    private String idRubrique = "";
    private FWCurrency montant = new FWCurrency(0);
    private String type = "0";

    public void addMontant(FWCurrency mnt) {
        montant.add(mnt);
    }

    public String getIdRubrique() {
        return idRubrique;
    }

    public Integer getKey() {
        return new Integer(hashCode());
    }

    public FWCurrency getMontant() {
        return montant;
    }

    public String getType() {
        return type;
    }

    @Override
    public int hashCode() {
        return (type + idRubrique).hashCode();
    }

    public void setIdRubrique(String idRubrique) {
        this.idRubrique = idRubrique;
    }

    public void setMontant(FWCurrency montant) {
        this.montant = montant;
    }

    public void setType(String type) {
        this.type = type;
    }

}
