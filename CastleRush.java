import processing.core.PApplet;
import processing.core.PImage;
import java.util.ArrayList;
import java.util.Random;

/**
 * CastleRush is a simple tower defense game where the player must defend their castle
 * against waves of enemies.
 */
public class CastleRush extends PApplet {

    PImage castleImage;
    PImage[] enemySprite;
    

    Random random = new Random();

    int castleHealth;
    int wave = 1;
    int spawnedEnemies = 0;
    int totalEnemyCostume = 1;
    int gold = 100;
    int selectedUnit = 0;

    float x = (width + 550);
    float y = 20;
    float barWidth = 100;
    float barHeight = 10;
    float buttonSize = 20;
    float buttonX = x - 285;
    float buttonY = y + barHeight;

    boolean nextWave = false;

    ArrayList<Enemy> enemies = new ArrayList<>();

    /**
     * The main method that launches the application.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        PApplet.main("CastleRush");
    }

    /**
     * Sets the size of the window.
     */
    public void settings() {
        size(800, 600);
    }

    /**
     * Initializes the game state and loads necessary resources.
     */
    public void setup() {
        enemySprite = new PImage[totalEnemyCostume];
        for (int i = 0; i < totalEnemyCostume; i++) {
            enemySprite[i] = loadImage("Sprites/Enemy" + i + ".png");
            enemySprite[i].resize(30, 30);
        }
        castleImage = loadImage("Buildings/Castle.png");
        castleHealth = 100;
        enemies = new ArrayList<>();
    }

    /**
     * The main game loop that handles drawing and updating game elements.
     */
    public void draw() {
        background(52, 140, 49);
        image(castleImage, width - castleImage.width, height / 2 - castleImage.height / 2);
        drawInfo(castleHealth, 100);
        drawSummonUI();

        if (mouseX >= buttonX && mouseX <= buttonX + buttonSize && mouseY >= buttonY && mouseY <= buttonY + buttonSize && mousePressed) {
            nextWave = true;
        }

        if (nextWave) {
            spawnEnemy();
        }

        updateEnemies();
        drawEnemies();

        if (lostGame()) {
            exit();
        }
    }

    /**
     * Draws the castle health bar, gold, and wave information on the screen.
     *
     * @param health    The current health of the castle.
     * @param maxHealth The maximum health of the castle.
     */
    public void drawInfo(float health, float maxHealth) {

        // Draws the gold counter
        fill(0);
        text("Gold: " + (int) (gold), width - 150, 55);

        // Draws healthbar
        float healthPercentage = health / maxHealth;

        fill(0);
        text("Castle Health", width - 150, 15);
        fill(150);
        stroke(0);
        rect(x, y, barWidth, barHeight);
        fill(255, 0, 0);
        rect(x, y, barWidth * healthPercentage, barHeight);

        // Draw start wave button
        fill(0);
        text("Wave: " + wave, width - 450, 15);

        if (!nextWave) {
            // Draws play button
            fill(150);
            stroke(0);
            triangle(buttonX, buttonY, buttonX, buttonY + buttonSize, buttonX + buttonSize, buttonY + buttonSize/2);
        } else {
            // Else draws pause button
            fill(150);
            stroke(0);
            rect(buttonX, buttonY, 6, 20);
            rect(buttonX + buttonSize - 10, buttonY, 6, 20);
        }
    }
    
    /**
     * Draws squares for summoning units
     */
    public void drawSummonUI() {
        float bottomSectionY = height - 65;
        float numSqaures = 4;
        float currentX = (width / 2) - (numSqaures / 2 * 60);
    
        for (int i = 1; i <= numSqaures; i++) {
            if (mouseX >= currentX && mouseX <= currentX + 50 && mouseY >= bottomSectionY && mouseY <= bottomSectionY + 50) {
                stroke(255, 255, 0);
                if (mousePressed) {
                    selectedUnit = i;
                }
            } else if (selectedUnit == i) {
                stroke (255);
            } else {
                stroke (0);
            }

            fill(150);
            rect(currentX, bottomSectionY, 50, 50);
    
            // Draw number on the top right corner
            fill(255);
            textSize(12);
            text(i, currentX + 40, bottomSectionY + 15);
            fill (0);
    
            currentX += 60;
        }
    
    }

    /**
     * Spawns enemies at regular intervals based on the frame count.
     */
    public void spawnEnemy() {
        int randomY = random.nextInt(568 - 32 + 1) + 32;

        if (frameCount % 60 == 0) {
            int x = 0;
            Enemy enemy = new Enemy(x, randomY);
            enemy.moveTowards(670, 300);

            if (nextWave) {
                enemies.add(enemy);
            }

            spawnedEnemies++;

            if (spawnedEnemies % 8 == 0) {
                wave++;
                nextWave = false;
            }
        }
    }
    

    /**
     * Updates the positions and states of all enemies.
     */
    public void updateEnemies() {
        for (int i = enemies.size() - 1; i >= 0; i--) {
            Enemy enemy = enemies.get(i);
            enemy.moveTowards(670, 300);

            if (enemy.isAtTarget(670, 300)) {
                castleHealth -= enemy.getHealth();
                enemies.remove(i);
            }

            if (mouseX >= enemy.x - 20 && mouseX <= enemy.x + 20 && mouseY >= enemy.y - 20 && mouseY <= enemy.y + 20 && mousePressed) {
                if (enemy.getHealth() <= 0) {
                    enemies.remove(i);
                } else {
                    enemy.health -= 1;
                }
                gold += enemy.getMaxHealth();
            }
        }
    }

    /**
     * Checks if the player has lost the game (castle health is zero).
     *
     * @return True if the game is lost, false otherwise.
     */
    public boolean lostGame() {
        return castleHealth <= 0;
    }

    /**
     * Draws all enemy objects on the screen.
     */
    public void drawEnemies() {
        for (Enemy enemy : enemies) {
            fill(0, 255, 0);
            image(enemySprite[0], enemy.x, enemy.y);
        }
    }

    /**
     * The Enemy class represents an enemy in the game.
     */
    public class Enemy {

        int x;
        int y;
        int health;
        int maxHealth;
        float initialSpeed;

        /**
         * Constructs an enemy at the specified position.
         *
         * @param x The x-coordinate of the enemy.
         * @param y The y-coordinate of the enemy.
         */
        public Enemy(int x, int y) {
            this.x = x;
            this.y = y;
            this.health = wave;
            this.maxHealth = wave;
            this.initialSpeed = wave;
        }

        /**
         * Moves the enemy towards a target position.
         *
         * @param targetX The x-coordinate of the target position.
         * @param targetY The y-coordinate of the target position.
         */
        public void moveTowards(int targetX, int targetY) {
            float distance = dist(x, y, targetX, targetY);
            float speed = initialSpeed;

            if (distance < 400) {
                if (x < targetX) x += speed;
                else if (x > targetX) x -= speed;

                if (y < targetY) y += speed;
                else if (y > targetY) y -= speed;
            } else {
                if (x < targetX) x += speed;
                else if (x > targetX) x -= speed;
            }
        }

        /**
         * Checks if the enemy has reached the target position.
         *
         * @param targetX The x-coordinate of the target position.
         * @param targetY The y-coordinate of the target position.
         * @return True if the enemy is at the target, false otherwise.
         */
        public boolean isAtTarget(int targetX, int targetY) {
            float threshold = 5;
            return dist(x, y, targetX, targetY) < threshold;
        }

        /**
         * Gets the current health of the enemy.
         *
         * @return The current health of the enemy.
         */
        public int getHealth() {
            return health;
        }

        /**
         * Gets the maximum health of the enemy.
         *
         * @return The maximum health of the enemy.
         */
        public int getMaxHealth() {
            return maxHealth;
        }
    }
}
