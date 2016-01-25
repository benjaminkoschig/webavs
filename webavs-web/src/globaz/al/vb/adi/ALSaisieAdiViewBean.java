package globaz.al.vb.adi;

import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import java.util.HashMap;
import ch.globaz.al.business.models.adi.AdiSaisieComplexModel;
import ch.globaz.al.business.models.adi.AdiSaisieComplexSearchModel;
import ch.globaz.al.business.models.adi.DecompteAdiModel;
import ch.globaz.al.business.models.prestation.DetailPrestationComplexSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;

/**
 * ViewBean g�rant l'�cran de saisie des ADI. Inclus : d�compte, prestation de travail,
 * 
 * @author GMO
 * 
 */
public class ALSaisieAdiViewBean extends BJadePersistentObjectViewBean {

    /**
     * Mod�le conteneur pour les donn�es � ajouter � l'historique des saisies ADI
     */
    private AdiSaisieComplexModel adiSaisieComplexModel = null;
    /**
     * Mod�le complexe du d�compte : d�compte adi, dossier, prestation travail
     */
    private DecompteAdiModel decompteModel = null;
    /**
     * Map contenant une liste par enfant r�capitulative des p�riodes couvertes par la prestation de travail et
     * indiquant si il faut saisir ou non
     */
    private HashMap listeASaisir = null;

    /**
     * Mod�les de recherche des d�tails prestations li�s au d�compte (prestation CH de travail)
     */
    private DetailPrestationComplexSearchModel prestationComplexSearchModel = null;

    /**
     * Mod�le de recherche des saisies d�j� existantes pour le d�compte ADI
     */
    private AdiSaisieComplexSearchModel saisieComplexSearchModel = null;

    /**
     * Mod�le de recherche sur les saisies ADI du d�compte
     */
    // private AdiSaisieEnfantSearchModel AdiSaisieEnfantSearchModel = null;

    /**
     * Constructeur du viewBean
     */
    public ALSaisieAdiViewBean() {
        super();
        decompteModel = new DecompteAdiModel();
        adiSaisieComplexModel = new AdiSaisieComplexModel();
        saisieComplexSearchModel = new AdiSaisieComplexSearchModel();
        prestationComplexSearchModel = new DetailPrestationComplexSearchModel();
        // this.enfantMoisSearchModel = new AdiEnfantMoisSearchModel();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        adiSaisieComplexModel = ALServiceLocator.getAdiSaisieComplexModelService().create(adiSaisieComplexModel);

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        adiSaisieComplexModel = ALServiceLocator.getAdiSaisieComplexModelService().delete(adiSaisieComplexModel);

    }

    /**
     * 
     * @return adiSaisieComplexModel
     */
    public AdiSaisieComplexModel getAdiSaisieComplexModel() {
        return adiSaisieComplexModel;
    }

    /**
     * 
     * @return decompteComplexModel
     */
    public DecompteAdiModel getDecompteModel() {
        return decompteModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {
        return adiSaisieComplexModel.getId();
    }

    /**
     * 
     * @return listeASaisir
     */
    public HashMap getListeASaisir() {
        return listeASaisir;
    }

    /**
     * 
     * @return prestationComplexSearchModel
     */
    public DetailPrestationComplexSearchModel getPrestationComplexSearchModel() {
        return prestationComplexSearchModel;
    }

    /**
     * @return saisieSearchModel
     */
    public AdiSaisieComplexSearchModel getSaisieComplexSearchModel() {
        return saisieComplexSearchModel;
    }

    /**
     * Retourne le mod�le de saisie situ� � la position indiqu� parmi les r�sultats (saisies li�s au d�compte)
     * 
     * @param idx
     *            Position de la saisie souhait�e
     * @return le mod�le de la saisie voulue ou un mod�le vide si position introuvable
     */
    public AdiSaisieComplexModel getSaisieHistoriqueAt(int idx) {
        return idx < saisieComplexSearchModel.getSize() ? (AdiSaisieComplexModel) saisieComplexSearchModel
                .getSearchResults()[idx] : new AdiSaisieComplexModel();
    }

    /**
     * @return session actuelle
     */
    public BSession getSession() {
        return (BSession) getISession();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {

        return (adiSaisieComplexModel != null) && !adiSaisieComplexModel.isNew() ? new BSpy(
                adiSaisieComplexModel.getSpy()) : new BSpy(getSession());

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {

        // que le chargement du adisaisiemodel est indipensable car appelable
        // arrive en mode add sur la page correspondante
        // voir _init du ALSaisieAdiHelper
        // pas de initmodel car utiliser quand dans suppression saisie, pas
        // besoin de listeASaisir et tout

        adiSaisieComplexModel = ALServiceLocator.getAdiSaisieComplexModelService().read(getId());
        // this.adiSaisieComplexModel = ALServiceLocator
        // .getAdiSaisieComplexModelService().initModel(
        // this.adiSaisieComplexModel, this.listeASaisir);

    }

    /**
     * D�finit le mod�le complexe de l'Adi pour la saisie
     * 
     * @param adiSaisieComplexModel
     *            Mod�le complexe de l'ADI de saisie
     */
    public void setAdiSaisieComplexModel(AdiSaisieComplexModel adiSaisieComplexModel) {
        this.adiSaisieComplexModel = adiSaisieComplexModel;
    }

    /**
     * 
     * @param decompteModel
     *            le mod�le du d�compte
     */
    public void setDecompteModel(DecompteAdiModel decompteModel) {
        this.decompteModel = decompteModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
        adiSaisieComplexModel.setId(newId);

    }

    /**
     * 
     * @param listeASaisir
     *            les listes r�capitulatives de l'�tat des saisies
     */
    public void setListeASaisir(HashMap listeASaisir) {
        this.listeASaisir = listeASaisir;

    }

    /**
     * 
     * @param prestationComplexSearchModel
     *            le mod�le de recherche pour les d�tails des prestations li�s au d�compte
     */
    public void setPrestationComplexSearchModel(DetailPrestationComplexSearchModel prestationComplexSearchModel) {
        this.prestationComplexSearchModel = prestationComplexSearchModel;
    }

    /**
     * @param saisieComplexSearchModel
     *            le mod�le de recherche pour les saisies d�j� existantes li�es au d�compte
     */
    public void setSaisieComplexSearchModel(AdiSaisieComplexSearchModel saisieComplexSearchModel) {
        this.saisieComplexSearchModel = saisieComplexSearchModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        throw new Exception(this.getClass() + " - Method called (update) not implemented (might be never called)");

    }

}
