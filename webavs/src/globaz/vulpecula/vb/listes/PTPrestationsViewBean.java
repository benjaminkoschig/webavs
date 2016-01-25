package globaz.vulpecula.vb.listes;

import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BSpy;
import globaz.jade.context.JadeThread;
import globaz.musca.db.facturation.FAModuleFacturation;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import ch.globaz.common.vb.BJadeSearchObjectELViewBean;
import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.employeur.EmployeurServiceCRUD;
import ch.globaz.vulpecula.business.services.passagefacturation.PassageFacturationServiceCRUD;
import ch.globaz.vulpecula.business.services.travailleur.TravailleurServiceCRUD;
import ch.globaz.vulpecula.domain.models.registre.Convention;
import ch.globaz.vulpecula.util.I18NUtil;
import ch.globaz.vulpecula.web.util.FormUtil;

public class PTPrestationsViewBean extends BJadeSearchObjectELViewBean {
    private List<Convention> conventions;

    private String email;
    private String idPassageFacturation;
    private String idTravailleur;
    private String idEmployeur;
    private String idConvention;
    private String typePrestation;
    private String periodeDebut;
    private String periodeFin;
    private String destinataire;

    private String designationPassageFacturation;
    private String designationEmployeur;
    private String designationTravailleur;

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void retrieve() throws Exception {
        conventions = VulpeculaRepositoryLocator.getConventionRepository().findAll();
    }

    @Override
    public void setId(String newId) {
    }

    @Override
    public BSpy getSpy() {
        return null;
    }

    /**
     * Méthode utilisée par le framework pour setter l'email.
     * 
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Méthode utilisée par le framework pour setter l'id du passage de facturation.
     * 
     * @param idPassageFacturation String représentant un id de passage de facturation
     */
    public void setIdPassageFacturation(String idPassageFacturation) {
        this.idPassageFacturation = idPassageFacturation;
    }

    /**
     * Méthode utilisée par le framework pour setter l'id de l'employeur.
     * 
     * @param idEmployeur String représentant un id de l'employeur
     */
    public void setIdEmployeur(String idEmployeur) {
        this.idEmployeur = idEmployeur;
    }

    /**
     * Méthode utilisée par le framework pour setter l'id du travailleur.
     * 
     * @param idTravailleur String représentant un id de travailleur
     */
    public void setIdTravailleur(String idTravailleur) {
        this.idTravailleur = idTravailleur;
    }

    /**
     * Méthode utilisée par le framework pour setter l'id de la convention.
     * 
     * @param idTravailleur String représentant un id de travailleur
     */
    public void setIdConvention(String idConvention) {
        this.idConvention = idConvention;
    }

    /**
     * Méthode utilisée par le framework pour setter l'id de la convention.
     * 
     * @return email auquel le mail sera envoyé
     */

    public String getEmail() {
        if (email == null) {
            return JadeThread.currentUserEmail();
        }
        return email;
    }

    public String getIdPassageFacturation() {
        return idPassageFacturation;
    }

    public String getIdTravailleur() {
        return idTravailleur;
    }

    public String getIdEmployeur() {
        return idEmployeur;
    }

    public String getIdConvention() {
        return idConvention;
    }

    public String getTypePrestation() {
        return typePrestation;
    }

    public void setTypePrestation(String typePrestation) {
        this.typePrestation = typePrestation;
    }

    public String getPeriodeDebut() {
        return periodeDebut;
    }

    public void setPeriodeDebut(String periodeDebut) {
        this.periodeDebut = periodeDebut;
    }

    public String getPeriodeFin() {
        return periodeFin;
    }

    public void setPeriodeFin(String periodeFin) {
        this.periodeFin = periodeFin;
    }

    public String getPassageViewService() {
        return PassageFacturationServiceCRUD.class.getName();
    }

    public String getEmployeurViewService() {
        return EmployeurServiceCRUD.class.getName();
    }

    public String getTravailleurViewService() {
        return TravailleurServiceCRUD.class.getName();
    }

    public List<Convention> getConventions() {
        return conventions;
    }

    public String getDesignationPassageFacturation() {
        return designationPassageFacturation;
    }

    public void setDesignationPassageFacturation(String designationPassageFacturation) {
        this.designationPassageFacturation = designationPassageFacturation;
    }

    public String getDesignationEmployeur() {
        return designationEmployeur;
    }

    public void setDesignationEmployeur(String designationEmployeur) {
        this.designationEmployeur = designationEmployeur;
    }

    public String getDesignationTravailleur() {
        return designationTravailleur;
    }

    public final String getDestinataire() {
        return destinataire;
    }

    public final void setDestinataire(String destinataire) {
        this.destinataire = destinataire;
    }

    public void setDesignationTravailleur(String designationTravailleur) {
        this.designationTravailleur = designationTravailleur;
    }

    public Vector<String[]> getTypesModules() {
        List<String> types = Arrays.asList(FAModuleFacturation.CS_MODULE_ABSENCES_JUSTIFIEES,
                FAModuleFacturation.CS_MODULE_CONGE_PAYE, FAModuleFacturation.CS_MODULE_SERVICE_MILITAIRE);
        return FormUtil.getList(types);
    }

    public String getMessagePeriodeOuLotRequis() {
        return SpecificationMessage.getMessage(I18NUtil.getUserLocale(),
                SpecificationMessage.PRESTATIONS_LISTES_PERIODE_OU_LOT_REQUIS);
    }

    public String getMessagePeriodeOuLot() {
        return SpecificationMessage.getMessage(I18NUtil.getUserLocale(),
                SpecificationMessage.PRESTATIONS_LISTES_PERIODE_OU_LOT);
    }

    public String getMessagePeriodeFinPlusGrandDebut() {
        return SpecificationMessage.getMessage(I18NUtil.getUserLocale(),
                SpecificationMessage.PRESTATIONS_LISTES_PERIODE_FIN_PLUS_GRAND_DEBUT);
    }

    public String getMessageProcessusLance() {
        return BSessionUtil.getSessionFromThreadContext().getLabel("LISTE_PROCESSUS_LANCE");
    }

}
