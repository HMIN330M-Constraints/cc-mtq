import javax.swing.text.AbstractDocument.Content;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class App 
{
	
	private static Instance[] instances = new Instance[] { Instance.inst0, Instance.inst1, Instance.inst2, 
		Instance.inst3, Instance.inst4, Instance.inst5, Instance.inst6, Instance.inst7, Instance.inst8};
	private static Instance inst = Instance.inst0;
	private static long timeout = 60000;
	private static boolean allSolutions;

	
	public static void main(String[] args) throws ParseException {

		final Options options = configParameters();
		final CommandLineParser parser = new DefaultParser();
		final CommandLine line = parser.parse(options, args);

		// Check arguments and options
		for (Option opt : line.getOptions()) {
			checkOption(line, opt.getLongOpt(), options);
		}

		//System.out.println("Cheking instance " + inst.toString());
		new CocoAirlines().solve(inst, timeout, allSolutions);
		//System.out.println("");
	}
	
	
	// Add options here
	private static Options configParameters() {

		final Option helpFileOption = Option.builder("h").longOpt("help").desc("Display help message").build();

		final Option instOption = Option.builder("i").longOpt("instance").hasArg(true).argName("aircraft instance")
				.desc("aircraft instance (#dividers, capacity, exit doors) - from inst1 to inst10").required(false)
				.build();

		final Option allsolOption = Option.builder("a").longOpt("all").hasArg(false).desc("all solutions")
				.required(false).build();

		final Option limitOption = Option.builder("t").longOpt("timeout").hasArg(true).argName("timeout in ms")
				.desc("Set the timeout limit to the specified time").required(false).build();

		// Create the options list
		final Options options = new Options();
		options.addOption(instOption);
		options.addOption(allsolOption);
		options.addOption(limitOption);
		options.addOption(helpFileOption);

		return options;
	}
	

	// Check all parameters values
	public static void checkOption(CommandLine line, String option, Options options) {

		if (option.equals("instance"))
			inst = Instance.valueOf(line.getOptionValue(option));
		else if (option.equals("timeout"))
			timeout = Long.parseLong(line.getOptionValue(option));
		else if (option.equals("all"))
			allSolutions = true;
		else if (option.equals("help")) {
			final HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("cocoAirlines", options, true);
		} else {
			System.err.println("Bad parameter: " + option);
			System.exit(2);
		}

	}
	

	
}
