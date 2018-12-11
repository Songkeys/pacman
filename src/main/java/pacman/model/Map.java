package pacman.model;

import java.util.Set;
import javafx.scene.layout.Pane;
import pacman.util.GameManager;
import pacman.util.MapPainter;
import pacman.util.MapReader;

public class Map {

  private String fileName;
  private String backgroundFileName;
  private String wallFileName;

  private MapConfig mapConfig;
  private GameManager parentGameManager;

  private Set<Obstacle> obstacles;
  private Set<Cookie> cookies;
  private Set<Ghost> ghosts;
  private Pacman pacman;
  private Spawn spawn;

  public Map() {}

  public GameManager getParentGameManager() {
    return parentGameManager;
  }

  public void setParentGameManager(GameManager parentGameManager) {
    this.parentGameManager = parentGameManager;
  }

  public Pacman getPacman() {
    return pacman;
  }

  public Set<Cookie> getCookies() {
    return cookies;
  }

  public Set<Obstacle> getObstacles() {
    return obstacles;
  }

  public Set<Ghost> getGhosts() {
    return ghosts;
  }

  public Spawn getSpawn() {
    return spawn;
  }

  public MapConfig getMapConfig() {
    return mapConfig;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getBackgroundFileName() {
    return backgroundFileName;
  }

  public void setBackgroundFileName(String backgroundFileName) {
    this.backgroundFileName = backgroundFileName;
  }

  public String getWallFileName() {
    return wallFileName;
  }

  public void setWallFileName(String wallFileName) {
    this.wallFileName = wallFileName;
  }

  private void read() {

    // read map
    MapReader mapReader = new MapReader(fileName, this);

    // get config
    mapReader.readFile(true);
    mapConfig = mapReader.getMapConfig();

    // get grids
    mapReader.readFile(false);
    obstacles = mapReader.getObstacles();
    cookies = mapReader.getCookies();
    pacman = mapReader.getPacman();
    ghosts = mapReader.getGhosts();
    spawn = mapReader.getSpawn();
  }

  public void draw(Pane root) {

    // read map
    read();

    // paint map
    MapPainter mapPainter = new MapPainter(root);
    mapPainter.drawObstacles(obstacles);
    mapPainter.drawCookies(cookies);
    mapPainter.drawPacman(pacman);
    mapPainter.drawGhost(ghosts);
  }
}
