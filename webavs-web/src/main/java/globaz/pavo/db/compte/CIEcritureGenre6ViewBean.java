package globaz.pavo.db.compte;

import globaz.commons.nss.NSUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pavo.vb.CIAbstractPersistentViewBean;

public class CIEcritureGenre6ViewBean extends CIAbstractPersistentViewBean {

    private String avs = null;
    private CICompteIndividuel ci = null;
    private CICompteIndividuel ciDest = null;
    private CIEcriture ec = null;

    @Override
    public void add() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void delete() throws Exception {
        // TODO Auto-generated method stub

    }

    public String getAvs() {
        return avs;
    }

    public String getNomPrenom() {
        return giveCI().getNomPrenom();
    }

    public String getNomPrenomDest() {
        String nomPrenomRetour = "";
        try {
            if (!JadeStringUtil.isBlankOrZero(avs)) {
                nomPrenomRetour = giveCIDest().getNomPrenom();
            }

        } catch (Exception e) {
            // Volontaire ne rien faire, ci ci pas trouver ou pb, on affiche vid à l'écrane
        }
        return nomPrenomRetour;
    }

    public String getNss() {
        return NSUtil.formatAVSUnknown(giveCI().getNumeroAvs());

    }

    public CICompteIndividuel giveCI() {
        try {
            if (ci == null) {
                CIEcriture ec = new CIEcriture();
                ec.setEcritureId(getId());
                ec.setSession(getSession());
                ec.retrieve();
                if (!ec.isNew()) {
                    ci = new CICompteIndividuel();
                    ci.setSession(getSession());
                    ci.setCompteIndividuelId(ec.getCompteIndividuelId());
                    ci.retrieve();
                }
            }
        } catch (Exception e) {
            ci = null;
        }
        return ci;

    }

    public CICompteIndividuel giveCIDest() throws Exception {
        if ((ciDest == null) && !JadeStringUtil.isBlankOrZero(avs)) {
            CICompteIndividuelManager ciMgr = new CICompteIndividuelManager();
            ciMgr.setSession(getSession());
            ciMgr.setForRegistre(CICompteIndividuel.CS_REGISTRE_ASSURES);
            ciMgr.setForNumeroAvs(NSUtil.unFormatAVS(avs));
            ciMgr.find();
            if (ciMgr.size() > 0) {
                ciDest = (CICompteIndividuel) ciMgr.getFirstEntity();
            }
        }
        return ciDest;
    }

    public CIEcriture giveEcriture() {
        try {
            if (ec == null) {
                ec = new CIEcriture();
                ec.setSession(getSession());
                ec.setEcritureId(getId());
                ec.retrieve();
            } else {

            }
        } catch (Exception e) {
            ec = null;
        }
        return ec;
    }

    @Override
    public void retrieve() throws Exception {
        // TODO Auto-generated method stub

    }

    public void setAvs(String avs) {
        this.avs = avs;
    }

    @Override
    public void update() throws Exception {
        // TODO Auto-generated method stub

    }

}
