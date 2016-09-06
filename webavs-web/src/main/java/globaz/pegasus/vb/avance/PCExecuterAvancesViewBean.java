package globaz.pegasus.vb.avance;

import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.osiris.api.ordre.APIOrdreGroupe;
import globaz.osiris.db.ordres.CAOrganeExecution;
import globaz.osiris.db.ordres.CAOrganeExecutionManager;
import java.rmi.RemoteException;
import java.util.Vector;

public class PCExecuterAvancesViewBean extends BJadePersistentObjectViewBean {

    private String dateEcheance = null;

    private String email = null;

    private String idOrganeExecution = null;
    private String noOg = null;
    private String typeTraitement = null;

    // SEPA iso20002
    private String isoCsTypeAvis = APIOrdreGroupe.ISO_TYPE_AVIS_COLLECT_SANS;
    private String isoGestionnaire = ((BSession) getISession()).getUserName();
    private String isoHightPriority;

    @Override
    public void add() throws Exception {
    }

    @Override
    public void delete() throws Exception {
        // TODO Auto-generated method stub

    }

    public String getDateEcheance() {
        return dateEcheance;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getIdOrganeExecution() {
        return idOrganeExecution;
    }

    public String getNoOg() {
        return noOg;
    }

    /**
     * Retourne le vecteur de tableaux a 2 entrées {idOrganeExecution, description} défini pour ce view bean.
     * 
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

    @Override
    public BSpy getSpy() {
        System.out.println();
        return null;
    }

    public String getTypeTraitement() {
        return typeTraitement;
    }

    @Override
    public void retrieve() throws Exception {
        // TODO Auto-generated method stub
        System.out.println();
    }

    public void setDateEcheance(String dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public void setId(String newId) {
        System.out.println();

    }

    public void setIdOrganeExecution(String idOrganeExecution) {
        this.idOrganeExecution = idOrganeExecution;
    }

    public void setNoOg(String noOg) {
        this.noOg = noOg;
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

    @Override
    public void update() throws Exception {
        System.out.println();

    }

}
