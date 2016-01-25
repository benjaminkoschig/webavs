/*
 * Créé le 24 juin 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.pavo.db.compte;

import globaz.commons.nss.NSUtil;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.pyxis.db.tiers.TIPersonneAvsManager;
import globaz.pyxis.db.tiers.TITiers;
import globaz.pyxis.db.tiers.TITiersViewBean;

/**
 * ViewBean pour gérer les exceptions
 * 
 * @author sda
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CIExceptionsViewBean extends CIExceptions implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public CIExceptionsViewBean() {
        super();

    }

    @Override
    public void _afterRetrieve(BTransaction transaction) throws Exception {
        if (!JadeStringUtil.isBlankOrZero(getIdAffiliation())) {
            AFAffiliation aff = new AFAffiliation();
            aff.setSession(getSession());
            aff.setAffiliationId(getIdAffiliation());
            aff.retrieve();
            if (!aff.isNew()) {
                setLangueCorrespondance(aff.getTiers().getLangue());
            }
        }
        // Intialiser la langue avec celle du premier tiers correspondant au NSS du CI
        if (JadeStringUtil.isEmpty(getLangueCorrespondance())) {
            TITiersViewBean tiers = null;
            if (!JadeStringUtil.isBlankOrZero(getNumeroAvs())) {
                TIPersonneAvsManager mng = new TIPersonneAvsManager();
                mng.setSession(getSession());
                mng.setForNumAvsHistorique(NSUtil.formatAVSUnknown(getNumeroAvs()));
                mng.find();
                if (mng.getSize() > 0) {
                    tiers = (TITiersViewBean) mng.getFirstEntity();
                    setLangueCorrespondance(tiers.getLangue());
                }
            }
        }
        // Si tiers ou langue vide, prendre la langue de la session
        if (JadeStringUtil.isEmpty(getLangueCorrespondance())) {
            setLangueCorrespondance(TITiers.langueISOtoCodeSystem(getSession().getIdLangueISO()));
        }
    }
}
