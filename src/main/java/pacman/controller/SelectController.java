package pacman.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import pacman.constant.FileName;
import pacman.model.Map;
import pacman.model.Score;
import pacman.model.ScoreBoard;
import pacman.util.SceneSwitch;
import pacman.util.ScoreBoardReader;

public class SelectController {
  @FXML private ComboBox backgroundComboBox;
  @FXML private ComboBox wallComboBox;
  @FXML private ListView levelListView;
  @FXML private TextField nicknameTextField;

  private Map map;

  @FXML
  public void initialize() {
    // init the 3d effect of panes
    // the 3d effect on boards will throw an exception of running warning in JavaFX
    // see: https://github.com/scalafx/scalafx/issues/265
    //
    //    leftPane.getTransforms().add(new Rotate(-30, 0, 0, 0, Rotate.Y_AXIS));
    //    rightPane.getTransforms().add(new Rotate(30, 0, 0, 0, Rotate.Y_AXIS));
    //

    // init UI
    initBackgroundComboBox();
    initWallComboBox();
    initLevelListView();

    // init map
    map = new Map();
    map.setNickname("Unknown Player");
    map.setFileName(levelListView.getSelectionModel().getSelectedItem().toString());
    map.setBackgroundFileName(backgroundComboBox.getSelectionModel().getSelectedItem().toString());
    map.setWallFileName(wallComboBox.getSelectionModel().getSelectedItem().toString());
  }

  @FXML
  protected void handleNicknameChange() {
    map.setNickname(nicknameTextField.getText());
  }

  @FXML
  protected void handleBackgroundChange() {
    backgroundComboBox.setButtonCell(new TextureListCellFactory());
    String fileName = backgroundComboBox.getSelectionModel().getSelectedItem().toString();
    map.setBackgroundFileName(fileName);
  }

  @FXML
  protected void handleWallChange() {
    wallComboBox.setButtonCell(new TextureListCellFactory());
    String fileName = wallComboBox.getSelectionModel().getSelectedItem().toString();
    map.setWallFileName(fileName);
  }

  @FXML
  protected void handleLevelChange() {
    String fileName = levelListView.getSelectionModel().getSelectedItem().toString();
    map.setFileName(fileName);
  }

  @FXML
  protected void handleGoClicked() throws Exception {
    SceneSwitch.INSTANCE.switchToGame(map);
  }

  @FXML
  protected void handleBackClicked() throws Exception {
    SceneSwitch.INSTANCE.switchToHome();
  }

  @FXML
  private void initBackgroundComboBox() {
    ObservableList<String> options = FXCollections.observableArrayList();
    options.addAll(FileName.IMAGE_BACKGROUNDS);
    backgroundComboBox.setItems(options);
    backgroundComboBox.setCellFactory(c -> new TextureListCellFactory());
    backgroundComboBox.getSelectionModel().selectFirst();
    backgroundComboBox.setButtonCell(new TextureListCellFactory());
  }

  @FXML
  private void initWallComboBox() {
    ObservableList<String> options = FXCollections.observableArrayList();
    options.addAll(FileName.IMAGE_OBSTACLES);
    wallComboBox.setItems(options);
    wallComboBox.setCellFactory(c -> new TextureListCellFactory());
    wallComboBox.getSelectionModel().selectFirst();
    wallComboBox.setButtonCell(new TextureListCellFactory());
  }

  @FXML
  private void initLevelListView() {
    ObservableList<String> options = FXCollections.observableArrayList();
    options.addAll(FileName.MAPS);
    levelListView.setItems(options);
    levelListView.setCellFactory(c -> new LevelListCellFactory());
    levelListView.getSelectionModel().selectFirst();
  }

  private class TextureListCellFactory extends ListCell<String> {
    private ImageView imageView = new ImageView();

    @Override
    protected void updateItem(String path, boolean empty) {
      super.updateItem(path, empty);
      setGraphic(null);
      setText(null);
      if (path != null) {
        try {
          Image image = new Image(path, true);
          imageView.setImage(image);
          imageView.setFitWidth(40);
          imageView.setFitHeight(40);
          setGraphic(imageView);
        } catch (Exception e) {
          e.printStackTrace();
        } finally {
          // get filename
          String filename = path.substring(path.lastIndexOf("/") + 1); // remove path
          String title = filename.substring(0, filename.lastIndexOf(".")); // remove type suffix
          setText(title);
        }
      }
    }
  }

  private class LevelListCellFactory extends ListCell<String> {
    private VBox vbox = new VBox();
    private Label title = new Label();
    private Label bestScore = new Label();

    LevelListCellFactory() {
      vbox.getChildren().addAll(title, bestScore);
    }

    @Override
    protected void updateItem(String path, boolean empty) {
      super.updateItem(path, empty);
      setText(null);
      setGraphic(null);
      if (path != null) {
        // get fileName
        String fileName = path.substring(path.lastIndexOf("/") + 1); // remove path
        String title = fileName.substring(0, fileName.lastIndexOf(".")); // remove type suffix
        this.title.setText(title);

        // get best score
        try {
          ScoreBoardReader scoreBoardReader = new ScoreBoardReader(title + ".txt");
          scoreBoardReader.read();
          ScoreBoard scoreBoard = scoreBoardReader.getScoreBoard();
          Score bestScore = scoreBoard.getBestScore();
          this.bestScore.setText("Best: " + bestScore.getValue());
        } catch (Exception e) {
          e.printStackTrace();
        }

        // set list cell
        setGraphic(vbox);
      }
    }
  }
}
