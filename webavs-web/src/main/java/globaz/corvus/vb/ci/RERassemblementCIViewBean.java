/*
 * Créé le 25 juin. 07
 */
package globaz.corvus.vb.ci;

import globaz.corvus.db.ci.RECompteIndividuel;
import globaz.corvus.db.ci.RECompteIndividuelManager;
import globaz.corvus.db.ci.RERassemblementCI;
import globaz.corvus.db.ci.RERassemblementCIManager;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.tools.PRImagesConstants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author bsc
 * 
 */
public class RERassemblementCIViewBean extends RERassemblementCI implements FWViewBeanInterface {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateClotureForDemande = "";
    private String motifForDemande = "";
    private String nssAyantDroitForDemande = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @return
     */
    public String getCsEtatLibelle() {
        return getSession().getCodeLibelle(getCsEtat());
    }

    // /**
    // * @return
    // */
    // public String getCsStatusLibelle() {
    // return getSession().getCodeLibelle(getCsStatus());
    // }

    /**
     * Memorise la date de cloture voulue lors de la demande
     * 
     * @return
     */
    public String getDateClotureForDemande() {
        return dateClotureForDemande;
    }

    /**
     * getter pour l'image isCiAdditionnelTraite
     * 
     * @return l'image correspondant a isCiAdditionnelTraite
     */
    public String getIsCiAdditionnelImage() {
        if (JadeStringUtil.isIntegerEmpty(getIdParent())) {
            return PRImagesConstants.IMAGE_ERREUR;
        } else {
            return PRImagesConstants.IMAGE_OK;
        }
    }

    /**
     * Memorise le motif voulu lors de la demande
     * 
     * @return
     */
    public String getMotifForDemande() {
        return motifForDemande;
    }

    /**
     * @return
     */
    public String getNssAyantDroitForDemande() {
        return nssAyantDroitForDemande;
    }

    /**
     * Retrouve la liste des rassemblement CI pour ce CI et qui ne sont pas des rassemblements pour CI add.
     * 
     * @return
     */
    public String[] getRassemblementCIList(String idTiers) {

        ArrayList rassembleemntCI;
        String[] result = {};
        try {
            Map idRassemblementCIDesc = new HashMap();
            rassembleemntCI = new ArrayList();

            // la ligne vide du menu
            rassembleemntCI.add("");
            rassembleemntCI.add("");
            rassembleemntCI.add("");
            rassembleemntCI.add("");

            // on cherche l'idCI avec l'idTiers
            if (isNew()) {
                RECompteIndividuelManager ciManager = new RECompteIndividuelManager();
                ciManager.setSession(getSession());
                ciManager.setForIdTiers(idTiers);
                ciManager.find();

                if (ciManager.getSize() > 0) {
                    RECompteIndividuel ci = (RECompteIndividuel) ciManager.getFirstEntity();
                    setIdCI(ci.getIdCi());
                }
            }

            if (!JadeStringUtil.isIntegerEmpty(getIdCI())) {

                // on cherche les rassemblements CI pour ce compte individuel
                RERassemblementCIListViewBean rciManager = new RERassemblementCIListViewBean();
                rciManager.setSession(getSession());
                rciManager.setForIdCI(getIdCI());
                rciManager.setForMotifBetween(" 71 AND 91 ");
                rciManager.find();

                for (int i = 0; i < rciManager.size(); i++) {

                    RERassemblementCIViewBean rciVb = (RERassemblementCIViewBean) rciManager.getEntity(i);
                    String idRci = rciVb.getIdRCI();

                    // on memorise le rassemblement
                    // sauf si
                    // - rassemblement pour CI add
                    // - lui meme
                    if (!idRassemblementCIDesc.containsKey(idRci) && (!idRci.equals(getIdRCI()))
                            && JadeStringUtil.isIntegerEmpty(rciVb.getIdParent())) {

                        idRassemblementCIDesc.put(idRci, null);
                        rassembleemntCI.add(idRci);
                        rassembleemntCI.add(rciVb.getMotif() + "/" + rciVb.getDateCloture() + "/"
                                + rciVb.getCsEtatLibelle());
                        rassembleemntCI.add(rciVb.getMotif());
                        rassembleemntCI.add(rciVb.getDateCloture());
                    }
                }
            }
        } catch (Exception e) {
            return result;
        }

        return (String[]) rassembleemntCI.toArray(result);
    }

    public boolean isSupprimable() {
        RERassemblementCIManager rciManager = new RERassemblementCIManager();
        rciManager.setSession(getSession());
        rciManager.setForIdParent(getIdRCI());
        try {
            return rciManager.getCount() == 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Memorise la date de cloture voulue lors de la demande
     * 
     * @param string
     */
    public void setDateClotureForDemande(String string) {
        dateClotureForDemande = string;
    }

    /**
     * Memorise le motif voulu lors de la demande
     * 
     * @param string
     */
    public void setMotifForDemande(String string) {
        motifForDemande = string;
    }

    /**
     * @param string
     */
    public void setNssAyantDroitForDemande(String string) {
        nssAyantDroitForDemande = string;
    }

}
