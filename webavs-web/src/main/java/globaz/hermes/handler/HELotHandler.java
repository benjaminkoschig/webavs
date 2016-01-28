package globaz.hermes.handler;

import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.FWFindParameter;
import globaz.globall.db.FWFindParameterManager;
import globaz.globall.util.JACalendar;
import globaz.hermes.application.HEApplication;
import globaz.hermes.db.access.HENbreARCDansLot;
import globaz.hermes.db.gestion.HELotListViewBean;
import globaz.hermes.db.gestion.HELotViewBean;
import java.math.BigDecimal;

public class HELotHandler {
    public static String MAX_ARC_PAR_LOT = "MAX_ARC";

    private static HELotViewBean addNewLot(String csPriorite, BSession session, String typeLot, String source,
            String dateAnnonce, BTransaction transaction) throws Exception {
        HELotViewBean lot;
        // Pas encore de lot, on en crée un nouveau
        lot = new HELotViewBean();
        // date d'aujourd'hui
        lot.setDateCentrale(dateAnnonce);
        // je mets l'heure
        lot.setHeureTraitement(JACalendar.formatTime(JACalendar.now()));
        lot.setDateTraitement(JACalendar.todayJJsMMsAAAA());
        // l'utilisateur/programme
        lot.setUtilisateur(source);
        // c'est un envoi
        lot.setType(typeLot);
        // pas acquitté sauf si une réception
        lot.setQuittance("0");
        lot.setPriorite(csPriorite);
        lot.setEtat(HELotViewBean.CS_LOT_ETAT_OUVERT);
        /*																                              */
        // je sauve le nouveau lot
        lot.setSession(session);
        lot.add(transaction);
        return lot;
    }

    public static HELotViewBean getLot(String typeLot, BSession session, BTransaction transacation, String dateAnnonce,
            String csPriorite) throws Exception {
        HELotListViewBean lots = new HELotListViewBean();
        // - je recherche un lot de type envoi pour aujourd'hui
        lots.setForDateEnvoi(dateAnnonce);
        lots.setSession(session);
        lots.setForType(typeLot);
        lots.setForQuittance("0");
        lots.setForPriorite(csPriorite);
        lots.setForEtat(HELotViewBean.CS_LOT_ETAT_OUVERT);
        lots.wantCallMethodAfter(false);
        lots.setOrder("RMILOT DESC");
        lots.find(transacation);
        if (lots.size() > 0) {
            return (HELotViewBean) lots.getFirstEntity();
        } else {
            return null;
        }
    }

    public static String getLotId(String idLot, String type, String csPriorite, BSession session, String typeLot,
            String dateAnnonce, String source, BTransaction transaction) throws Exception {
        HELotViewBean lot = getLot(typeLot, session, transaction, dateAnnonce, csPriorite);
        if (lot == null) {
            lot = addNewLot(csPriorite, session, typeLot, source, dateAnnonce, transaction);
            return lot.getIdLot();
        } else {
            // J'ai déjà un lot, contrôle la taille en fonction de la priorité
            if (!HELotViewBean.CS_LOT_PTY_HAUTE.equals(csPriorite)
                    && getNombreMaxArc(session, transaction) <= getNbLotCrt(session, transaction, lot)) {
                // limite dépassé, nouveau lot
                lot = addNewLot(csPriorite, session, typeLot, source, dateAnnonce, transaction);
            }
        }
        return lot.getIdLot();
    }

    private static int getNbLotCrt(BSession session, BTransaction transaction, HELotViewBean lot) throws Exception {
        HENbreARCDansLot crtNb = new HENbreARCDansLot();
        crtNb.setSession(session);
        crtNb.setNumeroLot(lot.getIdLot());
        crtNb.retrieve(transaction);
        return crtNb.getNombreARC();
    }

    private static int getNombreMaxArc(BSession session, BTransaction transaction) throws Exception {
        FWFindParameterManager param = new FWFindParameterManager();
        param.setSession(session);
        param.setIdApplParametre(HEApplication.DEFAULT_APPLICATION_HERMES);
        param.setIdCleDiffere(MAX_ARC_PAR_LOT);
        param.find(transaction);
        if (param.size() > 0) {
            FWFindParameter parametre = (FWFindParameter) param.getFirstEntity();
            return new BigDecimal(parametre.getValeurNumParametre()).intValue();
        } else {
            throw new Exception(session.getLabel("HERMES_10039"));
        }
    }
}
