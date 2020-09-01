package com.games.snake;

import java.io.*;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class Game extends Application implements GameScreen {
    private static int cellSize;

    private final Timeline timeline = new Timeline();
    private boolean isMessageShown = false;
    private final boolean showGrid = true;
    private final boolean showTV = true;
    private TextFlow dialogContainer;
    private StackPane[][] cells;
    private int timerStep = 0;
    private Text scoreText;
    private int height;
    private int width;
    private Pane root;

    public Game() {
    }

    public void start(Stage primaryStage) {
        this.scoreText = new Text("Score: 0");
        this.initialize();
        Scene scene = new Scene(this.createContent());
        scene.setOnMouseClicked((event) -> {
            if (this.isMessageShown) {
                this.isMessageShown = false;
                this.dialogContainer.setVisible(false);
            }
        });
        scene.setOnKeyReleased((event) -> {
            if (!this.isMessageShown) {
                this.onKeyReleased(this.getKey(event.getCode()));
            }
        });
        scene.setOnKeyPressed((event) -> {
            if (this.isMessageShown) {
                if (event.getCode().name().equalsIgnoreCase(Key.SPACE.name())) {
                    this.isMessageShown = false;
                    this.dialogContainer.setVisible(false);
                }

                this.onKeyPress(this.getKey(event.getCode()));
            } else {
                this.onKeyPress(this.getKey(event.getCode()));
            }
        });
        primaryStage.setTitle("JavaRush Game");
        primaryStage.setResizable(false);
        if (showTV) {
            primaryStage.initStyle(StageStyle.TRANSPARENT);
            scene.setFill(Color.TRANSPARENT);
        }

        primaryStage.setScene(scene);
        primaryStage.show();
        this.timeline.playFromStart();
    }

    private Key getKey(KeyCode code) {
        String keyName = Arrays.stream(Key.values()).map(Enum::name).filter((name) -> {
            return code.name().equalsIgnoreCase(name);
        }).findFirst().orElse(Key.UNKNOWN.name());
        return Key.valueOf(keyName);
    }

    private Parent createContent() {
        this.root = new Pane();
        this.root.setPrefSize(this.width * cellSize + 250, this.height * cellSize + 110 + 140);
        this.createBorderImage();

        for(int y = 0; y < this.height; ++y) {
            for(int x = 0; x < this.width; ++x) {
                ObservableList<Node> children = this.cells[y][x].getChildren();
                Rectangle cell;
                if (this.showGrid && children.size() > 0) {
                    cell = (Rectangle)children.get(0);
                    cell.setWidth(cellSize - 1);
                    cell.setHeight(cellSize - 1);
                    cell.setStroke(this.toFXColor(com.games.snake.Color.BLACK));
                }

                if (children.size() > 0) {
                    cell = (Rectangle)children.get(0);
                    cell.setWidth(cellSize);
                    cell.setHeight(cellSize);
                    this.cells[y][x].setLayoutX(x * cellSize + 125);
                    this.cells[y][x].setLayoutY(y * cellSize + 110);
                    this.root.getChildren().add(this.cells[y][x]);
                }
            }
        }

        this.createScorePanel();
        this.timeline.setCycleCount(-1);
        return this.root;
    }

    private void createBorderImage() {
        String fileName = "src/com/games/snake/resources/screen.png";
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Image image = null;
        if (inputStream != null) {
            image = new Image(inputStream);
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(this.width * cellSize + 250);
            imageView.setFitHeight(this.height * cellSize + 110 + 140);
            this.root.getChildren().add(imageView);
        }
    }

    private void createScorePanel() {
        this.scoreText.setFont(Font.font("Verdana", FontWeight.BOLD, 16.0D));
        this.scoreText.setFill(this.toFXColor(com.games.snake.Color.BLACK));
        StackPane scorePane = new StackPane(this.scoreText);
        scorePane.setBorder(new Border(new BorderStroke(this.toFXColor(com.games.snake.Color.BLACK), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        scorePane.setLayoutY(this.height * cellSize + 110 + 6);
        int scoreHeight = 20;
        Rectangle rectangle;
        if (this.showGrid) {
            rectangle = new Rectangle((this.width * cellSize - 1) / 2, scoreHeight, this.toFXColor(com.games.snake.Color.WHITE));
            scorePane.setLayoutX(125 + (this.width * cellSize - 1) / 4);
        } else {
            rectangle = new Rectangle(this.width * cellSize / 2, scoreHeight, this.toFXColor(com.games.snake.Color.WHITE));
            scorePane.setLayoutX(124 + this.width * cellSize / 4);
        }

        scorePane.getChildren().add(0, rectangle);
        this.root.getChildren().add(scorePane);
    }

    public void setScreenSize(int width, int height) {
        this.width = width < 3 ? 3 : (width > 100 ? 100 : width);
        this.height = height < 3 ? 3 : (height > 100 ? 100 : height);
        cellSize = 800 / this.width < 600 / this.height ? 800 / this.width : 600 / this.height;
        this.cells = new StackPane[this.height][this.width];

        for(int y = 0; y < this.height; ++y) {
            for(int x = 0; x < this.width; ++x) {
                this.cells[y][x] = new StackPane(new Rectangle(), new Text(), new Text());
            }
        }

    }

    public void setCellColor(int x, int y, com.games.snake.Color color) {
        if (color != null && color != com.games.snake.Color.NONE) {
            ObservableList<Node> children = this.cells[y][x].getChildren();
            if (children.size() > 0 && !Color.valueOf(color.name()).equals(((Rectangle)children.get(0)).getFill())) {
                ((Rectangle)children.get(0)).setFill(Color.valueOf(color.name()));
            }

        }
    }

    public void setCellValue(int x, int y, String value) {
        ObservableList<Node> children = this.cells[y][x].getChildren();
        if (children.size() > 1) {
            Text text = (Text)children.get(1);
            if (text.getText().equals(value)) {
                return;
            }

            if (value.length() <= 4) {
                double fontSize = (double)cellSize * 0.4D;
                text.setFont(Font.font(fontSize));
            } else {
                int fontSize = cellSize / value.length();
                text.setFont(Font.font(fontSize));
            }

            text.setText(value);
        }

    }

    public String getCellValue(int x, int y) {
        ObservableList<Node> children = this.cells[y][x].getChildren();
        return children.size() > 1 ? ((Text)children.get(1)).getText() : "";
    }

    public void setCellTextColor(int x, int y, com.games.snake.Color color) {
        ObservableList<Node> children = this.cells[y][x].getChildren();
        if (children.size() > 1) {
            Text text = (Text)children.get(1);
            if (!this.toFXColor(color).equals(text.getFill())) {
                text.setFill(this.toFXColor(color));
            }
        }
    }

    public void setTurnTimer(int timeMs) {
        this.timeline.stop();
        KeyFrame frame = new KeyFrame(Duration.millis(timeMs), (event) -> {
            if (!this.isMessageShown) {
                this.onTurn(++this.timerStep);
            }

        });
        this.timeline.getKeyFrames().clear();
        this.timeline.getKeyFrames().add(frame);
        this.timeline.play();
    }

    public void stopTurnTimer() {
        this.timeline.stop();
    }

    public int getRandomNumber(int max) {
        return new Random().nextInt(max);
    }

    public void initialize() {
    }

    public void onKeyPress(Key key) {
    }

    public void onKeyReleased(Key key) {
    }

    public void onTurn(int step) {
    }

    private Color toFXColor(com.games.snake.Color color) {
        if (color == com.games.snake.Color.NONE) {
            return Color.TRANSPARENT;
        } else {
            return color != null ? Color.valueOf(color.name()) : Color.BLACK;
        }
    }

    public void setCellTextSize(int x, int y, int size) {
        ObservableList<Node> children = this.cells[y][x].getChildren();
        if (children.size() > 1) {
            Text text = (Text)children.get(1);
            size = Math.min(size, 100);
            double fontSize = (double)cellSize * ((double)size / 100.0D);
            if (!Font.font(fontSize).equals(text.getFont())) {
                text.setFont(Font.font(fontSize));
            }
        }
    }

    public void setCellValueEx(int x, int y, com.games.snake.Color cellColor, String value) {
        this.setCellValue(x, y, value);
        this.setCellColor(x, y, cellColor);
    }

    public void setCellValueEx(int x, int y, com.games.snake.Color cellColor, String value, com.games.snake.Color textColor) {
        this.setCellValueEx(x, y, cellColor, value);
        this.setCellTextColor(x, y, textColor);
    }

    public void setCellValueEx(int x, int y, com.games.snake.Color cellColor, String value, com.games.snake.Color textColor, int textSize) {
        this.setCellValueEx(x, y, cellColor, value, textColor);
        this.setCellTextSize(x, y, textSize);
    }

    public void showMessageDialog(com.games.snake.Color cellColor, String message, com.games.snake.Color textColor, int textSize) {
        if (this.dialogContainer == null) {
            this.dialogContainer = new TextFlow();
            this.root.getChildren().add(this.dialogContainer);
        }

        this.dialogContainer.getChildren().clear();
        Text messageText = new Text();
        messageText.setFont(Font.font("Verdana", FontWeight.BOLD, textSize));
        messageText.setText(message);
        double preferredWidth = messageText.getLayoutBounds().getWidth();
        messageText.setFill(this.toFXColor(textColor));
        this.dialogContainer.setLayoutX((this.root.getWidth() - preferredWidth) / 2.0D);
        this.dialogContainer.setLayoutY(this.root.getHeight() / 2.0D - 30.0D);
        this.dialogContainer.setBackground(new Background(new BackgroundFill(this.toFXColor(cellColor), CornerRadii.EMPTY, Insets.EMPTY)));
        this.dialogContainer.setVisible(true);
        this.dialogContainer.getChildren().add(messageText);
        this.isMessageShown = true;
    }

    public void setScore(int score) {
        this.scoreText.setText("Score: " + score);
    }
}
