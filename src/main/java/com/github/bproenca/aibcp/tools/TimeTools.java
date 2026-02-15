package com.github.bproenca.aibcp.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.ZoneId;

@Component
public class TimeTools {
    private final static Logger log = LoggerFactory.getLogger(TimeTools.class);

    @Tool(name="getCurrentLocalTime", description="Gets the current local time in the user's timezone")
    public String getCurrentLocalTime() {
        log.info("Getting current local time in user's timezone");
        return LocalTime.now().toString();
    }

    @Tool(name="getCurrentTime", description="Gets the current  time in the specified timezone")
    public String getCurrentTime(
            @ToolParam(description = "Value representing the timezone") String timezone)
    {
        log.info("Getting current local time in  timezone {}", timezone);
        return LocalTime.now(ZoneId.of(timezone)).toString();
    }
}
