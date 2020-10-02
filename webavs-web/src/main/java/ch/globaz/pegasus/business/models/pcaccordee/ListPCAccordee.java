/**
 * 
 */
package ch.globaz.pegasus.business.models.pcaccordee;

import globaz.globall.db.BConstants;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.corvus.business.models.rentesaccordees.SimplePrestationsAccordees;
import ch.globaz.pegasus.business.models.demande.SimpleDemande;
import ch.globaz.pegasus.business.models.dossier.SimpleDossier;
import ch.globaz.pegasus.business.models.droit.SimpleDroit;
import ch.globaz.pegasus.business.models.droit.SimpleVersionDroit;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;

/**
 * @author SCE
 * 
 *         28 sept. 2010
 */
public class ListPCAccordee extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idCompteAnnexe = null;
    private String idPrestationAccordee = null;
    private String idPrestationAccordeeConjoint = null;
    private String idTiersBeneficiaire = null;
    private String _isPrestationBloqueeConjoint = null;
    private String _isRetenuesConjoint = null;
    private PersonneEtendueComplexModel personneEtendue = null;
    private SimpleDemande simpleDemande = null;
    private Boolean isCourante = Boolean.FALSE;

    public Boolean isCourante() {
        return isCourante;
    }

    public void defineAsCourante() {
        isCourante = Boolean.TRUE;
    }

    private SimpleDossier simpleDossier = null;

    private SimpleDroit simpleDroit = null;

    private SimplePCAccordee simplePCAccordee = null;

    private SimplePlanDeCalcul simplePlanDeCalcul = null;

    private SimplePrestationsAccordees simplePrestationsAccordees = null;
    private SimpleVersionDroit simpleVersionDroit = null;

    private String provisoire = null;

    public ListPCAccordee() {
        super();
        simplePrestationsAccordees = new SimplePrestationsAccordees();
        simpleVersionDroit = new SimpleVersionDroit();
        personneEtendue = new PersonneEtendueComplexModel();
        simpleDroit = new SimpleDroit();
        simplePCAccordee = new SimplePCAccordee();
        simplePlanDeCalcul = new SimplePlanDeCalcul();
        simpleDemande = new SimpleDemande();

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return simplePCAccordee.getIdPCAccordee();
    }

    public String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    /**
     * @return the idPrestationsAccordees
     */
    public String getIdPrestationAccordee() {
        return idPrestationAccordee;
    }

    public String getIdPrestationAccordeeConjoint() {
        return idPrestationAccordeeConjoint;
    }

    /**
     * @return the idTiersBeneficiaire
     */
    public String getIdTiersBeneficiaire() {
        return idTiersBeneficiaire;
    }

    /**
     * @return the personneEtendue
     */
    public PersonneEtendueComplexModel getPersonneEtendue() {
        return personneEtendue;
    }

    /**
     * @return the simpleDemande
     */
    public SimpleDemande getSimpleDemande() {
        return simpleDemande;
    }

    /**
     * @return the simpleDossier
     */
    public SimpleDossier getSimpleDossier() {
        return simpleDossier;
    }

    /**
     * @return the simpleDroit
     */
    public SimpleDroit getSimpleDroit() {
        return simpleDroit;
    }

    /**
     * @return the simplePCAccordee
     */
    public SimplePCAccordee getSimplePCAccordee() {
        return simplePCAccordee;
    }

    /**
     * @return the simplePlanDeCalcul
     */
    public SimplePlanDeCalcul getSimplePlanDeCalcul() {
        return simplePlanDeCalcul;
    }

    public SimplePrestationsAccordees getSimplePrestationsAccordees() {
        return simplePrestationsAccordees;
    }

    /**
     * @return the simpleVersionDroit
     */
    public SimpleVersionDroit getSimpleVersionDroit() {
        return simpleVersionDroit;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return simplePCAccordee.getSpy();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        simplePCAccordee.setId(id);

    }

    public void setIdCompteAnnexe(String idCompteAnnexe) {
        this.idCompteAnnexe = idCompteAnnexe;
    }

    /**
     * @param idPrestationsAccordees
     *            the idPrestationsAccordees to set
     */
    public void setIdPrestationAccordee(String idPrestationAccordee) {
        this.idPrestationAccordee = idPrestationAccordee;
    }

    public void setIdPrestationAccordeeConjoint(String idPrestationAccordeeConjoint) {
        this.idPrestationAccordeeConjoint = idPrestationAccordeeConjoint;
    }

    /**
     * @param idTiersBeneficiaire
     *            the idTiersBeneficiaire to set
     */
    public void setIdTiersBeneficiaire(String idTiersBeneficiaire) {
        this.idTiersBeneficiaire = idTiersBeneficiaire;
    }

    /**
     * @param personneEtendue
     *            the personneEtendue to set
     */
    public void setPersonneEtendue(PersonneEtendueComplexModel personneEtendue) {
        this.personneEtendue = personneEtendue;
    }

    /**
     * @param simpleDemande
     *            the simpleDemande to set
     */
    public void setSimpleDemande(SimpleDemande simpleDemande) {
        this.simpleDemande = simpleDemande;
    }

    /**
     * @param simpleDossier
     *            the simpleDossier to set
     */
    public void setSimpleDossier(SimpleDossier simpleDossier) {
        this.simpleDossier = simpleDossier;
    }

    /**
     * @param simpleDroit
     *            the simpleDroit to set
     */
    public void setSimpleDroit(SimpleDroit simpleDroit) {
        this.simpleDroit = simpleDroit;
    }

    /**
     * @param simplePCAccordee
     *            the simplePCAccordee to set
     */
    public void setSimplePCAccordee(SimplePCAccordee simplePCAccordee) {
        this.simplePCAccordee = simplePCAccordee;
    }

    /**
     * @param simplePlanDeCalcul
     *            the simplePlanDeCalcul to set
     */
    public void setSimplePlanDeCalcul(SimplePlanDeCalcul simplePlanDeCalcul) {
        this.simplePlanDeCalcul = simplePlanDeCalcul;
    }

    public void setSimplePrestationsAccordees(SimplePrestationsAccordees simplePrestationsAccordees) {
        this.simplePrestationsAccordees = simplePrestationsAccordees;
    }

    /**
     * @param simpleVersionDroit
     *            the simpleVersionDroit to set
     */
    public void setSimpleVersionDroit(SimpleVersionDroit simpleVersionDroit) {
        this.simpleVersionDroit = simpleVersionDroit;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        simplePCAccordee.setSpy(spy);

    }

    public boolean getIsPrestationBloqueeConjoint() {
        return new Boolean(BConstants.DB_BOOLEAN_TRUE.equals(_isPrestationBloqueeConjoint));
    }

    public boolean getIsRetenuesConjoint() {
        return new Boolean(BConstants.DB_BOOLEAN_TRUE.equals(_isRetenuesConjoint));
    }

    public String get_isPrestationBloqueeConjoint() {
        return _isPrestationBloqueeConjoint;
    }

    public void set_isPrestationBloqueeConjoint(String _isPrestationBloqueeConjoint) {
        this._isPrestationBloqueeConjoint = _isPrestationBloqueeConjoint;
    }

    public String get_isRetenuesConjoint() {
        return _isRetenuesConjoint;
    }

    public void set_isRetenuesConjoint(String _isRetenuesConjoint) {
        this._isRetenuesConjoint = _isRetenuesConjoint;
    }

    public String getProvisoire() {
        return provisoire;
    }

    public void setProvisoire(String provisoire) {
        this.provisoire = provisoire;
    }

    public boolean isProvisoire() {
        return !JadeStringUtil.isEmpty(provisoire) && BConstants.DB_BOOLEAN_TRUE.equals(provisoire);
    }

}
