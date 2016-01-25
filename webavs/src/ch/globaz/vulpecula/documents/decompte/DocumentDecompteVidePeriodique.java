package ch.globaz.vulpecula.documents.decompte;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.decompte.Absence;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;

/**
 * Création du document décompte vide pour envoi, lors de la création de décomptes vides.
 * 
 * @since WebBMS 1.0
 */
public class DocumentDecompteVidePeriodique extends DocumentDecompteVide {
    private static final long serialVersionUID = 2002626138123336066L;

    private static final String NO_POSTE_TRAVAIL = "COL_1";
    private static final String NOM_PRENOM = "COL_2";
    private static final String DATE_NAISSANCE = "COL_3";
    private static final String CODE_PROF = "COL_4";
    private static final String NB_HEURES = "COL_5";
    private static final String SALAIRE_HORAIRE = "COL_6";
    private static final String TOTAL_SALAIRES = "COL_7";
    private static final String TAUX_CONTRIB = "COL_8";
    private static final String REMARQUE = "COL_9";

    private static final int NB_LINE_MAX = 19;

    /**
     * Constructeur
     */
    public DocumentDecompteVidePeriodique() throws Exception {
        super();
    }

    /**
     * Constructeur, on l'on passe les décomptes que l'on désire imprimer
     * 
     * @param decomptes List<Decompte>
     * @throws Exception
     */
    public DocumentDecompteVidePeriodique(final DecompteContainer decompteContainer) throws Exception {
        super(decompteContainer, DocumentConstants.DECOMPTE_VIDE_PERIODIQUE_FILENAME,
                DocumentConstants.DECOMPTE_VIDE_PERIODIQUE_TYPE_NUMBER);
    }

    @Override
    protected void fillDataSourceSalaire() {
        Collection<Map<String, Object>> collection = new ArrayList<Map<String, Object>>();

        Decompte decompte = getCurrentElement().getDecompte();
        int linesNumber = decompte.getLignes().size();
        for (DecompteSalaire decompteSalaire : decompte.getLignes()) {
            Map<String, Object> ligne = new HashMap<String, Object>();
            ligne.put(NO_POSTE_TRAVAIL, decompteSalaire.getIdPosteTravail());
            ligne.put(NOM_PRENOM, decompteSalaire.getNomPrenomTravailleur());
            ligne.put(DATE_NAISSANCE, decompteSalaire.getDateNaissanceTravailleur());
            ligne.put(CODE_PROF, getCode(decompteSalaire.getQualificationPoste()));

            if (decompte.isReceptionnable()) {
                ligne.put(NB_HEURES, "");
                ligne.put(SALAIRE_HORAIRE, "");
                ligne.put(TOTAL_SALAIRES, null);
                ligne.put(REMARQUE, "");
            } else {
                ligne.put(NB_HEURES, String.valueOf(decompteSalaire.getHeures()));
                ligne.put(SALAIRE_HORAIRE, decompteSalaire.getSalaireHoraireAsValue());
                ligne.put(TOTAL_SALAIRES, decompteSalaire.getSalaireTotal().getBigDecimalValue());

                StringBuilder absences = new StringBuilder();
                int i = 0;
                for (Absence absence : decompteSalaire.getAbsences()) {
                    if (i != 0) {
                        absences.append(", ");
                    }
                    i++;
                    absences.append(getCodeLibelle(absence.getTypeAsValue()));
                }

                ligne.put(REMARQUE, absences.toString());
            }
            ligne.put(TAUX_CONTRIB, decompteSalaire.getTauxContribuableForCaissesSocialesAsValue());
            collection.add(ligne);
        }

        int lignesARajouter = linesToAdd(linesNumber, NB_LINE_MAX);

        // On complète la dernière page avec des lignes vides
        for (int i = 0; i < lignesARajouter; i++) {
            Map<String, Object> ligne = new HashMap<String, Object>();
            ligne.put(NO_POSTE_TRAVAIL, "");
            ligne.put(NOM_PRENOM, "");
            ligne.put(DATE_NAISSANCE, "");
            ligne.put(CODE_PROF, "");
            ligne.put(NB_HEURES, "");
            ligne.put(SALAIRE_HORAIRE, "");
            ligne.put(TOTAL_SALAIRES, null);
            ligne.put(TAUX_CONTRIB, "");
            ligne.put(REMARQUE, "");
            collection.add(ligne);
        }

        setDataSource(collection);
    }

    @Override
    protected String getEMailObject() {
        return DocumentConstants.DECOMPTE_VIDE_PERIODIQUE_SUBJECT;
    }

    /**
     * définit la template a utiliser
     */
    @Override
    public String getJasperTemplate() {
        return DocumentConstants.DECOMPTE_VIDE_PERIODIQUE_TEMPLATE;
    }

    @Override
    public String getNomDocumentForCataloguesTextes() {
        return DocumentConstants.DECOMPTE_VIDE_PERIODIQUE_CT_NAME;
    }
}
