// The characters in java are represented as Unicodes.

public class Unicode {
    public static void main( String[] args ) {
        for ( int i=0; i<args.length; i++ ) {
            String s = args[ i ];
            for ( int pos=0; pos < s.length(); pos++ ) {
                char c = s.charAt( pos );
                int u = (int) c;
                System.out.println( "char = " + c + ", unicode = " + u );
            }
        }
    }
}

// Compile this file
//
// > javac Unicode.java
//
// Execute the program passing characters on the command line,
//
// > java Unicode abcABC 123 ",:/"
// char = a, unicode = 97
// char = b, unicode = 98
// char = c, unicode = 99
// char = A, unicode = 65
// char = B, unicode = 66
// char = C, unicode = 67
// char = 1, unicode = 49
// char = 2, unicode = 50
// char = 3, unicode = 51
// char = ,, unicode = 44
// char = :, unicode = 58
// char = /, unicode = 47