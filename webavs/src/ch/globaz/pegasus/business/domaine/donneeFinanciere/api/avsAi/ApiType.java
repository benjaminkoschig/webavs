package ch.globaz.pegasus.business.domaine.donneeFinanciere.api.avsAi;

import ch.globaz.common.domaine.CodeSystemEnum;
import ch.globaz.common.domaine.CodeSystemEnumUtils;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.api.ApiDegre;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.api.ApiGenre;

public enum ApiType implements CodeSystemEnum<ApiType> {

    API_81("64012001", ApiGenre.INVALIDITE, ApiDegre.FAIBLE),
    API_82("64012002", ApiGenre.INVALIDITE, ApiDegre.MOYEN),
    API_83("64012003", ApiGenre.INVALIDITE, ApiDegre.GRAVE),
    API_84("64012004", ApiGenre.INVALIDITE, ApiDegre.FAIBLE),
    API_85("64012005", ApiGenre.VIELLESSE, ApiDegre.FAIBLE),
    API_86("64012006", ApiGenre.VIELLESSE, ApiDegre.MOYEN),
    API_87("64012007", ApiGenre.VIELLESSE, ApiDegre.GRAVE),
    API_88("64012008", ApiGenre.INVALIDITE, ApiDegre.MOYEN),
    API_89("64012015", ApiGenre.VIELLESSE, ApiDegre.FAIBLE),
    API_91("64012009", ApiGenre.INVALIDITE, ApiDegre.FAIBLE),
    API_92("64012010", ApiGenre.INVALIDITE, ApiDegre.MOYEN),
    API_93("64012011", ApiGenre.INVALIDITE, ApiDegre.GRAVE),
    API_95("64012012", ApiGenre.VIELLESSE, ApiDegre.FAIBLE),
    API_96("64012013", ApiGenre.VIELLESSE, ApiDegre.MOYEN),
    API_97("64012014", ApiGenre.VIELLESSE, ApiDegre.GRAVE);

    private String value;
    private ApiGenre apiGenre;
    private ApiDegre apiDegre;

    ApiType(String value, ApiGenre apiGenre, ApiDegre apiDegre) {
        this.value = value;
        this.apiDegre = apiDegre;
        this.apiGenre = apiGenre;
    }

    public static ApiType fromValue(String value) {
        return CodeSystemEnumUtils.valueOfById(value, ApiType.class);
    }

    @Override
    public String getValue() {
        return value;
    }

    public ApiGenre getApiGenre() {
        return apiGenre;
    }

    public ApiDegre getApiDegre() {
        return apiDegre;
    }
}
