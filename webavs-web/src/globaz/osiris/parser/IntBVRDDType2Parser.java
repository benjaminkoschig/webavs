package globaz.osiris.parser;

import globaz.framework.util.FWMemoryLog;
import globaz.globall.db.BSession;
import java.io.BufferedReader;

/**
 * @author jts 6 sept. 05 11:40:36
 */
public interface IntBVRDDType2Parser {

    String FOOTER = "F";
    String GENRE_CREDIT = "D";
    String GENRE_DEBIT = "C";
    String TRANSACTION = "T";

    /**
     * Retourne la cl� de tri (enregistrement total
     * 
     * @return la cl� de tri
     */
    public String getCleTri();

    /**
     * Retourne le code monnaie ISO pour �critures au cr�dit et refus/contre-passations (CHF ou EUR)
     * 
     * @return code monnaie ISO
     */
    public String getCodeMonnaieISO();

    /**
     * Retourne le code monnaie ISO du prix (CHF ou EUR)
     * 
     * @return code monnaie ISO du prix
     */
    public String getCodeMonnaieISOPrix();

    /**
     * Retourne le code de refus/contre-passation refus : 01, 03 � 07 contre-passation : 02
     * 
     * @return code de refus/contre-passation
     */
    public String getCodeRefus();

    public String getCurrentBuffer();

    /**
     * Retourne la date d'�ch�ance de l'ordre livr�
     * 
     * @return date d'�ch�ance
     */
    public String getDateEcheance();

    /**
     * Retourne la date d'�criture au cr�dit/d�bit
     * 
     * @return date d'�criture
     */
    public String getDateEcriture();

    public String getDateEtablissementSupport();

    /**
     * Retourne la date valeur
     * 
     * @return date valeur
     */
    public String getDateValeur();

    public boolean getEchoToConsole();

    /**
     * Retourne le code du genre de livraison (1 = original, 2 = double, 3 = test)
     * 
     * @return code du genre de livraison
     */
    public String getGenreLivraison();

    /**
     * Retourne le code du genre de transaction (081 ou 084)
     * 
     * @return genre de transaction
     */
    public String getGenreTransaction();

    public BufferedReader getInputReader();

    public FWMemoryLog getMemoryLog();

    /**
     * Retourne le montant
     * 
     * @return le montant
     */
    public String getMontant();

    /**
     * Retourne le montant des contre-passations
     * 
     * @return le montant des contre-passations
     */
    public String getMontantContrePassation();

    /**
     * Retourne le montant des refus
     * 
     * @return le montant des refus
     */
    public String getMontantRefus();

    /**
     * Retourne le nombre de transactions en contre-passation
     * 
     * @return le nombre de transactions en contre-passation
     */
    public String getNbrTransactionsContrePassation();

    /**
     * Retourne le nombre de transaction �critures au cr�dit
     * 
     * @return le nombre de transaction
     */
    public String getNbrTransactionsEcrituresCredit();

    /**
     * Retourne le nombre de transaction en refus
     * 
     * @return le nombre de transaction en refus
     */
    public String getNbrTransactionsRefus();

    /**
     * Retourne le num�ro de Compte Jaune du d�biteur
     * 
     * @return num�ro de compte
     */
    public String getNumCompteDeb();

    /**
     * Retourne le num�ro courant de transaction dans un ordre DD
     * 
     * @return num�ro courant de transaction
     */
    public String getNumCourantTransaction();

    /**
     * Retourne le num�ro d'adh�rent (format XXXXXC)
     * 
     * @return le num�ro d'adh�rent
     */
    public String getNumeroAdherent();

    /**
     * Retourne le num�ro de r�f�rence
     * 
     * @return le num�ro de r�f�rence
     */
    public String getNumeroReference();

    /**
     * Retourne le num�ro d'ordre DD de l'adh�rent
     * 
     * @return num�ro d'ordre DD de l'adh�rent
     */
    public String getNumOrdreDDAdherent();

    /**
     * Retourne le prix pour �critures au cr�dit et refus/contre-passation
     * 
     * @return prix
     */
    public String getPrix();

    /**
     * Retourne le num�ro d'identification de la transaction interne � PostFinance
     * 
     * @return r�f�rence de remise
     */
    public String getReferenceInterne();

    BSession getSession();

    /**
     * Retourne le prix total
     * 
     * @return le prix total
     */
    public String getTotalPrix();

    boolean parseNextElement();

    public void setEchoToConsole(boolean newEchoToConsole);

    public void setInputReader(BufferedReader newInputReader);

    public void setMemoryLog(FWMemoryLog newMemoryLog);

    void setSession(globaz.globall.db.BSession session);
}
