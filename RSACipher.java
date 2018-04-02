package cipher;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.Random;

public class RSACipher extends AbstractCipher implements ChunkReader{
	
	/**
	 * fileInput - used to read text from file
	 * fileOutput - used to write text to file
	 * byteArray - store bytes from message to be encrypted
	 * byteArrayDecrypt - store bytes from message to be decrypted 
	 * p - first randomly generated large prime
	 * q - second randomly generated large prime
	 * n - product p*q
	 * totient - value of (p-1)*(q-1)
	 * e - random number between 1 and totient that is relatively prime to totient
	 * d - multiplicative inverse of e modulo the totient
	 */
	
	private InputStream fileInput;
	private OutputStream fileOutput;
	private byte[] byteArray;
	private byte[] byteArrayDecrypt;
	private BigInteger p;
	private BigInteger q;
	private BigInteger n;
	private BigInteger totient;
	private BigInteger e;
	private BigInteger d;
	
	/**
	 * Constructor for RSACipher:
	 * 	 - creates a new RSACipher with randomly generated key
	 */
	public RSACipher() 
	{ 
		byteArray = new byte[127];
		byteArrayDecrypt = new byte[128];
		p = new BigInteger(510, 20, new Random());
		q = new BigInteger(510, 20, new Random());
		n = p.multiply(q);	
		totient = p.subtract(new BigInteger("1")).multiply(q.subtract(new BigInteger("1")));
		e = new BigInteger(totient.bitLength()-1, new Random());
		while(!e.gcd(totient).equals(BigInteger.ONE)) {
			e = new BigInteger(totient.bitLength()-1, new Random());
		}
		d = e.modInverse(totient);
	}
	
	/**
	 * Constructor for RSACipher:
	 * 	 - creates a new RSACipher with given keys n,e,d
	 * @param nVal
	 * 	 - BigInteger user input for key n
	 * @param eVal
	 * 	 - BigInteger user input for key e
	 * @param dVal
	 * 	 - BigInteger user input for key d
	 */
	public RSACipher(BigInteger nVal, BigInteger eVal, BigInteger dVal) 
	{
		byteArray = new byte[127];
		byteArrayDecrypt = new byte[128];
		n = nVal;
		e = eVal;
		d = dVal;
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
		fileInput = in;
		fileOutput = out;
		while(hasNext()) {
			nextChunk(byteArray);
			BigInteger msg = new BigInteger(byteArray);
			BigInteger ciphertext = msg.modPow(e,n);
			byte[] byteArrayMod = ciphertext.toByteArray();
			byte[] byteArrayFull = new byte[128];
			int pad = 128-byteArrayMod.length;
			for(int x=0; x<pad; x++) {
				byteArrayFull = new byte[128];
				byteArrayFull[x] = 0;
			}
			for(int x = pad; x<byteArrayFull.length; x++)
				byteArrayFull[x] = byteArrayMod[x-pad];
			fileOutput.write(byteArrayFull);
		}
		out.close();
		in.close();
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
		fileInput = in;
		fileOutput = out;
		while(hasNext()) {
			fileInput.read(byteArrayDecrypt,0,byteArrayDecrypt.length);
			BigInteger ciphertext = new BigInteger(byteArrayDecrypt);
			BigInteger textIndex = ciphertext.modPow(d,n);
			byte[] temp = textIndex.toByteArray();
			for(int x=1; x<temp[0]+1; x++) {
				byte[] test = new byte[1];
				test[0] = temp[x];
				fileOutput.write(test);
			}
		}
	}

		/**
	    * Returns the maximum number of bytes in a chunk.
	    */
	public int chunkSize() 
	{
		return 126;
	}

	   /**
	    * Returns true if there is at least one more byte of data to be read in the
	    * current stream.
	    */
	public boolean hasNext() 
	{
		try {
			return (fileInput.available()>0);
		} catch (IOException e) {
		}
		return false;
	}
		
	   
	   /**
	    * Returns the next chunk of up to {@code chunkSize()} bytes from the current
	    * input stream. The returned bytes are placed in the array {@code data}. The
	    * number of bytes returned is always {@code chunkSize()}, unless the end of
	    * the input stream has been reached and there are fewer than
	    * {@code chunkSize()} bytes available, in which case all remaining bytes are
	    * returned.
	    * 
	    * @param data
	    *           an array of length at least {@code chunkSize()}.
	    * @return The number of bytes returned, which is always between 1 and the
	    *         chunk size.
	    * @throws EOFException
	    *            if there are no more bytes available.
	    * @throws IOException
	    */
	public int nextChunk(byte[] data) throws java.io.EOFException, IOException
	{	
		int x = fileInput.read(data, 1, chunkSize());
		data[0] = (byte)(x);
		return x;
	}   
	
	 /**
	  * Saves the cipher key to the output stream, separates three aspects of RSA key into separate lines
	  * 
	  * @param out
	  *           The OutStream to save the cipher to
	  */
	public void save(OutputStream out) {
		try {
			out.write(n.toString().getBytes());
			out.write("\r\n".getBytes());
			out.write(e.toString().getBytes());
			out.write("\r\n".getBytes());
			out.write(d.toString().getBytes());
		} catch (IOException e) {
			System.out.println("Key could not be properly printed. Try again.");
		}	
	}
}
