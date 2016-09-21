/*
 * Créé le 27 août 07
 */
package globaz.corvus.vb.paiementMensuel;

import globaz.corvus.api.lots.IRELot;
import globaz.corvus.application.REApplication;
import globaz.corvus.db.lots.RELot;
import globaz.corvus.db.lots.RELotManager;
import globaz.corvus.properties.REProperties;
import globaz.corvus.utils.REPmtMensuel;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.ordre.APIOrdreGroupe;
import globaz.osiris.api.ordre.APIOrganeExecution;
import globaz.osiris.db.ordres.CAOrganeExecution;
import globaz.osiris.db.ordres.CAOrganeExecutionManager;
import globaz.prestation.tools.PRDateFormater;
import globaz.prestation.vb.PRAbstractViewBeanSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * <H1>Description</H1>
 * 
 * @author BSC
 */
public class REExecuterPaiementMensuelViewBean extends PRAbstractViewBeanSupport {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String dateEcheancePaiement = "";
    private String datePaiement = "";
    private String description = "";
    private String eMailAddress = "";
    private String idOrganeExecution = "";
    private Boolean isActiverTraitementPrstErreurs = Boolean.FALSE;
    private String numeroOG = "";

    // SEPA iso20002
    private String isoCsTypeAvis = APIOrdreGroupe.ISO_TYPE_AVIS_COLLECT_SANS;
    private String isoGestionnaire = "";
    private String isoHighPriority = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    public String getDateEcheancePaiement() {
        return dateEcheancePaiement;
    }

    public String getDatePaiement() {
        return datePaiement;
    }

    public String getDateProchainPmt() {
        try {

            if (isPrestationErronne()) {
                return REPmtMensuel.getDateDernierPmt(getSession());
            } else {
                JADate date = new JADate(REPmtMensuel.getDateDernierPmt(getSession()));
                JACalendar cal = new JACalendarGregorian();
                date = cal.addMonths(date, 1);
                return PRDateFormater.convertDate_AAAAMMJJ_to_MMxAAAA(date.toStrAMJ());
            }

        } catch (Exception e) {
            return "";
        }
    }

    /**
     * @return
     */
    public String getDescription() {
        return description;
    }

    public String getDescriptionDernierLotErreur() {
        try {

            RELotManager mgr = new RELotManager();
            mgr.setSession(getSession());
            mgr.setForCsEtat(IRELot.CS_ETAT_LOT_PARTIEL);
            mgr.setForCsType(IRELot.CS_TYP_LOT_MENSUEL);
            mgr.setForCsLotOwner(IRELot.CS_LOT_OWNER_RENTES);

            String ddp = REPmtMensuel.getDateDernierPmt(getSession());
            if (JadeStringUtil.isBlankOrZero(ddp)) {
                return "";
            }

            mgr.setForDateEnvoiInMMxAAAA(ddp);
            mgr.setOrderBy(RELot.FIELDNAME_DATE_ENVOI + " desc ");
            mgr.find(1);

            if (mgr.size() == 0) {
                return "";
            } else {
                RELot lot = (RELot) mgr.getEntity(0);
                return lot.getDescription();
            }
        } catch (Exception e) {
            return "";
        }

    }

    /**
     * getter pour l'attribut EMail address
     * 
     * @return la valeur courante de l'attribut EMail address
     */
    public String getEMailAddress() {
        return eMailAddress;
    }

    public String getIdOrganeExecution() {
        return idOrganeExecution;
    }

    public Boolean getIsActiverTraitementPrstErreurs() {
        return isActiverTraitementPrstErreurs;
    }

    public String getNumeroOG() {
        return numeroOG;
    }

    /**
     * Retourne le vecteur de tableaux a 2 entrées {idOrganeExecution, description} défini pour ce view bean.
     * 
     * @return Vecteur de tableau à 2 entrées {idOrganeExecution, description}
     * @throws Exception
     */
    public Vector getOrganesExecution() throws Exception {

        Vector result = new Vector();

        String propertyOrganeExecution = getSession().getApplication().getProperty(
                REApplication.PROPERTY_ID_ORGANE_EXECUTION);

        // TODO passer par les API
        CAOrganeExecution organeExecution = null;
        CAOrganeExecutionManager mgr = new CAOrganeExecutionManager();
        mgr.setForIdTypeTraitementOG(true);
        mgr.setSession(getSession());
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

        String idOrgane = getSession().getApplication().getProperty(
                REProperties.ORGANE_EXECUTION_PAIEMENT.getPropertyName());

        for (CAOrganeExecution caorgane : listIdOrgane) {

            if (idOrgane.equals(caorgane.getIdOrganeExecution())) {
                result.add(new String[] { caorgane.getIdOrganeExecution(), caorgane.getNom(),
                        caorgane.getIdTypeTraitementOG() });
                listIdOrgane.remove(caorgane);
                break;
            }
        }

        for (CAOrganeExecution caorgane : listIdOrgane) {
            result.add(new String[] { caorgane.getIdOrganeExecution(), caorgane.getNom(),
                    caorgane.getIdTypeTraitementOG() });
        }

        return result;
    }

    public boolean isPrestationErronne() {
        try {
            RELotManager mgr = new RELotManager();
            mgr.setSession(getSession());
            mgr.setForCsType(IRELot.CS_TYP_LOT_MENSUEL);
            mgr.setForCsLotOwner(IRELot.CS_LOT_OWNER_RENTES);

            String ddp = REPmtMensuel.getDateDernierPmt(getSession());
            if (JadeStringUtil.isBlankOrZero(ddp)) {
                return false;
            }

            mgr.setForDateEnvoiInMMxAAAA(ddp);
            mgr.setOrderBy(RELot.FIELDNAME_DATE_ENVOI + " desc ");
            mgr.find(1);

            if (mgr.size() != 1) {
                return false;
            } else {
                RELot lot = (RELot) mgr.getFirstEntity();
                if (IRELot.CS_ETAT_LOT_PARTIEL.equals(lot.getCsEtatLot())) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            return false;
        }

    }

    public void setDateEcheancePaiement(String dateEcheancePaiement) {
        this.dateEcheancePaiement = dateEcheancePaiement;
    }

    public void setDatePaiement(String datePaiement) {
        this.datePaiement = datePaiement;
    }

    /**
     * @param string
     */
    public void setDescription(String string) {
        description = string;
    }

    /**
     * setter pour l'attribut EMail address
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setEMailAddress(String string) {
        eMailAddress = string;
    }

    public void setIdOrganeExecution(String idOrganeExecution) {
        this.idOrganeExecution = idOrganeExecution;
    }

    public void setIsActiverTraitementPrstErreurs(Boolean isActiverTraitementPrstErreurs) {
        this.isActiverTraitementPrstErreurs = isActiverTraitementPrstErreurs;
    }

    public void setNumeroOG(String numeroOG) {
        this.numeroOG = numeroOG;
    }

    public String getIsoCsTypeAvis() {
        return isoCsTypeAvis;
    }

    public void setIsoCsTypeAvis(String isoCsTypeAvis) {
        this.isoCsTypeAvis = isoCsTypeAvis;
    }

    public String getIsoGestionnaire() {
        if (isoGestionnaire.isEmpty()) {
            return ((BSession) getISession()).getUserName();
        }
        return isoGestionnaire;
    }

    public void setIsoGestionnaire(String isoGestionnaire) {
        this.isoGestionnaire = isoGestionnaire;
    }

    public String getIsoHighPriority() {
        return isoHighPriority;
    }

    public void setIsoHighPriority(String isoHightPriority) {
        isoHighPriority = isoHightPriority;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.vb.PRAbstractViewBeanSupport#validate()
     */
    @Override
    public boolean validate() {
        try {
            JADate dateEcheancePaiement = new JADate(getDateEcheancePaiement());
            JADate dateProchainPmt = new JADate(getDateProchainPmt());

            if ((dateEcheancePaiement.getMonth() != dateProchainPmt.getMonth())
                    || (dateEcheancePaiement.getYear() != dateProchainPmt.getYear())) {

                throw new Exception(
                        "La date d'échéance du paiement doit être dans le même mois que le prochain pmt mensuel.");
            }

            // ISO20022
            CAOrganeExecution organeExecution = new CAOrganeExecution();
            organeExecution.setSession(getSession());
            organeExecution.setIdOrganeExecution(getIdOrganeExecution());
            organeExecution.retrieve();
            if (organeExecution.getCSTypeTraitementOG().equals(APIOrganeExecution.OG_OPAE_DTA)) {
                // BZ 5459
                if (!JadeNumericUtil.isInteger(numeroOG)
                        || (((Integer.parseInt(numeroOG)) < 1) || (Integer.parseInt(numeroOG) > 99))) {
                    throw new Exception(getSession().getLabel("ERREUR_NUMERO_OG_OBLIGATOIRE"));
                }
            }
        } catch (Exception e) {
            setMsgType(FWViewBeanInterface.ERROR);
            setMessage(e.getMessage());
            return false;
        }
        return true;
    }

}
