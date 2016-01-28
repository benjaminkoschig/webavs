package ch.globaz.corvus.process.echeances.analyseur.modules;

import globaz.globall.api.BISession;
import globaz.pyxis.api.ITIPersonne;
import ch.globaz.corvus.business.models.echeances.REMotifEcheance;

/**
 * Module vérifiant qu'un homme arrive à l'âge de vieillesse, ou est déjà à l'âge AVS sans rente, dans le mois courant.
 * Si ce n'est pas le cas, vérifie si son conjoint arrive à l'âge vieillesse. <br/>
 * <br/>
 * Les motifs de retour positif sont :
 * <ul>
 * <li>{@link REMotifEcheance#HommeAgeAvsDepasse}</li>
 * <li>{@link REMotifEcheance#HommeArrivantAgeAvs}</li>
 * <li>{@link REMotifEcheance#HommeArrivantAgeAvsAvecApiAi}</li>
 * <li>{@link REMotifEcheance#HommeArrivantAgeAvsRenteAnticipee}</li>
 * <li>{@link REMotifEcheance#ConjointAgeAvsDepasse}</li>
 * <li>{@link REMotifEcheance#ConjointArrivantAgeAvs}</li>
 * </ul>
 * Il est nécessaire de passer les testes unitaires (REModuleEcheanceHommeAgeAvsTest dans le projet __TestJUnit) si une
 * modification est fait sur ce module.
 * 
 * @author PBA
 */
public class REModuleEcheanceHommeAgeAvs extends REModuleEcheanceAgeAvs {

    /**
     * <p>
     * Construit un module ne retournant pas le motif {@link REMotifEcheance#ConjointAgeAvsDepasse}. <br/>
     * C'est le comportement par défaut qui a été choisi, car trop de cas de reprise de données n'avait pas de date de
     * décès pour les conjoints, et ces cas venait "polluer" l'échéancier.
     * </p>
     * <p>
     * Si vous voulez tout de même avoir ce motif, utilisez
     * {@link #REModuleEcheanceFemmeAgeAvs(BISession, String, boolean) l'autre constructeur}
     * </p>
     * 
     * @param session
     * @param moisTraitement
     * @throws Exception
     */
    public REModuleEcheanceHommeAgeAvs(BISession session, String moisTraitement,
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
     * @param dateDebutPriseEnCompteAgeAvsDepasse
     * @throws Exception
     */
    public REModuleEcheanceHommeAgeAvs(BISession session, String moisTraitement,
            String dateDebutPriseEnCompteAgeAvsDepasse, boolean wantConjointAgeAvsDepasse) {
        super(session, moisTraitement, ITIPersonne.CS_HOMME, dateDebutPriseEnCompteAgeAvsDepasse,
                REMotifEcheance.HommeArrivantAgeAvs, REMotifEcheance.HommeAgeAvsDepasse,
                REMotifEcheance.HommeArrivantAgeAvsAvecApiAi, REMotifEcheance.HommeArrivantAgeAvsRenteAnticipee,
                REMotifEcheance.HommeAgeAvsAnticipationDepassee, wantConjointAgeAvsDepasse);
    }
}
