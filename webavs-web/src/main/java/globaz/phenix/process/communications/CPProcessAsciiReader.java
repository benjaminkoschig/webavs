/*
 * Créé le 15 juil. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.phenix.process.communications;

import globaz.framework.util.FWMessage;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAException;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.phenix.application.CPApplication;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

/**
 * <H1>Description</H1>
 * 
 * @author MMU
 *         <p>
 *         Cette classe lit un fichier texte contenant les informations sur des contribuables Et converti ces donnees
 *         dans un format xml generique a toutes les caisses de compensation Ce parser est specifique a la Caisse de
 *         Neuchatel
 *         </p>
 */

/*
 * @author mmu Pour implementer un nouveau ascii reader pour générer des récéption au format XML, il faut hériter cette
 * classe et implémenter la methode getReception ainsi que la classe interne (CPReception). La méthode EcrireEntete()
 * peut égallement être surchargée La classe interne est une représention des données lues, il faut implémenter la
 * methode lireEntree en s'aidant des méthodes endLine() et setField(). A partir des données lues il faut pouvoir
 * réécrire partout où cela est possible les getter qui sont appelés par ecrireEntree().
 */
public abstract class CPProcessAsciiReader extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Classe interne qui materialise l'entree d'une communication comme un Bean
     * 
     * @author mmu
     */
    protected abstract class CPReception {
        protected final static int MAX_LINE_LENGHT = 1404;

        protected final static int MIN_LINE_LENGHT = 1404;
        protected CPProcessAsciiReader asciiReader;

        // ~ Instance fields
        // --------------------------------------------------------------------------------------------

        /**
		 */
        protected LinkedList<String> errorMsg;

        /**
		 */
        protected LinkedList<String> infoMsg;

        /**
		 */
        protected int numEntree;

        /**
		 */
        protected int position;

        /**
		 */
        protected boolean valide;

        /**
		 */
        protected LinkedList<String> warningMsg;

        /**
         * Crée une nouvelle instance de la classe Communication.
         * 
         * @param nbEntreesLues
         * @throws Exception
         */
        public CPReception(CPProcessAsciiReader reader, int nbEntreesLues) {
            asciiReader = reader;
            numEntree = nbEntreesLues + 1;
            errorMsg = new LinkedList<String>();
            warningMsg = new LinkedList<String>();
            infoMsg = new LinkedList<String>();
            position = 0;
            valide = true;

        }

        // ~ Constructors
        // -----------------------------------------------------------------------------------------------

        /*
         * FIN DES CHAMPS INSERES DANS LE DOCUMENT XML
         */
        /**
         * Ajoute un message d'erreur à la communication et marque la communication comme étant invalide.
         * 
         * @param string
         */
        public void _addError(String string) {

            errorMsg.addFirst(string + " (pos:" + (position + 1) + " ligne:" + numEntree + ")");
            valide = false;
        }

        // ~ Methods
        // ----------------------------------------------------------------------------------------------------

        /*
         * Transforme le nombre en boolean Globaz de la manière suivante ceci dans le but de s'assurer de pouvoir
         * inserer la communication dans la BD Valeur possible 0 ou 1 @param valeur @param fieldName @return String
         */
        private final String _validateBoolean(String value, String fieldName, Boolean defaultValue) {
            try {
                if ("0".equalsIgnoreCase(value)) {
                    return value;
                } else if ("1".equalsIgnoreCase(value)) {
                    return value;
                } else {
                    addWarning(FWMessageFormat
                            .format(getSession().getLabel("PROCASCIIREAD_WARNING_BOOLEAN_NOT_VALID_REPLACED"), value,
                                    fieldName));
                    addWarning("Le boolean: " + value + " du champ " + fieldName + " a été formatté ainsi: "
                            + defaultValue.toString());
                }
                return defaultValue.toString();
            } catch (Exception e) {
                addWarning(FWMessageFormat.format(
                        getSession().getLabel("PROCASCIIREAD_WARNING_BOOLEAN_NOT_VALID_REPLACED"), value, fieldName));
                addWarning("Le boolean: " + value + " du champ " + fieldName + " a été formatté ainsi: "
                        + defaultValue.toString());
                return defaultValue.toString();
            }
        }

        /*
         * Vérifie que le champ est bien un nombre, ceci dans le but de s'assurer de pouvoir inserer la communication
         * dans la BD Le test est le même que celui effectué par le BEntity lors de l'insertion @param date @param
         * fieldName @return
         */
        private final String _validateBoolean(String value, String fieldName, String defaultValue) {
            try {
                Boolean boolValue = new Boolean(value);
                if (!boolValue.toString().equalsIgnoreCase(value)) {
                    // addWarning(FWMessageFormat.format(getSession().getLabel("PROCASCIIREAD_WARNING_BOOLEAN_NOT_VALID_REPLACED"),
                    // value, fieldName, boolValue.toString()));
                    // addWarning("Le boolean: "+value+" du champ "+fieldName+" a été formatté ainsi: "+
                    // boolValue.toString());
                }
                return boolValue.toString();
            } catch (Exception e) {
                // _addError(FWMessageFormat.format(getSession().getLabel("PROCASCIIREAD_WARNING_BOOLEAN_NOT_VALID_REPLACED"),
                // value, fieldName, defaultValue));
                this._addError("Le boolean: " + value + " du champ " + fieldName
                        + " n'est pas valide! Il a été remplacé par: " + defaultValue);
                valide = false;
                return defaultValue;
            }
        }

        /*
         * Vérifie que le champ est bien une date, ceci dans le but de s'assurer de pouvoir inserer la communication
         * dans la BD Le test est le même que celui effectué par le BEntity lors de l'insertion @param date
         * 
         * @param fieldName @return
         */
        private final String _validateDate(String date, String fieldName, String defaultDate) {
            try {
                BSessionUtil.checkDateGregorian(asciiReader.getSession(), date);
            } catch (Exception e) {
                try {
                    date = fromString(date, '/');
                } catch (JAException e1) {
                    this._addError(FWMessageFormat.format(
                            getSession().getLabel("PROCASCIIREAD_ERROR_DATE_NOT_VALID_REPLACED"), date, fieldName,
                            defaultDate));
                    this._addError("La date: " + date + " du champ " + fieldName
                            + " n'est pas valide! Elle a été remplacée par: " + defaultDate);
                    valide = false;
                    date = defaultDate;
                }

            }
            return date;
        }

        /*
         * Vérifie que le champ est bien un nombre, ceci dans le but de s'assurer de pouvoir inserer la communication
         * dans la BD Le test est le même que celui effectué par le BEntity lors de l'insertion @param date @param
         * fieldName @return
         */
        private final String _validateNumber(String number, String fieldName, String defaultNumber) {
            BigDecimal formattedNumber = null;
            // if (JAUtil.isDecimalEmpty(number)) {
            // return defaultNumber;
            // }
            try {
                if (number == null) {
                    return defaultNumber;
                }
                if (JadeStringUtil.isBlank(number) == true) {
                    return defaultNumber;
                }
                double val = Double.parseDouble(number);
                if (val == 0.0) {
                    return number;
                }
                formattedNumber = new BigDecimal(JANumberFormatter.deQuote(number.trim()));
            } catch (Exception e) {
                try {
                    number = montantNeg(number, '-');
                    formattedNumber = new BigDecimal(JANumberFormatter.deQuote(number.trim()));
                } catch (JAException e1) {
                    this._addError("Le nombre: " + number + " du champ " + fieldName
                            + " n'est pas valide! Il a été remplacé par: " + defaultNumber);
                    valide = false;
                    return defaultNumber;
                }

            }
            return formattedNumber.toString();
        }

        /**
         * Ajoute un message d'information à la communication. Ne marque pas la commuunication commu invalide
         * 
         * @param String
         */
        public void addInfo(String message) {
            infoMsg.addFirst(message);
        }

        /**
         * Ajoute un message d'avertissement à la communication. Ne marque pas la commuunication commu invalide
         * 
         * @param String
         */
        public void addWarning(String message) {
            warningMsg.addFirst(message + " (pos:" + (position + 1) + " ligne:" + numEntree + ")");
        }

        /**
         * @param indentation
         *            dans le document xml
         * @param Niveau
         *            du log niveau
         * @param ArrayList
         *            <String> de log
         */
        private void ajouterLog(int ind, String niveau, LinkedList<String> logMsg) throws IOException {
            String message = null;
            while (logMsg.size() != 0) {
                message = logMsg.removeFirst();
                CPProcessAsciiReader.this.ajouterText(ind + 1, CPProcessAsciiReader.DEBUT_ERROR_ITEM);
                ajouterChamp(ind + 2, CPProcessAsciiReader.ERROR_NIVEAU, niveau);
                ajouterChamp(ind + 2, CPProcessAsciiReader.ERROR_TEXT, message);
                CPProcessAsciiReader.this.ajouterText(ind + 1, CPProcessAsciiReader.FIN_ERROR_ITEM);
            }

        }

        /**
         * Ajoute une entree xml "communication" au fichier en cours d'écriture. Cette méthode que les champs soient
         * conformes à leur type (méthodes _validates) Ce sont les seuls test effectués sur les entrée
         * 
         * @return
         * @throws Exception
         */
        // TODO: Mapping à déterminer: Pas tous les champs reçus sont transposé,
        // ni tous les champs attendus dans la
        // spécification renseignés....
        public final boolean ecrireEntree() throws Exception {
            // throw new Exception("This class cannot be instanciated");
            CPProcessAsciiReader.this.ajouterText(1, CPProcessAsciiReader.DEBUT_ENTREE);
            // Zone communes
            ajouterChamp(2, CPProcessAsciiReader.GENRE_AFFILIE, getGenreAffilie());
            ajouterChamp(2, CPProcessAsciiReader.ANNEE_1, _validateNumber(getAnnee1(), "Annee1", ""));
            ajouterChamp(2, CPProcessAsciiReader.ANNEE_2, _validateNumber(getAnnee2(), "Annee2", ""));
            ajouterChamp(2, CPProcessAsciiReader.REVENU_1, _validateNumber(getRevenu1(), "Revenu1", ""));
            ajouterChamp(2, CPProcessAsciiReader.REVENU_2, _validateNumber(getRevenu2(), "Revenu2", ""));
            ajouterChamp(2, CPProcessAsciiReader.COTISATION_1, _validateNumber(getCotisation1(), "Cotisation1", ""));
            ajouterChamp(2, CPProcessAsciiReader.COTISATION_2, _validateNumber(getCotisation2(), "Cotisation2", ""));
            ajouterChamp(2, CPProcessAsciiReader.FORTUNE, _validateNumber(getFortune(), "Fortune", ""));
            ajouterChamp(2, CPProcessAsciiReader.DATE_FORTUNE, _validateDate(getDateFortune(), "DateFortune", ""));
            ajouterChamp(2, CPProcessAsciiReader.CAPITAL, _validateNumber(getCapital(), "Capital", ""));
            ajouterChamp(2, CPProcessAsciiReader.DEBUT_EXERCICE_1,
                    _validateDate(getDebutExercice1(), "DebutExercice1", ""));
            ajouterChamp(2, CPProcessAsciiReader.FIN_EXERCICE_1, _validateDate(getFinExercice1(), "FinExercice1", ""));
            ajouterChamp(2, CPProcessAsciiReader.DEBUT_EXERCICE_2,
                    _validateDate(getDebutExercice2(), "DebutExercice2", ""));
            ajouterChamp(2, CPProcessAsciiReader.FIN_EXERCICE_2, _validateDate(getFinExercice2(), "FinExercice2", ""));
            ajouterChamp(2, CPProcessAsciiReader.GENRE_TAXATION, getGenreTaxation());
            ajouterChamp(2, CPProcessAsciiReader.PERIODE_IFD, _validateNumber(getPeriodeIFD(), "PeriodeIFD", ""));
            // Zones NE
            if (!JadeStringUtil.isEmpty(getNeNumAvs()) || !JadeStringUtil.isEmpty(getNeNumCaisse())) {
                ecrireEntreeNE();
            }
            // Zone JU
            else if (!JadeStringUtil.isEmpty(getJuCodeApplication())) {
                ecrireEntreeJU();
            }
            // Zone GE
            else if (!JadeStringUtil.isEmpty(getGeNumCaisse())) {
                ecrireEntreeGE();
            }
            // Zone VS
            else if (!JadeStringUtil.isEmpty(getVsNumCtb())) {
                ecrireEntreeVS();
            }
            //
            CPProcessAsciiReader.this.ajouterText(2, CPProcessAsciiReader.DEBUT_GENEREATION_MESSAGE);
            ajouterLog(3, CPProcessAsciiReader.LOG_ERROR, errorMsg);
            ajouterLog(3, CPProcessAsciiReader.LOG_WARNING, warningMsg);
            ajouterLog(3, CPProcessAsciiReader.LOG_INFO, infoMsg);
            CPProcessAsciiReader.this.ajouterText(2, CPProcessAsciiReader.FIN_GENEREATION_MESSAGE);

            CPProcessAsciiReader.this.ajouterText(1, CPProcessAsciiReader.FIN_ENTREE);

            return true;
        }

        /**
         * @author hna Ecrit les cahmps GE
         */
        private void ecrireEntreeGE() throws Exception {
            ajouterChamp(2, CPProcessAsciiReader.GE_NUM_CAISSE, getGeNumCaisse());
            ajouterChamp(2, CPProcessAsciiReader.GE_NUM_DEMANDE,
                    _validateNumber(getGeNumDemande(), "getGeNumDemande", "0"));
            ajouterChamp(2, CPProcessAsciiReader.GE_GENRE_AFFILIE,
                    _validateNumber(getGeGenreAffilie(), "getGeGenreAffilie", "0"));
            ajouterChamp(2, CPProcessAsciiReader.GE_NUM_AFFILIE, getGeNumAffilie());
            ajouterChamp(2, CPProcessAsciiReader.GE_NUM_AVS, getGeNumAvs());
            ajouterChamp(2, CPProcessAsciiReader.GE_NOM, getGeNom());
            ajouterChamp(2, CPProcessAsciiReader.GE_PRENOM, getGePrenom());
            ajouterChamp(2, CPProcessAsciiReader.GE_NUM_COMMUNICATION,
                    _validateNumber(getGeNumCommunication(), "getGeNumCommunication", ""));
            ajouterChamp(2, CPProcessAsciiReader.GE_PERSONNE_NONIDENTIFIEE,
                    this._validateBoolean(getGePersonneNonIdentifiee(), "gePersonneNonIdentifiee", Boolean.FALSE));
            ajouterChamp(2, CPProcessAsciiReader.GE_NUM_CONTRIBUABLE, getGeNumContribuable());
            ajouterChamp(2, CPProcessAsciiReader.GE_NOM_AFC, getGeNomAFC());
            ajouterChamp(2, CPProcessAsciiReader.GE_PRENOM_AFC, getGePrenomAFC());
            ajouterChamp(2, CPProcessAsciiReader.GE_NUM_AVS_CONJOINT,
                    _validateNumber(getGeNumAvsConjoint(), "getGeNumAvsConjoint", ""));
            ajouterChamp(2, CPProcessAsciiReader.GE_NOM_CONJOINT, getGeNomConjoint());
            ajouterChamp(2, CPProcessAsciiReader.GE_PRENOM_CONJOINT, getGePrenomConjoint());
            if ("1".equalsIgnoreCase(getGeGenreAffilie())) {
                // Indépendant
                ajouterChamp(2, CPProcessAsciiReader.GE_PAS_ACTIVITEDECLAREE,
                        this._validateBoolean(getGePasActiviteDeclaree(), "getGePasActiviteDeclaree", Boolean.FALSE));
            } else {
                // Non-actif
                ajouterChamp(2, CPProcessAsciiReader.GE_IMPOSITION_SELON_DEPENSE, this._validateBoolean(
                        getGeImpositionSelonDepense(), "getGeImpositionSelonDepense", Boolean.FALSE));
                ajouterChamp(2, CPProcessAsciiReader.GE_PENSION,
                        this._validateBoolean(getGePension(), "getGePension", Boolean.FALSE));
                ajouterChamp(2, CPProcessAsciiReader.GE_RETRAITE,
                        this._validateBoolean(getGeRetraite(), "getGeRetraite", Boolean.FALSE));
                ajouterChamp(2, CPProcessAsciiReader.GE_RENTE_VIEILLESSE,
                        this._validateBoolean(getGeRenteVieillesse(), "getGeRenteVieillesse", Boolean.FALSE));
                ajouterChamp(2, CPProcessAsciiReader.GE_RENTE_INVALIDITE,
                        this._validateBoolean(getGeRenteInvalidite(), "getGeRenteInvalidite", Boolean.FALSE));
                ajouterChamp(2, CPProcessAsciiReader.GE_PENSION_ALIMENTAIRE,
                        this._validateBoolean(getGePensionAlimentaire(), "getGePensionAlimentaire", Boolean.FALSE));
                ajouterChamp(2, CPProcessAsciiReader.GE_RENTE_VIAGERE,
                        this._validateBoolean(getGeRenteViagere(), "getGeRenteViagere", Boolean.FALSE));
                ajouterChamp(2, CPProcessAsciiReader.GE_INDEMNITE_JOURNALIERE,
                        this._validateBoolean(getGeIndemniteJournaliere(), "getGeIndemniteJournaliere", Boolean.FALSE));
                ajouterChamp(2, CPProcessAsciiReader.GE_BOURSES,
                        this._validateBoolean(getGeBourses(), "getGeBourses", Boolean.FALSE));
                ajouterChamp(2, CPProcessAsciiReader.GE_DIVERS,
                        this._validateBoolean(getGeDivers(), "getGeDivers", Boolean.FALSE));
                ajouterChamp(2, CPProcessAsciiReader.GE_EXPLICATIONS_DIVERS, getGeExplicationsDivers());
            }
            ajouterChamp(2, CPProcessAsciiReader.GE_NONASSUJETTI_IBO,
                    this._validateBoolean(getGeNonAssujettiIBO(), "getGeNonAssujettiIBO", Boolean.FALSE));
            ajouterChamp(2, CPProcessAsciiReader.GE_NONASSUJETTI_IFD,
                    this._validateBoolean(getGeNonAssujettiIFD(), "getGeNonAssujettiIFD", Boolean.FALSE));
            ajouterChamp(2, CPProcessAsciiReader.GE_IMPOT_SOURCE,
                    this._validateBoolean(getGeImpotSource(), "getGeImpotSource", Boolean.FALSE));
            ajouterChamp(2, CPProcessAsciiReader.GE_TAXATION_OFFICE,
                    this._validateBoolean(getGeTaxationOffice(), "getGeTaxationOffice", Boolean.FALSE));
            ajouterChamp(2, CPProcessAsciiReader.GE_OBSERVATIONS, getGeObservations());
            ajouterChamp(2, CPProcessAsciiReader.GE_DATETRANSFERT_MAD,
                    _validateDate(getGeDateTransfertMAD(), "getGeDateTransfertMAD", ""));
            ajouterChamp(2, CPProcessAsciiReader.GE_NUM_NNSS, getGeNNSS());
        }

        /**
         * @author hna Ecrit les cahmps JU
         */
        private void ecrireEntreeJU() throws Exception {
            ajouterChamp(2, CPProcessAsciiReader.JU_CODE_APPLICATION, getJuCodeApplication());
            ajouterChamp(2, CPProcessAsciiReader.JU_NUM_CONTRIBUABLE, getJuNumContribuable());
            ajouterChamp(2, CPProcessAsciiReader.JU_LOT, _validateNumber(getJuLot(), "juLot", ""));
            ajouterChamp(2, CPProcessAsciiReader.JU_NBR_JOUR_1, _validateNumber(getJuNbrJour1(), "juNbrJour1", ""));
            ajouterChamp(2, CPProcessAsciiReader.JU_NBR_JOUR_2, _validateNumber(getJuNbrJour2(), "juNbrJour2", ""));
            ajouterChamp(2, CPProcessAsciiReader.JU_GENRE_AFFILIE, getJuGenreAffilie());
            ajouterChamp(2, CPProcessAsciiReader.JU_GENRE_TAXATION, getJuGenreTaxation());
            ajouterChamp(2, CPProcessAsciiReader.JU_EPOUX, getJuEpoux());
            ajouterChamp(2, CPProcessAsciiReader.JU_FILLER, getJuFiller());
            ajouterChamp(2, CPProcessAsciiReader.JU_TAXE_MAN, getJuTaxeMan());
            ajouterChamp(2, CPProcessAsciiReader.JU_DATE_NAISSANCE,
                    _validateNumber(getJuDateNaissance(), "juDateNaissance", ""));
            ajouterChamp(2, CPProcessAsciiReader.JU_NEWNUM_CONTRIBUABLE,
                    _validateNumber(getJuNewNumContribuable(), "juNewNumContribuable", ""));
        }

        /**
         * @author hna Ecrit les cahmps NE
         */
        private void ecrireEntreeNE() throws Exception {
            ajouterChamp(2, CPProcessAsciiReader.NE_FILLER, getNeFiller());
            ajouterChamp(2, CPProcessAsciiReader.NE_NUM_AVS, _validateNumber(getNeNumAvs(), "neNumAvs", "0"));
            ajouterChamp(2, CPProcessAsciiReader.NE_NUM_CAISSE, _validateNumber(getNeNumCaisse(), "neNumCaisse", "0"));
            ajouterChamp(2, CPProcessAsciiReader.NE_GENRE_AFFILIE, getNeGenreAffilie());
            ajouterChamp(2, CPProcessAsciiReader.NE_PREMIERE_LETTRE_NOM, getNePremiereLettreNom());
            ajouterChamp(2, CPProcessAsciiReader.NE_NUM_CONTRIBUABLE,
                    _validateNumber(getNeNumContribuable(), "neNumContribuable", "0"));
            ajouterChamp(2, CPProcessAsciiReader.NE_NUM_COMMUNE, _validateNumber(getNeNumCommune(), "neNumCommune", ""));
            ajouterChamp(2, CPProcessAsciiReader.NE_NUM_DBP, _validateNumber(getNeNumBDP(), "neNumBDP", ""));
            ajouterChamp(2, CPProcessAsciiReader.NE_NUM_CLIENT, _validateNumber(getNeNumClient(), "neNumClient", ""));
            ajouterChamp(2, CPProcessAsciiReader.NE_DATE_DEBUT_ASSUJ,
                    _validateDate(getNeDateDebutAssuj(), "neDateDebutAssuj", ""));
            ajouterChamp(2, CPProcessAsciiReader.NE_GENRE_TAXATION, getNeGenreTaxation());
            ajouterChamp(2, CPProcessAsciiReader.NE_TAXATION_RECTIFICATIVE,
                    this._validateBoolean(getNeTaxationRectificative(), "neTaxationRectificative", ""));
            ajouterChamp(2, CPProcessAsciiReader.NE_FORTUNE_ANNEE1,
                    _validateNumber(getNeFortuneAnnee1(), "neFortuneAnnee1", ""));
            ajouterChamp(2, CPProcessAsciiReader.NE_PENSION_ANNEE1,
                    _validateNumber(getNePensionAnnee1(), "nePensionAnnee1", ""));
            ajouterChamp(2, CPProcessAsciiReader.NE_PENSION, _validateNumber(getNePension(), "nePension", ""));
            ajouterChamp(2, CPProcessAsciiReader.NE_PENSION_ALIMENTAIRE1,
                    _validateNumber(getNePensionAlimentaire1(), "nePensionAlimentaire1", ""));
            ajouterChamp(2, CPProcessAsciiReader.NE_PENSION_ALIMENTAIRE,
                    _validateNumber(getNePensionAlimentaire(), "nePensionAlimentaire", ""));
            ajouterChamp(2, CPProcessAsciiReader.NE_RENTE_VIAGERE1,
                    _validateNumber(getNeRenteViagere1(), "neRenteViagere1", ""));
            ajouterChamp(2, CPProcessAsciiReader.NE_RENTE_VIAGERE,
                    _validateNumber(getNeRenteViagere(), "neRenteViagere", ""));
            ajouterChamp(2, CPProcessAsciiReader.NE_INDEMNITE_JOUR1,
                    _validateNumber(getNeIndemniteJour1(), "neIndemniteJour1", ""));
            ajouterChamp(2, CPProcessAsciiReader.NE_INDEMNITE_JOUR,
                    _validateNumber(getNeIndemniteJour(), "neIndemniteJour", ""));
            ajouterChamp(2, CPProcessAsciiReader.NE_RENTE_TOTALE1,
                    _validateNumber(getNeRenteTotale1(), "neRenteTotale1", ""));
            ajouterChamp(2, CPProcessAsciiReader.NE_RENTE_TOTALE,
                    _validateNumber(getNeRenteTotale(), "neRenteTotale", ""));
            ajouterChamp(2, CPProcessAsciiReader.NE_DATE_VALEUR, _validateDate(getNeDateValeur(), "neDateValeur", ""));
            ajouterChamp(2, CPProcessAsciiReader.NE_DOSSIER_TAXE, getNeDossierTaxe());
            ajouterChamp(2, CPProcessAsciiReader.NE_DOSSIER_TROUVE, getNeDossierTrouve());
        }

        /**
         * @author jpa Ecrit les champs VS
         */
        private void ecrireEntreeVS() throws Exception {
            ajouterChamp(2, CPProcessAsciiReader.VS_NUMCTB, getVsNumCtb());
            ajouterChamp(2, CPProcessAsciiReader.VS_ANNEETAXATION, getVsAnneeTaxation());
            ajouterChamp(2, CPProcessAsciiReader.VS_DATEDEMANDECOMMUNICATION, getVsDateDemandeCommunication());
            ajouterChamp(2, CPProcessAsciiReader.VS_DATECOMMUNICATION, getVsDateCommunication());
            ajouterChamp(2, CPProcessAsciiReader.VS_DATETAXATION, getVsDateTaxation());
            ajouterChamp(2, CPProcessAsciiReader.VS_CODETAXATION1, getVsCodeTaxation1());
            ajouterChamp(2, CPProcessAsciiReader.VS_CODETAXATION2, getVsCodeTaxation2());
            ajouterChamp(2, CPProcessAsciiReader.VS_TYPETAXATION, getVsTypeTaxation());
            ajouterChamp(2, CPProcessAsciiReader.VS_NUMAFFILIE, getVsNumAffilie());
            ajouterChamp(2, CPProcessAsciiReader.VS_NUMAVSAFFILIE, getVsNumAvsAffilie());
            ajouterChamp(2, CPProcessAsciiReader.VS_DATENAISSANCEAFFILIE, getVsDateNaissanceAffilie());
            ajouterChamp(2, CPProcessAsciiReader.VS_DATEDEBUTAFFILIATION, getVsDateDebutAffiliation());
            ajouterChamp(2, CPProcessAsciiReader.VS_DATEFINAFFILIATION, getVsDateFinAffiliation());
            ajouterChamp(2, CPProcessAsciiReader.VS_NOMAFFILIE, getVsNomAffilie());
            ajouterChamp(2, CPProcessAsciiReader.VS_ADRESSEAFFILIE1, getVsAdresseAffilie1());
            ajouterChamp(2, CPProcessAsciiReader.VS_ADRESSEAFFILIE2, getVsAdresseAffilie2());
            ajouterChamp(2, CPProcessAsciiReader.VS_ADRESSEAFFILIE3, getVsAdresseAffilie3());
            ajouterChamp(2, CPProcessAsciiReader.VS_ADRESSEAFFILIE4, getVsAdresseAffilie4());
            ajouterChamp(2, CPProcessAsciiReader.VS_NOPOSTALLOCALITE, getVsNoPostalLocalite());
            ajouterChamp(2, CPProcessAsciiReader.VS_NOCAISSEAGENCEAFFILIE, getVsNoCaisseAgenceAffilie());
            ajouterChamp(2, CPProcessAsciiReader.VS_NOCAISSEPROFESSIONNELLEAFFILIE,
                    getVsNoCaisseProfessionnelleAffilie());
            ajouterChamp(2, CPProcessAsciiReader.VS_DATEDEBUTAFFILIATIONCAISSEPROFESSIONNELLE,
                    getVsDateDebutAffiliationCaisseProfessionnelle());
            ajouterChamp(2, CPProcessAsciiReader.VS_DATEFINAFFILIATIONCAISSEPROFESSIONNELLE,
                    getVsDateFinAffiliationCaisseProfessionnelle());
            ajouterChamp(2, CPProcessAsciiReader.VS_NUMAFFILIEINTERNECAISSEPROFESSIONNELLE,
                    getVsNumAffilieInterneCaisseProfessionnelle());
            ajouterChamp(2, CPProcessAsciiReader.VS_COTISATIONAVSAFFILIE, getVsCotisationAvsAffilie());
            ajouterChamp(2, CPProcessAsciiReader.VS_ETATCIVILAFFILIE, getVsEtatCivilAffilie());
            ajouterChamp(2, CPProcessAsciiReader.VS_SEXEAFFILIE, getVsSexeAffilie());
            ajouterChamp(2, CPProcessAsciiReader.VS_NUMAFFILIECONJOINT, getVsNumAffilieConjoint());
            ajouterChamp(2, CPProcessAsciiReader.VS_NUMAVSCONJOINT, getVsNumAvsConjoint());
            ajouterChamp(2, CPProcessAsciiReader.VS_DATENAISSANCECONJOINT, getVsDateNaissanceConjoint());
            ajouterChamp(2, CPProcessAsciiReader.VS_DATEDEBUTAFFILIATIONCONJOINT, getVsDateDebutAffiliationConjoint());
            ajouterChamp(2, CPProcessAsciiReader.VS_DATEFINAFFILIATIONCONJOINT, getVsDateFinAffiliationConjoint());
            ajouterChamp(2, CPProcessAsciiReader.VS_NOMCONJOINT, getVsNomConjoint());
            ajouterChamp(2, CPProcessAsciiReader.VS_ADRESSECONJOINT1, getVsAdresseConjoint1());
            ajouterChamp(2, CPProcessAsciiReader.VS_ADRESSECONJOINT2, getVsAdresseConjoint2());
            ajouterChamp(2, CPProcessAsciiReader.VS_ADRESSECONJOINT3, getVsAdresseConjoint3());
            ajouterChamp(2, CPProcessAsciiReader.VS_ADRESSECONJOINT4, getVsAdresseConjoint4());
            ajouterChamp(2, CPProcessAsciiReader.VS_NUMPOSTALLOCALITECONJOINT, getVsNumPostalLocaliteConjoint());
            ajouterChamp(2, CPProcessAsciiReader.VS_NUMCAISSEAGENCECONJOINT, getVsNumCaisseAgenceConjoint());
            ajouterChamp(2, CPProcessAsciiReader.VS_NUMCAISSEPROFESSIONNELLECONJOINT,
                    getVsNumCaisseProfessionnelleConjoint());
            ajouterChamp(2, CPProcessAsciiReader.VS_DATEDEBUTAFFILIATIONCONJOINTCAISSEPROFESSIONNELLE,
                    getVsDateDebutAffiliationCaisseProfessionnelle());
            ajouterChamp(2, CPProcessAsciiReader.VS_DATEFINAFFILIATIONCONJOINTCAISSEPROFESSIONNELLE,
                    getVsDateFinAffiliationConjointCaisseProfessionnelle());
            ajouterChamp(2, CPProcessAsciiReader.VS_NUMAFFILIEINTERNECONJOINTCAISSEPROFESSIONNELLE,
                    getVsNumAffilieInterneConjointCaisseProfessionnelle());
            ajouterChamp(2, CPProcessAsciiReader.VS_COTISATIONAVSCONJOINT, getVsCotisationAvsConjoint());
            ajouterChamp(2, CPProcessAsciiReader.VS_NOMPRENOMCONTRIBUABLEANNEE, getVsNomPrenomContribuableAnnee());
            ajouterChamp(2, CPProcessAsciiReader.VS_ADRESSECTB1, getVsAdresseCtb1());
            ajouterChamp(2, CPProcessAsciiReader.VS_ADRESSECTB2, getVsAdresseCtb2());
            ajouterChamp(2, CPProcessAsciiReader.VS_ADRESSECTB3, getVsAdresseCtb3());
            ajouterChamp(2, CPProcessAsciiReader.VS_ADRESSECTB4, getVsAdresseCtb4());
            ajouterChamp(2, CPProcessAsciiReader.VS_NUMPOSTALLOCALITECTB, getVsNumPostalLocaliteCtb());
            ajouterChamp(2, CPProcessAsciiReader.VS_ETATCIVILCTB, getVsEtatCivilCtb());
            ajouterChamp(2, CPProcessAsciiReader.VS_SEXECTB, getVsSexeCtb());
            ajouterChamp(2, CPProcessAsciiReader.VS_LANGUE, getVsLangue());
            ajouterChamp(2, CPProcessAsciiReader.VS_NUMAVSCTB, getVsNumAvsCtb());
            ajouterChamp(2, CPProcessAsciiReader.VS_DATENAISSANCECTB, getVsDateNaissanceCtb());
            ajouterChamp(2, CPProcessAsciiReader.VS_DEBUTACTIVITECTB, getVsDebutActiviteCtb());
            ajouterChamp(2, CPProcessAsciiReader.VS_FINACTIVITECTB, getVsFinActiviteCtb());
            ajouterChamp(2, CPProcessAsciiReader.VS_DEBUTACTIVITECONJOINT, getVsDebutActiviteConjoint());
            ajouterChamp(2, CPProcessAsciiReader.VS_FINACTIVITECONJOINT, getVsFinActiviteConjoint());
            ajouterChamp(2, CPProcessAsciiReader.VS_REVENUNONAGRICOLECTB, getVsRevenuNonAgricoleCtb());
            ajouterChamp(2, CPProcessAsciiReader.VS_REVENUNONAGRICOLECONJOINT, getVsRevenuNonAgricoleConjoint());
            ajouterChamp(2, CPProcessAsciiReader.VS_REVENUAGRICOLECTB, getVsRevenuAgricoleCtb());
            ajouterChamp(2, CPProcessAsciiReader.VS_REVENUAGRICOLECONJOINT, getVsRevenuAgricoleConjoint());
            ajouterChamp(2, CPProcessAsciiReader.VS_CAPITALPROPREENGAGEENTREPRISECTB,
                    getVsCapitalPropreEngageEntrepriseCtb());
            ajouterChamp(2, CPProcessAsciiReader.VS_CAPITALPROPREENGAGEENTREPRISECONJOINT,
                    getVsCapitalPropreEngageEntrepriseConjoint());
            ajouterChamp(2, CPProcessAsciiReader.VS_REVENURENTECTB, getVsRevenuRenteCtb());
            ajouterChamp(2, CPProcessAsciiReader.VS_REVENURENTECONJOINT, getVsRevenuRenteConjoint());
            ajouterChamp(2, CPProcessAsciiReader.VS_FORTUNEPRIVEECTB, getVsFortunePriveeCtb());
            ajouterChamp(2, CPProcessAsciiReader.VS_FORTUNEPRIVEECONJOINT, getVsFortunePriveeConjoint());
            ajouterChamp(2, CPProcessAsciiReader.VS_SALAIRESCONTRIBUABLE, getVsSalairesContribuable());
            ajouterChamp(2, CPProcessAsciiReader.VS_SALAIRESCONJOINT, getVsSalairesConjoint());
            ajouterChamp(2, CPProcessAsciiReader.VS_AUTRESREVENUSCTB, getVsAutresRevenusCtb());
            ajouterChamp(2, CPProcessAsciiReader.VS_AUTRESREVENUSCONJOINT, getVsAutresRevenusConjoint());
            ajouterChamp(2, CPProcessAsciiReader.VS_RACHAT_LPP, getVsRachatLpp());
            ajouterChamp(2, CPProcessAsciiReader.VS_RACHAT_LPP_CJT, getVsRachatLppCjt());
            ajouterChamp(2, CPProcessAsciiReader.VS_LIBRE3, getVsLibre3());
            ajouterChamp(2, CPProcessAsciiReader.VS_LIBRE4, getVsLibre4());
            ajouterChamp(2, CPProcessAsciiReader.VS_RESERVE, getVsReserve());
            ajouterChamp(2, CPProcessAsciiReader.VS_NBJOURSTAXATION, getVsNbJoursTaxation());
            ajouterChamp(2, CPProcessAsciiReader.VS_NUMCTBSUIVANT, getVsNumCtbSuivant());
            ajouterChamp(2, CPProcessAsciiReader.VS_DATEDECESCTB, getVsDateDecesCtb());
            ajouterChamp(2, CPProcessAsciiReader.VS_RESERVEDATENAISSANCECONJOINT, getVsReserveDateNaissanceConjoint());
            ajouterChamp(2, CPProcessAsciiReader.VS_RESERVEFICHIERIMPRESSION, getVsReserveFichierImpression());
            ajouterChamp(2, CPProcessAsciiReader.VS_RESERVETRINUMCAISSE, getVsReserveTriNumCaisse());
        }

        /*
         * DEBUTS DES CHAMPS INSERES DANS LE DOCUMENT XML Par défauts des chaînes vides sont insérées Ces méthodes
         * devraient être surchargées le plus possible a partire des données reçues
         */

        /**
         * Passe à la ligne suivante, si des caractères restent reporte une erreure
         * 
         * @throws IOException
         *             DOCUMENT ME!
         */
        protected void endLine() throws Exception {
            String endOfLine = fileInputReader.readLine();
            int restLength = 0;
            if (endOfLine != null) {
                restLength = endOfLine.length();
            }

            if (!JadeStringUtil.isBlank(endOfLine)) {
                if (restLength + position > CPReception.MAX_LINE_LENGHT) {
                    throw new Exception(FWMessageFormat.format(
                            getSession().getLabel("PROCASCIIREAD_ERROR_LIGNE_TROP_LONGUE"),
                            String.valueOf(restLength + position + 1)));
                } else {
                    addWarning(FWMessageFormat.format(getSession().getLabel("PROCASCIIREAD_ERROR_EOL_EXPECTED"),
                            endOfLine));
                }
            }
        }

        /**
         * Lit la date dans une chaine de caractères.
         * 
         * @param dateStr
         *            la date sous forme de chaine de caractères
         * @param separator
         *            le séparateur
         * @exception globaz.globall.util.JAException
         *                si la chaine ne contient pas une date
         */
        public String fromString(String dateStr, char separator) throws JAException {
            int day = 0;
            int month = 0;
            int year = 0;
            String date = "";
            int idx = dateStr.indexOf(separator);
            if (idx < 0) {
                throw new JAException(CPProcessAsciiReader.STRING_NOT_DATE);
            }
            if (JadeStringUtil.isBlank(dateStr)) {
                return "";
            }
            try {
                day = JadeStringUtil.toInt(dateStr.substring(0, idx));
                dateStr = dateStr.substring(idx + 1);
                idx = dateStr.indexOf(separator);
                month = JadeStringUtil.toInt(dateStr.substring(0, idx));
                dateStr = dateStr.substring(idx + 1);
                year = JadeStringUtil.toInt(dateStr.substring(0, 4));
                if ((day < 10) && (month < 10)) {
                    date = "0" + day + ".0" + month + "." + year;
                }
                if ((day < 10) && (month > 10)) {
                    date = "0" + day + "." + month + "." + year;
                }
                if ((day > 10) && (month < 10)) {
                    date = day + ".0" + month + "." + year;
                } else {
                    date = day + "." + month + "." + year;
                }

            } catch (Exception e) {
                throw new JAException(CPProcessAsciiReader.STRING_NOT_DATE);
            }
            return date;
        }

        /**
         * Ce parametre doit être implémenté. La date retournée doit être au format AAAA
         */
        public String getAnnee() {
            return "";
        }

        public String getAnnee1() {
            return "";
        }

        public String getAnnee2() {
            return "";
        }

        /**
         * A implémenter
         */
        public String getCapital() {
            return "";
        }

        /**
         * @return le parametre qui sera place dans le document xml. A surcharger
         */
        public String getCotisation1() {
            return "";
        }

        /**
         * @return le parametre qui sera place dans le document xml. A surcharger
         */
        public String getCotisation2() {
            return "";
        }

        /**
         * Par défaut la date de la fortune est le premier jour de l'an
         */
        public String getDateFortune() {
            if (!JadeStringUtil.isNull(getAnnee()) && !getAnnee().equals("") && !getAnnee().equals("0000")) {
                return "01.01." + getAnnee1();
            } else {
                return "";
            }

        }

        /**
         * Par défaut l'exercice commence le premier jour de l'an
         */
        public String getDebutExercice1() {
            return "";
        }

        /**
         * @return le parametre qui sera place dans le document xml. A surcharger
         */
        public String getDebutExercice2() {
            return "";
        }

        /**
         * Lit une chaîne de caractère le longeur length du fichier et affect à au champ de la classe
         * 
         * @param field
         * @param length
         * @throws Exception
         */
        protected String getField(int length) throws Exception {
            // S'il n'y a pas encore eu d'erreur, on lit les caractère
            if (valide && fileInputReader.ready()) {
                char c;
                int ci;
                StringBuffer tmpField = new StringBuffer();

                for (int i = 0; i < length; i++) {
                    ci = fileInputReader.read();
                    c = (char) ci;

                    if (ci == '\n') {
                        if (position < CPReception.MIN_LINE_LENGHT) {
                            throw new Exception(FWMessageFormat.format(
                                    getSession().getLabel("PROCASCIIREAD_ERROR_EOL_NOT_EXPECTED"),
                                    String.valueOf(position + 1)));
                        } else {
                            this._addError(getSession().getLabel("PROCASCIIREAD_ERROR_EOL_UNEXPECTED"));
                        }
                    } else if (ci == -1) { // Fin de fichier
                        this._addError(getSession().getLabel("PROCASCIIREAD_ERROR_EOF_UNEXPECTED"));
                        return tmpField.toString();
                    } else {
                        position++;
                        tmpField.append(c);
                    }

                }

                return tmpField.toString();
            } else {
                this._addError(getSession().getLabel("PROCASCIIREAD_ERROR_UNABLE_TO_READ"));
                return "";
            }
        }

        /**
         * Lit une chaîne de caractère le longeur length du fichier et affect à au champ de la classe
         * 
         * @param field
         * @param length
         * @throws Exception
         */
        protected String getField(int length, char separateur) throws Exception {
            // S'il n'y a pas encore eu d'erreur, on lit les caractère
            if (valide && fileInputReader.ready()) {
                char c;
                int ci;
                StringBuffer tmpField = new StringBuffer();

                for (int i = 0; i <= length; i++) {
                    ci = fileInputReader.read();
                    c = (char) ci;
                    if ((c == separateur) || (i == length) || (ci == '\r') || (ci == '\n')) {
                        return tmpField.toString();
                    }
                    // if (ci == '\n') {
                    // if (position < MIN_LINE_LENGHT) {
                    // throw new
                    // Exception(FWMessageFormat.format(getSession().getLabel("PROCASCIIREAD_ERROR_EOL_NOT_EXPECTED"),
                    // String.valueOf(position + 1)));
                    // } else {
                    // _addError(getSession().getLabel("PROCASCIIREAD_ERROR_EOL_UNEXPECTED"));
                    // }
                    // } else
                    if (ci == -1) { // Fin de fichier
                        this._addError(getSession().getLabel("PROCASCIIREAD_ERROR_EOF_UNEXPECTED"));
                        return tmpField.toString();
                    } else {
                        position++;
                        tmpField.append(c);
                    }
                }
                return tmpField.toString();
            } else {
                this._addError(getSession().getLabel("PROCASCIIREAD_ERROR_UNABLE_TO_READ"));
                return "";
            }
        }

        /**
         * Par défaut la fin de l'exercice est le dernier jour de l'an
         */
        public String getFinExercice1() {
            return "";
        }

        /**
         * @return le parametre qui sera place dans le document xml. A surcharger
         */
        public String getFinExercice2() {
            return "";
        }

        /**
         * A implémenter
         */
        public String getFortune() {
            return "";
        }

        /**
         * @return
         */
        public String getGeBourses() {
            return "";
        }

        public String getGeDateTransfertMAD() {
            return "";
        }

        /**
         * @return
         */
        public String getGeDivers() {
            return "";
        }

        /**
         * @return
         */
        public String getGeExplicationsDivers() {
            return "";
        }

        /**
         * Insérez la description de la méthode ici. Date de création : (06.03.2003 14:25:35)
         * 
         * @return java.lang.String
         */
        public String getGeGenreAffilie() {
            return "";
        }

        /**
         * @return
         */
        public String getGeImpositionSelonDepense() {
            return "";
        }

        public String getGeImpotSource() {
            return "";
        }

        /**
         * @return
         */
        public String getGeIndemniteJournaliere() {
            return "";
        }

        public String getGeNNSS() {
            return "";
        }

        /**
         * @return
         */
        public String getGeNom() {
            return "";
        }

        /**
         * @return
         */
        public String getGeNomAFC() {
            return "";
        }

        /**
         * @return
         */
        public String getGeNomConjoint() {
            return "";
        }

        /**
         * @return
         */
        public String getGeNonAssujettiIBO() {
            return "";
        }

        /**
         * @return
         */
        public String getGeNonAssujettiIFD() {
            return "";
        }

        /**
         * A implémenter
         */
        public String getGenreAffilie() {
            return "";
        }

        /**
         * @return le parametre qui sera place dans le document xml. A surcharger
         */
        public String getGenreTaxation() {
            return "";
        }

        /**
         * @return
         */
        public String getGeNumAffilie() {
            return "";
        }

        /**
         * @return
         */
        public String getGeNumAvs() {
            return "";
        }

        /**
         * @return
         */
        public String getGeNumAvsConjoint() {
            return "";
        }

        /**
         * @return
         */
        public String getGeNumCaisse() {
            return "";
        }

        /**
         * @return
         */
        public String getGeNumCommunication() {
            return "";
        }

        /**
         * @return
         */
        public String getGeNumContribuable() {
            return "";
        }

        /**
         * @return
         */
        public String getGeNumDemande() {
            return "";
        }

        /**
         * @return
         */
        public String getGeObservations() {
            return "";
        }

        /**
         * @return
         */
        public String getGePasActiviteDeclaree() {
            return "";
        }

        /**
         * @return
         */
        public String getGePension() {
            return "";
        }

        /**
         * @return
         */
        public String getGePensionAlimentaire() {
            return "";
        }

        /**
         * @return
         */
        public String getGePersonneNonIdentifiee() {
            return "";
        }

        /**
         * @return
         */
        public String getGePrenom() {
            return "";
        }

        /**
         * @return
         */
        public String getGePrenomAFC() {
            return "";
        }

        /**
         * @return
         */
        public String getGePrenomConjoint() {
            return "";
        }

        /**
         * @return
         */
        public String getGeRenteInvalidite() {
            return "";
        }

        /**
         * @return
         */
        public String getGeRenteViagere() {
            return "";
        }

        /**
         * @return
         */
        public String getGeRenteVieillesse() {
            return "";
        }

        /**
         * @return
         */
        public String getGeRetraite() {
            return "";
        }

        /**
         * @return
         */
        public String getGeTaxationOffice() {
            return "";
        }

        /**
         * @return le parametre qui sera place dans le document xml. A surcharger
         */
        public String getJuCodeApplication() {
            return "";
        }

        /**
         * @return le parametre qui sera place dans le document xml. A surcharger
         */
        public String getJuDateNaissance() {
            return "";
        }

        public String getJuEpoux() {
            return "";
        }

        public String getJuFiller() {
            return "";
        }

        /**
         * A implémenter
         */
        public String getJuGenreAffilie() {
            return "";
        }

        /**
         * @return le parametre qui sera place dans le document xml. A surcharger
         */
        public String getJuGenreTaxation() {
            return "";
        }

        public String getJuLot() {
            return "";
        }

        public String getJuNbrJour1() {
            return "";
        }

        public String getJuNbrJour2() {
            return "";
        }

        /**
         * @return le parametre qui sera place dans le document xml. A surcharger
         */
        public String getJuNewNumContribuable() {
            return "";
        }

        /**
         * @return le parametre qui sera place dans le document xml. A surcharger
         */
        public String getJuNumContribuable() {
            return "";
        }

        public String getJuTaxeMan() {
            return "";
        }

        public String getNeDateDebutAssuj() {
            return "";
        }

        public String getNeDateValeur() {
            return "";
        }

        public String getNeDossierTaxe() {
            return "";
        }

        public String getNeDossierTrouve() {
            return "";
        }

        public String getNeFiller() {
            return "";
        }

        public String getNeFortuneAnnee() {
            return "";
        }

        /**
         * @return le parametre qui sera place dans le document xml. A surcharger
         */
        public String getNeFortuneAnnee1() {
            return "";
        }

        /**
         * A implémenter
         */
        public String getNeGenreAffilie() {
            return "";
        }

        /**
         * @return le parametre qui sera place dans le document xml. A surcharger
         */
        public String getNeGenreTaxation() {
            return "";
        }

        public String getNeIndemniteJour() {
            return "";
        }

        public String getNeIndemniteJour1() {
            return "";
        }

        public String getNeJuCodeApplication() {
            return "";
        }

        /**
         * A implémenter
         */
        public String getNeNumAvs() {
            return "";
        }

        /**
         * @return le parametre qui sera place dans le document xml. A surcharger
         */
        public String getNeNumBDP() {
            return "";
        }

        /**
         * @return le parametre qui sera place dans le document xml. A surcharger
         */
        public String getNeNumCaisse() {
            return "";
        }

        public String getNeNumClient() {
            return "";
        }

        public String getNeNumCommune() {
            return "";
        }

        public String getNeNumContribuable() {
            return "";
        }

        public String getNePension() {
            return "";
        }

        public String getNePensionAlimentaire() {
            return "";
        }

        public String getNePensionAlimentaire1() {
            return "";
        }

        /**
         * @return le parametre qui sera place dans le document xml. A surcharger
         */
        public String getNePensionAnnee1() {
            return "";
        }

        public String getNePremiereLettreNom() {
            return "";
        }

        public String getNeRenteTotale() {
            return "";
        }

        public String getNeRenteTotale1() {
            return "";
        }

        public String getNeRenteViagere() {
            return "";
        }

        public String getNeRenteViagere1() {
            return "";
        }

        public String getNeTaxationRectificative() {
            return "";
        }

        /**
         * @return le numéro de l'entrée dans le document
         */
        public int getNumEntree() {
            return numEntree;
        }

        public String getPeriodeIFD() {
            return "";
        }

        /**
         * @return la position du curseur dans l'entrée
         */
        public int getPosition() {
            return position;
        }

        /**
         * Le revenu doit être implémenté
         */
        public String getRevenu1() {
            return "";
        }

        /**
         * @return le parametre qui sera place dans le document xml. A surcharger
         */
        public String getRevenu2() {
            return "";
        }

        public String getVsAdresseAffilie1() {
            return "";
        }

        public String getVsAdresseAffilie2() {
            return "";
        }

        public String getVsAdresseAffilie3() {
            return "";
        }

        public String getVsAdresseAffilie4() {
            return "";
        }

        public String getVsAdresseConjoint1() {
            return "";
        }

        public String getVsAdresseConjoint2() {
            return "";
        }

        public String getVsAdresseConjoint3() {
            return "";
        }

        public String getVsAdresseConjoint4() {
            return "";
        }

        public String getVsAdresseCtb1() {
            return "";
        }

        public String getVsAdresseCtb2() {
            return "";
        }

        public String getVsAdresseCtb3() {
            return "";
        }

        public String getVsAdresseCtb4() {
            return "";
        }

        public String getVsAnneeTaxation() {
            return "";
        }

        public String getVsAutresRevenusConjoint() {
            return "";
        }

        public String getVsAutresRevenusCtb() {
            return "";
        }

        public String getVsCapitalPropreEngageEntrepriseConjoint() {
            return "";
        }

        public String getVsCapitalPropreEngageEntrepriseCtb() {
            return "";
        }

        public String getVsCodeTaxation1() {
            return "";
        }

        public String getVsCodeTaxation2() {
            return "";
        }

        public String getVsCotisationAvsAffilie() {
            return "";
        }

        public String getVsCotisationAvsConjoint() {
            return "";
        }

        public String getVsDateCommunication() {
            return "";
        }

        public String getVsDateDebutAffiliation() {
            return "";
        }

        public String getVsDateDebutAffiliationCaisseProfessionnelle() {
            return "";
        }

        public String getVsDateDebutAffiliationConjoint() {
            return "";
        }

        public String getVsDateDebutAffiliationConjointCaisseProfessionnelle() {
            return "";
        }

        public String getVsDateDecesCtb() {
            return "";
        }

        public String getVsDateDemandeCommunication() {
            return "";
        }

        public String getVsDateFinAffiliation() {
            return "";
        }

        public String getVsDateFinAffiliationCaisseProfessionnelle() {
            return "";
        }

        public String getVsDateFinAffiliationConjoint() {
            return "";
        }

        public String getVsDateFinAffiliationConjointCaisseProfessionnelle() {
            return "";
        }

        public String getVsDateNaissanceAffilie() {
            return "";
        }

        public String getVsDateNaissanceConjoint() {
            return "";
        }

        public String getVsDateNaissanceCtb() {
            return "";
        }

        public String getVsDateTaxation() {
            return "";
        }

        public String getVsDebutActiviteConjoint() {
            return "";
        }

        public String getVsDebutActiviteCtb() {
            return "";
        }

        public String getVsEtatCivilAffilie() {
            return "";
        }

        public String getVsEtatCivilCtb() {
            return "";
        }

        public String getVsFinActiviteConjoint() {
            return "";
        }

        public String getVsFinActiviteCtb() {
            return "";
        }

        public String getVsFortunePriveeConjoint() {
            return "";
        }

        public String getVsFortunePriveeCtb() {
            return "";
        }

        public String getVsLangue() {
            return "";
        }

        public String getVsLibre3() {
            return "";
        }

        public String getVsLibre4() {
            return "";
        }

        public String getVsNbJoursTaxation() {
            return "";
        }

        public String getVsNoCaisseAgenceAffilie() {
            return "";
        }

        public String getVsNoCaisseProfessionnelleAffilie() {
            return "";
        }

        public String getVsNomAffilie() {
            return "";
        }

        public String getVsNomConjoint() {
            return "";
        }

        public String getVsNomPrenomContribuableAnnee() {
            return "";
        }

        public String getVsNoPostalLocalite() {
            return "";
        }

        public String getVsNumAffilie() {
            return "";
        }

        public String getVsNumAffilieConjoint() {
            return "";
        }

        public String getVsNumAffilieInterneCaisseProfessionnelle() {
            return "";
        }

        public String getVsNumAffilieInterneConjointCaisseProfessionnelle() {
            return "";
        }

        public String getVsNumAvsAffilie() {
            return "";
        }

        public String getVsNumAvsConjoint() {
            return "";
        }

        public String getVsNumAvsCtb() {
            return "";
        }

        public String getVsNumCaisseAgenceConjoint() {
            return "";
        }

        public String getVsNumCaisseProfessionnelleConjoint() {
            return "";
        }

        public String getVsNumCtb() {
            return "";
        }

        public String getVsNumCtbSuivant() {
            return "";
        }

        public String getVsNumPostalLocaliteConjoint() {
            return "";
        }

        public String getVsNumPostalLocaliteCtb() {
            return "";
        }

        public String getVsRachatLpp() {
            return "";
        }

        public String getVsRachatLppCjt() {
            return "";
        }

        public String getVsReserve() {
            return "";
        }

        public String getVsReserveDateNaissanceConjoint() {
            return "";
        }

        public String getVsReserveFichierImpression() {
            return "";
        }

        public String getVsReserveTriNumCaisse() {
            return "";
        }

        public String getVsRevenuAgricoleConjoint() {
            return "";
        }

        public String getVsRevenuAgricoleCtb() {
            return "";
        }

        public String getVsRevenuNonAgricoleConjoint() {
            return "";
        }

        public String getVsRevenuNonAgricoleCtb() {
            return "";
        }

        public String getVsRevenuRenteConjoint() {
            return "";
        }

        public String getVsRevenuRenteCtb() {
            return "";
        }

        public String getVsSalairesConjoint() {
            return "";
        }

        public String getVsSalairesContribuable() {
            return "";
        }

        public String getVsSexeAffilie() {
            return "";
        }

        public String getVsSexeCtb() {
            return "";
        }

        public String getVsTypeTaxation() {
            return "";
        }

        /**
         * @return true si l'entree ne contient pas d'erreur
         */
        public boolean isValide() {
            return valide;
        }

        /**
         * Lit dans le ficher une entree et remplit les champs de l'objet Elle bénéficie des methodes endLine() et
         * getField()
         * 
         * @return DOCUMENT ME!
         * @throws Exception
         *             DOCUMENT ME!
         */
        public abstract int lireEntree() throws Exception;

        public String montantNeg(String montant, char separator) throws JAException {
            int montant1 = 0;
            int montant2 = 0;
            int idx = montant.indexOf(separator);
            montant1 = JadeStringUtil.toInt(montant.substring(0, idx));
            montant = montant.substring(idx + 1);
            if (montant.equals("")) {
            } else {
                montant2 = JadeStringUtil.toInt(montant.substring(0, 8 - idx));
            }
            if (montant1 == 0) {
                montant = "-" + montant2;
            }
            if (montant2 == 0) {
                montant = "-" + montant1;
            }
            return montant;
        }

    }

    protected static final String ANNEE_1 = "Annee1";
    protected static final String ANNEE_2 = "Annee2";

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    protected static final String CAPITAL = "Capital";
    protected static final String COTISATION_1 = "Cotisation1";
    protected static final String COTISATION_2 = "Cotisation2";
    protected static final String DATE_FORTUNE = "DateFortune";
    protected static final String DATE_RECEPTION_COM = "DateReceptionCom";
    protected static final String DATE_TRANSFORMATION_COM = "DateTransformationCom";
    protected static final String DEBUT_DOCUMENT = "<CommunicationReception>";
    protected static final String DEBUT_ENTETE = "<EnteteCommunication>";
    protected static final String DEBUT_ENTREE = "<Communication>";
    protected static final String DEBUT_ERROR_ITEM = "<ErrorItem>";
    protected static final String DEBUT_EXERCICE_1 = "DebutExercice1";
    protected static final String DEBUT_EXERCICE_2 = "DebutExercice2";
    protected static final String DEBUT_GENEREATION_MESSAGE = "<ErrorMessage>";
    protected static final String DEBUT_QUEUE = "<QueueCommunication>";
    protected static final String EMAIL_CONTACT = "EMailCtcCom";
    protected static final int EN_ERREUR = 2;
    /**
     * Champs du fichier xml de référence Les données de la communication de neuchatel doivent être mappés sur ces
     * champs
     */
    protected static final String ENTETE_DOCUMENT = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"  ?>";
    protected static final String ERROR_NIVEAU = "ErrorNiveau";
    protected static final String ERROR_TEXT = "ErrorText";
    protected static final String FIN_DOCUMENT = "</CommunicationReception>";
    protected static final String FIN_ENTETE = "</EnteteCommunication>";
    protected static final String FIN_ENTREE = "</Communication>";
    protected static final String FIN_ERROR_ITEM = "</ErrorItem>";
    protected static final String FIN_EXERCICE_1 = "FinExercice1";
    protected static final String FIN_EXERCICE_2 = "FinExercice2";
    protected static final int FIN_FICHIER = 3;
    protected static final String FIN_GENEREATION_MESSAGE = "</ErrorMessage>";
    protected static final String FIN_QUEUE = "</QueueCommunication>";
    protected static final String FORTUNE = "Fortune";

    protected static final String GE_BOURSES = "geBourses";
    protected static final String GE_DATETRANSFERT_MAD = "geDateTransfertMAD";
    protected static final String GE_DIVERS = "geDivers";
    protected static final String GE_EXPLICATIONS_DIVERS = "geExplicationsDivers";
    /**
     * GENEVE
     */
    protected static final String GE_GENRE_AFFILIE = "geGenreAffilie";
    protected static final String GE_IMPOSITION_SELON_DEPENSE = "geImpositionSelonDepense";
    protected static final String GE_IMPOT_SOURCE = "geImpotSource";
    protected static final String GE_INDEMNITE_JOURNALIERE = "geIndemniteJournaliere";
    protected static final String GE_NOM = "geNom";
    protected static final String GE_NOM_AFC = "geNomAFC";
    protected static final String GE_NOM_CONJOINT = "geNomConjoint";
    protected static final String GE_NONASSUJETTI_IBO = "geNonAssujettiIBO";
    protected static final String GE_NONASSUJETTI_IFD = "geNonAssujettiIFD";
    protected static final String GE_NUM_AFFILIE = "geNumAffilie";
    protected static final String GE_NUM_AVS = "geNumAvs";
    protected static final String GE_NUM_AVS_CONJOINT = "geNumAvsConjoint";
    protected static final String GE_NUM_CAISSE = "geNumCaisse";
    protected static final String GE_NUM_COMMUNICATION = "geNumCommunication";
    protected static final String GE_NUM_CONTRIBUABLE = "geNumContribuable";
    protected static final String GE_NUM_DEMANDE = "geNumDemande";
    protected static final String GE_NUM_NNSS = "geNNSS";
    protected static final String GE_OBSERVATIONS = "geObservations";
    protected static final String GE_PAS_ACTIVITEDECLAREE = "gePasActiviteDeclaree";
    protected static final String GE_PENSION = "gePension";
    protected static final String GE_PENSION_ALIMENTAIRE = "gePensionAlimentaire";
    protected static final String GE_PERSONNE_NONIDENTIFIEE = "gePersonneNonIdentifiee";
    protected static final String GE_PRENOM = "gePrenom";
    protected static final String GE_PRENOM_AFC = "gePrenomAFC";
    protected static final String GE_PRENOM_CONJOINT = "gePrenomConjoint";
    protected static final String GE_RENTE_INVALIDITE = "geRenteInvalidite";
    protected static final String GE_RENTE_VIAGERE = "geRenteViagere";
    protected static final String GE_RENTE_VIEILLESSE = "geRenteVieillesse";
    protected static final String GE_RETRAITE = "geRetraite";
    protected static final String GE_TAXATION_OFFICE = "geTaxationOffice";
    protected static final String GENRE_AFFILIE = "GenreAffilie";
    protected static final String GENRE_TAXATION = "GenreTaxation";
    /** Champs de la communication */
    protected static final String ID_DEMANDE = "IdDemande";
    /**
     * JURA
     */
    protected static final String JU_CODE_APPLICATION = "juCodeApplication";
    protected static final String JU_DATE_NAISSANCE = "juDateNaissance";
    protected static final String JU_EPOUX = "juEpoux";
    protected static final String JU_FILLER = "juFiller";
    protected static final String JU_GENRE_AFFILIE = "juGenreAffilie";
    protected static final String JU_GENRE_TAXATION = "juGenreTaxation";
    protected static final String JU_LOT = "juLot";
    protected static final String JU_NBR_JOUR_1 = "juNbrJour1";
    protected static final String JU_NBR_JOUR_2 = "juNbrJour2";
    protected static final String JU_NEWNUM_CONTRIBUABLE = "juNewNumContribuable";
    protected static final String JU_NUM_CONTRIBUABLE = "juNumContribuable";
    protected static final String JU_TAXE_MAN = "juTaxeMan";
    protected static final String LOG_ERROR = FWMessage.ERREUR;
    protected static final String LOG_INFO = FWMessage.INFORMATION;
    protected static final String LOG_WARNING = FWMessage.AVERTISSEMENT;
    protected static final String NB_NOT_OK = "NbreCommNOK";
    /** Champs de la queue de communication */
    protected static final String NB_OK = "NbreCommOK";
    /** Champs de l'entete */
    protected static final String NB_RECEPTION_COM = "NbreReceptionCom";
    protected static final String NE_DATE_DEBUT_ASSUJ = "neDateDebutAssuj";
    protected static final String NE_DATE_VALEUR = "neDateValeur";
    protected static final String NE_DOSSIER_TAXE = "neDossierTaxe";
    protected static final String NE_DOSSIER_TROUVE = "neDossierTrouve";
    /**
     * NEUCHATEL
     */
    protected static final String NE_FILLER = "neFiller";
    protected static final String NE_FORTUNE_ANNEE1 = "neFortuneAnnee1";
    protected static final String NE_GENRE_AFFILIE = "neGenreAffilie";
    protected static final String NE_GENRE_TAXATION = "neGenreTaxation";
    protected static final String NE_INDEMNITE_JOUR = "neIndemniteJour";
    protected static final String NE_INDEMNITE_JOUR1 = "neIndemniteJour1";
    protected static final String NE_NUM_AVS = "neNumAvs";
    protected static final String NE_NUM_CAISSE = "neNumCaisse";
    protected static final String NE_NUM_CLIENT = "neNumClient";
    protected static final String NE_NUM_COMMUNE = "neNumCommune";
    protected static final String NE_NUM_CONTRIBUABLE = "neNumContribuable";
    protected static final String NE_NUM_DBP = "neNumBDP";
    protected static final String NE_PENSION = "nePension";
    protected static final String NE_PENSION_ALIMENTAIRE = "nePensionAlimentaire";
    protected static final String NE_PENSION_ALIMENTAIRE1 = "nePensionAlimentaire1";
    protected static final String NE_PENSION_ANNEE1 = "nePensionAnnee1";
    protected static final String NE_PREMIERE_LETTRE_NOM = "nePremiereLettreNom";
    protected static final String NE_RENTE_TOTALE = "neRenteTotale";
    protected static final String NE_RENTE_TOTALE1 = "neRenteTotale1";
    protected static final String NE_RENTE_VIAGERE = "neRenteViagere";
    protected static final String NE_RENTE_VIAGERE1 = "neRenteViagere1";
    protected static final String NE_TAXATION_RECTIFICATIVE = "neTaxationRectificative";
    protected static final String NOM_CONTACT = "NomCtcCom";
    protected static final String NUM_RECEPTION_COM = "NumReceptionCom";
    protected static final String PERIODE_IFD = "PeriodeIFD";
    protected static final String REVENU_1 = "Revenu1";
    protected static final String REVENU_2 = "Revenu2";
    protected static final String STRING_NOT_DATE = "La chaine de caractères n'est pas une date !";
    protected static final int SUCCES = 1;
    protected static final String TEL_CONTACT = "TelCtcCom";
    /**
	 */
    protected static final String USER_ID = "UserId";
    protected static final String VS_ADRESSEAFFILIE1 = "vsAdresseAffilie1";
    protected static final String VS_ADRESSEAFFILIE2 = "vsAdresseAffilie2";
    protected static final String VS_ADRESSEAFFILIE3 = "vsAdresseAffilie3";
    protected static final String VS_ADRESSEAFFILIE4 = "vsAdresseAffilie4";
    protected static final String VS_ADRESSECONJOINT1 = "vsAdresseConjoint1";
    protected static final String VS_ADRESSECONJOINT2 = "vsAdresseConjoint2";
    protected static final String VS_ADRESSECONJOINT3 = "vsAdresseConjoint3";
    protected static final String VS_ADRESSECONJOINT4 = "vsAdresseConjoint4";
    protected static final String VS_ADRESSECTB1 = "vsAdresseCtb1";
    protected static final String VS_ADRESSECTB2 = "vsAdresseCtb2";
    protected static final String VS_ADRESSECTB3 = "vsAdresseCtb3";
    protected static final String VS_ADRESSECTB4 = "vsAdresseCtb4";
    protected static final String VS_ANNEETAXATION = "vsAnneeTaxation";
    protected static final String VS_AUTRESREVENUSCONJOINT = "vsAutresRevenusConjoint";
    protected static final String VS_AUTRESREVENUSCTB = "vsAutresRevenusCtb";
    protected static final String VS_CAPITALPROPREENGAGEENTREPRISECONJOINT = "vsCapitalPropreEngageEntrepriseConjoint";
    protected static final String VS_CAPITALPROPREENGAGEENTREPRISECTB = "vsCapitalPropreEngageEntrepriseCtb";
    protected static final String VS_CODETAXATION1 = "vsCodeTaxation1";
    protected static final String VS_CODETAXATION2 = "vsCodeTaxation2";
    protected static final String VS_COTISATIONAVSAFFILIE = "vsCotisationAvsAffilie";
    protected static final String VS_COTISATIONAVSCONJOINT = "vsCotisationAvsConjoint";
    protected static final String VS_DATECOMMUNICATION = "vsDateCommunication";
    protected static final String VS_DATEDEBUTAFFILIATION = "vsDateDebutAffiliation";
    protected static final String VS_DATEDEBUTAFFILIATIONCAISSEPROFESSIONNELLE = "vsDateDebutAffiliationCaisseProfessionnelle";
    protected static final String VS_DATEDEBUTAFFILIATIONCONJOINT = "vsDateDebutAffiliationConjoint";
    protected static final String VS_DATEDEBUTAFFILIATIONCONJOINTCAISSEPROFESSIONNELLE = "vsDateDebutAffiliationConjointCaisseProfessionnelle";
    protected static final String VS_DATEDECESCTB = "vsDateDecesCtb";
    protected static final String VS_DATEDEMANDECOMMUNICATION = "vsDateDemandeCommunication";
    protected static final String VS_DATEFINAFFILIATION = "vsDateFinAffiliation";
    protected static final String VS_DATEFINAFFILIATIONCAISSEPROFESSIONNELLE = "vsDateFinAffiliationCaisseProfessionnelle";
    protected static final String VS_DATEFINAFFILIATIONCONJOINT = "vsDateFinAffiliationConjoint";
    protected static final String VS_DATEFINAFFILIATIONCONJOINTCAISSEPROFESSIONNELLE = "vsDateFinAffiliationConjointCaisseProfessionnelle";
    protected static final String VS_DATENAISSANCEAFFILIE = "vsDateNaissanceAffilie";
    protected static final String VS_DATENAISSANCECONJOINT = "vsDateNaissanceConjoint";
    protected static final String VS_DATENAISSANCECTB = "vsDateNaissanceCtb";
    protected static final String VS_DATETAXATION = "vsDateTaxation";
    protected static final String VS_DEBUTACTIVITECONJOINT = "vsDebutActiviteConjoint";
    protected static final String VS_DEBUTACTIVITECTB = "vsDebutActiviteCtb";
    protected static final String VS_ETATCIVILAFFILIE = "vsEtatCivilAffilie";
    protected static final String VS_ETATCIVILCTB = "vsEtatCivilCtb";
    protected static final String VS_FINACTIVITECONJOINT = "vsFinActiviteConjoint";
    protected static final String VS_FINACTIVITECTB = "vsFinActiviteCtb";
    protected static final String VS_FORTUNEPRIVEECONJOINT = "vsFortunePriveeConjoint";
    protected static final String VS_FORTUNEPRIVEECTB = "vsFortunePriveeCtb";
    protected static final String VS_LANGUE = "vsLangue";
    protected static final String VS_LIBRE3 = "vsLibre3";
    protected static final String VS_LIBRE4 = "vsLibre4";
    protected static final String VS_NBJOURSTAXATION = "vsNbJoursTaxation";
    protected static final String VS_NOCAISSEAGENCEAFFILIE = "vsNoCaisseAgenceAffilie";
    protected static final String VS_NOCAISSEPROFESSIONNELLEAFFILIE = "vsNoCaisseProfessionnelleAffilie";
    protected static final String VS_NOMAFFILIE = "vsNomAffilie";
    protected static final String VS_NOMCONJOINT = "vsNomConjoint";
    protected static final String VS_NOMPRENOMCONTRIBUABLEANNEE = "vsNomPrenomContribuableAnnee";
    protected static final String VS_NOPOSTALLOCALITE = "vsNoPostalLocalite";
    protected static final String VS_NUMAFFILIE = "vsNumAffilie";
    protected static final String VS_NUMAFFILIECONJOINT = "vsNumAffilieConjoint";
    protected static final String VS_NUMAFFILIEINTERNECAISSEPROFESSIONNELLE = "vsNumAffilieInterneCaisseProfessionnelle";
    protected static final String VS_NUMAFFILIEINTERNECONJOINTCAISSEPROFESSIONNELLE = "vsNumAffilieInterneConjointCaisseProfessionnelle";
    protected static final String VS_NUMAVSAFFILIE = "vsNumAvsAffilie";
    protected static final String VS_NUMAVSCONJOINT = "vsNumAvsConjoint";
    protected static final String VS_NUMAVSCTB = "vsNumAvsCtb";
    protected static final String VS_NUMCAISSEAGENCECONJOINT = "vsNumCaisseAgenceConjoint";
    protected static final String VS_NUMCAISSEPROFESSIONNELLECONJOINT = "vsNumCaisseProfessionnelleConjoint";
    /**
     * VALAIS
     */
    protected static final String VS_NUMCTB = "vsNumCtb";
    protected static final String VS_NUMCTBSUIVANT = "vsNumCtbSuivant";
    protected static final String VS_NUMPOSTALLOCALITECONJOINT = "vsNumPostalLocaliteConjoint";
    protected static final String VS_NUMPOSTALLOCALITECTB = "vsNumPostalLocaliteCtb";
    protected static final String VS_RACHAT_LPP = "vsRachatLpp";
    protected static final String VS_RACHAT_LPP_CJT = "vsRachatLppCjt";
    protected static final String VS_RESERVE = "vsReserve";
    protected static final String VS_RESERVEDATENAISSANCECONJOINT = "vsReserveDateNaissanceConjoint";
    protected static final String VS_RESERVEFICHIERIMPRESSION = "vsReserveFichierImpression";
    protected static final String VS_RESERVETRINUMCAISSE = "vsReserveTriNumCaisse";
    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------
    protected static final String VS_REVENUAGRICOLECONJOINT = "vsRevenuAgricoleConjoint";
    protected static final String VS_REVENUAGRICOLECTB = "vsRevenuAgricoleCtb";
    protected static final String VS_REVENUNONAGRICOLECONJOINT = "vsRevenuNonAgricoleConjoint";
    protected static final String VS_REVENUNONAGRICOLECTB = "vsRevenuNonAgricoleCtb";
    protected static final String VS_REVENURENTECONJOINT = "vsRevenuRenteConjoint";
    protected static final String VS_REVENURENTECTB = "vsRevenuRenteCtb";
    protected static final String VS_SALAIRESCONJOINT = "vsSalairesConjoint";
    protected static final String VS_SALAIRESCONTRIBUABLE = "vsSalairesContribuable";
    protected static final String VS_SEXEAFFILIE = "vsSexeAffilie";
    protected static final String VS_SEXECTB = "vsSexeCtb";

    protected static final String VS_TYPETAXATION = "vsTypeTaxation";
    private LinkedList<CPReception> communicationList = null;
    private String dateReception = null;
    private String eMailObject = null;
    private File fileInput = null;
    public BufferedReader fileInputReader = null;
    private PrintWriter fileOutputWriter = null;
    private String inputFileName = null;
    // Les données globales considèrent toutes les communications du fichier
    // ASCII reçu
    private int nbEntreesGlobal = 0;
    private int nbEntreesLues = 0;
    private int nbFiles = 0;
    private int nbNotOk = 0;
    private int nbNotOkGlobal = 0;
    // Les données locales considère les communications pour le fichier xml en
    // cours d'écriture
    private int nbOk = 0;
    private int nbOkGlobal = 0;
    private String outputFileDirectory = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private ArrayList<String> outputFilePath = new ArrayList<String>();

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeProcess()
     * @return DOCUMENT ME!
     */
    @Override
    protected boolean _executeProcess() {
        int successful = CPProcessAsciiReader.SUCCES;
        reset();
        try {
            /*
             * BufferedReader fileInput = new BufferedReader( new InputStreamReader(
             * this.getClass().getResourceAsStream( getInputFileName())));
             */
            fileInput = new File(getInputFileName());
            fileInputReader = new BufferedReader(new FileReader(fileInput)); // fichier
            // est
            // recherché
            // à
            // la
            // base
            // du
            // projet phenix
            // Determine la base des fichiers xml générés
            // Attribution d'un time stamp au fichier crée
            Calendar now = Calendar.getInstance();
            String uid = String.valueOf(now.get(Calendar.YEAR)).substring(2) + String.valueOf(now.get(Calendar.MONTH))
                    + String.valueOf(now.get(Calendar.DAY_OF_MONTH)) + String.valueOf(now.get(Calendar.HOUR_OF_DAY))
                    + String.valueOf(now.get(Calendar.MINUTE)) + String.valueOf(now.get(Calendar.MILLISECOND));
            String outputFileBase = fileInput.getName() + '.' + uid;
            communicationList = new LinkedList<CPReception>();
            // Tant que le fichier n'est pas vide et que la transformation
            // reussit,
            // On lit le fichier et crée des objets communication que l'on met
            // ds une liste
            CPReception communication = null;
            while (fileInputReader.ready() && (successful != CPProcessAsciiReader.FIN_FICHIER)) {
                // A partir d'un certain nombre d'entrees lues, on génère un
                // fichier
                if (nbEntreesLues >= getNbMaxCommunication()) {
                    ecrireFichier(outputFileBase);
                }
                communication = getReception(this, nbEntreesGlobal);
                successful = communication.lireEntree();
                nbEntreesLues++;
                nbEntreesGlobal++;
                if (successful == CPProcessAsciiReader.SUCCES) {
                    ++nbOkGlobal;
                    ++nbOk;
                } else if (successful == CPProcessAsciiReader.EN_ERREUR) {
                    ++nbNotOkGlobal;
                    ++nbNotOk;
                    // getMemoryLog().logMessage(communication.getErrorMsg(),
                    // FWMessage.ERREUR, getClass().getName());
                }
                if (successful != CPProcessAsciiReader.FIN_FICHIER) {
                    communicationList.addLast(communication);
                } else {
                    nbEntreesGlobal--;
                }
            }
            ecrireFichier(outputFileBase);
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
            successful = CPProcessAsciiReader.EN_ERREUR;
        } finally {
            if ((successful == CPProcessAsciiReader.SUCCES) || (successful == CPProcessAsciiReader.FIN_FICHIER)) {
                setEMailObject(getSession().getLabel("SUJET_EMAIL_RECEPTION_COMMUNICATION_SUCCES"));
            } else {
                setEMailObject(getSession().getLabel("SUJET_EMAIL_RECEPTION_COMMUNICATION_ECHEC"));
            }
            getMemoryLog().logMessage(getSession().getLabel("FICHIER_LU") + getInputFileName(), FWMessage.INFORMATION,
                    this.getClass().getName());
            getMemoryLog().logMessage(getSession().getLabel("NB_COMMUNICATIONS_TOTAL") + getNbEntreesLues(),
                    FWMessage.INFORMATION, this.getClass().getName());
            getMemoryLog().logMessage(getSession().getLabel("NB_COMMUNICATIONS_SUCCES") + getNbOk(),
                    FWMessage.INFORMATION, this.getClass().getName());
            getMemoryLog().logMessage(getSession().getLabel("NB_COMMUNICATIONS_ECHEC") + getNbNotOk(),
                    FWMessage.INFORMATION, this.getClass().getName());
        }
        if (successful == CPProcessAsciiReader.EN_ERREUR) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Verifie que les donnees soient consistantes et coherentes pour lancer le processus
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _validate() throws Exception {
        if (getInputFileName() == null) {
            throw new Exception(getSession().getLabel("PROCASCIIREAD_ERROR_NO_OUTPUT_FILE"));
        } else if (getOutputDirectory() == null) {
            throw new Exception(getSession().getLabel("PROCASCIIREAD_ERROR_NO_OUTPUT_DIR"));
        }
    }

    /**
     * Ajoute un champ au document xml avec la valeur du tmpBuffer exemple: ajouterChamp("Name","Michael") insere
     * " <Name>Michael</Name>"
     * 
     * @param tab
     *            DOCUMENT ME!
     * @param nom
     * @param valeur
     * @throws IOException
     */
    protected void ajouterChamp(int tab, String nom, String valeur) throws IOException {
        writeTab(tab);
        valeur = JadeStringUtil.change(valeur, "&", "&amp;");
        valeur = JadeStringUtil.change(valeur, "<", "&lt;");
        valeur = JadeStringUtil.change(valeur, ">", "&gt;");
        String xmlData = "<" + nom + ">" + valeur + "</" + nom + ">";
        fileOutputWriter.write(xmlData);
    }

    /**
     * Ajoute le text au document xml
     * 
     * @param tab
     *            Nombre de tabulation avant le texte
     * @param text
     * @throws IOException
     */
    protected void ajouterText(int tab, String text) throws IOException {
        writeTab(tab);
        fileOutputWriter.write(text);
    }

    /**
     * Ajoute le text au document xml
     * 
     * @param tab
     *            Nombre de tabulation avant le texte
     * @param text
     * @throws IOException
     */
    protected void ajouterText(String text) throws IOException {
        fileOutputWriter.write(text);
    }

    /**
     * Crée et ajoute l'entete du document xml au fichier en ecriture L'entete contient le nb de communications
     * contenues dans le document et la date de la communication
     * 
     * @return true si l'opération a réussie
     * @throws IOException
     */
    protected boolean ecrireEntete() throws IOException {
        this.ajouterText(1, CPProcessAsciiReader.DEBUT_ENTETE);
        ajouterChamp(2, CPProcessAsciiReader.NB_RECEPTION_COM, getNbEntreesLues());

        // Ajout de la date de réception
        if (JadeStringUtil.isBlank(getDateReception())) {
            Calendar receptionCal = Calendar.getInstance();
            receptionCal.setTime(new Date(fileInput.lastModified()));
            // String day = ((receptionCal.get(Calendar.DAY_OF_MONTH) <
            // 10)?"0":"") +
            // String.valueOf(receptionCal.get(Calendar.DAY_OF_MONTH));
            // String month = ((receptionCal.get(Calendar.MONTH) < 10)?"0":"") +
            // String.valueOf(receptionCal.get(Calendar.MONTH));
            setDateReception(JACalendar.todayJJsMMsAAAA());
            // setDateReception(day
            // + "." + month
            // + "." + String.valueOf(receptionCal.get(Calendar.YEAR)));
        }
        ajouterChamp(2, CPProcessAsciiReader.DATE_RECEPTION_COM, getDateReception());

        // Ajout de la date de transformation
        ajouterChamp(2, CPProcessAsciiReader.DATE_TRANSFORMATION_COM, Calendar.getInstance().getTime().toString());

        ajouterChamp(2, CPProcessAsciiReader.USER_ID, getSession().getUserId());
        this.ajouterText(1, CPProcessAsciiReader.FIN_ENTETE);

        return true;
    }

    /**
     * Ecrit dans un fichier xml les documents recus et chargés. S'il n'y a aucun document, aucun fichier n'est créé.
     * 
     * @return
     * @throws Exception
     */
    protected final boolean ecrireFichier(String outputFileBase) throws Exception {
        // Crée le fichier xml resultant
        boolean successful = true;

        if (nbEntreesLues == 0) {
            return true;
        }
        String curFileName = getOutputDirectory() + outputFileBase + "#" + nbFiles++ + ".xml";
        outputFilePath.add(curFileName);
        File outFile = new File(curFileName);

        outFile.createNewFile();
        fileOutputWriter = new PrintWriter(new BufferedWriter(new FileWriter(outFile)));

        // Crée le document xml a partir des objets communication
        this.ajouterText(CPProcessAsciiReader.ENTETE_DOCUMENT);
        this.ajouterText(0, CPProcessAsciiReader.DEBUT_DOCUMENT);
        successful = ecrireEntete();

        CPReception communication;

        while (communicationList.size() > 0) {
            communication = communicationList.removeFirst();
            communication.ecrireEntree();
        }

        // Ajoute la queue du document
        successful = ecrireQueue();

        this.ajouterText(0, CPProcessAsciiReader.FIN_DOCUMENT);

        fileOutputWriter.close();

        // Reset les compteurs
        nbEntreesLues = 0;
        nbOk = 0;
        nbNotOk = 0;

        return successful;
    }

    /**
     * Crée et ajoute la queue du document xml au fichier en ecriture La queue contient le nombre de communications
     * valides et invalides
     * 
     * @return
     * @throws IOException
     */
    protected final boolean ecrireQueue() throws IOException {
        this.ajouterText(1, CPProcessAsciiReader.DEBUT_QUEUE);
        ajouterChamp(2, CPProcessAsciiReader.NB_OK, getNbOk());
        ajouterChamp(2, CPProcessAsciiReader.NB_NOT_OK, getNbNotOk());
        this.ajouterText(1, CPProcessAsciiReader.FIN_QUEUE);

        return true;
    }

    /**
     * @return
     */
    public String getDateReception() {
        return dateReception;
    }

    /**
     * getter pour l'attribut EMail object
     * 
     * @return la valeur courante de l'attribut EMail object
     */
    @Override
    protected String getEMailObject() {
        return eMailObject;
    }

    /**
     * @return
     */
    public String getInputFileName() {
        return inputFileName;
    }

    /**
     * @return
     */
    public String getNbEntreesLues() {
        return Integer.toString(nbEntreesGlobal);
    }

    /**
     * @return
     * @throws Exception
     *             DOCUMENT ME!
     */
    public int getNbMaxCommunication() throws Exception {
        return Integer.parseInt(((CPApplication) getSession().getApplication()).getNbComMax());
    }

    /**
     * @return
     */
    public String getNbNotOk() {
        return Integer.toString(nbNotOkGlobal);
    }

    /**
     * @return
     */
    public String getNbOk() {
        return Integer.toString(nbOkGlobal);
    }

    /**
     * @return
     */
    public String getOutputDirectory() {
        return outputFileDirectory;
    }

    /**
     * @return
     */
    public ArrayList<String> getOutputFilePath() {
        return outputFilePath;
    }

    /**
     * @param nbEntreesLues
     * @return
     */
    protected abstract CPReception getReception(CPProcessAsciiReader reader, int nbEntreesLues);

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#jobQueue()
     * @return DOCUMENT ME!
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    /**
     * Reset les parametre du reader
     */
    private void reset() {
        nbEntreesLues = 0;
        nbOk = 0;
        nbNotOk = 0;
        nbFiles = 0;
        nbEntreesGlobal = 0;
        nbOkGlobal = 0;
        nbNotOkGlobal = 0;
    }

    /**
     * @param string
     */
    public void setDateReception(String string) {
        dateReception = string;
    }

    // ~ Inner Classes
    // --------------------------------------------------------------------------------------------------

    /**
     * @param string
     */
    public void setEMailObject(String string) {
        eMailObject = string;
    }

    /**
     * @param string
     */
    public void setInputFileName(String string) {
        inputFileName = string;
    }

    /**
     * @param string
     */
    public void setOutputDirectory(String string) {
        outputFileDirectory = string;
    }

    /**
     * Met le curseur à la ligne et écrit des tabuations
     * 
     * @param nbTab
     */
    private void writeTab(int nbTab) {
        fileOutputWriter.write('\n');

        for (int i = 0; i < nbTab; i++) {
            fileOutputWriter.write('\t');
        }
    }

}
