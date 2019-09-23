package ${packageName}.util;

import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 免密登录
 * @author Bob
 */
public class NoPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        return rawPassword.toString();
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encodedPassword.equals(rawPassword);
    }
}
