package com.games.snake;

import java.util.ArrayList;
import java.util.List;

public class Snake {

    private static final String HEAD_SIGN = "\uD83D\uDC7E";
    private static final String BODY_SIGN = "\u26AB";

    public boolean isAlive = true;

    private final List<GameObject> snakeParts = new ArrayList<>();
    private Direction direction = Direction.LEFT;
    private static int LIMIT_X;
    private static int LIMIT_Y;

    public Snake(int WIDTH, int HEIGHT) {
        int x = WIDTH/2;
        int y = HEIGHT/2;
        snakeParts.add(new GameObject(x, y));
        snakeParts.add(new GameObject(x + 1, y));
        snakeParts.add(new GameObject(x + 2, y));

        LIMIT_X = WIDTH;
        LIMIT_Y = HEIGHT;
    }

    public void draw(Game game) {
        for (int i = 0; i < snakeParts.size(); i++) {
            if (i == 0) {
                GameObject head = snakeParts.get(i);
                if (isAlive) {
                    game.setCellValueEx(head.x, head.y, Color.NONE, HEAD_SIGN, Color.DARKSEAGREEN, 75);
                } else {
                    game.setCellValueEx(head.x, head.y, Color.NONE, HEAD_SIGN, Color.RED, 75);
                }
            } else {
                GameObject bodyPart = snakeParts.get(i);
                if (isAlive) {
                    game.setCellValueEx(bodyPart.x, bodyPart.y, Color.NONE, BODY_SIGN, Color.DARKSEAGREEN, 75);
                } else {
                    game.setCellValueEx(bodyPart.x, bodyPart.y, Color.NONE, BODY_SIGN, Color.RED, 75);
                }
            }
        }
    }

    public void setDirection(Direction direction) {
        switch (this.direction) {
            case LEFT:
            case RIGHT:
                if (snakeParts.get(0).x == snakeParts.get(1).x) return;
                break;
            case UP:
            case DOWN:
                if (snakeParts.get(0).y == snakeParts.get(1).y) return;
                break;
        }
        if (!(((direction == Direction.LEFT) && (this.direction == Direction.RIGHT)) ||
            ((direction == Direction.RIGHT) && (this.direction == Direction.LEFT)) ||
            ((direction == Direction.UP) && (this.direction == Direction.DOWN)) ||
            ((direction == Direction.DOWN) && (this.direction == Direction.UP)))) {
                    this.direction = direction;
        }
    }

    public void move(Apple apple) {
        GameObject newHead = createNewHead();
        boolean collision = checkCollision(newHead);

        if ((newHead.x < 0) || (newHead.y <0) || (newHead.x >= LIMIT_X) || (newHead.y >= LIMIT_Y) || collision) {
            isAlive = false;
            return;
        }
        snakeParts.add(0, newHead);
        if ((apple.x == newHead.x) && (apple.y == newHead.y)) {
            apple.isAlive = false;
        } else {
            removeTail();
        }
    }

    public GameObject createNewHead() {
        GameObject currentHead = snakeParts.get(0);
        GameObject newHead;

        if (direction == Direction.LEFT) {
            newHead = new GameObject(currentHead.x - 1, currentHead.y);
        } else if (direction == Direction.RIGHT) {
            newHead = new GameObject(currentHead.x + 1, currentHead.y);
        } else if (direction == Direction.UP) {
            newHead = new GameObject(currentHead.x, currentHead.y - 1);
        } else {
            newHead = new GameObject(currentHead.x, currentHead.y + 1);
        }

        return newHead;
    }

    public void removeTail() {
        snakeParts.remove(snakeParts.size() - 1);
    }

    public boolean checkCollision(GameObject newHead) {
        for (GameObject bodyPart : snakeParts) {
            if ((newHead.x == bodyPart.x) && (newHead.y == bodyPart.y)) {
                return true;
            }
        }
        return false;
    }

    public int getLength() {
        return snakeParts.size();
    }
}
