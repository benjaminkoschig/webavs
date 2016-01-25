package globaz.pavo.util;

import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JADate;
import globaz.globall.util.JAStringFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.pavo.db.compte.CICompteIndividuel;
import globaz.pavo.db.compte.CICompteIndividuelManager;
import globaz.pavo.db.compte.CIEcriture;
import globaz.pavo.db.compte.CIEcritureManager;
import globaz.pavo.db.compte.CIRassemblementOuverture;
import java.util.ArrayList;

public class CICorrigeCCVDUtil {

    public boolean existeCloture(String nss, int annee, BSession session, BTransaction transaction) throws Exception {
        nss = JAStringFormatter.deformatAvs(nss);
        CICompteIndividuelManager mgr = new CICompteIndividuelManager();
        mgr.setSession(session);
        mgr.setForNumeroAvs(nss);
        mgr.setForRegistre(CICompteIndividuel.CS_REGISTRE_ASSURES);
        if (JadeStringUtil.isBlankOrZero(nss)) {
            throw new Exception();
        }
        mgr.find();
        if (mgr.size() > 0) {
            CICompteIndividuel ci = (CICompteIndividuel) mgr.getFirstEntity();
            CIRassemblementOuverture cloture = ci.getDerniereBCloture(transaction);
            if (cloture != null) {
                JADate date = new JADate(cloture.getDateCloture());
                int anneeCloture = date.getYear();
                if (anneeCloture >= annee) {
                    return true;
                }
            }
        }
        return false;
    }

    private void supprimeInscriptionsCI(String nss, String compteIndivuelId, String annee, BSession session,
            BTransaction transaction) throws Exception {
        CIEcritureManager ecMgr = new CIEcritureManager();
        ecMgr.setForCompteIndividuelId(compteIndivuelId);
        ecMgr.setForAnnee(annee);
        ecMgr.setSession(session);
        ecMgr.setForGenreCotPers("true");
        if (JadeStringUtil.isBlankOrZero(compteIndivuelId)) {
            System.out.println("id Vide : " + nss);
            return;
        }
        if (JadeStringUtil.isBlankOrZero(annee)) {
            System.out.println("Annee vide " + nss);
            return;
        }
        ecMgr.setForRassemblementOuvertureId("0");
        ecMgr.find();
        for (int i = 0; i < ecMgr.size(); i++) {
            CIEcriture ec = (CIEcriture) ecMgr.getEntity(i);
            ec.wantCallMethodBefore(false);
            ec.wantCallMethodAfter(false);
            ec.wantCallValidate(false);
            ec.delete(transaction);
        }
    }

    /**
     * Méthode qui supprime les inscriptions
     * 
     * @param nss
     * @param annee
     * @param session
     * @throws Exception
     */
    public void supprimeInscriptionsTiers(String nss, String annee, BSession session, BTransaction transaction)
            throws Exception {
        nss = JAStringFormatter.deformatAvs(nss);
        CICompteIndividuelManager ciMgr = new CICompteIndividuelManager();
        ciMgr.setSession(session);
        ciMgr.setForRegistre(CICompteIndividuel.CS_REGISTRE_ASSURES);
        ciMgr.setForNumeroAvs(nss);
        ciMgr.find();
        if (ciMgr.size() < 1) {
            ciMgr.setForRegistre(CICompteIndividuel.CS_REGISTRE_PROVISOIRE);
            ciMgr.find();
        }

        if (ciMgr.size() > 0) {
            CICompteIndividuel ci = (CICompteIndividuel) ciMgr.getFirstEntity();
            // On supprime en premier les inscriptions du CI
            supprimeInscriptionsCI(nss, ci.getCompteIndividuelId(), annee, session, transaction);

        } else {
            System.out.println("Aucun Assuré trouvé pour ce nss : " + nss);
        }
    }

    public boolean updateCI(String annee, String nss, BSession session, BTransaction transaction) throws Exception {
        boolean casUpdate = false;
        nss = JAStringFormatter.deformatAvs(nss);
        CICompteIndividuelManager mgr = new CICompteIndividuelManager();
        mgr.setSession(session);
        mgr.setForNumeroAvs(nss);
        mgr.setForRegistre(CICompteIndividuel.CS_REGISTRE_ASSURES);
        if (JadeStringUtil.isBlankOrZero(nss)) {
            throw new Exception();
        }
        mgr.find();
        if (mgr.size() > 0) {
            CICompteIndividuel ci = (CICompteIndividuel) mgr.getFirstEntity();
            ArrayList list = ci.getIdCILies(transaction);
            for (int i = 0; i < list.size(); i++) {
                if (ci.getCompteIndividuelId().equals(list.get(i))) {
                    continue;
                }
                CIEcritureManager ecMgr = new CIEcritureManager();
                ecMgr.setSession(session);
                ecMgr.setForAnnee(annee);
                ecMgr.setForCompteIndividuelId((String) list.get(i));
                ecMgr.setForGenreCotPers("true");
                ecMgr.find();
                for (int j = 0; j < ecMgr.size(); j++) {
                    CIEcriture ecri = (CIEcriture) ecMgr.getEntity(j);
                    casUpdate = true;
                    BStatement statement = new BStatement(transaction);
                    statement.createStatement();
                    statement.execute("update " + Jade.getInstance().getDefaultJdbcSchema() + ".ciecrip set kaiind = "
                            + ci.getCompteIndividuelId() + " where kbiecr =" + ecri.getEcritureId());
                    statement.closeStatement();
                }
            }
        }
        return casUpdate;

    }

}
