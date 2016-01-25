package globaz.corvus.vb.annonces;

import globaz.commons.nss.NSUtil;
import globaz.corvus.db.annonces.REAnnonceRente;
import globaz.corvus.db.annonces.REAnnonceRenteManager;
import globaz.corvus.db.annonces.REAnnoncesAbstractLevel1A;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BStatement;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.tools.nnss.PRNSSUtil;
import globaz.pyxis.db.tiers.ITIPersonneAvsDefTable;
import globaz.pyxis.db.tiers.ITIPersonneDefTable;
import globaz.pyxis.db.tiers.ITITiersDefTable;

public class REAnnoncesAbstractLevel1AViewBean extends REAnnoncesAbstractLevel1A implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csNationalite = "";
    private String csSexe = "";
    private String dateNaissance = "";
    private String idRenteAccordee = "";
    // Pour recherche dans la manager !
    private String isPRE0070 = "";
    // Autres champs nécessaires
    private String noAVS = "";
    private String nom = "";
    private String prenom = "";

    @Override
    protected void _readProperties(BStatement statement) throws Exception {

        super._readProperties(statement);

        noAVS = NSUtil.formatAVSUnknown(statement.dbReadString(ITIPersonneAvsDefTable.NUMERO_AVS_ACTUEL));
        dateNaissance = statement.dbReadDateAMJ(ITIPersonneDefTable.DATE_NAISSANCE);
        csSexe = statement.dbReadNumeric(ITIPersonneDefTable.CS_SEXE);
        nom = statement.dbReadString(ITITiersDefTable.DESIGNATION_1);
        prenom = statement.dbReadString(ITITiersDefTable.DESIGNATION_2);
        csNationalite = statement.dbReadNumeric(ITITiersDefTable.ID_PAYS);
    }

    public String getCsNationalite() {
        return csNationalite;
    }

    public String getCsSexe() {
        return csSexe;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    /**
     * Méthode qui retourne le détail du requérant formaté pour les listes
     * 
     * @return le détail du requérant formaté
     */
    public String getDetailRequerant() {
        return PRNSSUtil.formatDetailRequerantListe(getNoAVS(), getNom() + " " + getPrenom(), getDateNaissance(),
                getLibelleCourtSexe(), getLibellePays());
    }

    @Override
    public String getIdRenteAccordee() {
        return idRenteAccordee;
    }

    public String getIsPRE0070() {
        return isPRE0070;
    }

    public String getLibelleCodeTraitement() throws Exception {

        REAnnonceRenteManager annonceRente = new REAnnonceRenteManager();
        annonceRente.setSession(getSession());
        annonceRente.setForIdAnnonceHeader(getIdAnnonce());
        annonceRente.find(1);

        REAnnonceRente annRente = (REAnnonceRente) annonceRente.get(0);

        return getSession().getCodeLibelle(annRente.getCsTraitement());

    }

    /**
     * Méthode qui retourne le libellé court du sexe par rapport au csSexe qui est dans le vb
     * 
     * @return le libellé court du sexe (H ou F)
     */
    public String getLibelleCourtSexe() {

        if (PRACORConst.CS_HOMME.equals(getCsSexe())) {
            return getSession().getLabel("JSP_LETTRE_SEXE_HOMME");
        } else if (PRACORConst.CS_FEMME.equals(getCsSexe())) {
            return getSession().getLabel("JSP_LETTRE_SEXE_FEMME");
        } else {
            return "";
        }

    }

    public String getLibelleEtat() {
        return getSession().getCodeLibelle(getEtat());
    }

    /**
     * Méthode qui retourne le libellé de la nationalité par rapport au csNationalité qui est dans le vb
     * 
     * @return le libellé du pays (retourne une chaîne vide si pays inconnu)
     */
    public String getLibellePays() {

        if ("999".equals(getSession().getCode(getSession().getSystemCode("CIPAYORI", getCsNationalite())))) {
            return "";
        } else {
            return getSession().getCodeLibelle(getSession().getSystemCode("CIPAYORI", getCsNationalite()));
        }

    }

    public String getNoAVS() {
        return noAVS;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setCsNationalite(String csNationalite) {
        this.csNationalite = csNationalite;
    }

    public void setCsSexe(String csSexe) {
        this.csSexe = csSexe;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    @Override
    public void setIdRenteAccordee(String idRenteAccordee) {
        this.idRenteAccordee = idRenteAccordee;
    }

    public void setIsPRE0070(String isPRE0070) {
        this.isPRE0070 = isPRE0070;
    }

    public void setNoAVS(String noAVS) {
        this.noAVS = noAVS;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
}
