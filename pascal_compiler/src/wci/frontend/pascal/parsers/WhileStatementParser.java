package wci.frontend.pascal.parsers;

import wci.frontend.Token;
import wci.frontend.pascal.PascalParserTD;
import wci.intermediate.ICodeNode;

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

    public ICodeNode parse(Token token) throws Exception {
        return null;
    }
}
