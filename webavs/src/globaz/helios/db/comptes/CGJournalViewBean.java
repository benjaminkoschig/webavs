package globaz.helios.db.comptes;

import globaz.jade.client.util.JadeStringUtil;

public class CGJournalViewBean extends CGJournal implements globaz.framework.bean.FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public CGJournalViewBean() {
        super();
    }

    /**
     * Return le numéro de journal de comptabilité fournisseur.
     * 
     * @return
     */
    public String getLynxReference() {
        if (isReferenceLynx()) {
            return getReferenceExterne().substring(LX_REFERENCE_PREFIX.length());
        } else {
            return "";
        }
    }

    /**
     * La référence externe provient-elle de lynx ?
     * 
     * @return
     */
    public boolean isReferenceLynx() {
        if (JadeStringUtil.isIntegerEmpty(getReferenceExterne())) {
            return false;
        }

        return (getReferenceExterne().indexOf(LX_REFERENCE_PREFIX) > -1);
    }

    /**
     * La référence externe provient-elle d'osiris ?
     * 
     * @return
     */
    public boolean isReferenceOsiris() {
        if (JadeStringUtil.isIntegerEmpty(getReferenceExterne())) {
            return false;
        }

        try {
            Integer.parseInt(getReferenceExterne());

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
