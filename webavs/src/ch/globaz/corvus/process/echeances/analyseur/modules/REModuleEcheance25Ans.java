package ch.globaz.corvus.process.echeances.analyseur.modules;

import globaz.globall.api.BISession;
import globaz.jade.client.util.JadeDateUtil;
import ch.globaz.corvus.business.models.echeances.IREEcheances;
import ch.globaz.corvus.business.models.echeances.REMotifEcheance;

/**
 * Module vérifiant les échéances en rapport avec un enfant, dont l'âge est de 25 ans dans le mois courant, ou a déjà 25
 * ans mais possède toujours une rente d'enfant en cours.<br/>
 * Module final retournant, selon les cas, les motifs suivant :
 * <ul>
 * <li>{@link REMotifEcheance#Echeance25ans}</li>
 * <li>{@link REMotifEcheance#Echeance25ansDepassee}</li>
 * <li>{@link REMotifEcheance#Echeance25ansRenteBloquee} <br/>
 * -> Spécifique pour le processus de diminution des rentes 25 ans</li>
 * </ul>
 * Il est nécessaire de passer les testes unitaires (REModuleEcheance25AnsTest dans le projet __TestJUnit) si une
 * modification est fait sur ce module.
 * 
 * @author PBA
 */
public class REModuleEcheance25Ans extends REModuleAnalyseEcheance {

    private REModuleAnalyseEcheance moduleRentePourEnfant;
    private REModuleAnalyseEcheance moduleSelonAge;
    private boolean utiliseMotifRenteBloquee;

    public REModuleEcheance25Ans(BISession session, String moisTraitement, boolean utiliseMotifRenteBloquee) {
        super(session, moisTraitement);

        // construction des modules de testes
        moduleSelonAge = new REModuleEcheanceSelonAge(session, moisTraitement, 25);
        moduleRentePourEnfant = new REModuleEcheanceRentePourEnfant(session, moisTraitement);

        this.utiliseMotifRenteBloquee = utiliseMotifRenteBloquee;
    }

    @Override
    protected REReponseModuleAnalyseEcheance analyserEcheance(IREEcheances echeancesPourUnTiers) {
        if (!JadeDateUtil.isGlobazDate(echeancesPourUnTiers.getDateNaissanceTiers())) {
            return REReponseModuleAnalyseEcheance.Faux;
        }

        // si le tiers est au bénéfice d'une rente d'enfant
        if (moduleRentePourEnfant.analyserEcheance(echeancesPourUnTiers).isListerEcheance()) {
            // selon l'âge...
            REReponseModuleAnalyseEcheance reponseSelonAge = moduleSelonAge.analyserEcheance(echeancesPourUnTiers);
            if (reponseSelonAge.getMotif() != null) {
                switch (reponseSelonAge.getMotif()) {
                    case Interne_AgeVouluDepasseDansMoisCourant:
                        // si déjà 25 ans
                        return REReponseModuleAnalyseEcheance.Vrai(reponseSelonAge.getRente(),
                                REMotifEcheance.Echeance25ansDepassee, echeancesPourUnTiers.getIdTiers());
                    case Interne_AgeVouluDansMoisCourant:
                        if (utiliseMotifRenteBloquee && echeancesPourUnTiers.hasPrestationBloquee()) {
                            // si 25 ans dans le mois courant mais rente bloquée
                            return REReponseModuleAnalyseEcheance.Vrai(reponseSelonAge.getRente(),
                                    REMotifEcheance.Echeance25ansRenteBloquee, echeancesPourUnTiers.getIdTiers());
                        } else {
                            return REReponseModuleAnalyseEcheance.Vrai(reponseSelonAge.getRente(),
                                    REMotifEcheance.Echeance25ans, echeancesPourUnTiers.getIdTiers());
                        }
                    default:
                        break;
                }
            }
        }
        return REReponseModuleAnalyseEcheance.Faux;
    }

    @Override
    public void setMoisTraitement(String moisTraitement) {
        super.setMoisTraitement(moisTraitement);
        moduleRentePourEnfant.setMoisTraitement(moisTraitement);
        moduleSelonAge.setMoisTraitement(moisTraitement);
    }
}
