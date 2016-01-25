/**
 * 
 */
package globaz.al.vb.parametres;

import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import ch.globaz.al.business.models.tauxMonnaieEtrangere.TauxMonnaieEtrangereModel;
import ch.globaz.al.business.services.ALServiceLocator;

/**
 * 
 * View Bean représentant le modèle de du taux de la monnaie étrangère (Ecran 41)
 * 
 * @author PTA
 * 
 */
public class ALTauxMonnaieEtrangereViewBean extends BJadePersistentObjectViewBean {

    /**
     * Modèle contenant les données du taux de la monnaie
     */
    private TauxMonnaieEtrangereModel tauxMonnaieEtrangere = null;

    /**
     * Constructeur
     */
    public ALTauxMonnaieEtrangereViewBean() {
        super();
        setTauxMonnaieEtrangere(new TauxMonnaieEtrangereModel());

    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        tauxMonnaieEtrangere = ALServiceLocator.getTauxMonnaieEtrangereModelService().create(tauxMonnaieEtrangere);

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        tauxMonnaieEtrangere = ALServiceLocator.getTauxMonnaieEtrangereModelService().delete(tauxMonnaieEtrangere);

    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {

        return tauxMonnaieEtrangere.getId();
    }

    /**
     * 
     * @return la session courante
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

        return (tauxMonnaieEtrangere != null) && !tauxMonnaieEtrangere.isNew() ? new BSpy(tauxMonnaieEtrangere.getSpy())
                : new BSpy(getSession());
    }

    /**
     * @return the tauxMonnaieEtrangere
     */
    public TauxMonnaieEtrangereModel getTauxMonnaieEtrangere() {
        return tauxMonnaieEtrangere;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        tauxMonnaieEtrangere = ALServiceLocator.getTauxMonnaieEtrangereModelService().read(getId());

    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
        tauxMonnaieEtrangere.setId(newId);

    }

    /**
     * @param tauxMonnaieEtrangere
     *            the tauxMonnaieEtrangere to set
     */
    public void setTauxMonnaieEtrangere(TauxMonnaieEtrangereModel tauxMonnaieEtrangere) {
        this.tauxMonnaieEtrangere = tauxMonnaieEtrangere;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        tauxMonnaieEtrangere = ALServiceLocator.getTauxMonnaieEtrangereModelService().update(tauxMonnaieEtrangere);

    }

}
