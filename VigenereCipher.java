package cipher;

public class VigenereCipher extends SubstitutionCipher {
	
	/**
	 * keyPattern - altered alphabet based on user input
	 * numChar - keeps track of index of key to determine shift
	 */
	private String keyPattern; 
	private int numChar;
	
	/**
	 * Constructor for VigenereCipher:
	 * 	 - creates new VigenereCipher with encryption key pattern based on user inputted "key"
	 * 
	 * @param key
	 * 	 - alphabetical representation of shift values (a,A=1, b,B=2, etc.)
	 */
	public VigenereCipher(String key) 
	{
		keyPattern = key;
		numChar = 0;
	}
	
	/**
	 * Encrypts the plaintext string {@code msg} and returns the result.
	 * 
	 * @param msg
	 *           The plaintext to be encrypted
	 * @return An encrypted ciphertext
	 */
	public String encrypt(String msg) {
		
		String ciphertext = "";
		msg = format(msg);
		int patternLength = keyPattern.length();
		int numChar = 0;
		char msgLetter;
		for(int x=0; x<msg.length(); x++) {
			msgLetter = msg.charAt(x);
			if(msgLetter!=' ' && msgLetter!='\t' && msgLetter!='\n' && letters.indexOf(msgLetter)!=-1) {
				int patternIndex = numChar%patternLength;
				int alphaPos = letters.indexOf(keyPattern.charAt(patternIndex))+1; 
				int indexCipherLetter = letters.indexOf(msgLetter)+alphaPos;
				ciphertext = ciphertext + letters.charAt(indexCipherLetter%26); 
				numChar++;
			}
			else if(msgLetter==' ')
				ciphertext = ciphertext + " ";
			else if(msgLetter=='\t')
				ciphertext = ciphertext + "\t";
			else if(msgLetter=='\n')
				ciphertext = ciphertext + "\r\n";
			}
		return ciphertext;
	}
	
	/**
	 * Decrypts a message and returns the result.
	 * 
	 * @param ciphertext
	 *           The ciphertext to decrypt
	 * @return The decrypted plaintext
	 */
	public String decrypt(String ciphertext) {
		
		String msg = "";
		ciphertext = format(ciphertext);
		int patternLength = keyPattern.length();
		int numChar = 0;
		char cipherTextLetter;
		for(int x=0; x<ciphertext.length(); x++) {
			cipherTextLetter = ciphertext.charAt(x);
			if(cipherTextLetter!=' ' && cipherTextLetter!='\t' && cipherTextLetter!='\n' && letters.indexOf(cipherTextLetter)!=-1) {
				int patternIndex = numChar%patternLength;
				int alphaPos = letters.indexOf(keyPattern.charAt(patternIndex))+1; 
				int indexMsgLetter = letters.indexOf(cipherTextLetter)-alphaPos;
				msg = msg + letters.charAt((indexMsgLetter+26)%26);
				numChar++;
			}
			else if(cipherTextLetter==' ')
				msg = msg + " ";
			else if(cipherTextLetter=='\t')
				msg = msg + "\t";
			else if(cipherTextLetter=='\n')
				msg = msg + "\r\n";
		}
	return msg;
	}
}
