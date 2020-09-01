package com.games.snake;

public interface GameScreen {
    void setScreenSize(int var1, int var2);

    void setCellColor(int var1, int var2, Color var3);

    void setCellTextSize(int var1, int var2, int var3);

    void setCellValue(int var1, int var2, String var3);

    void setCellTextColor(int var1, int var2, Color var3);

    void setCellValueEx(int var1, int var2, Color var3, String var4);

    void setCellValueEx(int var1, int var2, Color var3, String var4, Color var5);

    void setCellValueEx(int var1, int var2, Color var3, String var4, Color var5, int var6);

    void showMessageDialog(Color var1, String var2, Color var3, int var4);

    void setScore(int var1);

    void setTurnTimer(int var1);

    void stopTurnTimer();

    int getRandomNumber(int var1);

    void initialize();

    void onKeyPress(Key var1);

    void onKeyReleased(Key var1);

    void onTurn(int var1);
}
