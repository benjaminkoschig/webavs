package ch.globaz.corvus.process.echeances.analyseur.modules;

import ch.globaz.corvus.business.models.echeances.IRERenteEcheances;
import ch.globaz.corvus.business.models.echeances.REMotifEcheance;

/**
 * <p>
 * Wrapper de r�ponse pour les diff�rents modules d'analyse d'�ch�ance ({@link REModuleAnalyseEcheance}).<br/>
 * Peut contenir un motif d�fini dans l'�num�r� {@link REMotifEcheance}
 * </p>
 * <p>
 * Ne peut �tre construit que par l'interm�diaire des m�thodes statiques
 * </p>
 * <p>
 * Une r�ponse est invariable (ne peut pas �tre modifi�e apr�s son instanciation)
 * </p>
 * <p>
 * Pour des r�ponses simples (tel qu'une r�ponse n�gative sans motifs sp�cifiques), il existe deux constantes de classe
 * couvrant les r�ponses basiques ({@link #Vrai} et {@link #Faux}, les deux sans motifs). <br/>
 * N'utiliser {@link #Vrai} que dans des modules non finaux, c'est � dire qui n'auront pas d'interaction directe avec le
 * processus g�n�rant la liste, mais qui seront utiliser par des autres modules. Les autres modules devront
 * obligatoirement retourner un motif pour une r�ponse positive (en utilisant {@link #Vrai(motif, idTiers)} ou
 * {@link #Vrai(motif, idTiers, remarques)}).
 * </p>
 * 
 * @author PBA
 */
public class REReponseModuleAnalyseEcheance {

    /**
     * Une r�ponse fausse, dont le motif est <code>null</code>
     */
    public static final REReponseModuleAnalyseEcheance Faux = new REReponseModuleAnalyseEcheance();
    /**
     * Une r�ponse vraie, dont le motif est <code>null</code>
     */
    public static final REReponseModuleAnalyseEcheance Vrai = new REReponseModuleAnalyseEcheance(true);

    /**
     * Construit et retourne une r�ponse n�gative avec le motif pass� en param�tre<br/>
     * A utiliser essentiellement pour des communications internes au diff�rents modules d'analyse.
     * 
     * @param motif
     *            le motif du rejet
     * @param idTiers
     *            ID du tiers concern� par le motif
     * @return la r�ponse, invariable
     */
    public static REReponseModuleAnalyseEcheance Faux(REMotifEcheance motif, String idTiers) {
        return new REReponseModuleAnalyseEcheance(null, false, motif, idTiers);
    }

    /**
     * Construit et retourne une r�ponse positive avec le motif pass� en param�tre
     * 
     * @param motif
     *            le motif de l'acceptation (ou de refus dans des cas internes)
     * @param idTiers
     *            ID du tiers concern� par le motif
     * @return la r�ponse, invariable
     */
    public static final REReponseModuleAnalyseEcheance Vrai(IRERenteEcheances rente, REMotifEcheance motif,
            String idTiers) {
        return new REReponseModuleAnalyseEcheance(rente, true, motif, idTiers);
    }

    /**
     * Construit et retourne une r�ponse positive avec le motif et les remarques pass�s en param�tre
     * 
     * @param motif
     *            le motif de l'acceptation (ou de refus dans des cas internes)
     * @param idTiers
     *            ID du tiers concern� par le motif
     * @param remarques
     * @return la r�ponse, invariable
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
     * Retourne le motif de la r�ponse
     * 
     * @return motif de la r�ponse
     * @see REMotifEcheance
     */
    public REMotifEcheance getMotif() {
        return motif;
    }

    /**
     * Retourne d'�ventuelles remarques sur le motif<br/>
     * Exemple : la date de fin de la p�riode d'�tude, si elle est ant�rieure au mois de traitement
     * 
     * @return
     */
    public String getRemarques() {
        return remarques;
    }

    /**
     * D�fini si l'�ch�ance doit �tre ajout�e � celles � imprimer dans la liste
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
     * @return the rente accord�e qui � provoqu� le motif d'�ch�ance
     */
    public final IRERenteEcheances getRente() {
        return rente;
    }

}
