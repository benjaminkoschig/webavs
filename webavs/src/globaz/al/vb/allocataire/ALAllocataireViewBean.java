package globaz.al.vb.allocataire;

import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.models.allocataire.AllocataireAgricoleComplexModel;
import ch.globaz.al.business.models.allocataire.AllocataireComplexModel;
import ch.globaz.al.business.models.dossier.DossierModel;
import ch.globaz.al.business.services.ALServiceLocator;

/**
 * ViewBean g�rant le mod�le repr�sentant un allocataire complet
 * 
 * @author GMO
 * 
 */
public class ALAllocataireViewBean extends BJadePersistentObjectViewBean {

    /**
     * Mod�le de l'allocataire agricole complexe
     */
    private AllocataireAgricoleComplexModel allocataireAgricoleComplexModel = null;

    /**
     * Mod�le de l'allocataire complet
     */
    private AllocataireComplexModel allocataireComplexModel = null;

    /** mod�le du dossier auquel est li� l'allocataire */
    private DossierModel dossierModel = null;

    private String idDossier = null;

    public String getIdDossier() {
        return idDossier;
    }

    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    /**
     * indique si il s'agit d'un allocataire agricole
     */
    private boolean isAgricoleContext = false;

    /**
     * Nombre de dossiers dans lesquels l'allocataire est actif
     */
    private int nbDossiersActifs = 0;

    /**
     * Constructeur du viewBean
     */
    public ALAllocataireViewBean() {
        super();
        allocataireComplexModel = new AllocataireComplexModel();
        allocataireAgricoleComplexModel = new AllocataireAgricoleComplexModel();
        dossierModel = new DossierModel();

    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {

        // Si la cr�ation de l'allocataire se fait dans le cadre d'un dossier
        // agricole
        if (isAgricoleContext) {
            allocataireAgricoleComplexModel = ALServiceLocator.getAllocataireAgricoleComplexModelService().create(
                    allocataireAgricoleComplexModel);

            if (!dossierModel.isNew()) {
                dossierModel = ALServiceLocator.getDossierModelService().update(dossierModel);
            }

        } else {
            allocataireComplexModel = ALServiceLocator.getAllocataireComplexModelService().create(
                    allocataireComplexModel);
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        // Contr�le quel mod�le est rempli pour l'utiliser pour l'effacement
        if (!allocataireAgricoleComplexModel.isNew()) {
            allocataireAgricoleComplexModel = ALServiceLocator.getAllocataireAgricoleComplexModelService().delete(
                    allocataireAgricoleComplexModel);
        } else if (!allocataireAgricoleComplexModel.isNew()) {
            allocataireComplexModel = ALServiceLocator.getAllocataireComplexModelService().delete(
                    allocataireComplexModel);
        }
    }

    public String getAgricoleContext() {
        return Boolean.toString(isAgricoleContext);
    }

    /**
     * @return allocataireAgricoleComplexModel Le mod�le de l'allocataire agricole complet
     */
    public AllocataireAgricoleComplexModel getAllocataireAgricoleComplexModel() {
        return allocataireAgricoleComplexModel;
    }

    /**
     * @return allocataireComplexModel Le mod�le de l'allocataire complet
     */
    public AllocataireComplexModel getAllocataireComplexModel() {
        return allocataireComplexModel;
    }

    public DossierModel getDossierModel() {
        return dossierModel;
    }

    public DossierModel getDossierModel(String idDossier) throws Exception {

        if ((dossierModel == null) || (dossierModel.isNew()) || !dossierModel.getIdDossier().equals(idDossier)) {
            dossierModel = ALServiceLocator.getDossierModelService().read(idDossier);
        }

        return dossierModel;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {
        return allocataireComplexModel.getId();
    }

    /**
     * @return nbDossiersActifs Le nombre de dossiers pour lesquels l'allocataire est actif
     */
    public int getNbDossiersActifs() {
        return nbDossiersActifs;
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
        return (allocataireComplexModel != null) && !allocataireComplexModel.isNew() ? new BSpy(
                allocataireComplexModel.getSpy()) : new BSpy(getSession());

    }

    public boolean isAgricoleContext() {
        return isAgricoleContext;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {

        // on charge le normal, il existe de toute fa�on puisqu'il h�rite de
        // allocataireAgricole
        allocataireComplexModel = ALServiceLocator.getAllocataireComplexModelService().read(getId());

        if (isAgricoleContext) {
            allocataireAgricoleComplexModel = ALServiceLocator.getAllocataireAgricoleComplexModelService()
                    .read(getId());
        }

        // Si on est dans un contexte agricole mais que l'allocataire n'est pas
        // encore agricole
        // on doit utiliser les donn�es allocataire et les injecter dans le
        // mod�le alloc. agricole
        if (allocataireAgricoleComplexModel.isNew()) {
            allocataireAgricoleComplexModel.setAllocataireModel(allocataireComplexModel.getAllocataireModel());
            allocataireAgricoleComplexModel.setPaysModel(allocataireComplexModel.getPaysModel());
            allocataireAgricoleComplexModel.setPersonneEtendueComplexModel(allocataireComplexModel
                    .getPersonneEtendueComplexModel());
            allocataireAgricoleComplexModel.setSpy(allocataireComplexModel.getSpy());
            allocataireAgricoleComplexModel.setId(allocataireComplexModel.getId());
        }

        nbDossiersActifs = ALServiceLocator.getAllocataireBusinessService().isActif(getId());

    }

    public void setAgricoleContext(boolean isAgricoleContext) {
        this.isAgricoleContext = isAgricoleContext;
    }

    public boolean isPecheur() throws Exception {

        DossierModel dossier = getDossierModel(getIdDossier());

        if (dossier == null || dossier.isNew()) {
            return false;
        } else {
            boolean isPecheur = ALCSDossier.ACTIVITE_PECHEUR.equals(dossier.getActiviteAllocataire());
            return isPecheur;
        }
    }

    public void setAgricoleContext(String isAgricoleContext) {
        if ("true".equals(isAgricoleContext)) {
            this.isAgricoleContext = true;
        }
        if ("false".equals(isAgricoleContext)) {
            this.isAgricoleContext = false;
        }

    }

    /**
     * @param allocataireAgricoleComplexModel
     *            Le mod�le de l'allocataire agricole complet
     */
    public void setAllocataireAgricoleComplexModel(AllocataireAgricoleComplexModel allocataireAgricoleComplexModel) {
        this.allocataireAgricoleComplexModel = allocataireAgricoleComplexModel;
    }

    /**
     * @param allocataireComplexModel
     *            Le mod�le de l'allocataire complet
     */
    public void setAllocataireComplexModel(AllocataireComplexModel allocataireComplexModel) {
        this.allocataireComplexModel = allocataireComplexModel;
    }

    public void setDossierModel(DossierModel dossierModel) {
        this.dossierModel = dossierModel;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
        allocataireComplexModel.setId(newId);

    }

    /**
     * @param nbDossiersActifs
     *            Le nombre de dossiers pour lesquels l'allocataire est actif
     */
    public void setNbDossiersActifs(int nbDossiersActifs) {
        this.nbDossiersActifs = nbDossiersActifs;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {

        if (isAgricoleContext) {
            // si c'�tait un alloc normal qu'on veut en faire un agric

            if (allocataireAgricoleComplexModel.getAgricoleModel().isNew()) {
                allocataireAgricoleComplexModel = ALServiceLocator.getAllocataireAgricoleComplexModelService().create(
                        allocataireAgricoleComplexModel);
            } else {
                allocataireAgricoleComplexModel = ALServiceLocator.getAllocataireAgricoleComplexModelService().update(
                        allocataireAgricoleComplexModel);
            }

            ALServiceLocator.getDossierModelService().update(dossierModel);

        } else {
            allocataireComplexModel = ALServiceLocator.getAllocataireComplexModelService().update(
                    allocataireComplexModel);
        }

    }
}
