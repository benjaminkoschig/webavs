package globaz.al.vb.dossier;

import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.prestation.EntetePrestationSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;

/**
 * ViewBean représentant un dossier complet utilisé dans l'écran complément dossier (AL0005), n'inclut pas l'allocataire
 * 
 * @author GMO
 * 
 */
public class ALDossierViewBean extends BJadePersistentObjectViewBean {

    /**
     * Le modèle du dossier utilisé dans l'écran complément dossier
     */
    private DossierComplexModel dossierComplexModel = null;

    /**
     * Contient la valeur de l'état du dossier en entrant dans l'écran
     */
    private String etatDossierAvantModification = null;

    /**
     * Constructeur du viewBean
     */
    public ALDossierViewBean() {
        super();
        dossierComplexModel = new DossierComplexModel();
    }

    /**
     * Constructeur du viewBean
     * 
     * @param dossierComplexModel
     *            Le modèle du dossier à utiliser
     */
    public ALDossierViewBean(DossierComplexModel dossierComplexModel) {
        super();
        this.dossierComplexModel = dossierComplexModel;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        dossierComplexModel = ALServiceLocator.getDossierBusinessService().createDossier(dossierComplexModel);

    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        dossierComplexModel = ALServiceLocator.getDossierBusinessService().deleteDossier(dossierComplexModel);

    }

    /**
     * @return dossierComplexModel Le modèle du dossier utilisé
     */
    public DossierComplexModel getDossierComplexModel() {
        return dossierComplexModel;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {
        return dossierComplexModel.getId();
    }

    /**
     * @return session courante
     */
    public BSession getSession() {
        return (BSession) getISession();
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        return (dossierComplexModel != null) && !dossierComplexModel.isNew() ? new BSpy(dossierComplexModel.getSpy())
                : new BSpy(getSession());
    }

    public boolean hasAnnonceRafam() throws Exception {
        if (dossierComplexModel.isNew()) {
            return false;
        } else {
            return ALServiceLocator.getDossierBusinessService().hasSentAnnonces(getId());
        }
    }

    public boolean hasPrestations() throws Exception {
        if (dossierComplexModel.isNew()) {
            return false;
        } else {

            EntetePrestationSearchModel searchEntete = new EntetePrestationSearchModel();
            searchEntete.setForIdDossier(dossierComplexModel.getId());
            searchEntete.setOrderKey("periode");
            searchEntete = ALServiceLocator.getEntetePrestationModelService().search(searchEntete);
            if (searchEntete.getSize() > 0) {
                return true;
            } else {
                return false;
            }
        }
    }

    public String renderHTMLSelectStatut() throws Exception {
        String html = "";

        String statutNselected = ALCSDossier.STATUT_N.equals(dossierComplexModel.getDossierModel().getStatut()) ? "selected"
                : "";
        String statutNPselected = ALCSDossier.STATUT_NP.equals(dossierComplexModel.getDossierModel().getStatut()) ? "selected"
                : "";
        String statutCPselected = ALCSDossier.STATUT_CP.equals(dossierComplexModel.getDossierModel().getStatut()) ? "selected"
                : "";
        String statutIPselected = ALCSDossier.STATUT_IP.equals(dossierComplexModel.getDossierModel().getStatut()) ? "selected"
                : "";
        String statutISselected = ALCSDossier.STATUT_IS.equals(dossierComplexModel.getDossierModel().getStatut()) ? "selected"
                : "";
        String statutCSelected = ALCSDossier.STATUT_CS.equals(dossierComplexModel.getDossierModel().getStatut()) ? "selected"
                : "";

        if (hasAnnonceRafam() || (hasPrestations())) {

            if (ALCSDossier.STATUT_N.equals(getDossierComplexModel().getDossierModel().getStatut())
                    || ALCSDossier.STATUT_NP.equals(getDossierComplexModel().getDossierModel().getStatut())
                    || ALCSDossier.STATUT_CP.equals(getDossierComplexModel().getDossierModel().getStatut())
                    || ALCSDossier.STATUT_IP.equals(getDossierComplexModel().getDossierModel().getStatut())) {

                html += "<select name=\"dossierComplexModel.dossierModel.statut\" data-g-select=\"\" tabindex=\"7\">";
                html += "<option " + statutNselected + " value='" + ALCSDossier.STATUT_N + "'>"
                        + getSession().getCode(ALCSDossier.STATUT_N) + "</option>";
                html += "<option " + statutNPselected + " value='" + ALCSDossier.STATUT_NP + "'>"
                        + getSession().getCode(ALCSDossier.STATUT_NP) + "</option>";
                html += "<option " + statutCPselected + " value='" + ALCSDossier.STATUT_CP + "'>"
                        + getSession().getCode(ALCSDossier.STATUT_CP) + "</option>";
                html += "<option " + statutIPselected + " value='" + ALCSDossier.STATUT_IP + "'>"
                        + getSession().getCode(ALCSDossier.STATUT_IP) + "</option>";
                html += "</select>";

            }

            else {
                html += "<input name=\"dossierComplexModel.dossierModel.statut\" class=\"small readOnly\" disabled=\"disabled\" readonly=\"readonly\"";
                html += " type=\"text\" value=\""
                        + getSession().getCode(getDossierComplexModel().getDossierModel().getStatut()) + "\" />";

            }
        } else {
            html += "<select name=\"dossierComplexModel.dossierModel.statut\" data-g-select=\"\" tabindex=\"7\">";
            html += "<option " + statutNselected + " value='" + ALCSDossier.STATUT_N + "'>"
                    + getSession().getCode(ALCSDossier.STATUT_N) + "</option>";
            html += "<option " + statutNPselected + " value='" + ALCSDossier.STATUT_NP + "'>"
                    + getSession().getCode(ALCSDossier.STATUT_NP) + "</option>";
            html += "<option " + statutCPselected + " value='" + ALCSDossier.STATUT_CP + "'>"
                    + getSession().getCode(ALCSDossier.STATUT_CP) + "</option>";
            html += "<option " + statutCSelected + " value='" + ALCSDossier.STATUT_CS + "'>"
                    + getSession().getCode(ALCSDossier.STATUT_CS) + "</option>";
            html += "<option " + statutIPselected + " value='" + ALCSDossier.STATUT_IP + "'>"
                    + getSession().getCode(ALCSDossier.STATUT_IP) + "</option>";
            html += "<option " + statutISselected + " value='" + ALCSDossier.STATUT_IS + "'>"
                    + getSession().getCode(ALCSDossier.STATUT_IS) + "</option>";
            html += "</select>";

        }
        return html;

    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        dossierComplexModel = ALServiceLocator.getDossierComplexModelService().read(getId());
        etatDossierAvantModification = dossierComplexModel.getDossierModel().getEtatDossier();

    }

    /**
     * @param dossierComplexModel
     *            Modèle du dossier à utiliser
     */
    public void setDossierComplexModel(DossierComplexModel dossierComplexModel) {
        this.dossierComplexModel = dossierComplexModel;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
        dossierComplexModel.setId(newId);

    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        dossierComplexModel = ALServiceLocator.getDossierBusinessService().updateDossier(dossierComplexModel,
                etatDossierAvantModification, "");

    }

}
