/***************************************************************************
 * Copyright (C) 2016 iObserve Project (https://www.iobserve-devops.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/
package org.iobserve.mobile.analysis.cli;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.iobserve.analysis.InitializeModelProviders;
import org.iobserve.analysis.model.AllocationModelProvider;
import org.iobserve.analysis.model.RepositoryModelProvider;
import org.iobserve.analysis.model.ResourceEnvironmentModelProvider;
import org.iobserve.analysis.model.SystemModelProvider;
import org.iobserve.analysis.model.UsageModelProvider;
import org.iobserve.mobile.analysis.AbstractPalladioAnalyzer;
import org.iobserve.mobile.analysis.ConstraintViolation;
import org.iobserve.mobile.analysis.PalladioInstance;

/**
 * Command Line Interface class for performing an analysis for a certain PCM
 * instance.
 * 
 * @author Robert Heinrich
 * @author David Monschein
 *
 */
public final class AnalysisStart {

	/**
	 * Pattern for constraint checker arguments.
	 */
	private static final Pattern CONSTRAINT_PATTERN = Pattern.compile("\\[(.*?)\\]");

	/**
	 * Contains the current command line object.
	 */
	private static CommandLine commandLine;

	/**
	 * No instance creation allowed.
	 */
	private AnalysisStart() {
	}

	/**
	 * Starts the analysis from given command line arguments.
	 * 
	 * @param args
	 *            command line arguments
	 */
	public static void main(final String[] args) {
		final CommandLineParser parser = new DefaultParser();

		try {
			commandLine = parser.parse(AnalysisStart.createHelpOptions(), args);
			if (commandLine.hasOption("h")) {
				final HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("iobserve-analysis", AnalysisStart.createOptions());
			} else {
				commandLine = parser.parse(AnalysisStart.createOptions(), args);

				/** process parameter. */
				final String constraintOption = commandLine.getOptionValue("c");

				if (constraintOption.length() > 0) {
					final File pcmModelsDirectory = new File(commandLine.getOptionValue("p"));
					if (pcmModelsDirectory.exists()) {
						final InitializeModelProviders modelProviderPlatform = new InitializeModelProviders(
								pcmModelsDirectory);

						final RepositoryModelProvider repositoryModelProvider = modelProviderPlatform
								.getRepositoryModelProvider();
						final UsageModelProvider usageModelProvider = modelProviderPlatform.getUsageModelProvider();
						final ResourceEnvironmentModelProvider resourceEvnironmentModelProvider = modelProviderPlatform
								.getResourceEnvironmentModelProvider();
						final AllocationModelProvider allocationModelProvider = modelProviderPlatform
								.getAllocationModelProvider();
						final SystemModelProvider systemModelProvider = modelProviderPlatform.getSystemModelProvider();

						final PalladioInstance instance = new PalladioInstance();
						instance.allocation = allocationModelProvider.getModel();
						instance.environment = resourceEvnironmentModelProvider.getModel();
						instance.system = systemModelProvider.getModel();
						instance.usageModel = usageModelProvider.getModel();
						instance.repository = repositoryModelProvider.getModel();

						final List<ConstraintViolation> violations = new ArrayList<>();
						final Matcher constraintMatcher = CONSTRAINT_PATTERN.matcher(constraintOption);
						if (constraintMatcher.find()) {
							final String[] classes = constraintMatcher.group(1).split(",");
							for (String clazz : classes) {
								final String[] argSplit = clazz.split("\\(");

								processClassArgument(instance, argSplit, violations);
							}
						}

						for (ConstraintViolation violation : violations) {
							System.out.println("Constraint violation: " + violation.getMessage());
						}

					}
				}
			}
		} catch (ParseException e) {
			System.err.println("CLI error: " + e.getMessage());
			final HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("iobserve-analysis", AnalysisStart.createOptions());
		}

	}

	/**
	 * Instantiates an analyzer class and executes it.
	 * 
	 * @param instance
	 *            the palladio instance which should be analyzed
	 * @param argSplit
	 *            the argument split into the class name and the parameters
	 * @param violations
	 *            the list which contains the violations
	 */
	private static void processClassArgument(final PalladioInstance instance, final String[] argSplit,
			final List<ConstraintViolation> violations) {
		if (argSplit.length == 2) {
			final String[] argv = argSplit[1].substring(0, argSplit[1].length() - 1).split(";");
			final Class<?>[] types = new Class<?>[argv.length];
			for (int i = 0; i < types.length; i++) {
				types[i] = String.class;
			}

			try {
				final Class<?> resolved = Class.forName(argSplit[0]);
				final Constructor<?> constructor = resolved.getConstructor(types);

				final Object obj = constructor.newInstance((Object[]) argv);
				if (AbstractPalladioAnalyzer.class.isAssignableFrom(obj.getClass())) {
					final AbstractPalladioAnalyzer<?> alz = (AbstractPalladioAnalyzer<?>) obj;
					violations.addAll(alz.analyze(instance));
				}
			} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
					| IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
		} else {
			try {
				final Class<?> resolved = Class.forName(argSplit[0]);
				final Constructor<?> constructor = resolved.getConstructor();

				final Object obj = constructor.newInstance();
				if (AbstractPalladioAnalyzer.class.isAssignableFrom(obj.getClass())) {
					final AbstractPalladioAnalyzer<?> alz = (AbstractPalladioAnalyzer<?>) obj;
					violations.addAll(alz.analyze(instance));
				}
			} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
					| IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Creates the options for the CLI.
	 * 
	 * @return options for the CLI
	 */
	private static Options createOptions() {
		final Options options = new Options();

		options.addOption(Option.builder("p").required(true).longOpt("pcm").hasArg()
				.desc("directory containing all PCM models").build());
		options.addOption(Option.builder("c").required(true).longOpt("cst").hasArg()
				.desc("Description of the constraints to check").build());

		return options;
	}

	/**
	 * Creates the help options for the CLI.
	 * 
	 * @return help options for the CLI
	 */
	private static Options createHelpOptions() {
		final Options options = new Options();

		options.addOption(Option.builder("p").required(false).longOpt("pcm").hasArg()
				.desc("directory containing all PCM models").build());
		options.addOption(Option.builder("c").required(true).longOpt("cst").hasArg()
				.desc("Description of the constraints to check").build());

		/** help */
		options.addOption(Option.builder("h").required(false).longOpt("help").desc("show usage information").build());

		return options;
	}

}
