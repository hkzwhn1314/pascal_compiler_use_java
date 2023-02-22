package wci.frontend.pascal.parsers;

import wci.frontend.Token;
import wci.frontend.pascal.PascalParserTD;
import wci.intermediate.ICodeNode;

/**
 * @Author zhaocenliu
 * @create 2023/2/16 5:16 PM
 */
public class CaseStatementParser extends StatementParser {
    /**
     * Constructor.
     *
     * @param parent the parent parser.
     */
    public CaseStatementParser(PascalParserTD parent) {
        super(parent);
    }

    //CASE i+1 OF
    //1: j := i;
    //4: j := 4*i;
    //5, 2, 3: j := 523*i;
    //END

    public ICodeNode parse(Token token) throws Exception {
        return null;
    }
}
