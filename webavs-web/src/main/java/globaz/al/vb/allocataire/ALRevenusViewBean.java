package globaz.al.vb.allocataire;

import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeDateUtil;
import java.util.Date;
import ch.globaz.al.business.models.allocataire.AllocataireComplexModel;
import ch.globaz.al.business.models.allocataire.RevenuModel;
import ch.globaz.al.business.models.allocataire.RevenuSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;

/**
 * ViewBean gérant le modèle représentant le revenu actuel et l'historique des revenus d'un allocataire
 * 
 * @author GMO
 * 
 */
public class ALRevenusViewBean extends BJadePersistentObjectViewBean {

    /**
     * Modèle de l'allocataire lié aux revenus, utilisé pour afficher le nom dans le titre
     */
    private AllocataireComplexModel allocataireComplexModel = null;
    /**
     * Modèle du revenu le plus récent de l'allocataire
     */
    private RevenuModel dernierRevenuAlloc = null;

    /**
     * Modèle du revenu le plus récent pour le conjoint de l'allocataire
     */
    private RevenuModel dernierRevenuConj = null;

    /**
     * Le modèle de revenus, utilisé pour ajouter un revenu à l'historique
     */
    private RevenuModel revenuModel = null;

    /**
     * Le modèle de recherche des revenus de l'allocataire (historique)
     */
    private RevenuSearchModel revenuSearchModel = null;

    /**
     * Constructeur du viewBean
     */
    public ALRevenusViewBean() {
        super();
        revenuModel = new RevenuModel();
        revenuSearchModel = new RevenuSearchModel();
        allocataireComplexModel = new AllocataireComplexModel();
    }

    /**
     * Constructeur du viewBean
     * 
     * @param revenuModel
     *            Le modèle de revenu pour celui à ajouter
     */
    public ALRevenusViewBean(RevenuModel revenuModel) {
        super();
        this.revenuModel = revenuModel;
        revenuSearchModel = new RevenuSearchModel();
        allocataireComplexModel = new AllocataireComplexModel();
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        revenuModel = ALServiceLocator.getRevenuModelService().create(revenuModel);
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        revenuModel = ALServiceLocator.getRevenuModelService().delete(revenuModel);
    }

    /**
     * @return allocataireModel Le modèle allocataire
     */
    public AllocataireComplexModel getAllocataireComplexModel() {
        return allocataireComplexModel;
    }

    /**
     * @return dernierRevenuAlloc Le revenu le plus récent de l'allocataire
     */
    public RevenuModel getDernierRevenuAlloc() {
        return dernierRevenuAlloc;
    }

    /**
     * 
     * @return dernierRevenuConj Le revenu le plus récent du conjoint de l'allocataire
     */
    public RevenuModel getDernierRevenuConj() {
        return dernierRevenuConj;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {
        return revenuModel.getId();
    }

    /**
     * Retourne le modèle de revenu situé à la position indiqué parmi les résultats (revenus liés à l'allocataire)
     * 
     * @param idx
     *            Position du revenu souhaité
     * @return le modèle du revenu voulu ou un modèle vide si position introuvable
     */
    public RevenuModel getRevenuHistoriqueAt(int idx) {
        return idx < revenuSearchModel.getSize() ? (RevenuModel) revenuSearchModel.getSearchResults()[idx]
                : new RevenuModel();
    }

    /**
     * @return revenuModel Le modèle de revenu utilisé pour l'ajout (saisie du nouveau revenu)
     */
    public RevenuModel getRevenuModel() {
        return revenuModel;
    }

    /**
     * @return revenuSearchModel Le modèle de recherche de revenus de l'allocataire
     */
    public RevenuSearchModel getRevenuSearchModel() {
        return revenuSearchModel;
    }

    /**
     * @return session courante
     */
    public BSession getSession() {
        return (BSession) getISession();
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        return (revenuModel != null) && !revenuModel.isNew() ? new BSpy(revenuModel.getSpy()) : new BSpy(getSession());

    }

    /**
     * Appelé que si l'utilisateur n'a pas les droits pour créér ou supprimer Sinon (puisqu'on entre en mode add,
     * recherche lancé depuis ALRevenuHelper, _init)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {

        revenuModel = ALServiceLocator.getRevenuModelService().initModel(revenuModel);

        allocataireComplexModel = ALServiceLocator.getAllocataireComplexModelService().read(
                revenuModel.getIdAllocataire());

        revenuSearchModel = ALServiceLocator.getRevenuModelService().search(revenuSearchModel);

        Date today = new Date();
        // Chargement du revenu le plus récent pour l'allocataire
        dernierRevenuAlloc = ALServiceLocator.getRevenuModelService().searchDernierRevenu(
                JadeDateUtil.getGlobazFormattedDate(today), revenuModel.getIdAllocataire(), false);

        if (dernierRevenuAlloc == null) {
            dernierRevenuAlloc = new RevenuModel();
            dernierRevenuAlloc.setMontant("");
        }

        // Chargement du revenu le plus récent pour le conjoint de l'allocataire
        dernierRevenuConj = ALServiceLocator.getRevenuModelService().searchDernierRevenu(
                JadeDateUtil.getGlobazFormattedDate(today), revenuModel.getIdAllocataire(), true);

        if (dernierRevenuConj == null) {
            dernierRevenuConj = new RevenuModel();
            dernierRevenuConj.setMontant("");
        }

    }

    /**
     * @param allocataireComplexModel
     *            Le modèle allocataire lié au revenus
     */
    public void setAllocataireComplexModel(AllocataireComplexModel allocataireComplexModel) {
        this.allocataireComplexModel = allocataireComplexModel;
    }

    /**
     * @param dernierRevenuAlloc
     *            Le modèle représentant le revenu le plus récent pour l'allocataire
     */
    public void setDernierRevenuAlloc(RevenuModel dernierRevenuAlloc) {
        this.dernierRevenuAlloc = dernierRevenuAlloc;
    }

    /**
     * @param dernierRevenuConj
     *            Le modèle représentant le revenu le plus récent pour le conjoint de l'allocataire
     */
    public void setDernierRevenuConj(RevenuModel dernierRevenuConj) {
        this.dernierRevenuConj = dernierRevenuConj;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
        revenuModel.setId(newId);
    }

    /**
     * @param revenuModel
     *            Le modèle représente le nouveau revenu (à ajouter à l'allocataire)
     */
    public void setRevenuModel(RevenuModel revenuModel) {
        this.revenuModel = revenuModel;
    }

    /**
     * @param revenuSearchModel
     *            Le modèle de recherche des revenus de l'allocataire
     */
    public void setRevenuSearchModel(RevenuSearchModel revenuSearchModel) {
        this.revenuSearchModel = revenuSearchModel;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        throw new Exception(this.getClass() + " - Method called (update) not implemented (might be never called)");
    }
}
