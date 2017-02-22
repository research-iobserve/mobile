package org.iobserve.mobile.analysis;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.palladiosimulator.pcm.resourceenvironment.CommunicationLinkResourceSpecification;
import org.palladiosimulator.pcm.resourceenvironment.LinkingResource;

import privacy_ext.CommunicationLinkPrivacy;

public abstract class AbstractPalladioAnalyzer<T> {

	protected AbstractPalladioAnalyzer() {
	}

	public List<ConstraintViolation> analyze(PalladioInstance palladio) {
		List<ConstraintViolation> o = new ArrayList<>();
		List<T> l = this.extract(palladio);
		for (T i : l) {
			ConstraintViolation v = this.isViolation(i);
			if (v != null) {
				o.add(v);
			}
		}
		return o;
	}

	protected abstract List<T> extract(PalladioInstance instance);

	protected abstract ConstraintViolation isViolation(T value);

	protected List<CommunicationLinkPrivacy> getPrivacyLinks(PalladioInstance instance) {
		List<CommunicationLinkPrivacy> output = new ArrayList<>();
		List<LinkingResource> lResources = instance.environment.getLinkingResources__ResourceEnvironment();
		for (LinkingResource link : lResources) {
			CommunicationLinkResourceSpecification specification = link
					.getCommunicationLinkResourceSpecifications_LinkingResource();

			if (specification instanceof CommunicationLinkPrivacy) {
				output.add((CommunicationLinkPrivacy) specification);
			}
		}
		return output;
	}

	protected Set<String> provideFile(String filename) throws IOException {
		FileReader fileReader = new FileReader(filename);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		List<String> lines = new ArrayList<String>();
		String line = null;
		while ((line = bufferedReader.readLine()) != null) {
			lines.add(line);
		}
		bufferedReader.close();
		return new HashSet<>(lines);
	}

}
