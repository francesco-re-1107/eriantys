package it.polimi.ingsw;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class PrettyLogFormatter extends Formatter {
        private static final String FORMAT = "[%1$tT] [%2$-4s] [%3$-4s] %4$s %n";

        public String format(LogRecord r) {
            return String.format(FORMAT,
                    r.getMillis(),
                    r.getSourceClassName(),
                    r.getLevel().getName(),
                    r.getMessage());
        }
}
