package ch.globaz.al.business.services.decision;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.ArrayList;
import java.util.HashMap;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * Service d'impression des d�cisions
 * 
 * @author JER
 */
public interface DecisionService extends JadeApplicationService {
    /**
     * m�thode qui charge toutes les donn�es pour la cr�ation des d�cisions
     * 
     * @param dossier
     *            Dossier de la d�cision
     * @param dateImpression
     *            Date d'impression de la d�cision
     * @return ArrayList
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public HashMap<String, ArrayList<DocumentData>> loadData(DossierComplexModel dossier, String dateImpression,
            String langueDocument, HashMap<String, String> userInfo) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Fonctionnement identique � {@link DecisionService#loadData(DossierComplexModel, String, String, HashMap)} mais
     * permet la gestion du texte libre selon les conditions suivantes :
     * 
     * <TABLE>
     * <TR>
     * <TD><B>Coche gestion texte libre</B></TD>
     * <TD><B>Texte mentionn&eacute; dans nouvel &eacute;cran</B></TD>
     * <TD><B>Texte mentionn&eacute; dans &eacute;cran AL0016</B></TD>
     * <TD><B>R&eacute;sultat</B></TD>
     * </TR>
     * <TR>
     * <TD>oui</TD>
     * <TD>non</TD>
     * <TD>non</TD>
     * <TD>D&eacute;cision sans texte ajout&eacute;</TD>
     * </TR>
     * <TR>
     * <TD>oui</TD>
     * <TD>oui</TD>
     * <TD>non</TD>
     * <TD>D&eacute;cision avec ajout du texte figurant dans le nouvel &eacute;cran</TD>
     * </TR>
     * <TR>
     * <TD>oui</TD>
     * <TD>non</TD>
     * <TD>oui</TD>
     * <TD>D&eacute;cision avec ajout du texte figurant dans le dossier</TD>
     * </TR>
     * <TR>
     * <TD>oui</TD>
     * <TD>oui</TD>
     * <TD>oui</TD>
     * <TD>D&eacute;cision avec ajout du texte figurant dans le nouvel &eacute;cran puis du texte figurant dans le
     * dossier</TD>
     * </TR>
     * <TR>
     * <TD>non</TD>
     * <TD>non</TD>
     * <TD>non</TD>
     * <TD>D&eacute;cision sans texte ajout&eacute;</TD>
     * </TR>
     * <TR>
     * <TD>non</TD>
     * <TD>oui</TD>
     * <TD>non</TD>
     * <TD>D&eacute;cision avec ajout du texte figurant dans le nouvel &eacute;cran</TD>
     * </TR>
     * <TR>
     * <TD>non</TD>
     * <TD>non</TD>
     * <TD>oui</TD>
     * <TD>D&eacute;cision sans texte ajout&eacute;</TD>
     * </TR>
     * <TR>
     * <TD>non</TD>
     * <TD>oui</TD>
     * <TD>oui</TD>
     * <TD>D&eacute;cision avec ajout du texte figurant dans le nouvel &eacute;cran</TD>
     * </TR>
     * </TABLE>
     * 
     * @param dossier
     *            Dossier de la d�cision
     * @param dateImpression
     *            Date d'impression de la d�cision
     * @return ArrayList
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */

    public HashMap<String, ArrayList<DocumentData>> loadData(DossierComplexModel dossier, String dateImpression,
            String langueDocument, HashMap<String, String> userInfo, String texteLibre, boolean gestionTexteLibre)
            throws JadeApplicationException, JadePersistenceException;

    // mandat d0113
    public HashMap<String, ArrayList<DocumentData>> loadData(DossierComplexModel dossier, String dateImpression,
            String langueDocument, HashMap<String, String> userInfos, boolean gestionCopie, String texteLibre,
            boolean gestionTexteLibre) throws JadeApplicationException, JadePersistenceException;
}
