package globaz.hermes.service;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.api.IHEOutputAnnonce;
import globaz.hermes.db.gestion.HELotViewBean;
import globaz.hermes.db.gestion.HEOutputAnnonceLotListViewBean;
import globaz.hermes.db.gestion.HEOutputAnnonceViewBean;
import globaz.hermes.utils.HEUtil;
import java.util.ArrayList;
import java.util.List;

public class HEAnnoncesCentrale extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected String _getTableName() {
        return null;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
    }

    public int countAnnoncesAdaptationRentes(BTransaction transaction) throws Exception {
        HEOutputAnnonceLotListViewBean outputAnnonceListViewBean = new HEOutputAnnonceLotListViewBean();
        outputAnnonceListViewBean.setSession(getSession());
        outputAnnonceListViewBean.setForTypeLot(HELotViewBean.CS_TYPE_ADAPTATION_RENTES_PC);
        outputAnnonceListViewBean.setForDateReceptionVide(true);
        outputAnnonceListViewBean.setForCodeApplication("61");
        outputAnnonceListViewBean.setForStatut(IHEAnnoncesViewBean.CS_TERMINE);
        outputAnnonceListViewBean.wantCallMethodAfter(false);
        outputAnnonceListViewBean.setOrder("RNIANN");
        return outputAnnonceListViewBean.getCount(transaction);
    }

    /**
     * @param transaction
     * @param limiteNbDemandes
     * @param listArc
     * @param listeReferences
     * @param outputAnnonceListViewBean
     * @return
     * @throws Exception
     */
    private IHEOutputAnnonce[] extraireAnnonces(BTransaction transaction, Integer limiteNbDemandes,
            HEOutputAnnonceLotListViewBean outputAnnonceListViewBean) throws Exception {
        ArrayList listeReferences = new ArrayList();
        List listArc = new ArrayList();
        BStatement statement = outputAnnonceListViewBean.cursorOpen(transaction);
        HEOutputAnnonceViewBean outputAnnonceViewBean = null;
        int index = 0;
        String lastRefUnique = "";
        while ((outputAnnonceViewBean = (HEOutputAnnonceViewBean) outputAnnonceListViewBean.cursorReadNext(statement)) != null) {
            if ((limiteNbDemandes.intValue() > 0) && (index >= limiteNbDemandes.intValue())) {
                break;
            } else {
                listArc.add(outputAnnonceViewBean);
                listeReferences.add(outputAnnonceViewBean.getRefUnique());
                if (!lastRefUnique.equals(outputAnnonceViewBean.getRefUnique())) {
                    index++;
                }
            }
            lastRefUnique = outputAnnonceViewBean.getRefUnique();
        }
        outputAnnonceListViewBean.cursorClose(statement);
        // valide les données extraites pour le prochain appel (set date dans
        // RNDECP)
        HEUtil.commitTreatReference(listeReferences, getSession(), transaction);
        // retourne le tableau des données
        return (IHEOutputAnnonce[]) listArc.toArray(new IHEOutputAnnonce[listArc.size()]);
    }

    public IHEOutputAnnonce[] getAnnoncesAdaptationRentes(BTransaction transaction, String idLot, Integer limiteDemandes)
            throws Exception {
        HEOutputAnnonceLotListViewBean outputAnnonceListViewBean = new HEOutputAnnonceLotListViewBean();
        outputAnnonceListViewBean.setSession(getSession());
        outputAnnonceListViewBean.setForIdLot(idLot);
        outputAnnonceListViewBean.setForTypeLot(HELotViewBean.CS_TYPE_ADAPTATION_RENTES);
        outputAnnonceListViewBean.setForDateReceptionVide(true);
        outputAnnonceListViewBean.setForCodesApps("'51' , '53'");
        outputAnnonceListViewBean.setForStatut(IHEAnnoncesViewBean.CS_TERMINE);
        outputAnnonceListViewBean.wantCallMethodAfter(false);
        outputAnnonceListViewBean.setOrder(" CAST(RNREFU AS INTEGER), RNIANN");

        return extraireAnnonces(transaction, new Integer(0), outputAnnonceListViewBean);
    }

    public IHEOutputAnnonce[] getAnnoncesAdaptationRentesPC(BTransaction transaction) throws Exception {
        HEOutputAnnonceLotListViewBean outputAnnonceListViewBean = new HEOutputAnnonceLotListViewBean();
        outputAnnonceListViewBean.setSession(getSession());
        outputAnnonceListViewBean.setForTypeLot(HELotViewBean.CS_TYPE_ADAPTATION_RENTES_PC);
        outputAnnonceListViewBean.setForDateReceptionVide(true);
        outputAnnonceListViewBean.setForCodeApplication("61");
        outputAnnonceListViewBean.setForStatut(IHEAnnoncesViewBean.CS_TERMINE);
        outputAnnonceListViewBean.wantCallMethodAfter(false);
        outputAnnonceListViewBean.setOrder("RNIANN");
        HELoadFields load = new HELoadFields();
        return load.extraireAnnoncesAndLoadfields(transaction, new Integer(0), outputAnnonceListViewBean);
        // return this.extraireAnnonces(transaction, new Integer(0), outputAnnonceListViewBean);
    }

    public IHEOutputAnnonce[] getAnnoncesAdaptationRentesPCForCentrale(BTransaction transaction) throws Exception {
        HEOutputAnnonceLotListViewBean outputAnnonceListViewBean = new HEOutputAnnonceLotListViewBean();
        outputAnnonceListViewBean.setSession(getSession());
        outputAnnonceListViewBean.setForTypeLot(HELotViewBean.CS_TYPE_ADAPTATION_RENTES_PC);
        outputAnnonceListViewBean.setForDateReceptionVide(true);
        outputAnnonceListViewBean.setForCodeApplication("61");
        outputAnnonceListViewBean.setForStatut(IHEAnnoncesViewBean.CS_TERMINE);
        outputAnnonceListViewBean.wantCallMethodAfter(false);
        outputAnnonceListViewBean.setOrder("RNIANN");
        outputAnnonceListViewBean.setAddConditionToReceiveCentrale(true);
        HELoadFields load = new HELoadFields();
        return load.extraireAnnoncesAndLoadfields(transaction, new Integer(0), outputAnnonceListViewBean);
        // return this.extraireAnnonces(transaction, new Integer(0), outputAnnonceListViewBean);
    }

    public IHEOutputAnnonce[] getAnnoncesRentes(BTransaction transaction) throws Exception {
        HEOutputAnnonceLotListViewBean outputAnnonceListViewBean = new HEOutputAnnonceLotListViewBean();
        outputAnnonceListViewBean.setSession(getSession());
        outputAnnonceListViewBean.setForTypeLot(HELotViewBean.CS_TYPE_RENTES);
        outputAnnonceListViewBean.setForDateReceptionVide(true);
        outputAnnonceListViewBean.setForCodeApplication("50");
        outputAnnonceListViewBean.setForStatut(IHEAnnoncesViewBean.CS_TERMINE);
        outputAnnonceListViewBean.wantCallMethodAfter(false);
        outputAnnonceListViewBean.setOrder(" CAST(RNREFU AS INTEGER), RNIANN");

        return extraireAnnonces(transaction, new Integer(0), outputAnnonceListViewBean);
    }

    public IHEOutputAnnonce[] getAnnoncesTerminees(BTransaction transaction, Integer limiteNbDemandes) throws Exception {
        HEOutputAnnonceLotListViewBean outputAnnonceListViewBean = new HEOutputAnnonceLotListViewBean();
        outputAnnonceListViewBean.setSession(getSession());
        // outputAnnonceListViewBean.setForTypeLot(HELotViewBean.CS_TYPE_RECEPTION);
        outputAnnonceListViewBean.setForDateReceptionVide(true);
        outputAnnonceListViewBean.setForCodesApps("'11' , '38' , '39' , '29', '25'");
        // BZ 8606 ajout des motifs 73 et 83
        outputAnnonceListViewBean.setForMotifs(new String[] { "02", "71", "73", "81", "75", "85", "83", "79", "92",
                "93", "94", "97", "98", "99" });
        outputAnnonceListViewBean.setForStatut(IHEAnnoncesViewBean.CS_TERMINE);
        outputAnnonceListViewBean.wantCallMethodAfter(false);
        outputAnnonceListViewBean.setOrder(" CAST(RNREFU AS INTEGER), RNIANN");
        outputAnnonceListViewBean.setForDateReceptionVide(true);
        // set RNTPRO IN HERMES ou CORVUS
        outputAnnonceListViewBean.setProducteurRentes(true);

        return extraireAnnonces(transaction, limiteNbDemandes, outputAnnonceListViewBean);
    }
}
