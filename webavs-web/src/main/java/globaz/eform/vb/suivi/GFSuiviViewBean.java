package globaz.eform.vb.suivi;

import ch.globaz.common.util.NSSUtils;
import ch.globaz.eform.business.models.GFDaDossierModel;
import ch.globaz.eform.business.models.GFFormulaireModel;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GFSuiviViewBean extends BJadePersistentObjectViewBean {
    private static final Logger LOG = LoggerFactory.getLogger(GFSuiviViewBean.class);

    GFDaDossierModel daDossier;

    public GFSuiviViewBean() {
        super();
        daDossier = new GFDaDossierModel();
    }

    public GFSuiviViewBean(GFDaDossierModel daDossier) {
        super();
        this.daDossier = daDossier;
    }

    @Override
    public String getId() {
        return daDossier.getId();
    }

    @Override
    public void setId(String newId) {

    }

    public GFDaDossierModel getDaDossier() {
        return daDossier;
    }

    public String getLikeNss() {
        return StringUtils.isBlank(daDossier.getNssAffilier()) ? daDossier.getNssAffilier() : NSSUtils.formatNss(daDossier.getNssAffilier());
    }

    public void setLikeNss(String likeNss) {
        if (StringUtils.isBlank(likeNss) || NSSUtils.checkNSS(likeNss)) {
            daDossier.setNssAffilier(likeNss);
        } else {
            daDossier.setNssAffilier(NSSUtils.unFormatNss(likeNss));
        }
    }

    public String getByCaisse() {
        return daDossier.getCodeCaisse();
    }

    public void setByCaisse(String codeCaisse) {
        daDossier.setCodeCaisse(codeCaisse);
    }

    public String getByType() {
        return daDossier.getType();
    }

    public void setByType(String type) {
        daDossier.setType(type);
    }

    public String getByStatus() {
        return daDossier.getStatus();
    }

    public void setByStatus(String status) {
        daDossier.setStatus(status);
    }

    @Override
    public BSpy getSpy() {
        return (daDossier != null) && !daDossier.isNew() ? new BSpy(daDossier.getSpy()) : new BSpy(getSession());
    }

    public BSession getSession() {
        return (BSession) getISession();
    }

    @Override
    public void add() throws Exception {

    }

    @Override
    public void delete() throws Exception {

    }

    @Override
    public void retrieve() throws Exception {

    }

    @Override
    public void update() throws Exception {

    }
}
