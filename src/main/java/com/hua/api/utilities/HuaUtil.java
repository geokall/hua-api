package com.hua.api.utilities;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class HuaUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(HuaUtil.class);

    public static final String DAY_MONTH_YEAR_PATTERN = "dd/MM/yyyy";
    public static final String ITP = "itp";
    public static final String HUA_EMAIL = "@hua.gr";

    public static Date convertToDateViaSqlDate(LocalDate dateToConvert) {
        return java.sql.Date.valueOf(dateToConvert);
    }

    public static Date formatStringToDate(String protocolDate) {
        SimpleDateFormat formatter = new SimpleDateFormat(DAY_MONTH_YEAR_PATTERN);
        Date yearMonthDay = null;

        try {
            yearMonthDay = formatter.parse(protocolDate);
        } catch (ParseException e) {
            LOGGER.info(e.getMessage());
        }

        return yearMonthDay;
    }

    public static String generateUsername(String surname) {
        return ITP.concat(surname).concat(RandomStringUtils.randomAlphanumeric(3).toUpperCase());
    }

    public static String generateEmail(String generatedUsername) {
        return generatedUsername.concat(HUA_EMAIL);
    }

}
