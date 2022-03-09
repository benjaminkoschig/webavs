package globaz.osiris.eservices.dto;

import ch.globaz.orion.ws.enums.Role;
import ch.globaz.vulpecula.external.models.pyxis.CodeLangue;
import globaz.jade.client.util.JadeDateUtil;
import globaz.osiris.api.APIOperation;
import globaz.osiris.db.comptes.extrait.CAExtraitCompteManager;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ESValidateDTO {

    public static final int MAX_PERIOD = 60; // max 5 ans de données autorisées;
    public static final List<String> validRole = Stream.of(Role.values()).map(Enum::name).collect(Collectors.toList());
    public static final List<String> validLangue = Arrays.asList(CodeLangue.FR.getCodeIsoLangue(), CodeLangue.DE.getCodeIsoLangue(), "");
    public static final List<String> validSelectionSections = Arrays.asList(CAExtraitCompteManager.SOLDE_ALL,CAExtraitCompteManager.SOLDE_CLOSED, CAExtraitCompteManager.SOLDE_OPEN);
    public static final List<String> validSelectionTris = Arrays.asList(CAExtraitCompteManager.ORDER_BY_DATE_COMPTABLE,CAExtraitCompteManager.ORDER_BY_DATE_VALEUR, CAExtraitCompteManager.ORDER_BY_IDSECTION);
    public static final List<String> validOperation = Arrays.asList(CAExtraitCompteManager.FOR_ALL_IDTYPEOPERATION, APIOperation.CAAUXILIAIRE, APIOperation.CAAUXILIAIRE_PAIEMENT, APIOperation.CAECRITURE, APIOperation.CAECRITURECOMPENSATION, APIOperation.CAECRITURELISSAGE, APIOperation.CAOPERATIONBULLETINNEUTRE, APIOperation.CAOPERATIONCONTENTIEUX, APIOperation.CAOPERATIONCONTENTIEUXAQUILA, APIOperation.CAOPERATIONORDRERECOUVREMENT, APIOperation.CAOPERATIONORDREVERSEMENT, APIOperation.CAOPERATIONORDREVERSEMENTAVANCE, APIOperation.CAPAIEMENT, APIOperation.CAPAIEMENTBVR, APIOperation.CAPAIEMENTETRANGER, APIOperation.CARECOUVREMENT, APIOperation.CAVERSEMENT, APIOperation.CAVERSEMENTAVANCE);

    public static Boolean isValid(String role, String langue, String selectionSections, String selectionTris, String operation, String startPeriod, String endPeriod) {
        return ESValidateDTO.validRole.contains(role)
            && ESValidateDTO.validLangue.contains(langue.toLowerCase())
            && ESValidateDTO.validSelectionSections.contains(selectionSections.toLowerCase())
            && ESValidateDTO.validSelectionTris.contains(selectionTris.toLowerCase())
            && ESValidateDTO.validOperation.contains(operation.toLowerCase())
            && JadeDateUtil.getNbMonthsBetween(startPeriod, endPeriod) <= ESValidateDTO.MAX_PERIOD;
    }
}
