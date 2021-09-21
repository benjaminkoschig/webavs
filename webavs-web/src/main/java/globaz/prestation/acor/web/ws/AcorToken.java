package globaz.prestation.acor.web.ws;

public interface AcorToken {
    AcorToken setUserId(String userId);

    String getUserId();

    AcorToken setLangue(String langue);

    String getLangue();

    AcorToken setEmail(String email);

    String getEmail();

}
