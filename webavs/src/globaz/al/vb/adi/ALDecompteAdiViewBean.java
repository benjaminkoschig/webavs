package globaz.al.vb.adi;

import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import ch.globaz.al.business.models.adi.AdiEnfantMoisComplexModel;
import ch.globaz.al.business.models.adi.AdiEnfantMoisComplexSearchModel;
import ch.globaz.al.business.models.adi.DecompteAdiModel;
import ch.globaz.al.business.services.ALServiceLocator;

/**
 * ViewBean g�rant le mod�le d'un d�compte ADI
 * 
 * @author GMO
 * 
 */
public class ALDecompteAdiViewBean extends BJadePersistentObjectViewBean {

    /**
     * Mod�le de recherche pour les calculs adi par enfant par mois
     */
    private AdiEnfantMoisComplexSearchModel adiEnfantMoisComplexSearchModel = null;

    /**
     * Mod�le d'un d�compte g�r� par le viewBean
     */
    private DecompteAdiModel decompteAdiModel = null;

    private String montantTotal = null;

    /**
     * Constructeur du viewBean
     */
    public ALDecompteAdiViewBean() {
        super();
        decompteAdiModel = new DecompteAdiModel();
        adiEnfantMoisComplexSearchModel = new AdiEnfantMoisComplexSearchModel();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        decompteAdiModel = ALServiceLocator.getDecompteAdiModelService().create(decompteAdiModel);

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        decompteAdiModel = ALServiceLocator.getDecompteAdiModelService().delete(decompteAdiModel);

    }

    /**
     * Retourne un mod�le AdiEnfantMois li� au d�compte
     * 
     * @param idx
     *            l'indice du mod�le parmi ceux li�s au d�compte
     * @return AdiEnfantMois
     */
    public AdiEnfantMoisComplexModel getAdiEnfantMoisAt(int idx) {
        return idx < adiEnfantMoisComplexSearchModel.getSize() ? (AdiEnfantMoisComplexModel) adiEnfantMoisComplexSearchModel
                .getSearchResults()[idx] : new AdiEnfantMoisComplexModel();
    }

    /**
     * 
     * @return adiEnfantMoisSearchModel
     */
    public AdiEnfantMoisComplexSearchModel getAdiEnfantMoisComplexSearchModel() {
        return adiEnfantMoisComplexSearchModel;
    }

    /**
     * @return decompteAdiModel
     */
    public DecompteAdiModel getDecompteAdiModel() {
        return decompteAdiModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {
        return decompteAdiModel.getId();
    }

    /**
     * 
     * @return montantTotal
     */
    public String getMontantTotal() {
        return montantTotal;
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
        return (decompteAdiModel != null) && !decompteAdiModel.isNew() ? new BSpy(decompteAdiModel.getSpy())
                : new BSpy(getSession());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        decompteAdiModel = ALServiceLocator.getDecompteAdiModelService().read(getId());

        adiEnfantMoisComplexSearchModel.setForIdDecompteAdi(getId());
        adiEnfantMoisComplexSearchModel.setOrderKey("impressionAdi");
        adiEnfantMoisComplexSearchModel = ALServiceLocator.getAdiEnfantMoisComplexModelService().search(
                adiEnfantMoisComplexSearchModel);
        setMontantTotal(ALServiceLocator.getCalculAdiBusinessService()
                .getTotalDecompte(adiEnfantMoisComplexSearchModel).toString());

    }

    /**
     * @param adiEnfantMoisComplexSearchModel
     *            le mod�le de recherche des adi enfant mois
     */
    public void setAdiEnfantMoisComplexSearchModel(AdiEnfantMoisComplexSearchModel adiEnfantMoisComplexSearchModel) {
        this.adiEnfantMoisComplexSearchModel = adiEnfantMoisComplexSearchModel;
    }

    /**
     * @param decompteAdiModel
     *            d�compte adi � saisir
     */
    public void setDecompteAdiModel(DecompteAdiModel decompteAdiModel) {
        this.decompteAdiModel = decompteAdiModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
        decompteAdiModel.setId(newId);

    }

    /**
     * 
     * @param montantTotal
     *            le montant total du d�compte
     */
    public void setMontantTotal(String montantTotal) {
        this.montantTotal = montantTotal;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        decompteAdiModel = ALServiceLocator.getDecompteAdiModelService().update(decompteAdiModel);

    }
}
