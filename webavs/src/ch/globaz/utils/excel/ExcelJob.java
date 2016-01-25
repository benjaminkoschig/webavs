package ch.globaz.utils.excel;

import globaz.jade.job.AbstractJadeJob;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.op.excelml.model.document.ExcelmlWorkbook;

/**
 * <p>
 * Permet de g�n�rer un fichier Excel et de le sauver (sur le serveur) pour ensuite l'envoyer par email ou permettre son
 * t�l�chargement par la notation javascript <code>globazDownload</code>
 * </p>
 * <p>
 * Ne g�re pas la mise en GED.
 * </p>
 * 
 * @author PBA
 * 
 * @param <T>
 *            un g�n�rateur de fichier Excel (h�ritant de {@link ExcelAbstractDocumentGenerator})
 */
public class ExcelJob<T extends ExcelAbstractDocumentGenerator> extends AbstractJadeJob {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String description;
    private T documentGenerator;
    private String email;
    private String filePath;
    private String name;
    private boolean sendFileByMail;

    /**
     * <p>
     * Construit un job pour construire un fichier Excel avec un email vide (� d�finir avant de lancer le job)
     * </p>
     * <p>
     * Par d�faut, le fichier Excel ne sera pas envoy� � l'email apr�s sa g�n�ration.
     * </p>
     */
    public ExcelJob(T documentGenerator) {
        this(documentGenerator, "", false);
    }

    /**
     * <p>
     * Construit un job pour construire un fichier Excel avec les donn�es pass�es en param�tre
     * </p>
     * 
     * @param email
     *            une adresse email valide
     * @param sendFileByEmail
     *            d�fini si le fichier Excel sera envoy� � l'email apr�s sa g�n�ration
     */
    public ExcelJob(T documentGenerator, String email, boolean sendFileByEmail) {
        this(documentGenerator, email, sendFileByEmail, "This is an excel generator", "Excel generator");
    }

    public ExcelJob(T documentGenerator, String email, boolean sendFileByEmail, String jobDescription, String jobName) {
        super();

        this.documentGenerator = documentGenerator;

        this.email = email;
        this.filePath = "";
        this.sendFileByMail = sendFileByEmail;

        this.description = jobDescription;
        this.name = jobName;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    public String getEmail() {
        return this.email;
    }

    /**
     * <p>
     * Retourne le chemin complet o� a �t� sauv� le document Excel apr�s sa g�n�ration.
     * </p>
     * <p>
     * Si la g�n�ration n'a pas encore eu lieu, retournera une cha�ne vide.
     * </p>
     * 
     * @return le chemin complet du fichier Excel ou vide si le fichier n'est pas encore existant
     */
    public String getFilePath() {
        return this.filePath;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void run() {
        try {
            ExcelmlWorkbook document = this.documentGenerator.createDoc();
            this.filePath = this.documentGenerator.save(document);

            if (this.willSendFileByMail()) {
                this.sendFileByEmail();
            }
        } catch (Exception ex) {
            this.sendErrorByEmail(ex);
            getLogSession().error(this.getName(), ex.getMessage());
        }
    }

    private void sendErrorByEmail(Exception exception) {
        try {
            JadeSmtpClient.getInstance().sendMail(this.getEmail(), this.getDescription(), exception.getMessage(), null);
        } catch (Exception ex) {
            getLogSession().error(this.getName(), ex.getMessage());
        }
    }

    private void sendFileByEmail() throws Exception {
        JadeSmtpClient.getInstance().sendMail(this.getEmail(), this.getDescription(), "",
                new String[] { this.filePath });
    }

    /**
     * D�fini l'adresse qui sera utilis�e pour notifier les erreurs dans le job, et/ou envoyer le fichier Excel si cela
     * a �t� d�fini par la m�thode {@link #setSendFileByMail(boolean)}
     * 
     * @param email
     *            une adresse email valide
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * D�fini si le job doit envoyer le fichier Excel g�n�r� par email (� l'adresse sp�cifi�e avec
     * {@link #setEmail(String)})
     * 
     * @param sendFileByMail
     */
    public void setSendFileByMail(boolean sendFileByMail) {
        this.sendFileByMail = sendFileByMail;
    }

    /**
     * D�fini si le job doit envoyer le fichier Excel g�n�r� par email (� l'adresse sp�cifi�e avec
     * {@link #setEmail(String)})
     * 
     * @return
     */
    public boolean willSendFileByMail() {
        return this.sendFileByMail;
    }
}
