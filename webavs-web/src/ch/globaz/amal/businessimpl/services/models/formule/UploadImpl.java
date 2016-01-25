package ch.globaz.amal.businessimpl.services.models.formule;

/**
 * Cette classe permet d'uploader un fichier en provenance du poste de travail à destination du webserveur.
 * <p>
 * Pour l'utiliser, procéder de la manière suivante :
 * <ul>
 * <li>dans la page JSP, modifier la balise FORM : enctype="multipart/form-data" method="post">
 * <li>ajouter une balise INPUT de type "file"
 * <li>déclarer le bean : jsp:useBean id="uploadBean" class="globaz.framework.filetransfer.FWFileUpload" scope="page"
 * <li>spécifier le répertoire d'enregistrement sur le webserveur par la méthode uploadBean.setSavePath()
 * <li>dans la balise HEAD, appeler la méthode uploadBean.doUpload(request);
 * <li>le fichier est automatiquement déposé sur le serveur avec le nom getFilename() à l'emplacement getSavePath()
 */
import globaz.jade.common.Jade;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Dictionary;
import java.util.Hashtable;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

public class UploadImpl {

    private Dictionary fields;
    private String savePath, filepath, filename, contentType;

    public void doUpload(HttpServletRequest request) throws IOException {
        // request.setCharacterEncoding("ANSI");
        ServletInputStream in = request.getInputStream();
        File file = new File(Jade.getInstance().getPersistenceDir() + "upload.txt");
        FileOutputStream os = new FileOutputStream(file);
        final int BUFFER_SIZE = 1024;

        byte[] line = new byte[BUFFER_SIZE];
        int i = in.readLine(line, 0, BUFFER_SIZE);
        os.write(line);
        if (i < 3) {
            return;
        }
        int boundaryLength = i - 2;

        String boundary = new String(line, 0, boundaryLength);
        // -2 discards the newline character
        fields = new Hashtable();

        while (i != -1) {
            String newLine = new String(line, 0, i, "UTF-8");
            if (newLine.startsWith("Content-Disposition: form-data; name=\"")) {
                if (newLine.indexOf("filename=\"") != -1) {
                    setFilename(new String(line, 0, i - 2));
                    if (filename == null) {
                        return;
                    }
                    // this is the file content
                    i = in.readLine(line, 0, BUFFER_SIZE);
                    os.write(line);
                    setContentType(new String(line, 0, i - 2));
                    i = in.readLine(line, 0, BUFFER_SIZE);
                    os.write(line);
                    // blank line
                    i = in.readLine(line, 0, BUFFER_SIZE);
                    os.write(line);
                    newLine = new String(line, 0, i, "UTF-8");

                    PrintWriter pw = new PrintWriter((new BufferedWriter(new FileWriter((savePath == null ? ""
                            : savePath) + filename))));
                    File filetest = new File((savePath == null ? "" : savePath) + filename);
                    PrintWriter pw1 = new PrintWriter((new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
                            filetest), "UTF8"))));

                    while ((i != -1) && !newLine.startsWith(boundary)) {
                        // the problem is the last line of the file content
                        // contains the new line character.
                        // So, we need to check if the current line is
                        // the last line.
                        i = in.readLine(line, 0, BUFFER_SIZE);
                        os.write(line);
                        if (((i == boundaryLength + 2) || (i == boundaryLength + 4)) // + 4
                                                                                     // is
                                                                                     // eof
                                && (new String(line, 0, i).startsWith(boundary))) {
                            pw.print(newLine.substring(0, newLine.length() - 2));
                        } else {
                            pw.print(newLine);
                        }
                        newLine = new String(line, 0, i, "UTF-8");

                    }
                    pw.close();

                } else {
                    // this is a field
                    // get the field name
                    int pos = newLine.indexOf("name=\"");
                    String fieldName = newLine.substring(pos + 6, newLine.length() - 3);
                    // System.out.println("fieldName:" + fieldName);
                    // blank line
                    i = in.readLine(line, 0, BUFFER_SIZE);
                    os.write(line);
                    i = in.readLine(line, 0, BUFFER_SIZE);
                    os.write(line);
                    newLine = new String(line, 0, i, "UTF-8");
                    StringBuffer fieldValue = new StringBuffer(BUFFER_SIZE);
                    while ((i != -1) && !newLine.startsWith(boundary)) {
                        // The last line of the field
                        // contains the new line character.
                        // So, we need to check if the current line is
                        // the last line.
                        i = in.readLine(line, 0, BUFFER_SIZE);
                        os.write(line);
                        if (((i == boundaryLength + 2) || (i == boundaryLength + 4)) // + 4
                                                                                     // is
                                                                                     // eof
                                && (new String(line, 0, i).startsWith(boundary))) {
                            fieldValue.append(newLine.substring(0, newLine.length() - 2));
                        } else {
                            fieldValue.append(newLine);
                        }
                        newLine = new String(line, 0, i, "UTF-8");
                    }
                    // System.out.println("fieldValue:" +
                    // fieldValue.toString());
                    fields.put(fieldName, fieldValue.toString());
                }
            }
            i = in.readLine(line, 0, BUFFER_SIZE);
            os.write(line);

        } // end while
        os.close();
    }

    public String getContentType() {
        return contentType;
    }

    public String getFieldValue(String fieldName) {
        if ((fields == null) || (fieldName == null)) {
            return null;
        }
        return (String) fields.get(fieldName);
    }

    public String getFilename() {
        return filename;
    }

    public String getFilepath() {
        return filepath;
    }

    public String getSavePath() {
        return savePath;
    }

    private void setContentType(String s) {
        if (s == null) {
            return;
        }

        int pos = s.indexOf(": ");
        if (pos != -1) {
            contentType = s.substring(pos + 2, s.length());
        }
    }

    private void setFilename(String s) {
        if (s == null) {
            return;
        }

        int pos = s.indexOf("filename=\"");
        if (pos != -1) {
            filepath = s.substring(pos + 10, s.length() - 1);
            // Windows browsers include the full path on the client
            // But Linux/Unix and Mac browsers only send the filename
            // test if this is from a Windows browser
            pos = filepath.lastIndexOf("\\");
            if (pos != -1) {
                filename = filepath.substring(pos + 1);
            } else {
                filename = filepath;
            }
        }
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }
}
