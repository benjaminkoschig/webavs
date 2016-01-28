/**
 * 
 */
package globaz.helios.db.classifications;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BManager;
import globaz.globall.db.BTransaction;
import globaz.helios.db.comptes.CGCompte;
import globaz.helios.itext.list.balance.comptes.CGCompteSoldeBean;
import globaz.helios.translation.CodeSystem;
import globaz.jade.client.util.JadeStringUtil;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Classe proposant les services pour géréer la classification, les comptes et les soldes
 * 
 * @author sel
 * 
 */
public class CGClasseCompteServices {

    /**
     * Retourne le libellé traduit de classe compte
     * 
     * @param idLangueISO
     * @return le libellé de la classification dans la bonne langue
     */
    public static String computeClasseCompteLevelLibelle(String idLangueISO, CGClasseCompte classeCompteLevel) {
        if (classeCompteLevel == null) {
            return "";
        }

        String retour = classeCompteLevel.getLibelleFr();
        if ("FR".equalsIgnoreCase(idLangueISO)) {
            retour = classeCompteLevel.getLibelleFr();
        } else if ("DE".equalsIgnoreCase(idLangueISO)) {
            retour = classeCompteLevel.getLibelleDe();
        } else if ("IT".equalsIgnoreCase(idLangueISO)) {
            retour = classeCompteLevel.getLibelleIt();
        }

        return retour;
    }

    /**
     * Retourne le libellé traduit du compte
     * 
     * @param idLangueISO
     * @param compteSolde
     * @return le libellé du compte dans la bonne langue
     */
    public static String computeLibellePlanComptable(String idLangueISO, CGExtendedCompte compteSolde) {
        if (compteSolde == null) {
            return "";
        }

        String retour = compteSolde.getLibelleFrPlanComptable();
        if ("FR".equalsIgnoreCase(idLangueISO)) {
            retour = compteSolde.getLibelleFrPlanComptable();
        } else if ("DE".equalsIgnoreCase(idLangueISO)) {
            retour = compteSolde.getLibelleDePlanComptable();
        } else if ("IT".equalsIgnoreCase(idLangueISO)) {
            retour = compteSolde.getLibelleItPlanComptable();
        }

        return retour;
    }

    /**
     * Retourne le libellé traduit du compte
     * 
     * @param idLangueISO
     * @param compteSolde
     * @return le libellé du compte dans la bonne langue
     */
    public static String computeLibellePlanComptable(String idLangueISO, CGExtendedCompteSolde compteSolde) {
        if (compteSolde == null) {
            return "";
        }

        String retour = compteSolde.getLibFrPlanComptable();
        if ("FR".equalsIgnoreCase(idLangueISO)) {
            retour = compteSolde.getLibFrPlanComptable();
        } else if ("DE".equalsIgnoreCase(idLangueISO)) {
            retour = compteSolde.getLibDePlanComptable();
        } else if ("IT".equalsIgnoreCase(idLangueISO)) {
            retour = compteSolde.getLibItPlanComptable();
        }

        return retour;
    }

    /**
     * @param idLangueISO
     * @param provisoire
     * @param compte
     * @return
     */
    public static CGCompteSoldeBean createComptesBean(String idLangueISO, boolean provisoire, CGExtendedCompte compte) {

        if (compte == null) {
            throw new IllegalArgumentException("Error : CGExtendedCompte is null !");
        }

        CGCompteSoldeBean beanCompte = null;

        beanCompte = new CGCompteSoldeBean();
        beanCompte.setNoCompteLibelle(compte.getIdExterne() + " "
                + CGClasseCompteServices.computeLibellePlanComptable(idLangueISO, compte));
        beanCompte.setNoClasse(compte.getIdExterne());
        beanCompte.setIdGenre(compte.getIdGenre());
        beanCompte.setIdDomaine(compte.getIdDomaine());
        beanCompte.setIdNature(compte.getIdNature());
        beanCompte.setMonnaieCodeIso(compte.getCodeISOMonnaie());

        return beanCompte;
    }

    /**
     * @param idLangueISO
     * @param provisoire
     * @param compteSolde
     * @return un bean renseigné en fonction des paramètres
     */
    public static CGCompteSoldeBean createCompteSoldeBean(String idLangueISO, boolean provisoire,
            CGExtendedCompteSolde compteSolde) {

        if (compteSolde == null) {
            throw new IllegalArgumentException("Error : CGExtendedCompteSolde is null !");
        }

        CGCompteSoldeBean beanCompteSolde = null;

        String montantDoit = "";
        String montantAvoir = "";
        String montantDoitMonnaie = "";
        String montantAvoirMonnaie = "";
        String soldeMonnaie = "";

        if (provisoire) {
            montantDoit = compteSolde.getDoitprovisoire();
            montantAvoir = compteSolde.getAvoirprovisoire();
            montantDoitMonnaie = compteSolde.getDoitprovisoiremonnaie();
            montantAvoirMonnaie = compteSolde.getAvoirprovisoiremonnaie();
            soldeMonnaie = compteSolde.getSoldeprovisoiremonnaie();
        } else {
            montantDoit = compteSolde.getDoit();
            montantAvoir = compteSolde.getAvoir();
            montantDoitMonnaie = compteSolde.getDoitmonnaie();
            montantAvoirMonnaie = compteSolde.getAvoirmonnaie();
            soldeMonnaie = compteSolde.getSoldemonnaie();
        }

        if (!CGClasseCompteServices.hasMontant(montantDoit, montantAvoir)) {
            return null;
        }

        beanCompteSolde = new CGCompteSoldeBean();
        beanCompteSolde.setNoCompteLibelle(compteSolde.getIdExterne() + " "
                + CGClasseCompteServices.computeLibellePlanComptable(idLangueISO, compteSolde));
        beanCompteSolde.setMvtDebit(CGClasseCompteServices.stringToDouble(montantDoit));
        beanCompteSolde.setMvtDebitMonnaie(CGClasseCompteServices.stringToDouble(montantDoitMonnaie));
        // On inverse le signe
        beanCompteSolde.setMvtCredit(-CGClasseCompteServices.stringToDouble(montantAvoir));
        beanCompteSolde.setMvtCreditMonnaie(-CGClasseCompteServices.stringToDouble(montantAvoirMonnaie));
        beanCompteSolde.setNoClasse(compteSolde.getIdExterne());
        beanCompteSolde.setIdGenre(compteSolde.getIdGenre());
        beanCompteSolde.setIdDomaine(compteSolde.getIdDomaine());
        beanCompteSolde.setIdNature(compteSolde.getIdNature());
        beanCompteSolde.setMonnaieCodeIso(compteSolde.getCodeISOMonnaie());
        beanCompteSolde.setBudget(Double.valueOf(compteSolde.getBudget()));
        // beanCompteSolde.setSolde(CGClasseCompteServices.montantStringToMontantDouble(solde));
        beanCompteSolde.setSolde(beanCompteSolde.computeSoldeCharge() != null ? beanCompteSolde.computeSoldeCharge()
                : beanCompteSolde.computeSoldeProduit());
        beanCompteSolde.setSoldeMonnaie(CGClasseCompteServices.stringToDouble(soldeMonnaie));
        beanCompteSolde.setIdCompte(compteSolde.getIdCompte());
        beanCompteSolde.setBudgetPourcent(beanCompteSolde.computeBudgetPourcent());

        // Gestion monnaie etrangère
        if (!JadeStringUtil.isBlankOrZero("" + beanCompteSolde.getMvtCreditMonnaie())) {
            FWCurrency creditMonnaie = new FWCurrency(beanCompteSolde.getMvtCreditMonnaie());
            beanCompteSolde.setMvtCreditMonnaieString("(" + beanCompteSolde.getMonnaieCodeIso() + ": "
                    + creditMonnaie.toStringFormat() + ")");
        }
        if (!JadeStringUtil.isBlankOrZero("" + beanCompteSolde.getMvtDebitMonnaie())) {
            FWCurrency debitMonnaie = new FWCurrency(beanCompteSolde.getMvtDebitMonnaie());
            beanCompteSolde.setMvtDebitMonnaieString("(" + beanCompteSolde.getMonnaieCodeIso() + ": "
                    + debitMonnaie.toStringFormat() + ")");
        }

        FWCurrency soldeDebitMonnaie = new FWCurrency(CGClasseCompteServices.stringToDouble(montantDoitMonnaie)
                - (-CGClasseCompteServices.stringToDouble(montantAvoirMonnaie)));
        if (soldeDebitMonnaie.isPositive()) {
            beanCompteSolde.setSoldeDebitMonnaie("(" + beanCompteSolde.getMonnaieCodeIso() + ": "
                    + soldeDebitMonnaie.toStringFormat() + ")");
        }

        FWCurrency soldeCreditMonnaie = new FWCurrency(-CGClasseCompteServices.stringToDouble(montantAvoirMonnaie)
                - CGClasseCompteServices.stringToDouble(montantDoitMonnaie));
        if (soldeCreditMonnaie.isPositive()) {
            beanCompteSolde.setSoldeCreditMonnaie("(" + beanCompteSolde.getMonnaieCodeIso() + ": "
                    + soldeCreditMonnaie.toStringFormat() + ")");
        }

        return beanCompteSolde;
    }

    /**
     * @param transaction
     * @param forIdClassification
     * @return
     * @throws Exception
     */
    public static CGClasseCompteManager findClasseCompte(BTransaction transaction, String forIdClassification)
            throws Exception {
        CGClasseCompteManager manager = new CGClasseCompteManager();
        manager.setSession(transaction.getSession());
        manager.setForIdClassification(forIdClassification);
        manager.changeManagerSize(BManager.SIZE_NOLIMIT);
        manager.orderByIdSuperClasse("IDSUPERCLASSE");
        manager.find(transaction);

        return manager;
    }

    /**
     * @param doit
     * @param avoir
     * @return true si un des paramètres est un montant
     */
    public static boolean hasMontant(String doit, String avoir) {
        return !(JadeStringUtil.isBlankOrZero(doit) && JadeStringUtil.isBlankOrZero(avoir));
    }

    /**
     * @param niveauPatron
     * @param niveauATester
     * @return TRUE si niveauATester > niveauPatron
     */
    public static boolean isNiveauClassificationToBig(int niveauPatron, String niveauATester) {
        return !JadeStringUtil.isBlank(niveauATester) && (Integer.valueOf(niveauATester) > niveauPatron);
    }

    /**
     * @param idComptabilite
     * @return true if provisoire
     */
    public static boolean isProvisoire(String idComptabilite) {
        return CodeSystem.CS_PROVISOIRE.equals(idComptabilite);
    }

    /**
     * @param compteSoldeExerPrecedentManager
     * @param idLangueISO
     * @param provisoire
     * @return
     */
    private static Map<String, CGCompteSoldeBean> loadBeanToCompare(
            CGExtendedCompteSoldeManager compteSoldeExerPrecedentManager, String idLangueISO, boolean provisoire) {
        Map<String, CGCompteSoldeBean> beansToCompare = null;
        if ((compteSoldeExerPrecedentManager != null) && !compteSoldeExerPrecedentManager.isEmpty()) {
            beansToCompare = new HashMap<String, CGCompteSoldeBean>();
            for (Iterator<CGExtendedCompteSolde> it = compteSoldeExerPrecedentManager.iterator(); it.hasNext();) {
                CGExtendedCompteSolde compteSolde = it.next();
                CGCompteSoldeBean bean = CGClasseCompteServices.createCompteSoldeBean(idLangueISO, provisoire,
                        compteSolde);
                beansToCompare.put(compteSolde.getIdCompte(), bean);
            }
        }
        return beansToCompare;
    }

    /**
     * Charge la structure de classification
     * 
     * @param nodesDepot
     *            : dépot de noeuds
     * @param manager
     *            : collection des classification
     * @param idLangueISO
     *            : langue pour la description
     */
    public static void loadClassification(String idLangueISO, CGClasseCompteManager manager,
            Map<String, CGClassificationNode<CGCompteSoldeBean>> nodesDepot, List<String> classeListe) {

        if (manager == null) {
            throw new IllegalArgumentException("Error : CGClasseCompteManager manager is null !");
        }

        if (nodesDepot == null) {
            throw new IllegalArgumentException(
                    "Error : Map<String, CGClassificationNode<CGBalanceComptesBean>> nodesDepot is null !");
        }

        if (classeListe == null) {
            throw new IllegalArgumentException("Error : List<String> classeListe is null !");
        }

        if (classeListe.isEmpty()) {
            return;
        }

        for (Iterator<CGClasseCompte> it = manager.iterator(); it.hasNext();) {
            CGClasseCompte classeCompteLevel = it.next();

            if (!classeListe.contains(classeCompteLevel.getIdClasseCompte())) {
                continue;
            }

            // Crée le bean
            CGCompteSoldeBean bean = new CGCompteSoldeBean();
            bean.setNiveau("0");
            bean.setNoClasse(classeCompteLevel.getNoClasse());
            bean.setNoClasseLibelle(classeCompteLevel.getNoClasse() + " "
                    + CGClasseCompteServices.computeClasseCompteLevelLibelle(idLangueISO, classeCompteLevel));

            // Crée le node
            CGClassificationNode<CGCompteSoldeBean> node = new CGClassificationNode<CGCompteSoldeBean>(bean);
            // Ajoute le noeud au dépot
            if (!nodesDepot.containsKey(classeCompteLevel.getIdClasseCompte())) {
                nodesDepot.put(classeCompteLevel.getIdClasseCompte(), node);
            }
            // Recherche son parent
            if (!JadeStringUtil.isBlankOrZero(classeCompteLevel.getIdSuperClasse())
                    && nodesDepot.containsKey(classeCompteLevel.getIdSuperClasse())) {
                // Renseigne le parent du noeud créé
                node.setParent(nodesDepot.get(classeCompteLevel.getIdSuperClasse()));
                // Renseigne le noeud créé comme enfant du noeud trouvé
                nodesDepot.get(classeCompteLevel.getIdSuperClasse()).addChild(classeCompteLevel.getNoClasse(), node);
            }
        }
    }

    /**
     * @param compteSoldeManager
     * @param nodesDepot
     * @param idLangueISO
     * @param provisoire
     * @param classeListe
     * @throws Exception
     */
    public static void loadCompteLevel(CGExtendedCompteManager compteSoldeManager,
            Map<String, CGClassificationNode<CGCompteSoldeBean>> nodesDepot, String idLangueISO, boolean provisoire,
            List<String> classeListe) throws Exception {

        if ((compteSoldeManager == null) || compteSoldeManager.isEmpty()) {
            throw new IllegalArgumentException("Error : CGExtendedCompteSoldeManager is null or empty !");
        }

        if (nodesDepot == null) {
            throw new IllegalArgumentException(
                    "Error : Map<String, CGClassificationNode<CGBalanceComptesBean>> is null !");
        }

        if (classeListe == null) {
            throw new IllegalArgumentException("Error : List<String> classeListe is null !");
        }

        if (classeListe.isEmpty()) {
            return;
        }

        for (Iterator<CGExtendedCompte> it = compteSoldeManager.iterator(); it.hasNext();) {
            CGExtendedCompte compteSolde = it.next();

            if (!classeListe.contains(compteSolde.getIdClasseCompte())) {
                continue;
            }

            // Crée le bean
            CGCompteSoldeBean bean = CGClasseCompteServices.createComptesBean(idLangueISO, provisoire, compteSolde);

            if (bean == null) {
                continue;
            }

            // Crée le node
            CGClassificationNode<CGCompteSoldeBean> node = new CGClassificationNode<CGCompteSoldeBean>(bean);

            // Pas besoins d'ajouter les noeuds au dépot car il s'agit de feuille qui n'ont pas d'enfants et
            // l'idClasseCompte est celui du parent

            // Recherche son parent
            if (!JadeStringUtil.isBlankOrZero(compteSolde.getIdClasseCompte())
                    && nodesDepot.containsKey(compteSolde.getIdClasseCompte())) {
                // Renseigne le parent du noeud créé
                node.setParent(nodesDepot.get(compteSolde.getIdClasseCompte()));
                // Renseigne le noeud créé comme enfant du noeud trouvé
                nodesDepot.get(compteSolde.getIdClasseCompte()).addChild(compteSolde.getIdExterne(), node);

                CGClasseCompteServices.recurseComputeTotal(node.getParent(), bean.getIdGenre(), bean.getMvtDebit(),
                        bean.getMvtCredit(), bean.getBudget(), null);
            } else {
                String error = "ERREUR de configuration des classifications de comptes !!! [idClasseCompte="
                        + compteSolde.getIdClasseCompte() + "]";
                throw new Exception(error);
            }

        }
    }

    /**
     * @param compteSoldeManager
     * @param compteSoldeExerPrecedentManager
     * @param nodesDepot
     * @param idLangueISO
     * @param provisoire
     * @param classeListe
     * @throws Exception
     */
    public static void loadCompteSoldeLevel(CGExtendedCompteSoldeManager compteSoldeManager,
            CGExtendedCompteSoldeManager compteSoldeExerPrecedentManager,
            Map<String, CGClassificationNode<CGCompteSoldeBean>> nodesDepot, String idLangueISO, boolean provisoire,
            List<String> classeListe) throws Exception {
        CGClasseCompteServices.loadCompteSoldeLevel(compteSoldeManager, null, nodesDepot, idLangueISO, provisoire,
                classeListe, null);
    }

    /**
     * Charge le dépot de noeuds avec les comptes soldes et met à jour les totaux et les niveaux
     * 
     * @param compteSoldeManager
     *            : collection des comptes soldes
     * @param compteSoldeExerPrecedentManager
     *            : collection des comptes soldes d'un exercice précédent
     * @param nodesDepot
     *            : dépot de noeuds stocké par idClasseCompte
     * @param idLangueISO
     *            : langue pour la description
     * @param provisoire
     *            : indique quel montant il faut utiliser
     * @param classeListe
     *            : liste des classes selectionnée
     * @param genreListe
     *            : liste des genres selectionnés
     * @throws Exception
     *             : si on ne trouve pas le parent du compte
     */
    public static void loadCompteSoldeLevel(CGExtendedCompteSoldeManager compteSoldeManager,
            CGExtendedCompteSoldeManager compteSoldeExerPrecedentManager,
            Map<String, CGClassificationNode<CGCompteSoldeBean>> nodesDepot, String idLangueISO, boolean provisoire,
            List<String> classeListe, List<String> genreListe) throws Exception {

        if ((compteSoldeManager == null)) {
            throw new IllegalArgumentException("Error : CGExtendedCompteSoldeManager is null !");
        }

        if (compteSoldeManager.isEmpty()) {
            throw new IllegalArgumentException("No record found !");
        }

        if (nodesDepot == null) {
            throw new IllegalArgumentException(
                    "Error : Map<String, CGClassificationNode<CGBalanceComptesBean>> is null !");
        }

        if (classeListe == null) {
            throw new IllegalArgumentException("Error : List<String> classeListe is null !");
        }

        if (classeListe.isEmpty()) {
            return;
        }

        Map<String, CGCompteSoldeBean> beansToCompare = CGClasseCompteServices.loadBeanToCompare(
                compteSoldeExerPrecedentManager, idLangueISO, provisoire);

        for (Iterator<CGExtendedCompteSolde> it = compteSoldeManager.iterator(); it.hasNext();) {
            CGExtendedCompteSolde compteSolde = it.next();

            if (!classeListe.contains(compteSolde.getIdClasseCompte())) {
                continue;
            }

            if ((genreListe != null) && !genreListe.contains(compteSolde.getIdGenre())) {
                continue;
            }

            // Crée le bean
            CGCompteSoldeBean bean = CGClasseCompteServices.createCompteSoldeBean(idLangueISO, provisoire, compteSolde);

            if (bean == null) {
                continue;
            }

            CGClassificationNode<CGCompteSoldeBean> node;
            Double soldeCompared = null;
            if (beansToCompare != null) {
                CGCompteSoldeBean beanCompared = beansToCompare.get(bean.getIdCompte());
                if (beanCompared != null) {
                    soldeCompared = beanCompared.computeSoldeCharge() != null ? beanCompared.computeSoldeCharge()
                            : beanCompared.computeSoldeProduit();
                }

                // Crée le node
                node = new CGClassificationNode<CGCompteSoldeBean>(bean, beanCompared);
            } else {
                node = new CGClassificationNode<CGCompteSoldeBean>(bean);
            }

            // Pas besoins d'ajouter les noeuds au dépot car il s'agit de feuille qui n'ont pas d'enfants et
            // l'idClasseCompte est celui du parent

            // Recherche son parent
            if (!JadeStringUtil.isBlankOrZero(compteSolde.getIdClasseCompte())
                    && nodesDepot.containsKey(compteSolde.getIdClasseCompte())) {
                // Renseigne le parent du noeud créé
                node.setParent(nodesDepot.get(compteSolde.getIdClasseCompte()));
                // Renseigne le noeud créé comme enfant du noeud trouvé
                nodesDepot.get(compteSolde.getIdClasseCompte()).addChild(compteSolde.getIdExterne(), node);

                CGClasseCompteServices.recurseComputeTotal(node.getParent(), bean.getIdGenre(), bean.getMvtDebit(),
                        bean.getMvtCredit(), bean.getBudget(), soldeCompared);
            } else {
                String error = "ERREUR de configuration des classifications de comptes !!! [idClasseCompte="
                        + compteSolde.getIdClasseCompte() + "]";
                throw new Exception(error);
            }
        }
    }

    /**
     * Charge le dépot de noeuds avec les comptes soldes et met à jour les totaux et les niveaux
     * 
     * @param compteSoldeManager
     *            : collection des comptes soldes
     * @param nodesDepot
     *            : dépot de noeuds
     * @param idLangueISO
     *            : langue pour la description
     * @param provisoire
     *            : indique quel montant il faut utiliser
     * @throws Exception
     *             : si on ne trouve pas le parent du compte
     */
    public static void loadCompteSoldeLevel(CGExtendedCompteSoldeManager compteSoldeManager,
            Map<String, CGClassificationNode<CGCompteSoldeBean>> nodesDepot, String idLangueISO, boolean provisoire,
            List<String> classeListe) throws Exception {
        CGClasseCompteServices.loadCompteSoldeLevel(compteSoldeManager, null, nodesDepot, idLangueISO, provisoire,
                classeListe);
    }

    /**
     * @param nodesDepot
     */
    public static void printDEBUGClassificationNode(Map<String, CGClassificationNode<CGCompteSoldeBean>> nodesDepot) {
        TreeMap<String, CGClassificationNode<CGCompteSoldeBean>> nodesDepotTrie = new TreeMap<String, CGClassificationNode<CGCompteSoldeBean>>();
        // Trie dans l'ordre croissant des numéros de classification
        for (CGClassificationNode<CGCompteSoldeBean> node : nodesDepot.values()) {
            nodesDepotTrie.put(node.getValue().getNoClasse(), node);
        }

        for (CGClassificationNode<CGCompteSoldeBean> node : nodesDepotTrie.values()) {
            if (node.isRoot()) {
                CGClasseCompteServices.printDEBUGNode(node);
            }
        }
    }

    /**
     * @param listBeanDocument
     */
    public static void printDEBUGListBean(List<CGCompteSoldeBean> listBeanDocument) {
        for (CGCompteSoldeBean bean : listBeanDocument) {
            System.out.print("(" + bean.getNiveau() + ") ");
            System.out.print(bean.getNoClasseLibelle() + " " + bean.getNoCompteLibelle() + " " + bean.getIdGenre());
            System.out.print(" D=" + bean.getMvtDebit() + " C=" + bean.getMvtCredit());
            System.out.println("");
        }
    }

    /**
     * @param node
     */
    private static void printDEBUGNode(CGClassificationNode<CGCompteSoldeBean> node) {
        CGCompteSoldeBean value = node.getValue();

        // System.out.println("");
        if (JadeStringUtil.isBlank(value.getNiveau())) {
            System.out.print("\t\t\t\t");
        } else {
            for (int i = 0; i < Integer.valueOf(value.getNiveau()); i++) {
                System.out.print("\t");
            }
        }

        System.out.print(value.toString());
        if (node.isLeaf()) {
            System.out.println(" |       = " + value.getMvtDebit() + " / " + value.getMvtCredit());
        } else {
            System.out.println(" | Total : " + value.getMvtDebitTotal() + " / " + value.getMvtCreditTotal());
        }

        for (CGClassificationNode<CGCompteSoldeBean> val : node.getChildren().values()) {
            CGClasseCompteServices.printDEBUGNode(val);
        }
    }

    /**
     * Met à jour le total de toute la branche avec le nouveau montant et renseigne le niveau (level)
     * 
     * @param node
     * @param debit
     * @param credit
     * @param budget
     */
    private static int recurseComputeTotal(CGClassificationNode<CGCompteSoldeBean> node, String idGenre, Double debit,
            Double credit, Double budget, Double soldeCompared) {

        if (node == null) {
            throw new IllegalArgumentException("Error : (CGClassificationNode<CGBalanceComptesBean> is null !");
        }

        CGCompteSoldeBean value = node.getValue();
        value.addToDebitTotal(debit);
        value.addToCreditTotal(credit);

        if (debit == null) {
            debit = 0.0;
        }
        if (credit == null) {
            credit = 0.0;
        }
        Double soldDoit = debit - credit;
        Double soldeAvoir = credit - debit;

        if (CGCompte.CS_GENRE_ACTIF.equals(idGenre)) {
            value.addToActifTotal(soldDoit);
        } else if (CGCompte.CS_GENRE_PASSIF.equals(idGenre)) {
            value.addToPassifTotal(soldeAvoir);
        } else if (CGCompte.CS_GENRE_CHARGE.equals(idGenre)) {
            value.addToChargeTotal(soldDoit);
        } else if (CGCompte.CS_GENRE_PRODUIT.equals(idGenre)) {
            value.addToProduitTotal(soldeAvoir);
        } else if (CGCompte.CS_GENRE_RESULTAT.equals(idGenre)) {
            if (soldDoit > 0) {
                value.addToChargeTotal(soldDoit);
            } else if (soldeAvoir > 0) {
                value.addToProduitTotal(soldeAvoir);
            }
        }

        value.addToBudgetTotal(budget);
        value.addToSoldeExerciceComparaisonTotal(soldeCompared);

        if (!node.isRoot()) {
            // Construit le niveau
            int level = CGClasseCompteServices.recurseComputeTotal(node.getParent(), idGenre, debit, credit, budget,
                    soldeCompared) + 1;
            value.setNiveau("" + level);
            return level;
        } else {
            return 0;
        }

    }

    /**
     * Convertit une valeur en Double
     * 
     * @param montant
     * @return zéro ou le montant recu en paramètre
     */
    public static Double stringToDouble(String montant) {
        if (JadeStringUtil.isBlankOrZero(montant)) {
            return 0.0;
        } else {
            return Double.valueOf(montant);
        }
    }
}
