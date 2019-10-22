package ${packageName}.config.mphelp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * MyBatis Plus查询注入支持
 * @author Bob
 */
@Component
public class QuerySupportInjectRunner implements CommandLineRunner {

    @Resource
    private QuerySupportMethod querySupportMethod;

    @Override
    public void run(String... args) throws Exception {
        querySupportMethod.injectQuerySupportMappedStatement();
    }
}
