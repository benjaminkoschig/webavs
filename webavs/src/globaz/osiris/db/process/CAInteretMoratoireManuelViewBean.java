package globaz.osiris.db.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.process.interetmanuel.CAProcessInteretMoratoireManuel;
import globaz.osiris.utils.CAUtil;

public class CAInteretMoratoireManuelViewBean extends CAProcessInteretMoratoireManuel implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public CAInteretMoratoireManuelViewBean() {
        super();
    }

    /**
     * Return le compte annexe en cours. Utile pour la présentation des informations à l'utilisateur.
     * 
     * @return
     */
    public CACompteAnnexe getCompteAnnexeInformation() {
        try {
            return (CACompteAnnexe) getSectionInformation().getCompteAnnexe();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Return le numéro de facturation des intérêts moratoires par défaut. Utile pour la présentation des informations à
     * l'utilisateur.
     * 
     * @return
     */
    public String getDefaultNumeroFactureGroupe() {
        try {
            return CAUtil.creerNumeroSectionUniquePourInteretMoratoire(getSession(), getTransaction(),
                    getCompteAnnexeInformation(), getSectionInformation().getIdTypeSection(), getSectionInformation()
                            .getYear(), getSectionInformation().getCategorieSection(), getSectionInformation()
                            .getIdExterne());
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Return la section en cours. Utile pour la présentation des informations à l'utilisateur.
     * 
     * @return
     */
    public CASection getSectionInformation() {
        try {
            CASection section = new CASection();
            section.setSession(getSession());

            section.setIdSection(getIdSection());

            section.retrieve(getTransaction());

            return section;
        } catch (Exception e) {
            return null;
        }
    }
}
