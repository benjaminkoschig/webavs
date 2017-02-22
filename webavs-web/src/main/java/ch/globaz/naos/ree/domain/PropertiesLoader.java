package ch.globaz.naos.ree.domain;

import java.io.IOException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ch.globaz.naos.ree.domain.pojo.ProcessProperties;

public class PropertiesLoader {

    private static final String PROP_FILENAME = "/TransmissionREE.properties";
    private static final String PROP_OTHER = "header.other";
    private static final String PROP_PHONE = "header.phone";
    private static final String PROP_EMAIL = "header.email";
    private static final String PROP_NAME = "header.name";
    private static final String PROP_DEPARTEMENT = "header.department";
    private static final String PROP_PAQUET = "process.paquet";
    private static final String PROP_UNIT_VALIDATION = "process.validation";
    private static final String PROP_RECIPIENT_ID = "message.header.recipient.id";

    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public ProcessProperties load() {
        Properties properties = new Properties();
        try {
            properties.load(getClass().getResourceAsStream(PROP_FILENAME));
        } catch (IOException e) {
            throw new IllegalArgumentException("No properties find, please check " + PROP_FILENAME);
        }
        ProcessProperties prop = new ProcessProperties();
        prop.setDepartment(properties.getProperty(PROP_DEPARTEMENT));
        prop.setName(properties.getProperty(PROP_NAME));
        prop.setPhone(properties.getProperty(PROP_PHONE));
        prop.setOther(properties.getProperty(PROP_OTHER));
        prop.setEmail(properties.getProperty(PROP_EMAIL));
        prop.setTailleLot(Integer.valueOf(properties.getProperty(PROP_PAQUET)));
        prop.setValidation(propertieBooleanTrueFalse(properties.getProperty(PROP_UNIT_VALIDATION)));
        prop.setRecipientId(properties.getProperty(PROP_RECIPIENT_ID));

        validate(prop);
        return prop;
    }

    protected boolean propertieBooleanTrueFalse(String propValue) {
        if (propValue == null || propValue.isEmpty()) {
            throw new IllegalArgumentException("Properties [Validation] value cannot be empty");
        }
        if (propValue.equalsIgnoreCase("TRUE")) {
            return true;
        } else if (propValue.equalsIgnoreCase("FALSE")) {
            return false;
        } else {
            throw new IllegalArgumentException("Properties [Validation] value must be true|false");
        }
    }

    protected void validate(ProcessProperties pojo) throws IllegalArgumentException {
        // name, phone, mail et paquet obligatoire
        if (pojo.getName() == null || pojo.getName().isEmpty()) {
            throw new IllegalArgumentException("Properties [Name] value cannot be empty");
        }
        if (pojo.getPhone() == null || pojo.getPhone().isEmpty()) {
            throw new IllegalArgumentException("Properties [Phone] value cannot be empty");
        }
        if (pojo.getEmail() == null || pojo.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Properties [Email] value cannot be empty");
        }
        if (pojo.getTailleLot() < 1) {
            throw new IllegalArgumentException("Properties [TailleLot] value cannot be zero or negative");
        }
        // mail is a valid mail
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(pojo.getEmail());
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Properties [Email] value not valid");
        }
        if (!pojo.getPhone().matches("[0-9]{10,20}")) {
            throw new IllegalArgumentException("Properties [Phone] value must be numeric between 10 and 20 digits");
        }

        if (pojo.getRecipientId() == null || pojo.getRecipientId().isEmpty()) {
            throw new IllegalArgumentException("Properties [getRecipientId] value cannot be empty");
        }
    }

}
