package ch.globaz.al.utils;

import globaz.jade.exception.JadeApplicationException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import ch.globaz.al.business.exceptions.utils.ALUtilsException;

/**
 * Fournit les outils nécessaire à la copie d'un objet sérialisé
 * 
 * @author JER/VYJ
 */
public class ALDeepCopy {

    /**
     * Effectue une copie de l'objet passé en paramètre
     * 
     * @param serializable
     *            Objet serializable a copier
     * @return Une copie de l'objet passé en paramètre
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public static Serializable copy(Serializable serializable) throws JadeApplicationException {
        ObjectOutputStream outputStream = null;
        ObjectInputStream inputStream = null;
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        ByteArrayInputStream arrayInputStream = null;
        try {
            outputStream = new ObjectOutputStream(arrayOutputStream);
            outputStream.writeObject(serializable);
            outputStream.flush();
            arrayInputStream = new ByteArrayInputStream(arrayOutputStream.toByteArray());
            inputStream = new ObjectInputStream(arrayInputStream);
            return (Serializable) inputStream.readObject();
        } catch (IOException e) {
            throw new ALUtilsException(e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            throw new ALUtilsException(e.getMessage(), e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                    inputStream = null;
                } catch (IOException e) {
                    throw new ALUtilsException(e.getMessage(), e);
                }
            }
            if (arrayInputStream != null) {
                try {
                    arrayInputStream.close();
                    arrayInputStream = null;
                } catch (IOException e) {
                    throw new ALUtilsException(e.getMessage(), e);
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                    outputStream = null;
                } catch (IOException e) {
                    throw new ALUtilsException(e.getMessage(), e);
                }
            }
            if (arrayOutputStream != null) {
                try {
                    arrayOutputStream.close();
                    arrayOutputStream = null;
                } catch (IOException e) {
                    throw new ALUtilsException(e.getMessage(), e);
                }
            }
        }
    }
}
