package ch.globaz.pegasus.business.models.renteijapi;

import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereModel;

/**
 * Modele complexe retournant le degré (léger, grave, moyen) d'allocation pour impotant. Utilisé uniquement par les RFM
 * 
 * @author JJE
 * 
 */
public class MembreFamilleAllocationImpotent extends AbstractDonneeFinanciereModel {

    private static final long serialVersionUID = 1L;
    private String csTypeRente = "";
    private String csDegre = "";
    private String idVersionDroit = "";

    public MembreFamilleAllocationImpotent() {
        super();
    }

    public String getCsTypeRente() {
        return csTypeRente;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public String getSpy() {
        return null;
    }

    public void setCsTypeRente(String csTypeRente) {
        this.csTypeRente = csTypeRente;
    }

    @Override
    public void setId(String id) {

    }

    @Override
    public void setSpy(String spy) {
    }

    public String getCsDegre() {
        return csDegre;
    }

    public void setCsDegre(String csDegre) {
        this.csDegre = csDegre;
    }

    public String getIdVersionDroit() {
        return idVersionDroit;
    }

    public void setIdVersionDroit(String idVersionDroit) {
        this.idVersionDroit = idVersionDroit;
    }

}
