package antlr;

/* ANTLR Translator Generator
 * Project led by Terence Parr at http://www.cs.usfca.edu
 * Software rights: http://www.antlr.org/license.html
 *
 * $Id: //depot/code/org.antlr/release/antlr-2.7.6/antlr/TokenStream.java#1 $
 */

public interface TokenStream {
    public Token nextToken() throws TokenStreamException;
}
