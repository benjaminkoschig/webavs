/*
 * Créé le 20 juin 05
 */
package globaz.apg.vb.process;

import globaz.apg.db.prestation.APNoPassageInscriptionCI;
import globaz.apg.db.prestation.APNoPassageInscriptionCIManager;
import globaz.apg.process.APEnvoyerCIProcess;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.prestation.api.IPRDemande;
import org.apache.commons.lang.StringUtils;

import java.util.Iterator;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class APEnvoyerCIViewBean extends APEnvoyerCIProcess implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private boolean isRegeneration = false;
    private String noPassageFinal = "";
    private String typePrestation = "";

    /**
     * Getter permettant de définir le no de passage (annee+mois+jour)
     * 
     * @return le numéro de passsage
     */
    @Override
    public String getNoPassage() {

        String noPassage = "";
        JADate today = JACalendar.today();

        // Récupération de l'année
        noPassage = Integer.toString(today.getYear());

        // Récupération du mois
        if (today.getMonth() < 10) {
            noPassage += "0" + today.getMonth();
        } else {
            noPassage += today.getMonth();
        }

        // Récupération du jour
        if (today.getDay() < 10) {
            noPassage += "0" + today.getDay();
        } else {
            noPassage += today.getDay();
        }

        return noPassage;
    }

    /**
     * @return
     */
    public String getNoPassageFinal() {
        return noPassageFinal;
    }

    /**
     * 
     * @return
     */
    public String[] getNoPassageList(String typePrestationModule) {

        APNoPassageInscriptionCIManager mgr = new APNoPassageInscriptionCIManager();
        if (StringUtils.equals(IPRDemande.CS_TYPE_PANDEMIE, typePrestationModule)) {
            mgr.setPandemie(true);
        }
        mgr.setSession(getSession());


        try {
            mgr.find();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String[] noPassageList = new String[mgr.size()];
        int nb = 0;

        for (Iterator iter = mgr.iterator(); iter.hasNext();) {
            APNoPassageInscriptionCI noPassage = (APNoPassageInscriptionCI) iter.next();
            noPassageList[nb] = noPassage.getNoPassage();
            nb++;
        }

        return noPassageList;
    }

    /**
     * @return
     */
    public boolean isRegeneration() {
        return isRegeneration;
    }

    /**
     * @param string
     */
    public void setNoPassageFinal(String string) {
        noPassageFinal = string;
    }

    /**
     * @param b
     */
    public void setRegeneration(boolean b) {
        isRegeneration = b;
    }

    public String getTypePrestation() {
        return typePrestation;
    }

    public void setTypePrestation(String typePrestation) {
        this.typePrestation = typePrestation;
    }
}
