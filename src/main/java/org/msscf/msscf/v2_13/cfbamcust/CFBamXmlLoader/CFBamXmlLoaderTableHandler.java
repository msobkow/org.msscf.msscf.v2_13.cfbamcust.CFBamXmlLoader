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
import org.msscf.msscf.v2_13.cfbam.CFBam.*;
import org.msscf.msscf.v2_13.cfbam.CFBamObj.*;

/*
 *	CFBamXmlLoaderTableParse XML SAX Element Handler implementation
 *	for Table.
 */
public class CFBamXmlLoaderTableHandler
	extends CFLibXmlCoreElementHandler
{
	public CFBamXmlLoaderTableHandler( CFBamXmlLoader saxLoader ) {
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
			// Table Attributes
			String	attrName = null;
			String	attrDbName = null;
			String	attrShortName = null;
			String	attrLabel = null;
			String	attrShortDescription = null;
			String	attrDescription = null;
			String	attrSchemaId = null;
			String	attrTableClassCode = null;
			String	attrPageData = null;
			String	attrIsInstantiable = null;
			String	attrHasHistory = null;
			String	attrHasAuditColumns = null;
			String	attrLookupIndex = null;
			String	attrAltIndex = null;
			String	attrQualTable = null;
			String	attrLoaderBehaviour = null;
			String	attrSecScope = null;
			String	attrIsMutable = null;
			String	attrIsServerOnly = null;
			// Table References
			ICFBamSchemaDefObj refSchemaDef = null;
			ICFBamIndexObj refLookupIndex = null;
			ICFBamIndexObj refAltIndex = null;
			ICFBamTableObj refQualTable = null;
			ICFBamSchema.LoaderBehaviourEnum refLoaderBehaviour = null;
			ICFBamSchema.SecScopeEnum refSecScope = null;
			// Attribute Extraction
			String	attrLocalName;
			int		numAttrs;
			int		idxAttr;
			final String S_ProcName = "startElement";
			final String S_LocalName = "LocalName";

			assert qName.equals( "Table" );

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
			ICFBamTableEditObj editBuff = (ICFBamTableEditObj)schemaObj.getTableTableObj().newInstance().beginEdit();

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
				else if( attrLocalName.equals( "DbName" ) ) {
					if( attrDbName != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrDbName = attrs.getValue( idxAttr );
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
				else if( attrLocalName.equals( "SchemaId" ) ) {
					if( attrSchemaId != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrSchemaId = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "TableClassCode" ) ) {
					if( attrTableClassCode != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrTableClassCode = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "PageData" ) ) {
					if( attrPageData != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrPageData = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "IsInstantiable" ) ) {
					if( attrIsInstantiable != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrIsInstantiable = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "HasHistory" ) ) {
					if( attrHasHistory != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrHasHistory = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "HasAuditColumns" ) ) {
					if( attrHasAuditColumns != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrHasAuditColumns = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "LookupIndex" ) ) {
					if( attrLookupIndex != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrLookupIndex = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "AltIndex" ) ) {
					if( attrAltIndex != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrAltIndex = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "QualTable" ) ) {
					if( attrQualTable != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrQualTable = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "LoaderBehaviour" ) ) {
					if( attrLoaderBehaviour != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrLoaderBehaviour = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "SecScope" ) ) {
					if( attrSecScope != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrSecScope = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "IsMutable" ) ) {
					if( attrIsMutable != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrIsMutable = attrs.getValue( idxAttr );
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
			if( attrTableClassCode == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"TableClassCode" );
			}
			if( ( attrIsInstantiable == null ) || ( attrIsInstantiable.length() <= 0 ) ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"IsInstantiable" );
			}
			if( ( attrHasHistory == null ) || ( attrHasHistory.length() <= 0 ) ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"HasHistory" );
			}
			if( ( attrHasAuditColumns == null ) || ( attrHasAuditColumns.length() <= 0 ) ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"HasAuditColumns" );
			}
			if( ( attrLoaderBehaviour == null ) || ( attrLoaderBehaviour.length() <= 0 ) ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"LoaderBehaviour" );
			}
			if( ( attrSecScope == null ) || ( attrSecScope.length() <= 0 ) ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"SecScope" );
			}

			// Save named attributes to context
			CFLibXmlCoreContext curContext = getParser().getCurContext();
			if( ! CFBamXmlLoader.getProcessSchema( curContext ) ) {
				return;
			}

			curContext.putNamedValue( "Id", attrId );
			curContext.putNamedValue( "Name", attrName );
			curContext.putNamedValue( "DbName", attrDbName );
			curContext.putNamedValue( "ShortName", attrShortName );
			curContext.putNamedValue( "Label", attrLabel );
			curContext.putNamedValue( "ShortDescription", attrShortDescription );
			curContext.putNamedValue( "Description", attrDescription );
			curContext.putNamedValue( "SchemaId", attrSchemaId );
			curContext.putNamedValue( "TableClassCode", attrTableClassCode );
			curContext.putNamedValue( "PageData", attrPageData );
			curContext.putNamedValue( "IsInstantiable", attrIsInstantiable );
			curContext.putNamedValue( "HasHistory", attrHasHistory );
			curContext.putNamedValue( "HasAuditColumns", attrHasAuditColumns );
			curContext.putNamedValue( "LookupIndex", attrLookupIndex );
			curContext.putNamedValue( "AltIndex", attrAltIndex );
			curContext.putNamedValue( "QualTable", attrQualTable );
			curContext.putNamedValue( "LoaderBehaviour", attrLoaderBehaviour );
			curContext.putNamedValue( "SecScope", attrSecScope );
			curContext.putNamedValue( "IsMutable", attrIsMutable );
			curContext.putNamedValue( "IsServerOnly", attrIsServerOnly );

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

			String natDbName = attrDbName;
			editBuff.setOptionalDbName( natDbName );

			String natShortName = attrShortName;
			editBuff.setOptionalShortName( natShortName );

			String natLabel = attrLabel;
			editBuff.setOptionalLabel( natLabel );

			String natShortDescription = attrShortDescription;
			editBuff.setOptionalShortDescription( natShortDescription );

			String natDescription = attrDescription;
			editBuff.setOptionalDescription( natDescription );

			String natTableClassCode = attrTableClassCode;
			editBuff.setRequiredTableClassCode( natTableClassCode );

			boolean natPageData;
			if( attrPageData == null ) {
				natPageData = false;
			}
			else if( attrPageData.equals( "true" ) || attrPageData.equals( "yes" ) || attrPageData.equals( "1" ) ) {
				natPageData = true;
			}
			else if( attrPageData.equals( "false" ) || attrPageData.equals( "no" ) || attrPageData.equals( "0" ) ) {
				natPageData = false;
			}
			else {
				throw new CFLibUsageException( getClass(),
					S_ProcName,
					"Unexpected PageData value, must be one of true, false, yes, no, 1, or 0, not \"" + attrPageData + "\"" );
			}
			editBuff.setRequiredPageData( natPageData );

			boolean natIsInstantiable;
			if( attrIsInstantiable.equals( "true" ) || attrIsInstantiable.equals( "yes" ) || attrIsInstantiable.equals( "1" ) ) {
				natIsInstantiable = true;
			}
			else if( attrIsInstantiable.equals( "false" ) || attrIsInstantiable.equals( "no" ) || attrIsInstantiable.equals( "0" ) ) {
				natIsInstantiable = false;
			}
			else {
				throw new CFLibUsageException( getClass(),
					S_ProcName,
					"Unexpected IsInstantiable value, must be one of true, false, yes, no, 1, or 0, not \"" + attrIsInstantiable + "\"" );
			}
			editBuff.setRequiredIsInstantiable( natIsInstantiable );

			boolean natHasHistory;
			if( attrHasHistory.equals( "true" ) || attrHasHistory.equals( "yes" ) || attrHasHistory.equals( "1" ) ) {
				natHasHistory = true;
			}
			else if( attrHasHistory.equals( "false" ) || attrHasHistory.equals( "no" ) || attrHasHistory.equals( "0" ) ) {
				natHasHistory = false;
			}
			else {
				throw new CFLibUsageException( getClass(),
					S_ProcName,
					"Unexpected HasHistory value, must be one of true, false, yes, no, 1, or 0, not \"" + attrHasHistory + "\"" );
			}
			editBuff.setRequiredHasHistory( natHasHistory );

			boolean natHasAuditColumns;
			if( attrHasAuditColumns.equals( "true" ) || attrHasAuditColumns.equals( "yes" ) || attrHasAuditColumns.equals( "1" ) ) {
				natHasAuditColumns = true;
			}
			else if( attrHasAuditColumns.equals( "false" ) || attrHasAuditColumns.equals( "no" ) || attrHasAuditColumns.equals( "0" ) ) {
				natHasAuditColumns = false;
			}
			else {
				throw new CFLibUsageException( getClass(),
					S_ProcName,
					"Unexpected HasAuditColumns value, must be one of true, false, yes, no, 1, or 0, not \"" + attrHasAuditColumns + "\"" );
			}
			editBuff.setRequiredHasAuditColumns( natHasAuditColumns );

			boolean natIsMutable;
			if( ( attrIsMutable == null ) || ( attrIsMutable.length() <= 0 ) ) {
				natIsMutable = false;
			}
			else if( attrIsMutable.equals( "true" ) || attrIsMutable.equals( "yes" ) || attrIsMutable.equals( "1" ) ) {
				natIsMutable = true;
			}
			else if( attrIsMutable.equals( "false" ) || attrIsMutable.equals( "no" ) || attrIsMutable.equals( "0" ) ) {
				natIsMutable = false;
			}
			else {
				throw new CFLibUsageException( getClass(),
					S_ProcName,
					"Unexpected IsMutable value, must be one of true, false, yes, no, 1, or 0, not \"" + attrPageData + "\"" );
			}
			editBuff.setRequiredIsMutable( natIsMutable );

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
					"Unexpected IsServerOnly value, must be one of true, false, yes, no, 1, or 0, not \"" + attrPageData + "\"" );
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
			else if( scopeObj instanceof ICFBamSchemaDefObj ) {
				refSchemaDef = (ICFBamSchemaDefObj) scopeObj;
				editBuff.setRequiredContainerSchemaDef( refSchemaDef );
				refTenant = (ICFBamTenantObj)editBuff.getRequiredOwnerTenant();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"scopeObj",
					scopeObj,
					"ICFBamSchemaDefObj" );
			}

			// Resolve and apply Owner reference

			if( refTenant == null ) {
				if( scopeObj instanceof ICFBamTenantObj ) {
					refTenant = (ICFBamTenantObj) scopeObj;
					editBuff.setRequiredOwnerTenant( refTenant );
				}
				else {
					throw new CFLibNullArgumentException( getClass(),
						S_ProcName,
						0,
						"Owner<Tenant>" );
				}
			}

			// Lookup refLookupIndex by qualified name
			refLookupIndex = null;
			editBuff.setOptionalLookupLookupIndex( refLookupIndex );

			// Lookup refAltIndex by qualified name
			refAltIndex = null;
			editBuff.setOptionalLookupAltIndex( refAltIndex );

			// Lookup refQualTable by qualified name
			if( ( attrQualTable != null ) && ( attrQualTable.length() > 0 ) ) {
				refQualTable = (ICFBamTableObj)(editBuff.getNamedObject( schemaObj.getTableTableObj().getObjQualifyingClass(),
					attrQualTable ) );
				if( refQualTable == null ) {
					throw new CFLibNullArgumentException( getClass(),
						S_ProcName,
						0,
						"Resolve QualTable reference qualified name \"" + attrQualTable + "\" to table Table" );
				}
			}
			else {
				refQualTable = null;
			}
			editBuff.setOptionalLookupQualTable( refQualTable );

			// Lookup refLoaderBehaviour by key name value attr
			if( ( attrLoaderBehaviour != null ) && ( attrLoaderBehaviour.length() > 0 ) ) {
				refLoaderBehaviour = CFBamSchema.parseLoaderBehaviourEnum( attrLoaderBehaviour );
				if( refLoaderBehaviour == null ) {
					throw new CFLibNullArgumentException( getClass(),
						S_ProcName,
						0,
						"Resolve LoaderBehaviour reference named \"" + attrLoaderBehaviour + "\" to table LoaderBehaviour" );
				}
			}
			else {
				refLoaderBehaviour = null;
			}
			editBuff.setRequiredLoaderBehaviour( refLoaderBehaviour );

			// Lookup refSecScope by key name value attr
			if( ( attrSecScope != null ) && ( attrSecScope.length() > 0 ) ) {
				refSecScope = CFBamSchema.parseSecScopeEnum( attrSecScope );
				if( refSecScope == null ) {
					throw new CFLibNullArgumentException( getClass(),
						S_ProcName,
						0,
						"Resolve SecScope reference named \"" + attrSecScope + "\" to table SecScope" );
				}
			}
			else {
				refSecScope = null;
			}
			editBuff.setRequiredSecScope( refSecScope );

			ICFBamTableEditObj editTable = editBuff;
			ICFBamTableObj origTable = (ICFBamTableObj)editTable.create();
			editTable = null;

			origTable.getSchema().getId16GenTableObj().readId16GenByDispIdx( origTable.getRequiredTenantId(),
					origTable.getRequiredId() );
			origTable.getSchema().getId32GenTableObj().readId32GenByDispIdx( origTable.getRequiredTenantId(),
					origTable.getRequiredId() );
			origTable.getSchema().getId64GenTableObj().readId64GenByDispIdx( origTable.getRequiredTenantId(),
					origTable.getRequiredId() );

			curContext.putNamedValue( "Object", origTable );
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
		final String S_ProcName = "endElement";

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

		CFLibXmlCoreContext curContext = getParser().getCurContext();
		if( ! CFBamXmlLoader.getProcessSchema( curContext ) ) {
			return;
		}

		ICFBamTableObj origBuff = (ICFBamTableObj)curContext.getNamedValue( "Object" );
		ICFBamTableEditObj editBuff = (ICFBamTableEditObj)origBuff.beginEdit();

		String attrLookupIndex = (String)curContext.getNamedValue( "LookupIndex" );
		ICFBamIndexObj refLookupIndex;
		// Lookup refLookupIndex by qualified name
		if( ( attrLookupIndex != null ) && ( attrLookupIndex.length() > 0 ) ) {
			refLookupIndex = (ICFBamIndexObj)(editBuff.getNamedObject( attrLookupIndex ) );
			if( refLookupIndex == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"Resolve LookupIndex reference qualified name \"" + attrLookupIndex + "\" to table Index" );
			}
		}
		else {
			refLookupIndex = null;
		}
		editBuff.setOptionalLookupLookupIndex( refLookupIndex );

		String attrAltIndex = (String)curContext.getNamedValue( "AltIndex" );
		ICFBamIndexObj refAltIndex;
		if( ( attrAltIndex != null ) && ( attrAltIndex.length() > 0 ) ) {
			refAltIndex = (ICFBamIndexObj)(editBuff.getNamedObject( attrAltIndex ) );
			if( refAltIndex == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"Resolve AltIndex reference qualified name \"" + attrAltIndex + "\" to table Index" );
			}
		}
		else {
			refAltIndex = null;
		}
		editBuff.setOptionalLookupAltIndex( refAltIndex );

		editBuff.update();
		editBuff = null;
	}
}
