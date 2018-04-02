package cipher;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * A cipher that both encrypts and decrypts.
 */
public interface Cipher {

   /**
    * Encrypts a message from the input stream using this cipher and sends the
    * result to the output stream.
    * 
    * @param in
    *           The InputStream the plaintext is on
    * @param out
    *           The OutputStream to send the ciphertext to
    * @throws IOException
    */
   public void encrypt(InputStream in, OutputStream out) throws IOException;

   /**
    * Decrypts a message from the input stream according to this cipher's
    * decryption protocol and sends the result to the output stream.
    * 
    * @param in
    *           The InputStream the ciphertext is on
    * @param out
    *           The OutputStream to send the plaintext to
    * @throws IOException
    */
   public void decrypt(InputStream in, OutputStream out) throws IOException;

   /**
    * Encrypts the plaintext string {@code msg} and returns the result.
    * 
    * @param msg
    *           The plaintext to be encrypted
    * @return An encrypted ciphertext
    */
   public String encrypt(String msg);

   /**
    * Decrypts a message and returns the result.
    * 
    * @param ciphertext
    *           The ciphertext to decrypt
    * @return The decrypted plaintext
    */
   public String decrypt(String ciphertext);

   // Hint: there is no reason why some of the methods above should not share
   // code

   /**
    * Saves the cipher key to the output stream
    * 
    * @param out
    *           The OutStream to save the cipher to
    */
   public void save(OutputStream out);

}
