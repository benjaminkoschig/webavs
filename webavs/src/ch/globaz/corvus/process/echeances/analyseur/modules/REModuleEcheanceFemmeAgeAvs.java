package ch.globaz.corvus.process.echeances.analyseur.modules;

import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.pyxis.api.ITIPersonne;
import ch.globaz.corvus.business.models.echeances.IREEcheances;
import ch.globaz.corvus.business.models.echeances.REMotifEcheance;

/**
 * Module v�rifiant qu'une femme arrive � l'�ge de vieillesse, ou est d�j� � l'�ge AVS sans rente, dans le mois courant.
 * Si ce n'est pas le cas, v�rifie si son conjoint arrive � l'�ge vieillesse. <br/>
 * <br/>
 * Les motifs de retour positif sont :
 * <ul>
 * <li>{@link REMotifEcheance#FemmeAgeAvsDepasse}</li>
 * <li>{@link REMotifEcheance#FemmeArrivantAgeAvs}</li>
 * <li>{@link REMotifEcheance#FemmeArrivantAgeAvsAvecApiAi}</li>
 * <li>{@link REMotifEcheance#FemmeArrivantAgeAvsRenteAnticipee}</li>
 * <li>{@link REMotifEcheance#ConjointAgeAvsDepasse}</li>
 * <li>{@link REMotifEcheance#ConjointArrivantAgeAvs}</li>
 * </ul>
 * Il est n�cessaire de passer les testes unitaires (REModuleEcheanceFemmeAgeAvsTest dans le projet __TestJUnit) si une
 * modification est fait sur ce module.
 * 
 * @author PBA
 */
public class REModuleEcheanceFemmeAgeAvs extends REModuleEcheanceAgeAvs {

    /**
     * <p>
     * Construit un module ne retournant pas le motif {@link REMotifEcheance#ConjointAgeAvsDepasse}. <br/>
     * C'est le comportement par d�faut qui a �t� choisi, car trop de cas de reprise de donn�es n'avait pas de date de
     * d�c�s pour les conjoints, et ces cas venait "polluer" l'�ch�ancier.
     * </p>
     * <p>
     * Si vous voulez tout de m�me avoir ce motif, utilisez
     * {@link #REModuleEcheanceFemmeAgeAvs(BSession, String, boolean) l'autre constructeur}
     * </p>
     * 
     * @param session
     * @param moisTraitement
     * @throws Exception
     */
    public REModuleEcheanceFemmeAgeAvs(BISession session, String moisTraitement,
            String dateDebutPriseEnCompteAgeAvsDepasse) {
        this(session, moisTraitement, dateDebutPriseEnCompteAgeAvsDepasse, false);
    }

    /**
     * Construit un module retournant en plus le motif {@link REMotifEcheance#ConjointAgeAvsDepasse} si
     * <code>wantConjointAgeAvsDepasse</code> est <code>true</code>
     * 
     * @param session
     * @param moisTraitement
     * @param wantConjointAgeAvsDepasse
     * @throws Exception
     */
    public REModuleEcheanceFemmeAgeAvs(BISession session, String moisTraitement,
            String dateDebutPriseEnCompteAgeAvsDepasse, boolean wantConjointAgeAvsDepasse) {
        super(session, moisTraitement, ITIPersonne.CS_FEMME, dateDebutPriseEnCompteAgeAvsDepasse,
                REMotifEcheance.FemmeArrivantAgeAvs, REMotifEcheance.FemmeAgeAvsDepasse,
                REMotifEcheance.FemmeArrivantAgeAvsAvecApiAi, REMotifEcheance.FemmeArrivantAgeAvsRenteAnticipee,
                REMotifEcheance.FemmeAgeAvsAnticipationDepassee, wantConjointAgeAvsDepasse);
    }

    @Override
    protected REReponseModuleAnalyseEcheance analyserEcheance(IREEcheances echeancesPourUnTiers) {
        REReponseModuleAnalyseEcheance reponse = super.analyserEcheance(echeancesPourUnTiers);

        // si l'analyse standard a d�tect� qu'il n'y avait pas de rente de vieillesse pour cette femme
        // on v�rifie en plus la pr�sence d'une rente de veuve perdure
        if (reponse.isListerEcheance()) {
            switch (reponse.getMotif()) {
                case ConjointAgeAvsDepasse:
                case ConjointArrivantAgeAvs:
                    // si motif pour le conjoint, on retourne la r�ponse
                    return reponse;
                default:
                    break;
            }
            // si une rente survivant, avec un type d'information compl�mentaire "rente de veuve perdure"
            // ou si rente de veuve et l'�ge AVS est survenu avant le 01.01.2012 (BZ 6138)
            // aucune �ch�ance � retenir
            if (echeancesPourUnTiers.hasRenteVeuvePerdure()
                    || ((REModuleAnalyseEcheanceUtils.getRenteSurvivantPrincipale(echeancesPourUnTiers) != null) && REModuleAnalyseEcheanceUtils
                            .hasAgeAvsAvant2012(echeancesPourUnTiers.getCsSexeTiers(),
                                    echeancesPourUnTiers.getDateNaissanceTiers()))) {
                return REReponseModuleAnalyseEcheance.Faux;
            }
        }
        return reponse;
    }
}
