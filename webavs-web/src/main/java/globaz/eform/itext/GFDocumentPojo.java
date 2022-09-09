package globaz.eform.itext;

import globaz.eform.vb.envoi.GFEnvoiViewBean;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
public class GFDocumentPojo {

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

    public static GFDocumentPojo buildFromGFEnvoiViewBean(GFEnvoiViewBean bean) {
        return GFDocumentPojo.builder()
                .nom(bean.getNomAssure())
                .prenom(bean.getPrenomAssure())
                .nss(bean.getNss())
                .dateNaissance(bean.getDateNaissance()).build();
    }

}
