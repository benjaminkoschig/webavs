/**
 * 
 */
package ch.globaz.amal.business.models.contribuable;

import java.util.ArrayList;
import ch.globaz.amal.business.models.detailfamille.SimpleDetailFamille;
import ch.globaz.amal.business.models.famille.SimpleFamille;
import ch.globaz.pyxis.business.model.AdresseComplexModel;

/**
 * @author CBU
 * 
 */
public class Contribuable extends ContribuableOnly {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private AdresseComplexModel adresseComplexModel = null;
    private SimpleDetailFamille detailFamille = null;
    private SimpleFamille famille = null;
    private ArrayList<String> histoNumeroContribuable = null;

    /**
	 * 
	 */
    public Contribuable() {
        super();
        famille = new SimpleFamille();
        detailFamille = new SimpleDetailFamille();
        adresseComplexModel = new AdresseComplexModel();
        setHistoNumeroContribuable(new ArrayList<String>());
    }

    public AdresseComplexModel getAdresseComplexModel() {
        return adresseComplexModel;
    }

    /**
     * @return the detailFamille
     */
    public SimpleDetailFamille getDetailFamille() {
        return detailFamille;
    }

    /**
     * @return the famille
     */
    public SimpleFamille getFamille() {
        return famille;
    }

    /**
     * @return the histoNumeroContribuable
     */
    public ArrayList<String> getHistoNumeroContribuable() {
        return histoNumeroContribuable;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return super.getContribuable().getId();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return super.getContribuable().getSpy();
    }

    public void setAdresseComplexModel(AdresseComplexModel adresseComplexModel) {
        this.adresseComplexModel = adresseComplexModel;
    }

    /**
     * @param detailFamille
     *            the detailFamille to set
     */
    public void setDetailFamille(SimpleDetailFamille detailFamille) {
        this.detailFamille = detailFamille;
    }

    /**
     * @param famille
     *            the famille to set
     */
    public void setFamille(SimpleFamille famille) {
        this.famille = famille;
    }

    /**
     * @param histoNumeroContribuable
     *            the histoNumeroContribuable to set
     */
    public void setHistoNumeroContribuable(ArrayList<String> histoNumeroContribuable) {
        this.histoNumeroContribuable = histoNumeroContribuable;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        super.getContribuable().setId(id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        super.getContribuable().setSpy(spy);
    }

}
