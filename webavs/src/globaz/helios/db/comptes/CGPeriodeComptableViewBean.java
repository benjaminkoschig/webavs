package globaz.helios.db.comptes;

import globaz.jade.client.util.JadeStringUtil;
import java.util.HashSet;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (17.09.2002 16:47:40)
 * 
 * @author: Administrator
 */

public class CGPeriodeComptableViewBean extends CGPeriodeComptable implements globaz.framework.bean.FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Commentaire relatif au constructeur CGPeriodeComptableViewBean.
     */
    public CGPeriodeComptableViewBean() {
        super();
    }

    /**
     * Renvois le type de p�riode comptable par d�faut si la p�riode est nouvelle.
     * 
     * @return
     */
    public String getDefaultTypePeriode() {
        if (JadeStringUtil.isIntegerEmpty(getIdTypePeriode())) {
            try {
                if (getExerciceComptable().getMandat().isEstComptabiliteAVS().booleanValue()) {
                    return CS_MENSUEL;
                } else {
                    return CS_ANNUEL;
                }
            } catch (Exception e) {
                return "";
            }
        } else {
            return getIdTypePeriode();
        }
    }

    public HashSet getExceptTypePeriode() {
        HashSet except = new HashSet();
        // liste des cs qui ne devront pas figurer dans la liste
        try {
            if (getExerciceComptable().getMandat().isEstComptabiliteAVS().booleanValue()) {
                except.add(CS_SEMESTRIEL);
                except.add(CS_TRIMESTRIEL);
                except.add(CS_CLOTURE);
            } else {
                except.add(CS_CLOTURE);
            }

        } catch (Exception e) {
        }

        return except;
    }
}
