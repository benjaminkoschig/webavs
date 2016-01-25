package globaz.corvus.process;

import globaz.corvus.db.annonces.REAnnonceRente;
import globaz.corvus.db.annonces.REAnnonceRenteManager;
import globaz.corvus.db.annonces.REAnnoncesAbstractLevel1A;
import globaz.corvus.db.basescalcul.REBasesCalcul;
import globaz.corvus.db.basescalcul.REBasesCalculDixiemeRevision;
import globaz.corvus.db.basescalcul.REBasesCalculNeuviemeRevision;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;

/**
 * 
 * @author SCR
 * 
 */
public class REAnnulerDiminutionRAHandler {

    public class AnnonceDiminutionContainer {
        public String codeApplication = "";
        public String idAnnonceHeader = "";
        public String idAnnonceRente = "";
        public String moisRapport = "";
        public String montant = "";

    }

    public AnnonceDiminutionContainer getAnnonceDiminution(BSession session, BITransaction transaction,
            RERenteAccordee ra, String noRevision) throws Exception {

        AnnonceDiminutionContainer result = new AnnonceDiminutionContainer();

        REAnnonceRenteManager mgr = new REAnnonceRenteManager();
        mgr.setSession(session);
        mgr.setForIdRenteAccordee(ra.getIdPrestationAccordee());
        mgr.find(transaction);

        for (int i = 0; i < mgr.size(); i++) {
            REAnnonceRente ar = (REAnnonceRente) mgr.getEntity(i);

            REAnnoncesAbstractLevel1A ann = new REAnnoncesAbstractLevel1A();
            ann.setSession(session);
            ann.setIdAnnonce(ar.getIdAnnonceHeader());
            ann.retrieve(transaction);

            if (noRevision.equals("10")) {
                // Recherche de l'annonce de diminution code application = 45

                if ("45".equals(ann.getCodeApplication())) {
                    result.codeApplication = "45";
                    result.moisRapport = ann.getMoisRapport();
                    result.montant = ann.getMensualitePrestationsFrancs();
                    result.idAnnonceHeader = ann.getIdAnnonce();
                    result.idAnnonceRente = ar.getIdAnnonceRente();
                    return result;
                }

            } else if (noRevision.equals("09") || noRevision.equals("9")) {
                // Recherche de l'annonce de diminution code application = 42

                if ("42".equals(ann.getCodeApplication())) {
                    result.codeApplication = "42";
                    result.moisRapport = ann.getMoisRapport();
                    result.montant = ann.getMensualitePrestationsFrancs();
                    result.idAnnonceHeader = ann.getIdAnnonce();
                    result.idAnnonceRente = ar.getIdAnnonceRente();
                    return result;
                }
            } else {
                throw new Exception("Unsupported révision number : " + noRevision);
            }
        }
        return null;
    }

    /**
     * 
     * Retourne le numéro de révision de la base de calcul liée à la rente accordée
     * 
     * @param transaction
     * @param ra
     * @return
     * @throws Exception
     */
    public String getNoRevision(BSession session, BITransaction transaction, RERenteAccordee ra) throws Exception {
        REBasesCalculDixiemeRevision bc10 = new REBasesCalculDixiemeRevision();
        REBasesCalculNeuviemeRevision bc9 = new REBasesCalculNeuviemeRevision();
        REBasesCalcul bc = new REBasesCalcul();

        bc.setIdBasesCalcul(ra.getIdBaseCalcul());
        bc.setSession(session);
        bc.retrieve(transaction);

        String noRevision;
        if (bc.isNew()) {
            throw new Exception("Base de calcul not found" + " (REAnnulerDiminutionRAHanlder)");
        } else {
            bc10.setSession(session);
            bc10.setIdBasesCalcul(bc.getIdBasesCalcul());
            bc10.retrieve(transaction);
            if (bc10.isNew()) {
                bc9.setSession(session);
                bc9.setIdBasesCalcul(bc.getIdBasesCalcul());
                bc9.retrieve(transaction);
                if (bc9.isNew()) {
                    throw new Exception("Base de calcul not found" + " (REAnnulerDiminutionRAHanlder)");
                } else {
                    noRevision = "9";
                }
            } else {
                noRevision = "10";
            }
        }
        return noRevision;

    }

}
