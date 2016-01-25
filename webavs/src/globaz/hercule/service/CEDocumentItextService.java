package globaz.hercule.service;

import globaz.babel.api.ICTDocument;
import globaz.babel.api.ICTListeTextes;
import globaz.babel.api.ICTTexte;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.globall.db.BSession;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.hercule.utils.CEUtils;
import globaz.jade.client.util.JadeStringUtil;

/**
 * Class permettant la gestion des document itext
 * 
 * @author SCO
 * @since 29 juil. 2010
 */
public class CEDocumentItextService {

    /**
     * Permet le calcul de l'année de départ
     * 
     * @param annee
     * @param dateDebutAffiliation
     * @return
     * @throws JAException
     */
    public static String calculAnneDepart(String annee, String dateDebutAffiliation) throws JAException {

        JADate date = new JADate(dateDebutAffiliation);
        int int_anneeDebutAffiliation = date.getYear();
        String anneeCalculee = "" + int_anneeDebutAffiliation;

        int int_annee = CEUtils.transformeStringToInt(annee) - 3;

        if (int_annee >= int_anneeDebutAffiliation) {
            anneeCalculee = "" + int_annee;
        }

        return anneeCalculee;
    }

    /**
     * Permet le formatage des textes du catalogue de texte pour les documents iText.<BR>
     * Il permet de remplacer toutes les occurences {x} par l'argument [x] correspondant.
     * 
     * @param message
     * @param args
     * @return
     */
    public static String formatMessage(String message, String[] args) {

        StringBuffer buffer = new StringBuffer(message);

        // doubler les guillemets simples si necessaire
        // for (int idChar = 0; idChar < buffer.length(); ++idChar) {
        // if ((buffer.charAt(idChar) == '\'')
        // && ((idChar == (buffer.length() - 1)) || (buffer.charAt(idChar + 1) != '\''))) {
        // buffer.insert(idChar, '\'');
        // ++idChar;
        // }
        // }
        // remplacer les arguments null par chaine vide
        for (int idArg = 0; idArg < args.length; ++idArg) {
            if (args[idArg] == null) {
                args[idArg] = "";
            }
        }

        // Remplace les {x} par les args[x]
        StringBuffer messageFormat = new StringBuffer();
        boolean isTexte = true;
        String param = "";
        for (int i = 0; i < buffer.length(); i++) {

            if ((buffer.charAt(i) != '{') && isTexte && (buffer.charAt(i) != '}')) {
                messageFormat.append(buffer.charAt(i));
            } else if (!isTexte && (buffer.charAt(i) != '}')) {
                param += buffer.charAt(i);
            } else if (buffer.charAt(i) == '}') {
                isTexte = true;
                if (!JadeStringUtil.isBlank(param)) {
                    int numArg = Integer.parseInt(param);
                    if (numArg < args.length) {
                        messageFormat.append(args[numArg]);
                    }
                }
            } else if (buffer.charAt(i) == '{') {
                isTexte = false;
                param = "";
            }
        }

        return messageFormat.toString();
    }

    /**
     * Récupère le texte du catalogue en fonction du niveau et de la position, et remplace les {n} par les textes passés
     * dans le tableau d'objet "args"
     * 
     * @param catalogue
     *            Le catlague des textes
     * @param niveau
     * @param position
     * @param args
     * @return
     * @throws FWIException
     */
    public static String getTexte(ICTDocument catalogue, int niveau, int position, String[] args) throws FWIException {
        String texte;
        try {
            if (args != null) {
                texte = CEDocumentItextService.formatMessage(catalogue.getTextes(niveau).getTexte(position)
                        .getDescription(), args);
            } else {
                texte = catalogue.getTextes(niveau).getTexte(position).getDescription();
            }
            return texte;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Récupère le texte du catalogue en fonction du niveau , et remplace les {n} par les textes passés dans le tableau
     * d'objet "args"
     * 
     * @param catalogue
     *            Le catlague des textes
     * @param niveau
     * @param args
     * @return
     * @throws FWIException
     */
    public static String getTexte(ICTDocument catalogue, int niveau, String[] args) throws FWIException {
        String resString = "";
        ICTTexte texte = null;
        try {

            ICTListeTextes listTexte = catalogue.getTextes(niveau);

            if (listTexte != null) {
                for (int i = 0; i < listTexte.size(); i++) {
                    texte = listTexte.getTexte(i + 1);
                    if (i + 1 < listTexte.size()) {
                        resString = resString.concat(texte.getDescription() + "\n\n");
                    } else {
                        resString = resString.concat(texte.getDescription());
                    }
                }
            }

            if (args != null) {
                resString = CEDocumentItextService.formatMessage(resString, args);
            }

            if (JadeStringUtil.isEmpty(resString)) {
                return " ";
            } else {
                return resString;
            }

        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Récupération du catalogue de texte
     * 
     * @param session
     * @param langueIso
     * @param csDomaine
     * @param csTypeDocument
     * @return
     * @throws FWIException
     */
    public static ICTDocument retrieveCatalogue(BSession session, String langueIso, String csDomaine,
            String csTypeDocument) throws FWIException {
        return CEDocumentItextService.retrieveCatalogue(session, langueIso, csDomaine, csTypeDocument, null, null);
    }

    /**
     * Récupération du catalogue de texte
     * 
     * @throws FWIException
     */
    public static ICTDocument retrieveCatalogue(BSession session, String langueIso, String csDomaine,
            String csTypeDocument, String idDocument, String idDocumentDefault) throws FWIException {

        ICTDocument catalogue = null;

        try {

            // Recherche le catalogue
            ICTDocument helper = (ICTDocument) session.getAPIFor(ICTDocument.class);

            helper.setCsDomaine(csDomaine); // domaine avs
            helper.setCsTypeDocument(csTypeDocument); // pour le type de
            // catalogue
            helper.setCodeIsoLangue(langueIso); // dans la langue du salarié
            helper.setActif(Boolean.TRUE); // actif

            // Chois par numero de document
            if (!JadeStringUtil.isBlank(idDocumentDefault)) {
                helper.setIdDocument(idDocumentDefault);

                // Récupération du document choisi par'utilisateur
                if (!JadeStringUtil.isBlank(idDocument)) {
                    helper.setIdDocument(idDocument);
                }
            } else {
                // Sinon on prend celui spécifié par defaut dans le catalogue de
                // texte
                helper.setDefault(Boolean.TRUE); // et par défaut
            }

            // charger le catalogue de texte
            ICTDocument[] candidats = helper.load();

            if ((candidats != null) && (candidats.length > 0)) {
                catalogue = candidats[0];
            }
        } catch (Exception e) {
            catalogue = null;
        }

        if (catalogue == null) {
            throw new FWIException(session.getLabel("CATALOGUE_INTROUVABLE"));
        }

        return catalogue;
    }

    /**
     * Constructeur de CEDocumentItextService
     */
    protected CEDocumentItextService() {
        throw new UnsupportedOperationException(); // prevents calls from
        // subclass
    }
}
