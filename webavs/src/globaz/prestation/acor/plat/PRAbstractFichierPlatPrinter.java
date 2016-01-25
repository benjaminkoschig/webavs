package globaz.prestation.acor.plat;

import globaz.globall.db.BSession;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.globall.util.JAStringFormatter;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.acor.PRACORException;
import globaz.prestation.acor.PRFichierACORPrinter;
import globaz.prestation.application.PRAbstractApplication;
import java.io.PrintWriter;
import java.math.BigDecimal;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Classe de base pour l'implémentation d'un printer de fichiers ACOR. Cette classe fournit un ensemble de méthode pour
 * l'écriture de champs métiers dans le writer.
 * </p>
 * 
 * <p>
 * Il est recommandé de passer par cette classe pour la création d'un printer de fichiers car les méthodes effectuent
 * quelques tests et inscrivent des valeurs par défaut valides pour le système ACOR.
 * </p>
 * 
 * @author vre
 */
public abstract class PRAbstractFichierPlatPrinter implements PRFichierACORPrinter {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    protected String nomFichier;
    protected PRAbstractPlatAdapter parent;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe FichierACORWriterImpl.
     * 
     * @param parent
     *            DOCUMENT ME!
     * @param nomFichier
     *            DOCUMENT ME!
     */
    protected PRAbstractFichierPlatPrinter(PRAbstractPlatAdapter parent, String nomFichier) {
        this.parent = parent;
        this.nomFichier = nomFichier;
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /** 
     */
    @Override
    public void dispose() {
        // casse le cycle de references reciproques adapteur - fichier et permet
        // de garbage-collecter le tout
        parent = null;
    }

    /**
     * getter pour l'attribut nom fichier.
     * 
     * @return la valeur courante de l'attribut nom fichier
     */
    @Override
    public String getNomFichier() {
        return nomFichier;
    }

    /**
     * getter pour l'attribut session.
     * 
     * @return la valeur courante de l'attribut session
     */
    protected BSession getSession() {
        return parent.getSession();
    }

    /**
     * par défaut, on ne crée pas de fichiers vides.
     * 
     * @return la valeur courante de l'attribut forcer fichier vide
     */
    @Override
    public boolean isForcerFichierVide() {
        return false;
    }

    /**
     * Inscrit un numéro AVS dans un format quelconque (il sera préalablement déformatté).
     * 
     * @param writer
     *            le writer dans lequel écrire.
     * @param valeur
     *            le numéro à écrire (si null ou vide, inscrit "0")
     */
    protected void writeAVS(PrintWriter writer, String valeur) {
        this.writeAVSSansFinDeChamp(writer, valeur);
        this.writeFinChamp(writer);
    }

    protected void writeAVS(StringBuffer cmd, String valeur) {
        this.writeAVSSansFinDeChamp(cmd, valeur);
        this.writeFinChamp(cmd);
    }

    /**
     * pareil que writeAVS mais sans fin de champ.
     * 
     * @param writer
     *            DOCUMENT ME!
     * @param valeur
     *            DOCUMENT ME!
     */
    protected void writeAVSSansFinDeChamp(PrintWriter writer, String valeur) {
        if (JadeStringUtil.isBlank(valeur)) {
            writer.print(PRACORConst.CA_NSS_VIDE);
        } else {
            writer.print(JAStringFormatter.deformatAvs(valeur));
        }
    }

    protected void writeAVSSansFinDeChamp(StringBuffer cmd, String valeur) {
        if (JadeStringUtil.isBlank(valeur)) {
            cmd.append(PRACORConst.CA_NSS_VIDE);
        } else {
            cmd.append(JAStringFormatter.deformatAvs(valeur));
        }
    }

    /**
     * inscrit une valeur boolean sous forme de CA_VRAI, CA_FAUX.
     * 
     * @param writer
     *            DOCUMENT ME!
     * @param valeur
     *            DOCUMENT ME!
     */
    protected void writeBoolean(PrintWriter writer, boolean valeur) {
        this.writeBooleanSansFinDeChamp(writer, valeur);
        this.writeFinChamp(writer);
    }

    protected void writeBoolean(StringBuffer cmd, boolean valeur) {
        this.writeBooleanSansFinDeChamp(cmd, valeur);
        this.writeFinChamp(cmd);
    }

    /**
     * @param writer
     *            DOCUMENT ME!
     * @param valeur
     *            DOCUMENT ME!
     */
    protected void writeBooleanSansFinDeChamp(PrintWriter writer, boolean valeur) {
        writer.write(valeur ? PRACORConst.CA_VRAI : PRACORConst.CA_FAUX);
    }

    protected void writeBooleanSansFinDeChamp(StringBuffer cmd, boolean valeur) {
        cmd.append(valeur ? PRACORConst.CA_VRAI : PRACORConst.CA_FAUX);
    }

    /**
     * Inscrit une chaine de caractères quelconque.
     * 
     * @param writer
     *            le writer dans lequel écrire.
     * @param valeur
     *            la valeur à écrire (si null ou vide, inscrit " ")
     */
    protected void writeChaine(PrintWriter writer, String valeur) {
        this.writeChaineSansFinDeChamp(writer, valeur);
        this.writeFinChamp(writer);
    }

    protected void writeChaine(StringBuffer cmd, String valeur) {
        this.writeChaineSansFinDeChamp(cmd, valeur);
        this.writeFinChamp(cmd);
    }

    /**
     * pareil que writeChaine mais sans fin de champ.
     * 
     * @param writer
     *            DOCUMENT ME!
     * @param valeur
     *            DOCUMENT ME!
     */
    protected void writeChaineSansFinDeChamp(PrintWriter writer, String valeur) {
        if (JadeStringUtil.isBlank(valeur)) {
            this.writeChampVideSansFinDeChamp(writer);
        } else {
            writer.print(valeur);
        }
    }

    protected void writeChaineSansFinDeChamp(StringBuffer cmd, String valeur) {
        if (JadeStringUtil.isBlank(valeur)) {
            this.writeChampVideSansFinDeChamp(cmd);
        } else {
            cmd.append(valeur);
        }
    }

    /**
     * Inscrit un champ vide (C'est à dire " ").
     * 
     * @param writer
     *            DOCUMENT ME!
     */
    protected void writeChampVide(PrintWriter writer) {
        this.writeChampVideSansFinDeChamp(writer);
        this.writeFinChamp(writer);
    }

    protected void writeChampVide(StringBuffer cmd) {
        this.writeChampVideSansFinDeChamp(cmd);
        this.writeFinChamp(cmd);
    }

    /**
     * pareil que writeChampVide mais sans fin de champ.
     * 
     * @param writer
     *            DOCUMENT ME!
     */
    protected void writeChampVideSansFinDeChamp(PrintWriter writer) {
        writer.print(PRACORConst.CA_CHAINE_VIDE);
    }

    protected void writeChampVideSansFinDeChamp(StringBuffer cmd) {
        cmd.append(PRACORConst.CA_CHAINE_VIDE);
    }

    /**
     * Inscrit une date dans un format compatible avec JADate sous la forme AAAAMMJJ.
     * 
     * @param writer
     *            le writer dans lequel écrire.
     * @param valeur
     *            la valeur à écrire (si null ou vide, inscrit "0")
     */
    protected void writeDate(PrintWriter writer, String valeur) {
        this.writeDateSanFinDeChamp(writer, valeur);
        this.writeFinChamp(writer);
    }

    protected void writeDate(StringBuffer cmd, String valeur) {
        this.writeDateSanFinDeChamp(cmd, valeur);
        this.writeFinChamp(cmd);
    }

    /**
     * Inscrit une date dans un format compatible avec JADate sous la forme AAAAMM.
     * 
     * @param writer
     *            le writer dans lequel écrire.
     * @param valeur
     *            mm.aaaa la valeur à écrire (si null ou vide, inscrit "0")
     */
    protected void writeDateAAAAMM(PrintWriter writer, String valeur) {
        this.writeDateAAAAMMSanFinDeChamp(writer, valeur);
        this.writeFinChamp(writer);
    }

    protected void writeDateAAAAMM(StringBuffer cmd, String valeur) {
        this.writeDateAAAAMMSanFinDeChamp(cmd, valeur);
        this.writeFinChamp(cmd);
    }

    /**
     * pareil que writeDate mais sans l'ajout du délimiteur de fin de champ.
     * 
     * @param writer
     *            DOCUMENT ME!
     * @param valeur
     *            mm.aaaa ME!
     */
    protected void writeDateAAAAMMSanFinDeChamp(PrintWriter writer, String valeur) {
        if (JAUtil.isDateEmpty(valeur)) {
            writer.print(PRACORConst.CA_DATE_VIDE_6POS);
        } else {
            try {
                JADate date = new JADate(valeur);

                writer.write(date.toStrAMJ().substring(0, 6));
            } catch (JAException e) {
                writer.print(PRACORConst.CA_DATE_VIDE_6POS);
            }
        }
    }

    protected void writeDateAAAAMMSanFinDeChamp(StringBuffer cmd, String valeur) {
        if (JAUtil.isDateEmpty(valeur)) {
            cmd.append(PRACORConst.CA_DATE_VIDE_6POS);
        } else {
            try {
                JADate date = new JADate(valeur);

                cmd.append(date.toStrAMJ().substring(0, 6));
            } catch (JAException e) {
                cmd.append(PRACORConst.CA_DATE_VIDE_6POS);
            }
        }
    }

    /**
     * pareil que writeDate mais sans l'ajout du délimiteur de fin de champ.
     * 
     * @param writer
     *            DOCUMENT ME!
     * @param valeur
     *            DOCUMENT ME!
     */
    protected void writeDateSanFinDeChamp(PrintWriter writer, String valeur) {
        if (JAUtil.isDateEmpty(valeur)) {
            writer.print(PRACORConst.CA_DATE_VIDE);
        } else {
            try {
                JADate date = new JADate(valeur);

                writer.write(date.toStrAMJ());
            } catch (JAException e) {
                writer.print(PRACORConst.CA_DATE_VIDE);
            }
        }
    }

    protected void writeDateSanFinDeChamp(StringBuffer cmd, String valeur) {
        if (JAUtil.isDateEmpty(valeur)) {
            cmd.append(PRACORConst.CA_DATE_VIDE);
        } else {
            try {
                JADate date = new JADate(valeur);

                cmd.append(date.toStrAMJ());
            } catch (JAException e) {
                cmd.append(PRACORConst.CA_DATE_VIDE);
            }
        }
    }

    /**
     * Inscrit un nombre entier.
     * 
     * @param writer
     *            le writer dans lequel écrire.
     * @param valeur
     *            la valeur à écrire (si null ou vide, inscrit "0")
     */
    protected void writeEntier(PrintWriter writer, String valeur) {
        this.writeEntierSansFinDeChamp(writer, valeur);
        this.writeFinChamp(writer);
    }

    protected void writeEntier(StringBuffer cmd, String valeur) {
        this.writeEntierSansFinDeChamp(cmd, valeur);
        this.writeFinChamp(cmd);
    }

    /**
     * pareil que writeNombre mais sans fin de champ.
     * 
     * @param writer
     *            DOCUMENT ME!
     * @param valeur
     *            DOCUMENT ME!
     */
    protected void writeEntierSansFinDeChamp(PrintWriter writer, String valeur) {
        if (JadeStringUtil.isIntegerEmpty(valeur)) {
            writer.write(PRACORConst.CA_ENTIER_VIDE);
        } else {
            writer.print(valeur);
        }
    }

    protected void writeEntierSansFinDeChamp(StringBuffer cmd, String valeur) {
        if (JadeStringUtil.isIntegerEmpty(valeur)) {
            cmd.append(PRACORConst.CA_ENTIER_VIDE);
        } else {
            cmd.append(valeur);
        }
    }

    /**
     * Inscrit un le ou les caractères de délimitation de fin de champ.
     * 
     * @param writer
     *            DOCUMENT ME!
     */
    protected void writeFinChamp(PrintWriter writer) {
        writer.print(parent.getDelimiteurChamps());
    }

    protected void writeFinChamp(StringBuffer cmd) {
        cmd.append(parent.getDelimiteurChamps());
    }

    /**
     * Inscrit un numéro d'affilie formaté ou non.
     * 
     * @param writer
     *            le writer dans lequel écrire.
     * @param valeur
     *            la valeur à écrire (si null ou vide, inscrit ".")
     * 
     * @throws PRACORException
     *             si le numéro d'affilié ne peut être formatté correctement.
     */
    protected void writeNumAffilie(PrintWriter writer, String valeur) throws PRACORException {
        this.writeNumAffilieSansFinDeChamp(writer, valeur);
        this.writeFinChamp(writer);
    }

    protected void writeNumAffilie(StringBuffer cmd, String valeur) throws PRACORException {
        this.writeNumAffilieSansFinDeChamp(cmd, valeur);
        this.writeFinChamp(cmd);
    }

    /**
     * pareil que writeNumAffilie mais sans fin de champ.
     * 
     * @param writer
     *            DOCUMENT ME!
     * @param valeur
     *            DOCUMENT ME!
     * 
     * @throws PRACORException
     *             si le numéro d'affilié ne peut être formatté correctement.
     */
    protected void writeNumAffilieSansFinDeChamp(PrintWriter writer, String valeur) throws PRACORException {
        if (JadeStringUtil.isBlank(valeur)) {
            writer.print(".");
        } else {
            try {
                writer.print(PRAbstractApplication.getAffileFormater().format(valeur));
            } catch (Exception e) {
                throw new PRACORException();
            }
        }
    }

    protected void writeNumAffilieSansFinDeChamp(StringBuffer cmd, String valeur) throws PRACORException {
        if (JadeStringUtil.isBlank(valeur)) {
            cmd.append(".");
        } else {
            try {
                String numAffilieFormate = PRAbstractApplication.getAffileFormater().format(valeur);
                // Numéro d'affilié trop long au valais (reformatage à la sortie)
                if (numAffilieFormate.length() > 13) {
                    numAffilieFormate = PRAbstractApplication.getAffileFormater().unformat(numAffilieFormate);
                }
                cmd.append(numAffilieFormate);
            } catch (Exception e) {
                throw new PRACORException();
            }
        }
    }

    /**
     * inscrit une valeur boolean sous forme de CA_OUI ou CA_NON.
     * 
     * @param writer
     *            DOCUMENT ME!
     * @param valeur
     *            DOCUMENT ME!
     */
    protected void writeOuiNon(PrintWriter writer, boolean valeur) {
        this.writeOuiNonSansFinDeChamp(writer, valeur);
        this.writeFinChamp(writer);
    }

    protected void writeOuiNon(StringBuffer cmd, boolean valeur) {
        this.writeOuiNonSansFinDeChamp(cmd, valeur);
        this.writeFinChamp(cmd);
    }

    /**
     * @param writer
     *            DOCUMENT ME!
     * @param valeur
     *            DOCUMENT ME!
     */
    protected void writeOuiNonSansFinDeChamp(PrintWriter writer, boolean valeur) {
        writer.write(valeur ? PRACORConst.CA_OUI : PRACORConst.CA_NON);
    }

    protected void writeOuiNonSansFinDeChamp(StringBuffer cmd, boolean valeur) {

        cmd.append(valeur ? PRACORConst.CA_OUI : PRACORConst.CA_NON);
    }

    /**
     * Inscrit un nombre entier.
     * 
     * @param writer
     *            le writer dans lequel écrire.
     * @param valeur
     *            la valeur à écrire (si null ou vide, inscrit "0")
     */
    protected void writeReel(PrintWriter writer, String valeur) {
        this.writeReelSansFinDeChamp(writer, valeur);
        this.writeFinChamp(writer);
    }

    protected void writeReel(StringBuffer cmd, String valeur) {
        this.writeReelSansFinDeChamp(cmd, valeur);
        this.writeFinChamp(cmd);
    }

    protected void writeTaux(StringBuffer cmd, String valeur) {
        BigDecimal taux = new BigDecimal(valeur);
        taux = taux.divide(new BigDecimal(100));
        writeReel(cmd, taux.toString());
    }

    /**
     * pareil que writeNombre mais sans fin de champ.
     * 
     * @param writer
     *            DOCUMENT ME!
     * @param valeur
     *            DOCUMENT ME!
     */
    protected void writeReelSansFinDeChamp(PrintWriter writer, String valeur) {
        if (JadeStringUtil.isDecimalEmpty(valeur)) {
            writer.write(PRACORConst.CA_REEL_VIDE);
        } else {
            writer.print(valeur);
        }
    }

    protected void writeReelSansFinDeChamp(StringBuffer cmd, String valeur) {
        if (JadeStringUtil.isDecimalEmpty(valeur)) {
            cmd.append(PRACORConst.CA_REEL_VIDE);
        } else {
            cmd.append(valeur);
        }
    }
}
