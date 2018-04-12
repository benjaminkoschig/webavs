package globaz.orion.vb.adi;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.db.BManager;
import globaz.orion.vb.EBAbstractListViewBeanPagination;
import ch.globaz.orion.business.domaine.demandeacompte.DemandeModifAcompteStatut;
import ch.globaz.orion.db.EBDemandeModifAcompteJoinDecisionEntity;
import ch.globaz.orion.db.EBDemandeModifAcompteJoinDecisionManager;
import ch.globaz.orion.db.EBDemandeModifAcompteManagerOrderBy;

public class EBDemandesTransmisesListViewBean extends EBAbstractListViewBeanPagination {
    private EBDemandeModifAcompteJoinDecisionManager manager = new EBDemandeModifAcompteJoinDecisionManager();
    private EBDemandeModifAcompteManagerOrderBy orderBy;
    private DemandeModifAcompteStatut forStatut;
    private String forType;
    private String likeAffilie;
    private String likeNom;
    private String forAnnee;
    private String fromDateReception;

    public EBDemandesTransmisesListViewBean() {
        orderBy = EBDemandeModifAcompteManagerOrderBy.NOM;
        forStatut = DemandeModifAcompteStatut.UNDEFINDED;
        forType = null;
        likeAffilie = null;
        likeNom = null;
        forAnnee = null;
        fromDateReception = null;
    }

    @Override
    public void find() throws Exception {
        manager.setLikeNom(likeNom);
        manager.setLikeNumAffilie(likeAffilie);
        manager.setForAnnee(forAnnee);
        manager.setFromDateReception(fromDateReception);
        manager.setForType(forType);
        manager.setForStatut(forStatut);
        manager.defineOrderBy(defineOrderBy());
        manager.find(BManager.SIZE_USEDEFAULT);
    }

    private String defineOrderBy() {
        String strOrderBy;
        switch (orderBy) {
            case NOM:
                strOrderBy = "HTLDE1";
                break;
            case NUM_AFFILIE:
                strOrderBy = "NUMAFFILIE";
                break;
            case DATE_RECEPTION:
                strOrderBy = "DATERECEPTION";
                break;
            default:
                strOrderBy = "HTLDE1";
        }
        return strOrderBy;
    }

    @Override
    public BIPersistentObject get(int idx) {
        EBDemandeModifAcompteJoinDecisionEntity demandesTransmises = (EBDemandeModifAcompteJoinDecisionEntity) manager
                .get(idx);

        return manager.size() > idx ? new EBDemandesTransmisesViewBean(demandesTransmises)
                : new EBDemandesTransmisesViewBean();
    }

    @Override
    public int getSize() {
        return manager.size();
    }

    @Override
    public BManager getManager() {
        return manager;
    }

    public String getOrderBy() {
        return orderBy.getLabel();
    }

    public void setOrderBy(String orderBy) {
        if (!orderBy.isEmpty()) {
            this.orderBy = EBDemandeModifAcompteManagerOrderBy.fromLabel(orderBy);
        } else {
            this.orderBy = null;
        }

    }

    public String getForStatut() {
        return forStatut.getValue();
    }

    public void setForStatut(String forStatut) {
        if (!forStatut.isEmpty()) {
            this.forStatut = DemandeModifAcompteStatut.fromValue(forStatut);
        } else {
            this.forStatut = null;
        }
    }

    public String getForType() {
        return forType;
    }

    public void setForType(String forType) {
        if (!forType.isEmpty()) {
            this.forType = forType;
        } else {
            this.forType = null;
        }
    }

    public String getLikeAffilie() {
        return likeAffilie;
    }

    public void setLikeAffilie(String likeAffilie) {
        if (!likeAffilie.isEmpty()) {
            this.likeAffilie = likeAffilie;
        } else {
            this.likeAffilie = null;
        }
    }

    public String getForAnnee() {
        return forAnnee;
    }

    public void setForAnnee(String forAnnee) {
        if (!forAnnee.isEmpty()) {
            this.forAnnee = forAnnee;
        } else {
            this.forAnnee = null;
        }
    }

    public String getFromDateReception() {
        return fromDateReception;
    }

    public void setFromDateReception(String fromDateReception) {
        if (!fromDateReception.isEmpty()) {
            this.fromDateReception = fromDateReception;
        } else {
            this.fromDateReception = null;
        }
    }

    public String getLikeNom() {
        return likeNom;
    }

    public void setLikeNom(String likeNom) {
        if (!likeNom.isEmpty()) {
            this.likeNom = likeNom;
        } else {
            this.likeNom = null;
        }
    }

}
