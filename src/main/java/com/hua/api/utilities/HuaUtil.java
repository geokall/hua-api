package com.hua.api.utilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HuaUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(HuaUtil.class);

    public static final String DAY_MONTH_YEAR_PATTERN = "dd/MM/yyyy";

    public static Date formatDate(String protocolDate) {
        SimpleDateFormat formatter = new SimpleDateFormat(DAY_MONTH_YEAR_PATTERN);
        Date yearMonthDay = null;

        try {
            yearMonthDay = formatter.parse(protocolDate);
        } catch (ParseException e) {
            LOGGER.info(e.getMessage());
        }

        return yearMonthDay;
    }
}
