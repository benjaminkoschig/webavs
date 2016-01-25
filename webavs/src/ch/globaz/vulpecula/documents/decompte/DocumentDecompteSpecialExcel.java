package ch.globaz.vulpecula.documents.decompte;

import globaz.globall.db.BSession;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;
import ch.globaz.vulpecula.external.api.poi.AbstractEditorExcel;
import ch.globaz.vulpecula.external.models.pyxis.Adresse;
import ch.globaz.vulpecula.external.models.pyxis.CodeLangue;
import ch.globaz.vulpecula.models.decompte.TableauContributions;
import ch.globaz.vulpecula.models.decompte.TableauContributions.EntreeContribution;
import ch.globaz.vulpecula.models.decompte.TableauContributions.TypeContribution;
import ch.globaz.vulpecula.util.I18NUtil;

public class DocumentDecompteSpecialExcel extends AbstractEditorExcel {

    private static final String SOURCE_FILE_PATH = "decompteSP.xls";
    private static final String SIGNATURE_FILE_PATH = "signature.png";

    private static final String START_REFERENCE = "A14";
    private static final String START_DECOMPTE_SALAIRE = "A20";
    private static final String START_ADRESSE = "J5";
    private static final String START_DESCRIPTION_DECOMPTE = "J1";
    private static final String START_NUMERO_DECOMPTE = "N2";
    private static final String START_NUMERO_AFFILIE = "J3";
    private static final String START_NOM_DECOMPTE = "M2";

    private Decompte decompte;
    private TableauContributions tableauContributions;

    public DocumentDecompteSpecialExcel(BSession session, Decompte decompte) {
        super(session);
        this.decompte = decompte;
        tableauContributions = new TableauContributions(decompte);
    }

    @Override
    protected void edit() {
        setNomDecompte();
        setReference();
        setDescriptionDecompte();
        setNumeroAffilie();
        setNumeroDecompte();
        setAdresse();

        setCell(START_DECOMPTE_SALAIRE);

        List<DecompteSalaire> lignes = decompte.getLignes();
        for (int i = 0; i < lignes.size(); i++) {
            boolean isLast = i == lignes.size() - 1;
            createLigneDecompte(lignes.get(i), isLast);
        }
        createTotalSalaire();
        moveToRow();
        createTableContributions();
        moveToRow(2);
        nextCells(6);
        createCell(decompte.getMontantContributionTotal());
        moveToRow(3);
        nextCell();
        createCell(getLabel("DOCUMENT_LIEU_DATE") + Date.now().nextDayOfWeek(Calendar.FRIDAY).getSwissValue());

        createSignature();
    }

    private void createSignature() {
        HSSFClientAnchor anchor = new HSSFClientAnchor();
        createPNGImage(SIGNATURE_FILE_PATH, anchor);
        anchor.setAnchor((short) 12, getRowNum() + 3, 0, 0, (short) 14, getRowNum() + 9, 0, 0);
    }

    private void createLigneDecompte(DecompteSalaire decompteSalaire, boolean last) {
        if (!last) {
            copyCurrentRow();
        }
        createCell(decompteSalaire.getIdPosteTravail());
        createCell(decompteSalaire.getNomPrenomTravailleur());
        createEmptyCells(4);
        createCell(decompteSalaire.getDateNaissanceTravailleur());
        createCell(getCode(decompteSalaire.getQualificationPoste()));
        if (decompteSalaire.getHeures() != 0) {
            createCell(decompteSalaire.getHeures());
        } else {
            createEmptyCell();
        }
        if (!decompteSalaire.getSalaireHoraire().isZero()) {
            createCell(decompteSalaire.getSalaireHoraire());
        } else {
            createEmptyCell();
        }
        createCell(decompteSalaire.getSalaireTotal());
        createCell(decompteSalaire.getTauxContribuableAffiche().doubleValue());

        if (!last) {
            moveToRow();
        }
    }

    private void createTotalSalaire() {
        moveToRow();
        createEmptyCells(7);
        createCell(getLabel("DOCUMENT_TOTAL_DES_SALAIRES"));
        createEmptyCells(2);
        createCell(decompte.getMasseSalarialeTotal());
    }

    private void createTableContributions() {
        Iterator<Entry<TypeContribution, Collection<EntreeContribution>>> entrySet = tableauContributions.entrySet()
                .iterator();
        while (entrySet.hasNext()) {
            Entry<TypeContribution, Collection<EntreeContribution>> entry = entrySet.next();
            moveToRow();
            if (entrySet.hasNext()) {
                copyRow(getRowNum(), getRowNum() + 2);
                copyRow(getRowNum() + 1, getRowNum() + 3);
            }
            nextCell();

            TypeContribution type = entry.getKey();
            switch (type) {
                case CAISSES_SOCIALES:
                    createCell(getLabel("DOCUMENT_CAISSES_SOCIALES"));
                    break;
                case AVS:
                    createCell(getLabel("DOCUMENT_AVS"));
                    break;
                case AC:
                    createCell(getLabel("DOCUMENT_AC"));
                    break;
                case AC2:
                    createCell(getLabel("DOCUMENT_AC2"));
                    break;
                case AF:
                    createCell(getLabel("DOCUMENT_AF"));
                    break;
            }

            int i = 0;
            for (EntreeContribution entree : entry.getValue()) {
                if (i > 0) {
                    copyCurrentRow();
                }
                nextRow();
                firstCell();
                nextCells(2);
                createCell(entree.getTaux().doubleValue());
                createCell("%");
                createCell(getLabel("DOCUMENT_SUR"));
                createCell(entree.getMasse());
                createCell(entree.getMontant());
                i++;
            }
        }
    }

    private void setNomDecompte() {
        setCell(START_NOM_DECOMPTE);
        if (decompte.getMontantContributionTotal().isNegative()) {
            createCell(getLabel("DOCUMENT_NOTE_DE_CREDIT"));
        } else {
            createCell(getLabel("DOCUMENT_COMPLEMENT"));
        }
        createCell(VulpeculaServiceLocator.getDecompteService().findTextePersonneReference(decompte));
    }

    private void setReference() {
        setCell(START_REFERENCE);
        createCell(VulpeculaServiceLocator.getDecompteService().findTextePersonneReference(decompte));
    }

    private void setDescriptionDecompte() {
        setCell(START_DESCRIPTION_DECOMPTE);
        createCell(decompte.getDescription(I18NUtil.getLocaleOf(decompte.getEmployeurLangue())));
    }

    private void setNumeroDecompte() {
        setCell(START_NUMERO_DECOMPTE);
        createCell(decompte.getId());
    }

    private void setNumeroAffilie() {
        setCell(START_NUMERO_AFFILIE);
        createCell(decompte.getEmployeurAffilieNumero());
    }

    private void setAdresse() {
        Adresse adresse = decompte.getEmployeur().getAdressePrincipale();
        if (adresse == null) {
            return;
        }
        setCell(START_ADRESSE);
        for (String ligne : decompte.getEmployeur().getAdressePrincipale().getAdresseAsStringLines()) {
            createCell(ligne);
            nextRow();
        }
    }

    @Override
    protected CodeLangue getCodeLangue() {
        return decompte.getEmployeurLangue();
    }

    @Override
    protected String getSourceFilePath() {
        return SOURCE_FILE_PATH;
    }

    @Override
    protected String getOutputFileName() {
        return "decompteSP";
    }
}
