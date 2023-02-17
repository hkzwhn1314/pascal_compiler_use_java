package wci.frontend.pascal.parsers;

import wci.frontend.Token;
import wci.frontend.pascal.PascalParserTD;
import wci.frontend.pascal.PascalTokenType;
import wci.intermediate.ICodeNode;

import java.util.EnumSet;

import static wci.frontend.pascal.PascalTokenType.DO;

/**
 * @Author zhaocenliu
 * @create 2023/2/16 5:16 PM
 */
public class WhileStatementParser extends StatementParser {


    /**
     * Constructor.
     *
     * @param parent the parent parser.
     */
    public WhileStatementParser(PascalParserTD parent) {
        super(parent);
    }

    // Synchronization set for DO.
    private static final EnumSet<PascalTokenType> DO_SET =
            StatementParser.STMT_START_SET.clone();

    static {
        DO_SET.add(DO);
        DO_SET.addAll(StatementParser.STMT_FOLLOW_SET);
    }

    public ICodeNode parse(Token token) throws Exception {
        return null;
    }
}
