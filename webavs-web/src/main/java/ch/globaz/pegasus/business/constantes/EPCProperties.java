package ch.globaz.pegasus.business.constantes;

import java.lang.reflect.Type;
import java.util.Map;
import ch.globaz.common.properties.CommonPropertiesUtils;
import ch.globaz.common.properties.IProperties;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.pegasus.utils.PCApplicationUtil;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

public enum EPCProperties implements IProperties {
    ALLOCATION_NOEL("pegasus.allocation.noel"),
    BLOCK_IF_AMAL_INCOMPLETE_FOR_DECISION("pegasus.amal.check.blockifincomplete"),
    CANTON_CAISSE("pegasus.cantonCaisse"),
    CHECK_AMAL_FOR_DECISION_ENABLE("pegasus.amal.check.enable"),
    DISPLAY_RENTES_DECISION_ADAPTATION("pegasus.decision.adaptation.displayrentes"),
    DECISION_ANNEXES_CARTE_LEGITIM_DEFAUT("pegasus.decision.annexe.cartelegitim.defaut"),
    DECISION_ANNEXES_EXO_TELERESEAU_DEFAUT("pegasus.decision.annexe.exotelereseau.defaut"),
    DECISION_ANNEXES_GUIDEREDUCTIONCARTELEGITM_DEFAUT("pegasus.decision.annexe.guidereductioncartelegitim.defaut"),
    DECISION_ANNEXES_IMPOTS_CHIEN_DEFAUT("pegasus.decision.annexe.impotschiens.defaut"),
    DECISION_ANNEXES_NOTICE_DEFAUT("pegasus.decision.annexe.notice.defaut"),
    DECISION_COPIE_AGENCE_AVS("pegasus.decision.copie.agenceavs"),
    DECISION_COPIE_HOME_DEFAUT("pegasus.decision.copie.home.defaut"),
    DECISION_COPIE_HOME_PLANCALCUL("pegasus.decision.copie.home.plancalcul"),
    DECISION_COPIE_PROSENECTUTE_DEFAUT("pegasus.decision.copie.prosenectute.defaut"),
    DECISION_COPIE_PROSENECTUTE_IDTIERS("pegasus.decision.copie.prosenectute.idtiers"),
    DECISION_COPIE_SERVICES_SOCIAUX_DEFAUT("pegasus.decision.copie.servicessociaux.defaut"),
    DECISION_COPIE_SERVICES_SOCIAUX_IDTIERS("pegasus.decision.copie.servicesociaux.idtiers"),
    DECISION_COPIE_SERVICES_SOCIAUX_PAGEGARDE("pegasus.decision.copie.servicesociaux.pagegarde"),
    DECISION_COPIE_SPAS_AUTO("pegasus.decision.copie.spas.auto"),
    DECISION_COPIE_SPAS_IDTIERS("pegasus.decision.copie.spas.idtiers"),
    DECISION_TRANSFERT_AGENCESAVS_CODE("pegasus.decision.transfert.agenceavs.code"),
    DECISION_TRANSFERT_ANNEXES_ASSURES("pegasus.decision.transfert.annexeassures"),
    DECISION_TRANSFERT_COPIE_AGENCEAVS("pegasus.decision.transfert.copie.agenceavs"),
    GESTION_ANNONCES_LAPRAMS("pegasus.droit.gestionAnnonceLAPRAMS"),
    GESTION_ANNONCES_LAPRAMS_SUFFIXE("pegasus.droit.annonceLAPRAMS.suffixe"),
    GESTION_COMMUNICATION_OCC("pegasus.droit.gestionCommunicationOCC"),
    GESTION_JOURS_APPOINTS("pegasus.droit.gestionJoursAppoint"),
    HOME_TIERS_OR_ADMIN("pegasus.home.tiersOrAdmin"),
    LIST_ID_CAISSE_REFUSANT("pegasus.rentes.transfert.idcaissesrefusant"),
    MONTH_BETWEEN("pegasus.revision.monthsBetween"),
    NUMERO_OFFICE_PC("pegasus.numeroOfficePC"),
    PUBLICATION_FTP_AUTO_DAC_PREVALIDATION("pegasus.decision.ftpauto.prevalidation"),
    PUBLICATION_FTP_AUTO_DAC_VALIDATION("pegasus.decision.ftpauto.validation"),
    DOMAINE_NOM_SERVICE_GED("domaine.nomService.ged"),
    PUT_THREAD_ERROR("addErrorInThread"),
    GED_PC_TARGET_NAME("pegasus.ged.targetName"),
    ADAPTATION_LEVEL_PASAGE_AVS_AI("pegasus.adaptation.level.passage.avsai"),
    LVPC("pegasus.loi.pc.vaud"),
    CHRYSAOR("pegasus.chrysaor.enabled"),
    COMMUNE_POLITIQUE_NUMERO_RUBRIQUE_PC("commune.politique.numero.rubrique.pc"),
    COMMUNE_POLITIQUE_NUMERO_RUBRIQUE_RFM("commune.politique.numero.rubrique.rfm"),
    MAILS_DEBUG("analyse.error.sendMail"),
    LOI_CANTONALE_PC("canton.loi.pc"),
    RPC_LOAD_PARTION_SIZE("rpc.load.partion.size"),
    RPC_DESTINATAIRE("rpc.message.header.recipient.id"),
    RPC_ELOFFICE("rpc.message.header.eloffice"),
    RPC_GROUPE_RESPONSABLE("rpc.groupresponsable"),
    RPC_LIMIT_DAY_GENERATION("rpc.limit.day.generation"),
    AFFICHAGE_FORCER_ANNULER("application.ForcerAnnuler.afficher");

    private String property;

    EPCProperties(String prop) {
        property = prop;
    }

    @Override
    public String getApplicationName() {
        return PCApplicationUtil.getInstance().getDefaultApplicationName();
    }

    @Override
    public Boolean getBooleanValue() throws PropertiesException {
        return CommonPropertiesUtils.getBoolean(this);
    }

    @Override
    public String getDescription() {
        return null;
    }

    public String getProperty() {
        return property;
    }

    @Override
    public String getPropertyName() {
        return property;
    }

    @Override
    public String getValue() throws PropertiesException {
        return CommonPropertiesUtils.getValue(this);
    }

    public int getValueInt() throws PropertiesException {
        return Integer.valueOf(CommonPropertiesUtils.getValue(this));
    }

    public Map<String, String> getValueJson() throws PropertiesException {
        Type type = new TypeToken<Map<String, String>>() {
        }.getType();
        Gson gson = new Gson();
        return gson.fromJson("{" + getValue() + "}", type);
    }
}
