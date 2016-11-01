package ch.globaz.orion.db;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import java.util.List;
import ch.globaz.common.jadedb.JadeManager;
import ch.globaz.common.sql.SQLWriter;
import ch.globaz.orion.business.domaine.pucs.DeclarationSalaireProvenance;

public class EBPucsFileManager extends JadeManager<EBPucsFileEntity> {

    private static final long serialVersionUID = 1L;
    private String orderBy;
    private String forDateSoumission;
    private String likeAffilie;
    private String forStatut;
    private DeclarationSalaireProvenance forProvenance = DeclarationSalaireProvenance.UNDEFINDED;
    private String fullText;
    private List<String> inIds;

    @Override
    protected void createWhere(SQLWriter sqlWhere) {
        String provenance = null;
        if (!forProvenance.isUndefinded()) {
            provenance = forProvenance.getValue();
        }
        sqlWhere.and(EBPucsFileDefTable.DATE_RECEPTION).equal(forDateSoumission);
        sqlWhere.and(EBPucsFileDefTable.NUMERO_AFFILIE).like(likeAffilie);
        sqlWhere.and(EBPucsFileDefTable.STATUS).in(forStatut);
        sqlWhere.and(EBPucsFileDefTable.PROVENANCE).equal(provenance);
        sqlWhere.and(EBPucsFileDefTable.ID).in(inIds);
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new EBPucsFileEntity();
    }

    @Override
    protected String _getOrder(BStatement statement) {
        return orderBy;
    }

    public DeclarationSalaireProvenance getForProvenance() {
        return forProvenance;
    }

    public void setForProvenance(DeclarationSalaireProvenance forProvenance) {
        this.forProvenance = forProvenance;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getForDateSoumission() {
        return forDateSoumission;
    }

    public void setForDateSoumission(String forDateSoumission) {
        this.forDateSoumission = forDateSoumission;
    }

    public String getLikeAffilie() {
        return likeAffilie;
    }

    public void setLikeAffilie(String likeAffilie) {
        this.likeAffilie = likeAffilie;
    }

    public String getForStatut() {
        return forStatut;
    }

    public void setForStatut(String forStatut) {
        this.forStatut = forStatut;
    }

    public String getFullText() {
        return fullText;
    }

    public void setFullText(String fullText) {
        this.fullText = fullText;
    }

    public void setInIds(List<String> ids) {
        inIds = ids;
    }
}
