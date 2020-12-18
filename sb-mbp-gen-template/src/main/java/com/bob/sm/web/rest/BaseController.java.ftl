package ${packageName}.web.rest;

import ${packageName}.config.Constants;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 基础Controller，主要用于URL前缀的统一
 * @author Bob
 */
@RestController
@RequestMapping(Constants.URL_DEFAULT_PREFIX)
public class BaseController {
}
