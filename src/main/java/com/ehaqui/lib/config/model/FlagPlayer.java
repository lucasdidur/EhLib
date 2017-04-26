package com.ehaqui.lib.config.model;

import com.ehaqui.lib.database.DatabaseField;
import com.ehaqui.lib.database.DatabaseScript;
import com.ehaqui.lib.database.DatabaseTable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@DatabaseTable("eh_core_flags_player")
@DatabaseScript({"ALTER TABLE `eh_core_flags_player`ADD CONSTRAINT `eh_core_flags_player_ibfk_1` FOREIGN KEY (`player_id`) REFERENCES `eh_core_players` (`player_id`) ON DELETE CASCADE ON UPDATE CASCADE;", "ALTER TABLE `eh_core_flags_player` ADD UNIQUE( `plugin`, `player_id`, `flag_key`);"})
public class FlagPlayer
{

    @DatabaseField("plugin")
    private String plugin;

    @DatabaseField(value = "player_id", type = "int(80)", nullField = true)
    private int playerid;

    @DatabaseField("flag_key")
    private String flag_key;

    @DatabaseField(value = "flag_value", type = "text")
    private String flag_value;

    @DatabaseField(value = "flag_server", defaultValue = "global")
    private String flag_server;
}
