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

public interface CatalogTokenTypes
{
    int EOF = 1;
    int NULL_TREE_LOOKAHEAD = 3;
    int COMMENT = 4;
    int EXTRACTION = 5;
    int REFERENCE = 6;
    int FLAG = 7;
    int DOMAIN = 8;
    int MSGCTXT = 9;
    int MSGID = 10;
    int MSGID_PLURAL = 11;
    int MSGSTR = 12;
    int MSGSTR_PLURAL = 13;
    int PREV_MSGCTXT = 14;
    int PREV_MSGID = 15;
    int PREV_MSGID_PLURAL = 16;
    int OBSOLETE = 17;
    int PLURALITY = 18;
    int CATALOG = 19;
    int MESSAGE = 20;
}
