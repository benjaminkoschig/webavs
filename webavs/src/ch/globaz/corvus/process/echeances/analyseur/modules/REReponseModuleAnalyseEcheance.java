package ch.globaz.corvus.process.echeances.analyseur.modules;

import ch.globaz.corvus.business.models.echeances.IRERenteEcheances;
import ch.globaz.corvus.business.models.echeances.REMotifEcheance;

/**
 * <p>
 * Wrapper de réponse pour les différents modules d'analyse d'échéance ({@link REModuleAnalyseEcheance}).<br/>
 * Peut contenir un motif défini dans l'énuméré {@link REMotifEcheance}
 * </p>
 * <p>
 * Ne peut être construit que par l'intermédiaire des méthodes statiques
 * </p>
 * <p>
 * Une réponse est invariable (ne peut pas être modifiée après son instanciation)
 * </p>
 * <p>
 * Pour des réponses simples (tel qu'une réponse négative sans motifs spécifiques), il existe deux constantes de classe
 * couvrant les réponses basiques ({@link #Vrai} et {@link #Faux}, les deux sans motifs). <br/>
 * N'utiliser {@link #Vrai} que dans des modules non finaux, c'est à dire qui n'auront pas d'interaction directe avec le
 * processus générant la liste, mais qui seront utiliser par des autres modules. Les autres modules devront
 * obligatoirement retourner un motif pour une réponse positive (en utilisant {@link #Vrai(motif, idTiers)} ou
 * {@link #Vrai(motif, idTiers, remarques)}).
 * </p>
 * 
 * @author PBA
 */
public class REReponseModuleAnalyseEcheance {

    /**
     * Une réponse fausse, dont le motif est <code>null</code>
     */
    public static final REReponseModuleAnalyseEcheance Faux = new REReponseModuleAnalyseEcheance();
    /**
     * Une réponse vraie, dont le motif est <code>null</code>
     */
    public static final REReponseModuleAnalyseEcheance Vrai = new REReponseModuleAnalyseEcheance(true);

    /**
     * Construit et retourne une réponse négative avec le motif passé en paramètre<br/>
     * A utiliser essentiellement pour des communications internes au différents modules d'analyse.
     * 
     * @param motif
     *            le motif du rejet
     * @param idTiers
     *            ID du tiers concerné par le motif
     * @return la réponse, invariable
     */
    public static REReponseModuleAnalyseEcheance Faux(REMotifEcheance motif, String idTiers) {
        return new REReponseModuleAnalyseEcheance(null, false, motif, idTiers);
    }

    /**
     * Construit et retourne une réponse positive avec le motif passé en paramètre
     * 
     * @param motif
     *            le motif de l'acceptation (ou de refus dans des cas internes)
     * @param idTiers
     *            ID du tiers concerné par le motif
     * @return la réponse, invariable
     */
    public static final REReponseModuleAnalyseEcheance Vrai(IRERenteEcheances rente, REMotifEcheance motif,
            String idTiers) {
        return new REReponseModuleAnalyseEcheance(rente, true, motif, idTiers);
    }

    /**
     * Construit et retourne une réponse positive avec le motif et les remarques passés en paramètre
     * 
     * @param motif
     *            le motif de l'acceptation (ou de refus dans des cas internes)
     * @param idTiers
     *            ID du tiers concerné par le motif
     * @param remarques
     * @return la réponse, invariable
     */
    public static final REReponseModuleAnalyseEcheance Vrai(IRERenteEcheances rente, REMotifEcheance motif,
            String idTiers, String remarques) {
        return new REReponseModuleAnalyseEcheance(rente, true, motif, idTiers, remarques);
    }

    private String idTiers;
    private boolean listerEcheance;
    private REMotifEcheance motif;
    private String remarques;
    private IRERenteEcheances rente;

    private REReponseModuleAnalyseEcheance() {
        this(false);
    }

    private REReponseModuleAnalyseEcheance(boolean listerEcheance) {
        this(null, listerEcheance, REMotifEcheance.Interne_NonDefini, "");
    }

    private REReponseModuleAnalyseEcheance(IRERenteEcheances rente, boolean listerEcheance, REMotifEcheance motif,
            String idTiers) {
        this(rente, listerEcheance, motif, idTiers, "");
    }

    private REReponseModuleAnalyseEcheance(IRERenteEcheances rente, boolean listerEcheance, REMotifEcheance motif,
            String idTiers, String remarques) {
        this.idTiers = idTiers;
        this.listerEcheance = listerEcheance;
        this.motif = motif;
        this.remarques = remarques;
        this.rente = rente;
    }

    public String getIdTiers() {
        return idTiers;
    }

    /**
     * Retourne le motif de la réponse
     * 
     * @return motif de la réponse
     * @see REMotifEcheance
     */
    public REMotifEcheance getMotif() {
        return motif;
    }

    /**
     * Retourne d'éventuelles remarques sur le motif<br/>
     * Exemple : la date de fin de la période d'étude, si elle est antérieure au mois de traitement
     * 
     * @return
     */
    public String getRemarques() {
        return remarques;
    }

    /**
     * Défini si l'échéance doit être ajoutée à celles à imprimer dans la liste
     * 
     * @return <code>true</code> ou <code>false</code>
     */
    public boolean isListerEcheance() {
        return listerEcheance;
    }

    /**
     * Renseigne l'id du tiers
     * 
     * @param idTiers
     */
    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    /**
     * @return the rente accordée qui à provoqué le motif d'échéance
     */
    public final IRERenteEcheances getRente() {
        return rente;
    }

}
