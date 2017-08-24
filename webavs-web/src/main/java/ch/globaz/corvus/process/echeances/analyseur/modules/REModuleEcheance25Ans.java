package ch.globaz.corvus.process.echeances.analyseur.modules;

import globaz.corvus.db.echeances.RERenteJoinDemandeEcheance;
import globaz.globall.api.BISession;
import globaz.jade.client.util.JadeDateUtil;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.corvus.business.models.echeances.IREEcheances;
import ch.globaz.corvus.business.models.echeances.IRERenteEcheances;
import ch.globaz.corvus.business.models.echeances.REMotifEcheance;
import ch.globaz.prestation.domaine.CodePrestation;

/**
 * Module v�rifiant les �ch�ances en rapport avec un enfant, dont l'�ge est de 25 ans dans le mois courant, ou a d�j� 25
 * ans mais poss�de toujours une rente d'enfant en cours.<br/>
 * Module final retournant, selon les cas, les motifs suivant :
 * <ul>
 * <li>{@link REMotifEcheance#Echeance25ans}</li>
 * <li>{@link REMotifEcheance#Echeance25ansDepassee}</li>
 * <li>{@link REMotifEcheance#Echeance25ansRenteBloquee} <br/>
 * -> Sp�cifique pour le processus de diminution des rentes 25 ans</li>
 * </ul>
 * Il est n�cessaire de passer les testes unitaires (REModuleEcheance25AnsTest dans le projet __TestJUnit) si une
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

        // R�cup�rer tout les rentes pour enfant
        List<IRERenteEcheances> rentesPourEnfant = new ArrayList<IRERenteEcheances>();
        for (IRERenteEcheances uneRenteDuTiers : echeancesPourUnTiers.getRentesDuTiers()) {
            CodePrestation codePrestation = CodePrestation.getCodePrestation(Integer.parseInt(uneRenteDuTiers
                    .getCodePrestation()));

            if (codePrestation.isRenteComplementairePourEnfant()) {
                rentesPourEnfant.add(uneRenteDuTiers);
            }
        }
        IRERenteEcheances rente = new RERenteJoinDemandeEcheance();

        if (!rentesPourEnfant.isEmpty()) {
            rente = rentesPourEnfant.get(0);
        }

        // si le tiers est au b�n�fice d'une rente d'enfant
        if (moduleRentePourEnfant.analyserEcheance(echeancesPourUnTiers).isListerEcheance()) {
            // selon l'�ge...
            REReponseModuleAnalyseEcheance reponseSelonAge = moduleSelonAge.analyserEcheance(echeancesPourUnTiers);
            if (reponseSelonAge.getMotif() != null) {
                switch (reponseSelonAge.getMotif()) {
                    case Interne_AgeVouluDepasseDansMoisCourant:
                        // si d�j� 25 ans
                        return REReponseModuleAnalyseEcheance.Vrai(rente, REMotifEcheance.Echeance25ansDepassee,
                                echeancesPourUnTiers.getIdTiers());
                    case Interne_AgeVouluDansMoisCourant:
                        if (utiliseMotifRenteBloquee && echeancesPourUnTiers.hasPrestationBloquee()) {
                            // si 25 ans dans le mois courant mais rente bloqu�e
                            return REReponseModuleAnalyseEcheance.Vrai(rente,
                                    REMotifEcheance.Echeance25ansRenteBloquee, echeancesPourUnTiers.getIdTiers());
                        } else {
                            return REReponseModuleAnalyseEcheance.Vrai(rente, REMotifEcheance.Echeance25ans,
                                    echeancesPourUnTiers.getIdTiers());
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
