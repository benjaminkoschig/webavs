package globaz.musca.db.facturation;

import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.db.BTransaction;
import globaz.globall.db.FWPKProvider;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeListUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Gestion pour un passage sp�cifique d'un sous-ensemble d'affili�s Permet de limiter les cas lors de la facturation.
 * 
 * @author JSI
 * 
 */
public class FAPassageFacturationSousEnsembleAffiliesViewBean extends BJadePersistentObjectViewBean {

    private String idPassage;
    private String listAffilies;

    private String _join(List<String> list, String string) {
        StringBuilder res = new StringBuilder();
        for (Iterator<String> it = list.iterator(); it.hasNext();) {
            res.append(it.next());
            if (it.hasNext()) {
                res.append("','");
            }
        }
        return res.toString();
    }

    @Override
    public void add() throws Exception {
    }

    @Override
    public void delete() throws Exception {
    }

    @Override
    public String getId() {
        return idPassage;
    }

    public String getIdPassage() {
        return idPassage;
    }

    public String getListAffilies() {
        return listAffilies.trim();
    }

    @Override
    public BSpy getSpy() {
        return null;
    }

    @Override
    public void retrieve() throws Exception {
        if (getIdPassage() != null) {
            FAPassageFacturationSousEnsembleAffiliesManager manager = new FAPassageFacturationSousEnsembleAffiliesManager();
            manager.setSession((BSession) getISession());
            manager.setIdPassage(getIdPassage());
            manager.find(0);
            String affilies = "";
            for (int i = 0; i < manager.size(); i++) {
                affilies += ((FAPassageFacturationSousEnsembleAffiliesEntity) manager.getEntity(i)).getNumeroAffilie()
                        + "\n";
            }
            setListAffilies(affilies);
        } else {
            throw new Exception("IdPassage missing");
        }
    }

    @Override
    public void setId(String newId) {
        idPassage = newId;

    }

    public void setIdPassage(String idPassage) {
        this.idPassage = idPassage;
    }

    public void setListAffilies(String listAffilies) {
        this.listAffilies = listAffilies;
    }

    /**
     * Mets � jour les affili�s � prendre en compte en fonction des donn�es re�ues du JSP
     */
    @Override
    public void update() throws Exception {
        final BSession session = (BSession) getISession();
        if (getIdPassage() == null) {
            // Gestion l'exception si on re�oit pas d'id de passage
            // Pr�ventif, ne devrait pas arriver
            throw new Exception(session.getLabel("ERREUR_PASSAGE_INCONNU"));
        } else {
            BTransaction transaction = null;
            try {
                transaction = new BTransaction((BSession) getISession());
                transaction.openTransaction();
                FAFacturationFiltreAffilieManager manager = new FAFacturationFiltreAffilieManager();
                manager.setSession((BSession) getISession());
                manager.setIdPassage(getIdPassage());
                // D'abord on efface toutes les entr�es li�es au passage
                manager.delete(transaction);
                transaction.commit();
                // Si on a des entr�es dans le textarea on va les entrer dans la base de donn�es
                if (!getListAffilies().equals("")) {
                    String[] affilies = getListAffilies().split("\n");

                    /*
                     * Supprime les doublons �ventuels pour �tre d'avoir les num�ros une seule fois.
                     */
                    Set<String> affiliationSet = new HashSet<String>();
                    for (String numAff : affilies) {
                        affiliationSet.add(numAff.trim());
                    }

                    /*
                     * Traite l'ajout des cas par lot, pour minimizer les indtructions SQL au cas ou on ajouterait 5000
                     * num�ros d'affili�s...
                     */

                    int taille = affiliationSet.size();
                    int tailleLot = 500;
                    final BTransaction finalTransaction = transaction;
                    JadeListUtil.Lot<String> lot = new JadeListUtil.Lot<String>(taille, tailleLot,
                            new JadeListUtil.LotExec<String>() {

                                @Override
                                public void exec(List<String> lot, long numLot, long nbLot) {
                                    try {
                                        /*
                                         * V�rification des num�ros d'affili�s pour ce lot
                                         */
                                        AFAffiliationManager affilieManager = new AFAffiliationManager();
                                        affilieManager.setSession(session);
                                        affilieManager.setForAffilieNumeroIn("'"
                                                + FAPassageFacturationSousEnsembleAffiliesViewBean.this._join(lot,
                                                        "','") + "'");
                                        affilieManager.find(0);
                                        /*
                                         * TODO : signaler les affiliations qui sont dans "lot" mais pas dans
                                         * affilieManager
                                         */

                                        /*
                                         * Ajout des affiliations existant bien dans la base
                                         */

                                        // Reservation d'une plage d'incr�ment pour gagner en performence
                                        FAFacturationFiltreAffilieEntity tmp = new FAFacturationFiltreAffilieEntity();
                                        tmp.setSession(session);
                                        FWPKProvider p = tmp.getNewPrimaryKeyProvider(lot.size());
                                        for (Iterator<?> it = affilieManager.iterator(); it.hasNext();) {
                                            AFAffiliation affiliation = (AFAffiliation) it.next();

                                            // Ajouter les affiliations pour ce lot dans la table de filtrage
                                            FAFacturationFiltreAffilieEntity fafia = new FAFacturationFiltreAffilieEntity();
                                            fafia.setIdFiltreAffilie(p.getNextPrimaryKey());
                                            fafia.setSession(session);
                                            fafia.setIdAffiliation(affiliation.getAffiliationId());
                                            fafia.setIdPassage(FAPassageFacturationSousEnsembleAffiliesViewBean.this
                                                    .getIdPassage());
                                            fafia.add(finalTransaction);
                                        }

                                        System.out.println("---------------------------------------");

                                        /*
                                         * Commit par lot
                                         */
                                        finalTransaction.commit();

                                    } catch (Exception e) {
                                        session.addError(e.getMessage());
                                    }
                                }
                            });
                    for (String aff : affiliationSet) {
                        lot.add(aff);
                    }

                }

            } catch (Exception e) {
                transaction.rollback();
                // Some code here
            } finally {
                if (transaction != null) {
                    transaction.closeTransaction();
                }
                // Some code here
            }
            // Some code here
        }

    }
}
