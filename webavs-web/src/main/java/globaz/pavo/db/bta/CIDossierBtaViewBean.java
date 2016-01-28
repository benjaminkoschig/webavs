package globaz.pavo.db.bta;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BManager;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pyxis.db.tiers.TIPersonne;
import globaz.pyxis.db.tiers.TIPersonneAvsManager;
import globaz.pyxis.db.tiers.TIPersonneManager;
import globaz.pyxis.db.tiers.TITiers;
import globaz.pyxis.db.tiers.TITiersManager;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

public class CIDossierBtaViewBean extends CIDossierBta implements FWViewBeanInterface, Comparator {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String CS_ETAT_DOSSIER_BTA_GROUP = "CIETATBTA";
    public final static String CS_ETAT_DOSSIER_BTA_TYPE = "10300032";

    private TIPersonne personne = null;
    private TITiers tiers = null;
    private TITiersViewBean tiersViewBean = null;

    @Override
    public int compare(Object o1, Object o2) {
        String[] codesys1 = (String[]) o1;
        String[] codesys2 = (String[]) o2;

        // comparaison sur l'id du codesys
        Integer id1 = new Integer(codesys1[0]);
        Integer id2 = new Integer(codesys2[0]);

        return id1.compareTo(id2);
    }

    public Vector getListCodeSystemEtatDossier() {
        Vector vList = new Vector();
        String[] list = new String[2];

        try {
            FWParametersSystemCodeManager manager = new FWParametersSystemCodeManager();
            manager.setSession(getSession());
            manager.setForIdGroupe(CS_ETAT_DOSSIER_BTA_GROUP);
            manager.setForIdTypeCode(CS_ETAT_DOSSIER_BTA_TYPE);
            manager.find(BManager.SIZE_NOLIMIT);
            vList = new Vector(manager.size());
            for (int i = 0; i < manager.size(); i++) {
                list = new String[2];
                FWParametersSystemCode entity = (FWParametersSystemCode) manager.getEntity(i);
                list[0] = entity.getIdCode();
                list[1] = entity.getLibelle();
                vList.add(list);
            }

            // tri du vector pour mettre les élément dans l'ordre chronologique
            // (order by id du codesys)
            Collections.sort(vList, this);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return vList;
    }

    public TIPersonne getPersonne() {
        if (personne == null) {
            TIPersonneManager personneManager = new TIPersonneManager();
            personneManager.setSession(getSession());
            personneManager.setForIdTiers(getIdTiersImpotent());
            try {
                personneManager.find();
                if (!personneManager.isEmpty()) {
                    personne = (TIPersonne) personneManager.getFirstEntity();
                }
            } catch (Exception e) {
                _addError(null, e.getMessage());
            }
        }

        return personne;
    }

    public TITiers getTiers() {
        if (tiers == null) {
            TITiersManager tiersManager = new TITiersManager();
            tiersManager.setSession(getSession());
            tiersManager.setForIdTiers(getIdTiersImpotent());
            try {
                tiersManager.find();
                if (!tiersManager.isEmpty()) {
                    tiers = (TITiers) tiersManager.getFirstEntity();
                }
            } catch (Exception e) {
                _addError(null, e.getMessage());
            }
        }

        return tiers;
    }

    /**
     * Retourne le tiers correspondant à l'id de l'impotent (n'utiliser que pour l'affichage)
     * 
     * @return
     */
    public TITiersViewBean getTiersViewBeanForDisplay() {
        if (tiersViewBean == null && !JadeStringUtil.isBlank(getIdTiersImpotent())) {
            TIPersonneAvsManager personneAvsManager = new TIPersonneAvsManager();
            personneAvsManager.setSession(getSession());
            personneAvsManager.setForIdTiers(getIdTiersImpotent());
            try {
                personneAvsManager.find();
                if (!personneAvsManager.isEmpty()) {
                    tiersViewBean = (TITiersViewBean) personneAvsManager.getFirstEntity();
                }
            } catch (Exception e) {
                _addError(null, e.getMessage());
            }
        } else {
            if (tiersViewBean != null && !tiersViewBean.getIdTiers().equals(getIdTiersImpotent())) {
                TIPersonneAvsManager personneAvsManager = new TIPersonneAvsManager();
                personneAvsManager.setSession(getSession());
                personneAvsManager.setForIdTiers(getIdTiersImpotent());
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

    public void recharge() {
        setSession(getSession());
        setIdDossierBta(getIdDossierBta());
        try {
            this.retrieve();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
