package src;
import java.util.Random;
import java.util.Scanner;

public class MyFirstApp {

    public static Random random = new Random();
    public static Scanner scanner = new Scanner(System.in);

    public static char player = '@';
    public static String playerName = "RogerTheRabbit ";
    public static int playerHealth = 100;
    public static int playerPower = 15;
    public static int playerPosX;
    public static int playerPosY;
    public static final int playerMoveUp = 8;
    public static final int playerMoveDown = 2;
    public static final int playerMoveLeft = 4;
    public static final int playerMoveRight = 6;

    public static char enemy = 'Q';
    public static int enemyHealth;
    public static int enemyPower;
    public static int enemyValueMin = 2;
    public static int enemyValueMax = 5;
    public static int countEnemies;


    public static char[][] map;
    public static char[][] invisibleMap;
    public static int mapWidth;
    public static int mapHeight;
    public static int mapValueMin = 2;
    public static int mapValueMax = 5;
    public static char emptyCell = '_';
    public static char readyCell = '*';
    public static int levelGame = 0;


    public static void main(String[] args) {

        while (isAlivePlayer()) {
            ++levelGame;
            System.out.println(">>>>> START LEVEL " + levelGame + " <<<<<");
            levelCycle();
        }

        System.out.println(">>>>>> GAME OVER! " + playerName + " passed " + levelGame + " level(s)");
    }

        public static void levelCycle () {
            createMap();
            spawnPlayer();
            spawnEnemy();

            while (true) {
                showMap();
                movePlayer();

                if (!isAlivePlayer()) {
                    System.out.println(playerName + " is dead");
                    break;
                }
                if (!isExistEnemies()) {
                    System.out.println("Level " + levelGame + " has been passed!");
                    break;
                }
            }

        }
        public static void createMap () {
            mapWidth = randomValue(mapValueMin, mapValueMax);
            mapHeight = randomValue(mapValueMin, mapValueMax);
            map = new char[mapHeight][mapWidth];
            invisibleMap = new char[mapHeight][mapWidth];

            for (int y = 0; y < mapHeight; y++) {
                for (int x = 0; x < mapWidth; x++) {
                    map[y][x] = emptyCell;
                }
            }

            System.out.println("Here's a map. Size is " + mapWidth + "x" + mapHeight);
        }

        public static void spawnPlayer () {
            playerPosX = randomValue(0, mapWidth - 1);
            playerPosY = randomValue(0, mapHeight - 1);
            map[playerPosY][playerPosX] = player;
            System.out.println(playerName + " has spawn in [" + (playerPosX + 1) + ":" + (playerPosY + 1) + "]. HP=" + playerHealth + ", POW=" + playerPower);
        }

        public static void spawnEnemy () {
            enemyHealth = randomValue(enemyValueMin, enemyValueMax);
            enemyPower = randomValue(enemyValueMin, enemyValueMax);

            countEnemies = (mapWidth + mapHeight) / 2;

            int enemyPosX;
            int enemyPosY;

            enemyPosX = random.nextInt(mapWidth);
            enemyPosY = random.nextInt(mapHeight);

            for (int i = 1; i <= countEnemies; i++) {

                do {
                    enemyPosX = random.nextInt(mapWidth);
                    enemyPosY = random.nextInt(mapHeight);

                } while ((enemyPosX == playerPosX && enemyPosY == playerPosY) && !isEmptyMapCell(invisibleMap, enemyPosX, enemyPosY) || (invisibleMap[enemyPosY][enemyPosX] == enemy));

//                System.out.println(i + ">" + (enemyPosX + 1) + ":" + (enemyPosY + 1));
                invisibleMap[enemyPosY][enemyPosX] = enemy;
            }
            System.out.println("There are " + countEnemies + " enemies." + "HP=" + enemyHealth + ", POW=" + enemyPower);
        }

        public static void showMap () {
            System.out.println("-----> MAP <-----");
            for (int y = 0; y < mapHeight; y++) {
                for (int x = 0; x < mapWidth; x++) {
                    System.out.print(map[y][x] + "|");
                }
                System.out.println();
            }
            System.out.println("=================");

        }

        public static void movePlayer () {
            int currentX = playerPosX;
            int currentY = playerPosY;

            int playerMove;

            do {
                System.out.println("Enter direction: (UP-" + playerMoveUp + "|DOWN-" + playerMoveDown + "|LEFT-" + playerMoveLeft + "|RIGHT-" + playerMoveRight + ") >>>");
                playerMove = scanner.nextInt();

                switch (playerMove) {
                    case playerMoveUp:
                        playerPosY -= 1;
                        break;
                    case playerMoveDown:
                        playerPosY += 1;
                        break;
                    case playerMoveLeft:
                        playerPosX -= 1;
                        break;
                    case playerMoveRight:
                        playerPosX += 1;
                        break;
                }
            } while (!isValidMovePlayer(currentX, currentY, playerPosX, playerPosY));

            playerNextMoveAction(currentX, currentY, playerPosX, playerPosY);

        }
        public static void playerNextMoveAction ( int previousPosX, int previousPosY, int nextPosX, int nextPosY){
            if (invisibleMap[nextPosY][nextPosX] == enemy) {
//                do {
//                    enemyHealth -= playerPower;
//                    playerHealth -= enemyPower;
//                    System.out.println("You've lost " + enemyPower + " HP");
//                } while (enemyHealth > 0 || playerHealth > 0);
            }
                playerHealth -= enemyPower;
                System.out.println("ALERT!Enemy's ahead! Your HP was damaged for " + enemyPower + " points. Your HP is " + playerHealth + " now.");

            countEnemies--;
            invisibleMap[nextPosY][nextPosX] = emptyCell;
            map[previousPosY][previousPosX] = readyCell;
            map[playerPosY][playerPosX] = player;
            System.out.println("Enemies left: " + countEnemies);
        }

        public static void battle() {

        }

        public static boolean isEmptyMapCell ( char[][] mapCheck, int x, int y){
            return mapCheck[y][x] == emptyCell;
        }
        public static boolean isValidMovePlayer ( int currentPosXPlayer, int currentPosYPlayer, int nextX, int nextY){
            if (nextX >= 0 && nextX < mapWidth && nextY >= 0 && nextY < mapHeight) {
                System.out.println(playerName + " successfully moved to [" + (nextX + 1) + ":" + (nextY + 1) + "]");
                return true;
            } else {
                System.out.println(playerName + ", you cannot move here. Please, try again!");
                playerPosX = currentPosXPlayer;
                playerPosY = currentPosYPlayer;
                return false;

            }
        }

        public static int randomValue ( int min, int max){
            return min + random.nextInt(max - min + 1);
        }

        public static boolean isAlivePlayer () {
            return playerHealth > 0;
        }

        public static boolean isExistEnemies () {
            return countEnemies > 0;
        }


}



