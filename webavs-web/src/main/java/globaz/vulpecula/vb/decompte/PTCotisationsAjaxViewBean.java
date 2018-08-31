package globaz.vulpecula.vb.decompte;

import globaz.vulpecula.vb.PTAjaxDisplayViewBean;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.Validate;
import ch.globaz.vulpecula.web.views.decompte.CotisationCalculeeView;
import ch.globaz.vulpecula.web.views.decompte.CotisationCalculeeViewService;

public class PTCotisationsAjaxViewBean extends PTAjaxDisplayViewBean {
    private static final long serialVersionUID = 860802898439198369L;

    private String idDecompte;

    private Map<CotisationCalculeeView, List<CotisationCalculeeView>> cotisationCalculeesGroups;

    @Override
    public void retrieve() throws Exception {
        Validate.notEmpty(idDecompte);
        cotisationCalculeesGroups = CotisationCalculeeViewService.getInstance().getCotisationsCalculee(idDecompte);
    }

    public Map<CotisationCalculeeView, List<CotisationCalculeeView>> getCotisationCalculeesGroups() {
        return cotisationCalculeesGroups;
    }

    public void setIdDecompte(String idDecompte) {
        this.idDecompte = idDecompte;
    }
}
