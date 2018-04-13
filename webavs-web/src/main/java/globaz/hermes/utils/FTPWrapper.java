/*
 * Cr�� le 7 juil. 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.hermes.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * @author ald Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class FTPWrapper extends FtpClient {
    /** Delete a remote file */
    public boolean deleteFile(String fileName) throws IOException {
        issueCommand("DELE " + fileName);
        return isValidResponse();
    }

    /** Download a file from the server, and save it to the specified local file */
    public boolean downloadFile(String serverFile, String localFile) throws IOException {
        return downloadFile(serverFile, localFile, true);
    }

    public boolean downloadFile(String serverFile, String localFile, boolean append) throws IOException {
        int i = 0;
        byte[] bytesIn = new byte[1024];
        FileOutputStream out;
        out = new FileOutputStream(localFile, append);
        BufferedInputStream in = new BufferedInputStream(get(serverFile));
        // ensuite ajouter les nouvelles donn�es provenant du ftp.
        while ((i = in.read(bytesIn)) >= 0) {
            out.write(bytesIn, 0, i);
        }
        out.close();
        return true;
    }

    /** Get the response code from the last command that was sent */
    public int getResponseCode() throws NumberFormatException {
        return Integer.parseInt(getResponseString().substring(0, 3));
    }

    public boolean isFileExist(String serverFile) throws IOException {
        ArrayList listFile = listRaw();
        for (int i = 0; i < listFile.size(); i++) {
            String crt = listFile.get(i).toString();
            int index = crt.indexOf(serverFile);
            // on v�rifie encore que ce qu'on a trouv� un un espace devant et
            // rien apr�s
            if (index != -1 && index != 0 && ' ' == crt.charAt(index - 1)
                    && crt.length() == index + serverFile.length()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Send a raw FTP command to the server. You can get the response by calling getResponseString (which returns the
     * entire response as a single String) or getResponseStrings (which returns the response as a Vector).
     */
    public int issueRawCommand(String command) throws IOException {
        return issueCommand(command);
    }

    /**
     * Return true if the last response code was in the 200 range, false otherwise
     */
    public boolean isValidResponse() {
        try {
            int respCode = getResponseCode();
            return (respCode >= 200 && respCode < 300);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get the results of the LIST command as a Vector of Strings. Because there's no standard format for the results of
     * a LIST command, it's hard to tell what resulting data will look like. Just be aware that different servers have
     * different ways of returning your LIST data.
     */
    public ArrayList listRaw() throws IOException {
        String fileName;
        ArrayList ftpList = new ArrayList();
        BufferedReader reader = new BufferedReader(new InputStreamReader(list()));
        while ((fileName = reader.readLine()) != null) {
            ftpList.add(fileName);
        }
        return ftpList;
    }

    public boolean uploadFile(FileInputStream in, String serverFile) throws IOException {
        int i = 0;
        byte[] bytesIn = new byte[1024];

        BufferedOutputStream out = new BufferedOutputStream(put(serverFile));
        while ((i = in.read(bytesIn)) >= 0) {
            out.write(bytesIn, 0, i);
        }
        out.close();
        return true;
    }

    /** Upload a file to the server */
    public boolean uploadFile(String localFile, String serverFile) throws IOException {
        FileInputStream in = new FileInputStream(localFile);
        boolean res = uploadFile(in, serverFile);
        in.close();
        return res;
    }

    /**
     * Cr�er un fichier dans le sous-r�pertoire (optionnel) du server ftp � partir d'un flux.
     * 
     * @param in
     * @param serverFile
     * @throws IOException
     */
    public void uploadStream(ByteArrayInputStream in, String serverFile) throws IOException {
        int i = 0;
        byte[] bytesIn = new byte[1024];

        BufferedOutputStream out = new BufferedOutputStream(put(serverFile));
        while ((i = in.read(bytesIn)) >= 0) {
            out.write(bytesIn, 0, i);
        }
        out.close();
    }
}
