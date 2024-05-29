package com.flappybird.ui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import com.flappybird.util.AudioManager;

public class SettingsDialog {

    public static void showSettingsDialog(Stage parentStage, AudioManager audioManager) {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(parentStage);

        // 使用局部变量暂存设置
        final boolean originalSoundEffectsEnabled = audioManager.isSoundEffectsEnabled();
        final boolean originalBackgroundMusicEnabled = audioManager.isBackgroundMusicEnabled();

        CheckBox soundEffectsCheckbox = new CheckBox("Sound Effects");
        soundEffectsCheckbox.setSelected(originalSoundEffectsEnabled);
        CheckBox backgroundMusicCheckbox = new CheckBox("Background Music");
        backgroundMusicCheckbox.setSelected(originalBackgroundMusicEnabled);

        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> {
            // 还原设置
            audioManager.toggleSoundEffects(originalSoundEffectsEnabled);
            audioManager.toggleBackgroundMusic(originalBackgroundMusicEnabled);
            dialogStage.close();
        });

        Button confirmButton = new Button("Confirm");
        confirmButton.setOnAction(e -> {
            // 应用新的设置
            audioManager.toggleSoundEffects(soundEffectsCheckbox.isSelected());
            audioManager.toggleBackgroundMusic(backgroundMusicCheckbox.isSelected());
            dialogStage.close();
        });

        HBox buttonBox = new HBox(10, confirmButton, closeButton);
        VBox layout = new VBox(10, soundEffectsCheckbox, backgroundMusicCheckbox, buttonBox);
        layout.setStyle("-fx-padding: 10;");

        Scene scene = new Scene(layout);
        dialogStage.setScene(scene);
        dialogStage.setTitle("Settings");
        dialogStage.show();
    }
}