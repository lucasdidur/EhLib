package com.ehaqui.lib.menu.exceptions;

/**
 * Created by Lucas on 23/12/2016.
 */
public class MenuException extends Exception
{
    public MenuException(String message)
    {
        super(message);
    }

    public MenuException(String message, Object... args)
    {
        super(String.format(message, args));
    }
}
