/**
 * Copyright (C) 2015 Agro-Know, Deutsches Forschungszentrum für Künstliche Intelligenz, iMinds,
 * Institut für Angewandte Informatik e. V. an der Universität Leipzig,
 * Istituto Superiore Mario Boella, Tilde, Vistatec, WRIPL (http://freme-project.eu)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.freme.common.rest;

import eu.freme.common.conversion.rdf.RDFConstants;
import eu.freme.common.conversion.rdf.RDFConstants.RDFSerialization;
import eu.freme.common.exception.BadRequestException;
import eu.freme.common.exception.NIFVersionNotSupportedException;

/**
 * describes nif parameters as defined in
 * http://persistence.uni-leipzig.org/nlp2rdf/specification/api.html
 * 
 * @author Jan Nehring - jan.nehring@dfki.de
 */
public class NIFParameterSet {

	private String input;

	/**
	 * Possible values: values contained in {@link RDFConstants#SERIALIZATION_FORMATS}
	 * 					and {@link eu.freme.common.conversion.SerializationFormatMapper#PLAINTEXT}
	 */
	private String informat;

	/**
	 * Possible values: values contained in {@link RDFConstants#SERIALIZATION_FORMATS}
	 */
	private String outformat;
	private String prefix;
	private String nifVersion = RDFConstants.nifVersion2_0;

	/**
	 * @deprecated use {@link #NIFParameterSet(String, String, String, String)} instead
     */
	@Deprecated
	public NIFParameterSet(String input, RDFSerialization informat,
			RDFSerialization outformat, String prefix) {
		super();
		this.input = input;
		this.informat = informat.contentType();
		this.outformat = outformat.contentType();
		this.prefix = prefix;
	}
	public NIFParameterSet(String input, String informat,
						   String outformat, String prefix) {
		super();
		this.input = input;
		this.informat = informat;
		this.outformat = outformat;
		this.prefix = prefix;
	}

	public NIFParameterSet(String input, String informat,
						   String outformat, String prefix, String nifVersion) {
		super();
		this.input = input;
		this.informat = informat;
		this.outformat = outformat;
		this.prefix = prefix;
		setNifVersion(nifVersion);
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	/**
	 * @deprecated use {@link #getInformatString()} instead
	 */
	@Deprecated
	public RDFSerialization getInformat() {
		return RDFSerialization.fromValue(informat);
	}

	public String getInformatString() {
		return informat;
	}

	/**
	 * @deprecated use {@link #getInformatString()} instead
	 */
	@Deprecated
	public void setInformat(RDFSerialization informat) {
		this.informat = informat.contentType();
	}

	/**
	 * @deprecated use {@link #getOutformatString()} instead
	 */
	@Deprecated
	public RDFSerialization getOutformat() {
		return RDFSerialization.fromValue(outformat);
	}

	public String getOutformatString(){
		return outformat;
	}

	/**
	 * @deprecated use {@link #setOutformatString(String)} instead
	 */
	@Deprecated
	public void setOutformat(RDFSerialization outformat) {
		this.outformat = outformat.contentType();
	}

	public void setOutformatString(String outformat) {
		this.outformat = outformat;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getNifVersion() {
		return nifVersion;
	}

	public void setNifVersion(String nifVersion) {
		if (nifVersion != null
				&& !(nifVersion.equals(RDFConstants.nifVersion2_0)
				|| nifVersion.equals(RDFConstants.nifVersion2_1))) {
			throw new NIFVersionNotSupportedException("NIF version \""
					+ nifVersion + "\" is not supported");
		}
		this.nifVersion = nifVersion;
	}
}
