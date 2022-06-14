package server;

import java.io.IOException;
import java.io.OutputStream;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

/**
 * This class replaces the output stream into a TextArea within the GUI in the FXML file
*/
public class Console extends OutputStream {
    private TextArea console;
    //We pass the TextArea variable from the F
    public Console(TextArea console) {
        this.console = console;
    }

    public void appendText(String valueOf) {
        Platform.runLater(() -> console.appendText(valueOf));
    }
    @Override
    public void write(int b) throws IOException {
        appendText(String.valueOf((char)b));
    }
}
