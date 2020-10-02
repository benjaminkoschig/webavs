package ch.globaz.pegasus.businessimpl.utils.calcul;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCIJAPG;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesCC;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesHome;

public class PersonnePCAccordee implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csRoleFamille = null;
    private String dateNaissance = null;
    final private transient List<CalculDonneesCC> donneesBD = new ArrayList<CalculDonneesCC>();
    private CalculDonneesHome home = null;
    private String idDroitPersonne = null;
    private String idPersonne = null;
    private Boolean isHome = null;
    private String idLocalite = null;
    final private TupleDonneeRapport rootDonneesConsolidees;
    private String typeRenteRequerant = null;

    public String getTypeRenteRequerant() {
        return typeRenteRequerant;
    }

    public void setTypeRenteRequerant(String typeRenteRequerant) {
        this.typeRenteRequerant = typeRenteRequerant;
    }

    /**
     * @param idPersonne
     */
    public PersonnePCAccordee(String idPersonne, String csRoleFamille) {
        super();
        this.idPersonne = idPersonne;
        this.csRoleFamille = csRoleFamille;
        rootDonneesConsolidees = new TupleDonneeRapport(idPersonne);
        home = new CalculDonneesHome();
    }

    public boolean isConjoint() {
        return IPCDroits.CS_ROLE_FAMILLE_CONJOINT.equals(getCsRoleFamille());
    }

    public boolean isEnfant() {
        return IPCDroits.CS_ROLE_FAMILLE_ENFANT.equals(getCsRoleFamille());
    }

    public boolean isRequerant() {
        return IPCDroits.CS_ROLE_FAMILLE_REQUERANT.equals(getCsRoleFamille());
    }

    /**
     * @return the csRoleFamille
     */
    public String getCsRoleFamille() {
        return csRoleFamille;
    }

    /**
     * @return the dateNaissance
     */
    public String getDateNaissance() {
        return dateNaissance;
    }

    /**
     * @return the donnees
     */
    public List<CalculDonneesCC> getDonneesBD() {
        return donneesBD;
    }

    public CalculDonneesHome getHome() {
        return home;
    }

    public String getIdDroitPersonne() {
        return idDroitPersonne;
    }

    /**
     * @return the idPersonne
     */
    public String getIdPersonne() {
        return idPersonne;
    }

    public Boolean getIsHome() {
        return isHome;
    }

    /**
     * @return the rootDonneesConsolidees
     */
    public TupleDonneeRapport getRootDonneesConsolidees() {
        return rootDonneesConsolidees;
    }

    public boolean isSansRente() {

        float result = tryGetValue(rootDonneesConsolidees
                .containsValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_IJAI));
        result += tryGetValue(rootDonneesConsolidees
                .containsValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_RENAVSAI_TOTAL));
        result += tryGetValue(rootDonneesConsolidees
                .containsValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_ALLOCAPI_AVS_AI));

        return result == 0;
    }

    public boolean hasIJAI() {
        for(CalculDonneesCC donnee:  donneesBD){
            if(donnee.getCsTypeDonneeFinanciere().equals(IPCDroits.CS_IJAI)){
                return true;
            }
        }
        return false;
    }

    /**
     * @param dateNaissance
     *            the dateNaissance to set
     */
    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setHome(CalculDonneesHome home) {
        this.home = home;
    }

    public void setIdDroitPersonne(String idDroitPersonne) {
        this.idDroitPersonne = idDroitPersonne;
    }

    /**
     * @param idPersonne
     *            the idPersonne to set
     */
    public void setIdPersonne(String idPersonne) {
        this.idPersonne = idPersonne;
    }

    public void setIsHome(Boolean isHome) {
        this.isHome = isHome;
    }

    public String getIdLocalite() {
        return idLocalite;
    }

    public void setIdLocalite(String idLocalite) {
        this.idLocalite = idLocalite;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return rootDonneesConsolidees.toString();
    }

    private float tryGetValue(Boolean hasValue) {
        if (hasValue) {
            // Dans le cas des rentes de type sans rente, le montant vaut zero.
            // Ici on retourne 1 dans le cas ou la cle n'est pas null (et non pas la valeur de la cle) pour que les
            // rentes de type sans rente soient prises en compte comme une rente
            // Bugzilla 7122
            return 1;
        } else {
            return 0;
        }
    }


}
