package globaz.al.vb.rafam;

import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import ch.globaz.al.business.constantes.enumerations.RafamEtatAnnonce;
import ch.globaz.al.business.models.rafam.AnnonceRafamErrorComplexSearchModel;
import ch.globaz.al.business.models.rafam.AnnonceRafamModel;
import ch.globaz.al.business.services.ALServiceLocator;

/**
 * ViewBean représentant une annonce RAFAM delégué
 * 
 * @author gmo
 * 
 */
public class ALAnnonceRafamEDViewBean extends BJadePersistentObjectViewBean {

    /**
     * Modèle d'une annonce
     */
    private AnnonceRafamModel annonce = null;

    /** Liste des erreurs liées à l'annonce */
    public AnnonceRafamErrorComplexSearchModel errors = null;

    private String idEmployeur = null;

    /**
     * Constructeur du viewBean
     */
    public ALAnnonceRafamEDViewBean() {
        super();
        annonce = new AnnonceRafamModel();
    }

    @Override
    public void add() throws Exception {
        throw new Exception(this.getClass() + " - Method called (add) not implemented (might be never called)");
    }

    public Boolean canValidationCAF() {
        if (RafamEtatAnnonce.ENREGISTRE.getCS().equals(annonce.getEtat())) {
            return true;
        }
        return false;

    }

    @Override
    public void delete() throws Exception {
        ALServiceLocator.getAnnonceRafamModelService().delete(annonce);
    }

    /**
     * Retourne le modèle d'annonce Rafam
     * 
     * @return the annonce
     */
    public AnnonceRafamModel getAnnonce() {
        return annonce;
    }

    /**
     * 
     * @return Erreurs liées à l'annonce
     * @throws Exception
     *             Exception levée si l'id de l'annonce n'est pas défini ou si les erreurs n'ont pas pu être chargées
     */
    public AnnonceRafamErrorComplexSearchModel getErrors() throws Exception {
        if (errors == null) {
            errors = ALServiceLocator.getAnnoncesRafamErrorBusinessService().getErrorsForAnnonce(getId());
        }

        return errors;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {
        return annonce.getId();
    }

    public String getIdEmployeur() {
        return idEmployeur;
    }

    /**
     * @return the session
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
        return (annonce != null) && !annonce.isNew() ? new BSpy(annonce.getSpy()) : new BSpy(getSession());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {

        annonce = ALServiceLocator.getAnnonceRafamModelService().read(getId());

    }

    /**
     * @param annonce
     *            le modèle représentant l'annonce
     */
    public void setAnnonce(AnnonceRafamModel annonce) {
        this.annonce = annonce;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
        annonce.setId(newId);

    }

    public void setIdEmployeur(String idEmployeur) {
        this.idEmployeur = idEmployeur;
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
