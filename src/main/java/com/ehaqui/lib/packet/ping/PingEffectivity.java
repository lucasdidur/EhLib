package com.ehaqui.lib.packet.ping;

public enum PingEffectivity
{
    VERY_WEAK, WEAK, AVERAGE, STRONG, VERY_STRONG;

    /**
     * Allows you to get how strong the player's ping/latency is at ease.
     *
     * @param ping The player's ping/latency (Recommend feeding this method the getPing() method).
     *
     * @return Whether the ping is very strong, strong, average, weak, or very weak.
     */
    public static PingEffectivity getPingEffectivity(int ping)
    {
        if (ping <= 30)
        {
            return VERY_STRONG;
        } else if (ping > 30 && ping <= 130)
        {
            return STRONG;
        } else if (ping > 130 && ping <= 230)
        {
            return AVERAGE;
        } else if (ping > 230 && ping <= 330)
        {
            return WEAK;
        }

        return VERY_WEAK;
    }
}
