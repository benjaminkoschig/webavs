package ch.globaz.corvus.process.echeances.analyseur.modules;

import globaz.globall.api.BISession;
import ch.globaz.corvus.business.models.echeances.IREEcheances;
import ch.globaz.corvus.business.models.echeances.IRERenteEcheances;
import ch.globaz.corvus.business.models.echeances.REMotifEcheance;

/**
 * Module non final permettant d'analyser l'âge du tiers d'une échéance.<br/>
 * Utilise les motifs internes suivants :
 * <ul>
 * <li>{@link REMotifEcheance#Interne_AgeVouluDansMoisCourant}</li>
 * <li>{@link REMotifEcheance#Interne_AgeVouluDepasseDansMoisCourant}</li>
 * </ul>
 * La réponse peut aussi avoir un motif <code>null</code> dans le cas ou l'âge n'as pas encore été atteint dans le mois
 * courant
 * 
 * @author PBA
 */
public class REModuleEcheanceSelonAge extends REModuleAnalyseEcheance {

    private Integer ageVoulu;

    public REModuleEcheanceSelonAge(BISession session, String moisTraitement, Integer ageVoulu) {
        super(session, moisTraitement);
        if (ageVoulu == null) {
            throw new NullPointerException("Age non défini");
        }
        this.ageVoulu = ageVoulu;
    }

    /**
     * Analyse l'âge du tiers
     * 
     * @return <ul>
     *         <li>Une réponse vraie, avec le motif {@link REMotifEcheance#Interne_AgeVouluDansMoisCourant} si l'âge
     *         voulu est atteint dans le mois courant.</li>
     *         <li>Une réponse fausse, avec le motif {@link REMotifEcheance#Interne_AgeVouluDepasseDansMoisCourant} si
     *         l'âge est déjà atteint dans le mois courant</li>
     *         <li>Une réponse fausse sans motif si l'âge n'est pas encore atteint</li>
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
