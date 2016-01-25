package globaz.osiris.print.itext;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIEvenementContentieux;
import globaz.osiris.db.contentieux.CACalculTaxe;
import globaz.osiris.db.contentieux.CATaxe;
import globaz.osiris.db.export.CAFusionCSV;
import globaz.osiris.db.interet.tardif.CAInteretTardif;
import globaz.osiris.db.interet.tardif.CAInteretTardifFactory;
import java.util.Enumeration;

/**
 * Cette classe hérite de la classe de taxation et permet d'extraire les éléments de la taxation pour effectuer une
 * fusion
 * 
 * @author SPA
 * 
 */
public class CATaxationForFusion extends CATaxation {

    private static final String HEADERS = "TYPE;ROLE;AFFILIE;FACTURE;DEBUT_PERIODE;FIN_PERIODE;ADR1;ADR2;ADR3;ADR4;ADR5;ADR6;ADR7;DATE;DELAI;ANCIENSOLDE;AVS;CAF;CPS;AUTRE;TAXEAVS;TAXECAF;TAXECPS;MAJSTAT;TOTALTAXES;NOUVEAUSOLDE;TEXTE;DATEINT;DATENOT;POLITESSE";
    /**
     * Numéro de version
     */
    private static final long serialVersionUID = -5849630651279337685L;
    private static BSession uSession;
    private static BSpy uSpy;

    /**
     * Constructeur par défaut
     * 
     * @throws Exception
     */
    public CATaxationForFusion() throws Exception {
        super();
    }

    @Override
    public void beforePrint(BTransaction transaction) throws Exception {
        String current = "";
        try {
            super.beforePrint(transaction);
            // Déterminer le cas
            current = getSection().getCompteAnnexe().getIdExterneRole();
            // Il s'agit d'un nouveau travail
            if ((CATaxationForFusion.uSession == null)
                    || !CATaxationForFusion.uSession.getId().equals(getSession().getId())) {
                CATaxationForFusion.uSession = getSession();
                CATaxationForFusion.uSpy = new BSpy(CATaxationForFusion.uSession);
                // Créer une ligne pour le header
                CAFusionCSV header = new CAFusionCSV();
                header.setSession(getSession());
                header.setNoSerie(CATaxationForFusion.uSpy.toString());
                header.setDate(getDate());
                header.setSource(getParametreEtape().getEtape().getDescription());
                header.setData(CATaxationForFusion.HEADERS);
                header.add(transaction);
            }
            // Met à jour les informations
            CAFusionCSV line = new CAFusionCSV();
            line.setSession(getSession());
            line.setNoSerie(CATaxationForFusion.uSpy.toString());
            line.setDate(getDate());
            line.setSource(getParametreEtape().getEtape().getDescription());
            // Décision/Taxation
            line.addColumn(getParametreEtape().getEtape().getDescription());
            // Affilié
            line.addColumn(getSection().getCompteAnnexe().getRole().getDescription());
            line.addColumn(getSection().getCompteAnnexe().getIdExterneRole());
            // Section
            line.addColumn(getSection().getIdExterne());
            line.addColumn(getSection().getDateDebutPeriode());
            line.addColumn(getSection().getDateFinPeriode());
            // Adresse
            line.addColumn(getLigneAdresse1());
            line.addColumn(getLigneAdresse2());
            line.addColumn(getLigneAdresse3());
            line.addColumn(getLigneAdresse4());
            line.addColumn(getLigneAdresse5());
            line.addColumn(getLigneAdresse6());
            line.addColumn(getLigneAdresse7());
            // Date
            line.addColumn(getDate());
            // Délai
            line.addColumn(getDateDelaiPaiement());
            // Information concernant la section
            line.addColumn(getSection().getSolde());
            // Calcul des taxes et bases
            FWCurrency cAVS = new FWCurrency();
            FWCurrency cTaxeAVS = new FWCurrency();
            FWCurrency cCAF = new FWCurrency();
            FWCurrency cTaxeCAF = new FWCurrency();
            FWCurrency cCPS = new FWCurrency();
            FWCurrency cTaxeCPS = new FWCurrency();
            FWCurrency cMajorationStatutaire = new FWCurrency();
            FWCurrency cAutre = new FWCurrency();
            FWCurrency cNouveauSolde = new FWCurrency();
            Enumeration eTaxes = listTaxes();
            if (eTaxes != null) {
                while (eTaxes.hasMoreElements()) {
                    CATaxe taxe = (CATaxe) eTaxes.nextElement();
                    // Ventilation
                    CACalculTaxe ct = taxe.getCalculTaxe();
                    int idCalculTaxe = Integer.parseInt(ct.getIdCalculTaxe());
                    switch (idCalculTaxe) {
                        case 1:
                            cAVS.add(taxe.getMontantBase());
                            cTaxeAVS.add(taxe.getMontantTaxe());
                            break;
                        case 2:
                            cCAF.add(taxe.getMontantBase());
                            cTaxeCAF.add(taxe.getMontantTaxe());
                            break;
                        case 3:
                        case 4:
                        case 5:
                        case 6:
                        case 7:
                        case 8:
                            cCPS.add(taxe.getMontantBase());
                            cMajorationStatutaire.add(taxe.getMontantTaxe());
                            break;
                        case 9:
                        case 10:
                        case 11:
                        case 12:
                        case 13:
                        case 14:
                            cTaxeCPS.add(taxe.getMontantTaxe());
                            break;
                    }
                }
            }
            // Calcul du montant "Autre" et "NouveauSolde"
            cAutre.add(getSection().getSolde());
            cAutre.sub(cAVS);
            cAutre.sub(cCPS);
            cAutre.sub(cCAF);
            cNouveauSolde.add(getSection().getSolde());
            cNouveauSolde.add(getTaxe());
            // Ajout des valeurs
            line.addColumn(cAVS.toString());
            line.addColumn(cCAF.toString());
            line.addColumn(cCPS.toString());
            line.addColumn(cAutre.toString());
            line.addColumn(cTaxeAVS.toString());
            line.addColumn(cTaxeCAF.toString());
            line.addColumn(cTaxeCPS.toString());
            line.addColumn(cMajorationStatutaire.toString());
            line.addColumn(getTaxe());
            line.addColumn(cNouveauSolde.toString());
            // Description
            line.addColumn(getSection().getDescription());
            // Date de début des intérêts
            CAInteretTardif interet = CAInteretTardifFactory.getInteretTardif(getSection().getCategorieSection());
            if (interet != null) {
                interet.setIdSection(getSection().getIdSection());
                line.addColumn(interet.getDateCalculDebutInteret(getSession(), getTransaction()).toStr("."));
            } else {
                line.addColumn(getSection().getDateDebutPeriode());
            }
            // Date de dernière étape
            APIEvenementContentieux ev = getParametreEtape().getEvenementContentieuxPrecedent(getSection());
            if ((ev != null) && !JadeStringUtil.isBlankOrZero(ev.getDateExecution())) {
                line.addColumn(ev.getDateExecution());
            } else {
                line.addColumn("");
            }
            // Politesse
            line.addColumn(getTiers().getPolitesse());
            //
            line.add(transaction);
        } catch (Exception e) {
            getTransaction().addErrors(e.getMessage());
            getTransaction().addErrors("-> " + current);
            CATaxationForFusion.uSession = null;
        }
    }

    @Override
    public boolean isPrintable() {
        return false;
    }
}
