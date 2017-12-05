package fxexperienceplayer;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.DropShadowBuilder;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioSpectrumListener;
import javafx.scene.media.EqualizerBand;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineBuilder;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.util.Pair;

/**
 * Fun media player app
 * 
 * @author Jasper Potts
 */
public class FxExperiencePlayer extends Application {
    private PlayList playList = new PlayList();
    private int curreentSongIndex = 0;
    private MediaPlayer mediaPlayer;
    private Slider[] sliders = new Slider[10];
    private Slider balanceKnob, volumeKnob;
    private Label trackLabel, timeLabel, nameLabel;
    private DoubleProperty leftVU = new SimpleDoubleProperty(0);
    private DoubleProperty rightVU = new SimpleDoubleProperty(0);
    private VUMeter[] vuMeters = new VUMeter[10];
    private AudioSpectrumListener spectrumListener;
    // Workaround for RT-18827
    private StringProperty timeLabelValue = new SimpleStringProperty() {
        @Override protected void invalidated() {
            super.invalidated();
            if (Platform.isFxApplicationThread()) {
                timeLabel.setText(get());
            } else {
                Platform.runLater(new Runnable() {
                    @Override public void run() {
                        timeLabel.setText(get());
                    }
                });
            }
        }
    };
    
    @Override public void start(final Stage primaryStage) {
        
        Button prevBtn = new Button();
        prevBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                if (curreentSongIndex > 0) {
                    curreentSongIndex --;
                    play(curreentSongIndex);
                }
            }
        });
        
        Button nextBtn = new Button();
        nextBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                if (curreentSongIndex < playList.getSongs().size()) {
                    curreentSongIndex ++;
                    play(curreentSongIndex);
                }
            }
        });
        
        Button playPauseBtn = new Button();
        playPauseBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                if (mediaPlayer == null) {
                    play(curreentSongIndex);
                } else if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                    mediaPlayer.pause();
                } else {
                    mediaPlayer.play();
                }
            }
        });
        
        Button loadBtn = new Button();
        loadBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                LoadDialog.loadPlayList(playList, primaryStage);
            }
        });
        
        Button powerBtn = new Button();
        powerBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                }
                Platform.exit();
            }
        });
        
        ImageView background = new ImageView(new Image(FxExperiencePlayer.class.getResourceAsStream("images/FXExperiencePlayer-BG.png")));
        
        for (int i=0; i<10; i++) {
            final int fi = i;
            sliders[i] = new Slider(EqualizerBand.MIN_GAIN, EqualizerBand.MAX_GAIN, 0);
            sliders[i].setOrientation(Orientation.VERTICAL);
            sliders[i].valueProperty().addListener(new ChangeListener<Number>() {
                @Override public void changed(ObservableValue<? extends Number> arg0, Number oldValue, Number newValue) {
                    if (mediaPlayer != null) {
                        mediaPlayer.getAudioEqualizer().getBands().get(fi).setGain(newValue.doubleValue());
                    }
                }
            });
        }
        
        trackLabel = new Label("Track 1/10");
        timeLabel = new Label("Remaining: 1:30   Total: 3:34");
        nameLabel = new Label("09._their_finest_hour_vol_1-volfo");
        Font lcd = Font.loadFont(FxExperiencePlayer.class.getResourceAsStream("lcddot.ttf"),14);
        trackLabel.setFont(lcd);
        timeLabel.setFont(lcd);
        nameLabel.setFont(lcd);
        
        Rotate leftVURotation = new Rotate(-40, 0,0);
        leftVURotation.angleProperty().bind(new DoubleBinding() {
            { bind(leftVU); }
            @Override protected double computeValue() {
                double zeroOne = leftVU.get();
                return -40 + (80*zeroOne);
            }
        });
        Line leftVUPointer = LineBuilder.create()
                .startX(0).startY(0)
                .endX(0).endY(-83)
                .stroke(new LinearGradient(0, 1, 0, 0, true, CycleMethod.NO_CYCLE, 
                            new Stop(0.33,Color.TRANSPARENT),
                            new Stop(0.34,Color.BLACK),
                            new Stop(1,Color.web("#7e7e7e"))
                ))
                .effect(DropShadowBuilder.create().radius(5).color(new Color(0,0,0,0.3)).build())
                .strokeWidth(3)
                .translateX(375).translateY(615)
                .transforms(leftVURotation)
                .build();
        Rotate rightVURotation = new Rotate(-40, 0,0);
        rightVURotation.angleProperty().bind(new DoubleBinding() {
            { bind(rightVU); }
            @Override protected double computeValue() {
                double zeroOne = rightVU.get();
                return -40 + (80*zeroOne);
            }
        });
        Line rightVUPointer = LineBuilder.create()
                .startX(0).startY(0)
                .endX(0).endY(-83)
                .stroke(new LinearGradient(0, 1, 0, 0, true, CycleMethod.NO_CYCLE, 
                            new Stop(0.33,Color.TRANSPARENT),
                            new Stop(0.34,Color.BLACK),
                            new Stop(1,Color.web("#7e7e7e"))
                ))
                .effect(DropShadowBuilder.create().radius(5).color(new Color(0,0,0,0.3)).build())
                .strokeWidth(3)
                .translateX(593).translateY(615)
                .transforms(rightVURotation)
                .build();
        for (int i=0; i<10; i++) vuMeters[i] = new VUMeter();
        
        spectrumListener = new AudioSpectrumListener() {
            @Override public void spectrumDataUpdate(double timestamp, double duration, float[] magnitudes, float[] phases) {
                double avarage = 0;
                for (int i=0; i<10; i++) {
                    vuMeters[i].setValue((60+magnitudes[i])/60);
                    if (i<3) avarage += ((60+magnitudes[i])/60);
                }
                // make up VU meter values
                avarage = avarage/3;
                avarage *= volumeKnob.getValue();
                if (balanceKnob.getValue() == 0) {
                    leftVU.set(avarage);
                    rightVU.set(avarage);
                } else if (balanceKnob.getValue() > 0) {
                    leftVU.set(avarage * (1-balanceKnob.getValue()));
                    rightVU.set(avarage);
                } else if (balanceKnob.getValue() < 0) {
                    leftVU.set(avarage);
                    rightVU.set(avarage * (balanceKnob.getValue()+1));
                }
            }
        };
        
        balanceKnob = new Slider(-1,1,0);
        balanceKnob.setBlockIncrement(0.1);
        balanceKnob.setId("balance");
        balanceKnob.getStyleClass().add("knobStyle");
        balanceKnob.valueProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number newValue) {
                if(mediaPlayer != null) mediaPlayer.setBalance(newValue.doubleValue());
            }
        });
        volumeKnob = new Slider(0,1,1);
        volumeKnob.setBlockIncrement(0.1);
        volumeKnob.setId("volume");
        volumeKnob.getStyleClass().add("knobStyle");
        volumeKnob.valueProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number newValue) {
                if(mediaPlayer != null) mediaPlayer.setVolume(newValue.doubleValue());
            }
        });
        
        Group root = new Group();
        root.setAutoSizeChildren(false);
        root.getChildren().addAll(background,prevBtn, playPauseBtn, nextBtn, loadBtn, powerBtn,
                trackLabel, timeLabel, nameLabel, leftVUPointer, rightVUPointer, balanceKnob, volumeKnob);
        root.getChildren().addAll(sliders);
        root.getChildren().addAll(vuMeters);
        
        prevBtn.resizeRelocate(106,285,74,74);
        playPauseBtn.resizeRelocate(201,285,88,74);
        nextBtn.resizeRelocate(310,285,74,74);
        loadBtn.resizeRelocate(413,285,77,74);
        powerBtn.resizeRelocate(104,532,68,86);
        for (int i=0; i<10; i++) {
            sliders[i].resizeRelocate(515+(58*i),228-20,53,181+40);
        }
        trackLabel.resizeRelocate(122,95,389,26);
        timeLabel.resizeRelocate(122,125,389,26);
        nameLabel.resizeRelocate(122,155,389,26);
        for (int i=0; i<10; i++) {
            vuMeters[i].setLayoutX(542+(58*i));
            vuMeters[i].setLayoutY(177);
        }
        balanceKnob.resizeRelocate(735,535,87,87);
        volumeKnob.resizeRelocate(907,491,175,175);
        
        Scene scene = new Scene(root, 1204,763);
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().add(FxExperiencePlayer.class.getResource("FxExperiencePlayer.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.show();
        
        // listen for when we have songs
        playList.getSongs().addListener(new ListChangeListener<Pair<String,String>>() {
            @Override public void onChanged(Change<? extends Pair<String,String>> arg0) {
                if (!playList.getSongs().isEmpty()) {
                    curreentSongIndex = 0;
                    play(curreentSongIndex);
                } else {
                    trackLabel.setText("No songs found");
                    timeLabel.setText("");
                    nameLabel.setText("");
                    if (mediaPlayer != null) {
                        mediaPlayer.stop();
                        mediaPlayer.setAudioSpectrumListener(null);
                    }
                    for (int i=0; i<10; i++) vuMeters[i].setValue(0);
                    leftVU.set(0);
                    rightVU.set(0);
                }
            }
        });
        // load initial playlist
        playList.load("http://ia600402.us.archive.org/11/items/their_finest_hour_vol1/their_finest_hour_vol1_files.xml");
//      playList.load("http://www.archive.org/download/their_finest_hour_vol3/their_finest_hour_vol3_files.xml");
    }
    
    private void play(int songIndex) {
        if (mediaPlayer != null ) {
            mediaPlayer.stop();
            mediaPlayer.setAudioSpectrumListener(null);
            for (int i=0; i<10; i++) vuMeters[i].setValue(0);
            leftVU.set(0);
            rightVU.set(0);
        }
        final Media media = new Media(playList.getSongs().get(songIndex).getValue());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setOnError(new Runnable() {
            @Override public void run() {
                System.out.println("mediaPlayer.getError() = " + mediaPlayer.getError());
            }
        });
        trackLabel.setText("Track "+(curreentSongIndex+1)+"/"+playList.getSongs().size());
        nameLabel.setText(playList.getSongs().get(curreentSongIndex).getKey());
        timeLabelValue.bind(new StringBinding() {
            { bind(mediaPlayer.currentTimeProperty(), media.durationProperty(), mediaPlayer.statusProperty()); }
            @Override protected String computeValue() {
                if (mediaPlayer.getStatus() == null) return "Streaming...";
                switch(mediaPlayer.getStatus()) {
                    case PLAYING:
                        if (media == null || media.getDuration() == null) {
                            return "Time: 00:00   Remaining: 00:00   Total: 00:00";
                        } else if (mediaPlayer == null || mediaPlayer.getCurrentTime() == null) {
                            return "Time: 00:00   Remaining: 00:00   Total: "+formatDuration(media.getDuration());
                        } else {
                            return "Time: "+formatDuration(mediaPlayer.getCurrentTime())+
                                    "   Remaining: "+formatDuration(media.getDuration().subtract(mediaPlayer.getCurrentTime()))+
                                    "   Total: "+formatDuration(media.getDuration());
                        }
                    case PAUSED:
                        return "Paused";
                    default:
                        return "Streaming...";
                }
            }
            String formatDuration(Duration time) {
                double minutes = time.toMinutes();
                int minutesWhole = (int)Math.floor(minutes);
                int secondsWhole = (int)Math.round((minutes-minutesWhole) * 60);
                return String.format("%1$02d:%2$02d", minutesWhole, secondsWhole);
            }
        });
        
        mediaPlayer.setAudioSpectrumNumBands(10);
        mediaPlayer.setAudioSpectrumListener(spectrumListener);
        mediaPlayer.setAudioSpectrumInterval(1d/30d);
    }
    
    /** @param args the command line arguments */
    public static void main(String[] args) {
        launch(args);
    }
    
}
