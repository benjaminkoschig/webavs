package globaz.pavo.db.splitting;

import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.db.gestion.HEAnnoncesViewBean;
import globaz.hermes.db.gestion.HEOutputAnnonceListViewBean;
import globaz.hermes.db.gestion.HEOutputAnnonceViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pavo.application.CIApplication;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;

public class CIAnnonceSplitting {

    /**
     * Méthode de test de conformité du partage
     * 
     * @param conjoint
     *            instance de même type que cette classe contenant les données du conjoint
     * @return true si les montants de splitting donnés et reçus sont bien identiques avec le conjoint, ainsi que les
     *         totaux.
     * @author David Girardin
     */
    private static HashMap<String, Boolean> checkSplitting(CIAnnonceSplitting assure, CIAnnonceSplitting conjoint,
            HashMap<String, Boolean> result) {
        // test pour toutes les cumuls
        CICumulSplitting cumulAssure;
        CICumulSplitting cumulConjoint;
        if (assure.exists() && conjoint.exists()) {

            Iterator<String> per = assure.getPeriodes().iterator();
            while (per.hasNext()) {
                String annee = per.next();
                cumulAssure = assure.getCumuls().get(annee);
                cumulConjoint = conjoint.getCumuls().get(annee);
                if (!cumulAssure.checkSplitting(cumulConjoint)) {
                    // année en erreur
                    result.put(annee, new Boolean(false));
                } else {
                    // test ok, ajout seulement si pas déjà existant
                    if (!result.containsKey(annee)) {
                        result.put(annee, new Boolean(true));
                    }
                }
            }
        }
        return result;
    }

    private String avs = new String();
    private Hashtable<String, CICumulSplitting> container;

    private String nom = new String();

    private ArrayList<String> periodes;

    public CIAnnonceSplitting(BTransaction transaction, String referenceUnique, boolean extractInfo) throws Exception {
        buildCumuls(transaction, referenceUnique, extractInfo);
    }

    private String _getCode1ou2(HEAnnoncesViewBean entity) throws globaz.hermes.db.gestion.HEOutputAnnonceException {
        return entity.getField(IHEAnnoncesViewBean.CODE_1_OU_2);
    }

    private String _getCodeApplication(HEAnnoncesViewBean entity)
            throws globaz.hermes.db.gestion.HEOutputAnnonceException {
        return entity.getField(IHEAnnoncesViewBean.CODE_APPLICATION);
    }

    private void buildCumuls(BTransaction transaction, String referenceUnique, boolean extractInfo) throws Exception {
        BTransaction remoteTransaction = null;
        BStatement statement = null;
        HEOutputAnnonceListViewBean manager = new HEOutputAnnonceListViewBean();
        try {
            HEOutputAnnonceViewBean entity;
            container = new Hashtable<String, CICumulSplitting>();
            // remote session et transaction
            if (transaction == null || transaction.getSession() == null) {
                throw new Exception();
            }
            CIApplication application = (CIApplication) transaction.getSession().getApplication();
            BSession remoteSession = (BSession) application.getSessionAnnonce(transaction.getSession());
            remoteTransaction = (BTransaction) remoteSession.newTransaction();
            remoteTransaction.openTransaction();
            if (extractInfo) {
                // extraction nom no avs
                extractCode2001(remoteTransaction, referenceUnique);
                // extraction périodes
                extractCode2203(remoteTransaction, referenceUnique);
            }
            // recherche de toutes les inscriptions
            manager.setSession(remoteSession);
            manager.setForRefUnique(referenceUnique);
            manager.setOrder("RNIANN");
            manager.setLikeEnregistrement("38");
            statement = manager.cursorOpen(remoteTransaction);

            // lit le nouveau entity
            while ((entity = (HEOutputAnnonceViewBean) manager.cursorReadNext(statement)) != null) {
                extractValues(transaction.getSession(), entity);
            }
        } finally {
            try {
                if (statement != null) {
                    try {
                        manager.cursorClose(statement);
                    } finally {
                        statement.closeStatement();
                    }
                }
            } finally {
                if (remoteTransaction != null) {
                    remoteTransaction.closeTransaction();
                }
            }
        }
    }

    /**
     * Méthode de test de conformité du partage
     * 
     * @param conjoint
     *            instance de même type que cette classe contenant les données du conjoint
     * @return true si les montants de splitting donnés et reçus sont bien identiques avec le conjoint, ainsi que les
     *         totaux.
     * @author David Girardin
     */
    public HashMap<String, Boolean> checkSplitting(CIAnnonceSplitting conjoint) {
        // effectuer une boucle sur l'assuré et le conjoint
        HashMap<String, Boolean> result = new HashMap<String, Boolean>();
        checkSplitting(this, conjoint, result);
        checkSplitting(conjoint, this, result);
        return result;

    }

    public boolean exists() {
        return !JadeStringUtil.isBlank(getNom());
    }

    private void extractCode2001(BTransaction transaction, String referenceUnique) throws Exception {
        HEOutputAnnonceListViewBean manager = new HEOutputAnnonceListViewBean();
        HEOutputAnnonceViewBean entity;
        manager.setISession(transaction.getSession());
        manager.setForRefUnique(referenceUnique);
        manager.setLikeEnregistrement("2001");
        // BStatement statement = manager.cursorOpen(transaction);
        manager.find(transaction);
        // PO 5118 : Ne plus prendre uniquement le premier enreg car le nom ou le n° avs peut être vide
        for (int i = 0; (i < manager.getSize() && (JadeStringUtil.isBlank(nom) || JadeStringUtil.isBlank(avs))); i++) {
            entity = (HEOutputAnnonceViewBean) manager.getEntity(i);
            nom = (entity.getField(IHEAnnoncesViewBean.ETAT_NOMINATIF));
            avs = (entity.getNumeroAvsFormatted());
        }
    }

    private void extractCode2203(BTransaction transaction, String referenceUnique) throws Exception {
        HEOutputAnnonceListViewBean manager = new HEOutputAnnonceListViewBean();
        HEOutputAnnonceViewBean entity;
        manager.setISession(transaction.getSession());
        manager.setForRefUnique(referenceUnique);
        manager.setLikeEnregistrement("2203");
        manager.find(transaction);
        if (manager.size() != 0) {
            entity = (HEOutputAnnonceViewBean) manager.getFirstEntity();
            periodes = new ArrayList<String>();
            for (int i = 1; i < 10; i++) {
                String periode = entity.getField(
                        HEOutputAnnonceViewBean.class.getField("LISTE_ANNEES_DEBUT_FIN_CHIFFRECLEF_AAAA_0" + i)
                                .get(null).toString()).trim();
                if (periode != null && periode.length() == 8) {
                    // pas de chiffre particulier -> mandat automatique à
                    // prendre en compte pour les tests
                    int current = Integer.parseInt(periode.substring(0, 4));
                    int last = Integer.parseInt(periode.substring(4));
                    // ajout de toutes les périodes
                    for (; current <= last; current++) {
                        periodes.add(String.valueOf(current));
                    }
                }
            }
        }

    }

    private void extractValues(BSession session, HEOutputAnnonceViewBean entity) throws Exception {
        String codeApplication = _getCodeApplication(entity);
        String code12 = _getCode1ou2(entity);

        if (codeApplication.equals("38") && code12.equals("1")) {
            String currYear = entity.getField(IHEAnnoncesViewBean.ANNEES_COTISATIONS_AAAA);
            CICumulSplitting bean = container.get(currYear);
            if (bean == null) {
                bean = new CICumulSplitting(session);
            }
            // Modif jmc 1-5-8, les codes 3,5 et 7 sont également des codes
            // extournes négatifs (vieilles écritures)
            boolean extourne = "1".equals(entity.getField(IHEAnnoncesViewBean.CHIFFRE_CLE_EXTOURNES))
                    || "3".equals(entity.getField(IHEAnnoncesViewBean.CHIFFRE_CLE_EXTOURNES))
                    || "5".equals(entity.getField(IHEAnnoncesViewBean.CHIFFRE_CLE_EXTOURNES))
                    || "7".equals(entity.getField(IHEAnnoncesViewBean.CHIFFRE_CLE_EXTOURNES));
            boolean ecrSplitting = "8"
                    .equals(entity.getField(IHEAnnoncesViewBean.CHIFFRE_CLE_GENRE_COTISATIONS).trim());
            boolean noPart = "".equals(entity.getField(IHEAnnoncesViewBean.CHIFFRE_CLEF_PARTICULIER).trim());
            String code = entity.getField(IHEAnnoncesViewBean.CODE_A_D_S);
            bean.add(session, currYear, entity.getField(IHEAnnoncesViewBean.REVENU), extourne, ecrSplitting, noPart,
                    code);

            container.put(currYear, bean);
        }
    }

    /**
     * @return
     */
    public String getAvs() {
        return avs;
    }

    /**
     * @return
     */
    public Hashtable<String, CICumulSplitting> getCumuls() {
        return container;
    }

    /**
     * @return
     */
    public String getNom() {
        return nom;
    }

    public ArrayList<String> getPeriodes() {
        return periodes;
    }
}
