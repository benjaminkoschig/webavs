package globaz.pyxis.web.token;

import ch.globaz.common.ws.token.Token;
import lombok.Data;

@Data
public class PYTokenImpl implements Token {
    private String dateDemande;
    private String timeDemande;
    private String timeStampGedo;

    // TODO: Delete the getters/setters since @Data should be adding them implicitly (I guess the compiler is messing with me ?)

    @Override
    public Token setUserId(String userId) {
        return null;
    }

    @Override
    public String getUserId() {
        return null;
    }

    @Override
    public Token setLangue(String langue) {
        return null;
    }

    @Override
    public String getLangue() {
        return null;
    }

    @Override
    public Token setEmail(String email) {
        return null;
    }

    @Override
    public String getEmail() {
        return null;
    }
}
