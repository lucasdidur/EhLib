package com.ehaqui.lib.config.model;

import com.ehaqui.lib.database.DatabaseField;
import com.ehaqui.lib.database.DatabaseTable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.UUID;


@DatabaseTable("eh_core_players")
@Getter
@Setter
@ToString
public class Players
{
    @DatabaseField(value = "player_id", id = true)
    private int id;

    @DatabaseField(value = "uuid", unique = true)
    private UUID uuid;

    @DatabaseField("nick")
    private String name;

    @DatabaseField(value = "lastseen", type = "TIMESTAMP on update CURRENT_TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date lastseen;
}
