package globaz.corvus.vb.lotdeblocage;

import globaz.corvus.db.deblocage.REDeblocageVersementManager;
import globaz.globall.db.BEntity;

public class RELotDeblocageListViewBean extends REDeblocageVersementManager {

    private static final long serialVersionUID = 1L;
    private String forCsSexe = "";
    private String forCsTypeDecision = "";
    private String forDateNaissance = "";
    private String likeNom = "";
    private String likeNumeroAVS = "";
    private String likePrenom = "";

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

    @Override
    protected BEntity _newEntity() throws Exception {
        return new RELotDeblocageViewBean();
    }
}
