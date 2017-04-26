package com.ehaqui.lib.localize;

public class Variable
{
    private String placeholder;
    private Object value;

    public Variable(String placeholder, Object value)
    {
        this.placeholder = placeholder;
        this.value = value;
    }

    public String getPlaceholder()
    {
        return placeholder;
    }

    public Object getValue()
    {
        return value;
    }

    @Override
    public String toString()
    {
        // TODO Auto-generated method stub
        return "{" + placeholder + "} ->" + value.toString();
    }

}
