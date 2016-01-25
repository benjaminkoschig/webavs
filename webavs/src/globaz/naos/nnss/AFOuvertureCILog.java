package globaz.naos.nnss;

public class AFOuvertureCILog {

    private String AffilieNumero;
    private String Annonce = null;
    private String IdTiers;
    private String Msg;
    private String NumAVS;

    public AFOuvertureCILog() {

    }

    public AFOuvertureCILog(String a, String b, String d, String c) {
        setIdTiers(a);
        setAffilieNumero(b);
        setMsg(c);
        setNumAVS(d);
    }

    public String getAffilieNumero() {
        return AffilieNumero;
    }

    public String getAnnonce() {
        return Annonce;
    }

    public String getIdTiers() {
        return IdTiers;
    }

    public String getMsg() {
        return Msg;
    }

    public String getNumAVS() {
        return NumAVS;
    }

    public void setAffilieNumero(String affilieNumero) {
        AffilieNumero = affilieNumero;
    }

    public void setAnnonce(String annonce) {
        Annonce = annonce;
    }

    public void setIdTiers(String idTiers) {
        IdTiers = idTiers;
    }

    public void setMsg(String msg) {
        Msg = msg;
    }

    public void setNumAVS(String numAVS) {
        NumAVS = numAVS;
    }

    public void setParam(String tiers, String affilie, String avs, String mgs) {

        setIdTiers(tiers);
        setAffilieNumero(affilie);
        setMsg(mgs);
        setNumAVS(avs);

    }

}
