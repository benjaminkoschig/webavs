package globaz.helios.api.helper;

import globaz.globall.shared.GlobazHelper;
import globaz.globall.shared.GlobazValueObject;
import globaz.helios.api.ICGPlanComptable;

/**
 * Classe helper d'une interface d'API
 * 
 * @author EFLCreateAPITool
 */
public class ICGPlanComptableHelper extends GlobazHelper implements ICGPlanComptable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructeur du type ICGPlanComptableViewBeanHelper
     */
    public ICGPlanComptableHelper() {
        super("globaz.helios.db.comptes.CGPlanComptableViewBean");
    }

    /**
     * Constructeur du type ICGPlanComptableViewBeanHelper
     * 
     * @param valueObject
     *            le Value Object contenant les données
     */
    public ICGPlanComptableHelper(GlobazValueObject valueObject) {
        super(valueObject);
    }

    /**
     * Constructeur du type ICGPlanComptableViewBeanHelper
     * 
     * @param implementationClassName
     *            le nom de la classe d'implémentation
     */
    public ICGPlanComptableHelper(String implementationClassName) {
        super(implementationClassName);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.11.2002 12:54:04)
     * 
     * @return int
     */
    @Override
    public String getAvoir() {
        return (String) _getValueObject().getProperty("avoir");
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.11.2002 12:54:18)
     * 
     * @return int
     */
    @Override
    public String getAvoirProvisoire() {
        return (String) _getValueObject().getProperty("avoirProvisoire");
    }

    @Override
    public java.lang.String getCodeISOMonnaie() {
        return (java.lang.String) _getValueObject().getProperty("codeISOMonnaie");
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.11.2002 12:53:39)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getDoit() {
        return (java.lang.String) _getValueObject().getProperty("doit");
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.11.2002 12:53:56)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getDoitProvisoire() {
        return (java.lang.String) _getValueObject().getProperty("doitProvisoire");
    }

    @Override
    public java.lang.String getDomaineLibelle() {
        return (java.lang.String) _getValueObject().getProperty("domaineLibelle");
    }

    @Override
    public java.lang.String getGenreLibelle() {
        return (java.lang.String) _getValueObject().getProperty("genreLibelle");
    }

    /**
     * Getter
     */
    @Override
    public java.lang.String getIdCompte() {
        return (java.lang.String) _getValueObject().getProperty("idCompte");
    }

    @Override
    public java.lang.String getIdCompteTVA() {
        return (java.lang.String) _getValueObject().getProperty("idCompteTVA");
    }

    @Override
    public java.lang.String getIdDomaine() {
        return (java.lang.String) _getValueObject().getProperty("idDomaine");
    }

    @Override
    public java.lang.String getIdExerciceComptable() {
        return (java.lang.String) _getValueObject().getProperty("idExerciceComptable");
    }

    @Override
    public java.lang.String getIdExterne() {
        return (java.lang.String) _getValueObject().getProperty("idExterne");
    }

    @Override
    public java.lang.String getIdGenre() {
        return (java.lang.String) _getValueObject().getProperty("idGenre");
    }

    @Override
    public java.lang.String getIdMandat() {
        return (java.lang.String) _getValueObject().getProperty("idMandat");
    }

    @Override
    public java.lang.String getIdNature() {
        return (java.lang.String) _getValueObject().getProperty("idNature");
    }

    @Override
    public java.lang.String getIdParametreBouclement() {
        return (java.lang.String) _getValueObject().getProperty("idParametreBouclement");
    }

    @Override
    public java.lang.String getIdRemarque() {
        return (java.lang.String) _getValueObject().getProperty("idRemarque");
    }

    @Override
    public java.lang.String getIdSecteurAVS() {
        return (java.lang.String) _getValueObject().getProperty("idSecteurAVS");
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.05.2003 18:21:21)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getIdSolde() {
        return (java.lang.String) _getValueObject().getProperty("idSolde");
    }

    @Override
    public java.lang.String getLibelle() {
        return (java.lang.String) _getValueObject().getProperty("libelle");
    }

    @Override
    public java.lang.String getLibelleDe() {
        return (java.lang.String) _getValueObject().getProperty("libelleDe");
    }

    @Override
    public java.lang.String getLibelleFr() {
        return (java.lang.String) _getValueObject().getProperty("libelleFr");
    }

    @Override
    public java.lang.String getLibelleIt() {
        return (java.lang.String) _getValueObject().getProperty("libelleIt");
    }

    @Override
    public java.lang.String getNumeroCompteAVS() {
        return (java.lang.String) _getValueObject().getProperty("numeroCompteAVS");
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.03.2003 09:35:31)
     * 
     * @return globaz.helios.db.comptes.CGRemarque
     */
    @Override
    public java.lang.String getRemarque() {
        return (java.lang.String) _getValueObject().getProperty("remarque");
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (15.11.2002 15:51:47)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getSolde() {
        return (java.lang.String) _getValueObject().getProperty("solde");
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (15.11.2002 15:51:47)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getSoldeProvisoire() {
        return (java.lang.String) _getValueObject().getProperty("soldeProvisoire");
    }

    @Override
    public void setAlternateKey(int newAlternateKey) throws Exception {
        this._invoke("setAlternateKey", new Object[] { new Integer(newAlternateKey) });
    }

    @Override
    public void setAReouvrir(java.lang.Boolean newAReouvrir) {
        _getValueObject().setProperty("aReouvrir", newAReouvrir);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.11.2002 12:54:04)
     * 
     * @param newAvoir
     *            int
     */
    @Override
    public void setAvoir(String newAvoir) {
        _getValueObject().setProperty("avoir", newAvoir);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.11.2002 12:54:18)
     * 
     * @param newAvoirProvisoire
     *            int
     */
    @Override
    public void setAvoirProvisoire(String newAvoirProvisoire) {
        _getValueObject().setProperty("avoirProvisoire", newAvoirProvisoire);
    }

    @Override
    public void setCodeISOMonnaie(java.lang.String newCodeISOMonnaie) {
        _getValueObject().setProperty("codeISOMonnaie", newCodeISOMonnaie);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.11.2002 12:53:39)
     * 
     * @param newDoit
     *            java.lang.String
     */
    @Override
    public void setDoit(java.lang.String newDoit) {
        _getValueObject().setProperty("doit", newDoit);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.11.2002 12:53:56)
     * 
     * @param newDoitProvisoire
     *            java.lang.String
     */
    @Override
    public void setDoitProvisoire(java.lang.String newDoitProvisoire) {
        _getValueObject().setProperty("doitProvisoire", newDoitProvisoire);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.09.2002 13:25:11)
     * 
     * @param newEstCompteAvs
     *            boolean
     */
    @Override
    public void setEstCompteAvs(Boolean newEstCompteAvs) {
        _getValueObject().setProperty("estCompteAvs", newEstCompteAvs);
    }

    @Override
    public void setEstConfidentiel(java.lang.Boolean newEstConfidentiel) {
        _getValueObject().setProperty("estConfidentiel", newEstConfidentiel);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (13.12.2002 17:24:49)
     * 
     * @param newEstPeriode
     *            java.lang.Boolean
     */
    @Override
    public void setEstPeriode(java.lang.Boolean newEstPeriode) {
        _getValueObject().setProperty("estPeriode", newEstPeriode);
    }

    @Override
    public void setEstVerrouille(java.lang.Boolean newEstVerrouille) {
        _getValueObject().setProperty("estVerrouille", newEstVerrouille);
    }

    /**
     * Setter
     */
    @Override
    public void setIdCompte(java.lang.String newIdCompte) {
        _getValueObject().setProperty("idCompte", newIdCompte);
    }

    @Override
    public void setIdCompteTVA(java.lang.String newIdCompteTVA) {
        _getValueObject().setProperty("idCompteTVA", newIdCompteTVA);
    }

    @Override
    public void setIdDomaine(java.lang.String newIdDomaine) {
        _getValueObject().setProperty("idDomaine", newIdDomaine);
    }

    /**
     * Setter
     */
    @Override
    public void setIdExerciceComptable(java.lang.String newIdExerciceComptable) {
        _getValueObject().setProperty("idExerciceComptable", newIdExerciceComptable);
    }

    @Override
    public void setIdExterne(java.lang.String newIdExterne) {
        _getValueObject().setProperty("idExterne", newIdExterne);
    }

    @Override
    public void setIdGenre(java.lang.String newIdGenre) {
        _getValueObject().setProperty("idGenre", newIdGenre);
    }

    @Override
    public void setIdMandat(java.lang.String newIdMandat) {
        _getValueObject().setProperty("idMandat", newIdMandat);
    }

    @Override
    public void setIdNature(java.lang.String newIdNature) {
        _getValueObject().setProperty("idNature", newIdNature);
    }

    @Override
    public void setIdParametreBouclement(java.lang.String newIdParametreBouclement) {
        _getValueObject().setProperty("idParametreBouclement", newIdParametreBouclement);
    }

    @Override
    public void setIdRemarque(java.lang.String newIdRemarque) {
        _getValueObject().setProperty("idRemarque", newIdRemarque);
    }

    @Override
    public void setIdSecteurAVS(java.lang.String newIdSecteurAVS) {
        _getValueObject().setProperty("idSecteurAVS", newIdSecteurAVS);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.05.2003 18:21:21)
     * 
     * @param newIdSolde
     *            java.lang.String
     */
    @Override
    public void setIdSolde(java.lang.String newIdSolde) {
        _getValueObject().setProperty("idSolde", newIdSolde);
    }

    @Override
    public void setLibelleDe(java.lang.String newLibelleDe) {
        _getValueObject().setProperty("libelleDe", newLibelleDe);
    }

    @Override
    public void setLibelleFr(java.lang.String newLibelleFr) {
        _getValueObject().setProperty("libelleFr", newLibelleFr);
    }

    @Override
    public void setLibelleIt(java.lang.String newLibelleIt) {
        _getValueObject().setProperty("libelleIt", newLibelleIt);
    }

    @Override
    public void setNumeroCompteAVS(java.lang.String newNumeroCompteAVS) {
        _getValueObject().setProperty("numeroCompteAVS", newNumeroCompteAVS);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.03.2003 09:35:31)
     * 
     * @return globaz.helios.db.comptes.CGRemarque
     */
    @Override
    public void setRemarque(java.lang.String rem) {
        _getValueObject().setProperty("remarque", rem);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (15.11.2002 15:51:02)
     * 
     * @param newSolde
     *            java.lang.String
     */
    @Override
    public void setSolde(java.lang.String newSolde) {
        _getValueObject().setProperty("solde", newSolde);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (15.11.2002 15:51:47)
     * 
     * @param newSoldeProvisoir
     *            java.lang.String
     */
    @Override
    public void setSoldeProvisoire(java.lang.String newSoldeProvisoire) {
        _getValueObject().setProperty("soldeProvisoire", newSoldeProvisoire);
    }
}
