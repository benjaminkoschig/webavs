package globaz.corvus.vb.avances;

import globaz.corvus.db.avances.REAvanceManager;
import globaz.corvus.utils.RETiersForJspUtils;
import globaz.globall.db.BEntity;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JAVector;
import globaz.hera.utils.SFFamilleUtils;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import java.util.Iterator;

/**
 * @author SCR
 */
public class REAvanceListViewBean extends REAvanceManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idTiersRequerant;

    public REAvanceListViewBean() {
        super();

        idTiersRequerant = null;

        wantCallMethodBeforeFind(true);
        wantCallMethodAfterFind(true);
    }

    @Override
    protected void _afterFind(BTransaction transaction) throws Exception {
        JAVector resultSet = getContainer();

        for (int i = 0; i < resultSet.size(); i++) {
            REAvanceViewBean uneAvance = (REAvanceViewBean) resultSet.get(i);

            PRTiersWrapper tiers = PRTiersHelper.getTiersParId(getSession(), uneAvance.getIdTiersBeneficiaire());

            uneAvance
                    .setInfoDuTiersFormatted(RETiersForJspUtils.getInstance(getSession()).getDetailsTiers(tiers, true));
        }
    }

    @Override
    protected void _beforeFind(BTransaction transaction) throws Exception {

        StringBuilder idFamille = new StringBuilder();

        for (Iterator<PRTiersWrapper> iterator = SFFamilleUtils.getTiersFamilleProche(getSession(), idTiersRequerant)
                .iterator(); iterator.hasNext();) {
            idFamille.append(iterator.next().getIdTiers());
            if (iterator.hasNext()) {
                idFamille.append(",");
            }
        }

        // si id famille vide
        if (idFamille.length() == 0) {
            idFamille.append(idTiersRequerant);
        }
        setForIdTiersIn(idFamille.toString());
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new REAvanceViewBean();
    }

    public String getIdTiersRequerant() {
        return idTiersRequerant;
    }

    public void setIdTiersRequerant(String idTiersRequerant) {
        this.idTiersRequerant = idTiersRequerant;
    }
}
