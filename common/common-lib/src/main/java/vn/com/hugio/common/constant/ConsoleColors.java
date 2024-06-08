package vn.com.hugio.common.constant;

public class ConsoleColors {

    // Example usage
    // System.out.println(ConsoleColors.RED + "RED COLORED" + ConsoleColors.RESET + " NORMAL");

    // Reset
    public static final String RESET = "\033[0m";  // Text Reset

    // Regular Colors
    public static final String BLACK = "\033[0;30m";   // BLACK
    public static final String RED = "\033[0;31m";     // RED
    public static final String GREEN = "\033[0;32m";   // GREEN
    public static final String YELLOW = "\033[0;33m";  // YELLOW
    public static final String BLUE = "\033[0;34m";    // BLUE
    public static final String PURPLE = "\033[0;35m";  // PURPLE
    public static final String CYAN = "\033[0;36m";    // CYAN
    public static final String WHITE = "\033[0;37m";   // WHITE
    // Bold
    public static final String BLACK_BOLD = "\033[1;30m";  // BLACK
    public static final String RED_BOLD = "\033[1;31m";    // RED
    public static final String GREEN_BOLD = "\033[1;32m";  // GREEN
    public static final String YELLOW_BOLD = "\033[1;33m"; // YELLOW
    public static final String BLUE_BOLD = "\033[1;34m";   // BLUE
    public static final String PURPLE_BOLD = "\033[1;35m"; // PURPLE
    public static final String CYAN_BOLD = "\033[1;36m";   // CYAN
    public static final String WHITE_BOLD = "\033[1;37m";  // WHITE
    // Underline
    public static final String BLACK_UNDERLINED = "\033[4;30m";  // BLACK
    public static final String RED_UNDERLINED = "\033[4;31m";    // RED
    public static final String GREEN_UNDERLINED = "\033[4;32m";  // GREEN
    public static final String YELLOW_UNDERLINED = "\033[4;33m"; // YELLOW
    public static final String BLUE_UNDERLINED = "\033[4;34m";   // BLUE
    public static final String PURPLE_UNDERLINED = "\033[4;35m"; // PURPLE
    public static final String CYAN_UNDERLINED = "\033[4;36m";   // CYAN
    public static final String WHITE_UNDERLINED = "\033[4;37m";  // WHITE
    // Background
    public static final String BLACK_BACKGROUND = "\033[40m";  // BLACK
    public static final String RED_BACKGROUND = "\033[41m";    // RED
    public static final String GREEN_BACKGROUND = "\033[42m";  // GREEN
    public static final String YELLOW_BACKGROUND = "\033[43m"; // YELLOW
    public static final String BLUE_BACKGROUND = "\033[44m";   // BLUE
    public static final String PURPLE_BACKGROUND = "\033[45m"; // PURPLE
    public static final String CYAN_BACKGROUND = "\033[46m";   // CYAN
    public static final String WHITE_BACKGROUND = "\033[47m";  // WHITE
    // High Intensity
    public static final String BLACK_BRIGHT = "\033[0;90m";  // BLACK
    public static final String RED_BRIGHT = "\033[0;91m";    // RED
    public static final String GREEN_BRIGHT = "\033[0;92m";  // GREEN
    public static final String YELLOW_BRIGHT = "\033[0;93m"; // YELLOW
    public static final String BLUE_BRIGHT = "\033[0;94m";   // BLUE
    public static final String PURPLE_BRIGHT = "\033[0;95m"; // PURPLE
    public static final String CYAN_BRIGHT = "\033[0;96m";   // CYAN
    public static final String WHITE_BRIGHT = "\033[0;97m";  // WHITE
    // Bold High Intensity
    public static final String BLACK_BOLD_BRIGHT = "\033[1;90m"; // BLACK
    public static final String RED_BOLD_BRIGHT = "\033[1;91m";   // RED
    public static final String GREEN_BOLD_BRIGHT = "\033[1;92m"; // GREEN
    public static final String YELLOW_BOLD_BRIGHT = "\033[1;93m";// YELLOW
    public static final String BLUE_BOLD_BRIGHT = "\033[1;94m";  // BLUE
    public static final String PURPLE_BOLD_BRIGHT = "\033[1;95m";// PURPLE
    public static final String CYAN_BOLD_BRIGHT = "\033[1;96m";  // CYAN
    public static final String WHITE_BOLD_BRIGHT = "\033[1;97m"; // WHITE
    // High Intensity backgrounds
    public static final String BLACK_BACKGROUND_BRIGHT = "\033[0;100m";// BLACK
    public static final String RED_BACKGROUND_BRIGHT = "\033[0;101m";// RED
    public static final String GREEN_BACKGROUND_BRIGHT = "\033[0;102m";// GREEN
    public static final String YELLOW_BACKGROUND_BRIGHT = "\033[0;103m";// YELLOW
    public static final String BLUE_BACKGROUND_BRIGHT = "\033[0;104m";// BLUE
    public static final String PURPLE_BACKGROUND_BRIGHT = "\033[0;105m"; // PURPLE
    public static final String CYAN_BACKGROUND_BRIGHT = "\033[0;106m";  // CYAN
    public static final String WHITE_BACKGROUND_BRIGHT = "\033[0;107m";   // WHITE

    public static String printBlack(String text) {
        return BLACK + text + RESET;
    }

    public static String printRed(String text) {
        return RED + text + RESET;
    }

    public static String printGreen(String text) {
        return GREEN + text + RESET;
    }

    public static String printYellow(String text) {
        return YELLOW + text + RESET;
    }

    public static String printBlue(String text) {
        return BLUE + text + RESET;
    }

    public static String printPurple(String text) {
        return PURPLE + text + RESET;
    }

    public static String printCyan(String text) {
        return CYAN + text + RESET;
    }

    public static String printWhite(String text) {
        return WHITE + text + RESET;
    }

    public static String printBlackBold(String text) {
        return BLACK_BOLD + text + RESET;
    }

    public static String printRedBold(String text) {
        return RED_BOLD + text + RESET;
    }

    public static String printGreenBold(String text) {
        return GREEN_BOLD + text + RESET;
    }

    public static String printYellowBold(String text) {
        return YELLOW_BOLD + text + RESET;
    }

    public static String printBlueBold(String text) {
        return BLUE_BOLD + text + RESET;
    }

    public static String printPurpleBold(String text) {
        return PURPLE_BOLD + text + RESET;
    }

    public static String printCyanBold(String text) {
        return CYAN_BOLD + text + RESET;
    }

    public static String printWhiteBold(String text) {
        return WHITE_BOLD + text + RESET;
    }

    public static String printBlackUnderlined(String text) {
        return BLACK_UNDERLINED + text + RESET;
    }

    public static String printRedUnderlined(String text) {
        return RED_UNDERLINED + text + RESET;
    }

    public static String printGreenUnderlined(String text) {
        return GREEN_UNDERLINED + text + RESET;
    }

    public static String printYellowUnderlined(String text) {
        return YELLOW_UNDERLINED + text + RESET;
    }

    public static String printBlueUnderlined(String text) {
        return BLUE_UNDERLINED + text + RESET;
    }

    public static String printPurpleUnderlined(String text) {
        return PURPLE_UNDERLINED + text + RESET;
    }

    public static String printCyanUnderlined(String text) {
        return CYAN_UNDERLINED + text + RESET;
    }

    public static String printWhiteUnderlined(String text) {
        return WHITE_UNDERLINED + text + RESET;
    }

    public static String printBlackBackground(String text) {
        return BLACK_BACKGROUND + text + RESET;
    }

    public static String printRedBackground(String text) {
        return RED_BACKGROUND + text + RESET;
    }

    public static String printGreenBackground(String text) {
        return GREEN_BACKGROUND + text + RESET;
    }

    public static String printYellowBackground(String text) {
        return YELLOW_BACKGROUND + text + RESET;
    }

    public static String printBlueBackground(String text) {
        return BLUE_BACKGROUND + text + RESET;
    }

    public static String printPurpleBackground(String text) {
        return PURPLE_BACKGROUND + text + RESET;
    }

    public static String printCyanBackground(String text) {
        return CYAN_BACKGROUND + text + RESET;
    }

    public static String printWhiteBackground(String text) {
        return WHITE_BACKGROUND + text + RESET;
    }

    public static String printBlackBright(String text) {
        return BLACK_BRIGHT + text + RESET;
    }

    public static String printRedBright(String text) {
        return RED_BRIGHT + text + RESET;
    }

    public static String printGreenBright(String text) {
        return GREEN_BRIGHT + text + RESET;
    }

    public static String printYellowBright(String text) {
        return YELLOW_BRIGHT + text + RESET;
    }

    public static String printBlueBright(String text) {
        return BLUE_BRIGHT + text + RESET;
    }

    public static String printPurpleBright(String text) {
        return PURPLE_BRIGHT + text + RESET;
    }

    public static String printCyanBright(String text) {
        return CYAN_BRIGHT + text + RESET;
    }

    public static String printWhiteBright(String text) {
        return WHITE_BRIGHT + text + RESET;
    }

    public static String printBlackBoldBright(String text) {
        return BLACK_BOLD_BRIGHT + text + RESET;
    }

    public static String printRedBoldBright(String text) {
        return RED_BOLD_BRIGHT + text + RESET;
    }

    public static String printGreenBoldBright(String text) {
        return GREEN_BOLD_BRIGHT + text + RESET;
    }

    public static String printYellowBoldBright(String text) {
        return YELLOW_BOLD_BRIGHT + text + RESET;
    }

    public static String printBlueBoldBright(String text) {
        return BLUE_BOLD_BRIGHT + text + RESET;
    }

    public static String printPurpleBoldBright(String text) {
        return PURPLE_BOLD_BRIGHT + text + RESET;
    }

    public static String printCyanBoldBright(String text) {
        return CYAN_BOLD_BRIGHT + text + RESET;
    }

    public static String printWhiteBoldBright(String text) {
        return WHITE_BOLD_BRIGHT + text + RESET;
    }

    public static String printBlackBackgroundBright(String text) {
        return BLACK_BACKGROUND_BRIGHT + text + RESET;
    }

    public static String printRedBackgroundBright(String text) {
        return RED_BACKGROUND_BRIGHT + text + RESET;
    }

    public static String printGreenBackgroundBright(String text) {
        return GREEN_BACKGROUND_BRIGHT + text + RESET;
    }

    public static String printYellowBackgroundBright(String text) {
        return YELLOW_BACKGROUND_BRIGHT + text + RESET;
    }

    public static String printBlueBackgroundBright(String text) {
        return BLUE_BACKGROUND_BRIGHT + text + RESET;
    }

    public static String printPurpleBackgroundBright(String text) {
        return PURPLE_BACKGROUND_BRIGHT + text + RESET;
    }

    public static String printCyanBackgroundBright(String text) {
        return CYAN_BACKGROUND_BRIGHT + text + RESET;
    }

    public static String printWhiteBackgroundBright(String text) {
        return WHITE_BACKGROUND_BRIGHT + text + RESET;
    }

}
