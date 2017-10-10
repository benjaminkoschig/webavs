package ch.globaz.corvus.process.echeances.analyseur.modules;

import globaz.globall.api.BISession;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import java.util.SortedSet;
import ch.globaz.corvus.business.models.echeances.IREEcheances;
import ch.globaz.corvus.business.models.echeances.IREPeriodeEcheances;
import ch.globaz.corvus.business.models.echeances.REMotifEcheance;
import ch.globaz.hera.business.constantes.ISFPeriode;

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
                            return checkResponseWithEcheanceRente(reponseSelonAge, echeancesPourUnTiers,
                                    REMotifEcheance.Echeance25ansRenteBloquee);
                        } else {
                            return checkResponseWithEcheanceRente(reponseSelonAge, echeancesPourUnTiers,
                                    REMotifEcheance.Echeance25ans);
                        }
                    default:
                        break;
                }
            }
        }
        return REReponseModuleAnalyseEcheance.Faux;
    }

    /**
     * Méthode permettant de déterminer si la rente du tiers contient une date d'échéance et si cette date se situe
     * avant les 25 ans du tiers. Cette méthode ne doit être utilisée que dans le cas ou l'âge voulu (25 ans) est dans
     * le mois courant (mois de traitement). Si la date d'échéance est dans le mois courant, un deuxième test est
     * effectué sur les périodes d'études. Cela permet de vérifier s'il ne s'agit pas d'un cas d'enquête intermédiaire.
     * 
     * @param reponseSelonAge
     * @return true si l'échéance doit être placée dans la liste finale | false si on ne fait rien
     */
    private REReponseModuleAnalyseEcheance checkResponseWithEcheanceRente(
            REReponseModuleAnalyseEcheance reponseSelonAge, IREEcheances echeancesPourUnTiers,
            REMotifEcheance motifEcheance) {

        String dateEcheanceRente = reponseSelonAge.getRente().getDateEcheance();
        // Si la rente principale du cas ne contient pas de date d'échéances, on liste le cas et on envoie la
        // lettre des 25 ans
        if (JadeStringUtil.isBlankOrZero(dateEcheanceRente)) {
            return REReponseModuleAnalyseEcheance.Vrai(reponseSelonAge.getRente(), motifEcheance,
                    echeancesPourUnTiers.getIdTiers());
        }
        // Si la rente principale contient une date d'échéance et qu'elle se trouve avant le mois de traitement, on ne
        // liste rien et n'envoie rien.
        if (JadeDateUtil.isDateBefore("01." + dateEcheanceRente, "01." + getMoisTraitement())) {
            return REReponseModuleAnalyseEcheance.Faux;
        }
        // Si la rente principale contient une date d'échéance égale au mois de traitement et qu'il ne s'agit pas d'une
        // enquête intermédiaire, on liste le cas et on envoie la lettre des 25 ans
        if (JadeDateUtil.areDatesEquals("01." + dateEcheanceRente, "01." + getMoisTraitement())
                && !isEnqueteIntermediaire(echeancesPourUnTiers)) {
            return REReponseModuleAnalyseEcheance.Vrai(reponseSelonAge.getRente(), motifEcheance,
                    echeancesPourUnTiers.getIdTiers());
        }
        // Si aucune des conditions n'est remplie, on ne liste pas le cas et on n'envoie rien.
        return REReponseModuleAnalyseEcheance.Faux;

    }

    /**
     * Cette méthode permet de vérifier si la date de fin de la dernière période d'étude d'un tiers est antérieure au
     * mois de traitement.
     * Si c'est le cas, aucun document ne doit être envoyé car il s'agit d'une enquête intermédiaire. Dans les autres
     * cas, on envoie les documents (liste + lettre). Cette méthode n'est utilisée que dans le cas ou le tiers a atteint
     * ses 25 ans et que les dates d'échéances et le mois de traitement sont égaux.
     * 
     * @param echeancesPourUnTiers
     * @return true s'il s'agit d'une enquête intermédiaire / false si ce n'est pas le cas.
     */
    private boolean isEnqueteIntermediaire(IREEcheances echeancesPourUnTiers) {
        // On trie la liste de base pour ne récupérer que les cas de type étude.
        SortedSet<IREPeriodeEcheances> periodeEtudesList = sortByCSEtude(echeancesPourUnTiers.getPeriodes());
        boolean isEnqueteIntermediaire = true;
        // On récupère la dernière période d'étude et on la compare à notre date de traitement. Si la date de fin
        // est inférieure à la date de traitement, on est en mode enquête intermédiaire -> pas de documents. Sinon
        // on envoi les documents
        if (!periodeEtudesList.isEmpty()
                && JadeDateUtil.areDatesEquals(
                        "01." + JadeDateUtil.convertDateMonthYear(periodeEtudesList.last().getDateFin()), "01."
                                + getMoisTraitement())) {
            isEnqueteIntermediaire = false;

        }
        return isEnqueteIntermediaire;

    }

    /**
     * Permet de conserver une liste avec des cas qui sont uniquement de type periode étude.
     * 
     * @param periodesEtudes
     * @return SortedSet<IREPeriodeEcheances> -> les périodes d'échéances filtrées par type étude
     */
    private SortedSet<IREPeriodeEcheances> sortByCSEtude(SortedSet<IREPeriodeEcheances> periodesEtudes) {
        if (!periodesEtudes.isEmpty()) {
            for (IREPeriodeEcheances periodeEcheance : periodesEtudes) {
                if (!ISFPeriode.CS_TYPE_PERIODE_ETUDE.equals(periodeEcheance.getCsTypePeriode())) {
                    periodesEtudes.remove(periodeEcheance);
                }
            }
        }
        return periodesEtudes;
    }

    @Override
    public void setMoisTraitement(String moisTraitement) {
        super.setMoisTraitement(moisTraitement);
        moduleRentePourEnfant.setMoisTraitement(moisTraitement);
        moduleSelonAge.setMoisTraitement(moisTraitement);
    }
}
