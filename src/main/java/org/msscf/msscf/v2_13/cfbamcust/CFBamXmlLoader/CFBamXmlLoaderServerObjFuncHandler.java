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

import org.msscf.msscf.v2_13.cflib.CFLib.*;
import org.xml.sax.*;
import org.msscf.msscf.v2_13.cfbam.CFBamObj.*;

/*
 *	CFBamXmlLoaderServerObjFuncParse XML SAX Element Handler implementation
 *	for ServerObjFunc.
 */
public class CFBamXmlLoaderServerObjFuncHandler
	extends CFLibXmlCoreElementHandler
{
	public CFBamXmlLoaderServerObjFuncHandler( CFBamXmlLoader saxLoader ) {
		super( saxLoader );
	}

	public void startElement(
		String		uri,
		String		localName,
		String		qName,
		Attributes	attrs )
	throws SAXException
	{
		try {
			// Common XML Attributes
			String	attrId = null;
			// Scope Attributes
			// Scope References
			ICFBamTenantObj refTenant = null;
			// ServerObjFunc Attributes
			String	attrName = null;
			String	attrShortName = null;
			String	attrLabel = null;
			String	attrShortDescription = null;
			String	attrDescription = null;
			String	attrSuffix = null;
			String	attrIsInstanceMethod = null;
			String	attrReturnTable = null;
			String	attrIsServerOnly = null;
			// ServerObjFunc References
			ICFBamTenantObj refSrvProcTenant = null;
			ICFBamTableObj refForTable = null;
			ICFBamTableObj refReturnTable = null;
			// Attribute Extraction
			String	attrLocalName;
			int		numAttrs;
			int		idxAttr;
			final String S_ProcName = "startElement";
			final String S_LocalName = "LocalName";

			assert qName.equals( "ServerObjFunc" );

			CFBamXmlLoader saxLoader = (CFBamXmlLoader)getParser();
			if( saxLoader == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"getParser()" );
			}

			ICFBamSchemaObj schemaObj = saxLoader.getSchemaObj();
			if( schemaObj == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"getParser().getSchemaObj()" );
			}

			// Instantiate an edit buffer for the parsed information
			ICFBamServerObjFuncEditObj editBuff = (ICFBamServerObjFuncEditObj)schemaObj.getServerObjFuncTableObj().newInstance().beginEdit();

			// Extract Attributes
			numAttrs = attrs.getLength();
			for( idxAttr = 0; idxAttr < numAttrs; idxAttr++ ) {
				attrLocalName = attrs.getLocalName( idxAttr );
				if( attrLocalName.equals( "Id" ) ) {
					if( attrId != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrId = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "Name" ) ) {
					if( attrName != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrName = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "ShortName" ) ) {
					if( attrShortName != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrShortName = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "Label" ) ) {
					if( attrLabel != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrLabel = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "ShortDescription" ) ) {
					if( attrShortDescription != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrShortDescription = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "Description" ) ) {
					if( attrDescription != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrDescription = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "Suffix" ) ) {
					if( attrSuffix != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrSuffix = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "IsInstanceMethod" ) ) {
					if( attrIsInstanceMethod != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrIsInstanceMethod = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "IsServerOnly" ) ) {
					if( attrIsServerOnly != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrIsServerOnly = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "ReturnTable" ) ) {
					if( attrReturnTable != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrReturnTable = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "schemaLocation" ) ) {
					// ignored
				}
				else {
					throw new CFLibUnrecognizedAttributeException( getClass(),
						S_ProcName,
						getParser().getLocationInfo(),
						attrLocalName );
				}
			}

			// Ensure that required attributes have values
			if( attrName == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"Name" );
			}
			if( ( attrIsInstanceMethod == null ) || ( attrIsInstanceMethod.length() <= 0 ) ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"IsInstanceMethod" );
			}
			if( ( attrReturnTable == null ) || ( attrReturnTable.length() <= 0 ) ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"ReturnTable" );
			}

			// Save named attributes to context
			CFLibXmlCoreContext curContext = getParser().getCurContext();

			// Convert string attributes to native Cafe types
			// and apply the converted attributes to the editBuff.

			Integer natId;
			if( ( attrId != null ) && ( attrId.length() > 0 ) ) {
				natId = Integer.valueOf( Integer.parseInt( attrId ) );
			}
			else {
				natId = null;
			}
			String natName = attrName;
			editBuff.setRequiredName( natName );

			String natShortName = attrShortName;
			editBuff.setOptionalShortName( natShortName );

			String natLabel = attrLabel;
			editBuff.setOptionalLabel( natLabel );

			String natShortDescription = attrShortDescription;
			editBuff.setOptionalShortDescription( natShortDescription );

			String natDescription = attrDescription;
			editBuff.setOptionalDescription( natDescription );

			String natSuffix = attrSuffix;
			editBuff.setOptionalSuffix( natSuffix );

			boolean natIsInstanceMethod;
			if( attrIsInstanceMethod.equals( "true" ) || attrIsInstanceMethod.equals( "yes" ) || attrIsInstanceMethod.equals( "1" ) ) {
				natIsInstanceMethod = true;
			}
			else if( attrIsInstanceMethod.equals( "false" ) || attrIsInstanceMethod.equals( "no" ) || attrIsInstanceMethod.equals( "0" ) ) {
				natIsInstanceMethod = false;
			}
			else {
				throw new CFLibUsageException( getClass(),
					S_ProcName,
					"Unexpected IsInstanceMethod value, must be one of true, false, yes, no, 1, or 0, not \"" + attrIsInstanceMethod + "\"" );
			}
			editBuff.setRequiredIsInstanceMethod( natIsInstanceMethod );

			boolean natIsServerOnly;
			if( ( attrIsServerOnly == null ) || ( attrIsServerOnly.length() <= 0 ) ) {
				natIsServerOnly = false;
			}
			else if( attrIsServerOnly.equals( "true" ) || attrIsServerOnly.equals( "yes" ) || attrIsServerOnly.equals( "1" ) ) {
				natIsServerOnly = true;
			}
			else if( attrIsServerOnly.equals( "false" ) || attrIsServerOnly.equals( "no" ) || attrIsServerOnly.equals( "0" ) ) {
				natIsServerOnly = false;
			}
			else {
				throw new CFLibUsageException( getClass(),
					S_ProcName,
					"Unexpected IsServerOnly value, must be one of true, false, yes, no, 1, or 0, not \"" + attrIsServerOnly + "\"" );
			}
			editBuff.setRequiredIsServerOnly( natIsServerOnly );

			// Get the scope/container object

			CFLibXmlCoreContext parentContext = curContext.getPrevContext();
			Object scopeObj;
			if( parentContext != null ) {
				scopeObj = parentContext.getNamedValue( "Object" );
			}
			else {
				scopeObj = null;
			}

			// Resolve and apply required Container reference

			if( scopeObj == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"scopeObj" );
			}
			else if( scopeObj instanceof ICFBamTableObj ) {
				refForTable = (ICFBamTableObj) scopeObj;
				editBuff.setRequiredContainerForTable( refForTable );
				refSrvProcTenant = (ICFBamTenantObj)editBuff.getRequiredOwnerTenant();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"scopeObj",
					scopeObj,
					"ICFBamTableObj" );
			}

			// Resolve and apply Owner reference

			if( refSrvProcTenant == null ) {
				if( scopeObj instanceof ICFBamTenantObj ) {
					refSrvProcTenant = (ICFBamTenantObj) scopeObj;
					editBuff.setRequiredOwnerTenant( refSrvProcTenant );
				}
				else {
					throw new CFLibNullArgumentException( getClass(),
						S_ProcName,
						0,
						"Owner<SrvProcTenant>" );
				}
			}

			refTenant = refSrvProcTenant;
			editBuff.setOptionalLookupDefSchema( null );

			// Lookup refReturnTable by qualified name
			if( ( attrReturnTable != null ) && ( attrReturnTable.length() > 0 ) ) {
				refReturnTable = (ICFBamTableObj)(editBuff.getNamedObject( schemaObj.getTableTableObj().getObjQualifyingClass(),
					attrReturnTable ) );
				if( refReturnTable == null ) {
					throw new CFLibNullArgumentException( getClass(),
						S_ProcName,
						0,
						"Resolve ReturnTable reference qualified name \"" + attrReturnTable + "\" to table Table" );
				}
			}
			else {
				refReturnTable = null;
			}
			editBuff.setOptionalLookupRetTable( refReturnTable );

			ICFBamServerObjFuncObj origServerObjFunc = (ICFBamServerObjFuncObj)editBuff.create();
			editBuff = null;

			curContext.putNamedValue( "Object", origServerObjFunc );
		}
		catch( RuntimeException e ) {
			throw new RuntimeException( "Near " + getParser().getLocationInfo() + ": Caught and rethrew " + e.getClass().getSimpleName() + " - " + e.getMessage(),
				e );
		}
		catch( Error e ) {
			throw new Error( "Near " + getParser().getLocationInfo() + ": Caught and rethrew " + e.getClass().getSimpleName() + " - " + e.getMessage(),
				e );
		}
	}

	public void endElement(
		String		uri,
		String		localName,
		String		qName )
	throws SAXException
	{
	}
}
