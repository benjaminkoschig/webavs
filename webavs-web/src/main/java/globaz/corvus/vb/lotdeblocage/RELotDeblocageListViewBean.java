package globaz.corvus.vb.lotdeblocage;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

public class RELotDeblocageListViewBean extends BManager {

    private static final long serialVersionUID = 1L;
    private String forCsSexe = "";
    private String forCsTypeDecision = "";
    private String forDateNaissance = "";
    private String forIdLot = "";
    private String likeNom = "";
    private String likeNumeroAVS = "";
    // private String likeNumeroAVSNNSS = "";
    private String likePrenom = "";

    @Override
    protected String _getSql(BStatement statement) {
        String schema = _getCollection();

        StringBuilder str = new StringBuilder();

        str.append("SELECT ");
        str.append("ld.ID_RENTE_PRESTATION ");
        str.append("FROM " + schema + "RE_LIGNE_DEBLOCAGE ld ");
        str.append("join " + schema + "REPRACC reac on reac.ZTIPRA = ld.ID_RENTE_PRESTATION ");

        return str.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new RELotDeblocageViewBean();
    }

    public String getForCsSexe() {
        return forCsSexe;
    }

    public void setForCsSexe(String forCsSexe) {
        this.forCsSexe = forCsSexe;
    }

    public String getForCsTypeDecision() {
        return forCsTypeDecision;
    }

    public void setForCsTypeDecision(String forCsTypeDecision) {
        this.forCsTypeDecision = forCsTypeDecision;
    }

    public String getForDateNaissance() {
        return forDateNaissance;
    }

    public void setForDateNaissance(String forDateNaissance) {
        this.forDateNaissance = forDateNaissance;
    }

    public String getForIdLot() {
        return forIdLot;
    }

    public void setForIdLot(String forIdLot) {
        this.forIdLot = forIdLot;
    }

    public String getLikeNom() {
        return likeNom;
    }

    public void setLikeNom(String likeNom) {
        this.likeNom = likeNom;
    }

    public String getLikeNumeroAVS() {
        return likeNumeroAVS;
    }

    public void setLikeNumeroAVS(String likeNumeroAVS) {
        this.likeNumeroAVS = likeNumeroAVS;
    }

    public String getLikePrenom() {
        return likePrenom;
    }

    public void setLikePrenom(String likePrenom) {
        this.likePrenom = likePrenom;
    }
}
