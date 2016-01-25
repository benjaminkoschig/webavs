package globaz.perseus.vb.decision;

import globaz.externe.IPRConstantesExternes;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BSpy;
import globaz.globall.util.JACalendar;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.perseus.utils.PFAgenceCommunaleHelper;
import globaz.perseus.utils.PFUserHelper;
import globaz.prestation.utils.ged.PRGedUtils;
import globaz.pyxis.constantes.IConstantes;
import java.util.Calendar;
import java.util.Vector;
import ch.globaz.perseus.business.constantes.IPFConstantes;
import ch.globaz.perseus.business.models.dossier.Dossier;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.businessimpl.services.document.BabelContainer;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;

/**
 * ViewBean derrière l'écran de détail sur les décisions.
 * 
 * @author MBO
 * 
 */

public class PFDecisionRefusDemandeViewBean extends BJadePersistentObjectViewBean {

    private String adresseCourrier = null;
    private String agenceAssurance = null;
    private BabelContainer babelContainer = new BabelContainer();
    private String caisse = null;
    private String dateDocument = null;
    private String detailAssure = null;
    private Dossier dossier = null;
    private String eMailAdresse = null;
    private String gestionnaire = null;
    private String idDomaineApplicatifAdresseCourrier = null;
    private String idDossier = null;
    private String idTiersAdresseCourrier = null;
    private String isSendToGed = null;
    private Vector listeAgenceAssurance = null;
    private Vector listeCaisse = null;

    public PFDecisionRefusDemandeViewBean() {
        super();

    }

    @Override
    public void add() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void delete() throws Exception {
        // TODO Auto-generated method stub
    }

    public String getAdresseCourrier() {
        return adresseCourrier;
    }

    public String getAgenceAssurance() {
        return agenceAssurance;
    }

    public BabelContainer getBabelContainer() {
        return babelContainer;
    }

    public String getCaisse() {
        return caisse;
    }

    // Methode permettant d'obtenir la date du jour pour le document
    public String getDateDocument() {
        if (JadeStringUtil.isEmpty(dateDocument)) {
            dateDocument = getDateDocumentDefault();
        }
        return dateDocument;
    }

    // Methode qui permet d'obtenir tous les jeudi de chaque semaine, en fonction de la date du jour.
    public String getDateDocumentDefault() {
        Calendar c = Calendar.getInstance();
        return JadeDateUtil.getGlobazFormattedDate(c.getTime());
    }

    // Methode permettant d'obtenir le nom et prenom du requerant
    public String getDetailAssure() throws JadePersistenceException, JadeApplicationException {

        String nss = dossier.getDemandePrestation().getPersonneEtendue().getPersonneEtendue().getNumAvsActuel();
        String nomPrenom = dossier.getDemandePrestation().getPersonneEtendue().getTiers().getDesignation1() + " "
                + dossier.getDemandePrestation().getPersonneEtendue().getTiers().getDesignation2();
        String dateNaissance = dossier.getDemandePrestation().getPersonneEtendue().getPersonne().getDateNaissance();
        String idTiers = dossier.getDemandePrestation().getPersonneEtendue().getTiers().getIdTiers();

        AdresseTiersDetail adr = PFUserHelper.getAdresseAssure(idTiers, null, null, JACalendar.todayJJsMMsAAAA());
        String adresse = AdresseTiersDetail.ADRESSE_VAR_RUE + " , " + AdresseTiersDetail.ADRESSE_VAR_NPA + " - "
                + AdresseTiersDetail.ADRESSE_VAR_LOCALITE;
        detailAssure = nss + " / " + nomPrenom + " / " + dateNaissance;
        return detailAssure;
    }

    public Dossier getDossier() {
        return dossier;
    }

    public String geteMailAdresse() {
        return eMailAdresse;

    }

    public String getGestionnaire() {
        return gestionnaire;
    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getIdDomaineApplicatifAdresseCourrier() {
        return idDomaineApplicatifAdresseCourrier;
    }

    public String getIdDossier() {
        return idDossier;
    }

    public String getIdTiersAdresseCourrier() {
        return idTiersAdresseCourrier;
    }

    public String getIsSendToGed() {
        return isSendToGed;
    }

    public Vector getListeAgenceAssurance() {
        return listeAgenceAssurance;
    }

    public Vector getListeCaisse() {
        return listeCaisse;
    }

    // TODO Récupération de la session user
    protected BSession getSession() {
        return BSessionUtil.getSessionFromThreadContext();
    }

    @Override
    public BSpy getSpy() {
        // TODO Auto-generated method stub
        return null;
    }

    public void init() throws Exception {
        dossier = PerseusServiceLocator.getDossierService().read(idDossier);
        // this.getDateDocumentDefault(); Pourquoi ca ici ?
        setListeAgenceAssurance(PFAgenceCommunaleHelper.getAgencesList());
        // this.setListeCaisse(PFAgenceCommunaleHelper.getRiList()); Pourquoi tu remplis la liste des caisses ave les
        // agences du RI ?
        // Adresse de courrier par défaut
        idTiersAdresseCourrier = dossier.getDemandePrestation().getPersonneEtendue().getTiers().getId();
        idDomaineApplicatifAdresseCourrier = IPFConstantes.CS_DOMAINE_ADRESSE;

        AdresseTiersDetail detailTiers = null;
        if (!JadeStringUtil.isEmpty(idTiersAdresseCourrier)) {
            detailTiers = PFUserHelper.getAdresseAssure(idTiersAdresseCourrier, IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                    idDomaineApplicatifAdresseCourrier, JACalendar.todayJJsMMsAAAA());
        }

        adresseCourrier = detailTiers != null ? detailTiers.getAdresseFormate() : "";
    }

    public boolean isSendToGed(String csCaisse) {
        if (null == csCaisse) {
            return false;
        } else {
            return PRGedUtils.isDocumentInGed(IPRConstantesExternes.PCF_FACTURE_REFUS_DEMANDE, csCaisse, getSession());
        }
    }

    @Override
    public void retrieve() throws Exception {
        init();
    }

    public void setAdresseCourrier(String adresseCourrier) {
        this.adresseCourrier = adresseCourrier;
    }

    public void setAgenceAssurance(String agenceAssurance) {
        this.agenceAssurance = agenceAssurance;
    }

    public void setBabelContainer(BabelContainer babelContainer) {
        this.babelContainer = babelContainer;
    }

    public void setCaisse(String caisse) {
        this.caisse = caisse;
    }

    public void setDateDocument(String dateDocument) {
        this.dateDocument = dateDocument;
    }

    public void setDetailAssure(String detailAssure) {
        this.detailAssure = detailAssure;
    }

    public void setDossier(Dossier dossier) {
        this.dossier = dossier;
    }

    public void seteMailAdresse(String eMailAdresse) {
        this.eMailAdresse = eMailAdresse;
    }

    public void setGestionnaire(String gestionnaire) {
        this.gestionnaire = gestionnaire;
    }

    @Override
    public void setId(String newId) {
        // TODO Auto-generated method stub

    }

    public void setIdDomaineApplicatifAdresseCourrier(String idDomaineApplicatifAdresseCourrier) {
        this.idDomaineApplicatifAdresseCourrier = idDomaineApplicatifAdresseCourrier;
    }

    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    public void setIdTiersAdresseCourrier(String idTiersAdresseCourrier) {
        this.idTiersAdresseCourrier = idTiersAdresseCourrier;
    }

    public void setIsSendToGed(String isSendToGed) {
        this.isSendToGed = isSendToGed;
    }

    public void setListeAgenceAssurance(Vector listeAgenceAssurance) {
        this.listeAgenceAssurance = listeAgenceAssurance;
    }

    public void setListeCaisse(Vector listeCaisse) {
        this.listeCaisse = listeCaisse;
    }

    @Override
    public void update() throws Exception {
        // TODO Auto-generated method stub

    }

}
