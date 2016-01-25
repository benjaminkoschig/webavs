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
 * Module abstrait servant de canevas pour tous les modules d'analyse d'�ch�ance dans les rentes.<br/>
 * Permet d'�valuer si une �ch�ance doit figurer sur une liste d'�ch�ance � l'aide de la m�thode
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
            throw new IllegalArgumentException("Mois de traitement �ronn� (il doit �tre au format MM.AAAA)");
        }

        this.session = session;
        this.moisTraitement = moisTraitement;
    }

    /**
     * �value et retourne une r�ponse concernant une �ch�ance<br/>
     * 
     * 
     * @param echeancesPourUnTiers
     *            les donn�es propres � un tiers, trouv�e par le manager {@link REEcheancesManager}
     * @return une wrapper de r�ponse d�finissant si l'�ch�ance doit �tre list�e ou non (avec un motif obligatoire pour
     *         les r�ponses positives, et optionnel pour les n�gatives)
     * @see #eval(REEcheancesEntity)
     */
    protected abstract REReponseModuleAnalyseEcheance analyserEcheance(IREEcheances echeancesPourUnTiers);

    /**
     * Retourne un wrapper de r�ponse dont l'�tat sera � <code>true</code> si l'�ch�ance correspond aux conditions
     * d�finies dans la m�thode {@link #analyserEcheance(REEcheancesEntity)}
     * 
     * @param echeancesPourUnTiers
     * @return {@link REReponseModuleAnalyseEcheance} contenant un boolean d�finissant si l'�ch�ance doit �tre lister,
     *         et un {@link String} avec le motif de la liste
     * @throws Exception
     *             si le mois de traitement est ind�fini ou n'est pas au format MM.AAAA
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
