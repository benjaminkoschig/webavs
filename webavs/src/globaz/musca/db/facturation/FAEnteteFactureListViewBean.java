package globaz.musca.db.facturation;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

public class FAEnteteFactureListViewBean extends FAEnteteFactureManager implements
        globaz.framework.bean.FWListViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String action = null;

    /**
     * Renvoie la liste des champs
     * 
     * @return la liste des champs
     */
    @Override
    protected String _getFields(BStatement statement) {
        return "FAENTFP.IDPASSAGE, FAENTFP.NONIMPRIMABLE, FAENTFP.IDENTETEFACTURE, FAENTFP.TOTALFACTURE, FAENTFP.IDEXTERNEFACTURE, FAENTFP.IDTYPEFACTURE, "
                + "FAENTFP.IDEXTERNEROLE,  FAENTFP.IDROLE, FAENTFP.IDTIERS, TITIERP.HTLDE1, TITIERP.HTLDE2, FAENTFP.MODIMP";
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.musca.db.facturation.FAEnteteFactureManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new FAEnteteFactureViewBean();
    }

    public java.lang.String getAction() {
        return action;
    }

    public String getDebiteur(int pos) throws Exception {
        FAEnteteFacture entity = (FAEnteteFacture) getEntity(pos);
        return entity.getDescriptionTiers();
    }

    public String getDecompte(int pos) {
        FAEnteteFacture entity = (FAEnteteFacture) getEntity(pos);
        return entity.getDescriptionDecompte();
    }

    public String getIdEnteteFacture(int pos) {
        return ((FAEnteteFacture) getEntity(pos)).getIdEntete();
    }

    public String getIdTiers(int pos) {
        return ((FAEnteteFacture) getEntity(pos)).getIdTiers();
    }

    public Boolean getIsNonImprimable(int pos) {
        Boolean val = ((FAEnteteFacture) getEntity(pos)).isNonImprimable();
        return val;
    }

    public String getTotalFacture(int pos) {
        return ((FAEnteteFacture) getEntity(pos)).getTotalFacture();
    }

    public void setAction(java.lang.String newAction) {
        action = newAction;
    }

}
