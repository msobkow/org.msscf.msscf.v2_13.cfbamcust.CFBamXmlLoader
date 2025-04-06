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
 *	CFBamXmlLoaderSchemaDefParse XML SAX Element Handler implementation
 *	for SchemaDef.
 */
public class CFBamXmlLoaderSchemaDefHandler
	extends CFLibXmlCoreElementHandler
{
	public CFBamXmlLoaderSchemaDefHandler( CFBamXmlLoader saxLoader ) {
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
			// SchemaDef Attributes
			String	attrName = null;
			String	attrDbName = null;
			String	attrShortName = null;
			String	attrLabel = null;
			String	attrShortDescription = null;
			String	attrDescription = null;
			String	attrCopyrightPeriod = null;
			String	attrCopyrightHolder = null;
			String	attrAuthorEMail = null;
			String	attrProjectURL = null;
			String	attrPublishURI = null;
			String	attrCommonLicenseCode = null;
			String	attrCommonLicenseName = null;
			String	attrClientImplLicenseCode = null;
			String	attrClientImplLicenseName = null;
			String	attrClientXFaceLicenseCode = null;
			String	attrClientXFaceLicenseName = null;
			String	attrServerImplLicenseCode = null;
			String	attrServerImplLicenseName = null;
			String	attrServerXFaceLicenseCode = null;
			String	attrServerXFaceLicenseName = null;
			// SchemaDef References
			ICFBamMinorVersionObj refMinorVersion = null;
			ICFBamTenantObj refCTenant = null;
			// Attribute Extraction
			String	attrLocalName;
			int		numAttrs;
			int		idxAttr;
			final String S_ProcName = "startElement";
			final String S_LocalName = "LocalName";

			assert qName.equals( "SchemaDef" );

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
			ICFBamSchemaDefEditObj editBuff = (ICFBamSchemaDefEditObj)schemaObj.getSchemaDefTableObj().newInstance().beginEdit();

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
				else if( attrLocalName.equals( "CopyrightPeriod" ) ) {
					if( attrCopyrightPeriod != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrCopyrightPeriod = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "CopyrightHolder" ) ) {
					if( attrCopyrightHolder != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrCopyrightHolder = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "AuthorEMail" ) ) {
					if( attrAuthorEMail != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrAuthorEMail = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "ProjectURL" ) ) {
					if( attrProjectURL != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrProjectURL = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "PublishURI" ) ) {
					if( attrPublishURI != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrPublishURI = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "CommonLicenseCode" ) ) {
					if( attrCommonLicenseCode != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrCommonLicenseCode = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "CommonLicenseName" ) ) {
					if( attrCommonLicenseName != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrCommonLicenseName = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "ClientImplLicenseCode" ) ) {
					if( attrClientImplLicenseCode != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrClientImplLicenseCode = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "ClientImplLicenseName" ) ) {
					if( attrClientImplLicenseName != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrClientImplLicenseName = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "ClientXFaceLicenseCode" ) ) {
					if( attrClientXFaceLicenseCode != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrClientXFaceLicenseCode = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "ClientXFaceLicenseName" ) ) {
					if( attrClientXFaceLicenseName != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrClientXFaceLicenseName = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "ServerImplLicenseCode" ) ) {
					if( attrServerImplLicenseCode != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrServerImplLicenseCode = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "ServerImplLicenseName" ) ) {
					if( attrServerImplLicenseName != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrServerImplLicenseName = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "ServerXFaceLicenseCode" ) ) {
					if( attrServerXFaceLicenseCode != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrServerXFaceLicenseCode = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "ServerXFaceLicenseName" ) ) {
					if( attrServerXFaceLicenseName != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrServerXFaceLicenseName = attrs.getValue( idxAttr );
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
			if( attrCopyrightPeriod == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"CopyrightPeriod" );
			}
			if( attrCopyrightHolder == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"CopyrightHolder" );
			}
			if( attrAuthorEMail == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"AuthorEMail" );
			}
			if( attrProjectURL == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"ProjectURL" );
			}
			if( attrPublishURI == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"PublishURI" );
			}
			if( attrCommonLicenseCode == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"CommonLicenseCode" );
			}
			if( attrCommonLicenseName == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"CommonLicenseName" );
			}

			// Save named attributes to context
			CFLibXmlCoreContext curContext = getParser().getCurContext();
			curContext.putNamedValue( "Id", attrId );
			curContext.putNamedValue( "Name", attrName );
			curContext.putNamedValue( "DbName", attrDbName );
			curContext.putNamedValue( "ShortName", attrShortName );
			curContext.putNamedValue( "Label", attrLabel );
			curContext.putNamedValue( "ShortDescription", attrShortDescription );
			curContext.putNamedValue( "Description", attrDescription );
			curContext.putNamedValue( "CopyrightPeriod", attrCopyrightPeriod );
			curContext.putNamedValue( "CopyrightHolder", attrCopyrightHolder );
			curContext.putNamedValue( "AuthorEMail", attrAuthorEMail );
			curContext.putNamedValue( "ProjectURL", attrProjectURL );
			curContext.putNamedValue( "PublishURI", attrPublishURI );
			curContext.putNamedValue( "CommonLicenseCode", attrCommonLicenseCode );
			curContext.putNamedValue( "CommonLicenseName", attrCommonLicenseName );
			curContext.putNamedValue( "ClientImplLicenseCode", attrClientImplLicenseCode );
			curContext.putNamedValue( "ClientImplLicenseName", attrClientImplLicenseName );
			curContext.putNamedValue( "ClientXFaceLicenseCode", attrClientXFaceLicenseCode );
			curContext.putNamedValue( "ClientXFaceLicenseName", attrClientXFaceLicenseName );
			curContext.putNamedValue( "ServerImplLicenseCode", attrServerImplLicenseCode );
			curContext.putNamedValue( "ServerImplLicenseName", attrServerImplLicenseName );
			curContext.putNamedValue( "ServerXFaceLicenseCode", attrServerXFaceLicenseCode );
			curContext.putNamedValue( "ServerXFaceLicenseName", attrServerXFaceLicenseName );

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

			String natCopyrightPeriod = attrCopyrightPeriod;
			editBuff.setRequiredCopyrightPeriod( natCopyrightPeriod );

			String natCopyrightHolder = attrCopyrightHolder;
			editBuff.setRequiredCopyrightHolder( natCopyrightHolder );

			String natAuthorEMail = attrAuthorEMail;
			editBuff.setRequiredAuthorEMail( natAuthorEMail );

			String natProjectURL = attrProjectURL;
			editBuff.setRequiredProjectURL( natProjectURL );

			String natPublishURI = attrPublishURI;
			editBuff.setRequiredPublishURI( natPublishURI );
			
			editBuff.setRequiredCommonLicenseCode( attrCommonLicenseCode );
			editBuff.setRequiredCommonLicenseName( attrCommonLicenseName );
			editBuff.setOptionalClientImplLicenseCode( attrClientImplLicenseCode );
			editBuff.setOptionalClientImplLicenseName( attrClientImplLicenseName );
			editBuff.setOptionalClientXFaceLicenseCode( attrClientXFaceLicenseCode );
			editBuff.setOptionalClientXFaceLicenseName( attrClientXFaceLicenseName );
			editBuff.setOptionalServerImplLicenseCode( attrServerImplLicenseCode );
			editBuff.setOptionalServerImplLicenseName( attrServerImplLicenseName );
			editBuff.setOptionalServerXFaceLicenseCode( attrServerXFaceLicenseCode );
			editBuff.setOptionalServerXFaceLicenseName( attrServerXFaceLicenseName );

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
			else if( scopeObj instanceof ICFBamMinorVersionObj ) {
				refMinorVersion = (ICFBamMinorVersionObj) scopeObj;
				editBuff.setRequiredContainerMinorVersion( refMinorVersion );
				refCTenant = (ICFBamTenantObj)editBuff.getRequiredOwnerCTenant();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"scopeObj",
					scopeObj,
					"ICFBamDomainObj" );
			}

			// Resolve and apply Owner reference

			if( refCTenant == null ) {
				if( scopeObj instanceof ICFBamTenantObj ) {
					refCTenant = (ICFBamTenantObj) scopeObj;
					editBuff.setRequiredOwnerCTenant( refCTenant );
				}
				else {
					throw new CFLibNullArgumentException( getClass(),
						S_ProcName,
						0,
						"Owner<CTenant>" );
				}
			}

			refTenant = refCTenant;

			ICFBamSchemaDefObj origSchemaDef = schemaObj.getSchemaDefTableObj().readSchemaDefByUNameIdx( refMinorVersion.getRequiredTenantId(),
				refMinorVersion.getRequiredId(),
				attrName );
			if( origSchemaDef == null ) {
				origSchemaDef = (ICFBamSchemaDefObj)editBuff.create();
				editBuff = null;
				curContext.putNamedValue( "ProcessSchema", "true" );
			}
			else {
				curContext.putNamedValue( "ProcessSchema", "false" );
			}

			curContext.putNamedValue( "Object", origSchemaDef );
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

		assert qName.equals( "SchemaDef" );

		CFLibXmlCoreContext curContext = getParser().getCurContext();
		if( ! CFBamXmlLoader.getProcessSchema( curContext ) ) {
			return;
		}

		CFLibXmlCoreContext parentContext = curContext.getPrevContext();
		ICFBamSchemaDefObj schemaDef = (ICFBamSchemaDefObj)( curContext.getNamedValue( "Object" ) );

		if( ( schemaDef.getRequiredCommonLicenseCode() == null )
			|| ( schemaDef.getRequiredCommonLicenseCode().length() <= 0 )
			|| ( schemaDef.getRequiredCommonLicenseName() == null )
			|| ( schemaDef.getRequiredCommonLicenseName().length() <= 0 )
			|| ( schemaDef.getRequiredCommonLicenseText() == null )
			|| ( schemaDef.getRequiredCommonLicenseText().length() <= 0 ) )
		{
			throw new CFLibUsageException( getClass(),
					S_ProcName,
					"SchemaDef \"" + schemaDef.getObjName() + "\" must specify all three common license attributes - CommonLicenseCode, CommonLicenseName, and the element CommonLicense" );
		}

		if( ( ( schemaDef.getOptionalClientImplLicenseCode() != null ) && ( schemaDef.getOptionalClientImplLicenseCode().length() > 0 ) )
			|| ( ( schemaDef.getOptionalClientImplLicenseName() != null ) && ( schemaDef.getOptionalClientImplLicenseName().length() > 0 ) )
			|| ( ( schemaDef.getOptionalClientImplLicenseText() != null ) && ( schemaDef.getOptionalClientImplLicenseText().length() > 0 ) ) )
		{
			if( ( schemaDef.getOptionalClientImplLicenseCode() == null )
				|| ( schemaDef.getOptionalClientImplLicenseCode().length() <= 0 )
				|| ( schemaDef.getOptionalClientImplLicenseName() == null )
				|| ( schemaDef.getOptionalClientImplLicenseName().length() <= 0 )
				|| ( schemaDef.getOptionalClientImplLicenseText() == null )
				|| ( schemaDef.getOptionalClientImplLicenseText().length() <= 0 ) )
			{
				throw new CFLibUsageException( getClass(),
						S_ProcName,
						"SchemaDef \"" + schemaDef.getObjName() + "\" must specify all three ClientImpl license attributes or none of them - ClientImplLicenseCode, ClientImplLicenseName, and the element ClientImplLicense" );
			}
		}
		
		if( ( ( schemaDef.getOptionalClientXFaceLicenseCode() != null ) && ( schemaDef.getOptionalClientXFaceLicenseCode().length() > 0 ) )
			|| ( ( schemaDef.getOptionalClientXFaceLicenseName() != null ) && ( schemaDef.getOptionalClientXFaceLicenseName().length() > 0 ) )
			|| ( ( schemaDef.getOptionalClientXFaceLicenseText() != null ) && ( schemaDef.getOptionalClientXFaceLicenseText().length() > 0 ) ) )
		{
			if( ( schemaDef.getOptionalClientXFaceLicenseCode() == null )
				|| ( schemaDef.getOptionalClientXFaceLicenseCode().length() <= 0 )
				|| ( schemaDef.getOptionalClientXFaceLicenseName() == null )
				|| ( schemaDef.getOptionalClientXFaceLicenseName().length() <= 0 )
				|| ( schemaDef.getOptionalClientXFaceLicenseText() == null )
				|| ( schemaDef.getOptionalClientXFaceLicenseText().length() <= 0 ) )
			{
				throw new CFLibUsageException( getClass(),
						S_ProcName,
						"SchemaDef \"" + schemaDef.getObjName() + "\" must specify all three ClientXFace license attributes or none of them - ClientXFaceLicenseCode, ClientXFaceLicenseName, and the element ClientXFaceLicense" );
			}
		}

		if( ( ( schemaDef.getOptionalServerImplLicenseCode() != null ) && ( schemaDef.getOptionalServerImplLicenseCode().length() > 0 ) )
			|| ( ( schemaDef.getOptionalServerImplLicenseName() != null ) && ( schemaDef.getOptionalServerImplLicenseName().length() > 0 ) )
			|| ( ( schemaDef.getOptionalServerImplLicenseText() != null ) && ( schemaDef.getOptionalServerImplLicenseText().length() > 0 ) ) )
		{
			if( ( schemaDef.getOptionalServerImplLicenseCode() == null )
				|| ( schemaDef.getOptionalServerImplLicenseCode().length() <= 0 )
				|| ( schemaDef.getOptionalServerImplLicenseName() == null )
				|| ( schemaDef.getOptionalServerImplLicenseName().length() <= 0 )
				|| ( schemaDef.getOptionalServerImplLicenseText() == null )
				|| ( schemaDef.getOptionalServerImplLicenseText().length() <= 0 ) )
			{
				throw new CFLibUsageException( getClass(),
						S_ProcName,
						"SchemaDef \"" + schemaDef.getObjName() + "\" must specify all three ServerImpl license attributes or none of them - ServerImplLicenseCode, ServerImplLicenseName, and the element ServerImplLicense" );
			}
		}
		
		if( ( ( schemaDef.getOptionalServerXFaceLicenseCode() != null ) && ( schemaDef.getOptionalServerXFaceLicenseCode().length() > 0 ) )
			|| ( ( schemaDef.getOptionalServerXFaceLicenseName() != null ) && ( schemaDef.getOptionalServerXFaceLicenseName().length() > 0 ) )
			|| ( ( schemaDef.getOptionalServerXFaceLicenseText() != null ) && ( schemaDef.getOptionalServerXFaceLicenseText().length() > 0 ) ) )
		{
			if( ( schemaDef.getOptionalServerXFaceLicenseCode() == null )
				|| ( schemaDef.getOptionalServerXFaceLicenseCode().length() <= 0 )
				|| ( schemaDef.getOptionalServerXFaceLicenseName() == null )
				|| ( schemaDef.getOptionalServerXFaceLicenseName().length() <= 0 )
				|| ( schemaDef.getOptionalServerXFaceLicenseText() == null )
				|| ( schemaDef.getOptionalServerXFaceLicenseText().length() <= 0 ) )
			{
				throw new CFLibUsageException( getClass(),
						S_ProcName,
						"SchemaDef \"" + schemaDef.getObjName() + "\" must specify all three ServerXFace license attributes or none of them - ServerXFaceLicenseCode, ServerXFaceLicenseName, and the element ServerXFaceLicense" );
			}
		}
	}
}
