package globaz.eform.itext;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
public class GEDocumentPojo {

    @Getter
    @Setter
    private String nom;
    @Getter
    @Setter
    private String prenom;
    @Getter
    @Setter
    private String nss;
    @Getter
    @Setter
    private String dateNaissance;



}
