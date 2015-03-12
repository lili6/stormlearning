package com.lili.zk.controller;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by lili on 15/3/12.
 */
@Controller
public class MainController {
    @RequestMapping("/")
    @ResponseBody
    public String logbackLevel() throws Exception {
        Logger logger = (Logger) LoggerFactory.getLogger("root");
        String levelStr = logger.getLevel().levelStr;
        return levelStr;
    }
}
