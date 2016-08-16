package globaz.pegasus.vb.lot;

import globaz.corvus.properties.REProperties;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.util.JACalendar;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.osiris.api.ordre.APIOrdreGroupe;
import globaz.osiris.db.ordres.CAOrganeExecution;
import globaz.osiris.db.ordres.CAOrganeExecutionManager;
import java.util.Vector;
import ch.globaz.common.properties.PropertiesException;

public class PCComptabiliserViewBean extends BJadePersistentObjectViewBean {

    private String csEtatLot = "";
    private String csTypeLot = "";
    private String dateEcheancePaiement = "";
    private String dateValeurComptable = JACalendar.todayJJsMMsAAAA();
    private String descriptionLot = null;
    private String eMailAddress = "";
    private String idLot = "";
    private String idOrganeExecution = "";
    private String numeroOG = "";

    // SEPA iso20002
    private String isoCsTypeAvis = APIOrdreGroupe.ISO_TYPE_AVIS_COLLECT_SANS;
    private String isoGestionnaire = "";
    private String isoHightPriority = "";

    public PCComptabiliserViewBean() throws PropertiesException {
        setIdOrganeExecution(getOrganeExecProperty());
    }

    @Override
    public void add() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void delete() throws Exception {
        // TODO Auto-generated method stub

    }

    public String getCsEtatLot() {
        return csEtatLot;
    }

    public String getCsTypeLot() {
        return csTypeLot;
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

    public String geteMailAddress() {
        return eMailAddress;
    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return null;
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
     */

    public Vector getOrganesExecution() {
        Vector result = new Vector();
        CAOrganeExecution organeExecution = null;
        CAOrganeExecutionManager mgr = new CAOrganeExecutionManager();
        mgr.setSession((BSession) getISession());
        mgr.setForIdTypeTraitementOG(true);
        try {
            mgr.find();
        } catch (Exception e) {
            return result;
        }

        for (int i = 0; i < mgr.size(); i++) {
            organeExecution = (CAOrganeExecution) mgr.getEntity(i);
            result.add(new String[] { organeExecution.getIdOrganeExecution(), organeExecution.getNom(),
                    organeExecution.getCSTypeTraitementOG() });
        }
        return result;
    }

    @Override
    public BSpy getSpy() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void retrieve() throws Exception {
        // TODO Auto-generated method stub

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

    public void seteMailAddress(String eMailAddress) {
        this.eMailAddress = eMailAddress;
    }

    @Override
    public void setId(String newId) {
        // TODO Auto-generated method stub

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

    public String getIsoHightPriority() {
        return isoHightPriority;
    }

    public void setIsoHightPriority(String isoHightPriority) {
        this.isoHightPriority = isoHightPriority;
    }

    @Override
    public void update() throws Exception {
        // TODO Auto-generated method stub

    }

    private String getOrganeExecProperty() throws PropertiesException {
        return REProperties.ORGANE_EXECUTION_PAIEMENT.getValue();
    }

}
