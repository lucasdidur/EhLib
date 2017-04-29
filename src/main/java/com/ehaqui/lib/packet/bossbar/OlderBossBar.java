package com.ehaqui.lib.packet.bossbar;

public interface OlderBossBar
{

    void hide();

    void show();

    void stop();

    String getText();

    void setText(String text);

    double getProgress();

    void setProgress(double progress);

}
