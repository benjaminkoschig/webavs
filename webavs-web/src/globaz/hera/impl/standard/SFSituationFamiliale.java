package globaz.hera.impl.standard;

import globaz.hera.api.ISFMembreFamille;
import globaz.hera.api.ISFMembreFamilleRequerant;
import globaz.hera.api.ISFRelationFamiliale;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.hera.db.famille.SFApercuRequerant;
import globaz.hera.impl.ASFSituationFamiliale;

/**
 * Implementation de l'interface ISFSituation Familliale, Il s'agit de l'implémentation pour le domaine standard.
 * 
 * Cette classe ne peut pas être utilisée comme un BEntity, mais uniquement pour appeler les getter
 * 
 * @author mmu Date de création 12 oct. 05
 */
public class SFSituationFamiliale extends ASFSituationFamiliale implements ISFSituationFamiliale {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public String domaine = null;

    public SFSituationFamiliale() {
        super();
        domaine = ISFSituationFamiliale.CS_DOMAINE_STANDARD;
    }

    @Override
    public ISFMembreFamille addMembreCelibataire(String idTiers) throws Exception {
        return _addMembreCelibataire(idTiers, domaine);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.hera.api.ISFSituationFamiliale#getMembresFamille(java.lang.String, java.lang.String)
     */
    @Override
    public ISFMembreFamilleRequerant[] getMembresFamille(String idTiers) throws Exception {
        return _getMembresFamille(idTiers, domaine);
    }

    @Override
    public ISFMembreFamilleRequerant[] getMembresFamilleRequerant(String idTiers) throws Exception {
        return _getMembresFamilleRequerant(idTiers, domaine, null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.hera.api.ISFSituationFamiliale#getMembresFamille(java.lang.String, java.lang.String)
     */
    @Override
    public ISFMembreFamilleRequerant[] getMembresFamilleRequerant(String idTiers, String date) throws Exception {
        return _getMembresFamilleRequerant(idTiers, domaine, date);
    }

    @Override
    public ISFMembreFamilleRequerant[] getMembresFamilleRequerantParMbrFamille(String idMembreFamille) throws Exception {
        return super._getMembresFamilleRequerantParMbrFamille(idMembreFamille, domaine);
    }

    @Override
    public ISFMembreFamille[] getParents(String idTiers) throws Exception {

        return _getParents(idTiers, domaine);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.hera.api.ISFSituationFamiliale#getRelationFamiliale(java.lang. String, java.lang.String)
     */
    @Override
    public ISFRelationFamiliale[] getRelationsConjoints(String idTiersRequerant, String date) throws Exception {

        return _getRelationsConjoints(idTiersRequerant, domaine, date);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.hera.api.ISFSituationFamiliale#getRelationFamilialeEtendue(java .lang.String, java.lang.String)
     */
    @Override
    public ISFRelationFamiliale[] getRelationsConjointsEtendues(String idTiersRequerant, String date) throws Exception {
        return _getRelationsConjointsEtendues(idTiersRequerant, domaine, date);
    }

    protected SFApercuRequerant getRequerant(String idTiersRequerant) throws Exception {
        return _getRequerant(idTiersRequerant, domaine);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.hera.api.ISFSituationFamiliale#getRelationsConjointsParIdTiers (java.lang.String, java.lang.String)
     */
    @Override
    public ISFRelationFamiliale[] getToutesRelationsConjointsParIdTiers(String idTiers1, String idTiers2,
            Boolean triDateDebutDecroissant) throws Exception {
        return _getToutesRelationsConjointsParIdTiers(idTiers1, idTiers2, domaine, triDateDebutDecroissant);
    }

    @Override
    public void setDomaine(String domaine) throws Exception {
        this.domaine = domaine;
    }

}
