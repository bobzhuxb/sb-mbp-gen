package ${packageName}.security;

import ${packageName}.service.help.RemoteAuthorizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Authenticate a user from the database.
 */
@Component("userDetailsService")
public class DomainUserDetailsService implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(DomainUserDetailsService.class);

    private final RemoteAuthorizationService remoteAuthorizationService;

    public DomainUserDetailsService(RemoteAuthorizationService remoteAuthorizationService) {
        this.remoteAuthorizationService = remoteAuthorizationService;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String login) {
        log.debug("Authenticating {}", login);
        return remoteAuthorizationService.loadUserByUsername(login);
    }

}
