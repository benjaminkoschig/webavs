/*
 * Créé le 26 juin. 07
 */
package globaz.corvus.vb.ci;

import globaz.corvus.db.annonces.REAnnonceHeader;
import globaz.corvus.db.annonces.REAnnonceInscriptionCI;
import globaz.corvus.db.ci.RECompteIndividuel;
import globaz.corvus.db.ci.REInscriptionCI;
import globaz.corvus.db.ci.REInscriptionCIManager;
import globaz.corvus.db.ci.RERassemblementCI;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author bsc
 */

public class REInscriptionCIListViewBean extends REInscriptionCIManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String labelProvisoire;
    private String labelTraite;
    private FWCurrency total = new FWCurrency("0.00");
    private Map totalParCaisse = new TreeMap();

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {

        StringBuffer fromClauseBuffer = new StringBuffer();
        String innerJoin = " INNER JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";

        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(RERassemblementCI.TABLE_NAME_RCI);

        // jointure entre table des ci et table des rassemblements de ci
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(RECompteIndividuel.TABLE_NAME_CI);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(RERassemblementCI.TABLE_NAME_RCI);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RERassemblementCI.FIELDNAME_ID_CI);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(RECompteIndividuel.TABLE_NAME_CI);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RECompteIndividuel.FIELDNAME_ID_CI);

        // jointure entre table des rassemblements de ci et des inscriptions ci
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(REInscriptionCI.TABLE_NAME_INS_CI);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(RERassemblementCI.TABLE_NAME_RCI);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RERassemblementCI.FIELDNAME_ID_RCI);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(REInscriptionCI.TABLE_NAME_INS_CI);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REInscriptionCI.FIELDNAME_ID_RCI);

        // jointure entre table des inscriptions ci et des headers d'annonce
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(REAnnonceHeader.TABLE_NAME_ANNONCE_HEADER);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(REInscriptionCI.TABLE_NAME_INS_CI);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REInscriptionCI.FIELDNAME_ID_ARC);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(REAnnonceHeader.TABLE_NAME_ANNONCE_HEADER);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REAnnonceHeader.FIELDNAME_ID_ANNONCE);

        // jointure entre table des headers d'annonce et des annonces CI
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(REAnnonceInscriptionCI.TABLE_NAME_ANNONCE_INSCRIPTION_CI);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(REAnnonceHeader.TABLE_NAME_ANNONCE_HEADER);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REAnnonceHeader.FIELDNAME_ID_ANNONCE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(REAnnonceInscriptionCI.TABLE_NAME_ANNONCE_INSCRIPTION_CI);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REAnnonceInscriptionCI.FIELDNAME_ID_ANNONCE_INSCRIPTION_CI);

        return fromClauseBuffer.toString();
    }

    /**
     * @return DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new REInscriptionCIViewBean();
    }

    /**
     * Ajoute le montant donne a la caisse
     * 
     * @param montant
     * @param caisse
     */
    public void addMontantToCaisse(FWCurrency montant, String caisse) {

        if (totalParCaisse.containsKey(caisse)) {
            ((FWCurrency) totalParCaisse.get(caisse)).add(montant);
        } else {
            totalParCaisse.put(caisse, montant);
        }

        total.add(montant);
    }

    /**
     * Donne un iterateur sur les caisse comprisent dans le rassemblement
     * 
     * @return
     */
    public Iterator getCaisseKeyIterator() {
        return totalParCaisse.keySet().iterator();
    }

    public final String getLabelProvisoire() {
        return labelProvisoire;
    }

    public final String getLabelTraite() {
        return labelTraite;
    }

    /**
     * Donne le montant total pour cette caisse
     * 
     * @param caisse
     * @return
     */
    public FWCurrency getMontantTotalCaisse(String caisse) {
        return (FWCurrency) totalParCaisse.get(caisse);
    }

    @Override
    public String getOrderByDefaut() {
        return REAnnonceInscriptionCI.FIELDNAME_ANNEE_COTISATION + " DESC, "
                + REAnnonceInscriptionCI.FIELDNAME_MOIS_DEBUT_COTISATION + ", "
                + REAnnonceInscriptionCI.FIELDNAME_MOIS_FIN_COTISATION;
    }

    /**
     * donne le montant total des CI
     * 
     * @return
     */
    public FWCurrency getTotal() {
        return total;
    }

    public final void setLabelProvisoire(String labelProvisoire) {
        this.labelProvisoire = labelProvisoire;
    }

    public final void setLabelTraite(String labelTraite) {
        this.labelTraite = labelTraite;
    }

}
