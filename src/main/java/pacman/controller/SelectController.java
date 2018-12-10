package pacman.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import pacman.constant.FileName;

public class SelectController {
  @FXML private ComboBox backgroundComboBox;
  @FXML private ComboBox wallComboBox;
  @FXML private ListView levelListView;

  @FXML
  public void initialize() {
    // init the 3d effect of panes
    // the 3d effect on boards will throw an exception of running warning in JavaFX
    // see: https://github.com/scalafx/scalafx/issues/265
    //
    //    leftPane.getTransforms().add(new Rotate(-30, 0, 0, 0, Rotate.Y_AXIS));
    //    rightPane.getTransforms().add(new Rotate(30, 0, 0, 0, Rotate.Y_AXIS));
    //

    initBackgroundComboBox();
    initWallComboBox();
    initLevelListView();
  }

  @FXML
  protected void handleBackgroundChange() {
    backgroundComboBox.setButtonCell(new TextureListCellFactory());
  }

  @FXML
  protected void handleWallChange() {
    wallComboBox.setButtonCell(new TextureListCellFactory());
  }

  @FXML
  private void initBackgroundComboBox() {
    ObservableList<String> options = FXCollections.observableArrayList();
    options.addAll(FileName.IMAGE_BACKGROUNDS);
    backgroundComboBox.setItems(options);
    backgroundComboBox.setCellFactory(c -> new TextureListCellFactory());
  }

  @FXML
  private void initWallComboBox() {
    ObservableList<String> options = FXCollections.observableArrayList();
    options.addAll(FileName.IMAGE_OBSTACLES);
    wallComboBox.setItems(options);
    wallComboBox.setCellFactory(c -> new TextureListCellFactory());
  }

  @FXML
  private void initLevelListView() {
    ObservableList<String> options = FXCollections.observableArrayList();
    options.addAll(FileName.MAPS);
    levelListView.setItems(options);
    levelListView.setCellFactory(c -> new LevelListCellFactory());
  }

  private class TextureListCellFactory extends ListCell<String> {
    private ImageView imageView = new ImageView();

    @Override
    protected void updateItem(String item, boolean empty) {
      super.updateItem(item, empty);
      setGraphic(null);
      setText(null);
      if (item != null) {
        try {
          Image image = new Image(item, true);
          imageView.setImage(image);
          imageView.setFitWidth(40);
          imageView.setFitHeight(40);
          setGraphic(imageView);
        } catch (Exception e) {
          e.printStackTrace();
        } finally {
          // get filename
          String filename = item.substring(item.lastIndexOf("/") + 1); // remove path
          filename = filename.substring(0, filename.lastIndexOf(".")); // remove type suffix
          setText(filename);
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
    protected void updateItem(String item, boolean empty) {
      super.updateItem(item, empty);
      setText(null);
      setGraphic(null);
      if (item != null) {
        // get filename
        String filename = item.substring(item.lastIndexOf("/") + 1); // remove path
        filename = filename.substring(0, filename.lastIndexOf(".")); // remove type suffix
        title.setText(filename);

        // get bestScore
        setGraphic(vbox);
      }
    }
  }
}
