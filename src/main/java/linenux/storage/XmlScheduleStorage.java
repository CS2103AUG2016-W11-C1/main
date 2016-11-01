package linenux.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import linenux.config.Config;
import linenux.model.Schedule;
import linenux.model.adapted.AdaptedSchedule;

//@@author A0135788M
public class XmlScheduleStorage implements ScheduleStorage {
    private Config config;

    public XmlScheduleStorage(Config config) {
        this.config = config;
    }

    @Override
    public Schedule loadScheduleFromFile() {
        try {
            JAXBContext context = JAXBContext.newInstance(AdaptedSchedule.class);
            Unmarshaller u = context.createUnmarshaller();

            if (!hasScheduleFile()) {
                createFile();
            }

            AdaptedSchedule aSchedule = (AdaptedSchedule) u.unmarshal(this.getFilePath().toFile());
            return aSchedule.convertToModel();
        } catch (Exception e) {
            Alert alert = throwAlert("Loading Error", "Could not load data from file: \n" + this.getFilePath().toString());
            alert.showAndWait();
            return null;
        }
    }

    @Override
    public void saveScheduleToFile(Schedule schedule) {
        try {
            AdaptedSchedule aSchedule = new AdaptedSchedule();
            aSchedule.convertToXml(schedule);
            JAXBContext context = JAXBContext.newInstance(aSchedule.getClass());
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            if (!hasScheduleFile()) {
                createFile();
            }

            m.marshal(aSchedule, this.getFilePath().toFile());
        } catch (Exception e) {
            Alert alert = throwAlert("Saving Error", "Could not save data to file: \n" + this.getFilePath().toString());
            alert.showAndWait();
        }
    }

    @Override
    public boolean hasScheduleFile() {
        return Files.exists(this.getFilePath());
    }

    private void createFile() throws IOException {
        try {
            Files.createFile(this.getFilePath());
        } catch (IOException i) {
            Files.createDirectories(this.getFilePath());
            createFile();
        } catch (Exception e) {
            Alert alert = throwAlert("Creating File Error", "Could not create file at: \n" + this.getFilePath().toString());
            alert.showAndWait();
        }
    }

    private Alert throwAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        return alert;
    }

    private Path getFilePath() {
        return Paths.get(this.config.getScheduleFilePath());
    }
}
