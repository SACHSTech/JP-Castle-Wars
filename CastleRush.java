import processing.core.PApplet;
import processing.core.PImage;
import java.util.ArrayList;
import java.util.Random;

public class CastleRush extends PApplet {

    PImage castleImage;

    Random random = new Random();

    int castleHealth;
    int wave = 1;
    
    ArrayList<Enemy> enemies = new ArrayList<>();

    public static void main(String[] args) {
        PApplet.main("CastleRush");
    }

    public void settings() {
        size(800, 600);
    }

    public void setup() {
        castleImage = loadImage("Maps/Castle.png");
        castleHealth = 100;
        enemies = new ArrayList<>();
    }

    public void draw() {
        background(255);
        image(castleImage, width - castleImage.width, height / 2 - castleImage.height / 2);
        drawHealthGold(width - 150, 20, castleHealth, 100, 100);
        spawnEnemy();
        updateEnemies();
        drawEnemies();

        if (lostGame()) {
            exit();
        }
    }

    public void drawHealthGold(float x, float y, float health, float maxHealth, float gold) {
        float barWidth = 100;
        float barHeight = 10;

        fill(150);
        rect(x, y, barWidth, barHeight);

        float healthPercentage = health / maxHealth;
        fill(0);
        text("Castle Health", width - 150, 15);
        text("Gold: " + (int)(gold), width - 150, 55);
        fill(255, 0, 0);
        rect(x, y, barWidth * healthPercentage, barHeight);
    }

    public void spawnEnemy() {
        int randomY = random.nextInt(568 - 32 + 1) + 32;
        if (frameCount % 60 == 0) {
            int x = 0;
            Enemy enemy = new Enemy(x, randomY);
            enemy.moveTowards(670, 300);
            enemies.add(enemy);
        }
    }

    public void updateEnemies() {
        for (int i = enemies.size() - 1; i >= 0; i--) {
            Enemy enemy = enemies.get(i);
            enemy.moveTowards(670, 300);

            if (enemy.isAtTarget(670, 300)) {
                castleHealth -= enemy.getHealth();
                enemies.remove(i);
            }
        }
    }

    public boolean lostGame() {
        return castleHealth <= 0;
    }

    public void drawEnemies() {
        for (Enemy enemy : enemies) {
            fill(0, 255, 0);
            ellipse(enemy.x, enemy.y, 20, 20);
        }
    }

    public class Enemy {
        int x;
        int y;
        int health;

        Enemy(int x, int y) {
            this.x = x;
            this.y = y;
            this.health = 1 + wave;
        }

        void moveTowards(int targetX, int targetY) {
            float distance = dist(x, y, targetX, targetY);
            float speed = wave;

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

        boolean isAtTarget(int targetX, int targetY) {
            float threshold = 5;
            return dist(x, y, targetX, targetY) < threshold;
        }

        int getHealth() {
            return health;
        }
    }
}
