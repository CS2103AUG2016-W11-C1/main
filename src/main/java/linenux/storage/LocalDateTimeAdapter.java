package linenux.storage;

import java.time.LocalDateTime;

import javax.xml.bind.annotation.adapters.XmlAdapter;

//@@author A0135788M
/**
 * Converts between LocalDateTime instance and a String.
 */
public class LocalDateTimeAdapter extends XmlAdapter<String, LocalDateTime> {

    /**
     * Converts a {@code String} to a {@code LocalDateTime}.
     * @param v A string from the XML file.
     * @return A {@code LocalDateTime} instance.
     */
    @Override
    public LocalDateTime unmarshal(String v) {
        if (v.isEmpty()) {
            return null;
        } else {
            return LocalDateTime.parse(v);
        }
    }

    /**
     * Converts a {@code LocalDateTime} to a {@code String}.
     * @param A {@code LocalDateTime} instance.
     * @return A {@code String} of the (@code LocalDateTime} instance.
     */
    @Override
    public String marshal(LocalDateTime v) {
        if (v == null) {
            return "";
        } else {
            return v.toString();
        }
    }
}
