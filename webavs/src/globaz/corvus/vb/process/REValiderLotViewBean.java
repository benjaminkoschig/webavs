/*
 * Créé le 12 janv. 07
 */
package globaz.corvus.vb.process;

import globaz.corvus.properties.REProperties;
import globaz.corvus.utils.REPmtMensuel;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.osiris.db.ordres.CAOrganeExecution;
import globaz.osiris.db.ordres.CAOrganeExecutionManager;
import globaz.prestation.vb.PRAbstractViewBeanSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * @author SCR
 */
public class REValiderLotViewBean extends PRAbstractViewBeanSupport {

    private String csEtatLot = "";
    private String csTypeLot = "";
    private String dateEcheancePaiement = "";
    private String dateValeurComptable = JACalendar.todayJJsMMsAAAA();
    private String descriptionLot = null;
    private String eMailAddress = "";
    private String idLot = "";
    private String idOrganeExecution = "";
    private String numeroOG = "";

    public String getCsEtatLot() {
        return csEtatLot;
    }

    public String getCsTypeLot() {
        return csTypeLot;
    }

    public String getCurrentDate() {
        return JACalendar.todayJJsMMsAAAA();

    }

    public String getDateEcheancePaiement() {
        return dateEcheancePaiement;
    }

    public String getDateValeurComptable() {
        return dateValeurComptable;
    }

    public String getDescriptionLot() {
        return descriptionLot;
    }

    public String getEMailAddress() {
        return eMailAddress;
    }

    public String getIdLot() {
        return idLot;
    }

    public String getIdOrganeExecution() {
        return idOrganeExecution;
    }

    public String getNumeroOG() {
        return numeroOG;
    }

    /**
     * Retourne le vecteur de tableaux a 2 entrées {idOrganeExecution, description} défini pour ce view bean.
     * 
     * 
     * @return Vecteur de tableau à 2 entrées {idOrganeExecution, description}
     * @throws Exception
     */
    public Vector getOrganesExecution() throws Exception {

        Vector result = new Vector();
        CAOrganeExecution organeExecution = null;
        CAOrganeExecutionManager mgr = new CAOrganeExecutionManager();
        mgr.setSession(getSession());
        mgr.setForIdTypeTraitementOG(true);
        try {
            mgr.find();
        } catch (Exception e) {
            return result;
        }

        List<CAOrganeExecution> listIdOrgane = new ArrayList<CAOrganeExecution>();

        for (int i = 0; i < mgr.size(); i++) {
            organeExecution = (CAOrganeExecution) mgr.getEntity(i);

            listIdOrgane.add(organeExecution);
        }

        String idOrgane = ((BSession) getISession()).getApplication().getProperty(
                REProperties.ORGANE_EXECUTION_PAIEMENT.getPropertyName());

        for (CAOrganeExecution caorgane : listIdOrgane) {

            if (idOrgane.equals(caorgane.getIdOrganeExecution())) {
                result.add(new String[] { caorgane.getIdOrganeExecution(), caorgane.getNom() });
                listIdOrgane.remove(caorgane);
                break;
            }
        }

        for (CAOrganeExecution caorgane : listIdOrgane) {
            result.add(new String[] { caorgane.getIdOrganeExecution(), caorgane.getNom() });
        }

        return result;
    }

    public void setCsEtatLot(String csEtatLot) {
        this.csEtatLot = csEtatLot;
    }

    public void setCsTypeLot(String csTypeLot) {
        this.csTypeLot = csTypeLot;
    }

    public void setDateEcheancePaiement(String dateEcheancePaiement) {
        this.dateEcheancePaiement = dateEcheancePaiement;
    }

    public void setDateValeurComptable(String dateValeurComptable) {
        this.dateValeurComptable = dateValeurComptable;
    }

    public void setDescriptionLot(String descriptionLot) {
        this.descriptionLot = descriptionLot;
    }

    public void setEMailAddress(String mailAddress) {
        eMailAddress = mailAddress;
    }

    public void setIdLot(String idLot) {
        this.idLot = idLot;
    }

    public void setIdOrganeExecution(String idOrganeExecution) {
        this.idOrganeExecution = idOrganeExecution;
    }

    public void setNumeroOG(String numeroOG) {
        this.numeroOG = numeroOG;
    }

    @Override
    public boolean validate() {

        try {
            JADate dateComptable = new JADate(getDateValeurComptable());
            JADate dateDernierPmt = new JADate(REPmtMensuel.getDateDernierPmt(getSession()));

            if ((dateComptable.getMonth() != dateDernierPmt.getMonth())
                    || (dateComptable.getYear() != dateDernierPmt.getYear())) {

                throw new Exception(
                        "La date de valeur comptable doit être dans le même mois que le dernier pmt mensuel.");
            }

            // BZ 5459
            if (!JadeNumericUtil.isInteger(getNumeroOG())
                    || (((Integer.parseInt(getNumeroOG())) < 1) || (Integer.parseInt(getNumeroOG()) > 99))) {
                throw new Exception(getSession().getLabel("ERREUR_NUMERO_OG_OBLIGATOIRE"));
            }
        } catch (Exception e) {
            setMsgType(FWViewBeanInterface.ERROR);
            setMessage(e.getMessage());
            return false;
        }
        return true;
    }
}
