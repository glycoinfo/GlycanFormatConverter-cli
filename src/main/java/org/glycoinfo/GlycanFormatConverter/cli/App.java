package org.glycoinfo.GlycanFormatConverter.cli;

import org.apache.commons.cli.*;
import java.io.PrintWriter;

public class App {

    static final String APP_NAME = "glycanformatconverter";
    static final String APP_VERSION = "2.5.2";

    static final String LIB_NAME = "wurcsframework";
    static final String LIB_VERSION = "1.0.1";

    private static final String OPTION_SEQUENCE_SHORT = "seq";
    private static final String OPTION_SEQUENCE_LONG = "sequence";

    private static final String OPTION_IMPORT_SHORT = "i";
    private static final String OPTION_IMPORT_LONG = "import";

    private static final String OPTION_EXPORT_SHORT = "e";
    private static final String OPTION_EXPORT_LONG = "export";

    private static final String OPTION_HELP_SHORT = "h";
    private static final String OPTION_HELP_LONG = "help";

    private static final String OPTION_VERSION_SHORT = "v";
    private static final String OPTION_VERSION_LONG = "version";

    public static void main (String[] args) {
        new App().run(args);
    }

    void run (String[] args) {
        CommandLineParser cliParser = new DefaultParser();
        ConverterPortal portal = new ConverterPortal();

        try {
            CommandLine cli = cliParser.parse(options(), args);

            if (cli.hasOption(OPTION_HELP_LONG) || cli.hasOption(OPTION_HELP_SHORT)) {
                printUsage();
                return;
            }

            if (cli.hasOption(OPTION_VERSION_LONG) || cli.hasOption(OPTION_VERSION_SHORT)) {
                printVersion();
                return;
            }

            if ((cli.getOptionValue(OPTION_SEQUENCE_LONG) == null && cli.getOptionValue(OPTION_SEQUENCE_SHORT) == null) ||
                    (cli.getOptionValue(OPTION_IMPORT_LONG) == null && cli.getOptionValue(OPTION_IMPORT_SHORT) == null) ||
                    (cli.getOptionValue(OPTION_EXPORT_LONG) == null && cli.getOptionValue(OPTION_EXPORT_SHORT) == null))
            {
                throw new Exception("Missing required options: i, e, seq");
            }

            portal.inputSequence(cli.getOptionValue(OPTION_SEQUENCE_LONG))
                    .inputFormat(InputFormat.forInputFormat(cli.getOptionValue(OPTION_IMPORT_LONG)))
                    .outputFormat(OutputFormat.forOutputFormat(cli.getOptionValue(OPTION_EXPORT_LONG)))
                    .start();

            System.out.println(portal.getOutputSequence());

        } catch (Exception e) {
            System.err.println(e.getMessage() + "\n");
            printUsage();
        }
    }

    private void printUsage() {

        String syntax =
                String.format("%s --%s <FORMAT> --%s <FORMAT> --%s <Sequence>",
                        APP_NAME,
                        OPTION_IMPORT_LONG,
                        OPTION_EXPORT_LONG,
                        OPTION_SEQUENCE_LONG
                );

        String header = "\nOptions:";
        String footer = String.format("\nVersion: %s", APP_VERSION);

        HelpFormatter hf = new HelpFormatter();

        hf.printHelp(new PrintWriter(System.err, true), HelpFormatter.DEFAULT_WIDTH, syntax,
                header, options(), HelpFormatter.DEFAULT_LEFT_PAD, HelpFormatter.DEFAULT_DESC_PAD, footer);
    }

    private Options options() {
        Options opt = new Options();

        opt.addOption(Option.builder(OPTION_IMPORT_SHORT)
                .longOpt(OPTION_IMPORT_LONG)
                .desc("import format.")
                .hasArg(true)
                .argName("IMPORT")
                .argName("FORMAT=[IUPAC-Condensed|IUPAC-Extended|GlycoCT|KCF|LinearCode|WURCS|JSON]")
                .build()
        );

        opt.addOption(Option.builder(OPTION_EXPORT_SHORT)
                .longOpt(OPTION_EXPORT_LONG)
                .desc("export format.")
                .hasArg(true)
                .argName("EXPORT")
                .argName("FORMAT=[IUPAC-Short|IUPAC-Condensed|IUPAC-Extended|GlycoCT|WURCS|GlycanWeb|JSON]")
                .build()
        );

        opt.addOption(Option.builder(OPTION_SEQUENCE_SHORT)
            .longOpt(OPTION_SEQUENCE_LONG)
                .desc("Import glycan text format.")
                .hasArg(true)
                .argName("SEQUENCE")
                .build()
        );

        opt.addOption(Option.builder(OPTION_HELP_SHORT)
            .longOpt(OPTION_HELP_LONG)
            .desc("Show usage help")
            .build()
        );

        opt.addOption(Option.builder(OPTION_VERSION_SHORT)
                .longOpt(OPTION_VERSION_LONG)
                .desc("Print version information.")
                .build()
        );

        return opt;
    }

    private void printVersion () {
        System.out.println(String.format("\n%s: %s", APP_NAME, APP_VERSION));
        System.out.println(String.format("\n%s: %s", LIB_NAME, LIB_VERSION));
    }
}