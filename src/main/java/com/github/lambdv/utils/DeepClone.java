package com.github.lambdv.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Utility class for deep cloning objects.
 * @author Marco Servetto
 */
public class DeepClone {
    @SuppressWarnings ("unchecked") 
    
    public static <T> T deepCopy(T orig) {
        ByteArrayOutputStream aux = new ByteArrayOutputStream();
        try (ObjectOutputStream out = new ObjectOutputStream(aux) ) {
            out.writeObject(orig);
            out.flush(); 
        }catch(IOException e){throw new Error(e);}

        try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(aux.toByteArray()))){
            return (T) in.readObject(); 
        }
        catch(IOException | ClassNotFoundException e){throw new Error(e);}
    }
}
