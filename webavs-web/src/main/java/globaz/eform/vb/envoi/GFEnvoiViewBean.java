package globaz.eform.vb.envoi;

import ch.globaz.common.exceptions.NotFoundException;
import ch.globaz.common.file.FileUtils;
import ch.globaz.eform.business.GFEFormServiceLocator;
import ch.globaz.eform.business.models.GFFormulaireModel;
import ch.globaz.eform.constant.GFTypeEForm;
import globaz.commons.nss.NSUtil;
import globaz.eform.vb.formulaire.GFFormulaireViewBean;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.pyxis.db.tiers.TIPersonneAvsManager;
import globaz.pyxis.db.tiers.TITiersViewBean;
import globaz.pyxis.util.CommonNSSFormater;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GFEnvoiViewBean extends BJadePersistentObjectViewBean {
    private static final Logger LOG = LoggerFactory.getLogger(GFEnvoiViewBean.class);

    private String id;

    private String nss ="";

    public GFEnvoiViewBean() {
        super();
    }

    @Override
    public BSpy getSpy() {
        return null;
    }


    @Override
    public void add() throws Exception {

    }

    @Override
    public void delete() throws Exception {

    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void retrieve() throws Exception {

    }

    @Override
    public void setId(String newId) {
        id = newId;
    }

    @Override
    public void update() throws Exception {

    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public BSession getSession() {
        return (BSession) getISession();
    }

    public String getNss() {
        return nss;
    }
    public String getNumeroAvsFormateSansPrefixe() {
        return NSUtil.formatWithoutPrefixe(getNss(), isNNSS().equals("true") ? true : false);
    }

    public final String isNNSS() {
        if (JadeStringUtil.isBlankOrZero(getNss())) {
            return "";
        }
        if (getNss().length() > 14) {
            return "true";
        } else {
            return "false";
        }
    }
}

