package ch.globaz.vulpecula.process.communicationsalaires;

import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.absencejustifiee.AbsenceJustifiee;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.congepaye.CongePaye;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;
import ch.globaz.vulpecula.domain.models.decompte.TypeDecompte;
import ch.globaz.vulpecula.external.api.poi.AbstractListExcel;
import ch.globaz.vulpecula.process.prestations.PrestationsListExcel;
import globaz.globall.db.BSession;

public class ListSalairesExcel extends PrestationsListExcel {
    private Annee annee;
    private String typeDecompte;

    private static final String SHEET_TITLE = "SA";
    private List<SalairesAAnnoncer> listDecompteSalaire;

    private final int COL_NUM_AFFILIE = 0;
    private final int COL_NSS = 1;
    private final int COL_NOM_TRAVAILLEUR = 2;
    private final int COL_MOIS_DEBUT = 3;
    private final int COL_MOIS_FIN = 4;
    private final int COL_ANNEE = 5;
    private final int COL_MONTANT = 6;

    private int currentElement = 1;

    private static final String EMPTY_CRITERE = "-";

    public ListSalairesExcel(BSession session, String filenameRoot, String documentTitle) {
        super(session, filenameRoot, documentTitle);
        HSSFSheet sheet = createSheet(SHEET_TITLE);
        sheet.setAutobreaks(true);
        sheet.setColumnWidth((short) COL_NUM_AFFILIE, AbstractListExcel.COLUMN_WIDTH_AFILIE);
        sheet.setColumnWidth((short) COL_NSS, AbstractListExcel.COLUMN_WIDTH_AFILIE);
        sheet.setColumnWidth((short) COL_NOM_TRAVAILLEUR, AbstractListExcel.COLUMN_WIDTH_5500);
        sheet.setColumnWidth((short) COL_MOIS_DEBUT, AbstractListExcel.COLUMN_WIDTH_DATE);
        sheet.setColumnWidth((short) COL_MOIS_FIN, AbstractListExcel.COLUMN_WIDTH_DATE);
        sheet.setColumnWidth((short) COL_ANNEE, AbstractListExcel.COLUMN_WIDTH_DATE);
        sheet.setColumnWidth((short) COL_MONTANT, AbstractListExcel.COLUMN_WIDTH_MONTANT);
    }

    @Override
    public void createContent() {
        HSSFPrintSetup ps = initPage(true);
        ps.setFitWidth((short)1);
        ps.setFitHeight((short)0);
        createRow();
        createCriteresConvention();
        createCriteresTypeDecompte();
        createRow(2);
        createTable();
    }

    private void createCriteresTypeDecompte() {
        createRow();
        createCell(getSession().getLabel("LISTE_TYPE_DECOMPTE"), getStyleCritereTitle());
        if (getTypeDecompte().equals(TypeDecompte.CONTROLE_EMPLOYEUR.getValue())) {
            createCell(getSession().getCodeLibelle(getTypeDecompte()), getStyleCritere());
        } else {
            createCell(getSession().getLabel("JSP_AUTRE_DECOMPTE"));
        }
    }

    private void createCriteresConvention() {
        createRow();
        createCell(getSession().getLabel("LISTE_CONVENTION"), getStyleCritereTitle());
        if (convention.getId() != null) {
            createCell(convention.getCode() + " " + convention.getDesignation(), getStyleCritere());
        } else {
            createCell(EMPTY_CRITERE);
        }
    }

    private void createTable() {
        createCell(getSession().getLabel("LISTE_SALAIRES_NUM_AFFILIE"), getStyleListGris25PourcentGras());
        createCell(getSession().getLabel("LISTE_SALAIRES_NSS"), getStyleListGris25PourcentGras());
        createCell(getSession().getLabel("LISTE_SALAIRES_NOM_TRAVAILLEUR"), getStyleListGris25PourcentGras());
        createCell(getSession().getLabel("LISTE_SALAIRES_MOIS_DEBUT"), getStyleListGris25PourcentGras());
        createCell(getSession().getLabel("LISTE_SALAIRES_MOIS_FIN"), getStyleListGris25PourcentGras());
        createCell(getSession().getLabel("LISTE_SALAIRES_ANNEE_FIN"), getStyleListGris25PourcentGras());
        createCell(getSession().getLabel("LISTE_SALAIRES_MONTANT_FIN"), getStyleListGris25PourcentGras());

        for (SalairesAAnnoncer salaires : listDecompteSalaire) {
            if (salaires.getListeDecomptes() != null && salaires.getListeDecomptes().size() > 0) {
                for (DecompteSalaire decompteSalaire : salaires.getListeDecomptes()) {
                    createRow();
                    createCell(decompteSalaire.getEmployeur().getAffilieNumero(), getStyleListLeft());
                    createCell(decompteSalaire.getPosteTravail().getTravailleur().getNumAvsActuel(), getStyleListLeft());
                    createCell(decompteSalaire.getNomPrenomTravailleur(), getStyleListLeft());
                    createCell(decompteSalaire.getPeriode().getDateDebut().toString(), getStyleListLeft());
                    createCell(decompteSalaire.getPeriode().getDateFin().toString(), getStyleListLeft());
                    createCell(getAnnee().getValue(), getStyleListRight());
                    createCell(decompteSalaire.getSalaireTotal(), getStyleMontant());
                    currentElement++;
                    setChanged();
                    notifyObservers();
                }
            }
            if (salaires.getListeAJ() != null && salaires.getListeAJ().size() > 0) {
                for (AbsenceJustifiee aj : salaires.getListeAJ()) {
                    if (aj.isAnnoncableAVS()) {
                        createRow();
                        createCell(aj.getEmployeur().getAffilieNumero(), getStyleListLeft());
                        createCell(aj.getTravailleur().getNumAvsActuel(), getStyleListLeft());
                        createCell(aj.getNomPrenomTravailleur(), getStyleListLeft());
                        createCell(aj.getDateVersement().toString(), getStyleListLeft());
                        createCell(aj.getDateVersement().toString(), getStyleListLeft());
                        createCell(aj.getDateVersement().getAnnee(), getStyleListLeft());
                        createCell(aj.getMasseAVS(), getStyleListLeft());
                    }
                }
            }
            if (salaires.getListeCP() != null && salaires.getListeCP().size() > 0) {
                for (CongePaye cp : salaires.getListeCP()) {
                    if (cp.isAnnoncableAVS()) {
                        createRow();
                        createCell(cp.getEmployeur().getAffilieNumero(), getStyleListLeft());
                        createCell(cp.getTravailleur().getNumAvsActuel(), getStyleListLeft());
                        createCell(cp.getNomPrenomTravailleur(), getStyleListLeft());
                        createCell(cp.getDateVersement().toString(), getStyleListLeft());
                        createCell(cp.getDateVersement().toString(), getStyleListLeft());
                        createCell(cp.getDateVersement().getAnnee(), getStyleListLeft());
                        createCell(cp.getMasseAVS(), getStyleListLeft());
                    }
                }
            }
        }
    }

    @Override
    public String getNumeroInforom() {
        return DocumentConstants.LISTES_SALAIRES_TYPE_NUMBER;
    }

    @Override
    public String getListName() {
        return getSession().getLabel("LISTE_SALAIRES");
    }

    public Annee getAnnee() {
        return annee;
    }

    public void setAnnee(Annee annee) {
        this.annee = annee;
    }

    public List<SalairesAAnnoncer> getListDecompteSalaire() {
        return listDecompteSalaire;
    }

    public void setListDecompteSalaire(List<SalairesAAnnoncer> listeSalairesAAnnoncer) {
        listDecompteSalaire = listeSalairesAAnnoncer;
    }

    public String getTypeDecompte() {
        return typeDecompte;
    }

    public void setTypeDecompte(String typeDecompte) {
        this.typeDecompte = typeDecompte;
    }

    /**
     * Retourne le numéro de l'élément actuellement en traitement
     * 
     * @return int représentant le numéro du traitement
     */
    public int getCurrentElement() {
        return currentElement;
    }
}