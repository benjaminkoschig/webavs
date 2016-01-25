/*
 * Créé le 15 nov. 05
 */
package globaz.osiris.db.ventilation;

import java.util.ArrayList;

/**
 * @author ald
 * @modified by sel 13.08.2009
 */
public class CAVPPoste {

    private ArrayList<CAVPDetailMontant> detailMontantList = new ArrayList<CAVPDetailMontant>();
    private String idSection = null;
    private boolean montantSimple = true;

    /*
     * (non-Javadoc)
     * 
     * @seeglobaz.osiris.api.APIVPPoste#addDetailMontant(globaz.osiris.api. APIVPDetailMontant)
     */
    public void addDetailMontant(CAVPDetailMontant detailMontant) {
        boolean trouve = false;
        for (int i = 0; i < detailMontantList.size(); i++) {
            CAVPDetailMontant montant = detailMontantList.get(i);
            if (montant.getTypeMontant().equals(detailMontant.getTypeMontant())) {
                montant.addMontant(detailMontant.getMontantBase());
                trouve = true;
            }
        }
        if (!trouve) {
            detailMontantList.add(detailMontant);
        }
    }

    /**
     * @param type
     *            valeur possible :
     *            <ul>
     *            <li>APIVPDetailMontant.CS_VP_MONTANT_EMPLOYEUR</li>
     *            <li>APIVPDetailMontant.CS_VP_MONTANT_SALARIE</li>
     *            <li>APIVPDetailMontant.CS_VP_MONTANT_SIMPLE</li>
     *            </ul>
     * @return CAVPDetailMontant en fonction du type.
     */
    public CAVPDetailMontant getDetailMontant(String type) {
        for (int i = 0; i < detailMontantList.size(); i++) {
            CAVPDetailMontant montant = detailMontantList.get(i);
            if (montant.getTypeMontant().equals(type)) {
                return montant;
            }
        }
        return new CAVPDetailMontant();
    }

    /**
     * @return the idSection
     */
    public String getIdSection() {
        return idSection;
    }

    /**
     * @return
     */
    public boolean isMontantSimple() {
        return montantSimple;
    }

    /**
     * @param idSection
     *            the idSection to set
     */
    public void setIdSection(String idSection) {
        this.idSection = idSection;
    }

    /**
     * @param b
     */
    public void setMontantSimple(boolean b) {
        montantSimple = b;
    }

}
