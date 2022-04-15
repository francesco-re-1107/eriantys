package it.polimi.ingsw;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class PrettyLogFormatter extends Formatter {
        private static final String format = "[%1$tT] [%2$-4s] %3$s %n";

        public String format(LogRecord record) {
            return String.format(format,
                    record.getMillis(),
                    record.getLevel().getName(),
                    record.getMessage());
        }
}
