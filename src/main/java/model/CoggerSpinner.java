package model;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;


public class CoggerSpinner extends Thread {
    private final ImageView imageView;

    public CoggerSpinner(ImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    public void run() {
        Platform.runLater(() -> {
            try {
                Image gifCogger = new Image(getClass().getResourceAsStream("src/main/resources/kuvat/coggers-poggers.gif"));
                imageView.setImage(gifCogger);
                imageView.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
