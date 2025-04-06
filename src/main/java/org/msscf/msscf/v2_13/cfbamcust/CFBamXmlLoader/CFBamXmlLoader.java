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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

import javax.xml.XMLConstants;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.msscf.msscf.v2_13.cflib.CFLib.*;

import org.apache.xerces.xni.grammars.Grammar;
import org.xml.sax.*;

import org.msscf.msscf.v2_13.cfbam.CFBamObj.*;
import org.msscf.msscf.v2_13.cfbamcust.MSSBamCF.MSSBamCFEngine;
import org.msscf.msscf.v2_13.cfcore.MssCF.MssCFSaxParser;

public class CFBamXmlLoader
	extends MssCFSaxParser
	implements ContentHandler
{

	// The namespace URI of the supported schema
	public final static String	SCHEMA_XMLNS = "uri://org.msscf/msscf/2.0.13/mssbam";

	// The source for loading the supported schema
	public final static String	SCHEMA_URI = "/opt/msscf/2.0.13/xsd/mssbam-2.13.xsd";
	public final static String	SCHEMA_ROOT_URI = "/xsd/mssbam-2.13.xsd";

	// The schema instance to load in to

	private ICFBamSchemaObj schemaObj = null;
	// Loader behaviour configuration attributes

	protected static Grammar myGrammar = null;

	// Constructors

	public CFBamXmlLoader( MSSBamCFEngine engine, ICFLibMessageLog jLogger ) {
		super( engine, jLogger );
		bamEngine = engine;
		setRootElementHandler( getSaxRootHandler() );
		if( myGrammar == null ) {
			InputStream input;
			File file = new File( SCHEMA_URI );
			if( file.exists() ) {
				try {
					input = new FileInputStream( file );
				}
				catch( Exception e ) {
					input = null;
				}
				if( input != null ) {
					myGrammar = addToGrammarPool( SCHEMA_URI, input );
				}
			}
			else {
				file = new File( SCHEMA_ROOT_URI );
				if( file.exists() ) {
					try {
						input = new FileInputStream( file );
					}
					catch( Exception e ) {
						input = null;
					}
					if( input != null ) {
						myGrammar = addToGrammarPool( SCHEMA_URI, input );
					}
				}
				else {
					input = getClass().getResourceAsStream( SCHEMA_ROOT_URI );
					if( input != null ) {
						myGrammar = addToGrammarPool( SCHEMA_ROOT_URI, input );
					}
				}
			}
		}
		initParser();
	}

	/**
	 *	The directories containing XML Models
	 */
	protected static ArrayList<String> modelPath = new ArrayList<String>();

	protected ICFBamTenantObj tenant = null;

	protected ICFLibAnyObj definedProjectVersion = null;

	protected MSSBamCFEngine bamEngine = null;

	public MSSBamCFEngine getBamEngine() {
		return( bamEngine );
	}

//	JVM Config: ModelPath

	/**
	 *	Add an XML repository model directory the model path.
	 */
	public static void addModelPath( String dirname ) {
		assert ( ( dirname != null ) && ( dirname.length() > 0 ) ) : "dirname (arg 1) is null or empty";
		Iterator<String> iter = modelPath.iterator();
		while( iter.hasNext() ) {
			String match = iter.next();
			if( dirname.equals( match ) ) {
				return;
			}
		}
		modelPath.add( new String( dirname ) );
	}

	/**
	 *	Get an iteration of the model path directories.
	 *
	 *	@return Iteration of Strings.
	 */
	public static Iterator<String> getModelPathIterator() {
		Iterator<String> iter = modelPath.iterator();
		return( iter );
	}

//	Results: Produced by a successful parse

	/**
	 *	Get the tenant that contains the parse.
	 *
	 *	@return	The parsed MSSBam
	 */
	public ICFBamTenantObj getTenant() {
		return( tenant );
	}

	/**
	 *	Set the tenant to receive the parse
	 */
	public void setTenant( ICFBamTenantObj value ) {
		tenant = value;
	}

	/**
	 *	Get the project version defined by the repository model.
	 *
	 *	@return	The ProjectVersion defined by the model.
	 */
	public ICFLibAnyObj getDefinedProjectVersion() {
		return( definedProjectVersion );
	}

//	Runtime: Load a MSSBam

	/**
	 *	Load a MSSBam.
	 *
	 *	@param	modelName	The name of the model to be loaded
	 */
	public ICFBamTenantObj loadTenant( String modelName ) {

//		Get the model path to be searched

		String modelNameDotXml;
		if( modelName.endsWith( ".xml" ) ) {
			modelNameDotXml = modelName;
		}
		else {
			modelNameDotXml = modelName + ".xml";
		}

		String tenantFileName = null;
		String tenantRootDir = null;
		InputStream tenantXml = null;

		Iterator<String> iterPath = getModelPathIterator();
		while( ( tenantXml == null ) && iterPath.hasNext() ) {

			String modelDir = iterPath.next();
			int modelDirLen = modelDir.length();
			assert modelDirLen > 0 : "ModelPath directory entry is empty";
			char modelDirTail = modelDir.charAt( modelDirLen - 1 );
			if( ( modelDirTail == '/' ) || ( modelDirTail ==  '\\' ) ) {
				tenantRootDir = modelDir;
			}
			else {
				tenantRootDir = modelDir + File.separator;
			}

			tenantFileName = tenantRootDir + modelNameDotXml;
			try {
				tenantXml = new FileInputStream( tenantFileName );
			}
			catch( FileNotFoundException e ) {
				tenantXml = null;
				tenantFileName = null;
				tenantRootDir = null;
			}
		}

//		Did we find the model?

		if( tenantXml == null ) {
			throw new CFLibRuntimeException( getClass(),
				"loadTenant",
				getLocationInfo() + " Could not locate Repository Model \"" + modelName + "\"" ); 
		}
		else {
			getBamEngine().getLog().message( "Loading " + modelName );
		}

//		Parse the model

		try {
			parse( tenantXml, tenantRootDir );
		}
		finally {
			try {
				tenantXml.close();
			}
			catch( Exception e ) {
				throw new CFLibRuntimeException( getClass(),
					"loadTenant",
					getLocationInfo() + " Could not close Business Application Model \"" + modelName + "\"", e ); 
			}
		}

		return( tenant );
	}

	public static boolean getProcessSchema( CFLibXmlCoreContext curContext ) {
		final String S_ProcName = "getProcessSchema";
		boolean retval = false;
		CFLibXmlCoreContext workContext = curContext;

		Object obj = null;
		while( ( obj == null ) && ( workContext != null ) ) {
			obj = workContext.getNamedValue( "ProcessSchema" );
			if( obj == null ) {
				workContext = workContext.getPrevContext();
			}
		}

		if( obj == null ) {
			retval = false;
		}
		else if( obj instanceof String ) {
			String strval = (String)obj;
			if( "true".equals( strval ) ) {
				retval = true;
			}
			else if( "false".equals( strval ) ) {
				retval = false;
			}
			else {
				throw new CFLibInvalidArgumentException( CFBamXmlLoader.class,
					S_ProcName,
					0,
					"ProcessSchema",
					"Invalid argument, must be one of true or false, not " + strval );
			}
		}
		else {
			new CFLibUnsupportedClassException( CFBamXmlLoader.class,
					S_ProcName,
					"ProcessSchema",
					obj,
					"String" );
		}

		return( retval );
	}

	// Element Handler instances

	private CFBamXmlLoaderCommonLicenseHandler commonLicenseHandler = null;
	private CFBamXmlLoaderClientImplLicenseHandler clientImplLicenseHandler = null;
	private CFBamXmlLoaderClientXFaceLicenseHandler clientXFaceLicenseHandler = null;
	private CFBamXmlLoaderServerImplLicenseHandler serverImplLicenseHandler = null;
	private CFBamXmlLoaderServerXFaceLicenseHandler serverXFaceLicenseHandler = null;
	private CFBamXmlLoaderBlobColHandler blobColHandler = null;
	private CFBamXmlLoaderBlobTypeHandler blobTypeHandler = null;
	private CFBamXmlLoaderBoolColHandler boolColHandler = null;
	private CFBamXmlLoaderBoolTypeHandler boolTypeHandler = null;
	private CFBamXmlLoaderChainHandler chainHandler = null;
	private CFBamXmlLoaderClearDepHandler clearDepHandler = null;
	private CFBamXmlLoaderDateColHandler dateColHandler = null;
	private CFBamXmlLoaderDateTypeHandler dateTypeHandler = null;
	private CFBamXmlLoaderDelDepHandler delDepHandler = null;
	private CFBamXmlLoaderDoubleColHandler doubleColHandler = null;
	private CFBamXmlLoaderDoubleTypeHandler doubleTypeHandler = null;
	private CFBamXmlLoaderEnumTagHandler enumTagHandler = null;
	private CFBamXmlLoaderEnumTypeHandler enumTypeHandler = null;
	private CFBamXmlLoaderFloatColHandler floatColHandler = null;
	private CFBamXmlLoaderFloatTypeHandler floatTypeHandler = null;
	private CFBamXmlLoaderId16GenHandler id16GenHandler = null;
	private CFBamXmlLoaderId32GenHandler id32GenHandler = null;
	private CFBamXmlLoaderId64GenHandler id64GenHandler = null;
	private CFBamXmlLoaderIndexHandler indexHandler = null;
	private CFBamXmlLoaderIndexColHandler indexColHandler = null;
	private CFBamXmlLoaderInt16ColHandler int16ColHandler = null;
	private CFBamXmlLoaderInt16TypeHandler int16TypeHandler = null;
	private CFBamXmlLoaderInt32ColHandler int32ColHandler = null;
	private CFBamXmlLoaderInt32TypeHandler int32TypeHandler = null;
	private CFBamXmlLoaderInt64ColHandler int64ColHandler = null;
	private CFBamXmlLoaderInt64TypeHandler int64TypeHandler = null;
	private CFBamXmlLoaderCafeMethodBodyHandler cafeMethodBodyHandler = null;
	private CFBamXmlLoaderCafeSchemaObjInterfaceHandler cafeSchemaObjInterfaceHandler = null;
	private CFBamXmlLoaderCafeSchemaObjMembersHandler cafeSchemaObjMembersHandler = null;
	private CFBamXmlLoaderCafeSchemaObjImportHandler cafeSchemaObjImportHandler = null;
	private CFBamXmlLoaderCafeSchemaObjImplementationHandler cafeSchemaObjImplementationHandler = null;
	private CFBamXmlLoaderCafeDb2LUWSchemaObjMembersHandler cafeDb2LUWSchemaObjMembersHandler = null;
	private CFBamXmlLoaderCafeDb2LUWSchemaObjImplHandler cafeDb2LUWSchemaObjImplHandler = null;
	private CFBamXmlLoaderCafeDb2LUWSchemaObjImportHandler cafeDb2LUWSchemaObjImportHandler = null;
	private CFBamXmlLoaderCafeMSSqlSchemaObjMembersHandler cafeMSSqlSchemaObjMembersHandler = null;
	private CFBamXmlLoaderCafeMSSqlSchemaObjImplHandler cafeMSSqlSchemaObjImplHandler = null;
	private CFBamXmlLoaderCafeMSSqlSchemaObjImportHandler cafeMSSqlSchemaObjImportHandler = null;
	private CFBamXmlLoaderCafeMySqlSchemaObjMembersHandler cafeMySqlSchemaObjMembersHandler = null;
	private CFBamXmlLoaderCafeMySqlSchemaObjImplHandler cafeMySqlSchemaObjImplHandler = null;
	private CFBamXmlLoaderCafeMySqlSchemaObjImportHandler cafeMySqlSchemaObjImportHandler = null;
	private CFBamXmlLoaderCafeOracleSchemaObjMembersHandler cafeOracleSchemaObjMembersHandler = null;
	private CFBamXmlLoaderCafeOracleSchemaObjImplHandler cafeOracleSchemaObjImplHandler = null;
	private CFBamXmlLoaderCafeOracleSchemaObjImportHandler cafeOracleSchemaObjImportHandler = null;
	private CFBamXmlLoaderCafePgSqlSchemaObjMembersHandler cafePgSqlSchemaObjMembersHandler = null;
	private CFBamXmlLoaderCafePgSqlSchemaObjImplHandler cafePgSqlSchemaObjImplHandler = null;
	private CFBamXmlLoaderCafePgSqlSchemaObjImportHandler cafePgSqlSchemaObjImportHandler = null;
	private CFBamXmlLoaderCafeRamSchemaObjMembersHandler cafeRamSchemaObjMembersHandler = null;
	private CFBamXmlLoaderCafeRamSchemaObjImplHandler cafeRamSchemaObjImplHandler = null;
	private CFBamXmlLoaderCafeRamSchemaObjImportHandler cafeRamSchemaObjImportHandler = null;
	private CFBamXmlLoaderCafeXMsgSchemaImportHandler cafeXMsgSchemaImportHandler = null;
	private CFBamXmlLoaderCafeXMsgSchemaFormattersHandler cafeXMsgSchemaFormattersHandler = null;
	private CFBamXmlLoaderCafeXMsgClientSchemaImportHandler cafeXMsgClientSchemaImportHandler = null;
	private CFBamXmlLoaderCafeXMsgClientSchemaBodyHandler cafeXMsgClientSchemaBodyHandler = null;
	private CFBamXmlLoaderCafeXMsgRqstSchemaBodyHandler cafeXMsgRqstSchemaBodyHandler = null;
	private CFBamXmlLoaderCafeXMsgRqstSchemaImportHandler cafeXMsgRqstSchemaImportHandler = null;
	private CFBamXmlLoaderCafeXMsgRqstSchemaWireParsersHandler cafeXMsgRqstSchemaWireParsersHandler = null;
	private CFBamXmlLoaderCafeXMsgRqstSchemaXsdElementListHandler cafeXMsgRqstSchemaXsdElementListHandler = null;
	private CFBamXmlLoaderCafeXMsgRqstSchemaXsdSpecHandler cafeXMsgRqstSchemaXsdSpecHandler = null;
	private CFBamXmlLoaderCafeXMsgRspnSchemaBodyHandler cafeXMsgRspnSchemaBodyHandler = null;
	private CFBamXmlLoaderCafeXMsgRspnSchemaImportHandler cafeXMsgRspnSchemaImportHandler = null;
	private CFBamXmlLoaderCafeXMsgRspnSchemaWireParsersHandler cafeXMsgRspnSchemaWireParsersHandler = null;
	private CFBamXmlLoaderCafeXMsgRspnSchemaXsdElementListHandler cafeXMsgRspnSchemaXsdElementListHandler = null;
	private CFBamXmlLoaderCafeXMsgRspnSchemaXsdSpecHandler cafeXMsgRspnSchemaXsdSpecHandler = null;
	private CFBamXmlLoaderCafeObjMembersHandler cafeObjMembersHandler = null;
	private CFBamXmlLoaderCafeObjInterfaceHandler cafeObjInterfaceHandler = null;
	private CFBamXmlLoaderCafeObjImplementationHandler cafeObjImplementationHandler = null;
	private CFBamXmlLoaderCafeEditObjMembersHandler cafeEditObjMembersHandler = null;
	private CFBamXmlLoaderCafeEditObjInterfaceHandler cafeEditObjInterfaceHandler = null;
	private CFBamXmlLoaderCafeEditObjImplementationHandler cafeEditObjImplementationHandler = null;
	private CFBamXmlLoaderCafeDb2LUWTableImportHandler cafeDb2LUWTableImportHandler = null;
	private CFBamXmlLoaderCafeMSSqlTableImportHandler cafeMSSqlTableImportHandler = null;
	private CFBamXmlLoaderCafeMySqlTableImportHandler cafeMySqlTableImportHandler = null;
	private CFBamXmlLoaderCafeOracleTableImportHandler cafeOracleTableImportHandler = null;
	private CFBamXmlLoaderCafePgSqlTableImportHandler cafePgSqlTableImportHandler = null;
	private CFBamXmlLoaderCafeRamTableImportHandler cafeRamTableImportHandler = null;
	private CFBamXmlLoaderCafeObjImportHandler cafeObjImportHandler = null;
	private CFBamXmlLoaderCafeEditObjImportHandler cafeEditObjImportHandler = null;
	private CFBamXmlLoaderCafeSaxLoaderImportHandler cafeSaxLoaderImportHandler = null;
	private CFBamXmlLoaderCafeTableImportHandler cafeTableImportHandler = null;
	private CFBamXmlLoaderCafeTableObjImportHandler cafeTableObjImportHandler = null;
	private CFBamXmlLoaderCafeTableMembersHandler cafeTableMembersHandler = null;
	private CFBamXmlLoaderCafeTableInterfaceHandler cafeTableInterfaceHandler = null;
	private CFBamXmlLoaderCafeTableImplementationHandler cafeTableImplementationHandler = null;
	private CFBamXmlLoaderCafeTableObjMembersHandler cafeTableObjMembersHandler = null;
	private CFBamXmlLoaderCafeTableObjInterfaceHandler cafeTableObjInterfaceHandler = null;
	private CFBamXmlLoaderCafeTableObjImplementationHandler cafeTableObjImplementationHandler = null;
	private CFBamXmlLoaderCafeDb2LUWTableMembersHandler cafeDb2LUWTableMembersHandler = null;
	private CFBamXmlLoaderCafeDb2LUWTableImplementationHandler cafeDb2LUWTableImplementationHandler = null;
	private CFBamXmlLoaderCafeMSSqlTableMembersHandler cafeMSSqlTableMembersHandler = null;
	private CFBamXmlLoaderCafeMSSqlTableImplementationHandler cafeMSSqlTableImplementationHandler = null;
	private CFBamXmlLoaderCafeMySqlTableMembersHandler cafeMySqlTableMembersHandler = null;
	private CFBamXmlLoaderCafeMySqlTableImplementationHandler cafeMySqlTableImplementationHandler = null;
	private CFBamXmlLoaderCafeOracleTableMembersHandler cafeOracleTableMembersHandler = null;
	private CFBamXmlLoaderCafeOracleTableImplementationHandler cafeOracleTableImplementationHandler = null;
	private CFBamXmlLoaderCafePgSqlTableMembersHandler cafePgSqlTableMembersHandler = null;
	private CFBamXmlLoaderCafePgSqlTableImplementationHandler cafePgSqlTableImplementationHandler = null;
	private CFBamXmlLoaderCafeRamTableMembersHandler cafeRamTableMembersHandler = null;
	private CFBamXmlLoaderCafeRamTableImplementationHandler cafeRamTableImplementationHandler = null;
	private CFBamXmlLoaderCafeSaxLoaderStartElementHandler cafeSaxLoaderStartElementHandler = null;
	private CFBamXmlLoaderCafeSaxLoaderEndElementHandler cafeSaxLoaderEndElementHandler = null;
	private CFBamXmlLoaderCafeXMsgTableImportHandler cafeXMsgTableImportHandler = null;
	private CFBamXmlLoaderCafeXMsgTableFormattersHandler cafeXMsgTableFormattersHandler = null;
	private CFBamXmlLoaderCafeXMsgRqstTableImportHandler cafeXMsgRqstTableImportHandler = null;
	private CFBamXmlLoaderCafeXMsgRspnTableImportHandler cafeXMsgRspnTableImportHandler = null;
	private CFBamXmlLoaderCafeXMsgClientTableImportHandler cafeXMsgClientTableImportHandler = null;
	private CFBamXmlLoaderCafeXMsgRqstTableBodyHandler cafeXMsgRqstTableBodyHandler = null;
	private CFBamXmlLoaderCafeXMsgRspnTableBodyHandler cafeXMsgRspnTableBodyHandler = null;
	private CFBamXmlLoaderCafeXMsgClientTableBodyHandler cafeXMsgClientTableBodyHandler = null;
	private CFBamXmlLoaderCPlusMethodBodyHandler cplusMethodBodyHandler = null;
	private CFBamXmlLoaderCPlusSchemaObjInterfaceHandler cplusSchemaObjInterfaceHandler = null;
	private CFBamXmlLoaderCPlusSchemaObjMembersHandler cplusSchemaObjMembersHandler = null;
	private CFBamXmlLoaderCPlusSchemaObjIncludeHandler cplusSchemaObjIncludeHandler = null;
	private CFBamXmlLoaderCPlusSchemaObjImplementationHandler cplusSchemaObjImplementationHandler = null;
	private CFBamXmlLoaderCPlusDb2LUWSchemaObjMembersHandler cplusDb2LUWSchemaObjMembersHandler = null;
	private CFBamXmlLoaderCPlusDb2LUWSchemaObjImplHandler cplusDb2LUWSchemaObjImplHandler = null;
	private CFBamXmlLoaderCPlusDb2LUWSchemaObjIncludeHandler cplusDb2LUWSchemaObjIncludeHandler = null;
	private CFBamXmlLoaderCPlusMSSqlSchemaObjMembersHandler cplusMSSqlSchemaObjMembersHandler = null;
	private CFBamXmlLoaderCPlusMSSqlSchemaObjImplHandler cplusMSSqlSchemaObjImplHandler = null;
	private CFBamXmlLoaderCPlusMSSqlSchemaObjIncludeHandler cplusMSSqlSchemaObjIncludeHandler = null;
	private CFBamXmlLoaderCPlusMySqlSchemaObjMembersHandler cplusMySqlSchemaObjMembersHandler = null;
	private CFBamXmlLoaderCPlusMySqlSchemaObjImplHandler cplusMySqlSchemaObjImplHandler = null;
	private CFBamXmlLoaderCPlusMySqlSchemaObjIncludeHandler cplusMySqlSchemaObjIncludeHandler = null;
	private CFBamXmlLoaderCPlusOracleSchemaObjMembersHandler cplusOracleSchemaObjMembersHandler = null;
	private CFBamXmlLoaderCPlusOracleSchemaObjImplHandler cplusOracleSchemaObjImplHandler = null;
	private CFBamXmlLoaderCPlusOracleSchemaObjIncludeHandler cplusOracleSchemaObjIncludeHandler = null;
	private CFBamXmlLoaderCPlusPgSqlSchemaObjMembersHandler cplusPgSqlSchemaObjMembersHandler = null;
	private CFBamXmlLoaderCPlusPgSqlSchemaObjImplHandler cplusPgSqlSchemaObjImplHandler = null;
	private CFBamXmlLoaderCPlusPgSqlSchemaObjIncludeHandler cplusPgSqlSchemaObjIncludeHandler = null;
	private CFBamXmlLoaderCPlusRamSchemaObjMembersHandler cplusRamSchemaObjMembersHandler = null;
	private CFBamXmlLoaderCPlusRamSchemaObjImplHandler cplusRamSchemaObjImplHandler = null;
	private CFBamXmlLoaderCPlusRamSchemaObjIncludeHandler cplusRamSchemaObjIncludeHandler = null;
	private CFBamXmlLoaderCPlusXMsgSchemaIncludeHandler cplusXMsgSchemaIncludeHandler = null;
	private CFBamXmlLoaderCPlusXMsgSchemaFormattersHandler cplusXMsgSchemaFormattersHandler = null;
	private CFBamXmlLoaderCPlusXMsgClientSchemaIncludeHandler cplusXMsgClientSchemaIncludeHandler = null;
	private CFBamXmlLoaderCPlusXMsgClientSchemaBodyHandler cplusXMsgClientSchemaBodyHandler = null;
	private CFBamXmlLoaderCPlusXMsgRqstSchemaBodyHandler cplusXMsgRqstSchemaBodyHandler = null;
	private CFBamXmlLoaderCPlusXMsgRqstSchemaIncludeHandler cplusXMsgRqstSchemaIncludeHandler = null;
	private CFBamXmlLoaderCPlusXMsgRqstSchemaWireParsersHandler cplusXMsgRqstSchemaWireParsersHandler = null;
	private CFBamXmlLoaderCPlusXMsgRqstSchemaXsdElementListHandler cplusXMsgRqstSchemaXsdElementListHandler = null;
	private CFBamXmlLoaderCPlusXMsgRqstSchemaXsdSpecHandler cplusXMsgRqstSchemaXsdSpecHandler = null;
	private CFBamXmlLoaderCPlusXMsgRspnSchemaBodyHandler cplusXMsgRspnSchemaBodyHandler = null;
	private CFBamXmlLoaderCPlusXMsgRspnSchemaIncludeHandler cplusXMsgRspnSchemaIncludeHandler = null;
	private CFBamXmlLoaderCPlusXMsgRspnSchemaWireParsersHandler cplusXMsgRspnSchemaWireParsersHandler = null;
	private CFBamXmlLoaderCPlusXMsgRspnSchemaXsdElementListHandler cplusXMsgRspnSchemaXsdElementListHandler = null;
	private CFBamXmlLoaderCPlusXMsgRspnSchemaXsdSpecHandler cplusXMsgRspnSchemaXsdSpecHandler = null;
	private CFBamXmlLoaderCPlusObjMembersHandler cplusObjMembersHandler = null;
	private CFBamXmlLoaderCPlusObjInterfaceHandler cplusObjInterfaceHandler = null;
	private CFBamXmlLoaderCPlusObjImplementationHandler cplusObjImplementationHandler = null;
	private CFBamXmlLoaderCPlusEditObjMembersHandler cplusEditObjMembersHandler = null;
	private CFBamXmlLoaderCPlusEditObjInterfaceHandler cplusEditObjInterfaceHandler = null;
	private CFBamXmlLoaderCPlusEditObjImplementationHandler cplusEditObjImplementationHandler = null;
	private CFBamXmlLoaderCPlusDb2LUWTableIncludeHandler cplusDb2LUWTableIncludeHandler = null;
	private CFBamXmlLoaderCPlusMSSqlTableIncludeHandler cplusMSSqlTableIncludeHandler = null;
	private CFBamXmlLoaderCPlusMySqlTableIncludeHandler cplusMySqlTableIncludeHandler = null;
	private CFBamXmlLoaderCPlusOracleTableIncludeHandler cplusOracleTableIncludeHandler = null;
	private CFBamXmlLoaderCPlusPgSqlTableIncludeHandler cplusPgSqlTableIncludeHandler = null;
	private CFBamXmlLoaderCPlusRamTableIncludeHandler cplusRamTableIncludeHandler = null;
	private CFBamXmlLoaderCPlusObjIncludeHandler cplusObjIncludeHandler = null;
	private CFBamXmlLoaderCPlusEditObjIncludeHandler cplusEditObjIncludeHandler = null;
	private CFBamXmlLoaderCPlusSaxLoaderIncludeHandler cplusSaxLoaderIncludeHandler = null;
	private CFBamXmlLoaderCPlusTableIncludeHandler cplusTableIncludeHandler = null;
	private CFBamXmlLoaderCPlusTableObjIncludeHandler cplusTableObjIncludeHandler = null;
	private CFBamXmlLoaderCPlusTableMembersHandler cplusTableMembersHandler = null;
	private CFBamXmlLoaderCPlusTableInterfaceHandler cplusTableInterfaceHandler = null;
	private CFBamXmlLoaderCPlusTableImplementationHandler cplusTableImplementationHandler = null;
	private CFBamXmlLoaderCPlusTableObjMembersHandler cplusTableObjMembersHandler = null;
	private CFBamXmlLoaderCPlusTableObjInterfaceHandler cplusTableObjInterfaceHandler = null;
	private CFBamXmlLoaderCPlusTableObjImplementationHandler cplusTableObjImplementationHandler = null;
	private CFBamXmlLoaderCPlusDb2LUWTableMembersHandler cplusDb2LUWTableMembersHandler = null;
	private CFBamXmlLoaderCPlusDb2LUWTableImplementationHandler cplusDb2LUWTableImplementationHandler = null;
	private CFBamXmlLoaderCPlusMSSqlTableMembersHandler cplusMSSqlTableMembersHandler = null;
	private CFBamXmlLoaderCPlusMSSqlTableImplementationHandler cplusMSSqlTableImplementationHandler = null;
	private CFBamXmlLoaderCPlusMySqlTableMembersHandler cplusMySqlTableMembersHandler = null;
	private CFBamXmlLoaderCPlusMySqlTableImplementationHandler cplusMySqlTableImplementationHandler = null;
	private CFBamXmlLoaderCPlusOracleTableMembersHandler cplusOracleTableMembersHandler = null;
	private CFBamXmlLoaderCPlusOracleTableImplementationHandler cplusOracleTableImplementationHandler = null;
	private CFBamXmlLoaderCPlusPgSqlTableMembersHandler cplusPgSqlTableMembersHandler = null;
	private CFBamXmlLoaderCPlusPgSqlTableImplementationHandler cplusPgSqlTableImplementationHandler = null;
	private CFBamXmlLoaderCPlusRamTableMembersHandler cplusRamTableMembersHandler = null;
	private CFBamXmlLoaderCPlusRamTableImplementationHandler cplusRamTableImplementationHandler = null;
	private CFBamXmlLoaderCPlusSaxLoaderStartElementHandler cplusSaxLoaderStartElementHandler = null;
	private CFBamXmlLoaderCPlusSaxLoaderEndElementHandler cplusSaxLoaderEndElementHandler = null;
	private CFBamXmlLoaderCPlusXMsgTableIncludeHandler cplusXMsgTableIncludeHandler = null;
	private CFBamXmlLoaderCPlusXMsgTableFormattersHandler cplusXMsgTableFormattersHandler = null;
	private CFBamXmlLoaderCPlusXMsgRqstTableIncludeHandler cplusXMsgRqstTableIncludeHandler = null;
	private CFBamXmlLoaderCPlusXMsgRspnTableIncludeHandler cplusXMsgRspnTableIncludeHandler = null;
	private CFBamXmlLoaderCPlusXMsgClientTableIncludeHandler cplusXMsgClientTableIncludeHandler = null;
	private CFBamXmlLoaderCPlusXMsgRqstTableBodyHandler cplusXMsgRqstTableBodyHandler = null;
	private CFBamXmlLoaderCPlusXMsgRspnTableBodyHandler cplusXMsgRspnTableBodyHandler = null;
	private CFBamXmlLoaderCPlusXMsgClientTableBodyHandler cplusXMsgClientTableBodyHandler = null;
	private CFBamXmlLoaderHPlusSchemaObjInterfaceHandler hplusSchemaObjInterfaceHandler = null;
	private CFBamXmlLoaderHPlusSchemaObjMembersHandler hplusSchemaObjMembersHandler = null;
	private CFBamXmlLoaderHPlusSchemaObjIncludeHandler hplusSchemaObjIncludeHandler = null;
	private CFBamXmlLoaderHPlusSchemaObjImplementationHandler hplusSchemaObjImplementationHandler = null;
	private CFBamXmlLoaderHPlusDb2LUWSchemaObjMembersHandler hplusDb2LUWSchemaObjMembersHandler = null;
	private CFBamXmlLoaderHPlusDb2LUWSchemaObjImplHandler hplusDb2LUWSchemaObjImplHandler = null;
	private CFBamXmlLoaderHPlusDb2LUWSchemaObjIncludeHandler hplusDb2LUWSchemaObjIncludeHandler = null;
	private CFBamXmlLoaderHPlusMSSqlSchemaObjMembersHandler hplusMSSqlSchemaObjMembersHandler = null;
	private CFBamXmlLoaderHPlusMSSqlSchemaObjImplHandler hplusMSSqlSchemaObjImplHandler = null;
	private CFBamXmlLoaderHPlusMSSqlSchemaObjIncludeHandler hplusMSSqlSchemaObjIncludeHandler = null;
	private CFBamXmlLoaderHPlusMySqlSchemaObjMembersHandler hplusMySqlSchemaObjMembersHandler = null;
	private CFBamXmlLoaderHPlusMySqlSchemaObjImplHandler hplusMySqlSchemaObjImplHandler = null;
	private CFBamXmlLoaderHPlusMySqlSchemaObjIncludeHandler hplusMySqlSchemaObjIncludeHandler = null;
	private CFBamXmlLoaderHPlusOracleSchemaObjMembersHandler hplusOracleSchemaObjMembersHandler = null;
	private CFBamXmlLoaderHPlusOracleSchemaObjImplHandler hplusOracleSchemaObjImplHandler = null;
	private CFBamXmlLoaderHPlusOracleSchemaObjIncludeHandler hplusOracleSchemaObjIncludeHandler = null;
	private CFBamXmlLoaderHPlusPgSqlSchemaObjMembersHandler hplusPgSqlSchemaObjMembersHandler = null;
	private CFBamXmlLoaderHPlusPgSqlSchemaObjImplHandler hplusPgSqlSchemaObjImplHandler = null;
	private CFBamXmlLoaderHPlusPgSqlSchemaObjIncludeHandler hplusPgSqlSchemaObjIncludeHandler = null;
	private CFBamXmlLoaderHPlusRamSchemaObjMembersHandler hplusRamSchemaObjMembersHandler = null;
	private CFBamXmlLoaderHPlusRamSchemaObjImplHandler hplusRamSchemaObjImplHandler = null;
	private CFBamXmlLoaderHPlusRamSchemaObjIncludeHandler hplusRamSchemaObjIncludeHandler = null;
	private CFBamXmlLoaderHPlusXMsgSchemaIncludeHandler hplusXMsgSchemaIncludeHandler = null;
	private CFBamXmlLoaderHPlusXMsgSchemaFormattersHandler hplusXMsgSchemaFormattersHandler = null;
	private CFBamXmlLoaderHPlusXMsgClientSchemaIncludeHandler hplusXMsgClientSchemaIncludeHandler = null;
	private CFBamXmlLoaderHPlusXMsgClientSchemaBodyHandler hplusXMsgClientSchemaBodyHandler = null;
	private CFBamXmlLoaderHPlusXMsgRqstSchemaBodyHandler hplusXMsgRqstSchemaBodyHandler = null;
	private CFBamXmlLoaderHPlusXMsgRqstSchemaIncludeHandler hplusXMsgRqstSchemaIncludeHandler = null;
	private CFBamXmlLoaderHPlusXMsgRqstSchemaWireParsersHandler hplusXMsgRqstSchemaWireParsersHandler = null;
	private CFBamXmlLoaderHPlusXMsgRqstSchemaXsdElementListHandler hplusXMsgRqstSchemaXsdElementListHandler = null;
	private CFBamXmlLoaderHPlusXMsgRqstSchemaXsdSpecHandler hplusXMsgRqstSchemaXsdSpecHandler = null;
	private CFBamXmlLoaderHPlusXMsgRspnSchemaBodyHandler hplusXMsgRspnSchemaBodyHandler = null;
	private CFBamXmlLoaderHPlusXMsgRspnSchemaIncludeHandler hplusXMsgRspnSchemaIncludeHandler = null;
	private CFBamXmlLoaderHPlusXMsgRspnSchemaWireParsersHandler hplusXMsgRspnSchemaWireParsersHandler = null;
	private CFBamXmlLoaderHPlusXMsgRspnSchemaXsdElementListHandler hplusXMsgRspnSchemaXsdElementListHandler = null;
	private CFBamXmlLoaderHPlusXMsgRspnSchemaXsdSpecHandler hplusXMsgRspnSchemaXsdSpecHandler = null;
	private CFBamXmlLoaderHPlusObjMembersHandler hplusObjMembersHandler = null;
	private CFBamXmlLoaderHPlusObjInterfaceHandler hplusObjInterfaceHandler = null;
	private CFBamXmlLoaderHPlusObjImplementationHandler hplusObjImplementationHandler = null;
	private CFBamXmlLoaderHPlusEditObjMembersHandler hplusEditObjMembersHandler = null;
	private CFBamXmlLoaderHPlusEditObjInterfaceHandler hplusEditObjInterfaceHandler = null;
	private CFBamXmlLoaderHPlusEditObjImplementationHandler hplusEditObjImplementationHandler = null;
	private CFBamXmlLoaderHPlusDb2LUWTableIncludeHandler hplusDb2LUWTableIncludeHandler = null;
	private CFBamXmlLoaderHPlusMSSqlTableIncludeHandler hplusMSSqlTableIncludeHandler = null;
	private CFBamXmlLoaderHPlusMySqlTableIncludeHandler hplusMySqlTableIncludeHandler = null;
	private CFBamXmlLoaderHPlusOracleTableIncludeHandler hplusOracleTableIncludeHandler = null;
	private CFBamXmlLoaderHPlusPgSqlTableIncludeHandler hplusPgSqlTableIncludeHandler = null;
	private CFBamXmlLoaderHPlusRamTableIncludeHandler hplusRamTableIncludeHandler = null;
	private CFBamXmlLoaderHPlusObjIncludeHandler hplusObjIncludeHandler = null;
	private CFBamXmlLoaderHPlusEditObjIncludeHandler hplusEditObjIncludeHandler = null;
	private CFBamXmlLoaderHPlusSaxLoaderIncludeHandler hplusSaxLoaderIncludeHandler = null;
	private CFBamXmlLoaderHPlusTableIncludeHandler hplusTableIncludeHandler = null;
	private CFBamXmlLoaderHPlusTableObjIncludeHandler hplusTableObjIncludeHandler = null;
	private CFBamXmlLoaderHPlusTableMembersHandler hplusTableMembersHandler = null;
	private CFBamXmlLoaderHPlusTableInterfaceHandler hplusTableInterfaceHandler = null;
	private CFBamXmlLoaderHPlusTableImplementationHandler hplusTableImplementationHandler = null;
	private CFBamXmlLoaderHPlusTableObjMembersHandler hplusTableObjMembersHandler = null;
	private CFBamXmlLoaderHPlusTableObjInterfaceHandler hplusTableObjInterfaceHandler = null;
	private CFBamXmlLoaderHPlusTableObjImplementationHandler hplusTableObjImplementationHandler = null;
	private CFBamXmlLoaderHPlusDb2LUWTableMembersHandler hplusDb2LUWTableMembersHandler = null;
	private CFBamXmlLoaderHPlusDb2LUWTableImplementationHandler hplusDb2LUWTableImplementationHandler = null;
	private CFBamXmlLoaderHPlusMSSqlTableMembersHandler hplusMSSqlTableMembersHandler = null;
	private CFBamXmlLoaderHPlusMSSqlTableImplementationHandler hplusMSSqlTableImplementationHandler = null;
	private CFBamXmlLoaderHPlusMySqlTableMembersHandler hplusMySqlTableMembersHandler = null;
	private CFBamXmlLoaderHPlusMySqlTableImplementationHandler hplusMySqlTableImplementationHandler = null;
	private CFBamXmlLoaderHPlusOracleTableMembersHandler hplusOracleTableMembersHandler = null;
	private CFBamXmlLoaderHPlusOracleTableImplementationHandler hplusOracleTableImplementationHandler = null;
	private CFBamXmlLoaderHPlusPgSqlTableMembersHandler hplusPgSqlTableMembersHandler = null;
	private CFBamXmlLoaderHPlusPgSqlTableImplementationHandler hplusPgSqlTableImplementationHandler = null;
	private CFBamXmlLoaderHPlusRamTableMembersHandler hplusRamTableMembersHandler = null;
	private CFBamXmlLoaderHPlusRamTableImplementationHandler hplusRamTableImplementationHandler = null;
	private CFBamXmlLoaderHPlusSaxLoaderStartElementHandler hplusSaxLoaderStartElementHandler = null;
	private CFBamXmlLoaderHPlusSaxLoaderEndElementHandler hplusSaxLoaderEndElementHandler = null;
	private CFBamXmlLoaderHPlusXMsgTableIncludeHandler hplusXMsgTableIncludeHandler = null;
	private CFBamXmlLoaderHPlusXMsgTableFormattersHandler hplusXMsgTableFormattersHandler = null;
	private CFBamXmlLoaderHPlusXMsgRqstTableIncludeHandler hplusXMsgRqstTableIncludeHandler = null;
	private CFBamXmlLoaderHPlusXMsgRspnTableIncludeHandler hplusXMsgRspnTableIncludeHandler = null;
	private CFBamXmlLoaderHPlusXMsgClientTableIncludeHandler hplusXMsgClientTableIncludeHandler = null;
	private CFBamXmlLoaderHPlusXMsgRqstTableBodyHandler hplusXMsgRqstTableBodyHandler = null;
	private CFBamXmlLoaderHPlusXMsgRspnTableBodyHandler hplusXMsgRspnTableBodyHandler = null;
	private CFBamXmlLoaderHPlusXMsgClientTableBodyHandler hplusXMsgClientTableBodyHandler = null;
	private CFBamXmlLoaderCSharpMethodBodyHandler csharpMethodBodyHandler = null;
	private CFBamXmlLoaderCSharpSchemaObjInterfaceHandler csharpSchemaObjInterfaceHandler = null;
	private CFBamXmlLoaderCSharpSchemaObjMembersHandler csharpSchemaObjMembersHandler = null;
	private CFBamXmlLoaderCSharpSchemaObjUsingHandler csharpSchemaObjUsingHandler = null;
	private CFBamXmlLoaderCSharpSchemaObjImplementationHandler csharpSchemaObjImplementationHandler = null;
	private CFBamXmlLoaderCSharpDb2LUWSchemaObjMembersHandler csharpDb2LUWSchemaObjMembersHandler = null;
	private CFBamXmlLoaderCSharpDb2LUWSchemaObjImplHandler csharpDb2LUWSchemaObjImplHandler = null;
	private CFBamXmlLoaderCSharpDb2LUWSchemaObjUsingHandler csharpDb2LUWSchemaObjUsingHandler = null;
	private CFBamXmlLoaderCSharpMSSqlSchemaObjMembersHandler csharpMSSqlSchemaObjMembersHandler = null;
	private CFBamXmlLoaderCSharpMSSqlSchemaObjImplHandler csharpMSSqlSchemaObjImplHandler = null;
	private CFBamXmlLoaderCSharpMSSqlSchemaObjUsingHandler csharpMSSqlSchemaObjUsingHandler = null;
	private CFBamXmlLoaderCSharpMySqlSchemaObjMembersHandler csharpMySqlSchemaObjMembersHandler = null;
	private CFBamXmlLoaderCSharpMySqlSchemaObjImplHandler csharpMySqlSchemaObjImplHandler = null;
	private CFBamXmlLoaderCSharpMySqlSchemaObjUsingHandler csharpMySqlSchemaObjUsingHandler = null;
	private CFBamXmlLoaderCSharpOracleSchemaObjMembersHandler csharpOracleSchemaObjMembersHandler = null;
	private CFBamXmlLoaderCSharpOracleSchemaObjImplHandler csharpOracleSchemaObjImplHandler = null;
	private CFBamXmlLoaderCSharpOracleSchemaObjUsingHandler csharpOracleSchemaObjUsingHandler = null;
	private CFBamXmlLoaderCSharpPgSqlSchemaObjMembersHandler csharpPgSqlSchemaObjMembersHandler = null;
	private CFBamXmlLoaderCSharpPgSqlSchemaObjImplHandler csharpPgSqlSchemaObjImplHandler = null;
	private CFBamXmlLoaderCSharpPgSqlSchemaObjUsingHandler csharpPgSqlSchemaObjUsingHandler = null;
	private CFBamXmlLoaderCSharpRamSchemaObjMembersHandler csharpRamSchemaObjMembersHandler = null;
	private CFBamXmlLoaderCSharpRamSchemaObjImplHandler csharpRamSchemaObjImplHandler = null;
	private CFBamXmlLoaderCSharpRamSchemaObjUsingHandler csharpRamSchemaObjUsingHandler = null;
	private CFBamXmlLoaderCSharpXMsgSchemaUsingHandler csharpXMsgSchemaUsingHandler = null;
	private CFBamXmlLoaderCSharpXMsgSchemaFormattersHandler csharpXMsgSchemaFormattersHandler = null;
	private CFBamXmlLoaderCSharpXMsgClientSchemaUsingHandler csharpXMsgClientSchemaUsingHandler = null;
	private CFBamXmlLoaderCSharpXMsgClientSchemaBodyHandler csharpXMsgClientSchemaBodyHandler = null;
	private CFBamXmlLoaderCSharpXMsgRqstSchemaBodyHandler csharpXMsgRqstSchemaBodyHandler = null;
	private CFBamXmlLoaderCSharpXMsgRqstSchemaUsingHandler csharpXMsgRqstSchemaUsingHandler = null;
	private CFBamXmlLoaderCSharpXMsgRqstSchemaWireParsersHandler csharpXMsgRqstSchemaWireParsersHandler = null;
	private CFBamXmlLoaderCSharpXMsgRqstSchemaXsdElementListHandler csharpXMsgRqstSchemaXsdElementListHandler = null;
	private CFBamXmlLoaderCSharpXMsgRqstSchemaXsdSpecHandler csharpXMsgRqstSchemaXsdSpecHandler = null;
	private CFBamXmlLoaderCSharpXMsgRspnSchemaBodyHandler csharpXMsgRspnSchemaBodyHandler = null;
	private CFBamXmlLoaderCSharpXMsgRspnSchemaUsingHandler csharpXMsgRspnSchemaUsingHandler = null;
	private CFBamXmlLoaderCSharpXMsgRspnSchemaWireParsersHandler csharpXMsgRspnSchemaWireParsersHandler = null;
	private CFBamXmlLoaderCSharpXMsgRspnSchemaXsdElementListHandler csharpXMsgRspnSchemaXsdElementListHandler = null;
	private CFBamXmlLoaderCSharpXMsgRspnSchemaXsdSpecHandler csharpXMsgRspnSchemaXsdSpecHandler = null;
	private CFBamXmlLoaderCSharpObjMembersHandler csharpObjMembersHandler = null;
	private CFBamXmlLoaderCSharpObjInterfaceHandler csharpObjInterfaceHandler = null;
	private CFBamXmlLoaderCSharpObjImplementationHandler csharpObjImplementationHandler = null;
	private CFBamXmlLoaderCSharpEditObjMembersHandler csharpEditObjMembersHandler = null;
	private CFBamXmlLoaderCSharpEditObjInterfaceHandler csharpEditObjInterfaceHandler = null;
	private CFBamXmlLoaderCSharpEditObjImplementationHandler csharpEditObjImplementationHandler = null;
	private CFBamXmlLoaderCSharpDb2LUWTableUsingHandler csharpDb2LUWTableUsingHandler = null;
	private CFBamXmlLoaderCSharpMSSqlTableUsingHandler csharpMSSqlTableUsingHandler = null;
	private CFBamXmlLoaderCSharpMySqlTableUsingHandler csharpMySqlTableUsingHandler = null;
	private CFBamXmlLoaderCSharpOracleTableUsingHandler csharpOracleTableUsingHandler = null;
	private CFBamXmlLoaderCSharpPgSqlTableUsingHandler csharpPgSqlTableUsingHandler = null;
	private CFBamXmlLoaderCSharpRamTableUsingHandler csharpRamTableUsingHandler = null;
	private CFBamXmlLoaderCSharpObjUsingHandler csharpObjUsingHandler = null;
	private CFBamXmlLoaderCSharpEditObjUsingHandler csharpEditObjUsingHandler = null;
	private CFBamXmlLoaderCSharpSaxLoaderUsingHandler csharpSaxLoaderUsingHandler = null;
	private CFBamXmlLoaderCSharpTableUsingHandler csharpTableUsingHandler = null;
	private CFBamXmlLoaderCSharpTableObjUsingHandler csharpTableObjUsingHandler = null;
	private CFBamXmlLoaderCSharpTableMembersHandler csharpTableMembersHandler = null;
	private CFBamXmlLoaderCSharpTableInterfaceHandler csharpTableInterfaceHandler = null;
	private CFBamXmlLoaderCSharpTableImplementationHandler csharpTableImplementationHandler = null;
	private CFBamXmlLoaderCSharpTableObjMembersHandler csharpTableObjMembersHandler = null;
	private CFBamXmlLoaderCSharpTableObjInterfaceHandler csharpTableObjInterfaceHandler = null;
	private CFBamXmlLoaderCSharpTableObjImplementationHandler csharpTableObjImplementationHandler = null;
	private CFBamXmlLoaderCSharpDb2LUWTableMembersHandler csharpDb2LUWTableMembersHandler = null;
	private CFBamXmlLoaderCSharpDb2LUWTableImplementationHandler csharpDb2LUWTableImplementationHandler = null;
	private CFBamXmlLoaderCSharpMSSqlTableMembersHandler csharpMSSqlTableMembersHandler = null;
	private CFBamXmlLoaderCSharpMSSqlTableImplementationHandler csharpMSSqlTableImplementationHandler = null;
	private CFBamXmlLoaderCSharpMySqlTableMembersHandler csharpMySqlTableMembersHandler = null;
	private CFBamXmlLoaderCSharpMySqlTableImplementationHandler csharpMySqlTableImplementationHandler = null;
	private CFBamXmlLoaderCSharpOracleTableMembersHandler csharpOracleTableMembersHandler = null;
	private CFBamXmlLoaderCSharpOracleTableImplementationHandler csharpOracleTableImplementationHandler = null;
	private CFBamXmlLoaderCSharpPgSqlTableMembersHandler csharpPgSqlTableMembersHandler = null;
	private CFBamXmlLoaderCSharpPgSqlTableImplementationHandler csharpPgSqlTableImplementationHandler = null;
	private CFBamXmlLoaderCSharpRamTableMembersHandler csharpRamTableMembersHandler = null;
	private CFBamXmlLoaderCSharpRamTableImplementationHandler csharpRamTableImplementationHandler = null;
	private CFBamXmlLoaderCSharpSaxLoaderStartElementHandler csharpSaxLoaderStartElementHandler = null;
	private CFBamXmlLoaderCSharpSaxLoaderEndElementHandler csharpSaxLoaderEndElementHandler = null;
	private CFBamXmlLoaderCSharpXMsgTableUsingHandler csharpXMsgTableUsingHandler = null;
	private CFBamXmlLoaderCSharpXMsgTableFormattersHandler csharpXMsgTableFormattersHandler = null;
	private CFBamXmlLoaderCSharpXMsgRqstTableUsingHandler csharpXMsgRqstTableUsingHandler = null;
	private CFBamXmlLoaderCSharpXMsgRspnTableUsingHandler csharpXMsgRspnTableUsingHandler = null;
	private CFBamXmlLoaderCSharpXMsgClientTableUsingHandler csharpXMsgClientTableUsingHandler = null;
	private CFBamXmlLoaderCSharpXMsgRqstTableBodyHandler csharpXMsgRqstTableBodyHandler = null;
	private CFBamXmlLoaderCSharpXMsgRspnTableBodyHandler csharpXMsgRspnTableBodyHandler = null;
	private CFBamXmlLoaderCSharpXMsgClientTableBodyHandler csharpXMsgClientTableBodyHandler = null;
	private CFBamXmlLoaderLicenseHandler licenseHandler = null;
	private CFBamXmlLoaderMajorVersionHandler majorVersionHandler = null;
	private CFBamXmlLoaderMinorVersionHandler minorVersionHandler = null;
	private CFBamXmlLoaderNmTokenColHandler nmTokenColHandler = null;
	private CFBamXmlLoaderNmTokenTypeHandler nmTokenTypeHandler = null;
	private CFBamXmlLoaderNmTokensColHandler nmTokensColHandler = null;
	private CFBamXmlLoaderNmTokensTypeHandler nmTokensTypeHandler = null;
	private CFBamXmlLoaderNumberColHandler numberColHandler = null;
	private CFBamXmlLoaderNumberTypeHandler numberTypeHandler = null;
	private CFBamXmlLoaderParamHandler paramHandler = null;
	private CFBamXmlLoaderPrimaryIndexHandler primaryIndexHandler = null;
	private CFBamXmlLoaderRelationHandler relationHandler = null;
	private CFBamXmlLoaderRelationColHandler relationColHandler = null;
	private CFBamXmlLoaderSchemaDefHandler schemaDefHandler = null;
	private CFBamXmlLoaderSchemaRefHandler schemaRefHandler = null;
	private CFBamXmlLoaderServerListFuncHandler serverListFuncHandler = null;
	private CFBamXmlLoaderServerObjFuncHandler serverObjFuncHandler = null;
	private CFBamXmlLoaderServerProcHandler serverProcHandler = null;
	private CFBamXmlLoaderSuperClassRelationHandler superClassRelationHandler = null;
	private CFBamXmlLoaderStringColHandler stringColHandler = null;
	private CFBamXmlLoaderStringTypeHandler stringTypeHandler = null;
	private CFBamXmlLoaderSubProjectHandler subProjectHandler = null;
	private CFBamXmlLoaderTZDateColHandler tZDateColHandler = null;
	private CFBamXmlLoaderTZDateTypeHandler tZDateTypeHandler = null;
	private CFBamXmlLoaderTZTimeColHandler tZTimeColHandler = null;
	private CFBamXmlLoaderTZTimeTypeHandler tZTimeTypeHandler = null;
	private CFBamXmlLoaderTZTimestampColHandler tZTimestampColHandler = null;
	private CFBamXmlLoaderTZTimestampTypeHandler tZTimestampTypeHandler = null;
	private CFBamXmlLoaderTableHandler tableHandler = null;
	private CFBamXmlLoaderTableAddendumHandler tableAddendumHandler = null;
	private CFBamXmlLoaderTableColHandler tableColHandler = null;
	private CFBamXmlLoaderTextColHandler textColHandler = null;
	private CFBamXmlLoaderTextTypeHandler textTypeHandler = null;
	private CFBamXmlLoaderTimeColHandler timeColHandler = null;
	private CFBamXmlLoaderTimeTypeHandler timeTypeHandler = null;
	private CFBamXmlLoaderTimestampColHandler timestampColHandler = null;
	private CFBamXmlLoaderTimestampTypeHandler timestampTypeHandler = null;
	private CFBamXmlLoaderTldHandler tldHandler = null;
	private CFBamXmlLoaderTokenColHandler tokenColHandler = null;
	private CFBamXmlLoaderTokenTypeHandler tokenTypeHandler = null;
	private CFBamXmlLoaderTopDomainHandler topDomainHandler = null;
	private CFBamXmlLoaderTopProjectHandler topProjectHandler = null;
	private CFBamXmlLoaderUInt16ColHandler uInt16ColHandler = null;
	private CFBamXmlLoaderUInt16TypeHandler uInt16TypeHandler = null;
	private CFBamXmlLoaderUInt32ColHandler uInt32ColHandler = null;
	private CFBamXmlLoaderUInt32TypeHandler uInt32TypeHandler = null;
	private CFBamXmlLoaderUInt64ColHandler uInt64ColHandler = null;
	private CFBamXmlLoaderUInt64TypeHandler uInt64TypeHandler = null;
	private CFBamXmlLoaderUuidColHandler uuidColHandler = null;
	private CFBamXmlLoaderUuidGenHandler uuidGenHandler = null;
	private CFBamXmlLoaderUuidTypeHandler uuidTypeHandler = null;
	private CFBamSaxRootHandler saxRootHandler = null;

	private CFBamSaxDocHandler saxDocHandler = null;

	// Schema object accessors

	// SchemaObj accessors

	public ICFBamSchemaObj getSchemaObj() {
		return( schemaObj );
	}

	public void setSchemaObj( ICFBamSchemaObj value ) {
		schemaObj = value;
	}

	// Element Handler Resolver Factories

	protected CFBamXmlLoaderCommonLicenseHandler getCommonLicenseHandler() {
		if( commonLicenseHandler == null ) {
			commonLicenseHandler = new CFBamXmlLoaderCommonLicenseHandler( this );
		}
		return( commonLicenseHandler );
	}

	protected CFBamXmlLoaderClientImplLicenseHandler getClientImplLicenseHandler() {
		if( clientImplLicenseHandler == null ) {
			clientImplLicenseHandler = new CFBamXmlLoaderClientImplLicenseHandler( this );
		}
		return( clientImplLicenseHandler );
	}

	protected CFBamXmlLoaderClientXFaceLicenseHandler getClientXFaceLicenseHandler() {
		if( clientXFaceLicenseHandler == null ) {
			clientXFaceLicenseHandler = new CFBamXmlLoaderClientXFaceLicenseHandler( this );
		}
		return( clientXFaceLicenseHandler );
	}

	protected CFBamXmlLoaderServerImplLicenseHandler getServerImplLicenseHandler() {
		if( serverImplLicenseHandler == null ) {
			serverImplLicenseHandler = new CFBamXmlLoaderServerImplLicenseHandler( this );
		}
		return( serverImplLicenseHandler );
	}

	protected CFBamXmlLoaderServerXFaceLicenseHandler getServerXFaceLicenseHandler() {
		if( serverXFaceLicenseHandler == null ) {
			serverXFaceLicenseHandler = new CFBamXmlLoaderServerXFaceLicenseHandler( this );
		}
		return( serverXFaceLicenseHandler );
	}

	protected CFBamXmlLoaderBlobColHandler getBlobColHandler() {
		if( blobColHandler == null ) {
			blobColHandler = new CFBamXmlLoaderBlobColHandler( this );
		}
		return( blobColHandler );
	}

	protected CFBamXmlLoaderBlobTypeHandler getBlobTypeHandler() {
		if( blobTypeHandler == null ) {
			blobTypeHandler = new CFBamXmlLoaderBlobTypeHandler( this );
		}
		return( blobTypeHandler );
	}

	protected CFBamXmlLoaderBoolColHandler getBoolColHandler() {
		if( boolColHandler == null ) {
			boolColHandler = new CFBamXmlLoaderBoolColHandler( this );
		}
		return( boolColHandler );
	}

	protected CFBamXmlLoaderBoolTypeHandler getBoolTypeHandler() {
		if( boolTypeHandler == null ) {
			boolTypeHandler = new CFBamXmlLoaderBoolTypeHandler( this );
		}
		return( boolTypeHandler );
	}

	protected CFBamXmlLoaderChainHandler getChainHandler() {
		if( chainHandler == null ) {
			chainHandler = new CFBamXmlLoaderChainHandler( this );
		}
		return( chainHandler );
	}

	protected CFBamXmlLoaderClearDepHandler getClearDepHandler() {
		if( clearDepHandler == null ) {
			clearDepHandler = new CFBamXmlLoaderClearDepHandler( this );
		}
		return( clearDepHandler );
	}

	protected CFBamXmlLoaderDateColHandler getDateColHandler() {
		if( dateColHandler == null ) {
			dateColHandler = new CFBamXmlLoaderDateColHandler( this );
		}
		return( dateColHandler );
	}

	protected CFBamXmlLoaderDateTypeHandler getDateTypeHandler() {
		if( dateTypeHandler == null ) {
			dateTypeHandler = new CFBamXmlLoaderDateTypeHandler( this );
		}
		return( dateTypeHandler );
	}

	protected CFBamXmlLoaderDelDepHandler getDelDepHandler() {
		if( delDepHandler == null ) {
			delDepHandler = new CFBamXmlLoaderDelDepHandler( this );
		}
		return( delDepHandler );
	}

	protected CFBamXmlLoaderDoubleColHandler getDoubleColHandler() {
		if( doubleColHandler == null ) {
			doubleColHandler = new CFBamXmlLoaderDoubleColHandler( this );
		}
		return( doubleColHandler );
	}

	protected CFBamXmlLoaderDoubleTypeHandler getDoubleTypeHandler() {
		if( doubleTypeHandler == null ) {
			doubleTypeHandler = new CFBamXmlLoaderDoubleTypeHandler( this );
		}
		return( doubleTypeHandler );
	}

	protected CFBamXmlLoaderEnumTagHandler getEnumTagHandler() {
		if( enumTagHandler == null ) {
			enumTagHandler = new CFBamXmlLoaderEnumTagHandler( this );
		}
		return( enumTagHandler );
	}

	protected CFBamXmlLoaderEnumTypeHandler getEnumTypeHandler() {
		if( enumTypeHandler == null ) {
			enumTypeHandler = new CFBamXmlLoaderEnumTypeHandler( this );
			enumTypeHandler.addElementHandler( "EnumTag", getEnumTagHandler() );
		}
		return( enumTypeHandler );
	}

	protected CFBamXmlLoaderFloatColHandler getFloatColHandler() {
		if( floatColHandler == null ) {
			floatColHandler = new CFBamXmlLoaderFloatColHandler( this );
		}
		return( floatColHandler );
	}

	protected CFBamXmlLoaderFloatTypeHandler getFloatTypeHandler() {
		if( floatTypeHandler == null ) {
			floatTypeHandler = new CFBamXmlLoaderFloatTypeHandler( this );
		}
		return( floatTypeHandler );
	}

	protected CFBamXmlLoaderId16GenHandler getId16GenHandler() {
		if( id16GenHandler == null ) {
			id16GenHandler = new CFBamXmlLoaderId16GenHandler( this );
		}
		return( id16GenHandler );
	}

	protected CFBamXmlLoaderId32GenHandler getId32GenHandler() {
		if( id32GenHandler == null ) {
			id32GenHandler = new CFBamXmlLoaderId32GenHandler( this );
		}
		return( id32GenHandler );
	}

	protected CFBamXmlLoaderId64GenHandler getId64GenHandler() {
		if( id64GenHandler == null ) {
			id64GenHandler = new CFBamXmlLoaderId64GenHandler( this );
		}
		return( id64GenHandler );
	}

	protected CFBamXmlLoaderIndexHandler getIndexHandler() {
		if( indexHandler == null ) {
			indexHandler = new CFBamXmlLoaderIndexHandler( this );
			indexHandler.addElementHandler( "IndexCol", getIndexColHandler() );
		}
		return( indexHandler );
	}

	protected CFBamXmlLoaderIndexColHandler getIndexColHandler() {
		if( indexColHandler == null ) {
			indexColHandler = new CFBamXmlLoaderIndexColHandler( this );
		}
		return( indexColHandler );
	}

	protected CFBamXmlLoaderInt16ColHandler getInt16ColHandler() {
		if( int16ColHandler == null ) {
			int16ColHandler = new CFBamXmlLoaderInt16ColHandler( this );
		}
		return( int16ColHandler );
	}

	protected CFBamXmlLoaderInt16TypeHandler getInt16TypeHandler() {
		if( int16TypeHandler == null ) {
			int16TypeHandler = new CFBamXmlLoaderInt16TypeHandler( this );
		}
		return( int16TypeHandler );
	}

	protected CFBamXmlLoaderInt32ColHandler getInt32ColHandler() {
		if( int32ColHandler == null ) {
			int32ColHandler = new CFBamXmlLoaderInt32ColHandler( this );
		}
		return( int32ColHandler );
	}

	protected CFBamXmlLoaderInt32TypeHandler getInt32TypeHandler() {
		if( int32TypeHandler == null ) {
			int32TypeHandler = new CFBamXmlLoaderInt32TypeHandler( this );
		}
		return( int32TypeHandler );
	}

	protected CFBamXmlLoaderInt64ColHandler getInt64ColHandler() {
		if( int64ColHandler == null ) {
			int64ColHandler = new CFBamXmlLoaderInt64ColHandler( this );
		}
		return( int64ColHandler );
	}

	protected CFBamXmlLoaderInt64TypeHandler getInt64TypeHandler() {
		if( int64TypeHandler == null ) {
			int64TypeHandler = new CFBamXmlLoaderInt64TypeHandler( this );
		}
		return( int64TypeHandler );
	}

	protected CFBamXmlLoaderCafeMethodBodyHandler getCafeMethodBodyHandler() {
		if( cafeMethodBodyHandler == null ) {
			cafeMethodBodyHandler = new CFBamXmlLoaderCafeMethodBodyHandler( this );
		}
		return( cafeMethodBodyHandler );
	}

	protected CFBamXmlLoaderCafeSchemaObjInterfaceHandler getCafeSchemaObjInterfaceHandler() {
		if( cafeSchemaObjInterfaceHandler == null ) {
			cafeSchemaObjInterfaceHandler = new CFBamXmlLoaderCafeSchemaObjInterfaceHandler( this );
		}
		return( cafeSchemaObjInterfaceHandler );
	}

	protected CFBamXmlLoaderCafeSchemaObjMembersHandler getCafeSchemaObjMembersHandler() {
		if( cafeSchemaObjMembersHandler == null ) {
			cafeSchemaObjMembersHandler = new CFBamXmlLoaderCafeSchemaObjMembersHandler( this );
		}
		return( cafeSchemaObjMembersHandler );
	}

	protected CFBamXmlLoaderCafeSchemaObjImportHandler getCafeSchemaObjImportHandler() {
		if( cafeSchemaObjImportHandler == null ) {
			cafeSchemaObjImportHandler = new CFBamXmlLoaderCafeSchemaObjImportHandler( this );
		}
		return( cafeSchemaObjImportHandler );
	}

	protected CFBamXmlLoaderCafeSchemaObjImplementationHandler getCafeSchemaObjImplementationHandler() {
		if( cafeSchemaObjImplementationHandler == null ) {
			cafeSchemaObjImplementationHandler = new CFBamXmlLoaderCafeSchemaObjImplementationHandler( this );
		}
		return( cafeSchemaObjImplementationHandler );
	}

	protected CFBamXmlLoaderCafeDb2LUWSchemaObjMembersHandler getCafeDb2LUWSchemaObjMembersHandler() {
		if( cafeDb2LUWSchemaObjMembersHandler == null ) {
			cafeDb2LUWSchemaObjMembersHandler = new CFBamXmlLoaderCafeDb2LUWSchemaObjMembersHandler( this );
		}
		return( cafeDb2LUWSchemaObjMembersHandler );
	}

	protected CFBamXmlLoaderCafeDb2LUWSchemaObjImplHandler getCafeDb2LUWSchemaObjImplHandler() {
		if( cafeDb2LUWSchemaObjImplHandler == null ) {
			cafeDb2LUWSchemaObjImplHandler = new CFBamXmlLoaderCafeDb2LUWSchemaObjImplHandler( this );
		}
		return( cafeDb2LUWSchemaObjImplHandler );
	}

	protected CFBamXmlLoaderCafeDb2LUWSchemaObjImportHandler getCafeDb2LUWSchemaObjImportHandler() {
		if( cafeDb2LUWSchemaObjImportHandler == null ) {
			cafeDb2LUWSchemaObjImportHandler = new CFBamXmlLoaderCafeDb2LUWSchemaObjImportHandler( this );
		}
		return( cafeDb2LUWSchemaObjImportHandler );
	}

	protected CFBamXmlLoaderCafeMSSqlSchemaObjMembersHandler getCafeMSSqlSchemaObjMembersHandler() {
		if( cafeMSSqlSchemaObjMembersHandler == null ) {
			cafeMSSqlSchemaObjMembersHandler = new CFBamXmlLoaderCafeMSSqlSchemaObjMembersHandler( this );
		}
		return( cafeMSSqlSchemaObjMembersHandler );
	}

	protected CFBamXmlLoaderCafeMSSqlSchemaObjImplHandler getCafeMSSqlSchemaObjImplHandler() {
		if( cafeMSSqlSchemaObjImplHandler == null ) {
			cafeMSSqlSchemaObjImplHandler = new CFBamXmlLoaderCafeMSSqlSchemaObjImplHandler( this );
		}
		return( cafeMSSqlSchemaObjImplHandler );
	}

	protected CFBamXmlLoaderCafeMSSqlSchemaObjImportHandler getCafeMSSqlSchemaObjImportHandler() {
		if( cafeMSSqlSchemaObjImportHandler == null ) {
			cafeMSSqlSchemaObjImportHandler = new CFBamXmlLoaderCafeMSSqlSchemaObjImportHandler( this );
		}
		return( cafeMSSqlSchemaObjImportHandler );
	}

	protected CFBamXmlLoaderCafeMySqlSchemaObjMembersHandler getCafeMySqlSchemaObjMembersHandler() {
		if( cafeMySqlSchemaObjMembersHandler == null ) {
			cafeMySqlSchemaObjMembersHandler = new CFBamXmlLoaderCafeMySqlSchemaObjMembersHandler( this );
		}
		return( cafeMySqlSchemaObjMembersHandler );
	}

	protected CFBamXmlLoaderCafeMySqlSchemaObjImplHandler getCafeMySqlSchemaObjImplHandler() {
		if( cafeMySqlSchemaObjImplHandler == null ) {
			cafeMySqlSchemaObjImplHandler = new CFBamXmlLoaderCafeMySqlSchemaObjImplHandler( this );
		}
		return( cafeMySqlSchemaObjImplHandler );
	}

	protected CFBamXmlLoaderCafeMySqlSchemaObjImportHandler getCafeMySqlSchemaObjImportHandler() {
		if( cafeMySqlSchemaObjImportHandler == null ) {
			cafeMySqlSchemaObjImportHandler = new CFBamXmlLoaderCafeMySqlSchemaObjImportHandler( this );
		}
		return( cafeMySqlSchemaObjImportHandler );
	}

	protected CFBamXmlLoaderCafeOracleSchemaObjMembersHandler getCafeOracleSchemaObjMembersHandler() {
		if( cafeOracleSchemaObjMembersHandler == null ) {
			cafeOracleSchemaObjMembersHandler = new CFBamXmlLoaderCafeOracleSchemaObjMembersHandler( this );
		}
		return( cafeOracleSchemaObjMembersHandler );
	}

	protected CFBamXmlLoaderCafeOracleSchemaObjImplHandler getCafeOracleSchemaObjImplHandler() {
		if( cafeOracleSchemaObjImplHandler == null ) {
			cafeOracleSchemaObjImplHandler = new CFBamXmlLoaderCafeOracleSchemaObjImplHandler( this );
		}
		return( cafeOracleSchemaObjImplHandler );
	}

	protected CFBamXmlLoaderCafeOracleSchemaObjImportHandler getCafeOracleSchemaObjImportHandler() {
		if( cafeOracleSchemaObjImportHandler == null ) {
			cafeOracleSchemaObjImportHandler = new CFBamXmlLoaderCafeOracleSchemaObjImportHandler( this );
		}
		return( cafeOracleSchemaObjImportHandler );
	}

	protected CFBamXmlLoaderCafePgSqlSchemaObjMembersHandler getCafePgSqlSchemaObjMembersHandler() {
		if( cafePgSqlSchemaObjMembersHandler == null ) {
			cafePgSqlSchemaObjMembersHandler = new CFBamXmlLoaderCafePgSqlSchemaObjMembersHandler( this );
		}
		return( cafePgSqlSchemaObjMembersHandler );
	}

	protected CFBamXmlLoaderCafePgSqlSchemaObjImplHandler getCafePgSqlSchemaObjImplHandler() {
		if( cafePgSqlSchemaObjImplHandler == null ) {
			cafePgSqlSchemaObjImplHandler = new CFBamXmlLoaderCafePgSqlSchemaObjImplHandler( this );
		}
		return( cafePgSqlSchemaObjImplHandler );
	}

	protected CFBamXmlLoaderCafePgSqlSchemaObjImportHandler getCafePgSqlSchemaObjImportHandler() {
		if( cafePgSqlSchemaObjImportHandler == null ) {
			cafePgSqlSchemaObjImportHandler = new CFBamXmlLoaderCafePgSqlSchemaObjImportHandler( this );
		}
		return( cafePgSqlSchemaObjImportHandler );
	}

	protected CFBamXmlLoaderCafeRamSchemaObjMembersHandler getCafeRamSchemaObjMembersHandler() {
		if( cafeRamSchemaObjMembersHandler == null ) {
			cafeRamSchemaObjMembersHandler = new CFBamXmlLoaderCafeRamSchemaObjMembersHandler( this );
		}
		return( cafeRamSchemaObjMembersHandler );
	}

	protected CFBamXmlLoaderCafeRamSchemaObjImplHandler getCafeRamSchemaObjImplHandler() {
		if( cafeRamSchemaObjImplHandler == null ) {
			cafeRamSchemaObjImplHandler = new CFBamXmlLoaderCafeRamSchemaObjImplHandler( this );
		}
		return( cafeRamSchemaObjImplHandler );
	}

	protected CFBamXmlLoaderCafeRamSchemaObjImportHandler getCafeRamSchemaObjImportHandler() {
		if( cafeRamSchemaObjImportHandler == null ) {
			cafeRamSchemaObjImportHandler = new CFBamXmlLoaderCafeRamSchemaObjImportHandler( this );
		}
		return( cafeRamSchemaObjImportHandler );
	}

	protected CFBamXmlLoaderCafeXMsgSchemaImportHandler getCafeXMsgSchemaImportHandler() {
		if( cafeXMsgSchemaImportHandler == null ) {
			cafeXMsgSchemaImportHandler = new CFBamXmlLoaderCafeXMsgSchemaImportHandler( this );
		}
		return( cafeXMsgSchemaImportHandler );
	}

	protected CFBamXmlLoaderCafeXMsgSchemaFormattersHandler getCafeXMsgSchemaFormattersHandler() {
		if( cafeXMsgSchemaFormattersHandler == null ) {
			cafeXMsgSchemaFormattersHandler = new CFBamXmlLoaderCafeXMsgSchemaFormattersHandler( this );
		}
		return( cafeXMsgSchemaFormattersHandler );
	}

	protected CFBamXmlLoaderCafeXMsgClientSchemaImportHandler getCafeXMsgClientSchemaImportHandler() {
		if( cafeXMsgClientSchemaImportHandler == null ) {
			cafeXMsgClientSchemaImportHandler = new CFBamXmlLoaderCafeXMsgClientSchemaImportHandler( this );
		}
		return( cafeXMsgClientSchemaImportHandler );
	}

	protected CFBamXmlLoaderCafeXMsgClientSchemaBodyHandler getCafeXMsgClientSchemaBodyHandler() {
		if( cafeXMsgClientSchemaBodyHandler == null ) {
			cafeXMsgClientSchemaBodyHandler = new CFBamXmlLoaderCafeXMsgClientSchemaBodyHandler( this );
		}
		return( cafeXMsgClientSchemaBodyHandler );
	}

	protected CFBamXmlLoaderCafeXMsgRqstSchemaBodyHandler getCafeXMsgRqstSchemaBodyHandler() {
		if( cafeXMsgRqstSchemaBodyHandler == null ) {
			cafeXMsgRqstSchemaBodyHandler = new CFBamXmlLoaderCafeXMsgRqstSchemaBodyHandler( this );
		}
		return( cafeXMsgRqstSchemaBodyHandler );
	}

	protected CFBamXmlLoaderCafeXMsgRqstSchemaImportHandler getCafeXMsgRqstSchemaImportHandler() {
		if( cafeXMsgRqstSchemaImportHandler == null ) {
			cafeXMsgRqstSchemaImportHandler = new CFBamXmlLoaderCafeXMsgRqstSchemaImportHandler( this );
		}
		return( cafeXMsgRqstSchemaImportHandler );
	}

	protected CFBamXmlLoaderCafeXMsgRqstSchemaWireParsersHandler getCafeXMsgRqstSchemaWireParsersHandler() {
		if( cafeXMsgRqstSchemaWireParsersHandler == null ) {
			cafeXMsgRqstSchemaWireParsersHandler = new CFBamXmlLoaderCafeXMsgRqstSchemaWireParsersHandler( this );
		}
		return( cafeXMsgRqstSchemaWireParsersHandler );
	}

	protected CFBamXmlLoaderCafeXMsgRqstSchemaXsdElementListHandler getCafeXMsgRqstSchemaXsdElementListHandler() {
		if( cafeXMsgRqstSchemaXsdElementListHandler == null ) {
			cafeXMsgRqstSchemaXsdElementListHandler = new CFBamXmlLoaderCafeXMsgRqstSchemaXsdElementListHandler( this );
		}
		return( cafeXMsgRqstSchemaXsdElementListHandler );
	}

	protected CFBamXmlLoaderCafeXMsgRqstSchemaXsdSpecHandler getCafeXMsgRqstSchemaXsdSpecHandler() {
		if( cafeXMsgRqstSchemaXsdSpecHandler == null ) {
			cafeXMsgRqstSchemaXsdSpecHandler = new CFBamXmlLoaderCafeXMsgRqstSchemaXsdSpecHandler( this );
		}
		return( cafeXMsgRqstSchemaXsdSpecHandler );
	}

	protected CFBamXmlLoaderCafeXMsgRspnSchemaBodyHandler getCafeXMsgRspnSchemaBodyHandler() {
		if( cafeXMsgRspnSchemaBodyHandler == null ) {
			cafeXMsgRspnSchemaBodyHandler = new CFBamXmlLoaderCafeXMsgRspnSchemaBodyHandler( this );
		}
		return( cafeXMsgRspnSchemaBodyHandler );
	}

	protected CFBamXmlLoaderCafeXMsgRspnSchemaImportHandler getCafeXMsgRspnSchemaImportHandler() {
		if( cafeXMsgRspnSchemaImportHandler == null ) {
			cafeXMsgRspnSchemaImportHandler = new CFBamXmlLoaderCafeXMsgRspnSchemaImportHandler( this );
		}
		return( cafeXMsgRspnSchemaImportHandler );
	}

	protected CFBamXmlLoaderCafeXMsgRspnSchemaWireParsersHandler getCafeXMsgRspnSchemaWireParsersHandler() {
		if( cafeXMsgRspnSchemaWireParsersHandler == null ) {
			cafeXMsgRspnSchemaWireParsersHandler = new CFBamXmlLoaderCafeXMsgRspnSchemaWireParsersHandler( this );
		}
		return( cafeXMsgRspnSchemaWireParsersHandler );
	}

	protected CFBamXmlLoaderCafeXMsgRspnSchemaXsdElementListHandler getCafeXMsgRspnSchemaXsdElementListHandler() {
		if( cafeXMsgRspnSchemaXsdElementListHandler == null ) {
			cafeXMsgRspnSchemaXsdElementListHandler = new CFBamXmlLoaderCafeXMsgRspnSchemaXsdElementListHandler( this );
		}
		return( cafeXMsgRspnSchemaXsdElementListHandler );
	}

	protected CFBamXmlLoaderCafeXMsgRspnSchemaXsdSpecHandler getCafeXMsgRspnSchemaXsdSpecHandler() {
		if( cafeXMsgRspnSchemaXsdSpecHandler == null ) {
			cafeXMsgRspnSchemaXsdSpecHandler = new CFBamXmlLoaderCafeXMsgRspnSchemaXsdSpecHandler( this );
		}
		return( cafeXMsgRspnSchemaXsdSpecHandler );
	}

	protected CFBamXmlLoaderCafeObjMembersHandler getCafeObjMembersHandler() {
		if( cafeObjMembersHandler == null ) {
			cafeObjMembersHandler = new CFBamXmlLoaderCafeObjMembersHandler( this );
		}
		return( cafeObjMembersHandler );
	}

	protected CFBamXmlLoaderCafeObjInterfaceHandler getCafeObjInterfaceHandler() {
		if( cafeObjInterfaceHandler == null ) {
			cafeObjInterfaceHandler = new CFBamXmlLoaderCafeObjInterfaceHandler( this );
		}
		return( cafeObjInterfaceHandler );
	}

	protected CFBamXmlLoaderCafeObjImplementationHandler getCafeObjImplementationHandler() {
		if( cafeObjImplementationHandler == null ) {
			cafeObjImplementationHandler = new CFBamXmlLoaderCafeObjImplementationHandler( this );
		}
		return( cafeObjImplementationHandler );
	}

	protected CFBamXmlLoaderCafeEditObjMembersHandler getCafeEditObjMembersHandler() {
		if( cafeEditObjMembersHandler == null ) {
			cafeEditObjMembersHandler = new CFBamXmlLoaderCafeEditObjMembersHandler( this );
		}
		return( cafeEditObjMembersHandler );
	}

	protected CFBamXmlLoaderCafeEditObjInterfaceHandler getCafeEditObjInterfaceHandler() {
		if( cafeEditObjInterfaceHandler == null ) {
			cafeEditObjInterfaceHandler = new CFBamXmlLoaderCafeEditObjInterfaceHandler( this );
		}
		return( cafeEditObjInterfaceHandler );
	}

	protected CFBamXmlLoaderCafeEditObjImplementationHandler getCafeEditObjImplementationHandler() {
		if( cafeEditObjImplementationHandler == null ) {
			cafeEditObjImplementationHandler = new CFBamXmlLoaderCafeEditObjImplementationHandler( this );
		}
		return( cafeEditObjImplementationHandler );
	}

	protected CFBamXmlLoaderCafeDb2LUWTableImportHandler getCafeDb2LUWTableImportHandler() {
		if( cafeDb2LUWTableImportHandler == null ) {
			cafeDb2LUWTableImportHandler = new CFBamXmlLoaderCafeDb2LUWTableImportHandler( this );
		}
		return( cafeDb2LUWTableImportHandler );
	}

	protected CFBamXmlLoaderCafeMSSqlTableImportHandler getCafeMSSqlTableImportHandler() {
		if( cafeMSSqlTableImportHandler == null ) {
			cafeMSSqlTableImportHandler = new CFBamXmlLoaderCafeMSSqlTableImportHandler( this );
		}
		return( cafeMSSqlTableImportHandler );
	}

	protected CFBamXmlLoaderCafeMySqlTableImportHandler getCafeMySqlTableImportHandler() {
		if( cafeMySqlTableImportHandler == null ) {
			cafeMySqlTableImportHandler = new CFBamXmlLoaderCafeMySqlTableImportHandler( this );
		}
		return( cafeMySqlTableImportHandler );
	}

	protected CFBamXmlLoaderCafeOracleTableImportHandler getCafeOracleTableImportHandler() {
		if( cafeOracleTableImportHandler == null ) {
			cafeOracleTableImportHandler = new CFBamXmlLoaderCafeOracleTableImportHandler( this );
		}
		return( cafeOracleTableImportHandler );
	}

	protected CFBamXmlLoaderCafePgSqlTableImportHandler getCafePgSqlTableImportHandler() {
		if( cafePgSqlTableImportHandler == null ) {
			cafePgSqlTableImportHandler = new CFBamXmlLoaderCafePgSqlTableImportHandler( this );
		}
		return( cafePgSqlTableImportHandler );
	}

	protected CFBamXmlLoaderCafeRamTableImportHandler getCafeRamTableImportHandler() {
		if( cafeRamTableImportHandler == null ) {
			cafeRamTableImportHandler = new CFBamXmlLoaderCafeRamTableImportHandler( this );
		}
		return( cafeRamTableImportHandler );
	}

	protected CFBamXmlLoaderCafeObjImportHandler getCafeObjImportHandler() {
		if( cafeObjImportHandler == null ) {
			cafeObjImportHandler = new CFBamXmlLoaderCafeObjImportHandler( this );
		}
		return( cafeObjImportHandler );
	}

	protected CFBamXmlLoaderCafeEditObjImportHandler getCafeEditObjImportHandler() {
		if( cafeEditObjImportHandler == null ) {
			cafeEditObjImportHandler = new CFBamXmlLoaderCafeEditObjImportHandler( this );
		}
		return( cafeEditObjImportHandler );
	}

	protected CFBamXmlLoaderCafeSaxLoaderImportHandler getCafeSaxLoaderImportHandler() {
		if( cafeSaxLoaderImportHandler == null ) {
			cafeSaxLoaderImportHandler = new CFBamXmlLoaderCafeSaxLoaderImportHandler( this );
		}
		return( cafeSaxLoaderImportHandler );
	}

	protected CFBamXmlLoaderCafeTableImportHandler getCafeTableImportHandler() {
		if( cafeTableImportHandler == null ) {
			cafeTableImportHandler = new CFBamXmlLoaderCafeTableImportHandler( this );
		}
		return( cafeTableImportHandler );
	}

	protected CFBamXmlLoaderCafeTableObjImportHandler getCafeTableObjImportHandler() {
		if( cafeTableObjImportHandler == null ) {
			cafeTableObjImportHandler = new CFBamXmlLoaderCafeTableObjImportHandler( this );
		}
		return( cafeTableObjImportHandler );
	}

	protected CFBamXmlLoaderCafeTableMembersHandler getCafeTableMembersHandler() {
		if( cafeTableMembersHandler == null ) {
			cafeTableMembersHandler = new CFBamXmlLoaderCafeTableMembersHandler( this );
		}
		return( cafeTableMembersHandler );
	}

	protected CFBamXmlLoaderCafeTableInterfaceHandler getCafeTableInterfaceHandler() {
		if( cafeTableInterfaceHandler == null ) {
			cafeTableInterfaceHandler = new CFBamXmlLoaderCafeTableInterfaceHandler( this );
		}
		return( cafeTableInterfaceHandler );
	}

	protected CFBamXmlLoaderCafeTableImplementationHandler getCafeTableImplementationHandler() {
		if( cafeTableImplementationHandler == null ) {
			cafeTableImplementationHandler = new CFBamXmlLoaderCafeTableImplementationHandler( this );
		}
		return( cafeTableImplementationHandler );
	}

	protected CFBamXmlLoaderCafeTableObjMembersHandler getCafeTableObjMembersHandler() {
		if( cafeTableObjMembersHandler == null ) {
			cafeTableObjMembersHandler = new CFBamXmlLoaderCafeTableObjMembersHandler( this );
		}
		return( cafeTableObjMembersHandler );
	}

	protected CFBamXmlLoaderCafeTableObjInterfaceHandler getCafeTableObjInterfaceHandler() {
		if( cafeTableObjInterfaceHandler == null ) {
			cafeTableObjInterfaceHandler = new CFBamXmlLoaderCafeTableObjInterfaceHandler( this );
		}
		return( cafeTableObjInterfaceHandler );
	}

	protected CFBamXmlLoaderCafeTableObjImplementationHandler getCafeTableObjImplementationHandler() {
		if( cafeTableObjImplementationHandler == null ) {
			cafeTableObjImplementationHandler = new CFBamXmlLoaderCafeTableObjImplementationHandler( this );
		}
		return( cafeTableObjImplementationHandler );
	}

	protected CFBamXmlLoaderCafeDb2LUWTableMembersHandler getCafeDb2LUWTableMembersHandler() {
		if( cafeDb2LUWTableMembersHandler == null ) {
			cafeDb2LUWTableMembersHandler = new CFBamXmlLoaderCafeDb2LUWTableMembersHandler( this );
		}
		return( cafeDb2LUWTableMembersHandler );
	}

	protected CFBamXmlLoaderCafeDb2LUWTableImplementationHandler getCafeDb2LUWTableImplementationHandler() {
		if( cafeDb2LUWTableImplementationHandler == null ) {
			cafeDb2LUWTableImplementationHandler = new CFBamXmlLoaderCafeDb2LUWTableImplementationHandler( this );
		}
		return( cafeDb2LUWTableImplementationHandler );
	}

	protected CFBamXmlLoaderCafeMSSqlTableMembersHandler getCafeMSSqlTableMembersHandler() {
		if( cafeMSSqlTableMembersHandler == null ) {
			cafeMSSqlTableMembersHandler = new CFBamXmlLoaderCafeMSSqlTableMembersHandler( this );
		}
		return( cafeMSSqlTableMembersHandler );
	}

	protected CFBamXmlLoaderCafeMSSqlTableImplementationHandler getCafeMSSqlTableImplementationHandler() {
		if( cafeMSSqlTableImplementationHandler == null ) {
			cafeMSSqlTableImplementationHandler = new CFBamXmlLoaderCafeMSSqlTableImplementationHandler( this );
		}
		return( cafeMSSqlTableImplementationHandler );
	}

	protected CFBamXmlLoaderCafeMySqlTableMembersHandler getCafeMySqlTableMembersHandler() {
		if( cafeMySqlTableMembersHandler == null ) {
			cafeMySqlTableMembersHandler = new CFBamXmlLoaderCafeMySqlTableMembersHandler( this );
		}
		return( cafeMySqlTableMembersHandler );
	}

	protected CFBamXmlLoaderCafeMySqlTableImplementationHandler getCafeMySqlTableImplementationHandler() {
		if( cafeMySqlTableImplementationHandler == null ) {
			cafeMySqlTableImplementationHandler = new CFBamXmlLoaderCafeMySqlTableImplementationHandler( this );
		}
		return( cafeMySqlTableImplementationHandler );
	}

	protected CFBamXmlLoaderCafeOracleTableMembersHandler getCafeOracleTableMembersHandler() {
		if( cafeOracleTableMembersHandler == null ) {
			cafeOracleTableMembersHandler = new CFBamXmlLoaderCafeOracleTableMembersHandler( this );
		}
		return( cafeOracleTableMembersHandler );
	}

	protected CFBamXmlLoaderCafeOracleTableImplementationHandler getCafeOracleTableImplementationHandler() {
		if( cafeOracleTableImplementationHandler == null ) {
			cafeOracleTableImplementationHandler = new CFBamXmlLoaderCafeOracleTableImplementationHandler( this );
		}
		return( cafeOracleTableImplementationHandler );
	}

	protected CFBamXmlLoaderCafePgSqlTableMembersHandler getCafePgSqlTableMembersHandler() {
		if( cafePgSqlTableMembersHandler == null ) {
			cafePgSqlTableMembersHandler = new CFBamXmlLoaderCafePgSqlTableMembersHandler( this );
		}
		return( cafePgSqlTableMembersHandler );
	}

	protected CFBamXmlLoaderCafePgSqlTableImplementationHandler getCafePgSqlTableImplementationHandler() {
		if( cafePgSqlTableImplementationHandler == null ) {
			cafePgSqlTableImplementationHandler = new CFBamXmlLoaderCafePgSqlTableImplementationHandler( this );
		}
		return( cafePgSqlTableImplementationHandler );
	}

	protected CFBamXmlLoaderCafeRamTableMembersHandler getCafeRamTableMembersHandler() {
		if( cafeRamTableMembersHandler == null ) {
			cafeRamTableMembersHandler = new CFBamXmlLoaderCafeRamTableMembersHandler( this );
		}
		return( cafeRamTableMembersHandler );
	}

	protected CFBamXmlLoaderCafeRamTableImplementationHandler getCafeRamTableImplementationHandler() {
		if( cafeRamTableImplementationHandler == null ) {
			cafeRamTableImplementationHandler = new CFBamXmlLoaderCafeRamTableImplementationHandler( this );
		}
		return( cafeRamTableImplementationHandler );
	}

	protected CFBamXmlLoaderCafeSaxLoaderStartElementHandler getCafeSaxLoaderStartElementHandler() {
		if( cafeSaxLoaderStartElementHandler == null ) {
			cafeSaxLoaderStartElementHandler = new CFBamXmlLoaderCafeSaxLoaderStartElementHandler( this );
		}
		return( cafeSaxLoaderStartElementHandler );
	}

	protected CFBamXmlLoaderCafeSaxLoaderEndElementHandler getCafeSaxLoaderEndElementHandler() {
		if( cafeSaxLoaderEndElementHandler == null ) {
			cafeSaxLoaderEndElementHandler = new CFBamXmlLoaderCafeSaxLoaderEndElementHandler( this );
		}
		return( cafeSaxLoaderEndElementHandler );
	}

	protected CFBamXmlLoaderCafeXMsgTableImportHandler getCafeXMsgTableImportHandler() {
		if( cafeXMsgTableImportHandler == null ) {
			cafeXMsgTableImportHandler = new CFBamXmlLoaderCafeXMsgTableImportHandler( this );
		}
		return( cafeXMsgTableImportHandler );
	}

	protected CFBamXmlLoaderCafeXMsgTableFormattersHandler getCafeXMsgTableFormattersHandler() {
		if( cafeXMsgTableFormattersHandler == null ) {
			cafeXMsgTableFormattersHandler = new CFBamXmlLoaderCafeXMsgTableFormattersHandler( this );
		}
		return( cafeXMsgTableFormattersHandler );
	}

	protected CFBamXmlLoaderCafeXMsgRqstTableImportHandler getCafeXMsgRqstTableImportHandler() {
		if( cafeXMsgRqstTableImportHandler == null ) {
			cafeXMsgRqstTableImportHandler = new CFBamXmlLoaderCafeXMsgRqstTableImportHandler( this );
		}
		return( cafeXMsgRqstTableImportHandler );
	}

	protected CFBamXmlLoaderCafeXMsgRspnTableImportHandler getCafeXMsgRspnTableImportHandler() {
		if( cafeXMsgRspnTableImportHandler == null ) {
			cafeXMsgRspnTableImportHandler = new CFBamXmlLoaderCafeXMsgRspnTableImportHandler( this );
		}
		return( cafeXMsgRspnTableImportHandler );
	}

	protected CFBamXmlLoaderCafeXMsgClientTableImportHandler getCafeXMsgClientTableImportHandler() {
		if( cafeXMsgClientTableImportHandler == null ) {
			cafeXMsgClientTableImportHandler = new CFBamXmlLoaderCafeXMsgClientTableImportHandler( this );
		}
		return( cafeXMsgClientTableImportHandler );
	}

	protected CFBamXmlLoaderCafeXMsgRqstTableBodyHandler getCafeXMsgRqstTableBodyHandler() {
		if( cafeXMsgRqstTableBodyHandler == null ) {
			cafeXMsgRqstTableBodyHandler = new CFBamXmlLoaderCafeXMsgRqstTableBodyHandler( this );
		}
		return( cafeXMsgRqstTableBodyHandler );
	}

	protected CFBamXmlLoaderCafeXMsgRspnTableBodyHandler getCafeXMsgRspnTableBodyHandler() {
		if( cafeXMsgRspnTableBodyHandler == null ) {
			cafeXMsgRspnTableBodyHandler = new CFBamXmlLoaderCafeXMsgRspnTableBodyHandler( this );
		}
		return( cafeXMsgRspnTableBodyHandler );
	}

	protected CFBamXmlLoaderCafeXMsgClientTableBodyHandler getCafeXMsgClientTableBodyHandler() {
		if( cafeXMsgClientTableBodyHandler == null ) {
			cafeXMsgClientTableBodyHandler = new CFBamXmlLoaderCafeXMsgClientTableBodyHandler( this );
		}
		return( cafeXMsgClientTableBodyHandler );
	}

	protected CFBamXmlLoaderCPlusMethodBodyHandler getCPlusMethodBodyHandler() {
		if( cplusMethodBodyHandler == null ) {
			cplusMethodBodyHandler = new CFBamXmlLoaderCPlusMethodBodyHandler( this );
		}
		return( cplusMethodBodyHandler );
	}

	protected CFBamXmlLoaderCPlusSchemaObjInterfaceHandler getCPlusSchemaObjInterfaceHandler() {
		if( cplusSchemaObjInterfaceHandler == null ) {
			cplusSchemaObjInterfaceHandler = new CFBamXmlLoaderCPlusSchemaObjInterfaceHandler( this );
		}
		return( cplusSchemaObjInterfaceHandler );
	}

	protected CFBamXmlLoaderCPlusSchemaObjMembersHandler getCPlusSchemaObjMembersHandler() {
		if( cplusSchemaObjMembersHandler == null ) {
			cplusSchemaObjMembersHandler = new CFBamXmlLoaderCPlusSchemaObjMembersHandler( this );
		}
		return( cplusSchemaObjMembersHandler );
	}

	protected CFBamXmlLoaderCPlusSchemaObjIncludeHandler getCPlusSchemaObjIncludeHandler() {
		if( cplusSchemaObjIncludeHandler == null ) {
			cplusSchemaObjIncludeHandler = new CFBamXmlLoaderCPlusSchemaObjIncludeHandler( this );
		}
		return( cplusSchemaObjIncludeHandler );
	}

	protected CFBamXmlLoaderCPlusSchemaObjImplementationHandler getCPlusSchemaObjImplementationHandler() {
		if( cplusSchemaObjImplementationHandler == null ) {
			cplusSchemaObjImplementationHandler = new CFBamXmlLoaderCPlusSchemaObjImplementationHandler( this );
		}
		return( cplusSchemaObjImplementationHandler );
	}

	protected CFBamXmlLoaderCPlusDb2LUWSchemaObjMembersHandler getCPlusDb2LUWSchemaObjMembersHandler() {
		if( cplusDb2LUWSchemaObjMembersHandler == null ) {
			cplusDb2LUWSchemaObjMembersHandler = new CFBamXmlLoaderCPlusDb2LUWSchemaObjMembersHandler( this );
		}
		return( cplusDb2LUWSchemaObjMembersHandler );
	}

	protected CFBamXmlLoaderCPlusDb2LUWSchemaObjImplHandler getCPlusDb2LUWSchemaObjImplHandler() {
		if( cplusDb2LUWSchemaObjImplHandler == null ) {
			cplusDb2LUWSchemaObjImplHandler = new CFBamXmlLoaderCPlusDb2LUWSchemaObjImplHandler( this );
		}
		return( cplusDb2LUWSchemaObjImplHandler );
	}

	protected CFBamXmlLoaderCPlusDb2LUWSchemaObjIncludeHandler getCPlusDb2LUWSchemaObjIncludeHandler() {
		if( cplusDb2LUWSchemaObjIncludeHandler == null ) {
			cplusDb2LUWSchemaObjIncludeHandler = new CFBamXmlLoaderCPlusDb2LUWSchemaObjIncludeHandler( this );
		}
		return( cplusDb2LUWSchemaObjIncludeHandler );
	}

	protected CFBamXmlLoaderCPlusMSSqlSchemaObjMembersHandler getCPlusMSSqlSchemaObjMembersHandler() {
		if( cplusMSSqlSchemaObjMembersHandler == null ) {
			cplusMSSqlSchemaObjMembersHandler = new CFBamXmlLoaderCPlusMSSqlSchemaObjMembersHandler( this );
		}
		return( cplusMSSqlSchemaObjMembersHandler );
	}

	protected CFBamXmlLoaderCPlusMSSqlSchemaObjImplHandler getCPlusMSSqlSchemaObjImplHandler() {
		if( cplusMSSqlSchemaObjImplHandler == null ) {
			cplusMSSqlSchemaObjImplHandler = new CFBamXmlLoaderCPlusMSSqlSchemaObjImplHandler( this );
		}
		return( cplusMSSqlSchemaObjImplHandler );
	}

	protected CFBamXmlLoaderCPlusMSSqlSchemaObjIncludeHandler getCPlusMSSqlSchemaObjIncludeHandler() {
		if( cplusMSSqlSchemaObjIncludeHandler == null ) {
			cplusMSSqlSchemaObjIncludeHandler = new CFBamXmlLoaderCPlusMSSqlSchemaObjIncludeHandler( this );
		}
		return( cplusMSSqlSchemaObjIncludeHandler );
	}

	protected CFBamXmlLoaderCPlusMySqlSchemaObjMembersHandler getCPlusMySqlSchemaObjMembersHandler() {
		if( cplusMySqlSchemaObjMembersHandler == null ) {
			cplusMySqlSchemaObjMembersHandler = new CFBamXmlLoaderCPlusMySqlSchemaObjMembersHandler( this );
		}
		return( cplusMySqlSchemaObjMembersHandler );
	}

	protected CFBamXmlLoaderCPlusMySqlSchemaObjImplHandler getCPlusMySqlSchemaObjImplHandler() {
		if( cplusMySqlSchemaObjImplHandler == null ) {
			cplusMySqlSchemaObjImplHandler = new CFBamXmlLoaderCPlusMySqlSchemaObjImplHandler( this );
		}
		return( cplusMySqlSchemaObjImplHandler );
	}

	protected CFBamXmlLoaderCPlusMySqlSchemaObjIncludeHandler getCPlusMySqlSchemaObjIncludeHandler() {
		if( cplusMySqlSchemaObjIncludeHandler == null ) {
			cplusMySqlSchemaObjIncludeHandler = new CFBamXmlLoaderCPlusMySqlSchemaObjIncludeHandler( this );
		}
		return( cplusMySqlSchemaObjIncludeHandler );
	}

	protected CFBamXmlLoaderCPlusOracleSchemaObjMembersHandler getCPlusOracleSchemaObjMembersHandler() {
		if( cplusOracleSchemaObjMembersHandler == null ) {
			cplusOracleSchemaObjMembersHandler = new CFBamXmlLoaderCPlusOracleSchemaObjMembersHandler( this );
		}
		return( cplusOracleSchemaObjMembersHandler );
	}

	protected CFBamXmlLoaderCPlusOracleSchemaObjImplHandler getCPlusOracleSchemaObjImplHandler() {
		if( cplusOracleSchemaObjImplHandler == null ) {
			cplusOracleSchemaObjImplHandler = new CFBamXmlLoaderCPlusOracleSchemaObjImplHandler( this );
		}
		return( cplusOracleSchemaObjImplHandler );
	}

	protected CFBamXmlLoaderCPlusOracleSchemaObjIncludeHandler getCPlusOracleSchemaObjIncludeHandler() {
		if( cplusOracleSchemaObjIncludeHandler == null ) {
			cplusOracleSchemaObjIncludeHandler = new CFBamXmlLoaderCPlusOracleSchemaObjIncludeHandler( this );
		}
		return( cplusOracleSchemaObjIncludeHandler );
	}

	protected CFBamXmlLoaderCPlusPgSqlSchemaObjMembersHandler getCPlusPgSqlSchemaObjMembersHandler() {
		if( cplusPgSqlSchemaObjMembersHandler == null ) {
			cplusPgSqlSchemaObjMembersHandler = new CFBamXmlLoaderCPlusPgSqlSchemaObjMembersHandler( this );
		}
		return( cplusPgSqlSchemaObjMembersHandler );
	}

	protected CFBamXmlLoaderCPlusPgSqlSchemaObjImplHandler getCPlusPgSqlSchemaObjImplHandler() {
		if( cplusPgSqlSchemaObjImplHandler == null ) {
			cplusPgSqlSchemaObjImplHandler = new CFBamXmlLoaderCPlusPgSqlSchemaObjImplHandler( this );
		}
		return( cplusPgSqlSchemaObjImplHandler );
	}

	protected CFBamXmlLoaderCPlusPgSqlSchemaObjIncludeHandler getCPlusPgSqlSchemaObjIncludeHandler() {
		if( cplusPgSqlSchemaObjIncludeHandler == null ) {
			cplusPgSqlSchemaObjIncludeHandler = new CFBamXmlLoaderCPlusPgSqlSchemaObjIncludeHandler( this );
		}
		return( cplusPgSqlSchemaObjIncludeHandler );
	}

	protected CFBamXmlLoaderCPlusRamSchemaObjMembersHandler getCPlusRamSchemaObjMembersHandler() {
		if( cplusRamSchemaObjMembersHandler == null ) {
			cplusRamSchemaObjMembersHandler = new CFBamXmlLoaderCPlusRamSchemaObjMembersHandler( this );
		}
		return( cplusRamSchemaObjMembersHandler );
	}

	protected CFBamXmlLoaderCPlusRamSchemaObjImplHandler getCPlusRamSchemaObjImplHandler() {
		if( cplusRamSchemaObjImplHandler == null ) {
			cplusRamSchemaObjImplHandler = new CFBamXmlLoaderCPlusRamSchemaObjImplHandler( this );
		}
		return( cplusRamSchemaObjImplHandler );
	}

	protected CFBamXmlLoaderCPlusRamSchemaObjIncludeHandler getCPlusRamSchemaObjIncludeHandler() {
		if( cplusRamSchemaObjIncludeHandler == null ) {
			cplusRamSchemaObjIncludeHandler = new CFBamXmlLoaderCPlusRamSchemaObjIncludeHandler( this );
		}
		return( cplusRamSchemaObjIncludeHandler );
	}

	protected CFBamXmlLoaderCPlusXMsgSchemaIncludeHandler getCPlusXMsgSchemaIncludeHandler() {
		if( cplusXMsgSchemaIncludeHandler == null ) {
			cplusXMsgSchemaIncludeHandler = new CFBamXmlLoaderCPlusXMsgSchemaIncludeHandler( this );
		}
		return( cplusXMsgSchemaIncludeHandler );
	}

	protected CFBamXmlLoaderCPlusXMsgSchemaFormattersHandler getCPlusXMsgSchemaFormattersHandler() {
		if( cplusXMsgSchemaFormattersHandler == null ) {
			cplusXMsgSchemaFormattersHandler = new CFBamXmlLoaderCPlusXMsgSchemaFormattersHandler( this );
		}
		return( cplusXMsgSchemaFormattersHandler );
	}

	protected CFBamXmlLoaderCPlusXMsgClientSchemaIncludeHandler getCPlusXMsgClientSchemaIncludeHandler() {
		if( cplusXMsgClientSchemaIncludeHandler == null ) {
			cplusXMsgClientSchemaIncludeHandler = new CFBamXmlLoaderCPlusXMsgClientSchemaIncludeHandler( this );
		}
		return( cplusXMsgClientSchemaIncludeHandler );
	}

	protected CFBamXmlLoaderCPlusXMsgClientSchemaBodyHandler getCPlusXMsgClientSchemaBodyHandler() {
		if( cplusXMsgClientSchemaBodyHandler == null ) {
			cplusXMsgClientSchemaBodyHandler = new CFBamXmlLoaderCPlusXMsgClientSchemaBodyHandler( this );
		}
		return( cplusXMsgClientSchemaBodyHandler );
	}

	protected CFBamXmlLoaderCPlusXMsgRqstSchemaBodyHandler getCPlusXMsgRqstSchemaBodyHandler() {
		if( cplusXMsgRqstSchemaBodyHandler == null ) {
			cplusXMsgRqstSchemaBodyHandler = new CFBamXmlLoaderCPlusXMsgRqstSchemaBodyHandler( this );
		}
		return( cplusXMsgRqstSchemaBodyHandler );
	}

	protected CFBamXmlLoaderCPlusXMsgRqstSchemaIncludeHandler getCPlusXMsgRqstSchemaIncludeHandler() {
		if( cplusXMsgRqstSchemaIncludeHandler == null ) {
			cplusXMsgRqstSchemaIncludeHandler = new CFBamXmlLoaderCPlusXMsgRqstSchemaIncludeHandler( this );
		}
		return( cplusXMsgRqstSchemaIncludeHandler );
	}

	protected CFBamXmlLoaderCPlusXMsgRqstSchemaWireParsersHandler getCPlusXMsgRqstSchemaWireParsersHandler() {
		if( cplusXMsgRqstSchemaWireParsersHandler == null ) {
			cplusXMsgRqstSchemaWireParsersHandler = new CFBamXmlLoaderCPlusXMsgRqstSchemaWireParsersHandler( this );
		}
		return( cplusXMsgRqstSchemaWireParsersHandler );
	}

	protected CFBamXmlLoaderCPlusXMsgRqstSchemaXsdElementListHandler getCPlusXMsgRqstSchemaXsdElementListHandler() {
		if( cplusXMsgRqstSchemaXsdElementListHandler == null ) {
			cplusXMsgRqstSchemaXsdElementListHandler = new CFBamXmlLoaderCPlusXMsgRqstSchemaXsdElementListHandler( this );
		}
		return( cplusXMsgRqstSchemaXsdElementListHandler );
	}

	protected CFBamXmlLoaderCPlusXMsgRqstSchemaXsdSpecHandler getCPlusXMsgRqstSchemaXsdSpecHandler() {
		if( cplusXMsgRqstSchemaXsdSpecHandler == null ) {
			cplusXMsgRqstSchemaXsdSpecHandler = new CFBamXmlLoaderCPlusXMsgRqstSchemaXsdSpecHandler( this );
		}
		return( cplusXMsgRqstSchemaXsdSpecHandler );
	}

	protected CFBamXmlLoaderCPlusXMsgRspnSchemaBodyHandler getCPlusXMsgRspnSchemaBodyHandler() {
		if( cplusXMsgRspnSchemaBodyHandler == null ) {
			cplusXMsgRspnSchemaBodyHandler = new CFBamXmlLoaderCPlusXMsgRspnSchemaBodyHandler( this );
		}
		return( cplusXMsgRspnSchemaBodyHandler );
	}

	protected CFBamXmlLoaderCPlusXMsgRspnSchemaIncludeHandler getCPlusXMsgRspnSchemaIncludeHandler() {
		if( cplusXMsgRspnSchemaIncludeHandler == null ) {
			cplusXMsgRspnSchemaIncludeHandler = new CFBamXmlLoaderCPlusXMsgRspnSchemaIncludeHandler( this );
		}
		return( cplusXMsgRspnSchemaIncludeHandler );
	}

	protected CFBamXmlLoaderCPlusXMsgRspnSchemaWireParsersHandler getCPlusXMsgRspnSchemaWireParsersHandler() {
		if( cplusXMsgRspnSchemaWireParsersHandler == null ) {
			cplusXMsgRspnSchemaWireParsersHandler = new CFBamXmlLoaderCPlusXMsgRspnSchemaWireParsersHandler( this );
		}
		return( cplusXMsgRspnSchemaWireParsersHandler );
	}

	protected CFBamXmlLoaderCPlusXMsgRspnSchemaXsdElementListHandler getCPlusXMsgRspnSchemaXsdElementListHandler() {
		if( cplusXMsgRspnSchemaXsdElementListHandler == null ) {
			cplusXMsgRspnSchemaXsdElementListHandler = new CFBamXmlLoaderCPlusXMsgRspnSchemaXsdElementListHandler( this );
		}
		return( cplusXMsgRspnSchemaXsdElementListHandler );
	}

	protected CFBamXmlLoaderCPlusXMsgRspnSchemaXsdSpecHandler getCPlusXMsgRspnSchemaXsdSpecHandler() {
		if( cplusXMsgRspnSchemaXsdSpecHandler == null ) {
			cplusXMsgRspnSchemaXsdSpecHandler = new CFBamXmlLoaderCPlusXMsgRspnSchemaXsdSpecHandler( this );
		}
		return( cplusXMsgRspnSchemaXsdSpecHandler );
	}

	protected CFBamXmlLoaderCPlusObjMembersHandler getCPlusObjMembersHandler() {
		if( cplusObjMembersHandler == null ) {
			cplusObjMembersHandler = new CFBamXmlLoaderCPlusObjMembersHandler( this );
		}
		return( cplusObjMembersHandler );
	}

	protected CFBamXmlLoaderCPlusObjInterfaceHandler getCPlusObjInterfaceHandler() {
		if( cplusObjInterfaceHandler == null ) {
			cplusObjInterfaceHandler = new CFBamXmlLoaderCPlusObjInterfaceHandler( this );
		}
		return( cplusObjInterfaceHandler );
	}

	protected CFBamXmlLoaderCPlusObjImplementationHandler getCPlusObjImplementationHandler() {
		if( cplusObjImplementationHandler == null ) {
			cplusObjImplementationHandler = new CFBamXmlLoaderCPlusObjImplementationHandler( this );
		}
		return( cplusObjImplementationHandler );
	}

	protected CFBamXmlLoaderCPlusEditObjMembersHandler getCPlusEditObjMembersHandler() {
		if( cplusEditObjMembersHandler == null ) {
			cplusEditObjMembersHandler = new CFBamXmlLoaderCPlusEditObjMembersHandler( this );
		}
		return( cplusEditObjMembersHandler );
	}

	protected CFBamXmlLoaderCPlusEditObjInterfaceHandler getCPlusEditObjInterfaceHandler() {
		if( cplusEditObjInterfaceHandler == null ) {
			cplusEditObjInterfaceHandler = new CFBamXmlLoaderCPlusEditObjInterfaceHandler( this );
		}
		return( cplusEditObjInterfaceHandler );
	}

	protected CFBamXmlLoaderCPlusEditObjImplementationHandler getCPlusEditObjImplementationHandler() {
		if( cplusEditObjImplementationHandler == null ) {
			cplusEditObjImplementationHandler = new CFBamXmlLoaderCPlusEditObjImplementationHandler( this );
		}
		return( cplusEditObjImplementationHandler );
	}

	protected CFBamXmlLoaderCPlusDb2LUWTableIncludeHandler getCPlusDb2LUWTableIncludeHandler() {
		if( cplusDb2LUWTableIncludeHandler == null ) {
			cplusDb2LUWTableIncludeHandler = new CFBamXmlLoaderCPlusDb2LUWTableIncludeHandler( this );
		}
		return( cplusDb2LUWTableIncludeHandler );
	}

	protected CFBamXmlLoaderCPlusMSSqlTableIncludeHandler getCPlusMSSqlTableIncludeHandler() {
		if( cplusMSSqlTableIncludeHandler == null ) {
			cplusMSSqlTableIncludeHandler = new CFBamXmlLoaderCPlusMSSqlTableIncludeHandler( this );
		}
		return( cplusMSSqlTableIncludeHandler );
	}

	protected CFBamXmlLoaderCPlusMySqlTableIncludeHandler getCPlusMySqlTableIncludeHandler() {
		if( cplusMySqlTableIncludeHandler == null ) {
			cplusMySqlTableIncludeHandler = new CFBamXmlLoaderCPlusMySqlTableIncludeHandler( this );
		}
		return( cplusMySqlTableIncludeHandler );
	}

	protected CFBamXmlLoaderCPlusOracleTableIncludeHandler getCPlusOracleTableIncludeHandler() {
		if( cplusOracleTableIncludeHandler == null ) {
			cplusOracleTableIncludeHandler = new CFBamXmlLoaderCPlusOracleTableIncludeHandler( this );
		}
		return( cplusOracleTableIncludeHandler );
	}

	protected CFBamXmlLoaderCPlusPgSqlTableIncludeHandler getCPlusPgSqlTableIncludeHandler() {
		if( cplusPgSqlTableIncludeHandler == null ) {
			cplusPgSqlTableIncludeHandler = new CFBamXmlLoaderCPlusPgSqlTableIncludeHandler( this );
		}
		return( cplusPgSqlTableIncludeHandler );
	}

	protected CFBamXmlLoaderCPlusRamTableIncludeHandler getCPlusRamTableIncludeHandler() {
		if( cplusRamTableIncludeHandler == null ) {
			cplusRamTableIncludeHandler = new CFBamXmlLoaderCPlusRamTableIncludeHandler( this );
		}
		return( cplusRamTableIncludeHandler );
	}

	protected CFBamXmlLoaderCPlusObjIncludeHandler getCPlusObjIncludeHandler() {
		if( cplusObjIncludeHandler == null ) {
			cplusObjIncludeHandler = new CFBamXmlLoaderCPlusObjIncludeHandler( this );
		}
		return( cplusObjIncludeHandler );
	}

	protected CFBamXmlLoaderCPlusEditObjIncludeHandler getCPlusEditObjIncludeHandler() {
		if( cplusEditObjIncludeHandler == null ) {
			cplusEditObjIncludeHandler = new CFBamXmlLoaderCPlusEditObjIncludeHandler( this );
		}
		return( cplusEditObjIncludeHandler );
	}

	protected CFBamXmlLoaderCPlusSaxLoaderIncludeHandler getCPlusSaxLoaderIncludeHandler() {
		if( cplusSaxLoaderIncludeHandler == null ) {
			cplusSaxLoaderIncludeHandler = new CFBamXmlLoaderCPlusSaxLoaderIncludeHandler( this );
		}
		return( cplusSaxLoaderIncludeHandler );
	}

	protected CFBamXmlLoaderCPlusTableIncludeHandler getCPlusTableIncludeHandler() {
		if( cplusTableIncludeHandler == null ) {
			cplusTableIncludeHandler = new CFBamXmlLoaderCPlusTableIncludeHandler( this );
		}
		return( cplusTableIncludeHandler );
	}

	protected CFBamXmlLoaderCPlusTableObjIncludeHandler getCPlusTableObjIncludeHandler() {
		if( cplusTableObjIncludeHandler == null ) {
			cplusTableObjIncludeHandler = new CFBamXmlLoaderCPlusTableObjIncludeHandler( this );
		}
		return( cplusTableObjIncludeHandler );
	}

	protected CFBamXmlLoaderCPlusTableMembersHandler getCPlusTableMembersHandler() {
		if( cplusTableMembersHandler == null ) {
			cplusTableMembersHandler = new CFBamXmlLoaderCPlusTableMembersHandler( this );
		}
		return( cplusTableMembersHandler );
	}

	protected CFBamXmlLoaderCPlusTableInterfaceHandler getCPlusTableInterfaceHandler() {
		if( cplusTableInterfaceHandler == null ) {
			cplusTableInterfaceHandler = new CFBamXmlLoaderCPlusTableInterfaceHandler( this );
		}
		return( cplusTableInterfaceHandler );
	}

	protected CFBamXmlLoaderCPlusTableImplementationHandler getCPlusTableImplementationHandler() {
		if( cplusTableImplementationHandler == null ) {
			cplusTableImplementationHandler = new CFBamXmlLoaderCPlusTableImplementationHandler( this );
		}
		return( cplusTableImplementationHandler );
	}

	protected CFBamXmlLoaderCPlusTableObjMembersHandler getCPlusTableObjMembersHandler() {
		if( cplusTableObjMembersHandler == null ) {
			cplusTableObjMembersHandler = new CFBamXmlLoaderCPlusTableObjMembersHandler( this );
		}
		return( cplusTableObjMembersHandler );
	}

	protected CFBamXmlLoaderCPlusTableObjInterfaceHandler getCPlusTableObjInterfaceHandler() {
		if( cplusTableObjInterfaceHandler == null ) {
			cplusTableObjInterfaceHandler = new CFBamXmlLoaderCPlusTableObjInterfaceHandler( this );
		}
		return( cplusTableObjInterfaceHandler );
	}

	protected CFBamXmlLoaderCPlusTableObjImplementationHandler getCPlusTableObjImplementationHandler() {
		if( cplusTableObjImplementationHandler == null ) {
			cplusTableObjImplementationHandler = new CFBamXmlLoaderCPlusTableObjImplementationHandler( this );
		}
		return( cplusTableObjImplementationHandler );
	}

	protected CFBamXmlLoaderCPlusDb2LUWTableMembersHandler getCPlusDb2LUWTableMembersHandler() {
		if( cplusDb2LUWTableMembersHandler == null ) {
			cplusDb2LUWTableMembersHandler = new CFBamXmlLoaderCPlusDb2LUWTableMembersHandler( this );
		}
		return( cplusDb2LUWTableMembersHandler );
	}

	protected CFBamXmlLoaderCPlusDb2LUWTableImplementationHandler getCPlusDb2LUWTableImplementationHandler() {
		if( cplusDb2LUWTableImplementationHandler == null ) {
			cplusDb2LUWTableImplementationHandler = new CFBamXmlLoaderCPlusDb2LUWTableImplementationHandler( this );
		}
		return( cplusDb2LUWTableImplementationHandler );
	}

	protected CFBamXmlLoaderCPlusMSSqlTableMembersHandler getCPlusMSSqlTableMembersHandler() {
		if( cplusMSSqlTableMembersHandler == null ) {
			cplusMSSqlTableMembersHandler = new CFBamXmlLoaderCPlusMSSqlTableMembersHandler( this );
		}
		return( cplusMSSqlTableMembersHandler );
	}

	protected CFBamXmlLoaderCPlusMSSqlTableImplementationHandler getCPlusMSSqlTableImplementationHandler() {
		if( cplusMSSqlTableImplementationHandler == null ) {
			cplusMSSqlTableImplementationHandler = new CFBamXmlLoaderCPlusMSSqlTableImplementationHandler( this );
		}
		return( cplusMSSqlTableImplementationHandler );
	}

	protected CFBamXmlLoaderCPlusMySqlTableMembersHandler getCPlusMySqlTableMembersHandler() {
		if( cplusMySqlTableMembersHandler == null ) {
			cplusMySqlTableMembersHandler = new CFBamXmlLoaderCPlusMySqlTableMembersHandler( this );
		}
		return( cplusMySqlTableMembersHandler );
	}

	protected CFBamXmlLoaderCPlusMySqlTableImplementationHandler getCPlusMySqlTableImplementationHandler() {
		if( cplusMySqlTableImplementationHandler == null ) {
			cplusMySqlTableImplementationHandler = new CFBamXmlLoaderCPlusMySqlTableImplementationHandler( this );
		}
		return( cplusMySqlTableImplementationHandler );
	}

	protected CFBamXmlLoaderCPlusOracleTableMembersHandler getCPlusOracleTableMembersHandler() {
		if( cplusOracleTableMembersHandler == null ) {
			cplusOracleTableMembersHandler = new CFBamXmlLoaderCPlusOracleTableMembersHandler( this );
		}
		return( cplusOracleTableMembersHandler );
	}

	protected CFBamXmlLoaderCPlusOracleTableImplementationHandler getCPlusOracleTableImplementationHandler() {
		if( cplusOracleTableImplementationHandler == null ) {
			cplusOracleTableImplementationHandler = new CFBamXmlLoaderCPlusOracleTableImplementationHandler( this );
		}
		return( cplusOracleTableImplementationHandler );
	}

	protected CFBamXmlLoaderCPlusPgSqlTableMembersHandler getCPlusPgSqlTableMembersHandler() {
		if( cplusPgSqlTableMembersHandler == null ) {
			cplusPgSqlTableMembersHandler = new CFBamXmlLoaderCPlusPgSqlTableMembersHandler( this );
		}
		return( cplusPgSqlTableMembersHandler );
	}

	protected CFBamXmlLoaderCPlusPgSqlTableImplementationHandler getCPlusPgSqlTableImplementationHandler() {
		if( cplusPgSqlTableImplementationHandler == null ) {
			cplusPgSqlTableImplementationHandler = new CFBamXmlLoaderCPlusPgSqlTableImplementationHandler( this );
		}
		return( cplusPgSqlTableImplementationHandler );
	}

	protected CFBamXmlLoaderCPlusRamTableMembersHandler getCPlusRamTableMembersHandler() {
		if( cplusRamTableMembersHandler == null ) {
			cplusRamTableMembersHandler = new CFBamXmlLoaderCPlusRamTableMembersHandler( this );
		}
		return( cplusRamTableMembersHandler );
	}

	protected CFBamXmlLoaderCPlusRamTableImplementationHandler getCPlusRamTableImplementationHandler() {
		if( cplusRamTableImplementationHandler == null ) {
			cplusRamTableImplementationHandler = new CFBamXmlLoaderCPlusRamTableImplementationHandler( this );
		}
		return( cplusRamTableImplementationHandler );
	}

	protected CFBamXmlLoaderCPlusSaxLoaderStartElementHandler getCPlusSaxLoaderStartElementHandler() {
		if( cplusSaxLoaderStartElementHandler == null ) {
			cplusSaxLoaderStartElementHandler = new CFBamXmlLoaderCPlusSaxLoaderStartElementHandler( this );
		}
		return( cplusSaxLoaderStartElementHandler );
	}

	protected CFBamXmlLoaderCPlusSaxLoaderEndElementHandler getCPlusSaxLoaderEndElementHandler() {
		if( cplusSaxLoaderEndElementHandler == null ) {
			cplusSaxLoaderEndElementHandler = new CFBamXmlLoaderCPlusSaxLoaderEndElementHandler( this );
		}
		return( cplusSaxLoaderEndElementHandler );
	}

	protected CFBamXmlLoaderCPlusXMsgTableIncludeHandler getCPlusXMsgTableIncludeHandler() {
		if( cplusXMsgTableIncludeHandler == null ) {
			cplusXMsgTableIncludeHandler = new CFBamXmlLoaderCPlusXMsgTableIncludeHandler( this );
		}
		return( cplusXMsgTableIncludeHandler );
	}

	protected CFBamXmlLoaderCPlusXMsgTableFormattersHandler getCPlusXMsgTableFormattersHandler() {
		if( cplusXMsgTableFormattersHandler == null ) {
			cplusXMsgTableFormattersHandler = new CFBamXmlLoaderCPlusXMsgTableFormattersHandler( this );
		}
		return( cplusXMsgTableFormattersHandler );
	}

	protected CFBamXmlLoaderCPlusXMsgRqstTableIncludeHandler getCPlusXMsgRqstTableIncludeHandler() {
		if( cplusXMsgRqstTableIncludeHandler == null ) {
			cplusXMsgRqstTableIncludeHandler = new CFBamXmlLoaderCPlusXMsgRqstTableIncludeHandler( this );
		}
		return( cplusXMsgRqstTableIncludeHandler );
	}

	protected CFBamXmlLoaderCPlusXMsgRspnTableIncludeHandler getCPlusXMsgRspnTableIncludeHandler() {
		if( cplusXMsgRspnTableIncludeHandler == null ) {
			cplusXMsgRspnTableIncludeHandler = new CFBamXmlLoaderCPlusXMsgRspnTableIncludeHandler( this );
		}
		return( cplusXMsgRspnTableIncludeHandler );
	}

	protected CFBamXmlLoaderCPlusXMsgClientTableIncludeHandler getCPlusXMsgClientTableIncludeHandler() {
		if( cplusXMsgClientTableIncludeHandler == null ) {
			cplusXMsgClientTableIncludeHandler = new CFBamXmlLoaderCPlusXMsgClientTableIncludeHandler( this );
		}
		return( cplusXMsgClientTableIncludeHandler );
	}

	protected CFBamXmlLoaderCPlusXMsgRqstTableBodyHandler getCPlusXMsgRqstTableBodyHandler() {
		if( cplusXMsgRqstTableBodyHandler == null ) {
			cplusXMsgRqstTableBodyHandler = new CFBamXmlLoaderCPlusXMsgRqstTableBodyHandler( this );
		}
		return( cplusXMsgRqstTableBodyHandler );
	}

	protected CFBamXmlLoaderCPlusXMsgRspnTableBodyHandler getCPlusXMsgRspnTableBodyHandler() {
		if( cplusXMsgRspnTableBodyHandler == null ) {
			cplusXMsgRspnTableBodyHandler = new CFBamXmlLoaderCPlusXMsgRspnTableBodyHandler( this );
		}
		return( cplusXMsgRspnTableBodyHandler );
	}

	protected CFBamXmlLoaderCPlusXMsgClientTableBodyHandler getCPlusXMsgClientTableBodyHandler() {
		if( cplusXMsgClientTableBodyHandler == null ) {
			cplusXMsgClientTableBodyHandler = new CFBamXmlLoaderCPlusXMsgClientTableBodyHandler( this );
		}
		return( cplusXMsgClientTableBodyHandler );
	}

	protected CFBamXmlLoaderHPlusSchemaObjInterfaceHandler getHPlusSchemaObjInterfaceHandler() {
		if( hplusSchemaObjInterfaceHandler == null ) {
			hplusSchemaObjInterfaceHandler = new CFBamXmlLoaderHPlusSchemaObjInterfaceHandler( this );
		}
		return( hplusSchemaObjInterfaceHandler );
	}

	protected CFBamXmlLoaderHPlusSchemaObjMembersHandler getHPlusSchemaObjMembersHandler() {
		if( hplusSchemaObjMembersHandler == null ) {
			hplusSchemaObjMembersHandler = new CFBamXmlLoaderHPlusSchemaObjMembersHandler( this );
		}
		return( hplusSchemaObjMembersHandler );
	}

	protected CFBamXmlLoaderHPlusSchemaObjIncludeHandler getHPlusSchemaObjIncludeHandler() {
		if( hplusSchemaObjIncludeHandler == null ) {
			hplusSchemaObjIncludeHandler = new CFBamXmlLoaderHPlusSchemaObjIncludeHandler( this );
		}
		return( hplusSchemaObjIncludeHandler );
	}

	protected CFBamXmlLoaderHPlusSchemaObjImplementationHandler getHPlusSchemaObjImplementationHandler() {
		if( hplusSchemaObjImplementationHandler == null ) {
			hplusSchemaObjImplementationHandler = new CFBamXmlLoaderHPlusSchemaObjImplementationHandler( this );
		}
		return( hplusSchemaObjImplementationHandler );
	}

	protected CFBamXmlLoaderHPlusDb2LUWSchemaObjMembersHandler getHPlusDb2LUWSchemaObjMembersHandler() {
		if( hplusDb2LUWSchemaObjMembersHandler == null ) {
			hplusDb2LUWSchemaObjMembersHandler = new CFBamXmlLoaderHPlusDb2LUWSchemaObjMembersHandler( this );
		}
		return( hplusDb2LUWSchemaObjMembersHandler );
	}

	protected CFBamXmlLoaderHPlusDb2LUWSchemaObjImplHandler getHPlusDb2LUWSchemaObjImplHandler() {
		if( hplusDb2LUWSchemaObjImplHandler == null ) {
			hplusDb2LUWSchemaObjImplHandler = new CFBamXmlLoaderHPlusDb2LUWSchemaObjImplHandler( this );
		}
		return( hplusDb2LUWSchemaObjImplHandler );
	}

	protected CFBamXmlLoaderHPlusDb2LUWSchemaObjIncludeHandler getHPlusDb2LUWSchemaObjIncludeHandler() {
		if( hplusDb2LUWSchemaObjIncludeHandler == null ) {
			hplusDb2LUWSchemaObjIncludeHandler = new CFBamXmlLoaderHPlusDb2LUWSchemaObjIncludeHandler( this );
		}
		return( hplusDb2LUWSchemaObjIncludeHandler );
	}

	protected CFBamXmlLoaderHPlusMSSqlSchemaObjMembersHandler getHPlusMSSqlSchemaObjMembersHandler() {
		if( hplusMSSqlSchemaObjMembersHandler == null ) {
			hplusMSSqlSchemaObjMembersHandler = new CFBamXmlLoaderHPlusMSSqlSchemaObjMembersHandler( this );
		}
		return( hplusMSSqlSchemaObjMembersHandler );
	}

	protected CFBamXmlLoaderHPlusMSSqlSchemaObjImplHandler getHPlusMSSqlSchemaObjImplHandler() {
		if( hplusMSSqlSchemaObjImplHandler == null ) {
			hplusMSSqlSchemaObjImplHandler = new CFBamXmlLoaderHPlusMSSqlSchemaObjImplHandler( this );
		}
		return( hplusMSSqlSchemaObjImplHandler );
	}

	protected CFBamXmlLoaderHPlusMSSqlSchemaObjIncludeHandler getHPlusMSSqlSchemaObjIncludeHandler() {
		if( hplusMSSqlSchemaObjIncludeHandler == null ) {
			hplusMSSqlSchemaObjIncludeHandler = new CFBamXmlLoaderHPlusMSSqlSchemaObjIncludeHandler( this );
		}
		return( hplusMSSqlSchemaObjIncludeHandler );
	}

	protected CFBamXmlLoaderHPlusMySqlSchemaObjMembersHandler getHPlusMySqlSchemaObjMembersHandler() {
		if( hplusMySqlSchemaObjMembersHandler == null ) {
			hplusMySqlSchemaObjMembersHandler = new CFBamXmlLoaderHPlusMySqlSchemaObjMembersHandler( this );
		}
		return( hplusMySqlSchemaObjMembersHandler );
	}

	protected CFBamXmlLoaderHPlusMySqlSchemaObjImplHandler getHPlusMySqlSchemaObjImplHandler() {
		if( hplusMySqlSchemaObjImplHandler == null ) {
			hplusMySqlSchemaObjImplHandler = new CFBamXmlLoaderHPlusMySqlSchemaObjImplHandler( this );
		}
		return( hplusMySqlSchemaObjImplHandler );
	}

	protected CFBamXmlLoaderHPlusMySqlSchemaObjIncludeHandler getHPlusMySqlSchemaObjIncludeHandler() {
		if( hplusMySqlSchemaObjIncludeHandler == null ) {
			hplusMySqlSchemaObjIncludeHandler = new CFBamXmlLoaderHPlusMySqlSchemaObjIncludeHandler( this );
		}
		return( hplusMySqlSchemaObjIncludeHandler );
	}

	protected CFBamXmlLoaderHPlusOracleSchemaObjMembersHandler getHPlusOracleSchemaObjMembersHandler() {
		if( hplusOracleSchemaObjMembersHandler == null ) {
			hplusOracleSchemaObjMembersHandler = new CFBamXmlLoaderHPlusOracleSchemaObjMembersHandler( this );
		}
		return( hplusOracleSchemaObjMembersHandler );
	}

	protected CFBamXmlLoaderHPlusOracleSchemaObjImplHandler getHPlusOracleSchemaObjImplHandler() {
		if( hplusOracleSchemaObjImplHandler == null ) {
			hplusOracleSchemaObjImplHandler = new CFBamXmlLoaderHPlusOracleSchemaObjImplHandler( this );
		}
		return( hplusOracleSchemaObjImplHandler );
	}

	protected CFBamXmlLoaderHPlusOracleSchemaObjIncludeHandler getHPlusOracleSchemaObjIncludeHandler() {
		if( hplusOracleSchemaObjIncludeHandler == null ) {
			hplusOracleSchemaObjIncludeHandler = new CFBamXmlLoaderHPlusOracleSchemaObjIncludeHandler( this );
		}
		return( hplusOracleSchemaObjIncludeHandler );
	}

	protected CFBamXmlLoaderHPlusPgSqlSchemaObjMembersHandler getHPlusPgSqlSchemaObjMembersHandler() {
		if( hplusPgSqlSchemaObjMembersHandler == null ) {
			hplusPgSqlSchemaObjMembersHandler = new CFBamXmlLoaderHPlusPgSqlSchemaObjMembersHandler( this );
		}
		return( hplusPgSqlSchemaObjMembersHandler );
	}

	protected CFBamXmlLoaderHPlusPgSqlSchemaObjImplHandler getHPlusPgSqlSchemaObjImplHandler() {
		if( hplusPgSqlSchemaObjImplHandler == null ) {
			hplusPgSqlSchemaObjImplHandler = new CFBamXmlLoaderHPlusPgSqlSchemaObjImplHandler( this );
		}
		return( hplusPgSqlSchemaObjImplHandler );
	}

	protected CFBamXmlLoaderHPlusPgSqlSchemaObjIncludeHandler getHPlusPgSqlSchemaObjIncludeHandler() {
		if( hplusPgSqlSchemaObjIncludeHandler == null ) {
			hplusPgSqlSchemaObjIncludeHandler = new CFBamXmlLoaderHPlusPgSqlSchemaObjIncludeHandler( this );
		}
		return( hplusPgSqlSchemaObjIncludeHandler );
	}

	protected CFBamXmlLoaderHPlusRamSchemaObjMembersHandler getHPlusRamSchemaObjMembersHandler() {
		if( hplusRamSchemaObjMembersHandler == null ) {
			hplusRamSchemaObjMembersHandler = new CFBamXmlLoaderHPlusRamSchemaObjMembersHandler( this );
		}
		return( hplusRamSchemaObjMembersHandler );
	}

	protected CFBamXmlLoaderHPlusRamSchemaObjImplHandler getHPlusRamSchemaObjImplHandler() {
		if( hplusRamSchemaObjImplHandler == null ) {
			hplusRamSchemaObjImplHandler = new CFBamXmlLoaderHPlusRamSchemaObjImplHandler( this );
		}
		return( hplusRamSchemaObjImplHandler );
	}

	protected CFBamXmlLoaderHPlusRamSchemaObjIncludeHandler getHPlusRamSchemaObjIncludeHandler() {
		if( hplusRamSchemaObjIncludeHandler == null ) {
			hplusRamSchemaObjIncludeHandler = new CFBamXmlLoaderHPlusRamSchemaObjIncludeHandler( this );
		}
		return( hplusRamSchemaObjIncludeHandler );
	}

	protected CFBamXmlLoaderHPlusXMsgSchemaIncludeHandler getHPlusXMsgSchemaIncludeHandler() {
		if( hplusXMsgSchemaIncludeHandler == null ) {
			hplusXMsgSchemaIncludeHandler = new CFBamXmlLoaderHPlusXMsgSchemaIncludeHandler( this );
		}
		return( hplusXMsgSchemaIncludeHandler );
	}

	protected CFBamXmlLoaderHPlusXMsgSchemaFormattersHandler getHPlusXMsgSchemaFormattersHandler() {
		if( hplusXMsgSchemaFormattersHandler == null ) {
			hplusXMsgSchemaFormattersHandler = new CFBamXmlLoaderHPlusXMsgSchemaFormattersHandler( this );
		}
		return( hplusXMsgSchemaFormattersHandler );
	}

	protected CFBamXmlLoaderHPlusXMsgClientSchemaIncludeHandler getHPlusXMsgClientSchemaIncludeHandler() {
		if( hplusXMsgClientSchemaIncludeHandler == null ) {
			hplusXMsgClientSchemaIncludeHandler = new CFBamXmlLoaderHPlusXMsgClientSchemaIncludeHandler( this );
		}
		return( hplusXMsgClientSchemaIncludeHandler );
	}

	protected CFBamXmlLoaderHPlusXMsgClientSchemaBodyHandler getHPlusXMsgClientSchemaBodyHandler() {
		if( hplusXMsgClientSchemaBodyHandler == null ) {
			hplusXMsgClientSchemaBodyHandler = new CFBamXmlLoaderHPlusXMsgClientSchemaBodyHandler( this );
		}
		return( hplusXMsgClientSchemaBodyHandler );
	}

	protected CFBamXmlLoaderHPlusXMsgRqstSchemaBodyHandler getHPlusXMsgRqstSchemaBodyHandler() {
		if( hplusXMsgRqstSchemaBodyHandler == null ) {
			hplusXMsgRqstSchemaBodyHandler = new CFBamXmlLoaderHPlusXMsgRqstSchemaBodyHandler( this );
		}
		return( hplusXMsgRqstSchemaBodyHandler );
	}

	protected CFBamXmlLoaderHPlusXMsgRqstSchemaIncludeHandler getHPlusXMsgRqstSchemaIncludeHandler() {
		if( hplusXMsgRqstSchemaIncludeHandler == null ) {
			hplusXMsgRqstSchemaIncludeHandler = new CFBamXmlLoaderHPlusXMsgRqstSchemaIncludeHandler( this );
		}
		return( hplusXMsgRqstSchemaIncludeHandler );
	}

	protected CFBamXmlLoaderHPlusXMsgRqstSchemaWireParsersHandler getHPlusXMsgRqstSchemaWireParsersHandler() {
		if( hplusXMsgRqstSchemaWireParsersHandler == null ) {
			hplusXMsgRqstSchemaWireParsersHandler = new CFBamXmlLoaderHPlusXMsgRqstSchemaWireParsersHandler( this );
		}
		return( hplusXMsgRqstSchemaWireParsersHandler );
	}

	protected CFBamXmlLoaderHPlusXMsgRqstSchemaXsdElementListHandler getHPlusXMsgRqstSchemaXsdElementListHandler() {
		if( hplusXMsgRqstSchemaXsdElementListHandler == null ) {
			hplusXMsgRqstSchemaXsdElementListHandler = new CFBamXmlLoaderHPlusXMsgRqstSchemaXsdElementListHandler( this );
		}
		return( hplusXMsgRqstSchemaXsdElementListHandler );
	}

	protected CFBamXmlLoaderHPlusXMsgRqstSchemaXsdSpecHandler getHPlusXMsgRqstSchemaXsdSpecHandler() {
		if( hplusXMsgRqstSchemaXsdSpecHandler == null ) {
			hplusXMsgRqstSchemaXsdSpecHandler = new CFBamXmlLoaderHPlusXMsgRqstSchemaXsdSpecHandler( this );
		}
		return( hplusXMsgRqstSchemaXsdSpecHandler );
	}

	protected CFBamXmlLoaderHPlusXMsgRspnSchemaBodyHandler getHPlusXMsgRspnSchemaBodyHandler() {
		if( hplusXMsgRspnSchemaBodyHandler == null ) {
			hplusXMsgRspnSchemaBodyHandler = new CFBamXmlLoaderHPlusXMsgRspnSchemaBodyHandler( this );
		}
		return( hplusXMsgRspnSchemaBodyHandler );
	}

	protected CFBamXmlLoaderHPlusXMsgRspnSchemaIncludeHandler getHPlusXMsgRspnSchemaIncludeHandler() {
		if( hplusXMsgRspnSchemaIncludeHandler == null ) {
			hplusXMsgRspnSchemaIncludeHandler = new CFBamXmlLoaderHPlusXMsgRspnSchemaIncludeHandler( this );
		}
		return( hplusXMsgRspnSchemaIncludeHandler );
	}

	protected CFBamXmlLoaderHPlusXMsgRspnSchemaWireParsersHandler getHPlusXMsgRspnSchemaWireParsersHandler() {
		if( hplusXMsgRspnSchemaWireParsersHandler == null ) {
			hplusXMsgRspnSchemaWireParsersHandler = new CFBamXmlLoaderHPlusXMsgRspnSchemaWireParsersHandler( this );
		}
		return( hplusXMsgRspnSchemaWireParsersHandler );
	}

	protected CFBamXmlLoaderHPlusXMsgRspnSchemaXsdElementListHandler getHPlusXMsgRspnSchemaXsdElementListHandler() {
		if( hplusXMsgRspnSchemaXsdElementListHandler == null ) {
			hplusXMsgRspnSchemaXsdElementListHandler = new CFBamXmlLoaderHPlusXMsgRspnSchemaXsdElementListHandler( this );
		}
		return( hplusXMsgRspnSchemaXsdElementListHandler );
	}

	protected CFBamXmlLoaderHPlusXMsgRspnSchemaXsdSpecHandler getHPlusXMsgRspnSchemaXsdSpecHandler() {
		if( hplusXMsgRspnSchemaXsdSpecHandler == null ) {
			hplusXMsgRspnSchemaXsdSpecHandler = new CFBamXmlLoaderHPlusXMsgRspnSchemaXsdSpecHandler( this );
		}
		return( hplusXMsgRspnSchemaXsdSpecHandler );
	}

	protected CFBamXmlLoaderHPlusObjMembersHandler getHPlusObjMembersHandler() {
		if( hplusObjMembersHandler == null ) {
			hplusObjMembersHandler = new CFBamXmlLoaderHPlusObjMembersHandler( this );
		}
		return( hplusObjMembersHandler );
	}

	protected CFBamXmlLoaderHPlusObjInterfaceHandler getHPlusObjInterfaceHandler() {
		if( hplusObjInterfaceHandler == null ) {
			hplusObjInterfaceHandler = new CFBamXmlLoaderHPlusObjInterfaceHandler( this );
		}
		return( hplusObjInterfaceHandler );
	}

	protected CFBamXmlLoaderHPlusObjImplementationHandler getHPlusObjImplementationHandler() {
		if( hplusObjImplementationHandler == null ) {
			hplusObjImplementationHandler = new CFBamXmlLoaderHPlusObjImplementationHandler( this );
		}
		return( hplusObjImplementationHandler );
	}

	protected CFBamXmlLoaderHPlusEditObjMembersHandler getHPlusEditObjMembersHandler() {
		if( hplusEditObjMembersHandler == null ) {
			hplusEditObjMembersHandler = new CFBamXmlLoaderHPlusEditObjMembersHandler( this );
		}
		return( hplusEditObjMembersHandler );
	}

	protected CFBamXmlLoaderHPlusEditObjInterfaceHandler getHPlusEditObjInterfaceHandler() {
		if( hplusEditObjInterfaceHandler == null ) {
			hplusEditObjInterfaceHandler = new CFBamXmlLoaderHPlusEditObjInterfaceHandler( this );
		}
		return( hplusEditObjInterfaceHandler );
	}

	protected CFBamXmlLoaderHPlusEditObjImplementationHandler getHPlusEditObjImplementationHandler() {
		if( hplusEditObjImplementationHandler == null ) {
			hplusEditObjImplementationHandler = new CFBamXmlLoaderHPlusEditObjImplementationHandler( this );
		}
		return( hplusEditObjImplementationHandler );
	}

	protected CFBamXmlLoaderHPlusDb2LUWTableIncludeHandler getHPlusDb2LUWTableIncludeHandler() {
		if( hplusDb2LUWTableIncludeHandler == null ) {
			hplusDb2LUWTableIncludeHandler = new CFBamXmlLoaderHPlusDb2LUWTableIncludeHandler( this );
		}
		return( hplusDb2LUWTableIncludeHandler );
	}

	protected CFBamXmlLoaderHPlusMSSqlTableIncludeHandler getHPlusMSSqlTableIncludeHandler() {
		if( hplusMSSqlTableIncludeHandler == null ) {
			hplusMSSqlTableIncludeHandler = new CFBamXmlLoaderHPlusMSSqlTableIncludeHandler( this );
		}
		return( hplusMSSqlTableIncludeHandler );
	}

	protected CFBamXmlLoaderHPlusMySqlTableIncludeHandler getHPlusMySqlTableIncludeHandler() {
		if( hplusMySqlTableIncludeHandler == null ) {
			hplusMySqlTableIncludeHandler = new CFBamXmlLoaderHPlusMySqlTableIncludeHandler( this );
		}
		return( hplusMySqlTableIncludeHandler );
	}

	protected CFBamXmlLoaderHPlusOracleTableIncludeHandler getHPlusOracleTableIncludeHandler() {
		if( hplusOracleTableIncludeHandler == null ) {
			hplusOracleTableIncludeHandler = new CFBamXmlLoaderHPlusOracleTableIncludeHandler( this );
		}
		return( hplusOracleTableIncludeHandler );
	}

	protected CFBamXmlLoaderHPlusPgSqlTableIncludeHandler getHPlusPgSqlTableIncludeHandler() {
		if( hplusPgSqlTableIncludeHandler == null ) {
			hplusPgSqlTableIncludeHandler = new CFBamXmlLoaderHPlusPgSqlTableIncludeHandler( this );
		}
		return( hplusPgSqlTableIncludeHandler );
	}

	protected CFBamXmlLoaderHPlusRamTableIncludeHandler getHPlusRamTableIncludeHandler() {
		if( hplusRamTableIncludeHandler == null ) {
			hplusRamTableIncludeHandler = new CFBamXmlLoaderHPlusRamTableIncludeHandler( this );
		}
		return( hplusRamTableIncludeHandler );
	}

	protected CFBamXmlLoaderHPlusObjIncludeHandler getHPlusObjIncludeHandler() {
		if( hplusObjIncludeHandler == null ) {
			hplusObjIncludeHandler = new CFBamXmlLoaderHPlusObjIncludeHandler( this );
		}
		return( hplusObjIncludeHandler );
	}

	protected CFBamXmlLoaderHPlusEditObjIncludeHandler getHPlusEditObjIncludeHandler() {
		if( hplusEditObjIncludeHandler == null ) {
			hplusEditObjIncludeHandler = new CFBamXmlLoaderHPlusEditObjIncludeHandler( this );
		}
		return( hplusEditObjIncludeHandler );
	}

	protected CFBamXmlLoaderHPlusSaxLoaderIncludeHandler getHPlusSaxLoaderIncludeHandler() {
		if( hplusSaxLoaderIncludeHandler == null ) {
			hplusSaxLoaderIncludeHandler = new CFBamXmlLoaderHPlusSaxLoaderIncludeHandler( this );
		}
		return( hplusSaxLoaderIncludeHandler );
	}

	protected CFBamXmlLoaderHPlusTableIncludeHandler getHPlusTableIncludeHandler() {
		if( hplusTableIncludeHandler == null ) {
			hplusTableIncludeHandler = new CFBamXmlLoaderHPlusTableIncludeHandler( this );
		}
		return( hplusTableIncludeHandler );
	}

	protected CFBamXmlLoaderHPlusTableObjIncludeHandler getHPlusTableObjIncludeHandler() {
		if( hplusTableObjIncludeHandler == null ) {
			hplusTableObjIncludeHandler = new CFBamXmlLoaderHPlusTableObjIncludeHandler( this );
		}
		return( hplusTableObjIncludeHandler );
	}

	protected CFBamXmlLoaderHPlusTableMembersHandler getHPlusTableMembersHandler() {
		if( hplusTableMembersHandler == null ) {
			hplusTableMembersHandler = new CFBamXmlLoaderHPlusTableMembersHandler( this );
		}
		return( hplusTableMembersHandler );
	}

	protected CFBamXmlLoaderHPlusTableInterfaceHandler getHPlusTableInterfaceHandler() {
		if( hplusTableInterfaceHandler == null ) {
			hplusTableInterfaceHandler = new CFBamXmlLoaderHPlusTableInterfaceHandler( this );
		}
		return( hplusTableInterfaceHandler );
	}

	protected CFBamXmlLoaderHPlusTableImplementationHandler getHPlusTableImplementationHandler() {
		if( hplusTableImplementationHandler == null ) {
			hplusTableImplementationHandler = new CFBamXmlLoaderHPlusTableImplementationHandler( this );
		}
		return( hplusTableImplementationHandler );
	}

	protected CFBamXmlLoaderHPlusTableObjMembersHandler getHPlusTableObjMembersHandler() {
		if( hplusTableObjMembersHandler == null ) {
			hplusTableObjMembersHandler = new CFBamXmlLoaderHPlusTableObjMembersHandler( this );
		}
		return( hplusTableObjMembersHandler );
	}

	protected CFBamXmlLoaderHPlusTableObjInterfaceHandler getHPlusTableObjInterfaceHandler() {
		if( hplusTableObjInterfaceHandler == null ) {
			hplusTableObjInterfaceHandler = new CFBamXmlLoaderHPlusTableObjInterfaceHandler( this );
		}
		return( hplusTableObjInterfaceHandler );
	}

	protected CFBamXmlLoaderHPlusTableObjImplementationHandler getHPlusTableObjImplementationHandler() {
		if( hplusTableObjImplementationHandler == null ) {
			hplusTableObjImplementationHandler = new CFBamXmlLoaderHPlusTableObjImplementationHandler( this );
		}
		return( hplusTableObjImplementationHandler );
	}

	protected CFBamXmlLoaderHPlusDb2LUWTableMembersHandler getHPlusDb2LUWTableMembersHandler() {
		if( hplusDb2LUWTableMembersHandler == null ) {
			hplusDb2LUWTableMembersHandler = new CFBamXmlLoaderHPlusDb2LUWTableMembersHandler( this );
		}
		return( hplusDb2LUWTableMembersHandler );
	}

	protected CFBamXmlLoaderHPlusDb2LUWTableImplementationHandler getHPlusDb2LUWTableImplementationHandler() {
		if( hplusDb2LUWTableImplementationHandler == null ) {
			hplusDb2LUWTableImplementationHandler = new CFBamXmlLoaderHPlusDb2LUWTableImplementationHandler( this );
		}
		return( hplusDb2LUWTableImplementationHandler );
	}

	protected CFBamXmlLoaderHPlusMSSqlTableMembersHandler getHPlusMSSqlTableMembersHandler() {
		if( hplusMSSqlTableMembersHandler == null ) {
			hplusMSSqlTableMembersHandler = new CFBamXmlLoaderHPlusMSSqlTableMembersHandler( this );
		}
		return( hplusMSSqlTableMembersHandler );
	}

	protected CFBamXmlLoaderHPlusMSSqlTableImplementationHandler getHPlusMSSqlTableImplementationHandler() {
		if( hplusMSSqlTableImplementationHandler == null ) {
			hplusMSSqlTableImplementationHandler = new CFBamXmlLoaderHPlusMSSqlTableImplementationHandler( this );
		}
		return( hplusMSSqlTableImplementationHandler );
	}

	protected CFBamXmlLoaderHPlusMySqlTableMembersHandler getHPlusMySqlTableMembersHandler() {
		if( hplusMySqlTableMembersHandler == null ) {
			hplusMySqlTableMembersHandler = new CFBamXmlLoaderHPlusMySqlTableMembersHandler( this );
		}
		return( hplusMySqlTableMembersHandler );
	}

	protected CFBamXmlLoaderHPlusMySqlTableImplementationHandler getHPlusMySqlTableImplementationHandler() {
		if( hplusMySqlTableImplementationHandler == null ) {
			hplusMySqlTableImplementationHandler = new CFBamXmlLoaderHPlusMySqlTableImplementationHandler( this );
		}
		return( hplusMySqlTableImplementationHandler );
	}

	protected CFBamXmlLoaderHPlusOracleTableMembersHandler getHPlusOracleTableMembersHandler() {
		if( hplusOracleTableMembersHandler == null ) {
			hplusOracleTableMembersHandler = new CFBamXmlLoaderHPlusOracleTableMembersHandler( this );
		}
		return( hplusOracleTableMembersHandler );
	}

	protected CFBamXmlLoaderHPlusOracleTableImplementationHandler getHPlusOracleTableImplementationHandler() {
		if( hplusOracleTableImplementationHandler == null ) {
			hplusOracleTableImplementationHandler = new CFBamXmlLoaderHPlusOracleTableImplementationHandler( this );
		}
		return( hplusOracleTableImplementationHandler );
	}

	protected CFBamXmlLoaderHPlusPgSqlTableMembersHandler getHPlusPgSqlTableMembersHandler() {
		if( hplusPgSqlTableMembersHandler == null ) {
			hplusPgSqlTableMembersHandler = new CFBamXmlLoaderHPlusPgSqlTableMembersHandler( this );
		}
		return( hplusPgSqlTableMembersHandler );
	}

	protected CFBamXmlLoaderHPlusPgSqlTableImplementationHandler getHPlusPgSqlTableImplementationHandler() {
		if( hplusPgSqlTableImplementationHandler == null ) {
			hplusPgSqlTableImplementationHandler = new CFBamXmlLoaderHPlusPgSqlTableImplementationHandler( this );
		}
		return( hplusPgSqlTableImplementationHandler );
	}

	protected CFBamXmlLoaderHPlusRamTableMembersHandler getHPlusRamTableMembersHandler() {
		if( hplusRamTableMembersHandler == null ) {
			hplusRamTableMembersHandler = new CFBamXmlLoaderHPlusRamTableMembersHandler( this );
		}
		return( hplusRamTableMembersHandler );
	}

	protected CFBamXmlLoaderHPlusRamTableImplementationHandler getHPlusRamTableImplementationHandler() {
		if( hplusRamTableImplementationHandler == null ) {
			hplusRamTableImplementationHandler = new CFBamXmlLoaderHPlusRamTableImplementationHandler( this );
		}
		return( hplusRamTableImplementationHandler );
	}

	protected CFBamXmlLoaderHPlusSaxLoaderStartElementHandler getHPlusSaxLoaderStartElementHandler() {
		if( hplusSaxLoaderStartElementHandler == null ) {
			hplusSaxLoaderStartElementHandler = new CFBamXmlLoaderHPlusSaxLoaderStartElementHandler( this );
		}
		return( hplusSaxLoaderStartElementHandler );
	}

	protected CFBamXmlLoaderHPlusSaxLoaderEndElementHandler getHPlusSaxLoaderEndElementHandler() {
		if( hplusSaxLoaderEndElementHandler == null ) {
			hplusSaxLoaderEndElementHandler = new CFBamXmlLoaderHPlusSaxLoaderEndElementHandler( this );
		}
		return( hplusSaxLoaderEndElementHandler );
	}

	protected CFBamXmlLoaderHPlusXMsgTableIncludeHandler getHPlusXMsgTableIncludeHandler() {
		if( hplusXMsgTableIncludeHandler == null ) {
			hplusXMsgTableIncludeHandler = new CFBamXmlLoaderHPlusXMsgTableIncludeHandler( this );
		}
		return( hplusXMsgTableIncludeHandler );
	}

	protected CFBamXmlLoaderHPlusXMsgTableFormattersHandler getHPlusXMsgTableFormattersHandler() {
		if( hplusXMsgTableFormattersHandler == null ) {
			hplusXMsgTableFormattersHandler = new CFBamXmlLoaderHPlusXMsgTableFormattersHandler( this );
		}
		return( hplusXMsgTableFormattersHandler );
	}

	protected CFBamXmlLoaderHPlusXMsgRqstTableIncludeHandler getHPlusXMsgRqstTableIncludeHandler() {
		if( hplusXMsgRqstTableIncludeHandler == null ) {
			hplusXMsgRqstTableIncludeHandler = new CFBamXmlLoaderHPlusXMsgRqstTableIncludeHandler( this );
		}
		return( hplusXMsgRqstTableIncludeHandler );
	}

	protected CFBamXmlLoaderHPlusXMsgRspnTableIncludeHandler getHPlusXMsgRspnTableIncludeHandler() {
		if( hplusXMsgRspnTableIncludeHandler == null ) {
			hplusXMsgRspnTableIncludeHandler = new CFBamXmlLoaderHPlusXMsgRspnTableIncludeHandler( this );
		}
		return( hplusXMsgRspnTableIncludeHandler );
	}

	protected CFBamXmlLoaderHPlusXMsgClientTableIncludeHandler getHPlusXMsgClientTableIncludeHandler() {
		if( hplusXMsgClientTableIncludeHandler == null ) {
			hplusXMsgClientTableIncludeHandler = new CFBamXmlLoaderHPlusXMsgClientTableIncludeHandler( this );
		}
		return( hplusXMsgClientTableIncludeHandler );
	}

	protected CFBamXmlLoaderHPlusXMsgRqstTableBodyHandler getHPlusXMsgRqstTableBodyHandler() {
		if( hplusXMsgRqstTableBodyHandler == null ) {
			hplusXMsgRqstTableBodyHandler = new CFBamXmlLoaderHPlusXMsgRqstTableBodyHandler( this );
		}
		return( hplusXMsgRqstTableBodyHandler );
	}

	protected CFBamXmlLoaderHPlusXMsgRspnTableBodyHandler getHPlusXMsgRspnTableBodyHandler() {
		if( hplusXMsgRspnTableBodyHandler == null ) {
			hplusXMsgRspnTableBodyHandler = new CFBamXmlLoaderHPlusXMsgRspnTableBodyHandler( this );
		}
		return( hplusXMsgRspnTableBodyHandler );
	}

	protected CFBamXmlLoaderHPlusXMsgClientTableBodyHandler getHPlusXMsgClientTableBodyHandler() {
		if( hplusXMsgClientTableBodyHandler == null ) {
			hplusXMsgClientTableBodyHandler = new CFBamXmlLoaderHPlusXMsgClientTableBodyHandler( this );
		}
		return( hplusXMsgClientTableBodyHandler );
	}

	protected CFBamXmlLoaderCSharpMethodBodyHandler getCSharpMethodBodyHandler() {
		if( csharpMethodBodyHandler == null ) {
			csharpMethodBodyHandler = new CFBamXmlLoaderCSharpMethodBodyHandler( this );
		}
		return( csharpMethodBodyHandler );
	}

	protected CFBamXmlLoaderCSharpSchemaObjInterfaceHandler getCSharpSchemaObjInterfaceHandler() {
		if( csharpSchemaObjInterfaceHandler == null ) {
			csharpSchemaObjInterfaceHandler = new CFBamXmlLoaderCSharpSchemaObjInterfaceHandler( this );
		}
		return( csharpSchemaObjInterfaceHandler );
	}

	protected CFBamXmlLoaderCSharpSchemaObjMembersHandler getCSharpSchemaObjMembersHandler() {
		if( csharpSchemaObjMembersHandler == null ) {
			csharpSchemaObjMembersHandler = new CFBamXmlLoaderCSharpSchemaObjMembersHandler( this );
		}
		return( csharpSchemaObjMembersHandler );
	}

	protected CFBamXmlLoaderCSharpSchemaObjUsingHandler getCSharpSchemaObjUsingHandler() {
		if( csharpSchemaObjUsingHandler == null ) {
			csharpSchemaObjUsingHandler = new CFBamXmlLoaderCSharpSchemaObjUsingHandler( this );
		}
		return( csharpSchemaObjUsingHandler );
	}

	protected CFBamXmlLoaderCSharpSchemaObjImplementationHandler getCSharpSchemaObjImplementationHandler() {
		if( csharpSchemaObjImplementationHandler == null ) {
			csharpSchemaObjImplementationHandler = new CFBamXmlLoaderCSharpSchemaObjImplementationHandler( this );
		}
		return( csharpSchemaObjImplementationHandler );
	}

	protected CFBamXmlLoaderCSharpDb2LUWSchemaObjMembersHandler getCSharpDb2LUWSchemaObjMembersHandler() {
		if( csharpDb2LUWSchemaObjMembersHandler == null ) {
			csharpDb2LUWSchemaObjMembersHandler = new CFBamXmlLoaderCSharpDb2LUWSchemaObjMembersHandler( this );
		}
		return( csharpDb2LUWSchemaObjMembersHandler );
	}

	protected CFBamXmlLoaderCSharpDb2LUWSchemaObjImplHandler getCSharpDb2LUWSchemaObjImplHandler() {
		if( csharpDb2LUWSchemaObjImplHandler == null ) {
			csharpDb2LUWSchemaObjImplHandler = new CFBamXmlLoaderCSharpDb2LUWSchemaObjImplHandler( this );
		}
		return( csharpDb2LUWSchemaObjImplHandler );
	}

	protected CFBamXmlLoaderCSharpDb2LUWSchemaObjUsingHandler getCSharpDb2LUWSchemaObjUsingHandler() {
		if( csharpDb2LUWSchemaObjUsingHandler == null ) {
			csharpDb2LUWSchemaObjUsingHandler = new CFBamXmlLoaderCSharpDb2LUWSchemaObjUsingHandler( this );
		}
		return( csharpDb2LUWSchemaObjUsingHandler );
	}

	protected CFBamXmlLoaderCSharpMSSqlSchemaObjMembersHandler getCSharpMSSqlSchemaObjMembersHandler() {
		if( csharpMSSqlSchemaObjMembersHandler == null ) {
			csharpMSSqlSchemaObjMembersHandler = new CFBamXmlLoaderCSharpMSSqlSchemaObjMembersHandler( this );
		}
		return( csharpMSSqlSchemaObjMembersHandler );
	}

	protected CFBamXmlLoaderCSharpMSSqlSchemaObjImplHandler getCSharpMSSqlSchemaObjImplHandler() {
		if( csharpMSSqlSchemaObjImplHandler == null ) {
			csharpMSSqlSchemaObjImplHandler = new CFBamXmlLoaderCSharpMSSqlSchemaObjImplHandler( this );
		}
		return( csharpMSSqlSchemaObjImplHandler );
	}

	protected CFBamXmlLoaderCSharpMSSqlSchemaObjUsingHandler getCSharpMSSqlSchemaObjUsingHandler() {
		if( csharpMSSqlSchemaObjUsingHandler == null ) {
			csharpMSSqlSchemaObjUsingHandler = new CFBamXmlLoaderCSharpMSSqlSchemaObjUsingHandler( this );
		}
		return( csharpMSSqlSchemaObjUsingHandler );
	}

	protected CFBamXmlLoaderCSharpMySqlSchemaObjMembersHandler getCSharpMySqlSchemaObjMembersHandler() {
		if( csharpMySqlSchemaObjMembersHandler == null ) {
			csharpMySqlSchemaObjMembersHandler = new CFBamXmlLoaderCSharpMySqlSchemaObjMembersHandler( this );
		}
		return( csharpMySqlSchemaObjMembersHandler );
	}

	protected CFBamXmlLoaderCSharpMySqlSchemaObjImplHandler getCSharpMySqlSchemaObjImplHandler() {
		if( csharpMySqlSchemaObjImplHandler == null ) {
			csharpMySqlSchemaObjImplHandler = new CFBamXmlLoaderCSharpMySqlSchemaObjImplHandler( this );
		}
		return( csharpMySqlSchemaObjImplHandler );
	}

	protected CFBamXmlLoaderCSharpMySqlSchemaObjUsingHandler getCSharpMySqlSchemaObjUsingHandler() {
		if( csharpMySqlSchemaObjUsingHandler == null ) {
			csharpMySqlSchemaObjUsingHandler = new CFBamXmlLoaderCSharpMySqlSchemaObjUsingHandler( this );
		}
		return( csharpMySqlSchemaObjUsingHandler );
	}

	protected CFBamXmlLoaderCSharpOracleSchemaObjMembersHandler getCSharpOracleSchemaObjMembersHandler() {
		if( csharpOracleSchemaObjMembersHandler == null ) {
			csharpOracleSchemaObjMembersHandler = new CFBamXmlLoaderCSharpOracleSchemaObjMembersHandler( this );
		}
		return( csharpOracleSchemaObjMembersHandler );
	}

	protected CFBamXmlLoaderCSharpOracleSchemaObjImplHandler getCSharpOracleSchemaObjImplHandler() {
		if( csharpOracleSchemaObjImplHandler == null ) {
			csharpOracleSchemaObjImplHandler = new CFBamXmlLoaderCSharpOracleSchemaObjImplHandler( this );
		}
		return( csharpOracleSchemaObjImplHandler );
	}

	protected CFBamXmlLoaderCSharpOracleSchemaObjUsingHandler getCSharpOracleSchemaObjUsingHandler() {
		if( csharpOracleSchemaObjUsingHandler == null ) {
			csharpOracleSchemaObjUsingHandler = new CFBamXmlLoaderCSharpOracleSchemaObjUsingHandler( this );
		}
		return( csharpOracleSchemaObjUsingHandler );
	}

	protected CFBamXmlLoaderCSharpPgSqlSchemaObjMembersHandler getCSharpPgSqlSchemaObjMembersHandler() {
		if( csharpPgSqlSchemaObjMembersHandler == null ) {
			csharpPgSqlSchemaObjMembersHandler = new CFBamXmlLoaderCSharpPgSqlSchemaObjMembersHandler( this );
		}
		return( csharpPgSqlSchemaObjMembersHandler );
	}

	protected CFBamXmlLoaderCSharpPgSqlSchemaObjImplHandler getCSharpPgSqlSchemaObjImplHandler() {
		if( csharpPgSqlSchemaObjImplHandler == null ) {
			csharpPgSqlSchemaObjImplHandler = new CFBamXmlLoaderCSharpPgSqlSchemaObjImplHandler( this );
		}
		return( csharpPgSqlSchemaObjImplHandler );
	}

	protected CFBamXmlLoaderCSharpPgSqlSchemaObjUsingHandler getCSharpPgSqlSchemaObjUsingHandler() {
		if( csharpPgSqlSchemaObjUsingHandler == null ) {
			csharpPgSqlSchemaObjUsingHandler = new CFBamXmlLoaderCSharpPgSqlSchemaObjUsingHandler( this );
		}
		return( csharpPgSqlSchemaObjUsingHandler );
	}

	protected CFBamXmlLoaderCSharpRamSchemaObjMembersHandler getCSharpRamSchemaObjMembersHandler() {
		if( csharpRamSchemaObjMembersHandler == null ) {
			csharpRamSchemaObjMembersHandler = new CFBamXmlLoaderCSharpRamSchemaObjMembersHandler( this );
		}
		return( csharpRamSchemaObjMembersHandler );
	}

	protected CFBamXmlLoaderCSharpRamSchemaObjImplHandler getCSharpRamSchemaObjImplHandler() {
		if( csharpRamSchemaObjImplHandler == null ) {
			csharpRamSchemaObjImplHandler = new CFBamXmlLoaderCSharpRamSchemaObjImplHandler( this );
		}
		return( csharpRamSchemaObjImplHandler );
	}

	protected CFBamXmlLoaderCSharpRamSchemaObjUsingHandler getCSharpRamSchemaObjUsingHandler() {
		if( csharpRamSchemaObjUsingHandler == null ) {
			csharpRamSchemaObjUsingHandler = new CFBamXmlLoaderCSharpRamSchemaObjUsingHandler( this );
		}
		return( csharpRamSchemaObjUsingHandler );
	}

	protected CFBamXmlLoaderCSharpXMsgSchemaUsingHandler getCSharpXMsgSchemaUsingHandler() {
		if( csharpXMsgSchemaUsingHandler == null ) {
			csharpXMsgSchemaUsingHandler = new CFBamXmlLoaderCSharpXMsgSchemaUsingHandler( this );
		}
		return( csharpXMsgSchemaUsingHandler );
	}

	protected CFBamXmlLoaderCSharpXMsgSchemaFormattersHandler getCSharpXMsgSchemaFormattersHandler() {
		if( csharpXMsgSchemaFormattersHandler == null ) {
			csharpXMsgSchemaFormattersHandler = new CFBamXmlLoaderCSharpXMsgSchemaFormattersHandler( this );
		}
		return( csharpXMsgSchemaFormattersHandler );
	}

	protected CFBamXmlLoaderCSharpXMsgClientSchemaUsingHandler getCSharpXMsgClientSchemaUsingHandler() {
		if( csharpXMsgClientSchemaUsingHandler == null ) {
			csharpXMsgClientSchemaUsingHandler = new CFBamXmlLoaderCSharpXMsgClientSchemaUsingHandler( this );
		}
		return( csharpXMsgClientSchemaUsingHandler );
	}

	protected CFBamXmlLoaderCSharpXMsgClientSchemaBodyHandler getCSharpXMsgClientSchemaBodyHandler() {
		if( csharpXMsgClientSchemaBodyHandler == null ) {
			csharpXMsgClientSchemaBodyHandler = new CFBamXmlLoaderCSharpXMsgClientSchemaBodyHandler( this );
		}
		return( csharpXMsgClientSchemaBodyHandler );
	}

	protected CFBamXmlLoaderCSharpXMsgRqstSchemaBodyHandler getCSharpXMsgRqstSchemaBodyHandler() {
		if( csharpXMsgRqstSchemaBodyHandler == null ) {
			csharpXMsgRqstSchemaBodyHandler = new CFBamXmlLoaderCSharpXMsgRqstSchemaBodyHandler( this );
		}
		return( csharpXMsgRqstSchemaBodyHandler );
	}

	protected CFBamXmlLoaderCSharpXMsgRqstSchemaUsingHandler getCSharpXMsgRqstSchemaUsingHandler() {
		if( csharpXMsgRqstSchemaUsingHandler == null ) {
			csharpXMsgRqstSchemaUsingHandler = new CFBamXmlLoaderCSharpXMsgRqstSchemaUsingHandler( this );
		}
		return( csharpXMsgRqstSchemaUsingHandler );
	}

	protected CFBamXmlLoaderCSharpXMsgRqstSchemaWireParsersHandler getCSharpXMsgRqstSchemaWireParsersHandler() {
		if( csharpXMsgRqstSchemaWireParsersHandler == null ) {
			csharpXMsgRqstSchemaWireParsersHandler = new CFBamXmlLoaderCSharpXMsgRqstSchemaWireParsersHandler( this );
		}
		return( csharpXMsgRqstSchemaWireParsersHandler );
	}

	protected CFBamXmlLoaderCSharpXMsgRqstSchemaXsdElementListHandler getCSharpXMsgRqstSchemaXsdElementListHandler() {
		if( csharpXMsgRqstSchemaXsdElementListHandler == null ) {
			csharpXMsgRqstSchemaXsdElementListHandler = new CFBamXmlLoaderCSharpXMsgRqstSchemaXsdElementListHandler( this );
		}
		return( csharpXMsgRqstSchemaXsdElementListHandler );
	}

	protected CFBamXmlLoaderCSharpXMsgRqstSchemaXsdSpecHandler getCSharpXMsgRqstSchemaXsdSpecHandler() {
		if( csharpXMsgRqstSchemaXsdSpecHandler == null ) {
			csharpXMsgRqstSchemaXsdSpecHandler = new CFBamXmlLoaderCSharpXMsgRqstSchemaXsdSpecHandler( this );
		}
		return( csharpXMsgRqstSchemaXsdSpecHandler );
	}

	protected CFBamXmlLoaderCSharpXMsgRspnSchemaBodyHandler getCSharpXMsgRspnSchemaBodyHandler() {
		if( csharpXMsgRspnSchemaBodyHandler == null ) {
			csharpXMsgRspnSchemaBodyHandler = new CFBamXmlLoaderCSharpXMsgRspnSchemaBodyHandler( this );
		}
		return( csharpXMsgRspnSchemaBodyHandler );
	}

	protected CFBamXmlLoaderCSharpXMsgRspnSchemaUsingHandler getCSharpXMsgRspnSchemaUsingHandler() {
		if( csharpXMsgRspnSchemaUsingHandler == null ) {
			csharpXMsgRspnSchemaUsingHandler = new CFBamXmlLoaderCSharpXMsgRspnSchemaUsingHandler( this );
		}
		return( csharpXMsgRspnSchemaUsingHandler );
	}

	protected CFBamXmlLoaderCSharpXMsgRspnSchemaWireParsersHandler getCSharpXMsgRspnSchemaWireParsersHandler() {
		if( csharpXMsgRspnSchemaWireParsersHandler == null ) {
			csharpXMsgRspnSchemaWireParsersHandler = new CFBamXmlLoaderCSharpXMsgRspnSchemaWireParsersHandler( this );
		}
		return( csharpXMsgRspnSchemaWireParsersHandler );
	}

	protected CFBamXmlLoaderCSharpXMsgRspnSchemaXsdElementListHandler getCSharpXMsgRspnSchemaXsdElementListHandler() {
		if( csharpXMsgRspnSchemaXsdElementListHandler == null ) {
			csharpXMsgRspnSchemaXsdElementListHandler = new CFBamXmlLoaderCSharpXMsgRspnSchemaXsdElementListHandler( this );
		}
		return( csharpXMsgRspnSchemaXsdElementListHandler );
	}

	protected CFBamXmlLoaderCSharpXMsgRspnSchemaXsdSpecHandler getCSharpXMsgRspnSchemaXsdSpecHandler() {
		if( csharpXMsgRspnSchemaXsdSpecHandler == null ) {
			csharpXMsgRspnSchemaXsdSpecHandler = new CFBamXmlLoaderCSharpXMsgRspnSchemaXsdSpecHandler( this );
		}
		return( csharpXMsgRspnSchemaXsdSpecHandler );
	}

	protected CFBamXmlLoaderCSharpObjMembersHandler getCSharpObjMembersHandler() {
		if( csharpObjMembersHandler == null ) {
			csharpObjMembersHandler = new CFBamXmlLoaderCSharpObjMembersHandler( this );
		}
		return( csharpObjMembersHandler );
	}

	protected CFBamXmlLoaderCSharpObjInterfaceHandler getCSharpObjInterfaceHandler() {
		if( csharpObjInterfaceHandler == null ) {
			csharpObjInterfaceHandler = new CFBamXmlLoaderCSharpObjInterfaceHandler( this );
		}
		return( csharpObjInterfaceHandler );
	}

	protected CFBamXmlLoaderCSharpObjImplementationHandler getCSharpObjImplementationHandler() {
		if( csharpObjImplementationHandler == null ) {
			csharpObjImplementationHandler = new CFBamXmlLoaderCSharpObjImplementationHandler( this );
		}
		return( csharpObjImplementationHandler );
	}

	protected CFBamXmlLoaderCSharpEditObjMembersHandler getCSharpEditObjMembersHandler() {
		if( csharpEditObjMembersHandler == null ) {
			csharpEditObjMembersHandler = new CFBamXmlLoaderCSharpEditObjMembersHandler( this );
		}
		return( csharpEditObjMembersHandler );
	}

	protected CFBamXmlLoaderCSharpEditObjInterfaceHandler getCSharpEditObjInterfaceHandler() {
		if( csharpEditObjInterfaceHandler == null ) {
			csharpEditObjInterfaceHandler = new CFBamXmlLoaderCSharpEditObjInterfaceHandler( this );
		}
		return( csharpEditObjInterfaceHandler );
	}

	protected CFBamXmlLoaderCSharpEditObjImplementationHandler getCSharpEditObjImplementationHandler() {
		if( csharpEditObjImplementationHandler == null ) {
			csharpEditObjImplementationHandler = new CFBamXmlLoaderCSharpEditObjImplementationHandler( this );
		}
		return( csharpEditObjImplementationHandler );
	}

	protected CFBamXmlLoaderCSharpDb2LUWTableUsingHandler getCSharpDb2LUWTableUsingHandler() {
		if( csharpDb2LUWTableUsingHandler == null ) {
			csharpDb2LUWTableUsingHandler = new CFBamXmlLoaderCSharpDb2LUWTableUsingHandler( this );
		}
		return( csharpDb2LUWTableUsingHandler );
	}

	protected CFBamXmlLoaderCSharpMSSqlTableUsingHandler getCSharpMSSqlTableUsingHandler() {
		if( csharpMSSqlTableUsingHandler == null ) {
			csharpMSSqlTableUsingHandler = new CFBamXmlLoaderCSharpMSSqlTableUsingHandler( this );
		}
		return( csharpMSSqlTableUsingHandler );
	}

	protected CFBamXmlLoaderCSharpMySqlTableUsingHandler getCSharpMySqlTableUsingHandler() {
		if( csharpMySqlTableUsingHandler == null ) {
			csharpMySqlTableUsingHandler = new CFBamXmlLoaderCSharpMySqlTableUsingHandler( this );
		}
		return( csharpMySqlTableUsingHandler );
	}

	protected CFBamXmlLoaderCSharpOracleTableUsingHandler getCSharpOracleTableUsingHandler() {
		if( csharpOracleTableUsingHandler == null ) {
			csharpOracleTableUsingHandler = new CFBamXmlLoaderCSharpOracleTableUsingHandler( this );
		}
		return( csharpOracleTableUsingHandler );
	}

	protected CFBamXmlLoaderCSharpPgSqlTableUsingHandler getCSharpPgSqlTableUsingHandler() {
		if( csharpPgSqlTableUsingHandler == null ) {
			csharpPgSqlTableUsingHandler = new CFBamXmlLoaderCSharpPgSqlTableUsingHandler( this );
		}
		return( csharpPgSqlTableUsingHandler );
	}

	protected CFBamXmlLoaderCSharpRamTableUsingHandler getCSharpRamTableUsingHandler() {
		if( csharpRamTableUsingHandler == null ) {
			csharpRamTableUsingHandler = new CFBamXmlLoaderCSharpRamTableUsingHandler( this );
		}
		return( csharpRamTableUsingHandler );
	}

	protected CFBamXmlLoaderCSharpObjUsingHandler getCSharpObjUsingHandler() {
		if( csharpObjUsingHandler == null ) {
			csharpObjUsingHandler = new CFBamXmlLoaderCSharpObjUsingHandler( this );
		}
		return( csharpObjUsingHandler );
	}

	protected CFBamXmlLoaderCSharpEditObjUsingHandler getCSharpEditObjUsingHandler() {
		if( csharpEditObjUsingHandler == null ) {
			csharpEditObjUsingHandler = new CFBamXmlLoaderCSharpEditObjUsingHandler( this );
		}
		return( csharpEditObjUsingHandler );
	}

	protected CFBamXmlLoaderCSharpSaxLoaderUsingHandler getCSharpSaxLoaderUsingHandler() {
		if( csharpSaxLoaderUsingHandler == null ) {
			csharpSaxLoaderUsingHandler = new CFBamXmlLoaderCSharpSaxLoaderUsingHandler( this );
		}
		return( csharpSaxLoaderUsingHandler );
	}

	protected CFBamXmlLoaderCSharpTableUsingHandler getCSharpTableUsingHandler() {
		if( csharpTableUsingHandler == null ) {
			csharpTableUsingHandler = new CFBamXmlLoaderCSharpTableUsingHandler( this );
		}
		return( csharpTableUsingHandler );
	}

	protected CFBamXmlLoaderCSharpTableObjUsingHandler getCSharpTableObjUsingHandler() {
		if( csharpTableObjUsingHandler == null ) {
			csharpTableObjUsingHandler = new CFBamXmlLoaderCSharpTableObjUsingHandler( this );
		}
		return( csharpTableObjUsingHandler );
	}

	protected CFBamXmlLoaderCSharpTableMembersHandler getCSharpTableMembersHandler() {
		if( csharpTableMembersHandler == null ) {
			csharpTableMembersHandler = new CFBamXmlLoaderCSharpTableMembersHandler( this );
		}
		return( csharpTableMembersHandler );
	}

	protected CFBamXmlLoaderCSharpTableInterfaceHandler getCSharpTableInterfaceHandler() {
		if( csharpTableInterfaceHandler == null ) {
			csharpTableInterfaceHandler = new CFBamXmlLoaderCSharpTableInterfaceHandler( this );
		}
		return( csharpTableInterfaceHandler );
	}

	protected CFBamXmlLoaderCSharpTableImplementationHandler getCSharpTableImplementationHandler() {
		if( csharpTableImplementationHandler == null ) {
			csharpTableImplementationHandler = new CFBamXmlLoaderCSharpTableImplementationHandler( this );
		}
		return( csharpTableImplementationHandler );
	}

	protected CFBamXmlLoaderCSharpTableObjMembersHandler getCSharpTableObjMembersHandler() {
		if( csharpTableObjMembersHandler == null ) {
			csharpTableObjMembersHandler = new CFBamXmlLoaderCSharpTableObjMembersHandler( this );
		}
		return( csharpTableObjMembersHandler );
	}

	protected CFBamXmlLoaderCSharpTableObjInterfaceHandler getCSharpTableObjInterfaceHandler() {
		if( csharpTableObjInterfaceHandler == null ) {
			csharpTableObjInterfaceHandler = new CFBamXmlLoaderCSharpTableObjInterfaceHandler( this );
		}
		return( csharpTableObjInterfaceHandler );
	}

	protected CFBamXmlLoaderCSharpTableObjImplementationHandler getCSharpTableObjImplementationHandler() {
		if( csharpTableObjImplementationHandler == null ) {
			csharpTableObjImplementationHandler = new CFBamXmlLoaderCSharpTableObjImplementationHandler( this );
		}
		return( csharpTableObjImplementationHandler );
	}

	protected CFBamXmlLoaderCSharpDb2LUWTableMembersHandler getCSharpDb2LUWTableMembersHandler() {
		if( csharpDb2LUWTableMembersHandler == null ) {
			csharpDb2LUWTableMembersHandler = new CFBamXmlLoaderCSharpDb2LUWTableMembersHandler( this );
		}
		return( csharpDb2LUWTableMembersHandler );
	}

	protected CFBamXmlLoaderCSharpDb2LUWTableImplementationHandler getCSharpDb2LUWTableImplementationHandler() {
		if( csharpDb2LUWTableImplementationHandler == null ) {
			csharpDb2LUWTableImplementationHandler = new CFBamXmlLoaderCSharpDb2LUWTableImplementationHandler( this );
		}
		return( csharpDb2LUWTableImplementationHandler );
	}

	protected CFBamXmlLoaderCSharpMSSqlTableMembersHandler getCSharpMSSqlTableMembersHandler() {
		if( csharpMSSqlTableMembersHandler == null ) {
			csharpMSSqlTableMembersHandler = new CFBamXmlLoaderCSharpMSSqlTableMembersHandler( this );
		}
		return( csharpMSSqlTableMembersHandler );
	}

	protected CFBamXmlLoaderCSharpMSSqlTableImplementationHandler getCSharpMSSqlTableImplementationHandler() {
		if( csharpMSSqlTableImplementationHandler == null ) {
			csharpMSSqlTableImplementationHandler = new CFBamXmlLoaderCSharpMSSqlTableImplementationHandler( this );
		}
		return( csharpMSSqlTableImplementationHandler );
	}

	protected CFBamXmlLoaderCSharpMySqlTableMembersHandler getCSharpMySqlTableMembersHandler() {
		if( csharpMySqlTableMembersHandler == null ) {
			csharpMySqlTableMembersHandler = new CFBamXmlLoaderCSharpMySqlTableMembersHandler( this );
		}
		return( csharpMySqlTableMembersHandler );
	}

	protected CFBamXmlLoaderCSharpMySqlTableImplementationHandler getCSharpMySqlTableImplementationHandler() {
		if( csharpMySqlTableImplementationHandler == null ) {
			csharpMySqlTableImplementationHandler = new CFBamXmlLoaderCSharpMySqlTableImplementationHandler( this );
		}
		return( csharpMySqlTableImplementationHandler );
	}

	protected CFBamXmlLoaderCSharpOracleTableMembersHandler getCSharpOracleTableMembersHandler() {
		if( csharpOracleTableMembersHandler == null ) {
			csharpOracleTableMembersHandler = new CFBamXmlLoaderCSharpOracleTableMembersHandler( this );
		}
		return( csharpOracleTableMembersHandler );
	}

	protected CFBamXmlLoaderCSharpOracleTableImplementationHandler getCSharpOracleTableImplementationHandler() {
		if( csharpOracleTableImplementationHandler == null ) {
			csharpOracleTableImplementationHandler = new CFBamXmlLoaderCSharpOracleTableImplementationHandler( this );
		}
		return( csharpOracleTableImplementationHandler );
	}

	protected CFBamXmlLoaderCSharpPgSqlTableMembersHandler getCSharpPgSqlTableMembersHandler() {
		if( csharpPgSqlTableMembersHandler == null ) {
			csharpPgSqlTableMembersHandler = new CFBamXmlLoaderCSharpPgSqlTableMembersHandler( this );
		}
		return( csharpPgSqlTableMembersHandler );
	}

	protected CFBamXmlLoaderCSharpPgSqlTableImplementationHandler getCSharpPgSqlTableImplementationHandler() {
		if( csharpPgSqlTableImplementationHandler == null ) {
			csharpPgSqlTableImplementationHandler = new CFBamXmlLoaderCSharpPgSqlTableImplementationHandler( this );
		}
		return( csharpPgSqlTableImplementationHandler );
	}

	protected CFBamXmlLoaderCSharpRamTableMembersHandler getCSharpRamTableMembersHandler() {
		if( csharpRamTableMembersHandler == null ) {
			csharpRamTableMembersHandler = new CFBamXmlLoaderCSharpRamTableMembersHandler( this );
		}
		return( csharpRamTableMembersHandler );
	}

	protected CFBamXmlLoaderCSharpRamTableImplementationHandler getCSharpRamTableImplementationHandler() {
		if( csharpRamTableImplementationHandler == null ) {
			csharpRamTableImplementationHandler = new CFBamXmlLoaderCSharpRamTableImplementationHandler( this );
		}
		return( csharpRamTableImplementationHandler );
	}

	protected CFBamXmlLoaderCSharpSaxLoaderStartElementHandler getCSharpSaxLoaderStartElementHandler() {
		if( csharpSaxLoaderStartElementHandler == null ) {
			csharpSaxLoaderStartElementHandler = new CFBamXmlLoaderCSharpSaxLoaderStartElementHandler( this );
		}
		return( csharpSaxLoaderStartElementHandler );
	}

	protected CFBamXmlLoaderCSharpSaxLoaderEndElementHandler getCSharpSaxLoaderEndElementHandler() {
		if( csharpSaxLoaderEndElementHandler == null ) {
			csharpSaxLoaderEndElementHandler = new CFBamXmlLoaderCSharpSaxLoaderEndElementHandler( this );
		}
		return( csharpSaxLoaderEndElementHandler );
	}

	protected CFBamXmlLoaderCSharpXMsgTableUsingHandler getCSharpXMsgTableUsingHandler() {
		if( csharpXMsgTableUsingHandler == null ) {
			csharpXMsgTableUsingHandler = new CFBamXmlLoaderCSharpXMsgTableUsingHandler( this );
		}
		return( csharpXMsgTableUsingHandler );
	}

	protected CFBamXmlLoaderCSharpXMsgTableFormattersHandler getCSharpXMsgTableFormattersHandler() {
		if( csharpXMsgTableFormattersHandler == null ) {
			csharpXMsgTableFormattersHandler = new CFBamXmlLoaderCSharpXMsgTableFormattersHandler( this );
		}
		return( csharpXMsgTableFormattersHandler );
	}

	protected CFBamXmlLoaderCSharpXMsgRqstTableUsingHandler getCSharpXMsgRqstTableUsingHandler() {
		if( csharpXMsgRqstTableUsingHandler == null ) {
			csharpXMsgRqstTableUsingHandler = new CFBamXmlLoaderCSharpXMsgRqstTableUsingHandler( this );
		}
		return( csharpXMsgRqstTableUsingHandler );
	}

	protected CFBamXmlLoaderCSharpXMsgRspnTableUsingHandler getCSharpXMsgRspnTableUsingHandler() {
		if( csharpXMsgRspnTableUsingHandler == null ) {
			csharpXMsgRspnTableUsingHandler = new CFBamXmlLoaderCSharpXMsgRspnTableUsingHandler( this );
		}
		return( csharpXMsgRspnTableUsingHandler );
	}

	protected CFBamXmlLoaderCSharpXMsgClientTableUsingHandler getCSharpXMsgClientTableUsingHandler() {
		if( csharpXMsgClientTableUsingHandler == null ) {
			csharpXMsgClientTableUsingHandler = new CFBamXmlLoaderCSharpXMsgClientTableUsingHandler( this );
		}
		return( csharpXMsgClientTableUsingHandler );
	}

	protected CFBamXmlLoaderCSharpXMsgRqstTableBodyHandler getCSharpXMsgRqstTableBodyHandler() {
		if( csharpXMsgRqstTableBodyHandler == null ) {
			csharpXMsgRqstTableBodyHandler = new CFBamXmlLoaderCSharpXMsgRqstTableBodyHandler( this );
		}
		return( csharpXMsgRqstTableBodyHandler );
	}

	protected CFBamXmlLoaderCSharpXMsgRspnTableBodyHandler getCSharpXMsgRspnTableBodyHandler() {
		if( csharpXMsgRspnTableBodyHandler == null ) {
			csharpXMsgRspnTableBodyHandler = new CFBamXmlLoaderCSharpXMsgRspnTableBodyHandler( this );
		}
		return( csharpXMsgRspnTableBodyHandler );
	}

	protected CFBamXmlLoaderCSharpXMsgClientTableBodyHandler getCSharpXMsgClientTableBodyHandler() {
		if( csharpXMsgClientTableBodyHandler == null ) {
			csharpXMsgClientTableBodyHandler = new CFBamXmlLoaderCSharpXMsgClientTableBodyHandler( this );
		}
		return( csharpXMsgClientTableBodyHandler );
	}

	protected CFBamXmlLoaderLicenseHandler getLicenseHandler() {
		if( licenseHandler == null ) {
			licenseHandler = new CFBamXmlLoaderLicenseHandler( this );
		}
		return( licenseHandler );
	}

	protected CFBamXmlLoaderMajorVersionHandler getMajorVersionHandler() {
		if( majorVersionHandler == null ) {
			majorVersionHandler = new CFBamXmlLoaderMajorVersionHandler( this );
			majorVersionHandler.addElementHandler( "MinorVersion", getMinorVersionHandler() );
		}
		return( majorVersionHandler );
	}

	protected CFBamXmlLoaderMinorVersionHandler getMinorVersionHandler() {
		if( minorVersionHandler == null ) {
			minorVersionHandler = new CFBamXmlLoaderMinorVersionHandler( this );
			minorVersionHandler.addElementHandler( "SchemaDef", getSchemaDefHandler() );
		}
		return( minorVersionHandler );
	}

	protected CFBamXmlLoaderNmTokenColHandler getNmTokenColHandler() {
		if( nmTokenColHandler == null ) {
			nmTokenColHandler = new CFBamXmlLoaderNmTokenColHandler( this );
		}
		return( nmTokenColHandler );
	}

	protected CFBamXmlLoaderNmTokenTypeHandler getNmTokenTypeHandler() {
		if( nmTokenTypeHandler == null ) {
			nmTokenTypeHandler = new CFBamXmlLoaderNmTokenTypeHandler( this );
		}
		return( nmTokenTypeHandler );
	}

	protected CFBamXmlLoaderNmTokensColHandler getNmTokensColHandler() {
		if( nmTokensColHandler == null ) {
			nmTokensColHandler = new CFBamXmlLoaderNmTokensColHandler( this );
		}
		return( nmTokensColHandler );
	}

	protected CFBamXmlLoaderNmTokensTypeHandler getNmTokensTypeHandler() {
		if( nmTokensTypeHandler == null ) {
			nmTokensTypeHandler = new CFBamXmlLoaderNmTokensTypeHandler( this );
		}
		return( nmTokensTypeHandler );
	}

	protected CFBamXmlLoaderNumberColHandler getNumberColHandler() {
		if( numberColHandler == null ) {
			numberColHandler = new CFBamXmlLoaderNumberColHandler( this );
		}
		return( numberColHandler );
	}

	protected CFBamXmlLoaderNumberTypeHandler getNumberTypeHandler() {
		if( numberTypeHandler == null ) {
			numberTypeHandler = new CFBamXmlLoaderNumberTypeHandler( this );
		}
		return( numberTypeHandler );
	}

	protected CFBamXmlLoaderParamHandler getParamHandler() {
		if( paramHandler == null ) {
			paramHandler = new CFBamXmlLoaderParamHandler( this );
		}
		return( paramHandler );
	}

	protected CFBamXmlLoaderPrimaryIndexHandler getPrimaryIndexHandler() {
		if( primaryIndexHandler == null ) {
			primaryIndexHandler = new CFBamXmlLoaderPrimaryIndexHandler( this );
			primaryIndexHandler.addElementHandler( "IndexCol", getIndexColHandler() );
		}
		return( primaryIndexHandler );
	}

	protected CFBamXmlLoaderRelationHandler getRelationHandler() {
		if( relationHandler == null ) {
			relationHandler = new CFBamXmlLoaderRelationHandler( this );
			relationHandler.addElementHandler( "RelationCol", getRelationColHandler() );
		}
		return( relationHandler );
	}

	protected CFBamXmlLoaderRelationColHandler getRelationColHandler() {
		if( relationColHandler == null ) {
			relationColHandler = new CFBamXmlLoaderRelationColHandler( this );
		}
		return( relationColHandler );
	}

	protected CFBamXmlLoaderSchemaDefHandler getSchemaDefHandler() {
		if( schemaDefHandler == null ) {
			schemaDefHandler = new CFBamXmlLoaderSchemaDefHandler( this );
			schemaDefHandler.addElementHandler( "CommonLicense", getCommonLicenseHandler() );
			schemaDefHandler.addElementHandler( "ClientImplLicense", getClientImplLicenseHandler() );
			schemaDefHandler.addElementHandler( "ClientXFaceLicense", getClientXFaceLicenseHandler() );
			schemaDefHandler.addElementHandler( "ServerImplLicense", getServerImplLicenseHandler() );
			schemaDefHandler.addElementHandler( "ServerXFaceLicense", getServerXFaceLicenseHandler() );
			schemaDefHandler.addElementHandler( "SchemaRef", getSchemaRefHandler() );
			schemaDefHandler.addElementHandler( "Table", getTableHandler() );
			schemaDefHandler.addElementHandler( "TableAddendum", getTableAddendumHandler() );
			schemaDefHandler.addElementHandler( "BlobType", getBlobTypeHandler() );
			schemaDefHandler.addElementHandler( "BoolType", getBoolTypeHandler() );
			schemaDefHandler.addElementHandler( "Int16Type", getInt16TypeHandler() );
			schemaDefHandler.addElementHandler( "Id16Gen", getId16GenHandler() );
			schemaDefHandler.addElementHandler( "EnumType", getEnumTypeHandler() );
			schemaDefHandler.addElementHandler( "Int32Type", getInt32TypeHandler() );
			schemaDefHandler.addElementHandler( "Id32Gen", getId32GenHandler() );
			schemaDefHandler.addElementHandler( "Int64Type", getInt64TypeHandler() );
			schemaDefHandler.addElementHandler( "Id64Gen", getId64GenHandler() );
			schemaDefHandler.addElementHandler( "UInt16Type", getUInt16TypeHandler() );
			schemaDefHandler.addElementHandler( "UInt32Type", getUInt32TypeHandler() );
			schemaDefHandler.addElementHandler( "UInt64Type", getUInt64TypeHandler() );
			schemaDefHandler.addElementHandler( "FloatType", getFloatTypeHandler() );
			schemaDefHandler.addElementHandler( "DoubleType", getDoubleTypeHandler() );
			schemaDefHandler.addElementHandler( "NumberType", getNumberTypeHandler() );
			schemaDefHandler.addElementHandler( "StringType", getStringTypeHandler() );
			schemaDefHandler.addElementHandler( "TextType", getTextTypeHandler() );
			schemaDefHandler.addElementHandler( "NmTokenType", getNmTokenTypeHandler() );
			schemaDefHandler.addElementHandler( "NmTokensType", getNmTokensTypeHandler() );
			schemaDefHandler.addElementHandler( "TokenType", getTokenTypeHandler() );
			schemaDefHandler.addElementHandler( "DateType", getDateTypeHandler() );
			schemaDefHandler.addElementHandler( "TimeType", getTimeTypeHandler() );
			schemaDefHandler.addElementHandler( "TimestampType", getTimestampTypeHandler() );
			schemaDefHandler.addElementHandler( "TZDateType", getTZDateTypeHandler() );
			schemaDefHandler.addElementHandler( "TZTimeType", getTZTimeTypeHandler() );
			schemaDefHandler.addElementHandler( "TZTimestampType", getTZTimestampTypeHandler() );
			schemaDefHandler.addElementHandler( "UuidType", getUuidTypeHandler() );
			schemaDefHandler.addElementHandler( "UuidGen", getUuidGenHandler() );
			schemaDefHandler.addElementHandler( "CafeSchemaObjInterface", getCafeSchemaObjInterfaceHandler() );
			schemaDefHandler.addElementHandler( "CafeSchemaObjMembers", getCafeSchemaObjMembersHandler() );
			schemaDefHandler.addElementHandler( "CafeSchemaObjImport", getCafeSchemaObjImportHandler() );
			schemaDefHandler.addElementHandler( "CafeSchemaObjImplementation", getCafeSchemaObjImplementationHandler() );
			schemaDefHandler.addElementHandler( "CafeDb2LUWSchemaObjMembers", getCafeDb2LUWSchemaObjMembersHandler() );
			schemaDefHandler.addElementHandler( "CafeDb2LUWSchemaObjImpl", getCafeDb2LUWSchemaObjImplHandler() );
			schemaDefHandler.addElementHandler( "CafeDb2LUWSchemaObjImport", getCafeDb2LUWSchemaObjImportHandler() );
			schemaDefHandler.addElementHandler( "CafeMSSqlSchemaObjMembers", getCafeMSSqlSchemaObjMembersHandler() );
			schemaDefHandler.addElementHandler( "CafeMSSqlSchemaObjImpl", getCafeMSSqlSchemaObjImplHandler() );
			schemaDefHandler.addElementHandler( "CafeMSSqlSchemaObjImport", getCafeMSSqlSchemaObjImportHandler() );
			schemaDefHandler.addElementHandler( "CafeMySqlSchemaObjMembers", getCafeMySqlSchemaObjMembersHandler() );
			schemaDefHandler.addElementHandler( "CafeMySqlSchemaObjImpl", getCafeMySqlSchemaObjImplHandler() );
			schemaDefHandler.addElementHandler( "CafeMySqlSchemaObjImport", getCafeMySqlSchemaObjImportHandler() );
			schemaDefHandler.addElementHandler( "CafeOracleSchemaObjMembers", getCafeOracleSchemaObjMembersHandler() );
			schemaDefHandler.addElementHandler( "CafeOracleSchemaObjImpl", getCafeOracleSchemaObjImplHandler() );
			schemaDefHandler.addElementHandler( "CafeOracleSchemaObjImport", getCafeOracleSchemaObjImportHandler() );
			schemaDefHandler.addElementHandler( "CafePgSqlSchemaObjMembers", getCafePgSqlSchemaObjMembersHandler() );
			schemaDefHandler.addElementHandler( "CafePgSqlSchemaObjImpl", getCafePgSqlSchemaObjImplHandler() );
			schemaDefHandler.addElementHandler( "CafePgSqlSchemaObjImport", getCafePgSqlSchemaObjImportHandler() );
			schemaDefHandler.addElementHandler( "CafeRamSchemaObjMembers", getCafeRamSchemaObjMembersHandler() );
			schemaDefHandler.addElementHandler( "CafeRamSchemaObjImpl", getCafeRamSchemaObjImplHandler() );
			schemaDefHandler.addElementHandler( "CafeRamSchemaObjImport", getCafeRamSchemaObjImportHandler() );
			schemaDefHandler.addElementHandler( "CafeXMsgSchemaImport", getCafeXMsgSchemaImportHandler() );
			schemaDefHandler.addElementHandler( "CafeXMsgSchemaFormatters", getCafeXMsgSchemaFormattersHandler() );
			schemaDefHandler.addElementHandler( "CafeXMsgClientSchemaImport", getCafeXMsgClientSchemaImportHandler() );
			schemaDefHandler.addElementHandler( "CafeXMsgClientSchemaBody", getCafeXMsgClientSchemaBodyHandler() );
			schemaDefHandler.addElementHandler( "CafeXMsgRqstSchemaBody", getCafeXMsgRqstSchemaBodyHandler() );
			schemaDefHandler.addElementHandler( "CafeXMsgRqstSchemaImport", getCafeXMsgRqstSchemaImportHandler() );
			schemaDefHandler.addElementHandler( "CafeXMsgRqstSchemaWireParsers", getCafeXMsgRqstSchemaWireParsersHandler() );
			schemaDefHandler.addElementHandler( "CafeXMsgRqstSchemaXsdElementList", getCafeXMsgRqstSchemaXsdElementListHandler() );
			schemaDefHandler.addElementHandler( "CafeXMsgRqstSchemaXsdSpec", getCafeXMsgRqstSchemaXsdSpecHandler() );
			schemaDefHandler.addElementHandler( "CafeXMsgRspnSchemaBody", getCafeXMsgRspnSchemaBodyHandler() );
			schemaDefHandler.addElementHandler( "CafeXMsgRspnSchemaImport", getCafeXMsgRspnSchemaImportHandler() );
			schemaDefHandler.addElementHandler( "CafeXMsgRspnSchemaWireParsers", getCafeXMsgRspnSchemaWireParsersHandler() );
			schemaDefHandler.addElementHandler( "CafeXMsgRspnSchemaXsdElementList", getCafeXMsgRspnSchemaXsdElementListHandler() );
			schemaDefHandler.addElementHandler( "CafeXMsgRspnSchemaXsdSpec", getCafeXMsgRspnSchemaXsdSpecHandler() );
			schemaDefHandler.addElementHandler( "CPlusSchemaObjInterface", getCPlusSchemaObjInterfaceHandler() );
			schemaDefHandler.addElementHandler( "CPlusSchemaObjMembers", getCPlusSchemaObjMembersHandler() );
			schemaDefHandler.addElementHandler( "CPlusSchemaObjInclude", getCPlusSchemaObjIncludeHandler() );
			schemaDefHandler.addElementHandler( "CPlusSchemaObjImplementation", getCPlusSchemaObjImplementationHandler() );
			schemaDefHandler.addElementHandler( "CPlusDb2LUWSchemaObjMembers", getCPlusDb2LUWSchemaObjMembersHandler() );
			schemaDefHandler.addElementHandler( "CPlusDb2LUWSchemaObjImpl", getCPlusDb2LUWSchemaObjImplHandler() );
			schemaDefHandler.addElementHandler( "CPlusDb2LUWSchemaObjInclude", getCPlusDb2LUWSchemaObjIncludeHandler() );
			schemaDefHandler.addElementHandler( "CPlusMSSqlSchemaObjMembers", getCPlusMSSqlSchemaObjMembersHandler() );
			schemaDefHandler.addElementHandler( "CPlusMSSqlSchemaObjImpl", getCPlusMSSqlSchemaObjImplHandler() );
			schemaDefHandler.addElementHandler( "CPlusMSSqlSchemaObjInclude", getCPlusMSSqlSchemaObjIncludeHandler() );
			schemaDefHandler.addElementHandler( "CPlusMySqlSchemaObjMembers", getCPlusMySqlSchemaObjMembersHandler() );
			schemaDefHandler.addElementHandler( "CPlusMySqlSchemaObjImpl", getCPlusMySqlSchemaObjImplHandler() );
			schemaDefHandler.addElementHandler( "CPlusMySqlSchemaObjInclude", getCPlusMySqlSchemaObjIncludeHandler() );
			schemaDefHandler.addElementHandler( "CPlusOracleSchemaObjMembers", getCPlusOracleSchemaObjMembersHandler() );
			schemaDefHandler.addElementHandler( "CPlusOracleSchemaObjImpl", getCPlusOracleSchemaObjImplHandler() );
			schemaDefHandler.addElementHandler( "CPlusOracleSchemaObjInclude", getCPlusOracleSchemaObjIncludeHandler() );
			schemaDefHandler.addElementHandler( "CPlusPgSqlSchemaObjMembers", getCPlusPgSqlSchemaObjMembersHandler() );
			schemaDefHandler.addElementHandler( "CPlusPgSqlSchemaObjImpl", getCPlusPgSqlSchemaObjImplHandler() );
			schemaDefHandler.addElementHandler( "CPlusPgSqlSchemaObjInclude", getCPlusPgSqlSchemaObjIncludeHandler() );
			schemaDefHandler.addElementHandler( "CPlusRamSchemaObjMembers", getCPlusRamSchemaObjMembersHandler() );
			schemaDefHandler.addElementHandler( "CPlusRamSchemaObjImpl", getCPlusRamSchemaObjImplHandler() );
			schemaDefHandler.addElementHandler( "CPlusRamSchemaObjInclude", getCPlusRamSchemaObjIncludeHandler() );
			schemaDefHandler.addElementHandler( "CPlusXMsgSchemaInclude", getCPlusXMsgSchemaIncludeHandler() );
			schemaDefHandler.addElementHandler( "CPlusXMsgSchemaFormatters", getCPlusXMsgSchemaFormattersHandler() );
			schemaDefHandler.addElementHandler( "CPlusXMsgClientSchemaInclude", getCPlusXMsgClientSchemaIncludeHandler() );
			schemaDefHandler.addElementHandler( "CPlusXMsgClientSchemaBody", getCPlusXMsgClientSchemaBodyHandler() );
			schemaDefHandler.addElementHandler( "CPlusXMsgRqstSchemaBody", getCPlusXMsgRqstSchemaBodyHandler() );
			schemaDefHandler.addElementHandler( "CPlusXMsgRqstSchemaInclude", getCPlusXMsgRqstSchemaIncludeHandler() );
			schemaDefHandler.addElementHandler( "CPlusXMsgRqstSchemaWireParsers", getCPlusXMsgRqstSchemaWireParsersHandler() );
			schemaDefHandler.addElementHandler( "CPlusXMsgRqstSchemaXsdElementList", getCPlusXMsgRqstSchemaXsdElementListHandler() );
			schemaDefHandler.addElementHandler( "CPlusXMsgRqstSchemaXsdSpec", getCPlusXMsgRqstSchemaXsdSpecHandler() );
			schemaDefHandler.addElementHandler( "CPlusXMsgRspnSchemaBody", getCPlusXMsgRspnSchemaBodyHandler() );
			schemaDefHandler.addElementHandler( "CPlusXMsgRspnSchemaInclude", getCPlusXMsgRspnSchemaIncludeHandler() );
			schemaDefHandler.addElementHandler( "CPlusXMsgRspnSchemaWireParsers", getCPlusXMsgRspnSchemaWireParsersHandler() );
			schemaDefHandler.addElementHandler( "CPlusXMsgRspnSchemaXsdElementList", getCPlusXMsgRspnSchemaXsdElementListHandler() );
			schemaDefHandler.addElementHandler( "CPlusXMsgRspnSchemaXsdSpec", getCPlusXMsgRspnSchemaXsdSpecHandler() );
			schemaDefHandler.addElementHandler( "HPlusSchemaObjInterface", getHPlusSchemaObjInterfaceHandler() );
			schemaDefHandler.addElementHandler( "HPlusSchemaObjMembers", getHPlusSchemaObjMembersHandler() );
			schemaDefHandler.addElementHandler( "HPlusSchemaObjInclude", getHPlusSchemaObjIncludeHandler() );
			schemaDefHandler.addElementHandler( "HPlusSchemaObjImplementation", getHPlusSchemaObjImplementationHandler() );
			schemaDefHandler.addElementHandler( "HPlusDb2LUWSchemaObjMembers", getHPlusDb2LUWSchemaObjMembersHandler() );
			schemaDefHandler.addElementHandler( "HPlusDb2LUWSchemaObjImpl", getHPlusDb2LUWSchemaObjImplHandler() );
			schemaDefHandler.addElementHandler( "HPlusDb2LUWSchemaObjInclude", getHPlusDb2LUWSchemaObjIncludeHandler() );
			schemaDefHandler.addElementHandler( "HPlusMSSqlSchemaObjMembers", getHPlusMSSqlSchemaObjMembersHandler() );
			schemaDefHandler.addElementHandler( "HPlusMSSqlSchemaObjImpl", getHPlusMSSqlSchemaObjImplHandler() );
			schemaDefHandler.addElementHandler( "HPlusMSSqlSchemaObjInclude", getHPlusMSSqlSchemaObjIncludeHandler() );
			schemaDefHandler.addElementHandler( "HPlusMySqlSchemaObjMembers", getHPlusMySqlSchemaObjMembersHandler() );
			schemaDefHandler.addElementHandler( "HPlusMySqlSchemaObjImpl", getHPlusMySqlSchemaObjImplHandler() );
			schemaDefHandler.addElementHandler( "HPlusMySqlSchemaObjInclude", getHPlusMySqlSchemaObjIncludeHandler() );
			schemaDefHandler.addElementHandler( "HPlusOracleSchemaObjMembers", getHPlusOracleSchemaObjMembersHandler() );
			schemaDefHandler.addElementHandler( "HPlusOracleSchemaObjImpl", getHPlusOracleSchemaObjImplHandler() );
			schemaDefHandler.addElementHandler( "HPlusOracleSchemaObjInclude", getHPlusOracleSchemaObjIncludeHandler() );
			schemaDefHandler.addElementHandler( "HPlusPgSqlSchemaObjMembers", getHPlusPgSqlSchemaObjMembersHandler() );
			schemaDefHandler.addElementHandler( "HPlusPgSqlSchemaObjImpl", getHPlusPgSqlSchemaObjImplHandler() );
			schemaDefHandler.addElementHandler( "HPlusPgSqlSchemaObjInclude", getHPlusPgSqlSchemaObjIncludeHandler() );
			schemaDefHandler.addElementHandler( "HPlusRamSchemaObjMembers", getHPlusRamSchemaObjMembersHandler() );
			schemaDefHandler.addElementHandler( "HPlusRamSchemaObjImpl", getHPlusRamSchemaObjImplHandler() );
			schemaDefHandler.addElementHandler( "HPlusRamSchemaObjInclude", getHPlusRamSchemaObjIncludeHandler() );
			schemaDefHandler.addElementHandler( "HPlusXMsgSchemaInclude", getHPlusXMsgSchemaIncludeHandler() );
			schemaDefHandler.addElementHandler( "HPlusXMsgSchemaFormatters", getHPlusXMsgSchemaFormattersHandler() );
			schemaDefHandler.addElementHandler( "HPlusXMsgClientSchemaInclude", getHPlusXMsgClientSchemaIncludeHandler() );
			schemaDefHandler.addElementHandler( "HPlusXMsgClientSchemaBody", getHPlusXMsgClientSchemaBodyHandler() );
			schemaDefHandler.addElementHandler( "HPlusXMsgRqstSchemaBody", getHPlusXMsgRqstSchemaBodyHandler() );
			schemaDefHandler.addElementHandler( "HPlusXMsgRqstSchemaInclude", getHPlusXMsgRqstSchemaIncludeHandler() );
			schemaDefHandler.addElementHandler( "HPlusXMsgRqstSchemaWireParsers", getHPlusXMsgRqstSchemaWireParsersHandler() );
			schemaDefHandler.addElementHandler( "HPlusXMsgRqstSchemaXsdElementList", getHPlusXMsgRqstSchemaXsdElementListHandler() );
			schemaDefHandler.addElementHandler( "HPlusXMsgRqstSchemaXsdSpec", getHPlusXMsgRqstSchemaXsdSpecHandler() );
			schemaDefHandler.addElementHandler( "HPlusXMsgRspnSchemaBody", getHPlusXMsgRspnSchemaBodyHandler() );
			schemaDefHandler.addElementHandler( "HPlusXMsgRspnSchemaInclude", getHPlusXMsgRspnSchemaIncludeHandler() );
			schemaDefHandler.addElementHandler( "HPlusXMsgRspnSchemaWireParsers", getHPlusXMsgRspnSchemaWireParsersHandler() );
			schemaDefHandler.addElementHandler( "HPlusXMsgRspnSchemaXsdElementList", getHPlusXMsgRspnSchemaXsdElementListHandler() );
			schemaDefHandler.addElementHandler( "HPlusXMsgRspnSchemaXsdSpec", getHPlusXMsgRspnSchemaXsdSpecHandler() );
			schemaDefHandler.addElementHandler( "CSharpSchemaObjInterface", getCSharpSchemaObjInterfaceHandler() );
			schemaDefHandler.addElementHandler( "CSharpSchemaObjMembers", getCSharpSchemaObjMembersHandler() );
			schemaDefHandler.addElementHandler( "CSharpSchemaObjUsing", getCSharpSchemaObjUsingHandler() );
			schemaDefHandler.addElementHandler( "CSharpSchemaObjImplementation", getCSharpSchemaObjImplementationHandler() );
			schemaDefHandler.addElementHandler( "CSharpDb2LUWSchemaObjMembers", getCSharpDb2LUWSchemaObjMembersHandler() );
			schemaDefHandler.addElementHandler( "CSharpDb2LUWSchemaObjImpl", getCSharpDb2LUWSchemaObjImplHandler() );
			schemaDefHandler.addElementHandler( "CSharpDb2LUWSchemaObjUsing", getCSharpDb2LUWSchemaObjUsingHandler() );
			schemaDefHandler.addElementHandler( "CSharpMSSqlSchemaObjMembers", getCSharpMSSqlSchemaObjMembersHandler() );
			schemaDefHandler.addElementHandler( "CSharpMSSqlSchemaObjImpl", getCSharpMSSqlSchemaObjImplHandler() );
			schemaDefHandler.addElementHandler( "CSharpMSSqlSchemaObjUsing", getCSharpMSSqlSchemaObjUsingHandler() );
			schemaDefHandler.addElementHandler( "CSharpMySqlSchemaObjMembers", getCSharpMySqlSchemaObjMembersHandler() );
			schemaDefHandler.addElementHandler( "CSharpMySqlSchemaObjImpl", getCSharpMySqlSchemaObjImplHandler() );
			schemaDefHandler.addElementHandler( "CSharpMySqlSchemaObjUsing", getCSharpMySqlSchemaObjUsingHandler() );
			schemaDefHandler.addElementHandler( "CSharpOracleSchemaObjMembers", getCSharpOracleSchemaObjMembersHandler() );
			schemaDefHandler.addElementHandler( "CSharpOracleSchemaObjImpl", getCSharpOracleSchemaObjImplHandler() );
			schemaDefHandler.addElementHandler( "CSharpOracleSchemaObjUsing", getCSharpOracleSchemaObjUsingHandler() );
			schemaDefHandler.addElementHandler( "CSharpPgSqlSchemaObjMembers", getCSharpPgSqlSchemaObjMembersHandler() );
			schemaDefHandler.addElementHandler( "CSharpPgSqlSchemaObjImpl", getCSharpPgSqlSchemaObjImplHandler() );
			schemaDefHandler.addElementHandler( "CSharpPgSqlSchemaObjUsing", getCSharpPgSqlSchemaObjUsingHandler() );
			schemaDefHandler.addElementHandler( "CSharpRamSchemaObjMembers", getCSharpRamSchemaObjMembersHandler() );
			schemaDefHandler.addElementHandler( "CSharpRamSchemaObjImpl", getCSharpRamSchemaObjImplHandler() );
			schemaDefHandler.addElementHandler( "CSharpRamSchemaObjUsing", getCSharpRamSchemaObjUsingHandler() );
			schemaDefHandler.addElementHandler( "CSharpXMsgSchemaUsing", getCSharpXMsgSchemaUsingHandler() );
			schemaDefHandler.addElementHandler( "CSharpXMsgSchemaFormatters", getCSharpXMsgSchemaFormattersHandler() );
			schemaDefHandler.addElementHandler( "CSharpXMsgClientSchemaUsing", getCSharpXMsgClientSchemaUsingHandler() );
			schemaDefHandler.addElementHandler( "CSharpXMsgClientSchemaBody", getCSharpXMsgClientSchemaBodyHandler() );
			schemaDefHandler.addElementHandler( "CSharpXMsgRqstSchemaBody", getCSharpXMsgRqstSchemaBodyHandler() );
			schemaDefHandler.addElementHandler( "CSharpXMsgRqstSchemaUsing", getCSharpXMsgRqstSchemaUsingHandler() );
			schemaDefHandler.addElementHandler( "CSharpXMsgRqstSchemaWireParsers", getCSharpXMsgRqstSchemaWireParsersHandler() );
			schemaDefHandler.addElementHandler( "CSharpXMsgRqstSchemaXsdElementList", getCSharpXMsgRqstSchemaXsdElementListHandler() );
			schemaDefHandler.addElementHandler( "CSharpXMsgRqstSchemaXsdSpec", getCSharpXMsgRqstSchemaXsdSpecHandler() );
			schemaDefHandler.addElementHandler( "CSharpXMsgRspnSchemaBody", getCSharpXMsgRspnSchemaBodyHandler() );
			schemaDefHandler.addElementHandler( "CSharpXMsgRspnSchemaUsing", getCSharpXMsgRspnSchemaUsingHandler() );
			schemaDefHandler.addElementHandler( "CSharpXMsgRspnSchemaWireParsers", getCSharpXMsgRspnSchemaWireParsersHandler() );
			schemaDefHandler.addElementHandler( "CSharpXMsgRspnSchemaXsdElementList", getCSharpXMsgRspnSchemaXsdElementListHandler() );
			schemaDefHandler.addElementHandler( "CSharpXMsgRspnSchemaXsdSpec", getCSharpXMsgRspnSchemaXsdSpecHandler() );
		}
		return( schemaDefHandler );
	}

	public CFBamXmlLoaderSchemaRefHandler getSchemaRefHandler() {
		if( schemaRefHandler == null ) {
			schemaRefHandler = new CFBamXmlLoaderSchemaRefHandler( this );
		}
		return( schemaRefHandler );
	}

	protected CFBamXmlLoaderServerListFuncHandler getServerListFuncHandler() {
		if( serverListFuncHandler == null ) {
			serverListFuncHandler = new CFBamXmlLoaderServerListFuncHandler( this );
			serverListFuncHandler.addElementHandler( "Param", getParamHandler() );
			serverListFuncHandler.addElementHandler( "CafeMethodBody", getCafeMethodBodyHandler() );
			serverListFuncHandler.addElementHandler( "CPlusMethodBody", getCPlusMethodBodyHandler() );
			serverListFuncHandler.addElementHandler( "CSharpMethodBody", getCSharpMethodBodyHandler() );
		}
		return( serverListFuncHandler );
	}

	protected CFBamXmlLoaderServerObjFuncHandler getServerObjFuncHandler() {
		if( serverObjFuncHandler == null ) {
			serverObjFuncHandler = new CFBamXmlLoaderServerObjFuncHandler( this );
			serverObjFuncHandler.addElementHandler( "Param", getParamHandler() );
			serverObjFuncHandler.addElementHandler( "CafeMethodBody", getCafeMethodBodyHandler() );
			serverObjFuncHandler.addElementHandler( "CPlusMethodBody", getCPlusMethodBodyHandler() );
			serverObjFuncHandler.addElementHandler( "CSharpMethodBody", getCSharpMethodBodyHandler() );
		}
		return( serverObjFuncHandler );
	}

	protected CFBamXmlLoaderServerProcHandler getServerProcHandler() {
		if( serverProcHandler == null ) {
			serverProcHandler = new CFBamXmlLoaderServerProcHandler( this );
			serverProcHandler.addElementHandler( "Param", getParamHandler() );
			serverProcHandler.addElementHandler( "CafeMethodBody", getCafeMethodBodyHandler() );
			serverProcHandler.addElementHandler( "CPlusMethodBody", getCPlusMethodBodyHandler() );
			serverProcHandler.addElementHandler( "CSharpMethodBody", getCSharpMethodBodyHandler() );
		}
		return( serverProcHandler );
	}

	protected CFBamXmlLoaderStringColHandler getStringColHandler() {
		if( stringColHandler == null ) {
			stringColHandler = new CFBamXmlLoaderStringColHandler( this );
		}
		return( stringColHandler );
	}

	protected CFBamXmlLoaderStringTypeHandler getStringTypeHandler() {
		if( stringTypeHandler == null ) {
			stringTypeHandler = new CFBamXmlLoaderStringTypeHandler( this );
		}
		return( stringTypeHandler );
	}

	protected CFBamXmlLoaderSubProjectHandler getSubProjectHandler() {
		if( subProjectHandler == null ) {
			subProjectHandler = new CFBamXmlLoaderSubProjectHandler( this );
			subProjectHandler.addElementHandler( "MajorVersion", getMajorVersionHandler() );
		}
		return( subProjectHandler );
	}

	protected CFBamXmlLoaderSuperClassRelationHandler getSuperClassRelationHandler() {
		if( superClassRelationHandler == null ) {
			superClassRelationHandler = new CFBamXmlLoaderSuperClassRelationHandler( this );
			superClassRelationHandler.addElementHandler( "RelationCol", getRelationColHandler() );
		}
		return( superClassRelationHandler );
	}

	protected CFBamXmlLoaderTZDateColHandler getTZDateColHandler() {
		if( tZDateColHandler == null ) {
			tZDateColHandler = new CFBamXmlLoaderTZDateColHandler( this );
		}
		return( tZDateColHandler );
	}

	protected CFBamXmlLoaderTZDateTypeHandler getTZDateTypeHandler() {
		if( tZDateTypeHandler == null ) {
			tZDateTypeHandler = new CFBamXmlLoaderTZDateTypeHandler( this );
		}
		return( tZDateTypeHandler );
	}

	protected CFBamXmlLoaderTZTimeColHandler getTZTimeColHandler() {
		if( tZTimeColHandler == null ) {
			tZTimeColHandler = new CFBamXmlLoaderTZTimeColHandler( this );
		}
		return( tZTimeColHandler );
	}

	protected CFBamXmlLoaderTZTimeTypeHandler getTZTimeTypeHandler() {
		if( tZTimeTypeHandler == null ) {
			tZTimeTypeHandler = new CFBamXmlLoaderTZTimeTypeHandler( this );
		}
		return( tZTimeTypeHandler );
	}

	protected CFBamXmlLoaderTZTimestampColHandler getTZTimestampColHandler() {
		if( tZTimestampColHandler == null ) {
			tZTimestampColHandler = new CFBamXmlLoaderTZTimestampColHandler( this );
		}
		return( tZTimestampColHandler );
	}

	protected CFBamXmlLoaderTZTimestampTypeHandler getTZTimestampTypeHandler() {
		if( tZTimestampTypeHandler == null ) {
			tZTimestampTypeHandler = new CFBamXmlLoaderTZTimestampTypeHandler( this );
		}
		return( tZTimestampTypeHandler );
	}

	protected CFBamXmlLoaderTableHandler getTableHandler() {
		if( tableHandler == null ) {
			tableHandler = new CFBamXmlLoaderTableHandler( this );
			tableHandler.addElementHandler( "ServerListFunc", getServerListFuncHandler() );
			tableHandler.addElementHandler( "ServerObjFunc", getServerObjFuncHandler() );
			tableHandler.addElementHandler( "ServerProc", getServerProcHandler() );
			tableHandler.addElementHandler( "Relation", getRelationHandler() );
			tableHandler.addElementHandler( "SuperClassRelation", getSuperClassRelationHandler() );
			tableHandler.addElementHandler( "TableCol", getTableColHandler() );
			tableHandler.addElementHandler( "BlobCol", getBlobColHandler() );
			tableHandler.addElementHandler( "BoolCol", getBoolColHandler() );
			tableHandler.addElementHandler( "Int16Col", getInt16ColHandler() );
			tableHandler.addElementHandler( "Int32Col", getInt32ColHandler() );
			tableHandler.addElementHandler( "Int64Col", getInt64ColHandler() );
			tableHandler.addElementHandler( "UInt16Col", getUInt16ColHandler() );
			tableHandler.addElementHandler( "UInt32Col", getUInt32ColHandler() );
			tableHandler.addElementHandler( "UInt64Col", getUInt64ColHandler() );
			tableHandler.addElementHandler( "FloatCol", getFloatColHandler() );
			tableHandler.addElementHandler( "DoubleCol", getDoubleColHandler() );
			tableHandler.addElementHandler( "NumberCol", getNumberColHandler() );
			tableHandler.addElementHandler( "StringCol", getStringColHandler() );
			tableHandler.addElementHandler( "TextCol", getTextColHandler() );
			tableHandler.addElementHandler( "NmTokenCol", getNmTokenColHandler() );
			tableHandler.addElementHandler( "NmTokensCol", getNmTokensColHandler() );
			tableHandler.addElementHandler( "TokenCol", getTokenColHandler() );
			tableHandler.addElementHandler( "DateCol", getDateColHandler() );
			tableHandler.addElementHandler( "TimeCol", getTimeColHandler() );
			tableHandler.addElementHandler( "TimestampCol", getTimestampColHandler() );
			tableHandler.addElementHandler( "TZDateCol", getTZDateColHandler() );
			tableHandler.addElementHandler( "TZTimeCol", getTZTimeColHandler() );
			tableHandler.addElementHandler( "TZTimestampCol", getTZTimestampColHandler() );
			tableHandler.addElementHandler( "UuidCol", getUuidColHandler() );
			tableHandler.addElementHandler( "Index", getIndexHandler() );
			tableHandler.addElementHandler( "PrimaryIndex", getPrimaryIndexHandler() );
			tableHandler.addElementHandler( "Chain", getChainHandler() );
			tableHandler.addElementHandler( "ClearDep", getClearDepHandler() );
			tableHandler.addElementHandler( "DelDep", getDelDepHandler() );
			tableHandler.addElementHandler( "CafeObjMembers", getCafeObjMembersHandler() );
			tableHandler.addElementHandler( "CafeObjInterface", getCafeObjInterfaceHandler() );
			tableHandler.addElementHandler( "CafeObjImplementation", getCafeObjImplementationHandler() );
			tableHandler.addElementHandler( "CafeEditObjMembers", getCafeEditObjMembersHandler() );
			tableHandler.addElementHandler( "CafeEditObjInterface", getCafeEditObjInterfaceHandler() );
			tableHandler.addElementHandler( "CafeEditObjImplementation", getCafeEditObjImplementationHandler() );
			tableHandler.addElementHandler( "CafeDb2LUWTableImport", getCafeDb2LUWTableImportHandler() );
			tableHandler.addElementHandler( "CafeMSSqlTableImport", getCafeMSSqlTableImportHandler() );
			tableHandler.addElementHandler( "CafeMySqlTableImport", getCafeMySqlTableImportHandler() );
			tableHandler.addElementHandler( "CafeOracleTableImport", getCafeOracleTableImportHandler() );
			tableHandler.addElementHandler( "CafePgSqlTableImport", getCafePgSqlTableImportHandler() );
			tableHandler.addElementHandler( "CafeRamTableImport", getCafeRamTableImportHandler() );
			tableHandler.addElementHandler( "CafeObjImport", getCafeObjImportHandler() );
			tableHandler.addElementHandler( "CafeEditObjImport", getCafeEditObjImportHandler() );
			tableHandler.addElementHandler( "CafeSaxLoaderImport", getCafeSaxLoaderImportHandler() );
			tableHandler.addElementHandler( "CafeTableImport", getCafeTableImportHandler() );
			tableHandler.addElementHandler( "CafeTableObjImport", getCafeTableObjImportHandler() );
			tableHandler.addElementHandler( "CafeTableMembers", getCafeTableMembersHandler() );
			tableHandler.addElementHandler( "CafeTableInterface", getCafeTableInterfaceHandler() );
			tableHandler.addElementHandler( "CafeTableImplementation", getCafeTableImplementationHandler() );
			tableHandler.addElementHandler( "CafeTableObjMembers", getCafeTableObjMembersHandler() );
			tableHandler.addElementHandler( "CafeTableObjInterface", getCafeTableObjInterfaceHandler() );
			tableHandler.addElementHandler( "CafeTableObjImplementation", getCafeTableObjImplementationHandler() );
			tableHandler.addElementHandler( "CafeDb2LUWTableMembers", getCafeDb2LUWTableMembersHandler() );
			tableHandler.addElementHandler( "CafeDb2LUWTableImplementation", getCafeDb2LUWTableImplementationHandler() );
			tableHandler.addElementHandler( "CafeMSSqlTableMembers", getCafeMSSqlTableMembersHandler() );
			tableHandler.addElementHandler( "CafeMSSqlTableImplementation", getCafeMSSqlTableImplementationHandler() );
			tableHandler.addElementHandler( "CafeMySqlTableMembers", getCafeMySqlTableMembersHandler() );
			tableHandler.addElementHandler( "CafeMySqlTableImplementation", getCafeMySqlTableImplementationHandler() );
			tableHandler.addElementHandler( "CafeOracleTableMembers", getCafeOracleTableMembersHandler() );
			tableHandler.addElementHandler( "CafeOracleTableImplementation", getCafeOracleTableImplementationHandler() );
			tableHandler.addElementHandler( "CafePgSqlTableMembers", getCafePgSqlTableMembersHandler() );
			tableHandler.addElementHandler( "CafePgSqlTableImplementation", getCafePgSqlTableImplementationHandler() );
			tableHandler.addElementHandler( "CafeRamTableMembers", getCafeRamTableMembersHandler() );
			tableHandler.addElementHandler( "CafeRamTableImplementation", getCafeRamTableImplementationHandler() );
			tableHandler.addElementHandler( "CafeSaxLoaderStartElement", getCafeSaxLoaderStartElementHandler() );
			tableHandler.addElementHandler( "CafeSaxLoaderEndElement", getCafeSaxLoaderEndElementHandler() );
			tableHandler.addElementHandler( "CafeXMsgTableImport", getCafeXMsgTableImportHandler() );
			tableHandler.addElementHandler( "CafeXMsgTableFormatters", getCafeXMsgTableFormattersHandler() );
			tableHandler.addElementHandler( "CafeXMsgRqstTableImport", getCafeXMsgRqstTableImportHandler() );
			tableHandler.addElementHandler( "CafeXMsgRspnTableImport", getCafeXMsgRspnTableImportHandler() );
			tableHandler.addElementHandler( "CafeXMsgClientTableImport", getCafeXMsgClientTableImportHandler() );
			tableHandler.addElementHandler( "CafeXMsgRqstTableBody", getCafeXMsgRqstTableBodyHandler() );
			tableHandler.addElementHandler( "CafeXMsgRspnTableBody", getCafeXMsgRspnTableBodyHandler() );
			tableHandler.addElementHandler( "CafeXMsgClientTableBody", getCafeXMsgClientTableBodyHandler() );
			tableHandler.addElementHandler( "CPlusObjMembers", getCPlusObjMembersHandler() );
			tableHandler.addElementHandler( "CPlusObjInterface", getCPlusObjInterfaceHandler() );
			tableHandler.addElementHandler( "CPlusObjImplementation", getCPlusObjImplementationHandler() );
			tableHandler.addElementHandler( "CPlusEditObjMembers", getCPlusEditObjMembersHandler() );
			tableHandler.addElementHandler( "CPlusEditObjInterface", getCPlusEditObjInterfaceHandler() );
			tableHandler.addElementHandler( "CPlusEditObjImplementation", getCPlusEditObjImplementationHandler() );
			tableHandler.addElementHandler( "CPlusDb2LUWTableInclude", getCPlusDb2LUWTableIncludeHandler() );
			tableHandler.addElementHandler( "CPlusMSSqlTableInclude", getCPlusMSSqlTableIncludeHandler() );
			tableHandler.addElementHandler( "CPlusMySqlTableInclude", getCPlusMySqlTableIncludeHandler() );
			tableHandler.addElementHandler( "CPlusOracleTableInclude", getCPlusOracleTableIncludeHandler() );
			tableHandler.addElementHandler( "CPlusPgSqlTableInclude", getCPlusPgSqlTableIncludeHandler() );
			tableHandler.addElementHandler( "CPlusRamTableInclude", getCPlusRamTableIncludeHandler() );
			tableHandler.addElementHandler( "CPlusObjInclude", getCPlusObjIncludeHandler() );
			tableHandler.addElementHandler( "CPlusEditObjInclude", getCPlusEditObjIncludeHandler() );
			tableHandler.addElementHandler( "CPlusSaxLoaderInclude", getCPlusSaxLoaderIncludeHandler() );
			tableHandler.addElementHandler( "CPlusTableInclude", getCPlusTableIncludeHandler() );
			tableHandler.addElementHandler( "CPlusTableObjInclude", getCPlusTableObjIncludeHandler() );
			tableHandler.addElementHandler( "CPlusTableMembers", getCPlusTableMembersHandler() );
			tableHandler.addElementHandler( "CPlusTableInterface", getCPlusTableInterfaceHandler() );
			tableHandler.addElementHandler( "CPlusTableImplementation", getCPlusTableImplementationHandler() );
			tableHandler.addElementHandler( "CPlusTableObjMembers", getCPlusTableObjMembersHandler() );
			tableHandler.addElementHandler( "CPlusTableObjInterface", getCPlusTableObjInterfaceHandler() );
			tableHandler.addElementHandler( "CPlusTableObjImplementation", getCPlusTableObjImplementationHandler() );
			tableHandler.addElementHandler( "CPlusDb2LUWTableMembers", getCPlusDb2LUWTableMembersHandler() );
			tableHandler.addElementHandler( "CPlusDb2LUWTableImplementation", getCPlusDb2LUWTableImplementationHandler() );
			tableHandler.addElementHandler( "CPlusMSSqlTableMembers", getCPlusMSSqlTableMembersHandler() );
			tableHandler.addElementHandler( "CPlusMSSqlTableImplementation", getCPlusMSSqlTableImplementationHandler() );
			tableHandler.addElementHandler( "CPlusMySqlTableMembers", getCPlusMySqlTableMembersHandler() );
			tableHandler.addElementHandler( "CPlusMySqlTableImplementation", getCPlusMySqlTableImplementationHandler() );
			tableHandler.addElementHandler( "CPlusOracleTableMembers", getCPlusOracleTableMembersHandler() );
			tableHandler.addElementHandler( "CPlusOracleTableImplementation", getCPlusOracleTableImplementationHandler() );
			tableHandler.addElementHandler( "CPlusPgSqlTableMembers", getCPlusPgSqlTableMembersHandler() );
			tableHandler.addElementHandler( "CPlusPgSqlTableImplementation", getCPlusPgSqlTableImplementationHandler() );
			tableHandler.addElementHandler( "CPlusRamTableMembers", getCPlusRamTableMembersHandler() );
			tableHandler.addElementHandler( "CPlusRamTableImplementation", getCPlusRamTableImplementationHandler() );
			tableHandler.addElementHandler( "CPlusSaxLoaderStartElement", getCPlusSaxLoaderStartElementHandler() );
			tableHandler.addElementHandler( "CPlusSaxLoaderEndElement", getCPlusSaxLoaderEndElementHandler() );
			tableHandler.addElementHandler( "CPlusXMsgTableInclude", getCPlusXMsgTableIncludeHandler() );
			tableHandler.addElementHandler( "CPlusXMsgTableFormatters", getCPlusXMsgTableFormattersHandler() );
			tableHandler.addElementHandler( "CPlusXMsgRqstTableInclude", getCPlusXMsgRqstTableIncludeHandler() );
			tableHandler.addElementHandler( "CPlusXMsgRspnTableInclude", getCPlusXMsgRspnTableIncludeHandler() );
			tableHandler.addElementHandler( "CPlusXMsgClientTableInclude", getCPlusXMsgClientTableIncludeHandler() );
			tableHandler.addElementHandler( "CPlusXMsgRqstTableBody", getCPlusXMsgRqstTableBodyHandler() );
			tableHandler.addElementHandler( "CPlusXMsgRspnTableBody", getCPlusXMsgRspnTableBodyHandler() );
			tableHandler.addElementHandler( "CPlusXMsgClientTableBody", getCPlusXMsgClientTableBodyHandler() );
			tableHandler.addElementHandler( "HPlusObjMembers", getHPlusObjMembersHandler() );
			tableHandler.addElementHandler( "HPlusObjInterface", getHPlusObjInterfaceHandler() );
			tableHandler.addElementHandler( "HPlusObjImplementation", getHPlusObjImplementationHandler() );
			tableHandler.addElementHandler( "HPlusEditObjMembers", getHPlusEditObjMembersHandler() );
			tableHandler.addElementHandler( "HPlusEditObjInterface", getHPlusEditObjInterfaceHandler() );
			tableHandler.addElementHandler( "HPlusEditObjImplementation", getHPlusEditObjImplementationHandler() );
			tableHandler.addElementHandler( "HPlusDb2LUWTableInclude", getHPlusDb2LUWTableIncludeHandler() );
			tableHandler.addElementHandler( "HPlusMSSqlTableInclude", getHPlusMSSqlTableIncludeHandler() );
			tableHandler.addElementHandler( "HPlusMySqlTableInclude", getHPlusMySqlTableIncludeHandler() );
			tableHandler.addElementHandler( "HPlusOracleTableInclude", getHPlusOracleTableIncludeHandler() );
			tableHandler.addElementHandler( "HPlusPgSqlTableInclude", getHPlusPgSqlTableIncludeHandler() );
			tableHandler.addElementHandler( "HPlusRamTableInclude", getHPlusRamTableIncludeHandler() );
			tableHandler.addElementHandler( "HPlusObjInclude", getHPlusObjIncludeHandler() );
			tableHandler.addElementHandler( "HPlusEditObjInclude", getHPlusEditObjIncludeHandler() );
			tableHandler.addElementHandler( "HPlusSaxLoaderInclude", getHPlusSaxLoaderIncludeHandler() );
			tableHandler.addElementHandler( "HPlusTableInclude", getHPlusTableIncludeHandler() );
			tableHandler.addElementHandler( "HPlusTableObjInclude", getHPlusTableObjIncludeHandler() );
			tableHandler.addElementHandler( "HPlusTableMembers", getHPlusTableMembersHandler() );
			tableHandler.addElementHandler( "HPlusTableInterface", getHPlusTableInterfaceHandler() );
			tableHandler.addElementHandler( "HPlusTableImplementation", getHPlusTableImplementationHandler() );
			tableHandler.addElementHandler( "HPlusTableObjMembers", getHPlusTableObjMembersHandler() );
			tableHandler.addElementHandler( "HPlusTableObjInterface", getHPlusTableObjInterfaceHandler() );
			tableHandler.addElementHandler( "HPlusTableObjImplementation", getHPlusTableObjImplementationHandler() );
			tableHandler.addElementHandler( "HPlusDb2LUWTableMembers", getHPlusDb2LUWTableMembersHandler() );
			tableHandler.addElementHandler( "HPlusDb2LUWTableImplementation", getHPlusDb2LUWTableImplementationHandler() );
			tableHandler.addElementHandler( "HPlusMSSqlTableMembers", getHPlusMSSqlTableMembersHandler() );
			tableHandler.addElementHandler( "HPlusMSSqlTableImplementation", getHPlusMSSqlTableImplementationHandler() );
			tableHandler.addElementHandler( "HPlusMySqlTableMembers", getHPlusMySqlTableMembersHandler() );
			tableHandler.addElementHandler( "HPlusMySqlTableImplementation", getHPlusMySqlTableImplementationHandler() );
			tableHandler.addElementHandler( "HPlusOracleTableMembers", getHPlusOracleTableMembersHandler() );
			tableHandler.addElementHandler( "HPlusOracleTableImplementation", getHPlusOracleTableImplementationHandler() );
			tableHandler.addElementHandler( "HPlusPgSqlTableMembers", getHPlusPgSqlTableMembersHandler() );
			tableHandler.addElementHandler( "HPlusPgSqlTableImplementation", getHPlusPgSqlTableImplementationHandler() );
			tableHandler.addElementHandler( "HPlusRamTableMembers", getHPlusRamTableMembersHandler() );
			tableHandler.addElementHandler( "HPlusRamTableImplementation", getHPlusRamTableImplementationHandler() );
			tableHandler.addElementHandler( "HPlusSaxLoaderStartElement", getHPlusSaxLoaderStartElementHandler() );
			tableHandler.addElementHandler( "HPlusSaxLoaderEndElement", getHPlusSaxLoaderEndElementHandler() );
			tableHandler.addElementHandler( "HPlusXMsgTableInclude", getHPlusXMsgTableIncludeHandler() );
			tableHandler.addElementHandler( "HPlusXMsgTableFormatters", getHPlusXMsgTableFormattersHandler() );
			tableHandler.addElementHandler( "HPlusXMsgRqstTableInclude", getHPlusXMsgRqstTableIncludeHandler() );
			tableHandler.addElementHandler( "HPlusXMsgRspnTableInclude", getHPlusXMsgRspnTableIncludeHandler() );
			tableHandler.addElementHandler( "HPlusXMsgClientTableInclude", getHPlusXMsgClientTableIncludeHandler() );
			tableHandler.addElementHandler( "HPlusXMsgRqstTableBody", getHPlusXMsgRqstTableBodyHandler() );
			tableHandler.addElementHandler( "HPlusXMsgRspnTableBody", getHPlusXMsgRspnTableBodyHandler() );
			tableHandler.addElementHandler( "HPlusXMsgClientTableBody", getHPlusXMsgClientTableBodyHandler() );
			tableHandler.addElementHandler( "CSharpObjMembers", getCSharpObjMembersHandler() );
			tableHandler.addElementHandler( "CSharpObjInterface", getCSharpObjInterfaceHandler() );
			tableHandler.addElementHandler( "CSharpObjImplementation", getCSharpObjImplementationHandler() );
			tableHandler.addElementHandler( "CSharpEditObjMembers", getCSharpEditObjMembersHandler() );
			tableHandler.addElementHandler( "CSharpEditObjInterface", getCSharpEditObjInterfaceHandler() );
			tableHandler.addElementHandler( "CSharpEditObjImplementation", getCSharpEditObjImplementationHandler() );
			tableHandler.addElementHandler( "CSharpDb2LUWTableUsing", getCSharpDb2LUWTableUsingHandler() );
			tableHandler.addElementHandler( "CSharpMSSqlTableUsing", getCSharpMSSqlTableUsingHandler() );
			tableHandler.addElementHandler( "CSharpMySqlTableUsing", getCSharpMySqlTableUsingHandler() );
			tableHandler.addElementHandler( "CSharpOracleTableUsing", getCSharpOracleTableUsingHandler() );
			tableHandler.addElementHandler( "CSharpPgSqlTableUsing", getCSharpPgSqlTableUsingHandler() );
			tableHandler.addElementHandler( "CSharpRamTableUsing", getCSharpRamTableUsingHandler() );
			tableHandler.addElementHandler( "CSharpObjUsing", getCSharpObjUsingHandler() );
			tableHandler.addElementHandler( "CSharpEditObjUsing", getCSharpEditObjUsingHandler() );
			tableHandler.addElementHandler( "CSharpSaxLoaderUsing", getCSharpSaxLoaderUsingHandler() );
			tableHandler.addElementHandler( "CSharpTableUsing", getCSharpTableUsingHandler() );
			tableHandler.addElementHandler( "CSharpTableObjUsing", getCSharpTableObjUsingHandler() );
			tableHandler.addElementHandler( "CSharpTableMembers", getCSharpTableMembersHandler() );
			tableHandler.addElementHandler( "CSharpTableInterface", getCSharpTableInterfaceHandler() );
			tableHandler.addElementHandler( "CSharpTableImplementation", getCSharpTableImplementationHandler() );
			tableHandler.addElementHandler( "CSharpTableObjMembers", getCSharpTableObjMembersHandler() );
			tableHandler.addElementHandler( "CSharpTableObjInterface", getCSharpTableObjInterfaceHandler() );
			tableHandler.addElementHandler( "CSharpTableObjImplementation", getCSharpTableObjImplementationHandler() );
			tableHandler.addElementHandler( "CSharpDb2LUWTableMembers", getCSharpDb2LUWTableMembersHandler() );
			tableHandler.addElementHandler( "CSharpDb2LUWTableImplementation", getCSharpDb2LUWTableImplementationHandler() );
			tableHandler.addElementHandler( "CSharpMSSqlTableMembers", getCSharpMSSqlTableMembersHandler() );
			tableHandler.addElementHandler( "CSharpMSSqlTableImplementation", getCSharpMSSqlTableImplementationHandler() );
			tableHandler.addElementHandler( "CSharpMySqlTableMembers", getCSharpMySqlTableMembersHandler() );
			tableHandler.addElementHandler( "CSharpMySqlTableImplementation", getCSharpMySqlTableImplementationHandler() );
			tableHandler.addElementHandler( "CSharpOracleTableMembers", getCSharpOracleTableMembersHandler() );
			tableHandler.addElementHandler( "CSharpOracleTableImplementation", getCSharpOracleTableImplementationHandler() );
			tableHandler.addElementHandler( "CSharpPgSqlTableMembers", getCSharpPgSqlTableMembersHandler() );
			tableHandler.addElementHandler( "CSharpPgSqlTableImplementation", getCSharpPgSqlTableImplementationHandler() );
			tableHandler.addElementHandler( "CSharpRamTableMembers", getCSharpRamTableMembersHandler() );
			tableHandler.addElementHandler( "CSharpRamTableImplementation", getCSharpRamTableImplementationHandler() );
			tableHandler.addElementHandler( "CSharpSaxLoaderStartElement", getCSharpSaxLoaderStartElementHandler() );
			tableHandler.addElementHandler( "CSharpSaxLoaderEndElement", getCSharpSaxLoaderEndElementHandler() );
			tableHandler.addElementHandler( "CSharpXMsgTableUsing", getCSharpXMsgTableUsingHandler() );
			tableHandler.addElementHandler( "CSharpXMsgTableFormatters", getCSharpXMsgTableFormattersHandler() );
			tableHandler.addElementHandler( "CSharpXMsgRqstTableUsing", getCSharpXMsgRqstTableUsingHandler() );
			tableHandler.addElementHandler( "CSharpXMsgRspnTableUsing", getCSharpXMsgRspnTableUsingHandler() );
			tableHandler.addElementHandler( "CSharpXMsgClientTableUsing", getCSharpXMsgClientTableUsingHandler() );
			tableHandler.addElementHandler( "CSharpXMsgRqstTableBody", getCSharpXMsgRqstTableBodyHandler() );
			tableHandler.addElementHandler( "CSharpXMsgRspnTableBody", getCSharpXMsgRspnTableBodyHandler() );
			tableHandler.addElementHandler( "CSharpXMsgClientTableBody", getCSharpXMsgClientTableBodyHandler() );
		}
		return( tableHandler );
	}

	protected CFBamXmlLoaderTableAddendumHandler getTableAddendumHandler() {
		if( tableAddendumHandler == null ) {
			tableAddendumHandler = new CFBamXmlLoaderTableAddendumHandler( this );
			tableAddendumHandler.addElementHandler( "ServerListFunc", getServerListFuncHandler() );
			tableAddendumHandler.addElementHandler( "ServerObjFunc", getServerObjFuncHandler() );
			tableAddendumHandler.addElementHandler( "ServerProc", getServerProcHandler() );
			tableAddendumHandler.addElementHandler( "SuperClassRelation", getSuperClassRelationHandler() );
			tableAddendumHandler.addElementHandler( "Relation", getRelationHandler() );
			tableAddendumHandler.addElementHandler( "Index", getIndexHandler() );
			tableAddendumHandler.addElementHandler( "Chain", getChainHandler() );
			tableAddendumHandler.addElementHandler( "ClearDep", getClearDepHandler() );
			tableAddendumHandler.addElementHandler( "DelDep", getDelDepHandler() );
		}
		return( tableAddendumHandler );
	}

	protected CFBamXmlLoaderTableColHandler getTableColHandler() {
		if( tableColHandler == null ) {
			tableColHandler = new CFBamXmlLoaderTableColHandler( this );
		}
		return( tableColHandler );
	}

	protected CFBamXmlLoaderTextColHandler getTextColHandler() {
		if( textColHandler == null ) {
			textColHandler = new CFBamXmlLoaderTextColHandler( this );
		}
		return( textColHandler );
	}

	protected CFBamXmlLoaderTextTypeHandler getTextTypeHandler() {
		if( textTypeHandler == null ) {
			textTypeHandler = new CFBamXmlLoaderTextTypeHandler( this );
		}
		return( textTypeHandler );
	}

	protected CFBamXmlLoaderTimeColHandler getTimeColHandler() {
		if( timeColHandler == null ) {
			timeColHandler = new CFBamXmlLoaderTimeColHandler( this );
		}
		return( timeColHandler );
	}

	protected CFBamXmlLoaderTimeTypeHandler getTimeTypeHandler() {
		if( timeTypeHandler == null ) {
			timeTypeHandler = new CFBamXmlLoaderTimeTypeHandler( this );
		}
		return( timeTypeHandler );
	}

	protected CFBamXmlLoaderTimestampColHandler getTimestampColHandler() {
		if( timestampColHandler == null ) {
			timestampColHandler = new CFBamXmlLoaderTimestampColHandler( this );
		}
		return( timestampColHandler );
	}

	protected CFBamXmlLoaderTimestampTypeHandler getTimestampTypeHandler() {
		if( timestampTypeHandler == null ) {
			timestampTypeHandler = new CFBamXmlLoaderTimestampTypeHandler( this );
		}
		return( timestampTypeHandler );
	}

	protected CFBamXmlLoaderTldHandler getTldHandler() {
		if( tldHandler == null ) {
			tldHandler = new CFBamXmlLoaderTldHandler( this );
			tldHandler.addElementHandler( "TopDomain", getTopDomainHandler() );
		}
		return( tldHandler );
	}

	protected CFBamXmlLoaderTokenColHandler getTokenColHandler() {
		if( tokenColHandler == null ) {
			tokenColHandler = new CFBamXmlLoaderTokenColHandler( this );
		}
		return( tokenColHandler );
	}

	protected CFBamXmlLoaderTokenTypeHandler getTokenTypeHandler() {
		if( tokenTypeHandler == null ) {
			tokenTypeHandler = new CFBamXmlLoaderTokenTypeHandler( this );
		}
		return( tokenTypeHandler );
	}

	protected CFBamXmlLoaderTopDomainHandler getTopDomainHandler() {
		if( topDomainHandler == null ) {
			topDomainHandler = new CFBamXmlLoaderTopDomainHandler( this );
			topDomainHandler.addElementHandler( "TopProject", getTopProjectHandler() );
			topDomainHandler.addElementHandler( "License", getLicenseHandler() );
		}
		return( topDomainHandler );
	}

	protected CFBamXmlLoaderTopProjectHandler getTopProjectHandler() {
		if( topProjectHandler == null ) {
			topProjectHandler = new CFBamXmlLoaderTopProjectHandler( this );
			topProjectHandler.addElementHandler( "SubProject", getSubProjectHandler() );
		}
		return( topProjectHandler );
	}

	protected CFBamXmlLoaderUInt16ColHandler getUInt16ColHandler() {
		if( uInt16ColHandler == null ) {
			uInt16ColHandler = new CFBamXmlLoaderUInt16ColHandler( this );
		}
		return( uInt16ColHandler );
	}

	protected CFBamXmlLoaderUInt16TypeHandler getUInt16TypeHandler() {
		if( uInt16TypeHandler == null ) {
			uInt16TypeHandler = new CFBamXmlLoaderUInt16TypeHandler( this );
		}
		return( uInt16TypeHandler );
	}

	protected CFBamXmlLoaderUInt32ColHandler getUInt32ColHandler() {
		if( uInt32ColHandler == null ) {
			uInt32ColHandler = new CFBamXmlLoaderUInt32ColHandler( this );
		}
		return( uInt32ColHandler );
	}

	protected CFBamXmlLoaderUInt32TypeHandler getUInt32TypeHandler() {
		if( uInt32TypeHandler == null ) {
			uInt32TypeHandler = new CFBamXmlLoaderUInt32TypeHandler( this );
		}
		return( uInt32TypeHandler );
	}

	protected CFBamXmlLoaderUInt64ColHandler getUInt64ColHandler() {
		if( uInt64ColHandler == null ) {
			uInt64ColHandler = new CFBamXmlLoaderUInt64ColHandler( this );
		}
		return( uInt64ColHandler );
	}

	protected CFBamXmlLoaderUInt64TypeHandler getUInt64TypeHandler() {
		if( uInt64TypeHandler == null ) {
			uInt64TypeHandler = new CFBamXmlLoaderUInt64TypeHandler( this );
		}
		return( uInt64TypeHandler );
	}

	protected CFBamXmlLoaderUuidColHandler getUuidColHandler() {
		if( uuidColHandler == null ) {
			uuidColHandler = new CFBamXmlLoaderUuidColHandler( this );
		}
		return( uuidColHandler );
	}

	protected CFBamXmlLoaderUuidGenHandler getUuidGenHandler() {
		if( uuidGenHandler == null ) {
			uuidGenHandler = new CFBamXmlLoaderUuidGenHandler( this );
		}
		return( uuidGenHandler );
	}

	protected CFBamXmlLoaderUuidTypeHandler getUuidTypeHandler() {
		if( uuidTypeHandler == null ) {
			uuidTypeHandler = new CFBamXmlLoaderUuidTypeHandler( this );
		}
		return( uuidTypeHandler );
	}

	// Root Element Handler Resolver Factory

	protected CFBamSaxRootHandler getSaxRootHandler() {
		if( saxRootHandler == null ) {
			saxRootHandler = new CFBamSaxRootHandler( this );
			saxRootHandler.addElementHandler( "MSSBam", getSaxDocHandler() );
		}
		return( saxRootHandler );
	}

	// Root Element Handler

	/*
	 *	CFBamSaxRootHandler XML SAX Root Element Handler implementation
	 */
	public class CFBamSaxRootHandler
		extends CFLibXmlCoreElementHandler
	{
		public CFBamSaxRootHandler( CFBamXmlLoader saxLoader ) {
			super( saxLoader );
		}

		public void startElement(
			String		uri,
			String		localName,
			String		qName,
			Attributes	attrs )
		throws SAXException
		{
		}

		public void endElement(
			String		uri,
			String		localName,
			String		qName )
		throws SAXException
		{
		}
	}

	// Document Element Handler Resolver Factory

	protected CFBamSaxDocHandler getSaxDocHandler() {
		if( saxDocHandler == null ) {
			saxDocHandler = new CFBamSaxDocHandler( this );
			saxDocHandler.addElementHandler( "Tld", getTldHandler() );
		}
		return( saxDocHandler );
	}

	// Document Element Handler

	/*
	 *	CFBamSaxDocHandler XML SAX Doc Element Handler implementation
	 */
	public class CFBamSaxDocHandler
		extends CFLibXmlCoreElementHandler
	{
		public CFBamSaxDocHandler( CFBamXmlLoader saxLoader ) {
			super( saxLoader );
		}

		public void startElement(
			String		uri,
			String		localName,
			String		qName,
			Attributes	attrs )
		throws SAXException
		{
			// AnyObj attributes
			String	attrId = null;
			String	attrDescription = null;
			String	attrName = null;
			String	attrRevision = null;
			String	attrProjectRoot = null;

			// Attribute Extraction
			String	attrLocalName;
			int		numAttrs;
			int		idxAttr;

			final String S_ProcName = "startElement";
			final String S_LocalNameIndex = "LocalName";

			assert qName.equals( "MSSBam" );

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

			numAttrs = attrs.getLength();
			for( idxAttr = 0; idxAttr < numAttrs; idxAttr++ ) {
				attrLocalName = attrs.getLocalName( idxAttr );
				if( attrLocalName.equals( "Id" ) ) {
					if( attrId != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalNameIndex,
							attrLocalName );
					}
					attrId = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "Description" ) ) {
					if( attrDescription != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalNameIndex,
							attrLocalName );
					}
					attrDescription = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "Name" ) ) {
					if( attrName != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalNameIndex,
							attrLocalName );
					}
					attrName = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "Revision" ) ) {
					if( attrRevision != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalNameIndex,
							attrLocalName );
					}
					attrRevision = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "ProjectRoot" ) ) {
					if( attrProjectRoot != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalNameIndex,
							attrLocalName );
					}
					attrProjectRoot = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "schemaLocation" ) ) {
					// ignored
				}
				else {
					throw new CFLibUnrecognizedAttributeException( getClass(),
						S_ProcName,
						getLocationInfo(),
						attrLocalName );
				}
			}

			if( ( attrName == null ) || ( attrName.length() <= 0 ) ) {
				throw new CFLibEmptyArgumentException( getClass(),
					S_ProcName,
					0,
					"Name" );
			}

			if( ( attrRevision == null ) || ( attrRevision.length() <= 0 ) ) {
				throw new CFLibEmptyArgumentException( getClass(),
					S_ProcName,
					0,
					"Revision" );
			}

			if( ( attrDescription == null ) || ( attrDescription.length() <= 0 ) ) {
				throw new CFLibEmptyArgumentException( getClass(),
					S_ProcName,
					0,
					"Description" );
			}

			if( ( attrProjectRoot == null ) || ( attrProjectRoot.length() <= 0 ) ) {
				throw new CFLibEmptyArgumentException( getClass(),
					S_ProcName,
					0,
					"ProjectRoot" );
			}

			getLog().message( "Loading MSSBam Name=\"" + attrName
				+ "\", Revision = \"" + attrRevision
				+ "\", Description=\"" + attrDescription
				+ "\", ProjectRoot=\"" + attrProjectRoot + "\"\n" ); 

			CFLibXmlCoreContext curContext = getParser().getCurContext();
			curContext.putNamedValue( "Id", attrId );
			curContext.putNamedValue( "Description", attrDescription );
			curContext.putNamedValue( "Name", attrName );
			curContext.putNamedValue( "Revision", attrRevision );
			curContext.putNamedValue( "ProjectRoot", attrProjectRoot );

			curContext.putNamedValue( "Object", getTenant() );
		}

		public void endElement(
			String		uri,
			String		localName,
			String		qName )
		throws SAXException
		{
			final String S_ProcName = "endElement";
			CFLibXmlCoreContext curContext = getParser().getCurContext();
			String projectRootName = (String)curContext.getNamedValue( "ProjectRoot" );
			String name = (String)curContext.getNamedValue( "Name" );
			String description = (String)curContext.getNamedValue( "Description" );
			ICFBamTenantObj tenant = (ICFBamTenantObj)curContext.getNamedValue( "Object" );
			if( tenant != null ) {
				ICFLibAnyObj rootDef;
				try {
					rootDef = tenant.getNamedObject( projectRootName );
				}
				catch( RuntimeException e ) {
					rootDef = null;
				}

				if( rootDef instanceof ICFBamMajorVersionObj ) {
					definedProjectVersion = rootDef;
					getLog().message( "Parsed " + name + " " + description );
				}
				else if( rootDef instanceof ICFBamMinorVersionObj ) {
					definedProjectVersion = rootDef;
					getLog().message( "Parsed " + name + " " + description );
				}
				else {
					definedProjectVersion = null;
					throw new CFLibRuntimeException( getClass(),
						S_ProcName,
						getLocationInfo() + " Loaded Business Application Model does not define Project Root \"" + projectRootName + "\"" );
				}
			}
		}
	}

	// Parse a file

	public void parseFile( String url ) {
		parse( url );
	}
}
