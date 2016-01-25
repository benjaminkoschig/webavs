package ch.globaz.utils.tests;

public enum DBToJSONDumpConfig {

    APPLICATION("FRAMEWORK"),
    MAXIMUM_NUMBER_ENTITIES("500"),
    NEW_PERSISTENCE_SEARCH_CLASS_SUFFIX("Search"),
    OLD_PERSISTENCE_MANAGER_CLASS_SUFFIX("Manager"),
    OUTPUT_JSON_FILE("c://dev//jsonFiles//"),
    PASS("glob4az"),
    USER("ccjuglo");

    public String value = null;

    DBToJSONDumpConfig(String value) {
        this.value = value;
    }

}
