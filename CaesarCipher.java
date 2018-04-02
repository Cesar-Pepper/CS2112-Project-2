package cipher;

public class CaesarCipher extends SubstitutionCipher {

	/**
	 * Constructor for CaesarCipher: 
	 *   - creates new CaesarCipher with altered alphabet based on parameter shift
	 * 	
	 * @param shift
	 * 		how much to shift the alphabet
	 */
	public CaesarCipher(int shift)
	{
		if(shift>=0)
			keyString = letters.substring(shift%26, letters.length())+letters.substring(0,shift%26);
		else
			keyString = letters.substring(26+shift%26, letters.length())+letters.substring(0,26+shift%26);
	}
}
