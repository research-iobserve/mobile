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

public class AnalysisStart {

	private static final Pattern CONSTRAINT_PATTERN = Pattern.compile("\\[(.*?)\\]");

	public static void main(String[] args) {
		final CommandLineParser parser = new DefaultParser();

		CommandLine commandLine;
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

						List<ConstraintViolation> violations = new ArrayList<>();
						Matcher constraintMatcher = CONSTRAINT_PATTERN.matcher(constraintOption);
						if (constraintMatcher.find()) {
							String[] classes = constraintMatcher.group(1).split(",");
							for (String clazz : classes) {
								String[] argSplit = clazz.split("\\(");

								if (argSplit.length == 2) {
									String[] argv = argSplit[1].substring(0, argSplit[1].length() - 1).split(";");
									Class<?>[] types = new Class<?>[argv.length];
									for (int i = 0; i < types.length; i++) {
										types[i] = String.class;
									}

									try {
										Class<?> resolved = Class.forName(argSplit[0]);
										Constructor<?> constructor = resolved.getConstructor(types);

										Object obj = constructor.newInstance((Object[]) argv);
										if (AbstractPalladioAnalyzer.class.isAssignableFrom(obj.getClass())) {
											AbstractPalladioAnalyzer<?> alz = (AbstractPalladioAnalyzer<?>) obj;
											violations.addAll(alz.analyze(instance));
										}
									} catch (ClassNotFoundException | NoSuchMethodException | SecurityException
											| InstantiationException | IllegalAccessException | IllegalArgumentException
											| InvocationTargetException e) {
										e.printStackTrace();
									}
								} else {
									// TODO fix code duplication
									try {
										Class<?> resolved = Class.forName(argSplit[0]);
										Constructor<?> constructor = resolved.getConstructor();

										Object obj = constructor.newInstance();
										if (AbstractPalladioAnalyzer.class.isAssignableFrom(obj.getClass())) {
											AbstractPalladioAnalyzer<?> alz = (AbstractPalladioAnalyzer<?>) obj;
											violations.addAll(alz.analyze(instance));
										}
									} catch (ClassNotFoundException | NoSuchMethodException | SecurityException
											| InstantiationException | IllegalAccessException | IllegalArgumentException
											| InvocationTargetException e) {
										e.printStackTrace();
									}
								}
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

	private static Options createOptions() {
		final Options options = new Options();

		options.addOption(Option.builder("p").required(true).longOpt("pcm").hasArg()
				.desc("directory containing all PCM models").build());
		options.addOption(Option.builder("c").required(true).longOpt("cst").hasArg()
				.desc("Description of the constraints to check").build());

		return options;
	}

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
