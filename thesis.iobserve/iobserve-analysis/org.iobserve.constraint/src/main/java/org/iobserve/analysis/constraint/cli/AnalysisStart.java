package org.iobserve.analysis.constraint.cli;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.iobserve.analysis.InitializeModelProviders;
import org.iobserve.analysis.constraint.analysis.ConstraintViolation;
import org.iobserve.analysis.constraint.analysis.PCMAnalyzer;
import org.iobserve.analysis.constraint.analysis.PalladioInstance;
import org.iobserve.analysis.constraint.model.ConstraintModel;
import org.iobserve.analysis.constraint.parser.ConstraintModelParser;
import org.iobserve.analysis.model.AllocationModelProvider;
import org.iobserve.analysis.model.RepositoryModelProvider;
import org.iobserve.analysis.model.ResourceEnvironmentModelProvider;
import org.iobserve.analysis.model.SystemModelProvider;
import org.iobserve.analysis.model.UsageModelProvider;

public class AnalysisStart {

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
				final File constraintFile = new File(commandLine.getOptionValue("c"));

				if (constraintFile.isFile() && constraintFile.exists()) {
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

						ConstraintModelParser modelParser = new ConstraintModelParser();
						ConstraintModel model = modelParser.parseModel(constraintFile);

						PCMAnalyzer analyzer = new PCMAnalyzer(instance);

						System.out.println("Analysis start");
						List<ConstraintViolation> violations = analyzer.analyze(model);
						System.out.println("Anaylsis complete");

						for (ConstraintViolation violation : violations) {
							System.out.println("Found a violation of constraint '" + violation.getConstraintName()
									+ "' at object of type '" + violation.getLocation() + "' with message '"
									+ violation.getMessage() + "'.");
						}
					}
				}
			}
		} catch (ParseException | IOException e) {
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
				.desc("File containing constraints to check").build());

		return options;
	}

	private static Options createHelpOptions() {
		final Options options = new Options();

		options.addOption(Option.builder("p").required(false).longOpt("pcm").hasArg()
				.desc("directory containing all PCM models").build());
		options.addOption(Option.builder("c").required(true).longOpt("cst").hasArg()
				.desc("File containing constraints to check").build());

		/** help */
		options.addOption(Option.builder("h").required(false).longOpt("help").desc("show usage information").build());

		return options;
	}

}
