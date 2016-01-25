package globaz.ccvd.hermes;

import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.db.gestion.HELotViewBean;
import globaz.hermes.db.gestion.HEOutputAnnonceListViewBean;
import globaz.hermes.db.gestion.HEOutputAnnonceLotListViewBean;
import globaz.hermes.db.gestion.HEOutputAnnonceViewBean;
import globaz.hermes.service.IHEExtractCopyFileTraitement;
import globaz.jade.client.util.JadeStringUtil;
import java.io.BufferedWriter;

// import java.util.GregorianCalendar;

public class HEExtractCopyFileCCVDTraitement implements IHEExtractCopyFileTraitement {
    public String[] forMotifs = null;

    private String creerReferenceCCVD(HEOutputAnnonceViewBean annonce, BTransaction transaction) throws Exception {
        StringBuffer reference = new StringBuffer(getNumeroAVSRassemblement(annonce, transaction));
        reference.append("   ");
        reference.append('Z');
        reference.append(getCodeProvenance(annonce.getSession()));
        reference.append("    ");
        return reference.toString();
    }

    @Override
    public void genererFichier(BufferedWriter file, boolean hasCarriageReturns, BSession session,
            BTransaction transaction) throws Exception {
        // générer l'entête
        // file.write(getLotHeader());
        // if(hasCarriageReturns)
        // file.write("\n");
        HEOutputAnnonceLotListViewBean outputAnnonceListViewBean = new HEOutputAnnonceLotListViewBean();
        outputAnnonceListViewBean.setSession(session);
        outputAnnonceListViewBean.setForTypeLot(HELotViewBean.CS_TYPE_RECEPTION);
        outputAnnonceListViewBean.setForDateReceptionVide(true);
        outputAnnonceListViewBean.setForCodeApplication("39");
        outputAnnonceListViewBean.setForCodeEnr01Or001(true);
        outputAnnonceListViewBean.setForMotifs(forMotifs);
        outputAnnonceListViewBean.setForStatut(IHEAnnoncesViewBean.CS_TERMINE);
        outputAnnonceListViewBean.wantCallMethodAfter(false);
        BStatement statement = outputAnnonceListViewBean.cursorOpen(transaction);
        HEOutputAnnonceViewBean outputAnnonceViewBean = null;

        while ((outputAnnonceViewBean = (HEOutputAnnonceViewBean) outputAnnonceListViewBean.cursorReadNext(statement)) != null) {
            traiterAnnonce(file, outputAnnonceViewBean, transaction, hasCarriageReturns);
        }
        outputAnnonceListViewBean.cursorClose(statement);
    }

    private String getCodeProvenance(BSession session) throws Exception {
        String numCaisse = session.getApplication().getProperty("numeroCaisse");
        if (JadeStringUtil.isEmpty(numCaisse)) {
            throw new Exception(
                    "Erreur critique, impossible de déterminer le code origine, numeroCaisse n'est pas renseigné");
        }
        String numAgence = session.getApplication().getProperty("numeroAgence");
        if (JadeStringUtil.isEmpty(numAgence)) {
            throw new Exception(
                    "Erreur critique, impossible de déterminer le code origine, numeroAgence n'est pas renseigné");
        }
        if ("116".equals(numCaisse)) {
            // AGRIVIT, code 5
            return "5";
        } else if ("022".equals(numCaisse)) {
            if ("132".equals(numAgence)) {
                // AGLSNE, code 4
                return "4";
            } else if ("000".equals(numAgence)) {
                // CCVD, code 1
                return "1";
            } else {
                throw new Exception(
                        "Erreur critique, impossible de déterminer le code origine, numeroAgence doit corresponde à 000 ou 132");
            }
        } else {
            throw new Exception(
                    "Erreur critique, impossible de déterminer le code origine, numeroCaisse doit corresponde à 116 ou 022");
        }
    }

    private String getNumeroAVSRassemblement(HEOutputAnnonceViewBean annonce, BTransaction transaction)
            throws Exception {
        HEOutputAnnonceListViewBean serie = new HEOutputAnnonceListViewBean();
        serie.setSession(annonce.getSession());
        serie.setForRefUnique(annonce.getRefUnique());
        serie.setForCodeApplication("11");
        serie.setForCodeEnr01Or001(true);
        serie.wantCallMethodAfter(false);
        serie.find(transaction);
        if (serie.size() > 0) {
            HEOutputAnnonceViewBean crt = (HEOutputAnnonceViewBean) serie.getFirstEntity();
            return crt.getNumeroAVS();
        } else {
            return annonce.getNumeroAVS();
        }
    }

    @Override
    public void setForListMotif(String[] motifs) {
        forMotifs = motifs;
    }

    private void traiterAnnonce(BufferedWriter file, HEOutputAnnonceViewBean annonce, BTransaction transaction,
            boolean hasCarriageReturns) throws Exception {
        String referenceCCVD = creerReferenceCCVD(annonce, transaction);
        HEOutputAnnonceListViewBean serie = new HEOutputAnnonceListViewBean();
        serie.setSession(annonce.getSession());
        serie.setForRefUnique(annonce.getRefUnique());
        serie.setForIdLot(annonce.getIdLot());
        serie.setForCodesApps("'38', '39'");
        serie.setForNumCaisse(annonce.getNumeroCaisse());
        serie.setForMotif(annonce.getMotif());
        serie.setForNumeroAVS(annonce.getNumeroAVS());
        serie.setForStatut(IHEAnnoncesViewBean.CS_TERMINE);
        serie.setOrder(" CAST(RNREFU AS INTEGER), RNIANN");
        BTransaction readTransaction = (BTransaction) annonce.getSession().newTransaction();
        try {
            readTransaction.openTransaction();
            BStatement statement = serie.cursorOpen(readTransaction);
            HEOutputAnnonceViewBean crt = null;
            while ((crt = (HEOutputAnnonceViewBean) serie.cursorReadNext(statement)) != null) {
                file.write(JadeStringUtil.leftJustify(traiterReference(crt, referenceCCVD), 120));
                // A chaque reprise, on transmet l'ensemble des rassemblements
                // crt.setDateReception(JACalendar.todayJJsMMsAAAA());
                // crt.wantCallValidate(false);
                // crt.update(transaction);
                if (hasCarriageReturns) {
                    file.write("\n");
                }
            }
            serie.cursorClose(statement);
        } finally {
            if (readTransaction != null) {
                readTransaction.closeTransaction();
            }
        }
    }

    private String traiterReference(HEOutputAnnonceViewBean crt, String referenceCCVD) {
        String enrInitial = crt.getChampEnregistrement();
        StringBuffer enrFinal = new StringBuffer(JadeStringUtil.substring(enrInitial, 0, 11));
        enrFinal.append(referenceCCVD);
        enrFinal.append(JadeStringUtil.substring(enrInitial, 31, 89));
        return enrFinal.toString();
    }

}
