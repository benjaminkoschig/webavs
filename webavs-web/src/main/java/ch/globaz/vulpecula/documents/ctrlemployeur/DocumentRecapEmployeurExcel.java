package ch.globaz.vulpecula.documents.ctrlemployeur;

import globaz.globall.db.BSession;
import java.util.List;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.controleemployeur.ControleEmployeur;
import ch.globaz.vulpecula.external.api.poi.AbstractListExcel;
import ch.globaz.vulpecula.external.models.affiliation.Cotisation;
import ch.globaz.vulpecula.external.models.affiliation.Particularite;
import ch.globaz.vulpecula.external.models.affiliation.SuiviCaisse;
import ch.globaz.vulpecula.external.models.pyxis.MoyenContact;
import ch.globaz.vulpecula.external.models.pyxis.TypeContact;

/**
 * Récapitulatif d'un employeur destiné aux contrôle d'employeur
 * 
 * Affiche en fonction de la date de référence :
 * <ul>
 * <li>l'adresse de domicile, de courrier et de paiement
 * <li>le premier contact ainsi que tous ses moyens de communication
 * <li>le solde ouvert du compte annexe (à la date du jour)
 * <li>des informations de l'affiliation (période d'activité, personalité juridique et convention)
 * <li>ses assurances actives
 * <li>le suivis de caisses
 * <li>les 6 derniers contrôles d'employeur
 * </ul>
 * 
 * @since WebBMS 2.6
 */
public class DocumentRecapEmployeurExcel extends AbstractListExcel {
    private static final String SHEET_TITLE = "recap";
    protected static final double UNITS_COL_FACTOR = 0.0036;

    private RecapControleEmployeur recap;

    public DocumentRecapEmployeurExcel(BSession session, RecapControleEmployeur recap) {
        super(session, DocumentConstants.LISTES_REVISION_RECAP_DOC_NAME, DocumentConstants.LISTES_REVISION_RECAP_NAME);
        this.recap = recap;

        HSSFSheet sheet = createSheet(SHEET_TITLE);
        sheet.setColumnWidth((short) 0, (short) (8.14 / UNITS_COL_FACTOR)); // 8.14
        sheet.setColumnWidth((short) 1, (short) (17 / UNITS_COL_FACTOR)); // 17
        sheet.setColumnWidth((short) 2, (short) (12.43 / UNITS_COL_FACTOR)); // 12.43
        sheet.setColumnWidth((short) 3, (short) (10 / UNITS_COL_FACTOR)); // 10
        sheet.setColumnWidth((short) 4, (short) (10.14 / UNITS_COL_FACTOR)); // 10.14
        sheet.setColumnWidth((short) 5, (short) (14.71 / UNITS_COL_FACTOR)); // 14.71
        sheet.setColumnWidth((short) 6, (short) (9.71 / UNITS_COL_FACTOR)); // 9.71
    }

    @Override
    public void createContent() {
        initPage(false);
        createRow();
        createTable();
    }

    private void createTable() {
        createMergedRegion(5, recap.getEmployeurDescription());
        createMergedRegion(2, recap.getDateReferenceSwissValue(), getStyleListRightNone());
        createRow();
        createCell(recap.getEmployeur().getDesignation3() + " " + recap.getEmployeur().getDesignation4());
        createRow();

        createRow();
        createMergedRegion(2, label("LISTE_RECAP_EMPLOYEUR_ADRESSEDOMICILE"), getStyleListGris25Pourcent());
        createMergedRegion(2, label("LISTE_RECAP_EMPLOYEUR_ADRESSECOURRIER"), getStyleListGris25Pourcent());
        createMergedRegion(3, label("LISTE_RECAP_EMPLOYEUR_ADRESSEPAIEMENT"), getStyleListGris25Pourcent());
        createRow();
        getCurrentRow().setHeightInPoints((short) 138);
        createMergedRegion(2, recap.getAdresseDomicile(), getStyleListVerticalAlignTopLeft());
        createMergedRegion(2, recap.getAdresseCourrier(), getStyleListVerticalAlignTopLeft());
        createMergedRegion(3, recap.getAdressePaiement(), getStyleListVerticalAlignTopLeft());

        createSepareteLine();

        createRow();
        createMergedRegion(7, label("LISTE_RECAP_EMPLOYEUR_CONTACT"), getStyleListGris25Pourcent());
        createRow();
        createMergedRegion(7, recap.getContactDescription());

        for (Map.Entry<TypeContact, MoyenContact> entry : recap.getMoyenContact().entrySet()) {
            createRow();
            createMergedRegion(2, getSession().getCodeLibelle(entry.getKey().getValue()), getStyleListLeft());
            createMergedRegion(5, entry.getValue().getValeur(), getStyleListLeft());
        }

        createSepareteLine();

        createRow();
        createMergedRegion(5, label("LISTE_RECAP_EMPLOYEUR_SOLDEOUVERT"), getStyleListGris25Pourcent());
        createMergedRegion(2, recap.getSoldeOuvert(), getStyleMontant());

        createSepareteLine();

        createRow();
        createMergedRegion(2, label("LISTE_RECAP_EMPLOYEUR_PERIODEACTIVITE"), getStyleListGris25Pourcent());
        createMergedRegion(3, label("LISTE_RECAP_EMPLOYEUR_PERSONNALITEJURIDIQUE"), getStyleListGris25Pourcent());
        createMergedRegion(2, label("LISTE_RECAP_EMPLOYEUR_CONVENTION"), getStyleListGris25Pourcent());
        createRow();
        createMergedRegion(2, recap.getEmployeurPeriodeActivite(), getStyleListLeft());
        createMergedRegion(3, recap.getEmployeurPersonaliteJuridique(getSession()), getStyleListLeft());
        createMergedRegion(2, recap.getEmployeurConvention(), getStyleListLeft());
        createRow();
        // Ajout des particularités
        List<Particularite> particularites = VulpeculaServiceLocator.getEmployeurService().findParticularites(
                recap.getEmployeur().getId());
        int taille = particularites.size();
        if (taille > 0) {
            createMergedRegion(3, label("LISTE_RECAP_EMPLOYEUR_PARTICULARITE"), getStyleListGris25Pourcent());
            createMergedRegion(2, label("LISTE_RECAP_EMPLOYEUR_PARTICULARITE_DEBUT"), getStyleListGris25Pourcent());
            createMergedRegion(2, label("LISTE_RECAP_EMPLOYEUR_PARTICULARITE_FIN"), getStyleListGris25Pourcent());
            createRow();

            int count = 0;
            for (Particularite particularite : particularites) {
                count++;
                createMergedRegion(3, getSession().getCodeLibelle(particularite.getParticularite()), getStyleListLeft());
                if (particularite.getDateDebut() != null) {
                    createMergedRegion(2, particularite.getDateDebut().getSwissValue(), getStyleListLeft());
                } else {
                    createMergedRegion(2, "", getStyleListLeft());
                }
                if (particularite.getDateFin() != null) {
                    createMergedRegion(2, particularite.getDateFin().getSwissValue(), getStyleListLeft());
                } else {
                    createMergedRegion(2, "", getStyleListLeft());
                }
                if (count < taille) {
                    createRow();
                }
            }

        }
        createSepareteLine();
        createRow();
        createMergedRegion(2, label("LISTE_RECAP_EMPLOYEUR_ASSURANCE"), getStyleListGris25Pourcent());
        createCell(label("LISTE_RECAP_EMPLOYEUR_NUMCAISSE"), getStyleListGris25Pourcent());
        createMergedRegion(2, label("LISTE_RECAP_EMPLOYEUR_PERIODEASSURANCE"), getStyleListGris25Pourcent());
        createMergedRegion(2, label("LISTE_RECAP_EMPLOYEUR_PERIODICITE"), getStyleListGris25Pourcent());

        for (Cotisation coti : recap.getCotisations()) {
            createRow();
            createMergedRegion(2, coti.getAssurance().getLibelleFr(), getStyleListLeft());
            createCell(coti.getPlanCaisse().getCodeAdministration(), getStyleListLeft());
            if (coti.getDateFin() == null) {
                createMergedRegion(2, coti.getDateDebut().getSwissValue() + " - 0", getStyleListRight());
            } else {
                createMergedRegion(2, coti.getDateDebut() + " - " + coti.getDateFin(), getStyleListRight());
            }
            createMergedRegion(2, getSession().getCodeLibelle(coti.getPeriodicite()), getStyleListLeft());
        }

        createSepareteLine();

        createRow();
        createMergedRegion(7, label("LISTE_RECAP_EMPLOYEUR_SUIVISCAISSES"), getStyleListGris25Pourcent());
        createRow();
        createCell(label("LISTE_RECAP_EMPLOYEUR_GENRESUIVIS"), getStyleListGris25Pourcent());
        createMergedRegion(2, label("LISTE_RECAP_EMPLOYEUR_CAISSESUIVIS"), getStyleListGris25Pourcent());
        createMergedRegion(2, label("LISTE_RECAP_EMPLOYEUR_NUMINTERNETCAISSE"), getStyleListGris25Pourcent());
        createMergedRegion(2, label("LISTE_RECAP_EMPLOYEUR_PERIODESUIVIS"), getStyleListGris25Pourcent());

        for (SuiviCaisse suivi : recap.getSuiviCaisses()) {
            createRow();
            createCell(getSession().getCode(suivi.getGenreCaisse()), getStyleListLeft());
            if (suivi.getCaisse() != null) {
                createMergedRegion(2, suivi.getCaisse().getDescription(), getStyleListLeft());
                createMergedRegion(2, suivi.getCaisse().getCodeAdministration(), getStyleListRight());
            } else {
                createMergedRegion(2, "", getStyleListLeft());
                createMergedRegion(2, "", getStyleListRight());
            }
            createMergedRegion(2, suivi.getPeriode().toString(), getStyleListRight());
        }

        createSepareteLine();

        createRow();
        createMergedRegion(7, label("LISTE_RECAP_EMPLOYEUR_CONTROLEEMPLOYEUR"), getStyleListGris25Pourcent());
        createRow();
        createCell(label("LISTE_RECAP_EMPLOYEUR_NUMRAPPORT"), getStyleListGris25Pourcent());
        createCell(label("LISTE_RECAP_EMPLOYEUR_DATECONTROLE"), getStyleListGris25Pourcent());
        createCell(label("LISTE_RECAP_EMPLOYEUR_PERIODECONTROLE"), getStyleListGris25Pourcent());
        createCell(label("LISTE_RECAP_EMPLOYEUR_MONTANTCONTROLE"), getStyleListGris25Pourcent());
        createCell(label("LISTE_RECAP_EMPLOYEUR_TYPECONTROLE"), getStyleListGris25Pourcent());
        createCell(label("LISTE_RECAP_EMPLOYEUR_AUTRESMESURES"), getStyleListGris25Pourcent());
        createCell(label("LISTE_RECAP_EMPLOYEUR_REVISEUR"), getStyleListGris25Pourcent());

        for (ControleEmployeur ce : recap.getControlesEmployeur()) {
            createRow();
            createCell(ce.getNumeroMeroba(), getStyleListRight());
            createCell(ce.getDateControleAsSwissValue(), getStyleListRight());
            createCell(ce.getDateAuAsSwissValue(), getStyleListRight());
            createCell(Double.parseDouble(ce.getMontantAsValue()), getStyleMontant());
            createCell(getSession().getCodeLibelle(ce.getTypeAsValue()), getStyleListLeft());
            createCell((ce.isAutresMesures() ? "X" : ""), getStyleListCenter());
            createCell(ce.getIdUtilisateur(), getStyleListLeft());
        }
    }

    /**
     *
     */
    private void createSepareteLine() {
        createRow();
        getCurrentRow().setHeightInPoints((short) 7.5);
    }

    @Override
    public String getNumeroInforom() {
        return DocumentConstants.RECAP_EMPLOYEUR_EMPLOYEUR_TYPE_NUMBER;
    }

    private String label(String id) {
        return getSession().getLabel(id);
    }

}
