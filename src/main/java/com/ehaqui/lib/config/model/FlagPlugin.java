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
@DatabaseTable("eh_core_flags_plugin")
@DatabaseScript({ "ALTER TABLE `eh_core_flags_plugin` ADD UNIQUE KEY `plugin` (`plugin`,`flag_key`, `server`);" })
public class FlagPlugin
{

    @DatabaseField("plugin")
    private String plugin;

    @DatabaseField("flag_key")
    private String flag_key;

    @DatabaseField(value = "flag_value", type = "text")
    private String flag_value;

    @DatabaseField("flag_server")
    private String flag_server;
}
