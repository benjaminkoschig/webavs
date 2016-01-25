package globaz.naos.db.annonceAffilie;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.pyxis.db.tiers.TITiers;

/**
 * Insérez la description du type ici. Date de création : (10.05.2002 09:35:05)
 * 
 * @author: Administrator
 */
public class AFAnnonceAffilieListViewBean extends AFAnnonceAffilieManager implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public AFAffiliation getAffiliation(int index) {
        return ((AFAnnonceAffilie) getEntity(index)).getAffiliation();
    }

    public String getAffiliationId(int index) {
        return ((AFAnnonceAffilie) getEntity(index)).getAffiliationId();
    }

    public String getAnnoncePreparationId(int index) {
        return ((AFAnnonceAffilie) getEntity(index)).getAnnoncePreparationId();
    }

    public String getChampAncienneDonnee(int index) {
        return ((AFAnnonceAffilie) getEntity(index)).getChampAncienneDonnee();
    }

    public String getChampModifier(int index) {
        return ((AFAnnonceAffilie) getEntity(index)).getChampModifier();
    }

    public String getDateAnnonce(int index) {
        return ((AFAnnonceAffilie) getEntity(index)).getDateAnnonce();
    }

    public String getDateEnregistrement(int index) {
        return ((AFAnnonceAffilie) getEntity(index)).getDateEnregistrement();
    }

    public String getHeureEnregistrement(int index) {
        return ((AFAnnonceAffilie) getEntity(index)).getHeureEnregistrement();
    }

    public String getObservation(int index) {
        return ((AFAnnonceAffilie) getEntity(index)).getObservation();
    }

    public TITiers getTiers(int index) {
        return ((AFAnnonceAffilie) getEntity(index)).getTiers();
    }

    public String getUtilisateur(int index) {
        return ((AFAnnonceAffilie) getEntity(index)).getUtilisateur();
    }

    public Boolean isTraitement(int index) {
        return ((AFAnnonceAffilie) getEntity(index)).isTraitement();
    }
}
