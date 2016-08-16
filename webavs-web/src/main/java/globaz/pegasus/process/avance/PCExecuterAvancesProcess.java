package globaz.pegasus.process.avance;

import globaz.corvus.api.avances.IREAvances;
import globaz.corvus.helpers.process.REGenererAvancesProcess;
import globaz.corvus.process.REExecuter1erAcompteAvancesProcess;
import globaz.pegasus.process.PCAbstractJob;

public class PCExecuterAvancesProcess extends PCAbstractJob {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateEchance = null;
    private String email = null;
    private String idOrganeExecution = null;
    private String noNog = null;
    private String typeTraitement = null;

    // SEPA iso20002
    private String isoCsTypeAvis;
    private String isoGestionnaire;
    private String isoHightPriority;

    public String getDateEchance() {
        return dateEchance;
    }

    @Override
    public String getDescription() {
        // TODO Auto-generated method stub
        return null;

    }

    public String getEmail() {
        return email;
    }

    public String getIdOrganeExecution() {
        return idOrganeExecution;
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getNoNog() {
        return noNog;
    }

    public String getTypeTraitement() {
        return typeTraitement;
    }

    @Override
    protected void process() throws Exception {

        if (typeTraitement.equals(IREAvances.CS_TYPE_ACOMPTES_UNIQUE)) {
            REExecuter1erAcompteAvancesProcess process = new REExecuter1erAcompteAvancesProcess();
            process.setSession(getSession());
            process.setEMailAddress(email);
            process.setCsDomaineApplicatif(IREAvances.CS_DOMAINE_AVANCE_PC);
            process.setNoOg(getNoNog());
            process.setIdOrganeExecution(getIdOrganeExecution());
            process.setDateEcheance(getDateEchance());
            process.setIsoCsTypeAvis(isoCsTypeAvis);
            process.setIsoGestionnaire(isoGestionnaire);
            process.setIsoHightPriority(isoHightPriority);
            process.start();
        } else if (typeTraitement.equals(IREAvances.CS_TYPE_LISTES)) {
            REGenererAvancesProcess process = new REGenererAvancesProcess();
            process.setSession(null);
            process.setEMailAddress(email);
            process.start();
        }

    }

    public void setDateEchance(String dateEchance) {
        this.dateEchance = dateEchance;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setIdOrganeExecution(String idOrganeExecution) {
        this.idOrganeExecution = idOrganeExecution;
    }

    public void setNoNog(String noNog) {
        this.noNog = noNog;
    }

    public void setTypeTraitement(String typeTraitement) {
        this.typeTraitement = typeTraitement;
    }

    public String getIsoCsTypeAvis() {
        return isoCsTypeAvis;
    }

    public void setIsoCsTypeAvis(String isoCsTypeAvis) {
        this.isoCsTypeAvis = isoCsTypeAvis;
    }

    public String getIsoGestionnaire() {
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

}
