package globaz.pegasus.vb.demande;

import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.pegasus.utils.PCUserHelper;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.nnss.PRNSSUtil;
import java.util.ArrayList;
import ch.globaz.hera.business.services.HeraServiceLocator;
import ch.globaz.hera.business.vo.famille.MembreFamilleVO;
import ch.globaz.pegasus.business.models.demande.Demande;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.model.PersonneEtendueSimpleModel;
import ch.globaz.pyxis.business.model.PersonneSimpleModel;
import ch.globaz.pyxis.business.model.TiersSimpleModel;

/**
 * @author sce
 * @date 11.08.2011
 */
public class PCFratrieViewBean extends BJadePersistentObjectViewBean {

    private Demande demande = null;
    private ArrayList<MembreFamilleVO> fratrie = null;
    private String idDemandePc = null;
    private String idTiers = null;
    private ArrayList<String> listSelected = new ArrayList<String>();

    public PCFratrieViewBean() {
        super();
    }

    @Override
    public void add() throws Exception {
        ArrayList<MembreFamilleVO> listeSelectionne = new ArrayList<MembreFamilleVO>();

        // iteration sur les membres de base
        for (MembreFamilleVO membre : fratrie) {
            // iterations sur la selection
            if (listSelected.contains(membre.getIdMembreFamille())
                    || membre.getIdTiers().equals(getIdTiersRequerantEnfant())) {
                listeSelectionne.add(membre);
            }
        }

        // appel au service
        PegasusServiceLocator.getDroitService().createDroitInitialForFratrie(demande, listeSelectionne);
    }

    @Override
    public void delete() throws Exception {
        // TODO Auto-generated method stub

    }

    /**
     * Retourne le tiers sous forme de chaine formatéée pour affichage dans la jsp
     * 
     * @param tiersFratrie
     * @return
     */
    public String formatTiersFratrieAsString(MembreFamilleVO tiersFratrie) {
        String NSS = tiersFratrie.getNss();
        String NomPrenom = tiersFratrie.getNom() + " " + tiersFratrie.getPrenom();
        String dateNaissance = tiersFratrie.getDateNaissance();
        String sexe = getSession().getCodeLibelle(tiersFratrie.getCsSexe());
        String nationalite = getSession().getCodeLibelle(tiersFratrie.getCsNationalite());// PCUserHelper.getLibellePays(this.getSession(),
                                                                                          // this.getPersonneEtendueComplex());//
                                                                                          // this.getSession().getCodeLibelle(

        String reqInfos = PRNSSUtil.formatDetailRequerantDetail(NSS, NomPrenom, dateNaissance, sexe, nationalite);

        return reqInfos;
    }

    public Demande getDemande() {
        return demande;
    }

    public ArrayList<MembreFamilleVO> getFratrie() {
        return fratrie;
    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getIdDemandPc() {
        return idDemandePc;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getIdTiersRequerantEnfant() {
        return demande.getDossier().getDemandePrestation().getDemandePrestation().getIdTiers();
    }

    public ArrayList getListSelected() {
        return listSelected;
    }

    /**
     * Retourne le modele simple de la personne
     * 
     * @return personneSimpleModel
     */
    private PersonneSimpleModel getPersonne() {
        // Requerant d'apres dossier
        // Requerant d'apres le dossier
        return getPersonneEtendueComplex().getPersonne();

    }

    /**
     * Retourne la personne etendue (requerant)
     * 
     * @return
     */
    private PersonneEtendueSimpleModel getPersonneEtendue() {
        // Requerant d'apres le dossier
        return getPersonneEtendueComplex().getPersonneEtendue();
    }

    /**
     * Retourne le modele complexe personne etendue
     * 
     * @param membre
     * @return
     */
    private PersonneEtendueComplexModel getPersonneEtendueComplex() {
        // Requerant d'apres le dossier
        return demande.getDossier().getDemandePrestation().getPersonneEtendue();
    }

    /**
     * Formatte une chaine de caratere pour afficher les infos du requérant
     * 
     * @return
     */
    public String getRequerantInfosAsString() {

        // String membre = IPCDroits.CS_ROLE_FAMILLE_REQUERANT;
        String NSS = getPersonneEtendue().getNumAvsActuel();
        String NomPrenom = getTiers().getDesignation1() + " " + getTiers().getDesignation2();
        String dateNaissance = getPersonne().getDateNaissance();
        String sexe = getSession().getCodeLibelle(getPersonne().getSexe());
        String nationalite = PCUserHelper.getLibellePays(getPersonneEtendueComplex());// this.getSession().getCodeLibelle(

        String reqInfos = PRNSSUtil.formatDetailRequerantDetail(NSS, NomPrenom, dateNaissance, sexe, nationalite);

        return reqInfos;
    }

    /**
     * Retourne la session
     * 
     * @return objet BSession
     */
    public BSession getSession() {
        return (BSession) getISession();
    }

    @Override
    public BSpy getSpy() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Retourne le Tiers
     * 
     * @return
     */
    private TiersSimpleModel getTiers() {
        // requerant d'apres dossier
        return getPersonneEtendueComplex().getTiers();
    }

    @Override
    public void retrieve() throws Exception {
        // Chargememnt de la demande
        demande = PegasusServiceLocator.getDemandeService().read(idDemandePc);

        // NSS du requérant-->enfant
        String nssEnfantReq = demande.getDossier().getDemandePrestation().getPersonneEtendue().getPersonneEtendue()
                .getNumAvsActuel();

        PRTiersWrapper tiersFils = PRTiersHelper.getTiers(getSession(), nssEnfantReq);
        String idTiersFils = tiersFils.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS);

        fratrie = HeraServiceLocator.getMembreFamilleService().getFamilleByIDEnfant(idTiersFils, true);

    }

    public void setDemande(Demande demande) {
        this.demande = demande;
    }

    public void setFratrie(ArrayList<MembreFamilleVO> fratrie) {
        this.fratrie = fratrie;
    }

    @Override
    public void setId(String newId) {
        // TODO Auto-generated method stub

    }

    public void setIdDemandePc(String idDemandePc) {
        this.idDemandePc = idDemandePc;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setListSelected(ArrayList listSelected) {
        this.listSelected = listSelected;
    }

    @Override
    public void update() throws Exception {
        // TODO Auto-generated method stub

    }

}
