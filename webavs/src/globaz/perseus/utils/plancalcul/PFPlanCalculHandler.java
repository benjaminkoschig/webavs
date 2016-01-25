/**
 * 
 */
package globaz.perseus.utils.plancalcul;

import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import java.rmi.RemoteException;
import java.util.ArrayList;
import ch.globaz.perseus.business.calcul.OutputCalcul;
import ch.globaz.perseus.business.exceptions.calcul.CalculException;
import ch.globaz.perseus.business.models.pcfaccordee.PCFAccordee;

/**
 * @author DDE
 * 
 */
public class PFPlanCalculHandler {

    private ArrayList<PFLignePlanCalculHandler> blocDepensesReconnues;
    private ArrayList<PFLignePlanCalculHandler> blocFortune;
    private ArrayList<PFLignePlanCalculHandler> blocPrestation;
    private ArrayList<PFLignePlanCalculHandler> blocRevenus;
    private OutputCalcul outputCalcul;
    private PCFAccordee pcfAccordee;
    private BSession session;

    public PFPlanCalculHandler(PCFAccordee pcfAccordee, BISession session) throws CalculException {
        blocFortune = new ArrayList<PFLignePlanCalculHandler>(0);
        this.pcfAccordee = pcfAccordee;
        outputCalcul = pcfAccordee.getCalcul();
        this.session = (BSession) session;

        try {
            createBlocFortune();
            createBlocRevenus();
            createBlocDepensesReconnues();
            createBlocPrestation();
        } catch (RemoteException e) {
            throw new CalculException("Erreur d'affichage du calcul, RemoteException : " + e.getMessage());
        }
    }

    /**
     * Creation du bloc dépensesReconnues
     * 
     * @throws RemoteException
     * 
     */
    private void createBlocDepensesReconnues() throws RemoteException {
        PFGroupeDepensesReconnuesHandler groupeDepensesReconnues = new PFGroupeDepensesReconnuesHandler(outputCalcul);
        blocDepensesReconnues = dealLibelle(groupeDepensesReconnues.getLignes());
    }

    /**
     * Creation du bloc fortune
     * 
     * @throws RemoteException
     * 
     */
    private void createBlocFortune() throws RemoteException {
        PFGroupeFortuneHandler groupeFortune = new PFGroupeFortuneHandler(outputCalcul);
        blocFortune = dealLibelle(groupeFortune.getLignes());
    }

    /**
     * Creation du bloc prestation
     * 
     * @throws RemoteException
     * 
     */
    private void createBlocPrestation() throws RemoteException {
        PFGroupePrestationHandler groupePrestation = new PFGroupePrestationHandler(pcfAccordee);
        blocPrestation = dealLibelle(groupePrestation.getLignes());
    }

    /**
     * Creation du bloc revenus
     * 
     * @throws RemoteException
     * 
     */
    private void createBlocRevenus() throws RemoteException {
        PFGroupeRevenuHandler groupeRevenu = new PFGroupeRevenuHandler(outputCalcul);
        blocRevenus = dealLibelle(groupeRevenu.getLignes());
    }

    /**
     * Transforme les labels des ligne en libelles
     * 
     * @param liste
     * @throws RemoteException
     */
    private ArrayList<PFLignePlanCalculHandler> dealLibelle(ArrayList<PFLignePlanCalculHandler> liste)
            throws RemoteException {

        // iteration sur toutes les lignes
        for (PFLignePlanCalculHandler ligne : liste) {
            String libelle = ligne.getLibelle();
            String nbsp = "";
            String codeLabel = libelle;
            if (libelle.lastIndexOf("&nbsp;") >= 0) {
                codeLabel = libelle.substring(libelle.lastIndexOf("&nbsp;") + 6);
                nbsp = libelle.substring(0, libelle.lastIndexOf("&nbsp;") + 5);
            }
            if (!JadeStringUtil.isEmpty(codeLabel)) {
                libelle = session.getLabel(codeLabel);
            }
            libelle = nbsp + libelle;
            ligne.setLibelle(libelle);
        }

        return liste;
    }

    /**
     * @return the blocDepensesReconnues
     */
    public ArrayList<PFLignePlanCalculHandler> getBlocDepensesReconnues() {
        return blocDepensesReconnues;
    }

    /**
     * @return the blocFortune
     */
    public ArrayList<PFLignePlanCalculHandler> getBlocFortune() {
        return blocFortune;
    }

    /**
     * @return the blocPrestation
     */
    public ArrayList<PFLignePlanCalculHandler> getBlocPrestation() {
        return blocPrestation;
    }

    /**
     * @return the blocRevenus
     */
    public ArrayList<PFLignePlanCalculHandler> getBlocRevenus() {
        return blocRevenus;
    }

    /**
     * @return the outputCalcul
     */
    public OutputCalcul getOutputCalcul() {
        return outputCalcul;
    }

}
