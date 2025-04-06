/*
 *	MSS Code Factory CFBam 2.13 CustXmlLoader
 *
 *	Copyright (c) 2020 Mark Stephen Sobkow
 *	
 *	This file is part of MSS Code Factory.
 *	
 *	MSS Code Factory is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.
 *	
 *	MSS Code Factory is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU General Public License for more details.
 *	
 *	You should have received a copy of the GNU General Public License
 *	along with MSS Code Factory.  If not, see https://www.gnu.org/licenses/.
 *	
 *	Donations to support MSS Code Factory can be made at
 *	https://www.paypal.com/paypalme2/MarkSobkow
 *	
 *	Contact Mark Stephen Sobkow at mark.sobkow@gmail.com for commercial licensing.
 */

package org.msscf.msscf.v2_13.cfbamcust.CFBamXmlLoader;

import java.math.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import org.msscf.msscf.v2_13.cflib.CFLib.*;
import org.apache.commons.codec.binary.Base64;
import org.xml.sax.*;
import org.msscf.msscf.v2_13.cfbam.CFBam.*;
import org.msscf.msscf.v2_13.cfbam.CFBamObj.*;

/*
 *	CFBamXmlLoaderTableAddendumHandler SAX Element Handler implementation
 */
public class CFBamXmlLoaderTableAddendumHandler
	extends CFLibXmlCoreElementHandler
{
	public CFBamXmlLoaderTableAddendumHandler( CFBamXmlLoader saxLoader ) {
		super( saxLoader );
	}

	public void startElement(
		String		uri,
		String		localName,
		String		qName,
		Attributes	attrs )
	throws SAXException
	{
		final String S_ProcName = "startElement";

		// TableRelations attributes
		String		attrFromTable = null;

		// Attribute Extraction
		String	attrLocalName;
		int		numAttrs;
		int		idxAttr;

		assert qName.equals( "TableAddendum" );

		numAttrs = attrs.getLength();
		for( idxAttr = 0; idxAttr < numAttrs; idxAttr++ ) {
			attrLocalName = attrs.getLocalName( idxAttr );
			if( attrLocalName.equals( "FromTable" ) ) {
				if( attrFromTable != null ) {
					throw new CFLibUniqueIndexViolationException( getClass(),
						S_ProcName,
						attrLocalName );
				}
				attrFromTable = attrs.getValue( idxAttr );
			}
			else if( attrLocalName.equals( "schemaLocation" ) ) {
				// ignored
			}
			else {
				throw new CFLibUnrecognizedAttributeException( getClass(),
					S_ProcName,
					"LocalName",
					attrLocalName );
			}
		}

		if( ( attrFromTable == null ) || ( attrFromTable.length() <= 0 ) ) {
			throw new CFLibEmptyArgumentException( getClass(),
				S_ProcName,
				0,
				"FromTable" );
		}

		CFBamXmlLoader saxLoader = (CFBamXmlLoader)getParser();
		if( saxLoader == null ) {
			throw new CFLibNullArgumentException( getClass(),
				S_ProcName,
				0,
				"getParser()" );
		}

		CFLibXmlCoreContext curContext = saxLoader.getCurContext();
		if( curContext == null ) {
			throw new CFLibNullArgumentException( getClass(),
				S_ProcName,
				0,
				"getParser().getCurContext()" );
		}

		ICFBamSchemaObj schemaObj = saxLoader.getSchemaObj();
		if( schemaObj == null ) {
			throw new CFLibNullArgumentException( getClass(),
				S_ProcName,
				0,
				"getParser().getSchemaObj()" );
		}

		if( ! CFBamXmlLoader.getProcessSchema( curContext ) ) {
			return;
		}

		CFLibXmlCoreContext prevContext = curContext.getPrevContext();
		ICFBamSchemaDefObj schemaDef = (ICFBamSchemaDefObj)( prevContext.getNamedValue( "Object" ) );
		ICFBamTableObj fromTable = schemaObj.getTableTableObj().readTableByUNameIdx( schemaDef.getRequiredTenantId(),
			schemaDef.getRequiredId(),
			attrFromTable );
		if( fromTable == null ) {
			throw new CFLibRuntimeException( getClass(),
				S_ProcName,
				" Could not resolve FromTable \""
					+ attrFromTable + "\" in Schema \""
					+ schemaDef.getObjFullName()
					+ "\"" );
		}
		curContext.putNamedValue( "FromTable", fromTable );

		// No scope to add here, just resolving a table context
		curContext.putNamedValue( "Object", fromTable );
	}

	public void endElement(
		String		uri,
		String		localName,
		String		qName )
	throws SAXException
	{
	}
}
