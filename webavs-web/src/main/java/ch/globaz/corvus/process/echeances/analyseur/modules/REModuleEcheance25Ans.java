package ch.globaz.corvus.process.echeances.analyseur.modules;

import globaz.globall.api.BISession;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;

import java.util.Iterator;
import java.util.SortedSet;
import ch.globaz.corvus.business.models.echeances.IREEcheances;
import ch.globaz.corvus.business.models.echeances.IREPeriodeEcheances;
import ch.globaz.corvus.business.models.echeances.REMotifEcheance;
import ch.globaz.hera.business.constantes.ISFPeriode;

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

        // si le tiers est au b�n�fice d'une rente d'enfant
        if (moduleRentePourEnfant.analyserEcheance(echeancesPourUnTiers).isListerEcheance()) {
            // selon l'�ge...
            REReponseModuleAnalyseEcheance reponseSelonAge = moduleSelonAge.analyserEcheance(echeancesPourUnTiers);
            if (reponseSelonAge.getMotif() != null) {
                switch (reponseSelonAge.getMotif()) {
                    case Interne_AgeVouluDepasseDansMoisCourant:
                        // si d�j� 25 ans
                        return REReponseModuleAnalyseEcheance.Vrai(reponseSelonAge.getRente(),
                                REMotifEcheance.Echeance25ansDepassee, echeancesPourUnTiers.getIdTiers());
                    case Interne_AgeVouluDansMoisCourant:
                        if (utiliseMotifRenteBloquee && echeancesPourUnTiers.hasPrestationBloquee()) {
                            // si 25 ans dans le mois courant mais rente bloqu�e
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
     * M�thode permettant de d�terminer si la rente du tiers contient une date d'�ch�ance et si cette date se situe
     * avant les 25 ans du tiers. Cette m�thode ne doit �tre utilis�e que dans le cas ou l'�ge voulu (25 ans) est dans
     * le mois courant (mois de traitement). Si la date d'�ch�ance est dans le mois courant, un deuxi�me test est
     * effectu� sur les p�riodes d'�tudes. Cela permet de v�rifier s'il ne s'agit pas d'un cas d'enqu�te interm�diaire.
     * 
     * @param reponseSelonAge
     * @return true si l'�ch�ance doit �tre plac�e dans la liste finale | false si on ne fait rien
     */
    private REReponseModuleAnalyseEcheance checkResponseWithEcheanceRente(
            REReponseModuleAnalyseEcheance reponseSelonAge, IREEcheances echeancesPourUnTiers,
            REMotifEcheance motifEcheance) {

        String dateEcheanceRente = reponseSelonAge.getRente().getDateEcheance();
        // Si la rente principale du cas ne contient pas de date d'�ch�ances, on liste le cas et on envoie la
        // lettre des 25 ans
        if (JadeStringUtil.isBlankOrZero(dateEcheanceRente)) {
            return REReponseModuleAnalyseEcheance.Vrai(reponseSelonAge.getRente(), motifEcheance,
                    echeancesPourUnTiers.getIdTiers());
        }
        // Si la rente principale contient une date d'�ch�ance et qu'elle se trouve avant le mois de traitement, on ne
        // liste rien et n'envoie rien.
        if (JadeDateUtil.isDateBefore("01." + dateEcheanceRente, "01." + getMoisTraitement())) {
            return REReponseModuleAnalyseEcheance.Faux;
        }
        // Si la rente principale contient une date d'�ch�ance �gale au mois de traitement et qu'il ne s'agit pas d'une
        // enqu�te interm�diaire, on liste le cas et on envoie la lettre des 25 ans
        if (JadeDateUtil.areDatesEquals("01." + dateEcheanceRente, "01." + getMoisTraitement())
                && !isEnqueteIntermediaire(echeancesPourUnTiers)) {
            return REReponseModuleAnalyseEcheance.Vrai(reponseSelonAge.getRente(), motifEcheance,
                    echeancesPourUnTiers.getIdTiers());
        }
        // Si aucune des conditions n'est remplie, on ne liste pas le cas et on n'envoie rien.
        return REReponseModuleAnalyseEcheance.Faux;

    }

    /**
     * Cette m�thode permet de v�rifier si la date de fin de la derni�re p�riode d'�tude d'un tiers est ant�rieure au
     * mois de traitement.
     * Si c'est le cas, aucun document ne doit �tre envoy� car il s'agit d'une enqu�te interm�diaire. Dans les autres
     * cas, on envoie les documents (liste + lettre). Cette m�thode n'est utilis�e que dans le cas ou le tiers a atteint
     * ses 25 ans et que les dates d'�ch�ances et le mois de traitement sont �gaux.
     * 
     * @param echeancesPourUnTiers
     * @return true s'il s'agit d'une enqu�te interm�diaire / false si ce n'est pas le cas.
     */
    private boolean isEnqueteIntermediaire(IREEcheances echeancesPourUnTiers) {
        // On trie la liste de base pour ne r�cup�rer que les cas de type �tude.
        SortedSet<IREPeriodeEcheances> periodeEtudesList = sortByCSEtude(echeancesPourUnTiers.getPeriodes());
        boolean isEnqueteIntermediaire = true;
        // On r�cup�re la derni�re p�riode d'�tude et on la compare � notre date de traitement. Si la date de fin
        // est inf�rieure � la date de traitement, on est en mode enqu�te interm�diaire -> pas de documents. Sinon
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
     * Permet de conserver une liste avec des cas qui sont uniquement de type periode �tude.
     * 
     * @param periodesEtudes
     * @return SortedSet<IREPeriodeEcheances> -> les p�riodes d'�ch�ances filtr�es par type �tude
     */
    private SortedSet<IREPeriodeEcheances> sortByCSEtude(SortedSet<IREPeriodeEcheances> periodesEtudes) {
        if (!periodesEtudes.isEmpty()) {
            Iterator<IREPeriodeEcheances> iteratorDesPeriodes = periodesEtudes.iterator();
            while (iteratorDesPeriodes.hasNext()) { // for (IREPeriodeEcheances periodeEcheance : periodesEtudes) { cause des ConcurrentModificationException depuis ajout p�riode EnfantReceuilliGratuitement
                IREPeriodeEcheances periodeEcheance = iteratorDesPeriodes.next();
                if (!ISFPeriode.CS_TYPE_PERIODE_ETUDE.equals(periodeEcheance.getCsTypePeriode())) {
                    iteratorDesPeriodes.remove();
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
