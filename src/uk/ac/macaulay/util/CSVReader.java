/*
 * uk.ac.macaulay.util: CSVReader.java
 * 
 * Copyright (C) 2009 Macaulay Institute
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
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * CSVReader
 * 
 * Read a CSV file. This can be from a file or a web address. In the simplest
 * form a CSV file consists of a series of lines, each cell of which is
 * separated by a comma. To include a comma in a cell, the cell must be in
 * quotes. If the cell contains a quote, then the cell must be in quotes, and
 * each quote to include in the cell appears twice.
 * <p>
 * Calling the constructor causes the file to be read in. The data can then be
 * accessed by asking for the Table. Class methods are available to allow CSV
 * formatted data to be parsed on behalf of the caller.
 * 
 * @author Gary Polhill
 */
public class CSVReader {
  /**
   * The data read in from the CSV file, as a list of list of cells
   */
  LinkedList<LinkedList<String>> cells;

  /**
   * The number of rows in the CSV file
   */
  int nrows;

  /**
   * The (maximum) number of columns
   */
  int ncols;

  /**
   * The name of the CSV file
   */
  String filename;

  /**
   * Store the name of the file being worked on temporarily
   */
  private static String working_on_file = null;

  /**
   * Store the row being worked on temporarily
   */
  private static int working_on_row;

  /**
   * Constructor, reading in all the data in the CSV file.
   * 
   * @param filename The file from which to read the CSV data. If this begins
   *          with http:// or https://, then it is assumed the file is to be
   *          downloaded from the web.
   * @throws IOException
   * @throws CSVException
   */
  public CSVReader(String filename) throws IOException, CSVException {
    this.filename = filename;
    BufferedReader buff = FileOpener.read(filename);
    cells = new LinkedList<LinkedList<String>>();
    nrows = 0;
    ncols = 0;
    String line;
    while((line = buff.readLine()) != null) {
      LinkedList<String> row = parseCells(line, filename, nrows + 1);
      if(row.size() > ncols) ncols = row.size();
      cells.addLast(row);
      nrows++;
    }
  }

  /**
   * <!-- getTable -->
   * 
   * @return A table from the data read in.
   */
  public Table<String> getTable() {
    Table<String> table = new Table<String>(nrows, ncols);

    int row_n = 0;
    for(LinkedList<String> row: cells) {
      int col_n = 0;

      for(String cell: row) {
        table.atRC(row_n, col_n, cell);
        col_n++;
      }
      row_n++;
    }

    return table;
  }

  /**
   * <!-- getHeadedTable -->
   * 
   * This method will throw an IllegalStateException if the number of column
   * headings is different from the maximum number of columns found in any one
   * row.
   * 
   * @return A table with headings from the data read in.
   */
  public HeadedTable<String> getHeadedTable() {
    String[] colHeadings;
 
    ListIterator<LinkedList<String>> i = cells.listIterator();
    
    if(i.hasNext()) {
      colHeadings = i.next().toArray(new String[0]);
    }
    else {
      throw new IllegalStateException("There are no headings");
    }

    if(colHeadings.length != ncols) {
      throw new IllegalStateException("Number of headings (" + colHeadings.length
        + ") different from number of columns (" + ncols + ")");
    }
    HeadedTable<String> table = new HeadedTable<String>(nrows - 1, colHeadings);

    while(i.hasNext()) {
      LinkedList<String> row = i.next();

      int col_n = 0;
      int row_n = i.previousIndex() - 1;

      for(String cell: row) {
        table.atRC(row_n, col_n, cell);
        col_n++;
      }
    }

    return table;
  }

  /**
   * <!-- tokenize -->
   * 
   * Convert a line of CSV text into a series of tokens. Each token is one of:
   * <ul>
   * <li>A comma</li>
   * <li>A double quote</li>
   * <li>A string of text not containing one of the above</li>
   * </ul>
   * 
   * @param line The line of text to tokenize.
   * @return A list of CSV tokens in the line.
   */
  public static LinkedList<String> tokenize(String line) {
    char[] chars = line.toCharArray();
    StringBuffer buf = new StringBuffer();
    LinkedList<String> tokens = new LinkedList<String>();
    for(int i = 0; i < chars.length; i++) {
      if(chars[i] == '"' || chars[i] == ',') {
        if(buf.length() > 0) {
          tokens.addLast(buf.toString());
          buf = new StringBuffer();
        }
        tokens.addLast(new String(new char[] { chars[i] }));
      }
      else {
        buf.append(chars[i]);
      }
    }
    if(buf.length() > 0) tokens.addLast(buf.toString());

    return tokens;
  }

  /**
   * <!-- parseCells -->
   * 
   * Parse a line of text from a file into a list of cells.
   * 
   * @param line The line of text
   * @param file The file it came from
   * @param row The row number (line number) in the file
   * @return A list of cells in the line
   * @throws CSVException
   */
  public static synchronized LinkedList<String> parseCells(String line, String file, int row) throws CSVException {
    // Temporarily store the file and line number in the file for feeding back
    // relevant information to the user
    working_on_file = file;
    working_on_row = row;
    // Call parseCells(String)
    LinkedList<String> rowcells = parseCells(line);
    working_on_file = null;
    working_on_row = 0;
    return rowcells;
  }

  /**
   * <!-- parseCells -->
   * 
   * Parse a line of text into a list of cells.
   * 
   * @param line The line of text
   * @return A list of cells in the line
   * @throws CSVException
   */
  public static synchronized LinkedList<String> parseCells(String line) throws CSVException {
    // Convert the line into a series of tokens
    LinkedList<String> tokens = tokenize(line);
    LinkedList<String> row = new LinkedList<String>();
    // Call private recursive methods to parse the tokens and build the list of
    // cells
    parseCells(tokens, row);
    return row;
  }

  /**
   * <!-- parseCells -->
   * 
   * Private entry method into recursive parsing of a line of CSV text, which
   * calls the main method.
   * 
   * @param tokens A list of tokens in the line
   * @param row A list of cells in the row so far (empty when called)
   * @throws CSVException
   */
  private static void parseCells(LinkedList<String> tokens, LinkedList<String> row) throws CSVException {
    parseCells(tokens, row, new StringBuffer());
  }

  /**
   * <!-- parseCells -->
   * 
   * Main private method for recursively parsing a line of CSV text.
   * 
   * @param tokens A list of the remaining tokens in the line
   * @param row The row of cells being built up from the tokens in the line
   * @param cell The current cell being built up from the tokens (empty when
   *          first called)
   * @throws CSVException
   */
  private static void parseCells(LinkedList<String> tokens, LinkedList<String> row, StringBuffer cell)
      throws CSVException {
    if(tokens.size() == 0) {
      row.addLast(cell.toString());
      return;
    }
    String token = tokens.removeFirst();
    if(token.equals(",")) {
      row.addLast(cell.toString());
      parseCells(tokens, row, new StringBuffer());
    }
    else if(token.equals("\"")) {
      if(cell.length() > 0)
        throw new CSVException(", (comma)", "\" (quote)", working_on_file, working_on_row, row.size() + 1);
      parseQuotedCell(tokens, row, cell);
    }
    else {
      cell.append(token);
      parseCells(tokens, row, cell);
    }
  }

  /**
   * <!-- parseQuotedCell -->
   * 
   * Partner of the main private recursive CSV parsing method to parse a cell in
   * quotes. The first quote in the cell is assumed to have been read. This
   * method will keep going until it finds the closing quote, which is followed
   * either by a comma or the end of the line. If this is not found, an
   * exception is raised.
   * 
   * @param tokens A list of tokens on the current line of text
   * @param row The row of cells so far found
   * @param cell The contents of the current cell.
   * @throws CSVException
   */
  private static void parseQuotedCell(LinkedList<String> tokens, LinkedList<String> row, StringBuffer cell)
      throws CSVException {
    if(tokens.size() == 0) {
      // End of line reached without a closing quote
      throw new CSVException("\" (quote)", "end of line", working_on_file, working_on_row, row.size() + 1);
    }
    String token = tokens.removeFirst();
    if(token.equals("\"")) {
      // Found a quote, but is it the closing quote? Yes if the end of the line
      // is reached (no more tokens) or the next token is a comma. No if the
      // next token is another quote (then the quote is to be added to the
      // contents of the cell). If the next token is not a comma or quote, then
      // a CSV format exception has occurred.
      if(tokens.size() == 0) return;
      String next_token = tokens.removeFirst();
      if(next_token.equals("\"")) {
        cell.append(token);
        parseQuotedCell(tokens, row, cell);
      }
      else if(next_token.equals(",")) {
        row.addLast(cell.toString());
        parseCells(tokens, row, new StringBuffer());
      }
      else {
        throw new CSVException("\" (quote) or , (comma)", token, working_on_file, working_on_row, row.size() + 1);
      }
    }
    else {
      cell.append(token);
      parseQuotedCell(tokens, row, cell);
    }
  }

}
