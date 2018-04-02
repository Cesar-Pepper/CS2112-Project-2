package cipher;

public class RandomSubstitutionCipher extends SubstitutionCipher {
	 
	/**
	 * Constructor for RandomSubstitutionCipher:
	 *	 - creates substitution cipher with a randomly generated alphabet to map to. 	
	 * keystring - stores random alphabet string.
	 */
	public RandomSubstitutionCipher()
	{ 
		keyString = alphaRandomizer();	
	}
	
	/**
	 * Constructor for RandomSubstitutionCipher:
	 * 	 - creates substitution cipher with a given key.
	 * @param key
	 *   - scrambled version of alphabet.
	 */
	public RandomSubstitutionCipher(String key)
	{
		keyString = key.toUpperCase();
	}
	
	/**
	 * Returns a scrambled version of the alphabet.
	 */
	public String alphaRandomizer() 
	{
		char[]charLetters = letters.toCharArray();
		for(int x=0; x<charLetters.length; x++) {
			int randomIndex = (int)(Math.random()*charLetters.length);
			char temp = charLetters[x];
			charLetters[x] = charLetters[randomIndex];
			charLetters[randomIndex] = temp;
		}
		return new String(charLetters);
	}
}
