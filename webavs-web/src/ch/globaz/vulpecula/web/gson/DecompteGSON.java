package ch.globaz.vulpecula.web.gson;

import java.io.Serializable;

/**
 * @author Arnaud Geiser (AGE) | Cr�� le 11 avr. 2014
 * 
 */
public class DecompteGSON implements Serializable {
    public String idDecompte;
    public String employeur;
    public String adresseEmployeur;
    public String noDecompte;
    public String descriptionDecompte;
    public String typeDecompte;
}
