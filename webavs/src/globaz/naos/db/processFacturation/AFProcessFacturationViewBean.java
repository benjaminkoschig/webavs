package globaz.naos.db.processFacturation;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.util.JANumberFormatter;
import globaz.naos.db.assurance.AFAssurance;
import globaz.naos.db.nombreAssures.AFNombreAssures;
import globaz.naos.db.nombreAssures.AFNombreAssuresManager;
import globaz.pyxis.api.ITILocalite;
import java.io.Serializable;

/**
 * Contient les informations pour la facturation des Cotisations Paritaire ou Personnelle pour la périodicité voulue.
 * 
 * @author: sau
 */
public class AFProcessFacturationViewBean extends BEntity implements Serializable {

    private static final long serialVersionUID = 2454069175962829307L;
    private String affiliationId;
    private String affilieNumero;
    private String assuranceCanton;
    private String assuranceId;
    private String assuranceLibelleAl;

    private String assuranceLibelleFr;
    private String assuranceLibelleIt;
    private String assuranceRubriqueId;
    private Boolean blocageEnvoi = new Boolean(false);

    private String codeFacturation;
    private String cotisationId;
    private String dateDebutAffiliation;
    private String dateDebutCoti;

    private String dateDeces;

    private String dateFinAffiliation;

    private String dateFinCoti;

    private String dateNaissance;
    private String domaineCourrier;
    private String domaineRecouvrement;
    private String domaineRemboursement;
    private String genreAssurance;
    private String idCaisseAdhesion;
    private String idCaissePrincipale;
    private String idPlanAffiliation;
    private String idReferenceAssurance;

    private String idTiers;
    private Boolean isRentier = new Boolean(false);
    private String langue;
    private String libelleFacture;
    private String libellePlan;
    private String masseAnnuelleCoti;
    private String montantAnnuelCoti;
    private String montantMensuelCoti;
    private String montantTrimestrielCoti;
    private String motifFinCoti;

    private String natureRubrique;

    private String periodiciteAff;
    private String periodiciteCoti;

    private String sexe;
    // private String role;
    private String traitementMoisAnnee;
    private String typeAffiliation;
    private String typeAssurance;
    private String typeCalcul;

    // D0168
    private Boolean decompte13Releve = new Boolean(true);

    /**
     * Constructeur de AFProcessFacturationViewBean
     */
    public AFProcessFacturationViewBean() {
        super();
    }

    /**
     * N'est pas utilisée pour ce viewBean.
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return "";
    }

    /**
     * Lit les propriètés necéssaires à la facturation.
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idTiers = statement.dbReadNumeric("HTITIE");
        langue = statement.dbReadNumeric("HTTLAN");
        dateNaissance = statement.dbReadDateAMJ("HPDNAI");
        dateDeces = statement.dbReadDateAMJ("HPDDEC");
        sexe = statement.dbReadNumeric("HPTSEX");

        affilieNumero = statement.dbReadString("MALNAF");
        typeAffiliation = statement.dbReadNumeric("MATTAF");
        periodiciteAff = statement.dbReadNumeric("MATPER");
        affiliationId = statement.dbReadNumeric("MAIAFF");
        // PO 9032 : Ajout date affiliation
        dateDebutAffiliation = statement.dbReadDateAMJ("MADDEB");
        dateFinAffiliation = statement.dbReadDateAMJ("MADFIN");

        cotisationId = statement.dbReadNumeric("MEICOT");
        dateDebutCoti = statement.dbReadDateAMJ("MEDDEB");
        dateFinCoti = statement.dbReadDateAMJ("MEDFIN");

        periodiciteCoti = statement.dbReadNumeric("METPER");
        masseAnnuelleCoti = statement.dbReadNumeric("MEMMAP", 2);
        montantTrimestrielCoti = statement.dbReadNumeric("MEMTRI", 2);
        montantAnnuelCoti = statement.dbReadNumeric("MEMANN", 2);
        montantMensuelCoti = statement.dbReadNumeric("MEMMEN", 2);
        motifFinCoti = statement.dbReadNumeric("METMOT");
        traitementMoisAnnee = statement.dbReadNumeric("METMOA");

        assuranceId = statement.dbReadNumeric("MBIASS");
        idReferenceAssurance = statement.dbReadNumeric("MBIREA");
        assuranceLibelleAl = statement.dbReadString("MBLLID");
        assuranceLibelleFr = statement.dbReadString("MBLLIF");
        assuranceLibelleIt = statement.dbReadString("MBLLII");
        assuranceRubriqueId = statement.dbReadNumeric("MBIRUB");
        genreAssurance = statement.dbReadNumeric("MBTGEN");
        typeCalcul = statement.dbReadNumeric("MBTTCA");
        typeAssurance = statement.dbReadNumeric("MBTTYP");
        assuranceCanton = statement.dbReadNumeric("MBTCAN");
        decompte13Releve = statement.dbReadBoolean(AFAssurance.FIELD_DECOMPTE_13_RELEVE);

        // PlanAffiliation
        domaineCourrier = statement.dbReadNumeric("HFIAPP");
        domaineRecouvrement = statement.dbReadNumeric("HFIAPL");
        domaineRemboursement = statement.dbReadNumeric("HFIAPR");
        idPlanAffiliation = statement.dbReadNumeric("MUIPLA");
        libelleFacture = statement.dbReadString("MULFAC");
        libellePlan = statement.dbReadString("MULLIB");
        blocageEnvoi = statement.dbReadBoolean("MUBBLO");

        natureRubrique = statement.dbReadNumeric("NATURERUBRIQUE");

        idCaisseAdhesion = statement.dbReadNumeric("IDCAISSEADH");
        idCaissePrincipale = statement.dbReadNumeric("IDCAISSEPRINC");

        codeFacturation = statement.dbReadNumeric("MATCFA");
        // nbrAssures = statement.dbReadNumeric("MVNNBR");
    }

    /**
     * N'est pas utilisée pour ce viewBean.
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    /**
     * N'est pas utilisée pour ce viewBean.
     * 
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
    }

    /**
     * N'est pas utilisée pour ce viewBean.
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
    }

    public String getAffiliationId() {
        return affiliationId;
    }

    public String getAffilieNumero() {
        return affilieNumero;
    }

    /**
     * Renvoie l'assurance associée à AFProcessFacturationViewBean !!! Ne contient pas les libellés courts des
     * assurances
     */
    public AFAssurance getAssurance() {
        AFAssurance assurance = new AFAssurance();
        assurance.setSession(getSession());
        assurance.setAssuranceId(assuranceId);
        assurance.setIdAssuranceReference(idReferenceAssurance);
        assurance.setAssuranceLibelleCourtAl(assuranceLibelleAl);
        assurance.setAssuranceLibelleCourtFr(assuranceLibelleFr);
        assurance.setAssuranceLibelleCourtIt(assuranceLibelleIt);
        assurance.setRubriqueId(assuranceRubriqueId);
        assurance.setAssuranceGenre(genreAssurance);
        assurance.setTypeCalcul(typeCalcul);
        assurance.setTypeAssurance(typeAssurance);
        assurance.setAssuranceCanton(assuranceCanton);
        assurance.setDecompte13Releve(decompte13Releve);
        return assurance;
    }

    public String getAssuranceCanton() {
        return assuranceCanton;
    }

    public String getAssuranceId() {
        return assuranceId;
    }

    public String getAssuranceLibelleAl() {
        return assuranceLibelleAl;
    }

    // ***********************************************
    // Getter
    // ***********************************************

    public String getAssuranceLibelleFr() {
        return assuranceLibelleFr;
    }

    public String getAssuranceLibelleIt() {
        return assuranceLibelleIt;
    }

    /*
     * public String getRole() { return role; }
     */
    public String getAssuranceRubriqueId() {
        return assuranceRubriqueId;
    }

    /**
     * @return
     */
    public Boolean getBlocageEnvoi() {
        return blocageEnvoi;
    }

    public String getCodeFacturation() {
        return codeFacturation;
    }

    public String getCotisationId() {
        return cotisationId;
    }

    public String getDateDebutAffiliation() {
        return dateDebutAffiliation;
    }

    public String getDateDebutCoti() {
        return dateDebutCoti;
    }

    public String getDateDeces() {
        return dateDeces;
    }

    public String getDateFinAffiliation() {
        return dateFinAffiliation;
    }

    public String getDateFinCoti() {
        return dateFinCoti;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public Boolean getDecompte13Releve() {
        return decompte13Releve;
    }

    public String getDomaineCourrier() {
        return domaineCourrier;
    }

    public String getDomaineRecouvrement() {
        return domaineRecouvrement;
    }

    public String getDomaineRemboursement() {
        return domaineRemboursement;
    }

    public String getGenreAssurance() {
        return genreAssurance;
    }

    public String getIdCaisseAdhesion() {
        return idCaisseAdhesion;
    }

    /**
     * @return
     */
    public String getIdCaissePrincipale() {
        return idCaissePrincipale;
    }

    /**
     * @return
     */
    public String getIdPlanAffiliation() {
        return idPlanAffiliation;
    }

    public String getIdReferenceAssurance() {
        return idReferenceAssurance;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public Boolean getIsRentier() {
        return isRentier;
    }

    public String getLangue() {
        return langue;
    }

    /**
     * @return
     */
    public String getLibelleFacture() {
        return libelleFacture;
    }

    public String getLibellePlan() {
        return libellePlan;
    }

    public String getMasseAnnuelleCoti() {
        return JANumberFormatter.fmt(masseAnnuelleCoti.toString(), false, true, false, 2);
    }

    public String getMontantAnnuelCoti() {
        return JANumberFormatter.fmt(montantAnnuelCoti.toString(), false, true, false, 2);
    }

    public String getMontantMensuelCoti() {
        return JANumberFormatter.fmt(montantMensuelCoti.toString(), false, true, false, 2);
    }

    public String getMontantTrimestrielCoti() {
        return JANumberFormatter.fmt(montantTrimestrielCoti.toString(), false, true, false, 2);
    }

    public String getMotifFinCoti() {
        return motifFinCoti;
    }

    public String getNatureRubrique() {
        return natureRubrique;
    }

    /**
     * Renvoie le nombre d'assurés pour une année donné. <BR>
     * Cette methode effectue à chaque appelle une requête
     */
    public String getNbrAssures(String anneeFacturation) throws Exception {
        AFNombreAssuresManager nbAssuMgr = new AFNombreAssuresManager();
        nbAssuMgr.setSession(getSession());
        nbAssuMgr.setForAffiliationId(getAffiliationId());
        nbAssuMgr.setForAssuranceId(getAssuranceId());
        AFNombreAssures nbAssu = null;
        // si genève recherche sur année -2, sinon année -1
        // PO 6729
        if (!ITILocalite.CS_GENEVE.equals(getAssuranceCanton())) {
            /*
             * PO 6729 if (!ITILocalite.CS_GENEVE.equals(GlobazServer.getCurrentSystem()
             * .getApplication(AFApplication.DEFAULT_APPLICATION_NAOS).getProperty("default.canton.caisse.location"))) {
             */
            // recherche pour année -1
            nbAssuMgr.setForAnnee(String.valueOf(Integer.parseInt(anneeFacturation) - 1));
            nbAssuMgr.find();
            if (nbAssuMgr.getSize() > 0) {
                nbAssu = (AFNombreAssures) nbAssuMgr.getFirstEntity();
            }
        }
        if (nbAssu == null) {
            // recherche avec année - 2 pour Genève ou si non trouvé sur année
            // -1 pour les autres cantons
            nbAssuMgr.setForAnnee(String.valueOf(Integer.parseInt(anneeFacturation) - 2));
            nbAssuMgr.find();
            if (nbAssuMgr.getSize() > 0) {
                nbAssu = (AFNombreAssures) nbAssuMgr.getFirstEntity();
            } else {
                return null;
            }
        }
        return nbAssu.getNbrAssures();

    }

    public String getPeriodiciteAff() {
        return periodiciteAff;
    }

    public String getPeriodiciteCoti() {
        return periodiciteCoti;
    }

    public String getSexe() {
        return sexe;
    }

    public String getTraitementMoisAnnee() {
        return traitementMoisAnnee;
    }

    public String getTypeAffiliation() {
        return typeAffiliation;
    }

    public String getTypeAssurance() {
        return typeAssurance;
    }

    public String getTypeCalcul() {
        return typeCalcul;
    }

    /**
     * @param boolean1
     */
    public void setBlocageEnvoi(Boolean boolean1) {
        blocageEnvoi = boolean1;
    }

    public void setCodeFacturation(String codeFacturation) {
        this.codeFacturation = codeFacturation;
    }

    public void setDateDebutAffiliation(String dateDebutAffiliation) {
        this.dateDebutAffiliation = dateDebutAffiliation;
    }

    public void setDateFinAffiliation(String dateFinAffiliation) {
        this.dateFinAffiliation = dateFinAffiliation;
    }

    public void setDecompte13Releve(Boolean decompte13Releve) {
        this.decompte13Releve = decompte13Releve;
    }

    public void setIdCaisseAdhesion(String idCaisseAdhesion) {
        this.idCaisseAdhesion = idCaisseAdhesion;
    }

    /**
     * @param string
     */
    public void setIdCaissePrincipale(String string) {
        idCaissePrincipale = string;
    }

    /**
     * @param string
     */
    public void setIdPlanAffiliation(String string) {
        idPlanAffiliation = string;
    }

    public void setIsRentier(Boolean isRentier) {
        this.isRentier = isRentier;
    }

    /**
     * @param string
     */
    public void setLibelleFacture(String string) {
        libelleFacture = string;
    }

    public void setLibellePlan(String libellePlan) {
        this.libellePlan = libellePlan;
    }

    public void setTraitementMoisAnnee(String string) {
        traitementMoisAnnee = string;
    }

}
