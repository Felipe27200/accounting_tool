package com.accounting.accounting_tool.common;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

@Component
public class DateFormatValidator
{
    private final String BASIC_FORMAT = "yyyy-MM-dd";

    public boolean isValidDate(String dateStr, String format)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);

        try
        {
            LocalDate date = LocalDate.parse(dateStr, formatter);
            return true;
        }
        catch (DateTimeParseException e)
        {
            return false;
        }
    }

    public boolean isValidDate(String dateStr)
    {
        return this.isValidDate(dateStr, BASIC_FORMAT);
    }

    public Date converToDate(String dateStr, String format)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);

        try
        {
            LocalDate date = LocalDate.parse(dateStr, formatter);

            return Date.from(
                date.atStartOfDay(ZoneId.systemDefault())
                    .toInstant()
            );
        }
        catch(DateTimeParseException e)
        {
            return null;
        }
    }

    public Date converToDate(String dateStr)
    {
        return this.converToDate(dateStr, BASIC_FORMAT);
    }
}
