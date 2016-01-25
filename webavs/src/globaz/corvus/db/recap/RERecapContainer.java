package globaz.corvus.db.recap;

import globaz.corvus.db.recap.access.RERecapInfo;
import globaz.corvus.db.recap.access.RERecapInfoManager;
import globaz.framework.util.FWCurrency;
import java.util.HashMap;
import java.util.Map;

public class RERecapContainer {
    private RERecapInfoManager infoManager = null;
    private Map totalMap = null;

    /**
     * Constructeur
     * 
     * @param _infoManager
     */
    public RERecapContainer(RERecapInfoManager _infoManager) {
        infoManager = _infoManager;
        initMap();
    }

    /**
     * cumule le montant dans la rubrique du map
     * 
     * @param key
     * @param montant
     */
    private void cumulMontant(String key, String montant) {
        if (totalMap.containsKey(key)) {
            FWCurrency value = (FWCurrency) totalMap.get(key);
            value.add(montant);
            totalMap.put(key, value);
        } else {
            totalMap.put(key, new FWCurrency(montant));
        }
    }

    /**
     * @param key
     * @param montant
     */
    private void cumulMontantNegate(String key, String montant) {
        FWCurrency mt = new FWCurrency(montant);
        mt.negate();
        cumulMontant(key, mt.toString());
    }

    /**
     * Récupère le total en fonction d'un code
     * 
     * @param code
     *            (prendre les codes statiques de la classe IRERecapElementCode
     * @return
     */
    public FWCurrency getTotal(String code) {
        return (FWCurrency) totalMap.get(code);
    }

    /**
     * Récupère l'ensembe du map
     * 
     * @return
     */
    public Map getTotalMap() {
        return totalMap;
    }

    /**
     * Initialisation du map
     */
    private void initMap() {
        totalMap = new HashMap();
        RERecapInfo info = null;
        for (int i = 0; i < infoManager.size(); i++) {
            info = (RERecapInfo) infoManager.getEntity(i);
            if (info.getCodeRecap().equals(IRERecapElementCode.AVS_RO_AUGMENTATION)
                    || info.getCodeRecap().equals(IRERecapElementCode.AVS_RO_500003)
                    || info.getCodeRecap().equals(IRERecapElementCode.AVS_RO_DIMINUTION)) {
                if (info.getCodeRecap().equals(IRERecapElementCode.AVS_RO_DIMINUTION)) {
                    cumulMontantNegate(IRERecapElementCode.AVS_RO_EN_COURS_MOIS_PRECEDENT, info.getTotalMontant());
                } else {
                    cumulMontant(IRERecapElementCode.AVS_RO_EN_COURS_MOIS_PRECEDENT, info.getTotalMontant());
                }
            } else if (info.getCodeRecap().equals(IRERecapElementCode.AVS_REO_AUGMENTATION)
                    || info.getCodeRecap().equals(IRERecapElementCode.AVS_REO_501003)
                    || info.getCodeRecap().equals(IRERecapElementCode.AVS_REO_DIMINUTION)) {
                if (info.getCodeRecap().equals(IRERecapElementCode.AVS_REO_DIMINUTION)) {
                    cumulMontantNegate(IRERecapElementCode.AVS_REO_EN_COURS_MOIS_PRECEDENT, info.getTotalMontant());
                } else {
                    cumulMontant(IRERecapElementCode.AVS_REO_EN_COURS_MOIS_PRECEDENT, info.getTotalMontant());
                }
            } else if (info.getCodeRecap().equals(IRERecapElementCode.AVS_API_AUGMENTATION)
                    || info.getCodeRecap().equals(IRERecapElementCode.AVS_API_503003)
                    || info.getCodeRecap().equals(IRERecapElementCode.AVS_API_DIMINUTION)) {
                if (info.getCodeRecap().equals(IRERecapElementCode.AVS_API_DIMINUTION)) {
                    cumulMontantNegate(IRERecapElementCode.AVS_API_EN_COURS_MOIS_PRECEDENT, info.getTotalMontant());
                } else {
                    cumulMontant(IRERecapElementCode.AVS_API_EN_COURS_MOIS_PRECEDENT, info.getTotalMontant());
                }
            } else if (info.getCodeRecap().equals(IRERecapElementCode.AI_RO_AUGMENTATION)
                    || info.getCodeRecap().equals(IRERecapElementCode.AI_RO_510003)
                    || info.getCodeRecap().equals(IRERecapElementCode.AI_RO_DIMINUTION)) {
                if (info.getCodeRecap().equals(IRERecapElementCode.AI_RO_DIMINUTION)) {
                    cumulMontantNegate(IRERecapElementCode.AI_RO_EN_COURS_MOIS_PRECEDENT, info.getTotalMontant());
                } else {
                    cumulMontant(IRERecapElementCode.AI_RO_EN_COURS_MOIS_PRECEDENT, info.getTotalMontant());
                }
            } else if (info.getCodeRecap().equals(IRERecapElementCode.AI_REO_AUGMENTATION)
                    || info.getCodeRecap().equals(IRERecapElementCode.AI_REO_511003)
                    || info.getCodeRecap().equals(IRERecapElementCode.AI_REO_DIMINUTION)) {
                if (info.getCodeRecap().equals(IRERecapElementCode.AI_REO_DIMINUTION)) {
                    cumulMontantNegate(IRERecapElementCode.AI_REO_EN_COURS_MOIS_PRECEDENT, info.getTotalMontant());
                } else {
                    cumulMontant(IRERecapElementCode.AI_REO_EN_COURS_MOIS_PRECEDENT, info.getTotalMontant());
                }
            } else if (info.getCodeRecap().equals(IRERecapElementCode.AI_API_AUGMENTATION)
                    || info.getCodeRecap().equals(IRERecapElementCode.AI_API_513003)
                    || info.getCodeRecap().equals(IRERecapElementCode.AI_API_DIMINUTION)) {
                if (info.getCodeRecap().equals(IRERecapElementCode.AI_API_DIMINUTION)) {
                    cumulMontantNegate(IRERecapElementCode.AI_API_EN_COURS_MOIS_PRECEDENT, info.getTotalMontant());
                } else {
                    cumulMontant(IRERecapElementCode.AI_API_EN_COURS_MOIS_PRECEDENT, info.getTotalMontant());
                }
            }
        }
    }
}
