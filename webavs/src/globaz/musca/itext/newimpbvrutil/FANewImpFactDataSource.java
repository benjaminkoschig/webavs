package globaz.musca.itext.newimpbvrutil;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeListUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.musca.db.facturation.FAAfact;
import globaz.musca.db.facturation.FAAfactImpressionManager;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.musca.db.facturation.FAOrdreAttribuer;
import globaz.musca.db.facturation.FAOrdreAttribuerManager;
import globaz.musca.db.facturation.FAPassage;
import globaz.musca.itext.FANewImpressionFacture_DS;
import globaz.osiris.api.APISection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Object qui va permettre de charger l'ensemble des datas pour une impression de facture BVR. Sert donc de datasource à
 * la génération et impression des documents traités par <code>FAImpressionFacture_BVR_Doc</code>
 * 
 * @author VYJ
 */
public class FANewImpFactDataSource {

    /**
     * Méthode qui permet d'initialiser et charger le contenu des données à charger pour générer les documents BVR
     * 
     * @param transaction
     *            La transaction à utiliser pour charger les datas
     * @param passage
     *            Le passage géré par l'impression
     * @param _enteteFacture
     *            L'entête de facture qui est gérée par cet objet
     * @return Le datasource contenant l'ensemble des données à utiliser pour générer et imprimer le document BVR
     * @throws Exception
     *             Levée en cas de problème lors du chargement des données
     */
    public static FANewImpFactDataSource init(BSession session, FAPassage passage, FAEnteteFacture _enteteFacture) {
        FANewImpFactDataSource ds = null;
        if ((_enteteFacture != null) && !JadeStringUtil.isEmpty(_enteteFacture.getIdEntete())) {
            ds = new FANewImpFactDataSource(_enteteFacture);
            ds.init(session, passage);
        }
        return ds;
    }

    /**
     * Adresse principale à fournir à l'impression
     */
    private String adressePrincipale = null;
    /**
     * L'entête en cours de chargement
     */
    private FAEnteteFacture enteteFacture = null;
    /**
     * Contient les erreurs de chargement si il y en a
     */
    private StringBuffer errorLog = null;
    /**
     * Le datasource des lignes de factures
     */
    private FANewImpressionFacture_DS impressionFacture_DS = null;

    /**
     * Permet de connaître le temps passé au chargement des données
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
     *            L'entête de facture à traiter
     */
    private FANewImpFactDataSource(FAEnteteFacture _enteteFacture) {
        super();
        enteteFacture = _enteteFacture;
        errorLog = new StringBuffer();
    }

    /**
     * @return L'adresse principale à utiliser lors de la génération du document à imprimer
     */
    public String getAdressePrincipale() {
        return adressePrincipale;
    }

    /**
     * @return L'entête de facture gérée par ce datasource
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
     * @return Le datasource qui contient les lignes à spécifier dans le document
     */
    public FANewImpressionFacture_DS getImpressionFacture_DS() {
        return impressionFacture_DS;
    }

    /**
     * @return Le temps passé à charger les données
     */
    public long getLoadingTime() {
        return loadingTime;
    }

    public FWCurrency getMontantImprimer() {
        return montantImprimer;
    }

    /**
     * @return Le nombre de lignes à traiter
     */
    public Integer getNbDeLigne() {
        return new Integer(nbDeLigne);
    }

    /**
     * @return <code>true</code> si des erreurs ont été trappées lors du chargement de l'objet, <code>false</code> sinon
     */
    public boolean hasError() {
        return errorLog.length() > 0;
    }

    /**
     * Méthode qui effectue le chargement des données - initialise cet objet
     * 
     * @param transaction
     *            La transaction à utiliser pour charger les datas
     * @param passage
     *            Le passage géré par l'impression
     * @throws Exception
     *             Levée en cas de problème lors du chargement des données
     */
    private void init(BSession session, FAPassage passage) {
        try {
            setMapNumCaisseIdRubrique(session);
        } catch (Exception e1) {
            JadeLogger.warn(this, "problem with thread : " + e1.getClass().getName() + " - " + e1.getMessage()
                    + " unable to retrieve map : caisse number / rubrique");
        }
        long startTime = System.currentTimeMillis();
        // Charge le tiers de l'entete
        enteteFacture.initTiers();
        // Set le passage !?? -> Dans le code, connais pas l'effet réel du
        // truc...
        enteteFacture.setPassage(passage);
        // Initialise l'adresse principale - Passe null pour la transaction,
        // elle est totalement inutile dans cette méthode
        try {
            adressePrincipale = enteteFacture.getAdressePrincipale(null, passage.getDateFacturation());
        } catch (Exception e) {
            errorLog.append("Impossible de retrouver l'adresse principale du domicile ou du paiement de l'entête de facture : ID="
                    + enteteFacture.getIdEntete() + " - Raison : " + e.getClass().getName() + " - " + e.getMessage());
            if (FANewImpFactPropertiesProvider.IS_DEBUG_MODE) {
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
            // Donc : doit créer des lists car des regroupements sont possible
            // et
            // traiter la ligne de facture par liste
            // Donc : doit créer des lists car des regroupements sont possible
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
            impressionFacture_DS = new FANewImpressionFacture_DS(container, enteteFacture);
            nbDeLigne = container.size();
        } catch (Exception e) {
            // Si le datasource n'est pas instancié, l'instancie histoire de ne
            // pas faire planter la génération du document PDF
            if (impressionFacture_DS == null) {
                impressionFacture_DS = new FANewImpressionFacture_DS(enteteFacture);
            }
            // Ajoute un message d'erreur
            errorLog.append("Problème lors du chargement des données liées à l'entête ID :"
                    + enteteFacture.getIdEntete() + " - Raison : " + e.getClass().getName() + " - " + e.getMessage());
            if (FANewImpFactPropertiesProvider.IS_DEBUG_MODE) {
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

    private void setListNumCaisseParIdRubrique(Map<String, List<FAOrdreAttribuer>> _listNumCaisseParIdRubrique) {
    }

    private void setMapNumCaisseIdRubrique(BSession session) throws Exception {
        FAOrdreAttribuerManager mgr = new FAOrdreAttribuerManager();
        mgr.setSession(session);
        mgr.find(BManager.SIZE_NOLIMIT);

        Map<String, List<FAOrdreAttribuer>> g = JadeListUtil.groupBy(mgr.iterator(),
                new JadeListUtil.Key<FAOrdreAttribuer>() {
                    @Override
                    public String exec(final FAOrdreAttribuer e) {
                        return e.getNumCaisse() + "-" + e.getIdRubrique();
                    }
                });
        setListNumCaisseParIdRubrique(g);
    }
}
