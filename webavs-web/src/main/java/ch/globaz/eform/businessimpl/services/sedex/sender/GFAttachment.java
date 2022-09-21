package ch.globaz.eform.businessimpl.services.sedex.sender;

import lombok.Getter;
import lombok.Setter;

@Getter
public class GFAttachment {
    private String fileName;
    private String path;

    public GFAttachment(String fileName, String path) {
        this.fileName = fileName;
        this.path = path;
    }
}
