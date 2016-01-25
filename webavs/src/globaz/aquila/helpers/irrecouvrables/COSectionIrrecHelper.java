package globaz.aquila.helpers.irrecouvrables;

import globaz.aquila.db.irrecouvrables.COSectionIrrecViewBean;
import globaz.aquila.process.COProcessComptabiliserIrrecouvrable;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.irrecouvrable.CAAmortissementCi;
import globaz.osiris.db.irrecouvrable.CAKeyPosteContainer;
import globaz.osiris.db.irrecouvrable.CAPoste;
import globaz.osiris.db.irrecouvrable.CAVentilateur;
import java.math.BigDecimal;
import java.util.Map;

/**
 * @author bjo
 * 
 */
public class COSectionIrrecHelper extends FWHelper {
    public COSectionIrrecHelper() {
        super();
    }

    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        COSectionIrrecViewBean sectionIrrecViewBean = (COSectionIrrecViewBean) viewBean;
        BSession sessionOsiris = createSessionOsiris((BSession) session);

        // ventilation et calcul des valeur pour le viewBean
        CAVentilateur ventilateur = new CAVentilateur(sessionOsiris, sectionIrrecViewBean.getIdSectionsList());
        ventilateur.executerVentilation();
        ventilateur.executerAmortissementCi();
        // ventilateur.displayAmortissementCiContainer();
        Map<CAKeyPosteContainer, CAPoste> postesMap = ventilateur.getPostesTries();
        BigDecimal[] montantDuAndAffecteTotal = ventilateur.getMontantDuAndAffecteTotal();
        BigDecimal montantAVentilerTotal = ventilateur.getMontantAVentilerTotalAvantVentilation();
        Map<Integer, CAAmortissementCi> amortissementCiMap = ventilateur.getAmortissementCiTries();

        // envoi des paramètres au viewBean
        sectionIrrecViewBean.setPostesMap(postesMap);
        sectionIrrecViewBean.setAmortissementCiMap(amortissementCiMap);
        sectionIrrecViewBean.setIdCompteAnnexe(ventilateur.getCompteAnnexe().getIdCompteAnnexe());
        sectionIrrecViewBean.setNumeroAffilie(ventilateur.getCompteAnnexe().getIdExterneRole());
        sectionIrrecViewBean.setDescriptionAffilie(ventilateur.getCompteAnnexe().getDescription());
        if (ventilateur.getCompteIndividuelAffilie() != null) {
            String compteIndividuelId = ventilateur.getCompteIndividuelAffilie().getCompteIndividuelId();
            sectionIrrecViewBean.setIdCompteIndividuelAffilie(compteIndividuelId);
        }
        sectionIrrecViewBean.setMontantDuTotal(montantDuAndAffecteTotal[0]);
        sectionIrrecViewBean.setMontantAffecteTotal(montantDuAndAffecteTotal[1]);
        sectionIrrecViewBean.setSoldeTotal(montantDuAndAffecteTotal[0].subtract(montantDuAndAffecteTotal[1]));
        sectionIrrecViewBean.setAffectationDisponible(montantAVentilerTotal);
        sectionIrrecViewBean.setAffectationAffecte(montantDuAndAffecteTotal[1]);
        sectionIrrecViewBean.setAffectationSolde(montantAVentilerTotal.subtract(montantDuAndAffecteTotal[1]));
        // sectionIrrecViewBean.displayPostesMapInConsole();
        sectionIrrecViewBean.setHasRubriqueCotPers(ventilateur.hasRubriqueCotPers());
        sectionIrrecViewBean.setTiers(ventilateur.getTiers());
    }

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        COSectionIrrecViewBean sectionIrrecViewBean = (COSectionIrrecViewBean) viewBean;
        COProcessComptabiliserIrrecouvrable process = new COProcessComptabiliserIrrecouvrable();
        try {
            process.setISession(session);
            process.setEMailAddress(sectionIrrecViewBean.getEmail());
            process.setIdCompteAnnexe(sectionIrrecViewBean.getIdCompteAnnexe());
            process.setPostesMap(sectionIrrecViewBean.getPostesMap());
            process.setIdCompteIndividuelAffilie(sectionIrrecViewBean.getIdCompteIndividuelAffilie());
            process.setAmortissementCiMap(sectionIrrecViewBean.getAmortissementCiMap());
            process.setWantTraiterAmortissementCi(sectionIrrecViewBean.isWantTraiterAmortissementCi());
            BProcessLauncher.start(process);
        } catch (Exception e) {
            sectionIrrecViewBean.setMsgType(FWViewBeanInterface.ERROR);
            sectionIrrecViewBean.setMessage(e.toString());
        }
    }

    private BSession createSessionOsiris(BSession session) throws Exception {
        BSession sessionOsiris;
        sessionOsiris = (BSession) GlobazSystem.getApplication(CAApplication.DEFAULT_APPLICATION_OSIRIS).newSession();
        session.connectSession(sessionOsiris);
        return sessionOsiris;
    }
}
