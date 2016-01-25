package globaz.prestation.controler.rentes;

import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.db.rentesaccordees.RERenteAccordeeManager;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.hera.db.famille.SFMembreFamille;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.controler.IPRDataController;
import globaz.prestation.controler.IPRImpactController;

/**
 * 
 * @author SCR
 * 
 */
public class PRRenteController implements IPRImpactController {

    /**
     * Controlleur d'impact sur l'application rente.
     * 
     * Retourne un StringBuffer contenant les eléments à controler dues à l'intervention d'elements externe contenu dans
     * le dataControler.
     */
    @Override
    public StringBuffer control(BSession session, BTransaction transaction, IPRDataController dataControler)
            throws Exception {

        StringBuffer sb = new StringBuffer();

        // Control d'impacte suite au changement d'état civil.
        if (dataControler instanceof PREtatCivilDataController) {
            PREtatCivilDataController dc = (PREtatCivilDataController) dataControler;
            sb.append(doTraitementEtatCivilDataControler(session, transaction, dc));

        }
        // else if (dataControler instanceof ???) {
        // ;
        // }
        else {
            throw new Exception("Unsuported dataControler : " + dataControler.getClass().getName());
        }

        return sb;
    }

    /**
     * 
     * Control d'impact suite au changement d'état civil.
     * 
     * 
     * @param session
     * @param transaction
     * @param dc
     *            le dataControler
     * @return memoryLog. Messages d'averissement des éléments à controler.
     * @throws Exception
     */
    private StringBuffer doTraitementEtatCivilDataControler(BSession session, BTransaction transaction,
            PREtatCivilDataController dc) throws Exception {

        StringBuffer sb = new StringBuffer();

        String csEtatCivilAvantMAJ1 = dc.getCsEtatCivilMF1AvantMAJ();
        String csEtatCivilAvantMAJ2 = dc.getCsEtatCivilMF2AvantMAJ();

        SFMembreFamille mf1 = dc.getMf1();
        SFMembreFamille mf2 = dc.getMf2();

        // Si pas d'idtiers, ni nss, l'assuré n'a pas droit à une rente, donc pas nécessaire de le controler.
        if (!JadeStringUtil.isBlankOrZero(mf1.getIdTiers()) && !JadeStringUtil.isBlankOrZero(mf1.getNss())) {

            String csEtatCivilApresMAJ1 = mf1.getEtatCivil(JACalendar.todayJJsMMsAAAA());

            // Cas 1) Remariage d'un rentier veuf. Etat civil : Veuf -> Mariage
            if (ISFSituationFamiliale.CS_ETAT_CIVIL_PARTENARIAT_DISSOUS_DECES.equals(csEtatCivilAvantMAJ1)
                    || ISFSituationFamiliale.CS_ETAT_CIVIL_VEUF.equals(csEtatCivilAvantMAJ1)) {

                if (ISFSituationFamiliale.CS_ETAT_CIVIL_MARIE.equals(csEtatCivilApresMAJ1)
                        || ISFSituationFamiliale.CS_ETAT_CIVIL_SEPARE_DE_FAIT.equals(csEtatCivilApresMAJ1)
                        || ISFSituationFamiliale.CS_ETAT_CIVIL_PARTENARIAT_ENREGISTRE.equals(csEtatCivilApresMAJ1)) {

                    // On controle que le tiers est au bénéfice d'une rente...
                    // Controle que l'assuré à une rente en cours.
                    RERenteAccordeeManager mgr = new RERenteAccordeeManager();
                    mgr.setSession(session);
                    mgr.setForCsEtatIn(IREPrestationAccordee.CS_ETAT_VALIDE + ", "
                            + IREPrestationAccordee.CS_ETAT_PARTIEL);
                    mgr.setForIdTiersBeneficiaire(mf1.getIdTiers());
                    mgr.find(transaction, 1);
                    if (!mgr.isEmpty()) {

                        sb.append(FWMessageFormat.format(session.getLabel("IMPACT_CTRL_SUPPL_VEUVAGE"), mf1.getNss()
                                + " - " + mf1.getNom() + " " + mf1.getPrenom()));
                        sb.append("<br/>");
                    }
                }
            }
        }
        // Si pas d'idtiers, ni nss, l'assuré n'a pas droit à une rente, donc pas nécessaire de le controler.
        if (!JadeStringUtil.isBlankOrZero(mf2.getIdTiers()) && !JadeStringUtil.isBlankOrZero(mf2.getNss())) {

            String csEtatCivilApresMAJ2 = mf2.getEtatCivil(JACalendar.todayJJsMMsAAAA());

            // Cas 1) Remariage d'un rentier veuf. Etat civil : Veuf -> Mariage
            if (ISFSituationFamiliale.CS_ETAT_CIVIL_PARTENARIAT_DISSOUS_DECES.equals(csEtatCivilAvantMAJ2)
                    || ISFSituationFamiliale.CS_ETAT_CIVIL_VEUF.equals(csEtatCivilAvantMAJ2)) {

                if (ISFSituationFamiliale.CS_ETAT_CIVIL_MARIE.equals(csEtatCivilApresMAJ2)
                        || ISFSituationFamiliale.CS_ETAT_CIVIL_SEPARE_DE_FAIT.equals(csEtatCivilApresMAJ2)
                        || ISFSituationFamiliale.CS_ETAT_CIVIL_PARTENARIAT_ENREGISTRE.equals(csEtatCivilApresMAJ2)) {

                    // On controle que le tiers est au bénéfice d'une rente...
                    // Controle que l'assuré à une rente en cours.
                    RERenteAccordeeManager mgr = new RERenteAccordeeManager();
                    mgr.setSession(session);
                    mgr.setForCsEtatIn(IREPrestationAccordee.CS_ETAT_VALIDE + ", "
                            + IREPrestationAccordee.CS_ETAT_PARTIEL);
                    mgr.setForIdTiersBeneficiaire(mf2.getIdTiers());
                    mgr.find(transaction, 1);
                    if (!mgr.isEmpty()) {
                        sb.append(FWMessageFormat.format(session.getLabel("IMPACT_CTRL_SUPPL_VEUVAGE"), mf1.getNss()
                                + " - " + mf1.getNom() + " " + mf1.getPrenom()));
                        sb.append("<br/>");
                    }
                }
            }
        }
        return sb;
    }

}
