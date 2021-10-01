package ch.globaz.common.ws;

import lombok.Value;

@Value(staticConstructor = "of")
public class ResponseEntity<T> {
    private Integer status;
    private T entity;


    public static <T> ResponseEntity<T> ofKo(){
        return new ResponseEntity<>(null, null);
    }

}
