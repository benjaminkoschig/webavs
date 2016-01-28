package globaz.pavo.db.inscriptions;

import globaz.commons.nss.NSUtil;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BManager;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import java.util.Collection;
import java.util.Vector;

/**
 * Vue d'un journal. Date de création : (07.11.2002 17:14:02)
 * 
 * @author: ema
 */
public class CIJournalViewBean extends CIJournal implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forAnneeSauvegarde = null;
    private String forMontantSauvegarde = null;
    private String forPremiereFoisSurPage = "true";
    /**
     * Commentaire relatif au constructeur CIJournalViewBean.
     */
    private String fromAvsSauvegarde = null;
    private String fromAvsSauvegardeNNSS = null;

    public CIJournalViewBean() {
        super();
        setAnneeCotisation(null);
    }

    @Override
    protected void _init() {
        if (getAnneeCotisation() == null) {
            setAnneeCotisation(String.valueOf(java.util.Calendar.getInstance().get(java.util.Calendar.YEAR) - 1));
        }
    }

    public Vector getChampsOrderedAsCodeSystem(String type, String group, Collection exceptValue) {

        boolean isExceptValue = true;
        Vector vList = new Vector();
        String[] list = new String[2];

        if (exceptValue == null || exceptValue.size() == 0) {
            isExceptValue = false;
        }

        try {
            FWParametersSystemCodeManager manager = new FWParametersSystemCodeManager();

            manager.setForIdTypeCode(type);
            manager.setForIdGroupe(group);
            manager.setSession(getSession());
            manager.find(BManager.SIZE_NOLIMIT);
            vList = new Vector(manager.size());
            for (int i = 0; i < manager.size(); i++) {
                list = new String[2];
                FWParametersSystemCode entity = (FWParametersSystemCode) manager.getEntity(i);

                String code = entity.getCurrentCodeUtilisateur().getIdCodeSysteme();

                // Passe au code suivant, si le code se trouve dans la liste des
                // valeurs à bypasser
                if (isExceptValue && exceptValue.contains(code)) {
                    continue;
                }

                list[0] = code;
                list[1] = entity.getCurrentCodeUtilisateur().getLibelle();
                vList.add(list);
            }
        } catch (Exception e) {
            // si probleme, retourne list vide.
        }

        // Sort the elements (bubble sort)
        String first = "", second = "";
        for (int i = 0; i < vList.size(); i++) {
            first = ((String[]) vList.elementAt(i))[1];
            for (int j = i + 1; j < vList.size(); j++) {
                second = ((String[]) vList.elementAt(j))[1];
                if (second.toUpperCase().trim().compareTo(first.toUpperCase().trim()) < 1) {
                    Object o2 = vList.elementAt(j);
                    vList.remove(o2);
                    vList.add(i, o2);
                    first = ((String[]) vList.elementAt(i))[1];
                }
            }
        }
        return vList;
    }

    /**
     * @return
     */
    public String getForAnneeSauvegarde() {
        return forAnneeSauvegarde;
    }

    /**
     * @return
     */
    public String getForMontantSauvegarde() {
        return forMontantSauvegarde;
    }

    /**
     * @return
     */
    public String getForPremiereFoisSurPage() {
        return forPremiereFoisSurPage;
    }

    /**
     * Returns the fromAvsSauvegarde.
     * 
     * @return String
     */
    public String getFromAvsSauvegarde() {
        return fromAvsSauvegarde;
    }

    public String getFromAvsSauvegardeNNSS() {
        return fromAvsSauvegardeNNSS;
    }

    /**
     * @param string
     */
    public void setForAnneeSauvegarde(String string) {
        forAnneeSauvegarde = string;
    }

    /**
     * @param string
     */
    public void setForMontantSauvegarde(String string) {
        forMontantSauvegarde = string;
    }

    /**
     * @param string
     */
    public void setForPremiereFoisSurPage(String string) {
        forPremiereFoisSurPage = string;
    }

    /**
     * Sets the fromAvsSauvegarde.
     * 
     * @param fromAvsSauvegarde
     *            The fromAvsSauvegarde to set
     */
    public void setFromAvsSauvegarde(String fromAvsSauvegarde) {
        if ("true".equalsIgnoreCase(fromAvsSauvegardeNNSS)) {
            this.fromAvsSauvegarde = NSUtil.formatWithoutPrefixe(fromAvsSauvegarde, true);
        } else if ("false".equalsIgnoreCase(fromAvsSauvegardeNNSS)) {
            this.fromAvsSauvegarde = NSUtil.formatWithoutPrefixe(fromAvsSauvegarde, false);
        } else {
            this.fromAvsSauvegarde = fromAvsSauvegarde;
        }
    }

    public void setFromAvsSauvegardeNNSS(String fromAvsSauvegardeNNSS) {
        this.fromAvsSauvegardeNNSS = fromAvsSauvegardeNNSS;
    }

}
