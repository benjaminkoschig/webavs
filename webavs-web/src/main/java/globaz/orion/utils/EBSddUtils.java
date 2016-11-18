package globaz.orion.utils;

import globaz.naos.db.releve.AFApercuReleve;
import globaz.naos.db.releve.AFApercuReleveLineFacturation;
import globaz.naos.translation.CodeSystem;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import ch.globaz.orion.ws.cotisation.DecompteMensuel;
import ch.globaz.orion.ws.cotisation.DecompteMensuelLine;
import ch.globaz.xmlns.eb.sdd.LigneDeDecompteEntity;

public final class EBSddUtils {

    private static final List<String> ALLOWED_MASSES_FOR_SDD = Arrays.asList(CodeSystem.TYPE_ASS_COTISATION_AVS_AI,
            CodeSystem.TYPE_ASS_COTISATION_AF, CodeSystem.TYPE_ASS_MATERNITE, CodeSystem.TYPE_ASS_COTISATION_AC,
            CodeSystem.TYPE_ASS_COTISATION_AC2);

    private static final List<String> ASSURANCE_AF = Arrays.asList(CodeSystem.TYPE_ASS_PC_FAMILLE,
            CodeSystem.TYPE_ASS_CPS_AUTRE, CodeSystem.TYPE_ASS_CPS_GENERAL);

    private static final List<String> ASSURANCE_FFPP = Arrays.asList(CodeSystem.TYPE_ASS_FFPP,
            CodeSystem.TYPE_ASS_FFPP_MASSE);

    private EBSddUtils() {
        // vide
    }

    public static DecompteMensuel prepareDataForEbusiness(DecompteMensuel decompte) {
        List<DecompteMensuelLine> lines = new ArrayList<DecompteMensuelLine>();

        for (DecompteMensuelLine decompteMensuelLine : decompte.getLinesDecompte()) {
            if (ALLOWED_MASSES_FOR_SDD.contains(decompteMensuelLine.getTypeCotisation())) {
                lines.add(decompteMensuelLine);
            }
        }

        decompte.addAllLines(lines);

        return decompte;
    }

    public static void computeDataForReleve(List<LigneDeDecompteEntity> lignes, AFApercuReleve releve) {

        BigDecimal masseAvs = BigDecimal.ZERO;
        BigDecimal masseAf = BigDecimal.ZERO;

        // REcherche de la masse avs et AF
        for (LigneDeDecompteEntity ligne : lignes) {
            if (CodeSystem.TYPE_ASS_COTISATION_AVS_AI.equals(String.valueOf(ligne.getTypeCotisationWebavs()))) {
                masseAvs = ligne.getNouvelleMasse();
            }
            if (CodeSystem.TYPE_ASS_COTISATION_AF.equals(String.valueOf(ligne.getTypeCotisationWebavs()))) {
                masseAf = ligne.getNouvelleMasse();
            }
        }

        for (int j = 0; j < releve.getCotisationList().size(); j++) {
            AFApercuReleveLineFacturation releveLine = releve.getCotisationList().get(j);

            if (ASSURANCE_AF.contains(releveLine.getTypeAssurance())) {
                releveLine.setMasse(masseAf.doubleValue());
            } else if (!ALLOWED_MASSES_FOR_SDD.contains(releveLine.getTypeAssurance())
                    && !ASSURANCE_FFPP.contains(releveLine.getTypeAssurance())) {
                releveLine.setMasse(masseAvs.doubleValue());
            } else {
                fillMasseForReleveLine(lignes, releveLine);
            }
        }
    }

    private static void fillMasseForReleveLine(List<LigneDeDecompteEntity> lignes,
            AFApercuReleveLineFacturation releveLine) {
        for (LigneDeDecompteEntity ligne : lignes) {
            int idAssuranceReleve = Integer.parseInt(releveLine.getCotisationId());
            if (idAssuranceReleve == ligne.getIdCotisationWebavs() && ligne.getNouvelleMasse() != null) {
                releveLine.setMasse(ligne.getNouvelleMasse().doubleValue());
            }
        }
    }
}
