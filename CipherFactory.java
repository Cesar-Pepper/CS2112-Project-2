package cipher;
import java.math.BigInteger;

/** Factory class for creating cipher objects. */
public class CipherFactory {
   /**
    * Returns: a monoalphabetic substitution cipher with the English alphabet
    * mapped to the provided alphabet.<br>
    * Requires: {@code encrAlp} contains exactly one occurrence of each English
    * letter and nothing more. No requirement is made on case.
    * 
    * @param encrAlp
    *           the encrypted alphabet
    */
   public Cipher getMonoCipher(String encrAlp) { 
	   return new RandomSubstitutionCipher(encrAlp); //MonoCipher can be created using the RandomSubstitutionCipher constructor		
   }

   /**
    * Returns a new Caesar cipher with the given shift parameter.
    * 
    * @param shftParam
    *           the cipher's shift parameter
    */
   public Cipher getCaesarCipher(int shftParam) {
      return new CaesarCipher(shftParam); // TODO implement
   }

   /**
    * Returns a Vigenere cipher (with multiple shifts).
    * 
    * @param key
    *           the cipher's shift parameters. Note that A is a shift of 1.
    */
   public Cipher getVigenereCipher(String key) {
      return new VigenereCipher(key); // TODO implement
   }

   /**
    * Returns a new monoalphabetic simple substitution cipher with a randomly
    * generated mapping.
    */
   public Cipher getRandomSubstitutionCipher() {
      return new RandomSubstitutionCipher(); // TODO implement
   }

   /**
    * Returns a new RSA private cipher with a randomly generated keys.
    */
   public Cipher getRSACipher() {
      return new RSACipher(); // TODO implement
   }

   /**
    * Returns a new RSA cipher with given n, e, and d
    *
    * @param d
    *           may not be null- this indicates the returned Cipher is for
    *           encryption only
    * @param e
    *           may not be null- this indicates the returned Cipher is for
    *           decryption only
    */
   public Cipher getRSACipher(BigInteger n, BigInteger e, BigInteger d) {
      return new RSACipher(n,e,d); // TODO implement
   }
}
