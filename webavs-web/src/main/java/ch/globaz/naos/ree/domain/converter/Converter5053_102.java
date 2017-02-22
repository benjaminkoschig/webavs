package ch.globaz.naos.ree.domain.converter;

import globaz.naos.util.AFIDEUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ree.ch.admin.bfs.xmlns.bfs_5053_000102._1.CombinationType;
import ree.ch.ech.xmlns.ech_0097._1.UidOrganisationIdCategorieType;
import ree.ch.ech.xmlns.ech_0097._1.UidStructureType;
import ch.globaz.naos.ree.domain.pojo.Pojo5053_102;
import ch.globaz.naos.ree.tools.InfoCaisse;

public class Converter5053_102 implements Converter<Pojo5053_102, CombinationType> {
    private static final Logger LOG = LoggerFactory.getLogger(Converter5053_102.class);
    private InfoCaisse currentCaisse;

    public Converter5053_102(InfoCaisse infoCaisse) {
        currentCaisse = infoCaisse;

    }

    @Override
    public CombinationType convert(Pojo5053_102 businessMessage) throws REEBusinessException {
        return convertPojos(businessMessage);
    }

    private CombinationType convertPojos(Pojo5053_102 pojo) throws REEBusinessException {
        CombinationType combiType = new CombinationType();

        // lien
        combiType.setCombinationCode(translateToCombinationCode(pojo.getTypeLien()));

        combiType.setAccountNumberU(pojo.getNumeroAffilieP());
        combiType.setCompensationOfficeU(ConverterUtils.formatCodeCaisse(currentCaisse.getNumeroCaisse() + "."
                + currentCaisse.getNumeroAgenceFormate()));
        if (pojo.getNumeroIdeP() != null && !pojo.getNumeroIdeP().isEmpty()) {
            UidStructureType uidStructureType = new UidStructureType();
            uidStructureType.setUidOrganisationIdCategorie(UidOrganisationIdCategorieType.CHE);
            uidStructureType.setUidOrganisationId(intUnformatedNumIde(pojo.getNumeroIdeP()));
            combiType.setUidU(uidStructureType);
        }

        combiType.setAccountNumberO(pojo.getNumeroAffilieL());
        combiType.setCompensationOfficeO(ConverterUtils.formatCodeCaisse(currentCaisse.getNumeroCaisse() + "."
                + currentCaisse.getNumeroAgenceFormate()));
        if (pojo.getNumeroIdeL() != null && !pojo.getNumeroIdeL().isEmpty()) {
            UidStructureType uidStructureType = new UidStructureType();
            uidStructureType.setUidOrganisationIdCategorie(UidOrganisationIdCategorieType.CHE);
            uidStructureType.setUidOrganisationId(intUnformatedNumIde(pojo.getNumeroIdeL()));
            combiType.setUidO(uidStructureType);
        }
        return combiType;
    }

    protected static String translateToCombinationCode(String codeCS) {
        int value = 0;
        try {
            value = Integer.valueOf(codeCS);
        } catch (NumberFormatException e) {
            LOG.debug("Mapping [translateToCombinationCode] Impossible de récupérer la valeur numérique du CS {} : {}",
                    codeCS, e.getMessage());
        }
        switch (value) {
            case 819001:// Personnel déclaré par
                return "06";
            case 819006:// Est succursale de
                return "05";
            case 819007:// Est associé de
                return "04";
            default:
                return "99";
        }
    }

    private static int intUnformatedNumIde(String numIde) {
        String chiffreIde = AFIDEUtil.giveMeNumIdeUnformatedWithoutPrefix(numIde);

        return Integer.parseInt(chiffreIde);

    }
}
