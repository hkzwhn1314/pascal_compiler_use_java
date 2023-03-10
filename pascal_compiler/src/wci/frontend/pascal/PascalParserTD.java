package wci.frontend.pascal;

import wci.frontend.*;
import wci.frontend.pascal.parsers.*;
import wci.intermediate.*;
import wci.message.*;

import java.util.EnumSet;

import static wci.frontend.pascal.PascalTokenType.*;
import static wci.frontend.pascal.PascalErrorCode.*;
import static wci.message.MessageType.PARSER_SUMMARY;

/**
 * <h1>PascalParserTD</h1>
 *
 * <p>The top-down Pascal parser.</p>
 *
 *
 * <p>For instructional purposes only.  No warranties.</p>
 */
public class PascalParserTD extends Parser {
    protected static PascalErrorHandler errorHandler = new PascalErrorHandler();

    /**
     * Constructor.
     *
     * @param scanner the scanner to be used with this parser.
     */
    public PascalParserTD(Scanner scanner) {
        super(scanner);
    }

    /**
     * Constructor for subclasses.
     *
     * @param parent the parent parser.
     */
    public PascalParserTD(PascalParserTD parent) {
        super(parent.getScanner());
    }

    /**
     * Getter.
     *
     * @return the error handler.
     */
    public PascalErrorHandler getErrorHandler() {
        return errorHandler;
    }

    /**
     * Parse a Pascal source program and generate the symbol table
     * and the intermediate code.
     *
     * @throws Exception if an error occurred.
     */
    public void parse()
            throws Exception {
        long startTime = System.currentTimeMillis();
        iCode = ICodeFactory.createICode();

        try {
            Token token = nextToken();
            ICodeNode rootNode = null;

            // Look for the BEGIN token to parse a compound statement.
            if (token.getType() == BEGIN) {
                StatementParser statementParser = new StatementParser(this);
                rootNode = statementParser.parse(token);
                token = currentToken();
            } else {
                errorHandler.flag(token, UNEXPECTED_TOKEN, this);
            }

            // Look for the final period.
            if (token.getType() != DOT) {
                errorHandler.flag(token, MISSING_PERIOD, this);
            }
            token = currentToken();

            // Set the parse tree root node.
            if (rootNode != null) {
                iCode.setRoot(rootNode);
            }

            // Send the parser summary message.
            float elapsedTime = (System.currentTimeMillis() - startTime) / 1000f;
            sendMessage(new Message(PARSER_SUMMARY,
                    new Number[]{token.getLineNumber(),
                            getErrorCount(),
                            elapsedTime}));
        } catch (java.io.IOException ex) {
            errorHandler.abortTranslation(IO_ERROR, this);
        }
    }

    /**
     * Return the number of syntax errors found by the parser.
     *
     * @return the error count.
     */
    public int getErrorCount() {
        return errorHandler.getErrorCount();
    }
    // variable
    //PLUS, MINUS, IDENTIFIER, INTEGER, REAL, STRING,
    // PascalTokenType.NOT, LEFT_PAREN := SEMICOLON, END, ELSE, UNTIL, DOT
    public Token synchronize(EnumSet syncSet) throws Exception {
        Token token = currentToken();
        if (!syncSet.contains(token.getType())) {
            // Flag the unexpected token.
            errorHandler.flag(token, UNEXPECTED_TOKEN, this);
            do {
                token = nextToken();
            } while (!(token instanceof EofToken) &&
                    !syncSet.contains(token.getType()));
        }
        return token;
    }

}

