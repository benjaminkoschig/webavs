package ch.globaz.vulpecula.process.comptabilite;

import globaz.externe.IPRConstantesExternes;
import globaz.globall.db.BSessionUtil;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.external.api.poi.AbstractListExcel;

public class ListeSoldesCPPAssociationExcel extends AbstractListExcel {
    private static final String SHEET_TITLE = "soldes";
    protected static final double UNITS_COL_FACTOR = 0.0036;

    private List<SoldesCPPAssociationDTO> datas = null;
    private Date dateReference = null;

    public ListeSoldesCPPAssociationExcel(Date dateRef, List<SoldesCPPAssociationDTO> datas) {
        super(BSessionUtil.getSessionFromThreadContext(), DocumentConstants.LISTES_SOLDE_CPP_ASSOCIATION_DOC_NAME,
                DocumentConstants.LISTES_SOLDE_CPP_ASSOCIATION_NAME);
        this.datas = datas;
        dateReference = dateRef;
    }

    private void createSheet() {
        HSSFSheet sheet = createSheet(SHEET_TITLE);
        sheet.setColumnWidth((short) 0, (short) 25000);
        sheet.setColumnWidth((short) 1, AbstractListExcel.COLUMN_WIDTH_4500);
        sheet.setColumnWidth((short) 2, AbstractListExcel.COLUMN_WIDTH_4500);
        sheet.setColumnWidth((short) 3, AbstractListExcel.COLUMN_WIDTH_DESCRIPTION);
        sheet.setColumnWidth((short) 4, AbstractListExcel.COLUMN_WIDTH_ADRESS);
        sheet.setColumnWidth((short) 5, AbstractListExcel.COLUMN_WIDTH_MONTANT);
    }

    @Override
    public void createContent() {
        createSheet();
        createTable();
    }

    private void setTitle() {
        createRow();
        createRow();
        createRow();
        createCell(getSession().getLabel("JSP_LISTE_TITLE_SOLDES_OUVERT_CPP_ASSOCIATION_NOM_CPP_ASSOCIATION_AU")
                + dateReference, getStyleGras());
        createRow();
        createRow();
        createRow();
        createCell(getSession().getLabel("JSP_LISTE_COLUMN_NOM_CPP_ASSOCIATION"), getStyleListTitleCenter());
        createCell(getSession().getLabel("JSP_LISTE_COLUMN_NUMERO_SECTION"), getStyleListTitleCenter());
        createCell(getSession().getLabel("JSP_LISTE_COLUMN_COMPTE_ANNEXE"), getStyleListTitleCenter());
        createCell(getSession().getLabel("JSP_LISTE_COLUMN_DESCRIPTION"), getStyleListTitleCenter());
        createCell(getSession().getLabel("JSP_LISTE_COLUMN_ADRESSE"), getStyleListTitleCenter());
        createCell(getSession().getLabel("JSP_LISTE_COLUMN_SOLDE"), getStyleListTitleCenter());
    }

    private void createTable() {

        setTitle();

        int nbElements = 0;
        Montant totalSoldes = new Montant(0);
        for (SoldesCPPAssociationDTO soldesCPPAssociationDTO : datas) {
            createRow();
            createCell(soldesCPPAssociationDTO.getNomCppAssociation());
            createCell(soldesCPPAssociationDTO.getNumSection());
            createCell(soldesCPPAssociationDTO.getIdExterneRole());
            createCell(soldesCPPAssociationDTO.getDescription());
            String adresseTiersAsString = "";
            try {
                AdresseTiersDetail adresseTiers = VulpeculaServiceLocator.getAdresseService().getAdresseTiers(
                        soldesCPPAssociationDTO.getIdTiers(), true, dateReference.getSwissValue(),
                        IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_DEFAULT,
                        IPRConstantesExternes.TIERS_CS_TYPE_ADRESSE_COURRIER, "");
                adresseTiersAsString = adresseTiers.getAdresseFormate();

            } catch (Exception e) {
                // On affiche le message d'erreur de récupération d'adresse dans le fichier excel et on n'arrête pas le
                // processus.
                adresseTiersAsString = getSession().getLabel("JSP_ERREUR_RECUPERATION_ADRESSE");
                adresseTiersAsString = adresseTiersAsString.replace("{0}", soldesCPPAssociationDTO.getIdTiers());
                adresseTiersAsString = adresseTiersAsString.replace("{1}", e.getMessage());
            }

            createCell(adresseTiersAsString, getStyleVerticalAlign());
            createCell(new Montant(soldesCPPAssociationDTO.getSolde()), getStyleMontantNoBorder());
            totalSoldes = totalSoldes.add(new Montant(soldesCPPAssociationDTO.getSolde()));
            nbElements++;
        }
        createRow();
        createCell(getSession().getLabel("JSP_LISTE_COLUMN_TOTAL_DE_CAS"), getStyleListTitleLeft());
        createCell(nbElements, getStyleListTitleCenter());
        createEmptyCell();
        createCell(totalSoldes, getStyleMontantBorderMediumTotal());
    }

    @Override
    public String getNumeroInforom() {
        // TODO Auto-generated method stub
        return null;
    }
}
