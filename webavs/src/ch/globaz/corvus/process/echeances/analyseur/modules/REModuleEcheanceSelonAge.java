package ch.globaz.corvus.process.echeances.analyseur.modules;

import globaz.globall.api.BISession;
import ch.globaz.corvus.business.models.echeances.IREEcheances;
import ch.globaz.corvus.business.models.echeances.IRERenteEcheances;
import ch.globaz.corvus.business.models.echeances.REMotifEcheance;

/**
 * Module non final permettant d'analyser l'�ge du tiers d'une �ch�ance.<br/>
 * Utilise les motifs internes suivants :
 * <ul>
 * <li>{@link REMotifEcheance#Interne_AgeVouluDansMoisCourant}</li>
 * <li>{@link REMotifEcheance#Interne_AgeVouluDepasseDansMoisCourant}</li>
 * </ul>
 * La r�ponse peut aussi avoir un motif <code>null</code> dans le cas ou l'�ge n'as pas encore �t� atteint dans le mois
 * courant
 * 
 * @author PBA
 */
public class REModuleEcheanceSelonAge extends REModuleAnalyseEcheance {

    private Integer ageVoulu;

    public REModuleEcheanceSelonAge(BISession session, String moisTraitement, Integer ageVoulu) {
        super(session, moisTraitement);
        if (ageVoulu == null) {
            throw new NullPointerException("Age non d�fini");
        }
        this.ageVoulu = ageVoulu;
    }

    /**
     * Analyse l'�ge du tiers
     * 
     * @return <ul>
     *         <li>Une r�ponse vraie, avec le motif {@link REMotifEcheance#Interne_AgeVouluDansMoisCourant} si l'�ge
     *         voulu est atteint dans le mois courant.</li>
     *         <li>Une r�ponse fausse, avec le motif {@link REMotifEcheance#Interne_AgeVouluDepasseDansMoisCourant} si
     *         l'�ge est d�j� atteint dans le mois courant</li>
     *         <li>Une r�ponse fausse sans motif si l'�ge n'est pas encore atteint</li>
     *         </ul>
     */
    @Override
    protected REReponseModuleAnalyseEcheance analyserEcheance(IREEcheances echeancesPourUnTiers) {
        Integer nbMoisVie = REModuleAnalyseEcheanceUtils.getNbMoisVie(echeancesPourUnTiers.getDateNaissanceTiers(),
                getMoisTraitement());
        switch (nbMoisVie.compareTo(ageVoulu * 12)) {
            case 0:
                IRERenteEcheances rentePrincipale = REModuleAnalyseEcheanceUtils
                        .getRentePrincipale(echeancesPourUnTiers);
                return REReponseModuleAnalyseEcheance.Vrai(rentePrincipale,
                        REMotifEcheance.Interne_AgeVouluDansMoisCourant, echeancesPourUnTiers.getIdTiers());
            case 1:
                return REReponseModuleAnalyseEcheance.Faux(REMotifEcheance.Interne_AgeVouluDepasseDansMoisCourant,
                        echeancesPourUnTiers.getIdTiers());
            default:
                return REReponseModuleAnalyseEcheance.Faux;
        }
    }

    public Integer getAgeVoulu() {
        return ageVoulu;
    }

    public void setAgeVoulu(Integer ageVoulu) {
        this.ageVoulu = ageVoulu;
    }
}
