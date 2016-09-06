/*
 * Créé le 12 janv. 07
 */
package globaz.corvus.vb.process;

import globaz.corvus.api.avances.IREAvances;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import globaz.osiris.api.ordre.APIOrdreGroupe;
import globaz.osiris.db.ordres.CAOrganeExecution;
import globaz.osiris.db.ordres.CAOrganeExecutionManager;
import globaz.prestation.vb.PRAbstractViewBeanSupport;
import java.rmi.RemoteException;
import java.util.Vector;

/**
 * @author scr
 * 
 */

public class REExecuterAvancesViewBean extends PRAbstractViewBeanSupport {

    private String csTypeAvance = IREAvances.CS_TYPE_ACOMPTES_UNIQUE;
    private String dateEcheance = null;
    private String datePaiement = "";
    private String eMailAddress = "";
    private String idOrganeExecution = null;
    private String noOg = null;

    // SEPA iso20002
    private String isoCsTypeAvis = APIOrdreGroupe.ISO_TYPE_AVIS_COLLECT_SANS;
    private String isoGestionnaire = "";
    private String isoHightPriority = "";

    public String getCsTypeAvance() {
        return csTypeAvance;
    }

    public String getDateEcheance() {
        return dateEcheance;
    }

    public String getDatePaiement() {
        return datePaiement;
    }

    public String getEMailAddress() {
        return eMailAddress;
    }

    public String getIdOrganeExecution() {
        return idOrganeExecution;
    }

    public String getNoOg() {
        return noOg;
    }

    /**
     * Retourne le vecteur de tableaux a 2 entrées {idOrganeExecution, description} défini pour ce view bean.
     * La valeur par defaut sera celle de la prorpiété
     * 
     * @return Vecteur de tableau à 2 entrées {idOrganeExecution, description}
     * @throws Exception
     * @throws RemoteException
     */

    public Vector getOrganesExecution() throws RemoteException, Exception {

        // récupération de l'id organe d'execution definit en propriété pour les rents
        String idOrganeExecutionPmtRentes = GlobazSystem.getApplication("CORVUS").getProperty(
                "id.orgrane.execution.pmt.rentes");

        Vector result = new Vector();
        CAOrganeExecution organeExecution = null;
        CAOrganeExecution organeExecutionDefaut = null;

        CAOrganeExecutionManager mgr = new CAOrganeExecutionManager();
        mgr.setSession((BSession) getISession());
        mgr.setForIdTypeTraitementOG(true);

        try {
            mgr.find();
        } catch (Exception e) {
            return result;
        }

        for (int i = 0; i < mgr.size(); i++) {
            // récupération de l'organe d'éxécution
            organeExecution = (CAOrganeExecution) mgr.getEntity(i);
            // gestion idOrganeExecutionPmtRentes
            if ((idOrganeExecutionPmtRentes != null) && organeExecution.getId().equals(idOrganeExecutionPmtRentes)) {
                organeExecutionDefaut = organeExecution;
            } else {
                result.add(new String[] { organeExecution.getIdOrganeExecution(), organeExecution.getNom(),
                        organeExecution.getCSTypeTraitementOG() });
            }
        }
        // ajout du defaut en position 1, si pas null
        if (organeExecutionDefaut != null) {
            result.add(0, (new String[] { organeExecutionDefaut.getIdOrganeExecution(), organeExecutionDefaut.getNom(),
                    organeExecutionDefaut.getCSTypeTraitementOG() }));
        }

        return result;
    }

    public void setCsTypeAvance(String csTypeAvance) {
        this.csTypeAvance = csTypeAvance;
    }

    public void setDateEcheance(String dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    public void setDatePaiement(String datePaiement) {
        this.datePaiement = datePaiement;
    }

    public void setEMailAddress(String mailAddress) {
        eMailAddress = mailAddress;
    }

    public void setIdOrganeExecution(String idOrganeExecution) {
        this.idOrganeExecution = idOrganeExecution;
    }

    public void setNoOg(String noOg) {
        this.noOg = noOg;
    }

    public String getIsoCsTypeAvis() {
        return isoCsTypeAvis;
    }

    public void setIsoCsTypeAvis(String isoCsTypeAvis) {
        this.isoCsTypeAvis = isoCsTypeAvis;
    }

    public String getIsoGestionnaire() {
        if (isoGestionnaire.isEmpty()) {
            return getSession().getUserName();
        }
        return isoGestionnaire;
    }

    public void setIsoGestionnaire(String isoGestionnaire) {
        this.isoGestionnaire = isoGestionnaire;
    }

    public String getIsoHightPriority() {
        return isoHightPriority;
    }

    public void setIsoHightPriority(String isoHightPriority) {
        this.isoHightPriority = isoHightPriority;
    }

    @Override
    public boolean validate() {
        return true;
    }
}
