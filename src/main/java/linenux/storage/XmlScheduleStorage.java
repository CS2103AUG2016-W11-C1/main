package linenux.storage;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import linenux.model.Schedule;
import linenux.model.adapted.AdaptedSchedule;

public class XmlScheduleStorage {
    public static final String FILENAME = "Schedule.xml";
    private File file;

    public XmlScheduleStorage(String filePath) {
        this.file = new File(filePath + "/" + FILENAME);
    }

    public Schedule loadScheduleFromFile() {
        try {
            JAXBContext context = JAXBContext.newInstance(AdaptedSchedule.class);
            Unmarshaller u = context.createUnmarshaller();
            AdaptedSchedule schedule = (AdaptedSchedule) u.unmarshal(this.file);
            return schedule.convertToModel();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void saveScheduleToFile(Schedule schedule) {
        try {
            AdaptedSchedule aSchedule = new AdaptedSchedule();
            aSchedule.convertToXml(schedule);
            JAXBContext context = JAXBContext.newInstance(aSchedule.getClass());
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            if (!file.exists()) {
                file.createNewFile();
            }

            m.marshal(schedule, this.file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public File getFile() {
        return file;
    }

}
