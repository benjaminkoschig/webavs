package globaz.orion.vb.recap;

import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.orion.vb.EBAbstractViewBean;
import ch.globaz.orion.business.models.af.LigneRecapAfEnrichie;
import ch.globaz.orion.business.models.af.RecapAfAndLignes;
import ch.globaz.orion.businessimpl.services.af.AfServiceImpl;
import com.google.gson.Gson;

public class EBRecapAfViewBean extends EBAbstractViewBean {

    private String id = null;
    private RecapAfAndLignes recapAfAndLignes;

    private Integer idSelectedLine;
    private LigneRecapAfEnrichie ligneRecapAfSelected;

    @Override
    public void retrieve() throws Exception {
        recapAfAndLignes = AfServiceImpl.readRecapAfAndLignes(BSessionUtil.getSessionFromThreadContext(),
                Integer.valueOf(id));

        ligneRecapAfSelected = recapAfAndLignes.getListLignesRecapAf().get(0);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public RecapAfAndLignes getRecapAfAndLignes() {
        return recapAfAndLignes;
    }

    public void setRecapAfAndLignes(RecapAfAndLignes recapAfAndLignes) {
        this.recapAfAndLignes = recapAfAndLignes;
    }

    public LigneRecapAfEnrichie getLigneRecapAfSelected() {
        return ligneRecapAfSelected;
    }

    public void setLigneRecapAfSelected(LigneRecapAfEnrichie ligneRecapAfSelected) {
        this.ligneRecapAfSelected = ligneRecapAfSelected;
    }

    public Integer getIdSelectedLine() {
        return idSelectedLine;
    }

    public void setIdSelectedLine(Integer idSelectedLine) {
        this.idSelectedLine = idSelectedLine;
    }

    public void loadSelectedLine() {
        if (idSelectedLine != 0) {
            ligneRecapAfSelected = recapAfAndLignes.getLigneForId(idSelectedLine);
        }
    }

    public String getLignesRecapAfAsJson() {
        String result = "";

        Gson gson = new Gson();
        result = gson.toJson(recapAfAndLignes.getListLignesRecapAf());

        if (JadeStringUtil.isEmpty(result)) {
            result = "[]";
        }

        return result;

    }

    public String getSpy() {
        return null;
    }

}
