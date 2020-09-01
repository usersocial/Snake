package com.games.snake;

public class SnakeGame extends Game {

    public static final int HEIGHT = 16;
    public static final int WIDTH = 16;

    private static final int GOAL = 30;

    private boolean isGameStopped;
    private int turnDelay;
    private Snake snake;
    private Apple apple;
    private int score;

    @Override
    public void initialize() {
        setScreenSize(WIDTH, HEIGHT);
        createGame();
    }

    @Override
    public void onTurn(int step) {
        snake.move(apple);
        if (!apple.isAlive) {
            setTurnTimer(turnDelay -= 5);
            setScore(score += 10);
            createNewApple();
        }
        if (!snake.isAlive) { gameOver(); }
        if (snake.getLength() > GOAL) { win(); }
        drawScene();
    }

    @Override
    public void onKeyPress(Key key) {

        switch (key) {
            case LEFT:
                snake.setDirection(Direction.LEFT);
                break;
            case RIGHT:
                snake.setDirection(Direction.RIGHT);
                break;
            case UP:
                snake.setDirection(Direction.UP);
                break;
            case DOWN:
                snake.setDirection(Direction.DOWN);
                break;
            case SPACE:
                if (isGameStopped) { createGame(); }
                break;
            case ESCAPE:
                System.exit(0);
        }
    }

    private void createGame() {
        isGameStopped = false;
        snake = new Snake(WIDTH, HEIGHT);
        turnDelay = 300;
        score = 0;
        setScore(0);
        setTurnTimer(turnDelay);
        createNewApple();
        drawScene();
    }

    private void drawScene() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                setCellValueEx(x, y, Color.IVORY, "");
            }
        }
        snake.draw(this);
        apple.draw(this);
    }

    private void createNewApple() {
        int x = getRandomNumber(WIDTH);
        int y = getRandomNumber(HEIGHT);
        Apple testApple = new Apple(x,y);
        while (true) {
            if (!snake.checkCollision(testApple)) {
                apple = testApple;
                break;
            } else {
                x = getRandomNumber(WIDTH);
                y = getRandomNumber(HEIGHT);
                testApple = new Apple(x,y);
            }
        }
    }

    private void gameOver() {
        stopTurnTimer();
        isGameStopped = true;
        showMessageDialog(Color.NONE, "Game Over", Color.RED, 75);
    }

    private void win() {
        stopTurnTimer();
        isGameStopped = true;
        showMessageDialog(Color.NONE, "You win", Color.GREEN, 75);

    }
}
