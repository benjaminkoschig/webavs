package globaz.musca.itext.impfactbvrutil;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.musca.db.facturation.FAAfact;
import globaz.musca.db.facturation.FAAfactImpressionManager;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.musca.db.facturation.FAPassage;
import globaz.musca.itext.FAImpressionFacture_DS;
import globaz.osiris.api.APISection;
import java.util.ArrayList;

/**
 * Object qui va permettre de charger l'ensemble des datas pour une impression de facture BVR. Sert donc de datasource �
 * la g�n�ration et impression des documents trait�s par <code>FAImpressionFacture_BVR_Doc</code>
 * 
 * @author VYJ
 */
public class FAImpFactDataSource {

    /**
     * M�thode qui permet d'initialiser et charger le contenu des donn�es � charger pour g�n�rer les documents BVR
     * 
     * @param transaction
     *            La transaction � utiliser pour charger les datas
     * @param passage
     *            Le passage g�r� par l'impression
     * @param _enteteFacture
     *            L'ent�te de facture qui est g�r�e par cet objet
     * @param mapNumCaisseIdRubrique
     * @return Le datasource contenant l'ensemble des donn�es � utiliser pour g�n�rer et imprimer le document BVR
     * @throws Exception
     *             Lev�e en cas de probl�me lors du chargement des donn�es
     */
    public static FAImpFactDataSource init(BSession session, FAPassage passage, FAEnteteFacture _enteteFacture) {
        FAImpFactDataSource ds = null;
        if ((_enteteFacture != null) && !JadeStringUtil.isEmpty(_enteteFacture.getIdEntete())) {
            ds = new FAImpFactDataSource(_enteteFacture);
            ds.init(session, passage);
        }
        return ds;
    }

    /**
     * Adresse principale � fournir � l'impression
     */
    private String adressePrincipale = null;

    /**
     * L'ent�te en cours de chargement
     */
    private FAEnteteFacture enteteFacture = null;

    /**
     * Contient les erreurs de chargement si il y en a
     */
    private StringBuffer errorLog = null;
    /**
     * Le datasource des lignes de factures
     */
    private FAImpressionFacture_DS impressionFacture_DS = null;
    // private Map<String, List<FAOrdreAttribuer>> listNumCaisseParIdRubrique = null;

    /**
     * Permet de conna�tre le temps pass� au chargement des donn�es
     */
    private long loadingTime = 0;

    /**
     * Le montant imprimer sur la facture (Pas sur le BVR)
     */
    private FWCurrency montantImprimer;

    /**
     * Le nombre de lignes de factures
     */
    private int nbDeLigne = 0;

    /**
     * @param _enteteFacture
     *            L'ent�te de facture � traiter
     */
    private FAImpFactDataSource(FAEnteteFacture _enteteFacture) {
        super();
        enteteFacture = _enteteFacture;
        errorLog = new StringBuffer();
    }

    /**
     * @return L'adresse principale � utiliser lors de la g�n�ration du document � imprimer
     */
    public String getAdressePrincipale() {
        return adressePrincipale;
    }

    /**
     * @return L'ent�te de facture g�r�e par ce datasource
     */
    public FAEnteteFacture getEnteteFacture() {
        return enteteFacture;
    }

    /**
     * @return Le contenu du log des erreurs
     */
    public StringBuffer getErrorLog() {
        return errorLog;
    }

    /**
     * @return Le datasource qui contient les lignes � sp�cifier dans le document
     */
    public FAImpressionFacture_DS getImpressionFacture_DS() {
        return impressionFacture_DS;
    }

    /**
     * @return Le temps pass� � charger les donn�es
     */
    public long getLoadingTime() {
        return loadingTime;
    }

    public FWCurrency getMontantImprimer() {
        return montantImprimer;
    }

    /**
     * @return Le nombre de lignes � traiter
     */
    public Integer getNbDeLigne() {
        return new Integer(nbDeLigne);
    }

    /**
     * @return <code>true</code> si des erreurs ont �t� trapp�es lors du chargement de l'objet, <code>false</code> sinon
     */
    public boolean hasError() {
        return errorLog.length() > 0;
    }

    /**
     * M�thode qui effectue le chargement des donn�es - initialise cet objet
     * 
     * @param transaction
     *            La transaction � utiliser pour charger les datas
     * @param passage
     *            Le passage g�r� par l'impression
     * @throws Exception
     *             Lev�e en cas de probl�me lors du chargement des donn�es
     */
    private void init(BSession session, FAPassage passage) {
        long startTime = System.currentTimeMillis();
        // Charge le tiers de l'entete
        enteteFacture.initTiers();
        // Set le passage !?? -> Dans le code, connais pas l'effet r�el du
        // truc...
        enteteFacture.setPassage(passage);
        // Initialise l'adresse principale - Passe null pour la transaction,
        // elle est totalement inutile dans cette m�thode
        try {
            adressePrincipale = enteteFacture.getAdressePrincipale(null, passage.getDateFacturation());
        } catch (Exception e) {
            errorLog.append("Impossible de retrouver l'adresse principale du domicile ou du paiement de l'ent�te de facture : ID="
                    + enteteFacture.getIdEntete() + " - Raison : " + e.getClass().getName() + " - " + e.getMessage());
            if (FAImpFactPropertiesProvider.IS_DEBUG_MODE) {
                JadeLogger.warn(this, errorLog.toString());
            }
        }

        try {
            // On recherche les afacts
            FAAfactImpressionManager factImpressionManager = new FAAfactImpressionManager();
            factImpressionManager.setSession(session);
            factImpressionManager.setForAQuittancer(new Boolean(false));
            factImpressionManager.setForIdEnteteFacture(enteteFacture.getIdEntete());
            // factImpressionManager.setOrderBy(" EHIDOR ASC, ANNEECOTISATION ASC, IDEXTERNE ASC ");
            factImpressionManager.setForImpression(true);
            factImpressionManager.find(BManager.SIZE_NOLIMIT);
            // On prend une map qui contient les numCaisse par rapport au idRubrique
            // Donc : doit cr�er des lists car des regroupements sont possible
            // et
            // traiter la ligne de facture par liste
            // Donc : doit cr�er des lists car des regroupements sont possible
            // et
            // traiter la ligne de facture par liste
            FAAfact preceding = null;
            ArrayList<FAAfact> ligne = null;
            ArrayList<ArrayList<FAAfact>> container = new ArrayList<ArrayList<FAAfact>>();
            for (int i = 0; i < factImpressionManager.size(); i++) {
                FAAfact current = (FAAfact) factImpressionManager.getEntity(i);
                if ((preceding != null) && preceding.getNumCaisse().equals(current.getNumCaisse())
                        && preceding.getIdOrdreRegroupement().equals(current.getIdOrdreRegroupement())
                        && !JadeStringUtil.isEmpty(current.getLibelleOrdre(enteteFacture.getISOLangueTiers()))
                        && !APISection.ID_CATEGORIE_SECTION_CONTROLE_EMPLOYEUR.equals(enteteFacture.getIdSousType())) {
                    ligne.add(current);
                } else {
                    ligne = new ArrayList<FAAfact>();
                    ligne.add(current);
                    container.add(ligne);
                }
                preceding = current;
            }

            // Initialise le dataSource de l'impression des factures
            montantImprimer = montantFactureImprimer(container, enteteFacture);
            impressionFacture_DS = new FAImpressionFacture_DS(container, enteteFacture);
            nbDeLigne = container.size();
        } catch (Exception e) {
            // Si le datasource n'est pas instanci�, l'instancie histoire de ne
            // pas faire planter la g�n�ration du document PDF
            if (impressionFacture_DS == null) {
                impressionFacture_DS = new FAImpressionFacture_DS(enteteFacture);
            }
            // Ajoute un message d'erreur
            errorLog.append("Probl�me lors du chargement des donn�es li�es � l'ent�te ID :"
                    + enteteFacture.getIdEntete() + " - Raison : " + e.getClass().getName() + " - " + e.getMessage());
            if (FAImpFactPropertiesProvider.IS_DEBUG_MODE) {
                JadeLogger.warn(this, errorLog.toString());
            }
        } finally {
            loadingTime = System.currentTimeMillis() - startTime;
        }
    }

    private FWCurrency montantFactureImprimer(ArrayList<ArrayList<FAAfact>> container, FAEnteteFacture entete) {
        FWCurrency totalContainer = new FWCurrency(0);
        for (int j = 0; j < container.size(); j++) {
            ArrayList<?> ligneFacture = container.get(j);
            if (ligneFacture.size() > 1) {
                for (int x = 0; x < ligneFacture.size(); x++) {
                    FAAfact afact = (FAAfact) ligneFacture.get(x);
                    totalContainer.add(afact.getMontantFactureToCurrency());
                }
            } else {
                totalContainer.add(((FAAfact) ligneFacture.get(0)).getMontantFactureToCurrency());
            }
        }
        return totalContainer;
    }
}
