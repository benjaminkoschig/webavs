package globaz.pegasus.vb.demanderenseignement;

import globaz.babel.api.ICTDocument;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pegasus.utils.PCUserHelper;
import globaz.prestation.tools.nnss.PRNSSUtil;
import java.util.ArrayList;
import java.util.Map;
import ch.globaz.babel.business.services.BabelServiceLocator;
import ch.globaz.common.business.language.LanguageResolver;
import ch.globaz.common.business.models.CTDocumentImpl;
import ch.globaz.jade.business.models.Langues;
import ch.globaz.pegasus.business.constantes.demanderenseignement.IPCDemandeRenseignementBuilderType;
import ch.globaz.pegasus.business.exceptions.models.demanderenseignement.DemandeRenseignementException;
import ch.globaz.pegasus.business.models.demande.Demande;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.utils.PegasusUtil;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.model.TiersSimpleModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

public class PCDemandeRenseignementViewBean extends BJadePersistentObjectViewBean {

    private String agenceInfo = null;
    private ArrayList<String> annexes = null;
    private ArrayList<String> copies = null;
    private String csTypeDemande = null;
    private String defaultAnnexe = null;
    private String idDemandePc = null;
    private String idDossier = null;
    private String idGestionnaire = null;
    private String idRequerant = null;
    private String mailAddress = null;

    private String nomPrenomRequerant;

    private PersonneEtendueComplexModel requerant;

    private String zoneTexteLibre = null;

    @Override
    public void add() throws Exception {
        // TODO Auto-generated method stub

    }

    private String buildDefaultAnnexe() throws DemandeRenseignementException {
        final ICTDocument babelDoc;
        try {
            Langues langueTiers = LanguageResolver.resolveISOCode(requerant.getTiers().getLangue());
            Map<Langues, CTDocumentImpl> documentsBabel = BabelServiceLocator.getPCCatalogueTexteService()
                    .searchForDemandeRenseignement(IPCDemandeRenseignementBuilderType.BABEL_COMMUN);
            babelDoc = documentsBabel.get(langueTiers);
        } catch (Exception e) {
            throw new DemandeRenseignementException("Error while loading catalogue Babel!", e);
        }

        defaultAnnexe = babelDoc.getTextes(1).getTexte(4).getDescription();
        return defaultAnnexe;
    }

    @Override
    public void delete() throws Exception {
        // TODO Auto-generated method stub

    }

    public String getAgenceInfo() {
        return agenceInfo;
    }

    public ArrayList<String> getAnnexes() {
        return annexes;
    }

    public ArrayList<String> getCopies() {
        return copies;
    }

    public String getCsTypeDemande() {
        return csTypeDemande;
    }

    public String getDefaultAnnexe() {
        if (defaultAnnexe != null) {
            return defaultAnnexe;
        }
        return "";

    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getIdDemandePc() {
        return idDemandePc;
    }

    public String getIdDossier() {
        return idDossier;
    }

    public String getIdGestionnaire() {
        return idGestionnaire;
    }

    public String getIdRequerant() {
        return idRequerant;
    }

    public String getMailAddress() {
        return mailAddress;
    }

    public String getNomPrenomRequerant() {
        return nomPrenomRequerant;
    }

    /**
     * Formatte une chaine de caratere pour afficher les infos du requérant
     * 
     * @return
     */
    public String getRequerantInfos() {

        TiersSimpleModel tiers = requerant.getTiers();

        String NSS = requerant.getPersonneEtendue().getNumAvsActuel();
        String NomPrenom = tiers.getDesignation1() + " " + tiers.getDesignation2();
        String dateNaissance = requerant.getPersonne().getDateNaissance();
        String sexe = getSession().getCodeLibelle(requerant.getPersonne().getSexe());
        String nationalite = PCUserHelper.getLibellePays(requerant);// this.getSession().getCodeLibelle(

        String reqInfos = PRNSSUtil.formatDetailRequerantDetail(NSS, NomPrenom, dateNaissance, sexe, nationalite);

        return reqInfos;
    }

    /**
     * Retourne l'objet session
     * 
     * @return objet BSession
     */
    public BSession getSession() {
        return (BSession) getISession();
    }

    @Override
    public BSpy getSpy() {
        // return new BSpy(this.getSession());
        return null;
    }

    public String getZoneTexteLibre() {
        return zoneTexteLibre;
    }

    @Override
    public void retrieve() throws Exception {

        Demande demande = PegasusServiceLocator.getDemandeService().read(idDemandePc);
        requerant = demande.getDossier().getDemandePrestation().getPersonneEtendue();
        String idAgence = TIBusinessServiceLocator.getAdministrationService().getAgenceCommunalAVSIdTiers(
                requerant.getTiers().getIdTiers());

        if (JadeStringUtil.isBlankOrZero(idAgence)) {
            agenceInfo = "Agence not found!";
        } else {
            AdministrationComplexModel agence = TIBusinessServiceLocator.getAdministrationService().read(idAgence);
            agenceInfo = agence.getTiers().getDesignation1() + " " + agence.getTiers().getDesignation2();
        }

        annexes = new ArrayList<String>();
        copies = new ArrayList<String>();

        annexes.add(buildDefaultAnnexe());
        idRequerant = requerant.getTiers().getIdTiers();
        nomPrenomRequerant = PegasusUtil.formatNomPrenom(requerant.getTiers());

    }

    public void setAgenceInfo(String agenceInfo) {
        this.agenceInfo = agenceInfo;
    }

    public void setAnnexes(ArrayList annexes) {
        this.annexes = annexes;
    }

    public void setCopies(ArrayList copies) {
        this.copies = copies;
    }

    public void setCsTypeDemande(String csTypeDemande) {
        this.csTypeDemande = csTypeDemande;
    }

    @Override
    public void setId(String newId) {
        // TODO Auto-generated method stub

    }

    public void setIdDemandePc(String idDemandePc) {
        this.idDemandePc = idDemandePc;
    }

    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    public void setIdGestionnaire(String idGestionnaire) {
        this.idGestionnaire = idGestionnaire;
    }

    public void setIdRequerant(String idRequerant) {
        this.idRequerant = idRequerant;
    }

    public void setMailAddress(String eMailAddress) {
        mailAddress = eMailAddress;
    }

    public void setNomPrenomRequerant(String nomPrenomRequerant) {
        this.nomPrenomRequerant = nomPrenomRequerant;
    }

    public void setZoneTexteLibre(String zoneTexteLibre) {
        this.zoneTexteLibre = zoneTexteLibre;
    }

    @Override
    public void update() throws Exception {
        // TODO Auto-generated method stub

    }

}
