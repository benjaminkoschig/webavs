package globaz.hercule.db.controleEmployeur;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.hercule.process.CEImprimerControleProcess;
import globaz.hercule.service.CEAffiliationService;
import globaz.hercule.service.CEControleEmployeurService;
import globaz.hercule.utils.CEUtils;
import globaz.jade.client.util.JadeStringUtil;

/**
 * Ce ViewBean va lancer un process pour imprimer les factures car son implémentation est différente des Listes de iText<BR>
 * qui héritent directement de FWIAbstractDocumentList dont la classe parente n'est autre que le BProcess <br>
 * Date de création : (10.03.2003 10:37:34)
 * 
 * @author: btc
 */
public class CEImprimerControleViewBean extends CEImprimerControleProcess implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructeur de CEImprimerControleViewBean
     */
    public CEImprimerControleViewBean() throws Exception {
    }

    /**
     * Retour de la date d'impression.<BR/>
     * Si date vide, on prend la date du jour
     * 
     * @return
     */
    public String _getDateImpression() {

        String dateImpression = null;

        try {
            dateImpression = CEControleEmployeurService.findDateImpression(getSession(), getTransaction(),
                    getControleId());

            if (JadeStringUtil.isEmpty(dateImpression)) {
                dateImpression = CEUtils.giveToday();
            }

        } catch (Exception e) {
            dateImpression = CEUtils.giveToday();
        }

        return dateImpression;
    }

    /**
     * Permet la récupération de l'information d'un affilie (Nom et date d'affiliation)
     * 
     * @param idAffiliation
     * @return
     */
    public String _getInfoTiers(String idAffiliation) {
        return CEAffiliationService.getNomEtDateAffiliation(getSession(), idAffiliation);
    }
}
