package cipher;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.io.EOFException; //just for testing delete after
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Console;

import org.omg.CORBA.DynAnyPackage.Invalid;

/**
 * 
 * Command line interface to allow users to interact with your ciphers.
 * 
 * We have provided some infrastructure to parse most of the arguments. It is
 * your responsibility to implement the appropriate actions according to the
 * assignment specifications. You may choose to "fill in the blanks" or rewrite
 * this class.
 *
 * Regardless of which option you choose, remember to minimize repetitive code.
 * You are welcome to add additional methods or alter the provided code to
 * achieve this.
 *
 */
public class Main {
	
	/**
	 * factory - CipherFactory object used to call methods to return new Cipher objects
	 * test - the Cipher object that is being created and utilized
	 * reader - used to read in keys as Strings from files
	 * result - altered String from encrypting/decrypting user input directly to console (small size)
	 * toDo - keeps track if Cipher should encrypt/decrypt to allow for FileStreaming for big files
	 * RSA - keeps track if the Cipher type is RSA (to check for compatibility with functions)
	 * RSAStream - used for RSACipher to encrypt data to file
	 * writer - writes onto output file
	 * fileReader - reads in input file
	 * fileInputName - tracks name of file to read from
	 * fileOutputName - tracks name of file to read to
	 */

	CipherFactory factory = new CipherFactory();
	Cipher test;
	BufferedReader reader;
	String result;
	String toDo = "";
	boolean RSA = false;
	ByteArrayInputStream RSAStream = null;
	FileOutputStream writer;
	FileInputStream fileReader;
	String fileInputName = "";
	String fileOutputName = "";
	
   public static void main(String[] args) {
      // TODO implement
	   Main a = new Main();
	   int index = a.parseCipherType(args, 0);
	   index = a.parseCipherFunction(args, index);
	   a.parseOutputOptions(args, index); 
   }

   /**
    * Set up the cipher type based on the options found in args starting at
    * position pos, and return the index into args just past any cipher type
    * options.
    */
   private int parseCipherType(String[] args, int pos)
            throws IllegalArgumentException {
      // check if arguments are exhausted
      if (pos == args.length) return pos;
      String cmdFlag = args[pos++];
      switch (cmdFlag) {
      case "--caesar":
         // TODO create a Caesar cipher object with the given shift parameter
    	  if(args.length<=pos) {
    		  System.out.println("Please enter a shift parameter.");
    	  	  System.exit(1);
    	  }
    	  try {
    		  test = factory.getCaesarCipher(Integer.parseInt(args[pos]));
    	  } catch (NumberFormatException e) {
    		  System.out.println("Invalid shift parameter given. Try again.");
    		  System.exit(1);
    	  }
    	  pos++;
         break;
      case "--random":
         // TODO create a random substitution cipher object
    	  test = factory.getRandomSubstitutionCipher();
         break;
      case "--monoLoad":
         // TODO load a monoalphabetic substitution cipher from a file
    	  try {
			reader = openFile(args[pos]);
		} catch (FileNotFoundException e) {
			System.out.println("Sorry, file name is Invalid. Try again with a different file.");
			System.exit(1);
		}
    	  try {
			test = factory.getMonoCipher(reader.readLine());
		} catch (IOException e) {
			System.out.println("Sorry, file could not be read. Try again.");
			System.exit(1);
		}
    	  pos++;
         break;
      case "--vigenere":
         // TODO create a new Vigenere Cipher with the given key
    	  for(int x=0; x<args[pos].length(); x++) {
    		  if(!args[pos].substring(x,x+1).matches("[a-zA-Z]")) {
    			  System.out.println("Please input a key that only contains alphabetical letters. Try again.");
    			  System.exit(1);
    		  }
    	  }
    	  test = factory.getVigenereCipher(args[pos]);
    	  pos++;
         break;
      case "--vigenereLoad":
         // TODO create a Vigenere cipher with key loaded from the given file
    	  try {
    		reader = openFile(args[pos]);
    	} catch (FileNotFoundException e) {
    		System.out.println("Sorry, file name is invalid. Try again with a different file.");
    		System.exit(1);
    	}
          try {
    		test = factory.getVigenereCipher(reader.readLine());
   		} catch (IOException e) {
    		System.out.println("Sorry, file could not be read. Try again.");
    		System.exit(1);
   		}
          pos++;
         break;
      case "--rsa":
         // TODO create new RSA cipher
    	  test = factory.getRSACipher();
    	  RSA = true;
         break;
      case "--rsaLoad":
         // TODO load an RSA key from the given file
    	  try {
			reader = new BufferedReader(new FileReader(args[pos]));
		} catch (FileNotFoundException e) {
			System.out.println("Sorry, file name is invalid. Try again with a different file.");
			System.exit(1);
		}
    	  BigInteger first = null;
		try {
			first = new BigInteger(reader.readLine());
		} catch (IOException e) {
			System.out.println("Sorry, first integer could not be read.");
			System.exit(1);
		} 
    	  BigInteger second = null;
		try {
			second = new BigInteger(reader.readLine());
		} catch (IOException e) {
			System.out.println("Sorry, second integer could not be read.");
			System.exit(1);
		} 
    	  BigInteger third = null;
		try {
			third = new BigInteger(reader.readLine());
		} catch (IOException e) {
			System.out.println("Sorry, third integer could not be read.");
			System.exit(1);
		} 
    	  test = factory.getRSACipher(first, second, third);
    	  RSA = true;
    	  pos++;
         break;
      default:
    	  System.out.println("Please create a valid Cipher to test.");
    	  System.exit(1);
      }
      return pos;
   }

   /**
    * Parse the operations to be performed by the program from the command-line
    * arguments in args starting at position pos. Return the index into args
    * just past the parsed arguments.
    */
   private int parseCipherFunction(String[] args, int pos)
            throws IllegalArgumentException {
      // check if arguments are exhausted
      if (pos == args.length) return pos;

      switch (args[pos++]) {
      case "--em":
         // TODO encrypt the given string
    	  if(!RSA) {
    		  try {
    			  if(!(test==null))
    				  result = test.encrypt(args[pos]);
    		  } catch (IndexOutOfBoundsException e) {
    			  System.out.println("Please provide a String to encrypt.");
    			  System.exit(1);
    		  }
    	  }
    	  else
    		  RSAStream = new ByteArrayInputStream(args[pos].getBytes());
    	  pos++;
         break;
      case "--ef":
         // TODO encrypt the contents of the given file
    	  fileInputName = args[pos];
    	  toDo = "encrypt";
    	  pos++;
         break;
      case "--dm":
         // TODO decrypt the given string --
         // substitution ciphers only
    	  if(RSA) {
    		  System.out.println("Arguments are incompatible. Try again.");
    		  System.exit(1);
    	  }
    	  try {
    	  result = test.decrypt(args[pos]);
    	  } catch (IndexOutOfBoundsException e) {
    		  System.out.println("Please provide a String to decrypt.");
    		  System.exit(1);
    	  }
    	  pos++;
         break;
      case "--df":
         // TODO decrypt the contents of the given file
    	  fileInputName = args[pos];
    	  toDo = "decrypt";
      	  pos++;
         break;
      default:
         // TODO
    	  System.out.println("Please provide sufficient and/or sensical arguments to be performed.");
    	  System.exit(1);
      }
      return pos;
   }

   /**
    * Parse options for output, starting within {@code args} at index
    * {@code argPos}. Return the index in args just past such options.
    */
   private int parseOutputOptions(String[] args, int pos)
            throws IllegalArgumentException {
      // check if arguments are exhausted
      if (pos == args.length) return pos;

      String cmdFlag;
      while (pos < args.length) {
         switch (cmdFlag = args[pos++]) {
         case "--print":
            // TODO print result of applying the cipher to the console --
            // substitution ciphers only
        	 if(RSA && toDo.equals("encrypt")) {
        		 System.out.println("Cannot print RSA-type cipher text directly to console.");
        		 System.exit(1);
        	 }
        	 if(!fileInputName.equals(""))
        		 try {
        			 fileReader = new FileInputStream(fileInputName);
        		 } catch (FileNotFoundException e) {
        			 System.out.println("Input File could not be found. Try another file.");
        			 System.exit(1);
        		 }
        	 
        	 if(fileInputName.equals(""))
        		 System.out.print(result);
        	 else if(toDo.equals("encrypt"))
				try {
					while(fileReader.available()>0)
						System.out.print(test.encrypt(String.valueOf((char)fileReader.read())));
				} catch (IOException e) {
					System.out.println("File could not be properly read and encrypted. Try again.");
					System.exit(1);
				}
        	 else if(toDo.equals("decrypt") && !RSA)
        		 try {
 					while(fileReader.available()>0)
 						System.out.print(test.decrypt(String.valueOf((char)fileReader.read())));
 				} catch (IOException e) {
 					System.out.println("File could not be properly read and decrypted. Try again.");
 					System.exit(1);
 				}
        	 else if(toDo.equals("decrypt") && RSA)
				try {
					test.decrypt(fileReader, System.out);
				} catch (IOException e2) {
					System.out.println("File could not be properly read and decrypted. Try again.");
					System.exit(1);
				}
        		 
        	pos++;
            break;
         case "--out":
            // TODO output result of applying the cipher to a file
        	if(!fileInputName.equals("")) {
        		try {
        			fileReader = new FileInputStream(fileInputName);
        		} catch (FileNotFoundException e) {
        			System.out.println("Input File could not be found. Try another file.");
        			System.exit(1);
        		}
        	}
        	try {
        		if(args.length<=pos) {
        			System.out.println("Please enter a file to print to.");
        			System.exit(1);
        		}
        		 writer = new FileOutputStream(args[pos]);
        	} catch (FileNotFoundException e) {
        		System.out.println("Output File could not be created. Try again.");
        		System.exit(1);
        	}
         	 
        	if(fileInputName.equals("") && !RSA)
				try {
					writer.write(result.getBytes());
				} catch (IOException e1) {
					System.out.println("Message could not be written to file. Try again.");
					System.exit(1);
				}
        	
        	else if(toDo.equals("encrypt") && !RSA)
         		try {
         			while(fileReader.available()>0)
 						writer.write((test.encrypt(String.valueOf((char)fileReader.read()))).getBytes());
 				} catch (IOException e) {
 					System.out.println("File could not be properly read and encrypted. Try again.");
 					System.exit(1);
 				}
         	 
        	else if(toDo.equals("decrypt") && !RSA)
         		 try {
  					while(fileReader.available()>0)
  						System.out.print(test.decrypt(String.valueOf((char)fileReader.read())));
  				} catch (IOException e) {
  					System.out.println("File could not be properly read and encrypted. Try again.");
  					System.exit(1);
  				}
			else if(RSA && !(RSAStream==null))
				try {
					test.encrypt(RSAStream, writer);
				} catch (IOException e1) {
					System.out.println("RSA Stream could not be encrypted into file. Try again.");
					System.exit(1);
				}
			else if(RSA && RSAStream==null)
				try {
					test.decrypt(fileReader, writer);
				} catch (IOException e1) {
					System.out.println("Ciphertext could not be decrypted with RSA. Try Again.");
					System.exit(1);
				}
        	pos++;
            break;
         case "--save":
            // TODO save the cipher key to a file
        	 try {
				test.save(new FileOutputStream(args[pos]));
			} catch (FileNotFoundException e) {
				System.out.println("File to be saved to could not be found. Try a new file.");
				System.exit(1);
			}
        	pos++;
            break;
         default:
            // TODO
        	 System.out.println("End of Arguments. Try again with new arguments.");
        	 System.exit(1);
         }
      }
      return pos;
   }

   /**
    * Create a BufferedReader that reads from a file
    * 
    * @param filename
    *           Name of file to read from
    * @return a BufferedReader to read from the given file
    * @throws FileNotFoundException
    */
   public static BufferedReader openFile(String filename)
            throws FileNotFoundException {
      return new BufferedReader(new FileReader(filename));
   }

   /**
    * Create a BufferedReader that write to a file
    * 
    * @param filename
    *           Name of file to write to
    * @return a BufferedReader to write to the given file
    * @throws FileNotFoundException
    */
   public static BufferedWriter writeFile(String filename) throws IOException {
      return new BufferedWriter(new FileWriter(filename));
   }

   /**
    * Read a given file into a String. Requires: the file contents decode
    * successfully into a String in the default character encoding.
    * 
    * @param filename
    *           Name of file to be read
    * @return Contents of the file as a String
    */
   public static String readFile(String filename) {
      String line = null;
      StringBuilder result = new StringBuilder();

      try {
         BufferedReader reader = new BufferedReader(new FileReader(filename));

         while ((line = reader.readLine()) != null) {
            result.append(line);
         }
         reader.close();
      } catch (IOException e) {
         // TODO Add an appropriate error message
    	  System.out.println("File could not be read. Try a new file");
      }
      return result.toString();
   }

   /**
    * Prints a string to a specified file
    * 
    * @param filename
    *           File in which string will be printed
    * @param towrite
    *           String to be printed in file
    * @throws IOException
    */
   public static void writeToFile(String filename, String towrite)
            throws IOException {
      BufferedWriter w = writeFile(filename);
      w.write(towrite, 0, towrite.length());
      w.close();
   }
}
