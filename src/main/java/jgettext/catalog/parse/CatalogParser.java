// $ANTLR 2.7.7 (20060906): "catalog.g" -> "CatalogParser.java"$

/*
 * Copyright (c) 2007, Red Hat Middleware, LLC. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, v. 2.1. This program is distributed in the
 * hope that it will be useful, but WITHOUT A WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details. You should have received a
 * copy of the GNU Lesser General Public License, v.2.1 along with this
 * distribution; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 *
 * Red Hat Author(s): Steve Ebersole
 */
package jgettext.catalog.parse;

import antlr.*;
import antlr.collections.AST;
import antlr.collections.impl.ASTArray;
import antlr.collections.impl.BitSet;

/**
 * Defines a parser for the GNU gettext PO/POT file formats.
 * <p/>
 * This grammar is copied nearly verbatim from the kdesdk project from its po/xml package.  It makes certain
 * assumptions about the PO/POT structure that are true for DocBook masters, which is what it was intended to
 * deal with (as is jDocBook, so we can live with those assumptions).
 */
public class CatalogParser extends antlr.LLkParser implements CatalogTokenTypes
{

    public static final String[] _tokenNames = {
            "<0>",
            "EOF",
            "<2>",
            "NULL_TREE_LOOKAHEAD",
            "COMMENT",
            "EXTRACTION",
            "REFERENCE",
            "FLAG",
            "DOMAIN",
            "MSGCTXT",
            "MSGID",
            "MSGID_PLURAL",
            "MSGSTR",
            "MSGSTR_PLURAL",
            "PREV_MSGCTXT",
            "PREV_MSGID",
            "PREV_MSGID_PLURAL",
            "OBSOLETE",
            "PLURALITY",
            "CATALOG",
            "MESSAGE"
    };
    public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
    public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
    public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());


    // callbacks ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());
    public static final BitSet _tokenSet_4 = new BitSet(mk_tokenSet_4());
    public static final BitSet _tokenSet_5 = new BitSet(mk_tokenSet_5());
    public static final BitSet _tokenSet_6 = new BitSet(mk_tokenSet_6());
    public static final BitSet _tokenSet_7 = new BitSet(mk_tokenSet_7());
    public static final BitSet _tokenSet_8 = new BitSet(mk_tokenSet_8());
    public static final BitSet _tokenSet_9 = new BitSet(mk_tokenSet_9());

    protected CatalogParser(TokenBuffer tokenBuf, int k)
    {
        super(tokenBuf, k);
        tokenNames = _tokenNames;
        buildTokenTypeASTClassMap();
        astFactory = new ASTFactory(getTokenTypeToASTClassMap());
    }

    public CatalogParser(TokenBuffer tokenBuf)
    {
        this(tokenBuf, 2);
    }

    protected CatalogParser(TokenStream lexer, int k)
    {
        super(lexer, k);
        tokenNames = _tokenNames;
        buildTokenTypeASTClassMap();
        astFactory = new ASTFactory(getTokenTypeToASTClassMap());
    }

    public CatalogParser(TokenStream lexer)
    {
        this(lexer, 2);
    }

    public CatalogParser(ParserSharedInputState state)
    {
        super(state, 2);
        tokenNames = _tokenNames;
        buildTokenTypeASTClassMap();
        astFactory = new ASTFactory(getTokenTypeToASTClassMap());
    }

    private static final long[] mk_tokenSet_0()
    {
        long[] data = {2L, 0L};
        return data;
    }

    private static final long[] mk_tokenSet_1()
    {
        long[] data = {247794L, 0L};
        return data;
    }

    private static final long[] mk_tokenSet_2()
    {
        long[] data = {231168L, 0L};
        return data;
    }

    private static final long[] mk_tokenSet_3()
    {
        long[] data = {198400L, 0L};
        return data;
    }

    private static final long[] mk_tokenSet_4()
    {
        long[] data = {132864L, 0L};
        return data;
    }

    private static final long[] mk_tokenSet_5()
    {
        long[] data = {132608L, 0L};
        return data;
    }

    private static final long[] mk_tokenSet_6()
    {
        long[] data = {132096L, 0L};
        return data;
    }

    private static final long[] mk_tokenSet_7()
    {
        long[] data = {137216L, 0L};
        return data;
    }

    private static final long[] mk_tokenSet_8()
    {
        long[] data = {139264L, 0L};
        return data;
    }

    private static final long[] mk_tokenSet_9()
    {
        long[] data = {255986L, 0L};
        return data;
    }

    protected AST buildCatalogNode(AST messageBlocks)
    {
        return (AST) astFactory.make((new ASTArray(2)).add(astFactory.create(CATALOG, "catalog")).add(messageBlocks));
    }

    private AST buildMessageBlockNode(AST entries)
    {
        AST node = buildMessageBlockNode("message", entries);
        handleMessageBlock(node);
        return node;
    }

    private AST buildObsoleteMessageBlockNode(AST entries)
    {
        AST node = buildMessageBlockNode("obsolete-message", entries);
        handleObsoleteMessageBlock(node);
        return node;
    }

    private AST buildMessageBlockNode(String text, AST entries)
    {
        return (AST) astFactory.make((new ASTArray(2)).add(astFactory.create(MESSAGE, text)).add(entries));
    }

    protected void handleMessageBlock(AST messageBlock)
    {
    }

    protected void handleObsoleteMessageBlock(AST messageBlock)
    {
    }

    protected void handleCatalogComment(AST comment)
    {
    }

    protected void handleExtractedComment(AST comment)
    {
    }

    protected void handleReference(AST sourceRef)
    {
    }

    protected void handleFlag(AST flag)
    {
    }

    protected void handlePreviousMsgctxt(AST previousMsgctxt)
    {
    }

    protected void handlePreviousMsgid(AST previousMsgid)
    {
    }

    protected void handlePreviousMsgidPlural(AST previousMsgidPlural)
    {
    }

    protected void handleDomain(AST domain)
    {
    }

    protected void handleMsgctxt(AST msgctxt)
    {
    }

    protected void handleMsgid(AST msgid)
    {
    }

    protected void handleMsgidPlural(AST msgidPlural)
    {
    }

    protected void handleMsgstr(AST msgstr)
    {
    }

    ;

    protected void handleMsgstrPlural(AST msgstr, AST plurality)
    {
    }

    /**
     * Main rule
     */
    public final void catalog() throws RecognitionException, TokenStreamException
    {

        returnAST = null;
        ASTPair currentAST = new ASTPair();
        AST catalog_AST = null;
        AST mb_AST = null;

        try
        {      // for error handling
            messageBlocks();
            mb_AST = (AST) returnAST;
            astFactory.addASTChild(currentAST, returnAST);
            catalog_AST = (AST) currentAST.root;

            catalog_AST = buildCatalogNode(mb_AST);

            currentAST.root = catalog_AST;
            currentAST.child = catalog_AST != null && catalog_AST.getFirstChild() != null ?
                    catalog_AST.getFirstChild() : catalog_AST;
            currentAST.advanceChildToEnd();
            catalog_AST = (AST) currentAST.root;
        } catch (RecognitionException ex)
        {
            reportError(ex);
            recover(ex, _tokenSet_0);
        }
        returnAST = catalog_AST;
    }

    public final void messageBlocks() throws RecognitionException, TokenStreamException
    {

        returnAST = null;
        ASTPair currentAST = new ASTPair();
        AST messageBlocks_AST = null;

        try
        {      // for error handling
            {
                _loop4:
                do
                {
                    switch (LA(1))
                    {
                        case COMMENT:
                        {
                            catalogComment();
                            astFactory.addASTChild(currentAST, returnAST);
                            break;
                        }
                        case EXTRACTION:
                        {
                            extractedComment();
                            astFactory.addASTChild(currentAST, returnAST);
                            break;
                        }
                        case REFERENCE:
                        {
                            reference();
                            astFactory.addASTChild(currentAST, returnAST);
                            break;
                        }
                        case FLAG:
                        {
                            flag();
                            astFactory.addASTChild(currentAST, returnAST);
                            break;
                        }
                        case DOMAIN:
                        case MSGCTXT:
                        case MSGID:
                        case PREV_MSGCTXT:
                        case PREV_MSGID:
                        case PREV_MSGID_PLURAL:
                        case OBSOLETE:
                        {
                            messageBlock();
                            astFactory.addASTChild(currentAST, returnAST);
                            break;
                        }
                        default:
                        {
                            break _loop4;
                        }
                    }
                } while (true);
            }
            messageBlocks_AST = (AST) currentAST.root;
        } catch (RecognitionException ex)
        {
            reportError(ex);
            recover(ex, _tokenSet_0);
        }
        returnAST = messageBlocks_AST;
    }

    public final void catalogComment() throws RecognitionException, TokenStreamException
    {

        returnAST = null;
        ASTPair currentAST = new ASTPair();
        AST catalogComment_AST = null;
        Token c = null;
        AST c_AST = null;

        try
        {      // for error handling
            c = LT(1);
            c_AST = astFactory.create(c);
            astFactory.addASTChild(currentAST, c_AST);
            match(COMMENT);

            handleCatalogComment(c_AST);

            catalogComment_AST = (AST) currentAST.root;
        } catch (RecognitionException ex)
        {
            reportError(ex);
            recover(ex, _tokenSet_1);
        }
        returnAST = catalogComment_AST;
    }

    public final void extractedComment() throws RecognitionException, TokenStreamException
    {

        returnAST = null;
        ASTPair currentAST = new ASTPair();
        AST extractedComment_AST = null;
        Token c = null;
        AST c_AST = null;

        try
        {      // for error handling
            c = LT(1);
            c_AST = astFactory.create(c);
            astFactory.addASTChild(currentAST, c_AST);
            match(EXTRACTION);

            handleExtractedComment(c_AST);

            extractedComment_AST = (AST) currentAST.root;
        } catch (RecognitionException ex)
        {
            reportError(ex);
            recover(ex, _tokenSet_1);
        }
        returnAST = extractedComment_AST;
    }

    public final void reference() throws RecognitionException, TokenStreamException
    {

        returnAST = null;
        ASTPair currentAST = new ASTPair();
        AST reference_AST = null;
        Token r = null;
        AST r_AST = null;

        try
        {      // for error handling
            r = LT(1);
            r_AST = astFactory.create(r);
            astFactory.addASTChild(currentAST, r_AST);
            match(REFERENCE);

            handleReference(r_AST);

            reference_AST = (AST) currentAST.root;
        } catch (RecognitionException ex)
        {
            reportError(ex);
            recover(ex, _tokenSet_1);
        }
        returnAST = reference_AST;
    }

    public final void flag() throws RecognitionException, TokenStreamException
    {

        returnAST = null;
        ASTPair currentAST = new ASTPair();
        AST flag_AST = null;
        Token f = null;
        AST f_AST = null;

        try
        {      // for error handling
            f = LT(1);
            f_AST = astFactory.create(f);
            astFactory.addASTChild(currentAST, f_AST);
            match(FLAG);

            handleFlag(f_AST);

            flag_AST = (AST) currentAST.root;
        } catch (RecognitionException ex)
        {
            reportError(ex);
            recover(ex, _tokenSet_1);
        }
        returnAST = flag_AST;
    }

    /**
     * A message block defines all the lines related to a single translatable message entry.
     */
    public final void messageBlock() throws RecognitionException, TokenStreamException
    {

        returnAST = null;
        ASTPair currentAST = new ASTPair();
        AST messageBlock_AST = null;
        AST o_AST = null;

        try
        {      // for error handling
            {
                switch (LA(1))
                {
                    case PREV_MSGCTXT:
                    {
                        previousMsgctxt();
                        astFactory.addASTChild(currentAST, returnAST);
                        break;
                    }
                    case DOMAIN:
                    case MSGCTXT:
                    case MSGID:
                    case PREV_MSGID:
                    case PREV_MSGID_PLURAL:
                    case OBSOLETE:
                    {
                        break;
                    }
                    default:
                    {
                        throw new NoViableAltException(LT(1), getFilename());
                    }
                }
            }
            {
                switch (LA(1))
                {
                    case PREV_MSGID:
                    {
                        previousMsgid();
                        astFactory.addASTChild(currentAST, returnAST);
                        break;
                    }
                    case DOMAIN:
                    case MSGCTXT:
                    case MSGID:
                    case PREV_MSGID_PLURAL:
                    case OBSOLETE:
                    {
                        break;
                    }
                    default:
                    {
                        throw new NoViableAltException(LT(1), getFilename());
                    }
                }
            }
            {
                switch (LA(1))
                {
                    case PREV_MSGID_PLURAL:
                    {
                        previousMsgidPlural();
                        astFactory.addASTChild(currentAST, returnAST);
                        break;
                    }
                    case DOMAIN:
                    case MSGCTXT:
                    case MSGID:
                    case OBSOLETE:
                    {
                        break;
                    }
                    default:
                    {
                        throw new NoViableAltException(LT(1), getFilename());
                    }
                }
            }
            {
                switch (LA(1))
                {
                    case DOMAIN:
                    {
                        domain();
                        astFactory.addASTChild(currentAST, returnAST);
                        break;
                    }
                    case MSGCTXT:
                    case MSGID:
                    case OBSOLETE:
                    {
                        break;
                    }
                    default:
                    {
                        throw new NoViableAltException(LT(1), getFilename());
                    }
                }
            }
            {
                switch (LA(1))
                {
                    case MSGCTXT:
                    case MSGID:
                    {
                        entries();
                        astFactory.addASTChild(currentAST, returnAST);
                        break;
                    }
                    case OBSOLETE:
                    {
                        obsoleteEntries();
                        o_AST = (AST) returnAST;
                        astFactory.addASTChild(currentAST, returnAST);
                        break;
                    }
                    default:
                    {
                        throw new NoViableAltException(LT(1), getFilename());
                    }
                }
            }
            messageBlock_AST = (AST) currentAST.root;

            if (o_AST == null)
            {
                messageBlock_AST = buildMessageBlockNode(messageBlock_AST);
            }
            else
            {
                messageBlock_AST = buildObsoleteMessageBlockNode(messageBlock_AST);
            }

            currentAST.root = messageBlock_AST;
            currentAST.child = messageBlock_AST != null && messageBlock_AST.getFirstChild() != null ?
                    messageBlock_AST.getFirstChild() : messageBlock_AST;
            currentAST.advanceChildToEnd();
            messageBlock_AST = (AST) currentAST.root;
        } catch (RecognitionException ex)
        {
            reportError(ex);
            recover(ex, _tokenSet_1);
        }
        returnAST = messageBlock_AST;
    }

    public final void previousMsgctxt() throws RecognitionException, TokenStreamException
    {

        returnAST = null;
        ASTPair currentAST = new ASTPair();
        AST previousMsgctxt_AST = null;
        Token pmc = null;
        AST pmc_AST = null;

        try
        {      // for error handling
            pmc = LT(1);
            pmc_AST = astFactory.create(pmc);
            astFactory.addASTChild(currentAST, pmc_AST);
            match(PREV_MSGCTXT);

            handlePreviousMsgctxt(pmc_AST);

            previousMsgctxt_AST = (AST) currentAST.root;
        } catch (RecognitionException ex)
        {
            reportError(ex);
            recover(ex, _tokenSet_2);
        }
        returnAST = previousMsgctxt_AST;
    }

    public final void previousMsgid() throws RecognitionException, TokenStreamException
    {

        returnAST = null;
        ASTPair currentAST = new ASTPair();
        AST previousMsgid_AST = null;
        Token pmi = null;
        AST pmi_AST = null;

        try
        {      // for error handling
            pmi = LT(1);
            pmi_AST = astFactory.create(pmi);
            astFactory.addASTChild(currentAST, pmi_AST);
            match(PREV_MSGID);

            handlePreviousMsgid(pmi_AST);

            previousMsgid_AST = (AST) currentAST.root;
        } catch (RecognitionException ex)
        {
            reportError(ex);
            recover(ex, _tokenSet_3);
        }
        returnAST = previousMsgid_AST;
    }

    public final void previousMsgidPlural() throws RecognitionException, TokenStreamException
    {

        returnAST = null;
        ASTPair currentAST = new ASTPair();
        AST previousMsgidPlural_AST = null;
        Token pmip = null;
        AST pmip_AST = null;

        try
        {      // for error handling
            pmip = LT(1);
            pmip_AST = astFactory.create(pmip);
            astFactory.addASTChild(currentAST, pmip_AST);
            match(PREV_MSGID_PLURAL);

            handlePreviousMsgidPlural(pmip_AST);

            previousMsgidPlural_AST = (AST) currentAST.root;
        } catch (RecognitionException ex)
        {
            reportError(ex);
            recover(ex, _tokenSet_4);
        }
        returnAST = previousMsgidPlural_AST;
    }

    public final void domain() throws RecognitionException, TokenStreamException
    {

        returnAST = null;
        ASTPair currentAST = new ASTPair();
        AST domain_AST = null;
        Token d = null;
        AST d_AST = null;

        try
        {      // for error handling
            d = LT(1);
            d_AST = astFactory.create(d);
            astFactory.addASTChild(currentAST, d_AST);
            match(DOMAIN);

            handleDomain(d_AST);

            domain_AST = (AST) currentAST.root;
        } catch (RecognitionException ex)
        {
            reportError(ex);
            recover(ex, _tokenSet_5);
        }
        returnAST = domain_AST;
    }

    public final void entries() throws RecognitionException, TokenStreamException
    {

        returnAST = null;
        ASTPair currentAST = new ASTPair();
        AST entries_AST = null;

        try
        {      // for error handling
            {
                switch (LA(1))
                {
                    case MSGCTXT:
                    {
                        msgctxt();
                        astFactory.addASTChild(currentAST, returnAST);
                        break;
                    }
                    case MSGID:
                    {
                        break;
                    }
                    default:
                    {
                        throw new NoViableAltException(LT(1), getFilename());
                    }
                }
            }
            msgid();
            astFactory.addASTChild(currentAST, returnAST);
            {
                switch (LA(1))
                {
                    case MSGSTR:
                    {
                        msgstr();
                        astFactory.addASTChild(currentAST, returnAST);
                        break;
                    }
                    case MSGID_PLURAL:
                    {
                        msgidPlural();
                        astFactory.addASTChild(currentAST, returnAST);
                        {
                            int _cnt15 = 0;
                            _loop15:
                            do
                            {
                                if ((LA(1) == MSGSTR_PLURAL))
                                {
                                    msgstrPlural();
                                    astFactory.addASTChild(currentAST, returnAST);
                                }
                                else
                                {
                                    if (_cnt15 >= 1)
                                    {
                                        break _loop15;
                                    }
                                    else
                                    {
                                        throw new NoViableAltException(LT(1), getFilename());
                                    }
                                }

                                _cnt15++;
                            } while (true);
                        }
                        break;
                    }
                    default:
                    {
                        throw new NoViableAltException(LT(1), getFilename());
                    }
                }
            }
            entries_AST = (AST) currentAST.root;
        } catch (RecognitionException ex)
        {
            reportError(ex);
            recover(ex, _tokenSet_1);
        }
        returnAST = entries_AST;
    }

    public final void obsoleteEntries() throws RecognitionException, TokenStreamException
    {

        returnAST = null;
        ASTPair currentAST = new ASTPair();
        AST obsoleteEntries_AST = null;

        try
        {      // for error handling
            {
                if ((LA(1) == OBSOLETE) && (LA(2) == MSGCTXT))
                {
                    match(OBSOLETE);
                    msgctxt();
                    astFactory.addASTChild(currentAST, returnAST);
                }
                else if ((LA(1) == OBSOLETE) && (LA(2) == MSGID))
                {
                }
                else
                {
                    throw new NoViableAltException(LT(1), getFilename());
                }

            }
            match(OBSOLETE);
            msgid();
            astFactory.addASTChild(currentAST, returnAST);
            {
                if ((LA(1) == OBSOLETE) && (LA(2) == MSGSTR))
                {
                    match(OBSOLETE);
                    msgstr();
                    astFactory.addASTChild(currentAST, returnAST);
                }
                else if ((LA(1) == OBSOLETE) && (LA(2) == MSGID_PLURAL))
                {
                    match(OBSOLETE);
                    msgidPlural();
                    astFactory.addASTChild(currentAST, returnAST);
                    {
                        int _cnt20 = 0;
                        _loop20:
                        do
                        {
                            if ((LA(1) == OBSOLETE) && (LA(2) == MSGSTR_PLURAL))
                            {
                                match(OBSOLETE);
                                msgstrPlural();
                                astFactory.addASTChild(currentAST, returnAST);
                            }
                            else
                            {
                                if (_cnt20 >= 1)
                                {
                                    break _loop20;
                                }
                                else
                                {
                                    throw new NoViableAltException(LT(1), getFilename());
                                }
                            }

                            _cnt20++;
                        } while (true);
                    }
                }
                else
                {
                    throw new NoViableAltException(LT(1), getFilename());
                }

            }
            obsoleteEntries_AST = (AST) currentAST.root;
        } catch (RecognitionException ex)
        {
            reportError(ex);
            recover(ex, _tokenSet_1);
        }
        returnAST = obsoleteEntries_AST;
    }

    public final void msgctxt() throws RecognitionException, TokenStreamException
    {

        returnAST = null;
        ASTPair currentAST = new ASTPair();
        AST msgctxt_AST = null;
        Token mc = null;
        AST mc_AST = null;

        try
        {      // for error handling
            mc = LT(1);
            mc_AST = astFactory.create(mc);
            astFactory.addASTChild(currentAST, mc_AST);
            match(MSGCTXT);

            handleMsgctxt(mc_AST);

            msgctxt_AST = (AST) currentAST.root;
        } catch (RecognitionException ex)
        {
            reportError(ex);
            recover(ex, _tokenSet_6);
        }
        returnAST = msgctxt_AST;
    }

    public final void msgid() throws RecognitionException, TokenStreamException
    {

        returnAST = null;
        ASTPair currentAST = new ASTPair();
        AST msgid_AST = null;
        Token mi = null;
        AST mi_AST = null;

        try
        {      // for error handling
            mi = LT(1);
            mi_AST = astFactory.create(mi);
            astFactory.addASTChild(currentAST, mi_AST);
            match(MSGID);

            handleMsgid(mi_AST);

            msgid_AST = (AST) currentAST.root;
        } catch (RecognitionException ex)
        {
            reportError(ex);
            recover(ex, _tokenSet_7);
        }
        returnAST = msgid_AST;
    }

    public final void msgstr() throws RecognitionException, TokenStreamException
    {

        returnAST = null;
        ASTPair currentAST = new ASTPair();
        AST msgstr_AST = null;
        Token t = null;
        AST t_AST = null;

        try
        {      // for error handling
            t = LT(1);
            t_AST = astFactory.create(t);
            astFactory.addASTChild(currentAST, t_AST);
            match(MSGSTR);

            handleMsgstr(t_AST);

            msgstr_AST = (AST) currentAST.root;
        } catch (RecognitionException ex)
        {
            reportError(ex);
            recover(ex, _tokenSet_1);
        }
        returnAST = msgstr_AST;
    }

    public final void msgidPlural() throws RecognitionException, TokenStreamException
    {

        returnAST = null;
        ASTPair currentAST = new ASTPair();
        AST msgidPlural_AST = null;
        Token mip = null;
        AST mip_AST = null;

        try
        {      // for error handling
            mip = LT(1);
            mip_AST = astFactory.create(mip);
            astFactory.addASTChild(currentAST, mip_AST);
            match(MSGID_PLURAL);

            handleMsgidPlural(mip_AST);

            msgidPlural_AST = (AST) currentAST.root;
        } catch (RecognitionException ex)
        {
            reportError(ex);
            recover(ex, _tokenSet_8);
        }
        returnAST = msgidPlural_AST;
    }

    public final void msgstrPlural() throws RecognitionException, TokenStreamException
    {

        returnAST = null;
        ASTPair currentAST = new ASTPair();
        AST msgstrPlural_AST = null;
        Token t = null;
        AST t_AST = null;
        Token p = null;
        AST p_AST = null;

        try
        {      // for error handling
            t = LT(1);
            t_AST = astFactory.create(t);
            astFactory.addASTChild(currentAST, t_AST);
            match(MSGSTR_PLURAL);
            p = LT(1);
            p_AST = astFactory.create(p);
            astFactory.addASTChild(currentAST, p_AST);
            match(PLURALITY);

            handleMsgstrPlural(t_AST, p_AST);

            msgstrPlural_AST = (AST) currentAST.root;
        } catch (RecognitionException ex)
        {
            reportError(ex);
            recover(ex, _tokenSet_9);
        }
        returnAST = msgstrPlural_AST;
    }

    protected void buildTokenTypeASTClassMap()
    {
        tokenTypeToASTClassMap = null;
    }

}
