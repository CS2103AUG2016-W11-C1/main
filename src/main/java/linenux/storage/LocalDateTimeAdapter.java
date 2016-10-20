package linenux.storage;

import java.time.LocalDateTime;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class LocalDateTimeAdapter extends XmlAdapter<String, LocalDateTime> {

    @Override
    public LocalDateTime unmarshal(String v) {
        if (v.isEmpty()) {
            return null;
        } else {
            return LocalDateTime.parse(v);
        }
    }

    @Override
    public String marshal(LocalDateTime v) {
        if (v == null) {
            return "";
        } else {
            return v.toString();
        }
    }
}
