package globaz.pavo.db.bta;

import globaz.commons.nss.NSUtil;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BManager;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pavo.db.compte.CICompteIndividuel;
import globaz.pavo.db.compte.CICompteIndividuelManager;
import globaz.pyxis.db.tiers.TIPersonneAvsManager;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.util.Vector;

public class CIRequerantBtaViewBean extends CIRequerantBta implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private TITiersViewBean tiersViewBean = null;

    public String getCompteIndividuel() {
        String idCompteInd = "";
        CICompteIndividuelManager manager = new CICompteIndividuelManager();
        manager.setSession(getSession());
        manager.setForNumeroAvs(NSUtil.unFormatAVS(getTiersViewBeanForDisplay().getNumAvsActuel()));
        manager.setForRegistre(CICompteIndividuel.CS_REGISTRE_ASSURES);// pour
        // ne
        // prendre
        // que
        // les
        // CI
        // ouverts
        try {
            if (!JadeStringUtil.isBlankOrZero(getTiersViewBeanForDisplay().getNumAvsActuel())) {
                manager.find();
            }
            if (manager.size() > 0) {
                CICompteIndividuel compteInd = (CICompteIndividuel) manager.getFirstEntity();
                idCompteInd = compteInd.getCompteIndividuelId();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return idCompteInd;
    }

    public Vector getListRequerantDossier() {
        Vector vList = new Vector();
        String[] list = new String[2];
        try {
            CIRequerantBtaManager manager = new CIRequerantBtaManager();
            manager.setSession(getSession());
            manager.setForIdDossierBta(getIdDossierBta());
            manager.find(BManager.SIZE_NOLIMIT);
            vList = new Vector(manager.size());
            // ajout d'un blanc
            list[0] = "";
            list[1] = "";
            vList.add(list);
            for (int i = 0; i < manager.size(); i++) {
                list = new String[2];
                CIRequerantBta entity = (CIRequerantBta) manager.getEntity(i);
                list[0] = entity.getIdRequerant();
                list[1] = entity.getNomRequerant() + " " + entity.getPrenomRequerant();

                // ajoute le requerant que si c'est pas le requerant en cours de
                // saisie
                if (!entity.getIdRequerant().equals(getIdRequerant())) {
                    vList.add(list);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return vList;
    }

    public TITiersViewBean getTiersViewBeanForDisplay() {
        if (tiersViewBean == null && !JadeStringUtil.isBlank(getIdTiersRequerant())) {
            TIPersonneAvsManager personneAvsManager = new TIPersonneAvsManager();
            personneAvsManager.setSession(getSession());
            personneAvsManager.setForIdTiers(getIdTiersRequerant());
            try {
                personneAvsManager.find();
                if (!personneAvsManager.isEmpty()) {
                    tiersViewBean = (TITiersViewBean) personneAvsManager.getFirstEntity();
                }
            } catch (Exception e) {
                _addError(null, e.getMessage());
            }
        } else {
            if (tiersViewBean != null && !tiersViewBean.getIdTiers().equals(getIdTiersRequerant())) {
                TIPersonneAvsManager personneAvsManager = new TIPersonneAvsManager();
                personneAvsManager.setSession(getSession());
                personneAvsManager.setForIdTiers(getIdTiersRequerant());
                try {
                    personneAvsManager.find();
                    if (!personneAvsManager.isEmpty()) {
                        tiersViewBean = (TITiersViewBean) personneAvsManager.getFirstEntity();
                    }
                } catch (Exception e) {
                    _addError(null, e.getMessage());
                }
            }
        }

        return tiersViewBean;
    }

}
