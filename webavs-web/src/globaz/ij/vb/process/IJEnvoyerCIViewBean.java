/*
 * Créé le 5 octobre 05
 */
package globaz.ij.vb.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.ij.db.prestations.IJNoPassageInscriptionCI;
import globaz.ij.db.prestations.IJNoPassageInscriptionCIManager;
import globaz.ij.process.IJEnvoyerCIProcess;
import java.util.Iterator;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class IJEnvoyerCIViewBean extends IJEnvoyerCIProcess implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private boolean isRegeneration = false;
    private String noPassageFinal = "";

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
    public String[] getNoPassageList() {

        IJNoPassageInscriptionCIManager mgr = new IJNoPassageInscriptionCIManager();
        mgr.setSession(getSession());

        try {
            mgr.find();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String[] noPassageList = new String[mgr.size()];
        int nb = 0;

        for (Iterator iter = mgr.iterator(); iter.hasNext();) {
            IJNoPassageInscriptionCI noPassage = (IJNoPassageInscriptionCI) iter.next();
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

}
