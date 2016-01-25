package ch.globaz.al.businessimpl.services.declarationVersement;

import java.util.ArrayList;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.prestation.DeclarationVersementDetailleComplexModel;
import ch.globaz.al.business.models.prestation.DetailPrestationGenComplexModel;

/**
 * Classe contenant un dossier, le tiers bénéficiaire des prestations et de ses une liste de ses prestations
 * 
 * @author pta
 * 
 */

public class DeclarationVersementDetailleDossierTiers {

    /**
     * Dossier complexe
     */
    private DossierComplexModel dossier = null;
    /**
     * idTiersDestinataire des prestations
     * 
     */
    private String idTiersDestinataire = null;
    /**
     * liste des prestations global du dossier
     * 
     * @return
     */
    private ArrayList<DetailPrestationGenComplexModel> listPrestaGlob = null;
    /**
     * Liste des prestations détaillée du dossier
     */
    private ArrayList<DeclarationVersementDetailleComplexModel> listPrestatiion = null;

    /**
     * Dossier rataché au document et prestation
     * 
     * @return
     */
    public DossierComplexModel getDossier() {
        return dossier;
    }

    public String getIdTiersDestinataire() {
        return idTiersDestinataire;
    }

    public ArrayList<DetailPrestationGenComplexModel> getListPrestaGlob() {
        return listPrestaGlob;
    }

    public ArrayList<DeclarationVersementDetailleComplexModel> getListPrestatiion() {
        return listPrestatiion;
    }

    public void setDossier(DossierComplexModel dossier) {
        this.dossier = dossier;
    }

    public void setIdTiersDestinataire(String idTiersDestinataire) {
        this.idTiersDestinataire = idTiersDestinataire;
    }

    public void setListPrestaGlob(ArrayList<DetailPrestationGenComplexModel> listPrestaGlob) {
        this.listPrestaGlob = listPrestaGlob;
    }

    public void setListPrestatiion(ArrayList<DeclarationVersementDetailleComplexModel> listPrestatiion) {
        this.listPrestatiion = listPrestatiion;
    }

}
