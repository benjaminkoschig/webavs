/*
 * Created on 25 nov. 05
 */
package globaz.phenix.db.communications;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.phenix.db.principale.CPDecision;
import globaz.phenix.toolbox.CPToolBox;
import globaz.phenix.util.Constante;
import globaz.pyxis.adresse.formater.TIAdresseHorizontalFormater;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TIHistoriqueContribuable;
import globaz.pyxis.db.tiers.TITiersViewBean;

/**
 * @author mar
 */
public class CPCommunicationFiscaleAffichage extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String anneeDecision = "";
    private String anneePrise = "";
    private String anneeRevenuDebut = "";
    private String anneeRevenuFin = "";
    private String canton = "";
    private String codeAnomalie = "";
    private TITiersViewBean conjoint = null;
    private String dateComptabilisation = "";
    private String dateEnvoi = "";
    private String dateEnvoiAnnulation = "";
    private Boolean dateEnvoiVide = new Boolean(false);
    private String dateNaissance = "";
    private String dateRetour = "";

    private Boolean demandeAnnulee = new Boolean(false);

    private String genreAffilie = "";
    private String idAffiliation = "";
    private String idCaisse = "";
    private String idCommunication = "";
    private String idIfd = "";
    private String idPays = "";
    private String idTiers = "";
    private String localite = "";
    private String nom = "";
    private String numAffilie = "";
    private String numAvs = "";
    private String numContri = "";
    private String numeroIfd = "";
    private String prenom = "";
    private String sexe = "";
    private TITiersViewBean tiers = null;
    private Boolean withAnneeEnCours = new Boolean(false);

    /**
	 * 
	 */
    public CPCommunicationFiscaleAffichage() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return "CPCOFIP";
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        anneePrise = statement.dbReadString("IBAPRI");
        codeAnomalie = statement.dbReadString("IBCANO");
        dateEnvoi = statement.dbReadDateAMJ("IBDENV");
        dateRetour = statement.dbReadDateAMJ("IBDRET");
        dateComptabilisation = statement.dbReadDateAMJ("IBDCPT");
        genreAffilie = statement.dbReadNumeric("IBTGAF");
        idCaisse = statement.dbReadNumeric("IBICAI");
        idCommunication = statement.dbReadNumeric("IBIDCF");
        idIfd = statement.dbReadNumeric("ICIIFD");
        idTiers = statement.dbReadNumeric("HTITIE");
        demandeAnnulee = statement.dbReadBoolean("IBCSUS");
        anneeDecision = statement.dbReadNumeric("ICANDD");
        nom = statement.dbReadString("HTLDE1");
        prenom = statement.dbReadString("HTLDE2");
        numAffilie = statement.dbReadString("MALNAF");
        numAvs = statement.dbReadString("HXNAVS");
        numContri = statement.dbReadString("HXNCON");
        canton = statement.dbReadNumeric("IBTCAN");
        anneeRevenuDebut = statement.dbReadNumeric("ICANRD");
        anneeRevenuFin = statement.dbReadNumeric("ICANRF");
        numeroIfd = statement.dbReadNumeric("ICNIFD");
        idAffiliation = statement.dbReadNumeric("MAIAFF");
        dateEnvoiAnnulation = statement.dbReadDateAMJ("IBDANN");
        dateNaissance = statement.dbReadDateAMJ("HPDNAI");
        sexe = statement.dbReadNumeric("HPTSEX");
        idPays = statement.dbReadNumeric("HNIPAY");

    }

    /**
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {

    }

    /**
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("IBIDCF", this._dbWriteNumeric(statement.getTransaction(), getIdCommunication(), ""));
    }

    /**
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // TODO Auto-generated method stub

    }

    /**
     * Retourne l'adresse d'un tiers dans un format donné en paramètre Si typeFormater="" => format adresse de type
     * lettre Date de création : (02.05.2003 11:59:50)
     * 
     * @return java.lang.String
     */
    public String getAdresse(BTransaction transaction, String typeFormater) {

        String adresse = "";
        String anneeRef = "";
        try {
            AFAffiliation aff = new AFAffiliation();
            aff.setSession(getSession());
            aff.setId(idAffiliation);
            aff.retrieve();
            if (!JadeStringUtil.isBlank(aff.getDateFin())) {
                int anneeFin = 0;
                anneeFin = globaz.globall.util.JACalendar.getYear(aff.getDateFin());
                if (String.valueOf(anneeFin).equals(getAnneeDecision())) {
                    anneeRef = aff.getDateFin();
                } else {
                    anneeRef = "31.12." + getAnneeDecision();
                }
            }
        } catch (Exception e) {
            anneeRef = "31.12." + getAnneeDecision();
        }

        try {
            if (TITiersViewBean.CS_FEMME.equals(this.getTiers().getSexe())) {
                // Recherche si il y a un conjoint
                conjoint = getConjoint();
                if ((conjoint != null) && !conjoint.isNew()) {
                    // Recherche adresse de domicile du conjoint
                    if (Constante.FORMAT_ADRESSE_LISTE.equalsIgnoreCase(typeFormater)) {
                        adresse = conjoint.getAdresseAsString(IConstantes.CS_AVOIR_ADRESSE_DOMICILE, "519005", "31.12."
                                + getAnneeDecision(), new TIAdresseHorizontalFormater());
                    } else {
                        adresse = conjoint.getAdresseAsString(IConstantes.CS_AVOIR_ADRESSE_DOMICILE, "519005", "31.12."
                                + getAnneeDecision());
                    }
                    // Si pas trouver adresse de domicile, rechercher l'adresse
                    // courrier
                    if (JadeStringUtil.isEmpty(adresse)) {
                        if (Constante.FORMAT_ADRESSE_LISTE.equalsIgnoreCase(typeFormater)) {
                            adresse = conjoint.getAdresseAsString(IConstantes.CS_AVOIR_ADRESSE_COURRIER, "519005",
                                    anneeRef, new TIAdresseHorizontalFormater());
                        } else {
                            adresse = conjoint.getAdresseAsString(IConstantes.CS_AVOIR_ADRESSE_COURRIER, "519005",
                                    anneeRef);
                        }
                    }
                }
            }
            if (JadeStringUtil.isEmpty(adresse)) {
                // Adresse domicile
                if (Constante.FORMAT_ADRESSE_LISTE.equalsIgnoreCase(typeFormater)) {
                    adresse = this.getTiers().getAdresseAsString(IConstantes.CS_AVOIR_ADRESSE_DOMICILE, "519005",
                            anneeRef, new TIAdresseHorizontalFormater());
                } else {
                    adresse = this.getTiers().getAdresseAsString(IConstantes.CS_AVOIR_ADRESSE_DOMICILE, "519005",
                            anneeRef);
                }
            }
            // Si pas trouver adresse de domicile, rechercher l'adresse courrier
            if (JadeStringUtil.isEmpty(adresse)) {
                if (Constante.FORMAT_ADRESSE_LISTE.equalsIgnoreCase(typeFormater)) {
                    adresse = this.getTiers().getAdresseAsString(IConstantes.CS_AVOIR_ADRESSE_COURRIER, "519005",
                            anneeRef, new TIAdresseHorizontalFormater());
                } else {
                    adresse = this.getTiers().getAdresseAsString(IConstantes.CS_AVOIR_ADRESSE_COURRIER, "519005",
                            anneeRef);
                }
            }
            return adresse;
        } catch (Exception e) {
            transaction.addErrors(e.getMessage());
        }
        return adresse;
    }

    /**
     * @return
     */
    public String getAnneeDecision() {
        return anneeDecision;
    }

    /**
     * @return
     */
    public String getAnneePrise() {
        return anneePrise;
    }

    /**
     * @return
     */
    public String getAnneeRevenuDebut() {
        return anneeRevenuDebut;
    }

    /**
     * @return
     */
    public String getAnneeRevenuFin() {
        return anneeRevenuFin;
    }

    /**
     * @return
     */
    public String getCanton() {
        return canton;
    }

    /**
     * @return
     */
    public String getCodeAnomalie() {
        return codeAnomalie;
    }

    /**
     * Extrait les données du conjoint
     * 
     * @return tiers
     */
    public TITiersViewBean getConjoint() {
        try {
            if (conjoint == null) {

                conjoint = CPToolBox.rechercheConjoint(getSession(), getIdTiers(),
                        "31.12." + CPToolBox.getAnneePeriodeFiscale(getSession(), getIdIfd()));
            }
            return conjoint;
        } catch (Exception e) {
            return null;
        }
    }

    public String getDateComptabilisation() {
        return dateComptabilisation;
    }

    /**
     * @return
     */
    public String getDateEnvoi() {
        return dateEnvoi;
    }

    public String getDateEnvoiAnnulation() {
        return dateEnvoiAnnulation;
    }

    /**
     * @return
     */
    public Boolean getDateEnvoiVide() {
        return dateEnvoiVide;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    /**
     * @return
     */
    public String getDateRetour() {
        return dateRetour;
    }

    /**
     * @return
     */
    public Boolean getDemandeAnnulee() {
        return demandeAnnulee;
    }

    /**
     * @return
     */
    public String getGenreAffilie() {
        return genreAffilie;
    }

    /**
     * @return
     */
    public String getIdAffiliation() {
        return idAffiliation;
    }

    /**
     * @return
     */
    public String getIdCaisse() {
        return idCaisse;
    }

    /**
     * @return
     */
    public String getIdCommunication() {
        return idCommunication;
    }

    /**
     * @return
     */
    public String getIdIfd() {
        return idIfd;
    }

    public String getIdPays() {
        return idPays;
    }

    /**
     * @return
     */
    public String getIdTiers() {
        return idTiers;
    }

    /**
     * @return
     */
    public String getLocalite() {
        return localite;
    }

    /**
     * @return
     */
    public String getNom() {
        return nom;
    }

    /**
     * @return
     */
    public String getNumAffilie() {
        if (!JadeStringUtil.isEmpty(numAffilie)) {
            return numAffilie;
        } else {
            if (!JadeStringUtil.isEmpty(idAffiliation)) {
                AFAffiliation aff = new AFAffiliation();
                aff.setSession(getSession());
                aff.setAffiliationId(idAffiliation);
                try {
                    aff.retrieve();
                    if (!aff.isNew()) {
                        return aff.getAffilieNumero();
                    } else {
                        return "";
                    }
                } catch (Exception e) {
                    return "";
                }
            } else {
                return "";
            }
        }

    }

    /**
     * @return
     */
    public String getNumAvs() {
        return numAvs;
    }

    /**
     * @return
     */
    public String getNumContri() {
        if (JadeStringUtil.isIntegerEmpty(numContri)) {
            return "";
        } else {
            return numContri;
        }
    }

    /**
     * @return
     */
    public String getNumContri(String annee) {
        try {
            String varNumContri = "";
            TIHistoriqueContribuable hist = new TIHistoriqueContribuable();
            hist.setSession(getSession());
            varNumContri = hist.findPrevKnownNumContribuable(getIdTiers(), "31.12." + annee);
            if (JadeStringUtil.isIntegerEmpty(varNumContri)) {
                varNumContri = hist.findNextKnownNumContribuable(getIdTiers(), "31.12." + annee);
                if (JadeStringUtil.isIntegerEmpty(varNumContri)) {
                    return "";
                } else {
                    return varNumContri;
                }
            } else {
                return varNumContri;
            }
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * @return
     */
    public String getNumeroIfd() {
        return numeroIfd;
    }

    /**
     * @return
     */
    public String getPrenom() {
        return prenom;
    }

    public String getSexe() {
        return sexe;
    }

    /**
     * Extrait les données du tiers
     * 
     * @return tiers
     */
    public TITiersViewBean getTiers() throws Exception {
        if (tiers == null) {
            tiers = new TITiersViewBean();
            tiers.setSession(getSession());
            tiers.setIdTiers(getIdTiers());
            tiers.retrieve();
        }
        return tiers;
    }

    /**
     * Extrait les données du tiers
     * 
     * @return tiers
     */
    public TITiersViewBean getTiers(BTransaction transaction) {
        try {
            if (tiers == null) {
                tiers = new TITiersViewBean();
                tiers.setSession(getSession());
                tiers.setIdTiers(getIdTiers());
                tiers.retrieve();
            }
        } catch (Exception e) {
            transaction.addErrors("Erreurs lors de la recherche du tiers " + e.getMessage());
        }

        return tiers;
    }

    public Boolean getWithAnneeEnCours() {
        return withAnneeEnCours;
    }

    /**
     * 07.09.2007: Cette méthode retourne si la décision est de genre non actif comme provisoire au point de vue métier
     * 
     * @param myDecision
     * @return
     */
    public boolean isNonActif() {
        if (CPDecision.CS_NON_ACTIF.equalsIgnoreCase(getGenreAffilie())
                || CPDecision.CS_ETUDIANT.equalsIgnoreCase(getGenreAffilie())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param string
     */
    public void setAnneeDecision(String string) {
        anneeDecision = string;
    }

    /**
     * @param string
     */
    public void setAnneePrise(String string) {
        anneePrise = string;
    }

    /**
     * @param string
     */
    public void setAnneeRevenuDebut(String string) {
        anneeRevenuDebut = string;
    }

    /**
     * @param string
     */
    public void setAnneeRevenuFin(String string) {
        anneeRevenuFin = string;
    }

    /**
     * @param string
     */
    public void setCanton(String string) {
        canton = string;
    }

    /**
     * @param string
     */
    public void setCodeAnomalie(String string) {
        codeAnomalie = string;
    }

    public void setConjoint(TITiersViewBean conjoint) {
        this.conjoint = conjoint;
    }

    public void setDateComptabilisation(String dateComptabilisation) {
        this.dateComptabilisation = dateComptabilisation;
    }

    /**
     * @param string
     */
    public void setDateEnvoi(String string) {
        dateEnvoi = string;
    }

    public void setDateEnvoiAnnulation(String dateEnvoiAnnulation) {
        this.dateEnvoiAnnulation = dateEnvoiAnnulation;
    }

    /**
     * @param boolean1
     */
    public void setDateEnvoiVide(Boolean boolean1) {
        dateEnvoiVide = boolean1;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    /**
     * @param string
     */
    public void setDateRetour(String string) {
        dateRetour = string;
    }

    /**
     * @param boolean1
     */
    public void setDemandeAnnulee(Boolean boolean1) {
        demandeAnnulee = boolean1;
    }

    /**
     * @param string
     */
    public void setGenreAffilie(String string) {
        genreAffilie = string;
    }

    /**
     * @param string
     */
    public void setIdAffiliation(String string) {
        idAffiliation = string;
    }

    /**
     * @param string
     */
    public void setIdCaisse(String string) {
        idCaisse = string;
    }

    /**
     * @param string
     */
    public void setIdCommunication(String string) {
        idCommunication = string;
    }

    /**
     * @param string
     */
    public void setIdIfd(String string) {
        idIfd = string;
    }

    public void setIdPays(String idPays) {
        this.idPays = idPays;
    }

    /**
     * @param string
     */
    public void setIdTiers(String string) {
        idTiers = string;
    }

    /**
     * @param string
     */
    public void setLocalite(String string) {
        localite = string;
    }

    /**
     * @param string
     */
    public void setNom(String string) {
        nom = string;
    }

    /**
     * @param string
     */
    public void setNumAffilie(String string) {
        numAffilie = string;
    }

    /**
     * @param string
     */
    public void setNumAvs(String string) {
        numAvs = string;
    }

    /**
     * @param string
     */
    public void setNumContri(String string) {
        numContri = string;
    }

    /**
     * @param string
     */
    public void setNumeroIfd(String string) {
        numeroIfd = string;
    }

    /**
     * @param string
     */
    public void setPrenom(String string) {
        prenom = string;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public void setWithAnneeEnCours(Boolean withAnneeEnCours) {
        this.withAnneeEnCours = withAnneeEnCours;
    }
}
