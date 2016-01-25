package globaz.apg.pojo;

/**
 * Cette classe représente une breakRule à ajouter à une annonce. Cette breakRule est reçu depuis l'écran de validation
 * des prestations
 * 
 * @author lga
 */
public class APBreakRulesFromView {

    private int code;
    private String idPrestation;
    private boolean isGlobazBreakRule;

    public APBreakRulesFromView() {
        code = 0;
        idPrestation = "0";
    }

    public APBreakRulesFromView(int code, boolean isGlobazBreakRule) {
        this.code = code;
        this.isGlobazBreakRule = isGlobazBreakRule;
    }

    public APBreakRulesFromView(String idPrestation, int code) {
        this.idPrestation = idPrestation;
        this.code = code;
    }

    public final int getCode() {
        return code;
    }

    public final String getCodeAsString() {
        return String.valueOf(code);
    }

    public final void setCode(int code) {
        this.code = code;
    }

    public final String getIdPrestation() {
        return idPrestation;
    }

    public final void setIdPrestation(String idPrestation) {
        this.idPrestation = idPrestation;
    }

    public boolean isGlobazBreakRule() {
        return isGlobazBreakRule;
    }

    public void setGlobazBreakRule(boolean isGlobazBreakRule) {
        this.isGlobazBreakRule = isGlobazBreakRule;
    }
}
