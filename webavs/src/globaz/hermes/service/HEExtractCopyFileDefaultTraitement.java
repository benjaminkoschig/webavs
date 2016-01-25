package globaz.hermes.service;

import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.hermes.db.gestion.HELotViewBean;
import globaz.hermes.db.gestion.HEOutputAnnonceLotListViewBean;
import globaz.hermes.db.gestion.HEOutputAnnonceViewBean;
import globaz.hermes.utils.HELotUtils;
import globaz.jade.client.util.JadeStringUtil;
import java.io.BufferedWriter;

/**
 * Classe de traitement des fichiers de rente par défaut
 * 
 * @author David Van Hooste
 */
public class HEExtractCopyFileDefaultTraitement implements IHEExtractCopyFileTraitement {

    @Override
    public void genererFichier(BufferedWriter file, boolean hasCarriageReturns, BSession session,
            BTransaction transaction) throws Exception {
        int nbRecords = 0;
        // écrire l'entête du lot
        HELotUtils lutils = new HELotUtils();
        file.write(lutils.getLotHeader(session));
        //
        if (hasCarriageReturns) {
            file.write("\n");
        }
        HEOutputAnnonceLotListViewBean outputAnnonceListViewBean = new HEOutputAnnonceLotListViewBean();
        outputAnnonceListViewBean.setSession(session);
        outputAnnonceListViewBean.setForTypeLot(HELotViewBean.CS_TYPE_RECEPTION);
        outputAnnonceListViewBean.setForDateReceptionVide(true);
        outputAnnonceListViewBean.setOrder(" CAST(RNREFU AS INTEGER), RNIANN");
        BStatement statement = outputAnnonceListViewBean.cursorOpen(transaction);
        HEOutputAnnonceViewBean outputAnnonceViewBean = null;
        while ((outputAnnonceViewBean = (HEOutputAnnonceViewBean) outputAnnonceListViewBean.cursorReadNext(statement)) != null) {
            file.write(JadeStringUtil.leftJustify(outputAnnonceViewBean.getChampEnregistrement(), 120));
            outputAnnonceViewBean.setDateReception(JACalendar.todayJJsMMsAAAA());
            outputAnnonceViewBean.wantCallValidate(false);
            outputAnnonceViewBean.update(transaction);
            nbRecords++;
            if (hasCarriageReturns) {
                file.write("\n");
            }
        }
        // écrire le pied de page
        file.write(lutils.getLotFooter(session, nbRecords));
        if (hasCarriageReturns) {
            file.write("\n");
        }
    }

    @Override
    public void setForListMotif(String[] motifs) throws Exception {
        // dans la configuration standard, transmettre tous les motifs
        if (motifs != null) {
            throw new Exception("Erreur : filtre non-géré pour le traitement standard !");
        }
    }

}
