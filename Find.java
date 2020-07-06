// Prints all the lines containing a given word. For each line
// containing the word, it prints the line number followed by the line
// itself.

import java.io.*;

public class Find {

    public static void find( String fileName, String word ) 
        throws IOException, FileNotFoundException {

		BufferedReader input;
		
		//create your BufferedReader

        
        int lineNumber = 0;
        String line;

        //create a while loop to read your file line by line
			//Verify if your word is in the line being read
			
        //close your file
		
    }

    public static void main( String[] args ) 
        throws IOException, FileNotFoundException {

        if ( args.length != 2 ) {
            System.out.println( "Usage: java Find file word" );
            System.exit( 0 );
        }

        find( args[0], args[1] );

    }
}


//output example
// > java Find Find.java IOException
// 10:   throws IOException, FileNotFoundException {
// 28:  throws IOException, FileNotFoundException {