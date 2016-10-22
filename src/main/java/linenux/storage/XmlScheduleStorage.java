package linenux.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import linenux.model.Schedule;
import linenux.model.adapted.AdaptedSchedule;

public class XmlScheduleStorage {
    public static final String FILENAME = "Schedule.xml";
    private Path filePath;

    public XmlScheduleStorage(String filePath) {
        this.filePath = Paths.get(filePath + "/" + FILENAME);
    }

    public Schedule loadScheduleFromFile() {
        try {
            JAXBContext context = JAXBContext.newInstance(AdaptedSchedule.class);
            Unmarshaller u = context.createUnmarshaller();
            if (!Files.exists(filePath)) {
                throw new FileNotFoundException();
            }
            AdaptedSchedule aSchedule = (AdaptedSchedule) u.unmarshal(filePath.toFile());
            return aSchedule.convertToModel();
        } catch (JAXBException e) {
            System.out.println("jaxb");
            return null;
        } catch (FileNotFoundException f) {
            System.out.println("file");
            return null;
        } catch (Exception e) {
            System.out.println("dont know");
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

            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
            }

            m.marshal(aSchedule, filePath.toFile());
        } catch (Exception e) {
            System.out.println("save");
        }
    }

    public File getFile() {
        return filePath.toFile();
    }

}
