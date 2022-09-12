package globaz.eform.vb.demande;

import ch.globaz.common.util.NSSUtils;
import ch.globaz.eform.business.models.GFDaDossierModel;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GFDemandeViewBean extends BJadePersistentObjectViewBean {
    private static final Logger LOG = LoggerFactory.getLogger(GFDemandeViewBean.class);

    GFDaDossierModel daDossier;

    public GFDemandeViewBean() {
        super();
        daDossier = new GFDaDossierModel();
    }

    public GFDemandeViewBean(GFDaDossierModel model) {
        super();
        this.daDossier = model;
    }

    public GFDaDossierModel getDaDossier() {
        return daDossier;
    }

    @Override
    public BSpy getSpy() {
        return null;
    }

    public String getNssAffilier() {
        return StringUtils.isBlank(daDossier.getNssAffilier()) ? daDossier.getNssAffilier() : NSSUtils.formatNss(daDossier.getNssAffilier());
    }

    public void setNssAffilier(String nssAffilier) {
        daDossier.setNssAffilier(StringUtils.isBlank(nssAffilier) ? nssAffilier : NSSUtils.unFormatNss(nssAffilier));
    }

    public String getCodeCaisse() {
        return daDossier.getCodeCaisse();
    }

    public void setCodeCaisse(String codeCaisse) {
        daDossier.setCodeCaisse(org.apache.commons.lang3.StringUtils.isBlank(codeCaisse) ? null : codeCaisse.split(" - ")[0]);
    }

    @Override
    public void add() throws Exception {
    }

    @Override
    public void delete() throws Exception {
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void retrieve() throws Exception {
    }

    @Override
    public void setId(String newId) {
    }

    @Override
    public void update() throws Exception {


    }

    public BSession getSession() {
        return (BSession) getISession();
    }
}
