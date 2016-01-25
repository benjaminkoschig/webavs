package globaz.musca.db.gestionJourFerie;

import globaz.framework.translation.FWTranslation;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.musca.application.FAApplication;
import java.util.ArrayList;
import java.util.List;

public class FAGestionJourFerieUtil {

    public static final void addWeekends(String year) throws Exception {
        JACalendarGregorian calendrier = new JACalendarGregorian();
        JADate dateCourante = new JADate("01.01." + year);
        JADate dateFin = new JADate("31.12." + year);

        while (calendrier.compare(dateCourante, dateFin) != JACalendar.COMPARE_FIRSTUPPER) {
            if (calendrier.isWeekend(dateCourante)) {
                FAGestionJourFerie entityFerie = new FAGestionJourFerie();
                BSession session = (BSession) GlobazSystem.getApplication(FAApplication.DEFAULT_APPLICATION_MUSCA)
                        .newSession();
                session.connect("ciciglo", "glob4az");
                entityFerie.setSession(session);
                entityFerie.setDateJour(dateCourante.toString());
                entityFerie.setLibelle("week-end");
                entityFerie.add();
            }

            dateCourante = calendrier.addDays(dateCourante, 1);

        }
    }

    /**
     * Cette méthode permet de supprimer la valeur fictive ajoutée indirectement par la méthode getHtmlNullDomain()
     */
    public static final void cleanNullDomainIdIn(List<String> aList) {
        if (aList != null) {
            for (String domaineId : aList) {
                if ("-null-".equals(domaineId)) {
                    aList.remove(domaineId);
                    break;
                }
            }
        }
    }

    public static final String getDomaineFerieToString(ArrayList<String> domaineFerie, BSession session)
            throws Exception {
        StringBuffer texte = new StringBuffer();

        FWParametersSystemCodeManager mgrCodeSystem = FWTranslation.getSystemCodeList("MUDOMFERIE", session);
        FWParametersSystemCode codeSystem = null;

        for (int i = 1; i <= mgrCodeSystem.size(); i++) {
            codeSystem = (FWParametersSystemCode) mgrCodeSystem.getEntity(i - 1);

            if ((domaineFerie != null) && domaineFerie.contains(codeSystem.getIdCode())) {
                texte.append(codeSystem.getLibelle() + " / ");
            }
        }

        if (!JadeStringUtil.isBlankOrZero(texte.toString())) {
            return texte.toString().substring(0, texte.toString().length() - 3);
        }

        return texte.toString();
    }

    public static final String getHtmlCheckBoxDomaineFerie() throws Exception {
        return FAGestionJourFerieUtil.getHtmlCheckBoxDomaineFerie(new ArrayList<String>(), new BSession(
                FAApplication.DEFAULT_APPLICATION_MUSCA));
    }

    public static final String getHtmlCheckBoxDomaineFerie(ArrayList<String> domaineFerie, BSession session)
            throws Exception {
        StringBuffer html = new StringBuffer();
        FWParametersSystemCodeManager mgrCodeSystem = FWTranslation.getSystemCodeList("MUDOMFERIE", session);
        FWParametersSystemCode codeSystem = null;

        for (int i = 1; i <= mgrCodeSystem.size(); i++) {
            codeSystem = (FWParametersSystemCode) mgrCodeSystem.getEntity(i - 1);
            html.append(codeSystem.getLibelle() + " ");
            html.append("<input type='checkbox' name='domaineFerie'  value='" + codeSystem.getIdCode() + "' ");
            if ((domaineFerie != null) && domaineFerie.contains(codeSystem.getIdCode())) {
                html.append(" checked ");
            }
            html.append(" >");
            html.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
        }

        return html.toString();
    }

    /**
     * La méthode ci-dessous est une astuce pour forcer la création d'une collection domaineFerie dans la requête http
     * 
     * Ainsi, même si aucune case à cocher "domaineFerie" est cochée, la collection domaineFerie est créée dans la
     * requête http et la méthode setBeanProperties() de la classe JSPUtils appelle la méthode setDomaineFerie du
     * viewbean lié à l'écran
     * 
     * En effet, il est indispensable que lorsque l'utilisateur modifie un férié et désélectionne tous les domaines, la
     * méthode setDomaineFerie() soit appelée avec une collection vide afin de supprimer les domaines du férié
     * 
     */
    public static final String getHtmlNullDomain() {
        return "<input type='hidden' name='domaineFerie' value='-null-'/>";
    }

    public static final String giveNextWorkDay(String dateDebut, int nbJourToWait, String domaine, BSession session)
            throws Exception {
        JACalendarGregorian calendrier = new JACalendarGregorian();
        String nextWorkDay = calendrier.addDays(dateDebut, nbJourToWait);

        FAGestionJourFerieWithDomaineManager mgrJourFerieWithDomaine = new FAGestionJourFerieWithDomaineManager();
        mgrJourFerieWithDomaine.setSession(session);
        mgrJourFerieWithDomaine.setFromDateJour(dateDebut);
        mgrJourFerieWithDomaine.setToDateJour(nextWorkDay);
        mgrJourFerieWithDomaine.setInIdDomaine(domaine);

        int nbFerie = mgrJourFerieWithDomaine.getCount();
        int compteur = 0;
        while (nbFerie > 0) {
            nextWorkDay = calendrier.addDays(nextWorkDay, nbFerie);

            mgrJourFerieWithDomaine.setFromDateJour(calendrier.addDays(nextWorkDay, 1 - nbFerie));
            mgrJourFerieWithDomaine.setToDateJour(nextWorkDay);
            nbFerie = mgrJourFerieWithDomaine.getCount();
            compteur++;
            System.out.println(mgrJourFerieWithDomaine.getFromDateJour() + " - "
                    + mgrJourFerieWithDomaine.getToDateJour());
        }

        System.out.println("nb requetes : " + compteur);

        return nextWorkDay;
    }

    public static final boolean isWorkDay(String dateJour, String domaine, BSession session) throws Exception {
        FAGestionJourFerieWithDomaineManager mgrJourFerieWithDomaine = new FAGestionJourFerieWithDomaineManager();
        mgrJourFerieWithDomaine.setSession(session);
        mgrJourFerieWithDomaine.setForDateJour(dateJour);
        mgrJourFerieWithDomaine.setInIdDomaine(domaine);

        return mgrJourFerieWithDomaine.getCount() <= 0;
    }

}
