/*
 * Cr�� le 30 ao�t 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.phenix.db.communications;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author mmu
 * 
 *         Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class CPRejetsListViewBean extends CPRejetsManager implements FWListViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String eMailAddress = "";
    private String[] listIdRetour = null;

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CPRejetsViewBean();
    }

    public String geteMailAddress() {
        return eMailAddress;
    }

    public String getIdTiers(int pos) {
        try {
            String idDem = ((CPRejets) getEntity(pos)).getIdDemande();
            if (!JadeStringUtil.isBlankOrZero(idDem)) {
                CPCommunicationFiscale demande = new CPCommunicationFiscale();
                demande.setSession(getSession());
                demande.setIdCommunication(idDem);
                demande.retrieve();
                if (!demande.isNew()) {
                    return demande.getIdTiers();
                }
            }
        } catch (Exception e) {
            return "";
        }
        return "";
    }

    public String[] getListIdRetour() {
        return listIdRetour;
    }

    public void seteMailAddress(String eMailAddress) {
        this.eMailAddress = eMailAddress;
    }

    public void setListIdRetour(String[] listIdRetour) {
        this.listIdRetour = listIdRetour;
    }
}
