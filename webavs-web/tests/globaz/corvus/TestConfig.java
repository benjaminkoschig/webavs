package globaz.corvus;

import globaz.corvus.application.REApplication;

/**
 * Le but de cette classe est d'avoir un point central pour la configuration des tests JUnit. A terme,
 * 
 * @author lga
 */
public class TestConfig {

    public enum Config {
        CCJU_QA("CCVD QA", "globazf", "globazf"),
        CCVD_QA("CCJU QA", "ccjuglo", "glob4az"),
        CICI_QA("CICI QA", "ciciglo", "glob4az");

        private String configName;

        private String password;
        private String user;

        private Config(String configName, String user, String password) {
            this.configName = configName;
            this.user = user;
            this.password = password;
        }

        public String getApplicationName() {
            return REApplication.DEFAULT_APPLICATION_CORVUS;
        }

        public final String getConfigName() {
            return configName;
        }

        public final String getUserName() {
            return user;
        }

        public final String getUserPassword() {
            return password;
        }
    }

    private static Config defaultConfig = Config.CICI_QA;

    public static Config getDefaultConfig() {
        return TestConfig.defaultConfig;
    }

}
