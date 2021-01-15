package launcher;

import javafx.event.EventHandler;

public abstract class CustomEventHandler implements EventHandler {

    private String text;

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

}
