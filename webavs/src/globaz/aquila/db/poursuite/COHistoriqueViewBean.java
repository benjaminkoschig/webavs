package globaz.aquila.db.poursuite;

import globaz.aquila.api.ICOEtape;
import globaz.aquila.db.access.batch.COEtapeInfo;
import globaz.aquila.db.access.batch.COEtapeInfoConfig;
import globaz.aquila.db.access.poursuite.COHistorique;
import globaz.aquila.helpers.poursuite.COHistoriqueHelper;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.iterators.FilterIterator;

/**
 * <h1>Description</h1>
 * <p>
 * Un viewBean pour l'affichages des historiques.
 * </p>
 * 
 * @author Pascal Lovy, 06-oct-2004
 */
public class COHistoriqueViewBean extends COHistorique implements FWViewBeanInterface {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final Predicate PREDICATE_ETAPES_CONFIG = new Predicate() {
        @Override
        public boolean evaluate(Object arg0) {
            return !JadeStringUtil.isIntegerEmpty(((COEtapeInfo) arg0).getIdEtapeInfoConfig());
        }
    };

    private static final Predicate PREDICATE_ETAPES_NON_CONFIG = new Predicate() {
        @Override
        public boolean evaluate(Object arg0) {
            return JadeStringUtil.isIntegerEmpty(((COEtapeInfo) arg0).getIdEtapeInfoConfig());
        }
    };

    private static final long serialVersionUID = -3400082289943431631L;

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private List toutesEtapesInfos;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Creates a new COHistoriqueViewBean object.
     */
    public COHistoriqueViewBean() {
        // charger le contentieux avec le chargement depuis la base
        loadContentieux = true;
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Retourne la liste des étapes pour cet historique qui sont liées à une configuration d'étapes (c'est-à-dire toutes
     * celles qui peuvent être configurées depuis les écrans).
     * <p>
     * Cette liste est obtenue dans le {@link COHistoriqueHelper}.
     * </p>
     * 
     * @return la liste des étapes pour cet historique qui sont liées à une configuration d'étapes
     */
    public Iterator etapesInfosIterator() {
        return new FilterIterator(toutesEtapesInfos.iterator(), COHistoriqueViewBean.PREDICATE_ETAPES_CONFIG);
    }

    /**
     * Retourne la liste des étapes qui ne sont pas liées à des configurations d'étapes, cette liste inclut les taxes.
     * <p>
     * Cette liste est obtenue dans le {@link COHistoriqueHelper}.
     * </p>
     * 
     * @return la liste des étapes qui ne sont pas liées à des configurations d'étapes
     */
    public Iterator etapesInfosNonConfigIterator() {
        return new FilterIterator(toutesEtapesInfos.iterator(), COHistoriqueViewBean.PREDICATE_ETAPES_NON_CONFIG);
    }

    /**
     * @return La valeur courante de la propriété
     */
    @Override
    public String getDateDeclenchement() {
        if (super.getEtape().getLibEtape().equals(ICOEtape.CS_ETAPE_MANUELLE)) {
            return "";
        } else {
            return super.getDateDeclenchement();
        }
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getEtapeLibelle() {
        if (super.getEtape().getLibEtape().equals(ICOEtape.CS_ETAPE_MANUELLE)) {
            return super.getMotif();
        } else {
            return super.getEtape().getLibEtapeLibelle() + getLibelleTypeSaisie();
        }
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public String getFraisVariablesFormatte() {
        try {
            return JANumberFormatter.format(loadEtapeInfo(COEtapeInfoConfig.CS_FRAIS_VARIABLES).getValeur());
        } catch (Exception e) {
            return "N/A";
        }
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public String getInteretsVariablesFormatte() {
        try {
            return JANumberFormatter.format(loadEtapeInfo(COEtapeInfoConfig.CS_INTERETS).getValeur());
        } catch (Exception e) {
            return "N/A";
        }
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public String getLibelleFraisVariables() {
        try {
            return loadEtapeInfo(COEtapeInfoConfig.CS_FRAIS_VARIABLES).getComplement1();
        } catch (Exception e) {
            return "N/A";
        }
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public String getLibelleInteretsVariables() {
        try {
            return loadEtapeInfo(COEtapeInfoConfig.CS_INTERETS).getComplement1();
        } catch (Exception e) {
            return "N/A";
        }
    }

    /**
     * Retourne le type de saisie (immobilier,mobilier ou salaire).
     */
    public String getLibelleTypeSaisie() {
        try {
            COEtapeInfo etapeInfo = loadEtapeInfo(COEtapeInfoConfig.CS_TYPE_SAISIE);
            return " : " + getSession().getCodeLibelle(etapeInfo.getValeur());
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public String getTaxesFormatte() {
        return JANumberFormatter.format(getTaxes());
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public boolean isFraisVariablesImputes() {
        try {
            return loadEtapeInfo(COEtapeInfoConfig.CS_FRAIS_VARIABLES).getComplement2().equalsIgnoreCase("true");
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public boolean isInteretsVariablesImputes() {
        try {
            return loadEtapeInfo(COEtapeInfoConfig.CS_INTERETS).getComplement2().equalsIgnoreCase("true");
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Renseigne la liste des etapes infos pour cet historique.
     * 
     * @param toutesEtapesInfos
     */
    public void setToutesEtapesInfos(List toutesEtapesInfos) {
        this.toutesEtapesInfos = toutesEtapesInfos;
    }

}
