package ${packageName}.service.help;

import ${packageName}.util.HttpUtil;
import com.alibaba.fastjson.JSON;
import ${packageName}.config.YmlConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 用户数据远程获取
 */
@Service
public class RemoteAuthorizationService {

    @Autowired
    private YmlConfig ymlConfig;

    public UserDetails loadUserByUsername(final String login) {
        String url = ymlConfig.getLoadCurrentUserUrl();
        LoadUserParam loadUserParam = new LoadUserParam(login);
        String resultJson = HttpUtil.doPost(url, JSON.toJSONString(loadUserParam), null);
        if (resultJson == null) {
            // 远程加载失败
            throw new UsernameNotFoundException("用户名不存在");
        } else {
            // 远程加载成功
            UserDetails userDetails = JSON.parseObject(resultJson, UserDetails.class);
            return userDetails;
        }
    }

    class LoadUserParam {
        private String login;
        public LoadUserParam(String login) {
            this.login = login;
        }
        public String getLogin() {
            return login;
        }
        public void setLogin(String login) {
            this.login = login;
        }
    }

}
