package ch.globaz.eform.businessimpl.services.sedex.handlers;

import ch.globaz.common.validation.ValidationResult;
import globaz.globall.db.BSession;

import java.util.Map;

public interface GFSedexhandler {
    void setData(Map<String, Object> externData) throws RuntimeException;
    void create(ValidationResult result);
    void update(ValidationResult result);
    void setMessage(Object message);

    void setSession(BSession session);
}
