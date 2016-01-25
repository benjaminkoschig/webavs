/*
 * Créé le 8 sept. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.hera.db.famille;

import globaz.globall.db.BSession;
import globaz.globall.util.JAStringFormatter;
import globaz.jade.client.util.JadeStringUtil;
import java.io.Serializable;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * DOCUMENT ME!
 * </p>
 * 
 * @author mmu
 * 
 *         <p>
 *         Basé sur le même model que globaz.apg.vb.droits.APDroitDTO
 *         </p>
 * 
 *         Contient les données du réquerant sur lequel les écrans travaillent
 */
public class SFRequerantDTO implements Serializable {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csCantonDomicile = "";
    private String csNationalite = "";
    private String csSexe = "";
    private String dateDeces = "";
    private String dateNaissance = "";
    private String idDomaineApplication = "";
    private String idMembreFamille = "";
    private String idRequerant = "";
    private String idTiers = "";
    private String nom = "";
    private String nss = "";
    private String pays = "";
    private String prenom = "";
    private String provenance = "";
    private BSession session;

    private boolean wantFamille = false;

    /**
     * Crée une nouvelle instance de la classe SFRequerantDTO.
     */
    public SFRequerantDTO() {
    }

    /**
     * Crée une nouvelle instance de la classe SFRequerantDTO.
     * 
     * @param requerant
     *            DOCUMENT ME!
     */
    public SFRequerantDTO(SFApercuRequerant requerant) {
        idRequerant = requerant.getIdRequerant();
        idDomaineApplication = requerant.getIdDomaineApplication();
        idMembreFamille = requerant.getIdMembreFamille();
        idTiers = requerant.getIdTiers();
        nom = requerant.getNom();
        prenom = requerant.getPrenom();
        nss = requerant.getNss();
        dateNaissance = requerant.getDateNaissance();
        dateDeces = requerant.getDateDeces();
        csSexe = requerant.getCsSexe();
        csCantonDomicile = requerant.getCsCantonDomicile();
        csNationalite = requerant.getCsNationalite();
        pays = requerant.getPays();
        session = requerant.getSession();
        provenance = requerant.getProvenance();

    }

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe SFRequerantDTO.
     * 
     * @param requerant
     *            DOCUMENT ME!
     */
    public SFRequerantDTO(SFRequerant requerant, SFMembreFamille membre) {
        idRequerant = requerant.getIdRequerant();
        idDomaineApplication = requerant.getIdDomaineApplication();
        idMembreFamille = requerant.getIdMembreFamille();
        provenance = requerant.getProvenance();
        idTiers = membre.getIdTiers();
        nom = membre.getNom();
        prenom = membre.getPrenom();
        nss = membre.getNss();
        dateNaissance = membre.getDateNaissance();
        dateDeces = membre.getDateDeces();
        csSexe = membre.getCsSexe();
        csCantonDomicile = membre.getCsCantonDomicile();
        csNationalite = membre.getCsNationalite();
        pays = membre.getPays();
        session = membre.getSession();
    }

    /**
     * Crée une nouvelle instance de la classe SFRequerantDTO.
     * 
     * @param requerantDTO
     *            DOCUMENT ME!
     */
    public SFRequerantDTO(SFRequerantDTO requerantDTO) {
        idRequerant = requerantDTO.getIdRequerant();
        idMembreFamille = requerantDTO.getIdMembreFamille();
        idTiers = requerantDTO.getIdTiers();
        nom = requerantDTO.getNom();
        prenom = requerantDTO.getPrenom();
        nss = requerantDTO.getNss();
        dateNaissance = requerantDTO.getDateNaissance();
        csSexe = requerantDTO.getCsSexe();
        dateDeces = requerantDTO.getDateDeces();
        csCantonDomicile = requerantDTO.getCsCantonDomicile();
        csNationalite = requerantDTO.getCsNationalite();
        pays = requerantDTO.getPays();
        session = requerantDTO.getSession();
        provenance = requerantDTO.getProvenance();
    }

    /**
     * DOCUMENT ME!
     * 
     * @return
     */
    public String getCsCantonDomicile() {
        return csCantonDomicile;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return
     */
    public String getCsNationalite() {
        return csNationalite;
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     * 
     * @return
     */
    public String getCsSexe() {
        return csSexe;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return
     */
    public String getDateDeces() {
        return dateDeces;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return
     */
    public String getDateNaissance() {
        return dateNaissance;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return
     */
    public String getIdDomaineApplication() {
        return idDomaineApplication;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return
     */
    public String getIdMembreFamille() {
        return idMembreFamille;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return
     */
    public String getIdRequerant() {
        return idRequerant;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return
     */
    public String getIdTiers() {
        return idTiers;
    }

    public String getLibelleNationalite() {
        return getSession().getCodeLibelle(getSession().getSystemCode("CIPAYORI", getCsNationalite()));
    }

    public String getLibellePays() {

        String pays = "";

        if (!globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(getPays())) {
            pays = getPays();
        } else {
            pays = getCsNationalite();
        }

        return getSession().getCodeLibelle(getSession().getSystemCode("CIPAYORI", pays));
    }

    public String getLibelleSexe() {
        return getSession().getCodeLibelle(getCsSexe());
    }

    /**
     * DOCUMENT ME!
     * 
     * @return
     */
    public String getNom() {
        return nom;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return
     */
    public String getNss() {
        return nss;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return
     */
    public String getPays() {
        return pays;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return
     */
    public String getPrenom() {
        return prenom;
    }

    public String getProvenance() {
        return provenance;
    }

    /**
     * @return
     */
    public BSession getSession() {
        return session;
    }

    public String getVisibleNoAvs() {
        return JAStringFormatter.unFormatAVS(JadeStringUtil.isEmpty(nss) ? "00000000000" : nss, "");
    }

    /**
     * @return
     */
    public boolean getWantFamille() {
        return wantFamille;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param string
     */
    public void setCsCantonDomicile(String string) {
        csCantonDomicile = string;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param string
     */
    public void setCsNationalite(String string) {
        csNationalite = string;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param string
     */
    public void setCsSexe(String string) {
        csSexe = string;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param string
     */
    public void setDateDeces(String string) {
        dateDeces = string;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param string
     */
    public void setDateNaissance(String string) {
        dateNaissance = string;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param string
     */
    public void setIdDomaineApplication(String string) {
        idDomaineApplication = string;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param string
     */
    public void setIdMembreFamille(String string) {
        idMembreFamille = string;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param string
     */
    public void setIdRequerant(String string) {
        idRequerant = string;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param string
     */
    public void setIdTiers(String string) {
        idTiers = string;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param string
     */
    public void setNom(String string) {
        nom = string;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param string
     */
    public void setNss(String string) {
        nss = string;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param string
     */
    public void setPays(String string) {
        pays = string;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param string
     */
    public void setPrenom(String string) {
        prenom = string;
    }

    public void setProvenance(String provenance) {
        this.provenance = provenance;
    }

    /**
     * @param session
     */
    public void setSession(BSession session) {
        this.session = session;
    }

    /**
     * @param boolean1
     */
    public void setWantFamille(boolean boolean1) {
        wantFamille = boolean1;
    }

}
