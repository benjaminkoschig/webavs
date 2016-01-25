package globaz.pegasus.vb.monnaieetrangere;

import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.monnaieetrangere.MonnaieEtrangereException;
import ch.globaz.pegasus.business.models.monnaieetrangere.MonnaieEtrangere;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

/**
 * Classe de gestion des beans MonnaieEtrangere
 * 
 * 6.2010
 * 
 * @author SCE
 * 
 */
public class PCMonnaieEtrangereViewBean extends BJadePersistentObjectViewBean {

    // instance de la classe métier
    private MonnaieEtrangere monnaieEtrangere = null;

    /**
     * Constructeur simple
     */
    public PCMonnaieEtrangereViewBean() {
        super();
        setMonnaieEtrangere(new MonnaieEtrangere());
    }

    /**
     * Constructeur avec moddèle en paramètre
     * 
     * @param monnaieEtrangere
     */
    public PCMonnaieEtrangereViewBean(MonnaieEtrangere monnaieEtrangere) {
        super();
        setMonnaieEtrangere(monnaieEtrangere);
    }

    /**
     * Ajout de l'entité
     * 
     * @throws MonnaieEtrangereException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    @Override
    public void add() throws MonnaieEtrangereException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        monnaieEtrangere = PegasusServiceLocator.getMonnaieEtrangereService().create(monnaieEtrangere);

    }

    /**
     * Supression de l'entité
     * 
     * @throws MonnaieEtrangereException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    @Override
    public void delete() throws MonnaieEtrangereException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        monnaieEtrangere = PegasusServiceLocator.getMonnaieEtrangereService().delete(monnaieEtrangere);
    }

    /**
     * Retourne l'id de l'entité
     * 
     * @return id
     */
    @Override
    public String getId() {
        return monnaieEtrangere.getId();
    }

    /**
     * @return the monnaieEtrangere
     */
    public MonnaieEtrangere getMonnaieEtrangere() {
        return monnaieEtrangere;
    }

    /**
     * Retourne l'objet session
     * 
     * @return objet BSession
     */
    public BSession getSession() {
        return (BSession) getISession();
    }

    /**
     * Retourne l'objet BSpy
     * 
     * @return objet BSpy
     */
    @Override
    public BSpy getSpy() {
        return (monnaieEtrangere != null) && !monnaieEtrangere.isNew() ? new BSpy(monnaieEtrangere.getSpy())
                : new BSpy(getSession());
    }

    /**
     * Formate la ligne vide (&nbsp) si chaine vide, pour tableau jsp
     * 
     * @param valueForTab
     * @return valueForTab si pas null, sinon &nbsp
     */
    public String getStringForTabValue(String valueForTab) {
        return JadeStringUtil.isEmpty(valueForTab) ? "&nbsp;" : valueForTab;
    }

    /**
     * Lit l'instance
     * 
     * @throws MonnaieEtrangereException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    @Override
    public void retrieve() throws MonnaieEtrangereException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        monnaieEtrangere = PegasusServiceLocator.getMonnaieEtrangereService().read(monnaieEtrangere.getId());

    }

    /**
     * Set l'id de l'entité
     * 
     */
    @Override
    public void setId(String newId) {
        monnaieEtrangere.setId(newId);

    }

    /**
     * @param monnaieEtrangere
     *            the monnaieEtrangere to set
     */
    public void setMonnaieEtrangere(MonnaieEtrangere monnaieEtrangere) {
        this.monnaieEtrangere = monnaieEtrangere;
    }

    /**
     * Mise à jour de l'entité
     * 
     * @throws MonnaieEtrangereException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    @Override
    public void update() throws MonnaieEtrangereException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        monnaieEtrangere = PegasusServiceLocator.getMonnaieEtrangereService().update(monnaieEtrangere);

    }

}
