package ch.globaz.corvus.process.echeances.analyseur.modules;

import globaz.corvus.db.echeances.REEcheancesEntity;
import globaz.corvus.db.echeances.REEcheancesManager;
import globaz.corvus.exceptions.RETechnicalException;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeDateUtil;
import ch.globaz.corvus.business.models.echeances.IREEcheances;
import ch.globaz.corvus.process.echeances.analyseur.REAnalyseurEcheances;

/**
 * Module abstrait servant de canevas pour tous les modules d'analyse d'échéance dans les rentes.<br/>
 * Permet d'évaluer si une échéance doit figurer sur une liste d'échéance à l'aide de la méthode
 * {@link #eval(REEcheancesEntity)}
 * 
 * @author PBA
 * @see REAnalyseurEcheances
 */
public abstract class REModuleAnalyseEcheance {

    private String moisTraitement;
    private BISession session;

    public REModuleAnalyseEcheance(BISession session, String moisTraitement) {
        super();

        if (session == null) {
            throw new NullPointerException("Session null");
        }
        if (!JadeDateUtil.isGlobazDateMonthYear(moisTraitement)) {
            throw new IllegalArgumentException("Mois de traitement éronné (il doit être au format MM.AAAA)");
        }

        this.session = session;
        this.moisTraitement = moisTraitement;
    }

    /**
     * Évalue et retourne une réponse concernant une échéance<br/>
     * 
     * 
     * @param echeancesPourUnTiers
     *            les données propres à un tiers, trouvée par le manager {@link REEcheancesManager}
     * @return une wrapper de réponse définissant si l'échéance doit être listée ou non (avec un motif obligatoire pour
     *         les réponses positives, et optionnel pour les négatives)
     * @see #eval(REEcheancesEntity)
     */
    protected abstract REReponseModuleAnalyseEcheance analyserEcheance(IREEcheances echeancesPourUnTiers);

    /**
     * Retourne un wrapper de réponse dont l'état sera à <code>true</code> si l'échéance correspond aux conditions
     * définies dans la méthode {@link #analyserEcheance(REEcheancesEntity)}
     * 
     * @param echeancesPourUnTiers
     * @return {@link REReponseModuleAnalyseEcheance} contenant un boolean définissant si l'échéance doit être lister,
     *         et un {@link String} avec le motif de la liste
     * @throws Exception
     *             si le mois de traitement est indéfini ou n'est pas au format MM.AAAA
     * @see {@link REReponseModuleAnalyseEcheance}<br/>
     *      {@link REReponseModuleAnalyseEcheance#isListerEcheance()}
     */
    public final REReponseModuleAnalyseEcheance eval(IREEcheances echeancesPourUnTiers) {
        if (!JadeDateUtil.isGlobazDateMonthYear(moisTraitement)) {
            throw new RETechnicalException(
                    ((BSession) getSession()).getLabel("MODULE_ANALYSEUR_ECHEANCE_MOIS_TRAITEMENT_INVALIDE"));
        }
        return analyserEcheance(echeancesPourUnTiers);
    }

    public String getMoisTraitement() {
        return moisTraitement;
    }

    public BISession getSession() {
        return session;
    }

    public void setMoisTraitement(String moisTraitement) {
        this.moisTraitement = moisTraitement;
    }
}
