package ch.globaz.vulpecula.process.congepaye;

import globaz.globall.db.BSession;
import java.util.Collection;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.congepaye.Compteur;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.domain.models.registre.Convention;
import ch.globaz.vulpecula.external.api.poi.AbstractListExcel;

public class BouclementExcel extends AbstractListExcel {
    private static final int COL_NO_AFFILIE = 0;
    private static final int COL_NO_TRAVAILLEUR = 1;
    private static final int COL_NO_POSTE = 2;
    private static final int COL_NOM_PRENOM = 3;
    private static final int COL_SAL_FIGE = 4;

    private Map<Convention, Collection<Compteur>> compteursByConvention;
    private Annee annee;

    public BouclementExcel(BSession session, String filenameRoot, String documentTitle,
            Map<Convention, Collection<Compteur>> compteursByConvention, Annee annee) {
        super(session, filenameRoot, documentTitle);
        this.compteursByConvention = compteursByConvention;
        this.annee = annee;
        setWantFooter(false);
        setWantHeader(false);
    }

    @Override
    public void createContent() {
        for (Map.Entry<Convention, Collection<Compteur>> entry : compteursByConvention.entrySet()) {
            Convention convention = entry.getKey();
            Collection<Compteur> compteurs = entry.getValue();

            createSheet(convention);
            createHeader();

            createRow(2);
            createCriteres(convention);
            createRow();
            createTable(compteurs);

            createFooter(getNumeroInforom());
        }
    }

    private void createSheet(Convention convention) {
        HSSFSheet sheet = createSheet(convention.getDesignation());
        sheet.setColumnWidth((short) COL_NO_AFFILIE, AbstractListExcel.COLUMN_WIDTH_AFILIE);
        sheet.setColumnWidth((short) COL_NO_TRAVAILLEUR, AbstractListExcel.COLUMN_WIDTH_4500);
        sheet.setColumnWidth((short) COL_NO_POSTE, AbstractListExcel.COLUMN_WIDTH_4500);
        sheet.setColumnWidth((short) COL_NOM_PRENOM, AbstractListExcel.COLUMN_WIDTH_DESCRIPTION);
        sheet.setColumnWidth((short) COL_SAL_FIGE, AbstractListExcel.COLUMN_WIDTH_MONTANT);
    }

    private void createCriteres(Convention convention) {
        createMergedRegion(2, getLabel("LISTE_BOUCLEMENT_CP_TITRE"), getStyleGras());
        createRow();
        createMergedRegion(2, getLabel("LISTE_BOUCLEMENT_CP_SITUATION_AU") + Date.now().getSwissValue(), getStyleGras());
        createRow();
        createRow();
        createCell(getLabel("LISTE_BOUCLEMENT_CP_CONVENTION"), getStyleCritereTitle());
        createCell(convention.getDesignation(), getStyleCritere());
        createRow();
        createCell(getLabel("LISTE_BOUCLEMENT_CP_ANNEE"), getStyleCritereTitle());
        createCell(annee.toString());
        createRow();
    }

    private void createTable(Collection<Compteur> compteurs) {
        createCell(getLabel("LISTE_BOUCLEMENT_CP_NO_AFFILIE"), getStyleListTitleLeft());
        createCell(getLabel("LISTE_BOUCLEMENT_CP_NO_TRAVAILLEUR"), getStyleListTitleLeft());
        createCell(getLabel("LISTE_BOUCLEMENT_CP_NO_POSTE"), getStyleListTitleLeft());
        createCell(getLabel("LISTE_BOUCLEMENT_CP_NOM_PRENOM"), getStyleListTitleLeft());
        createCell(getLabel("LISTE_BOUCLEMENT_CP_SAL_FIGE"), getStyleListTitleLeft());
        createRow();
        for (Compteur compteur : compteurs) {
            PosteTravail posteTravail = compteur.getPosteTravail();
            createCell(posteTravail.getAffilieNumero(), getStyleListLeft());
            createCell(posteTravail.getIdTravailleur(), getStyleListLeft());
            createCell(posteTravail.getId(), getStyleListLeft());
            createCell(posteTravail.getNomPrenomTravailleur(), getStyleListLeft());
            createCell(compteur.getMontantRestant().getValue(), getStyleMontant());
            createRow();
        }
    }

    @Override
    public String getNumeroInforom() {
        return DocumentConstants.LISTES_BOUCLEMENT;
    }

}
