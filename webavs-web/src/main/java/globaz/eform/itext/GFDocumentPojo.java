package globaz.eform.itext;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class GFDocumentPojo {
    private String nom;
    private String prenom;
    private String nss;
    private String dateNaissance;
}
