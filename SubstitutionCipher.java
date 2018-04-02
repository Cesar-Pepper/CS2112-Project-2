package cipher;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.TreeMap;

public class SubstitutionCipher extends AbstractCipher {
	
	/**
	 * letters - uppercase alphabet in proper order
	 * keyString - stores altered version of alphabet 
	 * alphaLink - used to map alphabet to altered version or vice versa
	 */
	protected String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	protected String keyString;
	protected TreeMap<Character,Character> alphaLink = new TreeMap<Character,Character>();
	
	/**
	 * Returns map that creates 1-1 correspondence between alphabet and key or vice versa.
	 * 
	 * @param keys
	 * 		- String version of alphabet to be mapped
	 * @param values
	 * 		- String version of alphabet to be mapped to
	 * @return
	 * 		- TreeMap that links "keys" to "values" used to translate messages
	 */
	public TreeMap<Character,Character> createMap(String keys, String values){ 
		TreeMap<Character,Character> temp = new TreeMap<Character,Character>();
		for(int x=0; x<values.length(); x++)
			temp.put(keys.charAt(x), values.charAt(x));
		return temp;
	}
	
	/**
	 * Returns translated version of "text" characters using alphaLink map.
	 * 
	 * @param text
	 * 		- text that needs to be translated 
	 * @return
	 * 		- translated version of text with alphaLink
	 */
	public String translate(String text) {               
		String trans = "";
		for(int x=0; x<text.length(); x++) {
			char temp = text.charAt(x);
			if(temp==' ')
				trans = trans+' ';
			else if(temp=='\n')
				trans = trans+"\r\n";
			else
				trans = trans+alphaLink.get(text.charAt(x));
		}
		return trans;
	}
	
	/**
	 * Returns formatted string stripped of special characters and capitalized.
	 * 
	 * @param original
	 * 		- crude string that needs to be formatted to meet specs
	 * @return
	 * 		- return refined version of original
	 */
	public String format(String original) {
		String edited = "";
		edited = original.toUpperCase().replaceAll("[^A-Z\\s\\n]", "");
		return edited;
	}
	
	/**
	 * Encrypts a String using this cipher and returns encrypted string.
	 * 
	 * @param msg
	 * 		- string to be encrypted
	 * 
	 * @return
	 * 		- encrypted string
	 */
	public String encrypt(String msg) 
	{
		alphaLink = createMap(letters, keyString);
		return translate(format(msg));
	}
	
	/**
	 * Decrypts a String using this cipher and returns decrypted string.
	 * 
	 * @param ciphertext
	 * 		- string to be decrypted
	 * 
	 * @return
	 * 		- decrypted string
	 */
	public String decrypt(String ciphertext)
	{
		alphaLink = createMap(keyString, letters);
		return translate(format(ciphertext));	
	}
	
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
	public void encrypt(InputStream in, OutputStream out) throws IOException
	{
		alphaLink = createMap(letters, keyString);
		transcribe(in,out);
	}
	
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
	public void decrypt(InputStream in, OutputStream out) throws IOException
	{
		alphaLink = createMap(keyString, letters);
		transcribe(in,out);
	}
	
	/**
	 * Helps encrypt/decrypt methods to read from InputStream and write to OutputStream.
	 * Reads through InputStream character by character as integer representations and 
	 * maps each character based on mapping of characters for translation.
	 * 
	 * @param in
	 * 		-InputStream to be translated
	 * @param out
	 * 		-OutputStream to be translated to
	 * @throws IOException
	 * 		-if there is nothing to be read
	 */
	public void transcribe(InputStream in, OutputStream out) throws IOException
	{
		while(in.available()>0) {
			int val = (int)(in.read());
			char next = (char)val;
			String trans = translate(format(String.valueOf(next)));
			out.write(trans.getBytes());
		}
	}
	
	/**
	 * Saves the cipher key to the output stream
	 * 
	 * @param out
	 *           The OutStream to save the cipher to
	 */
	public void save(OutputStream out) 
	{
		try {
			out.write(keyString.getBytes(Charset.forName("UTF-8")));
		} catch (IOException e) {
			System.out.println("Key could not be printed to specified file. Try again.");
		}
	}
}
