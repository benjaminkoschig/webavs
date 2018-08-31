package ch.globaz.vulpecula.documents.decompte;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;
import ch.globaz.vulpecula.domain.models.decompte.TypeProvenance;
import ch.globaz.vulpecula.ws.bean.ConventionEbu;

/**
 * Création du document décompte vide pour envoi, lors de la création de décomptes vides.
 * 
 * @since WebBMS 1.0
 */
public class DocumentDecompteVideComplementaire extends DocumentDecompteVide {
    private static final long serialVersionUID = 2002626138123336066L;

    private static final String NO_POSTE_TRAVAIL = "COL_1";
    private static final String NOM_PRENOM = "COL_2";
    private static final String DATE_NAISSANCE = "COL_3";
    private static final String CODE_PROF = "COL_4";
    private static final String VACANCES = "COL_5";
    private static final String GRATIFICATION = "COL_6";
    private static final String AJ = "COL_7";
    private static final String TOTAL_COL123 = "COL_8";
    private static final String TAUX_COL123 = "COL_9";
    private static final String APG_SM = "COL_10";
    private static final String MONTANT_TOTAL = "P_MONTANT_TOTAL";

    // private static final String REMARQUES = "COL_11";
    private static final String GROUPE_LIGNES = "F_GROUPE_LIGNES";

    private static DecompteContainer container = null;

    public static final String SERRURIER = "04";

    private static final int NB_LINE_MAX = 21;

    /**
     * Constructeur
     */
    public DocumentDecompteVideComplementaire() throws Exception {
        super();
    }

    @Override
    public void fillFields() throws Exception {
        super.fillFields();
        if (SERRURIER.equals(container.getDecompte().getEmployeur().getConvention().getCode())) {
            setParametres(DocumentConstants.P_COL_HEADER_VAC, "Vacances\nFerien");
        } else {
            setParametres(DocumentConstants.P_COL_HEADER_VAC, "Vacances/Jours fériés\nFerien/Feiertage");
        }

    }

    /**
     * Constructeur, on l'on passe les décomptes que l'on désire imprimer
     * 
     * @param decomptes List<Decompte>
     * @throws Exception
     */
    public DocumentDecompteVideComplementaire(final DecompteContainer decompteContainer) throws Exception {
        super(decompteContainer, DocumentConstants.DECOMPTE_VIDE_COMPLEMENTAIRE_CT_NAME,
                DocumentConstants.DECOMPTE_VIDE_COMPLEMENTAIRE_TYPE_NUMBER, false);
        container = decompteContainer;
    }

    /**
     * @param decompteContainer
     * @param isPrintingFromEbu
     * @throws Exception
     */
    public DocumentDecompteVideComplementaire(final DecompteContainer decompteContainer, boolean isPrintingFromEbu)
            throws Exception {
        super(decompteContainer, DocumentConstants.DECOMPTE_VIDE_COMPLEMENTAIRE_CT_NAME,
                DocumentConstants.DECOMPTE_VIDE_COMPLEMENTAIRE_TYPE_NUMBER, isPrintingFromEbu);
        container = decompteContainer;
    }

    @Override
    protected void fillDataSourceSalaire() {
        Collection<Map<String, Object>> collection = new ArrayList<Map<String, Object>>();
        int compteurLine = 1;
        Decompte decompte = getCurrentElement().getDecompte();
        int linesTotal = decompte.getLignes().size();
        int linesToAdd = linesToAdd(linesTotal, NB_LINE_MAX, decompte.getNbLignesAdresse(), decompte.getType());
        Map<Integer, Integer> groups = splittingGroupsByRow(linesTotal, NB_LINE_MAX, decompte.getNbLignesAdresse(),
                decompte.getType());

        if (TypeProvenance.EBUSINESS.equals(decompte.getTypeProvenance())) {
            for (DecompteSalaire decompteSalaire : decompte.getLignes()) {
                // Ne pas imprimer les lignes dont le total est ZERO pour les complémentaire ebusiness
                if (!Montant.ZERO.equals(decompteSalaire.getSalaireTotal())) {
                    Map<String, Object> ligne = new HashMap<String, Object>();
                    ligne.put(NO_POSTE_TRAVAIL, decompteSalaire.getIdPosteTravail());
                    ligne.put(NOM_PRENOM, decompteSalaire.getNomPrenomTravailleur());
                    ligne.put(DATE_NAISSANCE, decompteSalaire.getDateNaissanceTravailleur());
                    ligne.put(CODE_PROF, getCode(decompteSalaire.getQualificationPoste()));
                    ligne.put(VACANCES, decompteSalaire.getVacancesFeriesAsValue());
                    ligne.put(GRATIFICATION, decompteSalaire.getGratificationsAsValue());
                    ligne.put(AJ, decompteSalaire.getAbsencesJustifieesAsValue());
                    if (decompteSalaire.getApgComplementaireSM() != null
                            && !Montant.ZERO.equals(decompteSalaire.getApgComplementaireSM())
                            && decompteSalaire.getApgComplementaireSM().equals(decompteSalaire.getSalaireTotal())) {
                        ligne.put(TOTAL_COL123, BigDecimal.valueOf(0.00));
                    } else {
                        ligne.put(TOTAL_COL123, decompteSalaire.getSalaireTotal().getBigDecimalValue());
                    }
                    ligne.put(TAUX_COL123, decompteSalaire.getTauxContribuableForCaissesSocialesAsValue(false));
                    ligne.put(APG_SM, decompteSalaire.getApgComplementaireSM().getBigDecimalValue());
                    int currentGroup = groups.get(compteurLine);
                    ligne.put(GROUPE_LIGNES, String.valueOf(currentGroup));
                    collection.add(ligne);
                    compteurLine++;
                }
            }

        } else {
            for (DecompteSalaire decompteSalaire : decompte.getLignes()) {
                Map<String, Object> ligne = new HashMap<String, Object>();
                ligne.put(NO_POSTE_TRAVAIL, decompteSalaire.getIdPosteTravail());
                ligne.put(NOM_PRENOM, decompteSalaire.getNomPrenomTravailleur());
                ligne.put(DATE_NAISSANCE, decompteSalaire.getDateNaissanceTravailleur());
                ligne.put(CODE_PROF, getCode(decompteSalaire.getQualificationPoste()));
                ligne.put(VACANCES, "");
                ligne.put(GRATIFICATION, "");
                ligne.put(AJ, "");
                if (decompteSalaire.getDecompte().isValideOuComptablise()) {
                    ligne.put(TOTAL_COL123, decompteSalaire.getSalaireTotal().getBigDecimalValue());
                } else {
                    ligne.put(TOTAL_COL123, null);
                }
                ligne.put(TAUX_COL123, decompteSalaire.getTauxContribuableForCaissesSocialesAsValue(false));
                ligne.put(APG_SM, null);
                // ligne.put(REMARQUES, "");
                int currentGroup = groups.get(compteurLine);
                ligne.put(GROUPE_LIGNES, String.valueOf(currentGroup));
                collection.add(ligne);
                compteurLine++;
            }
        }

        // On complète la dernière page avec des lignes vides
        for (int i = 0; i < linesToAdd; i++) {
            Map<String, Object> ligne = new HashMap<String, Object>();
            ligne.put(NO_POSTE_TRAVAIL, "");
            ligne.put(NOM_PRENOM, "");
            ligne.put(DATE_NAISSANCE, "");
            ligne.put(CODE_PROF, "");
            ligne.put(VACANCES, "");
            ligne.put(GRATIFICATION, "");
            ligne.put(AJ, "");
            ligne.put(TAUX_COL123, "");
            ligne.put(TOTAL_COL123, null);
            ligne.put(APG_SM, null);
            // ligne.put(REMARQUES, "");
            ligne.put(GROUPE_LIGNES, String.valueOf(groups.get(compteurLine)));
            collection.add(ligne);
            compteurLine++;
        }

        setDataSource(collection);
    }

    @Override
    protected String getEMailObject() {
        return DocumentConstants.DECOMPTE_VIDE_COMPLEMENTAIRE_SUBJECT;
    }

    /**
     * définit la template a utiliser
     */
    @Override
    public String getJasperTemplate() {
        Decompte decompte = getCurrentElement().getDecompte();
        if (ConventionEbu.CONSTRUCTION_METALLIQUE.equals(decompte.getEmployeur().getConvention())) {
            return DocumentConstants.DECOMPTE_VIDE_COMPLEMENTAIRE_TEMPLATE;
        }
        return DocumentConstants.DECOMPTE_VIDE_COMPLEMENTAIRE_TEMPLATE;
    }

    @Override
    public String getNomDocumentForCataloguesTextes() {
        return DocumentConstants.DECOMPTE_VIDE_COMPLEMENTAIRE_CT_NAME;
    }
}
