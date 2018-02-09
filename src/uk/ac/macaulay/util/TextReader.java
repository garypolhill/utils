/*
 * uk.ac.macaulay.util: TextReader.java
 * 
 * Copyright (C) 2010 Macaulay Institute
 * 
 * This file is part of utils.
 * 
 * utils is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * utils is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with utils. If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact information: Gary Polhill Macaulay Institute, Craigiebuckler,
 * Aberdeen. AB15 8QH. UK. g.polhill@macaulay.ac.uk
 */
package uk.ac.macaulay.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * <!-- TextReader -->
 * 
 * A tool to assist with reading text files.
 * 
 * @author Gary Polhill
 */
public class TextReader {
  /**
   * The buffer from which to read
   */
  private BufferedReader buff;

  /**
   * The last line read from the buffer
   */
  private String lastLineRead;

  /**
   * The name of the file that the buffer is being read from
   */
  private String filename;

  /**
   * Whether or not the last line read was parsed
   */
  private boolean lastLineParsed;

  /**
   * End of file reached on last read
   */
  private boolean eof;

  /**
   * A text description of the format of the file, for error messages
   */
  private String format;

  /**
   * <!-- Comment -->
   * 
   * An enumeration of comments recognised by this reader class
   * 
   * @author Gary Polhill
   */
  public enum Comment {
    HASH, C, SLASH2, XML;
  }

  /**
   * Constructor
   * 
   * @param buff The buffer to read from
   * @param filename The name of the file the buffer is reading from
   * @param format The format that file is supposed to have
   */
  public TextReader(BufferedReader buff, String filename, String format) {
    this.buff = buff;
    this.filename = filename;
    this.format = format;
    lastLineRead = null;
    lastLineParsed = false;
    eof = false;
  }

  /**
   * <!-- eof -->
   * 
   * If the last read indicated that we were at the end of the file, this will
   * be set to <code>true</code>
   * 
   * @return <code>true</code> if we were at the end of the file
   */
  public boolean eof() {
    return eof;
  }

  /**
   * <!-- readExactString -->
   * 
   * Read the exact string passed as argument
   * 
   * @param str The string expected to be in the file
   * @return <code>true</code> if the string was read successfully
   * @throws IOException
   */
  public boolean readExactString(String str) throws IOException {
    return readExactString(str, false, false);
  }

  /**
   * <!-- readExactStringIgnoreCase -->
   * 
   * Read the exact string passed as argument, but ignore case
   * 
   * @param str The string expected to be in the file
   * @return <code>true</code> if the string was read successfully
   * @throws IOException
   */
  public boolean readExactStringIgnoreCase(String str) throws IOException {
    return readExactString(str, true, false);
  }

  /**
   * <!-- readExactStringIgnoreLeadingSpace -->
   * 
   * Read the exact string passed as argument, but ignore any whitespace before
   * the string
   * 
   * @param str The string expected to be in the file
   * @return <code>true</code> if the string was read successfully
   * @throws IOException
   */
  public boolean readExactStringIgnoreLeadingSpace(String str) throws IOException {
    return readExactString(str, false, true);
  }

  /**
   * <!-- readExactStringIgnoreCaseAndLeadingSpace -->
   * 
   * Read the exact string passed as argument, ignoring case and any whitespace
   * before the string
   * 
   * @param str The string expected to be in the file
   * @return <code>true</code> if the string was read successfully
   * @throws IOException
   */
  public boolean readExactStringIgnoreCaseAndLeadingSpace(String str) throws IOException {
    return readExactString(str, true, true);
  }

  /**
   * <!-- readExactString -->
   * 
   * Read the exact string given in the <code>str</code> argument, with options
   * to ignore case and ignore leading whitespace. The other
   * <code>readExactString()</code> methods all call this one. If
   * <code>ignoreLeadingSpace</code> is <code>true</code>, then this method will
   * fail if <code>str</code> begins with any whitespace characters.
   * 
   * @param str The string to read (should not have whitespace if
   *          <code>ignoreLeadingSpace</code> is <code>true</code>
   * @param ignoreCase <code>true</code> if the string is not case sensitive
   * @param ignoreLeadingSpace <code>true</code> if any leading whitespace
   *          should be ignored
   * @return <code>true</code> if the string was read successfully
   * @throws IOException
   */
  public boolean readExactString(String str, boolean ignoreCase, boolean ignoreLeadingSpace) throws IOException {
    if(str == null) return false;
    char[] cbuf = new char[str.length()];
    int c;
    while((c = buff.read()) != -1) {
      if(!ignoreLeadingSpace || !Character.isWhitespace(c)) break;
    }
    if(c == -1) {
      eof = true;
      return false;
    }
    cbuf[0] = (char)c;
    int readval = buff.read(cbuf, 1, str.length() - 1);
    if(readval == -1) eof = true;
    if(readval != str.length() - 1) return false;
    return ignoreCase ? str.equalsIgnoreCase(new String(cbuf)) : str.equals(new String(cbuf));
  }

  /**
   * <!-- readExactWord -->
   * 
   * Read the exact word passed in the argument. The word should not contain any
   * whitespace characters. Leading whitespace will be ignored.
   * 
   * @param str The word to read
   * @return <code>true</code> if the word was read successfully
   * @throws IOException
   */
  public boolean readExactWord(String str) throws IOException {
    return readExactWord(str, EnumSet.noneOf(Comment.class));
  }

  /**
   * <!-- readExactWord -->
   * 
   * Read the exact word passed in the argument, allowing comments given in the
   * {@link EnumSet}. Leading whitespace will be ignored.
   * 
   * @param str The word to read
   * @param comments The comment types that are allowed (comments are ignored)
   * @return <code>true</code> if the word was read successfully
   * @throws IOException
   */
  public boolean readExactWord(String str, EnumSet<Comment> comments) throws IOException {
    if(str == null) return false;
    String word = readWord(true, comments);
    return str.equals(word);
  }

  /**
   * <!-- readExactWordIgnoreCase -->
   * 
   * Read the exact word passed in the argument, ignoring case. Leading
   * whitespace will be ignored.
   * 
   * @param str The (case insensitive) word to read
   * @return <code>true</code> if the word was read successfully
   * @throws IOException
   */
  public boolean readExactWordIgnoreCase(String str) throws IOException {
    return readExactWordIgnoreCase(str, EnumSet.noneOf(Comment.class));
  }

  /**
   * <!-- readExactWordIgnoreCase -->
   * 
   * Read the exact word passed in the argument, ignoring case, and allowing the
   * comments given in the {@link EnumSet}. Leading whitespace will be ignored.
   * 
   * @param str The (case insensitive) word to read
   * @param comments The comments that are allowed
   * @return <code>true</code> if the word was read successfully
   * @throws IOException
   */
  public boolean readExactWordIgnoreCase(String str, EnumSet<Comment> comments) throws IOException {
    if(str == null) return false;
    String word = readWord(true, comments);
    return str.equalsIgnoreCase(word);
  }

  /**
   * <!-- readExactNumber -->
   * 
   * Read the exact number given in the argument. Leading whitespace will be
   * ignored.
   * 
   * @param number The number to read
   * @return <code>true</code> if the number was read successfully
   * @throws IOException
   */
  public boolean readExactNumber(int number) throws IOException {
    return readExactNumber(number, EnumSet.noneOf(Comment.class));
  }

  /**
   * <!-- readExactNumber -->
   * 
   * Read the exact number given in the argument, allowing the set of comments
   * given in the {@link EnumSet}. Leading whitespace will be ignored.
   * 
   * @param number The number to read in
   * @param comments Comments that are allowed
   * @return <code>true</code> if the number was read successfully
   * @throws IOException
   */
  public boolean readExactNumber(int number, EnumSet<Comment> comments) throws IOException {
    String word = readWord(true, comments);
    try {
      return number == Integer.parseInt(word);
    }
    catch(NumberFormatException e) {
      return false;
    }
  }

  /**
   * <!-- readExactNumber -->
   * 
   * @param number <code>long</code> number
   * @return <code>true</code> if the number was read successfully
   * @throws IOException
   */
  public boolean readExactNumber(long number) throws IOException {
    return readExactNumber(number, EnumSet.noneOf(Comment.class));
  }

  /**
   * <!-- readExactNumber -->
   * 
   * @param number <code>long</code> number
   * @param comments Comments allowed
   * @return <code>true</code> if the number was read successfully
   * @throws IOException
   */
  public boolean readExactNumber(long number, EnumSet<Comment> comments) throws IOException {
    String word = readWord(true, comments);
    try {
      return number == Long.parseLong(word);
    }
    catch(NumberFormatException e) {
      return false;
    }
  }

  /**
   * <!-- readExactNumber -->
   * 
   * @param number <code>float</code> number
   * @return <code>true</code> if the number was read successfully
   * @throws IOException
   */
  public boolean readExactNumber(float number) throws IOException {
    return readExactNumber(number, EnumSet.noneOf(Comment.class));
  }

  /**
   * <!-- readExactNumber -->
   * 
   * @param number <code>float</code> number
   * @param comments Comments allowed
   * @return <code>true</code> if the number was read successfully
   * @throws IOException
   */
  public boolean readExactNumber(float number, EnumSet<Comment> comments) throws IOException {
    String word = readWord(true, comments);
    try {
      return number == Float.parseFloat(word);
    }
    catch(NumberFormatException e) {
      return false;
    }
  }

  /**
   * <!-- readExactNumber -->
   * 
   * @param number <code>double</code> number
   * @return <code>true</code> if the number was read successfully
   * @throws IOException
   */
  public boolean readExactNumber(double number) throws IOException {
    return readExactNumber(number, EnumSet.noneOf(Comment.class));
  }

  /**
   * <!-- readExactNumber -->
   * 
   * @param number <code>double</code> number
   * @param comments Comments allowed
   * @return <code>true</code> if the number was read successfully
   * @throws IOException
   */
  public boolean readExactNumber(double number, EnumSet<Comment> comments) throws IOException {
    String word = readWord(true, comments);
    try {
      return number == Double.parseDouble(word);
    }
    catch(NumberFormatException e) {
      return false;
    }
  }

  /**
   * <!-- readInt -->
   * 
   * <p>
   * Read an integer. On failure to do so, a {@link FileFormatException} is
   * thrown. When reading from a file where you don't know how many integers you
   * are going to read, the following idiom could be used:
   * </p>
   * 
   * <p>
   * <code>&nbsp;&nbsp;TextReader reader;</code><br>
   * <br>
   * <code>&nbsp;&nbsp;while(!reader.eof()) {</code><br>
   * <code>&nbsp;&nbsp;&nbsp;&nbsp;try {</code><br>
   * <code>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;int number = reader.readInt();</code>
   * <br>
   * <br>
   * <code>&nbsp;&nbsp;&nbsp;&nbsp;}</code><br>
   * <code>&nbsp;&nbsp;&nbsp;&nbsp;catch(NumberFormatException e) {</code><br>
   * <code>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;if(!reader.eof()) throw e;</code>
   * <br>
   * <code>&nbsp;&nbsp;&nbsp;&nbsp;}</code><br>
   * <code>&nbsp;&nbsp;}</code><br>
   * </p>
   * 
   * @return The integer read
   * @throws IOException
   */
  public int readInt() throws IOException {
    return readInt(EnumSet.noneOf(Comment.class));
  }

  /**
   * <!-- readInt -->
   * 
   * Read an integer, allowing comments
   * 
   * @see #readInt()
   * @param comments The comments allowed
   * @return The integer read
   * @throws IOException
   */
  public int readInt(EnumSet<Comment> comments) throws IOException {
    String word = readWord(comments);
    try {
      return Integer.parseInt(word);
    }
    catch(NumberFormatException e) {
      throw new FileFormatException(filename, format, "an integer", word);
    }
  }

  /**
   * <!-- readLong -->
   * 
   * Read a long integer
   * 
   * @see #readInt()
   * @return The long integer read
   * @throws IOException
   */
  public long readLong() throws IOException {
    return readLong(EnumSet.noneOf(Comment.class));
  }

  /**
   * <!-- readLong -->
   * 
   * Read a long integer, allowing comments
   * 
   * @see #readInt()
   * @param comments The comments allowed
   * @return The long integer read
   * @throws IOException
   */
  public long readLong(EnumSet<Comment> comments) throws IOException {
    String word = readWord(comments);
    try {
      return Long.parseLong(word);
    }
    catch(NumberFormatException e) {
      throw new FileFormatException(filename, format, "a long integer", word);
    }
  }

  /**
   * <!-- readFloat -->
   * 
   * Read a float
   * 
   * @see #readInt()
   * @return The float read
   * @throws IOException
   */
  public float readFloat() throws IOException {
    return readFloat(EnumSet.noneOf(Comment.class));
  }

  /**
   * <!-- readFloat -->
   * 
   * Read a float, allowing comments
   * 
   * @see #readInt()
   * @param comments The comments allowed
   * @return The float read
   * @throws IOException
   */
  public float readFloat(EnumSet<Comment> comments) throws IOException {
    String word = readWord(comments);
    try {
      return Float.parseFloat(word);
    }
    catch(NumberFormatException e) {
      throw new FileFormatException(filename, format, "a single-precision floating point number", word);
    }
  }

  /**
   * <!-- readDouble -->
   * 
   * Read a double
   * 
   * @see #readInt()
   * @return The double read
   * @throws IOException
   */
  public double readDouble() throws IOException {
    return readDouble(EnumSet.noneOf(Comment.class));
  }

  /**
   * <!-- readDouble -->
   * 
   * Read a double, allowing comments
   * 
   * @see #readInt()
   * @param comments The comments allowed
   * @return The double read
   * @throws IOException
   */
  public double readDouble(EnumSet<Comment> comments) throws IOException {
    String word = readWord(comments);
    try {
      return Double.parseDouble(word);
    }
    catch(NumberFormatException e) {
      throw new FileFormatException(filename, format, "a double-precision floating point number", word);
    }
  }

  /**
   * <!-- readWord -->
   * 
   * Read a word (a word is any sequence of non-whitespace characters delimited
   * by whitespace or the file boundaries)
   * 
   * @see #readWord(boolean,EnumSet,char)
   * @return The word read
   * @throws IOException
   */
  public String readWord() throws IOException {
    return readWord(true, EnumSet.noneOf(Comment.class));
  }

  /**
   * <!-- readWord -->
   * 
   * Read a word, allowing comments
   * 
   * @see #readWord(boolean,EnumSet,char)
   * @param comments The comments allowed
   * @return The word read
   * @throws IOException
   */
  public String readWord(EnumSet<Comment> comments) throws IOException {
    return readWord(true, comments);
  }

  /**
   * <!-- readWord -->
   * 
   * Read a word, allowing comments, with an option to stop ignoring leading
   * whitespace (i.e. leading whitespace will be read in to the word)
   * 
   * @see #readWord(boolean,EnumSet,char)
   * @param ignoreLeadingSpace <code>false</code> if you want the word read to
   *          include any leading whitespace
   * @param comments The comments allowed
   * @return The word read
   * @throws IOException
   */
  public String readWord(boolean ignoreLeadingSpace, EnumSet<Comment> comments) throws IOException {
    return readWord(ignoreLeadingSpace, comments, ' ');
  }

  /**
   * <!-- readWord -->
   * 
   * <p>
   * Read a word, allowing comments, optionally ignoring leading whitespace, and
   * allowing the word to be terminated by a character other than space. If
   * <code>ignoreLeadingSpace</code> is <code>true</code>, this method will
   * throw away characters up to the first non-whitespace character (comments
   * are also thrown away), then read into the returned value until the
   * <code>delimiter</code> character is reached. All the other
   * <code>readWord()</code>, <code>readExactNumber()</code>, and
   * <code>readInt()</code>, <code>readLong()</code>, <code>readFloat()</code>
   * and readDouble()</code> methods call this one, with a space as the
   * <code>delimiter</code>.
   * </p>
   * 
   * <p>
   * The return value will be the empty String ("") if the end of the file was reached without reading anything. To check,
   * do something like this:
   * </p>
   * 
   * <p>
   * <code>&nbsp;&nbsp;TextReader reader;</code><br>
   * <br>
   * <code>&nbsp;&nbsp;String word = readWord(); <font color="grey">// Calls this method</font></code><br>
   * <code>&nbsp;&nbsp;if(word.length() == 0) {</code><br>
   * <code>&nbsp;&nbsp;&nbsp;&nbsp;<font color="grey">// whatever</font></code><br>
   * <code>&nbsp;&nbsp;}</code><br>
   * </p>
   * 
   * <p>
   * Or if you're paranoid or want to future-proof this method:
   * </p>
   * 
   * <p>
   * <code>&nbsp;&nbsp;if(word == null || word.length() == 0) {</code><br>
   * </p>
   * 
   * @param ignoreLeadingSpace
   * @param comments
   * @param delimiter Character to stop reading the word (not included in the
   *          return value, and thrown away). If a space (<code>' '</code>,
   *          <code>0x32</code>), then any whitespace character will stop the
   *          word.
   * @return The word read
   * @throws IOException
   */
  public String readWord(boolean ignoreLeadingSpace, EnumSet<Comment> comments, char delimiter) throws IOException {
    int c;
    int[] last_c = { -1, -1, -1 };
    boolean in_comment = false;
    Comment last_comment = null;
    StringBuffer word = new StringBuffer();
    boolean inLeadingSpace = true;
    while((c = buff.read()) != -1) {
      if(Character.isWhitespace(c) && ignoreLeadingSpace && inLeadingSpace) continue;
      if(((delimiter == ' ' && Character.isWhitespace(c)) || (delimiter != ' ' && c == delimiter)) && !inLeadingSpace
        && !in_comment) {
        break;
      }
      if(!in_comment && comments.contains(Comment.HASH) && c == '#') {
        lastLineRead = buff.readLine();
        lastLineParsed = true;
      }
      else if(!in_comment && comments.contains(Comment.SLASH2) && c == '/' && last_c[0] == '/') {
        word.setLength(word.length() - 1);
        if(word.length() == 0) inLeadingSpace = true;
        lastLineRead = buff.readLine();
        lastLineParsed = true;
      }
      else if(!in_comment && comments.contains(Comment.C) && c == '*' && last_c[0] == '/') {
        word.setLength(word.length() - 1);
        if(word.length() == 0) inLeadingSpace = true;
        in_comment = true;
        last_comment = Comment.C;
      }
      else if(in_comment && last_comment == Comment.C && c == '/' && last_c[0] == '*') {
        in_comment = false;
        last_comment = null;
      }
      else if(!in_comment && comments.contains(Comment.XML) && c == '-' && last_c[0] == '-' && last_c[1] == '!'
        && last_c[2] == '<') {
        word.setLength(word.length() - 3);
        if(word.length() == 0) inLeadingSpace = true;
        in_comment = true;
        last_comment = Comment.XML;
      }
      else if(in_comment && last_comment == Comment.XML && c == '>' && last_c[0] == '-' && last_c[1] == '-') {
        in_comment = false;
        last_comment = null;
      }
      else if(!in_comment) {
        word.append((char)c);
        inLeadingSpace = false;
      }
      for(int j = 1; c < last_c.length; j++) {
        last_c[j] = last_c[j - 1];
      }
      last_c[0] = c;
    }
    if(c == -1) eof = true;
    lastLineParsed = true;
    return word.toString();
  }

  /**
   * <!-- readQuotedString -->
   * 
   * Read a string enclosed in the quoted character.
   * 
   * @see #readQuotedString(EnumSet,char,char)
   * @param quote The character that encloses the string to read on either side
   * @return The string read (without the quotes)
   * @throws IOException
   */
  public String readQuotedString(char quote) throws IOException {
    return readQuotedString(EnumSet.noneOf(Comment.class), quote, quote);
  }

  /**
   * <!-- readQuotedString -->
   * 
   * Read a string enclosed in the quoted character, allowing comments
   * beforehand
   * 
   * @see #readQuotedString(EnumSet,char,char)
   * @param comments The comments allowed
   * @param quote The character that encloses the string to read on either side
   * @return The string read (without the quotes)
   * @throws IOException
   */
  public String readQuotedString(EnumSet<Comment> comments, char quote) throws IOException {
    return readQuotedString(comments, quote, quote);
  }

  /**
   * <!-- readQuotedString -->
   * 
   * Read a string enclosed by two characters, one at the start, one at the end
   * 
   * @see #readQuotedString(EnumSet,char,char)
   * @param startQuote The character starting the quoted string
   * @param endQuote The character ending the quoted string
   * @return The string read (without the quotes)
   * @throws IOException
   */
  public String readQuotedString(char startQuote, char endQuote) throws IOException {
    return readQuotedString(EnumSet.noneOf(Comment.class), startQuote, endQuote);
  }

  /**
   * <!-- readQuotedString -->
   * 
   * Read a string enclosed by two characters, one at the start, one at the end,
   * allowing comments. This method calls
   * {@link #readWord(boolean,EnumSet,char)} to read up to and including the
   * <code>startQuote</code> character, ignoring any leading space and any
   * comments. The word read by that call is expected to have zero length. (That
   * is, a {@link FileFormatException} is thrown if the next non-whitespace
   * non-comment character in the buffer isn't <code>startQuote</code>.) All
   * characters up to, but not including <code>endQuote</code> are then read in
   * to the returned value. No comments are allowed in the quoted string. The
   * <code>endQuote</code> character is read from the buffer, but thrown away.
   * 
   * @param comments Comments that may appear before the quoted string
   * @param startQuote The character denoting the start of the quoted string
   * @param endQuote The character denoting the end of the quoted string
   * @return The string inside the quoted string
   * @throws IOException
   */
  public String readQuotedString(EnumSet<Comment> comments, char startQuote, char endQuote) throws IOException {
    StringBuffer string = new StringBuffer();
    String word = readWord(true, comments, startQuote);
    if(word.length() > 0) {
      throw new FileFormatException(filename, format, "\"" + Character.toString(startQuote) + "\"", word);
    }
    int c;
    while((c = buff.read()) != -1) {
      if(c == endQuote) break;
      string.append((char)c);
    }
    if(c == -1) eof = true;
    if(c != endQuote) {
      throw new FileFormatException(filename, format, "\"" + Character.toString(endQuote) + "\"", null);
    }
    return string.toString();
  }

  /**
   * <!-- readLine -->
   * 
   * Read a line from the buffer. Strictly, this method reads from the current
   * point in the buffer to the line-termination character(s). It is not
   * necessarily the case that the last character read by the buffer was a
   * line-terminator. If such is indeed not the case, then the returned value
   * will not be an entire line.
   * 
   * @return The line read (not including line-termination characters)
   * @throws IOException
   */
  public String readLine() throws IOException {
    lastLineRead = lastLineParsed ? buff.readLine() : lastLineRead;
    if(lastLineRead == null) eof = true;
    lastLineParsed = true;
    return lastLineRead;
  }

  /**
   * <!-- readLineIgnoreLeadingSpace -->
   * 
   * Read a line from the buffer, throwing away any whitespace characters at the
   * start of the line.
   * 
   * @see #readLine()
   * @return The line read, with any initial whitespace characters discarded
   * @throws IOException
   */
  public String readLineIgnoreLeadingSpace() throws IOException {
    String line = readLine();
    String[] words = line.split("\\s+");
    StringBuffer reconstitutedLine = new StringBuffer();
    for(int i = 0; i < words.length; i++) {
      if(reconstitutedLine.length() > 0) reconstitutedLine.append(" ");
      if(words[i] != null && words[i].length() > 0) reconstitutedLine.append(words[i]);
    }
    return reconstitutedLine.toString();
  }

  /**
   * <!-- readTable -->
   * 
   * Read a table, which is expected to have a given number of rows and columns,
   * with one row per line, and columns separated by whitespace.
   * 
   * @param nrows Number of rows the table has
   * @param ncols Number of columns the table has
   * @return The table
   * @throws IOException
   */
  public Table<String> readTable(final int nrows, final int ncols) throws IOException {
    Table<String> table = new Table<String>(nrows, ncols);
    boolean lastLastLineParsed = lastLineParsed;
    int row = 0;
    while(row < nrows && (lastLineRead = lastLastLineParsed ? buff.readLine() : lastLineRead) != null) {
      lastLineParsed = false;
      String[] words = lastLineRead.split("\\s+");
      if(words.length != ncols) break;
      for(int col = 0; col < ncols; col++) {
        table.atRC(row, col, words[col]);
      }
      lastLineParsed = true;
      lastLastLineParsed = false;
      row++;
    }
    if(lastLineRead == null) eof = true;
    if(row < nrows) {
      throw new FileFormatException(filename, format, ncols + " columns of space-separated data", lastLineRead);
    }
    return table;
  }

  /**
   * <!-- readOrderedKeyValuePairs -->
   * 
   * <p>
   * This reads in a series of lines from a file, each of which is supposed to
   * contain a key-value pair separated by white space. Keys are not case
   * sensitive. The argument to the method contains an array of keys, dictating
   * the order in which key-value pairs are to appear in the file. Each element
   * of the array has a simple grammar:
   * </p>
   * 
   * <p>
   * <i>key</i> &rarr; <i>key expression</i> <br>
   * <i>key</i> &rarr; <code><b>?</b></code><i>key expression</i>
   * </p>
   * <p>
   * <i>key expression</i> &rarr; <i>key name</i> {<code><b>|</b></code> <i>key
   * name</i>}
   * </p>
   * <p>
   * <i>key name</i> &rarr; any string not containing white space, ? or |.
   * </p>
   * 
   * <p>
   * Examples: <code>ncols</code> -- the text "ncols" must appear as the key
   * name in the corresponding line in the file. <code>?ncols</code> -- the text
   * "ncols" may appear as a key name in the corresponding line in the file.
   * <code>ncols|nrows</code> -- the text "ncols" or the test "nrows" must
   * appear as the key name in the corresponding line in the file.
   * <code>?ncols|nrows|ntables</code> -- the text "ncols", "nrows" or "ntables"
   * may appear as a key name in the corresponding line in the file.
   * </p>
   * 
   * <p>
   * If the last line read in the file was not parsed, this method will read it.
   * It may also leave an unparsed but read line.
   * </p>
   * 
   * @param keys An array of keys, as defined using the above grammar.
   * @return A map of key names to values.
   * @throws IOException
   */
  public Map<String, String> readOrderedKeyValuePairs(String[] keys) throws IOException {
    Map<String, String> pairs = new HashMap<String, String>();
    int i = 0;
    boolean lastLastLineParsed = lastLineParsed;
    while((lastLineRead = lastLastLineParsed ? buff.readLine() : lastLineRead) != null && i < keys.length) {
      lastLineParsed = false;
      String[] words = lastLineRead.split("\\s+");
      if(words.length != 2) {
        while(i < keys.length && keys[i].startsWith("?")) {
          i++;
        }
        break;
      }
      boolean optional;
      boolean found = false;
      String[] options;
      int keystart = i;
      do {
        optional = keys[i].startsWith("?");
        options = getKeyOptions(keys[i]);
        for(int j = 0; j < options.length; j++) {
          if(words[0].equalsIgnoreCase(options[j])) {
            pairs.put(options[j], words[1]);
            found = true;
            break;
          }
        }
        i++;
        if(found) break;
        if(i == keys.length) break;
      } while(optional);
      if(!found && !optional) {
        throw new FileFormatException(filename, format, getKeyMessage(keys, keystart), words[0]);
      }
      lastLineParsed = true;
      lastLastLineParsed = false;
    }
    if(lastLineRead == null) eof = true;
    if(i < keys.length) {
      throw new FileFormatException(filename, format, getKeyMessage(keys, i), lastLineRead);
    }

    return pairs;
  }

  /**
   * <!-- getKeyMessage -->
   * 
   * Create a more human readable form of a key by replacing | characters with a
   * comma (the last one with "or").
   * 
   * @param keyOptions An array of key options already split from the key at |
   * @return A phrase describing the list
   */
  private String getKeyMessage(final String[] keyOptions) {
    StringBuffer keyMessage = new StringBuffer("\"" + keyOptions[0] + "\"");
    for(int j = 1; j < keyOptions.length - 1; j++) {
      keyMessage.append(", \"" + keyOptions[j] + "\"");
    }
    if(keyOptions.length > 1) {
      keyMessage.append(" or \"" + keyOptions[keyOptions.length - 1] + "\"");
    }
    return keyMessage.toString();
  }

  /**
   * <!-- getKeyMessage -->
   * 
   * Create a more human readable form of a key by removing ? and replacing |
   * characters with a comma (the last one with "or").
   * 
   * @param key The key
   * @return A phrase describing the key
   */
  private String getKeyMessage(String key) {
    return getKeyMessage(getKeyOptions(key));
  }

  /**
   * <!-- getKeyMessage -->
   * 
   * Create a human-readable form of a series of keys. This series should
   * consist of zero or more optional keys followed by one required key. The
   * absence of a required key is a bug.
   * 
   * @param keys The array of keys
   * @param i The position in the array at which to start
   * @return A phrase describing the series of keys
   */
  private String getKeyMessage(final String[] keys, final int i) {
    int count;
    for(count = 0; count + i < keys.length; count++) {
      if(!keys[count + i].startsWith("?")) break;
    }
    if(count == 0) return getKeyMessage(keys[i]);
    if(count + i == keys.length) throw new Bug();

    StringBuffer msg = new StringBuffer("one of the following: ");
    for(int j = 0; j < count; j++) {
      msg.append("; ");
      msg.append(getKeyMessage(keys[i + j]));
    }
    msg.append("; or ");
    msg.append(getKeyMessage(keys[i + count]));
    return msg.toString();
  }

  /**
   * <!-- getKeyOptions -->
   * 
   * Create an array of key options from a key by removing any initial ? and
   * splitting at |.
   * 
   * @param key The key
   * @return An array of key options
   */
  private String[] getKeyOptions(String key) {
    String keywords = key.startsWith("?") ? key.substring(0, key.length() - 1) : key;
    return keywords.split("\\|");
  }
}
