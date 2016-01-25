package globaz.corvus.vb.demandes;

import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.corvus.application.REApplication;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.demandes.REDemandeRenteJointDemande;
import globaz.corvus.db.demandes.REDemandeRenteJointDemandeManager;
import globaz.corvus.db.demandes.REDemandeRenteJointPrestationAccordee;
import globaz.corvus.db.demandes.REDemandeRenteJointPrestationAccordeeManager;
import globaz.corvus.utils.REPmtMensuel;
import globaz.corvus.vb.demandes.REDemandeRenteJointPrestationAccordeeViewBeanComparator.TypeComparaison;
import globaz.framework.db.postit.FWNoteP;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JAVector;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.tools.PRCodeSystem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

/**
 * ViewBean dédié au chargement de la liste des {@link REDemandeRenteJointPrestationAccordeeViewBean}<br/>
 * Il a été nécessaire d'étendre le manager pour l'impression de la liste<br/>
 * <br/>
 * Son rôle principale est de traité les données reçu. En effet, chaque {@link REDemandeRenteJointPrestationAccordee}
 * chargés représente une prestation accordée pour une demande. <br/>
 * Ce viewBean se charge de regrouper ces objet par demande. <br/>
 * Il trie ensuite la liste des résultats pour correspondre au tri que la requête SQL de base effectuait.
 * 
 * @author PBA
 * 
 */
public class REDemandeRenteJointPrestationAccordeeListViewBean extends REDemandeRenteJointPrestationAccordeeManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final String ALIAS_COUNT_POSTIT = "CNTPOST";

    private List attachedDocuments;
    private boolean hasPostitField = false;
    private transient Vector orderBy;

    private List<REDemandeRenteJointPrestationAccordeeViewBean> viewBeans = null;

    /**
     * Initialise la liste de {@link REDemandeRenteJointPrestationAccordeeViewBean}
     */
    public REDemandeRenteJointPrestationAccordeeListViewBean() {
        wantCallMethodAfterFind(true);

        // pour initialiser le Boolean (sinon mauvais fonctionnement du isEnCours)
        setEnCours(false);

        viewBeans = new ArrayList<REDemandeRenteJointPrestationAccordeeViewBean>();
    }

    /**
     * Méthode regroupant les {@link REDemandeRenteJointPrestationAccordeeViewBean} par demande, <br/>
     * puis les triant à l'aide d'un {@link REDemandeRenteJointPrestationAccordeeViewBeanComparator}
     */
    @Override
    protected void _afterFind(BTransaction transaction) throws Exception {
        // vidange de la liste qui sera utilisé dans la JSP
        viewBeans.clear();

        Map<Long, List<REDemandeRenteJointPrestationAccordeeViewBean>> listeDeTri = new HashMap<Long, List<REDemandeRenteJointPrestationAccordeeViewBean>>();

        // encapsulation des entités dans un viewbean, et tri en fonction de l'ID de la demande
        for (Iterator it = iterator(); it.hasNext();) {
            REDemandeRenteJointPrestationAccordeeViewBean entity = (REDemandeRenteJointPrestationAccordeeViewBean) it
                    .next();
            if ((entity == null) || JadeStringUtil.isBlank(entity.getIdDemandeRente())) {
                continue;
            }

            if (listeDeTri.containsKey(Long.parseLong(entity.getIdDemandeRente()))) {
                listeDeTri.get(Long.parseLong(entity.getIdDemandeRente())).add(entity);
            } else {
                List<REDemandeRenteJointPrestationAccordeeViewBean> newList = new ArrayList<REDemandeRenteJointPrestationAccordeeViewBean>();
                newList.add(entity);

                listeDeTri.put(Long.parseLong(entity.getIdDemandeRente()), newList);
            }
        }

        Set<Long> keys = listeDeTri.keySet();

        // regroupement des viewbean qui ont un id Similaire
        for (Long key : keys) {
            List<REDemandeRenteJointPrestationAccordeeViewBean> entities = listeDeTri.get(key);

            REDemandeRenteJointPrestationAccordeeViewBean finalViewBean = null;

            for (REDemandeRenteJointPrestationAccordeeViewBean entity : entities) {
                if (finalViewBean == null) {
                    finalViewBean = entity;
                    continue;
                }

                for (int i = 0; i < entity.getCodesPrestation().size(); i++) {
                    finalViewBean.addCodePrestation(entity.getCodesPrestation().get(i), entity.getDatesFinDroit()
                            .get(i));
                }
            }

            viewBeans.add(finalViewBean);
        }

        sortViewBeansFromOrderBy();
        replaceResultSet();
    }

    @Override
    protected String _getFields(BStatement statement) {
        StringBuilder fields = new StringBuilder();

        fields.append(super._getFields(statement));

        if (hasPostitField) {
            fields.append(", (");
            fields.append(createSelectCountPostit(_getCollection()));
            fields.append(") AS ");
            fields.append(REDemandeRenteJointPrestationAccordeeListViewBean.ALIAS_COUNT_POSTIT);
        }

        return fields.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new REDemandeRenteJointPrestationAccordeeViewBean();
    }

    private String createSelectCountPostit(String schema) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) FROM ");
        sql.append(schema);
        sql.append(FWNoteP.TABLE_NAME);
        sql.append(" WHERE ");
        sql.append(FWNoteP.FIELDNAME_NOTE_SOURCE_ID);
        sql.append(" = ");
        sql.append(schema);
        sql.append(REDemandeRenteJointDemande.TABLE_TIERS);
        sql.append(".");
        sql.append(REDemandeRenteJointDemande.FIELDNAME_ID_TIERS_TI);
        sql.append(" AND ");
        sql.append(FWNoteP.FIELDNAME_NOTE_TBL);
        sql.append(" = '");
        sql.append(REApplication.KEY_POSTIT_RENTES);
        sql.append("'");

        return sql.toString();
    }

    public List getAttachedDocuments() {
        return attachedDocuments;
    }

    public String getDateDernierPaiement() {
        return REPmtMensuel.getDateDernierPmt(getSession());
    }

    public Vector getEtatsDemande() {

        Vector etatsDemande = PRCodeSystem.getLibellesPourGroupe(IREDemandeRente.CS_GROUPE_ETAT_DEMANDE_RENTE,
                getSession());

        // ajout des options custom
        etatsDemande.add(
                1,
                new String[] { REDemandeRenteJointDemande.LABEL_NON_VALIDE,
                        getSession().getLabel("JSP_DRE_R_DEMANDES_NON_VALIDE") });
        etatsDemande.add(0, new String[] { "", "" });

        return etatsDemande;
    }

    public Vector getOrderByData() {
        if (orderBy == null) {
            orderBy = new Vector(4);
            orderBy.add(new String[] { getOrderByDefaut(), getSession().getLabel("JSP_DRE_R_TRIER_PAR_NOM") });
            orderBy.add(new String[] {
                    REDemandeRente.FIELDNAME_DATE_RECEPTION + " DESC" + ","
                            + REDemandeRenteJointDemandeManager.ALIAS_DATE_FIN + " DESC, "
                            + REDemandeRente.FIELDNAME_DATE_DEBUT + " DESC",
                    getSession().getLabel("JSP_DRE_R_TRIER_PAR_DATE_RECEPTION") });
            orderBy.add(new String[] {
                    REDemandeRente.FIELDNAME_CS_ETAT + " DESC" + "," + REDemandeRenteJointDemandeManager.ALIAS_DATE_FIN
                            + " DESC, " + REDemandeRente.FIELDNAME_DATE_DEBUT + " DESC",
                    getSession().getLabel("JSP_DRE_R_TRIER_PAR_ETAT") });
        }

        return orderBy;
    }

    public boolean hasPostitField() {
        return hasPostitField;
    }

    private void replaceResultSet() {
        JAVector newContainer = new JAVector();

        for (REDemandeRenteJointPrestationAccordeeViewBean viewBean : viewBeans) {
            newContainer.add(viewBean);
        }

        setContainer(newContainer);
    }

    public void setAttachedDocuments(List attachedDocuments) {
        this.attachedDocuments = attachedDocuments;
    }

    public void setHasPostitField(boolean hasPostitField) {
        this.hasPostitField = hasPostitField;
    }

    private void sortViewBeansFromOrderBy() {
        TypeComparaison typeComparaison = TypeComparaison.Alphabetique;
        String orderBy = getOrderBy();

        String[] splittedOrderBy = orderBy.split(",");

        if (splittedOrderBy[0].equalsIgnoreCase("YATETA DESC")) {
            typeComparaison = TypeComparaison.Etat;
        } else if (splittedOrderBy[0].equalsIgnoreCase("YADREC DESC")) {
            typeComparaison = TypeComparaison.DateReception;
        }

        Collections.sort(viewBeans, new REDemandeRenteJointPrestationAccordeeViewBeanComparator(typeComparaison));
    }
}
